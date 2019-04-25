package com.bd.data.loader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.simple.JSONObject;

import com.bd.data.model.Metric;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestClientFactory;
import io.searchbox.client.config.HttpClientConfig;
import io.searchbox.client.config.HttpClientConfig.Builder;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.IndicesExists;

/**
 * 
 * @author SahilRavinder
 *
 */
public class ElasticLoader {
	static final Logger logger = Logger.getLogger(ElasticLoader.class);

	private JestClient client;
	private static final int MAX_RETRY = 2;

	private final SimpleDateFormat tsFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss,SSS'Z'");
	private final SimpleDateFormat indexDateFormat = new SimpleDateFormat("yyyy.MM.dd");
	DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");

	private String datePart = "";
	private DateTime reportDate;

	public void process() {
		File[] csvFiles = getFileNames();
		for(File csvFile: csvFiles) {
			this.datePart = csvFile.getName().replaceAll(".csv", "");
			List<Metric> metrics = parseCsv(csvFile.getAbsolutePath());
			connect();
			prepareBulkStatement(metrics);
			disconnect();
		}
	}

	private Integer parseInt(String value) {
		if (value.isEmpty() || value == null)
			return 0;
		return Integer.valueOf(value);
	}

	private File[] getFileNames() {
		//String currentDirectory = System.getProperty("user.dir");
		String currentDir = "D:\\CI-Metrics\\ElasticLoader";
		File folder  = new File(currentDir + "\\CSV-Load");
		//File folder = new File("C:\\Users\\10257160\\Desktop\\CI\\CSV-Load"); //DEBUG ONLY
		File[] listOfFiles = folder.listFiles();
		return listOfFiles;
	}

	private List<Metric> parseCsv(String csvFile) {
		String line = "";
		String cvsSplitBy = Pattern.quote("|");

		List<Metric> metrics = new ArrayList<>();

		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			//int lineCount = 0;
			while ((line = br.readLine()) != null) {
				//if (lineCount != 0) {
					// use comma as separator
					String[] parts = line.split(cvsSplitBy, -1);
					metrics.add(new Metric(parts[0], parseInt(parts[1]), parseInt(parts[2]), parseInt(parts[3]),
							parseInt(parts[4]), parseInt(parts[5]), parseInt(parts[6]), parseInt(parts[7]),
							parseInt(parts[8]), parseInt(parts[9]), parseInt(parts[10]), parseInt(parts[11]), parts[12],
							parts[13], parts[14]));
				//}
				//lineCount++;
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		return metrics;
	}

	private void connect() {
		try {
			// TODO Get host name from configuration
			String addressesString = "http://localhost:9200";
			Set<String> addresses = new HashSet<String>(Arrays.asList(addressesString.split(",")));
			Builder builder = new HttpClientConfig.Builder(addressesString).multiThreaded(true);

			if (addresses.size() > 1) {
				builder = builder.discoveryEnabled(true).discoveryFrequency(1l, TimeUnit.MINUTES);
			} else {
				builder.discoveryEnabled(false);
			}

			// TODO Get user and pass from configuration
			builder = builder.defaultCredentials("elastic", "changeme");

			JestClientFactory factory = new JestClientFactory();
			factory.setHttpClientConfig(builder.build());
			this.client = factory.getObject();

			TimeZone tz = TimeZone.getTimeZone("UTC");
			tsFormat.setTimeZone(tz);
			indexDateFormat.setTimeZone(tz);

			createIndexIfNotExists();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void disconnect() {
		if (this.client != null) {
			this.client.shutdownClient();
		}
	}

	private void prepareBulkStatement(List<Metric> metrics) {
		try {
			Collection<Index> bulkableActions = new ArrayList<Index>();
			for (Metric metric : metrics) {

				// Create Map Object of Json Structure
				Map<String, Object> jsonMapper = createJsonMap(metric);				

				// Mandatory TimeStamp Field for all ES Records
				String timeStamp = this.reportDate.toString("yyyy-MM-dd'T'HH:mm:ss','SS'Z'");
				jsonMapper.put("@timestamp", timeStamp);
				
				// Mandatory TimeStamp Field for all ES Records
				//jsonMapper.put("@timestamp", DateHelper.getUtcDate("yyyy-MM-dd'T'HH:mm:ss','SS'Z'"));

				String json = new JSONObject(jsonMapper).toJSONString();

				Index idx = new Index.Builder(json).build();
				bulkableActions.add(idx);
			}

			if (bulkableActions.size() > 0) {
				this.pushToElasticSearch(bulkableActions);
			}

		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
	}

	private Map<String, Object> createJsonMap(Metric metric) {
		Map<String, Object> jsonMapper = new HashMap<>();
		try {
			jsonMapper.put("ScrumTeam", TypeHelper.getJsonFieldValue(metric.getScrumTeam(), "varchar"));
			jsonMapper.put("UnitTestBackEnd", TypeHelper.getJsonFieldValue(metric.getUnitTestBackEnd(), "int"));
			jsonMapper.put("UnitTestFrontEnd", TypeHelper.getJsonFieldValue(metric.getUnitTestFrontEnd(), "int"));
			jsonMapper.put("nDependCritical", TypeHelper.getJsonFieldValue(metric.getnDependCritical(), "int"));
			jsonMapper.put("nDependNonCritical", TypeHelper.getJsonFieldValue(metric.getnDependNonCritical(), "int"));
			jsonMapper.put("CheckMarxHigh", TypeHelper.getJsonFieldValue(metric.getCheckMarxHigh(), "int"));
			jsonMapper.put("CheckMarxMed", TypeHelper.getJsonFieldValue(metric.getCheckMarxMed(), "int"));
			jsonMapper.put("CheckMarxLow", TypeHelper.getJsonFieldValue(metric.getCheckMarxLow(), "int"));
			jsonMapper.put("FODCritical", TypeHelper.getJsonFieldValue(metric.getFodCritical(), "int"));
			jsonMapper.put("FODHigh", TypeHelper.getJsonFieldValue(metric.getFodHigh(), "int"));
			jsonMapper.put("FODMed", TypeHelper.getJsonFieldValue(metric.getFodMed(), "int"));
			jsonMapper.put("FODLow", TypeHelper.getJsonFieldValue(metric.getFodLow(), "int"));
			jsonMapper.put("ManualCodeReviews", TypeHelper.getJsonFieldValue(metric.getManualCodeReviews(), "varchar"));
			jsonMapper.put("AutomationStats", TypeHelper.getJsonFieldValue(metric.getAutomationStats(), "varchar"));
			jsonMapper.put("Comments", TypeHelper.getJsonFieldValue(metric.getComments(), "varchar"));

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return jsonMapper;
	}

	private void pushToElasticSearch(Collection<Index> bulkableActions) {
		String index = this.getIndexName();
		logger.info("BEGIN Insert ES Index, " + index + ", Rows " + bulkableActions.size());

		int retry = 0;

		Bulk bulk = new Bulk.Builder().defaultIndex(index).defaultType(this.getType()).addAction(bulkableActions)
				.build();
		while (retry <= MAX_RETRY) {
			try {
				BulkResult result = client.execute(bulk);
				if (result.isSucceeded()) {
					logger.info("END Insert ES Index, " + index + ", Rows " + bulkableActions.size());
					return;
				}

			} catch (Exception e) {
				logger.info(
						"Insert failed with message " + e.getMessage() + ", retries remaning " + (MAX_RETRY - retry));
				// Check if index exists.. and retry insert
				createIndexIfNotExists();
				retry++;
			}
		}
	}

	private void createIndexIfNotExists() {
		try {
			Boolean indexExists = client.execute(new IndicesExists.Builder(this.getIndexName()).build()).isSucceeded();
			if (indexExists) {
				return;
			}
			client.execute(new CreateIndex.Builder(this.getIndexName()).build());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	private String getIndexName() {
		this.reportDate = formatter.parseDateTime(this.datePart);
		return "ci-metrics" + "-" + indexDateFormat.format(this.reportDate.toDate());
	}

	private String getType() {
		return "ci-metrics" + "-event";
	}

	/*private Date getDate() {
		Long ts = System.currentTimeMillis();
		Date date = ts != null ? new Date(ts) : new Date();
		return date;
	}*/
}

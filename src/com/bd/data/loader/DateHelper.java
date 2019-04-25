package com.bd.data.loader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateHelper {

	/**
	 * Convert Epoch Value to java.util.Date field, used to prepare C* Insert
	 * Statement
	 * 
	 * @param seconds
	 * @return
	 * @throws ParseException
	 */
	public static Date convertEpochToTimeStamp(long seconds) throws ParseException {
		DateTime dateTime = new DateTime(seconds * 1000L);
		return dateTime.toDate();
	}

	/**
	 * Convert Epoch value to a Date string value, used by FileCreator to create
	 * dated dir
	 * 
	 * @param seconds
	 * @return
	 * @throws ParseException
	 */
	public static String convertEpochToDateString(long seconds) throws ParseException {
		String dateString = null;
		try {
			DateTime dateTime = new DateTime(seconds * 1000L);
			dateString = dateTime.toString("yyyyMMdd");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return dateString;
	}

	/**
	 * Convert Epoch value to a string Date value(UTC), used by the ES ingester
	 * 
	 * @param seconds
	 * @return
	 */
	public static String convertEpochToDateStringES(long seconds) {
		String pattern = "yyyy-MM-dd'T'HH:mm:ss','SS'Z'";
		DateTime date = new DateTime(seconds * 1000L).toDateTime(DateTimeZone.UTC);
		return date.toString(pattern);
	}

	/**
	 * Convert Epoch value to a string TimeStamp value(UTC), used by the ES
	 * ingester
	 * 
	 * @param seconds
	 * @return
	 */
	public static String convertEpochToTimeStampStringES(long seconds) {
		String pattern = "yyyy-MM-dd'T'HH:mm:ss','SS'Z'";
		DateTime date = new DateTime(seconds).toDateTime(DateTimeZone.UTC);
		return date.toString(pattern);
	}

	/**
	 * Convert date from a given format to a specified format
	 * @param dateString
	 * @param inputFormat
	 * @param outputFormat
	 * @return
	 * @throws ParseException
	 */
	public static String convertDateFormat(String dateString, String inputFormat, String outputFormat)
			throws ParseException {
		return new SimpleDateFormat(outputFormat).format(new SimpleDateFormat(inputFormat).parse(dateString));
	}
	
	/**
	 * Get current Utc Date with provided pattern
	 * 
	 * @param pattern
	 * @return
	 */
	public static String getUtcDate(String pattern) {
		return getUtcDate().toString(pattern);
	}

	/**
	 * Get Current Utc Hour
	 * 
	 * @param pattern
	 * @return
	 */
	public static int getUtcHour() {
		return getUtcDate().getHourOfDay();
	}

	private static DateTime getUtcDate() {
		DateTime now = new DateTime();
		DateTime utcDateTime = now.toDateTime(DateTimeZone.UTC);
		return utcDateTime;
	}
}

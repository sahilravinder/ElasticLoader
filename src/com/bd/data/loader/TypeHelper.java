package com.bd.data.loader;

import java.text.ParseException;

public class TypeHelper {
	public static Object getJsonFieldValue(Object value, String type) throws ParseException {
		Object parsedVal = null;
		switch (type) {
		case "text":
		case "varchar":
			parsedVal = value.toString();
			break;
		case "int":
			parsedVal = Integer.parseInt(value.toString());
		case "long":
			parsedVal = Long.parseLong(value.toString());
		case "float":
			parsedVal = Float.parseFloat(value.toString());
		case "double":
			parsedVal = Double.parseDouble(value.toString());
			break;
		case "date":
			String dateVal = null;
			try {
				Long val = 0L;
				if (!value.toString().isEmpty()) {
					val = Long.parseLong(value.toString());
				}
				dateVal = DateHelper.convertEpochToDateStringES(val);
				parsedVal = dateVal;
				break;
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}
		case "datetime":
			String dateTimeVal = null;
			try {
				Long val = 0L;
				if (!value.toString().isEmpty()) {
					val = Long.parseLong(value.toString());
				}
				dateTimeVal = DateHelper.convertEpochToTimeStampStringES(val);
				parsedVal = dateTimeVal;
				break;
			} catch (NumberFormatException e) {
				System.out.println(e.getMessage());
			}
		default:
			parsedVal = value;
			break;
		}
		return parsedVal;
	}
}

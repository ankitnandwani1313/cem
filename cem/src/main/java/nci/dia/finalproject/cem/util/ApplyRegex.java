package nci.dia.finalproject.cem.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ApplyRegex {

	public String validateAndFilterValue(String value, String finalValue, String appender) {

		if (!value.equals("\"\"")) {
			Matcher match1 = Pattern.compile("[-0-9]*\\.[0-9]*").matcher(value);
			Matcher match2 = Pattern.compile("-[0-9]+").matcher(value);
			Matcher match3 = Pattern.compile("[0-9]*\\.[0-9]*").matcher(value);
			Matcher match4 = Pattern.compile("[0-9]+").matcher(value);
			if (match1.find()) {
				finalValue = match1.group();
				finalValue = appender + finalValue;
			} else if (match2.find()) {
				finalValue = match2.group();
				finalValue = appender + finalValue;
			} else if (match3.find()) {
				finalValue = match3.group();
				finalValue = appender + finalValue;
			} else if(match4.find()) {
				finalValue = match4.group();
				finalValue = appender + finalValue;
			}

		}
		return finalValue;
	}
}

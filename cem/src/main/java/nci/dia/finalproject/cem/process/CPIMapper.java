package nci.dia.finalproject.cem.process;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import nci.dia.finalproject.cem.util.*;

public class CPIMapper extends Mapper<LongWritable, Text, Text, Text> {

	ApplyRegex regex = new ApplyRegex();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String finalCpiKey = "";
		String finalCpiValue = "";
		if (key.get() == 0) {
			return;
		} else {
			String cpiRecord = value.toString(); // Read each record from Trade dataset
			String[] cpiSplit = cpiRecord.split(","); // Parse CSV file
			finalCpiKey = cpiSplit[3];
			Matcher matchKey = Pattern.compile("[0-9]*M[0-9]*").matcher(finalCpiKey); // Apply Regex on Key
			if (matchKey.find()) {
				finalCpiKey = matchKey.group(); // match Regex 
			}
			String[] splitKey = finalCpiKey.split("M"); // split Key on month
			finalCpiKey = splitKey[0];
			String cpiValue = cpiSplit[7]; // Extract Value

			finalCpiValue = regex.validateAndFilterValue(cpiValue, finalCpiValue, "CPI,"); // Apply Regex on
																							// Values
			if (finalCpiValue != null) {
				context.write(new Text(finalCpiKey.toString()), new Text(finalCpiValue)); // Generate Key Value
																							// pair
			}

		}

	}
}
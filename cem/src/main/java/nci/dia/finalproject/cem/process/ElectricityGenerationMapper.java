package nci.dia.finalproject.cem.process;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.Mapper;

import nci.dia.finalproject.cem.util.*;

import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ElectricityGenerationMapper extends Mapper<LongWritable, Text, Text, Text> {

	ApplyRegex regex = new ApplyRegex();

	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String finalElecKey = "";
		String finalElecValue = "";
		if (key.get() == 0) {
			return;
		} else {

			String elecRecord = value.toString(); // Read each record from Recycle dataset
			String[] elecSplit = elecRecord.split("\",");// Parse CSV file
			finalElecKey = elecSplit[3];
			Matcher matchKey = Pattern.compile("[0-9]*M[0-9]*").matcher(finalElecKey); // Apply Regex on Key
			if (matchKey.find()) {
				finalElecKey = matchKey.group();
			}
			String[] finalCheck = finalElecKey.split("M");
			try {
				Date dateFormat = new SimpleDateFormat("yyyy-MM").parse(finalCheck[0] + "-" + finalCheck[1]);
				Date compareDate = new SimpleDateFormat("yyyy-MM").parse("1975-10"); // Parse Date
				if (dateFormat.after(compareDate)) { // Filter Key
					String tradeValue = elecSplit[7]; // Extract Value
					finalElecValue = regex.validateAndFilterValue(tradeValue, finalElecValue, "Elec,"); // Apply regex on Value
																											
					if (finalElecValue != null) { 

						context.write(new Text(finalCheck[0].toString()), new Text(finalElecValue)); // Create key- value pair
					} 

				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
}

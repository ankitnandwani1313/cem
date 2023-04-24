package nci.dia.finalproject.cem.process;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class CorrelationMapper extends Mapper<LongWritable, Text, Text, Text> {
	public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
		String finalValue = "";
		int firstcount = 0;
		int secondcount = 1;
		Float firstValue = 0.0f;
		Float secondValue = 0.0f;
		Float product = 0.0f;
		Float firstSquare = 0.0f;
		Float secondSquare = 0.0f;
		String record = value.toString(); // Read each record from trade dataset
		String[] parts = record.split("\t"); // Parse CSV file
		while (firstcount < 2 && secondcount < 3) {
			firstValue = Float.parseFloat(parts[firstcount + 1]);
			secondValue = Float.parseFloat(parts[secondcount + 1]);
			product = firstValue * secondValue;
			firstSquare = firstValue * firstValue;
			secondSquare = secondValue * secondValue;
			finalValue = firstValue.toString() + "," + secondValue.toString() + "," + product.toString() + ","
					+ firstSquare.toString() + "," + secondSquare.toString();
			firstcount++;
			secondcount++;
			if (firstcount == 1 && secondcount == 2) {
				String firstKey = "CPITrade";
				context.write(new Text(firstKey.toString()), new Text(finalValue));
			} else if (firstcount == 2 && secondcount == 3) {
				String secondKey = "ElecGenTrade";
				context.write(new Text(secondKey.toString()), new Text(finalValue));
			}
		}
		firstValue = Float.parseFloat(parts[1]);
		secondValue = Float.parseFloat(parts[3]);
		product = firstValue * secondValue;
		firstSquare = firstValue * firstValue;
		secondSquare = secondValue * secondValue;
		finalValue = firstValue.toString() + "," + secondValue.toString() + "," + product.toString() + ","
				+ firstSquare.toString() + "," + secondSquare.toString();
		String thirdKey = "CPIElecGen";
		context.write(new Text(thirdKey.toString()), new Text(finalValue));

	}
}

package nci.dia.finalproject.cem.process;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import java.io.IOException;
import java.lang.Math;

public class CorrelationReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {

		int count = 0;
		Double productTotal = 0.0;
		Double firstValue = 0.0;
		Double secondValue = 0.0;
		Double firstSquare = 0.0;
		Double secondSquare = 0.0;
		Double numerator = 0.0;
		Double secondSummationSquare = 0.0;
		Double firstSummationSquare = 0.0;
		Double denominator = 0.0;
		Double denominatorpart1 = 0.0;
		Double denominatorpart2 = 0.0;
		Double partialDenominator = 0.0;
		Double correlationValue = 0.0;
		for (Text value : values) {
			String[] record = value.toString().split(",");
			firstValue += Double.parseDouble(record[0]);
			secondValue += Double.parseDouble(record[1]);
			productTotal += Double.parseDouble(record[2]);
			firstSquare += Double.parseDouble(record[3]);
			secondSquare += Double.parseDouble(record[4]);
			count++;
		}

		numerator = (count * productTotal) - (firstValue * secondValue);
		firstSummationSquare = firstValue * firstValue;
		secondSummationSquare = secondValue * secondValue;
		denominatorpart1 = (count * firstSquare) - firstSummationSquare;
		denominatorpart2 = (count * secondSquare) - secondSummationSquare;
		partialDenominator = denominatorpart1 * denominatorpart2;
		denominator = Math.sqrt(partialDenominator);
		correlationValue = numerator / denominator;

		context.write(key, new Text(correlationValue.toString()));
	}
}
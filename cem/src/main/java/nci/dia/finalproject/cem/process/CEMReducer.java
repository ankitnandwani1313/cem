package nci.dia.finalproject.cem.process;

import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.io.Text;
import java.io.IOException;

public class CEMReducer extends Reducer<Text, Text, Text, Text> {
	public void reduce(Text key, Iterable<Text> values, Context context) throws IOException, InterruptedException {
		Float tradeTotal = 0.0f;
		Float cpiTotal = 0.0f;
		Float cpiFinalTotal = 0.0f;
		Float importValuein100Million = 0.0f;
		Float averageElecGen = 0.0f;
		int count = 0;
		for (Text value : values) {

			if (value.toString().contains("CPI")) {
				String[] cpiParts = value.toString().split(",");
				cpiTotal += Float.parseFloat(cpiParts[1]);
				cpiFinalTotal = cpiTotal / 10;
			} else if (value.toString().contains("Trade")) {
				String[] tradeParts = value.toString().split(",");
				tradeTotal += Float.parseFloat(tradeParts[1]);
				importValuein100Million = tradeTotal / 100000;
			} else if (value.toString().contains("Elec")) {
				String[] elecGenParts = value.toString().split(",");
				averageElecGen += Float.parseFloat(elecGenParts[1]);
				averageElecGen = averageElecGen / 2;
			}
		}
		String finalString = String.format("%.2f\t%.2f\t%.2f", cpiFinalTotal, importValuein100Million, averageElecGen);
		context.write(key, new Text(finalString));
	}
}
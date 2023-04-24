package nci.dia.finalproject.cem;

import nci.dia.finalproject.cem.config.*;
import nci.dia.finalproject.cem.process.CEMReducer;
import nci.dia.finalproject.cem.process.CPIMapper;
import nci.dia.finalproject.cem.process.ElectricityGenerationMapper;
import nci.dia.finalproject.cem.process.MerchandiseTradeMapper;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;

public class Main {
	public static void main(String[] args) throws Exception {

		if (args.length > 0) {

			Config config = new Config();
			Configuration conf = new Configuration();
			String path = config.readconfigParameters(args[0]);
			if (path != null) {
				String[] paths = path.split(",");
				Job job = new Job(conf, "Reduce-side-Join");
				job.setJarByClass(Main.class);
				job.setReducerClass(CEMReducer.class);
				job.setOutputKeyClass(Text.class);
				job.setOutputValueClass(Text.class);
				MultipleInputs.addInputPath(job, new Path(paths[0]), TextInputFormat.class,
						MerchandiseTradeMapper.class);
				MultipleInputs.addInputPath(job, new Path(paths[1]), TextInputFormat.class, CPIMapper.class);
				MultipleInputs.addInputPath(job, new Path(paths[2]), TextInputFormat.class,
						ElectricityGenerationMapper.class);
				Path outputPath = new Path(paths[3]);
				FileOutputFormat.setOutputPath(job, outputPath);
				outputPath.getFileSystem(conf).delete(outputPath);
				System.exit(job.waitForCompletion(true) ? 0 : 1);

			}
		}
	}
}

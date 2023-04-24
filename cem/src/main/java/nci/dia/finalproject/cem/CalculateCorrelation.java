package nci.dia.finalproject.cem;

import nci.dia.finalproject.cem.config.Config;
import nci.dia.finalproject.cem.process.CorrelationMapper;
import nci.dia.finalproject.cem.process.CorrelationReducer;

import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;

public class CalculateCorrelation {
	public static void main(String[] args) throws Exception {

		if (args.length > 0) {

			Config config = new Config();
			String path = config.readconfigParameters(args[0]);
			if (path != null) {
				Configuration conf = new Configuration();
				String[] paths = path.split(",");
				FileSystem fs = FileSystem.get(conf);
				Path outputDir = new Path("paths[3]");
				// if (fs.exists(outputDir)) {
				Job job = new Job(conf, "Reduce-side-Join");
				job.setJarByClass(CalculateCorrelation.class);
				job.setMapperClass(CorrelationMapper.class);
				job.setReducerClass(CorrelationReducer.class);
				job.setOutputKeyClass(Text.class);
				job.setOutputValueClass(Text.class);
				FileInputFormat.addInputPath(job, new Path(paths[3]));
				Path outputPath = new Path(paths[4]);
				FileOutputFormat.setOutputPath(job, outputPath);
				outputPath.getFileSystem(conf).delete(outputPath);
				System.exit(job.waitForCompletion(true) ? 0 : 1);
				//co}
			}
		}
	}
}

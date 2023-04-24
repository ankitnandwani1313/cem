package nci.dia.finalproject.cem;

import nci.dia.finalproject.cem.config.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.text.ParseException;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;

public class GeneratePlot extends JFrame {

	Config config = new Config();
	String path = config.readconfigParameters("src/main/resources/configuration.json");
	String fileLocation = path.split(",")[5];

	public GeneratePlot() {

		initUI();
	}

	public void initUI() {

		XYDataset dataset = createDataset();
		JFreeChart chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);

		add(chartPanel);

		pack();
		setTitle("Line chart");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public XYDataset createDataset() {
		XYSeries series1 = new XYSeries("ConsumerPriceIndex");
		XYSeries series2 = new XYSeries("ImportAmountIn100Million");
		XYSeries series3 = new XYSeries("ElectricityOutput");
		try {
			String line = "";
			File file = new File(fileLocation);
			FileReader fileReader = new FileReader(file);
			BufferedReader data = new BufferedReader(fileReader);
			line = data.readLine();
			while (line != null) {
				String[] record = line.split("\t");
				Date dateFormat = new SimpleDateFormat("yyyy").parse(record[0]);
				Date compareDate = new SimpleDateFormat("yyyy").parse("1987");
				if (dateFormat.after(compareDate)) {
					series1.add(Double.parseDouble(record[0]), Double.parseDouble(record[1]));
					series2.add(Double.parseDouble(record[0]), Double.parseDouble(record[2]));
					series3.add(Double.parseDouble(record[0]), Double.parseDouble(record[3]));
				}
				line = data.readLine();
			}
			data.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException p) {
			p.printStackTrace();
		}
		XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);
		return dataset;
	}

	public JFreeChart createChart(final XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Correlate electricity Consumption,import amount and CPI in years", "Year", "Value", dataset,
				PlotOrientation.VERTICAL, true, true, false);

		XYPlot plot = chart.getXYPlot();

		XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.YELLOW);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.GREEN);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle("Correlate electricity Consumption,import amount and CPI in years",
				new Font("Serif", Font.BOLD, 18)));

		return chart;
	}

	public static void main(String[] args) {
		GeneratePlot ex = new GeneratePlot();
		ex.setVisible(true);
	}
}
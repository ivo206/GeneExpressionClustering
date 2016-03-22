package GUI;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

import Algorithm.Cluster;
import Algorithm.ClusteringAlgorithm;
import Algorithm.Gene;
import Algorithm.HierarchicalClustering;
import Algorithm.KMeans;

public class ClusteringPlot extends ApplicationFrame {

	private ArrayList<JPanel> panels;
	private ArrayList<JPanel> heatMapPanels;
	private String name;
	private String metric;
	public ClusteringPlot(String title, String distance, String linkingDistance) {

		super(title);
		panels = new ArrayList<JPanel>();
		heatMapPanels = new ArrayList<JPanel>();
		ClusteringAlgorithm clusteringAlgoritm;
		if(title=="hierarchical")
		{
			clusteringAlgoritm = new HierarchicalClustering("C:\\Users\\Ivo\\Desktop\\Projects\\BioinformaticaEsame\\data\\230genes_log_expression.txt", linkingDistance);
			clusteringAlgoritm.cluster();
			name = "Hierarchical Clustering";
			metric = linkingDistance;
		}
		else
		{
			clusteringAlgoritm = new KMeans("C:\\Users\\Ivo\\Desktop\\Projects\\BioinformaticaEsame\\data\\230genes_log_expression.txt",6,distance);
			clusteringAlgoritm.cluster();
			name = "K-Means Clustering";
			metric = distance;
		}
		ArrayList<Cluster> clusters = clusteringAlgoritm.getClusters();
		Cluster cluster = new Cluster();
 		for(int i=0;i<clusters.size();i++)
		{
			cluster = clusters.get(i);
			BufferedImage image = cluster.getHeatMapBufferedImage();
			HeatMapPanel heatMapPanel = new HeatMapPanel(image);
			heatMapPanels.add(heatMapPanel);
			XYDataset dataset = createDataset(cluster);
			JFreeChart chart = createChart(dataset, name, metric);
			ChartPanel chartPanel = new ChartPanel(chart);
			panels.add(chartPanel);
		}
		

	}
	/**
	 * Creates a sample dataset.
	 *
	 * @return a sample dataset.
	 */
	private static XYDataset createDataset(Cluster cluster) {
	
		HashSet<Gene> geneSet= cluster.getGeneSet();
		ArrayList<XYSeries> seriesArray = new ArrayList<XYSeries>();
		for(Iterator<Gene> it = geneSet.iterator(); it.hasNext();)
		{
			Gene gene = it.next();
			geneSet.add(gene);
			XYSeries series = new XYSeries(gene.getName());
			double[] expValues = gene.getValues();
			int j=-6;
			for(int i=0;i<expValues.length;i++)
			{
				series.add(j, expValues[i]);
				j=j+2;
			}
			seriesArray.add(series);
		}
		
		
		XYSeriesCollection dataset = new XYSeriesCollection();
		for(int i=0;i<seriesArray.size();i++)
		{
			dataset.addSeries(seriesArray.get(i));
		}
		return dataset;
	}
	

	
	private static JFreeChart createChart(XYDataset dataset, String name, String metric) {
		// create the chart...
		JFreeChart chart = ChartFactory.createXYLineChart(
		name, // chart title
		"X", // x axis label
		"Y", // y axis label
		dataset, // data
		PlotOrientation.VERTICAL,
		true, // include legend
		true, // tooltips
		false // urls
		);
		chart.addSubtitle(new TextTitle("Using the "+metric+" distance"));
		// NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
		chart.setBackgroundPaint(Color.white);
		// get a reference to the plot for further customisation...
		XYPlot plot = (XYPlot) chart.getPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);
		XYLineAndShapeRenderer renderer
		= (XYLineAndShapeRenderer) plot.getRenderer();
		renderer.setShapesVisible(true);
		renderer.setShapesFilled(true);
		// change the auto tick unit selection to integer units only...
		NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
		// OPTIONAL CUSTOMISATION COMPLETED.
		return chart;
	}
	
	public ArrayList<JPanel> getPanels()
	{
		return panels;
	}
	
	public ArrayList<JPanel> getHeatMapPanels()
	{
		return heatMapPanels;
	}
}

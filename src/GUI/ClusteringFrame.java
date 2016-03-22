package GUI;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

//import MenuFrame.ItemHandler;

import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * An application for clustering and visualizing microarray genomic data
 * @author Ivo Rakar
 * Date: July 2, 2015
 * */

public class ClusteringFrame extends JFrame {
	
	private String algorithm;
	private String metrics;
	private String linking;
	//Menu buttons for various algorithms used
	private JRadioButtonMenuItem hierarchical;
	private JRadioButtonMenuItem kMeans;
	private JRadioButtonMenuItem spearmenRadio;
	private JRadioButtonMenuItem euclidianRadio;
	private JRadioButtonMenuItem singleLinkRadio;
	private JRadioButtonMenuItem averageLinkRadio;
	private JTabbedPane tabbedPane;
	
	public ClusteringFrame()
	{
		super("Clustering");
		
		algorithm="hierarchical";
		metrics="euclidian";
		linking="average-linking";
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('f');
		
		JMenuItem openItem = new JMenuItem("Open");
		fileMenu.add(openItem);
		
		JMenuItem closeItem =  new JMenuItem("Exit");
		fileMenu.add(closeItem);
		
		closeItem.addActionListener(
			new ActionListener()
			{
				public void actionPerformed( ActionEvent event )
	            {
	               System.exit( 0 );
	            } 
			}
		);
		
		JMenuBar bar = new JMenuBar();
		setJMenuBar(bar);
		bar.add(Box.createRigidArea(new Dimension(5,35)));
		bar.add(fileMenu);
		
		JMenu clusteringAlgorithm = new JMenu("Metrics");
		clusteringAlgorithm.setMnemonic('m');
		ItemHandler itemHendler = new ItemHandler();
		
		JMenu hierarchicalMenu = new JMenu("Lincking");
		ButtonGroup distanceButtonGroup = new ButtonGroup();
		
		
		averageLinkRadio = new JRadioButtonMenuItem("Average-lingking");
		hierarchicalMenu.add(averageLinkRadio);
		distanceButtonGroup.add(averageLinkRadio);
		averageLinkRadio.addActionListener(itemHendler);
		
		
		singleLinkRadio = new JRadioButtonMenuItem("Single-linking");
		hierarchicalMenu.add(singleLinkRadio);
		distanceButtonGroup.add(singleLinkRadio);
		singleLinkRadio.addActionListener(itemHendler);
		
		averageLinkRadio.setSelected(true);
		
		clusteringAlgorithm.add(hierarchicalMenu);
		
		JMenu kMeansDistanceMenu = new JMenu("Distances");
		ButtonGroup kMeansButtonGroup = new ButtonGroup();
		
		euclidianRadio = new JRadioButtonMenuItem("Euclidian");
		kMeansDistanceMenu.add(euclidianRadio);
		kMeansButtonGroup.add(euclidianRadio);
		euclidianRadio.addActionListener(itemHendler);
		
		euclidianRadio.setSelected(true);
		
		spearmenRadio = new JRadioButtonMenuItem("Spearmen Correlatin");
		kMeansDistanceMenu.add(spearmenRadio);
		kMeansButtonGroup.add(spearmenRadio);
		spearmenRadio.addActionListener(itemHendler);
		
		JMenu algorithm = new JMenu("Algorithm");
		algorithm.setMnemonic('a');
		ButtonGroup clusteringButtonGroup = new ButtonGroup();
		hierarchical = new JRadioButtonMenuItem("Hierarchical");
		kMeans = new JRadioButtonMenuItem("K-Means");
		algorithm.add(hierarchical);
		algorithm.add(kMeans);
		hierarchical.addActionListener(itemHendler);
		kMeans.addActionListener(itemHendler);
		clusteringButtonGroup.add(hierarchical);
		clusteringButtonGroup.add(kMeans);
		
		hierarchical.setSelected(true);
		
		bar.add(algorithm);
		
		clusteringAlgorithm.add(kMeansDistanceMenu);
		bar.add(clusteringAlgorithm);
		
		renderClusteringPlots();
		
	}
	
	private void renderClusteringPlots()
	{
		tabbedPane = new JTabbedPane(); // create JTabbedPane 
		
		ClusteringPlot cluster = new ClusteringPlot(algorithm, metrics, linking);
		 
		ArrayList<JPanel> panels = cluster.getPanels();
		for(int i=0;i<6;i++)
		{
			int j=i+1;
			tabbedPane.addTab( "Cluster "+j, null, panels.get(i), null );
		}
		 
		ArrayList<JPanel> heatMapPanels = cluster.getHeatMapPanels();
	    for(int i=0;i<6;i++)
	    {
	    	int j=i+1;
	    	tabbedPane.addTab( "HeatMap "+j, null, heatMapPanels.get(i), null );
	    }
		add( tabbedPane ); // add JTabbedPane to frame
		
	}
	 private class ItemHandler implements ActionListener 
	   {
	      // process color and font selections
	      public void actionPerformed( ActionEvent event )
	      {
	         
	    	 if(hierarchical.isSelected())
	    	 {
	    		 algorithm="hierarchical";
	    	 }
	    	 else
	    	 {
	    		 algorithm="kMeans";
	    	 }
	    	 
	    	 if(euclidianRadio.isSelected())
	    	 {
	    		 metrics="euclidian";
	    	 }
	    	 else
	    	 {
	    		 metrics="spearman";
	    	 }
	    	 
	    	 if(singleLinkRadio.isSelected())
	    	 {
	    		 linking="single-linking";
	    	 }
	    	 else
	    	 {
	    		 linking="average-linking";
	    	 }
	    	 remove(tabbedPane);
	    	 repaint();
	    	 renderClusteringPlots();
	    	 revalidate();
	      } 
	   } 

}

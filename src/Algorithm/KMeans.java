package Algorithm;
/**Class for computing and representing 
 * k-means clustering of expression data.
 * */

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class KMeans extends ClusteringAlgorithm
{
  // Data members
  private Gene[] genes; // Array of all genes in dataset
  private ArrayList<Cluster> clusters; //Array of all clusters; null until
  private boolean finished;                            // performClustering is called.
  private int numClusters;
  private String metric;
  /** The genes array is filled with the genes from the dataset. 
   *  The clusters array is not filled; to fill it, call performClustering. 
   * */
  public KMeans(String fileName, int numOfClusters, String distance) 
  {
	metric = distance;
    numClusters=numOfClusters;
    BufferedReader reader;
    int numGenes;
    String[] splitLine;
    double[] exprValues;
    finished=false;
    // Creates a new KMeans object by reading in all of the genes
    // and expression levels located in filename
    try 
    {
      reader = new BufferedReader(new FileReader(fileName));
      
      // Count the number of lines to determine how many genes are present
      reader.readLine();
      for(numGenes = 0; reader.readLine( ) != null; numGenes++);
      
      // Close and then re-open the file now that we know its length
      reader.close();
      reader = new BufferedReader(new FileReader(fileName));
      
      // Allocate space for the genes array
      genes = new Gene[numGenes];
      exprValues = null;
      
      // Now, read in each line and create the corresponding Gene object
      reader.readLine();
      for(int i = 0; i < genes.length; i++) 
      {
        String line = reader.readLine();
        // The files are tab-delimited, so split on tabs (\t)
        splitLine = line.split( "\t" );
        if((exprValues == null) || (exprValues.length != (splitLine.length - 1)))
		{
			exprValues = new double[splitLine.length - 3];
		}
        	

		for(int j = 0; j < exprValues.length; j++)
		{
			exprValues[j] = Double.parseDouble(splitLine[j + 3]);
		}
	    
	    genes[i] = new Gene(splitLine[1], exprValues);
       
      }
      
      // Lastly, close the file
      reader.close(); 
      clusters = new ArrayList<Cluster>();
      for(int i=0;i<numClusters;i++)
      {
    	 Cluster cluster = new Cluster();
    	 cluster.addGene(genes[i]);
     	 clusters.add(cluster);
     	 
      }
    }
    catch(FileNotFoundException e) 
    {
      System.out.println( "ERROR:  Unable to locate " + fileName + "." );
      System.exit( 0 ); 
    }
    catch(IOException e) 
    {
      System.out.println( "ERROR:  Unable to read from " + fileName + "." );
      System.exit( 0 );
    } 
  }

  /** gets the array of all genes in the dataset. */
  public Gene[] getGenes() 
  {
    return genes; 
  }
  
  
  public int getNumClasters()
  {
	  return numClusters;
  }
  
  public boolean isFinished()
  {
	  return finished;
  }
  /**Gets the array of all clusters after performing k-means clustering. Note 
   * that this method will return null if performClustering has not yet been 
   * called. 
   * */
  public ArrayList<Cluster> getClusters() 
  {
    return clusters; 
  }
  
  @Override
  public void cluster()
  {
	  boolean finished=false;
	  while(!finished)
	  {
		  performClustering();
		  finished=isFinished();
		  
	  }
  }
  /** Perform k-means clustering with the specified number of clusters and 
   * distance metric. The "metric" parameter can take two values: "euclid" for 
   * Euclidean distance, or "spearman" for Spearman distance. 
   * */
  public void performClustering() 
  {
    if (!metric.equals("euclidian") && !metric.equals("spearman"))
    {
    	throw new IllegalArgumentException("Parameter <metric> is " +
    	        metric + ", should be \"euclid\" or \"spearman\".");
    }
    boolean changes = false;
    Gene[] centroids = new Gene[numClusters];
    double[] distance = new double[numClusters];
    for(int i=0;i<numClusters;i++)
    {
    	centroids[i]=clusters.get(i).centroid();
    }
    
    for(int i=0;i<genes.length;i++)
    {
    	for(int j=0;j<centroids.length;j++)
    	{
    		if(metric=="euclidian")
    		{
    			distance[j]=genes[i].euclideanDistance(centroids[j]);
    		}
    		else
    		{
    			distance[j]=genes[i].spearmanDistance(centroids[j]);
    		}
    		
    	}
    	double min=100000;
    	int cluster=0;
    	for(int j=0;j<distance.length;j++)
    	{
    		if(distance[j]<=min)
    		{
    			min=distance[j];
    			cluster=j;
    		}
    	}
    	for(int j=0;j<numClusters;j++)
    	{
    		if(j==cluster)
    		{
    			clusters.get(cluster).addGene(genes[i]);
    		}
    		else
    		{
    			boolean isPresent=clusters.get(j).removeGene(genes[i]);
    			if(isPresent)
    			{
    				changes=true;
    			}
    		}
    	}
    }
    if(changes==false)
    {
    	finished=true;
    }
  }

}

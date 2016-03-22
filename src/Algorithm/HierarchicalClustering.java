package Algorithm;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class HierarchicalClustering extends ClusteringAlgorithm{

	private Gene[] genes;
	private int numClusters;
	private ArrayList<Cluster> clusters;
	private ArrayList<ArrayList<Double>> distanceMatrix;
	private String linkingDistance;
	
	public  HierarchicalClustering(String fileName, String linking)
	{
		try
		{
			clusters = new ArrayList<Cluster>();
			distanceMatrix = new ArrayList<ArrayList<Double>>();
			numClusters=6;
			linkingDistance = linking;
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			int numGenes=0;
			String[] splitLine;
			double[] exprValues = null;
			reader.readLine();
			while(reader.readLine()!=null)
			{
				numGenes++;
			}
			reader.close();
			reader = new BufferedReader(new FileReader(fileName));
			
			genes=new Gene[numGenes];
			reader.readLine();
			for(int i=0;i<genes.length;i++)
			{
				String row = reader.readLine();
				splitLine=row.split("\t");
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
			reader.close();
			Cluster cluster;
			for(int i=0;i<genes.length;i++)
			{
				cluster = new Cluster(Integer.toString(i+1));
				cluster.addGene(genes[i]);
				clusters.add(cluster);
			}

			computeDistanceMatrix();
			
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
	
	public void cluster()
	{
		while(clusters.size()>numClusters)
		{
			int [] parameters = matrixMinParameters();
			modifyMatrix(parameters[0], parameters[1]);
		}
	}
	
	public ArrayList<Cluster> getClusters()
	{
		return clusters;
	}
	
	//Method used to modify the distance matrix
	private void modifyMatrix(int x, int y)
	{
		int smallerIndex=0;
		int greaterIndex=0;
		if(x>y)
		{
			smallerIndex=y;
			greaterIndex=x;
		}
		else
		{
			smallerIndex=x;
			greaterIndex=y;
		}
		ArrayList<Double> mergedRow = new ArrayList<Double>();
		ArrayList<Double> colomn = new ArrayList<Double>();
		ArrayList<Double> row = new ArrayList<Double>();
		for(int i=0;i<distanceMatrix.size();i++)
		{
			colomn.add(distanceMatrix.get(smallerIndex).get(i));
			row.add(distanceMatrix.get(i).get(greaterIndex));
		}
		
		Cluster newCluster = new Cluster(clusters.get(smallerIndex),clusters.get(greaterIndex));
		int numGenesClusterSmallerIndex = clusters.get(smallerIndex).getNumerOfGenes();
		int numGenesClusterGreaterIndex = clusters.get(greaterIndex).getNumerOfGenes();
		clusters.remove(greaterIndex);
		clusters.remove(smallerIndex);
		clusters.add(smallerIndex, newCluster);
		
		
		if(linkingDistance=="average-linking")
		{
			//merging the two rows with the average algorithm
			double value=0;
			for(int i=0;i<distanceMatrix.size();i++)
			{
				if(i!=smallerIndex)
				{
					double sum1=numGenesClusterSmallerIndex+numGenesClusterGreaterIndex;
					double sum2=numGenesClusterSmallerIndex+numGenesClusterGreaterIndex;
					double alpha1=numGenesClusterSmallerIndex/sum1;
					double alpha2=numGenesClusterGreaterIndex/sum2;
				
					value = Math.round(((alpha1*colomn.get(i))+ (alpha2*row.get(i))) * 100.0) / 100.0;	
				}
				else
				{
					value=0.0;
				}
				mergedRow.add(value);
			}
		}
		else
		{
			// min distance algorithm
			double value=0;
			for(int i=0;i<distanceMatrix.size();i++)
			{
				if(i!=smallerIndex)
				{
					double alpha1=0.5;
					double dist=colomn.get(i)+row.get(i)-Math.abs(colomn.get(i)-row.get(i));
				
					value = Math.round((alpha1*dist) * 100.0) / 100.0;	
				}
				else
				{
					value=0.0;
				}
				mergedRow.add(value);
			}
		}
		//remove the smallerIndex row
		distanceMatrix.remove(smallerIndex);
		// add the merged row in the smallerIndex position
		distanceMatrix.add(smallerIndex,mergedRow);

		//remove the  smallerIndex colomn
		for(int i=0;i<distanceMatrix.size();i++)
		{
			if(i!=smallerIndex)
			{
				distanceMatrix.get(i).remove(smallerIndex);
			}
				
		}
		
		//add the merged row in the colomn smallerIndex
		for(int i=0;i<distanceMatrix.size();i++)
		{	
			if(i!=smallerIndex)
			{
				distanceMatrix.get(i).add(smallerIndex,mergedRow.get(i));
			}
						
		}
		
		//remove the greaterIndex row
		distanceMatrix.remove(greaterIndex);
		
		//remove  the greaterIndex column
		for(int i=0;i<distanceMatrix.size();i++)
		{	
			distanceMatrix.get(i).remove(greaterIndex);
		}
		
	}
	// Method that computes the distance matrix
	private void computeDistanceMatrix()
	{
		for(int i=0;i<genes.length;i++)
		{	
			ArrayList<Double> distances = new ArrayList<Double>();
			for(int j=0;j<genes.length;j++)
			{
				double distance = genes[i].euclideanDistance(genes[j]);
				distances.add(distance);
			}
			distanceMatrix.add(distances);
		}
	}
	
	// Method that returns the minimal matrix parameters
	private int[] matrixMinParameters()
	{
		int[] parameters = {0,0};
		double min=1000000;
		for(int i=0;i<distanceMatrix.get(0).size();i++)
		{
			for(int j=i+1;j<distanceMatrix.get(0).size();j++)
			{
				if(distanceMatrix.get(i).get(j)<min)
				{
					min=distanceMatrix.get(i).get(j);
					
					parameters[1]=i;
					parameters[0]=j;
				}
			}
		}
		
		return parameters;
	}
}

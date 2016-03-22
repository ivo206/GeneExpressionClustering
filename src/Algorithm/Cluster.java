package Algorithm;
// This class represents a cluster of genes with similar expression data. It
// contains methods for creating an image of the cluster's expression data and
// for returning the centroid of the cluster.

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.util.*;


public class Cluster 
{
  // Constants for creating Jpeg images. Do not change these.
  private static final double c_dCutoff = 2;
  private static final int c_iX  = 12;
  private static final int c_iY  = 5;
  
  // Data members
  private HashSet<Gene> geneSet; // List of genes in cluster
  private String clusterName;
  // Constructor; creates an empty cluster.
  public Cluster() 
  {
	geneSet = new HashSet<Gene>();
	clusterName="";
  }
  
  public  Cluster(String name)
  {
	  geneSet = new HashSet<Gene>();
	  clusterName=name;
  }
  
  public Cluster(Cluster cluster1, Cluster cluster2)
  {
	  String cluster1Name = cluster1.getClusterName();
	  String cluster2Name = cluster2.getClusterName();
	  
	  clusterName = cluster1Name+" "+cluster2Name;
	  
	  geneSet= new HashSet<Gene>();
	  HashSet<Gene> geneSetCluster1 = cluster1.getGeneSet();
	  HashSet<Gene> geneSetCluster2 = cluster2.getGeneSet();
	  for(Iterator<Gene> it = geneSetCluster1.iterator(); it.hasNext();)
	  {
			Gene gene = it.next();
			geneSet.add(gene);
	  }
	  for(Iterator<Gene> it = geneSetCluster2.iterator(); it.hasNext();)
	  {
			Gene gene = it.next();
			geneSet.add(gene);
	  }
	  
  }
  
  /**Returns the name of the cluster*/
  public String getClusterName()
  {
	  return clusterName;
  }
  
  
  /**Adds a gene to the cluster*/
  public void addGene(Gene gene) 
  {
    // Append a gene to the set
    geneSet.add(gene); 
  }
  
  /**Method that returns true if the gene is present in the Cluster*/
  public boolean isGenePresent(Gene gene)
  {
	  if(geneSet.contains(gene))
	  {
		  return true;
	  }
	  else return false;
  }
  
  /**Method that removes the gene from the geneSet*/
  public boolean removeGene(Gene gene)
  {
	  if(geneSet.remove(gene))
	  {
		  return true;
	  }
	  else return false;
  }
  /** Getter method for geneSet */
  public HashSet<Gene> getGeneSet()
  {
    return geneSet;
  }

  /** Returns the centroid of the cluster as a Gene object with name "centroid".
   * The expression data of the returned gene is the centroid of the cluster.
   */
  public Gene centroid( ) 
  {
    
	int numOfGenes=0;
	Iterator<Gene> iterator = geneSet.iterator();
	Gene gene1=iterator.next();
	int numOfValues=gene1.getValues().length;
	double[] values = new double[numOfValues];
	for(Iterator<Gene> it = geneSet.iterator(); it.hasNext();)
	{
		numOfGenes++;
		Gene gene = it.next();
		double[] exprValues = gene.getValues();
		
		for(int i=0;i<exprValues.length;i++)
		{
			values[i]=values[i]+exprValues[i];
		}	
	}
	for(int i=0;i<values.length;i++)
	{
		values[i]=values[i]/numOfGenes;
	}
	Gene centroid = new Gene("centroid",values);
    return centroid; 
  }
  
  /**Method used to mergeClusters*/
  public void mergeClustars(Cluster otherCluster)
  {
	  HashSet<Gene> otherGenes = otherCluster.getGeneSet();
	  for(Iterator<Gene> it = otherGenes.iterator(); it.hasNext();)
	  {
		  Gene otherGene = it.next();
		  geneSet.add(otherGene);
	  }
  }
  
  /**Returns the number of genes in the cluster*/ 
  public int getNumerOfGenes()
  {
	  int numberOfGenes=0;
	  for(Iterator<Gene> it = geneSet.iterator(); it.hasNext();)
	  {
			it.next();
			numberOfGenes++;
	  }
	  return numberOfGenes;
  }
  
  /** Creates an image of this cluster's expression data.*/
  public BufferedImage getHeatMapBufferedImage() 
  {
    //String   strOut;
    int    i, j, iGenes, iConditions;
    double   dValue;
    BufferedImage bimg;
    Graphics2D  gr2d;
    Color   colr;
    
    // Initialize some values
    Vector<Gene> geneVector = new Vector<Gene>();
    Iterator<Gene> it = geneSet.iterator();
    while(it.hasNext())
        geneVector.add(it.next());

    Comparator<Gene> GENE_ORDER = new Comparator<Gene>(){
       public int compare(Gene g1, Gene g2){
          return g1.getName().compareTo(g2.getName());
       }
    };

    Collections.sort(geneVector, GENE_ORDER);

    iGenes = geneVector.size();
    iConditions = (geneVector.get(0)).getValues().length;
    
    // Create the empty image and graphics2D
    bimg = new BufferedImage(c_iX * iConditions, c_iY * iGenes,
                             BufferedImage.TYPE_INT_RGB);
    gr2d = bimg.createGraphics();
    
    // Draw a rectangle for each gene/condition pair
    for(i=0;i<iGenes;++i) 
    {
      for(j=0;j<iConditions;++j) 
      {
        dValue = (geneVector.get(i)).getValues()[j];
        if(dValue<0)
          colr = new Color( 0.0f, ( dValue < -c_dCutoff ) ? 1.0f :
                                 (float)( dValue / -c_dCutoff ), 0.0f );
        else
          colr = new Color( ( dValue > c_dCutoff ) ? 1.0f :
                                 (float)( dValue / c_dCutoff ), 0.0f, 0.0f );
        gr2d.setColor( colr );
        gr2d.fill( new Rectangle2D.Float( j * c_iX, i * c_iY, c_iX, c_iY ) ); 
      } 
    }
    gr2d.dispose();
    
    return bimg;
    
  }
}

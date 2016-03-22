package Algorithm;
// This class represents a gene with associated expression data


public class Gene 
{
  // Data members
  private String geneName; //gene name
  private double[] exprValues; // Actual values of expression data
  private int[] exprRanks;     // Ranks of expression data
  
  // Constructor; sets up instance fields
  public Gene(String name, double[] expressionVals) 
  {
    geneName = name;
    exprValues = new double[expressionVals.length];
    for(int i = 0; i < exprValues.length; i++)
      exprValues[i] = expressionVals[i];
    exprRanks = new int[exprValues.length];
    for(int i = 0; i < exprValues.length; i++)
      for(int j = 0; j < exprValues.length; j++)
        if(exprValues[j] < exprValues[i])
          exprRanks[i]++; 
  }

  // Required for HashSet
  public boolean equals(Object o)
  {
    Gene g = (Gene) o;
    //Writte bybe
    String otherGeneName = g.getName();
    if(otherGeneName.equals(geneName))
    {
        return true;
    }
    else return false;

  }

  // Required for HashSet
  public int hashCode()
  {
    return this.getName().hashCode(); //Scritto da me
    
  }
  
  
  /** Gets expression values*/
  public double[] getValues() 
  {
    return exprValues; 
  }
  
  /**Gets gene name*/
  public String getName() 
  {
    return geneName; 
  }
  
  /** Computes Euclidean distance to another gene. 
   * Distance = 0 indicates identical expression data, 
   * with higher values representing increasingly dissimilar 
   * expression data. 
   */ 
  public double euclideanDistance(Gene other)
  {
	double[] exprValuesOtherGene = other.getValues();
	double quadrat=0;
	double sumOfDifferences=0;
	for(int i=0;i<exprValuesOtherGene.length;i++)
	{
		quadrat=(exprValuesOtherGene[i]-exprValues[i])*(exprValuesOtherGene[i]-exprValues[i]);
		sumOfDifferences=sumOfDifferences+quadrat;
	}
	double euclidianDistance=Math.sqrt(sumOfDifferences);
    
	return euclidianDistance; 
  }
  
  /** Computes Spearman distance between the two genes. 
   * The range of this measure is between 0 and 2, with 
   * 0 representing perfectly correlated expression data, 
   * 1 representing uncorrelated data, and 2 representing 
   * perfectly anticorrelated data. 
   * */
  public double spearmanDistance(Gene other) 
  {
	int[] exprRanksOther = other.getExprRanks();
	double dSquared=0;
	int n=exprRanks.length;
	for(int i=0;i<n;i++)
	{
		double squareOfD = Math.pow((exprRanks[i]-exprRanksOther[i]), 2);
		dSquared=dSquared+squareOfD;
	}
	double spearmanDistance = (6*dSquared)/((Math.pow(n, 3))-n);
	
    return spearmanDistance; 
  }
  
  /**Returns the expression ranks*/
  public int[] getExprRanks()
  {
	  return exprRanks;
  }
}


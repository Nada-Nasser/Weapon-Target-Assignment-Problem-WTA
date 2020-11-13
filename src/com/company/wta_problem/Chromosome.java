package com.company.wta_problem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chromosome 
{
	private ArrayList<Gene> genes;
	private float fitnessValue;
	
	
	/**
	 * @param chromosomeGenes
	 */
	public Chromosome(ArrayList<Gene> chromosomeGenes) {
		this.genes = chromosomeGenes;
		this.fitnessValue = calculateFitnessValue();
	}
	
	public float calculateFitnessValue()
	{
		// <targetName , multiplication of probabilities>. 
		HashMap<Integer, Float> map = new HashMap<Integer, Float>();
		
		for(int weaponIndex = 0 ; weaponIndex < genes.size() ; weaponIndex++)
		{
			Gene gene = genes.get(weaponIndex);
			int targetName = gene.getTarget();
			
			float survivalProb = 1f - Population.successProbability(weaponIndex, targetName);
			
			if(map.get(targetName) == null)
			{
				map.put(targetName, survivalProb);
			}
			else
			{
				float prob = map.get(targetName);
				map.put(targetName, survivalProb*prob);
			}
		}

		float sumFitness = 0f;
		for (Map.Entry<Integer,Float> entry : map.entrySet()) {
	    
			int targetName = entry.getKey();
			float prob = entry.getValue();
			
			sumFitness+= (prob * Population.getTargetThreatValue(targetName));
		
		}

		for (Map.Entry<Integer, Integer>entry : Population.targetThreatMap.entrySet()){
			int targetName = entry.getKey();
			if(!map.containsKey(targetName)) // when there is no weapon assigned to this target
			{
				float threat = 1.0f * entry.getValue();
				sumFitness+=threat;
			}
		}
		
		return sumFitness;
	}
	

	public ArrayList<Gene> getChromosomeGenes() {
		return genes;
	}
	
	

	public void setChromosomeGenes(ArrayList<Gene> chromosomeGenes) {
		this.genes = chromosomeGenes;
	}
	

	public float getFitnessValue() {
		return fitnessValue;
	}
	

	public void setFitnessValue(float fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	
	@Override
	public String toString() 
	{
		String genesStr = "[";
		
		for(int i = 0 ; i < genes.size() ; i++)
		{
			genesStr+= (genes.get(i).getTarget()+1)+"|";
		}
		
		return "Chromosome: genes=" + genesStr + "]\n"
				+ "fitnessValue=" + fitnessValue + "\n";
	}
	
	public ArrayList<Chromosome> crossOver(Chromosome other , int x)
	{
		ArrayList<Gene> genesO1 = new ArrayList<Gene>();
		ArrayList<Gene> genesO2 = new ArrayList<Gene>();
		
		for(int i = 0 ; i < this.genes.size() ; i++)
		{
			if(i < x)
			{
				genesO1.add(this.genes.get(i));
				genesO2.add(other.genes.get(i));
			}
			else
			{
				genesO2.add(this.genes.get(i));
				genesO1.add(other.genes.get(i));
			}
		}
		
		Chromosome O1 = new Chromosome(genesO1);
		Chromosome O2 = new Chromosome(genesO2);
		
		ArrayList<Chromosome> output = new ArrayList<Chromosome>();
		output.add(O1);
		output.add(O2);
		
		
		return output;
	}
	
}

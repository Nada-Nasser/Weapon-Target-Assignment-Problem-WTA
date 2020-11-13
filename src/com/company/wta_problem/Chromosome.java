package com.company.wta_problem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chromosome 
{
	private ArrayList<Gene> genes;
	private double fitnessValue;
	
	
	/**
	 * @param chromosomeGenes
	 */
	public Chromosome(ArrayList<Gene> chromosomeGenes) {
		this.genes = chromosomeGenes;
		this.fitnessValue = calculateFitnessValue();
	}
	
	public double calculateFitnessValue()
	{
		// <targetName , multiplication of probabilities>. 
		HashMap<Integer, Double> map = new HashMap<Integer, Double>();
		
		for(int weaponIndex = 0 ; weaponIndex < genes.size() ; weaponIndex++)
		{
			Gene gene = genes.get(weaponIndex);
			int targetName = gene.getTarget();
			
			double survivalProb = 1- Population.successProbability(weaponIndex, targetName);
			
			if(map.get(targetName) == null)
			{
				map.put(targetName, survivalProb);
			}
			else
			{
				double prob = map.get(targetName);
				map.put(targetName, survivalProb*prob);
			}		
		}
		
		double sumFitness = 0;
		for (Map.Entry<Integer,Double> entry : map.entrySet()) { 
	    
			int targetName = entry.getKey();
			double prob = entry.getValue();
			
			sumFitness+= (prob * Population.getTargetThreatValue(targetName));
		
		}
		
		return sumFitness;
	}
	

	public ArrayList<Gene> getChromosomeGenes() {
		return genes;
	}
	
	

	public void setChromosomeGenes(ArrayList<Gene> chromosomeGenes) {
		this.genes = chromosomeGenes;
	}
	

	public double getFitnessValue() {
		return fitnessValue;
	}
	

	public void setFitnessValue(double fitnessValue) {
		this.fitnessValue = fitnessValue;
	}

	
	@Override
	public String toString() 
	{
		String genesStr = "[";
		
		for(int i = 0 ; i < genes.size() ; i++)
		{
			genesStr+= genes.get(i).getTarget()+"|";
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

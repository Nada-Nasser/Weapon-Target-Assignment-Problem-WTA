package com.company.wta_problem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

public class Population 
{
	private static int nTargets;
	private static int nWeapon;
	private static int nPopulation;
	private static int nParents;
	private static int nIterations;
	
	final private static double Pc =  0.7; //Crossover probability
	final private static double Pm = 0.1; // mutation value

	private static ArrayList<Chromosome> populationChromosomes;
	
	//built from user inputs	
	private static HashMap<Integer , String> indexNameWeaponMap; // list of all weapon with indexes 0,1,2,3,...
																//  Ex: [<0,"Tank"> ,<1,"Tank">,
																//		 <2,aircraft> , <3,aircraft>,
																//		 <4,grenade];
		
	private static HashMap<String , Integer> nameCountWeaponMap; //<weapon name , count>
																// Ex. [<"Tank",2> ,<1,aircraft>,<2,grenade>];
	
	private static HashMap<String , Integer> weaponIntNamesMap; //<n. row in prob. matrix , weapon name>
																// Ex: [<0,"Tank"> , <1,aircraft> , <2,grenade>];
	
	private static HashMap<Integer, Integer> targetThreatMap; // <target name , target threatCoff>	
	private static ArrayList<ArrayList<Double>> probabilityMatrix; //[weapon ith name][target name]
	
	
	public static void initialize() // USER INPUTS
	{
		nIterations = 1000;
		nPopulation = 10;
		nParents = 10;
		
		Scanner in = new Scanner(System.in);
				
		readInputs();
		buildChromosomes(); // initialize population randomly , evaluate fitness
		
		int i = 0;
		
		do {
			i++;
			ArrayList<Chromosome> selectedParents = selection();
			
			ArrayList<Chromosome> offspringChromosomes = crossOver(selectedParents);
			
			//in.nextInt();
			mutation(offspringChromosomes);
			Replacement(offspringChromosomes);
			
		} while (i <= nIterations);
		finalOutput();
	}

	private static void finalOutput(){
		System.out.println("The final WTA solution is:");
		Chromosome best = populationChromosomes.get(0);
		for(Chromosome x : populationChromosomes){
			if(x.getFitnessValue() > best.getFitnessValue()){
				best = x;
			}
		}
		System.out.println("fitness : " + best.getFitnessValue());
		ArrayList<Gene> bestGenes = best.getChromosomeGenes();
		int geneIndex=0;
		for(Gene x: bestGenes){
			System.out.println(indexNameWeaponMap.get(geneIndex)
			+ " is assigned to target : " + (x.getTarget()+1));
			geneIndex++;
		}
		System.out.println();
	}

	private double totalThreat(Chromosome bestChromosome){
		double totalThreat = 0.0;
		ArrayList<Gene> Genes = bestChromosome.getChromosomeGenes();
		for(Gene c : Genes){

		}
		return totalThreat;
	}
	private static void Replacement(ArrayList<Chromosome> offspringChromosomes) {
		populationChromosomes.clear();
		populationChromosomes.addAll(offspringChromosomes);
		offspringChromosomes.clear();
	}
	
	private static void mutation(ArrayList<Chromosome> offspringChromosomes) {
		for(Chromosome x : offspringChromosomes) {
			ArrayList<Gene> ChromosomeGenes = x.getChromosomeGenes();
			for(Gene y: ChromosomeGenes) {
				Random r = new Random();
				double MutProb = r.nextDouble();
				if (MutProb <= Pm) {
					Random rand = new Random();
					int target = rand.nextInt(nTargets);
					y.setTarget(target);
				}
			}
			x.setFitnessValue(x.calculateFitnessValue());
		}
	}

	private static ArrayList<Chromosome> crossOver(ArrayList<Chromosome> selectedParents) {
		ArrayList<Chromosome> offspringChromosomes = new ArrayList<Chromosome>();
		
		for(int i = 0  ; i <nParents/2 ; i+=2) // cross over between i and i+1
		{
			Random r = new Random();
			int xc = r.nextInt(nWeapon-2) + 1;
			double rc =  r.nextDouble();
			
			if(rc <= Pc) //then Cross over occurs 
			{
				//perform crossover at Xc using  i and i+1 parents
				ArrayList<Chromosome> crossoverOutput = selectedParents.get(i)
											.crossOver(selectedParents.get(i+1), xc);
				offspringChromosomes.addAll(crossoverOutput);
			}
			else //then No CrossOver 
			{
				offspringChromosomes.add(selectedParents.get(i));
				offspringChromosomes.add(selectedParents.get(i+1));	
			}
		}	
		return offspringChromosomes;
	}

	private static ArrayList<Chromosome> selection() {
		ArrayList<Chromosome> selectedParents = new ArrayList<Chromosome>();
		
		ArrayList<Double> cumulativeFitness = new ArrayList<Double>();
		double cumulativeSum = 0;
		
		// calculate the cumulative fitness function and add it to cumulativeFitness array
		for(int i = 0 ; i < populationChromosomes.size() ; i++) 
		{
			// we use the inverse of the fitness value because it's a minimize problem
			cumulativeSum+= (1/populationChromosomes.get(i).getFitnessValue());  
			cumulativeFitness.add(cumulativeSum);
		}
		
		Random r = new Random();
		for(int i = 0 ; i < nParents ; i++) 
		{	
			double random = 0 + r.nextDouble() * (cumulativeSum - 0); // generate a random (r1) between 0 and Cumulative sum:
			
			for(int j = 0 ; j < cumulativeFitness.size() ; j++) // see in which range is the random r to select the parent
			{
				if(random < cumulativeFitness.get(j))
				{
					selectedParents.add(populationChromosomes.get(j));
					break;
				}
			}
		}
	
		return selectedParents;
	}

	private static void readInputs()
	{	
		indexNameWeaponMap = new HashMap<Integer , String>();
		nameCountWeaponMap = new HashMap<String, Integer>();
		weaponIntNamesMap = new HashMap<String, Integer>();

		Scanner in = new Scanner(System.in);
		System.out.println("Enter the weapon types and the number of instances of each type: (Enter\n" +
				"“x” when you’re done)");
		String weaponname;
		int count;
		int index =0;
		int place=0;
		weaponname = in.next();
		while(!weaponname.equals("x")){
			count = in.nextInt();
			for(int i=0; i<count; i++){
				indexNameWeaponMap.put(index, weaponname);
				index++;
			}
			nameCountWeaponMap.put(weaponname, count);
			weaponIntNamesMap.put(weaponname,place);
			weaponname = in.next();
			place++;
		}
		nWeapon = indexNameWeaponMap.size();

		System.out.println("Enter the number of targets:");
		nTargets = in.nextInt();

		targetThreatMap = new HashMap<Integer, Integer>();

		System.out.println("Enter the threat coefficient of each target:");
		for(int i=0; i<nTargets; i++){
			targetThreatMap.put(i, in.nextInt());
		}

		probabilityMatrix = new ArrayList<ArrayList<Double>>();
		ArrayList<Double> prob = new ArrayList<Double>();
		System.out.println("Enter the weapons’ success probabilities matrix:");
		for(int i=0; i<nameCountWeaponMap.size(); i++){
			for(int y=0; y<nTargets; y++){
				prob.add(in.nextDouble());
			}
			probabilityMatrix.add(prob);
		}
		System.out.println("Please wait while running the GA…");


//		indexNameWeaponMap.put(0, "Tank");
//		indexNameWeaponMap.put(1, "Tank");
//		indexNameWeaponMap.put(2, "aircraft");
//		indexNameWeaponMap.put(3, "grenade");
//		indexNameWeaponMap.put(4, "grenade");
//
//		nameCountWeaponMap.put("Tank", 2);
//		nameCountWeaponMap.put("aircraft", 1);
//		nameCountWeaponMap.put("grenade", 2);
//		weaponIntNamesMap.put("Tank" , 0);
//		weaponIntNamesMap.put("aircraft" , 1);
//		weaponIntNamesMap.put("grenade" , 2);
//
//		nTargets = 3;
//		nWeapon = 5;
		
//		targetThreatMap = new HashMap<Integer, Integer>();
//		targetThreatMap.put(0, 16);
//		targetThreatMap.put(1, 5);
//		targetThreatMap.put(2, 10);
		
//		probabilityMatrix = new ArrayList<ArrayList<Double>>();
		
//		ArrayList<Double> prob1 = new ArrayList<Double>();
//		prob1.add(0.3);
//		prob1.add( 0.6);
//		prob1.add( 0.5);
//
//		ArrayList<Double> prob2 = new ArrayList<Double>();
//		prob2.add(0.4);
//		prob2.add(0.5);
//		prob2.add(0.4);
//
//		ArrayList<Double> prob3 = new ArrayList<Double>();
//		prob3.add(0.1);
//		prob3.add(0.2);
//		prob3.add(0.2);
//
//		probabilityMatrix.add(prob1);
//		probabilityMatrix.add(prob2);
//		probabilityMatrix.add(prob3);
//
//		System.out.println("Please wait while running the GA�");
	}
	
	
	private static void buildChromosomes() // randomly generate chromosomes
	{
		
		populationChromosomes = new ArrayList<Chromosome>();
				
		for(int i = 0 ; i < nPopulation ; i++) // for each chromosome generate random genes.
		{
			Random rand = new Random();
			ArrayList<Gene> genes = new ArrayList<Gene>();
			
			for(int weapon = 0 ; weapon < nWeapon ; weapon++) // generate randome genes
			{
				int target = rand.nextInt(nTargets);
				genes.add(new Gene(target));	
			}
			
			Chromosome chromosome = new Chromosome(genes); // in the constructor, the fitness value evaluated also
			//System.out.println(chromosome.toString()); // TODO: Delete this line
			populationChromosomes.add(chromosome);
		}	
		
	}

	/**
	 * @param weapon ith name
	 * @param target name
	 * @return success Probability
	 */
	public static double successProbability(int weapon , int target)
	{
		String weaponName = indexNameWeaponMap.get(weapon);
		int weaponProbIndex = weaponIntNamesMap.get(weaponName);
		
		return probabilityMatrix.get(weaponProbIndex).get(target);
	}
	
	/**
	 * @param targetName
	 * @return target's threat value
	 */
	public static double getTargetThreatValue(int targetName)
	{
		return targetThreatMap.get(targetName);
	}
	
	
	
}

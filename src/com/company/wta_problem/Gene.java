package com.company.wta_problem;
public class Gene 
{
	private int  target; // target name (T) -> (T1 = 0 , T2 = 1 , T3=2 ,.... Tn = n-1)
	

	public Gene(int target) {
		this.target = target;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}
	
}

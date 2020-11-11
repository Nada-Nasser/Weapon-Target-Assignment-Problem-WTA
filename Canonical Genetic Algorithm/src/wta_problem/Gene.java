package wta_problem;

public class Gene 
{
	private int  target; // target name (T) -> (T1 = 0 , T2 = 1 , T3=2 ,.... Tn = n-1)
	
	/**
	 * @param target : int
	 */
	public Gene(int target) {
		this.target = target;
	}
	
	/**
	 * @return the target
	 */
	public int getTarget() {
		return target;
	}
	/**
	 * @param target the target to set
	 */
	public void setTarget(int target) {
		this.target = target;
	}
	
}

package project.utility;

public interface Time {

	public static final double SECONDS = 1.0;
	
	// Pauses the program for the supplied seconds
	public static void hold() {
		try {
			Thread.sleep((long) (SECONDS * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/** 
	* Pauses the program
	* @param sec seconds the program stops for
	*/ 
	public static void hold(double sec) {
		try {
			Thread.sleep((long) (sec * 1000));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
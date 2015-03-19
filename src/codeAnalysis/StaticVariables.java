package codeAnalysis;


public class StaticVariables {

	private static int numOfMethods = 0;
	private static int numOfLines = 0;

	public static void increaseMethodCount(){
		numOfMethods++;
	}
	
	public static void increaseLineCount(int line){
		numOfLines += line;
	}
	
	public static int getNumOfMethods(){
		return numOfMethods;
	}
	
	public static double AverageLinesPerMethod(){
		return (double) numOfLines / (double) numOfMethods;
	}
}

package codeAnalysis;
import java.util.List;

public class ClassInfo extends Info{

	public String name;
	public String associatedPackage;
	public int numberOfVariables;
	public int numberOfPublicVariables;
	public int numberOfProtectedVariables;
	public int numberOfPrivateVariables;
	
	public int numberOfLines;
	public int numberOfMethods;
	public double averageLinesPerMethod; // Excludes whitespace
	public double averageMethodComplexity;
	public double weightedMethodsPerClass = 0;
	public int depthOfInheritance;
	public double LCOM;
	
	public double CBO;
	
	public List<MethodInfo> methods;
	
	public ClassInfo(String name,
					String associatedPackage,
					int numberOfVariables,
					int numberOfPublicVariables,
					int numberOfProtectedVariables,
					int numberOfPrivateVariables,
					int numberOfLines,
					int numberOfMethods,
					double averageLinesPerMethod,
					int depthOfInheritance,
					double CBO,
					double LCOM,
					List<MethodInfo> methods){

		this.name = name;
		this.associatedPackage = associatedPackage;
		this.numberOfVariables = numberOfVariables;
		this.numberOfPublicVariables = numberOfPublicVariables;
		this.numberOfProtectedVariables = numberOfProtectedVariables;
		this.numberOfPrivateVariables = numberOfPrivateVariables;
		this.numberOfLines = numberOfLines;
		this.numberOfMethods = numberOfMethods;
		this.averageLinesPerMethod = averageLinesPerMethod;
		this.depthOfInheritance = depthOfInheritance;
		this.CBO = CBO;
		this.LCOM = LCOM;
		this.methods = methods;
		
		for(MethodInfo m : methods){
			weightedMethodsPerClass += m.complexity;
		}
		
		if(methods.size() > 0){
			averageMethodComplexity = weightedMethodsPerClass / methods.size();
		}
	}

	@Override
	public String toString(){
		return ("Name: " + name + 
				"\nPackage: " + associatedPackage + 
				"\nNumber of Variables: " + numberOfVariables + 
				"\nNumber of Public Variables: " + numberOfPublicVariables + 
				"\nNumber of Protected Variables: " + numberOfProtectedVariables + 
				"\nNumber of Private Variables: " + numberOfPrivateVariables + 
				"\nNumber of Lines: " + numberOfLines +
				"\nNumber Of Methods: " + numberOfMethods + 
				"\nAverage Lines per Method: " + averageLinesPerMethod +
				"\nDepth of Inheritance: " + depthOfInheritance + 
				"\nCoupling Between Objects: " + CBO + 
				"\nLack of Cohesion Methods: " + LCOM +
				"\nWeighted Methods per Class: " + weightedMethodsPerClass + 
				"\nAverage Method Complexity: " + averageMethodComplexity);
	}

	@Override
	public int getType() {
		return 1;
	}
}

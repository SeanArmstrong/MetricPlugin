package codeAnalysis;
import org.eclipse.jdt.core.dom.Type;


public class MethodInfo extends Info{

	public String name;
	public int numberOfLines;
	public Type returnType;
	public String body;
	public int complexity = 1;
	
	public MethodInfo(String name,
						int numberOfLines,
						Type returnType){
		this.name = name;
		this.numberOfLines = numberOfLines;
		this.returnType = returnType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumberOfLines() {
		return numberOfLines;
	}

	public void setNumberOfLines(int numberOfLines) {
		this.numberOfLines = numberOfLines;
	}

	public Type getReturnType() {
		return returnType;
	}

	public void setReturnType(Type returnType) {
		this.returnType = returnType;
	}
	
	@Override
	public String toString(){
		return "Name: " + name +
				" - Lines: " + numberOfLines + 
				", Complexity: " + complexity;
	}

	@Override
	public int getType() {
		return 2;
	}
}

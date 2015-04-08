package codeAnalysis;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

public class CohesionMethodVisitor extends ASTVisitor {

	private Map<String, Integer> m = new HashMap<String, Integer>();
	private int numberOfMethodsThatAccessFields = 0;
	
	public CohesionMethodVisitor(){
		
	}
	
	public boolean visit(VariableDeclarationFragment node){
		// Does this cause an error if variables are defined last?
		// Should collect all variables then all methods then run code
		m.put(node.getName().toString(), 0);
		return true;
	}
	
	public boolean visit(MethodDeclaration node){
		boolean acceptedMethod = false;
		for (Map.Entry<String, Integer> entry : m.entrySet()) {
		    String key = entry.getKey();
			if (node.getBody().toString().contains(entry.getKey())){
				entry.setValue(entry.getValue() + 1);
				if(!acceptedMethod){
					numberOfMethodsThatAccessFields++;
					acceptedMethod = true;
				}
			}
		}
		return true;
	}
	
	public void printMap(){
		for (Map.Entry<String, Integer> entry : m.entrySet()) {
			System.out.println(entry.getKey() + ": " + entry.getValue());
		}
	}
	
	public double getLCOM(){
		//Note 1: I have only included methods if they access at least one field.
		//Note 2: I have only included fields if they are accessed by at least one method in the class.
		// Henderson Sellers
		//http://eclipse-metrics.sourceforge.net/descriptions/pages/cohesion/HendersonSellers.html
		
		//<p> - |M|/1 - |M|
		
		if(m.size() == 0){
			return 0;
		}
		if(numberOfMethodsThatAccessFields == 1){
			numberOfMethodsThatAccessFields = 2;
		}
		
		double meanMethodAccess = 0;
		for (int value : m.values()) {
		    if(value > 0){
		    	meanMethodAccess += (double) value;
		    }
		}

		meanMethodAccess = meanMethodAccess / (double) m.size();

		double LCOM = (meanMethodAccess - (double) numberOfMethodsThatAccessFields) / (1.0 - (double) numberOfMethodsThatAccessFields);
		
		return LCOM;
	}
}

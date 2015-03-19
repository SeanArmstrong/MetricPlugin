package codeAnalysis;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

/*public class CyclomaticComplexityVisitor extends ASTVisitor {
	
	
	public CyclomaticComplexityVisitor(){
		System.out.println("New CYCLO");
	}
	
	//private int complexityScore = 0;
	//private int edges = 0;
	//private int nodes = 0;
	//private int exitPoints = 1;
	public int paths = 1;
	private boolean firstReturn = true;
 
	public boolean visit(IfStatement node){
		paths++;
		System.out.println("THERE WAS AN IF");
		//String statement = node.toString();
		//System.out.println(statement);

		return true;
	}
	
	public boolean visit(ReturnStatement node){
		paths++;
		//if (firstReturn) {
		//	firstReturn = false;
		//} else {
		//	exitPoints++;
		//}
		return true;
	}
	
	public boolean visit(ForStatement node){
		paths++;
		//System.out.println("THERE WAS AN FOR");
		return true;
	}
	
	public boolean visit(DoStatement node){
		paths++;
		return true;
	}
	
	public boolean visit(EnhancedForStatement node){
		paths++;
		return true;
	}
	
	public boolean visit(SwitchStatement node){
		return true;
	}
	
	public boolean visit(WhileStatement node){
		paths++;
		return true;
	}
	
	public boolean visit(TryStatement node){
		return true;
	}
	
	public boolean visit(CatchClause node){
		paths++;
		return true;
	}
	
	public boolean visit(ThrowStatement node){
		return true;
	}
}*/

/*
 * 
 * 
 * 
 * 
 * 		Pattern pattern = Pattern.compile("(else\\sif\\s)|if\\s|else\\s|");
		Matcher matcher = pattern.matcher(statement);
		while(matcher.find()){
			System.out.println("MATCH");
		}
		System.out.println();
*/

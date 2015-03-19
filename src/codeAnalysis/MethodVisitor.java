package codeAnalysis;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.WhileStatement;

public class MethodVisitor extends ASTVisitor {
	
	private CompilationUnit cu;
	private int numberOfMethods;
	private int lineCount;
	private int methodAt = -1;
	
	private List<MethodInfo> methods = new ArrayList<MethodInfo>();

	
	public MethodVisitor(CompilationUnit cu){
		this.cu = cu;
	}
	
	public boolean visit(MethodDeclaration node){
		int startPos = cu.getLineNumber(node.getStartPosition());
		int endPos = cu.getLineNumber(node.getStartPosition() + node.getLength());

		lineCount += (endPos - startPos);
		numberOfMethods++;
		
		String methodBody = node.toString();

		MethodInfo m = new MethodInfo(node.getName().getIdentifier(),
									 (endPos - startPos),
									  node.getReturnType2());
		
		
		m.body = methodBody;
		methods.add(m);	
		methodAt++; // From now on all visits will be on the new method
		return true;
	}
		
	public double getAverageLinesPerMethod(){
		return (double) lineCount / (double) numberOfMethods;
	}
	
	public int getNumberOfMethods(){
		return numberOfMethods;
	}
	
	public int getLineCount(){
		return lineCount;
	}
	
	public List<MethodInfo> getMethods(){
		return methods;
	}
	
	public boolean visit(IfStatement node){
        Pattern pattern = Pattern.compile("(&&|\\|\\|)");
        Matcher  matcher = pattern.matcher(node.getExpression().toString());
        
        // Add 1 for every && and ||
        while (matcher.find()){
        	methods.get(methodAt).complexity++;
        }
        
        // Add 1 for If statement
		methods.get(methodAt).complexity++;
		return true;
	}
	
	public boolean visit(ForStatement node){
		methods.get(methodAt).complexity++;
		return true;
	}
	
	public boolean visit(DoStatement node){
		methods.get(methodAt).complexity++;
		return true;
	}
	
	public boolean visit(EnhancedForStatement node){
		methods.get(methodAt).complexity++;
		return true;
	}
	
	public boolean visit(WhileStatement node){
		methods.get(methodAt).complexity++;
		return true;
	}
		
	public boolean visit(CatchClause node){
		methods.get(methodAt).complexity++;
		return true;
	}
	
	public boolean visit(ConditionalExpression node){
		methods.get(methodAt).complexity++;
		return true;
	}
	
	public boolean visit(SwitchCase node){
		methods.get(methodAt).complexity++;
		return true;
	}
}

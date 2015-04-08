package codeAnalysis;

import java.lang.reflect.Modifier;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassVisitor extends ASTVisitor {

	private String className;
	public String associatedPackage;
	
	private int numberOfVariables = 0;
	private int publicVariables = 0;
	private int protectedVariables = 0;
	private int privateVariables = 0;
	
	private int depthOfInheritance = 0;
	
	public ClassVisitor(){

	}
	
	public boolean visit(TypeDeclaration td) {
		className = td.getName().getIdentifier();
		associatedPackage = td.resolveBinding().getPackage().toString();
		
		FieldDeclaration[] fields = td.getFields();
		for(FieldDeclaration f : fields){
			numberOfVariables++;
			if(Modifier.isPublic(f.getModifiers())){
				publicVariables++;
			} else if(Modifier.isPrivate(f.getModifiers())){
				privateVariables++;
			} else {
				protectedVariables++;
			}
		}
		
		// Depth of Inheritance
		ITypeBinding superklass = td.resolveBinding().getSuperclass();
		while(superklass != null){
			depthOfInheritance++;
			superklass = superklass.getSuperclass();
		}
				
		return true;
	}
	
	public String getClassName(){
		return className;
	}
	
	public String getAssociatedPackageName(){
		return associatedPackage;
	}
	
	public int getDepthOfInheritance(){
		return depthOfInheritance;
	}
	
	public int getNumberOfVariables(){
		return numberOfVariables;
	}
	
	public int getNumberOfPublicVariables(){
		return publicVariables;
	}
	
	public int getNumberOfProtectedVariables(){
		return protectedVariables;
	}
	
	public int getNumberOfPrivateVariables(){
		return privateVariables;
	}
}

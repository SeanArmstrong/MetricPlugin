package codeAnalysis;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.SimpleName;

public class CouplingVisitor extends ASTVisitor {

	private Set<IType> uniqueCouplingObjects = new HashSet<IType>();
	
	public CouplingVisitor(){
		
	}
	
	@Override
	public boolean visit(SimpleName node){
		ITypeBinding typeBinding = node.resolveTypeBinding();
        if (typeBinding == null)
            return true;
        IJavaElement element = typeBinding.getJavaElement();
        if (element != null && element instanceof IType) {
        	uniqueCouplingObjects.add((IType)element);
        }
        return true;
	}
	
	public int getUniqueCouplingObjectsSize(){
		return uniqueCouplingObjects.size();
	}
}

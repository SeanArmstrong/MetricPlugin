package codeAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.swt.custom.CBanner;

public class Test {

	private static List<ClassInfo> klasses = new ArrayList<ClassInfo>();
	
	// Called for every file where str is what the file contains
	public static void parse(String str, IJavaProject project, String filePath) {
		
		
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setUnitName(filePath);
		
		parser.setProject(project);
		parser.setSource(str.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		
		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);	 
	
		ClassVisitor classV = new ClassVisitor();
		cu.accept(classV);
		
		CouplingVisitor couplingV = new CouplingVisitor();
		cu.accept(couplingV);
		
		CohesionMethodVisitor cohesionMV = new CohesionMethodVisitor();
		cu.accept(cohesionMV);
		
		MethodVisitor methodsVisitor = new MethodVisitor(cu);
		cu.accept(methodsVisitor);
		
		if(classV.getClassName() != null){
			ClassInfo klass = new ClassInfo(
					classV.getClassName(),
					classV.getAssociatedPackageName(),
					classV.getNumberOfVariables(),
					classV.getNumberOfPublicVariables(),
					classV.getNumberOfProtectedVariables(),
					classV.getNumberOfPrivateVariables(),
					cu.getLineNumber(cu.getLength() - 1),
					methodsVisitor.getNumberOfMethods(),
					methodsVisitor.getAverageLinesPerMethod(),
					classV.getDepthOfInheritance(),
					couplingV.getUniqueCouplingObjectsSize(),
					cohesionMV.getLCOM(),
					methodsVisitor.getMethods());
			
			klasses.add(klass);
		}
	}
	

		
	//read file content into a string
	public static String readFileToString(String filePath) throws IOException {
		StringBuilder fileData = new StringBuilder(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
 
		char[] buf = new char[10];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			//System.out.println(numRead);
			String readData = String.valueOf(buf, 0, numRead);
			fileData.append(readData);
			buf = new char[1024];
		}
 
		reader.close();
 
		return  fileData.toString();	
	}
 
	//loop directory to get file list
	public static void ParseFilesInDir() throws IOException, CoreException{
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(IProject project : projects ){
			IPath projPath = project.getLocation();
			String dirPath = projPath + File.separator+"src"+File.separator;
			File root = new File(dirPath);
			
			File[] files = root.listFiles();
			String filePath = null;
						
			for (File f : files ) {
				filePath = f.getAbsolutePath();
				if(f.isFile()){
					parse(readFileToString(filePath), JavaCore.create(project), filePath);
				}
			}
		}
	}
 
	public static void access(){
		try {
			ParseFilesInDir();
		} catch (IOException e) {
			System.out.println("File error");
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		toJson j = new toJson(klasses);
		j.convert();
		
		//createTotalClass();
		//printInfo();
	}
	
	public static void createTotalClass(){
		int variables = 0;
		int pubVariables = 0;
		int proVariables = 0;
		int priVariables = 0;
		int lines = 0;
		int methods = 0;
		
		double averageLinesPerMethod = 0.0;
		double averageCBO = 0.0;
		double averageLCOM = 0.0;
		double averageWMC = 0.0;
		double averageAMC = 0.0;
		
		int maxDepth = 0;
		
		
		for(ClassInfo klass : klasses){
			variables += klass.numberOfVariables;
			pubVariables += klass.numberOfPublicVariables;
			proVariables += klass.numberOfProtectedVariables;
			priVariables += klass.numberOfPrivateVariables;
			lines += klass.numberOfLines;
			methods += klass.methods.size();
			
			averageLinesPerMethod += klass.averageLinesPerMethod;
			averageCBO += klass.CBO;
			averageLCOM += klass.LCOM;
			averageWMC += klass.weightedMethodsPerClass;
			averageAMC += klass.averageMethodComplexity;
			
			if(klass.depthOfInheritance > maxDepth){
				maxDepth = klass.depthOfInheritance;
			}
		}
		
		averageLinesPerMethod /= klasses.size();
		averageCBO /= klasses.size();
		averageLCOM /= klasses.size();
		averageWMC /= klasses.size();
		averageAMC /= klasses.size();
		
		List<MethodInfo>  m = new ArrayList<MethodInfo>();
		
		ClassInfo klass = new ClassInfo(
				"Total",
				"-",
				variables,
				pubVariables,
				proVariables,
				priVariables,
				lines,
				methods,
				averageLinesPerMethod,
				maxDepth,
				averageCBO,
				averageLCOM,
				m);
		
		klass.averageMethodComplexity = averageAMC;
		klass.weightedMethodsPerClass = averageWMC;
		klasses.add(klass);
		
		
	}
	
	
	public static List<ClassInfo> getClasses(){
		return klasses;
	}
	
	public static void printInfo(){
		System.out.println();
		for(ClassInfo klass : klasses){
			System.out.println(klass);
			System.out.println("Methods: ");
			for(MethodInfo method : klass.methods){
				System.out.println(method);
				
			}
			System.out.println("\n");
		}
		
		System.out.println("Class Count: " + klasses.size());
	}
}

/*

public static void ParseMethodsInClasses(){
for(int i = 0; i < klasses.size(); i++){
	ClassInfo klass = klasses.get(i);
	for(int j = 0; j < klass.methods.size(); j++){
		//System.out.println(klass.methods.get(j).body);
		ParseMethod(klass.methods.get(j).body);
	}	
}
}

public static void ParseMethod(String str){
ASTParser parser = ASTParser.newParser(AST.JLS3);
parser.setSource(str.toCharArray());
parser.setKind(ASTParser.K_COMPILATION_UNIT);
parser.setResolveBindings(true);

final CompilationUnit cu = (CompilationUnit) parser.createAST(null);
System.out.println("CU = " + cu.toString());
ClassVisitor cv = new ClassVisitor();
//CyclomaticComplexityVisitor ccv = new CyclomaticComplexityVisitor();

cu.accept(ccv);

System.out.println(ccv.paths);
}

*/
package codeAnalysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.Workbench;

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
					methodsVisitor.getMethods(),
					false);
			
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
 
	public static void ParseFilesInDir(IProject project) throws IOException{
		IPath projPath = project.getLocation();
		String dirPath = projPath + File.separator+"src"+File.separator;
		File root = new File(dirPath);
		
		File[] files = root.listFiles();
		String filePath = null;
					
		for (File f : files ) {
			System.out.println(f.getAbsolutePath() + " : " + f.isDirectory());
			if(f.isDirectory()){ //If a package
				for(File subF : f.listFiles()){
					if(subF.isFile()){
						filePath = subF.getAbsolutePath();
						System.out.println(filePath);
						parse(readFileToString(filePath), JavaCore.create(project), filePath);
					}
				}
			}else{
				
				filePath = f.getAbsolutePath();
				if(f.isFile()){
					parse(readFileToString(filePath), JavaCore.create(project), filePath);
				}
			}
		}
	}
	
	public static List<ClassInfo> access(IProject project){
		klasses = new ArrayList<ClassInfo>();
		try {
			ParseFilesInDir(project);
		} catch (IOException e) {
			System.out.println("File error");
		} catch (Exception e) {
			e.printStackTrace();
		}
			
		createTotalClass();
		//printInfo();
		return klasses;
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
		double averageSDMC = 0.0;
		
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
			averageSDMC += klass.SDMC;
			
			if(klass.depthOfInheritance > maxDepth){
				maxDepth = klass.depthOfInheritance;
			}
		}
		
		averageLinesPerMethod /= klasses.size();
		averageCBO /= klasses.size();
		averageLCOM /= klasses.size();
		averageWMC /= klasses.size();
		averageAMC /= klasses.size();
		averageSDMC /= klasses.size();
		
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
				m,
				true);
		
		klass.averageMethodComplexity = averageAMC;
		klass.weightedMethodsPerClass = averageWMC;
		klass.SDMC = averageSDMC;
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
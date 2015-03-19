package metric.views;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

import codeAnalysis.ClassInfo;
import codeAnalysis.Info;
import codeAnalysis.MethodInfo;

public class MetricResultsViewLabelProvider extends LabelProvider implements ITableLabelProvider {

	@Override
	public Image getColumnImage(Object element, int columnIndex) {
		switch(columnIndex){
		case 0:
			break;
		case 1:
			break;
		case 2:
			break;
		case 4:
			break;
		}
		return null;
	}

	@Override
	public String getColumnText(Object element, int columnIndex) {
		if(element instanceof Info){
			if (element instanceof ClassInfo){
				switch(columnIndex){
				case 0:
					return ((ClassInfo) element).name;
				case 1:
					return ((ClassInfo) element).associatedPackage;
				case 2:
					return Integer.toString(((ClassInfo) element).numberOfLines);
				case 3:
					return Integer.toString(((ClassInfo) element).numberOfMethods);
				case 4:
					return Integer.toString(((ClassInfo) element).numberOfVariables);
				case 5:
					return Double.toString(((ClassInfo) element).averageLinesPerMethod);
				case 6:
					return Integer.toString(((ClassInfo) element).depthOfInheritance);
				case 7:
					return Double.toString(((ClassInfo) element).CBO);
				case 8:
					return Double.toString(((ClassInfo) element).LCOM);
				case 9:
					return Double.toString(((ClassInfo) element).weightedMethodsPerClass);
				case 10:
					return Double.toString(((ClassInfo) element).averageMethodComplexity);
				default:
					return "-";
				}
			} else if (element instanceof MethodInfo){
				switch(columnIndex){
				case 0:
					return ((MethodInfo) element).name;
				case 2:
					return Integer.toString(((MethodInfo) element).numberOfLines);
				case 9: 
					return Integer.toString(((MethodInfo) element).complexity);
				default:
					return "-";
				}
			}
		}
		return "-";
	}

}

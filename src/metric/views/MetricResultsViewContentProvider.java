package metric.views;

import java.util.List;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

import codeAnalysis.ClassInfo;
import codeAnalysis.Info;

public class MetricResultsViewContentProvider implements ITreeContentProvider {

	private List<ClassInfo> content;
	private TreeViewer viewer;
	
	@Override
	public void dispose() {	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		this.viewer = (TreeViewer)viewer;
		//content = (List<ClassInfo>)newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		List<ClassInfo> result = (List<ClassInfo>) inputElement;
		return result.toArray();
		//return new String[] { "Double Click to run metrics" };
	}
	
	
	/* Other methods */
	
	public void removeAll(){
		
		//viewer.remove(content.toArray());
		content.clear();
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return ((ClassInfo) parentElement).methods.toArray();
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		if(element instanceof Info){
			if(element instanceof ClassInfo){
				return ((ClassInfo) element).methods.size() > 0;
			}
		}
		return false;
	}
	

}

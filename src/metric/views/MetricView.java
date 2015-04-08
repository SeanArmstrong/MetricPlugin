package metric.views;

import helpers.PopupWindow;

import java.util.List;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.part.*;
import org.eclipse.core.internal.resources.Project;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.internal.ui.packageview.PackageFragmentRootContainer;
import org.eclipse.jface.viewers.*;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.action.*;
import org.eclipse.ui.*;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.SWT;

import codeAnalysis.ClassInfo;
import codeAnalysis.Test;


/**
 * This sample class demonstrates how to plug-in a new
 * workbench view. The view shows data obtained from the
 * model. The sample creates a dummy model on the fly,
 * but a real implementation would connect to the model
 * available either in this or another plug-in (e.g. the workspace).
 * The view is connected to the model using a content provider.
 * <p>
 * The view uses a label provider to define how model
 * objects should be presented in the view. Each
 * view can present the same model objects using
 * different labels and icons, if needed. Alternatively,
 * a single label provider can be shared between views
 * in order to ensure that objects of the same type are
 * presented in the same way everywhere.
 * <p>
 */

public class MetricView extends ViewPart {
	
	/**
	 * Column names
	 */
	private final String COLUMN0 = "Class Name";
	private final String COLUMN1 = "Package";
	private final String COLUMN2 = "Lines";
	private final String COLUMN3 = "Methods";
	private final String COLUMN4 = "Variables";;
	private final String COLUMN5 = "Average Lines Per Method";
	private final String COLUMN6 = "DOT";
	private final String COLUMN7 = "CBO";
	private final String COLUMN8 = "LCOM";
	private final String COLUMN9 = "Complexity (WMC)";
	private final String COLUMN10 = "Complexity (AMC)";
	private final String COLUMN11 = "Complexity (SDMC)";
	
	
	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "gfdg.views.Metric";

	Composite parent;
	private TreeViewer viewer;
	private Action action1;
	private Action action2;
	private Action doubleClickAction;

	private List<ClassInfo> klasses;
	
	/**
	 * The constructor.
	 */
	public MetricView() {
	}

	/**
	 * This is a callback that will allow us
	 * to create the viewer and initialize it.
	 */
	public void createPartControl(Composite parent) {
		//Test.access();
		
		
		this.parent = parent;

		viewer = new TreeViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);
		Tree table = viewer.getTree();
		TreeColumn column;
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN0);
		column.setWidth(200);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN1);
		column.setWidth(150);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN2);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN3);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN4);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN5);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN6);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN7);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN8);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN9);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN10);
		column.setWidth(100);
		
		column = new TreeColumn(table, SWT.LEFT);
		column.setText(COLUMN11);
		column.setWidth(100);
		
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		
		
		viewer.setContentProvider(new MetricResultsViewContentProvider());
		viewer.setLabelProvider(new MetricResultsViewLabelProvider());;

		// Create the help context id for the viewer's control
		PlatformUI.getWorkbench().getHelpSystem().setHelp(viewer.getControl(), "metric.views");
		makeActions();
		//hookContextMenu();
		//hookDoubleClickAction();
		contributeToActionBars();
	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				MetricView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}
	
	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(action1);
		manager.add(action2);
	}

	private void makeActions() {
		action1 = new Action() {
			public void run() {
				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				//ProjectSelectionWindow p = new ProjectSelectionWindow(shell, klasses);
				ElementListSelectionDialog dialog=new ElementListSelectionDialog(shell, new LabelProvider());
				dialog.setTitle("Project Selection");
				dialog.setMessage("Select a Project to run the Metrics Against\nYou can search in the box below");
				dialog.setElements(getProjects());
				
				int result = dialog.open();	
				if(result == Window.OK){
					System.out.println(((Object)dialog.getResult()[0]).toString());
					klasses = Test.access((Project)dialog.getResult()[0]);
					viewer.setInput(klasses);
					action2.setEnabled(true);
				}
			}
		};

		action1.setText("Run Metrics");
		action1.setToolTipText("Run Metrics");
		action1.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_TASK_TSK));
		//ISharedImages.IMG_OBJS_TASK_TSK
		
		action2 = new Action(){
			public void run() {
				Display display = Display.getDefault();
				Shell shell = new Shell(display);
				PopupWindow p = new PopupWindow(shell, klasses);
				int result = p.open();						
			}
		};
		action2.setEnabled(false);
		action2.setText("Upload Metrics");
		action2.setToolTipText("Upload Metrics");
		action2.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_ETOOL_SAVE_EDIT));
		
	}

	private void hookDoubleClickAction() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});
	}


	private void fillLocalPullDown(IMenuManager manager) {
		//manager.add(action1);
		//manager.add(new Separator());
		//manager.add(action2);
	}

	private void fillContextMenu(IMenuManager manager) {
		//manager.add(action1);
		//manager.add(action2);
		// Other plug-ins can contribute there actions here
		//manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.getControl().setFocus();
	}
	
	private IProject[] getProjects(){
		return ResourcesPlugin.getWorkspace().getRoot().getProjects();
	}
	
}
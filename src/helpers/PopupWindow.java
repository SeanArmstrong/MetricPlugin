package helpers;

import java.util.List;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import codeAnalysis.ClassInfo;

public class PopupWindow extends TitleAreaDialog {

	private Text projectNameText;
	private Text emailText;
	private Text passwordText;

	private String projectName;
	private String email;
	private String password;
	
	private List<ClassInfo> klasses;

	public PopupWindow(Shell parentShell, List<ClassInfo> klasses) {
		super(parentShell);
		this.klasses = klasses;
	}

	@Override
	public void create() {
		super.create();
		setTitle("Upload Metrics");
		setMessage("Enter your Metric Manager Project name, email and password to send up your metric calculation", IMessageProvider.INFORMATION);
	    getButton(IDialogConstants.OK_ID).setText("Upload");
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(layout);

		createProjectName(container);
		createEmail(container);
		createPassword(container);
			
		return area;
	}
  

	private void createProjectName(Composite container) {
		Label lbtProjectName = new Label(container, SWT.NONE);
		lbtProjectName.setText("Project Name");

		GridData dataProjectName = new GridData();
		dataProjectName.grabExcessHorizontalSpace = true;
		dataProjectName.horizontalAlignment = GridData.FILL;

		projectNameText = new Text(container, SWT.BORDER);
		projectNameText.setLayoutData(dataProjectName);
	}
  
	private void createEmail(Composite container) {
		Label lbtEmail = new Label(container, SWT.NONE);
		lbtEmail.setText("Email");
    
		GridData dataEmail = new GridData();
		dataEmail.grabExcessHorizontalSpace = true;
		dataEmail.horizontalAlignment = GridData.FILL;
    
		emailText = new Text(container, SWT.BORDER);
		emailText.setLayoutData(dataEmail);
	}
  
	private void createPassword(Composite container) {
		Label lbtPassword = new Label(container, SWT.NONE);
	    lbtPassword.setText("Password");
	    
	    GridData dataPassword = new GridData();
	    dataPassword.grabExcessHorizontalSpace = true;
	    dataPassword.horizontalAlignment = GridData.FILL;
	    
	    passwordText = new Text(container, SWT.BORDER | SWT.PASSWORD);
	    passwordText.setLayoutData(dataPassword);
	}



	@Override
	protected boolean isResizable() {
		return true;
	}

	private void saveInput() {
		projectName = projectNameText.getText();
		email = emailText.getText();
		password = passwordText.getText();
	}

	@Override
	protected void okPressed() {
		saveInput();
		HTTPConnection http = new HTTPConnection();
		try {
			http.postMetrics(projectName, email, password, klasses);
		} catch (Exception e) {
			System.out.println("Could not send metrics");
		}
		super.okPressed();
	}

	public String getProjectName() {
		return projectName;
	}

	public String getEmail() {
		return email;
	}
	
	public String getPassword() {
		return password;
	}
} 
package helpers;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
 


// Will need to get Project NAME EMAIL AND PASSWORD FROM USER WHEN POSTING METRICS


import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import codeAnalysis.ClassInfo;
import codeAnalysis.MethodInfo;
 
public class HTTPConnection {
 
	private final String USER_AGENT = "Mozilla/5.0";
	private final String METRIC_URL = "https://metricmanager.herokuapp.com/api/";
	private final String VERSION = "v1";
 
	public boolean checkUser(String email, String password) throws Exception{
		String url = METRIC_URL + VERSION + "/users?email="+email+";password="+password;
		String result = sendGet(url);
		if(result.compareTo("true") == 0){
			System.out.println("USER EXISTS");
			return true;
		}
		System.out.println("USER DOES NOT EXISTS");

		return false;
	}
		
	public String getProjectByName(String name, String email, String password) throws Exception{
		String url = METRIC_URL + VERSION + "/getprojectbyname?email=" + email + ";password=" + password + ";name=" + name;
		String result = sendGet(url);
		return result;
	}

	public String getProjectGuid(String name, String email, String password) throws Exception{
		String url = METRIC_URL + VERSION + "/getprojectguid?email=" + email + ";password=" + password + ";name=" + name;
		return sendGet(url);
	}	
	
	public void createProject(String name, String email, String password) throws Exception{
		String url = METRIC_URL + VERSION + "/projects";
		String urlParameters = "email="+ name +";password=" + password + ";name="+name;
		String result = sendPost(url, urlParameters);
	}
	
	public String getKlassId(String methodName, String GUID, String email, String password) throws Exception{
		String url = METRIC_URL + VERSION + "/klasses?email=" + email + ";password=" + password + ";project_guid="+ GUID + ";name=" + methodName;
		String result = sendGet(url).replace("\"", "");
		return result;
	}
	
	public void postMetrics(String projectName, String email, String password, List<ClassInfo> klasses) throws Exception {
		if(checkUser(email, password)){
			String GUID = getProjectGuid(projectName, email, password).replace("\"", "");
			System.out.println(GUID);
			if(GUID.compareTo("null") == 0){
				System.out.println("Creating new project");
				createProject(projectName, email, password);
				GUID = getProjectGuid(projectName, email, password).replace("\"", "");
			}

			String uploadGUID = java.util.UUID.randomUUID().toString();
			
			
			for(ClassInfo klass : klasses){
				System.out.println(klass.name);
				String url = METRIC_URL + VERSION + "/klasses";
				System.out.println(klass);
				String urlParameters = "email=" + email + ";password=" + password + ";project_guid="+ GUID +
										";name="+klass.name+";package=" + klass.associatedPackage.replace(" ", "") + ";variables=" + klass.numberOfVariables +
										";public_variables=" + klass.numberOfPublicVariables + ";protected_variables=" + klass.numberOfProtectedVariables +
										";private_variables=" + klass.numberOfPrivateVariables + ";number_of_lines=" + klass.numberOfLines + 
										";number_of_methods=" + klass.numberOfMethods + ";average_lines_per_method=" + klass.averageLinesPerMethod + 
										";average_method_complexity=" + klass.averageMethodComplexity + ";weighted_methods_per_class=" + klass.weightedMethodsPerClass + 
										";depth_of_inheritance=" + klass.depthOfInheritance + ";lcom=" + klass.LCOM + ";cbo=" + klass.CBO + ";upload_guid=" + uploadGUID +
										";is_total=" + klass.isTotal;						
				sendPost(url, urlParameters);
		
				
				for(MethodInfo m : klass.methods){
					url = METRIC_URL + VERSION + "/methoods";
					
					urlParameters = "email=" + email + ";password=" + password + ";project_guid="+ GUID +
									";name=" + m.name + ";klass_id=" + getKlassId(klass.name, GUID, email, password) + 
									";number_of_lines=" + m.numberOfLines + ";complexity=" + m.complexity +
									";upload_guid=" + uploadGUID;
					
					sendPost(url, urlParameters);
				}
			}
		} else{
			System.out.println("User issues");
		}
	}
	
	private String sendPost(String url, String urlParameters) throws Exception{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();
 
		//add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
		 
		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(urlParameters);
		wr.flush();
		wr.close();
 
		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'POST' request to URL : " + url);
		System.out.println("Post parameters : " + urlParameters);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 
		//print result
		System.out.println(response.toString());
		
		return response.toString();
	}
	
	private String sendGet(String url) throws Exception{
		URL obj = new URL(url);
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");
		con.setRequestProperty("User-Agent", USER_AGENT);

		int responseCode = con.getResponseCode();
		System.out.println("\nSending 'GET' request to URL : " + url);
		System.out.println("Response Code : " + responseCode);
 
		BufferedReader in = new BufferedReader(
		        new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();
 
		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();
 		System.out.println(response.toString());
 		return response.toString();
	}
}
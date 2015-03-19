package codeAnalysis;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.*;

public class toJson {
	private List<ClassInfo> klasses;
	
	public toJson(List<ClassInfo> klasses){
		this.klasses = klasses;
	}
	
	public void convert(){
        GsonBuilder builder = new GsonBuilder();
        builder.setPrettyPrinting().serializeNulls();
        
        
        Gson gson = builder.create();
        
        MethodInfo m = new MethodInfo("METHOD", 1, null);
        
        System.out.println(gson.toJson(m));
	}
}

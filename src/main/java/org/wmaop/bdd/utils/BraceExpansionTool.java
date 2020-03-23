package org.wmaop.bdd.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BraceExpansionTool {
	
	public static List<String> expand(String inputString){
		if (inputString == null){ 
			return null;
		}
		
		List<String> newList = new ArrayList<String>();
		
		// Look to see if we have Curly Braces to expand
		if(inputString.matches(".+\\{.+\\}.+")){
			String[] splitInput = inputString.split("[\\{,\\}]");
			for(int i=1; i<splitInput.length-1;i++){
				newList.add(splitInput[0] + splitInput[i] + splitInput[splitInput.length-1]);
			}
		}
		else{
			newList = new ArrayList<String>(Arrays.asList(inputString.split(",")));
		}
		
		return newList;
	}

}

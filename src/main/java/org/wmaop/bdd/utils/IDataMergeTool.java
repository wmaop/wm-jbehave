package org.wmaop.bdd.utils;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;

public class IDataMergeTool {

	public static void mergeNestedTypes(IData override, IData inputPipeline){
		IDataCursor orCursor = override.getCursor();
		IDataCursor ipCursor = inputPipeline.getCursor();
		
		// Loop over override idata keys
		String currentKey = null;
		while(orCursor.hasMoreData()){
			orCursor.next();
			currentKey = orCursor.getKey();
			
			// Check if the overwrite value is an IData or IDataArray
			if(IDataUtil.getIDataArray(orCursor, currentKey) != null
					&& IDataUtil.getIDataArray(ipCursor, currentKey) != null){
				//If we have an IDataArray in the overwrite pipeline
				//and existing IDataArray in the input pipeline
				//Call a recursive merge of these two IDataArrays
				IData[] mergedArray = mergeNestedTypes(IDataUtil.getIDataArray(orCursor, currentKey), IDataUtil.getIDataArray(ipCursor,currentKey));
				IDataUtil.put(ipCursor, currentKey, mergedArray);
			}
			else if(IDataUtil.getIData(orCursor, currentKey) != null
					&& IDataUtil.getIData(ipCursor, currentKey) != null){
				//If we have an IData in the overwrite pipeline
				//and existing IData in the input pipeline
				//Call a recursive merge of these two IDatas
				mergeNestedTypes(IDataUtil.getIData(orCursor, currentKey), IDataUtil.getIData(ipCursor,currentKey));
				//Don't need to put the IData because the reference was already merged
			}
			else{
				//Copy the value to the inputPipeline
				IDataUtil.put(ipCursor, currentKey, orCursor.getValue());
			}				
		}
	}
	
	public static IData[] mergeNestedTypes(IData[] overrideArray, IData[] inputPipelineArray){
		//Find the larger size of the two input arrays
		//And create the result array of this size
		int largerSize = Math.max(overrideArray.length, inputPipelineArray.length);
		IData[] result = new IData[largerSize];
		
		//Loop through inputPipelineArray and copy over to result array
		for(int i=0; i<inputPipelineArray.length; i++){
			result[i] = inputPipelineArray[i];
		}
		//Loop over each overrideArray element
		//and call to merge the IData
		for(int i=0; i<overrideArray.length;i++){
			mergeNestedTypes(overrideArray[i],result[i]);
		}
		return result;
	}
	
}

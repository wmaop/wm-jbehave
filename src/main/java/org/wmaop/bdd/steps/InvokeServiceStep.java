package org.wmaop.bdd.steps;

import org.wmaop.bdd.utils.IDataMergeTool;

import com.wm.data.IData;
import com.wm.data.IDataFactory;

public class InvokeServiceStep extends BaseServiceStep {

	private final String serviceName;
	private final String idataClasspathFile;

	public InvokeServiceStep(String serviceName, String idataClasspathFile) {
		this.serviceName = serviceName;
		this.idataClasspathFile = idataClasspathFile;
	}

	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		IData inputPipeline;
		if(idataClasspathFile == null){
			inputPipeline = IDataFactory.create();
		}else{
			inputPipeline = this.idataFromClasspathResource(idataClasspathFile);
		}
		IData override = executionContext.getPipeline();
		if (override != null) {
			IDataMergeTool.mergeNestedTypes(override, inputPipeline);
		}
		IData pipeline = invokeService(executionContext, serviceName, inputPipeline);
		executionContext.setPipeline(pipeline);
	}
	
}

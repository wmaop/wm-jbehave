package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class InvokeServiceStep extends BaseServiceStep {

	private String serviceName;
	private String idataClasspathFile;

	public InvokeServiceStep(String serviceName, String idataClasspathFile) {
		this.serviceName = serviceName;
		this.idataClasspathFile = idataClasspathFile;
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		IData idata = idataClasspathFile == null ? IDataFactory.create() : idataFromClasspathResource(idataClasspathFile);
		if (executionContext.getPipeline() != null) {
			IDataUtil.merge(executionContext.getPipeline(), idata);
		}
		IData pipeline = invokeService(executionContext, serviceName, idata);
		executionContext.setPipeline(pipeline);
	}
}

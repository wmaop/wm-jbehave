package org.wmaop.bdd.steps;

import java.io.IOException;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.data.IDataFactory;
import com.wm.util.coder.IDataXMLCoder;

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
		invokeService(executionContext, serviceName, idata);
	}
}

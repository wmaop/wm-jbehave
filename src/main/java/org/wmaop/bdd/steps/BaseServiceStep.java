package org.wmaop.bdd.steps;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.util.coder.IDataXMLCoder;
import com.wm.util.coder.InvalidDatatypeException;

public abstract class BaseServiceStep {

	static final String ADVICE_ID = "adviceId";
	static final String RESPONSE = "response";
	static final String INTERCEPT_POINT = "interceptPoint";
	static final String SERVICE_NAME = "serviceName";
	static final String CONDITION = "condition";
	
	static final String FIXED_RESPONSE_MOCK = "org.wmaop.define.fixedResponse:registerFixedResponseMock";
	static final String SETUP_ASSERTION = "org.wmaop.define.assertion:registerAssertion";
	static final String ASSERTION_INVOKE_COUNT = "org.wmaop.define.assertion:getInvokeCount";
	
	protected class ServiceSplit {
		String packageName;
		String serviceName;
		ServiceSplit(String p, String s) {
			packageName = p;
			serviceName = s;
		}
	}
	
	protected IData invokeService(ExecutionContext executionContext, String serviceName, IData idata) throws ServiceException {
		ServiceSplit svc = splitQualifiedServiceName(serviceName);
		return executionContext.getConnectionContext().invoke(svc.packageName, svc.serviceName, idata);

	}
	
	protected ServiceSplit splitQualifiedServiceName(String svc) {
		int colonPos = svc.lastIndexOf(':');
		return new ServiceSplit(svc.substring(0, colonPos), svc.substring(colonPos+1));
	}

	protected InputStream streamFromClasspathResource(String fileName) {
		try {
			return this.getClass().getClassLoader().getResourceAsStream(fileName);
		} catch (Exception e) {
			throw new RuntimeException("Unable to load file '" + fileName + "' from the classpath.  Cause is: " + e.getMessage());
		}
	}
	
	protected IData idataFromClasspathResource(String fileName) throws Exception {
		return new IDataXMLCoder().decode(streamFromClasspathResource(fileName));
	}

	protected String stringFromClasspathResource(String fileName) {
		try {
			return IOUtils.toString(streamFromClasspathResource(fileName));
		} catch (Exception e) {
			throw new RuntimeException("Unable to load " + fileName + " from the classpath");
		}
	}
	
	abstract void execute(ExecutionContext executionContext) throws Exception;
}

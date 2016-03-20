package org.wmaop.bdd.steps;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.io.PushbackReader;

import org.apache.commons.io.IOUtils;

import com.wm.app.b2b.client.ServiceException;
import com.wm.app.b2b.services.DocumentToRecordService;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
import com.wm.lang.xml.Document;
import com.wm.util.Values;
import com.wm.util.coder.IDataBinCoder;
import com.wm.util.coder.IDataXMLCoder;
import com.wm.util.coder.XMLCoder;
import com.wm.util.coder.XMLCoderWrapper;

public abstract class BaseServiceStep {

	static final String ADVICE_ID = "adviceId";
	static final String RESPONSE = "response";
	static final String INTERCEPT_POINT = "interceptPoint";
	static final String SERVICE_NAME = "serviceName";
	static final String CONDITION = "condition";
	static final String EXCEPTION = "exception";
	
	static final String FIXED_RESPONSE_MOCK = "org.wmaop.define.fixedResponse:registerFixedResponseMock";
	static final String SETUP_ASSERTION = "org.wmaop.define.assertion:registerAssertion";
	static final String ASSERTION_INVOKE_COUNT = "org.wmaop.define.assertion:getInvokeCount";
	static final String REGISTER_EXCEPTION = "org.wmaop.define.exception:registerExceptionMock";
	
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
		ServiceSplit split;
		if (colonPos == -1) {
			split = new ServiceSplit(null, svc);
		} else {
			split = new ServiceSplit(svc.substring(0, colonPos), svc.substring(colonPos+1));
		}
		return split;
	}

	protected InputStream streamFromClasspathResource(String fileName) {
		try {
			InputStream is = this.getClass().getClassLoader().getResourceAsStream(fileName);
			if (is == null) {
				throw new RuntimeException("Unable to load file '" + fileName + "' from the classpath.");
			}
			return is;
		} catch (Exception e) {
			throw new RuntimeException("Unable to load file '" + fileName + "' from the classpath.  Cause is: " + e.getMessage());
		}
	}
	
	protected IData idataFromClasspathResource(String fileName) throws Exception {
		InputStreamReader isr = new InputStreamReader(streamFromClasspathResource(fileName));
		char [] buf = new char[100];
		int len = isr.read(buf);
		boolean isIData = new String(buf,0,len).contains("IData");
		if (isIData) {
			return new IDataXMLCoder().decode(streamFromClasspathResource(fileName));
		} else {
			Document node = new com.wm.lang.xml.Document(streamFromClasspathResource(fileName), null, "UTF-8", false, null, true);
			Values in = new Values();
			DocumentToRecordService dtrs = new DocumentToRecordService(in, false);
			dtrs.setIsXTD(true);
			IData idata = (IData) dtrs.bind(node);
			IDataCursor cursor = idata.getCursor();
			IDataUtil.remove(cursor , "@version"); // Inserted during conversion
			cursor.destroy();
			return idata;
		}
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

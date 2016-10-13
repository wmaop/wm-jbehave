package org.wmaop.bdd.steps;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.wm.app.b2b.client.ServiceException;
import com.wm.app.b2b.services.DocumentToRecordService;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
import com.wm.lang.xml.Document;
import com.wm.util.Values;
import com.wm.util.coder.IDataXMLCoder;
import static org.junit.Assert.*;

public abstract class BaseServiceStep {

	protected static final String ADVICE_ID = "adviceId";
	protected static final String RESPONSE = "response";
	protected static final String INTERCEPT_POINT = "interceptPoint";
	protected static final String SERVICE_NAME = "serviceName";
	protected static final String CONDITION = "condition";
	protected static final String EXCEPTION = "exception";
	protected static final String SCOPE = "scope";
	protected static final String USER = "username";
	protected static final String CALLED_BY = "calledBy";
	
	protected static final String FIXED_RESPONSE_MOCK = "org.wmaop.define.fixedResponse:registerFixedResponseMock";
	protected static final String SETUP_ASSERTION = "org.wmaop.define.assertion:registerAssertion";
	protected static final String ASSERTION_INVOKE_COUNT = "org.wmaop.define.assertion:getInvokeCount";
	protected static final String REGISTER_EXCEPTION = "org.wmaop.define.exception:registerExceptionMock";
	
	protected class ServiceSplit {
		protected String packageName;
		protected String serviceName;
		protected ServiceSplit(String p, String s) {
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
			throw new StepException("Unable to load file '" + fileName + "' from the classpath.  Cause is: " + e.getMessage());
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

	protected List<String> listFromClasspathResources(List<String> fileNames) {
		List<String> contents = new ArrayList<>(fileNames.size());
		String filePathSuffix = "";
		for (String fileName : fileNames) {
			int slashPos = fileName.lastIndexOf('/'); // Check for path
			if (slashPos != -1) { // Keep path for future use
				filePathSuffix = fileName.substring(0, slashPos);
				contents.add(stringFromClasspathResource(fileName));
			} else if (filePathSuffix.length() > 0) { // Prepend path if defined
				contents.add(stringFromClasspathResource(filePathSuffix + '/' + fileName));
			} else {
				contents.add(stringFromClasspathResource(fileName));
			}
		}
		return contents;
	}

	protected String stringFromClasspathResource(String fileName) {
		try {
			return IOUtils.toString(streamFromClasspathResource(fileName));
		} catch (Exception e) {
			throw new StepException("Unable to load " + fileName + " from the classpath");
		}
	}
	
	protected InterceptPoint toInteceptPoint(String interceptPoint) {
		try {
			return InterceptPoint.valueOf(interceptPoint.toLowerCase());
		} catch (IllegalArgumentException ie) {
			fail("Invalid intercept point '" + interceptPoint + "' valid options are before, invoke or after");
			return null; // Only present to satisfy compilation.  Never reached
		}
	}
	
	abstract protected void execute(ExecutionContext executionContext) throws Exception;
	

	protected final void putNonNull(IDataCursor cursor, String key, Object value) {
		if (key != null) {
			IDataUtil.put(cursor, key, value);
		}
	}
	
}

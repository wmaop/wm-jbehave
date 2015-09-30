package uk.co.sysgen.webmethods.testing.steps;

import java.io.IOException;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.BasicData;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataUtil;
import com.wm.util.coder.IDataXMLCoder;

public class MockServiceStep extends BaseServiceStep {


	public MockServiceStep(ExecutionContext executionContext) {
		super(executionContext);
	}

	public void mockService(String serviceName, String idataFile)
			throws IOException, ServiceException {

		IData idata = new IDataXMLCoder().decodeFromBytes(idataFile.getBytes());
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "serviceName", serviceName);
		IDataUtil.put(cursor, "idataReturn", loadIDataFromClasspath(idataFile));
		
		cursor.destroy();
		
		invokeService("catlin.mock.mocks:addFixedResponseMock", idata);
	}

	public void mockService(String serviceName, String idataFile,
			String jexlExpression) throws IOException, ServiceException {

		IData idata = new BasicData();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "serviceName", serviceName);
		IDataUtil.put(cursor, "jexlExpression", jexlExpression);
		IDataUtil.put(cursor, "idataReturn", loadIDataFromClasspath(idataFile));
		
		cursor.destroy();
		
		invokeService("catlin.mock.mocks:addJexlResponseMock", idata);
	}

}

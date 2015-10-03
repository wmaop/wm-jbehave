package org.wmaop.bdd.steps;

import java.io.IOException;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.util.coder.IDataXMLCoder;

public class MockServiceStep extends BaseServiceStep {

	private static final String FIXED_RESPONSE_MOCK = "org.wmaop.define.fixedResponse:addFixedResponseMock";
	private final IData idata;
	private final String execService;

	public MockServiceStep(String serviceName, String idataFile) throws IOException, ServiceException {

		idata = new IDataXMLCoder().decodeFromBytes(idataFile.getBytes());
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "serviceName", serviceName);
		IDataUtil.put(cursor, "idataReturn", loadIDataFromClasspath(idataFile));
		cursor.destroy();
		execService = FIXED_RESPONSE_MOCK;
	}

	public MockServiceStep(String serviceName, String idataFile,
			String jexlExpression) throws IOException, ServiceException {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "serviceName", serviceName);
		IDataUtil.put(cursor, "jexlExpression", jexlExpression);
		IDataUtil.put(cursor, "idataReturn", loadIDataFromClasspath(idataFile));
		
		cursor.destroy();
		
		execService = "catlin.mock.mocks:addJexlResponseMock";
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, execService, idata);
	}
}
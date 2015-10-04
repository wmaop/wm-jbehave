package org.wmaop.bdd.steps;

import java.io.IOException;

import com.wm.app.b2b.client.ServiceException;
import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;
import com.wm.util.coder.IDataXMLCoder;

public class MockServiceStep extends BaseServiceStep {

	private static final String FIXED_RESPONSE_MOCK = "org.wmaop.define.fixedResponse:registerFixedResponseMock";
	private final IData idata;
	private final String execService;

	public MockServiceStep(String adviceId, String interceptPoint, String serviceName, String idataFile) throws Exception {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "adviceId", adviceId);
		IDataUtil.put(cursor, "serviceName", serviceName);
		IDataUtil.put(cursor, "response", stringFromClasspathResource(idataFile));
		cursor.destroy();
		execService = FIXED_RESPONSE_MOCK;
	}

	public MockServiceStep(String adviceId, String interceptPoint, String serviceName, String idataFile,
			String jexlExpression) throws Exception {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, "adviceId", adviceId);
		IDataUtil.put(cursor, "serviceName", serviceName);
		IDataUtil.put(cursor, "condition", jexlExpression);
		IDataUtil.put(cursor, "response", stringFromClasspathResource(idataFile));
		
		cursor.destroy();
		
		execService = "catlin.mock.mocks:addJexlResponseMock";
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, execService, idata);
	}
}
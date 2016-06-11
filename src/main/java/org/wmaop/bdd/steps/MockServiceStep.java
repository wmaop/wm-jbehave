package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class MockServiceStep extends BaseServiceStep {

	private final IData idata;

	public MockServiceStep(String adviceId, InterceptPoint interceptPoint, String serviceName, String idataFile) {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, adviceId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		IDataUtil.put(cursor, RESPONSE, stringFromClasspathResource(idataFile));
		IDataUtil.put(cursor, SCOPE, "session");
		cursor.destroy();
	}

	public MockServiceStep(String adviceId, InterceptPoint interceptPoint, String serviceName, String idataFile,
			String jexlExpression) {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, adviceId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		IDataUtil.put(cursor, CONDITION, jexlExpression);
		IDataUtil.put(cursor, RESPONSE, stringFromClasspathResource(idataFile));
		IDataUtil.put(cursor, SCOPE, "session");
		
		cursor.destroy();
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, FIXED_RESPONSE_MOCK, idata);
	}
}
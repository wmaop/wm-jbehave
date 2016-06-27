package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;

public class MockServiceStep extends BaseServiceStep {

	private final IData idata;

	public MockServiceStep(String adviceId, InterceptPoint interceptPoint, String serviceName, String idataFile,
			String jexlExpression, String pkgService) {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		putNonNull(cursor, ADVICE_ID, adviceId);
		putNonNull(cursor, SERVICE_NAME, serviceName);
		putNonNull(cursor, INTERCEPT_POINT, interceptPoint.toString());
		putNonNull(cursor, CONDITION, jexlExpression);
		putNonNull(cursor, RESPONSE, stringFromClasspathResource(idataFile));
		putNonNull(cursor, SCOPE, "session");
		putNonNull(cursor, CALLED_BY, pkgService);
		
		cursor.destroy();
	}
	
	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, FIXED_RESPONSE_MOCK, idata);
	}
}
package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class AssertionSetupStep extends BaseServiceStep {

	private final IData idata = IDataFactory.create();
	
	public AssertionSetupStep(String assertionId, InterceptPoint interceptPoint, String serviceName,
			String jexlPipelineExpression) {
		setup(assertionId, interceptPoint, serviceName, jexlPipelineExpression);
	}

	public AssertionSetupStep(String assertionId, String interceptPoint, String serviceName) {
		setup(assertionId, toInteceptPoint(interceptPoint), serviceName, null);
	}
	
	private void setup(String assertionId, InterceptPoint interceptPoint, String serviceName,
			String jexlPipelineExpression) {
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, assertionId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		if (jexlPipelineExpression != null) {
		IDataUtil.put(cursor, CONDITION, jexlPipelineExpression);
		}
		IDataUtil.put(cursor, SCOPE, "session"); 
		cursor.destroy();
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, SETUP_ASSERTION, idata);  // Ignore returned idata as its not part of the flow
	}

}

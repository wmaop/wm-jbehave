package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;

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
		putNonNull(cursor, ADVICE_ID, assertionId);
		putNonNull(cursor, SERVICE_NAME, serviceName);
		putNonNull(cursor, INTERCEPT_POINT, interceptPoint.toString());
		putNonNull(cursor, CONDITION, jexlPipelineExpression);
		putNonNull(cursor, SCOPE, "session"); 
		cursor.destroy();
	}

	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, SETUP_ASSERTION, idata);  // Ignore returned idata as its not part of the flow
	}

}

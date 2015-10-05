package org.wmaop.bdd.steps;

import org.wmaop.bdd.jbehave.InterceptPoint;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class AssertionSetupStep extends BaseServiceStep {

	private final String execService;
	private final IData idata;
	
	public AssertionSetupStep(String assertionId, InterceptPoint interceptPoint, String serviceName,
			String jexlPipelineExpression) {
		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, assertionId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		IDataUtil.put(cursor, CONDITION, jexlPipelineExpression);
		cursor.destroy();
		execService = SETUP_ASSERTION;
	}

	public AssertionSetupStep(String assertionId, String interceptPoint, String serviceName) {
		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, assertionId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		cursor.destroy();
		execService = SETUP_ASSERTION;
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, execService, idata);
	}

}

package org.wmaop.bdd.steps;

import org.wmaop.bdd.jbehave.InterceptPoint;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class ExceptionStep extends BaseServiceStep {

	private final IData idata;
	private final String execService;

	public ExceptionStep(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlExpression, String exception) {

		idata = IDataFactory.create();
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, adviceId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		IDataUtil.put(cursor, CONDITION, jexlExpression);
		IDataUtil.put(cursor, EXCEPTION, exception);
		cursor.destroy();

		execService = REGISTER_EXCEPTION;
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, execService, idata);

	}

}

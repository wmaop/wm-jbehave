package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class ExceptionStep extends BaseServiceStep {

	private final IData idata = IDataFactory.create();

	public ExceptionStep(String adviceId, String interceptPoint, String serviceName, String jexlExpression, String exception) {
		setup(adviceId,  toInteceptPoint(interceptPoint), serviceName, jexlExpression, exception);
	}
	
	public ExceptionStep(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlExpression, String exception) {
		setup(adviceId,  interceptPoint, serviceName, jexlExpression, exception);
	}
	
	private void setup(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlExpression, String exception) {
		IDataCursor cursor = idata.getCursor();
		IDataUtil.put(cursor, ADVICE_ID, adviceId);
		IDataUtil.put(cursor, SERVICE_NAME, serviceName);
		IDataUtil.put(cursor, INTERCEPT_POINT, interceptPoint.toString());
		if (jexlExpression != null) {
			IDataUtil.put(cursor, CONDITION, jexlExpression);
		}
		IDataUtil.put(cursor, EXCEPTION, exception);
		IDataUtil.put(cursor, SCOPE, "session");
		cursor.destroy();
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, REGISTER_EXCEPTION, idata);

	}

}

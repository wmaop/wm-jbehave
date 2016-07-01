package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;

public class ExceptionStep extends BaseServiceStep {

	private final IData idata = IDataFactory.create();

	public ExceptionStep(String adviceId, String interceptPoint, String serviceName, String jexlExpression, String exception, String pkgService) {
		setup(adviceId,  toInteceptPoint(interceptPoint), serviceName, jexlExpression, exception, pkgService);
	}
	
	public ExceptionStep(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlExpression, String exception, String pkgService) {
		setup(adviceId,  interceptPoint, serviceName, jexlExpression, exception, pkgService);
	}
	
	private void setup(String adviceId, InterceptPoint interceptPoint, String serviceName, String jexlExpression, String exception, String pkgService) {
		IDataCursor cursor = idata.getCursor();
		putNonNull(cursor, ADVICE_ID, adviceId);
		putNonNull(cursor, SERVICE_NAME, serviceName);
		putNonNull(cursor, INTERCEPT_POINT, interceptPoint.toString());
		putNonNull(cursor, CONDITION, jexlExpression);
		putNonNull(cursor, EXCEPTION, exception);
		putNonNull(cursor, SCOPE, "session");
		putNonNull(cursor, CALLED_BY, pkgService);
		cursor.destroy();
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, REGISTER_EXCEPTION, idata);

	}

}

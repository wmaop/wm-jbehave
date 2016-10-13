package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class TeardownStep extends BaseServiceStep {

	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		
		IData idata = IDataFactory.create();
		IDataCursor idc = idata.getCursor();
		IDataUtil.put(idc, "scope", "all");
		idc.destroy();
		invokeService(executionContext, "org.wmaop.reset:resetAdviceAndDisable", idata );
	}

}

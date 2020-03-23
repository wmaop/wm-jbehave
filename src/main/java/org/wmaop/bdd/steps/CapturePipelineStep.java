package org.wmaop.bdd.steps;

import com.wm.data.IData;
import com.wm.data.IDataCursor;
import com.wm.data.IDataFactory;
import com.wm.data.IDataUtil;

public class CapturePipelineStep extends BaseServiceStep {

	private final IData idata = IDataFactory.create();

	public CapturePipelineStep(String adviceId, String interceptPoint, String serviceName, String pipelineFileName,
			String condition){
		setup(adviceId, toInteceptPoint(interceptPoint), serviceName, pipelineFileName, condition);
	}
	
	public void setup(String adviceId, InterceptPoint interceptPoint, String serviceName, String pipelineFileName,
			String condition) {
		IDataCursor cursor = idata.getCursor();
		
		String scenarioAsString = 
			"<?xml version=\"1.0\"?>"
			+ "<scenario id=\"" + adviceId + "\">"
				+ "<scope>"
					+ "<session/>"
				+ "</scope>"
				+ "<given>"
					+ "<service intercepted=\"" + interceptPoint.toString() + "\">" + serviceName + "</service>"
				+ "</given>"
				+ (condition == null ? "<when>" : "<when condition=\'" + condition + "\'>")
					+ "<then>"
						+ "<pipelineCapture>" + pipelineFileName + "</pipelineCapture>"
					+ "</then>"
				+ "</when>"
			+ "</scenario>";
		System.out.println(scenarioAsString);
		IDataUtil.put(cursor, "scenarioAsString", scenarioAsString);
		cursor.destroy();
		
	}
	
	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		invokeService(executionContext, REGISTER_SCENARIO, idata);
	}
}
package org.wmaop.bdd.steps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl3.JexlExpression;
import org.wmaop.bdd.utils.IDataMergeTool;
import org.wmaop.util.jexl.IDataJexlContext;
import org.wmaop.util.jexl.JexlExpressionFactory;

import com.wm.data.IData;
import com.wm.data.IDataFactory;

public class PipelineVariableStep extends BaseServiceStep {

	private final List<JexlExpression> expressions;

	public PipelineVariableStep(String jexlVariableExpression) {
		expressions = new ArrayList<>();
		for (String expr : jexlVariableExpression.split(";")) {
			expressions.add(JexlExpressionFactory.createExpression(expr));
		}
	}

	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		IData idata = executionContext.getPipeline();
		for (JexlExpression expression : expressions) {
			IData expressionIData = IDataFactory.create();
			expression.evaluate(new IDataJexlContext(expressionIData));
			IDataMergeTool.mergeNestedTypes(expressionIData, idata);
		}
		executionContext.setPipeline(idata);
	}

}

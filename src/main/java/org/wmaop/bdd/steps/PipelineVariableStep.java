package org.wmaop.bdd.steps;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jexl2.Expression;
import org.wmaop.util.jexl.IDataJexlContext;
import org.wmaop.util.jexl.JexlExpressionFactory;

import com.wm.data.IData;
import com.wm.data.IDataFactory;

public class PipelineVariableStep extends BaseServiceStep {

	private List<Expression> expressions;

	public PipelineVariableStep(String jexlVariableExpression) {
		expressions = new ArrayList<Expression>();
		for (String expr : jexlVariableExpression.split(";")) {
			expressions.add(JexlExpressionFactory.getEngine().createExpression(expr));
		}
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		IData idata = executionContext.getPipeline();
		for (Expression expression : expressions) {
			expression.evaluate(new IDataJexlContext(idata));
		}
		executionContext.setPipeline(idata);
	}

}

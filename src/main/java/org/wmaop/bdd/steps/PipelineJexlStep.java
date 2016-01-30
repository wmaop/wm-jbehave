package org.wmaop.bdd.steps;

import org.apache.commons.jexl2.Expression;
import org.wmaop.util.jexl.IDataJexlContext;
import org.wmaop.util.jexl.JexlExpressionFactory;

import static org.junit.Assert.*;

public class PipelineJexlStep extends BaseServiceStep {

	private Expression expression;

	public PipelineJexlStep(String jexlExpression) {
		expression = JexlExpressionFactory.createExpression(jexlExpression);
	}

	@Override
	void execute(ExecutionContext executionContext) throws Exception {
		boolean result = (Boolean) expression.evaluate(new IDataJexlContext(executionContext.getPipeline()));
		if (!result) {
			fail("The expression [" + expression.getExpression() + "] returned false");
		}
	}

}

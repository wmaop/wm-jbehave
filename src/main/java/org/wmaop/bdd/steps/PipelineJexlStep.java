package org.wmaop.bdd.steps;

import org.apache.commons.jexl3.JexlExpression;
import org.wmaop.util.jexl.IDataJexlContext;
import org.wmaop.util.jexl.JexlExpressionFactory;

import static org.junit.Assert.*;

public class PipelineJexlStep extends BaseServiceStep {

	private final JexlExpression expression;

	public PipelineJexlStep(String jexlExpression) {
		expression = JexlExpressionFactory.createExpression(jexlExpression);
	}

	@Override
	protected void execute(ExecutionContext executionContext) throws Exception {
		boolean result;
		try {
			result = (Boolean) expression.evaluate(new IDataJexlContext(executionContext.getPipeline()));
			if (!result) {
				fail("The expression [" + expression.getSourceText() + "] returned false");
			}
		} catch (Exception e) {
			fail("parsing the expression '"+expression+"' failed");
			e.printStackTrace(); // Output in Eclipse console
		}
	}

}

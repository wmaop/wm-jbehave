package org.wmaop.bdd.jbehave;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.Alias;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.wmaop.bdd.steps.BddTestBuilder;
import org.wmaop.bdd.steps.InterceptPoint;
import org.wmaop.bdd.steps.ThreadContext;

public class WmJBehaveSteps  {

		@BeforeScenario
		public void setup() throws Exception {
			try {
				ThreadContext.get().teardown();
			} catch (Exception e) {
				e.printStackTrace(); // Ensures output in Eclipse console
				throw e;
			}
		}
		
		@AfterScenario
		public void teardown() throws Exception {
			try {
				ThreadContext.get().verify();
				ThreadContext.get().teardown();
			} catch (Exception e) {
				e.printStackTrace(); // Ensures output in Eclipse console
				throw e;
			}
		}
		
		@Given("pipeline values $jexlVariableExpression")
		public void pipeline_values(String jexlVariableExpression) {
			ThreadContext.get().withVariableExpression(jexlVariableExpression);
		}

		@Given("mock $serviceName always returning $idataFile")
		public void mock_service_always_returning(String serviceName, String idataFile) {
			ThreadContext.get().withMockService(serviceName, InterceptPoint.invoke, serviceName, idataFile, null, null);
		}
		
		
		@Given("mock $serviceName returning $idataFile when $jexlPipelineExpression")
		public void mock_service_returning_when(String serviceName, String idataFile, String jexlPipelineExpression) throws Throwable {
			BddTestBuilder bddt = ThreadContext.get();
			bddt.withMockService(serviceName + "-" + bddt.getExecutedStep(), InterceptPoint.invoke, serviceName, idataFile, jexlPipelineExpression, null);
		}

		
		@Given("$assertionId assertion $interceptPoint service $serviceName when $jexlPipelineExpression")
		public void assertion_service_when(String assertionId, InterceptPoint interceptPoint, String serviceName, String expression) {
			ThreadContext.get().withAssertion(assertionId, interceptPoint, serviceName, expression);
		}
		
		@Given("$assertionId assertion $interceptPoint service $serviceName always")
		public void assertion_service(String assertionId, String interceptPoint, String serviceName) {
			ThreadContext.get().withAssertion(assertionId, interceptPoint, serviceName);
		}

		@Given("exception $exception thrown calling service $serviceName always")
		public void exception_thrown_calling_service(String exception, String serviceName) {
			ThreadContext.get().withException(serviceName, InterceptPoint.invoke, serviceName, null, exception, null);
		}

		@Given("exception $exception thrown calling service $serviceName when $jexlPipelineExpression")
		public void exception_thrown_calling_service(String exception, String serviceName, String expression) {
			BddTestBuilder bddt = ThreadContext.get();
			bddt.withException(serviceName+ "-" + bddt.getExecutedStep(), InterceptPoint.invoke, serviceName, expression, exception, null);
		}

		@Given("exception $exception thrown $interceptPoint calling service $serviceName when $jexlPipelineExpression")
		public void exception_thrown_calling_service_with_expression(String exception, String interceptPoint, String serviceName, String expression) {
			BddTestBuilder bddt = ThreadContext.get();
			bddt.withException(serviceName+ "-" + bddt.getExecutedStep(), interceptPoint, serviceName, expression, exception, null);
		}
	
		@Given("exception $exception thrown $interceptPoint calling service $serviceName always")
		public void exception_thrown_at_intercept_point_calling_service(String exception, String interceptPoint, String serviceName) {
			BddTestBuilder bddt = ThreadContext.get();
			bddt.withException(serviceName+ "-" + bddt.getExecutedStep(), interceptPoint, serviceName, null, exception, null);
		}

		/*
		 * When
		 */
		
		@When("invoke $serviceName with $idataFile")
		public void invoke_service(final String serviceName, String idataFile) {
			ThreadContext.get().withInvokeService(serviceName, idataFile);
		}
		
		@When("invoke $serviceName without idata")
		public void invoke_service(final String serviceName) {
			ThreadContext.get().withInvokeService(serviceName, null);
		}
		
		/*
		 * Then
		 */
		
		@Then("assertion $assertionId was invoked $invokeCount times")
		@Alias("mock $assertionId was invoked $invokeCount times")
		public void assertion_was_invoked_times(String assertionId, int invokeCount) throws Throwable {
			ThreadContext.get().withAssertionInvokeCount(assertionId, invokeCount);
		}
		
		@Then("pipeline has $jexlPipelineExpression")
		public void pipeline_has_expression(String jexlExpression) throws Throwable {
			ThreadContext.get().withPipelineExpression(jexlExpression);
		}
		
		@Then("pipeline document $document matches $idataFile")
		public void pipeline_matches(String document, String idataFile) throws Throwable {
			ThreadContext.get().withMatchesDocument(document, idataFile);
		}
		
		@Then("exception $exception was thrown")
		public void exception_was_thrown(String exceptionName) {
			ThreadContext.get().withExceptionVerify(exceptionName);
		}

		/*
		 * Utility 
		 */

		@Then("show pipeline in console")
		public void showPipeline() {
			ThreadContext.get().showPipeline();
		}

}

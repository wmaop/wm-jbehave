package uk.co.sysgen.webmethods.testing.jbehave;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import uk.co.sysgen.webmethods.testing.steps.ExecutionContext;
import uk.co.sysgen.webmethods.testing.steps.BddTestBuilder;

import com.wm.app.b2b.client.Context;
import com.wm.app.b2b.client.ServiceException;

public class WmJBehaveSteps {

		private static final String EMPTY_IDATA = "<IDataXMLCoder version=\"1.0\"></IDataXMLCoder>";
		private BddTestBuilder testBuilder;
		
		@BeforeScenario
		public void init() throws IOException, ServiceException {
			testBuilder = new BddTestBuilder(new ExecutionContext("src/test/resources/jfeature/feature.properties"));
		}
		
		@When("invoke $serviceName with $idataFile")
		public void invoke_service(final String serviceName, String idataFile) throws ServiceException, IOException {
			System.out.println("Invoking " +serviceName + " with idata " +idataFile);
			testBuilder.withInvokeService(serviceName, idataFile);
		}
		

		@Given("invoke $serviceName")
		public void invoke_service(final String serviceName) throws IOException, ServiceException {
			System.out.println("Invoking " +serviceName);
			testBuilder.withInvokeService(serviceName, EMPTY_IDATA);
		}

		@Given("mock $serviceName always returning $idataFile")
		public void mock_service_always_returning(String serviceName, String idataFile) throws IOException, ServiceException {
			System.out.println("Creating mock for " + serviceName
					+ " with canned data " + idataFile);
			testBuilder.wirhMockService(serviceName, idataFile);
		}

		
		@Given("mock $serviceName returning $idataFile when $jexlPipelineExpression")
		public void mock_service_returning_when(String serviceName, String idataFile, String jexlPipelineExpression)
				throws Throwable {
			System.out.println(serviceName + " called with "+idataFile+" when " + jexlPipelineExpression);
			testBuilder.wirhMockService(serviceName, idataFile, jexlPipelineExpression);
		}
		
		@Given("$assertionId assertion $invokePosition service $service when $expression")
		public void assertion_service_when(String assertionId, String invokePosition, String serviceName, String expression) {
                           			
		}
		@Then("assertion $assertionId was invoked $invokeCount times")
		public void assertion_was_invoked_times(String assertionId, String invokeCount) throws Throwable {
			
		}
		
		@Then("service $serviceName was invoked $invokeCount times")
		public void service_was_invoked_times(String serviceName, String invokeCount) throws Throwable {
			System.out.println("verifying service" +serviceName+" was invoked "+invokeCount );
			// Register assertion
		}

		@Then("pipeline has $jexlExpression")
		public void pipeline_has_foo_data(String jexlExpression) throws Throwable {
			System.out.println("verifying jexl expression on idata " + jexlExpression);
		}
		
		@Then("exception $exception was thrown")
		public void exception_was_thrown(String exceptionName) {
			System.out.println("Exception was thrown");
		}
}

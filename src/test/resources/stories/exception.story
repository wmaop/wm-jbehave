Mock Story

Narrative:
In order to simulate service behaviour
As a test framework
I want to raise and check for exceptions
					 
Scenario:  Throw an exception and check for it
Given exception java.lang.RuntimeException thrown calling service org.wmaop.test.services:svcB always
When invoke org.wmaop.test.services:rootSvc without idata
Then exception java.lang.RuntimeException was thrown

 
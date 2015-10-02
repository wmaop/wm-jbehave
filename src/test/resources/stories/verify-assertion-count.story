Verify invocation count

Narrative:
In order to verify that service have been called
As a test framework
I want to count service invocations
					 
Scenario:  Check for atLeastOnce
#Given a precondition  
					 
Scenario:  Check for times count
#Given a precondition  

Scenario:  Check for service never called
#Given a precondition  

Scenario:  Check for atMost
#Given a precondition  

Scenario:  Check for atLeast 
#Given a precondition  

Scenario:  Check for service never called
#Given a precondition  

Scenario:  Check for check invocation on non-mocked service
#Given a precondition  

Scenario: assertion with no precondition
Given PreSvcBAssertion assertion before service pub.test:svcB always
When invoke pub.test:rootSvc with data/applepear.xml
Then assertion PreSvcBAssertion was invoked 1 times
And pipeline has varA == "A"

Scenario: assertion with pipeline variable precondition
Given PreSvcBAssertion assertion before service pub.test:svcB when varA == "B"
When invoke pub.test:rootSvc with data/applepear.xml
Then assertion PreSvcBAssertion was invoked 1 times


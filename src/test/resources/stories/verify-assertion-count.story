Verify invocation count

Narrative:
In order to verify that service have been called
As a test framework
I want to count service invocations
					 
!--Scenario:  Check for atLeastOnce
!--Given a precondition  
					 
!--Scenario:  Check for times count
!--Given a precondition  

!--Scenario:  Check for atMost
!--Given a precondition  

!--Scenario:  Check for atLeast 
!--Given a precondition  

Scenario:  Check for service never called
Given PreSvcBAssertion assertion before service org.wmaop.test.services:svcNotExist always
When invoke org.wmaop.test.services:rootSvc with data/applepear.xml
Then assertion PreSvcBAssertion was invoked 0 times

Scenario:  Check for invocation on mocked service
Given mock org.wmaop.test.services:svcA always returning data/applepear.xml
When invoke org.wmaop.test.services:rootSvc with data/empty.xml
Then pipeline has varA == null && apple == "alpha"
And mock org.wmaop.test.services:svcA was invoked 1 times

Scenario: assertion with pipeline variable precondition not matching
Given PreSvcBAssertion assertion before service org.wmaop.test.services:svcB when varA == "B"
When invoke org.wmaop.test.services:rootSvc with data/applepear.xml
Then assertion PreSvcBAssertion was invoked 0 times
And pipeline has apple == "alpha"

Scenario: assertion with pipeline variable precondition
Given PreSvcBAssertion assertion before service org.wmaop.test.services:svcB when varA == "A"
When invoke org.wmaop.test.services:rootSvc with data/applepear.xml
Then assertion PreSvcBAssertion was invoked 1 times
And pipeline has varA != "B"
And pipeline has apple == "alpha"

Scenario: assertion with no precondition
Given PreSvcBAssertion assertion before service org.wmaop.test.services:svcB always
When invoke org.wmaop.test.services:rootSvc with data/applepear.xml
Then assertion PreSvcBAssertion was invoked 1 times
And pipeline has varA == "A"

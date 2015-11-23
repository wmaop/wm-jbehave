Mock Story

Narrative:
In order to simulate service behaviour
As a test framework
I want to mock services
					
Scenario:  Invoke a fixed mock with empty pipeline. Verify pipline doesnt have variable from replace service and contains mock value
Given mock org.wmaop.test.services:svcA always returning data/applepear.xml
When invoke org.wmaop.test.services:rootSvc with data/empty.xml
Then pipeline has varA == null && apple == "alpha"

Scenario:  Invoke a fixed mock with pipeline content. Verify pipline doesnt have variable from replace service and contains mock and pipeline values
Given mock org.wmaop.test.services:svcA always returning data/applepear.xml
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then pipeline has varA == null && apple == "alpha" 
And pipeline has lorem == "ipsum"

Scenario: Conditional mock
Given mock org.wmaop.test.services:svcA returning data/applepear.xml when lorem == "ipsum"
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then pipeline has apple == "alpha"

Scenario: Mock a non-existant service
Given mock org.wmaop:doesNotExist always returning data/lorem.xml
When invoke org.wmaop.test.services:rootMissingService with data/lorem.xml
Then pipeline has lorem == "ipsum"

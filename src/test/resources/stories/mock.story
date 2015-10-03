Mock Story

Narrative:
In order to communicate effectively to the business some functionality
As a development team
I want to use Behaviour-Driven Development
					 
Scenario:  Invoke a fixed mock with empty pipeline. Verify pipline doesnt have variable from replace service and contains mock value
Given mock org.wmaop.test.services:svcA always returning data/applepear.xml
When invoke org.wmaop.test.services:rootSvc with data/empty.xml
Then pipeline has varA == null && apple == "alpha"

Scenario:  Invoke a fixed mock with pipeline content. Verify pipline doesnt have variable from replace service and contains mock and pipeline values
Given mock org.wmaop.test.services:svcA always returning data/applepear.xml
When invoke org.wmaop.test.services:rootSvc with data/empty.xml
Then pipeline has varA == null && apple == "alpha" 
And pipeline has lorem == "ipsum"

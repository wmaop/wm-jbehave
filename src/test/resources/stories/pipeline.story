Verify pipeline interaction

Narrative:
In order to observe service state
As a test framework
I want to interact with the pipeline
					 
!--Scenario: capture pipeline to prove interaction  

Scenario: Verify pipeline contents with string contains
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then pipeline has lorem.contains("ps")

Scenario: Verify pipeline contents  with jexl expression
When invoke org.wmaop.test.services:rootSvc without idata
Then pipeline has varA == "A"

Scenario: Verify pipeline contents  with regex
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then pipeline has lorem =~ "ips.*"

Scenario: Verify pipeline contents contained in selection
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then pipeline has lorem =~ ["sit", "delores", "ipsum"]

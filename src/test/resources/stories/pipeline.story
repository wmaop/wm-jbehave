Verify pipeline interaction

Narrative:
In order to observe service state
As a test framework
I want to interact with the pipeline

Scenario: Verify setting of pipeline values
Given pipeline values inString1 = "hello "; inString2 = "world"
When invoke pub.string:concat without idata
Then show pipeline in console
Then pipeline has value == "hello world" 

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

Scenario: Display pipeline content to assist with debuggin stories
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then show pipeline in console

Scenario: Verify function calls work
When invoke org.wmaop.test.services:rootSvc with data/arrayvalues.xml
Then pipeline has arrays:contains(values, "abc")


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

Scenario: Verify setting of pipeline values with multiple given
Given pipeline values inString1 = "hello "
Given pipeline values inString2 = "world"
When invoke pub.string:concat without idata
Then show pipeline in console
Then pipeline has value == "hello world" 

Scenario: Verify overriding of IData
Given pipeline values lorem = "amet"
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then pipeline has lorem == "amet"

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

Scenario: Display pipeline content to assist with debugging stories
When invoke org.wmaop.test.services:rootSvc with data/lorem.xml
Then show pipeline in console

Scenario: Verify function calls work
When invoke org.wmaop.test.services:rootSvc with data/arrayvalues.xml
Then pipeline has arrays:contains(files, "test.xslt")

Scenario: Verify dotted notation with namespace
When invoke org.wmaop.test.services:rootSvc with data/complex.xml
Then pipeline has producer.prd\:Id == "2"

Scenario: Verify array access
When invoke org.wmaop.test.services:rootSvc with data/complex.xml
Then pipeline has producer.prd\:GeneralPartyInfo.pty\:Addr[0].adr\:City == "Napa"

Scenario: Verify on invalid pipeline value
When invoke org.wmaop.test.services:rootSvc with data/complex.xml
Then pipeline has foo.bar[0].x != "not existing"

Scenario: Match document in pipeline
When invoke org.wmaop.test.services:rootSvc with data/complex.xml
Then pipeline document producer matches data/complexsnippet.xml

Scenario: Match document in pipeline based on snippet containing array
When invoke org.wmaop.test.services:rootSvc with data/complex.xml
Then pipeline document producer matches data/complexarraysnippet.xml

Scenario: Match document in pipeline based on XML snippet
When invoke org.wmaop.test.services:rootSvc with data/simpleidata.xml
Then pipeline document producer matches data/simplesnippet.xml
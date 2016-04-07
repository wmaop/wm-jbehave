Mock Story

Narrative:
In order to additional jexl behaviour
As a test framework
I want to raise and check for exceptions

Scenario: Array contains and doesnt contain
When invoke org.wmaop.test.services:empty with data/arrayvalues.xml
Then pipeline has arrays:contains(files, "test.xslt") && !arrays:contains(files, "foo")

Scenario: Array contains and doesnt contain
When invoke org.wmaop.test.services:empty with data/nulldata.xml
Then pipeline has arrays:contains(nularray, null)


					 
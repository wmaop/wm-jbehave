Mock Story

Narrative:
In order to additional jexl behaviour
As a test framework
I want to verify functions executed as expected

Scenario: Array contains and doesnt contain
When invoke org.wmaop.test.services:empty with data/arrayvalues.xml
Then pipeline has arrays:contains(files, "test.xslt") && !arrays:contains(files, "foo")

Scenario: Array contains and doesnt contain
When invoke org.wmaop.test.services:empty with data/nulldata.xml
Then pipeline has arrays:contains(nularray, null)

Scenario: Array has contents matching regular expression
When invoke org.wmaop.test.services:empty with data/arrayvalues.xml
Then pipeline has arrays:matches(files, '.*lt')

					 
Sample story

Narrative:
In order to communicate effectively to the business some functionality
As a development team
I want to use Behaviour-Driven Development
					 
Scenario: test before
Given chkBefore assertion before service pub.math:addInts when total == 5 && aValue == 4 && num1 == 5 && num2 == 4
When invoke org.wmaop.test.services:addInts without idata
Then assertion chkBefore was invoked 1 times

Scenario: test after
Given chkBefore assertion after service pub.math:addInts when total == 5 && aValue == 4 && num1 == 5 && num2 == 4 && value == 9
When invoke org.wmaop.test.services:addInts without idata
Then assertion chkBefore was invoked 1 times






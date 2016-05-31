Hello world story

Narrative:
In order to test and communicate effectively
As a development team
I want to use Behaviour-Driven Development
					 
Scenario: Concatenate two strings
Given pipeline values inString1 = "hello "; inString2 = "world"
When invoke pub.string:concat without idata
Then pipeline has value == "hello world"
Scenario: perform a suitable test
Given mock pub.util.jms:send always returning data/jmsmessage.idata
Given PreSendIdCheck assertion before service pub.util.jms:send when mydoc.x.data.id == 5
When invoke pub.foo:bar with data/myfoodata.xml
Then assertion PreSendIdCheck was invoked 2 times

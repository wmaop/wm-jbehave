Scenario: perform a suitable test
Given mock pub.util.jms:send always returning data/jmsmessage.idata
When invoke pub.foo:bar with data/myfoodata.xml
Then pipeline has message.data == 25
And service pub.util.jms:send was invoked 2 times

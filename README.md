# MQTT_RRPC

本项目的目的是基于eclipse的paho，在ActiveMq上实现用户发送消息并同步获得消息反馈结果的功能。
具体功能类似阿里IOT组件的RRPC功能。

实现过程按照计划将分为三期，一期实现简单Demo完成同步发送异步订阅并回调的功能，二期将异步回调的功能抽出做成统一的入口，三期将项目做成服务化。

2019.03.05
初次提交，已经实现了SimpleClient，调用SimpleClient的doSend方法，将在方法体内部发送MqttMessage，然后设置DefaultFuture回调对象（该方式参照Dubbo源码），异步提交给线程池任务，任务需要订阅消息对应的TOPIC并且在收到反馈消息的时候调用回调对象的doReceived方法执行回调。然后方法将阻塞调用DefaultFuture的get方法，方法内部将会对Condition进行轮询判断直到获取反馈结果或者回调超时，处理结果并返回。
SimpleClient的方法实现比较粗暴和简陋，主要是测试异步回调结果和RRPC的执行流程，在二期中将会按照实际业务的流程进行规范开发。

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Examples of errors starting an OMAG Server

When an OMAG Server is starting, it first outputs a message to the audit log that
records the name of the server and its type.

For example, this platform is starting up five OMAG Servers, each of a
[different type](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md).

```

Wed May 12 15:26:04 BST 2021 cocoMDS1 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the Metadata Server called cocoMDS1
 :
Wed May 12 15:26:13 BST 2021 cocoMDS4 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the Metadata Access Point called cocoMDS4
 :
Wed May 12 15:26:14 BST 2021 cocoView1 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the View Server called cocoView1
 :
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the Engine Host Server called governDL01
 :
Wed May 12 15:26:25 BST 2021 exchangeDL01 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the Integration Daemon called exchangeDL01

```

This is followed by messages that describe the services being started and whether they are
successfully initializing.

Below are the start up messages for `governDL01`

```
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMRS-AUDIT-0064 The Open Metadata Repository Services (OMRS) has initialized the audit log for the Engine Host Server called governDL01
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMAG-ADMIN-0001 The governDL01 server is configured with a max page size of 100
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMRS-AUDIT-0001 The Open Metadata Repository Services (OMRS) is initializing the subsystems to support a new server
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMRS-AUDIT-0007 The Open Metadata Repository Services (OMRS) has initialized
Wed May 12 15:26:24 BST 2021 governDL01 Startup OPEN-METADATA-SECURITY-0003 The Open Metadata Security Service org.odpi.openmetadata.metadatasecurity.samples.CocoPharmaServerSecurityConnector for server governDL01 is initializing
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMAG-ADMIN-0100 The governance services subsystem for the Engine Host Server called governDL01 is about to start
Wed May 12 15:26:24 BST 2021 governDL01 Startup ENGINE-HOST-SERVICES-0001 The engine host services are initializing in server governDL01
Wed May 12 15:26:24 BST 2021 governDL01 Startup ENGINE-HOST-SERVICES-0012 The Open Metadata Engine Services (OMESs) are initializing in server governDL01
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMES-ASSET-ANALYSIS-0001 The Asset Analysis engine services are initializing in server governDL01
Wed May 12 15:26:24 BST 2021 governDL01 Startup OMES-GOVERNANCE-ACTION-0001 The Governance Action engine services are initializing in server governDL01
Wed May 12 15:26:25 BST 2021 governDL01 Startup ENGINE-HOST-SERVICES-0014 2 out of 2 Open Metadata Engine Services (OMESs) in engine host server governDL01 have initialized
Wed May 12 15:26:25 BST 2021 governDL01 Startup ENGINE-HOST-SERVICES-0002 The engine host governDL01 has initialized
Wed May 12 15:26:25 BST 2021 governDL01 Startup OMAG-ADMIN-0101 The governance services subsystem for the Engine Host Server called governDL01 has completed start up

```

At the end of the server start up, the server lists the services it has running.
Below are the messages from the five servers show above:

```
Wed May 12 15:26:13 BST 2021 cocoMDS1 Startup OMAG-ADMIN-0004 The cocoMDS1 server has successfully completed start up.  The following services are running: [Open Metadata Repository Services (OMRS), Connected Asset Services, Asset Catalog OMAS, Asset Consumer OMAS, Asset Manager OMAS, Asset Owner OMAS, Community Profile OMAS, Glossary View OMAS, Discovery Engine OMAS, Data Engine OMAS, Data Manager OMAS, Governance Engine OMAS]

Wed May 12 15:26:14 BST 2021 cocoMDS4 Startup OMAG-ADMIN-0004 The cocoMDS4 server has successfully completed start up.  The following services are running: [Open Metadata Repository Services (OMRS), Connected Asset Services, Asset Catalog OMAS, Asset Consumer OMAS, Community Profile OMAS, Glossary View OMAS, Data Science OMAS]

Wed May 12 15:26:15 BST 2021 cocoView1 Startup OMAG-ADMIN-0004 The cocoView1 server has successfully completed start up.  The following services are running: [Open Metadata Repository Services (OMRS), Glossary Author OMVS, Repository Explorer OMVS, Type Explorer OMVS, Dynamic Infrastructure and Operations OMVS]

Wed May 12 15:26:25 BST 2021 governDL01 Startup OMAG-ADMIN-0004 The governDL01 server has successfully completed start up.  The following services are running: [Open Metadata Repository Services (OMRS), Asset Analysis OMES, Governance Action OMES, Engine Host Services]

Wed May 12 15:26:26 BST 2021 exchangeDL01 Startup OMAG-ADMIN-0004 The exchangeDL01 server has successfully completed start up.  The following services are running: [Open Metadata Repository Services (OMRS), Files Integrator OMIS, Integration Daemon Services]

```

These messages provide a quick way to verify that the right services are starting up.
When failures occur, audit log messages describing these failures appear among the start up messages.
Below are examples of start up errors


## Apache Kafka is not running

If a server is using Apache Kafka to send an receive events and its start up is very slow, check for
errors in the audit log that report problems with Kafka.  For example:

```
Wed May 12 15:07:29 BST 2021 cocoMDS2 Startup OMRS-AUDIT-0026 Initializing listener for cohort cocoCohort
Wed May 12 15:07:29 BST 2021 cocoMDS2 Startup OMRS-AUDIT-0019 An OMRS Topic Connector has registered with an event bus connector for topic egeria.omag.openmetadata.repositoryservices.cohort.cocoCohort.OMRSTopic
Wed May 12 15:07:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0001 Connecting to Apache Kafka Topic egeria.omag.openmetadata.repositoryservices.cohort.cocoCohort.OMRSTopic with a server identifier of 7a0bce2d-8d77-48f6-b895-74b6e0ec2498
Wed May 12 15:07:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 1
Wed May 12 15:08:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 2
Wed May 12 15:09:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 3
Wed May 12 15:10:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 4
Wed May 12 15:11:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 5
Wed May 12 15:12:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 6
Wed May 12 15:13:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 7
Wed May 12 15:14:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 8
Wed May 12 15:15:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 9
Wed May 12 15:16:29 BST 2021 cocoMDS2 Startup OCF-KAFKA-TOPIC-CONNECTOR-0015 The local server is attempting to connect to Kafka, attempt 10
Wed May 12 15:17:29 BST 2021 cocoMDS2 Error OCF-KAFKA-TOPIC-CONNECTOR-0014  Connecting to bootstrap Apache Kafka Broker egeria.omag.openmetadata.repositoryservices.cohort.cocoCohort.OMRSTopic
Wed May 12 15:17:29 BST 2021 cocoMDS2 Exception OMRS-AUDIT-0006 Configuration error detected while connecting to cohort cocoCohort, exception org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException was caught with error message: OCF-KAFKA-TOPIC-CONNECTOR-400-002  Egeria was unable to initialize a connection to a Kafka cluster.  The message in the exception was: org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s)
Wed May 12 15:17:29 BST 2021 cocoMDS2 Exception OMRS-AUDIT-0006 Supplementary information: log record id 0ef711f9-9ac1-429e-899e-094ddbfa0b30 org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException returned message of OCF-KAFKA-TOPIC-CONNECTOR-400-002  Egeria was unable to initialize a connection to a Kafka cluster.  The message in the exception was: org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s) and stacktrace of 
OCFCheckedExceptionBase{reportedHTTPCode=400, reportingClassName='org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector', reportingActionDescription='KafkaMonitor.waitForThisBroker', reportedErrorMessage='OCF-KAFKA-TOPIC-CONNECTOR-400-002  Egeria was unable to initialize a connection to a Kafka cluster.  The message in the exception was: org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s)', reportedErrorMessageId='OCF-KAFKA-TOPIC-CONNECTOR-400-002 ', reportedErrorMessageParameters=[java.util.concurrent.ExecutionException, egeria.omag.openmetadata.repositoryservices.cohort.cocoCohort.OMRSTopic, org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s)], reportedSystemAction='The system is unable initialize.', reportedUserAction='Ensure that Kafka is available', reportedCaughtException=java.util.concurrent.ExecutionException: org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s), reportedCaughtExceptionClassName='java.util.concurrent.ExecutionException', relatedProperties=null}
	at org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector.start(KafkaOpenMetadataTopicConnector.java:257)
	at org.odpi.openmetadata.repositoryservices.connectors.omrstopic.OMRSTopicConnector.start(OMRSTopicConnector.java:258)
	at org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSCohortManager.setSecurityVerifier(OMRSCohortManager.java:283)
	at org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager.setSecurityVerifier(OMRSMetadataHighwayManager.java:128)
	at org.odpi.openmetadata.repositoryservices.admin.OMRSOperationalServices.setSecurityVerifier(OMRSOperationalServices.java:848)
	at org.odpi.openmetadata.adminservices.OMAGServerOperationalServices.activateWithSuppliedConfig(OMAGServerOperationalServices.java:332)
	at org.odpi.openmetadata.adminservices.OMAGServerOperationalServices.activateWithStoredConfig(OMAGServerOperationalServices.java:148)
	at org.odpi.openmetadata.adminservices.spring.OperationalServicesResource.activateWithStoredConfig(OperationalServicesResource.java:59)
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:197)
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:141)
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:106)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:894)
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:808)
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:87)
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:1060)
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:962)
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:1006)
	at org.springframework.web.servlet.FrameworkServlet.doPost(FrameworkServlet.java:909)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:652)
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:883)
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:733)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:227)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:53)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:100)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.springframework.web.filter.FormContentFilter.doFilterInternal(FormContentFilter.java:93)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.springframework.boot.actuate.metrics.web.servlet.WebMvcMetricsFilter.doFilterInternal(WebMvcMetricsFilter.java:93)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:201)
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:119)
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:189)
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:162)
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:202)
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:97)
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:542)
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:143)
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:92)
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:78)
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:357)
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:374)
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:65)
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:893)
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1707)
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61)
	at java.lang.Thread.run(Thread.java:748)
Caused by: java.util.concurrent.ExecutionException: org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s)
	at org.apache.kafka.common.internals.KafkaFutureImpl.wrapAndThrow(KafkaFutureImpl.java:45)
	at org.apache.kafka.common.internals.KafkaFutureImpl.access$000(KafkaFutureImpl.java:32)
	at org.apache.kafka.common.internals.KafkaFutureImpl$SingleWaiter.await(KafkaFutureImpl.java:89)
	at org.apache.kafka.common.internals.KafkaFutureImpl.get(KafkaFutureImpl.java:260)
	at org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector$KafkaStatusChecker.getRunningBrokers(KafkaOpenMetadataTopicConnector.java:421)
	at org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector$KafkaStatusChecker.waitForBrokers(KafkaOpenMetadataTopicConnector.java:451)
	at org.odpi.openmetadata.adapters.eventbus.topic.kafka.KafkaOpenMetadataTopicConnector.start(KafkaOpenMetadataTopicConnector.java:239)
	... 61 more
Caused by: org.apache.kafka.common.errors.TimeoutException: Call(callName=listNodes, deadlineMs=1620829049902, tries=1, nextAllowedTryMs=1620829050010) timed out at 1620829049910 after 1 attempt(s)
Caused by: org.apache.kafka.common.errors.TimeoutException: Timed out waiting for a node assignment. Call: listNodes


```




## Further information

* [Integration Daemon Diagnostic Guide](integration-daemon-diagnostic-guide.md) covers specific guidance on
diagnosing and fixing errors in the Integration Daemon.
* [Repository Services Design Overview](../../../open-metadata-implementation/repository-services/docs) describes the different repository services subsystems used in the different types of OMAG Servers.
* [Repository Services Start up Design](../../../open-metadata-implementation/repository-services/docs/subsystem-descriptions) describe the components initialized in an Egeria Metadata Server.

----

* Return to [diagnostic guide](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
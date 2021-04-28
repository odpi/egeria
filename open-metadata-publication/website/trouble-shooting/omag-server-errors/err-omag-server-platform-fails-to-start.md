<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform fails on startup

If you see a series of error messages like this :

```text
2019-01-31 20:56:25.800 ERROR 66931 --- [           main] org.apache.catalina.util.LifecycleBase   : Failed to start component [Connector[HTTP/1.1-8080]]

org.apache.catalina.LifecycleException: Protocol handler start failed
	at org.apache.catalina.connector.Connector.startInternal(Connector.java:1004) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.catalina.util.LifecycleBase.start(LifecycleBase.java:183) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.catalina.core.StandardService.addConnector(StandardService.java:226) [tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.addPreviouslyRemovedConnectors(TomcatWebServer.java:259) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.web.embedded.tomcat.TomcatWebServer.start(TomcatWebServer.java:197) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.startWebServer(ServletWebServerApplicationContext.java:311) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.finishRefresh(ServletWebServerApplicationContext.java:164) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:549) [spring-context-5.1.4.RELEASE.jar:5.1.4.RELEASE]
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:142) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:775) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:397) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:316) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1260) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1248) [spring-boot-2.1.2.RELEASE.jar:2.1.2.RELEASE]
	at org.odpi.openmetadata.serverchassis.springboot.OMAGServerPlatform.main(OMAGServerPlatform.java:35) [classes/:na]
Caused by: java.net.BindException: Address already in use
	at sun.nio.ch.Net.bind0(Native Method) ~[na:1.8.0_181]
	at sun.nio.ch.Net.bind(Net.java:433) ~[na:1.8.0_181]
	at sun.nio.ch.Net.bind(Net.java:425) ~[na:1.8.0_181]
	at sun.nio.ch.ServerSocketChannelImpl.bind(ServerSocketChannelImpl.java:223) ~[na:1.8.0_181]
	at sun.nio.ch.ServerSocketAdaptor.bind(ServerSocketAdaptor.java:74) ~[na:1.8.0_181]
	at org.apache.tomcat.util.net.NioEndpoint.initServerSocket(NioEndpoint.java:236) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.tomcat.util.net.NioEndpoint.bind(NioEndpoint.java:210) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.tomcat.util.net.AbstractEndpoint.bindWithCleanup(AbstractEndpoint.java:1085) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.tomcat.util.net.AbstractEndpoint.start(AbstractEndpoint.java:1171) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.coyote.AbstractProtocol.start(AbstractProtocol.java:568) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	at org.apache.catalina.connector.Connector.startInternal(Connector.java:1001) ~[tomcat-embed-core-9.0.14.jar:9.0.14]
	... 14 common frames omitted


Process finished with exit code 1
```

This means another server is already using the same network address.

Issue the `server-platform-origin` REST call to determine if this is just another instance of your OMAG Server Platform
already running.  The example below is for an OMAG Server Platform using the default network
address of `https://localhost:9443`.  

```bash
$ curl --insecure -X GET https://localhost:9443/open-metadata/platform-services/users/test/server-platform-origin
Egeria OMAG Server Platform (version 2.10-SNAPSHOT)
```

If you get the `Egeria OMAG Server Platform (version `versionNumber`)` response then your OMAG server platform is already running.
If you get a response like this:

```json
{
  "timestamp": "2019-02-01T15:13:56.749+0000",
  "status": 404,
  "error": "Not Found",
  "message": "No message available",
  "path": "/open-metadata/platform-services/users/test/server-platform-origin"
}
```

Then a different type of server is running at the network address and you may need to consider
[changing your OMAG Server's network address](../../../../open-metadata-resources/open-metadata-tutorials/omag-server-tutorial/task-changing-the-omag-server-network-address.md).

Many other applications may also default to port 9443. Consider using tools like `lsof`. For example on *nix `lsof -i TCP:9443` may work. However this is platform dependent.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

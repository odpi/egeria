<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# OMAG Server Platform

The omag-server-platform.jar file holds the main runtime for Egeria,  To start the platform, enter:

```bash
java -Dloader.path=lib,extra omag-server-platform*.jar
```

You will see the platform logo plus other messages describing its startup configuration.

```bash
 Project Egeria - Open Metadata and Governance
    ____   __  ___ ___    ______   _____                                 ____   _         _     ___
   / __ \ /  |/  //   |  / ____/  / ___/ ___   ____ _   __ ___   ____   / _  \ / / __    / /  / _ /__   ____ _  _
  / / / // /|_/ // /| | / / __    \__ \ / _ \ / __/| | / // _ \ / __/  / /_/ // //   |  / _\ / /_ /  | /  _// || |
 / /_/ // /  / // ___ |/ /_/ /   ___/ //  __// /   | |/ //  __// /    /  __ // // /  \ / /_ /  _// / // /  / / / /
 \____//_/  /_//_/  |_|\____/   /____/ \___//_/    |___/ \___//_/    /_/    /_/ \__/\//___//_/   \__//_/  /_/ /_/

 :: Powered by Spring Boot (v3.1.1) ::

'date time'  INFO 46460 --- [           main] o.o.o.s.springboot.OMAGServerPlatform    : Starting OMAGServerPlatform using Java 17.0.6 with PID 46460 ('jar file name' started by 'user' in 'directory')
'date time'  INFO 46460 --- [           main] o.o.o.s.springboot.OMAGServerPlatform    : No active profile set, falling back to 1 default profile: "default"
'date time'  INFO 46460 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port(s): 9443 (https)
'date time'  INFO 46460 --- [           main] o.o.o.s.springboot.OMAGServerPlatform    : Working directory is: 'directory name'
'date time'  WARN 46460 --- [           main] o.o.o.s.springboot.OMAGServerPlatform    : Java trust store 'javax.net.ssl.trustStore' is null - this is needed by Tomcat - using 'server.ssl.trust-store'
'date time'  INFO 46460 --- [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port(s): 9443 (https) with context path ''
'date time'  INFO 46460 --- [           main] o.o.o.s.springboot.OMAGServerPlatform    : Started OMAGServerPlatform in 8.192 seconds (process running for 8.927)
'date time' No OMAG servers listed in startup configuration
'date time' OMAG server platform ready for more configuration

```
Once the "platform ready" message is displayed, its REST APIs are active and you can configure servers and services within them.

* [Configuring the OMAG Server Platform](https://egeria-project.org/guides/admin/configuring-the-omag-server-platform/) describes the different configuration options available for the OMAG Server Platform.
* [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform/) describes how the platform works.
* [Configuring OMAG Servers](https://egeria-project.org/guides/admin/servers/) describes how to configure servers to run on your platform.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.



 
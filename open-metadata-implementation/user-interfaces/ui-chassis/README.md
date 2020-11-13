<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
 
# User Interface (UI) Chassis

The UI chassis is written using [Spring Boot](https://spring.io/projects/spring-boot).
and manages the REST APIs' endpoint.  Calls to the ui chassis result in rest calls
to appropriate Open Metadata or Governance Services.

Since this is a Spring Boot application, it can be
[customized using the application.properties](https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
file found in the `resources` directory and using environment variables.

The ui chassis code is here:
* **[ui-chassis-spring](ui-chassis-spring)** - ui chassis using Spring.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
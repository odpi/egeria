<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuring developer logging options for OMAG Server Platform

## Options for logging system using Spring Boot server chassis

The default java application server chassis for OMAG server is Spring Boot. As such it comes with support for variety of logging options.
You can always check the options in the official guidelines [here](https://docs.spring.io/spring-boot/docs/current/reference/html/howto.html#howto-logging). 

Spring itself uses abstraction layer and chooses the logging implementation during runtime based on content of the classpath.
OMAG Server chassis spring application uses web starter by default which already brings basic logging options from JCL but also Logback logging system.

## Application level logging categories 

As described in the Spring documentation, you can control log levels for specific category logger using system or application properties:

```properties
logging.level.root=ERROR
logging.level.org.springframework.web=DEBUG
logging.level.org.odpi.openmetadata=ERROR
```
 
## Connecting the OMAG Audit Log Framework

While the default logging framework enables generic interface for system logging is not adequate option for diagnostic and auditing.
For this purpose in OMAG Server platform [Audit Log Framework](../../../frameworks/audit-log-framework) is used. You can find out more about this on Egeria website [Diagnostics Guide](../../../../open-metadata-publication/website/diagnostic-guide/).
 
Audit Logging framework is configured as part of the OMAG configuration document by choosing from the available destination systems.  
By default, the system will use the stdout - system standard output as a destination. 
  
One of the options suitable for production like operating environments is to choose SLF4J connector and connect AuditLog to the existing system logging implementation via dedicated audit log logger category:

```properties
logging.level.org.odpi.openmetadata.frameworks.auditlog=INFO
```

Using SLF4J connector you can also have granular auditlog logger category control per OMAG server instance following naming pattern below:

`logging.level.org.odpi.openmetadata.frameworks.auditlog.{omag-server-name}.{originator-component-name}`

> Note that the category is defined by omitting the empty space characters.

Example:

```properties
logging.level.org.odpi.openmetadata.frameworks.auditlog.omag-server-1=INFO
logging.level.org.odpi.openmetadata.frameworks.auditlog.omag-server-2.RepositoryContentManager=ERROR
```

---
* Return to [Configuring the Audit Log](configuring-the-audit-log.md)
* Return to [Configuring the OMAG Server Platform](configuring-the-omag-server-platform.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
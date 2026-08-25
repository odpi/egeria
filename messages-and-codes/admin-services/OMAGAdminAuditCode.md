<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OMAGAdminAuditCode

The OMAGAdminAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `OMAG-ADMIN-` |
| **Java class** | `org.odpi.openmetadata.adminservices.ffdc.OMAGAdminAuditCode` |
| **Module** | [open-metadata-implementation/admin-services/admin-services-api](../../open-metadata-implementation/admin-services/admin-services-api) |
| **Source** | [OMAGAdminAuditCode.java](../../open-metadata-implementation/admin-services/admin-services-api/src/main/java/org/odpi/openmetadata/adminservices/ffdc/OMAGAdminAuditCode.java) |
| **Further reading** | <https://egeria-project.org/guides/admin/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OMAG-ADMIN-0001](#omag-admin-0001) | STARTUP | The {0} service is being ignored in the startup of server {1} because it is not registered to this platform |
| [OMAG-ADMIN-0208](#omag-admin-0208) | ERROR | The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property |
| [OMAG-ADMIN-0209](#omag-admin-0209) | STARTUP | The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1} |
| [OMAG-ADMIN-0210](#omag-admin-0210) | ERROR | The {0} Open Metadata Access Service (OMAS) cannot register a listener with the enterprise OMRS Topic for server {1} because it is null |
| [OMAG-ADMIN-0211](#omag-admin-0211) | EXCEPTION | Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error message was {3} |
| [OMAG-ADMIN-0212](#omag-admin-0212) | EXCEPTION | Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3} |
| [OMAG-ADMIN-0216](#omag-admin-0216) | STARTUP | The {0} Open Metadata View Service (OMAS) is supporting the access to all types of assets |
| [OMAG-ADMIN-0217](#omag-admin-0217) | STARTUP | The {0} Open Metadata View Service (OMAS) is supporting the following asset types when searching: {1} |

----

### OMAG-ADMIN-0001

> The {0} service is being ignored in the startup of server {1} because it is not registered to this platform

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.IGNORING_UNREGISTERED_SERVICE` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The configured service will not be available in the running server because the code to run the service is missing from the platform's classpath.

**User action**

Determine if this service is needed in the server.  Remove it from the configuration is it is not.  If it is needed, add the jar file for the service into the platform's lib (or extra) directory to ensure it is picked up.  If the jar file is in the correct place then examine its implementation to ensure it registers with the runtime.


----

### OMAG-ADMIN-0208

> The {0} Open Metadata Access Service (OMAS) has been passed an invalid value of {1} in the {2} property

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.BAD_CONFIG_PROPERTY` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The access service has not been passed valid configuration in its option's map.

**User action**

Correct the configuration property and restart the server.


----

### OMAG-ADMIN-0209

> The {0} Open Metadata Access Service (OMAS) is registering a listener with the enterprise OMRS Topic for server {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.SERVICE_REGISTERED_WITH_ENTERPRISE_TOPIC` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The OMAS is registering to receive events from the open metadata repositories registered with the cohort.

**User action**

This is part of the normal start up of an access service in a server.


----

### OMAG-ADMIN-0210

> The {0} Open Metadata Access Service (OMAS) cannot register a listener with the enterprise OMRS Topic for server {1} because it is null

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.NO_ENTERPRISE_TOPIC` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The OMAS is registering to receive events from the open metadata repositories registered with the cohort but cannot because the enterprise OMRS topic is null.

**User action**

Review other error messages to determine why the connector to the enterprise topic is missing.


----

### OMAG-ADMIN-0211

> Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connector.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.BAD_TOPIC_CONNECTOR` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The access service has not been passed valid configuration. The server where it is configured failed to start.

**User action**

Use the information in the error message to determine the cause of the problem, then correct the failing configuration and restart the server.


----

### OMAG-ADMIN-0212

> Method {0} called on behalf of the {1} service detected a {2} exception when creating an open metadata topic connection because the connector provider is incorrect.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.BAD_TOPIC_CONNECTOR_PROVIDER` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This is an internal error.  The access service is not using a valid connector provider.

**User action**

Raise an issue on Egeria's GitHub and work with the Egeria community to resolve.


----

### OMAG-ADMIN-0216

> The {0} Open Metadata View Service (OMAS) is supporting the access to all types of assets

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.ALL_SEARCH_TYPES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}` |

**System action**

The view service has not been passed a list of asset types in the SupportedTypesForSearch property of the view services options.  This means it is providing access to all Assets irrespective of their type.

**User action**

No action is required if this view service should be giving access to all types of assets in the open metadata ecosystem.  If this scope is too broad then set up a list of asset types in the SupportedTypesForSearch property for this view service.


----

### OMAG-ADMIN-0217

> The {0} Open Metadata View Service (OMAS) is supporting the following asset types when searching: {1}

|  |  |
|---|---|
| **Java constant** | `OMAGAdminAuditCode.SUPPORTED_SEARCH_TYPES` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The view service was passed a list of asset types in the SupportedTypesForSearch property of the view services options.  This means it is only providing access to these types of Assets.

**User action**

Verify that these types are the right set for this service deployment.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

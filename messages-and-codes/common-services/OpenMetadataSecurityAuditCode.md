<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenMetadataSecurityAuditCode

The OpenMetadataSecurityAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 26 |
| **Message identifiers begin** | `OPEN-METADATA-SECURITY-` |
| **Java class** | `org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityAuditCode` |
| **Module** | [open-metadata-implementation/common-services/metadata-security/metadata-security-apis](../../open-metadata-implementation/common-services/metadata-security/metadata-security-apis) |
| **Source** | [OpenMetadataSecurityAuditCode.java](../../open-metadata-implementation/common-services/metadata-security/metadata-security-apis/src/main/java/org/odpi/openmetadata/metadatasecurity/ffdc/OpenMetadataSecurityAuditCode.java) |
| **Further reading** | <https://egeria-project.org/features/metadata-security/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-METADATA-SECURITY-0001](#open-metadata-security-0001) | STARTUP | The Open Metadata Security Service {0} for OMAG Server Platform {1} is initializing |
| [OPEN-METADATA-SECURITY-0002](#open-metadata-security-0002) | SHUTDOWN | The Open Metadata Security Service {0} for OMAG Server Platform {1} is shutting down |
| [OPEN-METADATA-SECURITY-0003](#open-metadata-security-0003) | STARTUP | The Open Metadata Security Service {0} for server {1} is initializing |
| [OPEN-METADATA-SECURITY-0004](#open-metadata-security-0004) | SHUTDOWN | The Open Metadata Security Service {0} for server {1} is shutting down |
| [OPEN-METADATA-SECURITY-0005](#open-metadata-security-0005) | SECURITY | User {0} is not authorized to issue a {1} request to OMAG Server Platform {2} |
| [OPEN-METADATA-SECURITY-0006](#open-metadata-security-0006) | SECURITY | User {0} is not authorized to issue a request to server {1} |
| [OPEN-METADATA-SECURITY-0007](#open-metadata-security-0007) | SECURITY | User {0} is not authorized to issue {1} requests for service {2} on server {3} |
| [OPEN-METADATA-SECURITY-0008](#open-metadata-security-0008) | SECURITY | User {0} is not authorized to attach feedback to element {1} |
| [OPEN-METADATA-SECURITY-0009](#open-metadata-security-0009) | SECURITY | User {0} is not authorized to change the zone membership of element {1} from {2} to {3} |
| [OPEN-METADATA-SECURITY-0011](#open-metadata-security-0011) | SECURITY | User {0} is not authorized to issue operation {1} on {2} anchor element {3} |
| [OPEN-METADATA-SECURITY-0015](#open-metadata-security-0015) | SECURITY | User {0} is not authorized to access open metadata type {1} ({2}) on server {3} |
| [OPEN-METADATA-SECURITY-0016](#open-metadata-security-0016) | SECURITY | User {0} is not authorized to change open metadata type {1} ({2}) on server {3} |
| [OPEN-METADATA-SECURITY-0017](#open-metadata-security-0017) | SECURITY | User {0} is not authorized to create an open metadata instance of type {1} on server {2} |
| [OPEN-METADATA-SECURITY-0018](#open-metadata-security-0018) | SECURITY | User {0} is not authorized to access open metadata instance {1} of type {2} on server {3} |
| [OPEN-METADATA-SECURITY-0019](#open-metadata-security-0019) | SECURITY | User {0} is not authorized to change open metadata type {1} of type {2} on server {3} using method {4} |
| [OPEN-METADATA-SECURITY-0020](#open-metadata-security-0020) | SECURITY | User {0} is not authorized to issue operation {1} on {2} element {3} |
| [OPEN-METADATA-SECURITY-0021](#open-metadata-security-0021) | INFO | Element {0} is not visible to user {1}; it has been filtered from the search results |
| [OPEN-METADATA-SECURITY-0022](#open-metadata-security-0022) | SECURITY | User {0} is not recognized |
| [OPEN-METADATA-SECURITY-0023](#open-metadata-security-0023) | SECURITY | Exception {0} occurred when retrieving user {1}; the exception message was {2} |
| [OPEN-METADATA-SECURITY-0024](#open-metadata-security-0024) | SECURITY | User {0} has expired credentials |
| [OPEN-METADATA-SECURITY-0025](#open-metadata-security-0025) | SECURITY | Security access control {0} is not recognized |
| [OPEN-METADATA-SECURITY-0026](#open-metadata-security-0026) | SECURITY | Exception {0} occurred when retrieving security access control {1}; the exception message was {2} |
| [OPEN-METADATA-SECURITY-0027](#open-metadata-security-0027) | SECURITY | Adding user {0} to the platform user directory |
| [OPEN-METADATA-SECURITY-0028](#open-metadata-security-0028) | SECURITY | Removing user {0} from the platform user directory |
| [OPEN-METADATA-SECURITY-0029](#open-metadata-security-0029) | SECURITY | Adding security access control {0} to the platform user directory |
| [OPEN-METADATA-SECURITY-0030](#open-metadata-security-0030) | SECURITY | Removing security access control {0} from the platform user directory |

----

### OPEN-METADATA-SECURITY-0001

> The Open Metadata Security Service {0} for OMAG Server Platform {1} is initializing

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.PLATFORM_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has started up a new instance of the Open Metadata Platform Security Service Connector.

**User action**

No action is required.  This is part of the normal operation of the service.


----

### OPEN-METADATA-SECURITY-0002

> The Open Metadata Security Service {0} for OMAG Server Platform {1} is shutting down

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.PLATFORM_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local administrator has requested shut down of the Open Metadata Platform Security Service Connector.

**User action**

No action is required.  This is part of the normal operation of the service.


----

### OPEN-METADATA-SECURITY-0003

> The Open Metadata Security Service {0} for server {1} is initializing

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.SERVICE_INITIALIZING` |
| **Severity** | STARTUP - A new component is starting up. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local server has started up a new instance of the Open Metadata Server Security Service Connector.

**User action**

No action is required.  This is part of the normal operation of the service.


----

### OPEN-METADATA-SECURITY-0004

> The Open Metadata Security Service {0} for server {1} is shutting down

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.SERVICE_SHUTDOWN` |
| **Severity** | SHUTDOWN - An existing component is shutting down. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The local administrator has requested shut down of the Open Metadata Server Security Service Connector.

**User action**

No action is required.  This is part of the normal operation of the service.


----

### OPEN-METADATA-SECURITY-0005

> User {0} is not authorized to issue a {1} request to OMAG Server Platform {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_PLATFORM_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The security service detected an unauthorized access to an OMAG Server Platform.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0006

> User {0} is not authorized to issue a request to server {1}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVER_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The security service detected an unauthorized access to a service.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0007

> User {0} is not authorized to issue {1} requests for service {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_SERVICE_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized access to a service.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0008

> User {0} is not authorized to attach feedback to element {1}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_ADD_FEEDBACK` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The security service detected an unauthorized change to an element.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0009

> User {0} is not authorized to change the zone membership of element {1} from {2} to {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_ZONE_CHANGE` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized change to an element.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0011

> User {0} is not authorized to issue operation {1} on {2} anchor element {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_ANCHOR_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized access to a member of the anchor element.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0015

> User {0} is not authorized to access open metadata type {1} ({2}) on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_TYPE_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized access of an open metadata type.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0016

> User {0} is not authorized to change open metadata type {1} ({2}) on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_TYPE_CHANGE` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized change of an open metadata type.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0017

> User {0} is not authorized to create an open metadata instance of type {1} on server {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_CREATE` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The security service detected an unauthorized access of an open metadata type.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0018

> User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized access of an open metadata instance.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0019

> User {0} is not authorized to change open metadata type {1} of type {2} on server {3} using method {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_INSTANCE_CHANGE` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The security service detected an unauthorized change of an open metadata instance.

**User action**

Review the security policies and settings to determine if this access should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0020

> User {0} is not authorized to issue operation {1} on {2} element {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNAUTHORIZED_ELEMENT_ACCESS` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized access to an element.

**User action**

Review the security policies and settings to determine if this access to the element should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-0021

> Element {0} is not visible to user {1}; it has been filtered from the search results

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.FILTERED_ELEMENT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system has filtered an element from the results because the user does not have the necessary permissions to access it.

**User action**

The element is filtered from the results.


----

### OPEN-METADATA-SECURITY-0022

> User {0} is not recognized

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNKNOWN_USER` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

The security service has received a request from an unknown user.

**User action**

Track down the source of the request and either add the user to the user directory or prevent the user from accessing again.


----

### OPEN-METADATA-SECURITY-0023

> Exception {0} occurred when retrieving user {1}; the exception message was {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.FAILED_TO_RETRIEVE_USER` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An exception occurred when the security service tried to retrieve a user account.

**User action**

Use the information in the exception to determine the cause of this error.  The user will not be granted access to the open metadata ecosystem.


----

### OPEN-METADATA-SECURITY-0024

> User {0} has expired credentials

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.EXPIRED_USER` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

The security service has received a request from a user whose credentials have expired.

**User action**

Track down the source of the request and encourage the used to rest their credentials (probably password).


----

### OPEN-METADATA-SECURITY-0025

> Security access control {0} is not recognized

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.UNKNOWN_CONTROL` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

The security service has received a request for an unknown control.

**User action**

Track down the source of the request and correct the name of the control - or add the missing control to the secrets store.


----

### OPEN-METADATA-SECURITY-0026

> Exception {0} occurred when retrieving security access control {1}; the exception message was {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.FAILED_TO_RETRIEVE_CONTROL` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An exception occurred when the security service tried to retrieve a security access control.

**User action**

Use the information in the exception to determine the cause of this error.  The control will not be returned to the calling user.


----

### OPEN-METADATA-SECURITY-0027

> Adding user {0} to the platform user directory

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.ADDING_USER` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

A user account is added or updated in the platform's user directory

**User action**

Make sure this user is valid and has the correct permissions.


----

### OPEN-METADATA-SECURITY-0028

> Removing user {0} from the platform user directory

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.REMOVING_USER` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

A user account is removed from the platform's user directory

**User action**

Make sure this user is no longer needed.


----

### OPEN-METADATA-SECURITY-0029

> Adding security access control {0} to the platform user directory

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.ADDING_CONTROL` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

A security access control is added or updated in the platform's user directory

**User action**

Make sure this control is valid and has the correct permissions.


----

### OPEN-METADATA-SECURITY-0030

> Removing security access control {0} from the platform user directory

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityAuditCode.REMOVING_CONTROL` |
| **Severity** | SECURITY - Unauthorized access to a service or metadata instance has been attempted. |
| **Message inserts** | `{0}` |

**System action**

A security access control is removed from the platform's user directory.

**User action**

Make sure this control is no longer needed.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

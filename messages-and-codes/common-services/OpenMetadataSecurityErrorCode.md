<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenMetadataSecurityErrorCode

The OpenMetadataSecurityErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with open metadata security connectors.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 21 |
| **Message identifiers begin** | `OPEN-METADATA-SECURITY-` |
| **Java class** | `org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode` |
| **Module** | [open-metadata-implementation/common-services/metadata-security/metadata-security-apis](../../open-metadata-implementation/common-services/metadata-security/metadata-security-apis) |
| **Source** | [OpenMetadataSecurityErrorCode.java](../../open-metadata-implementation/common-services/metadata-security/metadata-security-apis/src/main/java/org/odpi/openmetadata/metadatasecurity/ffdc/OpenMetadataSecurityErrorCode.java) |
| **Further reading** | <https://egeria-project.org/features/metadata-security/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-METADATA-SECURITY-400-001](#open-metadata-security-400-001) | 400 | The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1} |
| [OPEN-METADATA-SECURITY-400-002](#open-metadata-security-400-002) | 400 | The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2} |
| [OPEN-METADATA-SECURITY-403-001](#open-metadata-security-403-001) | 403 | User {0} is not authorized to issue {1} request to {2} |
| [OPEN-METADATA-SECURITY-403-002](#open-metadata-security-403-002) | 403 | User {0} is not authorized to issue a request to server {1} |
| [OPEN-METADATA-SECURITY-403-003](#open-metadata-security-403-003) | 403 | User {0} is not authorized to use the {1} service on server {2} |
| [OPEN-METADATA-SECURITY-403-006](#open-metadata-security-403-006) | 403 | User {0} is not authorized to issue {1} requests to the {2} service on server {3} |
| [OPEN-METADATA-SECURITY-403-004](#open-metadata-security-403-004) | 403 | User {0} is not authorized to attach feedback to element {1} |
| [OPEN-METADATA-SECURITY-403-005](#open-metadata-security-403-005) | 403 | User {0} is not authorized to change the zone membership for element {1} from {2} to {3} |
| [OPEN-METADATA-SECURITY-403-007](#open-metadata-security-403-007) | 403 | User {0} is not authorized to issue operation {1} on {2} anchor element {3} |
| [OPEN-METADATA-SECURITY-403-008](#open-metadata-security-403-008) | 403 | User {0} is not authorized to create an element of type {1} |
| [OPEN-METADATA-SECURITY-403-011](#open-metadata-security-403-011) | 403 | User {0} is not authorized to access open metadata type {1} ({2}) on server {3} |
| [OPEN-METADATA-SECURITY-403-012](#open-metadata-security-403-012) | 403 | User {0} is not authorized to change open metadata type {1} ({2}) on server {3} |
| [OPEN-METADATA-SECURITY-403-013](#open-metadata-security-403-013) | 403 | User {0} is not authorized to access open metadata instance {1} of type {2} on server {3} |
| [OPEN-METADATA-SECURITY-403-014](#open-metadata-security-403-014) | 403 | User {0} is not authorized to change open metadata instance {1} of type {2} on server {3} |
| [OPEN-METADATA-SECURITY-403-016](#open-metadata-security-403-016) | 403 | {0} connections are connected to the asset with unique identifier {1} but the user {2} is not permitted to use any of them; the calling method is {3} |
| [OPEN-METADATA-SECURITY-403-017](#open-metadata-security-403-017) | 403 | User {0} is not recognized |
| [OPEN-METADATA-SECURITY-403-018](#open-metadata-security-403-018) | 403 | Exception {0} occurred when retrieving user {1}; the exception message was {2} |
| [OPEN-METADATA-SECURITY-403-020](#open-metadata-security-403-020) | 403 | User {0} is not authorized to issue an operation {1} on {2} element {3} |
| [OPEN-METADATA-SECURITY-403-025](#open-metadata-security-403-025) | 403 | Security access control {0} is not recognized |
| [OPEN-METADATA-SECURITY-403-026](#open-metadata-security-403-026) | 403 | Exception {0} occurred when retrieving security access control {1}; the exception message was {2} |
| [OPEN-METADATA-SECURITY-500-002](#open-metadata-security-500-002) | 500 | Element {0} is not visible to user {1}; it has been filtered from the search results |

----

### OPEN-METADATA-SECURITY-400-001

> The OMAG server platform has been configured with a bad connection to its platform security connector.  Error message is {0}. Connection is {1}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.BAD_PLATFORM_SECURITY_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot validate the users issuing platform requests.

**User action**

Review the error message to determine the cause of the problem and correct the connection supplied for the platform security connector.  Then restart the OMAG Server Platform.


----

### OPEN-METADATA-SECURITY-400-002

> The OMAG server {0} has been configured with a bad connection to its security connector.  Error message is {1}. Connection is {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.BAD_SERVER_SECURITY_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot validate the users issuing requests to this server.

**User action**

Review the error message to determine the cause of the problem and correct the connection for the server's security connector in the server's configuration document.  Then restart the server.


----

### OPEN-METADATA-SECURITY-403-001

> User {0} is not authorized to issue {1} request to {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_PLATFORM_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process a request from the user because they do not have access to the requested platform services.  The request fails with a UserNotAuthorizedException exception.

**User action**

Determine if this is a configuration error, a mistake or the platform is under attack.  Correct any configuration error and re-run the request, if it is a valid request; otherwise contact your security team.


----

### OPEN-METADATA-SECURITY-403-002

> User {0} is not authorized to issue a request to server {1}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_SERVER_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process a request from the user because they do not have access to the requested OMAG server.  The request fails with a UserNotAuthorizedException exception.

**User action**

Determine whether the user should have access to the server.  If they should have, take steps to add them to the authorized list of users.  If this user should not have access, investigate where the request came from to determine if the system is under attack, or it was a mistake, or the user's tool is not configured to connect to the correct server.


----

### OPEN-METADATA-SECURITY-403-003

> User {0} is not authorized to use the {1} service on server {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_SERVICE_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot process a request from the user because they do not have access to the requested service. The request fails with a UserNotAuthorizedException exception.

**User action**

Determine whether the user should have access to the requested service. If they should have, take steps to add them to the authorized list of users.  If this user should not have access, investigate where the request came from to determine if the system is under attack, or it was a mistake.


----

### OPEN-METADATA-SECURITY-403-006

> User {0} is not authorized to issue {1} requests to the {2} service on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_SERVICE_OPERATION_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have access to the requested operation of the service. The request fails with a UserNotAuthorizedException exception.

**User action**

Determine whether the user should be able to issue this operation on the requested service. If they should, take steps to add them to the authorized list of users for the operation.  If this user should not have access, investigate where the request came from to determine if the system is under attack, or it was a mistake.


----

### OPEN-METADATA-SECURITY-403-004

> User {0} is not authorized to attach feedback to element {1}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_ADD_FEEDBACK` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process a request from the user because they do not have access to augment the requested element.  The request fails with a UserNotAuthorizedException exception.

**User action**

Using information about the element and the user, determine if this result is expected, or if the configuration needs to be adjusted to allow this user to perform the request.


----

### OPEN-METADATA-SECURITY-403-005

> User {0} is not authorized to change the zone membership for element {1} from {2} to {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_ZONE_CHANGE` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have access to update the requested element.  The request fails with a UserNotAuthorizedException exception.

**User action**

Using information about the element, the zones and the user, determine if this result is expected, or if the configuration needs to be adjusted to allow this user to perform the request.


----

### OPEN-METADATA-SECURITY-403-007

> User {0} is not authorized to issue operation {1} on {2} anchor element {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_ANCHOR_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have access to the requested element.  The request fails with a UserNotAuthorizedException exception.

**User action**

Using knowledge about the user and the element, determine if this is the correct result or the configuration needs to be changed to allow access.


----

### OPEN-METADATA-SECURITY-403-008

> User {0} is not authorized to create an element of type {1}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_INSTANCE_CREATE` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot process a request from the user because they do not have authority to create an element of the requested type.  The request fails with a UserNotAuthorizedException exception.

**User action**

Using knowledge about the user and the element, determine if this is the correct result or the configuration needs to be changed to allow the user to create the element.


----

### OPEN-METADATA-SECURITY-403-011

> User {0} is not authorized to access open metadata type {1} ({2}) on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_TYPE_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have access to the necessary services and/or resources to retrieve type information.  The request fails with a UserNotAuthorizedException exception.

**User action**

Determine if the user should be allowed access to the type information or not.  If they should then change the configuration to give them access.


----

### OPEN-METADATA-SECURITY-403-012

> User {0} is not authorized to change open metadata type {1} ({2}) on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_TYPE_CHANGE` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have access to update an open metadata type.  The request fails with a UserNotAuthorizedException exception.

**User action**

The ability to change types is typically limited to a restricted group of users.  Determine if the user is privileged to make these changes.  If they are then update the configuration to grant them access.


----

### OPEN-METADATA-SECURITY-403-013

> User {0} is not authorized to access open metadata instance {1} of type {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_INSTANCE_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have read access to the requested metadata.  The request fails with a UserNotAuthorizedException exception.

**User action**

Determine if the user should have access to this metadata instance and if they should then change the configuration to give them the required privileges.


----

### OPEN-METADATA-SECURITY-403-014

> User {0} is not authorized to change open metadata instance {1} of type {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_INSTANCE_CHANGE` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request from the user because they do not have access to make changes to the requested metadata instance.  The request fails with a UserNotAuthorizedException exception.

**User action**

Determine if the user should have access to this metadata instance and if they should then change the configuration to give them the required update privileges.


----

### OPEN-METADATA-SECURITY-403-016

> {0} connections are connected to the asset with unique identifier {1} but the user {2} is not permitted to use any of them; the calling method is {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.NO_CONNECTIONS_ALLOWED` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot process a request because the calling user does not have sufficient privileges.

**User action**

No action is required if this user should not have access to the connection.  To gain access to the connection, either the security credentials of the user need changing, or a different userId is required.


----

### OPEN-METADATA-SECURITY-403-017

> User {0} is not recognized

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNKNOWN_USER` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}` |

**System action**

The security service has received a request from an unknown user.

**User action**

Track down the source of the request and either add the user to the user directory or prevent the user from accessing again.


----

### OPEN-METADATA-SECURITY-403-018

> Exception {0} occurred when retrieving user {1}; the exception message was {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.FAILED_TO_RETRIEVE_USER` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An exception occurred when the security service tried to retrieve a user account.

**User action**

Use the information in the exception to determine the cause of this error.  The user will not be granted access to the open metadata ecosystem.


----

### OPEN-METADATA-SECURITY-403-020

> User {0} is not authorized to issue an operation {1} on {2} element {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNAUTHORIZED_ELEMENT_ACCESS` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The security service detected an unauthorized access to a glossary.

**User action**

Review the security policies and settings to determine if this access to the element should be allowed or not.  Take action to either change the security sessions or determine the reason for the unauthorized request.


----

### OPEN-METADATA-SECURITY-403-025

> Security access control {0} is not recognized

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.UNKNOWN_CONTROL` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}` |

**System action**

The security service has received a request for an unknown control.

**User action**

Track down the source of the request and correct the name of the control - or add the missing control to the secrets store.


----

### OPEN-METADATA-SECURITY-403-026

> Exception {0} occurred when retrieving security access control {1}; the exception message was {2}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.FAILED_TO_RETRIEVE_CONTROL` |
| **HTTP error code** | 403 - Forbidden - the caller is not authorized to perform this request |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

An exception occurred when the security service tried to retrieve a security access control.

**User action**

Use the information in the exception to determine the cause of this error.  The control will not be returned to the calling user.


----

### OPEN-METADATA-SECURITY-500-002

> Element {0} is not visible to user {1}; it has been filtered from the search results

|  |  |
|---|---|
| **Java constant** | `OpenMetadataSecurityErrorCode.FILTERED_ELEMENT` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system has filtered an element from the results because the user does not have the necessary permissions to access it.

**User action**

The element is filtered from the results.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

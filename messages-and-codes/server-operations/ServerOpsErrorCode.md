<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ServerOpsErrorCode

The ServerOpsErrorCode is used to define first failure data capture (FFDC) for errors that occur within the OMAG Server It is used in conjunction with OMAG Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `SERVER-OPS-` |
| **Java class** | `org.odpi.openmetadata.serveroperations.ffdc.ServerOpsErrorCode` |
| **Module** | [open-metadata-implementation/server-operations/server-operations-api](../../open-metadata-implementation/server-operations/server-operations-api) |
| **Source** | [ServerOpsErrorCode.java](../../open-metadata-implementation/server-operations/server-operations-api/src/main/java/org/odpi/openmetadata/serveroperations/ffdc/ServerOpsErrorCode.java) |
| **Further reading** | <https://egeria-project.org/services/server-operations/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [SERVER-OPS-400-011](#server-ops-400-011) | 400 | The OMAG server {0} has been passed a null admin services class name for access service {1} |
| [SERVER-OPS-400-012](#server-ops-400-012) | 400 | The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2} |
| [SERVER-OPS-400-014](#server-ops-400-014) | 400 | The OMAG server {0} has been passed an invalid maximum page size of {1} |
| [SERVER-OPS-400-015](#server-ops-400-015) | 400 | The OMAG server {0} cannot start the {1} enterprise OMRS topic connector, {2} exception with error message {3} occurred |
| [SERVER-OPS-400-029](#server-ops-400-029) | 400 | The View Server {0} has been passed a null admin services class name for view service {1} |
| [SERVER-OPS-400-030](#server-ops-400-030) | 400 | The View Server {0} has been passed an invalid admin services class name {1} for view service {2} |
| [SERVER-OPS-500-004](#server-ops-500-004) | 500 | The {0} service detected an unexpected {1} exception with message {2} during initialization |

----

### SERVER-OPS-400-011

> The OMAG server {0} has been passed a null admin services class name for access service {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.NULL_ACCESS_SERVICE_ADMIN_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot initialize this access service. The server failed to start.

**User action**

If the access service should be initialized then set up the appropriate admin services class name in the access service's configuration and restart the server instance. Otherwise, remove the configuration for this access service and restart the server.


----

### SERVER-OPS-400-012

> The OMAG server {0} has been passed an invalid admin services class name {1} for access service {2}

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.BAD_ACCESS_SERVICE_ADMIN_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot initialize this access service and the server failed to start.

**User action**

The configuration document for the serve needs to be fixed before the server will restart.  If the access service should be initialized then update its configuration andensure ist admin class name is set to the name of a Java Class that implements AccessServiceAdmin. Otherwise delete the configuration for this access service.  Once the configuration document is updated, restart the server.


----

### SERVER-OPS-400-014

> The OMAG server {0} has been passed an invalid maximum page size of {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.BAD_MAX_PAGE_SIZE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server failed to start.

**User action**

The maximum page size sets an upper limit on how many results a caller can request on a paged REST API call.  If it is set to zero then it means there is no limit, a positive number is the maximum paging size allowed.  Set the maximum page size in the configuration document to an appropriate value and restart the server.


----

### SERVER-OPS-400-015

> The OMAG server {0} cannot start the {1} enterprise OMRS topic connector, {2} exception with error message {3} occurred

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.ENTERPRISE_TOPIC_START_FAILED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The open metadata access services will not be able to receive events from the connected repositories.

**User action**

Review the error messages and once the source of the problem is resolved, restart the server and retry the request.


----

### SERVER-OPS-400-029

> The View Server {0} has been passed a null admin services class name for view service {1}

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.NULL_VIEW_SERVICE_ADMIN_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot initialize this view service since it has no admin class to call.

**User action**

If the view service should be initialized then set up the appropriate view service admin class name and restart the View Server.


----

### SERVER-OPS-400-030

> The View Server {0} has been passed an invalid admin services class name {1} for view service {2}

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.BAD_VIEW_SERVICE_ADMIN_CLASS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot initialize this view service since it can not find the view's admin class.

**User action**

If the view service should be initialized then ensure that the view service's admin class is specified correctly and available on the class path.  Then restart the View Server.


----

### SERVER-OPS-500-004

> The {0} service detected an unexpected {1} exception with message {2} during initialization

|  |  |
|---|---|
| **Java constant** | `ServerOpsErrorCode.UNEXPECTED_INITIALIZATION_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The system cannot start the service in the OMAG server.

**User action**

This is likely to be either an operational or logic error. Look for other errors in the audit log.  Validate the request.  If you are stuck, raise an issue.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

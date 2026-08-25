<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ConformanceSuiteErrorCode

The ConformanceSuiteErrorCode is used to define first failure data capture (FFDC) for errors that occur within the Open Metadata Conformance Suite It is used in conjunction with conformance suite exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 10 |
| **Message identifiers begin** | `CONFORMANCE-SUITE-` |
| **Java class** | `org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteErrorCode` |
| **Module** | [open-metadata-conformance-suite/open-metadata-conformance-suite-api](../../open-metadata-conformance-suite/open-metadata-conformance-suite-api) |
| **Source** | [ConformanceSuiteErrorCode.java](../../open-metadata-conformance-suite/open-metadata-conformance-suite-api/src/main/java/org/odpi/openmetadata/conformance/ffdc/ConformanceSuiteErrorCode.java) |
| **Further reading** | <https://egeria-project.org/guides/cts/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [CONFORMANCE-SUITE-400-001](#conformance-suite-400-001) | 400 | OMAG server has been called with a null local server name |
| [CONFORMANCE-SUITE-400-002](#conformance-suite-400-002) | 400 | OMAG server {0} has been called with a null username (userId) |
| [CONFORMANCE-SUITE-400-003](#conformance-suite-400-003) | 400 | Unable to create a report for a test case with unknown identifier {0} |
| [CONFORMANCE-SUITE-400-004](#conformance-suite-400-004) | 400 | Unable to create a report for a workbench with unknown identifier {0} |
| [CONFORMANCE-SUITE-400-022](#conformance-suite-400-022) | 400 | The Egeria Conformance Suite located in OMAG server {0} has been configured with no access to the enterprise repository services |
| [CONFORMANCE-SUITE-400-023](#conformance-suite-400-023) | 400 | Unable to create a report for a profile with unknown name {0} |
| [CONFORMANCE-SUITE-500-001](#conformance-suite-500-001) | 500 | The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise topic connector |
| [CONFORMANCE-SUITE-500-002](#conformance-suite-500-002) | 500 | The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise connector manager |
| [CONFORMANCE-SUITE-500-003](#conformance-suite-500-003) | 500 | The Egeria Conformance Suite testing technology {0} of type {1} has created two test cases with the same id of {2} |
| [CONFORMANCE-SUITE-503-003](#conformance-suite-503-003) | 503 | The conformance suite service has not been initialized for server {0} and can not support REST API call {1} |

----

### CONFORMANCE-SUITE-400-001

> OMAG server has been called with a null local server name

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.NULL_LOCAL_SERVER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot configure the local server.

**User action**

The local server name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### CONFORMANCE-SUITE-400-002

> OMAG server {0} has been called with a null username (userId)

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.NULL_USER_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot configure the local server.

**User action**

The user name is supplied by the caller to the OMAG server. This call needs to be corrected before the server can operate correctly.


----

### CONFORMANCE-SUITE-400-003

> Unable to create a report for a test case with unknown identifier {0}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.UNKNOWN_TEST_CASE_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the report.

**User action**

Validate the test case identifier with the messages being produced by the audit log  and the conformance suite documentation.


----

### CONFORMANCE-SUITE-400-004

> Unable to create a report for a workbench with unknown identifier {0}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.UNKNOWN_WORKBENCH_ID` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the report.

**User action**

Validate the workbench identifier with the messages being produced by the audit log and the conformance suite documentation.


----

### CONFORMANCE-SUITE-400-022

> The Egeria Conformance Suite located in OMAG server {0} has been configured with no access to the enterprise repository services

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.NO_ENTERPRISE_ACCESS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot access the connectors to issue metadata requests to the technologies under test.

**User action**

Change the setting of the enterprise access service to ensure it is enabled.


----

### CONFORMANCE-SUITE-400-023

> Unable to create a report for a profile with unknown name {0}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.UNKNOWN_PROFILE_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the report.

**User action**

Validate the profile name with the messages being produced by the audit log and the conformance suite documentation.


----

### CONFORMANCE-SUITE-500-001

> The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise topic connector

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.NO_ENTERPRISE_TOPIC` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

The conformance suite cannot receive and evaluate events from technologies under test.

**User action**

This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved.


----

### CONFORMANCE-SUITE-500-002

> The Egeria Conformance Suite located in OMAG server {0} has not been passed an enterprise connector manager

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.NO_ENTERPRISE_CONNECTOR_MANAGER` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}` |

**System action**

The conformance suite cannot issue metadata requests to the technologies under test.

**User action**

This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved.


----

### CONFORMANCE-SUITE-500-003

> The Egeria Conformance Suite testing technology {0} of type {1} has created two test cases with the same id of {2}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.DUPLICATE_TEST_CASE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The conformance suite cannot process one of the test cases.

**User action**

This is an internal logic error.  Create a git issue at https://github.com/odpi/egeria/issues to get this resolved.


----

### CONFORMANCE-SUITE-503-003

> The conformance suite service has not been initialized for server {0} and can not support REST API call {1}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteErrorCode.SERVICE_NOT_INITIALIZED` |
| **HTTP error code** | 503 - Service Unavailable - the service needed to process the request is not running |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The server has received a call to one of its open metadata conformance suite operations but cannot process it because the conformance suite service is not active.

**User action**

If the server is supposed to have the conformance suite service activated, correct the server configuration and restart the server.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

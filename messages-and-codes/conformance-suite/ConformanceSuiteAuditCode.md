<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ConformanceSuiteAuditCode

The ConformanceSuiteAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 15 |
| **Message identifiers begin** | `CONFORMANCE-SUITE-` |
| **Java class** | `org.odpi.openmetadata.conformance.ffdc.ConformanceSuiteAuditCode` |
| **Module** | [open-metadata-conformance-suite/open-metadata-conformance-suite-api](../../open-metadata-conformance-suite/open-metadata-conformance-suite-api) |
| **Source** | [ConformanceSuiteAuditCode.java](../../open-metadata-conformance-suite/open-metadata-conformance-suite-api/src/main/java/org/odpi/openmetadata/conformance/ffdc/ConformanceSuiteAuditCode.java) |
| **Further reading** | <https://egeria-project.org/guides/cts/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [CONFORMANCE-SUITE-0006](#conformance-suite-0006) | INFO | The Open Metadata Conformance Workbench {0} is initializing; see {1} for documentation |
| [CONFORMANCE-SUITE-0007](#conformance-suite-0007) | INFO | The Open Metadata Conformance Workbench {0} has initialized |
| [CONFORMANCE-SUITE-0008](#conformance-suite-0008) | INFO | The Open Metadata Repository Conformance Workbench {0} is waiting for server {1} to join the cohort |
| [CONFORMANCE-SUITE-0009](#conformance-suite-0009) | ERROR | The Open Metadata Conformance Workbench {0} cannot run its tests; error message is {1} |
| [CONFORMANCE-SUITE-0010](#conformance-suite-0010) | INFO | The Open Metadata Conformance Workbench {0} has finished |
| [CONFORMANCE-SUITE-0011](#conformance-suite-0011) | INFO | The Open Metadata Conformance Workbench {0} has completed its synchronous tests, further test cases may be triggered from incoming events |
| [CONFORMANCE-SUITE-0012](#conformance-suite-0012) | INFO | The Open Metadata Conformance Test Case {0} is initializing; see {1} for documentation |
| [CONFORMANCE-SUITE-0013](#conformance-suite-0013) | DECISION | The Open Metadata Conformance Test Case {0} has completed with {1} successful assertions, {2} unsuccessful assertions, {3} unexpected exceptions and {4} discovered properties. |
| [CONFORMANCE-SUITE-0014](#conformance-suite-0014) | DECISION | The Open Metadata Conformance Test Case {0} has completed with {1} successful assertions, {2} unsuccessful assertions, {3} unexpected exceptions and {4} discovered properties.  The message on completion was: {5} |
| [CONFORMANCE-SUITE-0015](#conformance-suite-0015) | INFO | The Open Metadata Conformance Suite has access to the local repository with metadata collection id {0} |
| [CONFORMANCE-SUITE-0016](#conformance-suite-0016) | INFO | The technology under test with a server name of {0} connected to the same cohort as the Open Metadata Conformance Suite with metadata collection id {1} |
| [CONFORMANCE-SUITE-0017](#conformance-suite-0017) | INFO | Server {0} with metadata collection id {1} connected to the same cohort as the Open Metadata Conformance Suite that is configured to test a server named {2} |
| [CONFORMANCE-SUITE-0018](#conformance-suite-0018) | INFO | The technology under test with server name {0} and metadata collection id {1} has left the cohort |
| [CONFORMANCE-SUITE-0019](#conformance-suite-0019) | INFO | Server {0} with metadata collection id {1} has left the cohort |
| [CONFORMANCE-SUITE-0021](#conformance-suite-0021) | ERROR | Test execution was polling for events {0} times every {1} ms, and has now overrun |

----

### CONFORMANCE-SUITE-0006

> The Open Metadata Conformance Workbench {0} is initializing; see {1} for documentation

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.WORKBENCH_INITIALIZING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Open Metadata Conformance Service has started one of the workbenches from the conformance suite.

**User action**

No action is required.  This is part of the normal operation of the service.


----

### CONFORMANCE-SUITE-0007

> The Open Metadata Conformance Workbench {0} has initialized

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.WORKBENCH_INITIALIZED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The Open Metadata Conformance Suite has completed initialization of a new workbench.

**User action**

No action is required.  This message confirms that the workbench is ready to run its test cases.


----

### CONFORMANCE-SUITE-0008

> The Open Metadata Repository Conformance Workbench {0} is waiting for server {1} to join the cohort

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.WORKBENCH_WAITING_TO_START` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Open Metadata Repository Conformance Workbench begins running tests once it receives a notification that the technology under test has joined the cohort.

**User action**

Ensure that the cohort is operating correctly and the technology under test joins the same cohort as the conformance suite.


----

### CONFORMANCE-SUITE-0009

> The Open Metadata Conformance Workbench {0} cannot run its tests; error message is {1}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.WORKBENCH_FAILURE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The access service detected an error during the start up of a specific server instance.  Its services are not available for the server.

**User action**

Review the error message and any other reported failures to determine the cause of the problem.  Once this is resolved, restart the server.


----

### CONFORMANCE-SUITE-0010

> The Open Metadata Conformance Workbench {0} has finished

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.WORKBENCH_COMPLETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The Open Metadata Conformance Workbench has completed running its tests.

**User action**

No action is required.  The results of this workbench are available in the conformance report.


----

### CONFORMANCE-SUITE-0011

> The Open Metadata Conformance Workbench {0} has completed its synchronous tests, further test cases may be triggered from incoming events

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.WORKBENCH_SYNC_COMPLETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The Open Metadata Conformance Workbench has completed running the tests it drives synchronously.  Further test activity will be triggered by incoming events from the cohort.

**User action**

No action is required, but expect further test case messages from this workbench as events arrive from the cohort.


----

### CONFORMANCE-SUITE-0012

> The Open Metadata Conformance Test Case {0} is initializing; see {1} for documentation

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.TEST_CASE_INITIALIZING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Open Metadata Conformance Service has started on of the workbenches from the conformance suite.

**User action**

No action is required.  This message records which test case is about to run.


----

### CONFORMANCE-SUITE-0013

> The Open Metadata Conformance Test Case {0} has completed with {1} successful assertions, {2} unsuccessful assertions, {3} unexpected exceptions and {4} discovered properties.

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.TEST_CASE_COMPLETED` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The Open Metadata Conformance Test Case has completed running its tests. Retrieve the report to find out more details.

**User action**

No action is required, but retrieve the report if this message shows unsuccessful assertions or unexpected exceptions.


----

### CONFORMANCE-SUITE-0014

> The Open Metadata Conformance Test Case {0} has completed with {1} successful assertions, {2} unsuccessful assertions, {3} unexpected exceptions and {4} discovered properties.  The message on completion was: {5}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.TEST_CASE_COMPLETED_SUCCESSFULLY` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The Open Metadata Conformance Test Case has completed running its tests and supplied a completion message.  Retrieve the report to find out more details.

**User action**

No action is required, but review the completion message and retrieve the report for the detailed results of this test case.


----

### CONFORMANCE-SUITE-0015

> The Open Metadata Conformance Suite has access to the local repository with metadata collection id {0}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.LOCAL_CONNECTOR_IN_COHORT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The Open Metadata Conformance Suite is initializing.

**User action**

No action is required.  This message records the metadata collection id of the repository that the conformance suite uses for its own metadata.


----

### CONFORMANCE-SUITE-0016

> The technology under test with a server name of {0} connected to the same cohort as the Open Metadata Conformance Suite with metadata collection id {1}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.TUT_CONNECTED_TO_COHORT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Open Metadata Conformance Suite is able to start running tests.

**User action**

No action is required.  This message confirms that the technology under test is visible to the conformance suite and that testing can begin.


----

### CONFORMANCE-SUITE-0017

> Server {0} with metadata collection id {1} connected to the same cohort as the Open Metadata Conformance Suite that is configured to test a server named {2}

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.ANOTHER_CONNECTED_TO_COHORT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The Open Metadata Conformance Suite will ignore this server.  However, it will also receive test metadata from the conformance suite which may not be what was intended.

**User action**

Verify that this server has joined the right cohort.


----

### CONFORMANCE-SUITE-0018

> The technology under test with server name {0} and metadata collection id {1} has left the cohort

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.TUT_LEFT_COHORT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The Open Metadata Conformance Suite cannot run any more tests.

**User action**

Extract the conformance report from the conformance suite to determine if the tests were successful.


----

### CONFORMANCE-SUITE-0019

> Server {0} with metadata collection id {1} has left the cohort

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.ANOTHER_LEFT_COHORT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This server will no longer receive metadata instances from the conformance tests.

**User action**

No action is required if this server was expected to leave the cohort.  If it is the technology under test, restart it and rerun the affected workbenches.


----

### CONFORMANCE-SUITE-0021

> Test execution was polling for events {0} times every {1} ms, and has now overrun

|  |  |
|---|---|
| **Java constant** | `ConformanceSuiteAuditCode.POLLING_OVERFLOW` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The execution of subsequent tests will likely fail based on the expectation that these polled events were processed, which due to the overrun they may not (yet) have been processed.

**User action**

Increase the polling interval, number of retries, or run on a system with more available resources or lower latency for events.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

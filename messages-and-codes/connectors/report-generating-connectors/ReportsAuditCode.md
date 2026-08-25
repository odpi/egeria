<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# ReportsAuditCode

The ReportsAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 2 |
| **Message identifiers begin** | `REPORT-GENERATORS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.reports.ffdc.ReportsAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/report-generating-connectors](../../../open-metadata-implementation/adapters/open-connectors/report-generating-connectors) |
| **Source** | [ReportsAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/report-generating-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/reports/ffdc/ReportsAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/report/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [REPORT-GENERATORS-0001](#report-generators-0001) | EXCEPTION | The report generating service {0} received an unexpected {1} exception during method {2}; the error message was: {3} |
| [REPORT-GENERATORS-0002](#report-generators-0002) | EXCEPTION | The report generating service {0} created report {1} |

----

### REPORT-GENERATORS-0001

> The report generating service {0} received an unexpected {1} exception during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `ReportsAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The service cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### REPORT-GENERATORS-0002

> The report generating service {0} created report {1}

|  |  |
|---|---|
| **Java constant** | `ReportsAuditCode.REPORT_CREATED` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The service has complete successfully.

**User action**

Review the contents of the report.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

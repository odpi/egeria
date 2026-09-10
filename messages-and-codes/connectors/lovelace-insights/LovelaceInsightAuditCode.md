<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# LovelaceInsightAuditCode

The LovelaceInsightAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 13 |
| **Message identifiers begin** | `LOVELACE-INSIGHTS-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.lovelaceinsight.ffdc.LovelaceInsightAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/lovelace-insights](../../../open-metadata-implementation/adapters/open-connectors/lovelace-insights) |
| **Source** | [LovelaceInsightAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/lovelace-insights/src/main/java/org/odpi/openmetadata/adapters/connectors/lovelaceinsight/ffdc/LovelaceInsightAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [LOVELACE-INSIGHTS-0001](#lovelace-insights-0001) | EXCEPTION | The organization insight service {0} received an unexpected exception {1} during method {2}; the error message was: {3} |
| [LOVELACE-INSIGHTS-0016](#lovelace-insights-0016) | INFO | The {0} governance action service received a {1} exception when it registered its completion status.  The exception message is: {2} |
| [LOVELACE-INSIGHTS-0017](#lovelace-insights-0017) | INFO | The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2} |
| [LOVELACE-INSIGHTS-0018](#lovelace-insights-0018) | INFO | The {0} governance service has processed the {1} governance zone ({2}) |
| [LOVELACE-INSIGHTS-0038](#lovelace-insights-0038) | INFO | The {0} governance action service has completed successfully |
| [LOVELACE-INSIGHTS-0039](#lovelace-insights-0039) | INFO | The {0} governance action service is analysing OpenLineage log store {1} for events between {2} and {3} |
| [LOVELACE-INSIGHTS-0040](#lovelace-insights-0040) | INFO | The {0} governance action service read {1} OpenLineage events ({2} files could not be read) covering {3} jobs and {4} datasets |
| [LOVELACE-INSIGHTS-0041](#lovelace-insights-0041) | INFO | The {0} governance action service has completed, updating {1} elements with {2} from OpenLineage log store {3} |
| [LOVELACE-INSIGHTS-0042](#lovelace-insights-0042) | INFO | The {0} governance action service found {3} elements of type {1} for OpenLineage element {2} so it is skipped |
| [LOVELACE-INSIGHTS-0043](#lovelace-insights-0043) | INFO | The {0} governance action service has profiled {3} runs of job {1} into process {2}; the inferred schedule is {4} |
| [LOVELACE-INSIGHTS-0044](#lovelace-insights-0044) | INFO | The {0} governance action service has updated the data scope of dataset {1} (asset {2}); the write pattern is {3} and the data collection window is {4} to {5} |
| [LOVELACE-INSIGHTS-0045](#lovelace-insights-0045) | INFO | The {0} governance action service has summarised the data quality of {1} {2} (element {3}) across {4} dimensions with an overall pass rate of {5}% |
| [LOVELACE-INSIGHTS-0046](#lovelace-insights-0046) | ERROR | The {0} governance action service cannot find an OpenLineage log store to analyse (directory: {1}) |

----

### LOVELACE-INSIGHTS-0001

> The organization insight service {0} received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |

**System action**

The connector cannot process the current request.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### LOVELACE-INSIGHTS-0016

> The {0} governance action service received a {1} exception when it registered its completion status.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.UNABLE_TO_SET_COMPLETION_STATUS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |

**System action**

The governance action throws a GovernanceServiceException in the hope that the hosting server is able to clean up.

**User action**

Review the exception messages that are logged about the same time as one of them will point to the root cause of the error.


----

### LOVELACE-INSIGHTS-0017

> The {0} governance action service received a {1} exception when it registered a listener with the governance context.  The exception message is: {2}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.UNABLE_TO_REGISTER_LISTENER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |

**System action**

The governance action service throws a GovernanceServiceException.

**User action**

This is likely to be a configuration error.  Review the description of the exception's message to understand what is not set up correctly and and follow its instructions.


----

### LOVELACE-INSIGHTS-0018

> The {0} governance service has processed the {1} governance zone ({2})

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.GOVERNANCE_ZONE_PROCESSED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |

**System action**

The service will move on to the next governance zone until all have been processed.

**User action**

No action is required except to validate that each of the governance zones have been processed.


----

### LOVELACE-INSIGHTS-0038

> The {0} governance action service has completed successfully

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.SERVICE_COMPLETED_SUCCESSFULLY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/organization-insight/overview/> |

**System action**

The service is shutting down.

**User action**

No action is required except to validate that the shutdown is occurring at an appropriate time.


----

### LOVELACE-INSIGHTS-0039

> The {0} governance action service is analysing OpenLineage log store {1} for events between {2} and {3}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_ANALYSIS_STARTED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The service is reading the OpenLineage events in the log store that fall within its analysis window.

**User action**

No action is required.  This message records the log store and window being analysed.


----

### LOVELACE-INSIGHTS-0040

> The {0} governance action service read {1} OpenLineage events ({2} files could not be read) covering {3} jobs and {4} datasets

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_EVENTS_READ` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The service has built the run history from the log store and is starting its analysis.

**User action**

If files could not be read, check the log store for files that are not OpenLineage run events in JSON.


----

### LOVELACE-INSIGHTS-0041

> The {0} governance action service has completed, updating {1} elements with {2} from OpenLineage log store {3}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_ANALYSIS_COMPLETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The service has finished its analysis of the log store.

**User action**

No action is required.  This message records how many elements were updated.


----

### LOVELACE-INSIGHTS-0042

> The {0} governance action service found {3} elements of type {1} for OpenLineage element {2} so it is skipped

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_ELEMENT_NOT_FOUND` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The service only updates elements that it can identify uniquely by namespace and resource name.  Either the element has not been catalogued by the OpenLineage cataloguer, or there are duplicates awaiting resolution.

**User action**

Ensure the OpenLineage cataloguer is running against the same events, and resolve any duplicate elements.


----

### LOVELACE-INSIGHTS-0043

> The {0} governance action service has profiled {3} runs of job {1} into process {2}; the inferred schedule is {4}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_PROCESS_PROFILED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The RunMetrics classification of the process has been updated with the run profile.

**User action**

No action is required.  This message records the process that was updated.


----

### LOVELACE-INSIGHTS-0044

> The {0} governance action service has updated the data scope of dataset {1} (asset {2}); the write pattern is {3} and the data collection window is {4} to {5}

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_DATA_SCOPE_UPDATED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The DataScope classification of the asset has been updated from the series of writes and reads in the log store.

**User action**

No action is required.  This message records the asset that was updated.


----

### LOVELACE-INSIGHTS-0045

> The {0} governance action service has summarised the data quality of {1} {2} (element {3}) across {4} dimensions with an overall pass rate of {5}%

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_DATA_QUALITY_SUMMARISED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

A survey report with a quality annotation per dimension has been attached to the element.

**User action**

Review the survey report for dimensions with low pass rates.


----

### LOVELACE-INSIGHTS-0046

> The {0} governance action service cannot find an OpenLineage log store to analyse (directory: {1})

|  |  |
|---|---|
| **Java constant** | `LovelaceInsightAuditCode.OPEN_LINEAGE_NO_LOG_STORE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-open-lineage/overview/> |

**System action**

The service needs either an openLineageLogStore action target that is a folder asset, or a logStoreDirectory request parameter naming an existing directory.  It has stopped without performing any analysis.

**User action**

Add the log store folder as an action target of the governance action type, or set the logStoreDirectory request parameter, and rerun the service.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

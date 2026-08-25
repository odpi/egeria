<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GovernanceActionSamplesErrorCode

The GovernanceActionSamplesErrorCode is used to define first failure data capture (FFDC) for errors that occur when working with the Basic File Connector. It is used in conjunction with both Checked and Runtime (unchecked) exceptions.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 4 |
| **Message identifiers begin** | `GOVERNANCE-ACTION-SAMPLES-` |
| **Java class** | `org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesErrorCode` |
| **Module** | [open-metadata-resources/open-metadata-samples/governance-action-samples](../../open-metadata-resources/open-metadata-samples/governance-action-samples) |
| **Source** | [GovernanceActionSamplesErrorCode.java](../../open-metadata-resources/open-metadata-samples/governance-action-samples/src/main/java/org/odpi/openmetadata/samples/governanceactions/ffdc/GovernanceActionSamplesErrorCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/governance-action-service/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [GOVERNANCE-ACTION-SAMPLES-400-001](#governance-action-samples-400-001) | 400 | The {0} governance action service has detected that hospital {1} ({2}) is not nominated to participate in project {3} ({4}) |
| [GOVERNANCE-ACTION-SAMPLES-400-002](#governance-action-samples-400-002) | 400 | The {0} governance action service has detected that hospital {1} ({2}) is not certified to supply data for project {3} ({4}) |
| [GOVERNANCE-ACTION-SAMPLES-400-003](#governance-action-samples-400-003) | 400 | The {0} governance action service has detected that certification type {1} is not linked to the clinical trial project {2} |
| [GOVERNANCE-ACTION-SAMPLES-500-001](#governance-action-samples-500-001) | 500 | The {0} governance action service received an unexpected exception {1} during its processing; the error message was: {2} |

----

### GOVERNANCE-ACTION-SAMPLES-400-001

> The {0} governance action service has detected that hospital {1} ({2}) is not nominated to participate in project {3} ({4})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesErrorCode.HOSPITAL_NOT_NOMINATED` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The service stops processing for this hospital.

**User action**

Retry the request once the nomination is complete.


----

### GOVERNANCE-ACTION-SAMPLES-400-002

> The {0} governance action service has detected that hospital {1} ({2}) is not certified to supply data for project {3} ({4})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesErrorCode.UNCERTIFIED_HOSPITAL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The service stops setting up the onboarding pipeline for this hospital.

**User action**

Retry the request once the certification is complete.


----

### GOVERNANCE-ACTION-SAMPLES-400-003

> The {0} governance action service has detected that certification type {1} is not linked to the clinical trial project {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesErrorCode.WRONG_CERTIFICATION_TYPE_FOR_TRIAL` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The service stops certifying this hospital.

**User action**

Retry the request and ensure that a valid certification type, that is linked to the clinical trial project using the GovernedBy relationship, is specified in the action targets.


----

### GOVERNANCE-ACTION-SAMPLES-500-001

> The {0} governance action service received an unexpected exception {1} during its processing; the error message was: {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action returns an exception to the Governance Action Engine.

**User action**

Use details from the error message to determine the cause of the error and retry the service call once it is resolved.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

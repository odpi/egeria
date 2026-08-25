<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# GovernanceActionSamplesAuditCode

The GovernanceActionSamplesAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 5 |
| **Message identifiers begin** | `GOVERNANCE-ACTION-SAMPLES-` |
| **Java class** | `org.odpi.openmetadata.samples.governanceactions.ffdc.GovernanceActionSamplesAuditCode` |
| **Module** | [open-metadata-resources/open-metadata-samples/governance-action-samples](../../open-metadata-resources/open-metadata-samples/governance-action-samples) |
| **Source** | [GovernanceActionSamplesAuditCode.java](../../open-metadata-resources/open-metadata-samples/governance-action-samples/src/main/java/org/odpi/openmetadata/samples/governanceactions/ffdc/GovernanceActionSamplesAuditCode.java) |
| **Further reading** | <https://egeria-project.org/concepts/governance-action-service/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [GOVERNANCE-ACTION-SAMPLES-0001](#governance-action-samples-0001) | DECISION | The {0} governance action service has verified that hospital {1} ({2}) is certified to supply data for project {3} ({4}) |
| [GOVERNANCE-ACTION-SAMPLES-0002](#governance-action-samples-0002) | ERROR | The {0} governance action service was passed a null value for {1} |
| [GOVERNANCE-ACTION-SAMPLES-0003](#governance-action-samples-0003) | INFO | The {0} governance action service has not been passed a Unity Catalog (UC) catalog for the data lake |
| [GOVERNANCE-ACTION-SAMPLES-0004](#governance-action-samples-0004) | INFO | The {0} governance action service was unable to create the landing area directory {1} |
| [GOVERNANCE-ACTION-SAMPLES-0005](#governance-action-samples-0005) | INFO | The {0} governance action service was unable to create the volume directory {1} for Unity Catalog Volume {2} |

----

### GOVERNANCE-ACTION-SAMPLES-0001

> The {0} governance action service has verified that hospital {1} ({2}) is certified to supply data for project {3} ({4})

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesAuditCode.CERTIFIED_HOSPITAL` |
| **Severity** | DECISION - A decision has been made related to the operation of the system. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The service sets up the onboarding pipeline for this hospital.

**User action**

No specific action is required.  This message is to log that a verification check has taken place.


----

### GOVERNANCE-ACTION-SAMPLES-0002

> The {0} governance action service was passed a null value for {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesAuditCode.MISSING_VALUE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service returns an INVALID completion status.

**User action**

This is an error in the way that the governance action service has been called, which could be a direct invocation through the initiateEngineAction() method, initiateGovernanceActionType() methodor as part of a governance action process.  Identify which approach was used and add the required information as an action target to the invocation code.  Then rerun the request.


----

### GOVERNANCE-ACTION-SAMPLES-0003

> The {0} governance action service has not been passed a Unity Catalog (UC) catalog for the data lake

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesAuditCode.MISSING_CATALOG` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |

**System action**

The governance action service is not able to set up the data lake for the clinical trial.

**User action**

Retry the request, but ensure that the action target for the catalog is supplied.


----

### GOVERNANCE-ACTION-SAMPLES-0004

> The {0} governance action service was unable to create the landing area directory {1}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesAuditCode.NO_LANDING_FOLDER` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The governance action service is not able to set up the landing area directory for the clinical trial.

**User action**

Add the landing area directory manually.


----

### GOVERNANCE-ACTION-SAMPLES-0005

> The {0} governance action service was unable to create the volume directory {1} for Unity Catalog Volume {2}

|  |  |
|---|---|
| **Java constant** | `GovernanceActionSamplesAuditCode.NO_VOLUME_DIRECTORY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The governance action service is not able to set up the directory for a Unity Catalog Volume.

**User action**

Add the volume's directory manually.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenMetadataObservabilityAuditCode

The OpenMetadataObservabilityAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 10 |
| **Message identifiers begin** | `METADATA-OBSERVABILITY-` |
| **Java class** | `org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode` |
| **Module** | [open-metadata-implementation/common-services/metadata-observability](../../open-metadata-implementation/common-services/metadata-observability) |
| **Source** | [OpenMetadataObservabilityAuditCode.java](../../open-metadata-implementation/common-services/metadata-observability/src/main/java/org/odpi/openmetadata/metadataobservability/ffdc/OpenMetadataObservabilityAuditCode.java) |
| **Further reading** | <https://egeria-project.org/services/common-services/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [METADATA-OBSERVABILITY-0001](#metadata-observability-0001) | ACTIVITY | User {0} created {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0002](#metadata-observability-0002) | ACTIVITY | User {0} retrieved {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0003](#metadata-observability-0003) | ACTIVITY | User {0} retrieved an attachment to {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0004](#metadata-observability-0004) | ACTIVITY | User {0} updated an attachment to {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0005](#metadata-observability-0005) | ACTIVITY | User {0} updated feedback related to {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0006](#metadata-observability-0006) | ACTIVITY | User {0} updated {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0007](#metadata-observability-0007) | ACTIVITY | User {0} deleted {1} asset {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0008](#metadata-observability-0008) | ACTIVITY | User {0} retrieved {1} asset {2} during search operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0009](#metadata-observability-0009) | ACTIVITY | User {0} retrieved an attachment to {1} asset {2} during search operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0010](#metadata-observability-0010) | ACTIVITY | User {0} issued REST API call to operation {1} of service {2} on server {3} |

----

### METADATA-OBSERVABILITY-0001

> User {0} created {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_CREATE` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to create an asset.

**User action**

No action is required, but this message can be used to capture user activity information related to asset creation.


----

### METADATA-OBSERVABILITY-0002

> User {0} retrieved {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_READ` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to receive an asset.

**User action**

No action is required, but this message can be used to capture user activity information.


----

### METADATA-OBSERVABILITY-0003

> User {0} retrieved an attachment to {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_READ_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to receive full details about an asset.

**User action**

No action is required, but this message can be used to capture user activity information about the use of assets.


----

### METADATA-OBSERVABILITY-0004

> User {0} updated an attachment to {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to build out the knowledge for an asset.

**User action**

No action is required, but this message can be used to capture user activity information related to curation of an asset.


----

### METADATA-OBSERVABILITY-0005

> User {0} updated feedback related to {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE_FEEDBACK` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to maintain feedback on an asset.

**User action**

No action is required, but this message can be used to capture user activity information related to the maintenance of feedback attached to an asset.


----

### METADATA-OBSERVABILITY-0006

> User {0} updated {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_UPDATE` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to update an asset.

**User action**

No action is required, but this message can be used to capture user activity information related to asset updates.


----

### METADATA-OBSERVABILITY-0007

> User {0} deleted {1} asset {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_DELETE` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to delete an asset.

**User action**

No action is required, but this message can be used to capture user activity information related to asset deletion.


----

### METADATA-OBSERVABILITY-0008

> User {0} retrieved {1} asset {2} during search operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_SEARCH` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to retrieve an asset as part of a search request.  The asset may not be the caller's choice.

**User action**

No action is required, but this message can be used to capture user activity information relating to the assets being retrieved through searches.


----

### METADATA-OBSERVABILITY-0009

> User {0} retrieved an attachment to {1} asset {2} during search operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ASSET_ACTIVITY_SEARCH_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

This message is used to capture user requests to retrieve part of an asset as part of a search request.  This asset may not be the caller's choice.

**User action**

No action is required, but this message can be used to capture user activity information relating to the attachments of an asset assets being retrieved through searches.


----

### METADATA-OBSERVABILITY-0010

> User {0} issued REST API call to operation {1} of service {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.USER_REQUEST_ACTIVITY` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

This message is used to capture user activity.

**User action**

No action is required, but this message can be used to capture user activity information.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

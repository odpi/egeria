<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OpenMetadataObservabilityAuditCode

The OpenMetadataObservabilityAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 11 |
| **Message identifiers begin** | `METADATA-OBSERVABILITY-` |
| **Java class** | `org.odpi.openmetadata.metadataobservability.ffdc.OpenMetadataObservabilityAuditCode` |
| **Module** | [open-metadata-implementation/common-services/metadata-observability](../../open-metadata-implementation/common-services/metadata-observability) |
| **Source** | [OpenMetadataObservabilityAuditCode.java](../../open-metadata-implementation/common-services/metadata-observability/src/main/java/org/odpi/openmetadata/metadataobservability/ffdc/OpenMetadataObservabilityAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [METADATA-OBSERVABILITY-0001](#metadata-observability-0001) | ACTIVITY | User {0} created {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0002](#metadata-observability-0002) | ACTIVITY | User {0} retrieved {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0003](#metadata-observability-0003) | ACTIVITY | User {0} retrieved an attachment to {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0004](#metadata-observability-0004) | ACTIVITY | User {0} updated an attachment to {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0005](#metadata-observability-0005) | ACTIVITY | User {0} updated feedback related to {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0006](#metadata-observability-0006) | ACTIVITY | User {0} updated the properties of {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0007](#metadata-observability-0007) | ACTIVITY | User {0} deleted an attachment to {1} anchor element {2} during operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0008](#metadata-observability-0008) | ACTIVITY | User {0} retrieved {1} anchor element {2} during search operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0009](#metadata-observability-0009) | ACTIVITY | User {0} retrieved an attachment to {1} anchor element {2} during search operation {3} of service {4} |
| [METADATA-OBSERVABILITY-0010](#metadata-observability-0010) | ACTIVITY | User {0} issued REST API call to operation {1} of service {2} on server {3} |
| [METADATA-OBSERVABILITY-0011](#metadata-observability-0011) | ACTIVITY | User {0} deleted feedback related to {1} anchor element {2} during operation {3} of service {4} |

----

### METADATA-OBSERVABILITY-0001

> User {0} created {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_CREATE` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The creation of an anchor element (such as an asset, digital product or glossary term) is recorded.  It is not added to the anchor element's UsageActivity counts, since creation says nothing about how much the element is used.

**User action**

No action is required, but this message can be used to capture user activity information related to the creation of anchor elements.


----

### METADATA-OBSERVABILITY-0002

> User {0} retrieved {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The retrieval counts towards the explicitGetCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often people ask for the element by name or identifier, which indicates that it is of specific interest to them.

**User action**

No action is required, but this message can be used to capture user activity information.


----

### METADATA-OBSERVABILITY-0003

> User {0} retrieved an attachment to {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_READ_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The retrieval counts towards the explicitGetCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  Retrieving an element anchored to the anchor element, such as a schema attribute or connection, is counted as interest in the anchor element itself.

**User action**

No action is required, but this message can be used to capture user activity information about the use of anchor elements.


----

### METADATA-OBSERVABILITY-0004

> User {0} updated an attachment to {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The change counts towards the updateAttachmentCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how actively the content attached to the element - its schema, connections, notes and other anchored elements - is being curated.

**User action**

No action is required, but this message can be used to capture user activity information related to the curation of an anchor element.


----

### METADATA-OBSERVABILITY-0005

> User {0} updated feedback related to {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE_FEEDBACK` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The change counts towards the updateFeedbackCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how much the element's consumers are engaging with it through comments, likes, ratings and informal tags.

**User action**

No action is required, but this message can be used to capture user activity information related to the maintenance of feedback attached to an anchor element.


----

### METADATA-OBSERVABILITY-0006

> User {0} updated the properties of {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_UPDATE` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The update counts towards the updatePropertyCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often the element's own description is being maintained.

**User action**

No action is required, but this message can be used to capture user activity information related to the updates of anchor elements.


----

### METADATA-OBSERVABILITY-0007

> User {0} deleted an attachment to {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The deletion counts towards the deleteAttachmentCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often content attached to the element is being removed, for example as it is reorganized or corrected.  It is not logged when the anchor element itself is deleted.

**User action**

No action is required, but this message can be used to capture user activity information related to the removal of content from an anchor element.


----

### METADATA-OBSERVABILITY-0008

> User {0} retrieved {1} anchor element {2} during search operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The retrieval counts towards the findCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often the element is returned by searches, which indicates how findable it is rather than whether it was chosen.

**User action**

No action is required, but this message can be used to capture user activity information relating to the anchor elements being retrieved through searches.


----

### METADATA-OBSERVABILITY-0009

> User {0} retrieved an attachment to {1} anchor element {2} during search operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_SEARCH_ATTACHMENT` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The retrieval counts towards the findCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  An element anchored to the anchor element that is returned by a search counts towards the findability of the anchor element itself.

**User action**

No action is required, but this message can be used to capture user activity information relating to the attachments of anchor elements being retrieved through searches.


----

### METADATA-OBSERVABILITY-0010

> User {0} issued REST API call to operation {1} of service {2} on server {3}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.USER_REQUEST_ACTIVITY` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/services/common-services/> |

**System action**

This message is used to capture user activity.

**User action**

No action is required, but this message can be used to capture which services and operations a user is calling.


----

### METADATA-OBSERVABILITY-0011

> User {0} deleted feedback related to {1} anchor element {2} during operation {3} of service {4}

|  |  |
|---|---|
| **Java constant** | `OpenMetadataObservabilityAuditCode.ANCHOR_ACTIVITY_DELETE_FEEDBACK` |
| **Severity** | ACTIVITY - This log record contains user activity information such as the requests being made and the metadata being accessed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |
| **Further reading** | <https://egeria-project.org/types/7/0795-Usage-Activity/> |

**System action**

The deletion counts towards the deleteFeedbackCount in the anchor element's UsageActivity classification (maintained by the Pepys Activity Observer).  This count shows how often comments, likes, ratings and informal tags attached to the element are being withdrawn.

**User action**

No action is required, but this message can be used to capture user activity information related to the removal of feedback from an anchor element.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

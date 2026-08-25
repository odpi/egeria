<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# AtlasIntegrationAuditCode

The AtlasIntegrationAuditCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 36 |
| **Message identifiers begin** | `APACHE-ATLAS-INTEGRATION-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors) |
| **Source** | [AtlasIntegrationAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/system-connectors/apache-atlas-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/apacheatlas/integration/ffdc/AtlasIntegrationAuditCode.java) |
| **Further reading** | <https://egeria-project.org/egeria-solutions/leveraging-apache-atlas/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0001](#apache-atlas-integration-connector-0001) | INFO | The {0} integration connector has been initialized to publish all glossary terms to the Apache Atlas server at URL {1} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0002](#apache-atlas-integration-connector-0002) | INFO | The {0} integration connector has been initialized to publish glossary terms from glossary {2} in the Apache Atlas server at URL {1} to the open metadata ecosystem |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0003](#apache-atlas-integration-connector-0003) | INFO | The {0} integration connector has been initialized to publish all glossary terms from the Apache Atlas server at URL {1} to the open metadata ecosystem |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0004](#apache-atlas-integration-connector-0004) | INFO | The {0} integration connector has been initialized to publish glossary terms from glossary {2} to the Apache Atlas server at URL {1} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0005](#apache-atlas-integration-connector-0005) | EXCEPTION | The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0006](#apache-atlas-integration-connector-0006) | INFO | The {0} integration connector cannot retrieve the requested {1} glossary from the open metadata ecosystem |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0007](#apache-atlas-integration-connector-0007) | INFO | The {0} integration connector us unable to retrieve requested {1} glossary from Apache Atlas |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0008](#apache-atlas-integration-connector-0008) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0009](#apache-atlas-integration-connector-0009) | INFO | The {0} integration connector has stopped its monitoring of Apache Atlas at {1} and is shutting down |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0010](#apache-atlas-integration-connector-0010) | INFO | The open metadata glossary {0} equivalent for Apache Atlas glossary {1} is missing; removing Apache Atlas copy |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0011](#apache-atlas-integration-connector-0011) | INFO | The open metadata glossary term {0} equivalent for Apache Atlas glossary term {1} is missing; removing Apache Atlas copy |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0013](#apache-atlas-integration-connector-0013) | ERROR | The equivalent Apache Atlas GUID for {0} open metadata element {1} is not stored as an external identifier |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0014](#apache-atlas-integration-connector-0014) | ERROR | The open metadata glossary {0} for equivalent Apache Atlas glossary {1} has been unilaterally deleted; connector {2} is putting it back |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0015](#apache-atlas-integration-connector-0015) | ERROR | The open metadata glossary term {0} for equivalent Apache Atlas glossary term {1} has been unilaterally deleted; connector {2} is putting it back |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0017](#apache-atlas-integration-connector-0017) | INFO | {0} integration connector will use the default value of {1} for configuration property {2} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0018](#apache-atlas-integration-connector-0018) | INFO | The list of values from the {0} configuration property has {1} items with values {2} for {3} integration connector |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0019](#apache-atlas-integration-connector-0019) | INFO | The {0} configuration property is set to {1} for {2} integration connector |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0033](#apache-atlas-integration-connector-0033) | EXCEPTION | The {0} integration connector encountered an {1} exception when defining a {2} open metadata type {3} in Apache Atlas.  The exception message included was {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0034](#apache-atlas-integration-connector-0034) | EXCEPTION | The {0} integration connector encountered an {1} exception when retrieving/setting up the classification reference set called {2}.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0035](#apache-atlas-integration-connector-0035) | EXCEPTION | The {0} integration connector encountered an {1} exception when retrieving/setting up the members of the classification reference set called {2}.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0036](#apache-atlas-integration-connector-0036) | EXCEPTION | The {0} integration connector encountered an {1} exception when setting up Classifications in Apache Atlas using the members of the classification reference set called {2}.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0037](#apache-atlas-integration-connector-0037) | EXCEPTION | The {0} integration connector encountered an {1} exception when retrieving/setting up the members of the classification reference set called {2} using the classifications from Apache Atlas.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0038](#apache-atlas-integration-connector-0038) | INFO | The {0} integration connector is calling the {1} integration module |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0039](#apache-atlas-integration-connector-0039) | ERROR | The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0040](#apache-atlas-integration-connector-0040) | INFO | The integration connector {0} created open metadata {1} entity {2} match Apache Atlas {3} entity {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0042](#apache-atlas-integration-connector-0042) | INFO | The integration connector {0} is synchronizing Apache Atlas {1} entity {2} to {3} open metadata entity {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0043](#apache-atlas-integration-connector-0043) | INFO | The integration connector {0} is synchronizing open metadata {1} entity {2} to the {3} Apache Atlas entity {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0044](#apache-atlas-integration-connector-0044) | INFO | The integration connector {0} is deleting {1} open metadata entity {2} since Apache Atlas entity {3} has been removed |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0046](#apache-atlas-integration-connector-0046) | ERROR | The integration connector {0} is replacing {1} open metadata entity {2} for Apache Atlas entity {3} since the open metadata entity has been unilaterally removed |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0048](#apache-atlas-integration-connector-0048) | INFO | The integration connector {0} is adding a DataFlow lineage relationship from {1} open metadata entity {2} to {3} open metadata entity {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0049](#apache-atlas-integration-connector-0049) | EXCEPTION | The {0} integration connector encountered an {1} exception when retrieving the related reference values assigned to entity {2}.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0050](#apache-atlas-integration-connector-0050) | EXCEPTION | The {0} integration connector encountered an {1} exception when setting up the related reference values assigned to the open metadata entity {2} that represents Apache Atlas entity {3}.  The exception message included was {4} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0051](#apache-atlas-integration-connector-0051) | INFO | The integration connector {0} is adding a ReferenceValueAssignment relationship from {1} open metadata entity {2} to reference value {3} to represent the Apache Atlas {4} classification on entity {5} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0052](#apache-atlas-integration-connector-0052) | EXCEPTION | The {0} integration connector encountered an {1} exception when retrieving the related elements linked to entity {2}.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0054](#apache-atlas-integration-connector-0054) | EXCEPTION | The {0} integration connector encountered an {1} exception when processing the related elements linked to entity {2}.  The exception message included was {3} |
| [APACHE-ATLAS-INTEGRATION-CONNECTOR-0055](#apache-atlas-integration-connector-0055) | INFO | The {0} integration connector can not retrieve the atlas GUID for {1} open metadata entity {2} and metadata collection {3} |

----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0001

> The {0} integration connector has been initialized to publish all glossary terms to the Apache Atlas server at URL {1}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_ALL_EGERIA_GLOSSARIES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is designed to publish changes to all active glossary terms to equivalent Apache Atlas glossaries.

**User action**

No specific action is required.  This message is to confirm the configuration for the integration connector allows all open metadata glossaries to be published to Apache Atlas.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0002

> The {0} integration connector has been initialized to publish glossary terms from glossary {2} in the Apache Atlas server at URL {1} to the open metadata ecosystem

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC_EGERIA_GLOSSARIES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is designed to publish changes to all active glossary terms from the named glossary to an equivalent glossary on Apache Atlas.

**User action**

No specific action is required.  This message is to confirm the configuration for the integration connector limits which open metadata glossaries are to be published to Apache Atlas.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0003

> The {0} integration connector has been initialized to publish all glossary terms from the Apache Atlas server at URL {1} to the open metadata ecosystem

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_ALL_ATLAS_GLOSSARIES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is designed to publish changes to all glossary terms from the Apache Atlas glossaries to Egeria.

**User action**

No specific action is required.  This message is to confirm the configuration for the integration connector allows all Atlas glossaries to be published to the open metadata ecosystem.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0004

> The {0} integration connector has been initialized to publish glossary terms from glossary {2} to the Apache Atlas server at URL {1}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONNECTOR_CONFIGURATION_SPECIFIC_ATLAS_GLOSSARIES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector is designed to publish changes to all active glossary terms from the name glossary to an equivalent glossary on Apache Atlas.

**User action**

No specific action is required.  This message is to confirm the configuration for the integration connector limits which Apache Atlas glossaries are to be published to the open metadata ecosystem.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0005

> The {0} integration connector encountered an {1} exception when connecting to {2} during the {3} method.  The exception message included was {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.BAD_CONFIGURATION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The exception is passed back to the integration daemon that is hosting this connector to enable it to perform error handling.  More messages are likely to follow describing the error handling that was performed.  These can help to determine how to recover from this error

**User action**

This message contains the exception that was the original cause of the problem. Use the information from the exception stack trace to determine why the connector is not able to access the event broker and resolve that issue.  Use the messages that where subsequently logged during the error handling to discover how to restart the connector in the integration daemon once the original cause of the error has been corrected.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0006

> The {0} integration connector cannot retrieve the requested {1} glossary from the open metadata ecosystem

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_RETRIEVE_EGERIA_GLOSSARY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

Synchronization of the requested glossary is skipped until the requested glossary has been created.

**User action**

Check that the configured glossary name is correct.  Check that the failure to retrieve the glossary is expected.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0007

> The {0} integration connector us unable to retrieve requested {1} glossary from Apache Atlas

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_RETRIEVE_ATLAS_GLOSSARY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

Synchronization of the requested Apache Atlas glossary is skipped until the requested glossary has been created.

**User action**

Check that the configured glossary name is correct.  Check that the failure to retrieve the glossary from Apache Atlas is expected.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0008

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0009

> The {0} integration connector has stopped its monitoring of Apache Atlas at {1} and is shutting down

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0010

> The open metadata glossary {0} equivalent for Apache Atlas glossary {1} is missing; removing Apache Atlas copy

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.EGERIA_GLOSSARY_DELETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

An open metadata glossary has been deleted.  It has been copied to Apache Atlas in the past.  The Atlas glossary needs to be deleted too.

**User action**

This is not necessarily an error, unless the open metadata glossary should not have been deleted.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0011

> The open metadata glossary term {0} equivalent for Apache Atlas glossary term {1} is missing; removing Apache Atlas copy

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.EGERIA_GLOSSARY_TERM_DELETED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

An open metadata glossary term has been deleted.  It has been copied to Apache Atlas in the past.  The Atlas glossary term needs to be deleted too.

**User action**

This is not necessarily an error, unless the open metadata glossary term should not have been deleted.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0013

> The equivalent Apache Atlas GUID for {0} open metadata element {1} is not stored as an external identifier

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.EGERIA_GUID_MISSING` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The open metadata element is marked as originating from Apache Atlas.  The unique identifier (GUID) of the original Apache Atlas element is not stored in the open metadata element as an external identifier which means it can not be resynchronized with Apache Atlas.

**User action**

This error occurs if the external identifier has been removed from the open metadata element.  To enable synchronization again, either delete the open metadata element and allow it to be recreated in the next refresh scan, or determine the correct Apache Atlas GUID and store it as an external identifier in the open metadata element.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0014

> The open metadata glossary {0} for equivalent Apache Atlas glossary {1} has been unilaterally deleted; connector {2} is putting it back

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.REPLACING_EGERIA_GLOSSARY` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The open metadata glossary can not be retrieved.  This glossary is owned by Apache Atlas.  The connector is creating a new copy of the Apache Atlas glossary in the open metadata ecosystem.

**User action**

Open metadata glossary elements that are copies from Apache Atlas should not be unilaterally removed.  Investigate why this element is missing from the open metadata ecosystem and make changes so it can not happen again.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0015

> The open metadata glossary term {0} for equivalent Apache Atlas glossary term {1} has been unilaterally deleted; connector {2} is putting it back

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.REPLACING_EGERIA_GLOSSARY_TERM` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The open metadata glossary term can not be retrieved.  This glossary term is owned by Apache Atlas.  The connector is creating a new copy of the Apache Atlas glossary term in the open metadata ecosystem.

**User action**

Open metadata glossary terms that are copies from Apache Atlas should not be unilaterally removed.  Investigate why this element is missing from the open metadata ecosystem and make changes so it can not happen again.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0017

> {0} integration connector will use the default value of {1} for configuration property {2}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_NOT_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will use the default value for this property.

**User action**

Check that this default behaviour is what is wanted from the integration connector.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0018

> The list of values from the {0} configuration property has {1} items with values {2} for {3} integration connector

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.LIST_CONFIGURATION_PROPERTY_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will use the listed values to control its behaviour.

**User action**

Check that this list of items is what is expected.  It is created by a parsing routine and it is important to ensure that the values are what is expected.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0019

> The {0} configuration property is set to {1} for {2} integration connector

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CONFIGURATION_PROPERTY_SET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector will use the value shown to control its behaviour.

**User action**

Check that this value is what is expected.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0033

> The {0} integration connector encountered an {1} exception when defining a {2} open metadata type {3} in Apache Atlas.  The exception message included was {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_DEFINE_TYPE_IN_ATLAS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector continues to scan and synchronize metadata as configured.  However, some metadata may not be copied due to this missing type.

**User action**

Review the exception to uncover why the type can not be defined and correct the issue.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0034

> The {0} integration connector encountered an {1} exception when retrieving/setting up the classification reference set called {2}.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_SET_UP_CLASSIFICATION_REFERENCE_SET` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry this request on the next refresh.

**User action**

Use the information in the exception to determine why it is not possible to either set up or retrieve the configured classification reference set.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0035

> The {0} integration connector encountered an {1} exception when retrieving/setting up the members of the classification reference set called {2}.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_GET_CLASSIFICATION_REFERENCE_SET_MEMBERS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry this retrieval on the next refresh.

**User action**

Use the information in the exception to determine why it is not possible to retrieve the members of the configured classification reference set.  Note: the problem is not caused by an empty classification reference set.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0036

> The {0} integration connector encountered an {1} exception when setting up Classifications in Apache Atlas using the members of the classification reference set called {2}.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_ADD_CLASSIFICATION_REFERENCE_SET_TO_ATLAS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry to add the classification to Apache Atlas on the next refresh.

**User action**

Use the information in the exception to determine why it is not possible to set up classifications in Apache Atlas.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0037

> The {0} integration connector encountered an {1} exception when retrieving/setting up the members of the classification reference set called {2} using the classifications from Apache Atlas.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_BUILD_CLASSIFICATION_REFERENCE_SET_FROM_ATLAS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry to build the classification reference set on the next refresh.

**User action**

Use the information in the exception to determine why it is not possible to build the membership of the configured classification reference set using classifications from Apache Atlas.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0038

> The {0} integration connector is calling the {1} integration module

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.SYNC_INTEGRATION_MODULE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector is calling one of its registered integration modules to refresh the metadata it is responsible for.

**User action**

No action is required.  This message is to record that the connector is working it way through the registered integration modules.  If an error occurs this message helps to identify which module experienced the error.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0039

> The {0} integration connector can not retrieve the correlation information for {1} open metadata entity {2} linked in Apache Atlas {3} to {4} entity {5}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.MISSING_CORRELATION` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The correlation information that should be associated with the open metadata entity is missing and the integration connector is not able to confidently synchronize it with the Apache Atlas entity.

**User action**

Review the audit log to determine if there were errors detected when the open metadata entity was created.  The simplest resolution is to delete the open metadata entity.  However, if this entity has been enhanced with many attachments and classifications then it is also possible to add the correlation information to the open metadata entity to allow the synchronization to continue.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0040

> The integration connector {0} created open metadata {1} entity {2} match Apache Atlas {3} entity {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is has created the open metadata entity with information from the Apache Atlas entity.

**User action**

No action is required. The connector working to ensure the open metadata ecosystem can store metadata from Apache Atlas.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0042

> The integration connector {0} is synchronizing Apache Atlas {1} entity {2} to {3} open metadata entity {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is updating the open metadata entity with information from the Apache Atlas entity.

**User action**

No action is required. The connector working to keep the Open metadata entity consistent with its Apache Atlas equivalent.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0043

> The integration connector {0} is synchronizing open metadata {1} entity {2} to the {3} Apache Atlas entity {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UPDATING_ATLAS_ENTITY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is updating the Apache Atlas entity with information from the open metadata entity.

**User action**

No action is required. The connector working to keep the Apache Atlas entity consistent with the open metadata one.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0044

> The integration connector {0} is deleting {1} open metadata entity {2} since Apache Atlas entity {3} has been removed

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.DELETING_EGERIA_ENTITY` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is deleting the open metadata entity because the Apache Atlas entity where its content is sourced from has gone.

**User action**

No action is required. The connector is working to keep the two systems consistent.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0046

> The integration connector {0} is replacing {1} open metadata entity {2} for Apache Atlas entity {3} since the open metadata entity has been unilaterally removed

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector is creating a new open metadata entity to represent the Apache Atlas entity in the open metadata ecosystem.  This is because the entity originated in Apache Atlas and this is the proper place to delete the entity.

**User action**

Investigate why the connector can not retrieve the original open metadata entity.  Has it been deleted, archived or moved to a governance zone that is not visible to this connector?  Make changes to ensure the open metadata entities synchronized from Apache Atlas are only maintained by this connector.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0048

> The integration connector {0} is adding a DataFlow lineage relationship from {1} open metadata entity {2} to {3} open metadata entity {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.ADDING_LINEAGE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector is creating a new lineage relationship around a process based on a similar relationship in Apache Atlas.

**User action**

No action is required. The connector is working to keep the two systems view of lineage consistent.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0049

> The {0} integration connector encountered an {1} exception when retrieving the related reference values assigned to entity {2}.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_GET_REFERENCE_VALUE_ASSIGNMENTS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry this reference value retrieval request on the next refresh.  These reference values are used to record the classifications attached to the corresponding Apache Atlas entity.

**User action**

Use the information in the exception to determine why it is not possible to retrieve the reference values.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0050

> The {0} integration connector encountered an {1} exception when setting up the related reference values assigned to the open metadata entity {2} that represents Apache Atlas entity {3}.  The exception message included was {4}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_SET_REFERENCE_VALUE_ASSIGNMENTS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}` |

**System action**

The connector will retry this reference value assignment request on the next refresh.  These reference values are used to record the classifications attached to the corresponding Apache Atlas entity.

**User action**

Use the information in the exception to determine why it is not possible to assign the reference values.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0051

> The integration connector {0} is adding a ReferenceValueAssignment relationship from {1} open metadata entity {2} to reference value {3} to represent the Apache Atlas {4} classification on entity {5}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.ASSIGNING_REFERENCE_VALUE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector is creating a new relationship to indicate the presence of a specific classification in Apache Atlas.

**User action**

No action is required. The connector is working to keep the two systems view of the use of classifications consistent.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0052

> The {0} integration connector encountered an {1} exception when retrieving the related elements linked to entity {2}.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_GET_RELATED_ELEMENTS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry this related elements retrieval request on the next refresh.  These related elements are used to augment the metadata attached to the corresponding Apache Atlas entity.

**User action**

Use the information in the exception to determine why it is not possible to retrieve the related elements.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0054

> The {0} integration connector encountered an {1} exception when processing the related elements linked to entity {2}.  The exception message included was {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.UNABLE_TO_PROCESS_RELATED_ELEMENTS` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector will retry the calls to process related elements on the next refresh.  These related elements are used to augment the metadata attached to the corresponding Apache Atlas entity.

**User action**

Use the information in the exception to determine why it is not possible to process the related elements.


----

### APACHE-ATLAS-INTEGRATION-CONNECTOR-0055

> The {0} integration connector can not retrieve the atlas GUID for {1} open metadata entity {2} and metadata collection {3}

|  |  |
|---|---|
| **Java constant** | `AtlasIntegrationAuditCode.MISSING_ATLAS_GUID` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

There is no Apache Atlas correlation information for this element.

**User action**

Review the follow on messages.  If there are none, it is just a timing issue.  If there are subsequent error messages then follow their instructions.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

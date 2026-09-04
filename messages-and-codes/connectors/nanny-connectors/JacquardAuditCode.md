<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# JacquardAuditCode

The JacquardAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 23 |
| **Message identifiers begin** | `JACQUARD-HARVESTER-` |
| **Java class** | `org.odpi.openmetadata.adapters.connectors.jacquard.ffdc.JacquardAuditCode` |
| **Module** | [open-metadata-implementation/adapters/open-connectors/nanny-connectors](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors) |
| **Source** | [JacquardAuditCode.java](../../../open-metadata-implementation/adapters/open-connectors/nanny-connectors/src/main/java/org/odpi/openmetadata/adapters/connectors/jacquard/ffdc/JacquardAuditCode.java) |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [JACQUARD-HARVESTER-0001](#jacquard-harvester-0001) | EXCEPTION | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [JACQUARD-HARVESTER-0002](#jacquard-harvester-0002) | ERROR | Integration connector {0} cannot determine if tabular data source {1} has changed since it has no last update time column |
| [JACQUARD-HARVESTER-0003](#jacquard-harvester-0003) | ERROR | Integration connector {0} cannot determine if tabular data source {1} has changed since it has no createTime column |
| [JACQUARD-HARVESTER-0006](#jacquard-harvester-0006) | ERROR | The {0} integration connector has no subscription manager configured: configuration property {1} is not set, so the products' notification types will not be handed to a subscription manager and their subscribers will not be notified |
| [JACQUARD-HARVESTER-0009](#jacquard-harvester-0009) | INFO | The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down |
| [JACQUARD-HARVESTER-0010](#jacquard-harvester-0010) | TRACE | The {0} integration connector has created a new {1} supporting definition called {2} with GUID {3} |
| [JACQUARD-HARVESTER-0011](#jacquard-harvester-0011) | INFO | The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products |
| [JACQUARD-HARVESTER-0012](#jacquard-harvester-0012) | INFO | The {0} integration connector has created a new digital product {1} called {2} |
| [JACQUARD-HARVESTER-0027](#jacquard-harvester-0027) | INFO | The {0} integration connector removed {1} duplicate catalog target relationship(s) from its own catalog targets, leaving one for each of its {2} product asset(s) |
| [JACQUARD-HARVESTER-0025](#jacquard-harvester-0025) | TRACE | The {0} integration connector has updated the {1} supporting definition called {2} with GUID {3} |
| [JACQUARD-HARVESTER-0026](#jacquard-harvester-0026) | TRACE | The {0} integration connector is unlinking {1} element {2} from {3} element {4} to remove relationship {5} |
| [JACQUARD-HARVESTER-0014](#jacquard-harvester-0014) | TRACE | The {0} integration connector is linking {1} element {2} to {3} element {4} using relationship {5} |
| [JACQUARD-HARVESTER-0015](#jacquard-harvester-0015) | TRACE | The {0} integration connector has retrieved a new {1} supporting definition called {2} with GUID {3} |
| [JACQUARD-HARVESTER-0016](#jacquard-harvester-0016) | INFO | The {0} integration connector has retrieved an existing digital product {1} called {2} |
| [JACQUARD-HARVESTER-0018](#jacquard-harvester-0018) | INFO | The {0} integration connector has updated an existing digital product {1} called {2} |
| [JACQUARD-HARVESTER-0019](#jacquard-harvester-0019) | INFO | The {0} integration connector is refreshing the {1} data set for digital product {2} |
| [JACQUARD-HARVESTER-0020](#jacquard-harvester-0020) | INFO | The {0} integration connector is refreshing the {1} is maintaining the DataScope classification for the {1} data set for digital product {2} |
| [JACQUARD-HARVESTER-0021](#jacquard-harvester-0021) | INFO | The {0} integration connector is harvesting valid metadata values - this may take some time ... |
| [JACQUARD-HARVESTER-0022](#jacquard-harvester-0022) | INFO | The {0} integration connector is harvesting reference data sets - this may take some time ... |
| [JACQUARD-HARVESTER-0023](#jacquard-harvester-0023) | INFO | The {0} integration connector is creating connectors to access the data in existing products - this may take some time ... |
| [JACQUARD-HARVESTER-0024](#jacquard-harvester-0024) | INFO | The {0} integration connector has linked its solution component {1} ({2}) to the equivalent solution component {3} as a validated duplicate |
| [JACQUARD-HARVESTER-0029](#jacquard-harvester-0029) | ERROR | The {0} integration connector found more than one element with the qualified name of {1} while cataloguing {2}; the copies have been linked as peer duplicates for the duplicate manager and {2} is skipped until they are resolved |
| [JACQUARD-HARVESTER-0028](#jacquard-harvester-0028) | ERROR | The {0} integration connector cannot read {1} as a date at record {2}, column {3} of the {4} data set for digital product {5}; values that are not dates are ignored when working out when the data set was created and last updated |

----

### JACQUARD-HARVESTER-0001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.UNEXPECTED_EXCEPTION` |
| **Severity** | EXCEPTION - An unexpected exception occurred. Details of the exception and stack trace are included in the log record. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector cannot catalog one or more metadata elements in the metadata repository.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### JACQUARD-HARVESTER-0002

> Integration connector {0} cannot determine if tabular data source {1} has changed since it has no last update time column

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.NO_LAST_UPDATE_DATE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The integration connector skips this data source.

**User action**

Update the data source to ensure it has a column called 'updateTime'.


----

### JACQUARD-HARVESTER-0003

> Integration connector {0} cannot determine if tabular data source {1} has changed since it has no createTime column

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.NO_CREATION_DATE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The integration connector skips this data source because of the missing create time column.

**User action**

Update the data source to ensure it has a column called 'createTime'.


----

### JACQUARD-HARVESTER-0006

> The {0} integration connector has no subscription manager configured: configuration property {1} is not set, so the products' notification types will not be handed to a subscription manager and their subscribers will not be notified

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.NO_SUBSCRIPTION_MANAGER` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector builds the products and their notification types, but no connector is looking after the notification types.  Subscriptions can be taken out, and nothing will deliver them.

**User action**

Set the configuration property to the unique identifier of the Baudot Subscription Manager integration connector.  The Open Metadata Digital Products content pack sets it when it defines this connector; a connector defined by hand needs it added.


----

### JACQUARD-HARVESTER-0009

> The {0} integration connector has stopped its monitoring of open metadata from server {1} on platform {2} and is shutting down

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.CONNECTOR_STOPPING` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is disconnecting.

**User action**

No action is required unless there are errors that follow indicating that there were problems shutting down.


----

### JACQUARD-HARVESTER-0010

> The {0} integration connector has created a new {1} supporting definition called {2} with GUID {3}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.CREATED_SUPPORTING_DEFINITION` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is creating the metadata elements that supports the definition of the Open Metadata Digital Product Catalog.

**User action**

No action is required.  This message is used to show the progress of the setup.


----

### JACQUARD-HARVESTER-0011

> The {0} integration connector is starting its harvesting of open metadata from server {1} on platform {2} into digital products

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.STARTING_CONNECTOR` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is initializing the definitions for the Open Metadata Digital Product Catalog.

**User action**

Monitor the set up of the catalog and the switch over to monitoring.


----

### JACQUARD-HARVESTER-0012

> The {0} integration connector has created a new digital product {1} called {2}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.NEW_OPEN_METADATA_PRODUCT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is setting up the fixed open metadata digital products.

**User action**

No action is required.  This message is for monitoring the set up of the fixed digital products.


----

### JACQUARD-HARVESTER-0027

> The {0} integration connector removed {1} duplicate catalog target relationship(s) from its own catalog targets, leaving one for each of its {2} product asset(s)

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.DUPLICATE_CATALOG_TARGETS_REMOVED` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

Earlier versions of this connector added a product's asset as a catalog target on every refresh, so each product accumulated one relationship per run and the connector started one processor per relationship.  The duplicates are removed and a product is now added once.

**User action**

No action is required.  This message appears once for a repository built by an earlier version of the connector, and not again.


----

### JACQUARD-HARVESTER-0025

> The {0} integration connector has updated the {1} supporting definition called {2} with GUID {3}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.UPDATED_SUPPORTING_DEFINITION` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector found a definition in the Open Metadata Digital Product Catalog that no longer described the deployment it is running in, and corrected it.

**User action**

No action is required.  This message records that the catalog has been brought back into line with the metadata access server that supplies it.


----

### JACQUARD-HARVESTER-0026

> The {0} integration connector is unlinking {1} element {2} from {3} element {4} to remove relationship {5}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.UNLINKING_ELEMENTS` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is removing a link that no longer describes the deployment it is running in, so that the corrected link can take its place.

**User action**

No action is required.  This message is for monitoring the maintenance of the Open Metadata Digital Product Catalog.


----

### JACQUARD-HARVESTER-0014

> The {0} integration connector is linking {1} element {2} to {3} element {4} using relationship {5}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.LINKING_ELEMENTS` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is linking product catalog elements together.

**User action**

No action is required.  This message is for monitoring the set up of the Open Metadata Digital Product Catalog.


----

### JACQUARD-HARVESTER-0015

> The {0} integration connector has retrieved a new {1} supporting definition called {2} with GUID {3}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.RETRIEVING_SUPPORTING_DEFINITION` |
| **Severity** | TRACE - This is additional information on the operation of the server that may be of assistance in debugging a problem. It is not normally logged to any destination, but can be added when needed. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is retrieving the metadata elements that supports the definition of the Open Metadata Digital Product Catalog.

**User action**

No action is required.  This message is used to show progress during the setup.


----

### JACQUARD-HARVESTER-0016

> The {0} integration connector has retrieved an existing digital product {1} called {2}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.RETRIEVING_OPEN_METADATA_PRODUCT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is retrieving the fixed open metadata digital products.

**User action**

No action is required.  This message is for monitoring the retrieval of the fixed digital products.


----

### JACQUARD-HARVESTER-0018

> The {0} integration connector has updated an existing digital product {1} called {2}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.UPDATED_OPEN_METADATA_PRODUCT` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is maintaining the fixed open metadata digital products.

**User action**

No action is required.  This message is for monitoring the updates to the fixed digital products.


----

### JACQUARD-HARVESTER-0019

> The {0} integration connector is refreshing the {1} data set for digital product {2}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.REFRESH_CATALOG_TARGET` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is reviewing whether a particular digital product has changed since it was last refreshed. Details of its review are attached to the data asset for the product using the DataScope classification.

**User action**

No action is required.  This message is for monitoring the refresh progress of the digital products.


----

### JACQUARD-HARVESTER-0020

> The {0} integration connector is refreshing the {1} is maintaining the DataScope classification for the {1} data set for digital product {2}

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.MAINTAINED_DATA_SCOPE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is maintaining the change record for a particular digital product. Details of its review are attached to the data asset for the product using the DataScope classification.

**User action**

No action is required.  This message is for monitoring the refresh activity of the digital products.


----

### JACQUARD-HARVESTER-0021

> The {0} integration connector is harvesting valid metadata values - this may take some time ...

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.HARVESTING_VALID_VALUES` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is reviewing the valid metadata sets in the open metadata ecosystem to discover if any new ones have been created.  If there are, it creates a new digital product for this set.

**User action**

No action is required beyond patience as this process can take 10+ minutes depending on how many valid metadata sets you have.  This message is to indicate that Jacquard has embarked on a potentially large piece of work.


----

### JACQUARD-HARVESTER-0022

> The {0} integration connector is harvesting reference data sets - this may take some time ...

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.HARVESTING_REFERENCE_DATA_SETS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is reviewing the reference data sets in the open metadata ecosystem to discover if any new ones have been created.  If there are, it creates a new digital product for this set.

**User action**

No action is required beyond patience as this process can take 10+ minutes depending on how many reference data sets you have.  This message is to indicate that Jacquard has embarked on a potentially large piece of work.


----

### JACQUARD-HARVESTER-0023

> The {0} integration connector is creating connectors to access the data in existing products - this may take some time ...

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.HARVESTING_CATALOG_TARGETS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector is creating connectors to each of the existing digital products in the open metadata ecosystem so they can be processed.

**User action**

No action is required beyond patience as this process can take 10+ minutes depending on how many digital products you have.  This message is to indicate that Jacquard has embarked on a potentially large piece of work.


----

### JACQUARD-HARVESTER-0024

> The {0} integration connector has linked its solution component {1} ({2}) to the equivalent solution component {3} as a validated duplicate

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.LINKING_DUPLICATE_SOLUTION_COMPONENTS` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |
| **Further reading** | <https://egeria-project.org/features/duplicate-management/overview/> |

**System action**

The connector has found another solution component with the same display name as one of its own solution components.  The two components are linked with a validated PeerDuplicateLink relationship and both are classified as KnownDuplicate so that the retrieval processing combines them.

**User action**

No action is required.  This message is for monitoring the alignment of the Open Metadata Digital Product Catalog's solution blueprint with the solution components supplied by the content packs.


----

### JACQUARD-HARVESTER-0029

> The {0} integration connector found more than one element with the qualified name of {1} while cataloguing {2}; the copies have been linked as peer duplicates for the duplicate manager and {2} is skipped until they are resolved

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.DUPLICATE_ELEMENT_DETECTED` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

Two writers created the same element at the same time - uniqueness of a qualified name cannot be guaranteed in a federated environment.  The metadata server has linked the copies with PeerDuplicateLink relationships in DISCOVERED status.  The connector leaves them to the Mendel Automated Duplicate Manager, which confirms close matches, refers the rest to a steward and consolidates them, and carries on with the rest of the catalogue.

**User action**

No action is required if the duplicate manager is deployed; the product is catalogued again once the copies have been combined.  If it is not deployed, a steward should review the peer duplicate links and remove or consolidate the copies.


----

### JACQUARD-HARVESTER-0028

> The {0} integration connector cannot read {1} as a date at record {2}, column {3} of the {4} data set for digital product {5}; values that are not dates are ignored when working out when the data set was created and last updated

|  |  |
|---|---|
| **Java constant** | `JacquardAuditCode.UNREADABLE_DATE_VALUE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |
| **Further reading** | <https://egeria-project.org/patterns/harvest-and-publish/overview/> |

**System action**

The connector expects a date column to hold an ISO-8601 instant, or a count of milliseconds since the epoch.  It skips the value and carries on with the rest of the data set, so the product's DataScope classification may be maintained from fewer records than it has.

**User action**

Check how the product's data set connector writes its date columns and correct it to write ISO-8601 instants.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OSFAuditCode

The OSFAuditCode is used to define the message content for the Audit Log.

|  |  |
|---|---|
| **Type of message** | Audit log messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `OPEN-SURVEY-` |
| **Java class** | `org.odpi.openmetadata.frameworks.opensurvey.ffdc.OSFAuditCode` |
| **Module** | [open-metadata-implementation/frameworks/open-survey-framework](../../open-metadata-implementation/frameworks/open-survey-framework) |
| **Source** | [OSFAuditCode.java](../../open-metadata-implementation/frameworks/open-survey-framework/src/main/java/org/odpi/openmetadata/frameworks/opensurvey/ffdc/OSFAuditCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/osf/overview/> |


## Messages

| Message Id | Severity | Message |
|---|---|---|
| [OPEN-SURVEY-0002](#open-survey-0002) | ERROR | The {0} Survey Action Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3} |
| [OPEN-SURVEY-0003](#open-survey-0003) | INFO | The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2} |
| [OPEN-SURVEY-0004](#open-survey-0004) | INFO | The survey action service {0} is overriding log file {1} |
| [OPEN-SURVEY-0005](#open-survey-0005) | ERROR | No information about the asset {0} has been returned from the asset store for survey action framework {1} |
| [OPEN-SURVEY-0006](#open-survey-0006) | ERROR | Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3} |
| [OPEN-SURVEY-0007](#open-survey-0007) | ERROR | Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3} |
| [OPEN-SURVEY-0008](#open-survey-0008) | ERROR | The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached |
| [OPEN-SURVEY-0009](#open-survey-0009) | ERROR | The {0} Survey Acton Service has been supplied with asset {1} which has no connection, so there is no way to reach the resource it describes |

----

### OPEN-SURVEY-0002

> The {0} Survey Action Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.WRONG_TYPE_OF_CONNECTOR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey cannot continue since it cannot work with the supplied connector.

**User action**

Use the details from the error message to determine the class of the connector.  Update the connector type associated with its Connection in the metadata store.


----

### OPEN-SURVEY-0003

> The survey action service {0} is creating log file {1} which is catalogued as CSVFile asset {2}

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.CREATING_LOG_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

This message tells the survey team that a particular survey log file is being created.

**User action**

No specific action is required.  The results are added to the log file and the asset for this log file is catalogued as a CSV file.


----

### OPEN-SURVEY-0004

> The survey action service {0} is overriding log file {1}

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.REUSING_LOG_FILE` |
| **Severity** | INFO - The server is providing information about its normal operation. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

This message warns the survey team that a particular survey log file is being reused.

**User action**

No specific action is required.  The new results are appended to the existing results.


----

### OPEN-SURVEY-0005

> No information about the asset {0} has been returned from the asset store for survey action framework {1}

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.NO_ASSET` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The service terminates without running the requested function.

**User action**

This is an unexpected condition because the lack of an asset should have been caught before this point.


----

### OPEN-SURVEY-0006

> Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.WRONG_TYPE_OF_ASSET` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey action service terminates.

**User action**

The caller has requested a governance request type that is incompatible with the type of the asset that has been supplied.  This problem could be resolved by issuing the survey request with a governance request type that is compatible with the asset, or changing the survey action service associated with the governance request type to one that supports this type of asset.


----

### OPEN-SURVEY-0007

> Asset {0} has a root schema of type {1} but survey action service {2} only supports the following root schema type(s): {3}

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.INVALID_ROOT_SCHEMA_TYPE` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey action service terminates because it can not proceed.

**User action**

The caller has requested a governance request type that cannot process a root schema for an asset because its type is unsupported.  This problem could be resolved by issuing the survey request with a governance request type that is compatible with the asset's schema, or changing the survey action service associated with the governance request type to one that supports this type of schema.


----

### OPEN-SURVEY-0008

> The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.NO_SCHEMA` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The survey cannot continue since it cannot assess whether the data stored in the associated resource matches the desired schema.

**User action**

Update the asset to include the desired schema and re-run this survey.  If you want to discover the asset's schema then use a different survey service.


----

### OPEN-SURVEY-0009

> The {0} Survey Acton Service has been supplied with asset {1} which has no connection, so there is no way to reach the resource it describes

|  |  |
|---|---|
| **Java constant** | `OSFAuditCode.NO_ASSET_CONNECTOR` |
| **Severity** | ERROR - An error occurred. This may restrict some of the server's operations. |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The survey cannot continue since it has no means of opening the resource to survey it.

**User action**

Check that the asset has a Connection attached to it, and that the connection is visible to the userId the survey is running under.  An asset catalogued without a connection - or one whose connection was not copied when the asset was created from a template - describes a resource that nothing can open.  Attach a connection to the asset and re-run this survey.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

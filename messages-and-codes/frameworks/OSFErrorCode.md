<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OSFErrorCode

The OSF error code is used to define first failure data capture (FFDC) for errors that occur when working with OSF Discovery Services. It is used in conjunction with all OSF Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 8 |
| **Message identifiers begin** | `OPEN-SURVEY-` |
| **Java class** | `org.odpi.openmetadata.frameworks.opensurvey.ffdc.OSFErrorCode` |
| **Module** | [open-metadata-implementation/frameworks/open-survey-framework](../../open-metadata-implementation/frameworks/open-survey-framework) |
| **Source** | [OSFErrorCode.java](../../open-metadata-implementation/frameworks/open-survey-framework/src/main/java/org/odpi/openmetadata/frameworks/opensurvey/ffdc/OSFErrorCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/osf/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OPEN-SURVEY-400-001](#open-survey-400-001) | 400 | No survey context supplied to the survey action service {0} |
| [OPEN-SURVEY-400-002](#open-survey-400-002) | 400 | No embedded survey action services supplied to the survey action pipeline {0} |
| [OPEN-SURVEY-400-003](#open-survey-400-003) | 400 | No embedded survey action services supplied to the survey action pipeline {0} |
| [OPEN-SURVEY-400-005](#open-survey-400-005) | 400 | Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3} |
| [OPEN-SURVEY-400-006](#open-survey-400-006) | 400 | The {0} Survey Acton Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3} |
| [OPEN-SURVEY-400-007](#open-survey-400-007) | 400 | The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached |
| [OPEN-SURVEY-400-008](#open-survey-400-008) | 400 | The {0} Survey Acton Service has been supplied with asset {1} which has no connection, so there is no way to reach the resource it describes |
| [OPEN-SURVEY-500-001](#open-survey-500-001) | 500 | Unexpected exception in survey action service {0} of type {1} detected by method {2}.  The error message was {3} |

----

### OPEN-SURVEY-400-001

> No survey context supplied to the survey action service {0}

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.NULL_SURVEY_CONTEXT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The survey action service is not able to determine which asset to analyze.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the survey action service.  Once the cause is resolved, retry the survey action request.


----

### OPEN-SURVEY-400-002

> No embedded survey action services supplied to the survey action pipeline {0}

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.NO_EMBEDDED_SURVEY_ACTION_SERVICES` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The survey action pipeline is not able to survey action which survey action services to run.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the survey action pipeline service.  Once the cause is resolved, retry the survey action request.


----

### OPEN-SURVEY-400-003

> No embedded survey action services supplied to the survey action pipeline {0}

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.INVALID_EMBEDDED_SURVEY_ACTION_SERVICE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The survey action pipeline is not able to discover which survey action services to run.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the survey action pipeline service or the associated open survey action engine.  Once the cause is resolved, retry the survey action request.


----

### OPEN-SURVEY-400-005

> Asset {0} is of type {1} but survey action service {2} only supports the following asset type(s): {3}

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.INVALID_ASSET_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey action service terminates.

**User action**

The caller has requested a governance request type that is incompatible with the type of the asset that has been supplied.  This problem could be resolved by issuing the survey request with a governance request type that is compatible with the asset, or changing the survey action service associated with the governance request type to one that supports this type of asset.


----

### OPEN-SURVEY-400-006

> The {0} Survey Acton Service has been supplied with a resource connector of class {1} rather than class {2} for asset {3}

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.WRONG_TYPE_OF_CONNECTOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey cannot continue since it cannot work with the supplied connector.

**User action**

Use the details from the error message to determine the class of the connector.  Update the connector type associated with its Connection in the metadata store.


----

### OPEN-SURVEY-400-007

> The {0} Survey Acton Service has been supplied with asset {1} which does not have a schema attached

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.NO_SCHEMA` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The survey cannot continue since it cannot assess whether the data stored in the associated resource matches the desired schema.

**User action**

Update the asset to include the desired schema and re-run this survey.  If you want to discover the asset's schema then use a different survey service.


----

### OPEN-SURVEY-400-008

> The {0} Survey Acton Service has been supplied with asset {1} which has no connection, so there is no way to reach the resource it describes

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.NO_ASSET_CONNECTOR` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The survey cannot continue since it has no means of opening the resource to survey it.

**User action**

Check that the asset has a Connection attached to it, and that the connection is visible to the userId the survey is running under.  An asset catalogued without a connection - or one whose connection was not copied when the asset was created from a template - describes a resource that nothing can open.  Attach a connection to the asset and re-run this survey.


----

### OPEN-SURVEY-500-001

> Unexpected exception in survey action service {0} of type {1} detected by method {2}.  The error message was {3}

|  |  |
|---|---|
| **Java constant** | `OSFErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The survey action service failed during its operation.

**User action**

This may be a configuration or a code error.  Look for other error messages and review the code of the survey action service.  Once the cause is resolved, retry the survey action request.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

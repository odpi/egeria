<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OIFErrorCode

The OIFErrorCode is used to define the message content for the OMRS Audit Log.

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 7 |
| **Message identifiers begin** | `OIF-CONNECTOR-` |
| **Java class** | `org.odpi.openmetadata.frameworks.integration.ffdc.OIFErrorCode` |
| **Module** | [open-metadata-implementation/frameworks/open-integration-framework](../../open-metadata-implementation/frameworks/open-integration-framework) |
| **Source** | [OIFErrorCode.java](../../open-metadata-implementation/frameworks/open-integration-framework/src/main/java/org/odpi/openmetadata/frameworks/integration/ffdc/OIFErrorCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/oif/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OIF-CONNECTOR-400-001](#oif-connector-400-001) | 400 | The integration connector {0} has been configured to have its own thread to issue blocking calls but has not implemented the engage() method |
| [OIF-CONNECTOR-400-002](#oif-connector-400-002) | 400 | Catalog target {0} is of type {1} but the {2} connector only supports the following type(s): {3} |
| [OIF-CONNECTOR-400-003](#oif-connector-400-003) | 400 | Catalog target {0} has a connection that is missing property {1} and connector {2} cannot proceed |
| [OIF-CONNECTOR-400-004](#oif-connector-400-004) | 400 | Catalog target {0} has a connector of type {1} but the {2} connector only supports the following type(s) of connector: {3} |
| [OIF-CONNECTOR-500-001](#oif-connector-500-001) | 500 | The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3} |
| [OIF-CONNECTOR-500-003](#oif-connector-500-003) | 500 | The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3} |
| [OIF-CONNECTOR-500-004](#oif-connector-500-004) | 500 | The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6} |

----

### OIF-CONNECTOR-400-001

> The integration connector {0} has been configured to have its own thread to issue blocking calls but has not implemented the engage() method

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.ENGAGE_IMPLEMENTATION_MISSING` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The integration daemon created a separate thread for this connector to enable it to issue blocking calls.  It called the engage() method on this thread.  However, the default implementation of the engage() method has been invoked suggesting that either the dedicated thread is not needed or there is an error in the implementation of the connector.  The integration daemon will terminate the thread once the engage() method returns.

**User action**

If the connector does not need to issue blocking calls update the configuration to remove the need for the dedicated thread.  Otherwise update the integration connector's implementation to override the default engage() method implementation.


----

### OIF-CONNECTOR-400-002

> Catalog target {0} is of type {1} but the {2} connector only supports the following type(s): {3}

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.INVALID_CATALOG_TARGET_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips the catalog target.

**User action**

The caller has requested a connector work with the wrong type of element.  It should be reconfigured with the correct type of element and rerun.


----

### OIF-CONNECTOR-400-003

> Catalog target {0} has a connection that is missing property {1} and connector {2} cannot proceed

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.INVALID_CATALOG_TARGET_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector stops processing the catalog target.

**User action**

The caller has requested a connector work with a catalog target that has a connection that has missing information.  Correct the set up of the connection.


----

### OIF-CONNECTOR-400-004

> Catalog target {0} has a connector of type {1} but the {2} connector only supports the following type(s) of connector: {3}

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.INVALID_CONNECTOR_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector skips the catalog target because it is not able to communicate with the third party technology.

**User action**

The caller has requested a connector work with the wrong type of connector to the third party technology.  It should be reconfigured with the correct type of connector and rerun.


----

### OIF-CONNECTOR-500-001

> The {0} integration connector received an unexpected exception {1} during method {2}; the error message was: {3}

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.UNEXPECTED_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector cannot catalog one or more metadata elements.

**User action**

Use the details from the error message to determine the cause of the error and retry the request once it is resolved.


----

### OIF-CONNECTOR-500-003

> The {0} connector has detected a missing or invalid {1} property in method {2} - element is: {3}

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.BAD_OM_VALUE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector throws an exception to indicate that it should not continue.

**User action**

Check the templates that where used to create the element to be sure they include the correct value.  If the template is correct then check the element's history to discover which processing cleared the value, since this is where the problem was introduced.  Now decide if the test in the connector is incorrect, or the .


----

### OIF-CONNECTOR-500-004

> The {0} connector has detected that element {1} which should be of type {2} has bean properties of {3} rather than {4} in method {5} - element is {6}

|  |  |
|---|---|
| **Java constant** | `OIFErrorCode.BAD_OM_PROPERTY_TYPE` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}`, `{6}` |

**System action**

The connector throws an exception to indicate that it should not continue since something has gone very wrong with the connector or Open Metadata Framework since the connector is expecting an element of a certain type, with bean properties that match that type, but the OMF has returned something different.

**User action**

Check that the type of element is that which was expected.  If not, this may be a connector logic problem, or another process may have created additional metadata that has confused the connector. Compare the element retrieved through the OMF with its stored value.  If the stored value is correct, then the problem is in the OMF converters.  If the stored value is not correct then look at the element's history to understand which processing caused the problem.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

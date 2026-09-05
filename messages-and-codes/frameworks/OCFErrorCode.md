<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# OCFErrorCode

The OCF error code is used to define first failure data capture (FFDC) for errors that occur when working with OCF Connectors. It is used in conjunction with all OCF Exceptions, both Checked and Runtime (unchecked).

|  |  |
|---|---|
| **Type of message** | Exception messages |
| **Number of messages** | 26 |
| **Message identifiers begin** | `OCF-` |
| **Java class** | `org.odpi.openmetadata.frameworks.connectors.ffdc.OCFErrorCode` |
| **Module** | [open-metadata-implementation/frameworks/open-connector-framework](../../open-metadata-implementation/frameworks/open-connector-framework) |
| **Source** | [OCFErrorCode.java](../../open-metadata-implementation/frameworks/open-connector-framework/src/main/java/org/odpi/openmetadata/frameworks/connectors/ffdc/OCFErrorCode.java) |
| **Further reading** | <https://egeria-project.org/frameworks/ocf/overview/> |


## Messages

| Message Id | HTTP Code | Message |
|---|---|---|
| [OCF-CONNECTION-400-001](#ocf-connection-400-001) | 400 | Null connection object passed on request for new connector instance |
| [OCF-CONNECTION-400-003](#ocf-connection-400-003) | 400 | Null connectorType property passed in connection {0} |
| [OCF-CONNECTION-400-004](#ocf-connection-400-004) | 400 | Null Connector Provider passed in connection {0} |
| [OCF-CONNECTION-400-005](#ocf-connection-400-005) | 400 | Unknown Connector Provider class {0} passed in connection {1} |
| [OCF-CONNECTION-400-006](#ocf-connection-400-006) | 400 | Class {0} passed in connection {1} is not a Connector Provider |
| [OCF-CONNECTION-400-008](#ocf-connection-400-008) | 400 | Connector Provider class {0} passed in connection {1} resulted in a {2} exception with error message of {3} |
| [OCF-CONNECTION-400-011](#ocf-connection-400-011) | 400 | The {0} configuration property of {1} is set to an invalid date format.  Use dd/MM/yyyy/hh:mm:ss |
| [OCF-PROPERTIES-400-014](#ocf-properties-400-014) | 400 | No more elements in {0} iterator |
| [OCF-PROPERTIES-400-019](#ocf-properties-400-019) | 400 | Virtual connection {0} has no embedded connections |
| [OCF-STATISTICS-400-001](#ocf-statistics-400-001) | 400 | The {0} is already in use as a counter statistic and can not be used by the {1} method to {2} |
| [OCF-STATISTICS-400-002](#ocf-statistics-400-002) | 400 | The {0} is already in use as a property statistic and can not be used by the {1} method to {2} |
| [OCF-STATISTICS-400-003](#ocf-statistics-400-003) | 400 | The {0} is already in use as a timestamp statistic and can not be used by the {1} method to {2} |
| [OCF-CONNECTOR-400-005](#ocf-connector-400-005) | 400 | Asset {0} is of type {1} but the {2} connector only supports the following asset type(s): {3} |
| [OCF-CONNECTOR-400-006](#ocf-connector-400-006) | 400 | Asset {0} has a root schema of type {1} but connector {2} only supports the following root schema type(s): {3} |
| [OCF-CONNECTOR-400-007](#ocf-connector-400-007) | 400 | {0} asset {1} describes a resource called {2} which is of type {3} but connector {4} only supports the following type(s) of resources: {5} |
| [OCF-CONNECTOR-400-008](#ocf-connector-400-008) | 400 | {0} asset {1} describes a resource called {2} that does not exist |
| [OCF-CONNECTOR-400-009](#ocf-connector-400-009) | 400 | The {0} connector cannot proceed with is processing of {1} because the configuration property called {2} was not supplied |
| [OCF-CONNECTOR-400-010](#ocf-connector-400-010) | 400 | The {0} connector cannot proceed with is processing because the endpoint address is null |
| [OCF-CONNECTOR-400-011](#ocf-connector-400-011) | 400 | {0} element {1} does not exist |
| [OCF-CONNECTION-500-001](#ocf-connection-500-001) | 500 | OCF method detected an unexpected exception |
| [OCF-CONNECTOR-500-002](#ocf-connector-500-002) | 500 | No information about the asset {0} has been returned from the asset store for connector {1} |
| [OCF-CONNECTOR-500-006](#ocf-connector-500-006) | 500 | The class name for the connector is not set up |
| [OCF-CONNECTOR-500-007](#ocf-connector-500-007) | 500 | Unknown Connector Java class {0} for Connector {1} |
| [OCF-CONNECTOR-500-008](#ocf-connector-500-008) | 500 | Java class {0} for connector named {1} does not implement the Connector interface |
| [OCF-CONNECTION-500-010](#ocf-connection-500-010) | 500 | Invalid Connector class {0} for connector {1}; resulting exception {2} produced message {3} |
| [OCF-CONNECTION-500-011](#ocf-connection-500-011) | 500 | Connector Provider {0} returned a null connector instance for connection {1} |

----

### OCF-CONNECTION-400-001

> Null connection object passed on request for new connector instance

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NULL_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | none |

**System action**

The system cannot create the requested connector instance without the connection information that describes which type of connector is required.

**User action**

Recode call to system to include a correctly formatted connection object and retry the request.


----

### OCF-CONNECTION-400-003

> Null connectorType property passed in connection {0}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NULL_CONNECTOR_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the requested connector instance without information on the type of connection required.

**User action**

Update the connection configuration to include a valid connectorType definition.  Then retry the request.


----

### OCF-CONNECTION-400-004

> Null Connector Provider passed in connection {0}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NULL_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The system cannot create the requested connector instance because the connectorType does not name the factory class that builds it.

**User action**

Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.


----

### OCF-CONNECTION-400-005

> Unknown Connector Provider class {0} passed in connection {1}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.UNKNOWN_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot create the requested connector instance because the Connector Provider's class is not known to the JVM.  This may be because the Connector Provider's jar is not installed in the local JVM or the wrong Java class name has been configured in the connection.

**User action**

Verify that the Connector Provider and Connector jar files are properly configured in the process.  Update the connection configuration to include a valid Java class name for the connector provider in the connectorProviderClassName property of the connection's connectorType. Then retry the request.


----

### OCF-CONNECTION-400-006

> Class {0} passed in connection {1} is not a Connector Provider

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NOT_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot create the requested connector instance because the Connector Provider's class does not implement org.odpi.openmetadata.ConnectorProvider.

**User action**

Update the connection configuration so that the connectorProviderClassName property of the connection's connectorType names a class that implements ConnectorProvider.  Then retry the request.


----

### OCF-CONNECTION-400-008

> Connector Provider class {0} passed in connection {1} resulted in a {2} exception with error message of {3}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.INVALID_CONNECTOR_PROVIDER` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot create the requested connector instance because the Connector Provider's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.

**User action**

Use the exception message to determine why the connector provider class failed to initialize.  Correct the class or its dependencies, or update the connection to use a different connector provider.  Then retry the request.


----

### OCF-CONNECTION-400-011

> The {0} configuration property of {1} is set to an invalid date format.  Use dd/MM/yyyy/hh:mm:ss

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.MALFORMED_DATE_CONFIGURATION_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot initialize the requested connector because the configuration property in the connection is not formatted correctly.

**User action**

Correct the configuration property into the connection object and retry the request.


----

### OCF-PROPERTIES-400-014

> No more elements in {0} iterator

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NO_MORE_ELEMENTS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

A caller stepping through an iterator has requested more elements when there are none left.

**User action**

Recode the caller to use the hasNext() method to check for more elements before calling next() and then retry.


----

### OCF-PROPERTIES-400-019

> Virtual connection {0} has no embedded connections

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.INVALID_VIRTUAL_CONNECTION` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The virtual connection properties object is invalid because it does not include any embedded connections.

**User action**

Add embedded connections to the virtual connection and retry the request.


----

### OCF-STATISTICS-400-001

> The {0} is already in use as a counter statistic and can not be used by the {1} method to {2}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.ALREADY_COUNTER_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration context returns an exception because the name is already recording a counter statistic.

**User action**

Change the connector logic to use a different name for the statistic.


----

### OCF-STATISTICS-400-002

> The {0} is already in use as a property statistic and can not be used by the {1} method to {2}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.ALREADY_PROPERTY_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration context returns an exception because the name is already recording a property statistic.

**User action**

Change the connector logic to use a name that is not already recording a property statistic.


----

### OCF-STATISTICS-400-003

> The {0} is already in use as a timestamp statistic and can not be used by the {1} method to {2}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.ALREADY_TIMESTAMP_NAME` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The integration context returns an exception because the name is already recording a timestamp statistic.

**User action**

Change the connector logic to use a name that is not already recording a timestamp statistic.


----

### OCF-CONNECTOR-400-005

> Asset {0} is of type {1} but the {2} connector only supports the following asset type(s): {3}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.INVALID_ASSET_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector terminates.

**User action**

The caller has requested a connector work with the wrong type of asset.  It should be reconfigured with the correct type of asset and rerun.


----

### OCF-CONNECTOR-400-006

> Asset {0} has a root schema of type {1} but connector {2} only supports the following root schema type(s): {3}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.INVALID_ROOT_SCHEMA_TYPE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The connector terminates because it can not proceed.

**User action**

The caller has requested a governance request type that cannot process a root schema for an asset because its type is unsupported.  This problem could be resolved by issuing the survey request with a governance request type that is compatible with the asset's schema, or changing the connector associated with the governance request type to one that supports this type of schema.


----

### OCF-CONNECTOR-400-007

> {0} asset {1} describes a resource called {2} which is of type {3} but connector {4} only supports the following type(s) of resources: {5}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.INVALID_RESOURCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}`, `{4}`, `{5}` |

**System action**

The connector terminates because it does not know how to process this type of resource.

**User action**

There is a mismatch between the asset in the open metadata catalog and the resource that it represents. Update the asset in the asset catalog so that it is matched with more appropriate services.


----

### OCF-CONNECTOR-400-008

> {0} asset {1} describes a resource called {2} that does not exist

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NO_RESOURCE` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector terminates because it does not have access to the resource.

**User action**

Ensure the resource is correctly identified in the asset. Rerun this request when the resource is created.


----

### OCF-CONNECTOR-400-009

> The {0} connector cannot proceed with is processing of {1} because the configuration property called {2} was not supplied

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.MISSING_CONFIGURATION_PROPERTY` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}`, `{2}` |

**System action**

The connector stop processing the named element.

**User action**

Update the source of the configuration properties.  This is typically in the connector's connection.  However, the configuration properties may be overridden by, say, the CatalogTarget relationship linking the connector to the resource it is processing.


----

### OCF-CONNECTOR-400-010

> The {0} connector cannot proceed with is processing because the endpoint address is null

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.MISSING_ENDPOINT_ADDRESS` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}` |

**System action**

The connector cannot access the digital resource it is supposed to connect to.

**User action**

Update the source of the endpoint.  This may be from a template or from a connector.


----

### OCF-CONNECTOR-400-011

> {0} element {1} does not exist

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.MISSING_ELEMENT` |
| **HTTP error code** | 400 - Bad Request - the caller has supplied invalid parameters |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector terminates because it can not find an element that it depends on.

**User action**

Ensure the element is correctly identified and exists in the metadata repository.  Then retry the request.


----

### OCF-CONNECTION-500-001

> OCF method detected an unexpected exception

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.CAUGHT_EXCEPTION` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

The system detected an error during connector processing.

**User action**

The root cause of the error is captured in previous reported messages.


----

### OCF-CONNECTOR-500-002

> No information about the asset {0} has been returned from the asset store for connector {1}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NO_ASSET` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The connector terminates without running the requested function.

**User action**

This is an unexpected condition because if the metadata server was unavailable, an exception would have been caught.


----

### OCF-CONNECTOR-500-006

> The class name for the connector is not set up

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NULL_CONNECTOR_CLASS` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | none |

**System action**

The system cannot create the requested connector instance without the name of the Java class for the connector.

**User action**

Update the implementation of the connector provider to ensure the connector's java class is initialized correctly


----

### OCF-CONNECTOR-500-007

> Unknown Connector Java class {0} for Connector {1}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.UNKNOWN_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot create the requested connector instance because the Connector's class is not known to the JVM.  This may be because the Connector Provider's jar is not installed in the local JVM or the wrong Java class name has been configured in the connection.

**User action**

Verify that the jar file containing the connector class is installed in the process alongside its connector provider.  Then retry the request.


----

### OCF-CONNECTOR-500-008

> Java class {0} for connector named {1} does not implement the Connector interface

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NOT_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system cannot create the requested connector instance because the Connector's class does not implement org.odpi.openmetadata.Connector.

**User action**

Correct the connector provider so that it creates a class that implements the Connector interface, or update the connection to use a connector provider that does.  Then retry the request.


----

### OCF-CONNECTION-500-010

> Invalid Connector class {0} for connector {1}; resulting exception {2} produced message {3}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.INVALID_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}`, `{2}`, `{3}` |

**System action**

The system cannot create the requested connector instance because the Connector's class is failing to initialize in the JVM.  This has resulted in an exception in the class loader.

**User action**

Verify that the Connector Provider and Connector jar files are properly configured in the process.  Then retry the request.


----

### OCF-CONNECTION-500-011

> Connector Provider {0} returned a null connector instance for connection {1}

|  |  |
|---|---|
| **Java constant** | `OCFErrorCode.NULL_CONNECTOR` |
| **HTTP error code** | 500 - Internal Server Error - an unexpected error occurred inside Egeria |
| **Message inserts** | `{0}`, `{1}` |

**System action**

The system detected an error during connector processing and was unable to create a connector.

**User action**

Review the connector provider's logic to determine why it returned no connector for this connection.


----

*This page is generated from the message set definitions in the Egeria source by the [messages-and-codes](../../open-metadata-resources/open-metadata-dev-utilities/messages-and-codes) utility.  Do not edit it by hand - change the message set and rebuild.*

License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.

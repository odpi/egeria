<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Creating an OCF connector to access a third party technology

When you want to connect to a tool or system from an existing service, you need to create an open connector for that
tool or system. For example, you might want to connect a new metadata repository into Egeria, or connect Egeria with
a new data processing engine.

To write an open connector you need to complete four steps:

1. Identify the properties for the [connection](../concepts/connection.md)
1. Write the [connector provider](../concepts/connector-provider.md)
1. Understand the interface to which the connector needs to implement
1. Write the [connector](../concepts/connector.md) itself

All of the code you write to implement these should exist in its own module, and as illustrated by the examples could
even be in its own independent code repository. Their implementation will have dependencies on Egeria's 

* Open Connector Framework (OCF)
* Audit Log Framework (ALF)
* Specific interfaces used by the type of connector
 
However there is no dependency on Egeria's OMAG Server Platform on these specific connector implementations
and they could run in another runtime that supported the connector APIs.

## 1. Identify the properties for the connection

Begin by identifying and designing the properties needed to connect to your tool or system. These will commonly
include a network address, protocol, and user credentials, but could also include other information.

## 2. Write the connector provider

The [connector provider](../concepts/connector-provider.md) is a simple Java factory that implements the creation of
the connector type it can instantiates using:

- a GUID for the [connector type](../concepts/connector-type.md)
- a name for the connector type
- a description of what the connector is for and how to configure it
- the connector class it instantiates
- a list of the additional properties, configuration properties and secured properties needed to configure instances of the connector

For example, the [DataStageConnectorProvider](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/datastage-adapter/src/main/java/org/odpi/egeria/connectors/ibm/datastage/dataengineconnector/DataStageConnectorProvider.java)
is used to instantiate connectors to IBM DataStage data processing engines. Therefore its name and description refer to
DataStage, and the connectors it instantiates are [DataStageConnectors](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/datastage-adapter/src/main/java/org/odpi/egeria/connectors/ibm/datastage/dataengineconnector/DataStageConnector.java).

Similarly, the [IGCOMRSRepositoryConnectorProvider](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/igc-adapter/src/main/java/org/odpi/egeria/connectors/ibm/igc/repositoryconnector/IGCOMRSRepositoryConnectorProvider.java)
is used to instantiate connectors to IBM Information Governance Catalog (IGC) metadata repositories. In contrast to the
DataStageConnectorProvider, the IGCOMRSRepositoryConnectorProvider's name and description refer to IGC, and the
connectors it instantiates are [IGCOMRSRepositoryConnectors](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/igc-adapter/src/main/java/org/odpi/egeria/connectors/ibm/igc/repositoryconnector/IGCOMRSRepositoryConnector.java).

Note that the code of all of these connector implementations exists outside Egeria itself (in separate code
repositories), and there are no dependencies within Egeria on these external repositories. 

All connectors can be configured with the network address and credential information needed to access the underlying
tool or system. Therefore, we do not need to explicitly list properties for such basic details. However, the names of
any additional configuration properties that may be useful to a specific type of connector can be described through
the `recognizedConfigurationProperties` of the connector type.

### The basic implementation pattern

From the two examples ([DataStageConnectorProvider](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/datastage-adapter/src/main/java/org/odpi/egeria/connectors/ibm/datastage/dataengineconnector/DataStageConnectorProvider.java)
and [IGCOMRSRepositoryConnectorProvider](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/igc-adapter/src/main/java/org/odpi/egeria/connectors/ibm/igc/repositoryconnector/IGCOMRSRepositoryConnectorProvider.java)),
you will see that writing a connector provider follows a simple pattern:

- Extend a _connector provider_ base class specific to your connector's interface.
- Define `static final` class members for the GUID, name, description and the names of any additional configuration
    properties.
- Write a single public constructor, with no parameters, that:
    - Calls `super.setConnectorClassName()` with the name of your connector class.
    - Creates a new `ConnectorType` object, sets it characteristics to the `static final` class members, and uses
        `.setConnectorProviderClassName()` to set the name of the connector provider class itself.
    - (Optional) Creates a list of additional configuration properties from the `static final` class members, and uses
        `.setRecognizedConfigurationProperties()` to add these to the connector type.
    - Sets `super.connectorTypeBean = connectorType`.

## 3. Understand the interface to which the connector needs to adhere

Now that we have the _connector provider_ to instantiate our _connectors_, we need to understand what our _connectors_
actually need to do. For a service to use our connector, the connector must provide a set of methods that are relevant
to that service.

For example, the [Data Engine Proxy Services](../../../../governance-servers/data-engine-proxy-services) integrate
metadata from data engines with Egeria. To integrate DataStage with Egeria, we want our [DataStageConnector](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/datastage-adapter/src/main/java/org/odpi/egeria/connectors/ibm/datastage/dataengineconnector/DataStageConnector.java)
to be used by the data engine proxy services. Therefore the connector needs to extend [DataEngineConnectorBase](../../../../governance-servers/data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineConnectorBase.java),
because this defines the methods needed by the data engine proxy services.

Likewise, we want our [IGCOMRSRepositoryConnector](https://github.com/odpi/egeria-connector-ibm-information-server/blob/master/igc-adapter/src/main/java/org/odpi/egeria/connectors/ibm/igc/repositoryconnector/IGCOMRSRepositoryConnector.java)
to integrate IGC with Egeria as a metadata repository. Therefore the connector needs to extend
[OMRSRepositoryConnector](../../../../repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/connectors/stores/metadatacollectionstore/repositoryconnector/OMRSRepositoryConnector.java),
because this defines the methods needed to integrate with Open Metadata Repository Services (OMRS).

How did we know to extend these base classes? The _connector provider_ implementations in the previous step each
extended a base class specific to the type of connector they provide ([DataEngineConnectorProviderBase](../../../../governance-servers/data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineConnectorProviderBase.java)
and [OMRSRepositoryConnectorProviderBase](../../../../repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/connectors/stores/metadatacollectionstore/repositoryconnector/OMRSRepositoryConnectorProviderBase.java)).
These _connector_ base classes ([DataEngineConnectorBase](../../../../governance-servers/data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineConnectorBase.java)
and [OMRSRepositoryConnector](../../../../repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/connectors/stores/metadatacollectionstore/repositoryconnector/OMRSRepositoryConnector.java))
are in the same package structure as those _connector provider_ base classes.

In both cases, by extending the abstract classes ([DataEngineConnectorBase](../../../../governance-servers/data-engine-proxy-services/data-engine-proxy-connector/src/main/java/org/odpi/openmetadata/governanceservers/dataengineproxy/connectors/DataEngineConnectorBase.java)
and [OMRSRepositoryConnector](../../../../repository-services/repository-services-apis/src/main/java/org/odpi/openmetadata/repositoryservices/connectors/stores/metadatacollectionstore/repositoryconnector/OMRSRepositoryConnector.java))
our connector must implement the methods these abstract classes define. These general methods implement our services
(Data Engine Proxy Services and OMRS), without needing to know anything about the underlying technology. Therefore, we
can simply "plug-in" the underlying technology: any technology with a connector that implements these methods can run
our service. Furthermore, each technology-specific connector can decide how best to implement those methods for itself.

## 4. Write the connector itself

Which brings us to writing the _connector_ itself. Now that we understand the interface our connector must provide, we
need to implement the methods defined by that interface.

Implement the connector by:

1. Retrieving _connection_ information provided by the configuration. The default method for `initialize`
   saves the connection object used to create the connector.  If your connector needs to override the `initialize`
    method, it should call `super.initialize()` to capture the connection properties for the base classes. 
1. The `start()` method is where the main logic for your connector runs.     
    Use the configuration details from the connection object to connect to your underlying technology.
    If the connector is long running, this may be the time to start up a separate thread. However, this has to conform
    the rules laid down for the category of connector you are implementing.
1. Using pre-existing, technology-specific clients and APIs to talk to your underlying technology.
1. Translating the underlying technology's representation of information into the Open Metadata representation used by
   the connector interface itself.

For the first point, you can retrieve general connection information like:

- the server address and protocol, by first retrieving the embedded [EndpointProperties](../../src/main/java/org/odpi/openmetadata/frameworks/connectors/properties/EndpointProperties.java)
    with `getEndpoint()`:
    - retrieving the protocol by calling `getProtocol()` on the [EndpointProperties](../../src/main/java/org/odpi/openmetadata/frameworks/connectors/properties/EndpointProperties.java)
    - retrieving the address by calling `getAddress()` on the [EndpointProperties](../../src/main/java/org/odpi/openmetadata/frameworks/connectors/properties/EndpointProperties.java)
- the user Id, by calling `getUserId()` on the [ConnectionProperties](../../src/main/java/org/odpi/openmetadata/frameworks/connectors/properties/ConnectionProperties.java)
- the password, by calling either `getClearPassword()` or `getEncryptedPassword()` on the [ConnectionProperties](../../src/main/java/org/odpi/openmetadata/frameworks/connectors/properties/ConnectionProperties.java),
    depending on what your underlying technology can handle

Use these details to connect to and authenticate against your underlying technology, even when it is running on a
different system from the connector itself. Of course, check for `null` objects (like the `EndpointProperties`) as
well before blindly operating on them.

Retrieve additional properties by:

- calling `getConfigurationProperties()` on the [ConnectionProperties](../../src/main/java/org/odpi/openmetadata/frameworks/connectors/properties/ConnectionProperties.java),
    which returns a `Map<String, Object>`
- calling `get(name)` against that `Map<>` with the name of each additional property of interest

Implementation of the remaining points (2-3) will vary widely depending on the specific technology being used.

## Further Information

The [Developer Guide](../../../../../open-metadata-publication/website/developer-guide) provides more information on the specific types of connectors supported by Egeria.


----
* [Return to OCF Overview](../..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Developer Guide - Extending Egeria using connectors

Connectors are Java classes that either perform an additional service,
or more typically, enable Egeria to integrate with a new technology.

## What is a connector?

The concept of a connector comes from the
[Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework).
The OCF provides a common framework for components that enable one technology to call another, arbitrary
technology through a common interface.  The implementation of the called technology is dynamically
loaded based on configuration.  Through the OCF, we can "plug in" different technologies to Egeria.
We also use the OCF to "plug in" support for open metadata into the client libraries used by applications
to access data resources and services.

Figure 1 contrasts these two approaches:

![Figure 1](../images/compare-use-of-connectors.png#pagewidth)
> **Figure 1:** How OCF connectors are used in Egeria

Many subsystems in Egeria support the first approach. They define the interface for a connector.
One or more connector implementations are written using different technology.  Then when an
Egeria OMAG Server is configured, details of which implementation to use is specified in the
server's configuration document.  When the server is started, the configuration is used by the OCF to
instantiate the required implementation.

The second approach is used by organizations that want to make use of metadata directly in
applications and tools - or to externalize the security and driver properties needed to call the data
source or service.  In this case the OCF Connector typically has the same interface as the client
library (unless you can do better :).  This minimized the learning curve for application developers.
The configuration for the connector is stored in an open metadata server and the application uses
the [Asset Consumer OMAS](../../../open-metadata-implementation/access-services/asset-consumer)
client to request a new instance of the connector.  It then uses the connector instance to
access the data source or server along with the metadata stored about it.


## Inside the connector

The OCF provides the base class for a connector
called `org.odpi.openmetadata.frameworks.connectors.ConnectorBase`.

This base class defines the basic lifecycle of a connector.  There are three calls.

* **initialize** - provides the connector with a unique instance identifier (for logging) and its configuration
  stored in a [Connection](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connection.md).
  The default implementation stores these values in protected variables called `connectorInstanceId` and 
  `connectionProperties` respectively.
* **start** - the connector is completely initialized and can start processing.
* **disconnect** - the connector must stop processing and release all of its resources.

Depending on the type of connector you are writing, there may be additional initialization
calls occurring between the `initialize` and the `start` method.  The connector may also support
additional methods for its normal operation that can be called between the `start` and `disconnect` calls.

The `ConnectorBase` base class manages the lifecycle state of the connector.  If
you override any of these methods, be sure to call `super.xxx()` at the start of your implementation
to call the appropriate super class method so that the state is properly maintained.

## Connector configuration

The configuration for a connector is managed in a **Connection** object.  There are two versions:

* `org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection` - a Java Bean that stores all of the
  properties for the connector.
* `org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties` - a read only wrapper for the
  Connection bean used to pass the connection properties to the application.

They can be retrieved from your connector implementation via properties
`connectionProperties` and `connectionBean` respectively.

A Connection contains properties about the specific use of the connector, such as
user Id and password, or parameters that control the scope or resources that should be
made available to the connector.
It links to an optional **Endpoint** and a mandatory  **ConnectorType** object.  

* **ConnectorType** - this is a object that describes the type of the connector.
  This is used to create an instance of the connector.
* **Endpoint** - this is the object that describes the server endpoint where the
data source or service is accessed from.

Connector types and endpoints can be reused in multiple connections.

## Connector factories

Each connector implementation has a factory object called a **Connector Provider**.
The OCF provides a base class for connector providers that manages the creation of
connectors with default constructors (that is no parameters need to be passed on the
constructor).
The base class is called `org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase`.

If you have a simple connector implementation then your connector provider follows the following
template.  It assumes the connector is for the `XXXStore` and is called `XXXStoreConnector`.

```java
/**
 * XXXStoreProvider is the OCF connector provider for the XXX store connector.
 */
public class XXXStoreProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "Add unique GUID here";
    static final String  connectorTypeName = "XXX Store Connector";
    static final String  connectorTypeDescription = "Connector supports ... add details here";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFileStoreProvider()
    {
        Class    connectorClass = XXXStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());


        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        super.connectorTypeBean = connectorType;
    }
}
```

If you connector needs special initialization then you need to override the `getConnector()`
method of the connector provider base class.

## Types of Connectors

Egeria has extended the basic concept of the OCF connector and created specialized connectors for
different purposes.  They are described below.

### Approach 1 - connectors for Egeria

The following types of connectors are supported by Egeria subsystems.  Connection objects describing
implementations of these connectors can be added to an OMAG Server's configuration document and
an instance of this connector implementation will be used within the server.


| Type of Connector | Description | Owning subsystem | Implementation Examples |
| :-----------------| :---------- | :--------------- | :---------------------- |
| Configuration Document Store | Persists the configuration document for an OMAG Server. | [admin-services](../../../open-metadata-implementation/admin-services) | [configuration-store-connectors](../../../open-metadata-implementation/adapters/open-connectors/configuration-store-connectors) |
| Platform Security Connector | Manages service authorization for the OMAG Server Platform. | [metadata-security-connectors](../../../open-metadata-implementation/common-services/metadata-security/metadata-security-connectors) | [open-metadata-security-samples](../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) |
| Server Security Connector | Manages service and metadata instance authorization for an OMAG Server. | [metadata-security-connectors](../../../open-metadata-implementation/common-services/metadata-security/metadata-security-connectors) | [open-metadata-security-samples](../../../open-metadata-resources/open-metadata-samples/open-metadata-security-samples) |
| Open Metadata Archive Store| Reads an open metadata archive from a particular type of store. | [repository-services](../../../open-metadata-implementation/repository-services) | [open-metadata-archive-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-archive-connectors)
| Audit Log Store | Logging destination | [repository-services](../../../open-metadata-implementation/repository-services) | [audit-log-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/audit-log-connectors) |
| Cohort Registry Store | Local store of membership of an open metadata repository cohort. | [repository-services](../../../open-metadata-implementation/repository-services) | [cohort-registry-store-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/cohort-registry-store-connectors) |
| Metadata Collection Store | Interfaces with a metadata repository API for retrieving and storing metadata. | [repository-services](../../../open-metadata-implementation/repository-services) | [open-metadata-collection-store-connectors](../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors)
| Metadata Collection Event Mapper | Maps events from a metadata repository to open metadata events. | [repository-services](../../../open-metadata-implementation/repository-services) | none ([documentation](../../../open-metadata-implementation/repository-services/docs/component-descriptions/connectors/event-mapper-connector.md)) |
| Open Metadata Topic Connector | Connects to a topic on an external Event bus such as Apache Kafka. | [repository-services](../../../open-metadata-implementation/repository-services) | [open-metadata-topic-connectors](../../../open-metadata-implementation/adapters/open-connectors/event-bus-connectors/open-metadata-topic-connectors) |
| Open Discovery Service | Implements automatic metadata discovery. | [discovery-engine-services](../../../open-metadata-implementation/governance-servers/discovery-engine-services) | [discovery-service-connectors](../../../open-metadata-implementation/adapters/open-connectors/discovery-service-connectors) |



### Approach 2 - connectors for applications

Egeria currently has a small collection of connectors for applications.
The aim is to eventually cover most common types of data sources.
These connectors can be found in the [data-store-connectors](../../../open-metadata-implementation/adapters/open-connectors/data-store-connectors)
module.

There is a code sample that shows how to work with the file connector
in the [asset-management-samples](../../../open-metadata-resources/open-metadata-samples/access-services-samples/asset-management-samples).


## Connector implementations supplied by Egeria

The [open-connectors](../../../open-metadata-implementation/adapters/open-connectors) module
contains the implementations of the different connector implementations supplied by Egeria
"out-of-the-box".  You can write more to integrate additional types of technology or extend the
capabilities of Egeria - and if you think your connector is more generally useful,
you could consider contributing it to the Egeria project.

----
* Return to [Developer Guide](.)
* Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
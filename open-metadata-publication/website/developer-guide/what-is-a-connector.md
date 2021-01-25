<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Developer Guide - What is a Connector?

Connectors are plug-in Java classes that either perform an additional service,
or, more typically, enable Egeria to integrate with a third party technology.

## What is a connector?

The concept of a connector comes from the
[Open Connector Framework (OCF)](../../../open-metadata-implementation/frameworks/open-connector-framework).
The OCF provides a common framework for components that enable one technology to call another, arbitrary
technology through a common interface.  The implementation of the connector is dynamically
loaded based on the connector's configuration.  

Through the OCF, we can "plug in" different technologies to Egeria.
We also use the OCF to "plug in" support for open metadata into the
client libraries used by applications
to access data resources and services.

Figure 1 contrasts these two approaches:

![Figure 1](../images/compare-use-of-connectors.png#pagewidth)
> **Figure 1:** How OCF connectors are used in Egeria

Many subsystems in Egeria's 
[OMAG Server Platform and Servers](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md) 
support the first approach. They define a specialized interface for the type of connector they support.

* One or more connector implementations supporting that interface are then written either by the Egeria community or
other organizations.

* When an Egeria [OMAG Server](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server.md) is configured, 
details of which connector implementation to use is specified in the
server's 
[configuration document](../../../open-metadata-implementation/admin-services/docs/concepts/configuration-document.md).  

* At start up, the OMAG server passes the connector configuration to the OCF to
instantiate the required connector instance.  Connectors enable Egeria to operate in many environments
and with many types of third party technologies, just by managing the configuration of the OMAG servers.

The second approach is used by organizations that want to make use of metadata directly in
applications and tools - or to externalize the security and driver properties needed to call the data
source or service.  In this case the OCF Connector typically has the same interface as the data source's client
library (unless you can do better :).  This minimized the learning curve for application developers.
The configuration for the connector is stored in an open metadata server and the application uses
the [Asset Consumer OMAS](../../../open-metadata-implementation/access-services/asset-consumer/docs/user)
client to 
[request a new instance of the connector](../../../open-metadata-implementation/access-services/asset-consumer/docs/scenarios/working-with-connectors.md).  
The application uses the returned connector instance to
access the data source or server along with the metadata stored about it.


## Connector configuration

The configuration for a connector is managed in a **Connection** object.  

A Connection contains properties about the specific use of the connector, such as
user Id and password, or parameters that control the scope or resources that should be
made available to the connector.
It links to an optional **Endpoint** and a mandatory **ConnectorType** object.  

* **ConnectorType** - this is a object that describes the type of the connector, its supported configuration properties and
its factory object (called the connector's provider).
This information is used to create an instance of the connector at runtime.

* **Endpoint** - this is the object that describes the server endpoint where the third party
data source or service is accessed from.

Connector types and endpoints can be reused in multiple connections.

![Connection Structure](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connection.png)
> **Figure 1:** Structure of a connection object

**[Click for more information on Connection objects ...](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connection.md)**

## Connector factories

Each connector implementation has a factory object called a **Connector Provider**.
The connector provider has two types of methods:

* Return a new instance of the connector based on the properties in a supplied Connection object. 
The Connection object that has all of the properties needed to create and 
configure the instance of the connector.

* Return additional information about the connector's behaviour and use to make it easier to consume.
For example, the standard base class for a connector provider has a method to
return the ConnectorType object for this connector implementation that can be
added to a Connection object used to hold the properties needed to create an instance of the connector.

**[Click for more information on Connector Providers ...](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connector-provider.md)**

## Inside the connector

Each connector has its own unique implementation that is structured around a simple
lifecycle that is defined by the OCF.
The OCF provides the interface for a connector
called `org.odpi.openmetadata.frameworks.connectors.Connector`.

This connector interface defines the basic lifecycle of a connector.  There are three calls.

* **initialize** - provides the connector with a unique instance identifier (for logging) and its configuration
  stored in a [Connection](../../../open-metadata-implementation/frameworks/open-connector-framework/docs/concepts/connection.md).
  The default implementation stores these values in protected variables called `connectorInstanceId` and 
  `connectionProperties` respectively.
* **start** - the connector is completely initialized and can start processing.
* **disconnect** - the connector must stop processing and release all of its resources.

Depending on the type of connector you are writing, there may be additional initialization
calls occurring between the `initialize` and the `start` method.  The connector may also support
additional methods for its normal operation that can be called between the `start` and `disconnect` calls.

The OCF also provides the base class for a connector
called `org.odpi.openmetadata.frameworks.connectors.ConnectorBase`.
The `ConnectorBase` base class manages the lifecycle state of the connector.  If
you override any of these methods, be sure to call `super.xxx()` at the start of your implementation
to call the appropriate super class method so that the state is properly maintained.

## Where to next?

* The [Connector Catalog](../connector-catalog) lists the pre-built connectors supplied with Egeria.

* Guidance on implementing new connectors supported by the Egeria OMAG servers is found in
[Extending Egeria using Connectors](extending-egeria-using-connectors.md).


----
* Return to [Developer Guide](.)
* Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
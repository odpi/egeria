<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connection - part of the [Open Connector Framework (OCF)](..)

The Connection provides the set of properties needed to create an instance
of a [connector](connector.md) used to access the contents of a specific
[Asset](../../../../../open-metadata-implementation/access-services/docs/concepts/assets/README.md).

## Inside a Connection

A Connection contains properties about the specific use of the connector, such as
user Id and password, or parameters that control the scope or resources that should be
made available to the connector.
It links to an optional **Endpoint** and/or **ConnectorType** object.  

* **ConnectorType** - this is a object that describes the type of the connector that needs to be created in order to
access the Asset.
* **Endpoint** - this is the object that describes the server endpoint where the asset is accessed from.

Connector types and endpoints can be reused in multiple connections.

Connections are typically managed in a metadata repository but they can also be manually populated.

## Connection implementations

The OCF offers two implementations of the Connection.

```
org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection
```

Connection is a bean implementation of the connection used in REST API requests and events.  It allows properties for be
set up and retrieved.

```
org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties
```

ConnectionProperties is a read-only wrapper for the Connection properties that is used in client interfaces that do not allow the 
properties to be updated.

## Using Connections from open metadata repositories

Each connection stored in a metadata repository has a unique identifier.
An application can request a connector instance through selected Egeria OMAS interfaces, such
as the [Asset Consumer OMAS](../../../../access-services/asset-consumer/docs/concepts/README.md), with just the unique identifier or name of a connection.  
The OMAS retrieves the connection object from the open metadata repositories and passes it to the [Connector Broker](connector-broker.md)
factory object.  The Connector Broker (and underlying [Connector Provider](connector-provider.md)) uses the information from the Connection object
to create an instance of the connector.

The advantage of retrieving the connection information from a metadata repository is that the connection properties
do not need to be hard-coded in the consuming applications.

Connections can be created in the open metadata repositories through the [Asset Owner OMAS](../../../../access-services/asset-owner/README.md).

## Configuring Egeria Connections

The [Administration Guide](../../../../admin-services/docs/user) describes how to configure Egeria's
OMAG Server Platforms and Servers.  Both the platform and the servers used connectors for access to the
external resources to support their basic operation and to coordinate metadata and governance with
third party technologies.  This means that the configuration includes Connection definitions for these connectors.

All of these interfaces have Java clients that enable you to set up the connection using the OCF Connection bean.
However if you want to use the REST API directly, then you need to specify the connection in JSON.

Egeria's JSON structures map one-to-ene with the properties in the equivalent Java beans and also include
a `class` property that includes the name of the class that it maps to.  So a simple Connection object
would look something like this in JSON:

```json
{
  "connection" : 
  {
    "class" : "Connection",
    "connectorType" : 
    {
      "class" : "ConnectorType",
      "connectorProviderClassName" : "...fully qualified class name..."
    },
    "endpoint" : 
    {
      "class" : "Endpoint",
      "address" : "... network address of resource ..."
    }
  }
}
```

----
* [Return to OCF Overview](../..)
  
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

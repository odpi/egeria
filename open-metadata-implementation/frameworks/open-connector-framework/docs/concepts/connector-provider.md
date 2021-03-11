<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector Provider - part of the [Open Connector Framework (OCF)](../..)

A Connector Provider is the factory for a particular type of [Connector](connector.md).  It is typically
called from the [Connector Broker](connector-broker.md), although it may be called directly.

Each Connector Provider implements the following interface:

```
org.odpi.openmetadata.frameworks.connectors.ConnectorProvider
```

It has two types of methods:

* Return the ConnectorType object that is added to a [Connection](connection.md) object used to
hold the properties needed to create an instance of the connector.

* Return a new instance of the connector based on the properties in a [Connection](connection.md) object.
The Connection object that has all of the properties
needed to create and configure the instance of the connector.

There is a base class that provides much of the implementation for a connector provider.

```
org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase
```

If you have a simple connector implementation then your connector provider follows the following
template.  It assumes the connector is for the `XXXStore` and is called `XXXStoreConnector`.

With this base implementation, a specific Connector Provider implementation need only implement a constructor to
configure the base class's function with details of itself and the Java class of the connector it needs.



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
        Class<?> connectorClass = XXXStoreConnector.class;

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

For example, here is the implementation of the Connector Provider for the
[basic file connector](../../../../adapters/open-connectors/data-store-connectors/file-connectors/basic-file-connector).

```java
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.datastore.basicfile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;


/**
 * BasicFileStoreProvider is the OCF connector provider for the basic file store connector.
 */
public class BasicFileStoreProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "ba213761-f5f5-4cf5-a95f-6150aef09e0b";
    static final String  connectorTypeName = "Basic File Store Connector";
    static final String  connectorTypeDescription = "Connector supports reading of Files.";

    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * store implementation.
     */
    public BasicFileStoreProvider()
    {
        Class<?>    connectorClass = BasicFileStoreConnector.class;

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


----
* [Return to OCF Overview](../..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
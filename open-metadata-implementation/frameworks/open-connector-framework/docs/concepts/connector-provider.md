<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector Provider - part of the [Open Connector Framework (OCF)](../README.md)

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

With this base implementation, a specific Connector Provider implementation need only implement a constructor to
configure the base class's function with details of itself and the Java class of the connector it needs.

For example, here is the implementation of the Connector Provider for the
[structured file connector](../../../adapters/open-connectors/data-store-connectors/structured-file-connector/README.md).

```java

package org.odpi.openmetadata.adapters.connectors.structuredfile;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;

import java.util.ArrayList;
import java.util.List;


/**
 * StructuredFileStoreProvider is the OCF connector provider for the structured file store connector.
 */
public class StructuredFileStoreProvider extends ConnectorProviderBase
{
    static final String  connectorTypeGUID = "108b85fe-d7b8-45c3-9fb8-742ac4e4fb14";
    static final String  connectorTypeName = "Structured File Store Connector";
    static final String  connectorTypeDescription = "Connector supports storing of the open metadata cohort registry in a file.";
    static final String  columnNamesProperty = "columnNames";
    static final String  delimiterCharacterProperty = "delimiterCharacter";
    static final String  quoteCharacterProperty = "quoteCharacter";


    /**
     * Constructor used to initialize the ConnectorProviderBase with the Java class name of the specific
     * registry store implementation.
     */
    public StructuredFileStoreProvider()
    {
        Class    connectorClass = StructuredFileStoreConnector.class;

        super.setConnectorClassName(connectorClass.getName());


        ConnectorType connectorType = new ConnectorType();
        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(this.getClass().getName());

        List<String> recognizedAdditionalProperties = new ArrayList<>();
        recognizedAdditionalProperties.add(columnNamesProperty);
        recognizedAdditionalProperties.add(delimiterCharacterProperty);
        recognizedAdditionalProperties.add(quoteCharacterProperty);

        connectorType.setRecognizedAdditionalProperties(recognizedAdditionalProperties);

        super.connectorTypeBean = connectorType;
    }
}
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
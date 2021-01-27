<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector Broker - part of the [Open Connector Framework (OCF)](../..)

The Connector Broker is the factory class for all open connectors.
Given a valid [Connection](connection.md) object, the
Connector Broker is able to create a new instance of a connector.
This means the caller does not need to know the implementation
details of the connector - just its interface.

It is implemented in the following Java class:

```
org.odpi.openmetadata.frameworks.connectors.ConnectorBroker
```

and is used as follows:

```
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;

   :
   
ConnectorBroker   connectorBroker     = new ConnectorBroker();
Connector         connector           = connectorBroker.getConnector(connection);
```

When the connector instance is requested, the Connector Broker uses the ConnectorType properties
from the supplied Connection to identify the appropriate [Connector Provider](connector-provider.md).
The Connector Broker delegates the connector instance request to the Connector Provider and returns
the result to its caller.

## Use of the Connector Broker in Egeria

The Connector Broker is used in the client code of the Open Metadata Access Services (OMASs) that provide
connector instances to their consumers.  Examples include:

* [Asset Consumer OMAS](../../../../access-services/asset-consumer)
* [Asset Owner OMAS](../../../../access-services/asset-owner)


----
* [Return to OCF Overview](../..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
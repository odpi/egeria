<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Getting a connector from a connection name with java

This operation retrieves a [connection](../../../../../frameworks/open-connector-framework/docs/concepts/connection.md) object
from the [open metadata repositories](../../../../../repository-services/docs/open-metadata-repository.md)
and uses it to create a [connector](../../../../../frameworks/open-connector-framework/docs/concepts/connector.md) object.
The connector can be used to interact with a specific [Asset](../../../../docs/concepts/assets).

```java
   import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;

   class MyAssetConsumer
   { 
       
       public Connector getConnector(String     serverName,
                                     String     omasServerURL,
                                     String     userId,
                                     String     password,
                                     String     connectionName) throws InvalidParameterException,
                                                                       UnrecognizedConnectionNameException,
                                                                       AmbiguousConnectionNameException,
                                                                       ConnectionCheckedException,
                                                                       ConnectorCheckedException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException;
       { 
           AssetConsumerClient client = new AssetConsumerClient(serverName, omasServerURL, userId, password);
           
           return client.getConnectorByName(userId, connectionName);
       }
       
   
   }

```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
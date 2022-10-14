<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# getConnectorByName

```
    /**
     * Returns the connector corresponding to the supplied connection name.
     *
     * @param userId           userId of user making request.
     * @param connectionName   this may be the qualifiedName or displayName of the connection.
     *
     * @return Connector   connector instance.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    Connector getConnectorByName(String userId,
                                 String connectionName) throws InvalidParameterException,
                                                               ConnectionCheckedException,
                                                               ConnectorCheckedException,
                                                               PropertyServerException,
                                                               UserNotAuthorizedException;
```

## Example usage


This operation retrieves a [connection](../../../../../../frameworks/open-connector-framework/docs/concepts/connection.md) object
from the [open metadata repositories](../../../../../../repository-services/docs/open-metadata-repository.md)
and uses it to create a [connector](../../../../../../frameworks/open-connector-framework/docs/concepts/connector.md) object.
The connector can be used to interact with a specific [Asset](https://egeria-project.org/concepts/asset).

```java
   import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;

   class MyAssetConsumer
   { 
       
       public Connector getConnector(String     serverName,
                                     String     serverPlatformRootURL,
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
           AssetConsumerClient client = new AssetConsumerClient(serverName, serverPlatformRootURL, userId, password);
           
           return client.getConnectorByName(userId, connectionName);
       }
       
   
   }
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Creating a connector for application use

The Asset Consumer OMAS supports a REST API to extract metadata from the open metadata repositories
linked to the same open metadata cohort as the Asset Consumer OMAS.  It also has a Java client that
provides an equivalent interface to the REST API plus connector factory methods supported by an
embedded Connector Broker.  

The Connector Broker is an [Open Connector Framework (OCF)](../../../../frameworks/open-connector-framework) component
that is able to create and configure instances of compliant connectors.  It is passed a Connection
object which has all of the properties needed to create the connector.  

The Asset Consumer OMAS java client
extracts the Connection object from the open metadata repositories and then calls the Connector Broker.

The code sample below creates the Asset Consumer OMAS Client.
It is passed the [server name]() and [platform root URL]() for the metadata server that
will supply the connection object.

```
      AssetConsumer client = new AssetConsumer(serverName, serverURLRoot);

```
The next code samples show how to create the connector.  There are three approaches:

* Supplying a hard-coded Connection object.
* Supplying the unique identifier (guid) of the Connection object in the metadata repositories that Asset Consumer OMAS has access to.
* Supplying the unique identifier (guid) of an Asset.  The connector is created using the Connection linked to the asset.

In this first example, the connector is created from the hard-coded connection.

```

    /**
     * This method creates a connector using a hard coded connection object.  This connector will be able
     * to retrieve the data from the file, but it will not be able to retrieve the metadata about
     * the file.
     *
     * @return connector to requested file
     */
    private CSVFileStoreConnector getConnectorUsingHardCodedConnection()
    {
        CSVFileStoreConnector connector = null;

        try
        {
            AssetConsumer client = new AssetConsumer(serverName, serverURLRoot);

            connector = (CSVFileStoreConnector)client.getConnectorByConnection(clientUserId, getHardCodedConnection(fileName));
        }
        catch (Exception error)
        {
            System.out.println("The connector can not be created with Asset Consumer OMAS.");
        }

        return connector;
    }
    
    
    /**
     * This method creates a connection.  The connection object provides the OCF with the properties to create the
     * connector and the properties needed by the connector to access the asset.
     *
     * The Connection object includes a Connector Type object.  This defines the type of connector to create.
     * The Connection object also includes an Endpoint object.  This is used by the connector to locate and connect
     * to the Asset.
     *
     * @param fileName name of the file to connect to
     * @return connection object
     */
    private Connection getHardCodedConnection(String   fileName)
    {
        final String endpointGUID      = "8bf8f5fa-b5d8-40e1-a00e-e4a0c59fd6c0";
        final String connectorTypeGUID = "2e1556a3-908f-4303-812d-d81b48b19bab";
        final String connectionGUID    = "b9af734f-f005-4085-9975-bf46c67a099a";

        final String endpointDescription = "File name.";

        String endpointName    = "CSVFileStore.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "CSVFileStore connector type.";
        final String connectorTypeJavaClassName = CSVFileStoreProvider.class.getName();

        String connectorTypeName = "CSVFileStore.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "CSVFileStore connection.";

        String connectionName = "CSVFileStore.Connection.Test";

        Connection connection = new Connection();

        connection.setType(Connection.getConnectionType());
        connection.setGUID(connectionGUID);
        connection.setQualifiedName(connectionName);
        connection.setDisplayName(connectionName);
        connection.setDescription(connectionDescription);
        connection.setEndpoint(endpoint);
        connection.setConnectorType(connectorType);

        return connection;
    }
```

The next example, uses the Connection guid:

```

    try
    {
        /*
         * Return a connector for the connection.
         */
        return (CSVFileStoreConnector) client.getConnectorByGUID(clientUserId, connectionGUID);
    }
    catch (Exception error)
    {
        System.out.println("Unable to create connector for connection: " + connectionGUID);
    }

```

The final example uses the Asset guid:

```

    try
    {
        /*
         * Return a connector for the asset.
         */
        return (CSVFileStoreConnector) client.getConnectorForAsset(clientUserId, assetGUID);
    }
    catch (Exception error)
    {
        System.out.println("Unable to create connector for asset: " + assetGUID);
    }

```

## Why use the Asset Consumer OMAS java client rather than the ConnectorBroker?

Each connector has a method call called `getConnectedAssetProperties`.  This returns the
metadata known about the asset that the connector is accessing.
When you create a connector using the Asset Consumer OMAS java client, and the Connection
used is linked to an asset then `getConnectedAssetProperties` returns all of the metadata
known about the asset.

```
            ConnectedAssetProperties assetProperties = connector.getConnectedAssetProperties(clientUserId);

            if (assetProperties != null)
            {
                AssetUniverse assetUniverse = assetProperties.getAssetUniverse();

                if (assetUniverse != null)
                {
                    System.out.println("Type Name: " + assetUniverse.getAssetTypeName());
                    System.out.println("Qualified Name: " + assetUniverse.getQualifiedName());
                }
                else
                {
                    System.out.println(assetProperties.toString());
                }
            }
            else
            {
                System.out.println("No asset properties  ...");
            }
```

----
* [Return to Asset Consumer OMAS User Guide](../user)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
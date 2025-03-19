/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.samples.readavrofile;

import org.odpi.openmetadata.accessservices.assetconsumer.client.AssetConsumer;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreConnector;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFileStoreProvider;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectedAssetDetails;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.http.HttpHelper;

import java.io.File;
import java.util.List;


/**
 * AvroFileReaderSample illustrates the use of the Asset Consumer OMAS API to create connectors to Avro files.
 */
public class AvroFileReaderSample
{
    private final String fileName;
    private final String serverName;
    private final String serverURLRoot;
    private final String clientUserId;


    /**
     * Set up the parameters for the sample.
     *
     * @param fileName name of the file
     * @param serverName server to call
     * @param serverURLRoot location of server
     * @param clientUserId userId to access the server
     */
    public AvroFileReaderSample(String  fileName,
                                String  serverName,
                                String  serverURLRoot,
                                String  clientUserId)
    {
        this.fileName = fileName;
        this.serverName = serverName;
        this.serverURLRoot = serverURLRoot;
        this.clientUserId = clientUserId;
    }


    /**
     * This runs the sample
     */
    void run()
    {
        BasicFileStoreConnector connector = getConnectorUsingMetadata();
        if (connector == null)
        {
            connector = getConnectorUsingHardCodedConnection();
        }

        displayFile(connector);
    }


    /**
     * This method uses Asset Consumer OMAS to locate and create an Open Connector Framework (OCF) connector
     * instance.
     *
     * @return connector to requested file
     */
    private BasicFileStoreConnector getConnectorUsingMetadata()
    {
        try
        {
            /*
             * The Asset Consumer OMAS supports a REST API to extract metadata from the open metadata repositories
             * linked to the same open metadata cohort as the Asset Consumer OMAS.  It also has a Java client that
             * provides an equivalent interface to the REST API plus connector factory methods supported by an
             * embedded Connector Broker.  The Connector Broker is an Open Connector Framework (OCF) component
             * that is able to create and configure instances of compliant connectors.  It is passed a Connection
             * object which has all of the properties needed to create the connector.  The Asset Consumer OMAS
             * extracts the Connection object from the open metadata repositories and then calls the Connector Broker.
             */
            AssetConsumer client = new AssetConsumer(serverName, serverURLRoot);

            /*
             * This call extracts the list of assets stored in the open metadata repositories that have a name
             * that matches the requested filename.
             */
            List<String>   knownAssets = client.getAssetsByName(clientUserId, fileName, 0, 99);

            if (knownAssets != null)
            {
                System.out.println("The open metadata repositories have returned " + knownAssets.size() + " asset definitions for the requested file name " + fileName);

                for (String assetGUID : knownAssets)
                {
                    if (assetGUID != null)
                    {
                        try
                        {
                            /*
                             * The aim is to return a connector for the first matching asset.  If an asset of a different
                             * type is returned, on one where it is not possible to create a connector for, then an
                             * exception is thrown and the code moves on to process the next asset.
                             */
                            return (BasicFileStoreConnector) client.getConnectorForAsset(clientUserId, assetGUID);
                        }
                        catch (Exception error)
                        {
                            System.out.println("Unable to create connector for asset: " + assetGUID);
                        }
                    }
                }
            }
            else
            {
                System.out.println("The open metadata repositories do not have an asset definition for the requested file name " + fileName);
            }
        }
        catch (Exception error)
        {
            System.out.println("The connector can not be created from metadata.  Error message is: " + error.getMessage());
        }

        return null;
    }


    /**
     * This method creates a connector using a hard coded connection object.  This connector will be able
     * to retrieve the data from the file, but it will not be able to retrieve the metadata about
     * the file.
     *
     * @return connector to requested file
     */
    private BasicFileStoreConnector getConnectorUsingHardCodedConnection()
    {
        BasicFileStoreConnector connector = null;

        try
        {
            AssetConsumer client = new AssetConsumer(serverName, serverURLRoot);

            connector = (BasicFileStoreConnector)client.getConnectorByConnection(clientUserId, getHardCodedConnection(fileName));
        }
        catch (Exception error)
        {
            System.out.println("The connector can not be created with Asset Consumer OMAS.");
        }

        return connector;
    }


    /**
     * This method is not called - it is an example of using the native OCF framework to create a connector.
     * This is functionally equivalent to the Asset Consumer OMAS call used in getConnectorUsingHardCodedConnection().
     *
     * @return connector to requested file
     */
    private BasicFileStoreConnector getConnectorWithOCF()
    {
        BasicFileStoreConnector connector = null;

        try
        {
            ConnectorBroker broker = new ConnectorBroker();

            connector = (BasicFileStoreConnector) broker.getConnector(getHardCodedConnection(fileName));
        }
        catch (Exception error)
        {
            System.out.println("The connector can not be created with OCF.");
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
        final String endpointGUID      = "85cedbcb-5a38-412d-a40e-df318cc3e48f";
        final String connectorTypeGUID = "de8ce2df-b990-4ef5-a716-26f134eddc51";
        final String connectionGUID    = "87013f6c-bff2-4326-a49e-ea953a925e14";

        final String endpointDescription = "File name.";

        String endpointName    = "AvroFileStore.Endpoint." + fileName;

        Endpoint endpoint = new Endpoint();

        endpoint.setType(Endpoint.getEndpointType());
        endpoint.setGUID(endpointGUID);
        endpoint.setQualifiedName(endpointName);
        endpoint.setDisplayName(endpointName);
        endpoint.setDescription(endpointDescription);
        endpoint.setAddress(fileName);


        final String connectorTypeDescription   = "BasicFileStore connector type.";
        final String connectorTypeJavaClassName = BasicFileStoreProvider.class.getName();

        String connectorTypeName = "AvroFileStore.ConnectorType.Test";

        ConnectorType connectorType = new ConnectorType();

        connectorType.setType(ConnectorType.getConnectorTypeType());
        connectorType.setGUID(connectorTypeGUID);
        connectorType.setQualifiedName(connectorTypeName);
        connectorType.setDisplayName(connectorTypeName);
        connectorType.setDescription(connectorTypeDescription);
        connectorType.setConnectorProviderClassName(connectorTypeJavaClassName);


        final String connectionDescription = "AvroFileStore connection.";

        String connectionName = "AvroFileStore.Connection.Test";

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


    /**
     * This method displays some of the data from the asset along with any metadata it has.
     *
     * @param connector connector to the asset
     */
    private void displayFile(BasicFileStoreConnector connector)
    {
        try
        {
            System.out.println("===============================");

            System.out.println("Accessing file: " + fileName);

            File file = connector.getFile();

            System.out.println("File: " + file.getName());
            System.out.println("Path: " + file.getPath());

            System.out.println("------------------------------------------------------------------------");

            ConnectedAssetDetails assetProperties = connector.getConnectedAssetProperties(clientUserId);

            if (assetProperties != null)
            {
                System.out.println(assetProperties.toString());
            }
            else
            {
                System.out.println("No asset properties  ...");
            }
        }
        catch (FileException error)
        {
            System.out.println("The connector is unable to retrieve the requested record because the file is not valid.");
        }
        catch (Exception exception)
        {
            System.out.println("Exception " + exception.getMessage());
        }
    }



    /**
     * Main program that controls the operation of the sample.  The parameters are passed space separated.
     * The file name must be passed as parameter 1.  The other parameters are used to override the
     * sample's default values.
     *
     * @param args 1. file name 2. server name, 3. URL root for the server, 4. client userId
     */
    public static void main(String[] args)
    {

        if ((args == null) || (args.length < 1))
        {
            System.out.println("Please specify the file name in the first parameter");
            System.exit(-1);
        }

        String  fileName = args[0];
        String  serverName = "active-metadata-store";
        String  serverURLRoot = "https://localhost:9443";
        String  clientUserId = "erinoverview";


        if (args.length > 1)
        {
            serverName = args[1];
        }

        if (args.length > 2)
        {
            serverURLRoot = args[2];
        }

        if (args.length > 3)
        {
            clientUserId = args[3];
        }

        System.out.println("===============================");
        System.out.println("Avro File Reader Sample   ");
        System.out.println("===============================");
        System.out.println("Running against server: " + serverName + " at " + serverURLRoot);
        System.out.println("Using userId: " + clientUserId);
        System.out.println();

        HttpHelper.noStrictSSL();

        try
        {
            AvroFileReaderSample sample = new AvroFileReaderSample(fileName, serverName, serverURLRoot, clientUserId);

            sample.run();
        }
        catch (Exception  error)
        {
            System.out.println("Exception: " + error.getClass().getName() + " with message " + error.getMessage());
            System.exit(-1);
        }
    }
}

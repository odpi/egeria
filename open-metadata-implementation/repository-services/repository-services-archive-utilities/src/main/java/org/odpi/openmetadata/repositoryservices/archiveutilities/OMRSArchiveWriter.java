/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archiveutilities;


import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

import java.util.UUID;

/**
 * OMRSArchiveWriter creates physical open metadata archive files for the supplied open metadata archives
 * encoded using Open Metadata Repository Services (OMRS) formats.  To use it, create a subclass that builds
 * the archive content in memory and then writes it out.
 */
public class OMRSArchiveWriter
{
    private static final Logger log = LoggerFactory.getLogger(OMRSArchiveWriter.class);

    /**
     * Default constructor
     */
    public OMRSArchiveWriter()
    {
    }


    /**
     * Return the connection for a open metadata archive file.
     *
     * @param fileName name of the archive file
     * @return OCF Connection used to create the file-based open metadata archive
     */
    protected Connection getOpenMetadataArchiveFileConnection(String fileName)
    {
        Endpoint endpoint = new Endpoint();

        endpoint.setAddress(fileName);

        Connection connection = new Connection();

        connection.setConnectorType(this.getConnectorType(FileBasedOpenMetadataArchiveStoreProvider.class.getName()));
        connection.setEndpoint(endpoint);

        return connection;
    }


    /**
     * Return the connector type for the requested connector provider.  This is best used for connector providers that
     * can return their own connector type.  Otherwise it makes one up.
     *
     * @param connectorProviderClassName name of the connector provider class
     * @return ConnectorType bean
     */
    private ConnectorType   getConnectorType(String   connectorProviderClassName)
    {
        ConnectorType  connectorType = null;

        if (connectorProviderClassName != null)
        {
            try
            {
                Class<?>   connectorProviderClass = Class.forName(connectorProviderClassName);
                Object     potentialConnectorProvider = connectorProviderClass.newInstance();

                ConnectorProvider connectorProvider = (ConnectorProvider)potentialConnectorProvider;

                connectorType = connectorProvider.getConnectorType();

                if (connectorType == null)
                {
                    connectorType = new ConnectorType();

                    connectorType.setType(this.getConnectorTypeType());
                    connectorType.setGUID(UUID.randomUUID().toString());
                    connectorType.setQualifiedName(connectorProviderClassName);
                    connectorType.setDisplayName(connectorProviderClass.getSimpleName());
                    connectorType.setDescription("ConnectorType for " + connectorType.getDisplayName());
                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
            }
            catch (Throwable classException)
            {
                log.error("Bad connectorProviderClassName: " + classException.getMessage());
            }
        }

        return connectorType;
    }


    /**
     * Return the standard type for an endpoint.
     *
     * @return ElementType object
     */
    private ElementType getEndpointType()
    {
        ElementType elementType = Endpoint.getEndpointType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Return the standard type for a connector type.
     *
     * @return ElementType object
     */
    private ElementType getConnectorTypeType()
    {
        ElementType elementType = ConnectorType.getConnectorTypeType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Return the standard type for a connection type.
     *
     * @return ElementType object
     */
    private ElementType getConnectionType()
    {
        ElementType elementType = Connection.getConnectionType();

        elementType.setElementOrigin(ElementOrigin.CONFIGURATION);

        return elementType;
    }


    /**
     * Opens up an open metadata archive store connector.
     *
     * @param connection connection information for the open metadata archive.
     * @return open metadata archive store connector
     */
    private OpenMetadataArchiveStore getOpenMetadataArchive(Connection connection)
    {
        OpenMetadataArchiveStore  openMetadataArchiveStore = null;

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker();
            Connector       connector       = connectorBroker.getConnector(connection);

            openMetadataArchiveStore = (OpenMetadataArchiveStore)connector;

            log.debug("Created connector to open metadata archive store");

        }
        catch (Throwable   error)
        {
            log.error("Unexpected exception occurred: " + error.getMessage());
            log.error("Exception: " + error.toString());
        }

        return openMetadataArchiveStore;
    }


    /**
     * Generates and writes out an open metadata archive containing all of the open metadata types.
     *
     * @param outputFileName name of file to write archive to
     * @param openMetadataArchive archive content
     */
    protected void writeOpenMetadataArchive(String                   outputFileName,
                                            OpenMetadataArchive      openMetadataArchive)
    {
        Connection               connection               = getOpenMetadataArchiveFileConnection(outputFileName);

        OpenMetadataArchiveStore openMetadataArchiveStore = this.getOpenMetadataArchive(connection);

        openMetadataArchiveStore.setArchiveContents(openMetadataArchive);
    }
}

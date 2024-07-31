/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archiveutilities;


import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.FileBasedOpenMetadataArchiveStoreProvider;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ConnectorProvider;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementOrigin;
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
     * Return the connection for an open metadata archive file.
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
     * can return their own connector type.  Otherwise, it makes one up.
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
                Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

                ConnectorProvider connectorProvider = (ConnectorProvider)potentialConnectorProvider;

                connectorType = connectorProvider.getConnectorType();

                if (connectorType == null)
                {
                    connectorType = new ConnectorType();

                    ElementOrigin elementOrigin = new ElementOrigin();
                    elementOrigin.setOriginCategory(ElementOriginCategory.CONFIGURATION);
                    connectorType.setOrigin(elementOrigin);

                    connectorType.setType(ConnectorType.getConnectorTypeType());
                    connectorType.setGUID(UUID.randomUUID().toString());
                    connectorType.setQualifiedName(connectorProviderClassName);
                    connectorType.setDisplayName(connectorProviderClass.getSimpleName());
                    connectorType.setDescription("ConnectorType for " + connectorType.getDisplayName());
                    connectorType.setConnectorProviderClassName(connectorProviderClassName);
                }
            }
            catch (Exception classException)
            {
                log.error("Bad connectorProviderClassName: " + classException.getMessage());
            }
        }

        return connectorType;
    }


    /**
     * Opens up an open metadata archive store connector.
     *
     * @param connection connection information for the open metadata archive
     * @param auditLog logging destination
     * @return open metadata archive store connector
     */
    private OpenMetadataArchiveStore getOpenMetadataArchive(Connection connection,
                                                            AuditLog   auditLog)
    {
        OpenMetadataArchiveStore  openMetadataArchiveStore = null;

        try
        {
            ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
            Connector       connector       = connectorBroker.getConnector(connection);

            openMetadataArchiveStore = (OpenMetadataArchiveStore)connector;

            log.debug("Created connector to open metadata archive store");

        }
        catch (Exception   error)
        {
            log.error("Unexpected exception occurred: " + error.getMessage());
            log.error("Exception: " + error);
        }

        return openMetadataArchiveStore;
    }


    /**
     * Generates and writes out an open metadata archive containing all the open metadata types.
     *
     * @param outputFileName name of file to write archive to
     * @param openMetadataArchive archive content
     */
    protected void writeOpenMetadataArchive(String                   outputFileName,
                                            OpenMetadataArchive      openMetadataArchive)
    {
        Connection               connection               = getOpenMetadataArchiveFileConnection(outputFileName);

        OpenMetadataArchiveStore openMetadataArchiveStore = this.getOpenMetadataArchive(connection, null);

        openMetadataArchiveStore.setArchiveContents(openMetadataArchive);
    }


    /**
     * Generates and writes out an open metadata archive containing all the open metadata types.
     *
     * @param outputFileName name of file to write archive to
     * @param openMetadataArchive archive content
     * @param auditLog logging destination
     */
    protected void writeOpenMetadataArchive(String                   outputFileName,
                                            OpenMetadataArchive      openMetadataArchive,
                                            AuditLog                 auditLog)
    {
        Connection               connection               = getOpenMetadataArchiveFileConnection(outputFileName);

        OpenMetadataArchiveStore openMetadataArchiveStore = this.getOpenMetadataArchive(connection, auditLog);

        openMetadataArchiveStore.setArchiveContents(openMetadataArchive);
    }
}

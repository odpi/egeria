/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.archivemanager;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.adapters.repositoryservices.ConnectorConfigurationFactory;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.repositoryservices.archivemanager.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;

/**
 * OMRSArchiveWriter creates physical open metadata archive files for the supplied open metadata archives
 * encoded in OMRS.
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
     * Opens up an open metadata archive store connector.
     *
     * @param connection connection information for the open metadata archive.
     * @return open metadata archive store connector
     */
    private OpenMetadataArchiveStore getOpenMetadataArchive(ConnectionProperties connection)
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
     */
    private void writeOpenMetadataArchiveTypes()
    {
        ConnectorConfigurationFactory configurationFactory = new ConnectorConfigurationFactory();
        ConnectionProperties          connection           = new ConnectionProperties(configurationFactory.getOpenMetadataTypesConnection());

        OpenMetadataArchiveStore openMetadataArchiveStore = this.getOpenMetadataArchive(connection);
        OpenMetadataTypesArchive openMetadataTypesArchive = new OpenMetadataTypesArchive();
        OpenMetadataArchive      openMetadataArchive      = openMetadataTypesArchive.getOpenMetadataArchive();

        openMetadataArchiveStore.setArchiveContents(openMetadataArchive);
    }


    /**
     * Main program to control the archive writer.
     *
     * @param args ignored arguments
     */
    public static void main(String[] args)
    {
        OMRSArchiveWriter  archiveWriter = new OMRSArchiveWriter();

        archiveWriter.writeOpenMetadataArchiveTypes();

        /*
         * Calls to create other standard archives will go here.
         */
    }
}

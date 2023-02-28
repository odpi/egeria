/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.ffdc.FileBasedOpenMetadataArchiveStoreConnectorAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class FileBasedOpenMetadataArchiveStoreConnector extends OpenMetadataArchiveStoreConnector
{
    /*
     * This is the default name of the open metadata archive file that is used if there is no file name in the connection.
     */
    private static final String defaultFilename = "open.metadata.archive";

    /*
     * Variables used in writing to the file.
     */
    private String archiveStoreName = null;

    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(FileBasedOpenMetadataArchiveStoreConnector.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final ObjectWriter OBJECT_WRITER = OBJECT_MAPPER.writer();
    private static final ObjectReader OBJECT_READER = OBJECT_MAPPER.reader();


    /**
     * Default constructor
     */
    public FileBasedOpenMetadataArchiveStoreConnector()
    {
    }


    /**
     * Retrieve the archive store information from the endpoint.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionProperties   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, ConnectionProperties connectionProperties)
    {
        super.initialize(connectorInstanceId, connectionProperties);

        EndpointProperties endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            archiveStoreName = endpoint.getAddress();
        }

        if (archiveStoreName == null)
        {
            archiveStoreName = defaultFilename;
        }
    }


    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     */
    @Override
    public OpenMetadataArchive getArchiveContents()
    {
        File                archiveStoreFile     = new File(archiveStoreName);
        OpenMetadataArchive newOpenMetadataArchive;

        try
        {
            log.debug("Retrieving open metadata archive from file");

            if (auditLog != null)
            {
                final String actionDescription = "Opening open metadata archive";

                auditLog.logMessage(actionDescription,
                                    FileBasedOpenMetadataArchiveStoreConnectorAuditCode.OPENING_FILE.getMessageDefinition(archiveStoreName));
            }

            String configStoreFileContents = FileUtils.readFileToString(archiveStoreFile, "UTF-8");

            newOpenMetadataArchive = OBJECT_READER.readValue(configStoreFileContents, OpenMetadataArchive.class);
        }
        catch (IOException ioException)
        {
            /*
             * The archive file is not found, create an empty one ...
             */
            if (auditLog != null)
            {
                final String actionDescription = "Unable to open file";

                auditLog.logException(actionDescription,
                                      FileBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName,
                                                                                                                        ioException.getClass().getName(),
                                                                                                                        ioException.getMessage()),
                                      ioException);
            }


            log.debug("Create empty archive", ioException);

            newOpenMetadataArchive = new OpenMetadataArchive();
        }

        return newOpenMetadataArchive;
    }


    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents   OpenMetadataArchive object
     */
    @Override
    public void setArchiveContents(OpenMetadataArchive   archiveContents)
    {
        File    archiveStoreFile = new File(archiveStoreName);

        try
        {
            log.debug("Writing open metadata archive store properties: " + archiveContents);

            if (archiveContents == null)
            {
                archiveStoreFile.delete();
            }
            else
            {
                String archiveStoreFileContents = OBJECT_WRITER.writeValueAsString(archiveContents);

                FileUtils.writeStringToFile(archiveStoreFile, archiveStoreFileContents, (String)null,false);
            }
        }
        catch (IOException   ioException)
        {
            log.debug("Unusable Server config Store :(", ioException);
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        log.debug("Closing Config Store.");
    }
}

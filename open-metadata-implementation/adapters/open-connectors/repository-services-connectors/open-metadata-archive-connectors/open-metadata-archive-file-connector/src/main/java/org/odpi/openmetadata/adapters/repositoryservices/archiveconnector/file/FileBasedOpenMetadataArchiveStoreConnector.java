/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.ffdc.FileBasedOpenMetadataArchiveStoreConnectorAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.file.ffdc.FileBasedOpenMetadataArchiveStoreConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * FileBasedOpenMetadataArchiveStoreConnector provides a connector that can read an Open Metadata Archive file coded in JSON.
 */
public class FileBasedOpenMetadataArchiveStoreConnector extends OpenMetadataArchiveStoreConnector
{
    /**
     * This is the default name of the open metadata archive file that is used if there is no file name in the connection.
     */
    private static final String defaultFilename = "open.metadata.omarchive";

    /**
     * Variables used in writing to the file.
     */
    private String archiveStoreName = null;

    /**
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
     * @param connectionDetails   POJO for the configuration used to create the connector.
     * @throws ConnectorCheckedException  a problem within the connector.
     */
    @Override
    public void initialize(String     connectorInstanceId,
                           Connection connectionDetails) throws ConnectorCheckedException
    {
        super.initialize(connectorInstanceId, connectionDetails);

        Endpoint endpoint = connectionDetails.getEndpoint();

        if (endpoint != null)
        {
            archiveStoreName = endpoint.getNetworkAddress();
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
     * @throws RepositoryErrorException a problem accessing the archive
     */
    @Override
    public OpenMetadataArchive getArchiveContents() throws RepositoryErrorException
    {
        final String methodName = "getArchiveContents";

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
                auditLog.logException(methodName,
                                      FileBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName,
                                                                                                                        ioException.getClass().getName(),
                                                                                                                        ioException.getMessage()),
                                      ioException);
            }


            log.error("Error opening archive", ioException);

            throw new RepositoryErrorException(FileBasedOpenMetadataArchiveStoreConnectorErrorCode.BAD_FILE.getMessageDefinition(archiveStoreName,
                                                                                                                                 ioException.getClass().getName(),
                                                                                                                                 ioException.getMessage()),
                                               this.getClass().getName(),
                                               methodName);
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
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        log.debug("Closing Config Store.");
    }
}

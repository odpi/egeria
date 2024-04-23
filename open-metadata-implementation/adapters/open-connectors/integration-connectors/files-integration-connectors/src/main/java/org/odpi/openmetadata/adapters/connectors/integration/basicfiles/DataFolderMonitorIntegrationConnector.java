/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.adapters.connectors.datastore.datafolder.DataFolderProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.DeployedImplementationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * DataFolderMonitorIntegrationConnector monitors a file directory that is linked to a DataFolder asset and
 * maintains the update time of the DataFolder each time one of the files or embedded directories change in some way.
 */
public class DataFolderMonitorIntegrationConnector extends BasicFilesMonitorIntegrationConnectorBase
{
    private static final Logger log = LoggerFactory.getLogger(DataFolderMonitorIntegrationConnector.class);

    private ConnectorCheckedException savedException = null;

    /**
     * Retrieve the Folder element from the open metadata repositories.
     * If the directory does not exist the connector waits for the directory to be created.
     *
     * @param dataFolderFile the directory to retrieve the folder from
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
     */
    FileFolderElement getFolderElement(File dataFolderFile) throws ConnectorCheckedException
    {
        return super.getFolderElement(dataFolderFile,
                                      DeployedImplementationType.DATA_FOLDER.getAssociatedTypeName(),
                                      DeployedImplementationType.DATA_FOLDER.getDeployedImplementationType(),
                                      DataFolderProvider.class.getName());
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        /*
         * Throw an error if the cataloguing failed
         */
        if (savedException != null)
        {
            throw savedException;
        }
    }


    /**
     * Indicate that the data folder has changed.
     *
     * @param fileChanged the file in the directory that changed
     * @param modifiedTime the time that the asset was last updated
     * @param methodName calling method
     */
    private void updateDataFolder(File   fileChanged,
                                  Date   modifiedTime,
                                  String methodName) throws ConnectorCheckedException
    {
        List<DirectoryToMonitor> directoriesToMonitor = super.getDirectoriesToMonitor();

        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            try
            {
                if (fileChanged.getAbsolutePath().startsWith(directoryToMonitor.directoryFile.getAbsolutePath()))
                {
                    /*
                     * The directory matches this directory.
                     */
                    if (directoryToMonitor.dataFolderElement == null)
                    {
                        directoryToMonitor.dataFolderElement = this.getFolderElement(directoryToMonitor.directoryFile);
                    }

                    if (directoryToMonitor.dataFolderElement != null)
                    {
                        Date lastRecordedChange = directoryToMonitor.dataFolderElement.getFileFolderProperties().getModifiedTime();

                        if ((lastRecordedChange == null) || (lastRecordedChange.before(new Date(directoryToMonitor.directoryFile.lastModified()))))
                        {
                            FileFolderProperties properties = new FileFolderProperties();

                            properties.setModifiedTime(modifiedTime);
                            this.getContext().updateDataFolderInCatalog(directoryToMonitor.dataFolderElement.getElementHeader().getGUID(), true,
                                                                        properties);

                            if (auditLog != null)
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FOLDER_UPDATED_FOR_FILE.getMessageDefinition(
                                                            connectorName,
                                                            directoryToMonitor.directoryFile.getAbsolutePath(),
                                                            modifiedTime.toString(),
                                                            fileChanged.getAbsolutePath()));
                            }
                        }
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    String folderGUID = null;

                    if (directoryToMonitor.dataFolderElement != null)
                    {
                        folderGUID = directoryToMonitor.dataFolderElement.getElementHeader().getGUID();
                    }

                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_FOLDER_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  connectorName,
                                                  folderGUID,
                                                  directoryToMonitor.directoryFile.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);

                }
            }
        }
    }


    /**
     * File created Event.
     *
     * @param file The file that was created
     */
    @Override
    public void onFileCreate(File file)
    {
        final String methodName = "onFileCreate";

        log.debug("File created: " + file.getName());

        try
        {
            this.updateDataFolder(file, new Date(), methodName);
        }
        catch (ConnectorCheckedException error)
        {
            savedException = error;
        }
        catch (Exception error)
        {
            savedException = new ConnectorCheckedException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                              error.getClass().getName(),
                                                                                                                                              file.getAbsolutePath(),
                                                                                                                                              methodName,
                                                                                                                                              error.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           error);
        }
    }


    /**
     * File deleted Event.
     *
     * @param file The file that was deleted
     */
    @Override
    public void onFileDelete(File file)
    {
        final String methodName = "onFileDelete";

        log.debug("File deleted: " + file.getName());

        try
        {
            this.updateDataFolder(file, new Date(), methodName);
        }
        catch (ConnectorCheckedException error)
        {
            savedException = error;
        }
        catch (Exception error)
        {
            savedException = new ConnectorCheckedException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                              error.getClass().getName(),
                                                                                                                                              file.getAbsolutePath(),
                                                                                                                                              methodName,
                                                                                                                                              error.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           error);
        }
    }


    /**
     * File changed Event.
     *
     * @param file The file that changed
     */
    @Override
    public void onFileChange(File file)
    {
        final String methodName = "onFileChange";

        log.debug("File changed: " + file.getName());

        try
        {
            this.updateDataFolder(file, new Date(), methodName);
        }
        catch (ConnectorCheckedException error)
        {
            savedException = error;
        }
        catch (Exception error)
        {
            savedException = new ConnectorCheckedException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                              error.getClass().getName(),
                                                                                                                                              file.getAbsolutePath(),
                                                                                                                                              methodName,
                                                                                                                                              error.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           error);
        }
    }


    /**
     * Directory created Event.
     *
     * @param directory The directory that was created
     */
    @Override
    public void onDirectoryCreate(File directory)
    {
        final String methodName = "onDirectoryCreate";

        log.debug("Folder created: " + directory.getName());

        try
        {
            this.updateDataFolder(directory, new Date(), methodName);
        }
        catch (ConnectorCheckedException error)
        {
            savedException = error;
        }
        catch (Exception error)
        {
            savedException = new ConnectorCheckedException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                              error.getClass().getName(),
                                                                                                                                              directory.getAbsolutePath(),
                                                                                                                                              methodName,
                                                                                                                                              error.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           error);
        }
    }


    /**
     * Directory deleted Event.
     *
     * @param directory The directory that was deleted
     */
    @Override
    public void onDirectoryDelete(File directory)
    {
        final String methodName = "onDirectoryDelete";

        log.debug("Folder deleted: " + directory.getName());

        try
        {
            this.updateDataFolder(directory, new Date(), methodName);
        }
        catch (ConnectorCheckedException error)
        {
            savedException = error;
        }
        catch (Exception error)
        {
            savedException = new ConnectorCheckedException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                              error.getClass().getName(),
                                                                                                                                              directory.getAbsolutePath(),
                                                                                                                                              methodName,
                                                                                                                                              error.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           error);
        }
    }


    /**
     * Directory changed Event.
     *
     * @param directory The directory that changed
     */
    @Override
    public void onDirectoryChange(final File directory)
    {
        final String methodName = "onDirectoryChange";

        log.debug("Folder changed: " + directory.getName());


        try
        {
            this.updateDataFolder(directory, new Date(), methodName);
        }
        catch (ConnectorCheckedException error)
        {
            savedException = error;
        }
        catch (Exception error)
        {
            savedException = new ConnectorCheckedException(BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                                              error.getClass().getName(),
                                                                                                                                              directory.getAbsolutePath(),
                                                                                                                                              methodName,
                                                                                                                                              error.getMessage()),
                                                           this.getClass().getName(),
                                                           methodName,
                                                           error);
        }
    }
}

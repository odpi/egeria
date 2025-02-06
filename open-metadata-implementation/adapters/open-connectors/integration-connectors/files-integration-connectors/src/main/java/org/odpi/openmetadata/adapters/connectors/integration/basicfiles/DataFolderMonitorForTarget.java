/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileFolderElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class DataFolderMonitorForTarget extends DirectoryToMonitor
{
    private static final Logger log = LoggerFactory.getLogger(DataFolderMonitorForTarget.class);

    private ConnectorCheckedException savedException = null;

    /**
     * Construct the monitor for a specific catalog target.
     *
     * @param connectorName name of associated connector
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector
     * @param integrationConnector associated connector
     * @param dataFolderElement Egeria element for this directory
     * @param auditLog logging destination
     */
    public DataFolderMonitorForTarget (String                                    connectorName,
                                       String                                    sourceName,
                                       String                                    pathName,
                                       String                                    catalogTargetGUID,
                                       DeleteMethod                              deleteMethod,
                                       Map<String,String>                        templates,
                                       Map<String, Object>                       configurationProperties,
                                       BasicFilesMonitorIntegrationConnectorBase integrationConnector,
                                       FileFolderElement                         dataFolderElement,
                                       AuditLog                                  auditLog)
    {
        super(connectorName,
              sourceName,
              pathName,
              catalogTargetGUID,
              deleteMethod,
              templates,
              configurationProperties,
              integrationConnector,
              dataFolderElement,
              auditLog);
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
        List<DirectoryToMonitor> directoriesToMonitor = integrationConnector.getDirectoriesToMonitor();

        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            if (integrationConnector.isActive())
            {
                try
                {
                    if (fileChanged.getCanonicalPath().startsWith(directoryToMonitor.directoryFile.getCanonicalPath()))
                    {
                        /*
                         * The directory matches this directory.
                         */
                        if (directoryToMonitor.dataFolderElement == null)
                        {
                            directoryToMonitor.dataFolderElement = integrationConnector.getFolderElement(directoryToMonitor.directoryFile);
                        }

                        if (directoryToMonitor.dataFolderElement != null)
                        {
                            Date lastRecordedChange   = directoryToMonitor.dataFolderElement.getFileFolderProperties().getModifiedTime();

                            if ((lastRecordedChange == null) || (lastRecordedChange.before(new Date(directoryToMonitor.directoryFile.lastModified()))))
                            {
                                FileFolderProperties properties = new FileFolderProperties();

                                properties.setModifiedTime(modifiedTime);
                                integrationConnector.getContext().updateDataFolderInCatalog(directoryToMonitor.metadataSourceGUID,
                                                                                            directoryToMonitor.metadataSourceName,
                                                                                            directoryToMonitor.dataFolderElement.getElementHeader().getGUID(),
                                                                                            true,
                                                                                            properties);

                                if (auditLog != null)
                                {
                                    auditLog.logMessage(methodName,
                                                        BasicFilesIntegrationConnectorsAuditCode.DATA_FOLDER_UPDATED_FOR_FILE.getMessageDefinition(
                                                                sourceName,
                                                                directoryToMonitor.directoryFile.getCanonicalPath(),
                                                                modifiedTime.toString(),
                                                                fileChanged.getCanonicalPath()));
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
                                                      sourceName,
                                                      folderGUID,
                                                      directoryToMonitor.directoryFile.getAbsolutePath(),
                                                      error.getMessage()),
                                              error);

                    }
                }
            }
            else
            {
                break;
            }
        }
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

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;


/**
 * DataFolderMonitorIntegrationConnector monitors a file directory that is linked to a DataFolder asset and
 * maintains the update time of the DataFolder each time one of the files or embedded directories change in some way.
 */
public class DataFolderMonitorIntegrationConnector extends BasicFilesMonitorIntegrationConnectorBase
{
    private static final Logger log = LoggerFactory.getLogger(DataFolderMonitorIntegrationConnector.class);

    private File dataFolderFile = null;

    /**
     * Set up the file listener class - this is implemented by the subclasses
     *
     * @return file alteration listener implementation
     */
    @Override
    FileAlterationListenerAdaptor getListener()
    {
        return new DataFolderMonitorIntegrationConnector.FileCataloguingListener(this);
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        dataFolderFile = super.getRootDirectoryFile();

        this.updateDataFolder(dataFolderFile, new Date(dataFolderFile.lastModified()), methodName);
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
        final String methodName = "refresh";

        dataFolderFile = super.getRootDirectoryFile();

        this.updateDataFolder(null, new Date(dataFolderFile.lastModified()), methodName);
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
                                  String methodName)
    {
        String directoryName = null;
        String fileFolderPathName = null;

        try
        {
            File              folderFile    = super.getRootDirectoryFile();
            FileFolderElement folderElement = super.getFolderElement();

            if ((folderElement == null) || (folderFile == null))
            {
                return;
            }

            directoryName = folderFile.getName();
            fileFolderPathName = folderElement.getFileFolderProperties().getPathName();

            Date lastRecordedChange = folderElement.getFileFolderProperties().getModifiedTime();

            if ((lastRecordedChange == null) || (lastRecordedChange.before(new Date(folderFile.lastModified()))))
            {
                FileFolderProperties properties = new FileFolderProperties();

                properties.setModifiedTime(modifiedTime);
                this.getContext().updateDataFolderInCatalog(folderElement.getElementHeader().getGUID(), true, properties);

                if (auditLog != null)
                {
                    if (fileChanged == null)
                    {
                        auditLog.logMessage(methodName,
                                            BasicFilesIntegrationConnectorsAuditCode.DATA_FOLDER_UPDATED.getMessageDefinition(connectorName,
                                                                                                                              fileFolderPathName,
                                                                                                                              modifiedTime.toString()));
                    }
                    else
                    {
                        auditLog.logMessage(methodName,
                                            BasicFilesIntegrationConnectorsAuditCode.DATA_FOLDER_UPDATED_FOR_FILE.getMessageDefinition(connectorName,
                                                                                                                                       fileFolderPathName,
                                                                                                                                       modifiedTime.toString(),
                                                                                                                                       fileChanged.getAbsolutePath()));
                    }
                }
            }
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_FOLDER_UPDATE.getMessageDefinition(error.getClass().getName(),
                                                                                                                                 connectorName,
                                                                                                                                 fileFolderPathName,
                                                                                                                                 directoryName,
                                                                                                                                 error.getMessage()),
                                      error);

            }
        }
    }


    /**
     * Inner class for the directory listener logic
     */
    class FileCataloguingListener extends FileAlterationListenerAdaptor
    {
        private final DataFolderMonitorIntegrationConnector connector;

        FileCataloguingListener(DataFolderMonitorIntegrationConnector connector)
        {
            this.connector = connector;
        }

        @Override
        public void onFileCreate(File file)
        {
            final String methodName = "onFileCreate";

            log.debug("File created: " + file.getName());
            connector.updateDataFolder(file, new Date(), methodName);
        }

        @Override
        public void onFileDelete(File file)
        {
            final String methodName = "onFileDelete";

            log.debug("File deleted: " + file.getName());
            connector.updateDataFolder(file, new Date(), methodName);
        }

        @Override
        public void onFileChange(File file)
        {
            final String methodName = "onFileChange";

            log.debug("File changed: " + file.getName());
            connector.updateDataFolder(file, new Date(), methodName);
        }

        @Override
        public void onDirectoryCreate(File directory)
        {
            final String methodName = "onDirectoryCreate";

            log.debug("Folder created: " + directory.getName());
            initiateDirectoryMonitoring(directory, methodName);
        }

        @Override
        public void onDirectoryDelete(File directory)
        {
            final String methodName = "onDirectoryDelete";

            log.debug("Folder deleted: " + directory.getName());
            stopDirectoryMonitoring(directory.getName(), methodName);
        }
    }
}

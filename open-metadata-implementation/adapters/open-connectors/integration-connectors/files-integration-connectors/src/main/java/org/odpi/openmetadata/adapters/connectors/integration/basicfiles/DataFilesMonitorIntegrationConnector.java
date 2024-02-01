/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DataFileElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ArchiveProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataFileProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.adapters.connectors.datastore.basicfile.BasicFolderProvider;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.governanceaction.refdata.DeployedImplementationType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.List;


/**
 * DataFilesMonitorIntegrationConnector monitors a file directory and catalogues the files it finds.
 */
public class DataFilesMonitorIntegrationConnector extends BasicFilesMonitorIntegrationConnectorBase
{
    private static final Logger log = LoggerFactory.getLogger(DataFilesMonitorIntegrationConnector.class);


    private String templateGUID = null;


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
                                      DeployedImplementationType.FILE_FOLDER.getAssociatedTypeName(),
                                      DeployedImplementationType.FILE_FOLDER.getDeployedImplementationType(),
                                      BasicFolderProvider.class.getName());
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * <br>
     * This method performs two sweeps.  It first retrieves the files in the directory and validates that are in the
     * catalog - adding or updating them if necessary.  The second sweep is to ensure that all the assets catalogued
     * in this directory actually exist on the file system.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        List<DirectoryToMonitor> directoriesToMonitor = super.getDirectoriesToMonitor();

        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            /*
             * Sweep one - cataloguing all files
             */
            catalogDirectory(directoryToMonitor.directoryFile, methodName);

            /*
             * Sweep two - ensuring all catalogued files still exist.  Notice that if the directory does not exist in the catalog,
             * it is ignored.  It will be dynamically created when a new file is added.
             */
            try
            {
                FileFolderElement folder = this.getFolderElement(directoryToMonitor.directoryFile);

                if (folder != null)
                {
                    int startFrom = 0;
                    int pageSize  = 100;

                    List<DataFileElement> cataloguedFiles = this.getContext().getFolderFiles(folder.getElementHeader().getGUID(),
                                                                                             startFrom,
                                                                                             pageSize);

                    while ((cataloguedFiles != null) && (! cataloguedFiles.isEmpty()))
                    {
                        for (DataFileElement dataFile : cataloguedFiles)
                        {
                            if (dataFile != null)
                            {
                                if ((dataFile.getElementHeader() != null) && (dataFile.getElementHeader().getGUID() != null) &&
                                            (dataFile.getDataFileProperties() != null) && (dataFile.getDataFileProperties().getPathName() != null))
                                {
                                    File file = new File(dataFile.getDataFileProperties().getPathName());

                                    if (! file.exists())
                                    {
                                        this.archiveFileInCatalog(file, dataFile, methodName);
                                    }
                                }
                                else
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(
                                                                    connectorName,
                                                                    dataFile.toString()));
                                    }
                                }
                            }
                        }

                        startFrom = startFrom + pageSize;
                        cataloguedFiles = this.getContext().getFolderFiles(folder.getElementHeader().getGUID(), startFrom, pageSize);
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  connectorName,
                                                  directoryToMonitor.directoryFile.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);

                }

                throw new FileException(
                        BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(error.getClass().getName(),
                                                                                                                      connectorName,
                                                                                                                      directoryToMonitor.directoryFile.getAbsolutePath(),
                                                                                                                      error.getMessage()),
                        error.getClass().getName(),
                        methodName,
                        error,
                        directoryToMonitor.directoryFile.getAbsolutePath());
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
        this.catalogFile(file, methodName);
    }


    @Override
    public void onFileDelete(File file)
    {
        final String methodName = "onFileDelete";

        log.debug("File deleted: " + file.getName());
        this.archiveFileInCatalog(file, null, methodName);
    }


    /**
     * File changed Event.
     *
     * @param file The file that changed
     */
    @Override
    public void onFileChange(File file)
    {
        log.debug("File changed: " + file.getName());
        this.updateFileInCatalog(file);
    }


    /**
     * Directory created Event.
     *
     * @param directory The directory that was created
     */
    @Override
    public void onDirectoryCreate(File directory)
    {
    }


    /**
     * Directory changed Event.
     *
     * @param directory The directory that changed
     */
    @Override
    public void onDirectoryChange(File directory)
    {
    }


    /**
     * Directory deleted Event.
     *
     * @param directory The directory that was deleted
     */
    @Override
    public void onDirectoryDelete(File directory)
    {
    }



    /**
     * Recursively catalog the files in a directory - and its subdirectories.
     *
     * @param directory starting directory
     * @param methodName calling method
     */
    private void catalogDirectory(File   directory,
                                  String methodName)
    {
        final String localMethodName = "catalogDirectory";

        File[] filesArray = directory.listFiles();

        if (filesArray != null)
        {
            for (File file : filesArray)
            {
                if (file != null)
                {
                    if (file.isDirectory())
                    {
                        this.catalogDirectory(file, localMethodName);
                    }
                    else
                    {
                        this.catalogFile(file, methodName);
                    }
                }
            }
        }
    }


    /**
     * Create a catalog entry for a specific file.
     *
     * @param file Java File accessor
     * @param methodName calling method
     */
    private void catalogFile(File   file,
                             String methodName)
    {
        if (this.isActive())
        {
            try
            {
                DataFileElement cataloguedElement = this.getContext().getFileByPathName(file.getAbsolutePath());

                if (cataloguedElement == null)
                {
                    if (fileTemplateQualifiedName == null)
                    {
                        FileClassifier fileClassifier = new FileClassifier(this.getContext().getIntegrationGovernanceContext().getOpenMetadataAccess(), file);

                        /*
                         * Create the file ...
                         */
                        DataFileProperties properties = new DataFileProperties();

                        properties.setTypeName(fileClassifier.getAssetTypeName());
                        properties.setDeployedImplementationType(fileClassifier.getDeployedImplementationType());
                        properties.setPathName(fileClassifier.getPathName());
                        properties.setName(fileClassifier.getFileName());
                        properties.setFileType(fileClassifier.getFileType());
                        properties.setModifiedTime(new Date(file.lastModified()));

                        List<String> guids = this.getContext().addDataFileToCatalog(properties, null);

                        if ((guids != null) && (!guids.isEmpty()) && (auditLog != null))
                        {
                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED.getMessageDefinition(connectorName,
                                                                                                                                properties.getPathName(),
                                                                                                                                guids.get(guids.size() - 1)));
                        }
                    }
                    else
                    {
                        if (templateGUID == null)
                        {
                            DataFileElement templateElement = this.getContext().getFileByPathName(fileTemplateQualifiedName);

                            if (templateElement != null)
                            {
                                if ((templateElement.getElementHeader() != null) && (templateElement.getElementHeader().getGUID() != null))
                                {
                                    templateGUID = templateElement.getElementHeader().getGUID();
                                }
                                else
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(
                                                                    connectorName,
                                                                    templateElement.toString()));
                                    }
                                }
                            }
                            else
                            {
                                if (auditLog != null)
                                {
                                    auditLog.logMessage(methodName,
                                                        BasicFilesIntegrationConnectorsAuditCode.MISSING_TEMPLATE.getMessageDefinition(connectorName,
                                                                                                                                       fileTemplateQualifiedName));
                                }
                            }
                        }

                        if (templateGUID != null)
                        {
                            TemplateProperties properties = new TemplateProperties();

                            properties.setPathName(file.getAbsolutePath());
                            properties.setDisplayName(file.getName());
                            properties.setNetworkAddress(file.getAbsolutePath());

                            List<String> guids = this.getContext().addDataFileToCatalogFromTemplate(templateGUID, properties);

                            if ((guids != null) && (!guids.isEmpty()) && (auditLog != null))
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED_FROM_TEMPLATE.getMessageDefinition(
                                                            connectorName,
                                                            properties.getPathName(),
                                                            guids.get(guids.size() - 1),
                                                            fileTemplateQualifiedName,
                                                            templateGUID));
                            }
                        }
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  connectorName,
                                                  file.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);

                }
            }
        }
    }


    /**
     * The file no longer exists so this method updates the metadata catalog. This may be a call to delete() or an archive action
     * depending on the setting of the allowCatalogDelete configuration property.
     *
     * @param file Java file access object
     * @param retrievedElement catalogued element
     * @param methodName calling method
     */
    private void archiveFileInCatalog(File            file,
                                      DataFileElement retrievedElement,
                                      String          methodName)
    {
        if (this.isActive())
        {
            try
            {
                DataFileElement cataloguedElement = retrievedElement;

                if (cataloguedElement == null)
                {
                    cataloguedElement = this.getContext().getFileByPathName(file.getAbsolutePath());
                }

                if (cataloguedElement == null)
                {
                    return;
                }

                if ((cataloguedElement.getElementHeader() != null) && (cataloguedElement.getElementHeader().getGUID() != null) &&
                            (cataloguedElement.getDataFileProperties() != null) && (cataloguedElement.getDataFileProperties().getPathName() != null))
                {
                    if (allowCatalogDelete)
                    {
                        this.getContext().deleteDataFileFromCatalog(cataloguedElement.getElementHeader().getGUID(),
                                                                    cataloguedElement.getDataFileProperties().getPathName());

                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_DELETED.getMessageDefinition(connectorName,
                                                                                                                                cataloguedElement.getDataFileProperties().getPathName(),
                                                                                                                                cataloguedElement.getElementHeader().getGUID()));
                        }
                    }
                    else
                    {
                        ArchiveProperties archiveProperties = new ArchiveProperties();

                        archiveProperties.setArchiveDate(new Date());
                        archiveProperties.setArchiveProcess(connectorName);

                        this.getContext().archiveDataFileInCatalog(cataloguedElement.getElementHeader().getGUID(), archiveProperties);

                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_ARCHIVED.getMessageDefinition(connectorName,
                                                                                                                                 cataloguedElement.getDataFileProperties().getPathName(),
                                                                                                                                 cataloguedElement.getElementHeader().getGUID()));
                        }
                    }
                }
                else
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                           cataloguedElement.toString()));
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  connectorName,
                                                  file.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);
                }
            }
        }
    }


    /**
     * Update the last modified time in the catalogued asset for the file.
     *
     * @param file file object from operating system
     */
    private void updateFileInCatalog(File   file)
    {
        if (isActive())
        {
            final String methodName = "updateFileInCatalog";

            try
            {
                DataFileElement dataFileInCatalog = this.getContext().getFileByPathName(file.getAbsolutePath());

                if (dataFileInCatalog != null)
                {
                    if ((dataFileInCatalog.getElementHeader() != null) && (dataFileInCatalog.getElementHeader().getGUID() != null) &&
                                (dataFileInCatalog.getDataFileProperties() != null) && (dataFileInCatalog.getDataFileProperties().getPathName() != null))
                    {
                        DataFileProperties properties = new DataFileProperties();

                        properties.setModifiedTime(new Date(file.lastModified()));

                        this.getContext().updateDataFileInCatalog(dataFileInCatalog.getElementHeader().getGUID(), true, properties);

                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_UPDATED.getMessageDefinition(connectorName,
                                                                                                                                dataFileInCatalog.getDataFileProperties().getPathName(),
                                                                                                                                dataFileInCatalog.getElementHeader().getGUID()));
                        }
                    }
                    else
                    {
                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                               dataFileInCatalog.toString()));
                        }
                    }
                }
                else
                {
                    this.catalogFile(file, methodName);
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  connectorName,
                                                  file.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);
                }
            }
        }
    }
}

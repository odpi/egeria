/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.DataFileElement;
import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.ArchiveProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.DataFileProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;


/**
 * DataFilesMonitorIntegrationConnector monitors a file directory and catalogues the files it finds.
 */
public class DataFilesMonitorIntegrationConnector extends BasicFilesMonitorIntegrationConnectorBase
{
    private static final Logger log = LoggerFactory.getLogger(DataFilesMonitorIntegrationConnector.class);

    private String templateGUID = null;

    /**
     * Set up the file listener class - this is implemented by the subclasses
     *
     * @return file alteration listener implementation
     */
    @Override
    FileAlterationListenerAdaptor getListener()
    {
        return new FileCataloguingListener(this);
    }


    /**
     * Inner class for the directory listener logic
     */
    class FileCataloguingListener extends FileAlterationListenerAdaptor
    {
        private final DataFilesMonitorIntegrationConnector connector;

        FileCataloguingListener(DataFilesMonitorIntegrationConnector connector)
        {
            this.connector = connector;
        }

        @Override
        public void onFileCreate(File file)
        {
            final String methodName = "onFileCreate";

            log.debug("File created: " + file.getName());
            connector.catalogFile(file, methodName);
        }

        @Override
        public void onFileDelete(File file)
        {
            final String methodName = "onFileDelete";

            log.debug("File deleted: " + file.getName());
            connector.archiveFileInCatalog(file, null, methodName);
        }

        @Override
        public void onFileChange(File file)
        {
            log.debug("File changed: " + file.getName());
            connector.updateFileInCatalog(file);
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


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
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

        File directory = this.getRootDirectoryFile();

        if (directory != null)
        {
            /*
             * Sweep one - cataloguing all files
             */
            File[] filesArray = directory.listFiles();

            if (filesArray != null)
            {
                for (File file : filesArray)
                {
                    if (file != null)
                    {
                        this.catalogFile(file, methodName);
                    }
                }
            }

            /*
             * Sweep two - ensuring all catalogued files still exist.  Notice that if the folder does not exist, it is
             * ignored.  It will be dynamically created when a new file is added.
             */
            try
            {
                FileFolderElement folder = super.getFolderElement();

                if (folder != null)
                {
                    int startFrom = 0;
                    int pageSize  = 100;

                    List<DataFileElement> cataloguedFiles = this.getContext().getFolderFiles(folder.getElementHeader().getGUID(), startFrom, pageSize);

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
                                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                                           dataFile.toString()));
                                    }
                                }
                            }
                        }

                        startFrom = startFrom + cataloguedFiles.size();
                        cataloguedFiles = this.getContext().getFolderFiles(folder.getElementHeader().getGUID(), startFrom, pageSize);
                    }
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(error.getClass().getName(),
                                                                                                                                        connectorName,
                                                                                                                                        directory.getAbsolutePath(),
                                                                                                                                        error.getMessage()),
                                          error);

                }

                throw new FileException(
                        BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(error.getClass().getName(),
                                                                                                                      connectorName,
                                                                                                                      directory.getAbsolutePath(),
                                                                                                                      error.getMessage()),
                        error.getClass().getName(),
                        methodName,
                        error,
                        directory.getAbsolutePath());
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
                    if (templateQualifiedName == null)
                    {
                        String fileExtension = FilenameUtils.getExtension(file.getAbsolutePath());

                        DataFileProperties properties = new DataFileProperties();

                        String assetTypeName = this.getAssetTypeName(fileExtension);
                        properties.setTypeName(assetTypeName);
                        properties.setPathName(file.getAbsolutePath());
                        properties.setName(file.getName());
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
                            DataFileElement templateElement = this.getContext().getFileByPathName(templateQualifiedName);

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
                                                                                                                                       templateQualifiedName));
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
                                                            templateQualifiedName,
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
     * Determine the open metadata type to use based on the file extension from the file name.  If no file extension, or it is unrecognized
     * then the default is "DataFile".
     *
     * @param fileExtension file extension extracted from the file name
     * @return asset type name to use
     */
    private String getAssetTypeName(String fileExtension)
    {
        String assetTypeName = "DataFile";

        if (fileExtension != null)
        {
            switch (fileExtension)
            {
                case "csv":
                    assetTypeName = "CSVFile";
                    break;

                case "json":
                    assetTypeName = "JSONFile";
                    break;

                case "avro":
                    assetTypeName = "AvroFileName";
                    break;

                case "pdf":
                case "doc":
                case "docx":
                case "ppt":
                case "pptx":
                case "xls":
                case "xlsx":
                case "md":
                    assetTypeName = "Document";
                    break;

                case "jpg":
                case "jpeg":
                case "png":
                case "gif":
                case "mp3":
                case "mp4":
                    assetTypeName = "MediaFile";
                    break;
            }
        }

        return assetTypeName;
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

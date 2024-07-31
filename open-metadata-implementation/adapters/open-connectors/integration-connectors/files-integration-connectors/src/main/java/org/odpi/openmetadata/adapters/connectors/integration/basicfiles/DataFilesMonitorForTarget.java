/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileFolderElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.accessservices.datamanager.properties.TemplateProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataFilesMonitorForTarget extends DirectoryToMonitor
{
    private static final Logger log = LoggerFactory.getLogger(DataFilesMonitorIntegrationConnector.class);

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
    public DataFilesMonitorForTarget  (String                                    connectorName,
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

        catalogDirectory(directoryFile, methodName);
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
        integrationConnector.archiveFileInCatalog(file, null, allowCatalogDelete, methodName);
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
                if (integrationConnector.isActive())
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
                else
                {
                    break;
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
        if (integrationConnector.isActive())
        {
            try
            {
                DataFileElement cataloguedElement = integrationConnector.getContext().getFileByPathName(file.getAbsolutePath());

                if (cataloguedElement == null)
                {
                    if (fileTemplateQualifiedName == null)
                    {
                        FileClassifier     fileClassifier     = integrationConnector.getContext().getFileClassifier();
                        FileClassification fileClassification = fileClassifier.classifyFile(file);

                        if ((! catalogClassifiedFiles) ||
                                (fileClassification.getFileType() != null) ||
                                (fileClassification.getAssetTypeName() != null) ||
                                (fileClassification.getDeployedImplementationType() != null))
                        {
                            /*
                             * Create the file ...
                             */
                            DataFileProperties properties = new DataFileProperties();

                            properties.setTypeName(fileClassification.getAssetTypeName());
                            properties.setDeployedImplementationType(fileClassification.getDeployedImplementationType());
                            properties.setPathName(fileClassification.getPathName());
                            properties.setName(fileClassification.getFileName());
                            properties.setFileName(fileClassification.getFileName());
                            properties.setFileType(fileClassification.getFileType());
                            properties.setFileExtension(fileClassification.getFileExtension());
                            properties.setModifiedTime(fileClassification.getLastModifiedTime());
                            properties.setCreateTime(fileClassification.getCreationTime());
                            properties.setEncodingType(fileClassification.getEncoding());

                            Map<String, String> additionalProperties = new HashMap<>();

                            additionalProperties.put("canRead", Boolean.toString(fileClassification.isCanRead()));
                            additionalProperties.put("canWrite", Boolean.toString(fileClassification.isCanWrite()));
                            additionalProperties.put("canExecute", Boolean.toString(fileClassification.isCanExecute()));
                            additionalProperties.put("isSymLink", Boolean.toString(fileClassification.isSymLink()));
                            additionalProperties.put("isHidden", Boolean.toString(fileClassification.isHidden()));

                            properties.setAdditionalProperties(additionalProperties);

                            List<String> guids = integrationConnector.getContext().addDataFileToCatalog(properties, null);

                            if ((guids != null) && (!guids.isEmpty()) && (auditLog != null))
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED.getMessageDefinition(connectorName,
                                                                                                                                    properties.getPathName(),
                                                                                                                                    guids.get(guids.size() - 1)));
                            }
                        }
                    }
                    else
                    {
                        if (fileTemplateGUID == null)
                        {
                            DataFileElement templateElement = integrationConnector.getContext().getFileByPathName(fileTemplateQualifiedName);

                            if (templateElement != null)
                            {
                                if ((templateElement.getElementHeader() != null) && (templateElement.getElementHeader().getGUID() != null))
                                {
                                    fileTemplateGUID = templateElement.getElementHeader().getGUID();
                                }
                                else
                                {
                                    if (auditLog != null)
                                    {
                                        auditLog.logMessage(methodName,
                                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(
                                                                    sourceName,
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

                        if (fileTemplateGUID != null)
                        {
                            TemplateProperties properties = new TemplateProperties();

                            properties.setPathName(file.getAbsolutePath());
                            properties.setDisplayName(file.getName());
                            properties.setNetworkAddress(file.getAbsolutePath());

                            List<String> guids = integrationConnector.getContext().addDataFileToCatalogFromTemplate(fileTemplateGUID, properties);

                            if ((guids != null) && (!guids.isEmpty()) && (auditLog != null))
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED_FROM_TEMPLATE.getMessageDefinition(
                                                            sourceName,
                                                            properties.getPathName(),
                                                            guids.get(guids.size() - 1),
                                                            fileTemplateQualifiedName,
                                                            fileTemplateGUID));
                            }
                        }
                    }
                }
                else
                {
                    updateFileInCatalog(file);
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  sourceName,
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
        if (integrationConnector.isActive())
        {
            final String methodName = "updateFileInCatalog";

            try
            {
                DataFileElement dataFileInCatalog = integrationConnector.getContext().getFileByPathName(file.getAbsolutePath());

                if (dataFileInCatalog != null)
                {
                    if ((dataFileInCatalog.getElementHeader() != null) && (dataFileInCatalog.getElementHeader().getGUID() != null) &&
                            (dataFileInCatalog.getDataFileProperties() != null) && (dataFileInCatalog.getDataFileProperties().getPathName() != null))
                    {
                        DataFileProperties properties = new DataFileProperties();

                        Date fileLastModifiedDate = new Date(file.lastModified());

                        if ((properties.getModifiedTime() == null) || (fileLastModifiedDate.after(properties.getModifiedTime())))
                        {
                            properties.setModifiedTime(fileLastModifiedDate);

                            integrationConnector.getContext().updateDataFileInCatalog(dataFileInCatalog.getElementHeader().getGUID(), true, properties);

                            if (auditLog != null)
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_UPDATED.getMessageDefinition(connectorName,
                                                                                                                                    dataFileInCatalog.getDataFileProperties().getPathName(),
                                                                                                                                    dataFileInCatalog.getElementHeader().getGUID()));
                            }
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
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_DATA_FILE_UPDATE.getMessageDefinition(
                                                  error.getClass().getName(),
                                                  sourceName,
                                                  file.getAbsolutePath(),
                                                  error.getMessage()),
                                          error);
                }
            }
        }
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.governanceaction.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.integration.context.OpenMetadataAccess;
import org.odpi.openmetadata.frameworks.integration.context.StewardshipAction;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileFolderElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;

/**
 * Manages the cataloguing of data files for a specific catalog target.
 */
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
                FileClassifier fileClassifier = integrationConnector.getContext().getFileClassifier();

                FileClassification fileClassification = fileClassifier.classifyFile(file);

                DataFileElement cataloguedElement = integrationConnector.getContext().getFileByPathName(file.getCanonicalPath());

                if (cataloguedElement == null)
                {
                    if (fileTemplateQualifiedName == null)
                    {
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

                            String guid = this.addDataFileToCatalog(properties);

                            if (guid != null)
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED.getMessageDefinition(connectorName,
                                                                                                                                    properties.getPathName(),
                                                                                                                                    guid));
                            }
                        }
                    }
                    else
                    {
                        OpenMetadataAccess openMetadataAccess = integrationConnector.getContext().getIntegrationGovernanceContext().getOpenMetadataAccess();

                        if (fileTemplateGUID == null)
                        {
                            OpenMetadataElement templateElement = openMetadataAccess.getMetadataElementByUniqueName(fileTemplateQualifiedName,
                                                                                                                    OpenMetadataProperty.QUALIFIED_NAME.name);
                            if (templateElement != null)
                            {
                                fileTemplateGUID = templateElement.getElementGUID();
                            }
                            else
                            {
                                auditLog.logMessage(methodName,
                                                        BasicFilesIntegrationConnectorsAuditCode.MISSING_TEMPLATE.getMessageDefinition(connectorName, fileTemplateQualifiedName));
                            }
                        }

                        if (fileTemplateGUID != null)
                        {
                            Map<String, String> placeholderProperties = new HashMap<>();

                            placeholderProperties.put(PlaceholderProperty.FILE_PATH_NAME.getName(), fileClassification.getPathName());
                            placeholderProperties.put(PlaceholderProperty.FILE_TYPE.getName(), fileClassification.getFileType());
                            placeholderProperties.put(PlaceholderProperty.FILE_EXTENSION.getName(), fileClassification.getFileExtension());
                            placeholderProperties.put(PlaceholderProperty.FILE_NAME.getName(), fileClassification.getFileName());
                            if (fileClassification.getCreationTime() != null)
                            {
                                placeholderProperties.put(PlaceholderProperty.CREATION_DATE.getName(), fileClassification.getCreationTime().toString());
                                placeholderProperties.put(PlaceholderProperty.RECEIVED_DATE.getName(), fileClassification.getCreationTime().toString());
                            }
                            else
                            {
                                placeholderProperties.put(PlaceholderProperty.CREATION_DATE.getName(), "");
                                placeholderProperties.put(PlaceholderProperty.RECEIVED_DATE.getName(), "");
                            }
                            if (fileClassification.getLastModifiedTime() != null)
                            {
                                placeholderProperties.put(PlaceholderProperty.LAST_UPDATE_DATE.getName(), fileClassification.getLastModifiedTime().toString());
                            }
                            else
                            {
                                placeholderProperties.put(PlaceholderProperty.LAST_UPDATE_DATE.getName(), "");
                            }
                            if (fileClassification.getLastAccessedTime() != null)
                            {
                                placeholderProperties.put(PlaceholderProperty.LAST_ACCESSED_DATE.getName(), fileClassification.getLastAccessedTime().toString());
                            }
                            else
                            {
                                placeholderProperties.put(PlaceholderProperty.LAST_ACCESSED_DATE.getName(), "");
                            }

                            String newFileGUID = this.addDataFileViaTemplate(fileClassification.getAssetTypeName(),
                                                                             fileTemplateGUID,
                                                                             null,
                                                                             placeholderProperties);

                            if (newFileGUID != null)
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_CREATED_FROM_TEMPLATE.getMessageDefinition(
                                                            sourceName,
                                                            fileClassification.getPathName(),
                                                            newFileGUID,
                                                            fileTemplateQualifiedName,
                                                            fileTemplateGUID));
                            }

                            if (newFileProcessName != null)
                            {
                                StewardshipAction stewardshipAction = integrationConnector.getContext().getIntegrationGovernanceContext().getStewardshipAction();

                                Map<String, String> requestParameters = new HashMap<>();

                                if (configurationProperties != null)
                                {
                                    for (String configurationProperty : configurationProperties.keySet())
                                    {
                                        if (configurationProperties.get(configurationProperty) != null)
                                        {
                                            requestParameters.put(configurationProperty, configurationProperties.get(configurationProperty).toString());
                                        }
                                    }
                                }

                                List<NewActionTarget> actionTargets = new ArrayList<>();

                                NewActionTarget actionTarget = new NewActionTarget();

                                actionTarget.setActionTargetGUID(newFileGUID);
                                actionTarget.setActionTargetName("sourceFile");
                                actionTargets.add(actionTarget);

                                stewardshipAction.initiateGovernanceActionProcess(newFileProcessName,
                                                                                  null,
                                                                                  actionTargets,
                                                                                  null,
                                                                                  requestParameters,
                                                                                  connectorName,
                                                                                  null);
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
                DataFileElement dataFileInCatalog = integrationConnector.getContext().getFileByPathName(file.getCanonicalPath());

                if (dataFileInCatalog != null)
                {
                    if ((dataFileInCatalog.getElementHeader() != null) && (dataFileInCatalog.getElementHeader().getGUID() != null) &&
                            (dataFileInCatalog.getProperties() != null) && (dataFileInCatalog.getProperties().getPathName() != null))
                    {
                        DataFileProperties properties = new DataFileProperties();

                        Date fileLastModifiedDate = new Date(file.lastModified());

                        if ((properties.getModifiedTime() == null) || (fileLastModifiedDate.after(properties.getModifiedTime())))
                        {
                            properties.setModifiedTime(fileLastModifiedDate);

                            integrationConnector.getContext().updateDataFileInCatalog(dataFileInCatalog.getElementHeader().getGUID(), true, properties);

                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_UPDATED.getMessageDefinition(connectorName,
                                                                                                                                dataFileInCatalog.getProperties().getPathName(),
                                                                                                                                dataFileInCatalog.getElementHeader().getGUID()));
                        }
                    }
                    else
                    {
                        auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.BAD_FILE_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                               dataFileInCatalog.toString()));
                    }
                }
            }
            catch (Exception error)
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


    /**
     * Return the unique identifier of a new metadata element describing the file.
     *
     * @param properties basic properties to use
     * @return unique identifier (guid)
     * @throws ConnectorCheckedException connector has been shutdown
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileToCatalog(DataFileProperties properties) throws ConnectorCheckedException,
                                                                                InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        List<String> guids = integrationConnector.getContext().addDataFileToCatalog(properties, null);

        if ((guids != null) && (!guids.isEmpty()))
        {
            return guids.get(guids.size() - 1);
        }

        return null;
    }


    /**
     * Return the unique identifier of a new metadata element describing the file created using the supplied template.
     *
     * @param assetTypeName type of asset to create
     * @param fileTemplateGUID template to use
     * @param replacementProperties properties from the template to replace
     * @param placeholderProperties values to use to replace placeholders in the template
     * @return unique identifier (guid)
     * @throws ConnectorCheckedException connector has been shutdown
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileViaTemplate(String              assetTypeName,
                                            String              fileTemplateGUID,
                                            ElementProperties   replacementProperties,
                                            Map<String, String> placeholderProperties) throws ConnectorCheckedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        OpenMetadataAccess openMetadataAccess = integrationConnector.getContext().getIntegrationGovernanceContext().getOpenMetadataAccess();

        return openMetadataAccess.getMetadataElementFromTemplate(assetTypeName,
                                                                 null,
                                                                 true,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 fileTemplateGUID,
                                                                 replacementProperties,
                                                                 placeholderProperties,
                                                                 null,
                                                                 null,
                                                                 null,
                                                                 false);
    }
}

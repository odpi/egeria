/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.frameworks.opengovernance.connectorcontext.StewardshipAction;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetEncodingProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.properties.NewActionTarget;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.DataFileProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
                                       OpenMetadataRootElement                   dataFolderElement,
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
     */
    @Override
    public void refresh()
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
        integrationConnector.deleteFileInCatalog(file, null, methodName);
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
                AssetClient fileClient = integrationConnector.integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);

                FileClassifier fileClassifier = integrationConnector.integrationContext.getFileClassifier();

                FileClassification fileClassification = fileClassifier.classifyFile(file);

                OpenMetadataRootElement cataloguedElement = fileClient.getAssetByUniqueName(file.getCanonicalPath(),
                                                                                 OpenMetadataProperty.PATH_NAME.name,
                                                                                 fileClient.getGetOptions());

                if (cataloguedElement == null)
                {
                    String fileTemplateGUID = templates.get(fileClassification.getDeployedImplementationType());
                    if (fileTemplateGUID == null)
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

                            properties.setQualifiedName(fileClassification.getAssetTypeName() + "::" + fileClassification.getPathName());
                            properties.setTypeName(fileClassification.getAssetTypeName());
                            properties.setDeployedImplementationType(fileClassification.getDeployedImplementationType());
                            properties.setPathName(fileClassification.getPathName());
                            properties.setDisplayName(fileClassification.getFileName());
                            properties.setFileName(fileClassification.getFileName());
                            properties.setFileType(fileClassification.getFileType());
                            properties.setFileExtension(fileClassification.getFileExtension());
                            properties.setStoreUpdateTime(fileClassification.getLastModifiedTime());
                            properties.setStoreCreateTime(fileClassification.getCreationTime());

                            DataAssetEncodingProperties encodingProperties = new DataAssetEncodingProperties();

                            encodingProperties.setEncodingType(fileClassification.getEncoding());

                            Map<String, String> additionalProperties = new HashMap<>();

                            additionalProperties.put("canRead", Boolean.toString(fileClassification.isCanRead()));
                            additionalProperties.put("canWrite", Boolean.toString(fileClassification.isCanWrite()));
                            additionalProperties.put("canExecute", Boolean.toString(fileClassification.isCanExecute()));
                            additionalProperties.put("isSymLink", Boolean.toString(fileClassification.isSymLink()));
                            additionalProperties.put("isHidden", Boolean.toString(fileClassification.isHidden()));

                            properties.setAdditionalProperties(additionalProperties);

                            String guid = this.addDataFileToCatalog(fileClassification.getAssetTypeName(),
                                                                    properties,
                                                                    encodingProperties);

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
                        Map<String, String> placeholderProperties = new HashMap<>();

                        placeholderProperties.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), "");
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
                                                        fileClassification.getDeployedImplementationType(),
                                                        fileTemplateGUID));
                        }

                        if (newFileProcessName != null)
                        {
                            StewardshipAction stewardshipAction = integrationConnector.integrationContext.getStewardshipAction();

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
                                                                              null,
                                                                              actionTargets,
                                                                              null,
                                                                              requestParameters,
                                                                              connectorName,
                                                                              null);
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
                AssetClient fileClient = integrationConnector.integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);

                OpenMetadataRootElement dataFileInCatalog = fileClient.getAssetByUniqueName(file.getCanonicalPath(),
                                                                                 OpenMetadataProperty.PATH_NAME.name,
                                                                                 null);

                if ((dataFileInCatalog != null) && (dataFileInCatalog.getProperties() instanceof DataFileProperties dataFileProperties))
                {
                    if (dataFileProperties.getPathName() != null)
                    {
                        DataFileProperties properties = new DataFileProperties();

                        Date fileLastModifiedDate = new Date(file.lastModified());

                        if ((properties.getStoreUpdateTime() == null) || (fileLastModifiedDate.after(properties.getStoreUpdateTime())))
                        {
                            properties.setStoreUpdateTime(fileLastModifiedDate);

                            fileClient.updateAsset(dataFileInCatalog.getElementHeader().getGUID(), fileClient.getUpdateOptions(true), properties);

                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_UPDATED.getMessageDefinition(connectorName,
                                                                                                                                dataFileProperties.getPathName(),
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
     * @param typeName subtype name for file
     * @param properties basic properties to use
     * @param encodingProperties properties for DataAssetEncoding classification
     * @return unique identifier (guid)
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException unable to communicate with the repository
     * @throws UserNotAuthorizedException access problem for userId
     */
    protected String addDataFileToCatalog(String                      typeName,
                                          DataFileProperties          properties,
                                          DataAssetEncodingProperties encodingProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        AssetClient fileClient = integrationConnector.integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);

        Map<String, ClassificationProperties> initialClassifications = null;

        if (encodingProperties != null)
        {
            initialClassifications = new HashMap<>();

            initialClassifications.put(OpenMetadataType.DATA_ASSET_ENCODING_CLASSIFICATION.typeName,
                                       encodingProperties);
        }

        NewElementOptions newElementOptions = new NewElementOptions(fileClient.getMetadataSourceOptions());

        newElementOptions.setIsOwnAnchor(true);

        properties.setTypeName(typeName);

        return fileClient.createAsset(newElementOptions,
                                      initialClassifications,
                                      properties,
                                      null);
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
        OpenMetadataStore openMetadataStore = integrationConnector.integrationContext.getOpenMetadataStore();

        return openMetadataStore.getMetadataElementFromTemplate(assetTypeName,
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

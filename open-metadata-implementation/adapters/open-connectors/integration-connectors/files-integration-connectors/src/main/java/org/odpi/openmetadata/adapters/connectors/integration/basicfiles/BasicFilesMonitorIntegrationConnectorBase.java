/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls.FilesTemplateType;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.ConfigException;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ClassificationExplorerClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.FileSystemConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassification;
import org.odpi.openmetadata.frameworks.openmetadata.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.FileSystemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.io.IOException;
import java.util.*;


/**
 * BasicFilesMonitorIntegrationConnectorBase provides common methods for the connectors in this module.
 */
public abstract class BasicFilesMonitorIntegrationConnectorBase extends IntegrationConnectorBase
{
    /**
     * Maintains a list of directories that are the root of the monitoring.
     */
    private final List<DirectoryToMonitor> directoriesToMonitor = new ArrayList<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It extracts all the useful configuration from the connection object.
     *
     * @throws ConnectorCheckedException the connector detected a problem.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        /*
         * The first possible catalog target is the endpoint from the connection.  It may be null.
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if (endpoint != null)
        {
            monitorEndpoint(connectionBean.getConfigurationProperties());
        }
    }


    /**
     * Add fixed endpoint to monitoring list
     *
     * @param configurationProperties configuration properties from the connector's connection
     * @throws ConnectorCheckedException connector problem
     */
    private void monitorEndpoint(Map<String, Object> configurationProperties) throws ConnectorCheckedException
    {
        /*
         * The first possible catalog target is the endpoint from the connection.  It may be null.
         */
        Endpoint endpoint = connectionBean.getEndpoint();

        if ((endpoint != null) && (endpoint.getNetworkAddress() != null))
        {
            String endpointNetworkAddress = endpoint.getNetworkAddress();

            for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
            {
                if (endpointNetworkAddress.equals(directoryToMonitor.directoryName))
                {
                    /*
                     * Endpoint already monitored
                     */
                    return;
                }
            }

            DirectoryToMonitor directoryToMonitor = checkDirectoryToMonitor(OpenMetadataType.ENDPOINT.typeName + "::" + OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                            endpoint.getNetworkAddress(),
                                                                            null,
                                                                            null,
                                                                            null,
                                                                            FilesTemplateType.getDefaultFileTemplates(),
                                                                            null,
                                                                            configurationProperties);

            directoriesToMonitor.add(directoryToMonitor);
        }
    }


    /**
     * Creates a monitor for the target.
     *
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param dataFolderGUID optional data folder GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param fileSystemProperties    properties of the file system
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @return directory to monitor structure
     * @throws ConnectorCheckedException connector problem
     */
    protected abstract DirectoryToMonitor createDirectoryToMonitor(String               sourceName,
                                                                   String               pathName,
                                                                   String               catalogTargetGUID,
                                                                   String               dataFolderGUID,
                                                                   DeleteMethod         deleteMethod,
                                                                   Map<String,String>   templates,
                                                                   FileSystemProperties fileSystemProperties,
                                                                   Map<String, Object>  configurationProperties) throws ConnectorCheckedException;


    /**
     * Validate the supplied pathname and return details of a directory to monitor.  Any problems, throw exception.
     *
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target relationship GUID
     * @param dataFolderGUID optional data folder GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param fileSystemProperties    properties of the file system
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @return directory to monitor structure
     * @throws ConnectorCheckedException problem with the path name
     */
    private DirectoryToMonitor checkDirectoryToMonitor(String               sourceName,
                                                       String               pathName,
                                                       String               catalogTargetGUID,
                                                       String               dataFolderGUID,
                                                       DeleteMethod         deleteMethod,
                                                       Map<String,String>   templates,
                                                       FileSystemProperties fileSystemProperties,
                                                       Map<String, Object>  configurationProperties) throws ConnectorCheckedException
    {
        final String methodName = "checkDirectoryToMonitor";

        DirectoryToMonitor directoryToMonitor = createDirectoryToMonitor(sourceName,
                                                                         pathName,
                                                                         catalogTargetGUID,
                                                                         dataFolderGUID,
                                                                         deleteMethod,
                                                                         templates,
                                                                         fileSystemProperties,
                                                                         configurationProperties);



        try
        {
            /*
             * Check that the directory exists - IllegalArgumentException thrown if not
             */
            FileUtils.sizeOf(directoryToMonitor.directoryFile);

            /*
             * Get upset if the directory is actually a file
             */
            if (! directoryToMonitor.directoryFile.isDirectory())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_DIRECTORY,
                                          directoryToMonitor.sourceName,
                                          directoryToMonitor.directoryName,
                                          null);
            }

            /*
             * Get upset if the location is not readable.
             */
            if (! directoryToMonitor.directoryFile.canRead())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_READABLE,
                                          directoryToMonitor.sourceName,
                                          directoryToMonitor.directoryName,
                                          null);
            }
        }
        catch (IllegalArgumentException notFound)
        {
            if (! directoryToMonitor.waitForDirectory)
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_DIRECTORY,
                                          directoryToMonitor.sourceName,
                                          directoryToMonitor.directoryName,
                                          null);
            }
            else if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.FILES_LOCATION_NOT_FOUND.getMessageDefinition(pathName,
                                                                                                                           connectorName,
                                                                                                                           sourceName));
            }
        }

        return directoryToMonitor;
    }


    /**
     * This method is called for each refresh to ensure the list of directories to monitor is up-to-date.
     *
     * @return list of directories to monitor - may be empty by not null
     * @throws ConnectorCheckedException problem setting up connector that can needs external help.
     */
    synchronized List<DirectoryToMonitor> getDirectoriesToMonitor() throws ConnectorCheckedException
    {
        final String methodName = "getDirectoriesToMonitor";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient();

            /*
             * Ensure all the catalog targets are included
             */
            List<String> activeCatalogTargets = new ArrayList<>();

            int startFrom   = 0;
            int maxPageSize = integrationContext.getMaxPageSize();

            List<OpenMetadataRootElement> catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                         assetClient.getQueryOptions(startFrom, maxPageSize));

            while (catalogTargets != null)
            {
                for (OpenMetadataRootElement catalogTarget : catalogTargets)
                {
                    if ((catalogTarget != null) && (catalogTarget.getRelatedBy() != null))
                    {
                        this.addCatalogTarget(catalogTarget);
                        activeCatalogTargets.add(catalogTarget.getRelatedBy().getRelationshipHeader().getGUID());
                    }
                }

                startFrom = startFrom + maxPageSize;
                catalogTargets = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                               assetClient.getQueryOptions(startFrom, maxPageSize));
            }

            /*
             * Before returning the list of directories, make sure all are still valid - and they are properly set up.
             */
            for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
            {
                if ((directoryToMonitor.catalogTargetGUID != null) && (! activeCatalogTargets.contains(directoryToMonitor.catalogTargetGUID)))
                {
                    directoriesToMonitor.remove(directoryToMonitor);
                }

                /* Todo - bring the file listening back once we are sure of the thread management
                else
                {
                    if (! directoryToMonitor.isListening)
                    {
                        if (integrationContext.registerDirectoryTreeListener(directoryToMonitor, directoryToMonitor.directoryFile, null))
                        {
                            directoryToMonitor.isListening = true;
                        }
                    }
                }
                 */
            }

            return new ArrayList<>(directoriesToMonitor);
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_CATALOG_TARGETS.getMessageDefinition(error.getClass().getName(),
                                                                                                                                        connectorName,
                                                                                                                                        methodName,
                                                                                                                                        connectorName,
                                                                                                                                        error.getMessage()));

            throw new ConnectorCheckedException(
                    BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_CATALOG_TARGETS.getMessageDefinition(error.getClass().getName(),
                                                                                                                            connectorName,
                                                                                                                            methodName,
                                                                                                                            connectorName,
                                                                                                                            error.getMessage()),
                    error.getClass().getName(),
                    methodName,
                    error);
        }
    }


    /**
     * Determine if this is a new catalog target.  If it is new then it is added to the directoriesToMonitor list.
     * If the catalog target matches the network address in the endpoint then it updates the DirectoryToMonitor entry in the
     * list with details of the catalog target.  This allows the deployer to gradually migrate to using the catalog targets
     * from the endpoint without performing the work twice.
     *
     * @param catalogTarget catalog target retrieved from the open metadata ecosystem.
     * @throws ConnectorCheckedException problem with open metadata or the connector
     * @throws IOException problem resolving the file name
     */
    void addCatalogTarget(OpenMetadataRootElement catalogTarget) throws ConnectorCheckedException,
                                                                        IOException,
                                                                        InvalidParameterException,
                                                                        PropertyServerException,
                                                                        ConnectionCheckedException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "addCatalogTarget";

        /*
         * First perform a simple check - does the relationship GUID of the catalog target match one of the directories to monitor?
         */
        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            if (catalogTarget.getRelatedBy().getRelationshipHeader().getGUID().equals(directoryToMonitor.catalogTargetGUID))
            {
                /*
                 * Already processing this catalog target.
                 */
                return;
            }
        }

        /*
         * Seems to be new - but we need to check that this is not matching the endpoint catalog target.
         */
        if (integrationContext.isTypeOf(catalogTarget.getElementHeader(), OpenMetadataType.FILE_FOLDER.typeName))
        {
            /*
             * It is the right type of catalog target.  We now need the path name associated with this catalog target.
             */
            Connector connector = integrationContext.getConnectedAssetContext().getConnectorForAsset(catalogTarget.getElementHeader().getGUID(), auditLog);

            if ((connector != null) && (connector.getConnection() != null) && (connector.getConnection().getEndpoint() != null) && (connector.getConnection().getEndpoint().getNetworkAddress() != null))
            {
                try
                {
                    if (catalogTarget.getRelatedBy().getRelationshipProperties() instanceof CatalogTargetProperties catalogTargetProperties)
                    {
                        Map<String, String> templates = FilesTemplateType.getDefaultFileTemplates();

                        if (catalogTargetProperties.getTemplates() != null)
                        {
                            templates.putAll(catalogTargetProperties.getTemplates());
                        }

                        FileSystemProperties fileSystemProperties = null;
                        if (catalogTargetProperties.getMetadataSourceQualifiedName() != null)
                        {
                            try
                            {
                                ClassificationExplorerClient classificationExplorerClient = integrationContext.getClassificationExplorerClient();

                                OpenMetadataRootElement metadataSource = classificationExplorerClient.getRootElementByUniqueName(catalogTargetProperties.getMetadataSourceQualifiedName(),
                                                                                                                                 OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                                                 classificationExplorerClient.getGetOptions());

                                if ((metadataSource != null) && (metadataSource.getProperties() instanceof FileSystemProperties properties))
                                {
                                    fileSystemProperties = properties;
                                }
                                else
                                {
                                    auditLog.logMessage(methodName,
                                                        BasicFilesIntegrationConnectorsAuditCode.BAD_METADATA_SOURCE.getMessageDefinition(
                                                                connectorName,
                                                                catalogTargetProperties.getCatalogTargetName(),
                                                                catalogTargetProperties.getMetadataSourceQualifiedName(),
                                                                methodName));
                                }
                            }
                            catch (Exception error)
                            {
                                auditLog.logMessage(methodName,
                                                    BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(
                                                            connectorName,
                                                            error.getClass().getName(),
                                                            methodName,
                                                            error.getMessage()));
                            }
                        }

                        DirectoryToMonitor directoryToMonitor = checkDirectoryToMonitor(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName + "::" + catalogTarget.getRelatedBy().getRelationshipHeader().getGUID(),
                                                                                        connector.getConnection().getEndpoint().getNetworkAddress(),
                                                                                        catalogTarget.getRelatedBy().getRelationshipHeader().getGUID(),
                                                                                        catalogTarget.getElementHeader().getGUID(),
                                                                                        catalogTargetProperties.getDeleteMethod(),
                                                                                        templates,
                                                                                        fileSystemProperties,
                                                                                        catalogTargetProperties.getConfigurationProperties());

                        directoriesToMonitor.add(directoryToMonitor);
                    }
                }
                catch (ConnectorCheckedException exception)
                {
                    // Skip directory for now
                }
            }
        }
    }


    /**
     * Retrieve the Folder element from the open metadata repositories.
     * If the directory does not exist the connector waits for the directory to be created.
     *
     * @param dataFolderFile          the directory to retrieve the folder from
     * @param fileSystemProperties    properties of the file system
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @throws ConnectorCheckedException a problem retrieving the folder element.
     */
    abstract OpenMetadataRootElement getFolderElement(File                 dataFolderFile,
                                                      FileSystemProperties fileSystemProperties,
                                                      Map<String, Object>  configurationProperties) throws ConnectorCheckedException;


    /**
     * Retrieve the Folder element from the open metadata repositories.
     *
     * @param dataFolderFile the directory to retrieve the folder from
     * @param assetTypeName name of the asset type to use if the folder is not catalogued
     * @param deployedImplementationType deployed implementation type to use if the folder is not catalogued
     * @param fileSystemProperties    properties of the file system
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @throws ConnectorCheckedException a problem retrieving the folder element.
     */
    synchronized OpenMetadataRootElement getFolderElement(File                 dataFolderFile,
                                                          String               assetTypeName,
                                                          String               deployedImplementationType,
                                                          FileSystemProperties fileSystemProperties,
                                                          Map<String, Object>  configurationProperties) throws ConnectorCheckedException
    {
        final String methodName = "getFolderElement";

        if ((dataFolderFile != null) && (dataFolderFile.exists()))
        {
            try
            {
                FileSystemProperties fileSystemPropertiesToUse;

                if (fileSystemProperties != null)
                {
                    fileSystemPropertiesToUse = new FileSystemProperties(fileSystemProperties);
                }
                else
                {
                    /*
                     * The file system info is not supplied in the catalog target, so use the default values.
                     */
                    fileSystemPropertiesToUse = new FileSystemProperties();

                    fileSystemPropertiesToUse.setResourceName(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getExample());
                    fileSystemPropertiesToUse.setCanonicalMountPoint(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getExample());
                    fileSystemPropertiesToUse.setLocalMountPoint(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample());
                }

                fileSystemPropertiesToUse.setResourceName(super.getStringConfigurationProperty(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getName(), configurationProperties, fileSystemPropertiesToUse.getResourceName()));
                fileSystemPropertiesToUse.setCanonicalMountPoint(super.getStringConfigurationProperty(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName(), configurationProperties, fileSystemPropertiesToUse.getCanonicalMountPoint()));
                fileSystemPropertiesToUse.setLocalMountPoint(super.getStringConfigurationProperty(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getName(), configurationProperties, fileSystemPropertiesToUse.getLocalMountPoint()));

                FileClassifier fileClassifier = integrationContext.getFileClassifier(fileSystemPropertiesToUse.getResourceName(), fileSystemPropertiesToUse.getCanonicalMountPoint(), fileSystemPropertiesToUse.getLocalMountPoint());

                FileClassification fileClassification = fileClassifier.classifyFile(dataFolderFile);

                AssetClient fileFolderClient = integrationContext.getAssetClient(assetTypeName);

                Map<String, String> placeholderProperties = new HashMap<>();

                placeholderProperties.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), fileClassification.getFileSystemName());
                placeholderProperties.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), fileClassification.getCanonicalPathName());
                placeholderProperties.put(PlaceholderProperty.DIRECTORY_ADDRESS.getName(), fileClassification.getFileAddress());
                placeholderProperties.put(PlaceholderProperty.DIRECTORY_NAME.getName(), fileClassification.getFileName());
                placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), null);
                placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), null);
                placeholderProperties.put(PlaceholderProperty.DEPLOYED_IMPLEMENTATION_TYPE.getName(), deployedImplementationType);

                TemplateOptions templateOptions = new TemplateOptions(fileFolderClient.getMetadataSourceOptions());

                /*
                 * This option is to return the element if already created.
                 */
                templateOptions.setAllowRetrieve(true);

                String folderGUID = fileFolderClient.createAssetFromTemplate(templateOptions,
                                                                             FilesTemplateType.getDefaultTemplateGUID(assetTypeName),
                                                                             null,
                                                                             null,
                                                                             placeholderProperties,
                                                                             null);

                return this.getFolderElement(folderGUID);
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_PATH_NAME.getMessageDefinition(
                                            error.getClass().getName(),
                                            connectorName,
                                            methodName,
                                            dataFolderFile.getName(),
                                            dataFolderFile.getAbsolutePath(),
                                            error.getMessage()));

                throw new FileException(
                        BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_PATH_NAME.getMessageDefinition(
                                error.getClass().getName(),
                                connectorName,
                                methodName,
                                dataFolderFile.getName(),
                                dataFolderFile.getAbsolutePath(),
                                error.getMessage()),
                        error.getClass().getName(),
                        methodName,
                        error,
                        dataFolderFile.getAbsolutePath());
            }
        }

        return null;
    }


    /**
     * Retrieve the Folder element from the open metadata repositories.  If it does not exist it means
     * there are no files defined in the folder.  The connector waits for the folder to be created.
     *
     * @param folderElementGUID the unique identifier of the folder
     * @throws ConnectorCheckedException a problem retrieving the folder element.
     */
    synchronized OpenMetadataRootElement getFolderElement(String folderElementGUID) throws ConnectorCheckedException
    {
        final String methodName = "getFolderElementByGUID";

        try
        {
            AssetClient folderClient = integrationContext.getAssetClient(OpenMetadataType.FILE_FOLDER.typeName);

            return folderClient.getAssetByGUID(folderElementGUID, folderClient.getGetOptions());
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_GUID.getMessageDefinition(error.getClass().getName(),
                                                                                                                                                connectorName,
                                                                                                                                                methodName,
                                                                                                                                                folderElementGUID,
                                                                                                                                                error.getMessage()));
            }

            throw new FileException(
                    BasicFilesIntegrationConnectorsErrorCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_GUID.getMessageDefinition(error.getClass().getName(),
                                                                                                                           connectorName,
                                                                                                                           methodName,
                                                                                                                           folderElementGUID,
                                                                                                                           error.getMessage()),
                    error.getClass().getName(),
                    methodName,
                    error,
                    null);
        }
    }


    /**
     * The file no longer exists so this method updates the metadata catalog. This may be a call to delete() or an archive action
     * depending on the setting of the allowCatalogDelete configuration property.
     *
     * @param file Java file access object
     * @param methodName calling method
     */
    public void deleteFileInCatalog(File   file,
                                    String methodName)
    {
        if (this.isActive())
        {
            try
            {
                AssetClient fileClient = integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);
                String pathName = file.getCanonicalPath();

                /*
                 * This will through an exception if more than one file is catalogued for the same path name.
                 * Nothing is done, and the duplicate management process takes over.
                 */
                OpenMetadataRootElement cataloguedElement = fileClient.getAssetByUniqueName(pathName,
                                                                                            OpenMetadataProperty.PATH_NAME.name,
                                                                                            fileClient.getGetOptions());

                if (cataloguedElement == null)
                {
                    /*
                     * No catalog entry for the file
                     */
                    return;
                }

                DeleteOptions deleteOptions = new DeleteOptions();

                deleteOptions.setArchiveDate(new Date());
                deleteOptions.setArchiveProcess(connectorName);

                fileClient.deleteAsset(cataloguedElement.getElementHeader().getGUID(), deleteOptions);

                if (auditLog != null)
                {
                    if (deleteOptions.getDeleteMethod() == DeleteMethod.ARCHIVE)
                    {
                        auditLog.logMessage(methodName,
                                            BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_ARCHIVED.getMessageDefinition(connectorName,
                                                                                                                             pathName,
                                                                                                                             cataloguedElement.getElementHeader().getGUID()));
                    }
                    else
                    {
                        auditLog.logMessage(methodName,
                                            BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_DELETED.getMessageDefinition(connectorName,
                                                                                                                            pathName,
                                                                                                                            cataloguedElement.getElementHeader().getGUID()));
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
     * Throw a config exception based on the supplied error code.
     *
     * @param errorCode error code describing the problem
     * @param fileLocationName name of file
     * @throws ConfigException exception that is generated
     */
    private void throwConfigException(BasicFilesIntegrationConnectorsErrorCode errorCode,
                                      String                                   fileLocationSource,
                                      String                                   fileLocationName,
                                      Exception                                caughtException) throws ConfigException
    {
        final String methodName = "throwConfigException";

        ExceptionMessageDefinition messageDefinition;

        if (fileLocationName == null)
        {
            messageDefinition = errorCode.getMessageDefinition(super.connectionBean.toString());
        }
        else
        {
            messageDefinition = errorCode.getMessageDefinition(fileLocationName);
        }

        ConfigException error;


        if (caughtException == null)
        {
            error = new ConfigException(messageDefinition,
                                        this.getClass().getName(),
                                        methodName,
                                        fileLocationSource,
                                        fileLocationName);
        }
        else
        {
            error = new ConfigException(messageDefinition,
                                        this.getClass().getName(),
                                        methodName,
                                        caughtException,
                                        fileLocationSource,
                                        fileLocationName);
        }

        if (auditLog != null)
        {
            auditLog.logException(methodName,
                                  BasicFilesIntegrationConnectorsAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                  ConfigException.class.getName(),
                                                                                                                  fileLocationName,
                                                                                                                  fileLocationSource,
                                                                                                                  methodName,
                                                                                                                  error.getMessage()),
                                  error);
        }

        throw error;
    }


    /**
     * Shutdown file monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName));
        }

        super.disconnect();
    }
}

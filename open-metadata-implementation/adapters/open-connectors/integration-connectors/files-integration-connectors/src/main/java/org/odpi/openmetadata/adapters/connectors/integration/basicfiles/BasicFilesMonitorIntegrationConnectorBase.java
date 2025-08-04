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
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnectorBase;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.controls.PlaceholderProperty;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
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
    Map<String, String> defaultTemplates = new HashMap<>();

    /**
     * Maintains a list of directories that are the root of the monitoring.
     */
    private final List<DirectoryToMonitor> directoriesToMonitor = new ArrayList<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It extracts all the useful configuration from the connection object.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        for (FilesTemplateType templateType : FilesTemplateType.values())
        {
            defaultTemplates.put(templateType.getTemplateName(), templateType.getTemplateGUID());
        }

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

        if ((endpoint != null) && (endpoint.getAddress() != null))
        {
            String endpointNetworkAddress = endpoint.getAddress();

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
                                                                            endpoint.getAddress(),
                                                                            null,
                                                                            null,
                                                                            defaultTemplates,
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
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @return directory to monitor structure
     * @throws ConnectorCheckedException connector problem
     */
    protected abstract DirectoryToMonitor createDirectoryToMonitor(String              sourceName,
                                                                   String              pathName,
                                                                   String              catalogTargetGUID,
                                                                   DeleteMethod        deleteMethod,
                                                                   Map<String,String>  templates,
                                                                   Map<String, Object> configurationProperties) throws ConnectorCheckedException;


    /**
     * Validate the supplied pathname and return a Directory to monitor structure.  Any problems, throw exception.
     *
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param configurationProperties parameters to further modify the behaviour of the connector.
     * @return directory to monitor structure
     * @throws ConnectorCheckedException problem with the path name
     */
    private DirectoryToMonitor checkDirectoryToMonitor(String              sourceName,
                                                       String              pathName,
                                                       String              catalogTargetGUID,
                                                       DeleteMethod        deleteMethod,
                                                       Map<String,String>  templates,
                                                       Map<String, Object> configurationProperties) throws ConnectorCheckedException
    {
        final String methodName = "checkDirectoryToMonitor";

        DirectoryToMonitor directoryToMonitor = createDirectoryToMonitor(sourceName,
                                                                         pathName,
                                                                         catalogTargetGUID,
                                                                         deleteMethod,
                                                                         templates,
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
            /*
             * Ensure all the catalog targets are included
             */
            List<String> activeCatalogTargets = new ArrayList<>();

            int startFrom   = 0;
            int maxPageSize = integrationContext.getMaxPageSize();

            List<CatalogTarget> catalogTargets = integrationContext.getConnectorConfigClient().getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                                                 startFrom,
                                                                                                                 maxPageSize);

            while (catalogTargets != null)
            {
                for (CatalogTarget catalogTarget : catalogTargets)
                {
                    this.addCatalogTarget(catalogTarget);
                    activeCatalogTargets.add(catalogTarget.getRelationshipGUID());
                }

                startFrom = startFrom + maxPageSize;
                catalogTargets = integrationContext.getConnectorConfigClient().getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                                 startFrom,
                                                                                                 maxPageSize);
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
                else
                {
                    if (directoryToMonitor.dataFolderElement == null)
                    {
                        directoryToMonitor.dataFolderElement = this.getFolderElement(directoryToMonitor.directoryFile);
                    }

                    if (! directoryToMonitor.isListening)
                    {
                        integrationContext.registerDirectoryTreeListener(directoryToMonitor, directoryToMonitor.directoryFile, null);
                        directoryToMonitor.isListening = true;
                    }
                }
            }

            return new ArrayList<>(directoriesToMonitor);
        }
        catch (ConnectorCheckedException error)
        {
            throw error;
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logMessage(methodName,
                                    BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_CATALOG_TARGETS.getMessageDefinition(error.getClass().getName(),
                                                                                                                                                connectorName,
                                                                                                                                                methodName,
                                                                                                                                                connectorName,
                                                                                                                                                error.getMessage()));
            }

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
    void addCatalogTarget(CatalogTarget catalogTarget) throws ConnectorCheckedException, IOException
    {
        /*
         * First perform a simple check - does the relationship GUID of the catalog target match one of the directories to monitor?
         */
        for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
        {
            if (catalogTarget.getRelationshipGUID().equals(directoryToMonitor.catalogTargetGUID))
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
        if (integrationContext.isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.FILE_FOLDER.typeName))
        {
            /*
             * It is the right type of catalog target.  We now need the path name associated with this catalog target.
             */
            OpenMetadataRootElement fileFolderElement = this.getFolderElement(catalogTarget.getCatalogTargetElement().getGUID());

            if ((fileFolderElement != null) &&
                    (fileFolderElement.getProperties() instanceof FileFolderProperties fileFolderProperties) &&
                    (fileFolderProperties.getPathName() != null))
            {
                /*
                 * Create a file object to perform the comparison on the absolute path name.
                 */
                File pathFile = new File(fileFolderProperties.getPathName());

                for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
                {
                    if (directoryToMonitor.directoryFile.getCanonicalPath().equals(pathFile.getCanonicalPath()))
                    {
                        directoryToMonitor.catalogTargetGUID = catalogTarget.getRelationshipGUID();

                        return;
                    }
                }

                /*
                 * No match so add this catalog target.
                 */
                try
                {
                    Map<String, String> templates = defaultTemplates;

                    if (catalogTarget.getTemplateProperties() != null)
                    {
                        templates.putAll(catalogTarget.getTemplateProperties());
                    }

                    DirectoryToMonitor directoryToMonitor = checkDirectoryToMonitor(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP.typeName + "::" + catalogTarget.getRelationshipGUID(),
                                                                                    fileFolderProperties.getPathName(),
                                                                                    catalogTarget.getRelationshipGUID(),
                                                                                    catalogTarget.getDeleteMethod(),
                                                                                    templates,
                                                                                    catalogTarget.getConfigurationProperties());

                    directoriesToMonitor.add(directoryToMonitor);
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
     * @param dataFolderFile the directory to retrieve the folder from
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
     */
    abstract OpenMetadataRootElement getFolderElement(File dataFolderFile) throws ConnectorCheckedException;



    /**
     * Retrieve the Folder element from the open metadata repositories.
     * If the directory does not exist the connector waits for the directory to be created.
     *
     * @param dataFolderFile the directory to retrieve the folder from
     * @param assetTypeName name of the asset type to use if the folder is not catalogued
     * @param deployedImplementationType deployed implementation type to use if the folder is not catalogued
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
     */
    OpenMetadataRootElement getFolderElement(File   dataFolderFile, 
                                             String assetTypeName, 
                                             String deployedImplementationType) throws ConnectorCheckedException
    {
        final String methodName = "getFolderElementByPathName";

        if (dataFolderFile.exists())
        {
            try
            {
                String      pathName         = dataFolderFile.getCanonicalPath();
                AssetClient fileFolderClient = integrationContext.getAssetClient(OpenMetadataType.FILE_FOLDER.typeName);

                OpenMetadataRootElement folderElement = fileFolderClient.getAssetByUniqueName(pathName, 
                                                                                              OpenMetadataProperty.PATH_NAME.name, 
                                                                                              fileFolderClient.getGetOptions());

                if (folderElement == null)
                {
                    Map<String, String> placeholderProperties = new HashMap<>();

                    if (integrationContext.getMetadataSourceQualifiedName() != null)
                    {
                        placeholderProperties.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), integrationContext.getMetadataSourceQualifiedName());
                    }
                    else
                    {
                        placeholderProperties.put(PlaceholderProperty.FILE_SYSTEM_NAME.getName(), connectorName);
                    }
                    placeholderProperties.put(PlaceholderProperty.DIRECTORY_PATH_NAME.getName(), dataFolderFile.getCanonicalPath());
                    placeholderProperties.put(PlaceholderProperty.DIRECTORY_NAME.getName(), dataFolderFile.getName());
                    placeholderProperties.put(PlaceholderProperty.VERSION_IDENTIFIER.getName(), "V1.0");
                    placeholderProperties.put(PlaceholderProperty.DESCRIPTION.getName(), null);

                    TemplateOptions templateOptions = new TemplateOptions(fileFolderClient.getMetadataSourceOptions());

                    templateOptions.setAllowRetrieve(true);
                    templateOptions.setOpenMetadataTypeName(assetTypeName);


                    String folderGUID = fileFolderClient.createAssetFromTemplate(templateOptions,
                                                                                 defaultTemplates.get(deployedImplementationType),
                                                                                 null,
                                                                                 placeholderProperties,
                                                                                 null);

                    folderElement = this.getFolderElement(folderGUID);
                }

                return folderElement;
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        BasicFilesIntegrationConnectorsAuditCode.UNEXPECTED_EXC_RETRIEVING_FOLDER_BY_PATH_NAME.getMessageDefinition(
                                                error.getClass().getName(),
                                                connectorName,
                                                methodName,
                                                dataFolderFile.getName(),
                                                dataFolderFile.getAbsolutePath(),
                                                error.getMessage()));
                }

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
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
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
     * @param retrievedElement catalogued element
     * @param methodName calling method
     */
    public void deleteFileInCatalog(File         file,
                                    OpenMetadataRootElement retrievedElement,
                                    String       methodName)
    {
        if (this.isActive())
        {
            try
            {
                AssetClient fileClient = integrationContext.getAssetClient(OpenMetadataType.DATA_FILE.typeName);
                OpenMetadataRootElement cataloguedElement = retrievedElement;

                String pathName = file.getCanonicalPath();

                if (cataloguedElement == null)
                {

                    /*
                     * Just check that it is not catalogued
                     */
                    cataloguedElement = fileClient.getAssetByUniqueName(pathName,
                                                                        OpenMetadataProperty.PATH_NAME.name,
                                                                        fileClient.getGetOptions());
                }

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

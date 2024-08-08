/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.apache.commons.io.FileUtils;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.DataFileElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.FileFolderElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ArchiveProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders.FileFolderProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.ConfigException;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.integrationservices.files.connector.FilesIntegratorConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * BasicFilesMonitorIntegrationConnectorBase provides common methods for the connectors in this module.
 */
public abstract class BasicFilesMonitorIntegrationConnectorBase extends FilesIntegratorConnector
{


    /**
     * Maintains a list of directories that are the root of the monitoring.
     */
    private final List<DirectoryToMonitor> directoriesToMonitor = new ArrayList<>();


    /**
     * Indicates that the connector is completely configured and can begin processing.
     * It extracts all the useful configuration from the connection object.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        /*
         * The first possible catalog target is the endpoint from the connection.  It may be null.
         */
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            monitorEndpoint(connectionProperties.getConfigurationProperties());
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
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

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

            DirectoryToMonitor directoryToMonitor = checkDirectoryToMonitor(OpenMetadataType.ENDPOINT.typeName + ":" + OpenMetadataProperty.NETWORK_ADDRESS.name,
                                                                            endpoint.getAddress(),
                                                                            null,
                                                                            null,
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

            List<CatalogTarget> catalogTargets = this.getContext().getCatalogTargets(startFrom, maxPageSize);

            while (catalogTargets != null)
            {
                for (CatalogTarget catalogTarget : catalogTargets)
                {
                    this.addCatalogTarget(catalogTarget);
                    activeCatalogTargets.add(catalogTarget.getRelationshipGUID());
                }

                startFrom = startFrom + maxPageSize;
                catalogTargets = this.getContext().getCatalogTargets(startFrom, maxPageSize);
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
                        this.getContext().registerDirectoryTreeListener(directoryToMonitor, directoryToMonitor.directoryFile, null);
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
     */
    void addCatalogTarget(CatalogTarget catalogTarget) throws ConnectorCheckedException
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
        if (this.getContext().isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.FILE_FOLDER.typeName))
        {
            /*
             * It is the right type of catalog target.  We now need the path name associated with this catalog target.
             */
            FileFolderElement fileFolderElement = this.getFolderElement(catalogTarget.getCatalogTargetElement().getGUID());

            if ((fileFolderElement != null) && (fileFolderElement.getFileFolderProperties() != null) && (fileFolderElement.getFileFolderProperties().getPathName() != null))
            {
                /*
                 * Create a file object to perform the comparison on the absolute path name.
                 */
                File pathFile = new File(fileFolderElement.getFileFolderProperties().getPathName());

                for (DirectoryToMonitor directoryToMonitor : directoriesToMonitor)
                {
                    if (directoryToMonitor.directoryFile.getAbsolutePath().equals(pathFile.getAbsolutePath()))
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
                    DirectoryToMonitor directoryToMonitor = checkDirectoryToMonitor(OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME + ":" + catalogTarget.getRelationshipGUID(),
                                                                                    fileFolderElement.getFileFolderProperties().getPathName(),
                                                                                    catalogTarget.getRelationshipGUID(),
                                                                                    catalogTarget.getDeleteMethod(),
                                                                                    catalogTarget.getTemplateProperties(),
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
    abstract FileFolderElement getFolderElement(File dataFolderFile) throws ConnectorCheckedException;



    /**
     * Retrieve the Folder element from the open metadata repositories.
     * If the directory does not exist the connector waits for the directory to be created.
     *
     * @param dataFolderFile the directory to retrieve the folder from
     * @param assetTypeName name of the asset type to use if the folder is not catalogued
     * @param deployedImplementationType deployed implementation type to use if the folder is not catalogued
     * @param connectorProviderName connector provider name to use if the folder is not catalogued
     * @throws ConnectorCheckedException there is a problem retrieving the folder element.
     */
    FileFolderElement getFolderElement(File   dataFolderFile,
                                       String assetTypeName,
                                       String deployedImplementationType,
                                       String connectorProviderName) throws ConnectorCheckedException
    {
        final String methodName = "getFolderElementByPathName";

        if (dataFolderFile.exists())
        {
            try
            {
                FileFolderElement folderElement = this.getContext().getFolderByPathName(dataFolderFile.getAbsolutePath());

                if (folderElement == null)
                {
                    FileFolderProperties properties = new FileFolderProperties();

                    properties.setTypeName(assetTypeName);
                    properties.setPathName(dataFolderFile.getAbsolutePath());
                    properties.setName(dataFolderFile.getName());
                    properties.setDeployedImplementationType(deployedImplementationType);

                    this.getContext().addDataFolderToCatalog(properties, connectorProviderName);

                    folderElement = this.getContext().getFolderByPathName(dataFolderFile.getAbsolutePath());
                }

                /*
                 * The folder element should not be null at this point so an NPE at this point is unexpected.
                 */
                if ((folderElement.getElementHeader() == null) ||
                            (folderElement.getElementHeader().getGUID() == null) ||
                            (folderElement.getFileFolderProperties() == null))
                {
                    if (auditLog != null)
                    {
                        auditLog.logMessage(methodName,
                                            BasicFilesIntegrationConnectorsAuditCode.BAD_FOLDER_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                             dataFolderFile.getAbsolutePath(),
                                                                                                                             folderElement.toString()));
                    }
                }
                else
                {
                    return folderElement;
                }
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
    synchronized FileFolderElement getFolderElement(String folderElementGUID) throws ConnectorCheckedException
    {
        final String methodName = "getFolderElementByGUID";

        try
        {
            FileFolderElement folderElement = this.getContext().getFolderByGUID(folderElementGUID);

            if (folderElement == null)
            {
                return null;
            }

            if ((folderElement.getElementHeader() == null) ||
                        (folderElement.getElementHeader().getGUID() == null) ||
                        (folderElement.getFileFolderProperties() == null))
            {
                if (auditLog != null)
                {
                    auditLog.logMessage(methodName,
                                        BasicFilesIntegrationConnectorsAuditCode.BAD_FOLDER_ELEMENT.getMessageDefinition(connectorName,
                                                                                                                         folderElementGUID,
                                                                                                                         folderElement.toString()));
                }
            }
            else
            {
                return folderElement;
            }
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

        return null;
    }


    /**
     * The file no longer exists so this method updates the metadata catalog. This may be a call to delete() or an archive action
     * depending on the setting of the allowCatalogDelete configuration property.
     *
     * @param file Java file access object
     * @param retrievedElement catalogued element
     * @param methodName calling method
     */
    public void archiveFileInCatalog(File            file,
                                     DataFileElement retrievedElement,
                                     boolean         allowCatalogDelete,
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
                        (cataloguedElement.getProperties() != null) && (cataloguedElement.getProperties().getPathName() != null))
                {
                    if (allowCatalogDelete)
                    {
                        this.getContext().deleteDataFileFromCatalog(cataloguedElement.getElementHeader().getGUID(),
                                                                                    cataloguedElement.getProperties().getPathName());

                        if (auditLog != null)
                        {
                            auditLog.logMessage(methodName,
                                                BasicFilesIntegrationConnectorsAuditCode.DATA_FILE_DELETED.getMessageDefinition(connectorName,
                                                                                                                                cataloguedElement.getProperties().getPathName(),
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
                                                                                                                                 cataloguedElement.getProperties().getPathName(),
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

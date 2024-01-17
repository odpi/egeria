/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.accessservices.datamanager.metadataelements.FileFolderElement;
import org.odpi.openmetadata.accessservices.datamanager.properties.FileFolderProperties;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsErrorCode;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.ConfigException;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.exception.FileException;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.frameworks.integration.filelistener.FileDirectoryListenerInterface;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.integrationservices.files.connector.FilesIntegratorConnector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.odpi.openmetadata.adapters.connectors.integration.basicfiles.BasicFilesMonitorIntegrationProviderBase.CATALOG_TARGET_NAME;


/**
 * BasicFilesMonitorIntegrationConnectorBase provides common methods for the connectors in this module.
 */
public abstract class BasicFilesMonitorIntegrationConnectorBase extends FilesIntegratorConnector implements FileDirectoryListenerInterface
{
    String  fileTemplateQualifiedName           = null;
    String  directoryTemplateQualifiedName      = null;
    String  toDoTemplateQualifiedName           = null;
    String  incidentReportTemplateQualifiedName = null;
    boolean allowCatalogDelete                  = false;
    boolean waitForDirectory                    = false;

    /**
     * Directory to monitor caches information about a specific directory that is at the root of the monitoring.
     */
    static class DirectoryToMonitor
    {
        /**
         * Where did information about this directory come from?
         */
        String            sourceName        = null;

        /**
         * What is the short name for this directory?
         */
        String            directoryName     = null;

        /**
         * This is the folder element describing this directory from the open metadata ecosystem.
         */
        FileFolderElement dataFolderElement = null;

        /**
         * This is the Java File object that is accessing the directory.
         */
        File              directoryFile     = null;

        /**
         * This boolean indicates that the connector is registered to listen for changes in the directory.
         */
        boolean           isListening       = false;

        /**
         * This is the unique identifier of the CatalogTarget relationship that this directory matches.
         */
        String            catalogTargetGUID = null;
    }

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

        final String methodName = "start";

        Map<String, Object> configurationProperties = connectionProperties.getConfigurationProperties();

        if (configurationProperties != null)
        {
            if (configurationProperties.containsKey(BasicFilesMonitorIntegrationProviderBase.ALLOW_CATALOG_DELETE_CONFIGURATION_PROPERTY))
            {
                allowCatalogDelete = true;
            }

            if (configurationProperties.containsKey(BasicFilesMonitorIntegrationProviderBase.WAIT_FOR_DIRECTORY_CONFIGURATION_PROPERTY))
            {
                waitForDirectory = true;
            }

            if (configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.FILE_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY) != null)
            {
                fileTemplateQualifiedName = configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.FILE_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
            }

            if (configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.DIRECTORY_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY) != null)
            {
                directoryTemplateQualifiedName = configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.DIRECTORY_TEMPLATE_QUALIFIED_NAME_CONFIGURATION_PROPERTY).toString();
            }

            if (configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.TO_DO_TEMPLATE_CONFIGURATION_PROPERTY) != null)
            {
                directoryTemplateQualifiedName = configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.TO_DO_TEMPLATE_CONFIGURATION_PROPERTY).toString();
            }

            if (configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.INCIDENT_REPORT_TEMPLATE_CONFIGURATION_PROPERTY) != null)
            {
                directoryTemplateQualifiedName = configurationProperties.get(BasicFilesMonitorIntegrationProviderBase.INCIDENT_REPORT_TEMPLATE_CONFIGURATION_PROPERTY).toString();
            }
        }

        /*
         * Used for the message logging.
         */
        String endpointNetworkAddress = null;

        /*
         * The first possible catalog target is the endpoint from the connection.  It may be null.
         */
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            endpointNetworkAddress = endpoint.getAddress();

            DirectoryToMonitor directoryToMonitor = new DirectoryToMonitor();

            directoryToMonitor.sourceName = OpenMetadataType.ENDPOINT_TYPE_NAME + ":" + OpenMetadataType.NETWORK_ADDRESS_PROPERTY_NAME;
            directoryToMonitor.directoryName = endpoint.getAddress();
            directoryToMonitor.directoryFile = new File(endpoint.getAddress());
            directoryToMonitor.dataFolderElement = this.getFolderElement(directoryToMonitor.directoryFile);

            if ((! directoryToMonitor.directoryFile.exists()) && (! waitForDirectory))
            {
                /*
                 * The requested directory does not exist and the connector is configured not to wail for it.
                 */
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_FOUND,
                                          directoryToMonitor.sourceName,
                                          endpointNetworkAddress,
                                          null);
            }

            if (! directoryToMonitor.directoryFile.isDirectory())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_DIRECTORY,
                                          directoryToMonitor.sourceName,
                                          endpointNetworkAddress,
                                          null);
            }

            if (! directoryToMonitor.directoryFile.canRead())
            {
                this.throwConfigException(BasicFilesIntegrationConnectorsErrorCode.FILES_LOCATION_NOT_READABLE,
                                          directoryToMonitor.sourceName,
                                          endpointNetworkAddress,
                                          null);
            }

            directoriesToMonitor.add(directoryToMonitor);
        }

        /*
         * Record the configuration
         */
        if (auditLog != null)
        {
            auditLog.logMessage(methodName,
                                BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                      endpointNetworkAddress,
                                                                                                                      Boolean.toString(allowCatalogDelete),
                                                                                                                      Boolean.toString(waitForDirectory),
                                                                                                                      fileTemplateQualifiedName,
                                                                                                                      directoryTemplateQualifiedName,
                                                                                                                      toDoTemplateQualifiedName,
                                                                                                                      incidentReportTemplateQualifiedName));
        }
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
            int maxPageSize = 0;

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
                else if (directoryToMonitor.directoryFile.exists())
                {
                    if (directoryToMonitor.dataFolderElement == null)
                    {
                        directoryToMonitor.dataFolderElement = this.getFolderElement(directoryToMonitor.directoryFile);
                    }

                    if (! directoryToMonitor.isListening)
                    {
                        this.getContext().registerDirectoryTreeListener(this, directoryToMonitor.directoryFile, null);
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
        if ((this.getContext().isTypeOf(catalogTarget.getCatalogTargetElement(), OpenMetadataType.DATA_FOLDER.typeName)) &&
                    ((catalogTarget.getCatalogTargetName() == null) || (CATALOG_TARGET_NAME.equals(catalogTarget.getCatalogTargetName()))))
        {
            /*
             * It is the right type of catalog target.  We now need the path name associated with this catalog target.
             */
            FileFolderElement fileFolderElement = this.getFolderElement(catalogTarget.getRelationshipGUID());

            if ((fileFolderElement != null) && (fileFolderElement.getFileFolderProperties().getPathName() != null))
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
                DirectoryToMonitor directoryToMonitor = new DirectoryToMonitor();

                directoryToMonitor.sourceName = OpenMetadataType.CATALOG_TARGET_RELATIONSHIP_TYPE_NAME + ":" + catalogTarget.getRelationshipGUID();
                directoryToMonitor.directoryFile = pathFile;
                directoryToMonitor.directoryName = pathFile.getName();
                directoryToMonitor.dataFolderElement = fileFolderElement;
                directoryToMonitor.catalogTargetGUID = catalogTarget.getRelationshipGUID();

                directoriesToMonitor.add(directoryToMonitor);
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

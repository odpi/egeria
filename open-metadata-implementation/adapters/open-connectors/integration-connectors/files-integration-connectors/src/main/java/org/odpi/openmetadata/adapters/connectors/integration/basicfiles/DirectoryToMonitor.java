/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.controls.FilesTemplateType;
import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.controls.FileSystemConfigurationProperty;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.FileSystemProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.filelistener.FileDirectoryListenerInterface;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.Map;

/**
 * Directory to monitor caches information about a specific directory that is at the root of the monitoring.
 */
public abstract class DirectoryToMonitor implements FileDirectoryListenerInterface
{
    protected final PropertyHelper                            propertyHelper = new PropertyHelper();

    protected final AuditLog                                  auditLog;
    protected final BasicFilesMonitorIntegrationConnectorBase integrationConnector;
    protected final String                                    connectorName;

    /**
     * Where did information about this directory come from?
     */
    protected       String                   sourceName        = null;

    /**
     * What is the short name for this directory?
     */
    protected String            directoryName     = null;

    /**
     * Default values for the file system name and mount point.
     */
    protected String            fileSystemName = FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getExample();
    protected String            localMountPoint = FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getExample();
    protected String            canonicalMountPoint = FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getExample();


    /**
     * This is the Java File object that is accessing the directory.
     */
    protected File directoryFile = null;

    /**
     * This boolean indicates that the connector is registered to listen for changes in the directory.
     */
    protected boolean           isListening       = false;

    /**
     * This is the unique identifier of the CatalogTarget relationship that this directory matches.
     * Null means the value was supplied in the endpoint.
     */
    protected final String               catalogTargetGUID;
    protected       String               dataFolderGUID;
    protected final FileSystemProperties fileSystemProperties;
    protected final Map<String, Object>  configurationProperties;
    /**
     * Templates for cataloguing files and folders
     */
    protected final Map<String, String> templates = FilesTemplateType.getDefaultFileTemplates();

    /**
     * Template for creating todos
     */
    protected String  toDoTemplateQualifiedName           = null;
    protected String  toDoTemplateGUID           = null;

    /**
     * Template for creating incident reports
     */
    protected String  incidentReportTemplateQualifiedName = null;
    protected String  incidentReportTemplateGUID = null;

    protected String  newFileProcessName = null;

    /**
     * Policy for deleting
     */
    protected boolean allowCatalogDelete                  = false;

    /**
     * Does the physical directory have to exist or will it wait?
     */
    protected boolean waitForDirectory                    = false;

    /**
     * Should only file types that are recognized by the classifier be catalogued.
     */
    protected boolean catalogClassifiedFiles              = true;

    protected String  metadataSourceGUID = null;
    protected String  metadataSourceName = null;


    /**
     * Construct the monitor for a specific catalog target.
     *
     * @param connectorName name of associated connector
     * @param sourceName source of the pathname
     * @param pathName pathname to the directory
     * @param catalogTargetGUID optional catalog target GUID
     * @param dataFolderGUID optional GUID of the data folder element
     * @param deleteMethod should the connector use delete or archive?
     * @param templates names and GUIDs of templates
     * @param fileSystemProperties properties of the file system
     * @param configurationProperties parameters to further modify the behaviour of the connector
     * @param integrationConnector associated connector
     * @param auditLog logging destination
     */
    public DirectoryToMonitor (String                                    connectorName,
                               String                                    sourceName,
                               String                                    pathName,
                               String                                    catalogTargetGUID,
                               String                                    dataFolderGUID,
                               DeleteMethod                              deleteMethod,
                               Map<String,String>                        templates,
                               FileSystemProperties                      fileSystemProperties,
                               Map<String, Object>                       configurationProperties,
                               BasicFilesMonitorIntegrationConnectorBase integrationConnector,
                               AuditLog                                  auditLog)
    {
        final String methodName = "DirectoryToMonitor";

        this.connectorName = connectorName;
        this.sourceName = sourceName;
        if (pathName.startsWith("file:///"))
        {
            this.directoryName = pathName.substring(7);
        }
        else
        {
            this.directoryName = pathName;
        }

        this.directoryFile = new File(this.directoryName);
        this.dataFolderGUID = dataFolderGUID;
        this.fileSystemProperties = fileSystemProperties;
        this.configurationProperties = configurationProperties;

        this.catalogTargetGUID = catalogTargetGUID;
        this.integrationConnector = integrationConnector;
        this.auditLog = auditLog;

        if (configurationProperties != null)
        {
            if (configurationProperties.get(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getName()) != null)
            {
                fileSystemName = configurationProperties.get(FileSystemConfigurationProperty.FILE_SYSTEM_NAME.getName()).toString();
            }

            if (configurationProperties.get(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getName()) != null)
            {
                localMountPoint = configurationProperties.get(FileSystemConfigurationProperty.LOCAL_MOUNT_POINT.getName()).toString();
            }

            if (configurationProperties.get(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName()) != null)
            {
                localMountPoint = configurationProperties.get(FileSystemConfigurationProperty.CANONICAL_MOUNT_POINT.getName()).toString();
            }

            if (configurationProperties.get(BasicFilesMonitoringConfigurationProperty.NEW_FILE_PROCESS_NAME.getName()) != null)
            {
                newFileProcessName = configurationProperties.get(BasicFilesMonitoringConfigurationProperty.NEW_FILE_PROCESS_NAME.getName()).toString();
            }

            if (configurationProperties.containsKey(BasicFilesMonitorIntegrationProviderBase.CATALOG_ALL_FILES_CONFIGURATION_PROPERTY))
            {
                catalogClassifiedFiles = false;
            }

            if (configurationProperties.containsKey(BasicFilesMonitorIntegrationProviderBase.WAIT_FOR_DIRECTORY_CONFIGURATION_PROPERTY))
            {
                waitForDirectory = true;
            }
        }

        if (templates != null)
        {
            if (templates.get(OpenMetadataType.TO_DO.typeName) != null)
            {
                toDoTemplateQualifiedName = templates.get(OpenMetadataType.TO_DO.typeName);
            }
            if (templates.get(OpenMetadataType.INCIDENT_REPORT.typeName) != null)
            {
                incidentReportTemplateQualifiedName = templates.get(OpenMetadataType.DATA_FILE.typeName);
            }

            this.templates.putAll(templates);
        }

        if (fileSystemProperties != null)
        {
            fileSystemName = fileSystemProperties.getResourceName();
            localMountPoint = fileSystemProperties.getLocalMountPoint();
            canonicalMountPoint = fileSystemProperties.getCanonicalMountPoint();
        }

        if (deleteMethod != null)
        {
            allowCatalogDelete = deleteMethod.equals(DeleteMethod.SOFT_DELETE);
        }

        auditLog.logMessage(methodName,
                            BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                  this.directoryName,
                                                                                                                  Boolean.toString(waitForDirectory),
                                                                                                                  toDoTemplateQualifiedName,
                                                                                                                  incidentReportTemplateQualifiedName,
                                                                                                                  fileSystemName,
                                                                                                                  localMountPoint,
                                                                                                                  canonicalMountPoint));
    }


    /**
     * Return the data folder element for this directory.
     *
     * @return root element for the data folder
     * @throws ConnectorCheckedException problem accessing information
     */
    protected OpenMetadataRootElement getDataFolderElement() throws ConnectorCheckedException
    {
        OpenMetadataRootElement dataFolderElement;
        if (dataFolderGUID == null)
        {
            dataFolderElement = integrationConnector.getFolderElement(directoryFile, fileSystemProperties, configurationProperties);

            if (dataFolderElement != null)
            {
                this.dataFolderGUID     = dataFolderElement.getElementHeader().getGUID();
                this.metadataSourceGUID = dataFolderElement.getElementHeader().getOrigin().getHomeMetadataCollectionId();
                this.metadataSourceName = dataFolderElement.getElementHeader().getOrigin().getHomeMetadataCollectionName();
            }
        }
        else
        {
            dataFolderElement = integrationConnector.getFolderElement(dataFolderGUID);
        }

        return dataFolderElement;
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     *
     * @throws ConnectorCheckedException a problem with the connector.  It is not able to refresh the metadata.
     */
    public abstract void refresh() throws ConnectorCheckedException;
}

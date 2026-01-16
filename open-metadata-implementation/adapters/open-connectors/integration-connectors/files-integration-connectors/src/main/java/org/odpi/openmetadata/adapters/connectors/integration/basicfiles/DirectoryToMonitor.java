/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.basicfiles;

import org.odpi.openmetadata.adapters.connectors.integration.basicfiles.ffdc.BasicFilesIntegrationConnectorsAuditCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.filelistener.FileDirectoryListenerInterface;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.util.HashMap;
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
     * This is the folder element describing this directory from the open metadata ecosystem.
     */
    protected OpenMetadataRootElement dataFolderElement = null;

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
    protected String            catalogTargetGUID = null;

    /**
     * Templates for cataloguing files and folders
     */
    protected final Map<String, String> templates = new HashMap<>();

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

    protected final Map<String, Object> configurationProperties;

    protected String  metadataSourceGUID = null;
    protected String  metadataSourceName = null;


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
    public DirectoryToMonitor (String                                    connectorName,
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
        this.dataFolderElement = dataFolderElement;

        if (dataFolderElement != null)
        {
            this.metadataSourceGUID = dataFolderElement.getElementHeader().getOrigin().getHomeMetadataCollectionId();
            this.metadataSourceName = dataFolderElement.getElementHeader().getOrigin().getHomeMetadataCollectionName();
        }

        this.catalogTargetGUID = catalogTargetGUID;
        this.integrationConnector = integrationConnector;
        this.auditLog = auditLog;
        this.configurationProperties = configurationProperties;

        if (configurationProperties != null)
        {
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

        if (deleteMethod != null)
        {
            if (deleteMethod.equals(DeleteMethod.SOFT_DELETE))
            {
                allowCatalogDelete = true;
            }
            else
            {
                allowCatalogDelete = false;
            }
        }

        auditLog.logMessage(methodName,
                            BasicFilesIntegrationConnectorsAuditCode.CONNECTOR_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                                  this.directoryName,
                                                                                                                  Boolean.toString(waitForDirectory),
                                                                                                                  toDoTemplateQualifiedName,
                                                                                                                  incidentReportTemplateQualifiedName));
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

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.integration.filelistener.FileDirectoryListenerInterface;
import org.odpi.openmetadata.frameworks.integration.filelistener.FileListenerInterface;
import org.odpi.openmetadata.frameworks.integration.filelistener.FilesListenerManager;
import org.odpi.openmetadata.frameworks.integration.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.reports.IntegrationReportWriter;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * IntegrationContext is the base class for the integration context provided to the integration connector to provide access to open metadata
 * services.  Each integration service specializes this class to provide the method appropriate for the particular type of technology it
 * is supporting.
 * This base class supports the common methods available to all types of integration connectors.
 */
public class IntegrationContext
{
    protected final OpenIntegrationClient    openIntegrationClient;
    protected final OpenMetadataClient       openMetadataStoreClient;
    protected final String                   userId;
    protected final String                   externalSourceGUID;
    protected final String                   externalSourceName;
    protected       boolean                  externalSourceIsHome    = true;
    protected final String                   connectorName;
    protected final String                   integrationConnectorGUID;
    protected final PermittedSynchronization permittedSynchronization;

    private   final IntegrationGovernanceContext integrationGovernanceContext;
    protected final IntegrationReportWriter      integrationReportWriter;

    protected final int maxPageSize;

    private final FilesListenerManager listenerManager;

    private boolean isRefreshInProgress = false;

    /**
     * Constructor handles standard values for all integration contexts.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param serverName name of the integration daemon
     * @param openIntegrationClient client for calling the metadata server
     * @param openMetadataStoreClient client for calling the metadata server
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization enum
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param integrationConnectorGUID unique identifier of the integration connector entity (maybe null)
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public IntegrationContext(String                       connectorId,
                              String                       connectorName,
                              String                       connectorUserId,
                              String                       serverName,
                              OpenIntegrationClient        openIntegrationClient,
                              OpenMetadataClient           openMetadataStoreClient,
                              boolean                      generateIntegrationReport,
                              PermittedSynchronization     permittedSynchronization,
                              String                       externalSourceGUID,
                              String                       externalSourceName,
                              String                       integrationConnectorGUID,
                              AuditLog                     auditLog,
                              int                          maxPageSize)
    {
        this.openIntegrationClient        = openIntegrationClient;
        this.openMetadataStoreClient      = openMetadataStoreClient;
        this.permittedSynchronization     = permittedSynchronization;
        this.userId                       = connectorUserId;
        this.connectorName                = connectorName;
        this.externalSourceGUID           = externalSourceGUID;
        this.externalSourceName           = externalSourceName;
        this.integrationConnectorGUID     = integrationConnectorGUID;
        this.maxPageSize                  = maxPageSize;

        this.listenerManager = new FilesListenerManager(auditLog, connectorName);

        if (generateIntegrationReport)
        {
            this.integrationReportWriter = new IntegrationReportWriter(serverName,
                                                                       connectorId,
                                                                       connectorName,
                                                                       userId,
                                                                       openIntegrationClient,
                                                                       openMetadataStoreClient);
        }
        else
        {
            this.integrationReportWriter = null;
        }

        this.integrationGovernanceContext = constructIntegrationGovernanceContext(openMetadataStoreClient,
                                                                                  connectorUserId,
                                                                                  externalSourceGUID,
                                                                                  externalSourceName,
                                                                                  integrationReportWriter);

    }


    /**
     * Return a new integrationGovernanceContext for a specific connector.
     *
     * @param openMetadataStore client implementation
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @param integrationReportWriter report writer (maybe null)
     * @return new context
     */
    private IntegrationGovernanceContext constructIntegrationGovernanceContext(OpenMetadataClient      openMetadataStore,
                                                                               String                  userId,
                                                                               String                  externalSourceGUID,
                                                                               String                  externalSourceName,
                                                                               IntegrationReportWriter integrationReportWriter)
    {
        if (openMetadataStoreClient != null)
        {
            OpenMetadataAccess      openMetadataAccess      = new OpenMetadataAccess(openMetadataStore,
                                                                                     userId,
                                                                                     externalSourceGUID,
                                                                                     externalSourceName,
                                                                                     integrationReportWriter);
            MultiLanguageManagement multiLanguageManagement = new MultiLanguageManagement(openMetadataStore, userId);
            StewardshipAction          stewardshipAction          = new StewardshipAction(openMetadataStore, userId);
            ValidMetadataValuesContext validMetadataValuesContext = new ValidMetadataValuesContext(openMetadataStore, userId);

            return new IntegrationGovernanceContext(openMetadataAccess, multiLanguageManagement, stewardshipAction, validMetadataValuesContext);
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with an integration connector.
     * Each catalog target may be configured with an optional symbolic name to guide the integration connector on how to use
     * the catalog targets.
     *
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of named element headers
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public List<CatalogTarget> getCatalogTargets(int     startingFrom,
                                                 int     maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        if ((openIntegrationClient != null) && (integrationConnectorGUID != null))
        {
            return openIntegrationClient.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults);
        }

        return null;
    }


    /**
     * Return the qualified name of the software capability that represents an external source of metadata.  Used to control external provenance.
     * If null the provenance is LOCAL_COHORT.
     *
     * @return  string name
     */
    public String getMetadataSourceQualifiedName()
    {
        return externalSourceName;
    }


    /**
     * Return the permitted synchronization direction.  This setting may affect which method in the context are available to the integration
     * connector.
     *
     * @return permittedSynchronization enum
     */
    public PermittedSynchronization getPermittedSynchronization()
    {
        return permittedSynchronization;
    }


    /**
     * Return the integration governance context that provides access to various Governance Action Framework (GAF) function.
     *
     * @return IntegrationGovernanceContext context object
     */
    public IntegrationGovernanceContext getIntegrationGovernanceContext()
    {
        return  integrationGovernanceContext;
    }


    /**
     * Set whether an integration report should be assembled and published.
     * This allows the integration connector to turn off/on integration report writing.
     * It only has an effect if the connector is configured to allow report writing
     *
     * @param flag required behaviour
     */
    public void setActiveReportPublishing(boolean flag)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setActiveReportPublishing(flag);
        }
    }


    /**
     * Clear the report properties ready for a new report.  This is not
     * normally needed by the integration connector since it is called by the
     * connector handler just before refresh.  It is also called by publish report.
     */
    public void startRecording()
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.startRecording();
        }
    }


    /**
     * Save the relationship between an element and its anchor.  This is called by the methods of the context that
     * create, update, archive or delete elements.
     *
     * @param elementGUID unique identifier of the element
     * @param anchorGUID unique identifier of the associated anchor
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    protected void reportAnchorGUID(String elementGUID,
                                    String anchorGUID) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(elementGUID, anchorGUID);
        }
    }


    /**
     * Save the relationship between an element and its anchor.  This is called by the methods of the context that
     * create, update, archive or delete elements.
     *
     * @param elementGUID unique identifier of the element
     * @param anchorGUID unique identifier of the associated anchor
     * @param anchorTypeName type name of the associated anchor
     */
    protected void reportAnchorGUID(String elementGUID,
                                    String anchorGUID,
                                    String anchorTypeName)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setAnchor(elementGUID, anchorGUID, anchorTypeName);
        }
    }


    /**
     * Attempt to use the parent's GUID to discover the relationship between an element and its anchor.  This is used to identify
     * which report that the element should be reported under.
     *
     * @param elementGUID unique identifier of the element
     * @param parentGUID unique identifier of the associated parent
     */
    protected void reportParentGUID(String elementGUID, String parentGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.setParent(elementGUID, parentGUID);
        }
    }


    /**
     * Save information about a newly created element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementCreation(String elementGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementCreation(elementGUID);
        }
    }


    /**
     * Save information about a newly updated element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementUpdate(String elementGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementUpdate(elementGUID);
        }
    }


    /**
     * Save information about a newly archived or deleted element.
     *
     * @param elementGUID unique identifier of the element
     */
    protected void reportElementDelete(String elementGUID)
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.reportElementDelete(elementGUID);
        }
    }


    /**
     * Assemble the data collected and write out a report (if configured).
     *
     * @throws InvalidParameterException an invalid property has been passed
     * @throws UserNotAuthorizedException the user is not authorized
     * @throws PropertyServerException there is a problem communicating with the metadata server (or it has a logic error).
     */
    public void publishReport() throws InvalidParameterException,
                                       UserNotAuthorizedException,
                                       PropertyServerException
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.publishReport();
        }
    }


    /**
     * Return whether there is a refresh in progress.  This method is used in processEvent() to enable to connector to ignore
     * events while it is running refresh() since many of the events are caused by the refresh process.  Using this flag
     * prevents the connector from processing the same elements multiple times.
     *
     * @return boolean flag
     */
    public boolean isRefreshInProgress()
    {
        return isRefreshInProgress;
    }


    /**
     * Set up whether the refresh is in progress or not.
     *
     * @param refreshInProgress boolean flag
     */
    void setRefreshInProgress(boolean refreshInProgress)
    {
        isRefreshInProgress = refreshInProgress;
    }


    /**
     * Retrieve the anchorGUID from the Anchors classification.
     *
     * @param elementHeader element header where the classifications reside
     * @return anchorGUID or null
     */
    public String getAnchorGUID(ElementHeader elementHeader)
    {
        if (elementHeader.getClassifications() != null)
        {
            for (ElementClassification classification : elementHeader.getClassifications())
            {
                if (classification.getClassificationName().equals("Anchors"))
                {
                    Map<String, Object> properties = classification.getClassificationProperties();

                    if (properties != null)
                    {
                        Object anchorGUID = properties.get("anchorGUID");

                        if (anchorGUID != null)
                        {
                            return anchorGUID.toString();
                        }
                    }
                }
            }
        }

        return null;
    }



    /* ========================================================
     * Register/unregister for inbound events from the file system
     */


    /**
     * Register a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to monitor
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerFileListener(FileListenerInterface listener,
                                     File                  fileToMonitor) throws InvalidParameterException
    {
        listenerManager.registerFileListener(listener, fileToMonitor);
    }


    /**
     * Unregister a listener object that will be called each time a specific file is created, changed or deleted.
     *
     * @param listener      listener object
     * @param fileToMonitor name of the file to unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterFileListener(FileListenerInterface listener,
                                       File                  fileToMonitor) throws InvalidParameterException
    {
        listenerManager.unregisterFileListener(listener, fileToMonitor);
    }


    /**
     * Register a listener object that will be called each time a file is created, changed or deleted in a specific root directory.
     * The file filter lets you request that only certain types of files are returned.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory to monitor
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerDirectoryListener(FileDirectoryListenerInterface listener,
                                          File                           directoryToMonitor,
                                          FileFilter                     fileFilter) throws InvalidParameterException
    {
        listenerManager.registerDirectoryListener(listener, directoryToMonitor, fileFilter);
    }


    /**
     * Unregister a listener object for the directory.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the file directory unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterDirectoryListener(FileDirectoryListenerInterface listener,
                                            File                           directoryToMonitor) throws InvalidParameterException
    {
        listenerManager.unregisterDirectoryListener(listener, directoryToMonitor);
    }


    /**
     * Register a listener object that will be called each time a file is created, changed or deleted in a specific root directory
     * and any of its subdirectories.  The file filter lets you request that only certain types of files and/or directories are returned.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the root file directory to monitor from
     * @param fileFilter         a file filter implementation that restricts the files/directories that will be returned to the listener
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void registerDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                              File                           directoryToMonitor,
                                              FileFilter                     fileFilter) throws InvalidParameterException
    {
        listenerManager.registerDirectoryTreeListener(listener, directoryToMonitor, fileFilter);
    }


    /**
     * Unregister a listener object for the directory.
     *
     * @param listener           listener object
     * @param directoryToMonitor details of the root file directory to unregister
     *
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    public void unregisterDirectoryTreeListener(FileDirectoryListenerInterface listener,
                                                File                           directoryToMonitor) throws InvalidParameterException
    {
        listenerManager.unregisterDirectoryTreeListener(listener, directoryToMonitor);
    }


    /* ==============================================================
     * Controlling paging
     */

    /**
     * Returns the server configuration for the maximum number of elements that can be returned on a request.  It is used to control
     * paging.
     *
     * @return integer
     */
    public int getMaxPageSize()
    {
        return maxPageSize;
    }


    /* =============================
     * Working with types
     */


    /**
     * Understand the type of element.  It checks the type and super types.
     *
     * @param elementHeader element to validate
     * @param typeName type to test
     * @return boolean flag
     */
    public boolean isTypeOf(ElementHeader  elementHeader,
                            String         typeName)
    {
        return isTypeOf(elementHeader.getType(), typeName);
    }


    /**
     * Understand the type of element.  It checks the type and super types.
     *
     * @param elementType element to validate
     * @param typeName type to test
     * @return boolean flag
     */
    public boolean isTypeOf(ElementType  elementType,
                            String       typeName)
    {
        if (elementType != null)
        {
            List<String> elementTypeNames = new ArrayList<>();

            elementTypeNames.add(elementType.getTypeName());
            if (elementType.getSuperTypeNames() != null)
            {
                elementTypeNames.addAll(elementType.getSuperTypeNames());
            }

            if (elementTypeNames.contains(typeName))
            {
                return true;
            }
        }

        return false;
    }


    /**
     * Disconnect the file listener.
     *
     * @throws ConnectorCheckedException exception disconnecting
     */
    public void disconnect() throws ConnectorCheckedException
    {
        if (listenerManager != null)
        {
            listenerManager.disconnect();
        }
    }
}

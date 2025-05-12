/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.governanceaction.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.governanceaction.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.governanceaction.client.ActionControlInterface;
import org.odpi.openmetadata.frameworks.governanceaction.fileclassifier.FileClassifier;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AttachedClassification;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.filelistener.FileDirectoryListenerInterface;
import org.odpi.openmetadata.frameworks.integration.filelistener.FileListenerInterface;
import org.odpi.openmetadata.frameworks.integration.filelistener.FilesListenerManager;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.integration.reports.IntegrationReportWriter;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * IntegrationContext is the base class for the integration context provided to the integration connector to provide access to open metadata
 * services.  Each integration service specializes this class to provide the method appropriate for the particular type of technology it
 * is supporting.
 * This base class supports the common methods available to all types of integration connectors.
 */
public class IntegrationContext
{
    protected final PropertyHelper           propertyHelper = new PropertyHelper();

    protected final OpenIntegrationClient    openIntegrationClient;
    protected final ActionControlInterface   actionControlInterface;
    protected final GovernanceConfiguration  governanceConfiguration;
    protected final OpenMetadataClient       openMetadataStoreClient;
    protected final String                   userId;
    protected final String                   connectorName;
    protected final String                   integrationConnectorGUID;
    protected final PermittedSynchronization permittedSynchronization;

    protected       String                   externalSourceGUID;
    protected       String                   externalSourceName;
    protected       boolean                  externalSourceIsHome = true;

    protected final FileClassifier           fileClassifier;

    private   final ConnectedAssetContext        connectedAssetContext;
    private   final IntegrationGovernanceContext integrationGovernanceContext;
    protected final IntegrationReportWriter      integrationReportWriter;

    private   final Map<String, String> externalSourceCache = new HashMap<>();

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
     * @param governanceConfiguration client for managing catalog targets
     * @param openMetadataStoreClient client for calling the metadata server
     * @param actionControlInterface client for initiating governance actions
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
                              GovernanceConfiguration      governanceConfiguration,
                              OpenMetadataClient           openMetadataStoreClient,
                              ActionControlInterface       actionControlInterface,
                              boolean                      generateIntegrationReport,
                              PermittedSynchronization     permittedSynchronization,
                              String                       externalSourceGUID,
                              String                       externalSourceName,
                              String                       integrationConnectorGUID,
                              AuditLog                     auditLog,
                              int                          maxPageSize)
    {
        this.openIntegrationClient        = openIntegrationClient;
        this.governanceConfiguration      = governanceConfiguration;
        this.openMetadataStoreClient      = openMetadataStoreClient;
        this.actionControlInterface       = actionControlInterface;
        this.permittedSynchronization     = permittedSynchronization;
        this.userId                       = connectorUserId;
        this.connectorName                = connectorName;
        this.externalSourceGUID           = externalSourceGUID;
        this.externalSourceName           = externalSourceName;
        this.integrationConnectorGUID     = integrationConnectorGUID;
        this.maxPageSize                  = maxPageSize;

        this.fileClassifier               = new FileClassifier(new OpenMetadataStore(openMetadataStoreClient, userId, integrationConnectorGUID));
        this.listenerManager              = new FilesListenerManager(auditLog, connectorName);

        if (generateIntegrationReport)
        {
            this.integrationReportWriter = new IntegrationReportWriter(serverName,
                                                                       connectorId,
                                                                       integrationConnectorGUID,
                                                                       connectorName,
                                                                       userId,
                                                                       openIntegrationClient);
        }
        else
        {
            this.integrationReportWriter = null;
        }

        this.connectedAssetContext = new ConnectedAssetContext(connectorUserId, openIntegrationClient);

        this.integrationGovernanceContext = constructIntegrationGovernanceContext(openMetadataStoreClient,
                                                                                  actionControlInterface,
                                                                                  connectorUserId,
                                                                                  externalSourceGUID,
                                                                                  externalSourceName,
                                                                                  integrationConnectorGUID,
                                                                                  integrationReportWriter);
    }


    /**
     * Return a new integrationGovernanceContext for a specific connector.
     *
     * @param openMetadataStore client implementation
     * @param actionControlInterface interface for creating governance actions
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @param originatorGUID unique identifier of the source of the to do
     * @param integrationReportWriter report writer (maybe null)
     * @return new context
     */
    private IntegrationGovernanceContext constructIntegrationGovernanceContext(OpenMetadataClient      openMetadataStore,
                                                                               ActionControlInterface  actionControlInterface,
                                                                               String                  userId,
                                                                               String                  externalSourceGUID,
                                                                               String                  externalSourceName,
                                                                               String                  originatorGUID,
                                                                               IntegrationReportWriter integrationReportWriter)
    {
        if (openMetadataStoreClient != null)
        {
            OpenMetadataAccess openMetadataAccess = new OpenMetadataAccess(openMetadataStore,
                                                                           userId,
                                                                           externalSourceGUID,
                                                                           externalSourceName,
                                                                           originatorGUID,
                                                                           integrationReportWriter);
            MultiLanguageManagement    multiLanguageManagement    = new MultiLanguageManagement(openMetadataStore, userId);
            StewardshipAction          stewardshipAction          = new StewardshipAction(openMetadataStore, actionControlInterface, userId, originatorGUID);
            ValidMetadataValuesContext validMetadataValuesContext = new ValidMetadataValuesContext(openMetadataStore, userId);

            return new IntegrationGovernanceContext(openMetadataAccess, multiLanguageManagement, stewardshipAction, validMetadataValuesContext);
        }

        return null;
    }


    /**
     * Return the userId for this connector.  It is used to determine if changes where made by this connector.
     * It should not be needed to issue calls to open metadata.
     *
     * @return string
     */
    public String getMyUserId()
    {
        return userId;
    }


    /**
     * Return the file classifier that uses reference data to describe a file.
     *
     * @return file classifier utility
     */
    public FileClassifier getFileClassifier()
    {
        return fileClassifier;
    }



    /**
     * Determine whether a particular element should be catalogued.  The include list takes precedent over
     * the exclude list.
     *
     * @param elementName name of the element
     * @param excludedNames list of names to exclude (null means ignore value)
     * @param includedNames list of names to include (null means ignore value)
     * @return flag indicating whether to work with the database
     */
    public boolean elementShouldBeCatalogued(String       elementName,
                                             List<String> excludedNames,
                                             List<String> includedNames)
    {
        if (elementName == null)
        {
            return false;
        }

        if (includedNames != null)
        {
            return includedNames.contains(elementName);
        }
        else if (excludedNames != null)
        {
            return ! excludedNames.contains(elementName);
        }

        return true;
    }


    /**
     * Add a catalog target to this integration connector.
     *
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param properties properties for the relationship.
     *
     * @return catalog target GUID
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    public String addCatalogTarget(String                  metadataElementGUID,
                                   CatalogTargetProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            return governanceConfiguration.addCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID, properties);
        }

        return null;
    }


    /**
     * Add a catalog target to another integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration service.
     * @param metadataElementGUID unique identifier of the metadata element that is a catalog target.
     * @param properties properties for the relationship.
     *
     * @return catalog target GUID
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    public String addCatalogTarget(String                  integrationConnectorGUID,
                                   String                  metadataElementGUID,
                                   CatalogTargetProperties properties) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            return governanceConfiguration.addCatalogTarget(userId, integrationConnectorGUID, metadataElementGUID, properties);
        }

        return null;
    }


    /**
     * Update a catalog target relationship for an integration connector.
     *
     * @param relationshipGUID unique identifier of the relationship.
     * @param properties properties for the relationship.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the catalog target definition.
     */
    public void updateCatalogTarget(String                  relationshipGUID,
                                    CatalogTargetProperties properties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            governanceConfiguration.updateCatalogTarget(userId, relationshipGUID, properties);
        }
    }


    /**
     * Retrieve a specific catalog target associated with an integration connector.
     *
     * @param relationshipGUID unique identifier of the relationship.
     *
     * @return details of the integration connector and the elements it is to catalog
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public CatalogTarget getCatalogTarget(String relationshipGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            return governanceConfiguration.getCatalogTarget(userId, relationshipGUID);
        }

        return null;
    }


    /**
     * Retrieve the identifiers of the metadata elements identified as catalog targets with another integration connector.
     *
     * @param integrationConnectorGUID unique identifier of the integration connector.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of named elements
     * @throws InvalidParameterException one of the parameters is null or invalid,
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector definition.
     */
    public List<CatalogTarget> getCatalogTargets(String  integrationConnectorGUID,
                                                 int     startingFrom,
                                                 int     maximumResults) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException
    {
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            return governanceConfiguration.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults);
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
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            return governanceConfiguration.getCatalogTargets(userId, integrationConnectorGUID, startingFrom, maximumResults);
        }

        return null;
    }


    /**
     * Unregister a catalog target from the integration connector.
     *
     * @param relationshipGUID unique identifier of the integration connector.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem accessing/updating the integration connector definition.
     */
    public void removeCatalogTarget(String relationshipGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException
    {
        if ((governanceConfiguration != null) && (integrationConnectorGUID != null))
        {
            governanceConfiguration.removeCatalogTarget(userId, relationshipGUID);
        }
    }


    /**
     * Return the qualified name of the software capability that represents an external source of metadata.
     * Used to control external provenance and as a parent for some asset cataloguing.
     * If null the provenance is LOCAL_COHORT.
     *
     * @return  string name
     */
    public String getMetadataSourceQualifiedName()
    {
        return externalSourceName;
    }


    /**
     * Return the guid  of the software capability that represents an external source of metadata.
     * Used to control external provenance and as a parent for some asset cataloguing.
     * If null the provenance is LOCAL_COHORT.
     *
     * @return  string name
     */
    public String getMetadataSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Change the metadata collection that is in use when working with open metadata.  It should be the qualified name
     * of a software capability,  The qualified name is supplied through open metadata values and may be incorrect
     * which is why any exceptions from retrieving the software capability are passed through to the caller.
     *
     * @param metadataSourceQualifiedName supplied qualified name for the metadata collection
     *
     * @throws InvalidParameterException the unique name is null or not known.
     * @throws UserNotAuthorizedException the caller's userId is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public void setMetadataSourceQualifiedName(String metadataSourceQualifiedName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        if (metadataSourceQualifiedName == null)
        {
            this.externalSourceName = null;
            this.externalSourceGUID = null;
        }
        else
        {
            if (externalSourceCache.get(metadataSourceQualifiedName) != null)
            {
                this.externalSourceName = metadataSourceQualifiedName;
                this.externalSourceGUID = externalSourceCache.get(metadataSourceQualifiedName);
            }
            else
            {
                String metadataSourceGUID = openMetadataStoreClient.getMetadataElementGUIDByUniqueName(userId,
                                                                                                       metadataSourceQualifiedName,
                                                                                                       OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                                       false,
                                                                                                       false,
                                                                                                       null,
                                                                                                       new Date());

                if (metadataSourceGUID != null)
                {
                    this.externalSourceName = metadataSourceQualifiedName;
                    this.externalSourceGUID = metadataSourceGUID;

                    externalSourceCache.put(metadataSourceQualifiedName, metadataSourceGUID);
                }
            }
        }

        integrationGovernanceContext.setExternalSourceIds(externalSourceGUID, externalSourceName);
    }


    /**
     * Change the metadata collection that is in use when working with open metadata.  It should be the qualified name
     * of a software capability,  The qualified name is supplied through open metadata values and may be incorrect
     * which is why any exceptions from retrieving the software capability are passed through to the caller.
     *
     * @param metadataSourceGUID unique identifier of the metadata source (if known)
     * @param metadataSourceQualifiedName supplied qualified name for the metadata collection
     *
     * @throws InvalidParameterException the unique name is null or not known.
     * @throws UserNotAuthorizedException the caller's userId is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    public void setMetadataSourceQualifiedName(String metadataSourceGUID,
                                               String metadataSourceQualifiedName) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException
    {
        if (metadataSourceQualifiedName == null)
        {
            this.externalSourceName = null;
            this.externalSourceGUID = null;
        }
        else
        {
            this.externalSourceName = metadataSourceQualifiedName;
            this.externalSourceGUID = metadataSourceGUID;

            externalSourceCache.put(metadataSourceQualifiedName, metadataSourceGUID);
        }

        integrationGovernanceContext.setExternalSourceIds(externalSourceGUID, externalSourceName);
    }


    /**
     * Return the flag indicating whether the external source name is to be used as the new element's
     * metadata collection, or they belong to the local cohort.
     *
     * @return flag
     */
    public boolean getExternalSourceIsHome()
    {
        return externalSourceIsHome;
    }


    /**
     * Set the flag indicating whether the external source name is to be used as the new element's
     * metadata collection, or they belong to the local cohort.
     *
     * @param newValue flag
     */
    public void setExternalSourceIsHome(boolean newValue)
    {
        this.externalSourceIsHome = newValue;
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
     * Return the connected asset context that support an integration connector working with assets and their connectors.
     *
     * @return connected asset context
     */
    public ConnectedAssetContext getConnectedAssetContext()
    {
        return connectedAssetContext;
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
    void startRecording()
    {
        if (integrationReportWriter != null)
        {
            integrationReportWriter.startRecording();
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
    void publishReport() throws InvalidParameterException,
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
                if (classification.getClassificationName().equals(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
                {
                    Map<String, Object> properties = classification.getClassificationProperties();

                    if (properties != null)
                    {
                        Object anchorGUID = properties.get(OpenMetadataProperty.ANCHOR_GUID.name);

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



    /**
     * Retrieve the anchorGUID from the Anchors classification.
     *
     * @param openMetadataElement element header where the classifications reside
     * @return anchorGUID or null
     */
    public String getAnchorGUID(OpenMetadataElement openMetadataElement)
    {
        final String methodName = "getAnchorGUID";

        if (openMetadataElement.getClassifications() != null)
        {
            for (AttachedClassification classification : openMetadataElement.getClassifications())
            {
                if (classification.getClassificationName().equals(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName))
                {
                    return propertyHelper.getStringProperty(connectorName,
                                                            OpenMetadataProperty.ANCHOR_GUID.name,
                                                            classification.getClassificationProperties(),
                                                            methodName);
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

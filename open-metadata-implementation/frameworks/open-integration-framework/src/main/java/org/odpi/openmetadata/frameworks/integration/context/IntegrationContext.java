/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.context;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.client.ConnectedAssetClient;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageEventListener;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageListenerManager;
import org.odpi.openmetadata.frameworks.integration.openlineage.OpenLineageRunEvent;
import org.odpi.openmetadata.frameworks.opengovernance.client.GovernanceConfiguration;
import org.odpi.openmetadata.frameworks.opengovernance.client.OpenGovernanceClient;
import org.odpi.openmetadata.frameworks.opengovernance.connectorcontext.StewardshipAction;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.ConnectorContextBase;
import org.odpi.openmetadata.frameworks.openmetadata.enums.DeleteMethod;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementOriginCategory;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * IntegrationContext is the base class for the integration context provided to the integration connector to provide access to open metadata
 * services.  Each integration service specializes this class to provide the method appropriate for the particular type of technology it
 * is supporting.
 * This base class supports the common methods available to all types of integration connectors.
 */
public class IntegrationContext extends ConnectorContextBase
{
    protected final OpenMetadataEventClient    openMetadataEventClient;
    private final   OpenLineageListenerManager openLineageListenerManager;

    protected final OpenGovernanceClient    openGovernanceClient;
    protected final ConnectedAssetClient    connectedAssetClient;
    protected final GovernanceConfiguration governanceConfiguration;
    protected final StewardshipAction       stewardshipAction;
    private final   ConnectedAssetContext    connectedAssetContext;
    private   final Map<String, String> externalSourceCache = new HashMap<>();

    protected final PermittedSynchronization permittedSynchronization;

    private boolean isRefreshInProgress = false;
    private boolean listenerRegistered = false;

    /**
     * Constructor handles standard values for all integration contexts.
     *
     * @param localServerName name of local server
     * @param localServiceName name of the service to call
     * @param externalSourceGUID metadata collection unique id
     * @param externalSourceName metadata collection unique name
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param connectorGUID unique identifier of the integration connector entity (maybe null)
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param permittedSynchronization enum
     * @param openMetadataClient client to access open metadata store
     * @param openMetadataEventClient client to access open metadata events
     * @param connectedAssetClient client for working with connectors
     * @param governanceConfiguration client for managing catalog targets
     * @param openGovernanceClient client for initiating governance actions
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     * @param deleteMethod default delete method
     */
    public IntegrationContext(String                       localServerName,
                              String                       localServiceName,
                              String                       externalSourceGUID,
                              String                       externalSourceName,
                              String                       connectorId,
                              String                       connectorName,
                              String                       connectorUserId,
                              String                       connectorGUID,
                              boolean                      generateIntegrationReport,
                              PermittedSynchronization     permittedSynchronization,
                              OpenMetadataClient           openMetadataClient,
                              OpenMetadataEventClient      openMetadataEventClient,
                              ConnectedAssetClient         connectedAssetClient,
                              OpenLineageListenerManager   openLineageListenerManager,
                              GovernanceConfiguration      governanceConfiguration,
                              OpenGovernanceClient         openGovernanceClient,
                              AuditLog                     auditLog,
                              int                          maxPageSize,
                              DeleteMethod                 deleteMethod)
    {
        super(localServerName,
              localServiceName,
              externalSourceGUID,
              externalSourceName,
              connectorId,
              connectorName,
              connectorUserId,
              connectorGUID,
              generateIntegrationReport,
              openMetadataClient,
              auditLog,
              maxPageSize,
              deleteMethod);

        this.openLineageListenerManager = openLineageListenerManager;
        this.governanceConfiguration    = governanceConfiguration;
        this.openGovernanceClient       = openGovernanceClient;
        this.connectedAssetClient       = connectedAssetClient;
        this.permittedSynchronization   = permittedSynchronization;

        this.openMetadataEventClient    = openMetadataEventClient;

        this.connectedAssetContext      = new ConnectedAssetContext(connectorUserId, connectedAssetClient);


        this.stewardshipAction          = new StewardshipAction(this,
                                                                localServerName,
                                                                localServiceName,
                                                                connectorUserId,
                                                                connectorGUID,
                                                                externalSourceGUID,
                                                                externalSourceName,
                                                                openMetadataClient,
                                                                openGovernanceClient,
                                                                auditLog,
                                                                maxPageSize);
    }


    /**
     * Return a new context for a configured catalog target.
     *
     * @param requestedCatalogTarget details of the catalog target configuration
     * @return new context with the correct external source
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public CatalogTargetContext getCatalogTargetContext(CatalogTarget requestedCatalogTarget) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        String metadataSourceName = requestedCatalogTarget.getMetadataSourceQualifiedName();

        if (metadataSourceName == null)
        {
            metadataSourceName = externalSourceName; // default to the value set up for the connector in RegisteredIntegrationConnector
        }

        String metadataSourceGUID = getMetadataSourceGUID(metadataSourceName,
                                                          requestedCatalogTarget.getCatalogTargetElement());

        return new CatalogTargetContext(localServerName,
                                        localServiceName,
                                        metadataSourceGUID,
                                        metadataSourceName,
                                        connectorId,
                                        connectorName,
                                        connectorUserId,
                                        connectorGUID,
                                        generateIntegrationReport,
                                        requestedCatalogTarget.getPermittedSynchronization(),
                                        openMetadataClient,
                                        openMetadataEventClient,
                                        connectedAssetClient,
                                        openLineageListenerManager,
                                        governanceConfiguration,
                                        openGovernanceClient,
                                        auditLog,
                                        maxPageSize,
                                        requestedCatalogTarget.getDeleteMethod());
    }


    /* ======================================================================================
     * Register a listener to receive open lineage events.
     */

    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its processOpenLineageRunEvent()
     * method is called each time an open lineage event is published to the integration daemon.
     *
     * @param listener listener to call
     */
    public void registerListener(OpenLineageEventListener listener)
    {
        openLineageListenerManager.registerListener(listener);
    }


    /**
     * Called each time an integration connector wishes to publish an open lineage run event.  The event is formatted and passed to each of the
     * registered open lineage event listeners.
     *
     * @param rawEvent json payload to send for the event
     */
    public void publishOpenLineageRunEvent(String rawEvent)
    {
        openLineageListenerManager.publishOpenLineageRunEvent(rawEvent);
    }


    /**
     * Called each time an open lineage run event is published to the integration daemon.  The integration connector is able to
     * work with the formatted event using the Egeria beans or reformat the open lineage run event using the supplied open lineage backend beans
     * or another set of beans.
     *
     * @param event bean for the event
     */
    public void publishOpenLineageRunEvent(OpenLineageRunEvent event) { openLineageListenerManager.publishOpenLineageRunEvent(event); }


    /* ========================================================
     * Configuration information
     */

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



    /* ========================================================
     * Register for inbound events from the Open Metadata OutTopic
     */


    /**
     * Return a flag indicating whether a listener has been registered or not.
     *
     * @return true means the listener has been successfully registered
     */
    public boolean noListenerRegistered()
    {
        return !listenerRegistered;
    }



    /**
     * Register a listener object that will be passed each of the events published by
     * the Open Metadata Server.
     *
     * @param listener listener object
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void registerListener(OpenMetadataEventListener listener) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        openMetadataEventClient.registerListener(connectorUserId, listener);
        this.listenerRegistered = true;
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
     * Return the connected asset context that support an integration connector working with assets and their connectors.
     *
     * @return connected asset context
     */
    public StewardshipAction getStewardshipAction()
    {
        return stewardshipAction;
    }


    /**
     * Return the open governance client.  This supports defining and initiating governance actions,
     * and managing duplicates.
     *
     * @return client
     */
    public OpenGovernanceClient getOpenGovernanceClient()
    {
        return openGovernanceClient;
    }


    /**
     * Return the unique identifier of the element that represents this integration connector in open metadata.
     *
     * @return string guid
     */
    public String getIntegrationConnectorGUID() { return connectorGUID; }


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
     * Return the unique identifier of metadata collection that corresponds to the qualified name
     * of a software capability,  This qualified name is supplied through open metadata values and may be incorrect
     * which is why any exceptions from retrieving the software capability are passed through to the caller.
     *
     * @param metadataSourceQualifiedName supplied qualified name for the metadata collection
     *
     * @return null or unique identifier of the associated software capability
     * @throws InvalidParameterException the unique name is null or not known.
     * @throws UserNotAuthorizedException the caller's userId is not able to access the element
     * @throws PropertyServerException there is a problem accessing the metadata store
     */
    private String getMetadataSourceGUID(String                  metadataSourceQualifiedName,
                                         OpenMetadataRootElement catalogTargetElement) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        if (metadataSourceQualifiedName != null)
        {
            if (externalSourceCache.get(metadataSourceQualifiedName) != null)
            {
                return externalSourceCache.get(metadataSourceQualifiedName);
            }
            else
            {
                AssetClient assetClient = this.getAssetClient(OpenMetadataType.METADATA_COLLECTION.typeName);

                String metadataSourceGUID = assetClient.setUpMetadataSource(metadataSourceQualifiedName,
                                                                            catalogTargetElement.getElementHeader().getGUID(),
                                                                            connectorName,
                                                                            connectorUserId,
                                                                            ElementOriginCategory.EXTERNAL_SOURCE);

                if (metadataSourceGUID != null)
                {
                    externalSourceCache.put(metadataSourceQualifiedName, metadataSourceGUID);

                    return metadataSourceGUID;
                }
            }
        }

        return null;
    }


    /**
     * Return whether there is a refresh in progress.  This method is used in processEvent() to enable to connector to ignore
     * events while it is running refresh() since many of the events are caused by the refresh process.  Using this flag
     * prevents the connector from processing the same elements multiple times.
     *
     * @return boolean flag
     */
    public boolean noRefreshInProgress()
    {
        return ! isRefreshInProgress;
    }


    /**
     * Set up whether the refresh is in progress or not.
     *
     * @param refreshInProgress boolean flag
     */
    public void setRefreshInProgress(boolean refreshInProgress)
    {
        isRefreshInProgress = refreshInProgress;

        if (connectorActivityReportWriter != null)
        {
            if (refreshInProgress)
            {
                connectorActivityReportWriter.setRefreshStartDate();
            }
            else
            {
                connectorActivityReportWriter.setRefreshEndDate();
            }
        }
    }
}

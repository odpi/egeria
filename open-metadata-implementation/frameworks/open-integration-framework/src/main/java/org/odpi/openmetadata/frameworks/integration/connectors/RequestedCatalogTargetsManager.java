/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.opengovernance.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataEventListener;
import org.odpi.openmetadata.frameworks.openmetadata.events.OpenMetadataOutTopicEvent;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the map of catalog targets that this integration connector is working with.  It is also responsible for
 * keeping the list current.  This is the OIF implementation of CatalogTargetChangeListener that is driven from
 * the connector base.  It is also possible for an integration connector to register itself as a listener with this class.
 */
public class RequestedCatalogTargetsManager implements CatalogTargetChangeListener
{
    /**
     * A map of relationshipGUID->RequestedCatalogTarget properties to indicate the catalog targets that are known to
     * this connector.
     */
    private final CatalogTargetMap currentCatalogTargetMap = new CatalogTargetMap();

    private final List<CatalogTargetChangeListener>  registeredChangeListeners = new ArrayList<>();

    private final PropertyHelper propertyHelper = new PropertyHelper();

    private final Map<String, Object> connectorConfigProperties;
    private final String              connectorName;
    private final AuditLog            auditLog;


    /**
     * Constructor - passed the values from the integration connector that are needed by each catalog target,
     *
     * @param connectorConfigProperties base configuration properties - these are overlaid with the config properties from the catalog target relationship
     * @param connectorName can of this connector for error messages
     * @param auditLog audit log for this connector
     */
    public RequestedCatalogTargetsManager(Map<String, Object> connectorConfigProperties,
                                          String              connectorName,
                                          AuditLog            auditLog)
    {
        if (connectorConfigProperties != null)
        {
            this.connectorConfigProperties = new HashMap<>(connectorConfigProperties);
        }
        else
        {
            this.connectorConfigProperties = new HashMap<>();
        }

        this.connectorName             = connectorName;
        this.auditLog                  = auditLog;
    }


    /**
     * Create the map of configuration properties for the catalog target.
     *
     * @param targetConfigProperties additional properties supplied by the relationship
     * @return combined map
     */
    private Map<String,Object> getCombinedConfigurationProperties(Map<String,Object> targetConfigProperties)
    {
        Map<String, Object> combinedConfigurationProperties = new HashMap<>(this.connectorConfigProperties);

        if (targetConfigProperties != null)
        {
            combinedConfigurationProperties.putAll(targetConfigProperties);
        }

        if (combinedConfigurationProperties.isEmpty())
        {
            return null;
        }

        return combinedConfigurationProperties;
    }


    /**
     * Return a list of requested catalog targets for the connector.  These are extracted from the metadata store.
     *
     * @param integrationContext the integration context for the parent connector
     * @param catalogTargetFactory subclass of connector implementation that is able to create a catalog target processor
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    public List<RequestedCatalogTarget> retrieveKnownCatalogTargets(IntegrationContext   integrationContext,
                                                                    CatalogTargetFactory catalogTargetFactory) throws ConnectorCheckedException
    {
        final String methodName = "retrieveKnownCatalogTargets";

        try
        {
            AssetClient assetClient = integrationContext.getAssetClient();
            int startFrom = 0;

            List<OpenMetadataRootElement> catalogTargetList  = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                                             assetClient.getQueryOptions(startFrom,
                                                                                             integrationContext.getMaxPageSize()));

            while (catalogTargetList != null)
            {
                for (OpenMetadataRootElement catalogTarget : catalogTargetList)
                {
                    if ((catalogTarget != null) && (catalogTarget.getRelatedBy() != null) && (catalogTarget.getRelatedBy().getRelationshipProperties() instanceof CatalogTargetProperties catalogTargetProperties))
                    {
                        RequestedCatalogTarget knownCatalogTarget = currentCatalogTargetMap.get(catalogTarget.getRelatedBy().getRelationshipHeader().getGUID());

                        if (knownCatalogTarget == null)
                        {
                            CatalogTarget newCatalogTarget = new CatalogTarget(catalogTargetProperties, catalogTarget);
                            /*
                             * This is a new catalog target.
                             */
                            knownCatalogTarget = setUpNewRequestedCatalogTarget(catalogTargetFactory,
                                                                                newCatalogTarget,
                                                                                integrationContext);

                            this.newCatalogTarget(knownCatalogTarget);
                        }
                        else if (! knownCatalogTarget.getRelationshipVersions().equals(catalogTarget.getRelatedBy().getRelationshipHeader().getVersions()))
                        {
                            RequestedCatalogTarget oldCatalogTarget = knownCatalogTarget;
                            CatalogTarget newCatalogTarget = new CatalogTarget(catalogTargetProperties, catalogTarget);

                            knownCatalogTarget = setUpNewRequestedCatalogTarget(catalogTargetFactory,
                                                                                newCatalogTarget,
                                                                                integrationContext);

                            this.updatedCatalogTarget(oldCatalogTarget, knownCatalogTarget);
                        }
                    }
                }

                startFrom         = startFrom + integrationContext.getMaxPageSize();
                catalogTargetList = assetClient.getCatalogTargets(integrationContext.getIntegrationConnectorGUID(),
                                                                  assetClient.getQueryOptions(startFrom, integrationContext.getMaxPageSize()));
            }

            return new ArrayList<>(currentCatalogTargetMap.values());
        }
        catch (ConnectorCheckedException exception)
        {
            throw exception;
        }
        catch (Exception exception)
        {
            auditLog.logException(methodName,
                                  OIFAuditCode.GET_CATALOG_TARGET_EXCEPTION.getMessageDefinition(exception.getClass().getName(),
                                                                                                 connectorName,
                                                                                                 exception.getMessage()),
                                  exception);
        }

        return null;
    }


    /**
     * Retrieve the latest list of catalog targets and call refresh on each one.
     *
     * @param integrationContext the integration context for the parent connector
     * @param catalogTargetFactory subclass of connector implementation that is able to create a catalog target processor
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     * @throws UserNotAuthorizedException connector has been shut down
     */
    public void refreshCatalogTargets(IntegrationContext   integrationContext,
                                      CatalogTargetFactory catalogTargetFactory) throws ConnectorCheckedException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "refreshCatalogTargets";

        integrationContext.validateIsActive(methodName);

        List<RequestedCatalogTarget> requestedCatalogTargets = this.retrieveKnownCatalogTargets(integrationContext,
                                                                                                catalogTargetFactory);

        if ((requestedCatalogTargets == null) || (requestedCatalogTargets.isEmpty()))
        {
            auditLog.logMessage(methodName, OIFAuditCode.NO_CATALOG_TARGETS.getMessageDefinition(connectorName));
        }
        else
        {
            try
            {
                for (RequestedCatalogTarget requestedCatalogTarget : requestedCatalogTargets)
                {

                    if (requestedCatalogTarget instanceof CatalogTargetProcessorBase catalogTargetProcessorBase)
                    {
                        try
                        {
                            auditLog.logMessage(methodName,
                                                OIFAuditCode.REFRESHING_CATALOG_TARGET.getMessageDefinition(connectorName,
                                                                                                            requestedCatalogTarget.getCatalogTargetName()));
                            catalogTargetProcessorBase.refresh();
                        }
                        catch (Exception error)
                        {
                            auditLog.logMessage(methodName,
                                                OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName + "::" + requestedCatalogTarget.getCatalogTargetName(),
                                                                                                       error.getMessage()));
                        }
                    }
                }
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                           error.getClass().getName(),
                                                                                           methodName,
                                                                                           error.getMessage()));
            }

            auditLog.logMessage(methodName, OIFAuditCode.REFRESHED_CATALOG_TARGETS.getMessageDefinition(connectorName,
                                                                                                        Integer.toString(requestedCatalogTargets.size())));
        }
    }


    /**
     * Retrieve the latest list of catalog targets and call refresh on each one.
     *
     * @param integrationContext the integration context for the parent connector
     * @param event event to pass on to each catalog target that is capable
     * @throws UserNotAuthorizedException connector has been shut down
     */
    public void passEventToCatalogTargets(IntegrationContext        integrationContext,
                                          OpenMetadataOutTopicEvent event) throws UserNotAuthorizedException
    {
        final String methodName = "passEventToCatalogTargets";

        integrationContext.validateIsActive(methodName);

        /*
         * just use the current list of catalog targets - the catalog target list is refreshed during the standard
         * refresh process. this is a performance optimization.
         */
        List<RequestedCatalogTarget> requestedCatalogTargets = this.getRequestedCatalogTargets();

        if (requestedCatalogTargets != null)
        {
            try
            {
                for (RequestedCatalogTarget requestedCatalogTarget : requestedCatalogTargets)
                {
                    if (requestedCatalogTarget instanceof OpenMetadataEventListener openMetadataEventListener)
                    {
                        try
                        {
                            openMetadataEventListener.processEvent(event);
                        }
                        catch (Exception error)
                        {
                            auditLog.logMessage(methodName,
                                                OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                       error.getClass().getName(),
                                                                                                       methodName + "::" + requestedCatalogTarget.getCatalogTargetName(),
                                                                                                       error.getMessage()));
                        }
                    }
                }
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                           error.getClass().getName(),
                                                                                           methodName,
                                                                                           error.getMessage()));
            }
        }
    }


    /**
     * Set up a new processor for the catalog target.
     *
     * @param catalogTargetFactory connector
     * @param retrievedCatalogTarget target relationship from the open metadata repository
     * @param integrationContext connector's context
     * @return filled out catalog target processor
     * @throws ConnectorCheckedException connector problem
     * @throws InvalidParameterException the asset guid is not recognized or the userId is null
     * @throws ConnectionCheckedException there are errors in the configuration of the connection which is preventing
     *                                      the creation of a connector.
     * @throws ConnectorCheckedException there are errors in the initialization of the connector.
     * @throws UserNotAuthorizedException the user is not authorized to access the asset and/or connection needed to
     *                                    create the connector.
     * @throws PropertyServerException there was a problem in the store whether the asset/connection properties are kept.
     */
    private RequestedCatalogTarget setUpNewRequestedCatalogTarget(CatalogTargetFactory catalogTargetFactory,
                                                                  CatalogTarget        retrievedCatalogTarget,
                                                                  IntegrationContext   integrationContext) throws ConnectorCheckedException,
                                                                                                                  InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  ConnectionCheckedException,
                                                                                                                  UserNotAuthorizedException
    {
        Connector connectorToTarget = null;

        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader(), OpenMetadataType.ASSET.typeName))
        {
            connectorToTarget = integrationContext.getConnectedAssetContext().getConnectorForAsset(retrievedCatalogTarget.getCatalogTargetElement().getElementHeader().getGUID(), auditLog);
        }

        RequestedCatalogTarget newRequestedCatalogTarget = catalogTargetFactory.getNewRequestedCatalogTargetSkeleton(retrievedCatalogTarget,
                                                                                                                     integrationContext.getCatalogTargetContext(retrievedCatalogTarget),
                                                                                                                     connectorToTarget);
        /*
         * Ensure that the catalog target sees all of the configuration properties.
         */
        newRequestedCatalogTarget.setConfigurationProperties(this.getCombinedConfigurationProperties(retrievedCatalogTarget.getConfigurationProperties()));

        /*
         * Ensure the catalog target honours the connector's permitted synchronization unless it has been explicitly set.
         */
        if (newRequestedCatalogTarget.getPermittedSynchronization() == null)
        {
            newRequestedCatalogTarget.setPermittedSynchronization(integrationContext.getPermittedSynchronization());
        }

        return newRequestedCatalogTarget;
    }


    /**
     * Return list of the current catalog targets - used in event processing for speed.
     *
     * @return list of the current catalog targets
     */
    public List<RequestedCatalogTarget> getRequestedCatalogTargets()
    {
        return currentCatalogTargetMap.values();
    }


    /**
     * Add a listener.
     *
     * @param listener listener to register
     */
    public void registerCatalogTargetChangeListener(CatalogTargetChangeListener listener)
    {
        if (listener != null)
        {
            registeredChangeListeners.add(listener);
        }
    }


    /**
     * This catalog target has been created since the last refresh.
     *
     * @param catalogTarget new catalog target
     */
    @Override
    public void newCatalogTarget(RequestedCatalogTarget catalogTarget) throws UserNotAuthorizedException, ConnectorCheckedException
    {
        final String methodName = "newCatalogTarget";

        currentCatalogTargetMap.put(catalogTarget);

        for (CatalogTargetChangeListener listener : registeredChangeListeners)
        {
            listener.newCatalogTarget(catalogTarget);
        }

        startConnector(catalogTarget, methodName);

        if (catalogTarget instanceof CatalogTargetProcessorBase catalogTargetProcessor)
        {
            catalogTargetProcessor.start();
        }
    }


    /**
     * This catalog target has new properties.
     *
     * @param oldCatalogTarget old values
     * @param newCatalogTarget new values
     */
    @Override
    public void updatedCatalogTarget(RequestedCatalogTarget oldCatalogTarget,
                                     RequestedCatalogTarget newCatalogTarget)
    {
        final String methodName = "updatedCatalogTarget";

        this.disconnectConnector(oldCatalogTarget, methodName);

        currentCatalogTargetMap.put(newCatalogTarget);

        for (CatalogTargetChangeListener listener : registeredChangeListeners)
        {
            listener.updatedCatalogTarget(oldCatalogTarget, newCatalogTarget);
        }

        startConnector(newCatalogTarget, methodName);
    }


    /**
     * This catalog target has been removed from the connector.
     *
     * @param catalogTarget removed relationship
     * @throws ConnectorCheckedException problem disconnecting
     */
    @Override
    public void removedCatalogTarget(RequestedCatalogTarget catalogTarget) throws ConnectorCheckedException
    {
        final String methodName = "removedCatalogTarget";

        this.disconnectConnector(catalogTarget, methodName);

        currentCatalogTargetMap.remove(catalogTarget.getRelationshipGUID());

        for (CatalogTargetChangeListener listener : registeredChangeListeners)
        {
            listener.removedCatalogTarget(catalogTarget);
        }

        if (catalogTarget instanceof CatalogTargetProcessorBase catalogTargetProcessor)
        {
            catalogTargetProcessor.disconnect();
        }
    }


    /**
     * Start the resource connector to the catalog target
     *
     * @param catalogTarget details of the target
     * @param methodName calling method
     */
    private void startConnector(RequestedCatalogTarget catalogTarget,
                                String                 methodName)
    {
        if (catalogTarget.getConnectorToTarget() != null)
        {
            try
            {
                catalogTarget.getConnectorToTarget().start();
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    OIFAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                           error.getClass().getName(),
                                                                                           catalogTarget.getCatalogTargetName(),
                                                                                           error.getMessage()));
            }
        }
    }


    /**
     * Disconnect the resource connector for a catalog target.  If it fails, an audit log message is produced, but the
     * integration connector continues,
     *
     * @param catalogTarget details of the target
     * @param methodName calling method
     */
    private void disconnectConnector(RequestedCatalogTarget catalogTarget,
                                     String                 methodName)
    {
        if ((catalogTarget != null) && (catalogTarget.getConnectorToTarget() != null))
        {
            try
            {
                catalogTarget.getConnectorToTarget().disconnect();
            }
            catch (Exception error)
            {
                auditLog.logMessage(methodName,
                                    OIFAuditCode.DISCONNECT_EXCEPTION.getMessageDefinition(connectorName,
                                                                                           error.getClass().getName(),
                                                                                           catalogTarget.getCatalogTargetName(),
                                                                                           error.getMessage()));
            }
        }
    }

    /**
     * Shutdown metadata synchronization
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    public void disconnect() throws ConnectorCheckedException
    {
        for (RequestedCatalogTarget catalogTarget : currentCatalogTargetMap.values())
        {
            if (catalogTarget.getConnectorToTarget() != null)
            {
                catalogTarget.getConnectorToTarget().disconnect();
            }
        }
    }


    /**
     * Protected map that allows updates and queries from multiple threads.
     */
    static class CatalogTargetMap
    {
        private final Map<String, RequestedCatalogTarget> currentCatalogTargetMap = new HashMap<>();


        /**
         * Put a catalog target into the map
         *
         * @param requestedCatalogTarget details of catalog target
         */
        synchronized void put(RequestedCatalogTarget requestedCatalogTarget)
        {
            currentCatalogTargetMap.put(requestedCatalogTarget.getRelationshipGUID(), requestedCatalogTarget);
        }

        /**
         * Retrieve a catalog target from the map.
         *
         * @param relationshipGUID unique identifier of the catalog target relationship
         * @return details of the catalog target
         */
        synchronized RequestedCatalogTarget get(String relationshipGUID)
        {
            return currentCatalogTargetMap.get(relationshipGUID);
        }


        /**
         * Return all the known catalog targets.
         *
         * @return list of catalog targets
         */
        synchronized List<RequestedCatalogTarget> values()
        {
            return new ArrayList<>(currentCatalogTargetMap.values());
        }


        /**
         * Remove an obsolete catalog target from the map.
         *
         * @param relationshipGUID unique identifier of the catalog target relationship
         */
        synchronized void remove(String relationshipGUID)
        {
            currentCatalogTargetMap.remove(relationshipGUID);
        }
    }
}

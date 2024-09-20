/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.CatalogTarget;
import org.odpi.openmetadata.frameworks.governanceaction.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.ffdc.OIFAuditCode;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the map of catalog targets that this integration connector is working with.
 */
public class RequestedCatalogTargetsManager implements CatalogTargetChangeListener
{
    /**
     * A map of relationshipGUID->RequestedCatalogTarget properties to indicate the catalog targets that are known to
     * this connector.
     */
    private final CatalogTargetMap currentCatalogTargetMap = new CatalogTargetMap();

    private final List<CatalogTargetChangeListener>   registeredChangeListeners = new ArrayList<>();

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
            this.connectorConfigProperties = connectorConfigProperties;
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
        Map<String,Object> combinedConfigurationProperties = this.connectorConfigProperties;

        if (targetConfigProperties != null)
        {
            combinedConfigurationProperties.putAll(targetConfigProperties);
        }

        return combinedConfigurationProperties;
    }


    /**
     * Return a list of requested catalog targets for the connector.  These are extracted from the metadata store.
     *
     * @param integrationContext the integration component that will process each catalog target
     * @param catalogTargetIntegrator subclass of connector implementation
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    List<RequestedCatalogTarget> refreshKnownCatalogTargets(IntegrationContext      integrationContext,
                                                            CatalogTargetIntegrator catalogTargetIntegrator) throws ConnectorCheckedException
    {
        final String methodName = "refreshKnownCatalogTargets";

        try
        {
            int startFrom = 0;

            List<CatalogTarget> catalogTargetList  = integrationContext.getCatalogTargets(startFrom, integrationContext.getMaxPageSize());

            while (catalogTargetList != null)
            {
                for (CatalogTarget catalogTarget : catalogTargetList)
                {
                    if (catalogTarget != null)
                    {
                        RequestedCatalogTarget knownCatalogTarget = currentCatalogTargetMap.get(catalogTarget.getRelationshipGUID());

                        if (knownCatalogTarget == null)
                        {
                            /*
                             * This is a new catalog target.
                             */
                            knownCatalogTarget = setUpNewRequestedCatalogTarget(catalogTargetIntegrator,
                                                                                catalogTarget,
                                                                                integrationContext);

                            this.newCatalogTarget(knownCatalogTarget);
                        }
                        else if (! knownCatalogTarget.getRelationshipVersions().equals(catalogTarget.getRelationshipVersions()))
                        {
                            RequestedCatalogTarget oldCatalogTarget = knownCatalogTarget;

                            knownCatalogTarget = setUpNewRequestedCatalogTarget(catalogTargetIntegrator,
                                                                                catalogTarget,
                                                                                integrationContext);

                            this.updatedCatalogTarget(oldCatalogTarget, knownCatalogTarget);
                        }
                    }
                }

                startFrom         = startFrom + integrationContext.getMaxPageSize();
                catalogTargetList = integrationContext.getCatalogTargets(startFrom, integrationContext.getMaxPageSize());
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
     * Set up a new processor for the catalog target.
     *
     * @param catalogTargetIntegrator connector
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
    private RequestedCatalogTarget setUpNewRequestedCatalogTarget(CatalogTargetIntegrator catalogTargetIntegrator,
                                                                  CatalogTarget           retrievedCatalogTarget,
                                                                  IntegrationContext      integrationContext) throws ConnectorCheckedException,
                                                                                                                     InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     ConnectionCheckedException,
                                                                                                                     UserNotAuthorizedException
    {
        Connector connectorToTarget = null;

        if (propertyHelper.isTypeOf(retrievedCatalogTarget.getCatalogTargetElement(), OpenMetadataType.ASSET.typeName))
        {
            connectorToTarget = integrationContext.getConnectedAssetContext().getConnectorToAsset(retrievedCatalogTarget.getCatalogTargetElement().getGUID());
        }

        RequestedCatalogTarget newRequestedCatalogTarget = catalogTargetIntegrator.getNewRequestedCatalogTargetSkeleton(retrievedCatalogTarget, connectorToTarget);
        newRequestedCatalogTarget.setConfigurationProperties(this.getCombinedConfigurationProperties(retrievedCatalogTarget.getConfigurationProperties()));

        return newRequestedCatalogTarget;
    }


    /**
     * Return list of the current catalog targets
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
    void registerCatalogTargetChangeListener(CatalogTargetChangeListener listener)
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
    public void newCatalogTarget(RequestedCatalogTarget catalogTarget)
    {
        final String methodName = "newCatalogTarget";

        currentCatalogTargetMap.put(catalogTarget);

        for (CatalogTargetChangeListener listener : registeredChangeListeners)
        {
            listener.newCatalogTarget(catalogTarget);
        }

        startConnector(catalogTarget, methodName);
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
     */
    @Override
    public void removedCatalogTarget(RequestedCatalogTarget catalogTarget)
    {
        final String methodName = "removedCatalogTarget";

        this.disconnectConnector(catalogTarget, methodName);

        currentCatalogTargetMap.remove(catalogTarget.getRelationshipGUID());

        for (CatalogTargetChangeListener listener : registeredChangeListeners)
        {
            listener.removedCatalogTarget(catalogTarget);
        }
    }


    /**
     * Start the connector to the catalog target
     *
     * @param catalogTarget details of the target
     * @param methodName calling method
     */
    private void startConnector(RequestedCatalogTarget catalogTarget,
                                String                 methodName)
    {
        if (catalogTarget.getCatalogTargetConnector() != null)
        {
            try
            {
                catalogTarget.getCatalogTargetConnector().start();
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
     * Disconnect the connector to a catalog target.  If it fails, an audit log message is produced, but the
     * integration connector continues,
     *
     * @param catalogTarget details of the target
     * @param methodName calling method
     */
    private void disconnectConnector(RequestedCatalogTarget catalogTarget,
                                     String                 methodName)
    {
        if ((catalogTarget != null) && (catalogTarget.getCatalogTargetConnector() != null))
        {
            try
            {
                catalogTarget.getCatalogTargetConnector().disconnect();
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
     * Shutdown event monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    public void disconnect() throws ConnectorCheckedException
    {
        for (RequestedCatalogTarget catalogTarget : currentCatalogTargetMap.values())
        {
            if (catalogTarget.getCatalogTargetConnector() != null)
            {
                catalogTarget.getCatalogTargetConnector().disconnect();
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

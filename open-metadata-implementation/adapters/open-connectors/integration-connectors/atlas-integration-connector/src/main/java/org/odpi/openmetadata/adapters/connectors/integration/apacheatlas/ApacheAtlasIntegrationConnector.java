/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas;

import org.odpi.openmetadata.accessservices.assetmanager.api.AssetManagerEventListener;
import org.odpi.openmetadata.accessservices.assetmanager.events.AssetManagerOutTopicEvent;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.EndpointProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementHeader;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * ApacheAtlasIntegrationConnector exchanges glossary terms between Apache Atlas and the open metadata ecosystem.
 */
public class ApacheAtlasIntegrationConnector extends CatalogIntegratorConnector implements AssetManagerEventListener
{
    private String                   targetRootURL = null;
    private CatalogIntegratorContext myContext = null;

    private final Map<String, List<AtlasIntegrationModuleBase>> moduleMap  = new HashMap<>();
    private final List<AtlasIntegrationModuleBase>              moduleList = new ArrayList<>();


    /* ==============================================================================
     * Standard methods that trigger activity.
     */

    /**
     * Indicates that the connector is completely configured and can begin processing.
     * This call can be used to register with non-blocking services.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public void start() throws ConnectorCheckedException
    {
        super.start();

        final String methodName = "start";

        myContext = super.getContext();

        if ((connectionProperties.getUserId() == null) || (connectionProperties.getClearPassword() == null))
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_USER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }

        /*
         * Retrieve the configuration
         */
        EndpointProperties  endpoint = connectionProperties.getEndpoint();

        if (endpoint != null)
        {
            targetRootURL = endpoint.getAddress();
        }

        if (targetRootURL == null)
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_URL.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        if (myContext.getAssetManagerName() == null)
        {
            throw new ConnectorCheckedException(ApacheAtlasErrorCode.NULL_ASSET_MANAGER.getMessageDefinition(connectorName),
                                                this.getClass().getName(),
                                                methodName);
        }


        try
        {
            /*
             * Create the client that calls Apache Atlas.
             */
            ApacheAtlasRESTClient atlasClient = new ApacheAtlasRESTClient(connectorName,
                                                                          "Apache Atlas",
                                                                          targetRootURL,
                                                                          connectionProperties.getUserId(),
                                                                          connectionProperties.getClearPassword(),
                                                                          auditLog);

            /*
             * Set up the processing modules.  This is currently static, but the intention is that the modules can be plug-in extensions too
             * to support privately defined types.
             */
            this.registerSupportedModule(new ApacheAtlasGlossaryIntegrationModule(connectorName,
                                                                                  connectionProperties,
                                                                                  auditLog,
                                                                                  this.getContext(),
                                                                                  targetRootURL,
                                                                                  atlasClient,
                                                                                  embeddedConnectors));
        }
        catch (Exception error)
        {
            if (auditLog != null)
            {
                auditLog.logException(methodName,
                                      ApacheAtlasAuditCode.BAD_CONFIGURATION.getMessageDefinition(connectorName,
                                                                                                  error.getClass().getName(),
                                                                                                  targetRootURL,
                                                                                                  methodName,
                                                                                                  error.getMessage()),
                                      error);
            }

            throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(),
                                                this.getClass().getName(),
                                                methodName,
                                                error);
        }
    }


    /**
     * Populate the moduleList and moduleMap with details of a supported module.
     *
     * @param supportedModule module
     */
    private void registerSupportedModule(AtlasIntegrationModuleBase  supportedModule)
    {
        moduleList.add(supportedModule);

        if (supportedModule.getSupportedTypes() != null)
        {
            for (String supportedType : supportedModule.getSupportedTypes())
            {
                if (supportedType != null)
                {
                    List<AtlasIntegrationModuleBase> modulesForTypeName = moduleMap.get(supportedType);

                    if (modulesForTypeName == null)
                    {
                        modulesForTypeName = new ArrayList<>();
                    }

                    modulesForTypeName.add(supportedModule);

                    moduleMap.put(supportedType, modulesForTypeName);
                }
            }
        }
    }


    /**
     * Requests that the connector does a comparison of the metadata in the third party technology and open metadata repositories.
     * Refresh is called when the integration connector first starts and then at intervals defined in the connector's configuration
     * as well as any external REST API calls to explicitly refresh the connector.
     * <br><br>
     * This method performs two sweeps. It first retrieves the glossary elements from Egeria and synchronizes them with Apache Atlas.
     * The second sweep works through the glossaries in Apache Atlas and ensures that none have been missed out by the first sweep.
     *
     * @throws ConnectorCheckedException there is a problem with the connector.  It is not able to refresh the metadata.
     */
    @Override
    public void refresh() throws ConnectorCheckedException
    {
        final String methodName = "refresh";

        for (AtlasIntegrationModuleBase registeredModule : moduleList)
        {
            registeredModule.refresh();
        }

        /*
         * Once the connector has completed a single refresh, it registered a listener with open metadata
         * to handle updates.  The delay in registering the listener is for efficiency-sake in that it
         * reduces the number of events coming in from updates to the open metadata ecosystem when the connector
         * is performing its first synchronization from Apache Atlas to Egeria.
         *
         * A listener is registered only if metadata is flowing from the open metadata ecosystem to Apache Atlas.
         */
        if ((! myContext.isListenerRegistered()) &&
                    (myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
                    (myContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * This request registers this connector to receive events from the open metadata ecosystem.  When an event occurs,
                 * the processEvent() method is called.
                 */
                myContext.registerListener(this);
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          ApacheAtlasAuditCode.UNABLE_TO_REGISTER_LISTENER.getMessageDefinition(connectorName,
                                                                                                                error.getClass().getName(),
                                                                                                                error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Process an event that was published by the Asset Manager OMAS.  This connector is only interested in
     * glossaries, glossary categories and glossary terms.   The listener is only registered if metadata is flowing
     * from the open metadata ecosystem to Apache Atlas.
     *
     * @param event event object
     */
    @Override
    public void processEvent(AssetManagerOutTopicEvent event)
    {
        /*
         * Only process events if refresh() is not running because the refresh() process creates lots of events and proceeding with event processing
         * at this time causes elements to be processed multiple times.
         */
        if (! myContext.isRefreshInProgress())
        {
            /*
             * Call the appropriate registered module that matches the type.  Notice that multiple modules can be registered for the same type.
             */
            ElementHeader elementHeader = event.getElementHeader();

            for (String supportedType : moduleMap.keySet())
            {
                if (myContext.isTypeOf(elementHeader, supportedType))
                {
                    List<AtlasIntegrationModuleBase> modulesForTypeName = moduleMap.get(supportedType);

                    if (modulesForTypeName != null)
                    {
                        for (AtlasIntegrationModuleBase registeredModule : modulesForTypeName)
                        {
                            registeredModule.processEvent(event);
                        }
                    }
                }
            }
        }
    }


    /**
     * Shutdown glossary monitoring
     *
     * @throws ConnectorCheckedException something failed in the super class
     */
    @Override
    public void disconnect() throws ConnectorCheckedException
    {
        final String methodName = "disconnect";

        if (auditLog != null)
        {
            auditLog.logMessage(methodName, ApacheAtlasAuditCode.CONNECTOR_STOPPING.getMessageDefinition(connectorName, targetRootURL));
        }

        super.disconnect();
    }
}

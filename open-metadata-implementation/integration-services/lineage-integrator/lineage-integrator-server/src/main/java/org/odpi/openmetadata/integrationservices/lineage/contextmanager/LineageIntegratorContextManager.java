/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.lineage.contextmanager;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.assetmanager.client.*;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorConnector;
import org.odpi.openmetadata.integrationservices.lineage.connector.LineageIntegratorContext;
import org.odpi.openmetadata.integrationservices.lineage.connector.OpenLineageEventListener;
import org.odpi.openmetadata.integrationservices.lineage.connector.OpenLineageListenerManager;
import org.odpi.openmetadata.integrationservices.lineage.ffdc.*;
import org.odpi.openmetadata.integrationservices.lineage.properties.OpenLineageRunEvent;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * LineageIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service.
 */
public class LineageIntegratorContextManager extends IntegrationContextManager implements OpenLineageListenerManager
{
    private ExternalAssetManagerClient assetManagerClient;
    private DataAssetExchangeClient    dataAssetExchangeClient;
    private LineageExchangeClient      lineageExchangeClient;
    private GovernanceExchangeClient   governanceExchangeClient;
    private StewardshipExchangeClient  stewardshipExchangeClient;

    private final ObjectMapper                   objectMapper             = new ObjectMapper();
    private final List<OpenLineageEventListener> registeredEventListeners = new ArrayList<>();


    /**
     * Default constructor
     */
    public LineageIntegratorContextManager() { super(); }


    /**
     * Initialize server properties for the context manager.
     *
     * @param partnerOMASServerName name of the server to connect to
     * @param partnerOMASPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param serviceOptions options from the integration service's configuration
     * @param maxPageSize maximum number of results that can be returned on a single REST call
     * @param auditLog logging destination
     */
    public void initializeContextManager(String              partnerOMASServerName,
                                         String              partnerOMASPlatformRootURL,
                                         String              userId,
                                         String              password,
                                         Map<String, Object> serviceOptions,
                                         int                 maxPageSize,
                                         AuditLog            auditLog)
    {
        super.initializeContextManager(partnerOMASServerName, partnerOMASPlatformRootURL, userId, password, serviceOptions, maxPageSize, auditLog);

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                            LineageIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public void createClients() throws InvalidParameterException
    {
        AssetManagerRESTClient restClient;

        if (localServerPassword == null)
        {
            restClient = new AssetManagerRESTClient(partnerOMASServerName,
                                                    partnerOMASPlatformRootURL,
                                                    auditLog);
        }
        else
        {
            restClient = new AssetManagerRESTClient(partnerOMASServerName,
                                                    partnerOMASPlatformRootURL,
                                                    localServerUserId,
                                                    localServerPassword,
                                                    auditLog);
        }

        assetManagerClient = new ExternalAssetManagerClient(partnerOMASServerName,
                                                            partnerOMASPlatformRootURL,
                                                            restClient,
                                                            maxPageSize,
                                                            auditLog);

        dataAssetExchangeClient = new DataAssetExchangeClient(partnerOMASServerName,
                                                              partnerOMASPlatformRootURL,
                                                              restClient,
                                                              maxPageSize,
                                                              auditLog);

        lineageExchangeClient = new LineageExchangeClient(partnerOMASServerName,
                                                          partnerOMASPlatformRootURL,
                                                          restClient,
                                                          maxPageSize,
                                                          auditLog);

        governanceExchangeClient = new GovernanceExchangeClient(partnerOMASServerName,
                                                                partnerOMASPlatformRootURL,
                                                                restClient,
                                                                maxPageSize,
                                                                auditLog);

        stewardshipExchangeClient = new StewardshipExchangeClient(partnerOMASServerName,
                                                                  partnerOMASPlatformRootURL,
                                                                  restClient,
                                                                  maxPageSize,
                                                                  auditLog);
    }


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this integrator.
     *
     * @param metadataSourceQualifiedName unique name of the software server capability that represents this integration service
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException there is a problem in the remote server running the partner OMAS
     */
    private String setUpMetadataSource(String   metadataSourceQualifiedName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException
    {
        if (metadataSourceQualifiedName != null)
        {
            String metadataSourceGUID = assetManagerClient.getExternalAssetManagerGUID(localServerUserId, metadataSourceQualifiedName);

            if (metadataSourceGUID == null)
            {
                AssetManagerProperties properties = new AssetManagerProperties();

                properties.setQualifiedName(metadataSourceQualifiedName);

                metadataSourceGUID = assetManagerClient.createExternalAssetManager(localServerUserId, properties);
            }

            return metadataSourceGUID;
        }

        return null;
    }


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param metadataSourceQualifiedName unique name of the software server capability that represents the metadata source.
     * @param integrationConnector connector created from connection integration service configuration
     * @param permittedSynchronization controls the direction(s) that metadata is allowed to flow
     *
     * @throws InvalidParameterException the connector is not of the correct type
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    @Override
    public void setContext(String                   connectorId,
                           String                   connectorName,
                           String                   metadataSourceQualifiedName,
                           IntegrationConnector     integrationConnector,
                           PermittedSynchronization permittedSynchronization) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String  methodName = "setContext";

        String permittedSynchronizationName = PermittedSynchronization.BOTH_DIRECTIONS.getName();
        String serviceOptionsString = "null";

        if (permittedSynchronization != null)
        {
            permittedSynchronizationName = permittedSynchronization.getName();
        }

        if (serviceOptions != null)
        {
            serviceOptionsString = serviceOptions.toString();
        }

        if (integrationConnector instanceof LineageIntegratorConnector)
        {
            auditLog.logMessage(methodName,
                                LineageIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                                                                                                               connectorId,
                                                                                                               metadataSourceQualifiedName,
                                                                                                               permittedSynchronizationName,
                                                                                                               serviceOptionsString));

            AssetManagerEventClient eventClient = new AssetManagerEventClient(partnerOMASServerName,
                                                                              partnerOMASPlatformRootURL,
                                                                              localServerUserId,
                                                                              localServerPassword,
                                                                              maxPageSize,
                                                                              auditLog,
                                                                              connectorId);

            LineageIntegratorConnector serviceSpecificConnector = (LineageIntegratorConnector)integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);

            serviceSpecificConnector.setContext(new LineageIntegratorContext(this,
                                                                             dataAssetExchangeClient,
                                                                             lineageExchangeClient,
                                                                             governanceExchangeClient,
                                                                             stewardshipExchangeClient,
                                                                             eventClient,
                                                                             localServerUserId,
                                                                             metadataSourceGUID,
                                                                             metadataSourceQualifiedName,
                                                                             connectorName,
                                                                             IntegrationServiceDescription.LINEAGE_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                             auditLog));
        }
        else
        {
            final String parameterName = "integrationConnector";

            throw new InvalidParameterException(
                    LineageIntegratorErrorCode.INVALID_CONNECTOR.getMessageDefinition(connectorName,
                                                                                      IntegrationServiceDescription.LINEAGE_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                                      LineageIntegratorConnector.class.getCanonicalName()),
                    this.getClass().getName(),
                    methodName,
                    parameterName);
        }
    }


    /* ======================================================================================
     * Support for open lineage events.
     */

    /**
     * The listener is implemented by the integration connector.  Once it is registered with the context, its processOpenLineageRunEvent()
     * method is called each time an open lineage event is published to the Lineage Integrator OMIS.
     *
     * @param listener listener to call
     */
    public synchronized void registerListener(OpenLineageEventListener listener)
    {
        registeredEventListeners.add(listener);
    }


    /**
     * Pass the incoming openLineage event to all connectors that are listening.
     *
     * @param rawEvent JSON payload containing the open lineage event
     */
    public synchronized void publishOpenLineageRunEvent(String rawEvent)
    {
        final String methodName = "publishOpenLineageRunEvent(rawEvent)";

        OpenLineageRunEvent event = null;

        if (rawEvent != null)
        {
            try
            {
                event = objectMapper.readValue(rawEvent, OpenLineageRunEvent.class);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LineageIntegratorAuditCode.OPEN_LINEAGE_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                error.getMessage(),
                                                                                                                rawEvent),
                                      rawEvent,
                                      error);
            }
        }

        publishToListeners(event, rawEvent, methodName);
    }


    /**
     * Pass the incoming openLineage event to all connectors that are listening.
     *
     * @param event JSON payload containing the open lineage event
     */
    public synchronized void publishOpenLineageRunEvent(OpenLineageRunEvent event)
    {
        final String methodName = "publishOpenLineageRunEvent(event)";

        String rawEvent = null;

        if (event != null)
        {
            try
            {
                rawEvent = objectMapper.writeValueAsString(event);
            }
            catch (Exception error)
            {
                auditLog.logException(methodName,
                                      LineageIntegratorAuditCode.OPEN_LINEAGE_FORMAT_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                error.getMessage(),
                                                                                                                event.toString()),
                                      event.toString(),
                                      error);
            }
        }

        publishToListeners(event, rawEvent, methodName);
    }


    /**
     * Loop through the listeners and sending an event to each.  If a connector throws an exception, it is logged and the publishing process
     * continues with the other listeners.
     *
     * @param event event bean
     * @param rawEvent string event
     * @param methodName calling method
     */
    private void publishToListeners(OpenLineageRunEvent event,
                                    String              rawEvent,
                                    String              methodName)
    {
        for (OpenLineageEventListener listener : registeredEventListeners)
        {
            if (listener != null)
            {
                try
                {
                    listener.processOpenLineageRunEvent(event, rawEvent);
                }
                catch (Exception error)
                {
                    auditLog.logException(methodName,
                                          LineageIntegratorAuditCode.OPEN_LINEAGE_PUBLISH_ERROR.getMessageDefinition(error.getClass().getName(),
                                                                                                                     error.getMessage()),
                                          rawEvent,
                                          error);
                }
            }
        }
    }
}

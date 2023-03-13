/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.search.contextmanager;

import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetmanager.client.ExternalAssetManagerClient;
import org.odpi.openmetadata.integrationservices.search.client.AssetCatalogOutTopicEventListener;
import org.odpi.openmetadata.accessservices.assetcatalog.eventclient.AssetCatalogEventClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.search.connector.SearchIntegratorConnector;
import org.odpi.openmetadata.integrationservices.search.connector.SearchIntegratorContext;
import org.odpi.openmetadata.integrationservices.search.ffdc.SearchIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.search.ffdc.SearchIntegratorErrorCode;

import java.util.Map;


/**
 * SearchIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class SearchIntegratorContextManager extends IntegrationContextManager
{
    private ExternalAssetManagerClient        assetManagerClient = null;
    private AssetCatalogEventClient           assetCatalogEventClient;
    private AssetCatalogOutTopicEventListener eventListener;

    /**
     * Default constructor
     */
    public SearchIntegratorContextManager() {
        super();
    }

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
                                         AuditLog            auditLog) {

        super.initializeContextManager(partnerOMASServerName, partnerOMASPlatformRootURL, userId, password, serviceOptions, maxPageSize, auditLog);

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                SearchIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public void createClients() throws InvalidParameterException {
        final String methodName = "createClients";
        AssetCatalog restClient;
        if (localServerPassword == null) {
            restClient = new AssetCatalog(partnerOMASServerName, partnerOMASPlatformRootURL);
        } else {
            restClient = new AssetCatalog(partnerOMASServerName,
                    partnerOMASPlatformRootURL,
                    localServerUserId,
                    localServerPassword);
        }

        assetCatalogEventClient = new AssetCatalogEventClient(partnerOMASServerName,
                partnerOMASPlatformRootURL,
                restClient,
                maxPageSize,
                auditLog,
                localServerUserId);

        eventListener = new AssetCatalogOutTopicEventListener();

        try {
            assetCatalogEventClient.registerListener(localServerUserId, eventListener);
        } catch (ConnectionCheckedException | ConnectorCheckedException | PropertyServerException | UserNotAuthorizedException e) {
            auditLog.logException(methodName,SearchIntegratorAuditCode.REGISTER_CATALOG_LISTENER_ERROR.getMessageDefinition(IntegrationServiceDescription.SEARCH_INTEGRATOR_OMIS.getIntegrationServiceFullName()),e);
        }

        AssetManagerRESTClient assetManagerRestClient;
        if (localServerPassword == null) {
            assetManagerRestClient = new AssetManagerRESTClient(partnerOMASServerName,
                    partnerOMASPlatformRootURL,
                    auditLog);
        } else {
            assetManagerRestClient = new AssetManagerRESTClient(partnerOMASServerName,
                    partnerOMASPlatformRootURL,
                    localServerUserId,
                    localServerPassword,
                    auditLog);
        }

        assetManagerClient = new ExternalAssetManagerClient(partnerOMASServerName,
                                                            partnerOMASPlatformRootURL,
                                                            assetManagerRestClient,
                                                            maxPageSize,
                                                            auditLog);
    }


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this integrator.
     *
     * @param metadataSourceQualifiedName unique name of the software server capability that represents this integration service
     * @return unique identifier of the metadata source
     * @throws InvalidParameterException  one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException    there is a problem in the remote server running the partner OMAS
     */
    private String setUpMetadataSource(String metadataSourceQualifiedName) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        final String metadataSourceQualifiedNameParameterName = "metadataSourceQualifiedName";
        final String methodName = "setUpMetadataSource";

        InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

        invalidParameterHandler.validateName(metadataSourceQualifiedName,
                metadataSourceQualifiedNameParameterName,
                methodName);


        String metadataSourceGUID = assetManagerClient.getExternalAssetManagerGUID(localServerUserId, metadataSourceQualifiedName);

        if (metadataSourceGUID == null) {
            AssetManagerProperties properties = new AssetManagerProperties();

            properties.setQualifiedName(metadataSourceQualifiedName);

            metadataSourceGUID = assetManagerClient.createExternalAssetManager(localServerUserId, properties);
        }

        return metadataSourceGUID;
    }


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorId                 unique identifier of the connector (used to configure the event listener)
     * @param connectorName               name of connector from config
     * @param metadataSourceQualifiedName unique name of the software server capability that represents the metadata source.
     * @param integrationConnector connector created from connection integration service configuration
     * @param permittedSynchronization controls the direction(s) that metadata is allowed to flow
     *
     * @throws InvalidParameterException the connector is not of the correct type
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    @Override
    public void setContext(String connectorId,
                           String connectorName,
                           String metadataSourceQualifiedName,
                           IntegrationConnector integrationConnector,
                           PermittedSynchronization permittedSynchronization) throws InvalidParameterException,
            UserNotAuthorizedException,
            PropertyServerException {
        final String methodName = "setContext";

        String permittedSynchronizationName = PermittedSynchronization.BOTH_DIRECTIONS.getName();
        String serviceOptionsString = "null";

        if (permittedSynchronization != null) {
            permittedSynchronizationName = permittedSynchronization.getName();
        }

        if (serviceOptions != null) {
            serviceOptionsString = serviceOptions.toString();
        }

        if (integrationConnector instanceof SearchIntegratorConnector) {
            auditLog.logMessage(methodName,
                    SearchIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                            connectorId,
                            metadataSourceQualifiedName,
                            permittedSynchronizationName,
                            serviceOptionsString));

            SearchIntegratorConnector serviceSpecificConnector = (SearchIntegratorConnector) integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);

            eventListener.setSearchIntegratorConnector(serviceSpecificConnector);
            serviceSpecificConnector.setContext(new SearchIntegratorContext(localServerUserId,
                    metadataSourceGUID,
                    metadataSourceQualifiedName,
                    connectorName,
                    assetCatalogEventClient,
                    IntegrationServiceDescription.SEARCH_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                    auditLog));


        } else {
            final String parameterName = "integrationConnector";

            throw new InvalidParameterException(
                    SearchIntegratorErrorCode.INVALID_CONNECTOR.getMessageDefinition(connectorName,
                            IntegrationServiceDescription.SEARCH_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                            SearchIntegratorConnector.class.getCanonicalName()),
                    this.getClass().getName(),
                    methodName,
                    parameterName);
        }
    }
}

/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.security.contextmanager;

import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.AssetManagerEventClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.DataAssetExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.security.connector.SecurityIntegratorConnector;
import org.odpi.openmetadata.integrationservices.security.connector.SecurityIntegratorContext;
import org.odpi.openmetadata.integrationservices.security.ffdc.SecurityIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.security.ffdc.SecurityIntegratorErrorCode;

import java.util.Map;


/**
 * SecurityIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class SecurityIntegratorContextManager extends IntegrationContextManager
{
    private AssetManagerClient      assetManagerClient;
    private DataAssetExchangeClient dataAssetExchangeClient;

    /**
     * Default constructor
     */
    public SecurityIntegratorContextManager()
    {
    }


    /**
     * Initialize server properties for the context manager.
     *
     * @param partnerOMASServerName name of the server to connect to
     * @param partnerOMASPlatformRootURL the network address of the server running the OMAS REST servers
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     * @param maxPageSize maximum number of results that can be returned on a single REST call
     * @param auditLog logging destination
     */
    @Override
    public void initializeContextManager(String   partnerOMASServerName,
                                         String   partnerOMASPlatformRootURL,
                                         String   userId,
                                         String   password,
                                         int      maxPageSize,
                                         AuditLog auditLog)
    {
        super.initializeContextManager(partnerOMASServerName, partnerOMASPlatformRootURL, userId, password, maxPageSize, auditLog);

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                            SecurityIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public  void createClients() throws InvalidParameterException
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

        assetManagerClient = new AssetManagerClient(partnerOMASServerName,
                                                    partnerOMASPlatformRootURL,
                                                    restClient,
                                                    maxPageSize,
                                                    auditLog);

        dataAssetExchangeClient = new DataAssetExchangeClient(partnerOMASServerName,
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
        final String metadataSourceQualifiedNameParameterName = "metadataSourceQualifiedName";
        final String methodName = "setUpMetadataSource";

        InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

        invalidParameterHandler.validateName(metadataSourceQualifiedName,
                                             metadataSourceQualifiedNameParameterName,
                                             methodName);


        String metadataSourceGUID = assetManagerClient.getExternalAssetManagerGUID(localServerUserId, metadataSourceQualifiedName);

        if (metadataSourceGUID == null)
        {
            AssetManagerProperties properties = new AssetManagerProperties();

            properties.setQualifiedName(metadataSourceQualifiedName);

            metadataSourceGUID = assetManagerClient.createExternalAssetManager(localServerUserId, properties);
        }

        return metadataSourceGUID;
    }


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param metadataSourceQualifiedName unique name of the software server capability that represents the metadata source.
     * @param integrationConnector connector created from connection integration service configuration
     * @param permittedSynchronization controls the direction(s) that metadata is allowed to flow
     * @param serviceOptions options from the integration service's configuration
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
                           PermittedSynchronization permittedSynchronization,
                           Map<String, Object>      serviceOptions) throws InvalidParameterException,
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

        if (integrationConnector instanceof SecurityIntegratorConnector)
        {
            auditLog.logMessage(methodName,
                                SecurityIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
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

            SecurityIntegratorConnector serviceSpecificConnector = (SecurityIntegratorConnector)integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);

            serviceSpecificConnector.setContext(new SecurityIntegratorContext(dataAssetExchangeClient,
                                                                              localServerUserId,
                                                                              metadataSourceGUID,
                                                                              metadataSourceQualifiedName,
                                                                              connectorName,
                                                                              auditLog));
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(
                    SecurityIntegratorErrorCode.INVALID_CONNECTOR.getMessageDefinition(connectorName,
                                                                                      IntegrationServiceDescription.SECURITY_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                                      SecurityIntegratorConnector.class.getCanonicalName()),
                    this.getClass().getName(),
                    methodName,
                    parameterName);
        }
    }

}

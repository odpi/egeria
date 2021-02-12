/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalog.contextmanager;

import org.odpi.openmetadata.accessservices.assetmanager.client.*;
import org.odpi.openmetadata.accessservices.assetmanager.client.rest.AssetManagerRESTClient;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.catalog.ffdc.CatalogIntegratorErrorCode;

import java.util.List;
import java.util.Map;


/**
 * CatalogIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class CatalogIntegratorContextManager extends IntegrationContextManager
{
    private static String disabledExchangeServicesOption     = "disabledExchangeServices";

    private AssetManagerClient           assetManagerClient;
    private CollaborationExchangeClient  collaborationExchangeClient;
    private DataAssetExchangeClient      dataAssetExchangeClient;
    private GlossaryExchangeClient       glossaryExchangeClient;
    private GovernanceExchangeClient     governanceExchangeClient;
    private InfrastructureExchangeClient infrastructureExchangeClient;
    private LineageExchangeClient        lineageExchangeClient;
    private StewardshipExchangeClient    stewardshipExchangeClient;
    private ValidValuesExchangeClient    validValuesExchangeClient;


    /**
     * Default constructor
     */
    public CatalogIntegratorContextManager()
    {
        super();
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
                            CatalogIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
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

        assetManagerClient = new AssetManagerClient(partnerOMASServerName,
                                                    partnerOMASPlatformRootURL,
                                                    restClient,
                                                    maxPageSize,
                                                    auditLog);

        collaborationExchangeClient = new CollaborationExchangeClient(partnerOMASServerName,
                                                                      partnerOMASPlatformRootURL,
                                                                      restClient,
                                                                      maxPageSize,
                                                                      auditLog);

        dataAssetExchangeClient = new DataAssetExchangeClient(partnerOMASServerName,
                                                              partnerOMASPlatformRootURL,
                                                              restClient,
                                                              maxPageSize,
                                                              auditLog);

        glossaryExchangeClient = new GlossaryExchangeClient(partnerOMASServerName,
                                                            partnerOMASPlatformRootURL,
                                                            restClient,
                                                            maxPageSize,
                                                            auditLog);

        governanceExchangeClient = new GovernanceExchangeClient(partnerOMASServerName,
                                                                partnerOMASPlatformRootURL,
                                                                restClient,
                                                                maxPageSize,
                                                                auditLog);

        infrastructureExchangeClient = new InfrastructureExchangeClient(partnerOMASServerName,
                                                                        partnerOMASPlatformRootURL,
                                                                        restClient,
                                                                        maxPageSize,
                                                                        auditLog);

        lineageExchangeClient = new LineageExchangeClient(partnerOMASServerName,
                                                          partnerOMASPlatformRootURL,
                                                          restClient,
                                                          maxPageSize,
                                                          auditLog);

        stewardshipExchangeClient = new StewardshipExchangeClient(partnerOMASServerName,
                                                                  partnerOMASPlatformRootURL,
                                                                  restClient,
                                                                  maxPageSize,
                                                                  auditLog);

        validValuesExchangeClient = new ValidValuesExchangeClient(partnerOMASServerName,
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

        if (integrationConnector instanceof CatalogIntegratorConnector)
        {
            auditLog.logMessage(methodName,
                                CatalogIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
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

            CatalogIntegratorConnector serviceSpecificConnector = (CatalogIntegratorConnector)integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);

            serviceSpecificConnector.setContext(new CatalogIntegratorContext(assetManagerClient,
                                                                             eventClient,
                                                                             collaborationExchangeClient,
                                                                             dataAssetExchangeClient,
                                                                             glossaryExchangeClient,
                                                                             governanceExchangeClient,
                                                                             infrastructureExchangeClient,
                                                                             lineageExchangeClient,
                                                                             stewardshipExchangeClient,
                                                                             validValuesExchangeClient,
                                                                             localServerUserId,
                                                                             metadataSourceGUID,
                                                                             metadataSourceQualifiedName,
                                                                             connectorName,
                                                                             IntegrationServiceDescription.CATALOG_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                             getSynchronizationDirection(permittedSynchronization),
                                                                             this.extractDisabledExchangeServices(serviceOptions, connectorName),
                                                                             auditLog));
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(
                    CatalogIntegratorErrorCode.INVALID_CONNECTOR.getMessageDefinition(connectorName,
                                                                                      IntegrationServiceDescription.CATALOG_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                                      CatalogIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }


    /**
     * Convert permitted synchronization from the configuration into the SynchronizationDirection enum.
     * The default is BOTH_DIRECTIONS which effectively enforces no restriction.
     *
     * @param permittedSynchronization value from the configuration
     * @return synchronization direction enum
     */
    private SynchronizationDirection getSynchronizationDirection(PermittedSynchronization permittedSynchronization)
    {
        if (permittedSynchronization != null)
        {
            switch (permittedSynchronization)
            {
                case TO_THIRD_PARTY:
                    return SynchronizationDirection.TO_THIRD_PARTY;

                case FROM_THIRD_PARTY:
                    return SynchronizationDirection.FROM_THIRD_PARTY;

                case BOTH_DIRECTIONS:
                    return SynchronizationDirection.BOTH_DIRECTIONS;

                case OTHER:
                    return SynchronizationDirection.OTHER;
            }
        }

        return SynchronizationDirection.BOTH_DIRECTIONS;
    }



    /**
     * Extract the supported zones property from the access services option.
     *
     * @param serviceOptions options passed to the access service.
     * @return null or list of disabled exchange service  names
     * @throws InvalidParameterException the supported zones property is not a list of zone names.
     */
    private List<String> extractDisabledExchangeServices(Map<String, Object> serviceOptions,
                                                         String              connectorName) throws InvalidParameterException
    {
        final String  methodName = "extractDisabledExchangeServices";

        if (serviceOptions == null)
        {
            return null;
        }
        else
        {
            Object serviceListObject = serviceOptions.get(disabledExchangeServicesOption);

            if (serviceListObject == null)
            {
                return null;
            }
            else
            {
                try
                {
                    @SuppressWarnings("unchecked")
                    List<String> serviceList = (List<String>) serviceListObject;

                    auditLog.logMessage(methodName,
                                        CatalogIntegratorAuditCode.DISABLED_EXCHANGE_SERVICES.getMessageDefinition(connectorName, serviceList.toString()));
                    return serviceList;
                }
                catch (Throwable error)
                {
                    final String  parameterName = "serviceOptions";

                    throw new InvalidParameterException(
                            CatalogIntegratorErrorCode.BAD_CONFIG_PROPERTIES.getMessageDefinition(IntegrationServiceDescription.CATALOG_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                                                  serviceListObject.toString(),
                                                                                                  disabledExchangeServicesOption,
                                                                                                  error.getClass().getName(),
                                                                                                  error.getMessage()),
                            this.getClass().getName(),
                            methodName,
                            parameterName);

                }
            }
        }
    }
}

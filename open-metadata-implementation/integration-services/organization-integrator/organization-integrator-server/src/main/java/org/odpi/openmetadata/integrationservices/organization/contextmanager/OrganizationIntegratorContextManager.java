/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.organization.contextmanager;

import org.odpi.openmetadata.accessservices.communityprofile.client.*;
import org.odpi.openmetadata.accessservices.communityprofile.client.rest.CommunityProfileRESTClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnector;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.adminservices.configuration.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.organization.connector.OrganizationIntegratorConnector;
import org.odpi.openmetadata.integrationservices.organization.connector.OrganizationIntegratorContext;
import org.odpi.openmetadata.integrationservices.organization.ffdc.OrganizationIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.organization.ffdc.OrganizationIntegratorErrorCode;

import java.util.Map;


/**
 * OrganizationIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class OrganizationIntegratorContextManager extends IntegrationContextManager
{
    private MetadataSourceClient    metadataSourceClient    = null;
    private OrganizationManagement  organizationManagement  = null;
    private SecurityGroupManagement securityGroupManagement = null;
    private UserIdentityManagement  userIdentityManagement  = null;

    /**
     * Default constructor
     */
    public OrganizationIntegratorContextManager()
    {
    }


    /**
     * Initialize server properties for the context manager.
     *
     * @param partnerOMASServerName name of the server to connect to
     * @param partnerOMASPlatformRootURL the network address of the server running the OMAS REST services
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
                            OrganizationIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public  void createClients() throws InvalidParameterException
    {
        super.openIntegrationClient = new OpenIntegrationServiceClient(partnerOMASServerName, partnerOMASPlatformRootURL);
        super.openMetadataStoreClient = new OpenMetadataStoreClient(partnerOMASServerName, partnerOMASPlatformRootURL);

        CommunityProfileRESTClient restClient;

        if (localServerPassword == null)
        {
            restClient = new CommunityProfileRESTClient(partnerOMASServerName,
                                                        partnerOMASPlatformRootURL,
                                                        auditLog);
        }
        else
        {
            restClient = new CommunityProfileRESTClient(partnerOMASServerName,
                                                        partnerOMASPlatformRootURL,
                                                        localServerUserId,
                                                        localServerPassword,
                                                        auditLog);
        }

        metadataSourceClient = new MetadataSourceClient(partnerOMASServerName,
                                                        partnerOMASPlatformRootURL,
                                                        restClient,
                                                        maxPageSize);

        organizationManagement = new OrganizationManagement(partnerOMASServerName,
                                                            partnerOMASPlatformRootURL,
                                                            restClient,
                                                            maxPageSize);

        securityGroupManagement = new SecurityGroupManagement(partnerOMASServerName,
                                                              partnerOMASPlatformRootURL,
                                                              restClient,
                                                              maxPageSize);

        userIdentityManagement = new UserIdentityManagement(partnerOMASServerName,
                                                            partnerOMASPlatformRootURL,
                                                            restClient,
                                                            maxPageSize);

    }


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorId unique identifier of the connector (used to configure the event listener)
     * @param connectorName name of connector from config
     * @param connectorUserId userId for the connector
     * @param integrationConnector connector created from connection integration service configuration
     * @param integrationConnectorGUID unique identifier of the integration connector entity (only set if working with integration groups)
     * @param permittedSynchronization controls the direction(s) that metadata is allowed to flow
     * @param generateIntegrationReport should the connector generate an integration reports?
     * @param metadataSourceQualifiedName unique name of the software server capability that represents the metadata source.
     *
     * @return the new integration context
     * @throws InvalidParameterException the connector is not of the correct type
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    @Override
    public IntegrationContext setContext(String                   connectorId,
                                         String                   connectorName,
                                         String                   connectorUserId,
                                         IntegrationConnector     integrationConnector,
                                         String                   integrationConnectorGUID,
                                         PermittedSynchronization permittedSynchronization,
                                         boolean                  generateIntegrationReport,
                                         String                   metadataSourceQualifiedName) throws InvalidParameterException,
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

        if (integrationConnector instanceof OrganizationIntegratorConnector serviceSpecificConnector)
        {
            auditLog.logMessage(methodName,
                                OrganizationIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                                                                                                                    connectorId,
                                                                                                                    metadataSourceQualifiedName,
                                                                                                                    permittedSynchronizationName,
                                                                                                                    serviceOptionsString));

            String externalSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName, null, null);
            String externalSourceName = metadataSourceQualifiedName;

            if (externalSourceGUID == null)
            {
                externalSourceName = null;
            }

            CommunityProfileEventClient eventClient = new CommunityProfileEventClient(partnerOMASServerName,
                                                                                      partnerOMASPlatformRootURL,
                                                                                      localServerUserId,
                                                                                      localServerPassword,
                                                                                      connectorId);

            OrganizationIntegratorContext integratorContext = new OrganizationIntegratorContext(connectorId,
                                                                                                connectorName,
                                                                                                connectorUserId,
                                                                                                partnerOMASServerName,
                                                                                                openIntegrationClient,
                                                                                                openMetadataStoreClient,
                                                                                                organizationManagement,
                                                                                                securityGroupManagement,
                                                                                                userIdentityManagement,
                                                                                                eventClient,
                                                                                                generateIntegrationReport,
                                                                                                permittedSynchronization,
                                                                                                integrationConnectorGUID,
                                                                                                externalSourceGUID,
                                                                                                externalSourceName,
                                                                                                maxPageSize,
                                                                                                auditLog);
            serviceSpecificConnector.setContext(integratorContext);
            integrationConnector.setConnectorName(connectorName);

            return integratorContext;
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(OrganizationIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.ORGANIZATION_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         OrganizationIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

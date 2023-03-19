/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.integration.contextmanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.integration.client.OpenIntegrationClient;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnector;
import org.odpi.openmetadata.frameworks.integration.context.*;

import java.util.Map;

/**
 * IntegrationContextManager is the base class for the context manager that is implemented by each integration service.
 */
public class IntegrationContextManager
{
    protected String                       partnerOMASPlatformRootURL   = null;
    protected String                       partnerOMASServerName        = null;
    protected OpenIntegrationClient        openIntegrationClient        = null;
    protected OpenMetadataClient           openMetadataStoreClient      = null;
    protected String                       localServerUserId            = null;
    protected String                       localServerPassword          = null;
    protected Map<String, Object>          serviceOptions               = null;
    protected int                          maxPageSize                  = 0;
    protected AuditLog                     auditLog                     = null;


    /**
     * Default constructor
     */
    protected IntegrationContextManager()
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
        this.partnerOMASPlatformRootURL = partnerOMASPlatformRootURL;
        this.partnerOMASServerName      = partnerOMASServerName;
        this.localServerUserId          = userId;
        this.localServerPassword        = password;
        this.serviceOptions             = serviceOptions;
        this.maxPageSize                = maxPageSize;
        this.auditLog                   = auditLog;
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    public void createClients() throws InvalidParameterException
    {
    }


    /**
     * Return a new  integrationGovernanceContext for a specific connector.
     *
     * @param openMetadataStore client implementation
     * @param userId calling user
     * @param externalSourceGUID unique identifier for external source (or null)
     * @param externalSourceName unique name for external source (or null)
     * @return new context
     */
    protected IntegrationGovernanceContext constructIntegrationGovernanceContext(OpenMetadataClient openMetadataStore,
                                                                                 String             userId,
                                                                                 String             externalSourceGUID,
                                                                                 String             externalSourceName)
    {
        if (openMetadataStoreClient != null)
        {
            OpenMetadataAccess      openMetadataAccess      = new OpenMetadataAccess(openMetadataStore, userId, externalSourceGUID, externalSourceName);
            MultiLanguageManagement multiLanguageManagement = new MultiLanguageManagement(openMetadataStore, userId);
            StewardshipAction       stewardshipAction       = new StewardshipAction(openMetadataStore, userId);
            ValidMetadataValues     validMetadataValues     = new ValidMetadataValues(openMetadataStore, userId);

            return new IntegrationGovernanceContext(openMetadataAccess, multiLanguageManagement, stewardshipAction, validMetadataValues);
        }

        return null;
    }


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this event broker.
     *
     * @param metadataSourceQualifiedName unique name of the software capability that represents this integration service
     * @param typeName subtype name of the software capability
     * @param classificationName optional classification for the software capability
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException there is a problem in the remote server running the partner OMAS
     */
    protected String setUpMetadataSource(String   metadataSourceQualifiedName,
                                         String   typeName,
                                         String   classificationName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        if (openIntegrationClient != null)
        {
            String metadataSourceGUID = openIntegrationClient.getMetadataSourceGUID(localServerUserId, metadataSourceQualifiedName);

            if (metadataSourceGUID == null)
            {
                metadataSourceGUID = openIntegrationClient.createMetadataSource(localServerUserId,
                                                                                typeName,
                                                                                classificationName,
                                                                                metadataSourceQualifiedName);
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
        IntegrationContext integrationContext = null;

        String externalSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName, null, null);
        String externalSourceName = metadataSourceQualifiedName;

        if (externalSourceGUID == null)
        {
            externalSourceName = null;
        }

        IntegrationGovernanceContext integrationGovernanceContext = constructIntegrationGovernanceContext(openMetadataStoreClient,
                                                                                                          connectorUserId,
                                                                                                          externalSourceGUID,
                                                                                                          externalSourceName);

        if ((openIntegrationClient != null) && (openMetadataStoreClient != null))
        {
            integrationContext = new IntegrationContext(connectorId,
                                                        connectorName,
                                                        connectorUserId,
                                                        partnerOMASServerName,
                                                        openIntegrationClient,
                                                        openMetadataStoreClient,
                                                        generateIntegrationReport,
                                                        permittedSynchronization,
                                                        externalSourceGUID,
                                                        externalSourceName,
                                                        integrationConnectorGUID,
                                                        integrationGovernanceContext);
        }

        integrationConnector.setContext(integrationContext);
        integrationConnector.setConnectorName(connectorName);

        return integrationContext;
    }
}

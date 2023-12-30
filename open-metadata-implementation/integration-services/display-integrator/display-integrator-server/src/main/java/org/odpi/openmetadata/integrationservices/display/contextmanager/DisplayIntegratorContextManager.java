/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.display.contextmanager;

import org.odpi.openmetadata.accessservices.datamanager.client.*;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.ApplicationProperties;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnector;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.adminservices.configuration.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.display.connector.DisplayIntegratorConnector;
import org.odpi.openmetadata.integrationservices.display.connector.DisplayIntegratorContext;
import org.odpi.openmetadata.integrationservices.display.ffdc.DisplayIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.display.ffdc.DisplayIntegratorErrorCode;

import java.util.Map;


/**
 * DisplayIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of the DataManagerIntegrator integration service
 */
public class DisplayIntegratorContextManager extends IntegrationContextManager
{
    private DisplayApplicationClient displayApplicationClient = null;
    private MetadataSourceClient     metadataSourceClient     = null;
    private DataManagerRESTClient    restClient               = null;

    /**
     * Default constructor
     */
    public DisplayIntegratorContextManager()
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
                            DisplayIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public void createClients() throws InvalidParameterException
    {
        super.openIntegrationClient = new OpenIntegrationServiceClient(partnerOMASServerName, partnerOMASPlatformRootURL);
        super.openMetadataStoreClient = new OpenMetadataStoreClient(partnerOMASServerName, partnerOMASPlatformRootURL);

        if (localServerPassword == null)
        {
            restClient = new DataManagerRESTClient(partnerOMASServerName,
                                                   partnerOMASPlatformRootURL,
                                                   auditLog);
        }
        else
        {
            restClient = new DataManagerRESTClient(partnerOMASServerName,
                                                   partnerOMASPlatformRootURL,
                                                   localServerUserId,
                                                   localServerPassword,
                                                   auditLog);
        }

        displayApplicationClient = new DisplayApplicationClient(partnerOMASServerName,
                                                                partnerOMASPlatformRootURL,
                                                                restClient,
                                                                maxPageSize);

        metadataSourceClient = new MetadataSourceClient(partnerOMASServerName,
                                                        partnerOMASPlatformRootURL,
                                                        restClient,
                                                        maxPageSize);
    }


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this application.
     *
     * @param metadataSourceQualifiedName unique name of the software server capability that represents this integration service
     *
     * @return unique identifier of the metadata source
     *
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException there is a problem in the remote server running the partner OMAS
     */
    protected String setUpMetadataSource(String   metadataSourceQualifiedName) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException
    {
        if (metadataSourceQualifiedName != null)
        {
            String metadataSourceGUID = metadataSourceClient.getMetadataSourceGUID(localServerUserId, metadataSourceQualifiedName);

            if (metadataSourceGUID == null)
            {
                ApplicationProperties properties = new ApplicationProperties();

                properties.setQualifiedName(metadataSourceQualifiedName);

                metadataSourceGUID = metadataSourceClient.createApplication(localServerUserId, null, null, properties);
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

        if (integrationConnector instanceof DisplayIntegratorConnector serviceSpecificConnector)
        {
            auditLog.logMessage(methodName,
                                DisplayIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                                                                                                               connectorId,
                                                                                                               metadataSourceQualifiedName,
                                                                                                               permittedSynchronizationName,
                                                                                                               serviceOptionsString));

            String externalSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);
            String externalSourceName = metadataSourceQualifiedName;

            if (externalSourceGUID == null)
            {
                externalSourceName = null;
            }

            DataManagerEventClient dataManagerEventClient = new DataManagerEventClient(partnerOMASServerName,
                                                                                       partnerOMASPlatformRootURL,
                                                                                       restClient,
                                                                                       maxPageSize,
                                                                                       auditLog,
                                                                                       connectorId);

            DisplayIntegratorContext integratorContext = new DisplayIntegratorContext(connectorId,
                                                                                      connectorName,
                                                                                      connectorUserId,
                                                                                      partnerOMASServerName,
                                                                                      openIntegrationClient,
                                                                                      openMetadataStoreClient,
                                                                                      displayApplicationClient,
                                                                                      dataManagerEventClient,
                                                                                      generateIntegrationReport,
                                                                                      permittedSynchronization,
                                                                                      integrationConnectorGUID,
                                                                                      externalSourceGUID,
                                                                                      externalSourceName,
                                                                                      auditLog,
                                                                                      maxPageSize);
            serviceSpecificConnector.setContext(integratorContext);
            integrationConnector.setConnectorName(connectorName);

            return integratorContext;
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(DisplayIntegratorErrorCode.INVALID_CONNECTOR.getMessageDefinition(connectorName,
                                                                                                                  IntegrationServiceDescription.DISPLAY_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                                                                                                  DisplayIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

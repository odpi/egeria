/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.api.contextmanager;

import org.odpi.openmetadata.accessservices.datamanager.client.APIManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.APIManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.api.connector.APIIntegratorConnector;
import org.odpi.openmetadata.integrationservices.api.connector.APIIntegratorContext;
import org.odpi.openmetadata.integrationservices.api.ffdc.APIIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.api.ffdc.APIIntegratorErrorCode;

import java.util.Map;


/**
 * APIIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of the DataManagerIntegrator integration service
 */
public class APIIntegratorContextManager extends IntegrationContextManager
{
    private APIManagerClient        apiManagerClient        = null;
    private ConnectionManagerClient connectionManagerClient = null;
    private MetadataSourceClient    metadataSourceClient    = null;
    private DataManagerRESTClient   restClient              = null;

    /**
     * Default constructor
     */
    public APIIntegratorContextManager()
    {
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
                                         AuditLog            auditLog)
    {
        super.initializeContextManager(partnerOMASServerName, partnerOMASPlatformRootURL, userId, password, serviceOptions, maxPageSize, auditLog);

        final String methodName = "initializeContextManager";

        auditLog.logMessage(methodName,
                            APIIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public void createClients() throws InvalidParameterException
    {
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

        connectionManagerClient = new ConnectionManagerClient(partnerOMASServerName,
                                                              partnerOMASPlatformRootURL,
                                                              restClient,
                                                              maxPageSize,
                                                              auditLog);

         apiManagerClient= new APIManagerClient(partnerOMASServerName,
                                                partnerOMASPlatformRootURL,
                                                restClient,
                                                maxPageSize,
                                                auditLog);

        metadataSourceClient = new MetadataSourceClient(partnerOMASServerName,
                                                        partnerOMASPlatformRootURL,
                                                        restClient,
                                                        maxPageSize);
    }


    /**
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this API manager.
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

        String metadataSourceGUID = metadataSourceClient.getMetadataSourceGUID(localServerUserId, metadataSourceQualifiedName);

        if (metadataSourceGUID == null)
        {
            APIManagerProperties properties = new APIManagerProperties();

            properties.setQualifiedName(metadataSourceQualifiedName);

            metadataSourceGUID = metadataSourceClient.createAPIManager(localServerUserId, null, null, properties);
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

        if (integrationConnector instanceof APIIntegratorConnector)
        {
            auditLog.logMessage(methodName,
                                APIIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                                                                                                                connectorId,
                                                                                                                metadataSourceQualifiedName,
                                                                                                                permittedSynchronizationName,
                                                                                                                serviceOptionsString));

            APIIntegratorConnector serviceSpecificConnector = (APIIntegratorConnector)integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);
            DataManagerEventClient dataManagerEventClient = new DataManagerEventClient(partnerOMASServerName,
                                                                                       partnerOMASPlatformRootURL,
                                                                                       restClient,
                                                                                       maxPageSize,
                                                                                       auditLog,
                                                                                       connectorId);

            serviceSpecificConnector.setContext(new APIIntegratorContext(apiManagerClient,
                                                                         connectionManagerClient,
                                                                         dataManagerEventClient,
                                                                         localServerUserId,
                                                                         metadataSourceGUID,
                                                                         metadataSourceQualifiedName));
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(APIIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.API_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         APIIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

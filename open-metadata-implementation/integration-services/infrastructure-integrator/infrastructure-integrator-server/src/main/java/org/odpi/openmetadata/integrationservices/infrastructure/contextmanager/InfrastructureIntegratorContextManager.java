/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.infrastructure.contextmanager;

import org.odpi.openmetadata.accessservices.itinfrastructure.client.CapabilityManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ConnectionManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ConnectorTypeManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.DataAssetManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.EndpointManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.HostManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ITInfrastructureEventClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ITProfileManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.PlatformManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ProcessManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.ServerManagerClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.client.rest.ITInfrastructureRESTClient;
import org.odpi.openmetadata.accessservices.itinfrastructure.metadataelements.SoftwareCapabilityElement;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.SoftwareCapabilityProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.infrastructure.connector.InfrastructureIntegratorConnector;
import org.odpi.openmetadata.integrationservices.infrastructure.connector.InfrastructureIntegratorContext;
import org.odpi.openmetadata.integrationservices.infrastructure.ffdc.InfrastructureIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.infrastructure.ffdc.InfrastructureIntegratorErrorCode;

import java.util.List;
import java.util.Map;


/**
 * InfrastructureIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class InfrastructureIntegratorContextManager extends IntegrationContextManager
{
    private CapabilityManagerClient    capabilityManagerClient    = null;
    private ConnectionManagerClient    connectionManagerClient    = null;
    private ConnectorTypeManagerClient connectorTypeManagerClient = null;
    private DataAssetManagerClient     dataAssetManagerClient     = null;
    private EndpointManagerClient      endpointManagerClient      = null;
    private HostManagerClient          hostManagerClient          = null;
    private ITProfileManagerClient     itProfileManagerClient     = null;
    private PlatformManagerClient      platformManagerClient      = null;
    private ProcessManagerClient       processManagerClient       = null;
    private ServerManagerClient        serverManagerClient        = null;
    private ITInfrastructureRESTClient restClient                 = null;

    /**
     * Default constructor
     */
    public InfrastructureIntegratorContextManager()
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
                            InfrastructureIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    @Override
    public  void createClients() throws InvalidParameterException
    {
        if (localServerPassword == null)
        {
            restClient = new ITInfrastructureRESTClient(partnerOMASServerName,
                                                   partnerOMASPlatformRootURL,
                                                   auditLog);
        }
        else
        {
            restClient = new ITInfrastructureRESTClient(partnerOMASServerName,
                                                   partnerOMASPlatformRootURL,
                                                   localServerUserId,
                                                   localServerPassword,
                                                   auditLog);
        }

        connectionManagerClient = new ConnectionManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        capabilityManagerClient  = new CapabilityManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        connectorTypeManagerClient  = new ConnectorTypeManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        dataAssetManagerClient = new DataAssetManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        endpointManagerClient = new EndpointManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        hostManagerClient = new HostManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        itProfileManagerClient = new ITProfileManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        platformManagerClient = new PlatformManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        processManagerClient = new ProcessManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
        serverManagerClient = new ServerManagerClient(partnerOMASServerName, partnerOMASPlatformRootURL, restClient, maxPageSize);
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
        if (metadataSourceQualifiedName != null)
        {
            String metadataSourceGUID = null;

            List<SoftwareCapabilityElement> softwareCapabilityElements = capabilityManagerClient.getSoftwareCapabilitiesByName(localServerUserId,
                                                                                                                               metadataSourceQualifiedName,
                                                                                                                               null, 0, 0);

            if ((softwareCapabilityElements != null) && (! softwareCapabilityElements.isEmpty()))
            {
                metadataSourceGUID = softwareCapabilityElements.get(0).getElementHeader().getGUID();
            }

            if (metadataSourceGUID == null)
            {
                SoftwareCapabilityProperties properties = new SoftwareCapabilityProperties();

                properties.setQualifiedName(metadataSourceQualifiedName);

                metadataSourceGUID = capabilityManagerClient.createSoftwareCapability(localServerUserId, null, null, false, null, properties);
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

        if (integrationConnector instanceof InfrastructureIntegratorConnector)
        {
            auditLog.logMessage(methodName,
                                InfrastructureIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                                                                                                                    connectorId,
                                                                                                                    metadataSourceQualifiedName,
                                                                                                                    permittedSynchronizationName,
                                                                                                                    serviceOptionsString));

            InfrastructureIntegratorConnector serviceSpecificConnector = (InfrastructureIntegratorConnector)integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);
            ITInfrastructureEventClient eventClient = new ITInfrastructureEventClient(partnerOMASServerName,
                                                                                      partnerOMASPlatformRootURL,
                                                                                      restClient,
                                                                                      maxPageSize,
                                                                                      auditLog,
                                                                                      connectorId);

            serviceSpecificConnector.setContext(new InfrastructureIntegratorContext(capabilityManagerClient,
                                                                                    connectionManagerClient,
                                                                                    connectorTypeManagerClient,
                                                                                    dataAssetManagerClient,
                                                                                    endpointManagerClient,
                                                                                    hostManagerClient,
                                                                                    itProfileManagerClient,
                                                                                    platformManagerClient,
                                                                                    processManagerClient,
                                                                                    serverManagerClient,
                                                                                    eventClient,
                                                                                    localServerUserId,
                                                                                    metadataSourceGUID,
                                                                                    metadataSourceQualifiedName));
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(InfrastructureIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.INFRASTRUCTURE_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         InfrastructureIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

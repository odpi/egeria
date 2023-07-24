/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.files.contextmanager;

import org.odpi.openmetadata.accessservices.datamanager.client.*;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.integration.connectors.IntegrationConnector;
import org.odpi.openmetadata.frameworks.integration.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.files.connector.FilesIntegratorConnector;
import org.odpi.openmetadata.integrationservices.files.connector.FilesIntegratorContext;
import org.odpi.openmetadata.integrationservices.files.ffdc.FilesIntegratorAuditCode;
import org.odpi.openmetadata.integrationservices.files.ffdc.FilesIntegratorErrorCode;

import java.util.Map;

/**
 * FilesIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of the Files Integrator integration service.
 */
public class FilesIntegratorContextManager extends IntegrationContextManager
{
    private FilesAndFoldersClient   filesAndFoldersClient   = null;
    private ConnectionManagerClient connectionManagerClient = null;
    private ValidValueManagement    validValueManagement    = null;
    private MetadataSourceClient    metadataSourceClient    = null;
    private DataManagerRESTClient   restClient              = null;

    /**
     * Default constructor
     */
    public FilesIntegratorContextManager()
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
                            FilesIntegratorAuditCode.CONTEXT_INITIALIZING.getMessageDefinition(partnerOMASServerName, partnerOMASPlatformRootURL));
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

        filesAndFoldersClient = new FilesAndFoldersClient(partnerOMASServerName,
                                                          partnerOMASPlatformRootURL,
                                                          restClient,
                                                          maxPageSize);

        connectionManagerClient = new ConnectionManagerClient(partnerOMASServerName,
                                                              partnerOMASPlatformRootURL,
                                                              restClient,
                                                              maxPageSize,
                                                              auditLog);

        validValueManagement = new ValidValueManagement(partnerOMASServerName,
                                                        partnerOMASPlatformRootURL,
                                                        restClient,
                                                        maxPageSize);

        metadataSourceClient = new MetadataSourceClient(partnerOMASServerName,
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

        if (integrationConnector instanceof FilesIntegratorConnector)
        {
            auditLog.logMessage(methodName,
                                FilesIntegratorAuditCode.CONNECTOR_CONTEXT_INITIALIZING.getMessageDefinition(connectorName,
                                                                                                             connectorId,
                                                                                                             metadataSourceQualifiedName,
                                                                                                             permittedSynchronizationName,
                                                                                                             serviceOptionsString));

            FilesIntegratorConnector serviceSpecificConnector = (FilesIntegratorConnector)integrationConnector;

            String externalSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName, "DataManager", "FileSystem");
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

            FilesIntegratorContext integratorContext = new FilesIntegratorContext(connectorId,
                                                                                  connectorName,
                                                                                  connectorUserId,
                                                                                  partnerOMASServerName,
                                                                                  openIntegrationClient,
                                                                                  openMetadataStoreClient,
                                                                                  filesAndFoldersClient,
                                                                                  connectionManagerClient,
                                                                                  validValueManagement,
                                                                                  dataManagerEventClient,
                                                                                  generateIntegrationReport,
                                                                                  permittedSynchronization,
                                                                                  integrationConnectorGUID,
                                                                                  externalSourceGUID,
                                                                                  externalSourceName,
                                                                                  maxPageSize);
            serviceSpecificConnector.setContext(integratorContext);
            integrationConnector.setConnectorName(connectorName);

            return integratorContext;
        }
        else
        {
            final String  parameterName = "integrationConnector";

            throw new InvalidParameterException(FilesIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.FILES_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         FilesIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

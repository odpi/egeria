/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.database.contextmanager;

import org.odpi.openmetadata.accessservices.datamanager.client.DatabaseManagerClient;
import org.odpi.openmetadata.accessservices.datamanager.client.DataManagerEventClient;
import org.odpi.openmetadata.accessservices.datamanager.client.MetadataSourceClient;
import org.odpi.openmetadata.accessservices.datamanager.client.rest.DataManagerRESTClient;
import org.odpi.openmetadata.accessservices.datamanager.properties.DatabaseManagerProperties;
import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorConnector;
import org.odpi.openmetadata.integrationservices.database.connector.DatabaseIntegratorContext;
import org.odpi.openmetadata.integrationservices.database.ffdc.DatabaseIntegratorErrorCode;

import java.util.Map;


/**
 * DatabaseIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of the DataManagerIntegrator integration service
 */
public class DatabaseIntegratorContextManager extends IntegrationContextManager
{
    private DatabaseManagerClient databaseManagerClient  = null;
    private MetadataSourceClient  metadataSourceClient   = null;
    private DataManagerRESTClient restClient             = null;

    /**
     * Default constructor
     */
    public DatabaseIntegratorContextManager()
    {
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
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

        databaseManagerClient = new DatabaseManagerClient(partnerOMASServerName,
                                                          partnerOMASPlatformRootURL,
                                                          restClient,
                                                          maxPageSize,
                                                          auditLog);

        metadataSourceClient = new MetadataSourceClient(partnerOMASServerName,
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

        String metadataSourceGUID = metadataSourceClient.getMetadataSourceGUID(localServerUserId, metadataSourceQualifiedName);

        if (metadataSourceGUID == null)
        {
            DatabaseManagerProperties properties = new DatabaseManagerProperties();

            properties.setQualifiedName(metadataSourceQualifiedName);

            metadataSourceGUID = metadataSourceClient.createDatabaseManager(localServerUserId, null, null, properties);
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
    public void setContext(String                   connectorId,
                           String                   connectorName,
                           String                   metadataSourceQualifiedName,
                           IntegrationConnector     integrationConnector,
                           PermittedSynchronization permittedSynchronization,
                           Map<String, Object>      serviceOptions) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException
    {
        if (integrationConnector instanceof DatabaseIntegratorConnector)
        {
            DatabaseIntegratorConnector serviceSpecificConnector = (DatabaseIntegratorConnector)integrationConnector;

            String metadataSourceGUID = this.setUpMetadataSource(metadataSourceQualifiedName);
            DataManagerEventClient dataManagerEventClient = new DataManagerEventClient(partnerOMASServerName,
                                                                                       partnerOMASPlatformRootURL,
                                                                                       restClient,
                                                                                       maxPageSize,
                                                                                       auditLog,
                                                                                       connectorId);

            serviceSpecificConnector.setContext(new DatabaseIntegratorContext(databaseManagerClient,
                                                                              dataManagerEventClient,
                                                                              localServerUserId,
                                                                              metadataSourceGUID,
                                                                              metadataSourceQualifiedName));
        }
        else
        {
            final String  parameterName = "integrationConnector";
            final String  methodName = "setContext";

            throw new InvalidParameterException(DatabaseIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.DATABASE_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         DatabaseIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

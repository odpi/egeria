/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.securityintegrator.contextmanager;

import org.odpi.openmetadata.adminservices.configuration.properties.PermittedSynchronization;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.securityintegrator.connector.SecurityIntegratorConnector;
import org.odpi.openmetadata.integrationservices.securityintegrator.connector.SecurityIntegratorContext;
import org.odpi.openmetadata.integrationservices.securityintegrator.ffdc.SecurityIntegratorErrorCode;

import java.util.Map;


/**
 * SecurityIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class SecurityIntegratorContextManager extends IntegrationContextManager
{
    private SecurityIntegratorContext context = null;

    /**
     * Default constructor
     */
    public SecurityIntegratorContextManager()
    {
    }


    /**
     * Suggestion for subclass to create client(s) to partner OMAS.
     *
     * @throws InvalidParameterException the subclass is not able to create one of its clients
     */
    public  void createClients() throws InvalidParameterException
    {

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
    public void setContext(String                   connectorId,
                           String                   connectorName,
                           String                   metadataSourceQualifiedName,
                           IntegrationConnector     integrationConnector,
                           PermittedSynchronization permittedSynchronization) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        if (integrationConnector instanceof SecurityIntegratorConnector)
        {
            SecurityIntegratorConnector serviceSpecificConnector = (SecurityIntegratorConnector)integrationConnector;

            if (context == null)
            {
                context = new SecurityIntegratorContext();
            }

        }
        else
        {
            final String  parameterName = "integrationConnector";
            final String  methodName = "setContext";

            throw new InvalidParameterException(SecurityIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.ORGANIZATION_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         SecurityIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

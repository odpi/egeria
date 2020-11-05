/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.integrationservices.catalogintegrator.contextmanager;

import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.connectors.IntegrationConnector;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.contextmanager.IntegrationContextManager;
import org.odpi.openmetadata.governanceservers.integrationdaemonservices.registration.IntegrationServiceDescription;
import org.odpi.openmetadata.integrationservices.catalogintegrator.connector.CatalogIntegratorConnector;
import org.odpi.openmetadata.integrationservices.catalogintegrator.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalogintegrator.ffdc.CatalogIntegratorErrorCode;


/**
 * CatalogIntegratorContextManager provides the bridge between the integration daemon services and
 * the specific implementation of an integration service
 */
public class CatalogIntegratorContextManager extends IntegrationContextManager
{
    private CatalogIntegratorContext context                     = null;
    private String                   metadataSourceQualifiedName = null;
    private String                   metadataSourceGUID          = null;

    /**
     * Default constructor
     */
    public CatalogIntegratorContextManager()
    {
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
     * Retrieve the metadata source's unique identifier (GUID) or if it is not defined, create the software server capability
     * for this metadata source.
     *
     * @param integratorQualifiedName unique name of the software server capability that represents this integration
     *                                service
     * @throws InvalidParameterException one of the parameters passed (probably on initialize) is invalid
     * @throws UserNotAuthorizedException the integration daemon's userId does not have access to the partner OMAS
     * @throws PropertyServerException there is a problem in the remote server running the partner OMAS
     */
    private void setUpMetadataSource(String   integratorQualifiedName) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {

    }


    /**
     * Set up the context in the supplied connector. This is called between initialize() and start() on the connector.
     *
     * @param connectorName name of the connector
     * @param integrationConnector connector created from connection integration service configuration
     * @throws InvalidParameterException the connector is not of the correct type
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void setContext(String                 connectorName,
                           String                 metadataSourceQualifiedName,
                           IntegrationConnector   integrationConnector) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException,
                                                                               ConnectorCheckedException
    {
        if (integrationConnector instanceof CatalogIntegratorConnector)
        {
            CatalogIntegratorConnector serviceSpecificConnector = (CatalogIntegratorConnector)integrationConnector;

            if (context == null)
            {
                context = new CatalogIntegratorContext();
            }

        }
        else
        {
            final String  parameterName = "integrationConnector";
            final String  methodName = "setContext";

            throw new InvalidParameterException(CatalogIntegratorErrorCode.INVALID_CONNECTOR.
                    getMessageDefinition(connectorName,
                                         IntegrationServiceDescription.CATALOG_INTEGRATOR_OMIS.getIntegrationServiceFullName(),
                                         CatalogIntegratorConnector.class.getCanonicalName()),
                                                this.getClass().getName(),
                                                methodName,
                                                parameterName);
        }
    }
}

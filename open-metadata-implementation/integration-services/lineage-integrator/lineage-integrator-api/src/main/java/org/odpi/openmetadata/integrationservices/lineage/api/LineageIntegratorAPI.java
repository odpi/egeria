/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.integrationservices.lineage.api;

import io.openlineage.client.OpenLineage;
import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

/**
 * LineageIntegratorAPI is the interface to call the services provides by the Lineage Integrator OMIS running in an integration daemon.
 */
public interface LineageIntegratorAPI
{
    /**
     * Validate that a connector is suitable to run in the Lineage Integrator OMIS.
     * It validates that it implements the correct interfaces and returns the connector type for the connector.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector report for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there is a problem processing the request
     */
    ConnectorReport validateConnector(String userId,
                                      String connectorProviderClassName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;

    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param userId calling user
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException there is a problem processing the request
     */
    void publishOpenLineageEvent(String               userId,
                                 OpenLineage.RunEvent event) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Pass an open lineage event to the integration service.  It will pass it on to the integration connectors that have registered a
     * listener for open lineage events.
     *
     * @param userId calling user
     * @param event open lineage event to publish.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws UserNotAuthorizedException the caller is not authorized to call the service
     * @throws PropertyServerException there is a problem processing the request
     */
    void publishOpenLineageEvent(String userId,
                                 String event) throws InvalidParameterException,
                                                      UserNotAuthorizedException,
                                                      PropertyServerException;
}

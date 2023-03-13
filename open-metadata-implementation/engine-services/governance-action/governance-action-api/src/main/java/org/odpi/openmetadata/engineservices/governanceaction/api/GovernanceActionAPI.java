/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.engineservices.governanceaction.api;

import org.odpi.openmetadata.commonservices.ffdc.properties.ConnectorReport;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;


/**
 * GovernanceActionAPI is the interface to validate that a connector is suitable to run in the Governance Action OMES.
 * It validates that it implements the correct interfaces and returns the connector type for the connector.
 */
public interface GovernanceActionAPI
{
    /**
     * Validate the connector and return its connector type.
     *
     * @param userId calling user
     * @param connectorProviderClassName name of a specific connector or null for all connectors
     *
     * @return connector type and other capabilities for this connector
     *
     * @throws InvalidParameterException the connector provider class name is not a valid connector fo this service
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException there was a problem detected by the integration service
     */
    ConnectorReport validateConnector(String userId,
                                      String connectorProviderClassName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;
}

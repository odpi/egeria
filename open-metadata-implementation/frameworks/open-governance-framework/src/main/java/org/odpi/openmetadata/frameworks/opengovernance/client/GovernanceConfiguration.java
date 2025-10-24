/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.opengovernance.client;

import org.odpi.openmetadata.frameworks.opengovernance.properties.GovernanceEngineElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.IntegrationGroupElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredGovernanceServiceElement;
import org.odpi.openmetadata.frameworks.opengovernance.properties.RegisteredIntegrationConnectorElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;


/**
 * GovernanceConfiguration supports the configuration of governance engine and governance services.
 */
public abstract class GovernanceConfiguration
{
    protected final String                                  serverName;               /* Initialized in constructor */
    protected final String                                  serverPlatformURLRoot;    /* Initialized in constructor */
    protected final String                                  serviceURLMarker;         /* Initialized in constructor */


    /**
     * Constructor called by the subclasses.
     *
     * @param serverName remote server to call
     * @param serverPlatformURLRoot  platform hosting remote server
     * @param serviceURLMarker service that his request is being made to
     */
    public GovernanceConfiguration(String serverName,
                                   String serverPlatformURLRoot,
                                   String serviceURLMarker)
    {
        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.serviceURLMarker      = serviceURLMarker;
    }



    /**
     * Return the properties from a governance engine definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public abstract GovernanceEngineElement getGovernanceEngineByName(String    userId,
                                                                      String    name) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;

    /**
     * Retrieve a specific governance service registered with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @return details of the governance service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public abstract RegisteredGovernanceServiceElement getRegisteredGovernanceService(String userId,
                                                                                      String governanceEngineGUID,
                                                                                      String governanceServiceGUID) throws InvalidParameterException,
                                                                                                                           UserNotAuthorizedException,
                                                                                                                           PropertyServerException;


    /**
     * Retrieve the identifiers of the governance services registered with a governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public abstract List<RegisteredGovernanceServiceElement>  getRegisteredGovernanceServices(String userId,
                                                                                              String governanceEngineGUID,
                                                                                              int    startingFrom,
                                                                                              int    maximumResults) throws InvalidParameterException,
                                                                                                                            UserNotAuthorizedException,
                                                                                                                            PropertyServerException;
    /*
     * Integration connectors
     */

    /**
     * Return the properties from an integration group definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     * @return properties from the integration group definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration group definition.
     */
    public abstract IntegrationGroupElement getIntegrationGroupByName(String    userId,
                                                                      String    name) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;

    /**
     * Return the list of integration groups that a specific integration connector is registered with.
     *
     * @param userId identifier of calling user
     * @param integrationConnectorGUID integration connector to search for.
     *
     * @return list of integration group unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public abstract List<String>  getIntegrationConnectorRegistrations(String   userId,
                                                                       String   integrationConnectorGUID) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException,
                                                                                                                 PropertyServerException;

    /**
     * Retrieve a specific integration connector registered with an integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param integrationConnectorGUID unique identifier of the integration connector.
     *
     * @return details of the integration connector and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public abstract RegisteredIntegrationConnectorElement getRegisteredIntegrationConnector(String userId,
                                                                                            String integrationGroupGUID,
                                                                                            String integrationConnectorGUID) throws InvalidParameterException,
                                                                                                                                    UserNotAuthorizedException,
                                                                                                                                    PropertyServerException;


    /**
     * Retrieve the identifiers of the integration connectors registered with an integration group.
     *
     * @param userId identifier of calling user
     * @param integrationGroupGUID unique identifier of the integration group.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the integration connector and/or integration group definitions.
     */
    public abstract List<RegisteredIntegrationConnectorElement>  getRegisteredIntegrationConnectors(String userId,
                                                                                                    String integrationGroupGUID,
                                                                                                    int    startingFrom,
                                                                                                    int    maximumResults) throws InvalidParameterException,
                                                                                                                                  UserNotAuthorizedException,
                                                                                                                                  PropertyServerException;
}

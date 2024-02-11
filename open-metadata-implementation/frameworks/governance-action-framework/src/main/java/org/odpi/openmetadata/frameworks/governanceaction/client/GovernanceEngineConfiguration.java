/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceEngineElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.GovernanceServiceElement;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RegisteredGovernanceServiceElement;

import java.util.List;
import java.util.Map;


/**
 * GovernanceEngineConfiguration supports the configuration of governance engine and governance services.
 */
public abstract class GovernanceEngineConfiguration
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
    public GovernanceEngineConfiguration(String serverName,
                                         String serverPlatformURLRoot,
                                         String serviceURLMarker)
    {
        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.serviceURLMarker      = serviceURLMarker;
    }


    /**
     * Create a new governance engine definition.
     *
     * @param userId identifier of calling user
     * @param governanceEngineType type of governance engine to create
     * @param qualifiedName unique name for the governance engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of governance services that will be associated with
     *                    this governance engine.
     *
     * @return unique identifier (guid) of the governance engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    public abstract String  createGovernanceEngine(String  userId,
                                                   String  governanceEngineType,
                                                   String  qualifiedName,
                                                   String  displayName,
                                                   String  description) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Return the properties from a governance engine definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance engine definition.
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public abstract GovernanceEngineElement getGovernanceEngineByGUID(String    userId,
                                                                      String    guid) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


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
     * Return the list of governance engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param governanceEngineType type of governance engine to create
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     * @return list of governance engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definitions.
     */
    public abstract List<GovernanceEngineElement> getAllGovernanceEngines(String  userId,
                                                                          String  governanceEngineType,
                                                                          int     startingFrom,
                                                                          int     maximumResults) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException;


    /**
     * Update the properties of an existing governance engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName new value for unique name of governance engine.
     * @param displayName new value for the display name.
     * @param description new description for the governance engine.
     * @param typeDescription new description of the type ofg governance engine.
     * @param version new version number for the governance engine implementation.
     * @param patchLevel new patch level for the governance engine implementation.
     * @param source new source description for the implementation of the governance engine.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance engine definition.
     */
    public abstract void    updateGovernanceEngine(String                userId,
                                                   String                guid,
                                                   String                qualifiedName,
                                                   String                displayName,
                                                   String                description,
                                                   String                typeDescription,
                                                   String                version,
                                                   String                patchLevel,
                                                   String                source,
                                                   Map<String, String>   additionalProperties,
                                                   Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Remove the properties of the governance engine.  Both the guid and the qualified name is supplied
     * to validate that the correct governance engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance engine - used to locate the definition.
     * @param qualifiedName unique name for the governance engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public abstract void    deleteGovernanceEngine(String  userId,
                                                   String  guid,
                                                   String  qualifiedName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Create a governance service definition.  The same governance service can be associated with multiple
     * governance engines.
     *
     * @param userId identifier of calling user
     * @param governanceServiceType type of the governance service to create
     * @param qualifiedName  unique name for the governance service.
     * @param displayName   display name for the governance service.
     * @param description  description of the analysis provided by the governance service.
     * @param connection   connection to instantiate the governance service implementation.
     *
     * @return unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public abstract String  createGovernanceService(String     userId,
                                                    String     governanceServiceType,
                                                    String     qualifiedName,
                                                    String     displayName,
                                                    String     description,
                                                    Connection connection) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the governance service definition.
     *
     * @return properties of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public abstract GovernanceServiceElement getGovernanceServiceByGUID(String    userId,
                                                                        String    guid) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Return the properties from a governance service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the governance engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance engine definition.
     */
    public abstract GovernanceServiceElement getGovernanceServiceByName(String    userId,
                                                                        String    name) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Return the list of governance services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of governance service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definitions.
     */
    public abstract List<GovernanceServiceElement> getAllGovernanceServices(String  userId,
                                                                            int     startingFrom,
                                                                            int     maximumResults) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Return the list of governance engines that a specific governance service is registered with.
     *
     * @param userId identifier of calling user
     * @param governanceServiceGUID governance service to search for.
     *
     * @return list of governance engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public abstract List<String>  getGovernanceServiceRegistrations(String   userId,
                                                                    String   governanceServiceGUID) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;


    /**
     * Update the properties of an existing governance service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName new value for unique name of governance service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this governance service.
     * @param additionalProperties additional properties for the governance engine.
     * @param extendedProperties properties to populate the subtype of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the governance service definition.
     */
    public abstract void    updateGovernanceService(String                userId,
                                                    String                guid,
                                                    String                qualifiedName,
                                                    String                displayName,
                                                    String                description,
                                                    Connection            connection,
                                                    Map<String, String>   additionalProperties,
                                                    Map<String, Object>   extendedProperties) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Remove the properties of the governance service.  Both the guid and the qualified name is supplied
     * to validate that the correct governance service is being deleted.  The governance service is also
     * unregistered from its governance engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the governance service - used to locate the definition.
     * @param qualifiedName unique name for the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service definition.
     */
    public abstract void    deleteGovernanceService(String  userId,
                                                    String  guid,
                                                    String  qualifiedName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Register a governance service with a specific governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param requestType request type that this governance service is able to process.
     * @param requestParameters list of parameters that are passed to the governance service (via
     *                             the context).  These values can be overridden on the actual governance service request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public abstract void  registerGovernanceServiceWithEngine(String              userId,
                                                              String              governanceEngineGUID,
                                                              String              governanceServiceGUID,
                                                              String              requestType,
                                                              Map<String, String> requestParameters) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;

    /**
     * Register a governance service with a specific governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     * @param governanceRequestType governance request type used by caller.
     * @param serviceRequestType mapped governance request type that this governance service is able to process.
     * @param requestParameters list of parameters that are passed to the governance service (via
     *                                  the governance context).  These values can be overridden on the actual governance request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public abstract void registerGovernanceServiceWithEngine(String               userId,
                                                             String               governanceEngineGUID,
                                                             String               governanceServiceGUID,
                                                             String               governanceRequestType,
                                                             String               serviceRequestType,
                                                             Map<String, String>  requestParameters) throws InvalidParameterException,
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


    /**
     * Unregister a governance service from the governance engine.
     *
     * @param userId identifier of calling user
     * @param governanceEngineGUID unique identifier of the governance engine.
     * @param governanceServiceGUID unique identifier of the governance service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the governance service and/or governance engine definitions.
     */
    public abstract void  unregisterGovernanceServiceFromEngine(String userId,
                                                                String governanceEngineGUID,
                                                                String governanceServiceGUID) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;
}

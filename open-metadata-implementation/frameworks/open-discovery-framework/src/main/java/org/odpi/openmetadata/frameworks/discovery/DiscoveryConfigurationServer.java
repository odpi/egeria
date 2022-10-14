/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.discovery;

import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryEngineProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.DiscoveryServiceProperties;
import org.odpi.openmetadata.frameworks.discovery.properties.RegisteredDiscoveryService;

import java.util.List;
import java.util.Map;

/**
 * DiscoveryConfigurationServer defines the external interface used to define information
 * about a discovery engine.  Discovery engines are hosted by the discovery server.
 */
public abstract class DiscoveryConfigurationServer
{
    /**
     * Create a new discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param qualifiedName unique name for the discovery engine.
     * @param displayName display name for messages and user interfaces.
     * @param description description of the types of discovery services that will be associated with
     *                    this discovery engine.
     *
     * @return unique identifier (guid) of the discovery engine definition.  This is for use on other requests.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery engine definition.
     */
    public abstract String  createDiscoveryEngine(String  userId,
                                                  String  qualifiedName,
                                                  String  displayName, 
                                                  String  description) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public abstract DiscoveryEngineProperties getDiscoveryEngineByGUID(String    userId,
                                                                       String    guid) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public abstract DiscoveryEngineProperties getDiscoveryEngineByName(String    userId,
                                                                       String    name) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException;


    /**
     * Return the list of discovery engine definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery engine definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definitions.
     */
    public abstract List<DiscoveryEngineProperties> getAllDiscoveryEngines(String  userId,
                                                                           int     startingFrom,
                                                                           int     maximumResults) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException;


    /**
     * Update the properties of an existing discovery engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param qualifiedName new value for unique name of discovery engine.
     * @param displayName new value for the display name.
     * @param description new description for the discovery engine.
     * @param typeDescription new description of the type ofg discovery engine.
     * @param version new version number for the discovery engine implementation.
     * @param patchLevel new patch level for the discovery engine implementation.
     * @param source new source description for the implementation of the discovery engine.
     * @param additionalProperties additional properties for the discovery engine.
     * @param extendedProperties properties to populate the subtype of the discovery engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery engine definition.
     */
    public abstract void    updateDiscoveryEngine(String                userId,
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
     * Remove the properties of the discovery engine.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery engine is being deleted.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param qualifiedName unique name for the discovery engine.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public abstract void    deleteDiscoveryEngine(String  userId,
                                                  String  guid,
                                                  String  qualifiedName) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Create a discovery service definition.  The same discovery service can be associated with multiple
     * discovery engines.
     *
     * @param userId identifier of calling user
     * @param qualifiedName  unique name for the discovery service.
     * @param displayName   display name for the discovery service.
     * @param description  description of the analysis provided by the discovery service.
     * @param connection   connection to instanciate the discovery service implementation.
     *
     * @return unique identifier of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery service definition.
     */
    public abstract String  createDiscoveryService(String      userId,
                                                   String      qualifiedName,
                                                   String      displayName,
                                                   String      description,
                                                   Connection  connection) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;



    /**
     * Return the properties from a discovery service definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier (guid) of the discovery service definition.
     *
     * @return properties of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    public abstract DiscoveryServiceProperties getDiscoveryServiceByGUID(String    userId,
                                                                         String    guid) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Return the properties from a discovery service definition.
     *
     * @param userId identifier of calling user
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery engine definition.
     */
    public abstract DiscoveryServiceProperties getDiscoveryServiceByName(String    userId,
                                                                         String    name) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param userId identifier of calling user
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery service definitions.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definitions.
     */
    public abstract List<DiscoveryServiceProperties> getAllDiscoveryServices(String  userId,
                                                                             int     startingFrom,
                                                                             int     maximumResults) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException;


    /**
     * Return the list of discovery engines that a specific discovery service is registered with.
     *
     * @param userId identifier of calling user
     * @param discoveryServiceGUID discovery service to search for.
     *
     * @return list of discovery engine unique identifiers (guids)
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public abstract List<String>  getDiscoveryServiceRegistrations(String   userId,
                                                                   String   discoveryServiceGUID) throws InvalidParameterException,
                                                                                                         UserNotAuthorizedException,
                                                                                                         PropertyServerException;


    /**
     * Update the properties of an existing discovery service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param qualifiedName new value for unique name of discovery service.
     * @param displayName new value for the display name.
     * @param description new value for the description.
     * @param connection connection used to create an instance of this discovery service.
     * @param additionalProperties additional properties for the discovery engine.
     * @param extendedProperties properties to populate the subtype of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem storing the discovery service definition.
     */
    public abstract void    updateDiscoveryService(String                userId,
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
     * Remove the properties of the discovery service.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery service is being deleted.  The discovery service is also
     * unregistered from its discovery engines.
     *
     * @param userId identifier of calling user
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param qualifiedName unique name for the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service definition.
     */
    public abstract void    deleteDiscoveryService(String  userId,
                                                   String  guid,
                                                   String  qualifiedName) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Register a discovery service with a specific discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     * @param discoveryRequestType discovery request type that this discovery service is able to process.
     * @param defaultAnalysisParameters list of analysis parameters that are passed to the discovery service (via
     *                                  the discovery context).  These values can be overridden on the actual discovery request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public abstract void  registerDiscoveryServiceWithEngine(String               userId,
                                                             String               discoveryEngineGUID,
                                                             String               discoveryServiceGUID,
                                                             String               discoveryRequestType,
                                                             Map<String, String>  defaultAnalysisParameters) throws InvalidParameterException,
                                                                                                                    UserNotAuthorizedException,
                                                                                                                    PropertyServerException;


    /**
     * Retrieve a specific discovery service registered with a discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @return details of the discovery service and the asset types it is registered for.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public abstract RegisteredDiscoveryService getRegisteredDiscoveryService(String  userId,
                                                                             String  discoveryEngineGUID,
                                                                             String  discoveryServiceGUID) throws InvalidParameterException,
                                                                                                                  UserNotAuthorizedException,
                                                                                                                  PropertyServerException;


    /**
     * Retrieve the identifiers of the discovery services registered with a discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public abstract List<String>  getRegisteredDiscoveryServices(String  userId,
                                                                 String  discoveryEngineGUID,
                                                                 int     startingFrom,
                                                                 int     maximumResults) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Unregister a discovery service from the discovery engine.
     *
     * @param userId identifier of calling user
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException problem retrieving the discovery service and/or discovery engine definitions.
     */
    public abstract void  unregisterDiscoveryServiceFromEngine(String        userId,
                                                               String        discoveryEngineGUID,
                                                               String        discoveryServiceGUID) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException;
}

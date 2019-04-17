/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.discoveryengine.server;

import org.odpi.openmetadata.accessservices.discoveryengine.handlers.DiscoveryConfigurationServerHandler;
import org.odpi.openmetadata.accessservices.discoveryengine.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.OwnerType;
import org.odpi.openmetadata.frameworks.discovery.ffdc.InvalidParameterException;

import org.odpi.openmetadata.frameworks.discovery.ffdc.ODFCheckedExceptionBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The DiscoveryConfigurationServices provides the server-side implementation of the configuration services
 * from the Open Discovery Framework (ODF).  These services configure discovery engines and discovery services.
 */
public class DiscoveryConfigurationServices
{
    private static DiscoveryEngineInstanceHandler   instanceHandler     = new DiscoveryEngineInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DiscoveryConfigurationServices.class);


    /**
     * Default constructor
     */
    public DiscoveryConfigurationServices()
    {
    }


    /**
     * Create a new discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                qualifiedName - unique name for the discovery engine;
     *                displayName - display name for messages and user interfaces;
     *                description - description of the types of discovery services that wil be associated with
     *                    this discovery engine.
     *
     * @return unique identifier (guid) of the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public GUIDResponse createDiscoveryEngine(String                        serverName,
                                              String                        userId,
                                              NewDiscoveryEngineRequestBody requestBody)
    {
        final String        methodName = "createDiscoveryEngine";

        log.debug("Calling method: " + methodName);

        String       qualifiedName = null;
        String       displayName   = null;
        String       description   = null;
        GUIDResponse response      = new GUIDResponse();


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setGUID(handler.createDiscoveryEngine(userId,
                                                           qualifiedName,
                                                           displayName,
                                                           description));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the discovery engine definition.
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public DiscoveryEnginePropertiesResponse getDiscoveryEngineByGUID(String    serverName,
                                                                      String    userId,
                                                                      String    guid)
    {
        final String        methodName = "getDiscoveryEngineByGUID";

        log.debug("Calling method: " + methodName);

        DiscoveryEnginePropertiesResponse response = new DiscoveryEnginePropertiesResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setDiscoveryEngineProperties(handler.getDiscoveryEngineByGUID(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery engine definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  DiscoveryEnginePropertiesResponse getDiscoveryEngineByName(String    serverName,
                                                                       String    userId,
                                                                       String    name)
    {
        final String        methodName = "getDiscoveryEngineByName";

        log.debug("Calling method: " + methodName);

        DiscoveryEnginePropertiesResponse response = new DiscoveryEnginePropertiesResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setDiscoveryEngineProperties(handler.getDiscoveryEngineByName(userId, name));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of discovery engine definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery engine definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public DiscoveryEngineListResponse getAllDiscoveryEngines(String  serverName,
                                                              String  userId,
                                                              int     startingFrom,
                                                              int     maximumResults)
    {
        final String        methodName = "getAllDiscoveryEngines";

        log.debug("Calling method: " + methodName);

        DiscoveryEngineListResponse response = new DiscoveryEngineListResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setDiscoveryEngines(handler.getAllDiscoveryEngines(userId, startingFrom, maximumResults));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Update the properties of an existing discovery engine definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param requestBody containing the new properties of the discovery engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    updateDiscoveryEngine(String                           serverName,
                                                  String                           userId,
                                                  String                           guid,
                                                  UpdateDiscoveryEngineRequestBody requestBody)
    {
        final String        methodName = "updateDiscoveryEngine";

        log.debug("Calling method: " + methodName);

        String              qualifiedName        = null;
        String              displayName          = null;
        String              description          = null;
        String              typeDescription      = null;
        String              version              = null;
        String              patchLevel           = null;
        String              source               = null;
        Map<String, String> additionalProperties = null;
        Map<String, Object> extendedProperties   = null;
        VoidResponse        response             = new VoidResponse();


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
            typeDescription = requestBody.getTypeDescription();
            version = requestBody.getVersion();
            patchLevel = requestBody.getPatchLevel();
            source = requestBody.getSource();
            additionalProperties = requestBody.getAdditionalProperties();
            extendedProperties = requestBody.getExtendedProperties();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            handler.updateDiscoveryEngine(userId,
                                          guid,
                                          qualifiedName,
                                          displayName,
                                          description,
                                          typeDescription,
                                          version,
                                          patchLevel,
                                          source,
                                          additionalProperties,
                                          extendedProperties);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove the properties of the discovery engine.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery engine is being deleted.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery engine - used to locate the definition.
     * @param requestBody containing the unique name for the discovery engine.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    deleteDiscoveryEngine(String             serverName,
                                                  String             userId,
                                                  String             guid,
                                                  DeleteRequestBody  requestBody)
    {
        final String        methodName = "deleteDiscoveryEngine";

        log.debug("Calling method: " + methodName);

        String       qualifiedName = null;
        VoidResponse response      = new VoidResponse();


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            handler.deleteDiscoveryEngine(userId,
                                          guid,
                                          qualifiedName);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Create a discovery service definition.  The same discovery service can be associated with multiple
     * discovery engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param requestBody containing:
     *                    qualifiedName - unique name for the discovery service;
     *                    displayName -  display name for the discovery service;
     *                    description - description of the analysis provided by the discovery service;
     *                    connection -  connection to instanciate the discovery service implementation.
     *
     * @return unique identifier of the discovery service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  GUIDResponse  createDiscoveryService(String                         serverName,
                                                 String                         userId,
                                                 NewDiscoveryServiceRequestBody requestBody)
    {
        final String        methodName = "createDiscoveryService";

        log.debug("Calling method: " + methodName);

        String       qualifiedName = null;
        String       displayName   = null;
        String       description   = null;
        Connection   connection    = null;
        GUIDResponse response      = new GUIDResponse();


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            description = requestBody.getDescription();
            connection = requestBody.getConnection();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setGUID(handler.createDiscoveryService(userId,
                                                            qualifiedName,
                                                            displayName,
                                                            description,
                                                            connection));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier (guid) of the discovery service definition.
     *
     * @return properties of the discovery service or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public DiscoveryServicePropertiesResponse getDiscoveryServiceByGUID(String    serverName,
                                                                        String    userId,
                                                                        String    guid)
    {
        final String        methodName = "getDiscoveryServiceByGUID";

        log.debug("Calling method: " + methodName);

        DiscoveryServicePropertiesResponse response = new DiscoveryServicePropertiesResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setDiscoveryServiceProperties(handler.getDiscoveryServiceByGUID(userId, guid));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the properties from a discovery service definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param name qualified name or display name (if unique).
     *
     * @return properties from the discovery engine definition or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  DiscoveryServicePropertiesResponse getDiscoveryServiceByName(String    serverName,
                                                                         String    userId,
                                                                         String    name)
    {
        final String        methodName = "getDiscoveryServiceByName";

        log.debug("Calling method: " + methodName);

        DiscoveryServicePropertiesResponse response = new DiscoveryServicePropertiesResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setDiscoveryServiceProperties(handler.getDiscoveryServiceByName(userId, name));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of discovery services definitions that are stored.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of discovery service definitions or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  DiscoveryServiceListResponse getAllDiscoveryServices(String  serverName,
                                                                 String  userId,
                                                                 int     startingFrom,
                                                                 int     maximumResults)
    {
        final String        methodName = "getAllDiscoveryServices";

        log.debug("Calling method: " + methodName);

        DiscoveryServiceListResponse response = new DiscoveryServiceListResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setDiscoveryServices(handler.getAllDiscoveryServices(userId, startingFrom, maximumResults));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the list of discovery engines that a specific discovery service is registered with.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryServiceGUID discovery service to search for.
     *
     * @return list of discovery engine unique identifiers (guids) or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  GUIDListResponse  getDiscoveryServiceRegistrations(String   serverName,
                                                               String   userId,
                                                               String   discoveryServiceGUID)
    {
        final String        methodName = "getDiscoveryServiceRegistrations";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setGUIDs(handler.getDiscoveryServiceRegistrations(userId, discoveryServiceGUID));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Update the properties of an existing discovery service definition.  Use the current value to
     * keep a property value the same, or use the new value.  Null means remove the property from
     * the definition.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param requestBody containing the new parameters for the discovery service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    updateDiscoveryService(String                             serverName,
                                                   String                             userId,
                                                   String                             guid,
                                                   UpdateDiscoveryServiceRequestBody  requestBody)
    {
        final String        methodName = "updateDiscoveryService";

        log.debug("Calling method: " + methodName);

        String              qualifiedName        = null;
        String              displayName          = null;
        String              shortDescription     = null;
        String              description          = null;
        String              owner                = null;
        OwnerType           ownerType            = null;
        List<String>        zoneMembership       = null;
        String              latestChange         = null;
        Connection          connection           = null;
        Map<String, String> additionalProperties = null;
        Map<String, Object> extendedProperties   = null;
        VoidResponse        response             = new VoidResponse();


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
            displayName = requestBody.getDisplayName();
            shortDescription = requestBody.getShortDescription();
            description = requestBody.getDescription();
            owner = requestBody.getOwner();
            ownerType = requestBody.getOwnerType();
            zoneMembership = requestBody.getZoneMembership();
            latestChange = requestBody.getLatestChange();
            connection = requestBody.getConnection();
            additionalProperties = requestBody.getAdditionalProperties();
            extendedProperties = requestBody.getExtendedProperties();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            handler.updateDiscoveryService(userId,
                                           guid,
                                           qualifiedName,
                                           displayName,
                                           shortDescription,
                                           description,
                                           owner,
                                           ownerType,
                                           zoneMembership,
                                           latestChange,
                                           connection,
                                           additionalProperties,
                                           extendedProperties);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove the properties of the discovery service.  Both the guid and the qualified name is supplied
     * to validate that the correct discovery service is being deleted.  The discovery service is also
     * unregistered from its discovery engines.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param guid unique identifier of the discovery service - used to locate the definition.
     * @param requestBody containing the unique name for the discovery service.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse    deleteDiscoveryService(String             serverName,
                                                   String             userId,
                                                   String             guid,
                                                   DeleteRequestBody  requestBody)
    {
        final String        methodName = "deleteDiscoveryService";

        log.debug("Calling method: " + methodName);

        String       qualifiedName = null;
        VoidResponse response      = new VoidResponse();


        if (requestBody != null)
        {
            qualifiedName = requestBody.getQualifiedName();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            handler.deleteDiscoveryService(userId,
                                           guid,
                                           qualifiedName);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Register a discovery service with a specific discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param requestBody containing:
     *                    discoveryServiceGUID - unique identifier of the discovery service;
     *                    assetTypes - list of asset types that this discovery service is able to process.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  VoidResponse  registerDiscoveryServiceWithEngine(String                                  serverName,
                                                             String                                  userId,
                                                             String                                  discoveryEngineGUID,
                                                             DiscoveryServiceRegistrationRequestBody requestBody)
    {
        final String        methodName = "registerDiscoveryServiceWithEngine";

        log.debug("Calling method: " + methodName);

        String       discoveryServiceGUID = null;
        List<String> assetTypes           = null;
        VoidResponse response             = new VoidResponse();


        if (requestBody != null)
        {
            discoveryServiceGUID = requestBody.getDiscoveryServiceGUID();
            assetTypes = requestBody.getAssetTypes();
        }

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            handler.registerDiscoveryServiceWithEngine(userId, discoveryEngineGUID, discoveryServiceGUID, assetTypes);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve a specific discovery service registered with a discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     *
     * @return details of the discovery service and the asset types it is registered for or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public RegisteredDiscoveryServiceResponse getRegisteredDiscoveryService(String  serverName,
                                                                            String  userId,
                                                                            String  discoveryEngineGUID,
                                                                            String  discoveryServiceGUID)
    {
        final String        methodName = "getRegisteredDiscoveryService";

        log.debug("Calling method: " + methodName);

        RegisteredDiscoveryServiceResponse response = new RegisteredDiscoveryServiceResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setRegisteredDiscoveryService(handler.getRegisteredDiscoveryService(userId, discoveryEngineGUID, discoveryServiceGUID));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve the identifiers of the discovery services registered with a discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param startingFrom initial position in the stored list.
     * @param maximumResults maximum number of definitions to return on this call.
     *
     * @return list of unique identifiers or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public  GUIDListResponse  getRegisteredDiscoveryServices(String  serverName,
                                                             String  userId,
                                                             String  discoveryEngineGUID,
                                                             int     startingFrom,
                                                             int     maximumResults)
    {
        final String        methodName = "getRegisteredDiscoveryServices";

        log.debug("Calling method: " + methodName);

        GUIDListResponse response = new GUIDListResponse();

        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            response.setGUIDs(handler.getRegisteredDiscoveryServices(userId, discoveryEngineGUID, startingFrom, maximumResults));
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Unregister a discovery service from the discovery engine.
     *
     * @param serverName name of the service to route the request to.
     * @param userId identifier of calling user.
     * @param discoveryEngineGUID unique identifier of the discovery engine.
     * @param discoveryServiceGUID unique identifier of the discovery service.
     * @param requestBody null request body.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid or
     * UserNotAuthorizedException user not authorized to issue this request or
     * PropertyServerException problem storing the discovery engine definition.
     */
    public VoidResponse unregisterDiscoveryServiceFromEngine(String          serverName,
                                                             String          userId,
                                                             String          discoveryEngineGUID,
                                                             String          discoveryServiceGUID,
                                                             NullRequestBody requestBody)
    {
        final String        methodName = "unregisterDiscoveryServiceFromEngine";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();


        try
        {
            DiscoveryConfigurationServerHandler handler = instanceHandler.getDiscoveryConfigurationHandler(serverName);

            handler.unregisterDiscoveryServiceFromEngine(userId, discoveryEngineGUID, discoveryServiceGUID);
        }
        catch (InvalidParameterException  error)
        {
            captureInvalidParameterException(response, error);
        }
        catch (PropertyServerException  error)
        {
            capturePropertyServerException(response, error);
        }
        catch (UserNotAuthorizedException error)
        {
            captureUserNotAuthorizedException(response, error);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /*
     * ==========================
     * Support methods
     * ==========================
     */


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(DiscoveryEngineOMASAPIResponse      response,
                                         OCFCheckedExceptionBase             error,
                                         String                              exceptionClassName,
                                         Map<String, Object>                 exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }

    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(DiscoveryEngineOMASAPIResponse      response,
                                         OCFCheckedExceptionBase             error,
                                         String                              exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(DiscoveryEngineOMASAPIResponse      response,
                                         ODFCheckedExceptionBase             error,
                                         String                              exceptionClassName,
                                         Map<String, Object>                 exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(DiscoveryEngineOMASAPIResponse    response,
                                                  InvalidParameterException         error)
    {
        String  parameterName = error.getParameterName();

        if (parameterName != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("parameterName", parameterName);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName(),  null);
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void capturePropertyServerException(DiscoveryEngineOMASAPIResponse     response,
                                                PropertyServerException            error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }



    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(DiscoveryEngineOMASAPIResponse response,
                                                   UserNotAuthorizedException   error)
    {
        String  userId = error.getUserId();

        if (userId != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("userId", userId);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }
}

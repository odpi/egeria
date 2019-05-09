/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataPlatformSecurity;

import java.util.*;

/**
 * OMAGServerPlatformInstanceMap provides part of the mapping for inbound REST requests to the appropriate
 * service instances for the requested server.  It manages the server name to server instance mapping.
 * The map is maintained in a static so it is scoped to the class loader.
 *
 * Instances of this class call the synchronized static methods to work with the map.
 */
public class OMAGServerPlatformInstanceMap
{
    private static Map<String, OMAGServerInstance> serverInstanceMap        = new HashMap<>();
    private static OpenMetadataPlatformSecurity    platformSecurityVerifier = null;

    /**
     * Add a new service instance to the server map.
     *
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     * @param instance instance object
     */
    private static synchronized void  setInstanceForPlatform(String                    serverName,
                                                             String                    serviceName,
                                                             OMAGServerServiceInstance instance)
    {
        /*
         * Is this the first service for this server?
         */
        OMAGServerInstance  serverInstance = serverInstanceMap.get(serverName);

        if (serverInstance == null)
        {
            /*
             * Creating the server instance automatically registers it in the serverInstanceMap
             */
            serverInstance = new OMAGServerInstance(serverName);
        }

        serverInstance.registerService(serviceName, instance);
        serverInstanceMap.put(serverName, serverInstance);
    }


    /**
     * Return the instance of this service for this server.
     *
     * @param userId calling user or null if it is an anonymous request
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     * @param methodName calling method
     *
     * @return OMAGServerServiceInstance object
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not know - indicating a logic error
     */
    private static synchronized OMAGServerServiceInstance getInstanceForPlatform(String  userId,
                                                                                 String  serverName,
                                                                                 String  serviceName,
                                                                                 String  methodName) throws InvalidParameterException,
                                                                                                            UserNotAuthorizedException,
                                                                                                            PropertyServerException
    {

        OMAGServerInstance  serverInstance = serverInstanceMap.get(serverName);

        if (platformSecurityVerifier != null)
        {
            platformSecurityVerifier.validateUserForPlatform(userId);
        }

        if (serverInstance != null)
        {
            return serverInstance.getRegisteredService(userId, serviceName, methodName);
        }
        else
        {
            handleBadServerName(userId, serverName, methodName);
        }

        return null;
    }


    /**
     * Return the list of OMAG Servers running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized List<String> getActiveServerListForPlatform(String userId) throws UserNotAuthorizedException
    {
        if (platformSecurityVerifier != null)
        {
            platformSecurityVerifier.validateUserForPlatform(userId);
        }

        if (platformSecurityVerifier != null)
        {
            platformSecurityVerifier.validateUserAsInvestigatorForPlatform(userId);
        }

        Set<String>  activeServerSet = serverInstanceMap.keySet();

        if (activeServerSet.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(activeServerSet);
        }
    }


    /**
     * Return the list of services running in an OMAG Server that is running on this OMAG Server Platform.
     *
     * @param serverName name of the server
     * @return list on OMAG Services or null if the server is not
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    private static synchronized List<String>   getActiveServiceListForServerOnPlatform(String userId,
                                                                                       String serverName) throws InvalidParameterException,
                                                                                                                 UserNotAuthorizedException
    {
        final String  methodName = "getActiveServiceListForServerOnPlatform";

        if (platformSecurityVerifier != null)
        {
            platformSecurityVerifier.validateUserForPlatform(userId);
        }

        OMAGServerInstance  serverInstance = serverInstanceMap.get(serverName);

        if (serverInstance != null)
        {
            return serverInstance.getRegisteredServices();
        }
        else
        {
            handleBadServerName(userId, serverName, methodName);
        }

        return null;
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     */
    private static synchronized void removeInstanceForPlatform(String   serverName,
                                                               String   serviceName)
    {
        OMAGServerInstance  serverInstance = serverInstanceMap.get(serverName);

        if (serverInstance != null)
        {
             serverInstance.unRegisterService(serviceName);
        }
    }


    /**
     * Throw a standard exception for when the server name is not known.
     *
     * @param userId calling user
     * @param serverName name of the unknown server
     * @param methodName calling method
     * @throws InvalidParameterException requested exception
     */
    private static void handleBadServerName(String    userId,
                                            String    serverName,
                                            String    methodName) throws InvalidParameterException
    {
        OMAGServerInstanceErrorCode errorCode    = OMAGServerInstanceErrorCode.SERVER_NOT_AVAILABLE;
        String                      errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, userId);
        Map<String, Object>         debugProperties = new HashMap<>();

        final String  serverNameProperty = "serverName";
        debugProperties.put(serverNameProperty, serverName);

        throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                            OMAGServerPlatformInstanceMap.class.getName(),
                                            methodName,
                                            errorMessage,
                                            errorCode.getSystemAction(),
                                            errorCode.getUserAction(),
                                            serverNameProperty,
                                            debugProperties);
    }



    /**
     * Constructor for instances - used by service instances to get access to the platform map
     */
    public OMAGServerPlatformInstanceMap()
    {
    }


    /**
     * Add a new service instance to the server map.
     *
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     * @param instance instance object
     */
    void  addServiceInstanceToPlatform(String                    serverName,
                                       String                    serviceName,
                                       OMAGServerServiceInstance instance)
    {
        OMAGServerPlatformInstanceMap.setInstanceForPlatform(serverName, serviceName, instance);
    }


    /**
     * Return the instance for this server.
     *
     * @param userId calling user
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     *
     * @return OMAGServerServiceInstance object
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     * @throws PropertyServerException the service name is not know - indicating a logic error
     */
    OMAGServerServiceInstance getServiceInstance(String    userId,
                                                 String    serverName,
                                                 String    serviceName) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException
    {
        final String methodName = "getServiceInstance";

        return OMAGServerPlatformInstanceMap.getInstanceForPlatform(userId, serverName, serviceName, methodName);
    }


    /**
     * Remove the instance for this server.
     *
     * @param serverName name of the server
     * @param serviceName name of the service running on the server
     */
    void removeServiceInstanceFromPlatform(String   serverName,
                                           String   serviceName)
    {
        OMAGServerPlatformInstanceMap.removeInstanceForPlatform(serverName, serviceName);
    }


    /**
     * Return the list of OMAG Servers running in this OMAG Server Platform.
     *
     * @param userId calling user
     * @return list of OMAG server names
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public List<String>   getActiveServerList(String userId) throws UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getActiveServerListForPlatform(userId);
    }


    /**
     * Return the list of services running in an OMAG Server that is running on this OMAG Server Platform.
     *
     * @param userId calling user
     * @param serverName name of the server
     * @return list on OMAG Services
     * @throws InvalidParameterException the server name is not known
     * @throws UserNotAuthorizedException the user is not authorized to issue the request.
     */
    public List<String>   getActiveServiceListForServer(String  userId,
                                                        String  serverName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException
    {
        return OMAGServerPlatformInstanceMap.getActiveServiceListForServerOnPlatform(userId, serverName);
    }
}

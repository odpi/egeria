/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataServiceSecurity;

import java.util.*;

/**
 * OMAGServerInstance represents an instance of a service in an OMAG Server.
 * It is also responsible for registering itself in the instance map.
 */
public class OMAGServerInstance
{
    private String                                 serverName;
    private Map<String, OMAGServerServiceInstance> serviceInstanceMap = new HashMap<>();
    private Date                                   serverStartTime    = new Date();
    private Date                                   serverEndTime      = null;
    private OpenMetadataServiceSecurity            securityValidator  = null;


    /**
     * Default constructor
     */
    OMAGServerInstance(String   serverName)
    {
        this.serverName = serverName;
    }


    /**
     * Set up the security validator for this server
     *
     * @param securityValidator pluggable authentication validator
     */
    void setServiceSecurityValidator(OpenMetadataServiceSecurity   securityValidator)
    {
        this.securityValidator = securityValidator;
    }


    /**
     * Return the server name.
     *
     * @return  name of the server for this instance
     */
    String getServerName()
    {
        return serverName;
    }


    /**
     * Return the list of services registered for this server.
     *
     * @return list of service names
     */
    synchronized List<String>  getRegisteredServices()
    {
        Set<String>  keySet = serviceInstanceMap.keySet();

        if (keySet.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(keySet);
        }
    }


    /**
     * Return the time this server instance started.
     *
     * @return start time
     */
    public Date getServerStartTime()
    {
        return serverStartTime;
    }


    /**
     * Return the time this server instance ended (or null if it is still running).
     *
     * @return end time or null
     */
    public Date getServerEndTime()
    {
        return serverEndTime;
    }


    /**
     * Register a new service - this normally happens at server start up.
     *
     * @param serviceName name of service
     * @param serviceInstance properties used to run the service
     */
    synchronized  void registerService(String                    serviceName,
                                       OMAGServerServiceInstance serviceInstance)
    {
        serviceInstanceMap.put(serviceName, serviceInstance);
    }


    /**
     * Return the properties for this running service or exceptions if there are problems.
     *
     * @param userId calling user
     * @param serviceName server name
     * @param methodName calling method
     *
     * @return instance object with runtime properties for the service
     * @throws UserNotAuthorizedException calling user not authorized to call the request
     * @throws PropertyServerException service is not running in this server
     */
    synchronized OMAGServerServiceInstance getRegisteredService(String    userId,
                                                                String    serviceName,
                                                                String    methodName) throws UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        if (securityValidator != null)
        {
            securityValidator.validateUserForService(userId, serviceName);
        }

        OMAGServerServiceInstance serverServiceInstance = serviceInstanceMap.get(serviceName);

        if (serverServiceInstance == null)
        {
            OMAGServerInstanceErrorCode errorCode    = OMAGServerInstanceErrorCode.SERVICE_NOT_AVAILABLE;
            String          errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serviceName,
                                                                                                              serverName,
                                                                                                              userId);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());
        }

        return serverServiceInstance;
    }

    /**
     * Remove the service from the active map - this normally happens during server shutdown.
     *
     * @param serviceName name of service to unregister
     */
    synchronized  void unRegisterService(String   serviceName)
    {
        serviceInstanceMap.remove(serviceName);
    }


    /**
     * The server is being shutdown
     */
    synchronized void shutdown()
    {
        serviceInstanceMap = new HashMap<>();
        this.serverEndTime = new Date();
    }
}

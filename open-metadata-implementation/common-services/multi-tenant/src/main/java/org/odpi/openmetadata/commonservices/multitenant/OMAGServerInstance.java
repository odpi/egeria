/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.OpenMetadataPlatformSecurityVerifier;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.platformservices.properties.OMAGServerInstanceHistory;

import java.util.*;

/**
 * OMAGServerInstance represents an instance of a service in an OMAG Server.
 * It is also responsible for registering itself in the instance map.
 */
class OMAGServerInstance
{
    private String                                 serverName;
    private List<OMAGServerInstanceHistory>        serverHistory      = new ArrayList<>();
    private Map<String, OMAGServerServiceInstance> serviceInstanceMap = new HashMap<>();
    private Date                                   serverStartTime    = new Date();
    private Date                                   serverEndTime      = null;
    private OpenMetadataServerSecurityConnector    securityValidator  = null;


    /**
     * Only constructor - server name is always set
     */
    OMAGServerInstance(String   serverName)
    {
        this.serverName = serverName;
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
     * Prepare to start a new instance
     */
    void initialize()
    {
        serviceInstanceMap = new HashMap<>();
        serverStartTime    = new Date();
        serverEndTime      = null;
        securityValidator  = null;
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
     * Return the time this server instance last started.
     *
     * @return start time
     */
    synchronized Date getServerStartTime()
    {
        return serverStartTime;
    }


    /**
     * Return the time this server instance last ended (or null if it is still running).
     *
     * @return end time or null
     */
    synchronized Date getServerEndTime()
    {
        return serverEndTime;
    }


    /**
     * Return the time this server instance last started.
     *
     * @return start time
     */
    synchronized List<OMAGServerInstanceHistory> getServerHistory()
    {
        if (serverHistory.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(serverHistory);
        }
    }


    /**
     * Register an open metadata server security connector to verify access to the server's services.
     *
     * @param connection properties used to create the connector
     * @throws InvalidParameterException the connection is invalid
     */
    synchronized  void registerSecurityValidator(Connection connection) throws InvalidParameterException
    {
        try
        {
            this.securityValidator = OpenMetadataPlatformSecurityVerifier.getServerSecurityConnector(serverName, connection);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException error)
        {
            throw new InvalidParameterException(error);
        }
    }


    /**
     * Return the security verifier for the server.
     *
     * @return connector
     */
    synchronized OpenMetadataServerSecurityConnector  getSecurityVerifier()
    {
        return securityValidator;
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
     * @param serviceOperationName calling method (should be top-level method name)
     *
     * @return instance object with runtime properties for the service
     * @throws UserNotAuthorizedException calling user not authorized to call the request
     * @throws PropertyServerException service is not running in this server
     */
    synchronized OMAGServerServiceInstance getRegisteredService(String    userId,
                                                                String    serviceName,
                                                                String    serviceOperationName) throws UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        if (securityValidator != null)
        {
            try
            {
                securityValidator.validateUserForService(userId, serviceName);
                securityValidator.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
            }
            catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
            {
                throw new UserNotAuthorizedException(error);
            }
        }

        OMAGServerServiceInstance serverServiceInstance = serviceInstanceMap.get(serviceName);

        if (serverServiceInstance == null)
        {
            OMAGServerInstanceErrorCode errorCode    = OMAGServerInstanceErrorCode.SERVICE_NOT_AVAILABLE;
            String                      errorMessage = errorCode.getErrorMessageId()
                                                     + errorCode.getFormattedErrorMessage(serviceName,
                                                                                          serverName,
                                                                                          userId);

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              serviceOperationName,
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
     * The server is being shutdown.  No services should be active at this time
     */
    synchronized void shutdown(String  methodName) throws PropertyServerException
    {
        if (serviceInstanceMap.isEmpty())
        {
            this.serverEndTime = new Date();
            this.serverHistory.add(new OMAGServerInstanceHistory(this.serverStartTime, this.serverEndTime));
        }
        else
        {
            OMAGServerInstanceErrorCode errorCode    = OMAGServerInstanceErrorCode.SERVICES_NOT_SHUTDOWN;
            String                      errorMessage = errorCode.getErrorMessageId()
                                                     + errorCode.getFormattedErrorMessage(serverName,
                                                                                          serviceInstanceMap.keySet().toString());

            throw new PropertyServerException(errorCode.getHTTPErrorCode(),
                                              this.getClass().getName(),
                                              methodName,
                                              errorMessage,
                                              errorCode.getSystemAction(),
                                              errorCode.getUserAction());        }
    }
}

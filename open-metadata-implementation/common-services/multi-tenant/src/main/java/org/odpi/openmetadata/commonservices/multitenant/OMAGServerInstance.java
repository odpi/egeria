/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.platformservices.properties.OMAGServerInstanceHistory;

import java.util.*;

/**
 * OMAGServerInstance represents an instance of a service in an OMAG Server.
 * It is also responsible for registering itself in the instance map.
 */
class OMAGServerInstance
{
    private final String                             serverName;
    private String                                   serverType;
    private final List<OMAGServerInstanceHistory>    serverHistory      = new ArrayList<>();
    private Map<String, OMAGServerServiceInstance>   serviceInstanceMap = new HashMap<>();
    private Date                                     serverStartTime    = new Date();
    private final OpenMetadataServerSecurityVerifier securityVerifier   = new OpenMetadataServerSecurityVerifier();


    /**
     * Only constructor - server name is always set
     *
     * @param serverName active server name
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
     * Set up the server type.
     *
     * @param serverType   Type of the server for this instance
     */
    void setServerType(String serverType)
    {
        this.serverType = serverType;
    }


    /**
     * Return the server type.
     *
     * @return  Type of the server for this instance
     */
    String getServerType()
    {
        return serverType;
    }


    /**
     * Prepare to start a new instance
     */
    void initialize()
    {
        serverStartTime    = new Date();
    }


    /**
     * Return the list of services configured for this server.
     *
     * @return list of service names
     */
    synchronized List<String> getConfiguredServices()
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
        if (serverStartTime == null)
        {
            return serverHistory.get(serverHistory.size() - 1).getEndTime();
        }

        return null;
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
     * @param localServerUserId local server's userId
     * @param auditLog logging destination
     * @param connection properties used to create the connector
     *
     * @return OpenMetadataServerSecurityVerifier object
     *
     * @throws InvalidParameterException the connection is invalid
     */
    synchronized  OpenMetadataServerSecurityVerifier registerSecurityValidator(String     localServerUserId,
                                                                               AuditLog   auditLog,
                                                                               Connection connection) throws InvalidParameterException
    {
        try
        {
            this.securityVerifier.registerSecurityValidator(localServerUserId,
                                                            serverName,
                                                            auditLog,
                                                            connection);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException  error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }

        return this.securityVerifier;
    }


    /**
     * Return the security verifier for the server.
     *
     * @return connector
     */
    synchronized OpenMetadataServerSecurityVerifier  getSecurityVerifier()
    {
        return securityVerifier;
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
        if (serviceInstance != null)
        {
            serviceInstanceMap.put(serviceName, serviceInstance);
            serviceInstance.setSecurityVerifier(securityVerifier);
        }
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
        try
        {
            securityVerifier.validateUserForService(userId, serviceName);
            securityVerifier.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
        }
        catch (org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException  error)
        {
            throw new UserNotAuthorizedException(error);
        }

        OMAGServerServiceInstance serverServiceInstance = serviceInstanceMap.get(serviceName);

        if (serverServiceInstance == null)
        {
            throw new PropertyServerException(OMAGServerInstanceErrorCode.SERVICE_NOT_AVAILABLE.getMessageDefinition(serviceName,
                                                                                                                     serverName,
                                                                                                                     userId),
                                              this.getClass().getName(),
                                              serviceOperationName);
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
     * The server is being shutdown.  No services should be active at this time.
     *
     * @param methodName calling method
     * @throws PropertyServerException residual services left in the service map
     */
    synchronized void shutdown(String  methodName) throws PropertyServerException
    {
        this.serverHistory.add(new OMAGServerInstanceHistory(this.serverStartTime, new Date()));
        this.serverStartTime = null;

        if (!serviceInstanceMap.isEmpty())
        {
            this.serviceInstanceMap = new HashMap<>();
            throw new PropertyServerException(OMAGServerInstanceErrorCode.SERVICES_NOT_SHUTDOWN.getMessageDefinition(serverName,
                                                                                                                     serviceInstanceMap.keySet().toString()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }
}

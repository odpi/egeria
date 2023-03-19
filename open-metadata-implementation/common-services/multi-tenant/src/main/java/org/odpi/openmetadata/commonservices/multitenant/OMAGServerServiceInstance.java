/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.OMAGServerInstanceErrorCode;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.platformservices.properties.ServerInstanceStatus;

/**
 * OMAGServerServiceInstance represents an instance of a service in an OMAG Server.
 * It is also responsible for registering itself in the instance map.
 */
public abstract class OMAGServerServiceInstance
{
    protected String               serverName;
    protected String               serviceName;
    private   ServerInstanceStatus serviceInstanceStatus = ServerInstanceStatus.UNKNOWN;
    protected int                  maxPageSize;

    protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    private final  OMAGServerPlatformInstanceMap platformInstanceMap = new OMAGServerPlatformInstanceMap();

    protected OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();


    /**
     * Default constructor
     *
     * @param serverName name of the new server
     * @param serverType type for new server
     * @param serviceName name of the new service instance
     * @param maxPageSize maximum number of results that can be returned on a single call.
     */
    public OMAGServerServiceInstance(String   serverName,
                                     String   serverType,
                                     String   serviceName,
                                     int      maxPageSize)
    {
        this.serverName = serverName;
        this.serviceName = serviceName;
        this.maxPageSize = maxPageSize;

        invalidParameterHandler.setMaxPagingSize(maxPageSize);

        if (serverName != null)
        {
            platformInstanceMap.addServiceInstanceToPlatform(serverName, serverType, serviceName, this);
        }
    }


    /**
     * Default constructor
     *
     * @param serverName name of the new server
     * @param serviceName name of the new service instance
     * @param maxPageSize maximum number of results that can be returned on a single call.
     */
    public OMAGServerServiceInstance(String   serverName,
                                     String   serviceName,
                                     int      maxPageSize)
    {
        this(serverName, null, serviceName, maxPageSize);
    }


    /**
     * Set the server name.
     *
     * @param serverName name of this OMAG server
     */
    protected void setServerName(String serverName)
    {
        this.serverName = serverName;

        if (serverName != null)
        {
            platformInstanceMap.addServiceInstanceToPlatform(serverName, null, serviceName, this);
        }
    }


    /**
     * Set up the current status of the service.
     *
     * @param serviceInstanceStatus new status
     */
    public void setServiceInstanceStatus(ServerInstanceStatus serviceInstanceStatus)
    {
        this.serviceInstanceStatus = serviceInstanceStatus;
    }


    /**
     * Return the current status of the service.
     *
     * @return server instance status enum
     */
    public ServerInstanceStatus getServiceInstanceStatus()
    {
        return serviceInstanceStatus;
    }


    /**
     * Set up a new security verifier (the handler runs with a default verifier until this
     * method is called).
     *
     * The security verifier provides authorization checks for access and maintenance
     * changes to open metadata.  Authorization checks are enabled through the
     * OpenMetadataServerSecurityConnector.
     *
     * @param securityVerifier new security verifier
     */
    public void setSecurityVerifier(OpenMetadataServerSecurityVerifier securityVerifier)
    {
        if (securityVerifier != null)
        {
            this.securityVerifier = securityVerifier;
        }
    }



    /**
     * Override the default maximum paging size.
     *
     * @param maxPageSize new value
     */
    public void setMaxPageSize(int maxPageSize)
    {
        this.maxPageSize = maxPageSize;
        invalidParameterHandler.setMaxPagingSize(maxPageSize);
    }


    /**
     * Return the server name.
     *
     * @return serverName name of the server for this instance
     * @throws NewInstanceException a problem occurred during initialization
     */
    public String getServerName() throws NewInstanceException
    {
        final String methodName = "getServerName";

        if (serverName != null)
        {
            return serverName;
        }
        else
        {
            throw new NewInstanceException(OMAGServerInstanceErrorCode.SERVER_NAME_NOT_AVAILABLE.getMessageDefinition(methodName),
                                           this.getClass().getName(),
                                           methodName);
        }
    }


    /**
     * Return this service's official name.
     *
     * @return name
     */
    public String getServiceName()
    {
        return serviceName;
    }


    /**
     * Return the handler for validating parameters.
     *
     * @return invalid parameter handler
     */
    public InvalidParameterHandler getInvalidParameterHandler()
    {
        return invalidParameterHandler;
    }


    /**
     * Unregister this instance from the instance map.
     */
    public void shutdown()
    {
        platformInstanceMap.removeServiceInstanceFromPlatform(serverName, serviceName);
    }
}

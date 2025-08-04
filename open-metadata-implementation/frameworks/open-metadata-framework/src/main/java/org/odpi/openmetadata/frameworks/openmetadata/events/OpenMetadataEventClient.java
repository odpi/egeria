/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.events;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;


/**
 * OpenMetadataEventClient provides the implementation to manage the interaction with the server to
 * set up a listener to support the receipt of inbound events from the Asset Manager OMAS Out Topic.
 */
public abstract class OpenMetadataEventClient implements OpenMetadataEventInterface
{
    protected final String   serviceName;              /* Initialized in constructor */
    protected final String   serverName;               /* Initialized in constructor */
    protected final String   serverPlatformURLRoot;    /* Initialized in constructor */
    protected final String   userId;                   /* Initialized in constructor */
    protected final String   password;                 /* Initialized in constructor */
    protected final AuditLog auditLog;                 /* Initialized in constructor */
    protected final String   callerId;                 /* Initialized in constructor */
    protected final int      maxPageSize;              /* Initialized in constructor */


    /**
     * Create a new client that is to be used to exchange events.
     *
     * @param serverName name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param serverUserId this server's userId
     * @param serverPassword this server's userId
     * @param maxPageSize pre-initialized parameter limit
     * @param auditLog logging destination
     * @param callerId unique identifier of the caller
     */
    public OpenMetadataEventClient(String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   serverUserId,
                                   String   serverPassword,
                                   String   serviceName,
                                   int      maxPageSize,
                                   AuditLog auditLog,
                                   String   callerId)
    {
        this.serverName            = serverName;
        this.serverPlatformURLRoot = serverPlatformURLRoot;
        this.userId                = serverUserId;
        this.password              = serverPassword;
        this.serviceName           = serviceName;
        this.maxPageSize           = maxPageSize;
        this.auditLog              = auditLog;
        this.callerId              = callerId;
    }


    /**
     * Return the name of the server where configuration is supposed to be stored.
     *
     * @return server name
     */
    public String getConfigurationServerName()
    {
        return serverName;
    }


    /**
     * Register a listener object that will be passed each of the events published by
     * the Asset Manager OMAS.
     *
     * @param userId calling user
     * @param listener listener object
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws PropertyServerException there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @Override
    public abstract void registerListener(String                    userId,
                                          OpenMetadataEventListener listener) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;
}

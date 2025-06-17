/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.communityprofile.client;


import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ContactDetailsHandler;


/**
 * ContactMethodManagement is the client for explicitly managing contact details attached to an actor profile.
 */
public class ContactMethodManagement extends ContactDetailsHandler
{
    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param localServerName       name of this server (view server)
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param auditLog              logging destination
     * @param maxPageSize           maximum number of results supported by this server
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ContactMethodManagement(String   localServerName,
                                   String   serverName,
                                   String   serverPlatformURLRoot,
                                   AuditLog auditLog,
                                   int      maxPageSize) throws InvalidParameterException
    {
        super(localServerName,
              serverName,
              serverPlatformURLRoot,
              auditLog,
              AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceURLMarker(),
              AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
              maxPageSize);
    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName            name of the server to connect to
     * @param serverPlatformURLRoot the network address of the server running the OMAS REST services
     * @param userId                caller's userId embedded in all HTTP requests
     * @param password              caller's userId embedded in all HTTP requests
     * @param maxPageSize           maximum number of results supported by this server
     * @param auditLog              logging destination
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     *                                   REST API calls.
     */
    public ContactMethodManagement(String   localServerName,
                                   String   serverName,
                                   String   serverPlatformURLRoot,
                                   String   userId,
                                   String   password,
                                   AuditLog auditLog,
                                   int      maxPageSize) throws InvalidParameterException
    {
        super(localServerName,
              serverName,
              serverPlatformURLRoot,
              userId,
              password,
              auditLog,
              AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceURLMarker(),
              AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
              maxPageSize);
    }
}

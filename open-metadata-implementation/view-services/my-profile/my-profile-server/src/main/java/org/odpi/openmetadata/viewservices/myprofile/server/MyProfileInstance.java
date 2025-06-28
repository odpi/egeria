/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.adminservices.configuration.registration.AccessServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ActorProfileHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.handlers.ToDoActionHandler;

/**
 * MyProfileInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class MyProfileInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.MY_PROFILE;

    private final OrganizationManagement organizationManagement;
    private final ToDoActionHandler      toDoActionHandler;


    /**
     * Set up theMy Profile OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public MyProfileInstance(String       serverName,
                             AuditLog     auditLog,
                             String       localServerUserId,
                             String       localServerUserPassword,
                             int          maxPageSize,
                             String       remoteServerName,
                             String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            organizationManagement = new OrganizationManagement(remoteServerName, remoteServerURL, auditLog, maxPageSize);
            toDoActionHandler      = new ToDoActionHandler(serverName,
                                                           remoteServerName,
                                                           remoteServerURL,
                                                           auditLog,
                                                           AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceURLMarker(),
                                                           AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                           maxPageSize);
        }
        else
        {
            organizationManagement = new OrganizationManagement(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, auditLog, maxPageSize);
            toDoActionHandler      = new ToDoActionHandler(serverName,
                                                           remoteServerName,
                                                           remoteServerURL,
                                                           localServerUserId,
                                                           localServerUserPassword,
                                                           auditLog,
                                                           AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceURLMarker(),
                                                           AccessServiceDescription.COMMUNITY_PROFILE_OMAS.getAccessServiceFullName(),
                                                           maxPageSize);
        }
    }


    /**
     * Return the organization management client.  This client is from Community Profile OMAS and is for maintaining information .
     *
     * @return client
     */
    public OrganizationManagement getOrganizationManagement()
    {
        return organizationManagement;
    }


    /**
     * Return the to do management client.  This client is from Community Profile OMAS and is for maintaining information .
     *
     * @return client
     */
    public ToDoActionHandler getToDoActionManagement()
    {
        return toDoActionHandler;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.myprofile.server;

import org.odpi.openmetadata.accessservices.communityprofile.client.OrganizationManagement;
import org.odpi.openmetadata.accessservices.communityprofile.client.ToDoActionManagement;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * MyProfileInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class MyProfileInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.MY_PROFILE;

    private final OrganizationManagement   organizationManagement;
    private final ToDoActionManagement     toDoActionManagement;


    /**
     * Set up theMy Profile OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public MyProfileInstance(String       serverName,
                             AuditLog     auditLog,
                             String       localServerUserId,
                             int          maxPageSize,
                             String       remoteServerName,
                             String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        organizationManagement = new OrganizationManagement(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        toDoActionManagement   = new ToDoActionManagement(remoteServerName, remoteServerURL, auditLog, maxPageSize);
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
    public ToDoActionManagement getToDoActionManagement()
    {
        return toDoActionManagement;
    }
}

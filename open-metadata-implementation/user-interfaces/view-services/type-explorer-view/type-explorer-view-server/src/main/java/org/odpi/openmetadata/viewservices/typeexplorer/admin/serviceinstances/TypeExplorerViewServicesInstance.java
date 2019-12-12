/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.typeexplorer.admin.serviceinstances;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;


/**
 * TypeExplorerViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class TypeExplorerViewServicesInstance extends OMVSServiceInstance
{

    /**
     * Set up the objects for the type explorer view service
     * @param serverName name of the server
     * @param auditLog audit log
     * @param localServerUserId local server userId
     * @param maxPageSize maximum page size, for use with paging requests
     * @param metadataServerName  metadata server name
     * @param metadataServerURL metadata server URL
     */
    public TypeExplorerViewServicesInstance(String serverName,
                                           OMRSAuditLog auditLog,
                                           String       localServerUserId,
                                           int          maxPageSize,
                                           String       metadataServerName,
                                           String       metadataServerURL)
    {
        super(serverName, ViewServiceDescription.TYPE_EXPLORER.getViewServiceName(), auditLog, localServerUserId, maxPageSize, metadataServerName,
                metadataServerURL);
    }

    public String getViewServiceName()
    {
        return ViewServiceDescription.TYPE_EXPLORER.getViewServiceName();
    }
}

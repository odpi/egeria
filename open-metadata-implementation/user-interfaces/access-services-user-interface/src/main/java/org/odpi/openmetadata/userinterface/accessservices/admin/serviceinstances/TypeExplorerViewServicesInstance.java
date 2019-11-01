/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.admin.serviceinstances;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * TypeExplorerViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class TypeExplorerViewServicesInstance extends OMVSServiceInstance
{
    private static final Logger log = LoggerFactory.getLogger(TypeExplorerViewServicesInstance.class);

    private static final String className = TypeExplorerViewServicesInstance.class.getName();

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
        super(serverName, auditLog, localServerUserId, maxPageSize, metadataServerName,
                metadataServerURL);
    }

    public String getViewServiceName()
    {
        return ViewServiceDescription.TYPE_EXPLORER.getViewServiceName();
    }
}

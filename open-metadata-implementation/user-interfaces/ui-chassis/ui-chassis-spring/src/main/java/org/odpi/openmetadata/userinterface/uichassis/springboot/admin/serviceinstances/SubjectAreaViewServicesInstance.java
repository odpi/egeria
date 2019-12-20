/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.admin.serviceinstances;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;


/**
 * SubjectAreaViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class SubjectAreaViewServicesInstance extends OMVSServiceInstance
{
    /**
     * Set up the objects for the subject area view service
     * @param serverName name of the server
     * @param auditLog audit log
     * @param localServerUserId local server userId
     * @param maxPageSize maximum page size, for use with paging requests
     * @param metadataServerName  metadata server name
     * @param metadataServerURL metadata server URL
     */
    public SubjectAreaViewServicesInstance(String serverName,
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
        return ViewServiceDescription.SUBJECT_AREA.getViewServiceName();
    }


}

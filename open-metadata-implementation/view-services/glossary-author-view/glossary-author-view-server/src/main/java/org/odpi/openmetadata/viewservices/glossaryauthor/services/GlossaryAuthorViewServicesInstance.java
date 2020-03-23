/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryauthor.services;

import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;


/**
 * GlossaryAuthorViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GlossaryAuthorViewServicesInstance extends OMVSServiceInstance
{
    /**
     * Set up the objects for the Glossary Author view service
     * @param serverName name of the server
     * @param auditLog audit log
     * @param localServerUserId local server userId
     * @param maxPageSize maximum page size, for use with paging requests
     * @param metadataServerName  metadata server name
     * @param metadataServerURL metadata server URL
     */
    public GlossaryAuthorViewServicesInstance(String serverName,
                                              OMRSAuditLog auditLog,
                                              String       localServerUserId,
                                              int          maxPageSize,
                                              String       metadataServerName,
                                              String       metadataServerURL)
    {
        super(serverName, ViewServiceDescription.GLOSSARY_AUTHOR.getViewServiceName(), auditLog, localServerUserId, maxPageSize, metadataServerName,
              metadataServerURL);
    }

}

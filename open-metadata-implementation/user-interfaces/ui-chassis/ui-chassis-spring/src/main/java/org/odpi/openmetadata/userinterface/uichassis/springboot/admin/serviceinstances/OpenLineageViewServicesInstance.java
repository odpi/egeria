/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.admin.serviceinstances;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * OpenLineageViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class OpenLineageViewServicesInstance extends OMVSServiceInstance
{
    private static final Logger log = LoggerFactory.getLogger(OpenLineageViewServicesInstance.class);

    private static final String className = OpenLineageViewServicesInstance.class.getName();
    private String        lineageServerName = null;
    private String        lineageServerURL =null;
    /**
     * Set up the objects for the open lineage view service
     * @param serverName name of the server
     * @param auditLog audit log
     * @param localServerUserId local server userId
     * @param maxPageSize maximum page size, for use with paging requests
     * @param metadataServerName  metadata server name
     * @param metadataServerURL metadata server URL
     * @param lineageServerName lineage server name
     * @param lineageServerURL lineage server URL
     */
    public OpenLineageViewServicesInstance(String serverName,
                                           OMRSAuditLog auditLog,
                                           String       localServerUserId,
                                           int          maxPageSize,
                                           String       metadataServerName,
                                           String       metadataServerURL,
                                           String       lineageServerName,
                                           String       lineageServerURL)
    {
        super(serverName, auditLog, localServerUserId, maxPageSize, metadataServerName,
                metadataServerURL);
        this.lineageServerName = lineageServerName;
        this.lineageServerURL = lineageServerURL;

    }

    /**
     * Return the lineage server name
     * @return String lineage server name
     */
    public String getLineageServerName() {
        return lineageServerName;
    }

    /**
     * Set up[ the lineage server name
     * @param lineageServerName String lineage server name
     */
    public void setLineageServerName(String lineageServerName) {
        this.lineageServerName = lineageServerName;
    }

    /**
     * Return the lineage server URL
     * @return the lineage server URL
     */
    public String getLineageServerURL() {
        return lineageServerURL;
    }

    /**
     * Set the Lineage server URL
     * @param lineageServerURL lineage server URL
     */
    public void setLineageServerURL(String lineageServerURL) {
        this.lineageServerURL = lineageServerURL;
    }

    public String getViewServiceName()
    {
        return ViewServiceDescription.OPEN_LINEAGE.getViewServiceName();
    }
}

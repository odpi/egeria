/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * Represents an instance of an Open Metadata Engine Service (OMES) running in a specific server.
 * It is responsible for registering itself in the instance map.
 */
public class OMESServiceInstance extends AuditableServerServiceInstance
{
    protected String remoteServerName;
    protected String remoteServerURLRoot;


    /**
     * Set up the OMES service instance
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURLRoot remote server URL
     */
    public OMESServiceInstance(String   serverName,
                               String   serviceName,
                               AuditLog auditLog,
                               String   localServerUserId,
                               int      maxPageSize,
                               String   remoteServerName,
                               String   remoteServerURLRoot)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);

        this.remoteServerName    = remoteServerName;
        this.remoteServerURLRoot = remoteServerURLRoot;

        this.setServerName(serverName);
    }
    

    /**
     * Return the remote server name
     *
     * @return string server name
     */
    public String getRemoteServerName() {
        return remoteServerName;
    }


    /**
     * the remote server URL
     * @return string server URL
     */
    public String getRemoteServerURLRoot() {
        return remoteServerURLRoot;
    }
}

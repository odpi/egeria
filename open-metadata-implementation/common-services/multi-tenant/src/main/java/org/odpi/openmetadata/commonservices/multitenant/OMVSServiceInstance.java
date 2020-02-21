/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;

/**
 * It is responsible for registering itself in the instance map.
 */
public class OMVSServiceInstance extends AuditableServerServiceInstance
{
    protected String remoteServerName = null;
    protected String remoteServerURL = null;
    /**
     * Set up the OMVS service instance
     * 
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     */
    public OMVSServiceInstance(String                  serverName,
                               String                  serviceName,
                               OMRSAuditLog            auditLog,
                               String                  localServerUserId,
                               int                     maxPageSize,
                               String                  remoteServerName,
                               String                  remoteServerURL ) {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);
        this.remoteServerName = remoteServerName;
        this.remoteServerURL = remoteServerURL;
        this.setServerName(serverName);
    }
    

    /**
     * the remote server name
     * @return the remote server name
     */
    public String getRemoteServerName() {
        return remoteServerName;
    }

    /**
     * the remote server URL
     * @return the remote server URL
     */
    public String getRemoteServerURL() {
        return remoteServerURL;
    }
}

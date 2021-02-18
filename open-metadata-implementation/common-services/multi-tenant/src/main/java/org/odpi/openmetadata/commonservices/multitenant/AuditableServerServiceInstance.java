/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * AuditableServerServiceInstance caches references to OMRS objects for a specific server.
 * It is also responsible for registering itself in the instance map.
 */
public abstract class AuditableServerServiceInstance extends OMAGServerServiceInstance
{
    protected String   localServerUserId = null;
    protected AuditLog auditLog;


    /**
     * Constructor.
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog link to the repository responsible for servicing the REST calls.
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum number of results returned on a single call
     */
    public AuditableServerServiceInstance(String      serverName,
                                          String      serviceName,
                                          AuditLog    auditLog,
                                          String      localServerUserId,
                                          int         maxPageSize)
    {
        super(serverName, serviceName, maxPageSize);

        this.localServerUserId = localServerUserId;
        this.auditLog = auditLog;
    }


    /**
     * Return the audit log for this access service.
     *
     * @return audit log
     */
    protected AuditLog getAuditLog()
    {
        return auditLog;
    }
}

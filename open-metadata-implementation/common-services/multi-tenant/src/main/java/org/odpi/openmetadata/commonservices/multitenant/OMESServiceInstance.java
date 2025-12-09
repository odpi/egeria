/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.multitenant;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;

/**
 * Represents an instance of an Open Metadata Engine Service (OMES) running in a specific server.
 * It is responsible for registering itself in the instance map.
 */
public class OMESServiceInstance extends AuditableServerServiceInstance
{
    /**
     * Set up the OMES service instance
     *
     * @param serverName name of this server
     * @param serviceName name of this service
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     */
    public OMESServiceInstance(String   serverName,
                               String   serviceName,
                               AuditLog auditLog,
                               String   localServerUserId,
                               int      maxPageSize)
    {
        super(serverName, serviceName, auditLog, localServerUserId, maxPageSize);

        this.setServerName(serverName);
    }
}

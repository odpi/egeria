/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server;

import org.odpi.openmetadata.accessservices.designmodel.client.DataDesignManager;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * DataDesignerInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class DataDesignerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.DATA_DESIGNER;

    private final DataDesignManager dataDesignManager;



    /**
     * Set up the Data Designer OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public DataDesignerInstance(String       serverName,
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

        dataDesignManager = new DataDesignManager(remoteServerName, remoteServerURL, maxPageSize, auditLog);

    }


    /**
     * Return the solution manager client.  This client is from the Design Model OMAS and is for maintaining
     * data design artifacts.
     *
     * @return client
     */
    public DataDesignManager getDataDesignManagerClient()
    {
        return dataDesignManager;
    }
}

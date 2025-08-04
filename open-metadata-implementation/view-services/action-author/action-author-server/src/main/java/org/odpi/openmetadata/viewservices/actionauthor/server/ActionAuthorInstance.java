/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actionauthor.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworkservices.gaf.client.EgeriaOpenGovernanceClient;

/**
 * ActionAuthorInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ActionAuthorInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ACTION_AUTHOR;

    private final EgeriaOpenGovernanceClient openGovernanceClient;

    /**
     * Set up the Action Author OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public ActionAuthorInstance(String       serverName,
                                AuditLog     auditLog,
                                String       localServerUserId,
                                String       localServerUserPassword,
                                int          maxPageSize,
                                String       remoteServerName,
                                String       remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            openGovernanceClient = new EgeriaOpenGovernanceClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            openGovernanceClient = new EgeriaOpenGovernanceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
        }
    }


    /**
     * Return the open governance client.  This client is from the Governance Action Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public EgeriaOpenGovernanceClient getOpenGovernanceClient()
    {
        return openGovernanceClient;
    }
}

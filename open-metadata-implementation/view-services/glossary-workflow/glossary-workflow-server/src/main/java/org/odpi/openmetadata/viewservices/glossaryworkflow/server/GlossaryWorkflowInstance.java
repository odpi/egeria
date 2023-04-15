/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossaryworkflow.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * GlossaryWorkflowInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GlossaryWorkflowInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.GLOSSARY_WORKFLOW;

    private final GlossaryManagementClient glossaryManagementClient;

    /**
     * Set up the Glossary Workflow OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public GlossaryWorkflowInstance(String       serverName,
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

        glossaryManagementClient = new GlossaryManagementClient(remoteServerName, remoteServerURL, auditLog);
    }


    /**
     * Return the glossary management client.  This client is from Asset Manager OMAS.
     *
     * @return client
     */
    public GlossaryManagementClient getGlossaryManagementClient()
    {
        return glossaryManagementClient;
    }
}

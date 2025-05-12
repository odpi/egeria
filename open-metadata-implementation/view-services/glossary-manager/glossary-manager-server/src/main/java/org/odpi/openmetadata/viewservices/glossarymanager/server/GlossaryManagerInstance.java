/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarymanager.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.CollaborationExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.GlossaryExchangeClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.exchange.StewardshipExchangeClient;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * GlossaryManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GlossaryManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.GLOSSARY_MANAGER;

    private final CollaborationExchangeClient collaborationExchangeClient;
    private final GlossaryExchangeClient      glossaryExchangeClient;
    private final StewardshipExchangeClient   stewardshipExchangeClient;

    /**
     * Set up the Glossary Manager OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public GlossaryManagerInstance(String       serverName,
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

        collaborationExchangeClient = new CollaborationExchangeClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        glossaryExchangeClient = new GlossaryExchangeClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        stewardshipExchangeClient = new StewardshipExchangeClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
    }


    /**
     * Return the collaboration client.  This client is from Asset Manager OMAS and is for maintaining note logs.
     *
     * @return client
     */
    public CollaborationExchangeClient getCollaborationExchangeClient()
    {
        return collaborationExchangeClient;
    }


    /**
     * Return the glossary client.  This client is from Asset Manager OMAS and is for maintaining glossaries and their content.
     *
     * @return client
     */
    public GlossaryExchangeClient getGlossaryExchangeClient()
    {
        return glossaryExchangeClient;
    }


    /**
     * Return the stewardship client.  This client is from Asset Manager OMAS and is for setting up classifications and relationships for
     * a glossary term.
     *
     * @return client
     */
    public StewardshipExchangeClient getStewardshipExchangeClient()
    {
        return stewardshipExchangeClient;
    }
}

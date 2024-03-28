/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;

import org.odpi.openmetadata.accessservices.assetmanager.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.CollaborationManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.GlossaryManagementClient;
import org.odpi.openmetadata.accessservices.assetmanager.client.management.StewardshipManagementClient;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;

/**
 * GlossaryBrowserInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class GlossaryBrowserInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.GLOSSARY_BROWSER;

    private final CollaborationManagementClient collaborationManagementClient;
    private final GlossaryManagementClient      glossaryManagementClient;
    private final StewardshipManagementClient   stewardshipManagementClient;
    private final OpenMetadataStoreClient       openMetadataStoreClient;

    /**
     * Set up the Glossary Browser OMVS instance*
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public GlossaryBrowserInstance(String       serverName,
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

        collaborationManagementClient = new CollaborationManagementClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        glossaryManagementClient = new GlossaryManagementClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        stewardshipManagementClient = new StewardshipManagementClient(remoteServerName, remoteServerURL, auditLog, maxPageSize);
        openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
    }


    /**
     * Return the collaboration management client.  This client is from Asset Manager OMAS and is for maintaining notelogs.
     *
     * @return client
     */
    public CollaborationManagementClient getCollaborationManagementClient()
    {
        return collaborationManagementClient;
    }


    /**
     * Return the glossary management client.  This client is from Asset Manager OMAS and is for maintaining glossaries and their content.
     *
     * @return client
     */
    public GlossaryManagementClient getGlossaryManagementClient()
    {
        return glossaryManagementClient;
    }


    /**
     * Return the stewardship management client.  This client is from Asset Manager OMAS and is for setting up classifications and relationships for
     * a glossary term.
     *
     * @return client
     */
    public StewardshipManagementClient getStewardshipManagementClient()
    {
        return stewardshipManagementClient;
    }


    /**
     * Return the open metadata store client.  This client is from Asset Manager OMAS and is for accessing all types of metadata.
     *
     * @return client
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }
}

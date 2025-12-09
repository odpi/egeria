/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.communitymatters.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CommunityHandler;
import org.odpi.openmetadata.frameworkservices.omf.client.EgeriaOpenMetadataStoreClient;

/**
 * CommunityMattersInstance caches references to the objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class CommunityMattersInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.COMMUNITY_MATTERS;

    private final CommunityHandler communityHandler;


    /**
     * Set up the Community Matters OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param localServerSecretsStoreProvider secrets store connector for bearer token
     * @param localServerSecretsStoreLocation secrets store location for bearer token
     * @param localServerSecretsStoreCollection secrets store collection for bearer token
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public CommunityMattersInstance(String   serverName,
                                    AuditLog auditLog,
                                    String   localServerUserId,
                                    String   localServerSecretsStoreProvider,
                                    String   localServerSecretsStoreLocation,
                                    String   localServerSecretsStoreCollection,
                                    int      maxPageSize,
                                    String   remoteServerName,
                                    String   remoteServerURL) throws InvalidParameterException
    {
        super(serverName,
              myDescription.getViewServiceFullName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        OpenMetadataClient openMetadataClient = new EgeriaOpenMetadataStoreClient(remoteServerName,
                                                                                  remoteServerURL,
                                                                                  localServerSecretsStoreProvider,
                                                                                  localServerSecretsStoreLocation,
                                                                                  localServerSecretsStoreCollection,
                                                                                  maxPageSize,
                                                                                  auditLog);

        communityHandler = new CommunityHandler(serverName,
                                                auditLog,
                                                myDescription.getViewServiceFullName(),
                                                openMetadataClient);
    }



    /**
     * Return the open metadata handler.
     *
     * @return client
     */
    public CommunityHandler getCommunityHandler()
    {
        return communityHandler;
    }
}

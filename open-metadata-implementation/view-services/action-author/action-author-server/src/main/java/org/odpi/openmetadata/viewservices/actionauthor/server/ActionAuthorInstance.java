/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.actionauthor.server;


import org.odpi.openmetadata.accessservices.governanceprogram.client.*;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * ActionAuthorInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ActionAuthorInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.ACTION_AUTHOR;

    private final ExternalReferenceManager     externalReferenceManager;
    private final OpenMetadataStoreClient      openMetadataStoreClient;
    private final OpenIntegrationServiceClient openIntegrationServiceClient;
    private final ConnectedAssetClient         connectedAssetClient;
    private final OpenGovernanceClient         openGovernanceClient;

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
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              localServerUserPassword,
              maxPageSize,
              remoteServerName,
              remoteServerURL);

        if (localServerUserPassword == null)
        {
            externalReferenceManager     = new ExternalReferenceManager(remoteServerName, remoteServerURL);
            connectedAssetClient         = new ConnectedAssetClient(remoteServerName, remoteServerURL);
            openMetadataStoreClient      = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
            openIntegrationServiceClient = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, maxPageSize);
            openGovernanceClient         = new OpenGovernanceClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            externalReferenceManager     = new ExternalReferenceManager(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            connectedAssetClient         = new ConnectedAssetClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            openMetadataStoreClient      = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            openIntegrationServiceClient = new OpenIntegrationServiceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            openGovernanceClient         = new OpenGovernanceClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);

        }
    }



    /**
     * Return the client.  This client is from Asset Owner OMAS and is for maintaining external references for assets.
     *
     * @return client
     */
    public ExternalReferenceManager getExternalReferenceManager()
    {
        return externalReferenceManager;
    }


    /**
     * Return the connected asset client.  This client is from Open Connector Framework (OCF) and is for retrieving information about
     * assets and creating connectors.
     *
     * @return client
     */
    public ConnectedAssetClient getConnectedAssetClient()
    {
        return connectedAssetClient;
    }


    /**
     * Return the open metadata store client.  This client is from the Governance Action Framework (GAF) and is for accessing and
     * maintaining all types of metadata.
     *
     * @return client
     */
    public OpenMetadataStoreClient getOpenMetadataStoreClient()
    {
        return openMetadataStoreClient;
    }


    /**
     * Return the open governance client.  This client is from the Governance Action Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public OpenGovernanceClient getOpenGovernanceClient()
    {
        return openGovernanceClient;
    }


    /**
     * Return the open governance client.  This client is from the Governance Action Framework (GAF) and is for
     * working with automation services.
     *
     * @return client
     */
    public OpenIntegrationServiceClient getOpenIntegrationServiceClient()
    {
        return openIntegrationServiceClient;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.projectmanager.server;

import org.odpi.openmetadata.accessservices.projectmanagement.client.ProjectManagement;
import org.odpi.openmetadata.accessservices.projectmanagement.client.ConnectedAssetClient;
import org.odpi.openmetadata.accessservices.projectmanagement.client.OpenMetadataStoreClient;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;

/**
 * ProjectManagerInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ProjectManagerInstance extends OMVSServiceInstance
{
    private static final ViewServiceDescription myDescription = ViewServiceDescription.PROJECT_MANAGER;

    private final ProjectManagement    projectManagement;
    private final ConnectedAssetClient connectedAssetClient;
    private final OpenMetadataStoreClient openMetadataStoreClient;

    /**
     * Set up the Project Manager OMVS instance
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId user id to use on OMRS calls where there is no end user, or as part of an HTTP authentication mechanism with serverUserPassword.
     * @param localServerUserPassword password to use as part of an HTTP authentication mechanism.
     * @param maxPageSize maximum page size
     * @param remoteServerName  remote server name
     * @param remoteServerURL remote server URL
     * @throws InvalidParameterException problem with server name or platform URL
     */
    public ProjectManagerInstance(String       serverName,
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
            projectManagement       = new ProjectManagement(remoteServerName, remoteServerURL, maxPageSize);
            connectedAssetClient    = new ConnectedAssetClient(remoteServerName, remoteServerURL, maxPageSize);
            openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, maxPageSize);
        }
        else
        {
            projectManagement       = new ProjectManagement(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
            connectedAssetClient    = new ConnectedAssetClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword);
            openMetadataStoreClient = new OpenMetadataStoreClient(remoteServerName, remoteServerURL, localServerUserId, localServerUserPassword, maxPageSize);
        }
    }


    /**
     * Return the project management client.  This client is from Project Management OMAS and is for maintaining projects.
     *
     * @return client
     */
    public ProjectManagement getProjectManagement()
    {
        return projectManagement;
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
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.serverauthor.initialization;

import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.serverauthor.handlers.ServerAuthorViewHandler;

import java.util.List;


/**
 * ServerAuthorViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class ServerAuthorViewServicesInstance extends OMVSServiceInstance
{

    private static ViewServiceDescription myDescription = ViewServiceDescription.SERVER_AUTHOR;

    private ServerAuthorViewHandler serverAuthorViewHandler = null;



    /**
     * Set up the Server Author OMVS instance
     * Unlike the superclass () ServerAuthor does not expect to be passed remoteServerName or remoteServerURL during configuration or initialization.
     * This is because in ServerAuthor these are variable (set per requested operation), so the are passed by the RESTServices methods.
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     * @param metadataServerURL metadata server URL
     * @param resourceEndpoints a list of resource endpoints.
     *
     */
    public ServerAuthorViewServicesInstance(String       serverName,
                                   AuditLog     auditLog,
                                   String       localServerUserId,
                                   int          maxPageSize,
                                   String       metadataServerURL,
                                   List<ResourceEndpointConfig> resourceEndpoints)
    {



        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              null,  // remoteServerName is not meaningful for this view service
              metadataServerURL);


        this.serverAuthorViewHandler = new ServerAuthorViewHandler(localServerUserId, metadataServerURL, resourceEndpoints);
    }

    /**
     * Return the handler for Server Author view requests
     *
     * @return handler object
     */
    public ServerAuthorViewHandler getServerAuthorViewHandler()
    {
        return serverAuthorViewHandler;
    }
}

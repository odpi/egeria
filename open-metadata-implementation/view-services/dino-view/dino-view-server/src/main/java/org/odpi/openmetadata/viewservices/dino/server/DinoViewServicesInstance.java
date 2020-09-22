/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.dino.server;

import org.odpi.openmetadata.adminservices.configuration.properties.ResourceEndpointConfig;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.dino.handlers.DinoViewHandler;
import org.odpi.openmetadata.viewservices.dino.api.properties.ResourceEndpoint;

import java.util.List;


/**
 * DinoViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class DinoViewServicesInstance extends OMVSServiceInstance
{

    private static ViewServiceDescription myDescription = ViewServiceDescription.DINO;

    private DinoViewHandler dinoViewHandler = null;



    /**
     * Set up the Dino OMVS instance
     * Unlike the superclass () Dino does not expect to be passed remoteServerName or remoteServerURL during configuration or initialization.
     * This is because in Dino these are variable (set per requested operation), so the are passed by the RESTServices methods.
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     */
    public DinoViewServicesInstance(String                  serverName,
                                    AuditLog                auditLog,
                                    String                  localServerUserId,
                                    int                     maxPageSize,
                                    List<ResourceEndpointConfig>  resourceEndpoints)
    {



        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              null,  // see comment above about remoteServerName
              null);  // .... and remoteServerURL.
        

        this.dinoViewHandler = new DinoViewHandler(resourceEndpoints);
    }



    /**
     * Return the handler for Dino view requests
     *
     * @return handler object
     */
    DinoViewHandler getDinoViewHandler()
    {
        return dinoViewHandler;
    }



}

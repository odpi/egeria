/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.rex.server;

import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstance;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.multitenant.ffdc.exceptions.NewInstanceException;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.viewservices.rex.handlers.RexViewHandler;


/**
 * RexViewServicesInstance caches references to objects it needs for a specific server.
 * It is also responsible for registering itself in the instance map.
 */

public class RexViewServicesInstance extends OMVSServiceInstance
{

    private static ViewServiceDescription myDescription = ViewServiceDescription.REPOSITORY_EXPLORER;

    private RexViewHandler rexViewHandler = null;


    /**
     * Set up the Rex OMVS instance
     * Unlike the superclass () Rex does not expect to be passed remoteServerName or remoteServerURL during configuration or initialization.
     * This is because in Repository Explorer these are variable (set per requested operation), so the are passed by the RESTServices methods.
     *
     * @param serverName name of this server
     * @param auditLog logging destination
     * @param localServerUserId userId used for server initiated actions
     * @param maxPageSize maximum page size
     */
    public RexViewServicesInstance(String       serverName,
                                   AuditLog     auditLog,
                                   String       localServerUserId,
                                   int          maxPageSize)
    {



        super(serverName,
              myDescription.getViewServiceName(),
              auditLog,
              localServerUserId,
              maxPageSize,
              null,  // see comment above about remoteServerName
              null);  // .... and remoteServerURL.


        this.rexViewHandler = new RexViewHandler();
    }



    /**
     * Return the handler for rex view requests
     *
     * @return handler object
     */
    RexViewHandler getRexViewHandler()
    {
        return rexViewHandler;
    }



}

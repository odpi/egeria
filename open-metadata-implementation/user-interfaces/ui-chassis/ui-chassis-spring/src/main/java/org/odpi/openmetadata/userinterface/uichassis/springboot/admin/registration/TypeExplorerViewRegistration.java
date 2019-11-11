/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.uichassis.springboot.admin.registration;

import org.odpi.openmetadata.userinterface.uichassis.springboot.admin.TypeExplorerViewAdmin;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceOperationalStatus;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceRegistration;

/**
 * TypeExplorerViewRegistration registers the view service with the UI Server administration services.
 * This registration must be driven once at server start up. The UI Server administration services
 * then use this registration information as confirmation that there is an implementation of this
 * view service in the server and it can be configured and used.
 */
public class TypeExplorerViewRegistration
{
    /**
     * Pass information about this view service to the UI Server administration services.
     */
    public static void registerViewService()
    {
        ViewServiceDescription myDescription = ViewServiceDescription.TYPE_EXPLORER;
        ViewServiceRegistration myRegistration = new ViewServiceRegistration(myDescription,
                ViewServiceOperationalStatus.ENABLED,
                TypeExplorerViewAdmin.class.getName());
        OMAGViewServiceRegistration.registerViewService(myRegistration);
    }
}

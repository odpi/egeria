/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.openlineage.admin.registration;

import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceOperationalStatus;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceRegistration;
import org.odpi.openmetadata.userinterface.common.registration.OMAGViewServiceRegistration;
import org.odpi.openmetadata.viewservices.openlineage.admin.OpenLineageViewAdmin;


/**
 * OpenLineageViewRegistration registers the view service with the UI Server administration services.
 * This registration must be driven once at server start up. The UI Server administration services
 * then use this registration information as confirmation that there is an implementation of this
 * view service in the server and it can be configured and used.
 */
public class OpenLineageViewRegistration
{
    /**
     * Pass information about this view service to the UI Server administration services.
     */
    public static void registerViewService()
    {
        ViewServiceDescription myDescription = ViewServiceDescription.OPEN_LINEAGE;
        ViewServiceRegistration myRegistration = new ViewServiceRegistration(myDescription,
                ViewServiceOperationalStatus.ENABLED,
                OpenLineageViewAdmin.class.getName());
        OMAGViewServiceRegistration.registerViewService(myRegistration);
    }
}

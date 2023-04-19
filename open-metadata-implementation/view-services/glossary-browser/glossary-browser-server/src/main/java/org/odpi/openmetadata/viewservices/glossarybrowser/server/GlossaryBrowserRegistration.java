/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.glossarybrowser.server;

import org.odpi.openmetadata.adminservices.registration.OMAGViewServiceRegistration;
import org.odpi.openmetadata.adminservices.configuration.registration.ServiceOperationalStatus;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceRegistrationEntry;
import org.odpi.openmetadata.viewservices.glossarybrowser.admin.GlossaryBrowserAdmin;

/**
 * GlossaryBrowserRegistration registers the view service with the OMAG Server administration services.
 * This registration must be driven once at server start up. The OMAG Server administration services
 * then use this registration information as confirmation that there is an implementation of this
 * view service in the server and it can be configured and used.
 */
public class GlossaryBrowserRegistration
{
    /**
     * Pass information about this view service to the OMAG Server administration services.
     */
    public static void registerViewService()
    {
        ViewServiceDescription myDescription = ViewServiceDescription.GLOSSARY_BROWSER;
        ViewServiceRegistrationEntry myRegistration = new ViewServiceRegistrationEntry(myDescription,
                                                                                       ServiceOperationalStatus.ENABLED,
                                                                                       GlossaryBrowserAdmin.class.getName());
        OMAGViewServiceRegistration.registerViewService(myRegistration);
    }
}

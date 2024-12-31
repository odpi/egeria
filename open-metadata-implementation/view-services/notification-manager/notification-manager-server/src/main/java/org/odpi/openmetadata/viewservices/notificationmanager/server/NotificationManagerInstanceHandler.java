/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.notificationmanager.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * NotificationManagerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the NotificationManagerAdmin class.
 */
public class NotificationManagerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public NotificationManagerInstanceHandler()
    {
        super(ViewServiceDescription.NOTIFICATION_MANAGER.getViewServiceName());

        NotificationManagerRegistration.registerViewService();
    }


}

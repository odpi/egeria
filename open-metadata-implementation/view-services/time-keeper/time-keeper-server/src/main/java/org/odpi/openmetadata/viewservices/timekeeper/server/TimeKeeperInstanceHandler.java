/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.timekeeper.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * TimeKeeperInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the TimeKeeperAdmin class.
 */
public class TimeKeeperInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public TimeKeeperInstanceHandler()
    {
        super(ViewServiceDescription.TIME_KEEPER.getViewServiceName());

        TimeKeeperRegistration.registerViewService();
    }


}

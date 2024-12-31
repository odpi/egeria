/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.peopleorganizer.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * PeopleOrganizerInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the PeopleOrganizerAdmin class.
 */
public class PeopleOrganizerInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public PeopleOrganizerInstanceHandler()
    {
        super(ViewServiceDescription.PEOPLE_ORGANIZER.getViewServiceName());

        PeopleOrganizerRegistration.registerViewService();
    }


}

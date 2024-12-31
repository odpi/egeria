/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.peopleorganizer.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The PeopleOrganizerRESTServices provides the server-side implementation of the People Organizer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class PeopleOrganizerRESTServices extends TokenController
{
    private static final PeopleOrganizerInstanceHandler instanceHandler = new PeopleOrganizerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(PeopleOrganizerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public PeopleOrganizerRESTServices()
    {
    }



}

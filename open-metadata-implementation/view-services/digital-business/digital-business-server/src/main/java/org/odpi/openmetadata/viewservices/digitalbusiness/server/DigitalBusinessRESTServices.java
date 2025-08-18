/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.digitalbusiness.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The DigitalBusinessRESTServices provides the server-side implementation of the Digital Business Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class DigitalBusinessRESTServices extends TokenController
{
    private static final DigitalBusinessInstanceHandler instanceHandler = new DigitalBusinessInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DigitalBusinessRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DigitalBusinessRESTServices()
    {
    }



}

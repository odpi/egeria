/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.templatemanager.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;


/**
 * The TemplateManagerRESTServices provides the server-side implementation of the Template Manager Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class TemplateManagerRESTServices extends TokenController
{
    private static final TemplateManagerInstanceHandler instanceHandler = new TemplateManagerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(TemplateManagerRESTServices.class),
                                                                            instanceHandler.getServiceName());


    /**
     * Default constructor
     */
    public TemplateManagerRESTServices()
    {
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.devopspipeline.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The DevopsPipelineRESTServices provides the server-side implementation of the Devops Pipeline Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class DevopsPipelineRESTServices extends TokenController
{
    private static final DevopsPipelineInstanceHandler instanceHandler = new DevopsPipelineInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DevopsPipelineRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DevopsPipelineRESTServices()
    {
    }



}

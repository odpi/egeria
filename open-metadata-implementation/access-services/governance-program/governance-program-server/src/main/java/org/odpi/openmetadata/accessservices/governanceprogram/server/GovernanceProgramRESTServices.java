/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;



import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The GovernanceProgramRESTServices provides the server-side implementation of the GovernanceProgram Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class GovernanceProgramRESTServices
{
    static final private GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private final static RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(GovernanceProgramRESTServices.class),
                                                                      instanceHandler.getServiceName());

    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceProgramRESTServices()
    {
    }


}

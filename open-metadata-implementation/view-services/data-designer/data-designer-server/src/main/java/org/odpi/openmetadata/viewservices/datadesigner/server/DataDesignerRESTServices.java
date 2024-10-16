/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The DataDesignerRESTServices provides the server-side implementation of the Data Designer Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class DataDesignerRESTServices extends TokenController
{
    private static final DataDesignerInstanceHandler instanceHandler = new DataDesignerInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(DataDesignerRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public DataDesignerRESTServices()
    {
    }



}

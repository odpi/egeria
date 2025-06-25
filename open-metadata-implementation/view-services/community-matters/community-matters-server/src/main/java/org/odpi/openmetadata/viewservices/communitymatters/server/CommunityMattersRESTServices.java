/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.communitymatters.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The CommunityMattersRESTServices provides the server-side implementation of the Community Matters Open Metadata
 * View Service (OMVS).  This interface provides support for communities and their members.
 */
public class CommunityMattersRESTServices extends TokenController
{
    private static final CommunityMattersInstanceHandler instanceHandler = new CommunityMattersInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(CommunityMattersRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public CommunityMattersRESTServices()
    {
    }



}

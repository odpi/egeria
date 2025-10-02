/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.multilanguage.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The MultiLanguageRESTServices provides the server-side implementation of the Multi Language Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class MultiLanguageRESTServices extends TokenController
{
    private static final MultiLanguageInstanceHandler instanceHandler = new MultiLanguageInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(MultiLanguageRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public MultiLanguageRESTServices()
    {
    }



}

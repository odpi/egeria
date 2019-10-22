/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.stewardshipaction.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The StewardshipActionRESTServices provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class StewardshipActionRESTServices
{
    private static StewardshipActionInstanceHandler   instanceHandler     = new StewardshipActionInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(StewardshipActionRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public StewardshipActionRESTServices()
    {
    }

}
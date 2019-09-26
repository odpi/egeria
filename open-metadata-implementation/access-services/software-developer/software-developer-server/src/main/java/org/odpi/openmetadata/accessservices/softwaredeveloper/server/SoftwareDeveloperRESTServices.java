/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.softwaredeveloper.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The SoftwareDeveloperRESTServices provides the server-side implementation of the Software Developer Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class SoftwareDeveloperRESTServices
{
    private static SoftwareDeveloperInstanceHandler   instanceHandler     = new SoftwareDeveloperInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(SoftwareDeveloperRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public SoftwareDeveloperRESTServices()
    {
    }

}
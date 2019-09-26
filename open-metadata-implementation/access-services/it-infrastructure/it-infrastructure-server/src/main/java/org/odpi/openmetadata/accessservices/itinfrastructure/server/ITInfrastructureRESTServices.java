/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The ITInfrastructureRESTServices provides the server-side implementation of the Stewardship Action Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class ITInfrastructureRESTServices
{
    private static ITInfrastructureInstanceHandler instanceHandler = new ITInfrastructureInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(ITInfrastructureRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public ITInfrastructureRESTServices()
    {
    }

}
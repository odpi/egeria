/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DigitalArchitectureRESTServices provides the server-side implementation of the Digital Architecture Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class DigitalArchitectureRESTServices
{
    private static DigitalArchitectureInstanceHandler   instanceHandler     = new DigitalArchitectureInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DigitalArchitectureRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DigitalArchitectureRESTServices()
    {
    }

}
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.designmodel.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DesignModelRESTServices provides the server-side implementation of the Design Model Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class DesignModelRESTServices
{
    private static DesignModelInstanceHandler instanceHandler = new DesignModelInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DesignModelRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DesignModelRESTServices()
    {
    }

}
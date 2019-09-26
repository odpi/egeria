/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamodel.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DataModelRESTServices provides the server-side implementation of the Data Model Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class DataModelRESTServices
{
    private static DataModelInstanceHandler   instanceHandler     = new DataModelInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DataModelRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DataModelRESTServices()
    {
    }

}
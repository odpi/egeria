/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataprivacy.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



/**
 * The DataPrivacyRESTServices provides the server-side implementation of the Data Privacy Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class DataPrivacyRESTServices
{
    private static DataPrivacyInstanceHandler   instanceHandler     = new DataPrivacyInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(DataPrivacyRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public DataPrivacyRESTServices()
    {
    }

}
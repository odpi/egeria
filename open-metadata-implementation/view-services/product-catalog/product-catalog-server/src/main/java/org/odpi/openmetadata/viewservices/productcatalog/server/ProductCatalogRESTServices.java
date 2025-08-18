/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productcatalog.server;


import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;



/**
 * The ProductCatalogRESTServices provides the server-side implementation of the Product Catalog Open Metadata
 * View Service (OMVS).  This interface provides access to a person's profile, roles and network.
 */
public class ProductCatalogRESTServices extends TokenController
{
    private static final ProductCatalogInstanceHandler instanceHandler = new ProductCatalogInstanceHandler();

    private static final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(ProductCatalogRESTServices.class),
                                                                            instanceHandler.getServiceName());

    /**
     * Default constructor
     */
    public ProductCatalogRESTServices()
    {
    }



}

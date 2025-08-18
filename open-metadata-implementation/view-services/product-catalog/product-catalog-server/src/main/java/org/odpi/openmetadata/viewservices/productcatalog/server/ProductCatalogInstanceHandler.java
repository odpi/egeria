/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productcatalog.server;


import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.multitenant.OMVSServiceInstanceHandler;



/**
 * ProductCatalogInstanceHandler retrieves information from the instance map for the
 * view service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the ProductCatalogAdmin class.
 */
public class ProductCatalogInstanceHandler extends OMVSServiceInstanceHandler
{
    /**
     * Default constructor registers the view service
     */
    public ProductCatalogInstanceHandler()
    {
        super(ViewServiceDescription.PRODUCT_CATALOG.getViewServiceFullName());

        ProductCatalogRegistration.registerViewService();
    }


}

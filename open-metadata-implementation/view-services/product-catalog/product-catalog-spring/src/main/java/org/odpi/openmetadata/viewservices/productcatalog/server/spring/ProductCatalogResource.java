/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productcatalog.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.productcatalog.server.ProductCatalogRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ProductCatalogResource provides part of the server-side implementation of the Product Catalog OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/product-catalog")

@Tag(name="API: Product Catalog", description="Supports searching a digital product catalog and subscribing to specific products and product families.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/product-catalog/overview/"))

public class ProductCatalogResource
{
    private final ProductCatalogRESTServices restAPI = new ProductCatalogRESTServices();

    /**
     * Default constructor
     */
    public ProductCatalogResource()
    {
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.viewservices.productmanager.server.ProductManagerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ProductManagerResource provides part of the server-side implementation of the Product Manager OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/product-manager")

@Tag(name="API: Product Manager", description="Supports the definition and maintenance of digital products and digital product families.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/product-manager/overview/"))

public class ProductManagerResource
{
    private final ProductManagerRESTServices restAPI = new ProductManagerRESTServices();

    /**
     * Default constructor
     */
    public ProductManagerResource()
    {
    }

}

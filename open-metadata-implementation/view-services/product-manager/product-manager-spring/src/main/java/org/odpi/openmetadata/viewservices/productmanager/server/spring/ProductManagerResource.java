/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.CollectionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness.DigitalProductDependencyProperties;
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



    /**
     * Link two dependent digital products.
     *
     * @param serverName         name of called server
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/{consumerDigitalProductGUID}/product-dependencies/{consumedDigitalProductGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDigitalProductDependency",
            description="Link two dependent digital products.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkDigitalProductDependency(@PathVariable
                                                     String                                serverName,
                                                     @PathVariable
                                                     String consumerDigitalProductGUID,
                                                     @PathVariable
                                                     String consumedDigitalProductGUID,
                                                     @RequestBody (required = false)
                                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDigitalProductDependency(serverName, consumerDigitalProductGUID, consumedDigitalProductGUID, requestBody);
    }


    /**
     * Unlink dependent digital products.
     *
     * @param serverName         name of called server
     * @param consumerDigitalProductGUID    unique identifier of the digital product that has the dependency.
     * @param consumedDigitalProductGUID    unique identifier of the digital product that it is using.
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/{consumerDigitalProductGUID}/product-dependencies/{consumedDigitalProductGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDigitalProductDependency",
            description="Unlink dependent digital products.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachDigitalProductDependency(@PathVariable
                                                       String                    serverName,
                                                       @PathVariable
                                                       String consumerDigitalProductGUID,
                                                       @PathVariable
                                                       String consumedDigitalProductGUID,
                                                       @RequestBody (required = false)
                                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDigitalProductDependency(serverName, consumerDigitalProductGUID, consumedDigitalProductGUID, requestBody);
    }



    /**
     * Attach a product manager to a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/{digitalProductGUID}/product-managers/{digitalProductManagerRoleGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkProductManager",
            description="Attach a product manager to a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse linkProductManager(@PathVariable
                                           String                                serverName,
                                           @PathVariable
                                           String digitalProductGUID,
                                           @PathVariable
                                           String digitalProductManagerRoleGUID,
                                           @RequestBody (required = false)
                                           NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkProductManager(serverName, digitalProductGUID, digitalProductManagerRoleGUID, requestBody);
    }


    /**
     * Detach a product manager from a digital product.
     *
     * @param serverName         name of called server
     * @param digitalProductGUID  unique identifier of the digital product
     * @param digitalProductManagerRoleGUID      unique identifier of the product manager role
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/digital-products/{digitalProductGUID}/product-managers/{digitalProductManagerRoleGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachProductManager",
            description="Detach a product manager from a digital product.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachProductManager(@PathVariable
                                             String                    serverName,
                                             @PathVariable
                                             String digitalProductGUID,
                                             @PathVariable
                                             String digitalProductManagerRoleGUID,
                                             @RequestBody (required = false)
                                             DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachProductManager(serverName, digitalProductGUID, digitalProductManagerRoleGUID, requestBody);
    }
}

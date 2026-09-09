/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.productmanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.integration.bitol.odcs.DataContract;
import org.odpi.openmetadata.frameworks.integration.bitol.odps.DataProduct;
import org.odpi.openmetadata.viewservices.productmanager.rest.DataContractResponse;
import org.odpi.openmetadata.viewservices.productmanager.rest.DataProductResponse;
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

    public GUIDResponse linkDigitalProductDependency(@PathVariable
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
     * Update the properties of a digital product dependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param digitalProductDependencyRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/digital-product-dependencies/{digitalProductDependencyRelationshipGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDigitalProductDependency",
            description="Update the properties of a digital product dependency relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse updateDigitalProductDependency(@PathVariable String serverName,
                                                       @PathVariable String digitalProductDependencyRelationshipGUID,
                                                       @RequestBody (required = false)
                                                       UpdateRelationshipRequestBody requestBody)
    {
        return restAPI.updateDigitalProductDependency(serverName, digitalProductDependencyRelationshipGUID, requestBody);
    }


    /**
     * Remove a digital product dependency relationship.
     *
     * @param serverName name of the server to route the request to
     * @param digitalProductDependencyRelationshipGUID unique identifier of the relationship
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/digital-product-dependencies/{digitalProductDependencyRelationshipGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDigitalProductDependency",
            description="Remove a digital product dependency relationship.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/digital-product"))

    public VoidResponse detachDigitalProductDependency(@PathVariable String serverName,
                                                       @PathVariable String digitalProductDependencyRelationshipGUID,
                                                       @RequestBody (required = false)
                                                       DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDigitalProductDependency(serverName, digitalProductDependencyRelationshipGUID, requestBody);
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
     *
     * This is a multi-link relationship, so this request removes every digital product dependency relationship
     * between the two elements.  Use the request that takes the relationship's own unique identifier to
     * remove just one of them.
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


    /* =====================================================================================================================
     * Bitol documents: Open Data Contract Standard (ODCS) data contracts and Open Data Product Standard (ODPS) data products.
     */

    /**
     * Pass an Open Data Contract Standard (ODCS) data contract, in YAML or JSON, to an integration daemon.  It will pass it on to
     * the integration connectors that have registered a listener for Bitol documents.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param document data contract to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/data-contracts/publish-document-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="publishDataContractString",
            description="Send an Open Data Contract Standard (ODCS) data contract, in YAML or JSON, to an integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for Bitol documents.",
            externalDocs=@ExternalDocumentation(description="Open Data Contract Standard",
                    url="https://bitol-io.github.io/open-data-contract-standard/"))

    public VoidResponse publishDataContract(@PathVariable String serverName,
                                            @PathVariable String serverGUID,
                                            @RequestBody  String document)
    {
        return restAPI.publishDataContract(serverName, serverGUID, document);
    }


    /**
     * Pass an Open Data Contract Standard (ODCS) data contract bean to an integration daemon.  It will pass it on to
     * the integration connectors that have registered a listener for Bitol documents.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param dataContract data contract to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/data-contracts/publish-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="publishDataContract",
            description="Send an Open Data Contract Standard (ODCS) data contract bean to an integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for Bitol documents.",
            externalDocs=@ExternalDocumentation(description="Open Data Contract Standard",
                    url="https://bitol-io.github.io/open-data-contract-standard/"))

    public VoidResponse publishDataContract(@PathVariable String       serverName,
                                            @PathVariable String       serverGUID,
                                            @RequestBody  DataContract dataContract)
    {
        return restAPI.publishDataContract(serverName, serverGUID, dataContract);
    }


    /**
     * Pass an Open Data Product Standard (ODPS) data product, in YAML or JSON, to an integration daemon.  It will pass it on to
     * the integration connectors that have registered a listener for Bitol documents.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param document data product to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/data-products/publish-document-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="publishDataProductString",
            description="Send an Open Data Product Standard (ODPS) data product, in YAML or JSON, to an integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for Bitol documents.",
            externalDocs=@ExternalDocumentation(description="Open Data Product Standard",
                    url="https://bitol-io.github.io/open-data-product-standard/"))

    public VoidResponse publishDataProduct(@PathVariable String serverName,
                                           @PathVariable String serverGUID,
                                           @RequestBody  String document)
    {
        return restAPI.publishDataProduct(serverName, serverGUID, document);
    }


    /**
     * Pass an Open Data Product Standard (ODPS) data product bean to an integration daemon.  It will pass it on to
     * the integration connectors that have registered a listener for Bitol documents.
     *
     * @param serverName name of called server
     * @param serverGUID unique identifier of the integration daemon's software server asset
     * @param dataProduct data product to publish
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/integration-daemons/{serverGUID}/data-products/publish-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="publishDataProduct",
            description="Send an Open Data Product Standard (ODPS) data product bean to an integration daemon.  It will pass it on to the integration connectors that have" +
                    " registered a listener for Bitol documents.",
            externalDocs=@ExternalDocumentation(description="Open Data Product Standard",
                    url="https://bitol-io.github.io/open-data-product-standard/"))

    public VoidResponse publishDataProduct(@PathVariable String      serverName,
                                           @PathVariable String      serverGUID,
                                           @RequestBody  DataProduct dataProduct)
    {
        return restAPI.publishDataProduct(serverName, serverGUID, dataProduct);
    }


    /**
     * Catalog an Open Data Contract Standard (ODCS) data contract, supplied as YAML or JSON, directly in open metadata.
     *
     * @param serverName name of called server
     * @param document data contract to catalog
     * @return unique identifier of the resulting agreement or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-contracts/import-document-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="importDataContractString",
            description="Catalog an Open Data Contract Standard (ODCS) data contract, supplied as YAML or JSON, directly in open metadata as an Agreement" +
                    " classified as a DataSharingAgreement.  The unique identifier of the agreement is returned.",
            externalDocs=@ExternalDocumentation(description="Open Data Contract Standard",
                    url="https://bitol-io.github.io/open-data-contract-standard/"))

    public GUIDResponse importDataContract(@PathVariable String serverName,
                                           @RequestBody  String document)
    {
        return restAPI.importDataContract(serverName, document);
    }


    /**
     * Catalog an Open Data Contract Standard (ODCS) data contract bean directly in open metadata.
     *
     * @param serverName name of called server
     * @param dataContract data contract to catalog
     * @return unique identifier of the resulting agreement or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-contracts/import-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="importDataContract",
            description="Catalog an Open Data Contract Standard (ODCS) data contract bean directly in open metadata as an Agreement" +
                    " classified as a DataSharingAgreement.  The unique identifier of the agreement is returned.",
            externalDocs=@ExternalDocumentation(description="Open Data Contract Standard",
                    url="https://bitol-io.github.io/open-data-contract-standard/"))

    public GUIDResponse importDataContract(@PathVariable String       serverName,
                                           @RequestBody  DataContract dataContract)
    {
        return restAPI.importDataContract(serverName, dataContract);
    }


    /**
     * Catalog an Open Data Product Standard (ODPS) data product, supplied as YAML or JSON, directly in open metadata.
     *
     * @param serverName name of called server
     * @param document data product to catalog
     * @return unique identifier of the resulting digital product or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-products/import-document-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="importDataProductString",
            description="Catalog an Open Data Product Standard (ODPS) data product, supplied as YAML or JSON, directly in open metadata as a DigitalProduct." +
                    "  The unique identifier of the digital product is returned.",
            externalDocs=@ExternalDocumentation(description="Open Data Product Standard",
                    url="https://bitol-io.github.io/open-data-product-standard/"))

    public GUIDResponse importDataProduct(@PathVariable String serverName,
                                          @RequestBody  String document)
    {
        return restAPI.importDataProduct(serverName, document);
    }


    /**
     * Catalog an Open Data Product Standard (ODPS) data product bean directly in open metadata.
     *
     * @param serverName name of called server
     * @param dataProduct data product to catalog
     * @return unique identifier of the resulting digital product or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-products/import-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="importDataProduct",
            description="Catalog an Open Data Product Standard (ODPS) data product bean directly in open metadata as a DigitalProduct." +
                    "  The unique identifier of the digital product is returned.",
            externalDocs=@ExternalDocumentation(description="Open Data Product Standard",
                    url="https://bitol-io.github.io/open-data-product-standard/"))

    public GUIDResponse importDataProduct(@PathVariable String      serverName,
                                          @RequestBody  DataProduct dataProduct)
    {
        return restAPI.importDataProduct(serverName, dataProduct);
    }


    /**
     * Generate the Open Data Contract Standard (ODCS) document for an agreement.
     *
     * @param serverName name of called server
     * @param agreementGUID unique identifier of the agreement
     * @return data contract or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/agreements/{agreementGUID}/data-contract-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="generateDataContract",
            description="Generate the Open Data Contract Standard (ODCS) document that describes an agreement (typically one classified as a DataSharingAgreement).",
            externalDocs=@ExternalDocumentation(description="Open Data Contract Standard",
                    url="https://bitol-io.github.io/open-data-contract-standard/"))

    public DataContractResponse generateDataContract(@PathVariable String serverName,
                                                     @PathVariable String agreementGUID)
    {
        return restAPI.generateDataContract(serverName, agreementGUID);
    }


    /**
     * Generate the Open Data Product Standard (ODPS) document for a digital product.
     *
     * @param serverName name of called server
     * @param digitalProductGUID unique identifier of the digital product
     * @return data product or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @GetMapping(path = "/digital-products/{digitalProductGUID}/data-product-document")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="generateDataProduct",
            description="Generate the Open Data Product Standard (ODPS) document that describes a digital product, including the contracts referenced by its ports.",
            externalDocs=@ExternalDocumentation(description="Open Data Product Standard",
                    url="https://bitol-io.github.io/open-data-product-standard/"))

    public DataProductResponse generateDataProduct(@PathVariable String serverName,
                                                   @PathVariable String digitalProductGUID)
    {
        return restAPI.generateDataProduct(serverName, digitalProductGUID);
    }
}

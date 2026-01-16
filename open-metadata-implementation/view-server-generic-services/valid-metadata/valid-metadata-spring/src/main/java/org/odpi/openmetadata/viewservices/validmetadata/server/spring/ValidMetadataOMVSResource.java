/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.validmetadata.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValue;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;
import org.odpi.openmetadata.frameworks.openmetadata.specificationproperties.SpecificationProperty;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.validmetadata.server.ValidMetadataRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ValidMetadataOMVSResource provides part of the server-side implementation of the Valid Metadata OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/{urlMarker}")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Valid Metadata", description="Retrieves and maintains lists of valid metadata values and specification properties.  Retrieves open metadata type definitions.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/valid-metadata/overview/"))

public class ValidMetadataOMVSResource
{
    private final ValidMetadataRESTServices restAPI = new ValidMetadataRESTServices();

    /**
     * Default constructor
     */
    public ValidMetadataOMVSResource()
    {
    }


    /**
     * Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value
     * applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is
     * already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/setup-value/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setUpValidMetadataValue",
            description="Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value" +
                    " applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is" +
                    " already set up for this property (with overlapping effective dates) then the valid value is updated.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setUpValidMetadataValue(@PathVariable String             serverName,
                                                @PathVariable String                        urlMarker,
                                                @RequestParam(required = false)
                                                String             typeName,
                                                @PathVariable String             propertyName,
                                                @RequestBody  ValidMetadataValue requestBody)
    {
        return restAPI.setUpValidMetadataValue(serverName, urlMarker, typeName, propertyName, requestBody);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The mapName is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/setup-map-name/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setUpValidMetadataMapName",
            description="Create or update the valid value for a name that can be stored in a particular open metadata property name." +
                    " This property is of type map from name to string." +
                    " The mapName is stored in the preferredValue property of validMetadataValue." +
                    " If the typeName is null, this valid value applies to properties of this name from any open metadata type." +
                    " If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setUpValidMetadataMapName(@PathVariable String             serverName,
                                                  @PathVariable String                        urlMarker,
                                                  @RequestParam(required = false)
                                                  String             typeName,
                                                  @PathVariable String             propertyName,
                                                  @RequestBody  ValidMetadataValue requestBody)
    {
        return restAPI.setUpValidMetadataMapName(serverName, urlMarker, typeName, propertyName, requestBody);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/setup-map-value/{propertyName}/{mapName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setUpValidMetadataMapValue",
            description="Create or update the valid value for a name that can be stored in a particular open metadata property name." +
                    " This property is of type map from name to string." +
                    " The valid value is stored in the preferredValue property of validMetadataValue." +
                    " If the typeName is null, this valid value applies to properties of this name from any open metadata type." +
                    " If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setUpValidMetadataMapValue(@PathVariable String             serverName,
                                                   @PathVariable String                        urlMarker,
                                                   @RequestParam(required = false)
                                                   String             typeName,
                                                   @PathVariable String             propertyName,
                                                   @PathVariable String             mapName,
                                                   @RequestBody  ValidMetadataValue validMetadataValue)
    {
        return restAPI.setUpValidMetadataMapValue(serverName, urlMarker, typeName, propertyName, mapName, validMetadataValue);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/clear-value/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearValidMetadataValue",
            description="Remove a valid value for a property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse clearValidMetadataValue(@PathVariable String          serverName,
                                                @PathVariable String                        urlMarker,
                                                @RequestParam(required = false)
                                                String          typeName,
                                                @PathVariable String          propertyName,
                                                @RequestParam(required = false)
                                                String          preferredValue,
                                                @RequestBody(required = false)
                                                DeleteElementRequestBody requestBody)
    {
        return restAPI.clearValidMetadataValue(serverName, urlMarker, typeName, propertyName, preferredValue, requestBody);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on mapName.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/clear-map-name/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearValidMetadataMapName",
            description="Remove a valid map name value for a property.  The match is done on preferred name.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse clearValidMetadataMapName(@PathVariable String          serverName,
                                                  @PathVariable String                        urlMarker,
                                                  @RequestParam(required = false)
                                                  String          typeName,
                                                  @PathVariable String          propertyName,
                                                  @RequestParam(required = false)
                                                  String          mapName,
                                                  @RequestBody(required = false)
                                                  DeleteElementRequestBody requestBody)
    {
        return restAPI.clearValidMetadataMapName(serverName, urlMarker, typeName, propertyName, mapName, requestBody);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/clear-map-value/{propertyName}/{mapName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="clearValidMetadataMapValue",
            description="Remove a valid map name value for a property.  The match is done on preferred name.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse clearValidMetadataMapValue(@PathVariable String          serverName,
                                                   @PathVariable String                        urlMarker,
                                                   @RequestParam(required = false)
                                                   String          typeName,
                                                   @PathVariable String          propertyName,
                                                   @PathVariable String          mapName,
                                                   @RequestParam(required = false)
                                                   String          preferredValue,
                                                   @RequestBody(required = false)
                                                   DeleteElementRequestBody requestBody)
    {
        return restAPI.clearValidMetadataMapValue(serverName, urlMarker, typeName, propertyName, mapName, preferredValue, requestBody);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/validate-value/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="validateMetadataValue",
            description="Validate whether the value found in an open metadata property is valid.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public BooleanResponse validateMetadataValue(@PathVariable String serverName,
                                                 @PathVariable String                        urlMarker,
                                                 @RequestParam(required = false)
                                                 String typeName,
                                                 @PathVariable String propertyName,
                                                 @RequestParam String actualValue)
    {
        return restAPI.validateMetadataValue(serverName, urlMarker, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/validate-map-name/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="validateMetadataMapName",
            description="Validate whether the name found in an open metadata map property is valid.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public BooleanResponse validateMetadataMapName(@PathVariable String serverName,
                                                   @PathVariable String                        urlMarker,
                                                   @RequestParam(required = false)
                                                   String typeName,
                                                   @PathVariable String propertyName,
                                                   @RequestParam String mapName)
    {
        return restAPI.validateMetadataMapName(serverName, urlMarker, typeName, propertyName, mapName);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/validate-map-value/{propertyName}/{mapName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="validateMetadataMapValue",
            description="Validate whether the name found in an open metadata map property is valid.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public BooleanResponse validateMetadataMapValue(@PathVariable String serverName,
                                                    @PathVariable String                        urlMarker,
                                                    @RequestParam(required = false)
                                                    String typeName,
                                                    @PathVariable String propertyName,
                                                    @PathVariable String mapName,
                                                    @RequestParam String actualValue)
    {
        return restAPI.validateMetadataMapValue(serverName, urlMarker, typeName, propertyName, mapName, actualValue);
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/get-value/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getValidMetadataValue",
            description="Retrieve details of a specific valid value for a property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueResponse getValidMetadataValue(@PathVariable String serverName,
                                                            @PathVariable String                        urlMarker,
                                                            @RequestParam(required = false)
                                                            String typeName,
                                                            @PathVariable String propertyName,
                                                            @RequestParam String preferredValue)
    {
        return restAPI.getValidMetadataValue(serverName, urlMarker, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/get-map-name/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getValidMetadataMapName",
            description="Retrieve details of a specific valid name for a map property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueResponse getValidMetadataMapName(@PathVariable String serverName,
                                                              @PathVariable String                        urlMarker,
                                                              @RequestParam(required = false)
                                                              String typeName,
                                                              @PathVariable String propertyName,
                                                              @RequestParam String mapName)
    {
        return  restAPI.getValidMetadataMapName(serverName, urlMarker, typeName, propertyName, mapName);
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/get-map-value/{propertyName}/{mapName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getValidMetadataMapValue",
            description="Retrieve details of a specific valid value for a map name.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueResponse getValidMetadataMapValue(@PathVariable String serverName,
                                                               @PathVariable String                        urlMarker,
                                                               @RequestParam(required = false)
                                                               String typeName,
                                                               @PathVariable String propertyName,
                                                               @PathVariable String mapName,
                                                               @RequestParam String preferredValue)
    {
        return restAPI.getValidMetadataMapValue(serverName, urlMarker, typeName, propertyName, mapName, preferredValue);
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/get-valid-metadata-values/{propertyName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getValidMetadataValues",
            description="Retrieve all the valid values for the requested property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueListResponse getValidMetadataValues(@PathVariable String serverName,
                                                                 @PathVariable String                        urlMarker,
                                                                 @RequestParam(required = false)
                                                                 String typeName,
                                                                 @PathVariable String propertyName,
                                                                 @RequestParam int    startFrom,
                                                                 @RequestParam int    pageSize)
    {
        return restAPI.getValidMetadataValues(serverName, urlMarker, typeName, propertyName, startFrom, pageSize);
    }


    /**
     * Retrieve all the consistent valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName optional name of map key that this valid value applies
     * @param preferredValue the value to match against
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @GetMapping(path = "/{propertyName}/consistent-metadata-values")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getConsistentMetadataValues",
            description="Retrieve all the consistent valid values for the requested property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueListResponse getConsistentMetadataValues(@PathVariable String serverName,
                                                                      @PathVariable String                        urlMarker,
                                                                      @RequestParam(required = false)
                                                                      String typeName,
                                                                      @PathVariable String propertyName,
                                                                      @RequestParam(required = false)
                                                                      String mapName,
                                                                      @RequestParam String preferredValue,
                                                                      @RequestParam int    startFrom,
                                                                      @RequestParam int    pageSize)
    {
        return restAPI.getConsistentMetadataValues(serverName, urlMarker, typeName, propertyName, mapName, preferredValue, startFrom, pageSize);
    }


    /**
     * Set up consistent metadata values relationship between the two property values.
     *
     * @param serverName     name of server instance to route request to
     * @param urlMarker  view service URL marker
     * @param typeName1 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName1 name of property that this valid value applies
     * @param mapName1 optional name of map key that this valid value applies
     * @param preferredValue1 the value to match against
     * @param typeName2 type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName2 name of property that this valid value applies
     * @param mapName2 optional name of map key that this valid value applies
     * @param preferredValue2 the value to match against
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    a problem accessing the metadata store
     */
    @PostMapping(path = "/{propertyName1}/consistent-metadata-values/{propertyName2}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setConsistentMetadataValues",
            description="Set up consistent metadata values relationship between the two property values.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setConsistentMetadataValues(@PathVariable String          serverName,
                                                    @PathVariable String                        urlMarker,
                                                    @RequestParam(required = false)
                                                    String          typeName1,
                                                    @PathVariable String          propertyName1,
                                                    @RequestParam(required = false)
                                                    String          mapName1,
                                                    @RequestParam String          preferredValue1,
                                                    @RequestParam(required = false)
                                                    String          typeName2,
                                                    @PathVariable String          propertyName2,
                                                    @RequestParam(required = false)
                                                    String          mapName2,
                                                    @RequestParam String          preferredValue2,
                                                    @RequestBody(required = false)
                                                    NullRequestBody requestBody)
    {
        return restAPI.setConsistentMetadataValues(serverName,
                                                   urlMarker,
                                                   typeName1,
                                                   propertyName1,
                                                   mapName1,
                                                   preferredValue1,
                                                   typeName2,
                                                   propertyName2,
                                                   mapName2,
                                                   preferredValue2,
                                                   requestBody);
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for attributes in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getAllTypes",
            description="Return the list of types loaded into this server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefGalleryResponse getAllTypes(@PathVariable String   serverName,
                                              @PathVariable String   urlMarker)
    {
        return restAPI.getAllTypes(serverName, urlMarker);
    }


    /**
     * Returns all the entity type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/entity-defs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getEntityDefs",
            description="Returns all the entity type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getEntityDefs(@PathVariable String serverName,
                                             @PathVariable String   urlMarker)
    {
        return restAPI.getTypeDefsByCategory(serverName, urlMarker, OpenMetadataTypeDefCategory.ENTITY_DEF);
    }


    /**
     * Returns all the relationship type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/relationship-defs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getRelationshipDefs",
            description="Returns all the relationship type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getRelationshipDefs(@PathVariable String serverName,
                                                   @PathVariable String   urlMarker)
    {
        return restAPI.getTypeDefsByCategory(serverName, urlMarker, OpenMetadataTypeDefCategory.RELATIONSHIP_DEF);
    }


    /**
     * Returns all the classification type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/classification-defs")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getClassificationDefs",
            description="Returns all the classification type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getClassificationDefs(@PathVariable String serverName,
                                                     @PathVariable String urlMarker)
    {
        return restAPI.getTypeDefsByCategory(serverName, urlMarker, OpenMetadataTypeDefCategory.CLASSIFICATION_DEF);
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @param typeName name of type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/sub-types/{typeName}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSubTypes",
            description="Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the type has no subtypes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getSubTypes(@PathVariable String serverName,
                                           @PathVariable String urlMarker,
                                           @PathVariable String typeName)
    {
        return restAPI.getSubTypes(serverName, urlMarker, typeName);
    }


    /**
     * Returns all the TypeDefs for relationships that can be attached to the requested entity type.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @param typeName name of entity type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/{typeName}/attached-relationships")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getValidRelationshipTypes",
            description="Returns all the TypeDefs for relationships that can be attached to the requested entity type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getValidRelationshipTypes(@PathVariable String serverName,
                                                         @PathVariable String urlMarker,
                                                         @PathVariable String typeName)
    {
        return restAPI.getValidRelationshipTypes(serverName, urlMarker, typeName);
    }


    /**
     * Returns all the TypeDefs for classifications that can be attached to the requested entity type.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @param typeName name of type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/{typeName}/attached-classifications")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getValidClassificationTypes",
            description="Returns all the TypeDefs for relationships that can be attached to the requested entity type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getValidClassificationTypes(@PathVariable String serverName,
                                                           @PathVariable String urlMarker,
                                                           @PathVariable String typeName)
    {
        return restAPI.getValidClassificationTypes(serverName, urlMarker, typeName);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param urlMarker  view service URL marker
     * @param name String name of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/name/{name}")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getTypeDefByName",
            description="Return the TypeDef identified by the unique name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefResponse getTypeDefByName(@PathVariable String    serverName,
                                            @PathVariable String    urlMarker,
                                            @PathVariable String    name)
    {
        return restAPI.getTypeDefByName(serverName, urlMarker, name);
    }


    /**
     * Return the list of specification property types.
     *
     * @param serverName name of the server instance to connect to
     * @return list of type names that are subtypes of asset or
     * throws InvalidParameterException full path or userId is null or
     * throws PropertyServerException problem accessing property server or
     * throws UserNotAuthorizedException security access problem.
     */
    @GetMapping(path = "/specification-properties/type-names")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSpecificationPropertyTypes",
            description="Return the list of specification property types.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/specification/"))

    public StringMapResponse getSpecificationPropertyTypes(@PathVariable String serverName,
                                                           @PathVariable String urlMarker)
    {
        return restAPI.getSpecificationPropertyTypes(serverName, urlMarker);
    }


    /**
     * Creates a specification property and attaches it to an element.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param elementGUID        String - unique id for the element.
     * @param requestBody containing details of the specificationProperty.
     *
     * @return elementGUID for new specification property object or
     * InvalidParameterException one of the parameters is null or invalid or
     * PropertyServerException There is a problem adding the element properties to
     *                                   the metadata repository or
     * UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/specification-properties")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="setUpSpecificationProperty",
            description="Creates a specification property and attaches it to an element.",
            externalDocs=@ExternalDocumentation(description="Search Keyword",
                    url="https://egeria-project.org/concepts/specification/"))

    public GUIDResponse setUpSpecificationProperty(@PathVariable String                 serverName,
                                                   @PathVariable String                 urlMarker,
                                                   @PathVariable String                 elementGUID,
                                                   @RequestBody(required = false) SpecificationProperty requestBody)
    {
        return restAPI.setUpSpecificationProperty(serverName, urlMarker, elementGUID, requestBody);
    }


    /**
     * Removes a specification property added to the element.  This deletes the link to the specification property and the specification property itself.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param specificationPropertyGUID  String - unique id for the specification property object
     * @param requestBody  containing type of specification property enum and the text of the specificationProperty.
     *
     * @return void or
     * InvalidParameterException one of the parameters is null or invalid.
     * PropertyServerException There is a problem updating the element properties in the metadata repository.
     * UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/specification-properties/{specificationPropertyGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteSpecificationProperty",
            description="Removes a specification property added to the element.  This deletes the link to the specificationProperty and the specification property itself.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/specification/"))

    public VoidResponse deleteSpecificationProperty(@PathVariable String                         serverName,
                                                    @PathVariable String                        urlMarker,
                                                    @PathVariable String                         specificationPropertyGUID,
                                                    @RequestBody(required = false)
                                                    DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteSpecificationProperty(serverName, urlMarker, specificationPropertyGUID, requestBody);
    }


    /**
     * Return the list of specification properties containing the supplied string.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of specification property objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/specification-properties/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findSpecificationProperty",
            description="Return the list of specification properties containing the supplied string. The search string is located in the request body and is interpreted as a plain string.  The request parameters, startsWith, endsWith and ignoreCase can be used to allow a fuzzy search.  The request body also supports the specification of an effective time to restrict the search to element that are/were effective at a particular time.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/specification/"))

    public OpenMetadataRootElementsResponse findSpecificationProperties(@PathVariable String                  serverName,
                                                                        @PathVariable String                        urlMarker,
                                                                        @RequestBody  (required = false)
                                                                        SearchStringRequestBody              requestBody)
    {
        return restAPI.findSpecificationProperties(serverName, urlMarker, requestBody);
    }


    /**
     * Return the list of specification properties containing the supplied type.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param specificationPropertyType type to search on
     * @param requestBody search string and effective time.
     *
     * @return list of specification property objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/specification-properties/by-type")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSpecificationPropertyByType",
            description="Return the list of specification properties containing the supplied type.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/specification/"))

    public OpenMetadataRootElementsResponse getSpecificationPropertyByType(@PathVariable String                  serverName,
                                                                           @RequestParam SpecificationPropertyType specificationPropertyType,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestBody  (required = false)
                                                                           ResultsRequestBody              requestBody)
    {
        return restAPI.getSpecificationPropertiesByType(serverName, urlMarker, specificationPropertyType, requestBody);
    }


    /**
     * Return the list of specification properties containing the supplied name.
     *
     * @param serverName name of the server instances for this request.
     * @param urlMarker  view service URL marker
     * @param requestBody search string and effective time.
     *
     * @return list of specification property objects or
     * InvalidParameterException - one of the parameters is invalid or
     * PropertyServerException - a problem retrieving information from the property server(s) or
     * UserNotAuthorizedException - the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/specification-properties/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSpecificationPropertyByName",
            description="Return the list of specification properties containing the supplied name.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/specification/"))

    public OpenMetadataRootElementsResponse getSpecificationPropertyByName(@PathVariable String                  serverName,
                                                                           @PathVariable String                        urlMarker,
                                                                           @RequestBody  (required = false)
                                                                           FilterRequestBody              requestBody)
    {
        return restAPI.getSpecificationPropertiesByName(serverName, urlMarker, requestBody);
    }


    /**
     * Return the requested specificationProperty.
     *
     * @param serverName name of the server instances for this request
     * @param urlMarker  view service URL marker
     * @param specificationPropertyGUID  unique identifier for the specification property object.
     * @param requestBody optional effective time
     * @return specification property properties or
     *  InvalidParameterException one of the parameters is null or invalid.
     *  PropertyServerException a problem updating the element properties in the property server.
     *  UserNotAuthorizedException the user does not have permission to perform this request.
     */
    @PostMapping(path = "/specification-properties/{specificationPropertyGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getSpecificationPropertyByGUID",
            description="Return the requested specification property.",
            externalDocs=@ExternalDocumentation(description="Search Keywords",
                    url="https://egeria-project.org/concepts/specification/"))

    public OpenMetadataRootElementResponse getSpecificationPropertyByGUID(@PathVariable String                        serverName,
                                                                          @PathVariable String                        urlMarker,
                                                                          @PathVariable String                        specificationPropertyGUID,
                                                                          @RequestBody(required = false)
                                                                          GetRequestBody requestBody)
    {
        return restAPI.getSpecificationPropertyByGUID(serverName, urlMarker, specificationPropertyGUID, requestBody);
    }
}

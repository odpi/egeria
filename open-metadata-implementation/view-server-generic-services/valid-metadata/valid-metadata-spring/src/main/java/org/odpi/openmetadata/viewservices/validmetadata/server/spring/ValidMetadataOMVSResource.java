/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.validmetadata.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ValidMetadataValueProperties;
import org.odpi.openmetadata.frameworkservices.omf.rest.*;
import org.odpi.openmetadata.viewservices.validmetadata.server.ValidMetadataRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The ValidMetadataOMVSResource provides part of the server-side implementation of the Valid Metadata OMVS.
= */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/valid-metadata")

@Tag(name="API: Valid Metadata OMVS", description="The Valid Metadata OMVS provides APIs for retrieving and updating lists of valid metadata values.",
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
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/setup-value/{propertyName}")

    @Operation(summary="setUpValidMetadataValue",
            description="Create or update the valid value for a particular open metadata property name.  If the typeName is null, this valid value" +
                    " applies to properties of this name from all types.  The valid value is stored in the preferredValue property.  If a valid value is" +
                    " already set up for this property (with overlapping effective dates) then the valid value is updated.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setUpValidMetadataValue(@PathVariable String             serverName,
                                                @RequestParam(required = false)
                                                              String             typeName,
                                                @PathVariable String             propertyName,
                                                @RequestBody ValidMetadataValueProperties requestBody)
    {
        return restAPI.setUpValidMetadataValue(serverName, typeName, propertyName, requestBody);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The mapName is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param requestBody preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/setup-map-name/{propertyName}")

    @Operation(summary="setUpValidMetadataMapName",
            description="Create or update the valid value for a name that can be stored in a particular open metadata property name." +
                    " This property is of type map from name to string." +
                    " The mapName is stored in the preferredValue property of validMetadataValue." +
                    " If the typeName is null, this valid value applies to properties of this name from any open metadata type." +
                    " If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setUpValidMetadataMapName(@PathVariable String             serverName,
                                                  @RequestParam(required = false)
                                                                String             typeName,
                                                  @PathVariable String             propertyName,
                                                  @RequestBody ValidMetadataValueProperties requestBody)
    {
        return restAPI.setUpValidMetadataMapName(serverName, typeName, propertyName, requestBody);
    }


    /**
     * Create or update the valid value for a name that can be stored in a particular open metadata property name.
     * This property is of type map from name to string.
     * The valid value is stored in the preferredValue property of validMetadataValue.
     * If the typeName is null, this valid value applies to properties of this name from any open metadata type.
     * If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param validMetadataValue preferred value to use in the open metadata types plus additional descriptive values.
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/setup-map-value/{propertyName}/{mapName}")

    @Operation(summary="setUpValidMetadataMapValue",
            description="Create or update the valid value for a name that can be stored in a particular open metadata property name." +
                    " This property is of type map from name to string." +
                    " The valid value is stored in the preferredValue property of validMetadataValue." +
                    " If the typeName is null, this valid value applies to properties of this name from any open metadata type." +
                    " If a valid value is already set up for this property (with overlapping effective dates) then the valid value is updated.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setUpValidMetadataMapValue(@PathVariable String             serverName,
                                                   @RequestParam(required = false)
                                                                 String             typeName,
                                                   @PathVariable String             propertyName,
                                                   @PathVariable String             mapName,
                                                   @RequestBody ValidMetadataValueProperties validMetadataValue)
    {
        return restAPI.setUpValidMetadataMapValue(serverName, typeName, propertyName, mapName, validMetadataValue);
    }


    /**
     * Remove a valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/clear-value/{propertyName}")

    @Operation(summary="clearValidMetadataValue",
            description="Remove a valid value for a property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse clearValidMetadataValue(@PathVariable String          serverName,
                                                @RequestParam(required = false)
                                                              String          typeName,
                                                @PathVariable String          propertyName,
                                                @RequestParam(required = false)
                                                              String          preferredValue,
                                                @RequestBody(required = false)
                                                              NullRequestBody requestBody)
    {
        return restAPI.clearValidMetadataValue(serverName, typeName, propertyName, preferredValue, requestBody);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on mapName.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/clear-map-name/{propertyName}")

    @Operation(summary="clearValidMetadataMapName",
            description="Remove a valid map name value for a property.  The match is done on preferred name.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse clearValidMetadataMapName(@PathVariable String          serverName,
                                                  @RequestParam(required = false)
                                                                String          typeName,
                                                  @PathVariable String          propertyName,
                                                  @RequestParam(required = false)
                                                                String          mapName,
                                                  @RequestBody(required = false)
                                                                NullRequestBody requestBody)
    {
        return restAPI.clearValidMetadataMapName(serverName, typeName, propertyName, mapName, requestBody);
    }


    /**
     * Remove a valid map name value for a property.  The match is done on preferred name.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue specific valid value to remove
     * @param requestBody null request body
     *
     * @return void or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/clear-map-value/{propertyName}/{mapName}")

    @Operation(summary="clearValidMetadataMapValue",
            description="Remove a valid map name value for a property.  The match is done on preferred name.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse clearValidMetadataMapValue(@PathVariable String          serverName,
                                                   @RequestParam(required = false)
                                                   String          typeName,
                                                   @PathVariable String          propertyName,
                                                   @PathVariable String          mapName,
                                                   @RequestParam(required = false)
                                                   String          preferredValue,
                                                   @RequestBody(required = false)
                                                       NullRequestBody requestBody)
    {
        return restAPI.clearValidMetadataMapValue(serverName, typeName, propertyName, mapName, preferredValue, requestBody);
    }


    /**
     * Validate whether the value found in an open metadata property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/validate-value/{propertyName}")

    @Operation(summary="validateMetadataValue",
            description="Validate whether the value found in an open metadata property is valid.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public BooleanResponse validateMetadataValue(@PathVariable String serverName,
                                                 @RequestParam(required = false)
                                                               String typeName,
                                                 @PathVariable String propertyName,
                                                 @RequestParam String actualValue)
    {
        return restAPI.validateMetadataValue(serverName, typeName, propertyName, actualValue);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/validate-map-name/{propertyName}")

    @Operation(summary="validateMetadataMapName",
            description="Validate whether the name found in an open metadata map property is valid.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public BooleanResponse validateMetadataMapName(@PathVariable String serverName,
                                                   @RequestParam(required = false)
                                                                 String typeName,
                                                   @PathVariable String propertyName,
                                                   @RequestParam String mapName)
    {
        return restAPI.validateMetadataMapName(serverName, typeName, propertyName, mapName);
    }


    /**
     * Validate whether the name found in an open metadata map property is valid.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param actualValue value stored in the property - if this is null, true is only returned if null is set up as a valid value.
     *
     * @return boolean flag - true if the value is one of the defined valid values or there are no valid values set up for the property (and so any value is value) or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/validate-map-value/{propertyName}/{mapName}")

    @Operation(summary="validateMetadataMapValue",
            description="Validate whether the name found in an open metadata map property is valid.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public BooleanResponse validateMetadataMapValue(@PathVariable String serverName,
                                                    @RequestParam(required = false)
                                                    String typeName,
                                                    @PathVariable String propertyName,
                                                    @PathVariable String mapName,
                                                    @RequestParam String actualValue)
    {
        return restAPI.validateMetadataMapValue(serverName, typeName, propertyName, mapName, actualValue);
    }


    /**
     * Retrieve details of a specific valid value for a property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/get-value/{propertyName}")

    @Operation(summary="getValidMetadataValue",
            description="Retrieve details of a specific valid value for a property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueResponse getValidMetadataValue(@PathVariable String serverName,
                                                            @RequestParam(required = false)
                                                                          String typeName,
                                                            @PathVariable String propertyName,
                                                            @RequestParam String preferredValue)
    {
        return restAPI.getValidMetadataValue(serverName, typeName, propertyName, preferredValue);
    }


    /**
     * Retrieve details of a specific valid name for a map property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/get-map-name/{propertyName}")

    @Operation(summary="getValidMetadataMapName",
            description="Retrieve details of a specific valid name for a map property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueResponse getValidMetadataMapName(@PathVariable String serverName,
                                                              @RequestParam(required = false)
                                                                            String typeName,
                                                              @PathVariable String propertyName,
                                                              @RequestParam String mapName)
    {
        return  restAPI.getValidMetadataMapName(serverName, typeName, propertyName, mapName);
    }


    /**
     * Retrieve details of a specific valid value for a map name.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param mapName name in the map that this valid value applies.  If null then the value can be used for any name in the map.
     * @param preferredValue valid value to match
     *
     * @return specific valid value definition or none if there is no definition stored or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/get-map-value/{propertyName}/{mapName}")

    @Operation(summary="getValidMetadataMapValue",
            description="Retrieve details of a specific valid value for a map name.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueResponse getValidMetadataMapValue(@PathVariable String serverName,
                                                               @RequestParam(required = false)
                                                               String typeName,
                                                               @PathVariable String propertyName,
                                                               @PathVariable String mapName,
                                                               @RequestParam String preferredValue)
    {
        return restAPI.getValidMetadataMapValue(serverName, typeName, propertyName, mapName, preferredValue);
    }


    /**
     * Retrieve all the valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
     * @param typeName type name if this is valid value is specific for a type, or null if this valid value if for the property name for all types
     * @param propertyName name of property that this valid value applies
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of valid values defined for the property or
     * InvalidParameterException  the property name is null or not known.
     * UserNotAuthorizedException the service is not able to create/access the element
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/get-valid-metadata-values/{propertyName}")

    @Operation(summary="getValidMetadataValues",
            description="Retrieve all the valid values for the requested property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueDetailListResponse getValidMetadataValues(@PathVariable String serverName,
                                                                       @RequestParam(required = false)
                                                                                     String typeName,
                                                                       @PathVariable String propertyName,
                                                                       @RequestParam int    startFrom,
                                                                       @RequestParam int    pageSize)
    {
        return restAPI.getValidMetadataValues(serverName, typeName, propertyName, startFrom, pageSize);
    }


    /**
     * Retrieve all the consistent valid values for the requested property.
     *
     * @param serverName     name of server instance to route request to
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
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @GetMapping(path = "/{propertyName}/consistent-metadata-values")

    @Operation(summary="getConsistentMetadataValues",
            description="Retrieve all the consistent valid values for the requested property.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public ValidMetadataValueListResponse getConsistentMetadataValues(@PathVariable String serverName,
                                                                      @RequestParam(required = false)
                                                                      String typeName,
                                                                      @PathVariable String propertyName,
                                                                      @RequestParam(required = false)
                                                                      String mapName,
                                                                      @RequestParam String preferredValue,
                                                                      @RequestParam int    startFrom,
                                                                      @RequestParam int    pageSize)
    {
        return restAPI.getConsistentMetadataValues(serverName, typeName, propertyName, mapName, preferredValue, startFrom, pageSize);
    }


    /**
     * Set up consistent metadata values relationship between the two property values.
     *
     * @param serverName     name of server instance to route request to
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
     * PropertyServerException    there is a problem accessing the metadata store
     */
    @PostMapping(path = "/{propertyName1}/consistent-metadata-values/{propertyName2}")

    @Operation(summary="setConsistentMetadataValues",
            description="Set up consistent metadata values relationship between the two property values.",
            externalDocs=@ExternalDocumentation(description="Valid Metadata Values",
                    url="https://egeria-project.org/guides/planning/valid-values/overview/"))

    public VoidResponse setConsistentMetadataValues(@PathVariable String          serverName,
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
     * @return TypeDefGalleryResponse:
     * List of different categories of type definitions or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types")

    @Operation(summary="getAllTypes",
            description="Return the list of types loaded into this server.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefGalleryResponse getAllTypes(@PathVariable String   serverName)
    {
        return restAPI.getAllTypes(serverName);
    }


    /**
     * Returns all the entity type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/entity-defs")

    @Operation(summary="getEntityDefs",
            description="Returns all the entity type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getEntityDefs(@PathVariable String serverName)
    {
        return restAPI.getTypeDefsByCategory(serverName, OpenMetadataTypeDefCategory.ENTITY_DEF);
    }


    /**
     * Returns all the relationship type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/relationship-defs")

    @Operation(summary="getRelationshipDefs",
            description="Returns all the relationship type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getRelationshipDefs(@PathVariable String serverName)
    {
        return restAPI.getTypeDefsByCategory(serverName, OpenMetadataTypeDefCategory.RELATIONSHIP_DEF);
    }


    /**
     * Returns all the classification type definitions.
     *
     * @param serverName unique identifier for requested server.
     * @return TypeDefListResponse:
     * TypeDefs list or
     * InvalidParameterException the TypeDefCategory is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/classification-defs")

    @Operation(summary="getClassificationDefs",
            description="Returns all the classification type definitions.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getClassificationDefs(@PathVariable String serverName)
    {
        return restAPI.getTypeDefsByCategory(serverName, OpenMetadataTypeDefCategory.CLASSIFICATION_DEF);
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.
     *
     * @param serverName unique identifier for requested server.
     * @param typeName name of type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/sub-types/{typeName}")

    @Operation(summary="getSubTypes",
            description="Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the type has no subtypes.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getSubTypes(@PathVariable String serverName,
                                           @PathVariable String typeName)
    {
        return restAPI.getSubTypes(serverName, typeName);
    }


    /**
     * Returns all the TypeDefs for relationships that can be attached to the requested entity type.
     *
     * @param serverName unique identifier for requested server.
     * @param typeName name of entity type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/{typeName}/attached-relationships")

    @Operation(summary="getValidRelationshipTypes",
            description="Returns all the TypeDefs for relationships that can be attached to the requested entity type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getValidRelationshipTypes(@PathVariable String serverName,
                                                         @PathVariable String typeName)
    {
        return restAPI.getValidRelationshipTypes(serverName, typeName);
    }


    /**
     * Returns all the TypeDefs for classifications that can be attached to the requested entity type.
     *
     * @param serverName unique identifier for requested server.
     * @param typeName name of type to retrieve against.
     * @return TypeDefsGalleryResponse:
     * A list of types or
     * InvalidParameterException all attributes of the external id are null or
     * RepositoryErrorException there is a problem communicating with the metadata repository or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/{typeName}/attached-classifications")

    @Operation(summary="getValidClassificationTypes",
            description="Returns all the TypeDefs for relationships that can be attached to the requested entity type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefListResponse getValidClassificationTypes(@PathVariable String serverName,
                                                           @PathVariable String typeName)
    {
        return restAPI.getValidClassificationTypes(serverName, typeName);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param serverName unique identifier for requested server.
     * @param name String name of the TypeDef.
     * @return TypeDefResponse:
     * TypeDef structure describing its category and properties or
     * InvalidParameterException the name is null or
     * RepositoryErrorException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored or
     * TypeDefNotKnownException the requested TypeDef is not found in the metadata collection or
     * UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    @GetMapping(path = "/open-metadata-types/name/{name}")

    @Operation(summary="getTypeDefByName",
            description="Return the TypeDef identified by the unique name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/types/"))

    public TypeDefResponse getTypeDefByName(@PathVariable String    serverName,
                                            @PathVariable String    name)
    {
        return restAPI.getTypeDefByName(serverName, name);
    }
}

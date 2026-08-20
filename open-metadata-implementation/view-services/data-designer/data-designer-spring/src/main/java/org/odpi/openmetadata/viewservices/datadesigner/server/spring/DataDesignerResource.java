/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.GetRequestBody;
import org.odpi.openmetadata.viewservices.datadesigner.server.DataDesignerRESTServices;
import org.springframework.web.bind.annotation.*;


/**
 * The DataDesignerResource provides part of the server-side implementation of the Data Designer OMVS.
 = */
@RestController
@RequestMapping("/servers/{serverName}/api/open-metadata/data-designer")
@SecurityScheme(
        name = "BearerAuthorization",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer",
        in = SecuritySchemeIn.HEADER
)
@Tag(name="API: Data Designer", description="Supports the building of data specifications and data dictionaries to support your teams when communicating data requirements to one another.  The data dictionary defines commonly used data fields, and the data specification defines the specific data requirements for a project. Data specifications are also used to describe the data provided by a digital product.",
        externalDocs=@ExternalDocumentation(description="Further Information",
                url="https://egeria-project.org/services/omvs/data-designer/overview/"))

public class DataDesignerResource
{
    private final DataDesignerRESTServices restAPI = new DataDesignerRESTServices();

    /**
     * Default constructor
     */
    public DataDesignerResource()
    {
    }


    /**
     * Create a data structure.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data structure.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataStructure",
            description="Create a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public GUIDResponse createDataStructure(@PathVariable
                                            String                               serverName,
                                            @RequestBody (required = false)
                                            NewElementRequestBody requestBody)
    {
        return restAPI.createDataStructure(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a data structure using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataStructureFromTemplate",
            description="Create a new metadata element to represent a data structure using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public GUIDResponse createDataStructureFromTemplate(@PathVariable
                                                        String              serverName,
                                                        @RequestBody (required = false)
                                                        TemplateRequestBody requestBody)
    {
        return restAPI.createDataStructureFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a data structure.
     *
     * @param serverName         name of called server.
     * @param dataStructureGUID unique identifier of the data structure (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDataStructure",
            description="Update the properties of a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public BooleanResponse updateDataStructure(@PathVariable
                                               String                                  serverName,
                                               @PathVariable
                                               String                                  dataStructureGUID,
                                               @RequestBody (required = false)
                                               UpdateElementRequestBody requestBody)
    {
        return restAPI.updateDataStructure(serverName, dataStructureGUID, requestBody);
    }


    /**
     * Attach a data field to a data structure.
     *
     * @param serverName         name of called server
     * @param dataStructureGUID  unique identifier of the first data structure
     * @param dataFieldGUID      unique identifier of the second data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/member-data-fields/{dataFieldGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkMemberDataField",
            description="Attach a data field to a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public VoidResponse linkMemberDataField(@PathVariable
                                            String                  serverName,
                                            @PathVariable
                                            String                  dataStructureGUID,
                                            @PathVariable
                                            String                  dataFieldGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkMemberDataField(serverName, dataStructureGUID, dataFieldGUID, requestBody);
    }


    /**
     * Detach a data field from a data structure.
     *
     * @param serverName         name of called server
     * @param parentDataStructureGUID  unique identifier of the first data structure
     * @param memberDataFieldGUID      unique identifier of the second data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{parentDataStructureGUID}/member-data-fields/{memberDataFieldGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachMemberDataField",
            description="Detach a data field from a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public VoidResponse detachMemberDataField(@PathVariable
                                              String                    serverName,
                                              @PathVariable
                                              String parentDataStructureGUID,
                                              @PathVariable
                                              String memberDataFieldGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachMemberDataField(serverName, parentDataStructureGUID, memberDataFieldGUID, requestBody);
    }


    /**
     * Delete a data structure.
     *
     * @param serverName         name of called server
     * @param dataStructureGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteDataStructure",
            description="Delete a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public VoidResponse deleteDataStructure(@PathVariable
                                            String                    serverName,
                                            @PathVariable
                                            String                    dataStructureGUID,
                                            @RequestBody (required = false)
                                            DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteDataStructure(serverName, dataStructureGUID, requestBody);
    }


    /**
     * Returns the list of data structures with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataStructuresByName",
            description="Returns the list of data structures with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public OpenMetadataRootElementsResponse getDataStructuresByName(@PathVariable
                                                                    String            serverName,
                                                                    @RequestBody (required = false)
                                                                    FilterRequestBody requestBody)
    {
        return restAPI.getDataStructuresByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of data structure metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDataStructures",
            description="Retrieve the list of data structure metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public OpenMetadataRootElementsResponse findDataStructures(@PathVariable
                                                               String                  serverName,
                                                               @RequestBody (required = false)
                                                               SearchStringRequestBody requestBody)
    {
        return restAPI.findDataStructures(serverName, requestBody);
    }


    /**
     * Return the properties of a specific data structure.
     *
     * @param serverName name of the service to route the request to
     * @param dataStructureGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataStructureByGUID",
            description="Return the properties of a specific data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public OpenMetadataRootElementResponse getDataStructureByGUID(@PathVariable
                                                                  String             serverName,
                                                                  @PathVariable
                                                                  String             dataStructureGUID,
                                                                  @RequestBody (required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getDataStructureByGUID(serverName, dataStructureGUID, requestBody);
    }

    /*==============================================
     * Data Fields
     */

    /**
     * Create a data field.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data field.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataField",
            description="Create a data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public GUIDResponse createDataField(@PathVariable
                                        String                               serverName,
                                        @RequestBody (required = false)
                                        NewElementRequestBody requestBody)
    {
        return restAPI.createDataField(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a data field using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataFieldFromTemplate",
            description="Create a new metadata element to represent a data field using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public GUIDResponse createDataFieldFromTemplate(@PathVariable
                                                    String              serverName,
                                                    @RequestBody (required = false)
                                                    TemplateRequestBody requestBody)
    {
        return restAPI.createDataFieldFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a data field.
     *
     * @param serverName         name of called server.
     * @param dataFieldGUID unique identifier of the data field (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDataField",
            description="Update the properties of a data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public BooleanResponse updateDataField(@PathVariable
                                           String                                  serverName,
                                           @PathVariable
                                           String                                  dataFieldGUID,
                                           @RequestBody (required = false)
                                           UpdateElementRequestBody requestBody)
    {
        return restAPI.updateDataField(serverName, dataFieldGUID, requestBody);
    }


    /**
     * Connect two data field as parent and child.
     *
     * @param serverName         name of called server
     * @param parentDataFieldGUID  unique identifier of the parent data field
     * @param nestedDataFieldGUID      unique identifier of the child data field
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{parentDataFieldGUID}/nested-data-fields/{nestedDataFieldGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkNestedDataFields",
            description="Connect two data field as parent and child.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse linkNestedDataFields(@PathVariable
                                             String                   serverName,
                                             @PathVariable
                                             String                   parentDataFieldGUID,
                                             @PathVariable
                                             String nestedDataFieldGUID,
                                             @RequestBody (required = false)
                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNestedDataFields(serverName, parentDataFieldGUID, nestedDataFieldGUID, requestBody);
    }


    /**
     * Detach two data fields from one another.
     *
     * @param serverName         name of called server
     * @param parentDataFieldGUID  unique identifier of the parent data field
     * @param nestedDataFieldGUID      unique identifier of the child data field
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{parentDataFieldGUID}/nested-data-fields/{nestedDataFieldGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachNestedDataFields",
            description="Detach two data fields from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse detachNestedDataFields(@PathVariable
                                               String                    serverName,
                                               @PathVariable
                                               String                    parentDataFieldGUID,
                                               @PathVariable
                                               String nestedDataFieldGUID,
                                               @RequestBody (required = false)
                                               DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNestedDataFields(serverName, parentDataFieldGUID, nestedDataFieldGUID, requestBody);
    }


    /**
     * Delete a data field.
     *
     * @param serverName         name of called server
     * @param dataFieldGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteDataField",
            description="Delete a data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse deleteDataField(@PathVariable
                                        String                    serverName,
                                        @PathVariable
                                        String                    dataFieldGUID,
                                        @RequestBody (required = false)
                                        DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteDataField(serverName, dataFieldGUID, requestBody);
    }


    /**
     * Returns the list of data fields with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataFieldsByName",
            description="Returns the list of data fields with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public OpenMetadataRootElementsResponse getDataFieldsByName(@PathVariable
                                                                String            serverName,
                                                                @RequestBody (required = false)
                                                                FilterRequestBody requestBody)
    {
        return restAPI.getDataFieldsByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of data field metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDataFields",
            description="Retrieve the list of data field metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public OpenMetadataRootElementsResponse findDataFields(@PathVariable
                                                           String                  serverName,
                                                           @RequestBody (required = false)
                                                           SearchStringRequestBody requestBody)
    {
        return restAPI.findDataFields(serverName, requestBody);
    }


    /**
     * Return the properties of a specific data field.
     *
     * @param serverName name of the service to route the request to
     * @param dataFieldGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataFieldByGUID",
            description="Return the properties of a specific data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public OpenMetadataRootElementResponse getDataFieldByGUID(@PathVariable
                                                              String             serverName,
                                                              @PathVariable
                                                              String             dataFieldGUID,
                                                              @RequestBody (required = false)
                                                              GetRequestBody requestBody)
    {
        return restAPI.getDataFieldByGUID(serverName, dataFieldGUID, requestBody);
    }


    /*===============================================
     * Data value specifications (data classes and data grains)
     */

    /**
     * Create a data value specification.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data value specification.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-value-specifications")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataValueSpecification",
            description="Create a data value specification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public GUIDResponse createDataValueSpecification(@PathVariable
                                                     String                serverName,
                                                     @RequestBody (required = false)
                                                     NewElementRequestBody requestBody)
    {
        return restAPI.createDataValueSpecification(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a data value specification using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-value-specifications/from-template")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="createDataValueSpecificationFromTemplate",
            description="Create a new metadata element to represent a data value specification using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public GUIDResponse createDataValueSpecificationFromTemplate(@PathVariable
                                                                 String              serverName,
                                                                 @RequestBody (required = false)
                                                                 TemplateRequestBody requestBody)
    {
        return restAPI.createDataValueSpecificationFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a data value specification.
     *
     * @param serverName         name of called server.
     * @param dataValueSpecificationGUID unique identifier of the data value specification (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return boolean or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-value-specifications/{dataValueSpecificationGUID}/update")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="updateDataValueSpecification",
            description="Update the properties of a data value specification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public BooleanResponse updateDataValueSpecification(@PathVariable
                                                        String                                  serverName,
                                                        @PathVariable
                                                        String                                  dataValueSpecificationGUID,
                                                        @RequestBody (required = false)
                                                        UpdateElementRequestBody requestBody)
    {
        return restAPI.updateDataValueSpecification(serverName, dataValueSpecificationGUID, requestBody);
    }


    /**
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the parent data class
     * @param childDataClassGUID      unique identifier of the child data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/nested-data-classes/{childDataClassGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkNestedDataClass",
            description="Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse linkNestedDataClass(@PathVariable
                                            String                                serverName,
                                            @PathVariable
                                            String                                parentDataClassGUID,
                                            @PathVariable
                                            String                                childDataClassGUID,
                                            @RequestBody (required = false)
                                                NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkNestedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Detach two nested data classes from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the parent data class
     * @param childDataClassGUID      unique identifier of the child data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/nested-data-classes/{childDataClassGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachNestedDataClass",
            description="Detach two nested data classes from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse detachNestedDataClass(@PathVariable
                                              String                    serverName,
                                              @PathVariable
                                              String                    parentDataClassGUID,
                                              @PathVariable
                                              String                    childDataClassGUID,
                                              @RequestBody (required = false)
                                                  DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachNestedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Connect two data value specifications to show that one provides a more specialist evaluation.
     *
     * @param serverName         name of called server
     * @param dataValueSpecificationGUID  unique identifier of the first data value specification
     * @param specializedDataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-value-specifications/{dataValueSpecificationGUID}/specialized-data-value-specifications/{specializedDataValueSpecificationGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSpecializedDataValueSpecification",
            description="Connect two data value specifications to show that one provides a more specialist evaluation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public VoidResponse linkSpecializedDataValueSpecification(@PathVariable
                                                              String                  serverName,
                                                              @PathVariable
                                                              String dataValueSpecificationGUID,
                                                              @PathVariable
                                                              String specializedDataValueSpecificationGUID,
                                                              @RequestBody (required = false)
                                                              NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSpecializedDataValueSpecification(serverName, dataValueSpecificationGUID, specializedDataValueSpecificationGUID, requestBody);
    }


    /**
     * Detach two data value specifications from one another.
     *
     * @param serverName         name of called server
     * @param dataValueSpecificationGUID  unique identifier of the first data value specification
     * @param specializedDataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-value-specifications/{dataValueSpecificationGUID}/specialized-data-value-specifications/{specializedDataValueSpecificationGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSpecializedDataValueSpecification",
            description="Detach two data value specifications from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public VoidResponse detachSpecializedDataValueSpecification(@PathVariable
                                                                String                    serverName,
                                                                @PathVariable
                                                                String dataValueSpecificationGUID,
                                                                @PathVariable
                                                                String specializedDataValueSpecificationGUID,
                                                                @RequestBody (required = false)
                                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSpecializedDataValueSpecification(serverName, dataValueSpecificationGUID, specializedDataValueSpecificationGUID, requestBody);
    }


    /**
     * Connect an element to a data value specification that describes the data associated with this element.
     *
     * @param serverName         name of called server
     * @param elementGUID  unique identifier of the first data value specification
     * @param dataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/data-value-specifications/{dataValueSpecificationGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="assignDataValueSpecification",
            description="Connect an element to a data value specification that describes the data associated with this element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public VoidResponse assignDataValueSpecification(@PathVariable
                                                         String                  serverName,
                                                     @PathVariable String elementGUID,
                                                     @PathVariable
                                                         String dataValueSpecificationGUID,
                                                     @RequestBody (required = false)
                                                         NewRelationshipRequestBody requestBody)
    {
        return restAPI.assignDataValueSpecification(serverName, elementGUID, dataValueSpecificationGUID, requestBody);
    }


    /**
     * Detach an element from one of its assigned data value specifications.
     *
     * @param serverName         name of called server
     * @param elementGUID  unique identifier of the first data value specification
     * @param dataValueSpecificationGUID      unique identifier of the second data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/elements/{elementGUID}/data-value-specifications/{dataValueSpecificationGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataValueSpecificationAssignment",
            description="Detach an element from one of its assigned data value specifications.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public VoidResponse detachDataValueSpecificationAssignment(@PathVariable
                                                                String                    serverName,
                                                                @PathVariable
                                                                String elementGUID,
                                                                @PathVariable
                                                                String dataValueSpecificationGUID,
                                                                @RequestBody (required = false)
                                                                DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataValueSpecificationAssignment(serverName, elementGUID, dataValueSpecificationGUID, requestBody);
    }


    /**
     * Delete a data value specification.
     *
     * @param serverName         name of called server
     * @param dataValueSpecificationGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-value-specifications/{dataValueSpecificationGUID}/delete")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="deleteDataValueSpecification",
            description="Delete a data value specification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public VoidResponse deleteDataValueSpecification(@PathVariable
                                                     String                    serverName,
                                                     @PathVariable
                                                     String                    dataValueSpecificationGUID,
                                                     @RequestBody (required = false)
                                                     DeleteElementRequestBody requestBody)
    {
        return restAPI.deleteDataValueSpecification(serverName, dataValueSpecificationGUID, requestBody);
    }


    /**
     * Returns the list of data value specifications with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-value-specifications/by-name")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataValueSpecificationsByName",
            description="Returns the list of data value specifications with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public OpenMetadataRootElementsResponse getDataValueSpecificationsByName(@PathVariable
                                                                             String            serverName,
                                                                             @RequestBody (required = false)
                                                                             FilterRequestBody requestBody)
    {
        return restAPI.getDataValueSpecificationsByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of data value specification metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-value-specifications/by-search-string")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="findDataValueSpecifications",
            description="Retrieve the list of data value specification metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public OpenMetadataRootElementsResponse findDataValueSpecifications(@PathVariable
                                                                        String                  serverName,
                                                                        @RequestBody (required = false)
                                                                        SearchStringRequestBody requestBody)
    {
        return restAPI.findDataValueSpecifications(serverName, requestBody);
    }


    /**
     * Return the properties of a specific data value specification.
     *
     * @param serverName name of the service to route the request to
     * @param dataValueSpecificationGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-value-specifications/{dataValueSpecificationGUID}/retrieve")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="getDataValueSpecificationByGUID",
            description="Return the properties of a specific data value specification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-value-specification"))

    public OpenMetadataRootElementResponse getDataValueSpecificationByGUID(@PathVariable
                                                                           String             serverName,
                                                                           @PathVariable
                                                                           String             dataValueSpecificationGUID,
                                                                           @RequestBody (required = false)
                                                                           GetRequestBody requestBody)
    {
        return restAPI.getDataValueSpecificationByGUID(serverName, dataValueSpecificationGUID, requestBody);
    }


    /*
     * Assembling a data specification
     */


    /**
     * Connect an element that is part of a data design to a data value specification to show that the data value specification should be used
     * as the specification for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param dataValueSpecificationGUID          unique identifier of the data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/data-value-specification-definition/{dataValueSpecificationGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkDataValueSpecificationDefinition",
            description="Connect an element that is part of a data design to a data value specification to show that the data value specification should be used as the specification for the data values when interpreting the data definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse linkDataValueSpecificationDefinition(@PathVariable String                    serverName,
                                                             @PathVariable String                    dataDefinitionGUID,
                                                             @PathVariable String dataValueSpecificationGUID,
                                                             @RequestBody (required = false)
                                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataValueSpecificationDefinition(serverName, dataDefinitionGUID, dataValueSpecificationGUID, requestBody);
    }


    /**
     * Detach a data definition from a data value specification.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param dataValueSpecificationGUID          unique identifier of the data value specification
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/data-value-specification-definition/{dataValueSpecificationGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachDataValueSpecificationDefinition",
            description="Detach a data definition from a data value specification.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse detachDataValueSpecificationDefinition(@PathVariable String                    serverName,
                                                               @PathVariable String                    dataDefinitionGUID,
                                                               @PathVariable String dataValueSpecificationGUID,
                                                               @RequestBody (required = false)
                                                               DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachDataValueSpecificationDefinition(serverName, dataDefinitionGUID, dataValueSpecificationGUID, requestBody);
    }


    /**
     * Connect an element that is part of a data design to a glossary term to show that the term should be used
     * as the semantic definition for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/semantic-definition/{glossaryTermGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSemanticDefinition",
            description="Connect an element that is part of a data design to a glossary term to show that the term should be used as the semantic definition for the data values when interpreting the data definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse linkSemanticDefinition(@PathVariable String                    serverName,
                                               @PathVariable String                    dataDefinitionGUID,
                                               @PathVariable String                    glossaryTermGUID,
                                               @RequestBody (required = false)
                                               NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSemanticDefinition(serverName, dataDefinitionGUID, glossaryTermGUID, requestBody);
    }


    /**
     * Detach a data definition from a glossary term.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data value specification
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/semantic-definition/{glossaryTermGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSemanticDefinition",
            description="Detach a data definition from a glossary term.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse detachSemanticDefinition(@PathVariable String                    serverName,
                                                 @PathVariable String                    dataDefinitionGUID,
                                                 @PathVariable String                    glossaryTermGUID,
                                                 @RequestBody (required = false)
                                                 DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSemanticDefinition(serverName, dataDefinitionGUID, glossaryTermGUID, requestBody);
    }

    /**
     * Connect a certification type to a data structure to guide the survey action service (that checks the data
     * quality of a data resource as part of certifying it with the supplied certification type) to the definition
     * of the data structure to use as a specification of how the data should be both structured and (if
     * data value specifications are attached to the associated data fields using the DataValueSpecificationDefinition relationship)
     * contain the valid values.
     *
     * @param serverName         name of called server
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/certification-types/{certificationTypeGUID}/data-structure-definition/{dataStructureGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkCertificationTypeToDataStructure",
            description="Connect a certification type to a data structure to guide the survey action service (that checks the data quality of a data resource as part of certifying it with the supplied certification type) to the definition of the data structure to use as a specification of how the data should be both structured and (if data value specifications are attached to the associated data fields using the DataValueSpecificationDefinition relationship) contain the valid values.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse linkCertificationTypeToDataStructure(@PathVariable String                    serverName,
                                                             @PathVariable String                    certificationTypeGUID,
                                                             @PathVariable String                    dataStructureGUID,
                                                             @RequestBody (required = false)
                                                             NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkCertificationTypeToDataStructure(serverName, certificationTypeGUID, dataStructureGUID, requestBody);
    }


    /**
     * Detach a data structure from a certification type.
     *
     * @param serverName         name of called server
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/certification-types/{certificationTypeGUID}/data-structure-definition/{dataStructureGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachCertificationTypeToDataStructure",
            description="Detach a data structure from a certification type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))
    public VoidResponse detachCertificationTypeToDataStructure(@PathVariable String                    serverName,
                                                               @PathVariable String                    certificationTypeGUID,
                                                               @PathVariable String                    dataStructureGUID,
                                                               @RequestBody (required = false)
                                                               DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachCertificationTypeToDataStructure(serverName, certificationTypeGUID, dataStructureGUID, requestBody);
    }

    /**
     * Attach a data field to another data field that it is linked to.
     *
     * @param serverName name of the server to route the request to
     * @param dataFieldOneGUID unique identifier of the data field that the link is from
     * @param dataFieldTwoGUID unique identifier of the data field that the link is to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/{dataFieldOneGUID}/linked-data-fields/{dataFieldTwoGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkLinkedDataField",
            description="Attach a data field to another data field that it is linked to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse linkLinkedDataField(@PathVariable String serverName,
                                            @PathVariable String dataFieldOneGUID,
                                            @PathVariable String dataFieldTwoGUID,
                                            @RequestBody (required = false)
                                            NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkLinkedDataField(serverName, dataFieldOneGUID, dataFieldTwoGUID, requestBody);
    }


    /**
     * Detach a data field from another data field that it was linked to.
     *
     * @param serverName name of the server to route the request to
     * @param dataFieldOneGUID unique identifier of the data field that the link is from
     * @param dataFieldTwoGUID unique identifier of the data field that the link is to
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/{dataFieldOneGUID}/linked-data-fields/{dataFieldTwoGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachLinkedDataField",
            description="Detach a data field from another data field that it was linked to.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse detachLinkedDataField(@PathVariable String serverName,
                                              @PathVariable String dataFieldOneGUID,
                                              @PathVariable String dataFieldTwoGUID,
                                              @RequestBody (required = false)
                                              DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachLinkedDataField(serverName, dataFieldOneGUID, dataFieldTwoGUID, requestBody);
    }


    /**
     * Attach a data field to the schema attribute that implements it.
     *
     * @param serverName name of the server to route the request to
     * @param dataFieldGUID unique identifier of the data field
     * @param schemaAttributeGUID unique identifier of the equivalent schema attribute
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/schema-attribute-definitions/{schemaAttributeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSchemaAttributeDefinition",
            description="Attach a data field to the schema attribute that implements it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse linkSchemaAttributeDefinition(@PathVariable String serverName,
                                                      @PathVariable String dataFieldGUID,
                                                      @PathVariable String schemaAttributeGUID,
                                                      @RequestBody (required = false)
                                                      NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSchemaAttributeDefinition(serverName, dataFieldGUID, schemaAttributeGUID, requestBody);
    }


    /**
     * Detach a data field from the schema attribute that implemented it.
     *
     * @param serverName name of the server to route the request to
     * @param dataFieldGUID unique identifier of the data field
     * @param schemaAttributeGUID unique identifier of the equivalent schema attribute
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/schema-attribute-definitions/{schemaAttributeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSchemaAttributeDefinition",
            description="Detach a data field from the schema attribute that implemented it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse detachSchemaAttributeDefinition(@PathVariable String serverName,
                                                        @PathVariable String dataFieldGUID,
                                                        @PathVariable String schemaAttributeGUID,
                                                        @RequestBody (required = false)
                                                        DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSchemaAttributeDefinition(serverName, dataFieldGUID, schemaAttributeGUID, requestBody);
    }


    /**
     * Attach a data structure to the schema type that implements it.
     *
     * @param serverName name of the server to route the request to
     * @param dataStructureGUID unique identifier of the data structure
     * @param schemaTypeGUID unique identifier of the equivalent schema type
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/schema-type-definitions/{schemaTypeGUID}/attach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="linkSchemaTypeDefinition",
            description="Attach a data structure to the schema type that implements it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse linkSchemaTypeDefinition(@PathVariable String serverName,
                                                 @PathVariable String dataStructureGUID,
                                                 @PathVariable String schemaTypeGUID,
                                                 @RequestBody (required = false)
                                                 NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSchemaTypeDefinition(serverName, dataStructureGUID, schemaTypeGUID, requestBody);
    }


    /**
     * Detach a data structure from the schema type that implemented it.
     *
     * @param serverName name of the server to route the request to
     * @param dataStructureGUID unique identifier of the data structure
     * @param schemaTypeGUID unique identifier of the equivalent schema type
     * @param requestBody properties for the request
     *
     * @return response object
     * InvalidParameterException  one of the parameters is invalid
     * UserNotAuthorizedException the user is not authorized to issue this request
     * PropertyServerException    a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/schema-type-definitions/{schemaTypeGUID}/detach")
    @SecurityRequirement(name = "BearerAuthorization")

    @Operation(summary="detachSchemaTypeDefinition",
            description="Detach a data structure from the schema type that implemented it.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse detachSchemaTypeDefinition(@PathVariable String serverName,
                                                   @PathVariable String dataStructureGUID,
                                                   @PathVariable String schemaTypeGUID,
                                                   @RequestBody (required = false)
                                                   DeleteRelationshipRequestBody requestBody)
    {
        return restAPI.detachSchemaTypeDefinition(serverName, dataStructureGUID, schemaTypeGUID, requestBody);
    }
}

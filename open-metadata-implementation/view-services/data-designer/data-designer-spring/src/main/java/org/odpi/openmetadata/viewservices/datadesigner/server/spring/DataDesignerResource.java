/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
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

@Tag(name="API: Data Designer OMVS", description="The Data Designer OMVS provides APIs for building schemas for new data assets.",
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures")

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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/from-template")
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
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/update")
    @Operation(summary="updateDataStructure",
            description="Update the properties of a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public VoidResponse updateDataStructure(@PathVariable
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/member-data-fields/{dataFieldGUID}/attach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{parentDataStructureGUID}/member-data-fields/{memberDataFieldGUID}/detach")
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
                                                  DeleteRequestBody requestBody)
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/delete")
    @Operation(summary="deleteDataStructure",
            description="Delete a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public VoidResponse deleteDataStructure(@PathVariable
                                            String                    serverName,
                                            @PathVariable
                                            String                    dataStructureGUID,
                                            @RequestBody (required = false)
                                                DeleteRequestBody requestBody)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/by-name")
    @Operation(summary="getDataStructuresByName",
            description="Returns the list of data structures with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public DataStructuresResponse getDataStructuresByName(@PathVariable
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/by-search-string")
    @Operation(summary="findDataStructures",
            description="Retrieve the list of data structure metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public DataStructuresResponse findDataStructures(@PathVariable
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-structures/{dataStructureGUID}/retrieve")
    @Operation(summary="getDataStructureByGUID",
            description="Return the properties of a specific data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public DataStructureResponse getDataStructureByGUID(@PathVariable
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields")

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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/from-template")
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
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/update")
    @Operation(summary="updateDataField",
            description="Update the properties of a data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse updateDataField(@PathVariable
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{parentDataFieldGUID}/nested-data-fields/{nestedDataFieldGUID}/attach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{parentDataFieldGUID}/nested-data-fields/{nestedDataFieldGUID}/detach")
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
                                                   DeleteRequestBody requestBody)
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/delete")
    @Operation(summary="deleteDataField",
            description="Delete a data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public VoidResponse deleteDataField(@PathVariable
                                        String                    serverName,
                                        @PathVariable
                                        String                    dataFieldGUID,
                                        @RequestBody (required = false)
                                            DeleteRequestBody requestBody)
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/by-name")
    @Operation(summary="getDataFieldsByName",
            description="Returns the list of data fields with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public DataFieldsResponse getDataFieldsByName(@PathVariable
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/by-search-string")
    @Operation(summary="findDataFields",
            description="Retrieve the list of data field metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public DataFieldsResponse findDataFields(@PathVariable
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
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-fields/{dataFieldGUID}/retrieve")
    @Operation(summary="getDataFieldByGUID",
            description="Return the properties of a specific data field.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-field"))

    public DataFieldResponse getDataFieldByGUID(@PathVariable
                                                String             serverName,
                                                @PathVariable
                                                String             dataFieldGUID,
                                                @RequestBody (required = false)
                                                    GetRequestBody requestBody)
    {
        return restAPI.getDataFieldByGUID(serverName, dataFieldGUID, requestBody);
    }


    /*===============================================
     * Data classes
     */

    /**
     * Create a data class.
     *
     * @param serverName                 name of called server.
     * @param requestBody             properties for the data class.
     *
     * @return unique identifier of the newly created element
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes")

    @Operation(summary="createDataClass",
            description="Create a data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public GUIDResponse createDataClass(@PathVariable
                                        String                serverName,
                                        @RequestBody (required = false)
                                        NewElementRequestBody requestBody)
    {
        return restAPI.createDataClass(serverName, requestBody);
    }


    /**
     * Create a new metadata element to represent a data class using an existing metadata element as a template.
     * The template defines additional classifications and relationships that should be added to the new element.
     *
     * @param serverName             calling user
     * @param requestBody properties that override the template
     *
     * @return unique identifier of the new metadata element
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-classes/from-template")
    @Operation(summary="createDataClassFromTemplate",
            description="Create a new metadata element to represent a data class using an existing metadata element as a template.  The template defines additional classifications and relationships that should be added to the new element.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public GUIDResponse createDataClassFromTemplate(@PathVariable
                                                    String              serverName,
                                                    @RequestBody (required = false)
                                                    TemplateRequestBody requestBody)
    {
        return restAPI.createDataClassFromTemplate(serverName, requestBody);
    }


    /**
     * Update the properties of a data class.
     *
     * @param serverName         name of called server.
     * @param dataClassGUID unique identifier of the data class (returned from create)
     * @param requestBody     properties for the new element.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{dataClassGUID}/update")
    @Operation(summary="updateDataClass",
            description="Update the properties of a data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse updateDataClass(@PathVariable
                                        String                                  serverName,
                                        @PathVariable
                                        String                                  dataClassGUID,
                                        @RequestBody (required = false)
                                        UpdateElementRequestBody requestBody)
    {
        return restAPI.updateDataClass(serverName, dataClassGUID, requestBody);
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/nested-data-classes/{childDataClassGUID}/attach")
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/nested-data-classes/{childDataClassGUID}/detach")
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
                                                  DeleteRequestBody requestBody)
    {
        return restAPI.detachNestedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Connect two data classes to show that one provides a more specialist evaluation.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/specialized-data-classes/{childDataClassGUID}/attach")
    @Operation(summary="linkSpecializedDataClass",
            description="Connect two data classes to show that one provides a more specialist evaluation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse linkSpecializedDataClass(@PathVariable
                                                 String                  serverName,
                                                 @PathVariable
                                                 String                  parentDataClassGUID,
                                                 @PathVariable
                                                 String                  childDataClassGUID,
                                                 @RequestBody (required = false)
                                                     NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkSpecializedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Detach two data classes from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first data class
     * @param childDataClassGUID      unique identifier of the second data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/specialized-data-classes/{childDataClassGUID}/detach")
    @Operation(summary="detachSpecializedDataClass",
            description="Detach two data classes from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse detachSpecializedDataClass(@PathVariable
                                                   String                    serverName,
                                                   @PathVariable
                                                   String                    parentDataClassGUID,
                                                   @PathVariable
                                                   String                    childDataClassGUID,
                                                   @RequestBody (required = false)
                                                       DeleteRequestBody requestBody)
    {
        return restAPI.detachSpecializedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Delete a data class.
     *
     * @param serverName         name of called server
     * @param dataClassGUID  unique identifier of the element to delete
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{dataClassGUID}/delete")
    @Operation(summary="deleteDataClass",
            description="Delete a data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse deleteDataClass(@PathVariable
                                        String                    serverName,
                                        @PathVariable
                                        String                    dataClassGUID,
                                        @RequestBody (required = false)
                                            DeleteRequestBody requestBody)
    {
        return restAPI.deleteDataClass(serverName, dataClassGUID, requestBody);
    }


    /**
     * Returns the list of data classes with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-classes/by-name")
    @Operation(summary="getDataClassesByName",
            description="Returns the list of data classes with a particular name.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public OpenMetadataRootElementsResponse getDataClassesByName(@PathVariable
                                                                 String            serverName,
                                                                 @RequestBody (required = false)
                                                                 FilterRequestBody requestBody)
    {
        return restAPI.getDataClassesByName(serverName, requestBody);
    }


    /**
     * Retrieve the list of data class metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-classes/by-search-string")
    @Operation(summary="findDataClasses",
            description="Retrieve the list of data class metadata elements that contain the search string.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public OpenMetadataRootElementsResponse findDataClasses(@PathVariable
                                                            String                  serverName,
                                                            @RequestBody (required = false)
                                                            SearchStringRequestBody requestBody)
    {
        return restAPI.findDataClasses(serverName, requestBody);
    }


    /**
     * Return the properties of a specific data class.
     *
     * @param serverName name of the service to route the request to
     * @param dataClassGUID    unique identifier of the required element
     * @param requestBody string to find in the properties
     *
     * @return list of matching metadata elements or
     *  InvalidParameterException  one of the parameters is invalid
     *  UserNotAuthorizedException the user is not authorized to issue this request
     *  PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/data-classes/{dataClassGUID}/retrieve")
    @Operation(summary="getDataClassByGUID",
            description="Return the properties of a specific data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public OpenMetadataRootElementResponse getDataClassByGUID(@PathVariable
                                                              String             serverName,
                                                              @PathVariable
                                                              String             dataClassGUID,
                                                              @RequestBody (required = false)
                                                                  GetRequestBody requestBody)
    {
        return restAPI.getDataClassByGUID(serverName, dataClassGUID, requestBody);
    }


    /*
     * Assembling a data specification
     */


    /**
     * Connect an element that is part of a data design to a data class to show that the data class should be used
     * as the specification for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/data-class-definition/{dataClassGUID}/attach")
    @Operation(summary="linkDataClassDefinition",
            description="Connect an element that is part of a data design to a data class to show that the data class should be used as the specification for the data values when interpreting the data definition.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse linkDataClassDefinition(@PathVariable String                    serverName,
                                                @PathVariable String                    dataDefinitionGUID,
                                                @PathVariable String                    dataClassGUID,
                                                @RequestBody (required = false)
                                                    NewRelationshipRequestBody requestBody)
    {
        return restAPI.linkDataClassDefinition(serverName, dataDefinitionGUID, dataClassGUID, requestBody);
    }


    /**
     * Detach a data definition from a data class.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/data-class-definition/{dataClassGUID}/detach")
    @Operation(summary="detachDataClassDefinition",
            description="Detach a data definition from a data class.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse detachDataClassDefinition(@PathVariable String                    serverName,
                                                  @PathVariable String                    dataDefinitionGUID,
                                                  @PathVariable String                    dataClassGUID,
                                                  @RequestBody (required = false)
                                                      DeleteRequestBody requestBody)
    {
        return restAPI.detachDataClassDefinition(serverName, dataDefinitionGUID, dataClassGUID, requestBody);
    }


    /**
     * Connect an element that is part of a data design to a glossary term to show that the term should be used
     * as the semantic definition for the data values when interpreting the data definition.
     *
     * @param serverName         name of called server
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/semantic-definition/{glossaryTermGUID}/attach")
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
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-definitions/{dataDefinitionGUID}/semantic-definition/{glossaryTermGUID}/detach")
    @Operation(summary="detachSemanticDefinition",
            description="Detach a data definition from a glossary term.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))

    public VoidResponse detachSemanticDefinition(@PathVariable String                    serverName,
                                                 @PathVariable String                    dataDefinitionGUID,
                                                 @PathVariable String                    glossaryTermGUID,
                                                 @RequestBody (required = false)
                                                     DeleteRequestBody requestBody)
    {
        return restAPI.detachSemanticDefinition(serverName, dataDefinitionGUID, glossaryTermGUID, requestBody);
    }

    /**
     * Connect a certification type to a data structure to guide the survey action service (that checks the data
     * quality of a data resource as part of certifying it with the supplied certification type) to the definition
     * of the data structure to use as a specification of how the data should be both structured and (if
     * data classes are attached to the associated data fields using the DataClassDefinition relationship)
     * contain the valid values.
     *
     * @param serverName         name of called server
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/certification-types/{certificationTypeGUID}/data-structure-definition/{dataStructureGUID}/attach")
    @Operation(summary="linkCertificationTypeToDataStructure",
            description="Connect a certification type to a data structure to guide the survey action service (that checks the data quality of a data resource as part of certifying it with the supplied certification type) to the definition of the data structure to use as a specification of how the data should be both structured and (if data classes are attached to the associated data fields using the DataClassDefinition relationship) contain the valid values.",
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
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/certification-types/{certificationTypeGUID}/data-structure-definition/{dataStructureGUID}/detach")
    @Operation(summary="detachCertificationTypeToDataStructure",
            description="Detach a data structure from a certification type.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-specification"))
    public VoidResponse detachCertificationTypeToDataStructure(@PathVariable String                    serverName,
                                                               @PathVariable String                    certificationTypeGUID,
                                                               @PathVariable String                    dataStructureGUID,
                                                               @RequestBody (required = false)
                                                                   DeleteRequestBody requestBody)
    {
        return restAPI.detachCertificationTypeToDataStructure(serverName, certificationTypeGUID, dataStructureGUID, requestBody);
    }
}

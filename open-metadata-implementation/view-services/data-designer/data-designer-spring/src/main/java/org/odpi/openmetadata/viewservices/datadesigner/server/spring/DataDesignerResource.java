/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.datadesigner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworkservices.gaf.rest.AnyTimeRequestBody;
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
                                            NewDataStructureRequestBody requestBody)
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                            @RequestParam (required = false, defaultValue = "false")
                                            boolean                                 replaceAllProperties,
                                            @RequestBody (required = false)
                                            UpdateDataStructureRequestBody requestBody)
    {
        return restAPI.updateDataStructure(serverName, dataStructureGUID, replaceAllProperties, requestBody);
    }


    /**
     * Attach a data field to a data structure.
     *
     * @param serverName         name of called server
     * @param parentDataStructureGUID  unique identifier of the first segment
     * @param memberDataFieldGUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-structures/{parentDataStructureGUID}/member-data-fields/{memberDataFieldGUID}/attach")
    @Operation(summary="linkMemberDataField",
            description="Attach a data field to a data structure.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-structure"))

    public VoidResponse linkMemberDataField(@PathVariable
                                            String                                serverName,
                                            @PathVariable
                                            String parentDataStructureGUID,
                                            @PathVariable
                                            String memberDataFieldGUID,
                                            @RequestBody (required = false)
                                            MemberDataFieldRequestBody requestBody)
    {
        return restAPI.linkMemberDataField(serverName, parentDataStructureGUID, memberDataFieldGUID, requestBody);
    }


    /**
     * Detach a data field from a data structure.
     *
     * @param serverName         name of called server
     * @param parentDataStructureGUID  unique identifier of the first segment
     * @param memberDataFieldGUID      unique identifier of the second segment
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
                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachMemberDataField(serverName, parentDataStructureGUID, memberDataFieldGUID, requestBody);
    }


    /**
     * Delete a data structure.
     *
     * @param serverName         name of called server
     * @param dataStructureGUID  unique identifier of the element to delete
     * @param cascadedDelete ca data structures be deleted if segments are attached?
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
                                            @RequestParam(required = false, defaultValue = "false")
                                            boolean                   cascadedDelete,
                                            @RequestBody (required = false)
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteDataStructure(serverName, dataStructureGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of data structures with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                          @RequestParam (required = false, defaultValue = "0")
                                                          int                     startFrom,
                                                          @RequestParam (required = false, defaultValue = "0")
                                                          int                     pageSize,
                                                          @RequestBody (required = false)
                                                          FilterRequestBody requestBody)
    {
        return restAPI.getDataStructuresByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of data structure metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                     @RequestParam (required = false, defaultValue = "0")
                                                     int                     startFrom,
                                                     @RequestParam (required = false, defaultValue = "0")
                                                     int                     pageSize,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 startsWith,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 endsWith,
                                                     @RequestParam (required = false, defaultValue = "false")
                                                     boolean                 ignoreCase,
                                                     @RequestBody (required = false)
                                                     FilterRequestBody requestBody)
    {
        return restAPI.findDataStructures(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
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
                                                        AnyTimeRequestBody requestBody)
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
                                        NewDataFieldRequestBody requestBody)
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                                 replaceAllProperties,
                                        @RequestBody (required = false)
                                        UpdateDataFieldRequestBody requestBody)
    {
        return restAPI.updateDataField(serverName, dataFieldGUID, replaceAllProperties, requestBody);
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
                                             String                                serverName,
                                             @PathVariable
                                             String                                parentDataFieldGUID,
                                             @PathVariable
                                             String nestedDataFieldGUID,
                                             @RequestBody (required = false)
                                             MemberDataFieldRequestBody requestBody)
    {
        return restAPI.linkNestedDataFields(serverName, parentDataFieldGUID, nestedDataFieldGUID, requestBody);
    }


    /**
     * Detach two data fields from one another.
     *
     * @param serverName         name of called server
     * @param parentDataFieldGUID  unique identifier of the first segment
     * @param nestedDataFieldGUID      unique identifier of the second segment
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
                                               MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachNestedDataFields(serverName, parentDataFieldGUID, nestedDataFieldGUID, requestBody);
    }


    /**
     * Delete a data field.
     *
     * @param serverName         name of called server
     * @param dataFieldGUID  unique identifier of the element to delete
     * @param cascadedDelete ca data fields be deleted if segments are attached?
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
                                        @RequestParam(required = false, defaultValue = "false")
                                        boolean                   cascadedDelete,
                                        @RequestBody (required = false)
                                        MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteDataField(serverName, dataFieldGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of data fields with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                                  @RequestParam (required = false, defaultValue = "0")
                                                  int                     startFrom,
                                                  @RequestParam (required = false, defaultValue = "0")
                                                  int                     pageSize,
                                                  @RequestBody (required = false)
                                                  FilterRequestBody requestBody)
    {
        return restAPI.getDataFieldsByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of data field metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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
                                             @RequestParam (required = false, defaultValue = "0")
                                             int                     startFrom,
                                             @RequestParam (required = false, defaultValue = "0")
                                             int                     pageSize,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                 startsWith,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                 endsWith,
                                             @RequestParam (required = false, defaultValue = "false")
                                             boolean                 ignoreCase,
                                             @RequestBody (required = false)
                                             FilterRequestBody requestBody)
    {
        return restAPI.findDataFields(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
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
                                                AnyTimeRequestBody requestBody)
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
                                        String                               serverName,
                                        @RequestBody (required = false)
                                        NewDataClassRequestBody requestBody)
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
     * @param replaceAllProperties flag to indicate whether to completely replace the existing properties with the new properties, or just update
     *                          the individual properties specified on the request.
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
                                        @RequestParam (required = false, defaultValue = "false")
                                        boolean                                 replaceAllProperties,
                                        @RequestBody (required = false)
                                        UpdateDataClassRequestBody requestBody)
    {
        return restAPI.updateDataClass(serverName, dataClassGUID, replaceAllProperties, requestBody);
    }


    /**
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first segment
     * @param childDataClassGUID      unique identifier of the second segment
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
                                            MetadataSourceRequestBody requestBody)
    {
        return restAPI.linkNestedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Detach two nested data classes from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first segment
     * @param childDataClassGUID      unique identifier of the second segment
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
                                              MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachNestedDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Connect two data classes to show that one provides a more specialist evaluation.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first segment
     * @param childDataClassGUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/specialist-data-classes/{childDataClassGUID}/attach")
    @Operation(summary="linkSpecialistDataClass",
            description="Connect two data classes to show that one provides a more specialist evaluation.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse linkSpecialistDataClass(@PathVariable
                                                String                                serverName,
                                                @PathVariable
                                                String                                parentDataClassGUID,
                                                @PathVariable
                                                String                                childDataClassGUID,
                                                @RequestBody (required = false)
                                                MetadataSourceRequestBody requestBody)
    {
        return restAPI.linkSpecialistDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Detach two data classes from one another.
     *
     * @param serverName         name of called server
     * @param parentDataClassGUID  unique identifier of the first segment
     * @param childDataClassGUID      unique identifier of the second segment
     * @param requestBody  description of the relationship.
     *
     * @return void or
     *  InvalidParameterException  one of the parameters is null or invalid.
     *  PropertyServerException    there is a problem retrieving information from the property server(s).
     *  UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    @PostMapping(path = "/data-classes/{parentDataClassGUID}/specialist-data-classes/{childDataClassGUID}/detach")
    @Operation(summary="detachSpecialistDataClass",
            description="Detach two data classes from one another.",
            externalDocs=@ExternalDocumentation(description="Further Information",
                    url="https://egeria-project.org/concepts/data-class"))

    public VoidResponse detachSpecialistDataClass(@PathVariable
                                                  String                    serverName,
                                                  @PathVariable
                                                  String                    parentDataClassGUID,
                                                  @PathVariable
                                                  String                    childDataClassGUID,
                                                  @RequestBody (required = false)
                                                  MetadataSourceRequestBody requestBody)
    {
        return restAPI.detachSpecialistDataClass(serverName, parentDataClassGUID, childDataClassGUID, requestBody);
    }


    /**
     * Delete a data class.
     *
     * @param serverName         name of called server
     * @param dataClassGUID  unique identifier of the element to delete
     * @param cascadedDelete ca data classes be deleted if segments are attached?
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
                                        @RequestParam(required = false, defaultValue = "false")
                                        boolean                   cascadedDelete,
                                        @RequestBody (required = false)
                                        MetadataSourceRequestBody requestBody)
    {
        return restAPI.deleteDataClass(serverName, dataClassGUID, cascadedDelete, requestBody);
    }


    /**
     * Returns the list of data classes with a particular name.
     *
     * @param serverName name of the service to route the request to
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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

    public DataClassesResponse getDataClassesByName(@PathVariable
                                                    String            serverName,
                                                    @RequestParam (required = false, defaultValue = "0")
                                                    int                     startFrom,
                                                    @RequestParam (required = false, defaultValue = "0")
                                                    int                     pageSize,
                                                    @RequestBody (required = false)
                                                    FilterRequestBody requestBody)
    {
        return restAPI.getDataClassesByName(serverName, startFrom, pageSize, requestBody);
    }


    /**
     * Retrieve the list of data class metadata elements that contain the search string.
     *
     * @param serverName name of the service to route the request to
     * @param startsWith does the value start with the supplied string?
     * @param endsWith does the value end with the supplied string?
     * @param ignoreCase should the search ignore case?
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
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

    public DataClassesResponse findDataClasses(@PathVariable
                                               String                  serverName,
                                               @RequestParam (required = false, defaultValue = "0")
                                               int                     startFrom,
                                               @RequestParam (required = false, defaultValue = "0")
                                               int                     pageSize,
                                               @RequestParam (required = false, defaultValue = "false")
                                               boolean                 startsWith,
                                               @RequestParam (required = false, defaultValue = "false")
                                               boolean                 endsWith,
                                               @RequestParam (required = false, defaultValue = "false")
                                               boolean                 ignoreCase,
                                               @RequestBody (required = false)
                                               FilterRequestBody requestBody)
    {
        return restAPI.findDataClasses(serverName, startsWith, endsWith, ignoreCase, startFrom, pageSize, requestBody);
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

    public DataClassResponse getDataClassByGUID(@PathVariable
                                                String             serverName,
                                                @PathVariable
                                                String             dataClassGUID,
                                                @RequestBody (required = false)
                                                AnyTimeRequestBody requestBody)
    {
        return restAPI.getDataClassByGUID(serverName, dataClassGUID, requestBody);
    }
}

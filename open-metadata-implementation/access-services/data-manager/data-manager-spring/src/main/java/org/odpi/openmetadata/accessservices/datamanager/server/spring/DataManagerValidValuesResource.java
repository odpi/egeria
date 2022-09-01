/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.datamanager.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.datamanager.rest.ExternalSourceRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.rest.ReferenceableRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.rest.RelatedElementListResponse;
import org.odpi.openmetadata.accessservices.datamanager.rest.RelationshipRequestBody;
import org.odpi.openmetadata.accessservices.datamanager.rest.ValidValueListResponse;
import org.odpi.openmetadata.accessservices.datamanager.rest.ValidValueResponse;
import org.odpi.openmetadata.accessservices.datamanager.server.ValidValuesRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NameRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * The DataManagerValidValuesResource provides a Spring based server-side REST API
 * that supports the ValidValueManagementInterface.   It delegates each request to the
 * ValidValueRESTServices.  This provides the server-side implementation of the Community Profile Open Metadata
 * Assess Service (OMAS) which is used to manage information about people, roles and organizations.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/data-manager/users/{userId}")

@Tag(name="Data Manager OMAS",
     description="The Data Manager OMAS provides APIs for tools and applications wishing to manage metadata relating to data managers " +
                         "such as database servers, event brokers, content managers and file systems.",
     externalDocs=@ExternalDocumentation(description="Data Manager Open Metadata Access Service (OMAS)",
                                         url="https://egeria-project.org/services/omas/data-manager/overview/"))

public class DataManagerValidValuesResource
{
    private final ValidValuesRESTServices restAPI = new ValidValuesRESTServices();


    /**
     * Default constructor
     */
    public DataManagerValidValuesResource()
    {
    }


    /**
     * Create a new metadata element to represent a validValues.
     *
     * @param serverName  name of the service to route the request to.
     * @param userId      calling user
     * @param requestBody properties to store
     *
     * @return unique identifier of the new metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values")

    public GUIDResponse createValidValue(@PathVariable String                   serverName,
                                         @PathVariable String                   userId,
                                         @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.createValidValue(serverName, userId, requestBody);
    }


    /**
     * Update the metadata element representing a validValues.
     *
     * @param serverName     name of the service to route the request to.
     * @param userId         calling user
     * @param validValueGUID unique identifier of the metadata element to update
     * @param isMergeUpdate  should the new properties be merged with the existing properties of overlay them?
     * @param requestBody    new properties for this element
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{validValueGUID}")

    public VoidResponse updateValidValue(@PathVariable String                   serverName,
                                         @PathVariable String                   userId,
                                         @PathVariable String                   validValueGUID,
                                         @RequestParam boolean                  isMergeUpdate,
                                         @RequestBody  ReferenceableRequestBody requestBody)
    {
        return restAPI.updateValidValue(serverName, userId, validValueGUID, isMergeUpdate, requestBody);
    }


    /**
     * Create a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param serverName           name of the service to route the request to.
     * @param userId               calling user
     * @param validValueSetGUID    unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     * @param requestBody          relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{validValueSetGUID}/valid-value-members/{validValueMemberGUID}")

    public VoidResponse setupValidValueMember(@PathVariable String                  serverName,
                                              @PathVariable String                  userId,
                                              @PathVariable String                  validValueSetGUID,
                                              @PathVariable String                  validValueMemberGUID,
                                              @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupValidValueMember(serverName, userId, validValueSetGUID, validValueMemberGUID, requestBody);
    }


    /**
     * Remove a membership relationship between a validValue and a validValueSet that it belongs to.
     *
     * @param serverName           name of the service to route the request to.
     * @param userId               calling user
     * @param validValueSetGUID    unique identifier of the valid value set
     * @param validValueMemberGUID unique identifier of the member
     * @param requestBody          external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{validValueSetGUID}/valid-value-members/{validValueMemberGUID}/delete")

    public VoidResponse clearValidValueMember(@PathVariable String                    serverName,
                                              @PathVariable String                    userId,
                                              @PathVariable String                    validValueSetGUID,
                                              @PathVariable String                    validValueMemberGUID,
                                              @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearValidValueMember(serverName, userId, validValueSetGUID, validValueMemberGUID, requestBody);
    }


    /**
     * Create a valid value assignment relationship between an element and a valid value (typically, a valid value set) to show that
     * the valid value defines the values that can be stored in the data item that the element represents.
     *
     * @param serverName     name of the service to route the request to.
     * @param userId         calling user
     * @param elementGUID    unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody    relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{elementGUID}/valid-values/{validValueGUID}")

    public VoidResponse setupValidValues(@PathVariable String                  serverName,
                                         @PathVariable String                  userId,
                                         @PathVariable String                  elementGUID,
                                         @PathVariable String                  validValueGUID,
                                         @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupValidValues(serverName, userId, elementGUID, validValueGUID, requestBody);
    }


    /**
     * Remove a valid value assignment relationship between an element and a valid value.
     *
     * @param serverName     name of the service to route the request to.
     * @param userId         calling user
     * @param elementGUID    unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody    external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{elementGUID}/valid-values/{validValueGUID}/delete")

    public VoidResponse clearValidValues(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    elementGUID,
                                         @PathVariable String                    validValueGUID,
                                         @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearValidValues(serverName, userId, elementGUID, validValueGUID, requestBody);
    }


    /**
     * Create a reference value assignment relationship between an element and a valid value to show that
     * the valid value is a semiformal tag/classification.
     *
     * @param serverName     name of the service to route the request to.
     * @param userId         calling user
     * @param elementGUID    unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody    relationship properties
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{elementGUID}/reference-value-tags/{validValueGUID}")

    public VoidResponse setupReferenceValueTag(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @PathVariable String                  elementGUID,
                                               @PathVariable String                  validValueGUID,
                                               @RequestBody  RelationshipRequestBody requestBody)
    {
        return restAPI.setupReferenceValueTag(serverName, userId, elementGUID, validValueGUID, requestBody);
    }


    /**
     * Remove a reference value assignment relationship between an element and a valid value.
     *
     * @param serverName     name of the service to route the request to.
     * @param userId         calling user
     * @param elementGUID    unique identifier of the element
     * @param validValueGUID unique identifier of the valid value
     * @param requestBody    external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{elementGUID}/reference-value-tags/{validValueGUID}/delete")

    public VoidResponse clearReferenceValueTag(@PathVariable String                    serverName,
                                               @PathVariable String                    userId,
                                               @PathVariable String                    elementGUID,
                                               @PathVariable String                    validValueGUID,
                                               @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.clearReferenceValueTag(serverName, userId, elementGUID, validValueGUID, requestBody);
    }


    /**
     * Remove the metadata element representing a validValue.
     *
     * @param serverName     name of the service to route the request to.
     * @param userId         calling user
     * @param validValueGUID unique identifier of the metadata element to remove
     * @param requestBody    external source identifiers
     *
     * @return void or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/delete")

    public VoidResponse removeValidValue(@PathVariable String                    serverName,
                                         @PathVariable String                    userId,
                                         @PathVariable String                    validValueGUID,
                                         @RequestBody  ExternalSourceRequestBody requestBody)
    {
        return restAPI.removeValidValue(serverName, userId, validValueGUID, requestBody);
    }


    /**
     * Retrieve the list of validValue metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param serverName  name of the service to route the request to.
     * @param userId      calling user
     * @param requestBody string to find in the properties
     * @param startFrom   paging start point
     * @param pageSize    maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/by-search-string")

    public ValidValueListResponse findValidValues(@PathVariable String                  serverName,
                                                  @PathVariable String                  userId,
                                                  @RequestParam int                     startFrom,
                                                  @RequestParam int                     pageSize,
                                                  @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findValidValues(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Retrieve the list of validValue metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName  name of the service to route the request to.
     * @param userId      calling user
     * @param requestBody name to search for
     * @param startFrom   paging start point
     * @param pageSize    maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @PostMapping(path = "/valid-values/by-name")

    public ValidValueListResponse getValidValuesByName(@PathVariable String          serverName,
                                                       @PathVariable String          userId,
                                                       @RequestParam int             startFrom,
                                                       @RequestParam int             pageSize,
                                                       @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getValidValuesByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param serverName        called server
     * @param userId            calling user
     * @param validValueSetGUID unique identifier of the valid value set
     * @param startFrom         index of the list to start from (0 for start)
     * @param pageSize          maximum number of elements to return.
     *
     * @return list of valid value beans
     * <p>
     * InvalidParameterException name or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/valid-values/members/{validValueSetGUID}")

    public ValidValueListResponse getValidValueSetMembers(@PathVariable String serverName,
                                                          @PathVariable String userId,
                                                          @PathVariable String validValueSetGUID,
                                                          @RequestParam int    startFrom,
                                                          @RequestParam int    pageSize)
    {
        return restAPI.getValidValueSetMembers(serverName, userId, validValueSetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param serverName     called server
     * @param userId         calling user
     * @param validValueGUID unique identifier of the valid value
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of valid value beans
     * <p>
     * InvalidParameterException name or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/valid-values/sets/{validValueGUID}")

    public ValidValueListResponse getSetsForValidValue(@PathVariable String serverName,
                                                       @PathVariable String userId,
                                                       @PathVariable String validValueGUID,
                                                       @RequestParam int    startFrom,
                                                       @RequestParam int    pageSize)
    {
        return restAPI.getSetsForValidValue(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid value set linked to an element as its
     * set of valid values.
     *
     * @param serverName  name of the service to route the request to.
     * @param userId      calling user
     * @param elementGUID unique identifier for the element using the valid value set
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/valid-values/by-consumer/{elementGUID}")

    public ValidValueResponse getValidValuesForConsumer(@PathVariable String serverName,
                                                        @PathVariable String userId,
                                                        @PathVariable String elementGUID)
    {
        return restAPI.getValidValuesForConsumer(serverName, userId, elementGUID);
    }


    /**
     * Return information about the consumers linked to a valid value.
     *
     * @param serverName     called server
     * @param userId         calling user
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of matching person roles
     * <p>
     * InvalidParameterException name or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/consumers")

    public RelatedElementListResponse getConsumersOfValidValue(@PathVariable String serverName,
                                                               @PathVariable String userId,
                                                               @PathVariable String validValueGUID,
                                                               @RequestParam int    startFrom,
                                                               @RequestParam int    pageSize)
    {
        return restAPI.getConsumersOfValidValue(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Return information about the valid values linked as reference value tags to an element.
     *
     * @param serverName  name of the service to route the request to.
     * @param userId      calling user
     * @param elementGUID unique identifier for the element using the valid value set
     * @param startFrom   index of the list to start from (0 for start)
     * @param pageSize    maximum number of elements to return.
     *
     * @return matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/valid-values/by-reference-value-tags/{elementGUID}")

    public ValidValueListResponse getReferenceValues(@PathVariable String serverName,
                                                     @PathVariable String userId,
                                                     @PathVariable String elementGUID,
                                                     @RequestParam int    startFrom,
                                                     @RequestParam int    pageSize)
    {
        return restAPI.getReferenceValues(serverName, userId, elementGUID, startFrom, pageSize);
    }


    /**
     * Return information about the consumers linked to a valid value.
     *
     * @param serverName     called server
     * @param userId         calling user
     * @param validValueGUID unique identifier for the validValue
     * @param startFrom      index of the list to start from (0 for start)
     * @param pageSize       maximum number of elements to return.
     *
     * @return list of matching person roles
     * <p>
     * InvalidParameterException name or userId is null
     * PropertyServerException problem accessing property server
     * UserNotAuthorizedException security access problem
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/by-reference-value-tag-assignees")

    public RelatedElementListResponse getAssigneesOfReferenceValue(@PathVariable String serverName,
                                                                   @PathVariable String userId,
                                                                   @PathVariable String validValueGUID,
                                                                   @RequestParam int    startFrom,
                                                                   @RequestParam int    pageSize)
    {
        return restAPI.getAssigneesOfReferenceValue(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Retrieve the list of validValue metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param serverName name of the service to route the request to.
     * @param userId     calling user
     * @param startFrom  paging start point
     * @param pageSize   maximum results that can be returned
     *
     * @return list of matching metadata elements or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/valid-values")

    public ValidValueListResponse getAllValidValues(@PathVariable String serverName,
                                                    @PathVariable String userId,
                                                    @RequestParam int    startFrom,
                                                    @RequestParam int    pageSize)
    {
        return restAPI.getAllValidValues(serverName, userId, startFrom, pageSize);
    }


    /**
     * Retrieve the validValue metadata element with the supplied unique identifier.
     *
     * @param serverName name of the service to route the request to.
     * @param userId     calling user
     * @param guid       unique identifier of the requested metadata element
     *
     * @return matching metadata element or
     * InvalidParameterException  one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to issue this request or
     * PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    @GetMapping(path = "/valid-values/{guid}")

    public ValidValueResponse getValidValueByGUID(@PathVariable String serverName,
                                                  @PathVariable String userId,
                                                  @PathVariable String guid)
    {
        return restAPI.getValidValueByGUID(serverName, userId, guid);
    }
}
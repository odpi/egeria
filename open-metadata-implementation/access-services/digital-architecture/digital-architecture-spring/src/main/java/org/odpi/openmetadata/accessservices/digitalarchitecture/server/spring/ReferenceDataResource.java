/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.server.spring;


import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.digitalarchitecture.properties.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.rest.*;
import org.odpi.openmetadata.accessservices.digitalarchitecture.server.ReferenceDataRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.springframework.web.bind.annotation.*;


/**
 * ReferenceDataResource provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/digital-architecture/users/{userId}")

@Tag(name="Digital Architecture OMAS",
        description="The Digital Architecture OMAS provides APIs for tools and applications managing the design of data structures, software and the IT infrastructure that supports the operations of the organization.",
        externalDocs=@ExternalDocumentation(description="Digital Architecture Open Metadata Access Service (OMAS)",
                url="https://egeria-project.org/services/omas/digital-architecture/overview/"))

public class ReferenceDataResource
{
    private final ReferenceDataRESTServices restAPI = new ReferenceDataRESTServices();


    /**
     * Default constructor
     */
    public ReferenceDataResource()
    {
    }


    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param requestBody parameters for the new object.
     *
     * @return unique identifier for the new set or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/sets")

    public GUIDResponse createValidValueSet(@PathVariable String               serverName,
                                            @PathVariable String               userId,
                                            @RequestBody  ValidValueProperties requestBody)
    {
        return restAPI.createValidValueSet(serverName, userId, requestBody);
    }


    /**
     * Create a new valid value definition.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param isDefaultValue     is this the default value for the set?
     * @param requestBody parameters for the new object.
     *
     * @return unique identifier for the new definition
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/sets/{setGUID}")

    public GUIDResponse  createValidValueDefinition(@PathVariable String               serverName,
                                                    @PathVariable String               userId,
                                                    @PathVariable String               setGUID,
                                                    @RequestParam
                                                            (required = false, defaultValue = "false")
                                                                  boolean              isDefaultValue,
                                                    @RequestBody  ValidValueProperties requestBody)
    {
        return restAPI.createValidValueDefinition(serverName, userId, setGUID, isDefaultValue, requestBody);
    }


    /**
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param requestBody parameters to update.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/update")

    public VoidResponse    updateValidValue(@PathVariable String               serverName,
                                            @PathVariable String               userId,
                                            @PathVariable String               validValueGUID,
                                            @RequestParam(required = false, defaultValue = "true")
                                                          boolean              isMergeUpdate,
                                            @RequestBody  ValidValueProperties requestBody)
    {
        return restAPI.updateValidValue(serverName, userId, validValueGUID, isMergeUpdate, requestBody);
    }


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of the value to delete
     * @param qualifiedName unique name of the value to delete.  This is used to verify that
     *                      the correct valid value is being deleted.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/delete")

    public VoidResponse    deleteValidValue(@PathVariable String   serverName,
                                            @PathVariable String   userId,
                                            @PathVariable String   validValueGUID,
                                            @RequestBody  String   qualifiedName)
    {
        return restAPI.deleteValidValue(serverName, userId, validValueGUID, qualifiedName);
    }


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param isDefaultValue     is this the default value for the set?
     * @param requestBody null request body supplied to satisfy REST protocol
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/sets/{setGUID}/members/{validValueGUID}")

    public VoidResponse    attachValidValueToSet(@PathVariable                  String          serverName,
                                                 @PathVariable                  String          userId,
                                                 @PathVariable                  String          setGUID,
                                                 @PathVariable                  String          validValueGUID,
                                                 @RequestParam
                                                         (required = false, defaultValue = "false")
                                                                                boolean isDefaultValue,
                                                 @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.attachValidValueToSet(serverName, userId, setGUID, validValueGUID, isDefaultValue, requestBody);
    }


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/sets/{setGUID}/members/{validValueGUID}/delete")

    public VoidResponse    detachValidValueFromSet(@PathVariable                  String          serverName,
                                                   @PathVariable                  String          userId,
                                                   @PathVariable                  String          setGUID,
                                                   @PathVariable                  String          validValueGUID,
                                                   @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.detachValidValueFromSet(serverName, userId, setGUID, validValueGUID, requestBody);
    }


    /**
     * Link a valid value to an asset that provides the implementation.  Typically this method is
     * used to link a valid value set to a code table.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that implements the valid value.
     * @param requestBody implementation relationship properties
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/implementations/{assetGUID}")

    public VoidResponse  linkValidValueToImplementation(@PathVariable String                    serverName,
                                                        @PathVariable String                    userId,
                                                        @PathVariable String                    validValueGUID,
                                                        @PathVariable String                    assetGUID,
                                                        @RequestBody  ValidValuesImplProperties requestBody)
    {
        return restAPI.linkValidValueToImplementation(serverName, userId, validValueGUID, assetGUID, requestBody);
    }


    /**
     * Add the ReferenceData classification to an asset.  IF the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/assets/{assetGUID}/classify-as-reference-data")

    public VoidResponse  classifyAssetAsReferenceData(@PathVariable                  String          serverName,
                                                      @PathVariable                  String          userId,
                                                      @PathVariable                  String          assetGUID,
                                                      @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.classifyAssetAsReferenceData(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that used to implement the valid value.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/implementations/{assetGUID}/delete")

    public VoidResponse  unlinkValidValueFromImplementation(@PathVariable                  String          serverName,
                                                            @PathVariable                  String          userId,
                                                            @PathVariable                  String          validValueGUID,
                                                            @PathVariable                  String          assetGUID,
                                                            @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unlinkValidValueFromImplementation(serverName, userId, validValueGUID, assetGUID, requestBody);
    }


    /**
     * Remove the ReferenceData classification form an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/assets/{assetGUID}/declassify-as-reference-data")

    public VoidResponse  declassifyAssetAsReferenceData(@PathVariable                  String          serverName,
                                                        @PathVariable                  String          userId,
                                                        @PathVariable                  String          assetGUID,
                                                        @RequestBody(required = false) NullRequestBody requestBody)
    {

        return restAPI.declassifyAssetAsReferenceData(serverName, userId, assetGUID, requestBody);
    }


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to link to.
     * @param requestBody request body supplied to pass the strictRequirement flag
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/consumers/{consumerGUID}")

    public VoidResponse    assignValidValueToConsumer(@PathVariable String                         serverName,
                                                      @PathVariable String                         userId,
                                                      @PathVariable String                         validValueGUID,
                                                      @PathVariable String                         consumerGUID,
                                                      @RequestBody  ValidValueAssignmentProperties requestBody)
    {
        return restAPI.assignValidValueToConsumer(serverName, userId, validValueGUID, consumerGUID, requestBody);
    }


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to remove the link from.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/consumers/{consumerGUID}/delete")

    public VoidResponse unassignValidValueFromConsumer(@PathVariable                  String          serverName,
                                                       @PathVariable                  String          userId,
                                                       @PathVariable                  String          validValueGUID,
                                                       @PathVariable                  String          consumerGUID,
                                                       @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unassignValidValueFromConsumer(serverName, userId, validValueGUID, consumerGUID, requestBody);
    }


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to link to.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/reference-values/{validValueGUID}/items/{referenceableGUID}")

    public VoidResponse    assignReferenceValueToItem(@PathVariable String                             serverName,
                                                      @PathVariable String                             userId,
                                                      @PathVariable String                             validValueGUID,
                                                      @PathVariable String                             referenceableGUID,
                                                      @RequestBody  ReferenceValueAssignmentProperties requestBody)
    {
        return restAPI.assignReferenceValueToItem(serverName, userId, validValueGUID, referenceableGUID, requestBody);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/reference-values/{validValueGUID}/items/{referenceableGUID}/delete")

    public VoidResponse unassignReferenceValueFromItem(@PathVariable                  String          serverName,
                                                       @PathVariable                  String          userId,
                                                       @PathVariable                  String          validValueGUID,
                                                       @PathVariable                  String          referenceableGUID,
                                                       @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unassignReferenceValueFromItem(serverName, userId, validValueGUID, referenceableGUID, requestBody);
    }


    /**
     * Link together 2 valid values from different sets that have equivalent values/meanings.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value to link to.
     * @param requestBody properties for the relationship.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValue1GUID}/map/{validValue2GUID}")

    public VoidResponse    mapValidValues(@PathVariable String                       serverName,
                                          @PathVariable String                       userId,
                                          @PathVariable String                       validValue1GUID,
                                          @PathVariable String                       validValue2GUID,
                                          @RequestBody  ValidValuesMappingProperties requestBody)
    {
        return restAPI.mapValidValues(serverName, userId, validValue1GUID, validValue2GUID, requestBody);
    }


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value element to remove the link from.
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValue1GUID}/map/{validValue2GUID}/delete")

    public VoidResponse  unmapValidValues(@PathVariable                  String          serverName,
                                          @PathVariable                  String          userId,
                                          @PathVariable                  String          validValue1GUID,
                                          @PathVariable                  String          validValue2GUID,
                                          @RequestBody(required = false) NullRequestBody requestBody)
    {
        return restAPI.unmapValidValues(serverName, userId, validValue1GUID, validValue2GUID, requestBody);
    }


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of the valid value.
     *
     * @return Valid value bean or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}")

    public ValidValueResponse getValidValueByGUID(@PathVariable String  serverName,
                                                  @PathVariable String  userId,
                                                  @PathVariable String  validValueGUID)
    {
        return restAPI.getValidValueByGUID(serverName, userId, validValueGUID);
    }


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param requestBody qualified name of the valid value.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return Valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/by-name")

    public ValidValuesResponse getValidValueByName(@PathVariable String          serverName,
                                                   @PathVariable String          userId,
                                                   @RequestParam int             startFrom,
                                                   @RequestParam int             pageSize,
                                                   @RequestBody  NameRequestBody requestBody)
    {
        return restAPI.getValidValueByName(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param requestBody string value to look for - may contain RegEx characters.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/by-search-string")

    public ValidValuesResponse findValidValues(@PathVariable String                  serverName,
                                               @PathVariable String                  userId,
                                               @RequestParam int                     startFrom,
                                               @RequestParam int                     pageSize,
                                               @RequestBody  SearchStringRequestBody requestBody)
    {
        return restAPI.findValidValues(serverName, userId, requestBody, startFrom, pageSize);
    }


    /**
     * Page through the members of a valid value set.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/sets/{validValueSetGUID}/members")

    public ValidValuesResponse getValidValueSetMembers(@PathVariable String  serverName,
                                                       @PathVariable String  userId,
                                                       @PathVariable String  validValueSetGUID,
                                                       @RequestParam int     startFrom,
                                                       @RequestParam int     pageSize)
    {
        return restAPI.getValidValueSetMembers(serverName, userId, validValueSetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/set-membership")

    public ValidValuesResponse getSetsForValidValue(@PathVariable String  serverName,
                                                    @PathVariable String  userId,
                                                    @PathVariable String  validValueGUID,
                                                    @RequestParam int     startFrom,
                                                    @RequestParam int     pageSize)
    {
        return restAPI.getSetsForValidValue(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of consumers beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/consumers")

    public ValidValueAssignmentConsumersResponse getValidValuesAssignmentConsumers(@PathVariable String  serverName,
                                                                                   @PathVariable String  userId,
                                                                                   @PathVariable String  validValueGUID,
                                                                                   @RequestParam int     startFrom,
                                                                                   @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesAssignmentConsumers(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of consumers beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/referenceables/{referenceableGUID}/valid-value-assignments")

    public ValidValueAssignmentDefinitionsResponse getValidValuesAssignmentDefinition(@PathVariable String  serverName,
                                                                                      @PathVariable String  userId,
                                                                                      @PathVariable String  referenceableGUID,
                                                                                      @RequestParam int     startFrom,
                                                                                      @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesAssignmentDefinition(serverName, userId, referenceableGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of implementations for a valid value.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of asset beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/implementations")

    public ValidValuesImplAssetsResponse getValidValuesImplementationAssets(@PathVariable String  serverName,
                                                                            @PathVariable String  userId,
                                                                            @PathVariable String  validValueGUID,
                                                                            @RequestParam int     startFrom,
                                                                            @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesImplementationAssets(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of valid values defining the content of a reference data asset.
     * This is always called from the assetHandler after it has checked that the asset is in the right zone.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param assetGUID unique identifier of asset to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/assets/{assetGUID}/valid-value-implementation-definitions")

    public ValidValuesImplDefinitionsResponse getValidValuesImplementationDefinitions(@PathVariable String  serverName,
                                                                                      @PathVariable String  userId,
                                                                                      @PathVariable String  assetGUID,
                                                                                      @RequestParam int     startFrom,
                                                                                      @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesImplementationDefinitions(serverName, userId, assetGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of mappings for a valid value.  These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/mapped-values")

    public ValidValueMappingsResponse getValidValueMappings(@PathVariable String  serverName,
                                                            @PathVariable String  userId,
                                                            @PathVariable String  validValueGUID,
                                                            @RequestParam int     startFrom,
                                                            @RequestParam int     pageSize)
    {
        return restAPI.getValidValueMappings(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of mapping relationships associated with a valid value.
     * These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/map-relationships")

    public ValidValuesMappingsResponse getValidValuesMappings(@PathVariable String  serverName,
                                                              @PathVariable String  userId,
                                                              @PathVariable String  validValueGUID,
                                                              @RequestParam int     startFrom,
                                                              @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesMappings(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of referenceable beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/valid-values/{validValueGUID}/reference-values/assigned-items")

    public ReferenceValueAssignmentItemsResponse getReferenceValueAssignedItems(@PathVariable String  serverName,
                                                                                @PathVariable String  userId,
                                                                                @PathVariable String  validValueGUID,
                                                                                @RequestParam int     startFrom,
                                                                                @RequestParam int     pageSize)
    {
        return restAPI.getReferenceValueAssignedItems(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @GetMapping(path = "/referenceables/{referenceableGUID}/reference-value-assignments")

    public ReferenceValueAssignmentDefinitionsResponse getReferenceValueAssignments(@PathVariable String  serverName,
                                                                                    @PathVariable String  userId,
                                                                                    @PathVariable String  referenceableGUID,
                                                                                    @RequestParam int     startFrom,
                                                                                    @RequestParam int     pageSize)
    {
        return restAPI.getReferenceValueAssignments(serverName, userId, referenceableGUID, startFrom, pageSize);
    }
}

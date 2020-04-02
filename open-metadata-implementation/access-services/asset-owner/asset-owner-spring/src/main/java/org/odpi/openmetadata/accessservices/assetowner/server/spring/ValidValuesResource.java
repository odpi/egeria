/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.server.spring;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.odpi.openmetadata.accessservices.assetowner.rest.ValidValuesRequestBody;
import org.odpi.openmetadata.accessservices.assetowner.server.ValidValuesRESTServices;
import org.odpi.openmetadata.commonservices.ffdc.rest.BooleanRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NullRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.AssetsResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ValidValueConsumersResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ValidValueResponse;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest.ValidValuesResponse;
import org.springframework.web.bind.annotation.*;


/**
 * ValidValuesResource provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all of the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
@RestController
@RequestMapping("/servers/{serverName}/open-metadata/access-services/asset-owner/users/{userId}")

@Tag(name="Asset Owner OMAS", description="The Asset Owner OMAS provides APIs and notifications for tools and applications supporting the work of Asset Owners in protecting and enhancing their assets.\n" +
        "\n", externalDocs=@ExternalDocumentation(description="Asset Owner Open Metadata Access Service (OMAS)",url="https://egeria.odpi.org/open-metadata-implementation/access-services/asset-owner/"))

public class ValidValuesResource
{
    private ValidValuesRESTServices restAPI = new ValidValuesRESTServices();


    /**
     * Default constructor
     */
    public ValidValuesResource()
    {
    }


    /*
     * ==============================================
     * AssetOnboardingValidValues
     * ==============================================
     */

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
    @PostMapping(path = "/valid-values/new-set")

    public GUIDResponse createValidValueSet(@PathVariable String                 serverName,
                                            @PathVariable String                 userId,
                                            @RequestBody ValidValuesRequestBody requestBody)
    {
        return restAPI.createValidValueSet(serverName, userId, requestBody);
    }


    /**
     * Create a new valid value definition.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param requestBody parameters for the new object.
     *
     * @return unique identifier for the new definition
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/new-definition")

    public GUIDResponse  createValidValueDefinition(@PathVariable String                 serverName,
                                                    @PathVariable String                 userId,
                                                    @PathVariable String                 setGUID,
                                                    @RequestBody  ValidValuesRequestBody requestBody)
    {
        return restAPI.createValidValueDefinition(serverName, userId, setGUID, requestBody);
    }


    /**
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param serverName name of calling server
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param requestBody parameters to update.
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/update")

    public VoidResponse    updateValidValue(@PathVariable String                 serverName,
                                            @PathVariable String                 userId,
                                            @PathVariable String                 validValueGUID,
                                            @RequestBody  ValidValuesRequestBody requestBody)
    {
        return restAPI.updateValidValue(serverName, userId, validValueGUID, requestBody);
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
     * @param requestBody null request body supplied to satisfy REST protocol
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/sets/{setGUID}/members/{validValueGUID}")

    public VoidResponse    attachValidValueToSet(@PathVariable String          serverName,
                                                 @PathVariable String          userId,
                                                 @PathVariable String          setGUID,
                                                 @PathVariable String          validValueGUID,
                                                 @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.attachValidValueToSet(serverName, userId, setGUID, validValueGUID, requestBody);
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

    public VoidResponse    detachValidValueFromSet(@PathVariable String          serverName,
                                                   @PathVariable String          userId,
                                                   @PathVariable String          setGUID,
                                                   @PathVariable String          validValueGUID,
                                                   @RequestBody  NullRequestBody requestBody)
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
     * @param requestBody null request body supplied to satisfy REST protocol
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/implementations/{assetGUID}")

    public VoidResponse  linkValidValueToImplementation(@PathVariable String          serverName,
                                                        @PathVariable String          userId,
                                                        @PathVariable String          validValueGUID,
                                                        @PathVariable String          assetGUID,
                                                        @RequestBody  NullRequestBody requestBody)
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

    public VoidResponse  classifyAssetAsReferenceData(@PathVariable String          serverName,
                                                      @PathVariable String          userId,
                                                      @PathVariable String          assetGUID,
                                                      @RequestBody  NullRequestBody requestBody)
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

    public VoidResponse  unlinkValidValueFromImplementation(@PathVariable String          serverName,
                                                            @PathVariable String          userId,
                                                            @PathVariable String          validValueGUID,
                                                            @PathVariable String          assetGUID,
                                                            @RequestBody  NullRequestBody requestBody)
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

    public VoidResponse  declassifyAssetAsReferenceData(@PathVariable String          serverName,
                                                        @PathVariable String          userId,
                                                        @PathVariable String          assetGUID,
                                                        @RequestBody  NullRequestBody requestBody)
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
     * @param requestBody boolean request body supplied to pass the strictRequirement flag
     *
     * @return void or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/{validValueGUID}/consumers/{consumerGUID}")

    public VoidResponse    assignValidValueToConsumer(@PathVariable String             serverName,
                                                      @PathVariable String             userId,
                                                      @PathVariable String             validValueGUID,
                                                      @PathVariable String             consumerGUID,
                                                      @RequestBody  BooleanRequestBody requestBody)
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

    public VoidResponse unassignValidValueFromConsumer(@PathVariable String          serverName,
                                                       @PathVariable String          userId,
                                                       @PathVariable String          validValueGUID,
                                                       @PathVariable String          consumerGUID,
                                                       @RequestBody  NullRequestBody requestBody)
    {
        return restAPI.unassignValidValueFromConsumer(serverName, userId, validValueGUID, consumerGUID, requestBody);
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
     * @param validValueName qualified name of the valid value.
     *
     * @return Valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/by-name")

    public ValidValuesResponse getValidValueByName(@PathVariable String  serverName,
                                                   @PathVariable String  userId,
                                                   @RequestBody  String  validValueName)
    {
        return restAPI.getValidValueByName(serverName, userId, validValueName);
    }


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param serverName name of calling server
     * @param userId calling user
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param searchString string value to look for - may contain RegEx characters.
     *
     * @return list of valid value beans or
     * InvalidParameterException one of the parameters is invalid or
     * UserNotAuthorizedException the user is not authorized to make this request or
     * PropertyServerException the repository is not available or not working properly.
     */
    @PostMapping(path = "/valid-values/by-search-string")

    public ValidValuesResponse findValidValues(@PathVariable String  serverName,
                                               @PathVariable String  userId,
                                               @RequestParam int     startFrom,
                                               @RequestParam int     pageSize,
                                               @RequestBody  String  searchString)
    {
        return restAPI.findValidValues(serverName, userId, searchString, startFrom, pageSize);
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

    public ValidValueConsumersResponse getValidValuesConsumers(@PathVariable String  serverName,
                                                               @PathVariable String  userId,
                                                               @PathVariable String  validValueGUID,
                                                               @RequestParam int     startFrom,
                                                               @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesConsumers(serverName, userId, validValueGUID, startFrom, pageSize);
    }


    /**
     * Pag through the list of implementations for a valid value.
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

    public AssetsResponse getValidValuesImplementations(@PathVariable String  serverName,
                                                        @PathVariable String  userId,
                                                        @PathVariable String  validValueGUID,
                                                        @RequestParam int     startFrom,
                                                        @RequestParam int     pageSize)
    {
        return restAPI.getValidValuesImplementations(serverName, userId, validValueGUID, startFrom, pageSize);
    }
}

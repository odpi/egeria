/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.digitalarchitecture.api;

import org.odpi.openmetadata.accessservices.digitalarchitecture.metadataelements.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * ManageReferenceData provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public interface ManageReferenceData
{
    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param userId calling user.
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this set be used.
     * @param scope what is the scope of this set's values.
     * @param additionalProperties additional properties for this set.
     * @param typeName name of subtype of the definition (or null to use the standard open type)
     * @param extendedProperties properties that need to be populated into a subtype (or null for the standard open type).
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    String  createValidValueSet(String              userId,
                                String              qualifiedName,
                                String              displayName,
                                String              description,
                                String              usage,
                                String              scope,
                                Map<String, String> additionalProperties,
                                String              typeName,
                                Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;


    /**
     * Create a new valid value definition.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param isDefaultValue     is this the default value for the set?
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this value be used.
     * @param scope what is the scope of the values.
     * @param preferredValue the value that should be used in an implementation if possible.
     * @param additionalProperties additional properties for this definition.
     * @param typeName name of subtype of the definition (or null to use the standard open type)
     * @param extendedProperties properties that need to be populated into a subtype (or null for the standard open type).
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    String  createValidValueDefinition(String              userId,
                                       String              setGUID,
                                       boolean             isDefaultValue,
                                       String              qualifiedName,
                                       String              displayName,
                                       String              description,
                                       String              usage,
                                       String              scope,
                                       String              preferredValue,
                                       Map<String, String> additionalProperties,
                                       String              typeName,
                                       Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param qualifiedName unique name.
     * @param displayName displayable descriptive name.
     * @param description further information.
     * @param usage how/when should this value be used.
     * @param scope what is the scope of the values.
     * @param preferredValue the value that should be used in an implementation if possible.
     * @param isDeprecated is this value deprecated?
     * @param additionalProperties additional properties for this valid value.
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param typeName name of subtype of the definition (or null to use the standard open type)
     * @param extendedProperties properties that need to be populated into a subtype.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    updateValidValue(String              userId,
                             String              validValueGUID,
                             String              qualifiedName,
                             String              displayName,
                             String              description,
                             String              usage,
                             String              scope,
                             String              preferredValue,
                             boolean             isDeprecated,
                             Map<String, String> additionalProperties,
                             boolean             isMergeUpdate,
                             String              typeName,
                             Map<String, Object> extendedProperties) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the value to delete
     * @param qualifiedName unique name of the value to delete.  This is used to verify that
     *                      the correct valid value is being deleted.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    deleteValidValue(String userId,
                             String validValueGUID,
                             String qualifiedName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param isDefaultValue     is this the default value for the set?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    attachValidValueToSet(String  userId,
                                  String  setGUID,
                                  String  validValueGUID,
                                  boolean isDefaultValue) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId calling user
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    detachValidValueFromSet(String userId,
                                    String setGUID,
                                    String validValueGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Link a valid value to an asset that provides the implementation.  Typically this method is
     * used to link a valid value set to a code table.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that implements the valid value.
     * @param symbolicName lookup name for valid value
     * @param implementationValue value used in implementation
     * @param additionalValues additional values stored under the symbolic name
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void  linkValidValueToImplementation(String              userId,
                                         String              validValueGUID,
                                         String              assetGUID,
                                         String              symbolicName,
                                         String              implementationValue,
                                         Map<String, String> additionalValues) throws InvalidParameterException,
                                                                                      UserNotAuthorizedException,
                                                                                      PropertyServerException;


    /**
     * Add the ReferenceData classification to an asset.  IF the asset is already classified
     * in this way, the method is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of the asset that contains reference data.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void  classifyAssetAsReferenceData(String userId,
                                       String assetGUID) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;


    /**
     * Remove the link between a valid value and an implementing asset.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param assetGUID unique identifier of the asset that used to implement the valid value.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void  unlinkValidValueFromImplementation(String userId,
                                             String validValueGUID,
                                             String assetGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Remove the ReferenceData classification form an Asset.  If the asset was not classified in this way,
     * this call is a no-op.
     *
     * @param userId calling user.
     * @param assetGUID unique identifier of asset.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void  declassifyAssetAsReferenceData(String userId,
                                         String assetGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Link a valid value typically to a schema element or glossary term to show that it uses
     * the valid values.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to link to.
     * @param strictRequirement the valid values defines the only values that are permitted.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    assignValidValueToConsumer(String  userId,
                                       String  validValueGUID,
                                       String  consumerGUID,
                                       boolean strictRequirement) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the link between a valid value and a consumer.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param consumerGUID unique identifier of the element to remove the link from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    unassignValidValueFromConsumer(String userId,
                                           String validValueGUID,
                                           String consumerGUID) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException;


    /**
     * Link a valid value as a reference value to a referencable to act as a tag/classification to help with locating and
     * grouping the referenceable.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to link to.
     * @param confidence how confident is the steward that this mapping is correct (0-100).
     * @param steward identifier of steward
     * @param notes additional notes from the steward
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    assignReferenceValueToItem(String   userId,
                                       String   validValueGUID,
                                       String   referenceableGUID,
                                       int      confidence,
                                       String   steward,
                                       String   notes) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of the valid value.
     * @param referenceableGUID unique identifier of the element to remove the link from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    unassignReferenceValueFromItem(String   userId,
                                           String   validValueGUID,
                                           String   referenceableGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Link together 2 valid values from different sets that have equivalent values/meanings.
     *
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value to link to.
     * @param associationDescription how are the valid values related?
     * @param confidence how confident is the steward that this mapping is correct (0-100).
     * @param steward identifier of steward
     * @param notes additional notes from the steward
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    mapValidValues(String   userId,
                           String   validValue1GUID,
                           String   validValue2GUID,
                           String   associationDescription,
                           int      confidence,
                           String   steward,
                           String   notes) throws InvalidParameterException,
                                                  UserNotAuthorizedException,
                                                  PropertyServerException;


    /**
     * Remove the reference value link between a valid value and a referenceable (item).
     *
     * @param userId calling user.
     * @param validValue1GUID unique identifier of the valid value.
     * @param validValue2GUID unique identifier of the other valid value element to remove the link from.
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    unmapValidValues(String   userId,
                             String   validValue1GUID,
                             String   validValue2GUID) throws InvalidParameterException,
                                                              UserNotAuthorizedException,
                                                              PropertyServerException;


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of the valid value.
     *
     * @return Valid value bean
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValueElement getValidValueByGUID(String userId,
                                          String validValueGUID) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId calling user
     * @param validValueName qualified name of the valid value.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement>   getValidValueByName(String userId,
                                                  String validValueName,
                                                  int    startFrom,
                                                  int    pageSize) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> findValidValues(String userId,
                                            String searchString,
                                            int    startFrom,
                                            int    pageSize) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;

    /**
     * Page through the members of a valid value set.
     *
     * @param userId calling user.
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> getValidValueSetMembers(String userId,
                                                    String validValueSetGUID,
                                                    int    startFrom,
                                                    int    pageSize) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> getSetsForValidValue(String userId,
                                                 String validValueGUID,
                                                 int    startFrom,
                                                 int    pageSize) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Page through the list of consumers for a valid value.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueAssignmentConsumerElement> getValidValuesAssignmentConsumers(String userId,
                                                                                String validValueGUID,
                                                                                int    startFrom,
                                                                                int    pageSize) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException;


    /**
     * Page through the list of valid values assigned to referenceable element.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of anchoring referenceable
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value consumer beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueAssignmentDefinitionElement> getValidValuesAssignmentDefinition(String   userId,
                                                                                   String   referenceableGUID,
                                                                                   int      startFrom,
                                                                                   int      pageSize) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException;


    /**
     * Page through the list of implementations for a valid value.
     *
     * @param userId calling user.
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of asset beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueImplAssetElement> getValidValuesImplementationAssets(String userId,
                                                                        String validValueGUID,
                                                                        int    startFrom,
                                                                        int    pageSize) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;


    /**
     * Page through the list of valid values defining the content of a reference data asset.
     * This is always called from the assetHandler after it has checked that the asset is in the right zone.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueImplDefinitionElement> getValidValuesImplementationDefinitions(String       userId,
                                                                                  String       assetGUID,
                                                                                  int          startFrom,
                                                                                  int          pageSize) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException;


    /**
     * Page through the list of mappings for a valid value.  These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueMappingElement> getValidValueMappings(String       userId,
                                                         String       validValueGUID,
                                                         int          startFrom,
                                                         int          pageSize) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Page through the list of mapping relationships associated with a valid value.
     * These are other valid values from different valid value sets that are equivalent
     * in some way.  The association description covers the type of association.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of mappings to other valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValuesMappingElement> getValidValuesMappings(String       userId,
                                                           String       validValueGUID,
                                                           int          startFrom,
                                                           int          pageSize) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Page through the list of referenceables that have this valid value as a reference value.
     *
     * @param userId calling user
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of referenceable beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ReferenceValueAssignmentItemElement> getReferenceValueAssignedItems(String       userId,
                                                                             String       validValueGUID,
                                                                             int          startFrom,
                                                                             int          pageSize) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException;

    /**
     * Page through the list of assigned reference values for a referenceable.
     *
     * @param userId calling user
     * @param referenceableGUID unique identifier of assigned item
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ReferenceValueAssignmentDefinitionElement> getReferenceValueAssignments(String       userId,
                                                                                 String       referenceableGUID,
                                                                                 int          startFrom,
                                                                                 int          pageSize) throws InvalidParameterException,
                                                                                                               UserNotAuthorizedException,
                                                                                                               PropertyServerException;
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ValidValueElement;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * AssetOnboardingValidValues provides the API operations to create and maintain lists of valid
 * value definitions grouped into a valid value set.  Both valid value definitions and valid value sets have
 * the same attributes and so inherit from ValidValue where all the attributes are defined.
 *
 * A set is just grouping of valid values.   Valid value definitions and set can be nested many times in other
 * valid value sets.
 */
public interface AssetOnboardingValidValues
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
     * @param extendedProperties properties that need to be populated into a subtype.
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
     * @param dataType the data type of the preferred value.
     * @param additionalProperties additional properties for this definition.
     * @param extendedProperties properties that need to be populated into a subtype.
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
                                       String              dataType,
                                       Map<String, String> additionalProperties,
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
     * @param dataType the data type of the preferred value.
     * @param isDeprecated is this value deprecated?
     * @param additionalProperties additional properties for this valid value.
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
                             String              dataType,
                             boolean             isDeprecated,
                             Map<String, String> additionalProperties,
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
    void    deleteValidValue(String   userId,
                             String   validValueGUID,
                             String   qualifiedName) throws InvalidParameterException,
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
    void    attachValidValueToSet(String   userId,
                                  String   setGUID,
                                  String   validValueGUID,
                                  boolean  isDefaultValue) throws InvalidParameterException,
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
    void    detachValidValueFromSet(String   userId,
                                    String   setGUID,
                                    String   validValueGUID) throws InvalidParameterException,
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
    ValidValueElement getValidValueByGUID(String   userId,
                                          String   validValueGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException;


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId calling user
     * @param validValueName qualified name of the valid value.
     * @param startFrom         starting element (used in paging through large result sets)
     * @param pageSize          maximum number of results to return
     * @param effectiveTime the effective date/time to use for the query
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement>   getValidValueByName(String   userId,
                                                  String   validValueName,
                                                  int      startFrom,
                                                  int      pageSize,
                                                  Date     effectiveTime) throws InvalidParameterException,
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
     * @param effectiveTime the effective date/time to use for the query
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> findValidValues(String   userId,
                                            String   searchString,
                                            int      startFrom,
                                            int      pageSize,
                                            Date     effectiveTime) throws InvalidParameterException,
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
    List<ValidValueElement> getValidValueSetMembers(String   userId,
                                                    String   validValueSetGUID,
                                                    int      startFrom,
                                                    int      pageSize) throws InvalidParameterException,
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
    List<ValidValueElement> getSetsForValidValue(String   userId,
                                                 String   validValueGUID,
                                                 int      startFrom,
                                                 int      pageSize) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;
}

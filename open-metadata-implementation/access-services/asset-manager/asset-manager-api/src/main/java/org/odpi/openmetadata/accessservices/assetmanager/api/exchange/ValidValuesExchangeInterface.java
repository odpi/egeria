/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api.exchange;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ValidValueElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ValidValueProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;

/**
 * The ValidValuesExchangeInterface supports the exchanges of valid values and reference data.
 */
public interface ValidValuesExchangeInterface
{
    /**
     * Create a new valid value set.  This just creates the Set itself.  Members are added either as they are
     * created, or they can be attached to a set after they are created.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param validValueProperties properties to store
     *
     * @return unique identifier for the new set
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    String  createValidValueSet(String                       userId,
                                String                       assetManagerGUID,
                                String                       assetManagerName,
                                ExternalIdentifierProperties externalIdentifierProperties,
                                ValidValueProperties         validValueProperties) throws InvalidParameterException,
                                                                                          UserNotAuthorizedException,
                                                                                          PropertyServerException;

    /**
     * Create a new valid value definition.
     *
     * @param userId calling user.
     * @param setGUID unique identifier of the set to attach this to.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param validValueProperties properties to store
     *
     * @return unique identifier for the new definition
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    String  createValidValueDefinition(String                       userId,
                                       String                       setGUID,
                                       String                       assetManagerGUID,
                                       String                       assetManagerName,
                                       ExternalIdentifierProperties externalIdentifierProperties,
                                       ValidValueProperties         validValueProperties) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Update the properties of the valid value.  All properties are updated.
     * If only changing some if the properties, retrieve the current values from the repository
     * and pass existing values back on this call if they are not to change.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of the valid value.
     * @param validValueExternalIdentifier unique identifier of the valid value in the external asset manager
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param validValueProperties properties to store
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    updateValidValue(String               userId,
                             String               assetManagerGUID,
                             String               assetManagerName,
                             String               validValueGUID,
                             String               validValueExternalIdentifier,
                             boolean              isMergeUpdate,
                             ValidValueProperties validValueProperties,
                             Date                 effectiveTime,
                             boolean              forLineage,
                             boolean              forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Remove the valid value form the repository.  All links to it are deleted too.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of the valid value.
     * @param validValueExternalIdentifier unique identifier of the valid value in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    removeValidValue(String  userId,
                             String  assetManagerGUID,
                             String  assetManagerName,
                             String  validValueGUID,
                             String  validValueExternalIdentifier,
                             Date    effectiveTime,
                             boolean forLineage,
                             boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Create a link between a valid value set or definition and a set.  This means the valid
     * value is a member of the set.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param setGUID unique identifier of the set.
     * @param validValueGUID unique identifier of the valid value to add to the set.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    attachValidValueToSet(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  setGUID,
                                  String  validValueGUID,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the link between a valid value and a set it is a member of.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param setGUID owning set
     * @param validValueGUID unique identifier of the member to be removed.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    void    detachValidValueFromSet(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  setGUID,
                                    String  validValueGUID,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Retrieve a specific valid value from the repository.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of the valid value.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return Valid value bean
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    ValidValueElement getValidValueByGUID(String  userId,
                                          String  assetManagerGUID,
                                          String  assetManagerName,
                                          String  validValueGUID,
                                          Date    effectiveTime,
                                          boolean forLineage,
                                          boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;


    /**
     * Retrieve a specific valid value from the repository.  Duplicates may be returned if
     * multiple valid values have been assigned the same qualified name.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueName qualified name of the valid value.
     * @param startFrom         starting element (used in paging through large result sets)
     * @param pageSize          maximum number of results to return
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return Valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> getValidValueByName(String  userId,
                                                String  assetManagerGUID,
                                                String  assetManagerName,
                                                String  validValueName,
                                                int     startFrom,
                                                int     pageSize,
                                                Date    effectiveTime,
                                                boolean forLineage,
                                                boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Locate valid values that match the search string.  It considers the names, description, scope,
     * usage and preferred value.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param searchString string value to look for - may contain RegEx characters.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> findValidValues(String  userId,
                                            String  assetManagerGUID,
                                            String  assetManagerName,
                                            String  searchString,
                                            int     startFrom,
                                            int     pageSize,
                                            Date    effectiveTime,
                                            boolean forLineage,
                                            boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException;

    /**
     * Page through the members of a valid value set.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueSetGUID unique identifier of the valid value set.
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> getValidValueSetMembers(String  userId,
                                                    String  assetManagerGUID,
                                                    String  assetManagerName,
                                                    String  validValueSetGUID,
                                                    int     startFrom,
                                                    int     pageSize,
                                                    Date    effectiveTime,
                                                    boolean forLineage,
                                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException;


    /**
     * Page through the list of valid value sets that a valid value definition/set belongs to.
     *
     * @param userId calling user.
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param validValueGUID unique identifier of valid value to query
     * @param startFrom paging starting point
     * @param pageSize maximum number of return values.
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @return list of valid value beans
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<ValidValueElement> getSetsForValidValue(String  userId,
                                                 String  assetManagerGUID,
                                                 String  assetManagerName,
                                                 String  validValueGUID,
                                                 int     startFrom,
                                                 int     pageSize,
                                                 Date    effectiveTime,
                                                 boolean forLineage,
                                                 boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;
}

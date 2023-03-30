/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api;

import org.odpi.openmetadata.accessservices.assetmanager.properties.SecurityTagsProperties;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * The StewardshipExchangeInterface supports the exchange of relationships (such as SemanticAssignment)
 * and classifications that are added by stewards (or automated stewardship processes) such as Confidentiality.
 */
public interface StewardshipExchangeInterface
{
    /**
     * Classify the element with the Memento classification to indicate that it has been logically deleted for by lineage requests.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addMementoClassification(String  userId,
                                  String  assetManagerGUID,
                                  String  assetManagerName,
                                  String  elementGUID,
                                  String  elementExternalIdentifier,
                                  Date    effectiveTime,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;


    /**
     * Remove the memento designation from the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearMementoClassification(String  userId,
                                    String  assetManagerGUID,
                                    String  assetManagerName,
                                    String  elementGUID,
                                    String  elementExternalIdentifier,
                                    Date    effectiveTime,
                                    boolean forLineage,
                                    boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;


    /**
     * Classify the element with the Incomplete classification to indicate that it has more details to come.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void addIncompleteClassification(String  userId,
                                     String  assetManagerGUID,
                                     String  assetManagerName,
                                     String  elementGUID,
                                     String  elementExternalIdentifier,
                                     Date    effectiveTime,
                                     boolean forLineage,
                                     boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException;


    /**
     * Remove the Incomplete designation from the element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software capability representing the caller
     * @param assetManagerName unique name of software capability representing the caller
     * @param elementGUID unique identifier of the metadata element to update
     * @param elementExternalIdentifier unique identifier of the element in the external asset manager
     * @param effectiveTime optional date for effective time of the query.  Null means any effective time
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void clearIncompleteClassification(String  userId,
                                       String  assetManagerGUID,
                                       String  assetManagerName,
                                       String  elementGUID,
                                       String  elementExternalIdentifier,
                                       Date    effectiveTime,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Add or replace the security tags for an element.
     *
     * @param userId calling user
     * @param elementGUID element to link it to - its type must inherit from Referenceable.
     * @param properties details of the security tags
     *
     * @throws InvalidParameterException element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addSecurityTags(String                 userId,
                          String                 elementGUID,
                          SecurityTagsProperties properties) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the security tags classification from an element.
     *
     * @param userId calling user
     * @param elementGUID element where the security tags need to be removed.
     *
     * @throws InvalidParameterException asset or element not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  removeSecurityTags(String userId,
                             String elementGUID) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;
}

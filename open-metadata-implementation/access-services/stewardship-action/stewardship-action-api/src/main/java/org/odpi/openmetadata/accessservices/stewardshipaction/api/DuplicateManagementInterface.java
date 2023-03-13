/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.stewardshipaction.api;

import org.odpi.openmetadata.accessservices.stewardshipaction.metadataelements.DuplicateElement;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;

import java.util.List;

/**
 * DuplicateManagementInterface defines the operations to manage the linking of elements that are duplicates of one another.
 */
public interface DuplicateManagementInterface
{
    /**
     * Create a simple relationship between two elements.  These elements must be of the same type.  If the relationship already exists,
     * the properties are updated.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     *
     * @throws InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  linkElementsAsDuplicates(String userId,
                                   String element1GUID,
                                   String element2GUID,
                                   int    statusIdentifier,
                                   String steward,
                                   String stewardTypeName,
                                   String stewardPropertyName,
                                   String source,
                                   String notes) throws InvalidParameterException,
                                                        UserNotAuthorizedException,
                                                        PropertyServerException;


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param element1GUID unique identifier of first element
     * @param element2GUID unique identifier of second element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  unlinkElementsAsDuplicates(String userId,
                                     String element1GUID,
                                     String element2GUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Classify an element as a known duplicate.  This will mean that it is included in duplicate processing during metadata retrieval requests.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid, or the elements are of different types
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  markElementAsKnownDuplicate(String userId,
                                      String elementGUID) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Remove the classification that identifies this element as a known duplicate.
     *
     * @param userId calling user
     * @param elementGUID unique identifier of the element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  unmarkElementAsKnownDuplicate(String userId,
                                        String elementGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * List the elements that are linked as peer duplicates to the requested element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of linked duplicates
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<DuplicateElement> getPeerDuplicates(String userId,
                                             String elementGUID,
                                             int    startFrom,
                                             int    pageSize) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Mark an element as a consolidated duplicate (or update the properties if it is already marked as such).
     * This method assumes that a standard create method has been used to create the element first using the values from contributing elements.
     * It is just adding the ConsolidatedDuplicate classification to the element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of the element that contains the consolidated information from a collection of elements
     *                                  that are all duplicates of one another.
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  markAsConsolidatedDuplicate(String       userId,
                                      String       consolidatedDuplicateGUID,
                                      int          statusIdentifier,
                                      String       steward,
                                      String       stewardTypeName,
                                      String       stewardPropertyName,
                                      String       source,
                                      String       notes) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException;


    /**
     * Create a ConsolidatedDuplicateLink relationship between the consolidated duplicate element and one of its contributing element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  linkElementToConsolidatedDuplicate(String userId,
                                             String consolidatedDuplicateGUID,
                                             String contributingElementGUID) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    PropertyServerException;


    /**
     * Remove the relationship between two elements that marks them as duplicates.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of consolidated duplicate
     * @param contributingElementGUID unique identifier of duplicate element
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  unlinkElementFromConsolidatedDuplicate(String userId,
                                                 String consolidatedDuplicateGUID,
                                                 String contributingElementGUID) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * List the elements that are contributing to a consolidating duplicate element.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID element to query
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of contributing duplicates
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<ElementStub> getContributingDuplicates(String userId,
                                                String consolidatedDuplicateGUID,
                                                int    startFrom,
                                                int    pageSize) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Return details of the consolidated duplicate for a requested element.
     *
     * @param userId calling user
     * @param elementGUID element to query
     *
     * @return header of consolidated duplicated or null if none
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    ElementStub getConsolidatedDuplicate(String userId,
                                         String elementGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Remove the consolidated duplicate element and the links to the elements that contributed to its values.
     *
     * @param userId calling user
     * @param consolidatedDuplicateGUID unique identifier of element to remove
     *
     * @throws InvalidParameterException one of the parameters is null or invalid or the elements are not linked as duplicates
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  removeConsolidatedDuplicate(String userId,
                                      String consolidatedDuplicateGUID) throws InvalidParameterException,
                                                                               UserNotAuthorizedException,
                                                                               PropertyServerException;
}

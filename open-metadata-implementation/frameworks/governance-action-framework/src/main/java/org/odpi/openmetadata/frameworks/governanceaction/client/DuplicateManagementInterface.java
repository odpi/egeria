/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * DuplicateManagementInterface defines methods that are used by governance services to manage duplicates in open metadata.
 */
public interface DuplicateManagementInterface
{
    /**
     * Link elements as peer duplicates. Create a simple relationship between two elements.
     * If the relationship already exists, the properties are updated.
     *
     * @param userId caller's userId
     * @param metadataElement1GUID unique identifier of the metadata element at end 1 of the relationship
     * @param metadataElement2GUID unique identifier of the metadata element at end 2 of the relationship
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param setKnownDuplicate boolean flag indicating whether the KnownDuplicate classification should be set on the linked entities.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void linkElementsAsPeerDuplicates(String  userId,
                                      String  metadataElement1GUID,
                                      String  metadataElement2GUID,
                                      int     statusIdentifier,
                                      String  steward,
                                      String  stewardTypeName,
                                      String  stewardPropertyName,
                                      String  source,
                                      String  notes,
                                      boolean setKnownDuplicate) throws InvalidParameterException,
                                                                        UserNotAuthorizedException,
                                                                        PropertyServerException;


    /**
     * Identify an element that acts as a consolidated version for a set of duplicate elements.
     * (The consolidated element is created using createMetadataElement.)
     *
     * @param userId caller's userId
     * @param consolidatedElementGUID unique identifier of the metadata element
     * @param statusIdentifier what is the status of this relationship (negative means untrusted, 0 means unverified and positive means trusted)
     * @param steward identifier of the steward
     * @param stewardTypeName type of element used to identify the steward
     * @param stewardPropertyName property name used to identify steward
     * @param source source of the duplicate detection processing
     * @param notes notes for the steward
     * @param sourceElementGUIDs List of the source elements that must be linked to the consolidated element.  It is assumed that they already
     *                           have the KnownDuplicateClassification.
     * @throws InvalidParameterException the unique identifier's of the metadata elements are null or invalid in some way; the properties are
     *                                    not valid for this type of relationship
     * @throws UserNotAuthorizedException the governance action service is not authorized to create this type of relationship
     * @throws PropertyServerException there is a problem with the metadata store
     */
    void linkConsolidatedDuplicate(String       userId,
                                   String       consolidatedElementGUID,
                                   int          statusIdentifier,
                                   String       steward,
                                   String       stewardTypeName,
                                   String       stewardPropertyName,
                                   String       source,
                                   String       notes,
                                   List<String> sourceElementGUIDs) throws InvalidParameterException,
                                                                           UserNotAuthorizedException,
                                                                           PropertyServerException;
}

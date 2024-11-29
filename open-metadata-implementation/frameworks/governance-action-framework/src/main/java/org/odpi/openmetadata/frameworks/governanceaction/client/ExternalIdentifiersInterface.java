/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.governanceaction.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.frameworks.governanceaction.properties.MetadataCorrelationHeader;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * ExternalIdentifiersInterface provides the interface for managing external identifiers.
 */
public interface ExternalIdentifiersInterface
{
    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param externalScopeName unique name of software capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void addExternalIdentifier(String                       userId,
                               String                       externalScopeGUID,
                               String                       externalScopeName,
                               String                       externalScopeTypeName,
                               String                       openMetadataElementGUID,
                               String                       openMetadataElementTypeName,
                               ExternalIdentifierProperties externalIdentifierProperties,
                               Date                         effectiveFrom,
                               Date                         effectiveTo,
                               boolean                      forLineage,
                               boolean                      forDuplicateProcessing,
                               Date                         effectiveTime) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Update the description of a specific external identifier.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param externalScopeName unique name of software capability representing the caller
     * @param externalScopeTypeName type name of the software capability describing the manager for the external identifier
     * @param externalIdentifierProperties optional properties used to define an external identifier
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param effectiveFrom the date when this element is active - null for active now
     * @param effectiveTo the date when this element becomes inactive - null for active until deleted
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void updateExternalIdentifier(String                       userId,
                                  String                       externalScopeGUID,
                                  String                       externalScopeName,
                                  String                       externalScopeTypeName,
                                  String                       openMetadataElementGUID,
                                  String                       openMetadataElementTypeName,
                                  ExternalIdentifierProperties externalIdentifierProperties,
                                  Date                         effectiveFrom,
                                  Date                         effectiveTo,
                                  boolean                      forLineage,
                                  boolean                      forDuplicateProcessing,
                                  Date                         effectiveTime) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param externalScopeName unique name of software capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party external scope
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void removeExternalIdentifier(String  userId,
                                  String  externalScopeGUID,
                                  String  externalScopeName,
                                  String  openMetadataElementGUID,
                                  String  openMetadataElementTypeName,
                                  String  externalIdentifier,
                                  boolean forLineage,
                                  boolean forDuplicateProcessing,
                                  Date    effectiveTime) throws InvalidParameterException,
                                                                UserNotAuthorizedException,
                                                                PropertyServerException;



    /**
     * Remove the scope associated with a collection of external identifiers.  All associated external identifiers are removed too.
     * The linked open metadata elements are not affected.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime what is the effective time for related queries needed to do the update
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void removeExternalScope(String  userId,
                             String  externalScopeGUID,
                             boolean forLineage,
                             boolean forDuplicateProcessing,
                             Date    effectiveTime) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException;


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param externalScopeName unique name of software capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param externalIdentifier unique identifier of this element in the external external scope
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void confirmSynchronization(String userId,
                                String externalScopeGUID,
                                String externalScopeName,
                                String openMetadataElementGUID,
                                String openMetadataElementTypeName,
                                String externalIdentifier) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Return the list of headers for open metadata elements that are associated with a particular
     * external identifier.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software capability representing the caller
     * @param externalScopeName unique name of software capability representing the caller
     * @param externalIdentifier unique identifier of this element in the external external scope
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    List<ElementHeader> getElementsForExternalIdentifier(String  userId,
                                                         String  externalScopeGUID,
                                                         String  externalScopeName,
                                                         String  externalIdentifier,
                                                         int     startFrom,
                                                         int     pageSize,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;


    /**
     * Check that the supplied external identifier matches the element GUID.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID element guid used for the lookup
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param elementExternalIdentifier external identifier value
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return boolean
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    boolean validateExternalIdentifier(String  userId,
                                       String  externalScopeGUID,
                                       String  externalScopeName,
                                       String  openMetadataElementGUID,
                                       String  openMetadataElementTypeName,
                                       String  elementExternalIdentifier,
                                       boolean forLineage,
                                       boolean forDuplicateProcessing,
                                       Date    effectiveTime) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;


    /**
     * Assemble the correlation headers attached to the supplied element guid.  This includes the external identifiers
     * plus information on the scope and usage.
     *
     * @param userId calling user
     * @param externalScopeGUID unique identifier of software server capability representing the caller
     * @param externalScopeName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     *
     * @return list of correlation headers (note if external scope identifiers are present, only the matching correlation header is returned)
     *
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    List<MetadataCorrelationHeader> getExternalIdentifiers(String  userId,
                                                           String  externalScopeGUID,
                                                           String  externalScopeName,
                                                           String  openMetadataElementGUID,
                                                           String  openMetadataElementTypeName,
                                                           int     startFrom,
                                                           int     pageSize,
                                                           boolean forLineage,
                                                           boolean forDuplicateProcessing,
                                                           Date    effectiveTime) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException;


    /**
     * Return the vendor properties associated with an element.  The inner map holds the specific properties for each
     * vendor.  The outer maps the vendor identifier to the properties.
     *
     * @param userId calling user
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param forLineage return elements marked with the Memento classification?
     * @param forDuplicateProcessing do not merge elements marked as duplicates?
     * @param effectiveTime the time that the retrieved elements must be effective for (null for any time, new Date() for now)
     * @return map of vendor properties
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    Map<String, Map<String, String>> getVendorProperties(String  userId,
                                                         String  openMetadataElementGUID,
                                                         String  openMetadataElementTypeName,
                                                         boolean forLineage,
                                                         boolean forDuplicateProcessing,
                                                         Date    effectiveTime) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException;
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api;

import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ElementHeader;
import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * AssetManagerInterface provides the interface for retrieving the identity of an external asset manager.
 * The definition of the external asset manager may also be created using the IT Infrastructure OMAS.
 */
public interface AssetManagerInterface
{
    /**
     * Create information about the external asset manager.  This is represented as a software server capability
     * and all information that is specific to the external asset manager (such as the identifiers of the
     * metadata elements it stores) will be linked to it.
     *
     * @param userId calling user
     * @param assetManagerProperties description of the integration daemon (specify qualified name at a minimum)
     *
     * @return unique identifier of the asset management's software server capability
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String createExternalAssetManager(String                 userId,
                                      AssetManagerProperties assetManagerProperties) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Retrieve the unique identifier of the external asset manager from its qualified name.
     * Typically the qualified name comes from the integration connector configuration.
     *
     * @param userId calling user
     * @param qualifiedName unique name to use for the external asset
     *
     * @return unique identifier of the external asset manager's software server capability
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    String  getExternalAssetManagerGUID(String  userId,
                                        String  qualifiedName) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;


    /**
     * Add a new external identifier to an existing open metadata element.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param synchronizationDirection details of the synchronization direction
     * @param synchronizationDescription optional additional description about the synchronization approach
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     * @param externalIdentifierName name of the identifier in the third party asset manager
     * @param externalIdentifierUsage description of how the open metadata element maps to the identifier
     * @param externalIdentifierSource component that issuing this request.
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void addExternalIdentifier(String                   userId,
                               String                   assetManagerGUID,
                               String                   assetManagerName,
                               SynchronizationDirection synchronizationDirection,
                               String                   synchronizationDescription,
                               String                   openMetadataElementGUID,
                               String                   openMetadataElementTypeName,
                               String                   externalIdentifier,
                               String                   externalIdentifierName,
                               String                   externalIdentifierUsage,
                               String                   externalIdentifierSource,
                               KeyPattern               keyPattern,
                               Map<String, String>      mappingProperties) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException;


    /**
     * Update the description of a specific external identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param synchronizationDirection details of the synchronization direction
     * @param synchronizationDescription optional additional description about the synchronization approach
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     * @param externalIdentifierName name of the identifier in the third party asset manager
     * @param externalIdentifierUsage description of how the open metadata element maps to the identifier
     * @param externalIdentifierSource component that issuing this request.
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void updateExternalIdentifier(String                   userId,
                                  String                   assetManagerGUID,
                                  String                   assetManagerName,
                                  SynchronizationDirection synchronizationDirection,
                                  String                   synchronizationDescription,
                                  String                   openMetadataElementGUID,
                                  String                   openMetadataElementTypeName,
                                  String                   externalIdentifier,
                                  String                   externalIdentifierName,
                                  String                   externalIdentifierUsage,
                                  String                   externalIdentifierSource,
                                  KeyPattern               keyPattern,
                                  Map<String, String>      mappingProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Remove an external identifier from an existing open metadata element.  The open metadata element is not
     * affected.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param synchronizationDirection details of the synchronization direction
     * @param synchronizationDescription optional additional description about the synchronization approach
     * @param openMetadataElementGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param openMetadataElementTypeName type name for the open metadata element
     * @param externalIdentifier unique identifier of this element in the third party asset manager
     * @param externalIdentifierName name of the identifier in the third party asset manager
     * @param externalIdentifierUsage description of how the open metadata element maps to the identifier
     * @param externalIdentifierSource component that issuing this request.
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void removeExternalIdentifier(String                   userId,
                                  String                   assetManagerGUID,
                                  String                   assetManagerName,
                                  SynchronizationDirection synchronizationDirection,
                                  String                   synchronizationDescription,
                                  String                   openMetadataElementGUID,
                                  String                   openMetadataElementTypeName,
                                  String                   externalIdentifier,
                                  String                   externalIdentifierName,
                                  String                   externalIdentifierUsage,
                                  String                   externalIdentifierSource,
                                  KeyPattern               keyPattern,
                                  Map<String, String>      mappingProperties) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException;


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit point of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataElementGUID unique identifier (GUID) of this element in open metadata
     * @param openMetadataElementTypeName type name of the element in the open metadata ecosystem (default referenceable)
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void confirmSynchronization(String userId,
                                String assetManagerGUID,
                                String assetManagerName,
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
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of element headers
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    List<ElementHeader> getElementsForExternalIdentifier(String userId,
                                                         String assetManagerGUID,
                                                         String assetManagerName,
                                                         String externalIdentifier,
                                                         int    startFrom,
                                                         int    pageSize) throws InvalidParameterException,
                                                                                 UserNotAuthorizedException,
                                                                                 PropertyServerException;
}

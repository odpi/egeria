/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.api;

import org.odpi.openmetadata.accessservices.assetmanager.properties.AssetManagerProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.KeyPattern;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

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
     * Update the description of a specific external identifier.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier (GUID) of the element in the open metadata ecosystem
     * @param externalIdentifier unique identifier of this element in the external asset manager
     * @param keyPattern style of the external identifier
     * @param mappingProperties additional mapping properties
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void updateExternalIdentifier(String              userId,
                                  String              assetManagerGUID,
                                  String              assetManagerName,
                                  String              openMetadataGUID,
                                  String              externalIdentifier,
                                  KeyPattern          keyPattern,
                                  Map<String, String> mappingProperties) throws InvalidParameterException,
                                                                                UserNotAuthorizedException,
                                                                                PropertyServerException;


    /**
     * Confirm that the values of a particular metadata element have been synchronized.  This is important
     * from an audit points of view, and to allow bidirectional updates of metadata using optimistic locking.
     *
     * @param userId calling user
     * @param assetManagerGUID unique identifier of software server capability representing the caller
     * @param assetManagerName unique name of software server capability representing the caller
     * @param openMetadataGUID unique identifier (GUID) of this element in open metadata
     * @param externalIdentifier unique identifier of this element in the external asset manager
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    void confirmSynchronization(String userId,
                                String assetManagerGUID,
                                String assetManagerName,
                                String openMetadataGUID,
                                String externalIdentifier) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;
}

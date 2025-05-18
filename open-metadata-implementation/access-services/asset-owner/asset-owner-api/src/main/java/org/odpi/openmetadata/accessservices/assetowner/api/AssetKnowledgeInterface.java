/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;

import java.util.List;
import java.util.Map;

/**
 * AssetKnowledgeInterface provides basic information about the types of access supported in the open metadata ecosystem
 */
public interface AssetKnowledgeInterface
{
    /**
     * Return the asset subtype names.
     *
     * @param userId calling user
     * @return list of type names that are subtypes of asset
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    List<String> getTypesOfAsset(String userId) throws InvalidParameterException,
                                                       UserNotAuthorizedException,
                                                       PropertyServerException;


    /**
     * Return the asset subtype names mapped to their descriptions.
     *
     * @param userId calling user
     * @return list of type names that are subtypes of asset mapped to their descriptions
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    Map<String, String> getTypesOfAssetWithDescriptions(String userId) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;
}

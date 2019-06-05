/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.Map;

/**
 * The AssetClassificationInterface is used by the asset owner to add classifications and detailed definitions
 * to the asset.  Typically this interface is used after the discovery services have explored the asset's content
 * and created helpful annotations to guide the asset owner.
 */
public interface AssetClassificationInterface
{
    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema).
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void  addSemanticAssignment(String    userId,
                                String    assetGUID,
                                String    glossaryTermGUID,
                                String    assetElementGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     *
     * @param userId calling user
     * @param assetGUID unique identifier of asset
     * @param organizationGUID Unique identifier (GUID) of the organization where this asset originated from - or null
     * @param businessCapabilityGUID  Unique identifier (GUID) of the business capability where this asset originated from.
     * @param otherOriginValues Descriptive labels describing origin of the asset
     * @throws InvalidParameterException entity not known, null userId or guid
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
    */
    void  addAssetOrigin(String                userId,
                         String                assetGUID,
                         String                organizationGUID,
                         String                businessCapabilityGUID,
                         Map<String, String>   otherOriginValues) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException;
}

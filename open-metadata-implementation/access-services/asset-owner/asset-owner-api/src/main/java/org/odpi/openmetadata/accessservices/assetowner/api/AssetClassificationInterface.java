/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.api;


import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * The AssetClassificationInterface is used by the asset owner to add classifications and detailed definitions
 * to the asset.  Typically this interface is used after the discovery services have explored the asset's content
 * and created helpful annotations to guide the asset owner.
 */
public interface AssetClassificationInterface
{
    /**
     * Create a simple relationship between a glossary term and an element in an Asset description (typically
     * a field in the schema.
     *
     * @param userId calling user
     * @param glossaryTermGUID unique identifier of the glossary term
     * @param assetElementGUID element to link it to - its type must inherit from Referenceable.
     *
     * @return unique identifier of the relationship.
     *
     * @throws InvalidParameterException entity not known
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    String  addSemanticAssignment(String    userId,
                                  String    glossaryTermGUID,
                                  String    assetElementGUID) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;
}

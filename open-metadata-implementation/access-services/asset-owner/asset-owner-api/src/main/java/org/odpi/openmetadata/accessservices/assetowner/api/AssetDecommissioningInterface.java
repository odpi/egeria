/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetowner.api;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

/**
 * The AssetDecommissioningInterface covers the steps that the asset owner needs to perform at the end of life of the
 * asset.  The requirements may be part of the license for the asset, or come from the governance program.
 * It includes updating the state of the asset, moving it to an archiving or purging zone and sending a
 * notification to report that the asset is no longer needed.  This notification will kick off the appropriate
 * cleanup processes.
 */
public interface AssetDecommissioningInterface
{
    /**
     * Deletes an asset and all of its associated elements such as schema, connections (unless they are linked to
     * another asset), discovery reports and associated feedback.
     *
     * Given the depth of the delete operation performed by this call, it should be used with care.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset to delete
     * @throws InvalidParameterException full path or userId is null
     * @throws PropertyServerException problem accessing property server
     * @throws UserNotAuthorizedException security access problem
     */
    void deleteAsset(String        userId,
                     String        assetGUID) throws InvalidParameterException,
                                                     UserNotAuthorizedException,
                                                     PropertyServerException;
}

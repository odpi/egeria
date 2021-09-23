/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.stewardshipaction;

/**
 * StewardshipActionInterface defines the client-side interface for the Stewardship Action OMAS.  This includes
 * operations to make corrections and updates to metadata.
 */
public interface StewardshipActionInterface
{
    /**
     * Assign a new owner to an asset.
     *
     * @param userId   identifier of the user issuing the request.
     * @param assetGUID   identifier of the asset needing a new owner.
     * @param owner   userId of the new owner.
     */
    void assignAssetOwner(String     userId,
                          String     assetGUID,
                          String     owner);


}

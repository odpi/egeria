/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetowner;

/**
 * The AssetDecommissioningInterface covers the steps that the asset owner needs to perform at the end of life of the
 * asset.  The requirements may be part of the license for the asset, or come from the governance program.
 * It includes updating the state of the asset, moving it to an archiving or purging zone and sending a
 * notification to report that the asset is no longer needed.  This notification will kick off the appropriate
 * cleanup processes.
 */
public interface AssetDecommissioningInterface
{

}

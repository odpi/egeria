/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


/**
 * Response for request of the tables of the schema. 
 * 
 *
 */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.Arrays;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;

public class AssetsResponse extends AnalyticsModelingOMASAPIResponse {
	
	/**
	 * Set list of asset GUIDs.
	 * @param assets GUIDs to set.
	 */
    public void setAssetList(ResponseContainerAssets assets) {
        this.setData(Arrays.asList(assets));
    }
}

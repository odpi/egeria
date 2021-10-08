/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


/**
 * Response for request of the tables of the schema. 
 * 
 *
 */

package org.odpi.openmetadata.accessservices.analyticsmodeling.responses;

import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AssetsResponse extends AnalyticsModelingOMASAPIResponse {
	
	private List<ResponseContainerAssets> data;

	/**
	 * Set list of asset GUIDs.
	 * @param assets GUIDs to set.
	 */
    public void setAssetList(ResponseContainerAssets assets) {
        data = Arrays.asList(assets);
    }
	/**
	 * Get list of asset GUIDs.
	 * @return list of asset GUIDs.
	 */
    public ResponseContainerAssets getAssetList() {
        return (data == null || data.isEmpty()) ? null : (ResponseContainerAssets) data.get(0);
    }
}

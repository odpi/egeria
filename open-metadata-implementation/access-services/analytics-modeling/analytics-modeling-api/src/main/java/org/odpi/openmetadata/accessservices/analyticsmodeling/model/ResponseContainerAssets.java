/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.Assets;

/**
 * Response container for list of GUIDs.<br>
 * The container has following attributes:<br>
 * &emsp;assets - list of affected asset GUIDs (created, modified, or deleted).<br>
 * 
 * The type of the container is 'assets'.
 * 
 * 
 */

public class ResponseContainerAssets extends ResponseContainer {
	
	public static final String TYPE_ASSETS = "assets";
	
	Assets attributes = new Assets();

	public ResponseContainerAssets() {
		super(TYPE_ASSETS);
	}
	
	public void setAssetsList(List<String> value) {
		attributes.setAssets(value);
	}
	
	public List<String>  getAssetsList() {
		return attributes.getAssets();
	}

}

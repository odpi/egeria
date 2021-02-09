/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import java.util.List;

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
	
	private static final String ASSETS = "assets";
	public static final String TYPE_ASSETS = "assets";

	public ResponseContainerAssets() {
		super(TYPE_ASSETS);
	}
	
	public void setAssetsList(List<String> value) {
		setAttribute(ASSETS, value);
	}
	
	@SuppressWarnings("unchecked")
	public List<String>  getAssetsList() {
		return (List<String>) getAttribute(ASSETS);
	}

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.MetadataModule;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.PhysicalModule;

/**
 * Response container for Analytics Modeling module representation.<br>
 * The container has following attributes:<br>
 * &emsp;physicalDefinition - Analytics Modeling module representation.<br>
 * 
 * The type of the container is 'module'.
 * 
 * 
 */
public class ResponseContainerModule extends ResponseContainer {

	public static final String TYPE_MODULE = "module";

	PhysicalModule attributes = new PhysicalModule();
	
	/**
	 * Constructor defines 'module' type
	 */
	public ResponseContainerModule() {
		super(TYPE_MODULE);
	}

	/**
	 * Set Analytics Modeling module.
	 * @param value module definition to set.
	 */
	public void setPhysicalModule(MetadataModule value) {
		attributes.setModule(value);
	}

	/**
	 * Get Analytics Modeling module.
	 * @return Analytics Modeling module.
	 */
	public MetadataModule  getPhysicalModule() {
		return attributes.getModule();
	}


}

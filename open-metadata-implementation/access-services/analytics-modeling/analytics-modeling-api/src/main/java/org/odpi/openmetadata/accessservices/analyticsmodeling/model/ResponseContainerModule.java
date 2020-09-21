/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.MetadataModule;

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

	private static final String PHYSICAL_DEFINITION = "physicalDefinition";
	public static final String TYPE_MODULE = "module";

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
		setAttribute(PHYSICAL_DEFINITION, value);
	}

	/**
	 * Get Analytics Modeling module.
	 * @return Analytics Modeling module.
	 */
	public MetadataModule  getPhysicalModule() {
		return (MetadataModule) getAttribute(PHYSICAL_DEFINITION);
	}


}

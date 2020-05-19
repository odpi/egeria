/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model;

import org.odpi.openmetadata.accessservices.cognos.model.module.Module;

/**
 * Response container for Cognos physical module representation.<br>
 * The container has following attributes:<br>
 * &emsp;physicalDefinition - Cognos physical module representation.<br>
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
	 * Set Cognos physical module.
	 * @param value module definition to set.
	 */
	public void setPhysicalModule(Module value) {
		setAttribute(PHYSICAL_DEFINITION, value);
	}

	/**
	 * Get Cognos physical module.
	 * @return Cognos physical module.
	 */
	public Module  getPhysicalModule() {
		return (Module) getAttribute(PHYSICAL_DEFINITION);
	}


}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model;

import org.odpi.openmetadata.accessservices.cognos.model.module.Module;

public class ResponseContainerModule extends ResponseContainer {

	private static final String PHYSICAL_DEFINITION = "physicalDefinition";
	public static final String TYPE_MODULE = "module";

	public ResponseContainerModule() {
		super(TYPE_MODULE);
	}

	public void setPhysicalModule(Module value) {
		setAttribute(PHYSICAL_DEFINITION, value);
	}

	@SuppressWarnings("unchecked")
	public Module  getPhysicalModule() {
		return (Module) getAttribute(PHYSICAL_DEFINITION);
	}


}

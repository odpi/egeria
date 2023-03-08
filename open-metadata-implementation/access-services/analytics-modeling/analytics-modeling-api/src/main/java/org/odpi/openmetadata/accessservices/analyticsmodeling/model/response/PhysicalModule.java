/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model.response;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.MetadataModule;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Schema contains the properties and guid for a database schema.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PhysicalModule {

	@JsonProperty("physicalDefinition")
	MetadataModule module;

	/**
	 * Get list of tables in database schema.
	 * @return list of tables.
	 */
	public MetadataModule getModule() {
		return module;
	}
	
	/**
	 * Set list of table names of the database schema.
	 * @param value to set.
	 */
	public void setModule(MetadataModule value) {
		module = value;
	}
}

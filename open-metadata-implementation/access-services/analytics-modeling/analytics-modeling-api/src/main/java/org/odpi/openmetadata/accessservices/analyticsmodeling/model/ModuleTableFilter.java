/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *	Filter for tables to include in a module.
 *
 *	Filter can have two mutually excluded definitions:
 *  1. List of tables explicitly include in the module.
 *  2. All tables except listed should be included in the module.
 *  
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ModuleTableFilter {
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class Envelop {
		
		@JsonProperty("excludedTables")
		private List<String> excludedTables;
		@JsonProperty("includedTables")
		private List<String> includedTables;
		
		public List<String> getExcludedTables() {
			return excludedTables;
		}
		public void setExcludedTables(List<String> excludedTables) {
			this.excludedTables = excludedTables;
		}
		public List<String> getIncludedTables() {
			return includedTables;
		}
		public void setIncludedTables(List<String> includedTables) {
			this.includedTables = includedTables;
		}
		
	};
	
	@JsonProperty("meta")
	Envelop meta;

	public Envelop getMeta() {
		if (meta == null) {
			meta = new Envelop();
		}
		return meta;
	}

	public void setMeta(Envelop meta) {
		this.meta = meta;
	}
	
	/**
	 * Test if the value passes filter conditions.
	 * @param value to test.
	 * @return true if passes.
	 */
	public boolean match(String value) {

		if (meta == null) {
			return true; 		// passed if empty
		}
		
		if (getMeta().getExcludedTables() != null) {
			// not passed if excluded list is define and value is listed
			return !getMeta().getExcludedTables().contains(value);
		} else if (getMeta().getIncludedTables() != null) {
			// passed if included list is define and value is listed
			return getMeta().getIncludedTables().contains(value);
		}
		return true; 		// passed if there are no conditions
	}

}

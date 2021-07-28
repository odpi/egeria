/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Base class container for the generic data in a response.<br>
 * 
 * Each response data has set of attributes defined by names.<br>
 * Id - identifier of the response data<br>
 * Type - set by subclass to identify content of the response.<br>
 * 
 */
@JsonAutoDetect(getterVisibility=NONE, setterVisibility=NONE, fieldVisibility=ANY)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseContainer {

	private String id;
	private final String type;

	/**
	 * Default constructor required for serialization.
	 */
	ResponseContainer() {
		type = null;
	}

	/**
	 * Constructor defines type of response/data stored
	 * @param type of the response.
	 */
	ResponseContainer(String type) {
		this.type = type;
	}
	/**
	 * Get response data identifier.
	 * @return response data identifier.
	 */
	public String getId() {
		return id;
	}

	/**
	 * Set response data identifier.
	 * @param id response data identifier.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * Get type of the response data.
	 * @return type of the response data.
	 */
	public String getType() {
		return type;
	}
	
}

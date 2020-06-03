/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

import java.util.SortedMap;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Base class container for the generic data in a response.<br>
 * 
 * Each response data has set of attributes defined by names.<br>
 * Id - identifier of the response data<br>
 * Type - set by subclass to identify content of the response.<br>
 * 
 * 
 *
 */
@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ResponseContainer {

	@JsonProperty("attributes")
	private SortedMap<String, Object> attributes = new TreeMap<>();
	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private final String type;
	
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
	/**
	 * Get attribute value identified by name.
	 * @param key attribute identifier
	 * @return value of the requested attribute.
	 */
	protected Object getAttribute(String key) {
		return attributes.get(key);
	}

	/**
	 * Set attribute value identified by name.
	 * @param key attribute identifier
	 * @param value of the attribute to be set.
	 */
	protected void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}


}

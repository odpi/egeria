/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.cognos.model;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonAutoDetect(getterVisibility = NONE, setterVisibility = NONE, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class ResponseContainer {

	@JsonProperty("attributes")
	private Attributes attributes = new Attributes();
	@JsonProperty("id")
	private String id;
	@JsonProperty("type")
	private String type;
	
	
	ResponseContainer(String type) {
		this.setType(type);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	protected void setType(String type) {
		this.type = type;
	}
	
	protected Object getAttribute(String key) {
		return attributes.get(key);
	}

	protected void setAttribute(String key, Object value) {
		this.attributes.put(key, value);
	}


}

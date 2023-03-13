/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model.response;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Database contains the properties and guid for a database.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Database {

	String guid;
	String name;
	String type;
	String version;
	
	/**
	 * Get GUID of the database.
	 * @return GUID of the database.
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Set GUID of the database.
	 * @param guid to set.
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}

	/**
	 * Get name of the database.
	 * @return name of the database.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of the database.
	 * @param name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get type of the database.
	 * @return type of the database.
	 */
	public String getType() {
		return type;
	}
	
	/**
	 * Set type of the database.
	 * @param type to set.
	 */
	public void setType(String type) {
		this.type = type;
	}
	
	/**
	 * Get version of the database.
	 * @return version of the database.
	 */
	public String getVersion() {
		return version;
	}
	/**
	 * Set version of the database.
	 * @param version to set.
	 */

	public void setVersion(String version) {
		this.version = version;
	}
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.metadata;

/**
 * Database contains the properties and guid for a database entity retrieved from the metadata repository.
 */
public class Database extends MetadataBase {

	String name;
	String type;
	String version;

	/**
	 * Get name of the database.
	 * @return name of the database.
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set name of the database bean usually from entity.
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
	 * Set type of the database bean usually from entity.
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
	 * Set version of the data base bean usually from entity.
	 * @param version to set.
	 */

	public void setVersion(String version) {
		this.version = version;
	}
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.metadata;


/**
 * Base class for metadata beans retrieved from the metadata repository.
 */
public class MetadataBase {
	
	String guid;

	/**
	 * Get repository entity GUID for bean instantiated from that entity.
	 * @return guid.
	 */
	public String getGuid() {
		return guid;
	}

	/**
	 * Set GUID of the bean usually from the entity.
	 * @param guid to set.
	 */
	public void setGuid(String guid) {
		this.guid = guid;
	}
}

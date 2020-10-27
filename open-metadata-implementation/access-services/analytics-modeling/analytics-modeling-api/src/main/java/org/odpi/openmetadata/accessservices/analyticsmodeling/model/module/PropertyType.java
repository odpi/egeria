/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

/**
 *  Generic property abstraction.
 *	<br>Sample:<br>
 *	{<br>
 *	&emsp;	"name": "guid",<br>
 *	&emsp;	"value": "abc_123"<br>
 *	}
 *
 */
public class PropertyType implements IModuleObject {
	
	private String name;
	private String value;
	
	public PropertyType() {
	}

	public PropertyType(String name, String value) {
		this.name = name;
		this.value = value;
	}
	/**
	 * Get name of the property.
	 * @return name of the property.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set name of the property.
	 * @param name of the property.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get property value.
	 * @return value of the property.
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Set property value.
	 * @param value of the property.
	 */
	public void setValue(String value) {
		this.value = value;
	}

}

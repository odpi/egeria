/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

import java.util.ArrayList;
import java.util.List;

/**
 * Base class for model objects having custom properties.
 * <br>Sample:<br>
 *	{<br>
 *	&emsp;	"property": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType}<br>
 *	&emsp;	]<br>
 *	} 
 **/
public class BaseObjectType implements IModuleObject {
	
	List<PropertyType> property;

	/**
	 * Get list of the object properties.
	 * @return  list of the object properties.
	 */
	public List<PropertyType> getProperty() {
		return property;
	}

	/**
	 * Set list of the object properties.
	 * @param property list.
	 */
	public void setProperty(List<PropertyType> property) {
		this.property = property;
	}
	
	/**
	 * Get property by name.
	 * @param name of the property.
	 * @return the property or null if not found.
	 */
	public PropertyType getPropertyByName(String name) {
		if (property == null || name == null) {
			return null;
		}
		return property.stream()
			.filter(prop->name.equals(prop.getName()))
			.findFirst().orElse(null);
	}
	
	/**
	 * Add property to the collection.
	 * @param property to add.
	 */
	void addProperty(PropertyType property) {
		if(this.property == null) {
			this.property = new ArrayList<>();
		}
		this.property.add(property);
	}
	/**
	 * Add property to the collection.
	 * @param name of the property.
	 * @param value of the property.
	 */
	public void addProperty(String name, String value) {
		if(this.property == null) {
			this.property = new ArrayList<>();
		}
		this.property.add(new PropertyType(name, value));
	}

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

import java.util.List;

/**
 * Database table primary key abstraction.
 * <br>Sample:<br>
 *	{<br>
 *	&emsp;	"keyedColumn": [&nbsp;
 *				"AddressID"
 *	&emsp;	],<br>
 *	&emsp;	"name": "PK_Address_AddressID"<br>
 *	}<br>
 *
 *
 */
public class PrimaryKey implements IModuleObject {

	private String name;
	private List<String> keyedColumn;

	/**
	 * Get name of the primary key. Usually PK_TableName
	 * @return name of the primary key
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set the name of primary key.
	 * @param name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get list of columns names defining primary key.
	 * @return set of columns names defining primary key.
	 */
	public List<String> getKeyedColumn() {
		return keyedColumn;
	}
	/**
	 * Set list of names defining primary key columns.
	 * @param keyedColumn list of names defining primary key columns.
	 */
	public void setKeyedColumn(List<String> keyedColumn) {
		this.keyedColumn = keyedColumn;
	}
}

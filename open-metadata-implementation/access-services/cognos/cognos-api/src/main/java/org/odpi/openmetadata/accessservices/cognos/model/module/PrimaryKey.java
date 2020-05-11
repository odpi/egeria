/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;

import java.util.List;

/**
 * Database table primary key abstraction.
 * Sample:
 *	{
 *		"keyedColumn": [
 *			"AddressID"
 *		],
 *		"name": "PK_Address_AddressID"
 *	}
 *
 * @author YEVGENIYMarchenko
 *
 */
public class PrimaryKey implements IModuleObject {

	private String name;
	private List<String> keyedColumn;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<String> getKeyedColumn() {
		return keyedColumn;
	}
	public void setKeyedColumn(List<String> keyedColumn) {
		this.keyedColumn = keyedColumn;
	}
}

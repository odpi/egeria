/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;


/**
 *  Relational database column abstraction.
 *	Sample:
 *	{
 *		"name": "AddressID"
 *		"vendorType": "INT32",
 *		"nullable": false,
 *		"datatype": "INTEGER",
 *	}
 * @author YEVGENIYMarchenko
 *
 */
public class Column implements IModuleObject {

	private String name;
	private String vendorType;
	private Boolean nullable;
	private String datatype;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getVendorType() {
		return vendorType;
	}
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	public Boolean isNullable() {
		return nullable;
	}
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}
	public String getDatatype() {
		return datatype;
	}
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}

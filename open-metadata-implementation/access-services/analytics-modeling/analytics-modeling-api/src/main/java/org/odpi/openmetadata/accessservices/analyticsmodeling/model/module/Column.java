/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;


/**
 *  Relational database column abstraction.
 *	<br>Sample:<br>
 *	{<br>
 *	&emsp;	"property": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType}<br>
 *	&emsp;	],<br>
 *	&emsp;	"name": "AddressID",<br>
 *	&emsp;	"vendorType": "INT32",<br>
 *	&emsp;	"nullable": false,<br>
 *	&emsp;	"datatype": "INTEGER"<br>
 *	}
 *
 */
public class Column extends BaseObjectType {

	private String name;
	private String vendorType;
	private Boolean nullable;
	private String datatype;

	/**
	 * Get name of the relational database column.
	 * @return name of the relational database column.
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set name of the relational database column.
	 * @param name of the relational database column.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Get name of the relational database column data type defined by vendor.
	 * @return name of the relational database column data type defined by vendor.
	 */
	public String getVendorType() {
		return vendorType;
	}
	/**
	 * Set name of the relational database column data type defined by vendor.
	 * @param vendorType name of the relational database column data type defined by vendor.
	 */
	public void setVendorType(String vendorType) {
		this.vendorType = vendorType;
	}
	/**
	 * Query if the column value can be NULL - not set
	 * @return true if the column value can be NULL
	 */
	public Boolean isNullable() {
		return nullable;
	}
	/**
	 * Define if the column value can be NULL - not set
	 * @param nullable true if the column value can be NULL
	 */
	public void setNullable(Boolean nullable) {
		this.nullable = nullable;
	}
	
	/**
	 * Get name of the column data type defined by ODBC standard.
	 * @return name of the column data type defined by ODBC standard.
	 */
	public String getDatatype() {
		return datatype;
	}
	/**
	 * Set name of the column data type defined by ODBC standard.
	 * @param datatype name of the column data type defined by ODBC standard.
	 */
	public void setDatatype(String datatype) {
		this.datatype = datatype;
	}
}

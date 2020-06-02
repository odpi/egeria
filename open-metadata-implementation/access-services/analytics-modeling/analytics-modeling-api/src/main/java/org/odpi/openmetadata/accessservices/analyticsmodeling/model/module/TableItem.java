/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *	Container to define column and additional attributes related to it.
 *	<br>Sample:<br>
 * 	{<br>
 *	&emsp;	"column": {@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.Column}<br>
 *	}
 *
 */
public class TableItem implements IModuleObject {

	private Column column;
	@JsonIgnore
	private int position;	// used for sorting columns within table, should not be serialized
	@JsonIgnore
	private String pkName;	// used to build PK for table, should not be serialized
	@JsonIgnore
	private List<String> referencedColumns;	// GUIDs for referenced columns, used to build FK for table, should not be serialized

	/**
	 * Get list of GUIDs for referenced columns. 
	 * @return GUIDs list.
	 */
	public List<String> getReferencedColumns() {
		return referencedColumns;
	}

	/**
	 * Set list of referenced columns defined by GUID.
	 * @param referencedColumns list of referenced columns defined by GUID.
	 */
	public void setReferencedColumns(List<String> referencedColumns) {
		this.referencedColumns = referencedColumns;
	}

	/**
	 * Get name of the primary key.
	 * @return name of the primary key.
	 */
	public String getPkName() {
		return pkName;
	}

	/**
	 * Set name of the primary key.
	 * @param pkName name of the primary key.
	 */
	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	/**
	 * Get position of the item in the table.
	 * @return  position of the item in the table.
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * Set position of the item in the table.
	 * @param  position of the item in the table.
	 */
	public void setPosition(int position) {
		this.position = position;
	}

	/**
	 * Get column definition.
	 * @return column definition.
	 */
	public Column getColumn() {
		return column;
	}

	/**
	 * Set column definition.
	 * @param column definition.
	 */
	public void setColumn(Column column) {
		this.column = column;
	}
}

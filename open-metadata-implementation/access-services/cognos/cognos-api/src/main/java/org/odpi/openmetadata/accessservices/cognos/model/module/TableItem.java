/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * 	{
		"column": {
			@see org.odpi.openmetadata.accessservices.cognos.model.module.Column
		}
	},

 * @author YEVGENIYMarchenko
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

	public List<String> getReferencedColumns() {
		return referencedColumns;
	}

	public void setReferencedColumns(List<String> referencedColumns) {
		this.referencedColumns = referencedColumns;
	}

	public String getPkName() {
		return pkName;
	}

	public void setPkName(String pkName) {
		this.pkName = pkName;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}



}

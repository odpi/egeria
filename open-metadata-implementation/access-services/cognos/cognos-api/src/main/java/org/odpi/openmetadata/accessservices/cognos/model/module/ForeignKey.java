/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;

import java.util.List;

/**
 * Database table foreign key abstraction.
 *	Sample:
 *	{
 *		"name": "FK_Address_StateProvince_StateProvinceID",
 *		"fkColumn": [
 *			{
 *				@see org.odpi.openmetadata.accessservices.cognos.model.module.ForeignColumn
 *			}
 *		]
 *	}
 *
 * @author YEVGENIYMarchenko
 *
 */
public class ForeignKey implements IModuleObject {
	
	private String name;
	private List<ForeignColumn> fkColumn;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ForeignColumn> getFkColumn() {
		return fkColumn;
	}
	public void setFkColumn(List<ForeignColumn> fkColumn) {
		this.fkColumn = fkColumn;
	}

}

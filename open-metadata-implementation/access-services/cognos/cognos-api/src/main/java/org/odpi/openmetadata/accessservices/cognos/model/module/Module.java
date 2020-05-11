/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.cognos.model.module;

import java.util.List;

/**
 * COGNOS MOSER physical module.
 * Sample:
 * {
 *		"identifier": "AdventureWorks2014.Person",
 *		"version": "9.0",
 *		"dataSource": [
 *			{ @see org.odpi.openmetadata.accessservices.cognos.model.module.DataSource }
 *		]
 *	}
 * @author YEVGENIYMarchenko
 *
 */
public class Module implements IModuleObject {

	private String identifier;
	private String version = "9.0";	// fixed version matching MOSER XSD on the moment of development.
	private List <DataSource> dataSource;

	public String getIdentifier() {
		return identifier;
	}
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public List <DataSource> getDataSource() {
		return dataSource;
	}
	public void setDataSource(List <DataSource> dataSource) {
		this.dataSource = dataSource;
	}


}

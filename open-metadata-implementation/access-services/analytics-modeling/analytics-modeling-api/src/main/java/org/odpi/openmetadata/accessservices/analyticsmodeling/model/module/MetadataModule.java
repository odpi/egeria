/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.model.module;

import java.util.List;

/**
 * Analytics Modeling service module.
 * <br>Sample:<br>
 * {<br>
 *	&emsp;	"property": [&nbsp;
 *	&emsp;		{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.PropertyType}<br>
 *	&emsp;	],<br>
 *	&emsp;	"identifier": "AdventureWorks2014.Person",<br>
 *	&emsp;	"version": "9.0",<br>
 *	&emsp;	"dataSource": [&nbsp;
 *			{@link org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.DataSource}
 *	&emsp;	]<br>
 *	}
 *
 */
public class MetadataModule extends BaseObjectType {

	private String identifier;
	private String version = "9.0";	// fixed version matching MOSER XSD on the moment of development.
	private List <DataSource> dataSource;

	/**
	 * Get module identifier.
	 * @return module identifier.
	 */
	public String getIdentifier() {
		return identifier;
	}
	/**
	 * Set module identifier.
	 * @param identifier of the module.
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	/**
	 * Get version of the module.
	 * @return version of the module.
	 */
	public String getVersion() {
		return version;
	}
	
	/**
	 * Set version of the module.
	 * @param version of the module.
	 */
	public void setVersion(String version) {
		this.version = version;
	}
	
	/**
	 * Get data source element of the module.
	 * @return data source element of the module.
	 */
	public List <DataSource> getDataSource() {
		return dataSource;
	}
	/**
	 * Set data source element of the module.
	 * @param dataSource element
	 */
	public void setDataSource(List <DataSource> dataSource) {
		this.dataSource = dataSource;
	}


}

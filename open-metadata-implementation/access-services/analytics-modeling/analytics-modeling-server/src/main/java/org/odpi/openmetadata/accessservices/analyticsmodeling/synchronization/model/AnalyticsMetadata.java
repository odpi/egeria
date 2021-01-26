/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;

import java.util.ArrayList;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaAttribute;

public abstract class AnalyticsMetadata extends SchemaAttribute {

	private String identifier;				// identifier within parent element
	private List<String> sourceGuid;		// GUIDs of external metadata objects
	private List<String> sourceId;			// IDs of external metadata objects
	private String type;
	

	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}

	public enum Type {
		
		QUERY_ITEM ("queryItem"),		// metadata stored in data bases like RDBMS, OLAP, CSV files, etc.
		DATA_ITEM ("dataItem"),			// projected metadata within visualization
		QUERY_SUBJECT ("querySubject"),	// group of related metadata items: Relation/Table/View in RDBMS, Dimension in OLAP
		QUERY ("query"),				// group of metadata items projected in a query: SQL (RDBMS), MDX (OLAP)
		PAGE ("page"),					// multiple page report logical grouping for widgets
		TAB ("tab"),					// multiple tab per page logical grouping for widgets
		WIDGET("widget"),				// visual elements with metadata items
		BASE_MODULE ("baseModule"),		// asset of metadata connected to native repository objects
		MODULE ("module"),				// asset of metadata defined by base module items
		REPORT("report"),				// report visualization
		DASHBOARD("dashboard");			// dashboard visualization
		
		
		Type (String value) {
			name = value;
		}
	
		public String getName() {
			return name;
		}

		private String name;
	};
	
	/**
	 * Getter method for attribute identifier
	 * @return the identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Setter method for attribute identifier
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	/**
	 * @return the sourceGuid
	 */
	public List<String> getSourceGuid() {
		return sourceGuid;
	}

	/**
	 * @param sourceGuid the sourceGuid to set
	 */
	public void setSourceGuid(List<String> sourceGuid) {
		this.sourceGuid = sourceGuid;
	}

	/**
	 * @return the sourceId
	 */
	public List<String> getSourceId() {
		return sourceId;
	}

	/**
	 * @param sourceId the sourceId to set
	 */
	public void setSourceId(List<String> sourceId) {
		this.sourceId = sourceId;
	}
	
	public void addSourceGuid(String guid) {
		if (sourceGuid == null) {
			sourceGuid = new ArrayList<>();
		}
		sourceGuid.add(guid);
	}

	public void addSourceId(String id) {
		if (sourceId == null) {
			sourceId = new ArrayList<>();
		}
		sourceId.add(id);
	}
	
	public abstract void convertProperties();

}

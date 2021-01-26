/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;

import java.util.ArrayList;
import java.util.List;

/**
 *	MetadataItem is an abstraction of something that references source of data.
 *
 */
public class MetadataItem extends AnalyticsMetadata {

	private String expression;
	private String dataType;

	private List<MetadataItem> items;

	/**
	 * @return the items
	 */
	public List<MetadataItem> getItem() {
		return items;
	}
	/**
	 * @param items the items to set
	 */
	public void setItem(List<MetadataItem> items) {
		this.items = items;
	}
	
	public void addItem(MetadataItem item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}

	public MetadataItem removeItem(int index) {
		if (items != null && items.size() > index) {
			return items.remove(index);
		}
		return null;
	}

	public MetadataItem getItem(int index) {
		if (items != null && items.size() > index) {
			return items.get(index);
		}
		return null;
	}
	
	/**
	 * @return the qualifiedName
	 */
	public String getQualifiedName() {
		return qualifiedName;
	}
	/**
	 * @param qualifiedName the qualifiedName to set
	 */
	public void setQualifiedName(String qualifiedName) {
		this.qualifiedName = qualifiedName;
	}
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	@Override
	public void convertProperties() {
		// TODO Auto-generated method stub
		
	}
}

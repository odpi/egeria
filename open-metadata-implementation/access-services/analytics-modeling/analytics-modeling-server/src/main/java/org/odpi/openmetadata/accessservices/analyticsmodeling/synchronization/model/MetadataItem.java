/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.ArrayList;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 *	MetadataItem is an abstraction of something that references source of
 *	simple data that have same type like integer, string, etc.
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataItem extends AnalyticsMetadata {

	private String expression;
	private String dataType;

	/**
	 * Nested items.
	 * OLAP hierarchy/levels/members.
	 * DATE, TIMESTAMP consist of smaller items.
	 */
	private List<MetadataItem> items;

	/**
	 * Get the nested data.
	 * @return the items.
	 */
	public List<MetadataItem> getItem() {
		return items;
	}
	/**
	 * Set all nested items.
	 * @param items the items to set.
	 */
	public void setItem(List<MetadataItem> items) {
		this.items = items;
	}
	
	/**
	 * Add single nested item.
	 * @param item to add.
	 */
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
	protected void convertProperties() {
        this.setExpression(additionalProperties.get(Constants.SYNC_EXPRESSION));
        this.setDataType(additionalProperties.get(Constants.SYNC_DATA_TYPE));
	}
	
	/**
	 * The function to save custom properties as additional properties.
	 */
	@Override
	protected void prepareCustomProperties() {
        additionalProperties.put(Constants.SYNC_EXPRESSION, this.getExpression());
        additionalProperties.put(Constants.SYNC_DATA_TYPE, this.getDataType());
	}
}

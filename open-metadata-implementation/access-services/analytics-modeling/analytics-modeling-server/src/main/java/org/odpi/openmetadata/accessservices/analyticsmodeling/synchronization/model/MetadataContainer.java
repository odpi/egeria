/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * MetadataContainer is abstraction of grouping element for 
 * {@link org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem}
 * and nested containers.
 *
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataContainer extends AnalyticsMetadata {
	
	private List<MetadataContainer> container;
	private List<MetadataItem> item;

	/**
	 * @return the item
	 */
	public List<MetadataItem> getItem() {
		return item;
	}

	/**
	 * @param item the items to set
	 */
	public void setItem(List<MetadataItem> item) {
		this.item = item;
	}

	public void addItem(MetadataItem item) {
		if (this.item == null) {
			this.item = new ArrayList<>();
		}
		this.item.add(item);
	}

	public MetadataItem removeItem(int index) {
		if (item != null && item.size() > index) {
			return item.remove(index);
		}
		return null;
	}

	public MetadataItem getItem(int index) {
		if (item != null && item.size() > index) {
			return item.get(index);
		}
		return null;
	}
	
	/**
	 * @return the container
	 */
	public List<MetadataContainer> getContainer() {
		return container;
	}

	/**
	 * @param container the container to set
	 */
	public void setContainer(List<MetadataContainer> container) {
		this.container = container;
	}

	public void addContainer(MetadataContainer child) {
		if (container == null) {
			container = new ArrayList<>();
		}
		container.add(child);
	}

	public MetadataContainer removeContainer(int index) {
		if (container != null && container.size() > index) {
			return container.remove(index);
		}
		return null;
	}

	public MetadataContainer getContainer(int index) {
		if (container != null && container.size() > index) {
			return container.get(index);
		}
		return null;
	}

	@Override
	public void convertProperties() {
		
	}
	
	/**
	 * The function to save custom properties as additional properties.
	 */
	@Override
	protected void prepareCustomProperties() {
		// container does not have any custom property yet
	}


	

}

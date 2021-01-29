/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;


import java.util.ArrayList;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.Asset;

public class AnalyticsAsset extends Asset {

	private String uid;					// external unique identifiers
	private String location;			// external location of the asset
	private String type;				// type defines asset content

	private List<String> sourceGuid;	// GUIDs of external metadata objects

	private List<AssetReference> reference;
	private List<MetadataContainer> container;
	private List<MetadataItem> item;

	private List<MetadataContainer> visualization;


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
	
	public void addItem(MetadataItem item) {
		if (this.item == null) {
			this.item = new ArrayList<>();
		}
		this.item.add(item);
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getUid() {
		return uid;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public List<String> getSourceGuid() {
		return sourceGuid;
	}

	public void setSourceGuid(List<String> sourceGuid) {
		this.sourceGuid = sourceGuid;
	}

	public void addSourceGuid(String guid) {
		if (sourceGuid == null) {
			sourceGuid = new ArrayList<>();
		}
		sourceGuid.add(guid);
	}

	public List<AssetReference> getReference() {
		return reference;
	}

	public void setReference(List<AssetReference> reference) {
		this.reference = reference;
	}

	public void addReference(AssetReference theReference) {
		if (reference == null) {
			reference = new ArrayList<>();
		}
		reference.add(theReference);
	}

	public List<MetadataContainer> getContainer() {
		return container;
	}

	public void setContainer(List<MetadataContainer> container) {
		this.container = container;
	}

	public List<MetadataItem> getItem() {
		return item;
	}

	public void setItem(List<MetadataItem> items) {
		this.item = items;
	}
	
	/**
	 * @return the visualization
	 */
	public List<MetadataContainer> getVisualization() {
		return visualization;
	}
	/**
	 * @param visualization the visualization to set
	 */
	public void setVisualization(List<MetadataContainer> visualization) {
		this.visualization = visualization;
	}
	
	/**
	 * @return the location
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * @param location the location to set
	 */
	public void setLocation(String location) {
		this.location = location;
	}
	
	public boolean isVisualization() {
		return visualization != null;
	}
	
	public boolean hasMetadataModule() {
		return (container != null && !container.isEmpty())
				|| (item != null && !item.isEmpty());
	}


}

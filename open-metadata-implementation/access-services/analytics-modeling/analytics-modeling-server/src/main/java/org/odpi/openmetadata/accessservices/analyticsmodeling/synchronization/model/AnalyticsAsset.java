/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;


import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.Asset;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnalyticsAsset extends Asset {

	private String uid;					// external unique identifiers
	private String location;			// external location of the asset
	private String type;				// type defines asset content
	private String lastModified;		// string timestamp

	private List<String> sourceGuid;	// GUIDs of external metadata objects

	private List<AssetReference> reference;
	private List<MetadataContainer> container;
	private List<MetadataItem> item;

	private List<MetadataContainer> visualization;
	
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

	public List<AssetReference> getReference() {
		return reference;
	}

	public void setReference(List<AssetReference> reference) {
		this.reference = reference;
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
	
	/**
	 * Get string time stamp when the module was modified.
	 * @return the lastModified
	 */
	public String getLastModified() {
		return lastModified;
	}

	/**
	 * Set string time stamp when the module was modified.
	 * @param lastModified the lastModified to set
	 */
	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	@Override
	public boolean equals(Object obj) {
		
		if (this == obj) {
			return true;
		}
		
		if (!(obj instanceof AnalyticsAsset)) {
			return false;
		}
		
		AnalyticsAsset asset = (AnalyticsAsset)obj;
		
		return Objects.equals(uid, asset.uid)
				&& Objects.equals(location, asset.location)
				&& Objects.equals(type, asset.type)
				&& Objects.equals(lastModified, asset.lastModified)
				&& super.equals(asset);
	}
	
    /**
     * Return a number that represents the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), uid, location, type, lastModified);
    }
}

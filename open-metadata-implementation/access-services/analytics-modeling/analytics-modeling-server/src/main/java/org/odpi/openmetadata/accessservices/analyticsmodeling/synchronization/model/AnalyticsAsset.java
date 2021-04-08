/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.Asset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;


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

	public boolean isVisualization() {
		return visualization != null;
	}
	
	public boolean hasMetadataModule() {
		return (container != null && !container.isEmpty())
				|| (item != null && !item.isEmpty());
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
	
	/**
	 * Check if internal data of the asset is modified.
	 * @param asset to check.
	 * @return true if modified.
	 * @throws AnalyticsModelingCheckedException if different assets are compared. 
	 */
	public boolean isModified(AnalyticsAsset asset) throws AnalyticsModelingCheckedException {
		if (!Objects.equals(guid, asset.guid)) {
			// only same instance should be compared
			String methodName = "isModified";
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.ILLEGAL_OPERATION.getMessageDefinition(methodName, asset.getQualifiedName()),
					this.getClass().getSimpleName(),
					methodName);
		}
		return     !Objects.equals(lastModified, asset.lastModified)
				|| !Objects.equals(uid, asset.uid)
				|| !Objects.equals(location, asset.location)
				|| !Objects.equals(type, asset.type)
				|| !Objects.equals(reference, asset.reference);
	}
	
	public Map<String, String> buildAdditionalProperties() {
		Map<String, String>  additionalProperties = new HashMap<>();
        additionalProperties.put(Constants.TYPE, this.getType());
        additionalProperties.put(Constants.LASTMODIFIED, this.getLastModified());
        additionalProperties.put(Constants.LOCATION, this.getLocation());

        if (this.getReference() != null ) {
            try {
    			String references = new ObjectMapper().writeValueAsString(this.getReference());
    	        additionalProperties.put(Constants.REFERENCE, references);
    		} catch (JsonProcessingException e) {
    			// log to execution context
    		}
        }
        return additionalProperties;
	}
	
	/**
	 * Find reference to asset with GUID.
	 * @param guid to search.
	 * @return asset reference or null.
	 */
	public AssetReference getAssetReferenceByGuid(String guid) {
		if (reference == null) {
			return null;
		}
		return reference.stream().filter(r->guid.equals(r.getGuid())).findFirst().orElse(null);
	}

}

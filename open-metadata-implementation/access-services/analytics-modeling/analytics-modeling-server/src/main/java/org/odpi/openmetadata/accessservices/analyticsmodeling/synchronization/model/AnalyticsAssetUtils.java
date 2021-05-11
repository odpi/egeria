/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;

/**
 * Class provides useful operations with bean class
 * {@link org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset}.
 *
 */
public class AnalyticsAssetUtils {

	/**
	 * Add container to the asset.
	 * @param asset to add new container to.
	 * @param container to add.
	 */
	public static void addContainer(AnalyticsAsset asset, MetadataContainer container) {
		if (asset.getContainer() == null) {
			asset.setContainer(new ArrayList<>());
		}
		asset.getContainer().add(container);
	}

	/**
	 * Remove container from asset at defined index.
	 * @param asset to remove container from.
	 * @param index to remove.
	 * @return removed container or null if requested container does not exists.
	 */
	public static MetadataContainer removeContainer(AnalyticsAsset asset, int index) {
		if (asset.getContainer() != null && asset.getContainer().size() > index) {
			return asset.getContainer().remove(index);
		}
		return null;
	}

	/**
	 * get container from asset at defined index.
	 * @param asset to get container from.
	 * @param index to get.
	 * @return requested container or null if it does not exist.
	 */
	public static MetadataContainer getContainer(AnalyticsAsset asset, int index) {
		if (asset.getContainer() != null && asset.getContainer().size() > index) {
			return asset.getContainer().get(index);
		}
		return null;
	}
	
	/**
	 * Add item to the asset.
	 * @param asset to add new item to.
	 * @param item to add.
	 */
	public static void addItem(AnalyticsAsset asset, MetadataItem item) {
		if (asset.getItem() == null) {
			asset.setItem(new ArrayList<>());
		}
		asset.getItem().add(item);
	}

	/**
	 * Add source GUID to the asset.
	 * @param asset to add new GUID to.
	 * @param guid to add.
	 */
	public static void addSourceGuid(AnalyticsAsset asset, String guid) {
		if (asset.getSourceGuid() == null) {
			asset.setSourceGuid(new ArrayList<>());
		}
		asset.getSourceGuid().add(guid);
	}

	/**
	 * Add reference to the asset.
	 * @param asset to add new reference to.
	 * @param reference to add.
	 */
	public static void addReference(AnalyticsAsset asset, AssetReference reference) {
		if (asset.getReference() == null) {
			asset.setReference(new ArrayList<>());
		}
		asset.getReference().add(reference);
	}

	/**
	 * Check if asset is visualization.
	 * @param asset to check.
	 * @return true if asset has any visualization.
	 */
	public static boolean isVisualization(AnalyticsAsset asset) {
		return asset.getVisualization() != null;
	}
	
	/**
	 * Check if asset is metadata module.
	 * @param asset to check.
	 * @return true if asset has any metadata element.
	 */
	public static boolean hasMetadataModule(AnalyticsAsset asset) {
		return (asset.getContainer() != null && !asset.getContainer().isEmpty())
				|| (asset.getItem() != null && !asset.getItem().isEmpty());
	}

	/**
	 * Build additional properties of the bean from asset content.
	 * @param asset provides property values.
	 * @return map of properties by name.
	 */
	public static Map<String, String> buildAdditionalProperties(AnalyticsAsset asset) {
		Map<String, String>  additionalProperties = new HashMap<>();
        additionalProperties.put(Constants.UID, asset.getUid());
        additionalProperties.put(Constants.TYPE, asset.getType());
        additionalProperties.put(Constants.LASTMODIFIED, asset.getLastModified());
        additionalProperties.put(Constants.LOCATION, asset.getLocation());

        if (asset.getReference() != null ) {
            try {
    			String references = new ObjectMapper().writeValueAsString(asset.getReference());
    	        additionalProperties.put(Constants.REFERENCE, references);
    		} catch (JsonProcessingException e) {
    			// log to execution context
    		}
        }
        return additionalProperties;
	}
	
	/**
	 * Find reference to asset with GUID.
	 * @param asset owner of the reference.
	 * @param guid of the referenced asset to search.
	 * @return referencing asset or null.
	 */
	public static AssetReference getAssetReferenceByGuid(AnalyticsAsset asset, String guid) {
		if (asset.getReference() == null) {
			return null;
		}
		return asset.getReference().stream().filter(r->guid.equals(r.getGuid())).findFirst().orElse(null);
	}
}

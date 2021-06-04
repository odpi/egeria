/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */


package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AssetReference;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.QualifiedNameUtils;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

/**
 *	Class implements mapping algorithm for external assets elements (containers and items)
 *	and GUIDs assigned to objects stored in the repository. 
 *
 */
public class IdentifierResolver {
	
	private static String CLIENT           = org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants.ANALYTICS_MODELING_OMAS_NAME;
	public static String NAME_SEPARATOR    = ".";
	
	private ExecutionContext ctx;
	
	AnalyticsAsset asset;

	Set<String> identifiersToResolve;

	// referenced identifier -> GUID
	Map<String, String> identifierToGuid = new HashMap<>();


	/**
	 * Constructor with initialization of metadata referenced in asset.
	 * 
	 * @param ctx execution context to access repository.
	 * @param asset which identifiers to resolve.
	 */
	public IdentifierResolver(ExecutionContext ctx, AnalyticsAsset asset) {

		this.ctx = ctx;
		this.asset = asset;
		
		if (!required()) {
			return;
		}
		
		identifiersToResolve = new HashSet<>();
		buildSetOfMetadataSources();
		resolveAssetReferences();
	}
	
	/**
	 * Add GUID for identifier that need to be resolved.
	 * @param guid to add.
	 * @param id of the identifier.
	 * @return true if GUID was added for known identifier.
	 */
	public boolean addGuidForIdentifier(String guid, String id) {
		if (identifiersToResolve != null && identifiersToResolve.remove(id)) {
			identifierToGuid.put(id, guid);
			return true;
		}
		return false;
	}
	

	/**
	 * Resolve GUIDs for source referenced by identifier.
	 * @param ref of the source.
	 */
	private void resolveSourcesGuids(AssetReference ref) {
		
		String methodName = "resolveSourcesGuids";
		String alias = ref.getAlias() + NAME_SEPARATOR;
		// number of metadata objects referenced from the referenced asset
		long nRefs = identifiersToResolve.stream().filter(id->id.startsWith(alias)).count();
		
		if (nRefs == 0) {
			// no metadata is referenced from this referenced asset.
			return;
		}
		
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(
				ctx.getServerSoftwareCapability().getQualifiedName(), IdMap.INFOTMATION_VIEW_TYPE_NAME, ref.getUid());
		
		List<EntityDetail> metadata = getSchemaAttributes(qualifiedName, methodName);
		
		for (EntityDetail entity : metadata) {
			String qName = ctx.getRepositoryHelper().getStringProperty(CLIENT, 
					OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, entity.getProperties(), methodName);
			
			String id = getIdFromQName(qName, ref.getAlias());
			
			if (addGuidForIdentifier(entity.getGUID(), id)) {
				if (--nRefs == 0) {
					break;	// all references from this import done
				}
			}
		}
	}


	/**
	 * Get compound identifier from qualified name.
	 * @param qName source of identifier.
	 * @param alias optional.
	 * @return identifier.
	 */
	public static String getIdFromQName(String qName, String alias) {
		List<String> ids = QualifiedNameUtils.extractIdentifiersFromQualifiedName(qName);
		if (alias != null) {
			ids.add(0, alias);
		}
		return String.join(NAME_SEPARATOR,  ids);
	}
	
	/** 
	 * Select all referenced objects in the asset.
	 */
	private void buildSetOfMetadataSources() {
		addMetadataSources(asset.getContainer());
		addMetadataSources(asset.getItem());
		addMetadataSources(asset.getVisualization());
	}


	/**
	 * Select identifiers to resolve.
	 * @param lstMetadata identifier storage.
	 */
	private void addMetadataSources(List<? extends AnalyticsMetadata> lstMetadata) {
		
		if (lstMetadata == null) {
			return;
		}
		
		for (AnalyticsMetadata mtdObject : lstMetadata) {
			if (mtdObject.getSourceId() != null) {
				mtdObject.getSourceId().forEach(src->{
					identifiersToResolve.add(src);
				});
			}
			
			if (mtdObject instanceof MetadataContainer) {
				addMetadataSources(((MetadataContainer) mtdObject).getContainer());
				addMetadataSources(((MetadataContainer) mtdObject).getItem());
			} else if (mtdObject instanceof MetadataItem) {
				addMetadataSources(((MetadataItem) mtdObject).getItem());
			}
		}
	}

	/**
	 * Function augments references to external artifacts with repository GUIDs of corresponding assets.
	 */
	private void resolveAssetReferences() {

		String methodName = "resolveAliases";
		
		if (asset.getReference() == null) {
			return;	// base module does not have references: all GUIDs resolved.
		}

		try {
			// all assets are children of software server capability
			for(AssetReference ref : asset.getReference()) {
				
				String qualifiedName = QualifiedNameUtils.buildQualifiedName(
						ctx.getServerSoftwareCapability().getQualifiedName(), IdMap.INFOTMATION_VIEW_TYPE_NAME, ref.getUid());
				
				List<EntityDetail> refAsset = ctx.getServerSoftwareCapabilityHandler().getEntitiesByValue(
						ctx.getUserId(),
						qualifiedName,
						"qualifiedName",
						IdMap.INFOTMATION_VIEW_TYPE_GUID,
						IdMap.INFOTMATION_VIEW_TYPE_NAME,
						Arrays.asList(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME), true,
						null, null, 0, 0, methodName);
				
				if (refAsset == null || refAsset.isEmpty()) {
					// missing referenced asset: maybe fine if the reference is not used, but log warning
				} else if (refAsset.size() > 1) {
					// qualified name collision: corrupted repository data
				} else {
					ref.setGuid(refAsset.get(0).getGUID());
					resolveSourcesGuids(ref);
				}
			}
		} catch (InvalidParameterException | UserNotAuthorizedException | PropertyServerException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Fetch all schema elements of the asset by asset qualified name.
	 * 
	 * @param assetQualifiedName of the asset.
	 * @param methodName for logging.
	 * @return list of elements
	 */
	public List<EntityDetail> getSchemaAttributes(String assetQualifiedName, String methodName) {
		
		List<EntityDetail> metadata = new ArrayList<>();
		String pattern = ctx.getRepositoryHelper().getStartsWithRegex(assetQualifiedName + "::");

		try {
			List<EntityDetail> metadataPage;
			while (
				 (metadataPage = ctx.getServerSoftwareCapabilityHandler().getEntitiesByValue(
						ctx.getUserId(),
						pattern,
						"pattern",
						IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID,
						IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
						Arrays.asList(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
						null, null, metadata.size(), 0, methodName)) != null) 
			{
				metadata.addAll(metadataPage);
			}
		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
			e.printStackTrace();
		}
		return metadata;
	}

	/**
	 * Check when asset identifiers need to be resolved.
	 * @return if asset references something then identifiers need to be resolved.
	 */
	public boolean required() {
		return asset.getReference() != null && !asset.getReference().isEmpty();
	}

	/**
	 * Get GUIDs of the identifiers listed as sources of metadata.
	 * @param item referencing metadata
	 * @return list of GUIDs.
	 */
	public List<String> getItemGUIDs(AnalyticsMetadata item) {
		List<String>  list = item.getSourceId();
		return list == null || list.isEmpty()
				? Collections.emptyList()
				: list.stream().map(id->identifierToGuid.get(id)).collect(Collectors.toList());
	}
}

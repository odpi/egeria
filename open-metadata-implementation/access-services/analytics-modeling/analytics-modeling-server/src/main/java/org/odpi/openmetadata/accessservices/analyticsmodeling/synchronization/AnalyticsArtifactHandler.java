/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaAttribute;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders.AnalyticsMetadataBuilder;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.AnalyticsMetadataConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAssetUtils;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AssetReference;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.QualifiedNameUtils;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeBuilder;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SoftwareServerCapability;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class AnalyticsArtifactHandler {

	private AssetHandler<AnalyticsAsset> assetHandler;
	private SchemaTypeHandler<SchemaType> schemaTypeHandler;
	private SchemaAttributeHandler<SchemaAttribute, SchemaType> metadataHandler;
	private AnalyticsMetadataConverter analyticsMetadataConverter;	
	
	
	private ExecutionContext ctx;
	
	private IdentifierResolver resolver;
	private Map<String, String> newItem;		// map identifier to GUID for new items during update operation
	private List<String> invalidAliases = new ArrayList<>();	// referenced asset aliases removed or used for other asset during update.

	public AnalyticsArtifactHandler(ExecutionContext ctx) {
		
		this.ctx = ctx;

		assetHandler = new AssetHandler<>(new AssetConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
				AnalyticsAsset.class, ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
		
		schemaTypeHandler = new SchemaTypeHandler<>(
				new SchemaTypeConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
				SchemaType.class, ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
		
		analyticsMetadataConverter = new AnalyticsMetadataConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
		
		metadataHandler = new SchemaAttributeHandler<>(
				analyticsMetadataConverter,
                SchemaAttribute.class,
                new SchemaTypeConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
                SchemaType.class,
                ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
	}
	
	public AssetHandler<AnalyticsAsset> getAssetHandler() {
		return assetHandler;
	}

	/**
	 * Create assets defined by input.
	 * @param user making the request.
	 * @param serverCapability where the artifact is located.
	 * @param input definition of analytic artifact.
	 * @return set of asset GUIDs representing the artifact.
	 * @throws AnalyticsModelingCheckedException in case of error.
	 */
	public ResponseContainerAssets createAssets(String user, String serverCapability, String input)
			throws AnalyticsModelingCheckedException
	{
		String methodName = "createAssets";
		ctx.initializeSoftwareServerCapability(user, serverCapability);
		
		ObjectMapper mapper = new ObjectMapper();
		List<String> guids = new ArrayList<>();
		
		try {
			AnalyticsAsset asset = mapper.readValue(input, AnalyticsAsset.class);
			
			if (AnalyticsAssetUtils.hasMetadataModule(asset) || !AnalyticsAssetUtils.isVisualization(asset)) {
				guids.add(createModuleAsset(asset));
			}
			
			if (AnalyticsAssetUtils.isVisualization(asset)) {
				guids.add(createVisualizationAsset(asset));
			}
			
		} catch (JsonProcessingException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.INCORRECT_ARTIFACT_DEFINITION.getMessageDefinition(input),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_CREATE_ARTIFACT.getMessageDefinition(),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		}
		
		ResponseContainerAssets ret = new ResponseContainerAssets();
		ret.setAssetsList(guids);
		return ret;
	}
	
	/**
	 * Create an asset with metadata model.
	 * 
	 * @param asset definition.
	 * @return GUID of the created Asset
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private String createModuleAsset(AnalyticsAsset asset) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
	{
		resolver = new IdentifierResolver(ctx, asset);
		
		// create asset
		String assetGUID = createAssetEntity(asset, true);
		String qualifiedName = asset.getQualifiedName();

		// create relationships for referenced assets
		createAssetReferences(asset, assetGUID);
		
		String schemaTypeGUID = createSchemaType(asset, assetGUID);	

		// create containers
		if (asset.getContainer() != null) {
			verifyOrder(asset.getContainer());
			for (MetadataContainer container : asset.getContainer()) {
				createContainer(container, schemaTypeGUID, assetGUID, false, qualifiedName);
			}
		}

		// create items
		createItems(asset.getItem(), assetGUID, qualifiedName, schemaTypeGUID, false); 
		
		return assetGUID;
	}

	/**
	 * Create relationships of the asset using references to existing assets.
	 * @param asset
	 * @param assetGUID
	 * @throws PropertyServerException 
	 * @throws UserNotAuthorizedException 
	 */
	private void createAssetReferences(AnalyticsAsset asset, String assetGUID) 
			throws UserNotAuthorizedException, PropertyServerException 
	{
		String methodName = "createAssetReferences";
		List<AssetReference> refAssets = asset.getReference();
		
		if (refAssets == null) {
			return;
		}
	
		for (AssetReference ref : refAssets) {
			if (ref.getGuid() != null) {
				ctx.getRepositoryHandler().createRelationship(ctx.getUserId(), IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, 
						null, null, assetGUID, ref.getGuid(), null, methodName);
			} else {
				// unresolved reference
			}
		}
	}

	/**
	 * Create SchemaType entity element of the asset.
	 * 
	 * @param asset parent of the created element.
	 * @param assetGUID
	 * @return GUID of the created entity.
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private String createSchemaType(AnalyticsAsset asset, String assetGUID)
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "createSchemaType";
		SoftwareServerCapability ssc = ctx.getServerSoftwareCapability();
		SchemaTypeBuilder builder = new SchemaTypeBuilder(
				QualifiedNameUtils.buildQualifiedName(asset.getQualifiedName(), IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME, asset.getType()),
				IdMap.COMPLEX_SCHEMA_TYPE_TYPE_GUID, IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
                ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
		
		builder.setAnchors(ctx.getUserId(), assetGUID, methodName);
		
		String schemaTypeGUID = schemaTypeHandler.addSchemaType(ctx.getUserId(), ssc.getGUID(), ssc.getSource(), builder, methodName);
		
		assetHandler.attachSchemaTypeToAsset(ctx.getUserId(), ssc.getGUID(), ssc.getSource(), assetGUID, "assetGUID", 
				schemaTypeGUID, "schemaTypeGUID", methodName);
		return schemaTypeGUID;
	}

	/**
	 * Create Asset entity.
	 * @param asset object whose entity to create.
	 * @param bModuleAsset true for module asset and false for deployed report.
	 * @return GUID of the created entity.
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private String createAssetEntity(AnalyticsAsset asset, boolean bModuleAsset)
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "createAsset";
		SoftwareServerCapability ssc = ctx.getServerSoftwareCapability();
		String assetTypeName = bModuleAsset ? IdMap.INFOTMATION_VIEW_TYPE_NAME : IdMap.DEPLOYED_REPORT_TYPE_NAME;
		String assetTypeGuid = bModuleAsset ? IdMap.INFOTMATION_VIEW_TYPE_GUID : IdMap.DEPLOYED_REPORT_TYPE_GUID;
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(ssc.getQualifiedName(), assetTypeName, asset.getUid());
		

        asset.setQualifiedName(qualifiedName);
		String assetGUID = assetHandler.createAssetInRepository(ctx.getUserId(),
				ssc.getGUID(), ssc.getSource(),
				qualifiedName, asset.getDisplayName(),
				asset.getDescription(),
				ctx.getSupportedZones(), // zoneMembership,
				null,	// owner
				0,		// ownerType (0 = OWNS),
				null,	//originOrganizationCapabilityGUID,
				null,	//originBusinessCapabilityGUID,
				null,	//otherOriginValues,
				AnalyticsAssetUtils.buildAdditionalProperties(asset),	//additionalProperties, 
				assetTypeGuid, assetTypeName,
				null,	//extended properties
				methodName);
		
		ctx.getRepositoryHandler().createRelationship(ctx.getUserId(),
				IdMap.SERVER_ASSET_USE_TYPE_GUID, null, null, ssc.getGUID(), assetGUID, null, methodName);
		
		return assetGUID;
	}
	

	/**
	 * Create DeployedReport asset for visualization.
	 * 
	 * @param report specification.
	 * @return created asset GUID.
	 * 
	 * @throws UserNotAuthorizedException 
	 * @throws PropertyServerException 
	 * @throws InvalidParameterException 
	 * 
	 * Note: use resolver from InformationView asset.
	 */
	private String createVisualizationAsset(AnalyticsAsset report) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		if (resolver == null || !AnalyticsAssetUtils.hasMetadataModule(report)) {
			resolver = new IdentifierResolver(ctx, report);
		}
		
		// create asset
		String assetGUID = createAssetEntity(report, false);
		String qualifiedName = report.getQualifiedName();

		createAssetReferences(report, assetGUID);
		

		// create SchemaType
		String schemaTypeGUID = createSchemaType(report, assetGUID);

		// create containers
		if (report.getVisualization() != null) {
			verifyOrder(report.getVisualization());
			for (MetadataContainer container : report.getVisualization()) {
				createContainer(container, schemaTypeGUID, assetGUID, false, qualifiedName);
			}
		}

		return assetGUID;
	}
	/**
	 * Update DeployedReport asset for visualization.
	 * 
	 * @param report specification.
	 * @return created asset GUID.
	 * 
	 * @throws UserNotAuthorizedException 
	 * @throws PropertyServerException 
	 * @throws InvalidParameterException 
	 * 
	 * Note: use resolver from InformationView asset.
	 * @throws AnalyticsModelingCheckedException 
	 */
	private String updateVisualizationAsset(AnalyticsAsset report) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AnalyticsModelingCheckedException 
	{
		String methodName = "updateModuleAsset";

		if (resolver == null || !AnalyticsAssetUtils.hasMetadataModule(report)) {
			resolver = new IdentifierResolver(ctx, report);
		}
		
		// update asset attributes
		updateAssetAttributes(report, false);

		// get all entities created for containers and items of the asset
		Map<String, EntityDetail> assetEntities = new HashMap<>();
		resolver.getSchemaAttributes(report.getQualifiedName(), methodName).forEach(
				entity->assetEntities.put(ctx.getStringProperty(Constants.QUALIFIED_NAME, entity.getProperties(), methodName), entity)
		);
		

		String schemaTypeGUID = ctx.getRepositoryHandler().getEntityForRelationshipType(ctx.getUserId(), report.getGuid(), IdMap.DEPLOYED_REPORT_TYPE_NAME,
				IdMap.ASSET_TO_SCHEMA_TYPE_TYPE_GUID, IdMap.ASSET_TO_SCHEMA_TYPE_TYPE_NAME, methodName).getGUID();

		// create containers
		if (report.getVisualization() != null) {
			verifyOrder(report.getVisualization());
			for (MetadataContainer container : report.getVisualization()) {
				updateContainer(container, schemaTypeGUID, report.getGuid(), false, report.getQualifiedName(), assetEntities);
			}
		}
		
		// remove objects left in repository and missing in new definition.
		for(EntityDetail entity : assetEntities.values()) {
			removeMetadataObject(entity, methodName);
		}

		return report.getGuid();
	}


	/**
	 * Set positions of the element within collection.
	 * @param attributes collection.
	 */
	private void verifyOrder(List<? extends SchemaAttribute> attributes) {
		
		if (attributes.stream().filter(att->att.getElementPosition() != 0).count() != attributes.size()) {
			// not all positions are set within the collection
			for (int i = 0; i < attributes.size(); ++i) {
				attributes.get(i).setElementPosition(i+1);
			}
		}
	}

	/**
	 * Create container entity to store container bean.
	 * 
	 * @param container bean to create.
	 * @param parentGUID to use as parent entity.
	 * @param anchorGUID to create anchor classification.
	 * @param bNested to create nested container.
	 * @param parentQName of the created container.
	 * @return created container GUID.
	 * @throws InvalidParameterException repository access error.
	 * @throws PropertyServerException repository access error.
	 * @throws UserNotAuthorizedException repository access error.
	 */
	public String createContainer(MetadataContainer container, String parentGUID, String anchorGUID, boolean bNested, String parentQName) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "createContainer";
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(parentQName, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, container.getIdentifier());
		container.setQualifiedName(qualifiedName);

		String guid = bNested 
				? metadataHandler.createNestedSchemaAttribute(ctx.getUserId(), null, null,
						parentGUID, Constants.PARAM_NAME_PARENT_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
						IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
		                qualifiedName, "qualifiedName",
		                createAnalyticsMetadataBuilder(container, anchorGUID, true), methodName)
				: metadataHandler.createBeanInRepository(ctx.getUserId(), null, null,
						IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, null, null,
						createAnalyticsMetadataBuilder(container, anchorGUID, true), methodName);
		
		if (!bNested) {
			// global calculation connects to the schema
			assetHandler.linkElementToElement(ctx.getUserId(), null, null,
					parentGUID, Constants.PARAM_NAME_PARENT_GUID, IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
					guid, "guid", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_GUID,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_NAME, null, methodName);
		}
		
		if (newItem != null) {
			newItem.put(IdentifierResolver.getIdFromQName(qualifiedName, null), guid);
		}

		// create nested containers
		if (container.getContainer() != null) {
			verifyOrder(container.getContainer());
			for (MetadataContainer subContainer: container.getContainer()) {
				createContainer(subContainer, guid, anchorGUID, true, qualifiedName);
			}
		}

		// create items
		createItems(container.getItem(), anchorGUID, qualifiedName, guid, true);


		return guid;
	}

	/**
	 * Update container.
	 * 
	 * @param container to update.
	 * @param parentGUID
	 * @param anchorGUID
	 * @param assetEntities 
	 * @return
	 * @throws InvalidParameterException repository access error.
	 * @throws PropertyServerException repository access error.
	 * @throws UserNotAuthorizedException repository access error.
	 */
	private String updateContainer(MetadataContainer container, String parentGUID, String anchorGUID,
			boolean bNested, String parentQName, Map<String, EntityDetail> assetEntities) 
					throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "updateContainer";
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(parentQName, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, container.getIdentifier());
		container.setQualifiedName(qualifiedName);
		
		EntityDetail entity = assetEntities.remove(qualifiedName);
		
		if (entity == null) {
			// new container
			return createContainer(container, parentGUID, anchorGUID, bNested, parentQName);
		}
		
		AnalyticsMetadata containerOld = analyticsMetadataConverter.getNewBean(entity, methodName);
		container.prepareAnalyticsMetadataProperties();
		
		if (!container.equals(containerOld)) {
			metadataHandler.updateSchemaAttribute(ctx.getUserId(), null, null, entity.getGUID(),
					createAnalyticsMetadataBuilder(container, null, false).getInstanceProperties(methodName));
		}
		
		// update nested containers
		if (container.getContainer() != null) {
			verifyOrder(container.getContainer());
			for (MetadataContainer subContainer: container.getContainer()) {
				updateContainer(subContainer, entity.getGUID(), anchorGUID, true, qualifiedName, assetEntities);
			}
		}

		// update items
		updateItems(container.getItem(), entity.getGUID(), anchorGUID, true, qualifiedName, assetEntities);

		return entity.getGUID();
	}
	
	/**
	 * Create item entity to store item bean.
	 * 
	 * @param item bean to create.
	 * @param parentGUID to use as parent.
	 * @param anchorGUID to create classification.
	 * @param bNested true for child of container or item, not asset.
	 * @param parentQName to build QName of the item.
	 * @throws InvalidParameterException repository access error.
	 * @throws PropertyServerException repository access error.
	 * @throws UserNotAuthorizedException repository access error.
	 */
	public void createItem(MetadataItem item, String parentGUID, String anchorGUID, boolean bNested, String parentQName) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "createItem";
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(parentQName, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, item.getIdentifier());
		item.setQualifiedName(qualifiedName);

		String guid = bNested 
				? metadataHandler.createNestedSchemaAttribute(ctx.getUserId(), null, null,
						parentGUID, Constants.PARAM_NAME_PARENT_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
						IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
		                qualifiedName, "qualifiedName",
		                createAnalyticsMetadataBuilder(item, anchorGUID, true), methodName)
				: metadataHandler.createBeanInRepository(ctx.getUserId(), null, null,
						IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, null, null,
						createAnalyticsMetadataBuilder(item, anchorGUID, true), methodName);

		item.setGuid(guid);

		if (!bNested) {
			// top level items connects to the schema
			assetHandler.linkElementToElement(ctx.getUserId(), null, null,
					parentGUID, Constants.PARAM_NAME_PARENT_GUID, IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
					guid, "guid", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_GUID,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_NAME, null, methodName);
		}

		if (newItem != null) {
			newItem.put(IdentifierResolver.getIdFromQName(item.getQualifiedName(), null), guid);
		}

		
		// create nested items
		createItems(item.getItem(), anchorGUID, qualifiedName, guid, true);
		
		// collect GUIDs for internal identifiers
		resolver.addGuidForIdentifier(guid, IdentifierResolver.getIdFromQName(qualifiedName, null));
	}

	/**
	 * Create items from the list.
	 * 
	 * @param items list.
	 * @param anchorGUID GUID of anchor asset
	 * @param parentQName qualified name of the parent.
	 * @param parentGUID GUID of the parent.
	 * @param bNested true if items are children of container or item, not asset. 
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private void createItems(List<MetadataItem> items, String anchorGUID, String parentQName, String parentGUID, boolean bNested) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		if (items == null) {
			return;
		}
		
		List<MetadataItem> guidToItem = new ArrayList<>();
		for (MetadataItem item : items) {
			createItem(item, parentGUID, anchorGUID, bNested, parentQName);
			guidToItem.add(item);
		}

		guidToItem.forEach(this::createMetadataLink);
	}

	/**
	 * Update metadata item.
	 * 
	 * @param item to update.
	 * @param parentGUID to attach item entity.
	 * @param anchorGUID to create anchor classification for new item entity.
	 * @param bNested true if items are children of container or item, not asset. 
	 * @param parentQName to build qualified name of the item.
	 * @return true if metadata references must be updated, otherwise false.
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private boolean updateItem(MetadataItem item, String parentGUID, String anchorGUID, boolean bNested, String parentQName, Map<String, EntityDetail> assetEntities) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "updateItem";
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(parentQName, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, item.getIdentifier());
		item.setQualifiedName(qualifiedName);

		EntityDetail entity = assetEntities.remove(qualifiedName);
		
		if (entity == null) {
			// new item
			createItem(item, parentGUID, anchorGUID, bNested, parentQName);
			return true;
		}
		
		AnalyticsMetadata itemOld = analyticsMetadataConverter.getNewBean(entity, methodName);
		item.prepareAnalyticsMetadataProperties();
		item.setGuid(entity.getGUID());	// after update the item has entity

		if (!item.equals(itemOld)) {
			metadataHandler.updateSchemaAttribute(ctx.getUserId(), null, null, entity.getGUID(),
					createAnalyticsMetadataBuilder(item, null, false).getInstanceProperties(methodName));
		}

		// update nested items
		updateItems(item.getItem(), entity.getGUID(), anchorGUID, true, qualifiedName, assetEntities);
		
		// collect GUIDs for internal identifiers
		resolver.addGuidForIdentifier(entity.getGUID(), IdentifierResolver.getIdFromQName(qualifiedName, null));
		
		if (!Objects.equals(item.getSourceId(), itemOld.getSourceId()) || !Objects.equals(item.getSourceGuid(), itemOld.getSourceGuid())) {
			return true;
		}
		
		// check if invalid metadata reference is used due to alias is modified
		if (item.getSourceId() != null && invalidAliases != null) {
			for(String alias : invalidAliases) {
				for(String metadata : item.getSourceId()) {
					if(metadata.startsWith(alias)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Update repository for list of items which can reference items from the list. 
	 * @param items to update
	 * @param parentGUID GUID of the parent entity.
	 * @param anchorGUID GUID of asset.
	 * @param bNested for nested in container or item.
	 * @param parentQName parent qualified name.
	 * @param assetEntities from repository mapped by QNames
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private void updateItems(List<MetadataItem> items, String parentGUID, String anchorGUID, 
			boolean bNested, String parentQName, Map<String, EntityDetail> assetEntities) 
					throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
	{
		if (items == null) {
			return;
		}
		verifyOrder(items);
		List<MetadataItem> guidToItem = new ArrayList<>();
		for (MetadataItem item : items) {
			if (updateItem(item, parentGUID, anchorGUID, bNested, parentQName, assetEntities)) {
				guidToItem.add(item);
			}
		}

		guidToItem.forEach(this::updateMetadataLink);
	}
	
	/**
	 * Create metadata links for the item.
	 * 
	 * @param item whose links to create.
	 */
	private void createMetadataLink(MetadataItem item) {

		String methodName = "createMetadataLink";
		List<String> metadata = resolver.required() ? resolver.getItemGUIDs(item)
				: item.getSourceGuid();
		
		if (metadata == null || metadata.isEmpty()) {
			// expression that does not references any metadata: constants, current time functions, etc.
			return;
		}
		
		for (int i = 0; i < metadata.size(); ) {
			String srcGUID = metadata.get(0);
			try {
				ctx.getRepositoryHandler().createRelationship(
						ctx.getUserId(), 
						IdMap.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
						null, null, item.getGuid(), srcGUID, null, methodName);
				metadata.remove(0);	// relationship replaced the GUID
			} catch (UserNotAuthorizedException | PropertyServerException e) {
				// log warning in execution context
				++i;	// leave GUID for relationship which was not created
			}
			
		}
	}

	/**
	 * Update metadata links of the item.
	 * @param item whose links to update.
	 * Note: item may reference something from its container so
	 * the links update must be performed after all items updated.
	 */
	private void updateMetadataLink(MetadataItem item) {

		String methodName = "updateMetadataLink";
		List<String> metadata = resolver.required() ? resolver.getItemGUIDs(item)
				: item.getSourceGuid();
		
		Map<String, Relationship> itemReferences = new HashMap<>();
		
		try {
			List<Relationship> list = ctx.getRepositoryHandler().getRelationshipsByType(
					ctx.getUserId(), item.getGuid(), IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					IdMap.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID, 
					IdMap.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME, methodName);
			
			if (list != null) {
				// exclude references pointing to this item
				list.forEach(relationship->{
					if (relationship.getEntityOneProxy().getGUID().equals(item.getGuid())) {
						itemReferences.put(relationship.getEntityTwoProxy().getGUID(), relationship);
					}
				});
			}
		} catch (UserNotAuthorizedException | PropertyServerException e1) {
			// log warning in execution context: relationships for item are not fetched from repository.
		}
		
		metadata.forEach(srcGUID->{
			
			Relationship relationship = itemReferences.remove(srcGUID);
			if (relationship == null) {	// new reference to the metadata object
				try {
					ctx.getRepositoryHandler().createRelationship(
							ctx.getUserId(), 
							IdMap.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
							null, null, item.getGuid(), srcGUID, null, methodName);
				} catch (UserNotAuthorizedException | PropertyServerException e) {
					// log warning in execution context: relationship for item is not created
				}
			}
		});
		
		// remove old references not used in the new definition
		itemReferences.values().forEach(relationship -> {
			try {
				ctx.getRepositoryHandler().removeRelationship(ctx.getUserId(), null, null, relationship, methodName);
			} catch (UserNotAuthorizedException | PropertyServerException e) {
				// log warning in execution context: old relationship for item is not removed
			}
		});
	}


	/**
	 * Helper function to create AnalyticsMetadataBuilder for AnalyticMetadata item.
	 * @param src item to build.
	 * @param assetGUID to use as anchor.
	 * @param bCreate true if nested SchemaBuilder is required.
	 * @return requested builder.
	 * @throws InvalidParameterException
	 */
	private AnalyticsMetadataBuilder createAnalyticsMetadataBuilder(AnalyticsMetadata src, String assetGUID, boolean bCreate) 
			throws InvalidParameterException, PropertyServerException 
	{
		String methodName = "createAnalyticsMetadataBuilder";

		src.prepareAnalyticsMetadataProperties();

        AnalyticsMetadataBuilder builder = new AnalyticsMetadataBuilder(src, null, ctx);
		
        if (assetGUID != null) {
    		builder.setAnchors(ctx.getUserId(), assetGUID, methodName);
        }

        if (bCreate) {
            SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(src.getQualifiedName(),
            		IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
                    ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
    		
    		builder.setSchemaType(ctx.getUserId(), schemaTypeBuilder, methodName);
        }
		
		return builder;	
	}
	
	/**
	 * Update assets defined by input.
	 * @param user making the request.
	 * @param serverCapability where the artifact is located.
	 * @param asset analytic artifact.
	 * @return set of asset GUIDs representing the artifact.
	 * @throws AnalyticsModelingCheckedException in case of error.
	 */
	public ResponseContainerAssets updateAssets(String user, String serverCapability, AnalyticsAsset asset)
			throws AnalyticsModelingCheckedException
	{
		String methodName = "updateAssets";
		ctx.initializeSoftwareServerCapability(user, serverCapability);
		
		List<String> guids = new ArrayList<>();
		
		newItem = new HashMap<>();
		
		try {
			
			if (AnalyticsAssetUtils.hasMetadataModule(asset)) {
				String guid = updateModuleAsset(asset);
				guids.add(guid);
				
				updateDependentAssets(guid);
				
			} else if (!AnalyticsAssetUtils.isVisualization(asset)) {
				// update empty module: no metadata definitions maybe all were removed
				guids.add(updateModuleAsset(asset));
			}
			
			if (AnalyticsAssetUtils.isVisualization(asset)) {
				guids.add(updateVisualizationAsset(asset));
			}
			
		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_UPDATE_ARTIFACT.getMessageDefinition(user, asset.getQualifiedName(), ex.getLocalizedMessage()),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		}
		
		ResponseContainerAssets ret = new ResponseContainerAssets();
		ret.setAssetsList(guids);
		return ret;
	}
	
	/**
	 * Update assets that import asset with the guid.
	 * @param guid of imported asset.
	 */
	private void updateDependentAssets(String guid) {

		String methodName = "updateDependentAssets";
		
		if (newItem.isEmpty()) {
			return;	// nothing to update
		}

		AnalyticsMetadataConverter converter = new AnalyticsMetadataConverter(ctx.getRepositoryHelper(), ctx.getServerName(), ctx.getServiceName());
		
		try {
			// select references pointing to asset GUID
			List<Relationship> refAssets = ctx.getRepositoryHandler().getRelationshipsByType(
					ctx.getUserId(),
					guid,
					IdMap.ASSET_TYPE_NAME,
					IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
					IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
					methodName);
			
			// select assets GUIDs referencing the asset with the GUID
			List<String> filter = refAssets.stream().map(r->r.getEntityOneProxy().getGUID()).filter(g->!guid.equals(g)).collect(Collectors.toList());
		
			List<EntityDetail> dependants = metadataHandler.getAttachedEntities(ctx.getUserId(), guid, "guid", IdMap.ASSET_TYPE_NAME, 
					IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME, 
					IdMap.ASSET_TYPE_NAME, 0, 0, methodName);
			
			for(EntityDetail entity : dependants) {
				if (!filter.contains(entity.getGUID())) {
					continue;
				}
				AnalyticsAsset assetRepo = assetHandler.getBeanFromEntity(ctx.getUserId(), entity, "entity", methodName);
				Optional<AssetReference> reference = assetRepo.getReference().stream().filter(ref->guid.equals(ref.getGuid())).findFirst();
				
				if (reference.isPresent()) {
					String alias = reference.get().getAlias() + IdentifierResolver.NAME_SEPARATOR;
					Map<String, String> uid2guid = newItem.entrySet().stream().collect(Collectors.toMap(e->alias + e.getKey(), Entry::getValue));

					List<EntityDetail> items = resolver.getSchemaAttributes(assetRepo.getQualifiedName(), methodName);
					for (EntityDetail item : items) {
						updateDependentItemRelationship(item, converter, uid2guid);
					}
				} else {
					// log reference property is not found
				}
			}
		} catch (InvalidParameterException | PropertyServerException | UserNotAuthorizedException e) {
			// log to execution context
		}
		
	}

	/**
	 * Update items in asset referencing the initially updated asset.
	 * @param item entity to update if needed.
	 * @param converter to get item entity properties.
	 * @param uid2guid new items in initial asset that be referenced by the updated item.
	 * @throws PropertyServerException
	 */
	private void updateDependentItemRelationship(EntityDetail item, AnalyticsMetadataConverter converter, Map<String, String> uid2guid)
			throws PropertyServerException 
	{
		String methodName = "updateDependentItemRelationship";
		AnalyticsMetadata metadata = converter.getNewBean(item, methodName);
		
		if (!(metadata instanceof MetadataItem)) {
			return;	// only for items so far
		}
		
		List<String> sources = metadata.getSourceId();
		if (sources != null) {
			sources.forEach(uid->{
				String guidReferenced = uid2guid.get(uid);
				if (guidReferenced != null) {
					// create relationship
					try {
						ctx.getRepositoryHandler().createRelationship(
								ctx.getUserId(), 
								IdMap.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
								null, null, item.getGUID(), guidReferenced, null, methodName);
					} catch (UserNotAuthorizedException | PropertyServerException e) {
						// log warning in execution context: relationship for item is not created
					}
				}
			});
		}
	}

	/**
	 * Update an asset from metadata model.
	 * 
	 * @param asset definition.
	 * @return GUID of the updated Asset
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 * @throws AnalyticsModelingCheckedException 
	 */
	private String updateModuleAsset(AnalyticsAsset asset) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AnalyticsModelingCheckedException
	{
		String methodName = "updateModuleAsset";
		
		resolver = new IdentifierResolver(ctx, asset);
		
		// update asset attributes
		updateAssetAttributes(asset, true);

		// get all entities created for containers and items of the asset
		Map<String, EntityDetail> assetEntities = new HashMap<>();
		resolver.getSchemaAttributes(asset.getQualifiedName(), methodName).forEach(
				entity->assetEntities.put(ctx.getStringProperty(Constants.QUALIFIED_NAME, entity.getProperties(), methodName), entity)
		);
		

		String assetGUID = asset.getGuid();	
		String schemaTypeGUID = ctx.getRepositoryHandler().getEntityForRelationshipType(ctx.getUserId(), assetGUID, IdMap.INFOTMATION_VIEW_TYPE_NAME,
				IdMap.ASSET_TO_SCHEMA_TYPE_TYPE_GUID, IdMap.ASSET_TO_SCHEMA_TYPE_TYPE_NAME, methodName).getGUID();

		// update containers
		if (asset.getContainer() != null) {
			verifyOrder(asset.getContainer());
			for (MetadataContainer container : asset.getContainer()) {
				updateContainer(container, schemaTypeGUID, assetGUID, false, asset.getQualifiedName(), assetEntities);
			}
		}

		// update items
		updateItems(asset.getItem(), schemaTypeGUID, assetGUID, false, asset.getQualifiedName(), assetEntities);

		// remove objects left in repository and missing in new definition.
		for(EntityDetail entity : assetEntities.values()) {
			removeMetadataObject(entity, methodName);
		}
		
		updateNewItemsReferences(asset);
		
		return assetGUID;
	}

	/**
	 * Update asset metadata links to new items.
	 * The asset has metadata reference to local missing item.
	 * The links to the missing items is created for all new items. 
	 * @param asset to update.
	 */
	private void updateNewItemsReferences(AnalyticsAsset asset) {

		if (newItem.isEmpty()) {
			return;	// nothing to update
		}
		
		updateNewItemsReferences(asset.getContainer());
		updateNewItemsReferences(asset.getItem());
		updateNewItemsReferences(asset.getVisualization());
		
	}
	
	/**
	 * Update metadata references to created local items.
	 * @param lstMetadata identifier storage.
	 */
	private void updateNewItemsReferences(List<? extends AnalyticsMetadata> lstMetadata) {
		
		if (lstMetadata == null) {
			return;
		}
		
		for (AnalyticsMetadata mtdObject : lstMetadata) {
			if (mtdObject instanceof MetadataItem && mtdObject.getSourceId() != null) {
				mtdObject.getSourceId().forEach(src->{
					String guid = newItem.get(src);
					if(guid != null) {
						updateMetadataLink((MetadataItem)mtdObject);
					}
				});
			}
			
			if (mtdObject instanceof MetadataContainer) {
				updateNewItemsReferences(((MetadataContainer) mtdObject).getContainer());
				updateNewItemsReferences(((MetadataContainer) mtdObject).getItem());
			} else if (mtdObject instanceof MetadataItem) {
				updateNewItemsReferences(((MetadataItem) mtdObject).getItem());
			}
		}
	}

	/**
	 * Remove metadata entity left after update is completed.
	 * @param entity to remove
	 * @param methodName requested removal.
	 * @throws InvalidParameterException repository access error.
	 * @throws UserNotAuthorizedException repository access error.
	 * @throws PropertyServerException repository access error.
	 */
	public void removeMetadataObject(EntityDetail entity, String methodName)
			throws InvalidParameterException, UserNotAuthorizedException, PropertyServerException 
	{
		ctx.getRepositoryHandler().removeEntity(ctx.getUserId(), null, null, 
				entity.getGUID(), "entityGuid", 
				IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
				null, null, methodName);
	}
	
	/**
	 * Update asset entity attributes.
	 * @param asset object whose entity to update.
	 * @param bModuleAsset true for module asset and false for deployed report.
	 * @return true if updated, false nothing to update.
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 * @throws AnalyticsModelingCheckedException 
	 */
	private boolean updateAssetAttributes(AnalyticsAsset asset, boolean bModuleAsset)
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AnalyticsModelingCheckedException 
	{
		String methodName = "updateAssetAttributes";
		SoftwareServerCapability ssc = ctx.getServerSoftwareCapability();
		String assetTypeName = bModuleAsset ? IdMap.INFOTMATION_VIEW_TYPE_NAME : IdMap.DEPLOYED_REPORT_TYPE_NAME;
		String assetTypeGuid = bModuleAsset ? IdMap.INFOTMATION_VIEW_TYPE_GUID : IdMap.DEPLOYED_REPORT_TYPE_GUID;
		asset.setQualifiedName(QualifiedNameUtils.buildQualifiedName(ssc.getQualifiedName(), assetTypeName, asset.getUid()));

		AnalyticsAsset assetRepo = assetHandler.getBeanByQualifiedName(
				ctx.getUserId(), assetTypeGuid, assetTypeName, asset.getQualifiedName(), Constants.QUALIFIED_NAME, methodName);

		asset.setGuid(assetRepo.getGuid());	// asset is new definition of the assetRepo
		
		// update relationships for referenced assets
		updateAssetReferences(asset, assetRepo);
		
		
		if (!asset.equals(assetRepo)) {
			assetHandler.updateAsset(ctx.getUserId(), ssc.getGUID(), ssc.getSource(),
					asset.getGuid(), "assetGUID", 
					asset.getQualifiedName(), asset.getDisplayName(), asset.getDescription(), 
					AnalyticsAssetUtils.buildAdditionalProperties(asset), assetTypeGuid, assetTypeName, 
					null, methodName);
			return true;
		}
		// nothing changed
		return false;
	}
	
	/**
	 * Update relationships of the asset merging existing and new asset references.
	 * @param asset updated definition.
	 * @param assetRepo repository version of the asset.
	 * @throws PropertyServerException 
	 * @throws UserNotAuthorizedException 
	 */
	private void updateAssetReferences(AnalyticsAsset asset, AnalyticsAsset assetRepo) 
			throws UserNotAuthorizedException, PropertyServerException 
	{
		String methodName = "updateAssetReferences";
		List<AssetReference> refAssets = asset.getReference();
		
		// select references starting from asset GUID
		Map<String, Relationship> mapReferences = selectReferencedAssets(asset);
		
		if (refAssets != null) {
			for (AssetReference ref : refAssets) {
				String guid = ref.getGuid();
				if (guid != null) {
					String alias = ref.getAlias();
					if (alias == null || alias.isEmpty()) {
						// asset reference cannot be used in the module without alias: skip it.
						continue;
					}
					
					Relationship assetRef = mapReferences.remove(guid);
					if (assetRef == null) {
						// this is new reference
						ctx.getRepositoryHandler().createRelationship(ctx.getUserId(), IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, 
								null, null, asset.getGuid(), guid, null, methodName);
					}
					// DATA_CONTENT_FOR_DATA_SET_TYPE_GUID has no property
					// all reference properties updated as asset additional properties
					
				} else {
					// log unresolved reference
				}
			}
		}

		// delete the removed asset references from repository
		for (Relationship rel : mapReferences.values()) {
			AssetReference reference = AnalyticsAssetUtils.getAssetReferenceByGuid(assetRepo, rel.getEntityTwoProxy().getGUID());
			if (reference != null) {
				// old references to this alias are invalid
				invalidAliases.add(reference.getAlias() + IdentifierResolver.NAME_SEPARATOR);
			}
			ctx.getRepositoryHandler().removeRelationship(ctx.getUserId(), null, null, rel, methodName);
		}
	}

	/**
	 * Build map referenced GUIDs to relationship of the asset reference.
	 * @param asset whose references are selected.
	 * @return map referenced GUID to relationship.
	 * @throws UserNotAuthorizedException
	 * @throws PropertyServerException
	 */
	private Map<String, Relationship> selectReferencedAssets(AnalyticsAsset asset) 
			throws UserNotAuthorizedException, PropertyServerException 
	{
		List<Relationship> refAssetsOld = ctx.getRepositoryHandler().getRelationshipsByType(
				ctx.getUserId(),
				asset.getGuid(),
				IdMap.ASSET_TYPE_NAME,
				IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID,
				IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_NAME,
				"selectReferencedAssets");
		
		Map<String, Relationship> mapReferences = new HashMap<>();
		
		if (refAssetsOld != null) {	// asset may not have references
			refAssetsOld.forEach(	// select only assets referenced by this asset
					r->{
						if (r.getEntityOneProxy().getGUID().equals(asset.getGuid()) ) {
							mapReferences.put(r.getEntityTwoProxy().getGUID(), r);
						}
					}
				);
		}
		return mapReferences;
	}

}

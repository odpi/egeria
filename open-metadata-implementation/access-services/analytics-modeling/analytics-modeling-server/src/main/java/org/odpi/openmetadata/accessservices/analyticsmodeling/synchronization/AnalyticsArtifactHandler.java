/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.Asset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaAttribute;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders.AnalyticsMetadataBuilder;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.AnalyticsMetadataConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
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

public class AnalyticsArtifactHandler {

	public static final String ID_LIST_DELIMITER = "\t";
	private AssetHandler<Asset> assetHandler;
	private SchemaTypeHandler<SchemaType> schemaTypeHandler;
	private SchemaAttributeHandler<SchemaAttribute, SchemaType> metadataHandler;
	
	private ExecutionContext ctx;
	
	private IdentifierResolver resolver;

	public AnalyticsArtifactHandler(ExecutionContext ctx) {
		
		this.ctx = ctx;

		assetHandler = new AssetHandler<>(new AssetConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
				Asset.class, ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
		
		schemaTypeHandler = new SchemaTypeHandler<>(
				new SchemaTypeConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
				SchemaType.class, ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
		
		metadataHandler = new SchemaAttributeHandler<>(
				new AnalyticsMetadataConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
                SchemaAttribute.class,
                new SchemaTypeConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
                SchemaType.class,
                ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
	}

	/**
	 * Create Asset.
	 * 
	 * @param asset to create.
	 * @return GUID of the created Asset
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	public String createAsset(AnalyticsAsset asset) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException
	{
		String methodName = "createAsset";
		SoftwareServerCapability ssc = ctx.getServerSoftwareCapability();
		
		resolver = new IdentifierResolver(ctx, asset);
		
		Map<String, String>  additionalProperties = new HashMap<>();
        additionalProperties.put(Constants.TYPE, asset.getType());

		// create asset
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(ssc.getQualifiedName(), IdMap.INFOTMATION_VIEW_TYPE_NAME, asset.getUid());
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
				additionalProperties,	//additionalProperties, 
				IdMap.INFOTMATION_VIEW_TYPE_GUID, IdMap.INFOTMATION_VIEW_TYPE_NAME,
				null,	//extended properties
				methodName);
		
		
		
		ctx.getRepositoryHandler().createRelationship(ctx.getUserId(),
				IdMap.SERVER_ASSET_USE_TYPE_GUID, null, null, ssc.getGUID(), assetGUID, null, methodName);

		// create relationships for referenced assets
		List<AssetReference> refAssets = asset.getReference();
		
		if (refAssets != null) {
			refAssets.forEach(ref->{
				try {
					if (ref.getGuid() == null) {
						// unresolved referenced base module
					} else {
						ctx.getRepositoryHandler().createRelationship(ctx.getUserId(), IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, 
								null, null, assetGUID, ref.getGuid(), null, methodName);
					}
				} catch (UserNotAuthorizedException | PropertyServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		

		// create SchemaType
		SchemaTypeBuilder builder = new SchemaTypeBuilder(
				QualifiedNameUtils.buildQualifiedName(qualifiedName, IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME, asset.getType()),
				IdMap.COMPLEX_SCHEMA_TYPE_TYPE_GUID,	IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
                ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
		
		builder.setAnchors(ctx.getUserId(), assetGUID, methodName);
		
		String schemaTypeGUID = schemaTypeHandler.addSchemaType(ctx.getUserId(), ssc.getGUID(), ssc.getSource(), builder, methodName);
		
		assetHandler.attachSchemaTypeToAsset(ctx.getUserId(), ssc.getGUID(), ssc.getSource(), assetGUID, "assetGUID", 
				schemaTypeGUID, "schemaTypeGUID", methodName);	

		// create containers
		if (asset.getContainer() != null) {
			verifyOrder(asset.getContainer());
			for (MetadataContainer container : asset.getContainer()) {
				createContainer(container, schemaTypeGUID, assetGUID, false, qualifiedName);
			}
		}

		// create items
		if (asset.getItem() != null) {
			verifyOrder(asset.getItem());
			Map<String, MetadataItem> guidToItem = new HashMap<>();
			for (MetadataItem item : asset.getItem()) {
				String itemGUID = createItem(item, schemaTypeGUID, assetGUID, false, qualifiedName);
				guidToItem.put(itemGUID, item);
			}

			// after all siblings created all referenced internal identifiers are resolved.
			guidToItem.forEach((k,v)->{
				createMetadataLink(v, k);
			});
		}

		
		return assetGUID;
	}
	

	/**
	 * Create asset for visualization.
	 * 
	 * DeployedReport asset created for visualization.
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
	public String createVisualizationAsset(AnalyticsAsset report) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{

		String methodName = "createVisualizationAsset";
		SoftwareServerCapability ssc = ctx.getServerSoftwareCapability();
		
		if (resolver == null) {
			resolver = new IdentifierResolver(ctx, report);
		}
		
		Map<String, String>  additionalProperties = new HashMap<>();
        additionalProperties.put(Constants.TYPE, report.getType());

        // create asset
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(ssc.getQualifiedName(), IdMap.DEPLOYED_REPORT_TYPE_NAME, report.getUid());
		report.setQualifiedName(qualifiedName);
		String assetGUID = assetHandler.createAssetInRepository(ctx.getUserId(),
				ssc.getGUID(), ssc.getSource(),
				qualifiedName, report.getDisplayName(),
				report.getDescription(),
				ctx.getSupportedZones(), // zoneMembership,
				null,	// owner
				0,		// ownerType (0 = OWNS),
				null,	//originOrganizationCapabilityGUID,
				null,	//originBusinessCapabilityGUID,
				null,	//otherOriginValues,
				additionalProperties,	//additionalProperties, 
				IdMap.DEPLOYED_REPORT_TYPE_GUID, IdMap.DEPLOYED_REPORT_TYPE_NAME,
				null,	//extended properties
				methodName);
		
		
		
		ctx.getRepositoryHandler().createRelationship(ctx.getUserId(),
				IdMap.SERVER_ASSET_USE_TYPE_GUID, null, null, ssc.getGUID(), assetGUID, null, methodName);

		// create relationships for referenced assets
		List<AssetReference> refAssets = report.getReference();
		
		if (refAssets != null) {
			refAssets.forEach(ref->{
				try {
					if (ref.getGuid() == null) {
						// unresolved referenced base module
					} else {
						ctx.getRepositoryHandler().createRelationship(ctx.getUserId(), IdMap.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, 
								null, null, assetGUID, ref.getGuid(), null, methodName);
					}
				} catch (UserNotAuthorizedException | PropertyServerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
		}
		

		// create SchemaType
		SchemaTypeBuilder builder = new SchemaTypeBuilder(
				QualifiedNameUtils.buildQualifiedName(qualifiedName, IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME, report.getType()),
				IdMap.COMPLEX_SCHEMA_TYPE_TYPE_GUID,	IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
                ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
		
		builder.setAnchors(ctx.getUserId(), assetGUID, methodName);
		
		String schemaTypeGUID = schemaTypeHandler.addSchemaType(ctx.getUserId(), ssc.getGUID(), ssc.getSource(), builder, methodName);
		
		assetHandler.attachSchemaTypeToAsset(ctx.getUserId(), ssc.getGUID(), ssc.getSource(), assetGUID, "assetGUID", 
				schemaTypeGUID, "schemaTypeGUID", methodName);	

		// create containers
		if (report.getVisualization() != null) {
			verifyOrder(report.getVisualization());
			for (MetadataContainer container : report.getVisualization()) {
				createContainer(container, schemaTypeGUID, assetGUID, false, qualifiedName);
			}
		}

		return assetGUID;
	}


	// set positions of the element within collection
	private void verifyOrder(List<? extends SchemaAttribute> attributes) {
		
		if (attributes.stream().filter(att->att.getElementPosition() != 0).count() != attributes.size()) {
			// not all positions are set within the collection
			for (int i = 0; i < attributes.size(); ++i) {
				attributes.get(i).setElementPosition(i+1);
			}
		}
	}

	/**
	 * Create container.
	 * 
	 * @param container to create.
	 * @param parentGUID
	 * @param anchorGUID
	 * @return
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private String createContainer(MetadataContainer container, String parentGUID, String anchorGUID, boolean bNested, String parentQName) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "createContainer";
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(parentQName, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, container.getIdentifier());
		container.setQualifiedName(qualifiedName);

		String guid = bNested 
				? metadataHandler.createNestedSchemaAttribute(ctx.getUserId(), null, null,
						parentGUID, "parentGUID", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
						IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
		                qualifiedName, "qualifiedName",
		                createContainerBuilder(container, anchorGUID), methodName)
				: metadataHandler.createBeanInRepository(ctx.getUserId(), null, null,
						IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, null, null,
						createContainerBuilder(container, anchorGUID), methodName);
		
		if (!bNested) {
			// global calculation connects to the schema
			assetHandler.linkElementToElement(ctx.getUserId(), null, null,
					parentGUID, "parentGUID", IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
					guid, "guid", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_GUID,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_NAME, null, methodName);
		}
		
		// create nested containers
		if (container.getContainer() != null) {
			verifyOrder(container.getContainer());
			for (MetadataContainer subContainer: container.getContainer()) {
				createContainer(subContainer, guid, anchorGUID, true, qualifiedName);
			}
		}

		// create items
		if (container.getItem() != null) {
			verifyOrder(container.getItem());
			Map<String, MetadataItem> guidToItem = new HashMap<>();
			for (MetadataItem item : container.getItem()) {
				String itemGUID = createItem(item, guid, anchorGUID, true, qualifiedName);
				guidToItem.put(itemGUID, item);
			}

			// all referenced internal identifiers are resolved after all siblings created 
			guidToItem.forEach((k,v)->{
				createMetadataLink(v, k);
			});
		}

		return guid;
	}


	/**
	 * Create item.
	 * 
	 * @param item to create.
	 * @param parentGUID
	 * @param anchorGUID
	 * @param bNested
	 * @param parentQName
	 * @return
	 * @throws InvalidParameterException
	 * @throws PropertyServerException
	 * @throws UserNotAuthorizedException
	 */
	private String createItem(MetadataItem item, String parentGUID, String anchorGUID, boolean bNested, String parentQName) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String methodName = "createItem";
		String qualifiedName = QualifiedNameUtils.buildQualifiedName(parentQName, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, item.getIdentifier());
		item.setQualifiedName(qualifiedName);

		String guid = bNested 
				? metadataHandler.createNestedSchemaAttribute(ctx.getUserId(), null, null,
						parentGUID, "parentGUID", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
						IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, IdMap.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME,
		                qualifiedName, "qualifiedName",
		                createItemBuilder(item, anchorGUID), methodName)
				: metadataHandler.createBeanInRepository(ctx.getUserId(), null, null,
						IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, null, null,
						createItemBuilder(item, anchorGUID), methodName);

		if (!bNested) {
			// top level items connects to the schema
			assetHandler.linkElementToElement(ctx.getUserId(), null, null,
					parentGUID, "parentGUID", IdMap.COMPLEX_SCHEMA_TYPE_TYPE_NAME,
					guid, "guid", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_GUID,
					IdMap.SCHEMATYPE_TO_SCHEMAATTRIBUTE_NAME, null, methodName);
		}
		
		// create nested items
		if (item.getItem() != null) {
			Map<String, MetadataItem> guidToItem = new HashMap<>();
			for (MetadataItem nestedItem : item.getItem()) {
				String itemGUID = createItem(nestedItem, guid, anchorGUID, true, qualifiedName);
				guidToItem.put(itemGUID, nestedItem);
			}

			guidToItem.forEach((k,v)->{
				createMetadataLink(v, k);
			});
		}
		
		// collect GUIDs for internal identifiers
		resolver.addGuidForIdentifier(guid, IdentifierResolver.getIdFromQName(qualifiedName, null));
		
		return guid;
	}

	
	private void createMetadataLink(MetadataItem item, String guid) {

		String methodName = "createMetadataLink";
		List<String> metadata = resolver.required() ? resolver.getItemGUIDs(item)
				: item.getSourceGuid();
		
		metadata.forEach(srcGUID->{
			try {
				ctx.getRepositoryHandler().createRelationship(
						ctx.getUserId(), 
						IdMap.SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID,
						null, null, guid, srcGUID, null, methodName);
			} catch (UserNotAuthorizedException | PropertyServerException e) {
				// log warning in execution context
			}
		});
	}

	private AnalyticsMetadataBuilder createItemBuilder(MetadataItem src, String assetGUID) throws InvalidParameterException 
	{
		String methodName = "createItemBuilder";
		Map<String, String>  additionalProperties = new HashMap<>();
        Map<String, Object>  extendedProperties = null;
	
        if (src.getSourceId() != null) {
            additionalProperties.put(IdMap.SOURCE_ID, String.join(ID_LIST_DELIMITER, src.getSourceId()));
        }

        additionalProperties.put(Constants.TYPE, src.getType());

        AnalyticsMetadataBuilder builder = new AnalyticsMetadataBuilder(
        		src,
				src.getQualifiedName(),
                src.getDisplayName(),
                src.getDescription(),
                src.getElementPosition(),
                additionalProperties,
                extendedProperties,
                ctx.getRepositoryHelper(),
                ctx.getServiceName(),
                ctx.getServerName());
		
        if (assetGUID != null) {
    		builder.setAnchors(ctx.getUserId(), assetGUID, methodName);
        }

        SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(src.getQualifiedName(),
        		IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
                ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
		
		builder.setSchemaType(ctx.getUserId(), schemaTypeBuilder, methodName);
		
		return builder;	
	}

	AnalyticsMetadataBuilder createContainerBuilder(MetadataContainer src, String assetGUID) throws InvalidParameterException {

		String methodName = "createContainerBuilder";
		
		Map<String, String>  additionalProperties = new HashMap<>();
        Map<String, Object>  extendedProperties = null;

        if (src.getSourceId() != null) {
            additionalProperties.put(IdMap.SOURCE_ID, String.join(ID_LIST_DELIMITER, src.getSourceId()));
        }

        additionalProperties.put(Constants.TYPE, src.getType());
        
        AnalyticsMetadataBuilder builder = new AnalyticsMetadataBuilder(
				src,
				src.getQualifiedName(),
                src.getDisplayName(),
                src.getDescription(),
                src.getElementPosition(),
                additionalProperties,
                extendedProperties,
                ctx.getRepositoryHelper(),
                ctx.getServiceName(),
                ctx.getServerName());
		
        if (assetGUID != null) {
        	builder.setAnchors(ctx.getUserId(), assetGUID, methodName);
        }

        SchemaTypeBuilder schemaTypeBuilder = new SchemaTypeBuilder(src.getQualifiedName(),
        		IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
                ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());
		
		builder.setSchemaType(ctx.getUserId(), schemaTypeBuilder, methodName);
		

		return builder;
	}

	public void deleteAsset(String uid) {
	}
	
	public void updateAsset(Asset asset) {

	}



}

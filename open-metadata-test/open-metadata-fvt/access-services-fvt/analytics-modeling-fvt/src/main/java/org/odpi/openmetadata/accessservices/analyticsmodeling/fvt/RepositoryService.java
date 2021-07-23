/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.fvt;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.repositoryservices.clients.LocalRepositoryServicesClient;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotDeletedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;

/**
 * This class aims to offer support for the FVT in regards to calling the LocalRepositoryServicesClient.
 * It is used to build Database object in the repository to test import functionality.
 */
public class RepositoryService {


    private static final String ODBC_TYPE = "odbc_type";
    public static final String TYPE_SUFFIX = "_type";
	private final String context;
    
    HashMap <String, String> uid2guid = new HashMap<>();

    private final String userId;
    private final LocalRepositoryServicesClient client;
    protected OMRSRepositoryPropertiesUtilities propertyUtils = new OMRSRepositoryPropertiesUtilities();

    public RepositoryService(String serverName, String userId, String serverPlatformRootURL, String context)
            throws InvalidParameterException {
    	this.context = context;
        this.userId = userId;
        this.client = new LocalRepositoryServicesClient("repository", serverPlatformRootURL +
                "/servers/" + serverName);
    }

	public void createRequiredEntities(AnalyticsAsset artifact) 
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, 
			PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, 
			FunctionNotSupportedException, UserNotAuthorizedException 
	{
		createReferencedEntitiesForMetadataLinks(artifact.getQualifiedName(), artifact.getContainer());
	}
	
	
	private void createReferencedEntitiesForMetadataLinks(String parentQName, List<? extends AnalyticsMetadata> list)
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, UserNotAuthorizedException,
			PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, FunctionNotSupportedException
	{
		if (list == null) {
			return;
		}
		
		for (int i = 0; i < list.size(); ++i) {
			AnalyticsMetadata mtdObject = list.get(i);
			if (mtdObject instanceof MetadataItem) {
				createReferencedColumns(parentQName, i + 1, mtdObject);
				createReferencedEntitiesForMetadataLinks(mtdObject.getQualifiedName(), ((MetadataItem) mtdObject).getItem());
			} else if (mtdObject instanceof MetadataContainer) {
				createReferencedEntitiesForMetadataLinks(mtdObject.getQualifiedName(), ((MetadataContainer) mtdObject).getContainer());
				createReferencedEntitiesForMetadataLinks(mtdObject.getQualifiedName(), ((MetadataContainer) mtdObject).getItem());
			}
		}
	}

	private void createReferencedColumns(String parentQName, int position, AnalyticsMetadata mtdObject)
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException,
			ClassificationErrorException, StatusNotSupportedException, FunctionNotSupportedException, UserNotAuthorizedException
	{
		if (mtdObject.getSourceGuid() == null) {
			return;
		}
		
		for( int j = 0; j < mtdObject.getSourceGuid().size(); ++j) {
			String id = mtdObject.getSourceGuid().get(j);
			String guid;
			if (!uid2guid.containsKey(id)) {
				EntityDetail entity = createReferencedColumn(parentQName, ((MetadataItem) mtdObject).getExpression(), position);
				guid = entity.getGUID();
				uid2guid.put(id, entity.getGUID());
			} else {
				guid = uid2guid.get(id);
			}
			mtdObject.getSourceGuid().set(j, guid);
		}
	}

	protected EntityDetail createReferencedColumn(String parentQName, String columnName, int position) 
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, 
			PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, 
			FunctionNotSupportedException, UserNotAuthorizedException 
	{
    	String method = "createReferencedColumn";
    	
    	String columnQName = buildQualifiedName(parentQName,
    			OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME, columnName);
    	
        InstanceProperties properties = new EntityPropertiesBuilder(context, method, null)
                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, columnQName)
                .withStringProperty(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME, columnName)
                .withIntegerProperty(OpenMetadataAPIMapper.POSITION_PROPERTY_NAME, position)
                .build();
        
		return client.addEntity(userId, OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_GUID, 
				properties, null, InstanceStatus.ACTIVE);
	}

	/**
	 * Clean repository from previous test setup.
	 * @throws InvalidParameterException in case of the error. 
	 * @throws RepositoryErrorException in case of the error. 
	 * @throws TypeErrorException in case of the error. 
	 * @throws PropertyErrorException in case of the error. 
	 * @throws FunctionNotSupportedException in case of the error. 
	 * @throws UserNotAuthorizedException in case of the error. 
	 * @throws PagingErrorException in case of the error. 
	 * @throws EntityNotKnownException in case of the error. 
	 * @throws EntityNotDeletedException  in case of the error.
	 */
	public void cleanRepository() 
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, 
			PropertyErrorException, FunctionNotSupportedException, UserNotAuthorizedException,
			PagingErrorException, EntityNotKnownException, EntityNotDeletedException 
	{
		// order is important: follows tree hierarchy of the Relational schema structure.
		String [] types = {
			OpenMetadataAPIMapper.DATABASE_TYPE_GUID,
			OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_GUID,
			OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID,
			OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_GUID,
			OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_GUID,
			OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID
		};
		
		for (String type : types) {
			if (!deleteEntitiesByType(type))
				break;
		}
	}

	/**
	 * Delete entity of the certain type from repository.
	 * @param entityTypeGUID to be deleted.
	 * @return true if any entity was deleted.
	 * @throws InvalidParameterException in case of the error. 
	 * @throws RepositoryErrorException in case of the error. 
	 * @throws TypeErrorException in case of the error. 
	 * @throws PropertyErrorException in case of the error. 
	 * @throws FunctionNotSupportedException in case of the error. 
	 * @throws UserNotAuthorizedException in case of the error. 
	 * @throws PagingErrorException in case of the error. 
	 * @throws EntityNotKnownException in case of the error. 
	 * @throws EntityNotDeletedException in case of the error.
	 */
	private boolean deleteEntitiesByType(String entityTypeGUID) 
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, 
			PropertyErrorException, FunctionNotSupportedException, UserNotAuthorizedException, 
			PagingErrorException, EntityNotKnownException, EntityNotDeletedException 
	{
		List<EntityDetail> entities = client.findEntities(userId, entityTypeGUID, null, null, 0, null, null, null, null, null, 0);
		
		if (entities != null && !entities.isEmpty()) {
	        for (EntityDetail entity : entities) {
	        	client.deleteEntity(userId, entityTypeGUID, entity.getType().getTypeDefName(), entity.getGUID());
	        	client.purgeEntity(userId, entityTypeGUID, entity.getType().getTypeDefName(), entity.getGUID());
	        }
			return true;
		}
        return false;
	}

	public EntityDetail createDatabaseEntity(String dbName, String type, String version ) 
			throws InvalidParameterException, RepositoryErrorException, TypeErrorException, 
			PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, 
			FunctionNotSupportedException, UserNotAuthorizedException 
	{
		String qualifiedName = buildQualifiedName("", OpenMetadataAPIMapper.DATABASE_TYPE_NAME, dbName);
        InstanceProperties properties = new EntityPropertiesBuilder(context, "createDatabaseEntity", null)
                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName)
                .withStringProperty(OpenMetadataAPIMapper.NAME_PROPERTY_NAME, dbName)
                .withStringProperty(OpenMetadataAPIMapper.DATABASE_TYPE_PROPERTY_NAME_DEP, type)
                .withStringProperty(OpenMetadataAPIMapper.DATABASE_VERSION_PROPERTY_NAME_DEP, version)
                .build();
        
		return client.addEntity(userId, OpenMetadataAPIMapper.DATABASE_TYPE_GUID, properties, null, InstanceStatus.ACTIVE);
	}
	
	public EntityDetail createDatabaseSchemaEntity(String guidDB, String schemaName) 
			throws InvalidParameterException, RepositoryErrorException, FunctionNotSupportedException,
			EntityNotKnownException, EntityProxyOnlyException, UserNotAuthorizedException, TypeErrorException,
			PropertyErrorException, ClassificationErrorException, StatusNotSupportedException 
	{
		String methodName = "createDatabaseSchemaEntity";
		EntityDetail db = client.getEntityDetail(userId, guidDB);
		String dbQName = getEntityQName(db, methodName);
		String qualifiedName = buildQualifiedName(dbQName, OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_NAME, schemaName);
        InstanceProperties properties = new EntityPropertiesBuilder(context, methodName, null)
                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, qualifiedName)
                .withStringProperty(OpenMetadataAPIMapper.NAME_PROPERTY_NAME, schemaName)
                .build();
        
    	EntityDetail schema =  client.addEntity(userId, OpenMetadataAPIMapper.DEPLOYED_DATABASE_SCHEMA_TYPE_GUID,
    			properties, null, InstanceStatus.ACTIVE);
    	// add relationship catalog->schema
    	client.addRelationship(userId, OpenMetadataAPIMapper.DATA_CONTENT_FOR_DATA_SET_TYPE_GUID, 
    			null, db.getGUID(), schema.getGUID(), InstanceStatus.ACTIVE);
    	
    	return schema;
	}
	
	String getEntityQName(EntityDetail entity, String methodName) {
		return propertyUtils.getStringProperty(context, OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, entity.getProperties(), methodName);
	}
	
	public EntityDetail createSchemaTable(EntityDetail schema, String tableName)
			throws InvalidParameterException, TypeErrorException, EntityProxyOnlyException, ClassificationErrorException,
			RepositoryErrorException, EntityNotKnownException, PropertyErrorException, PagingErrorException,
			FunctionNotSupportedException, UserNotAuthorizedException, StatusNotSupportedException
	{
		
		String method = "createSchemaTable";
		List<Relationship> relationshipsRDBSchemaList = client.getRelationshipsForEntity(userId, schema.getGUID(),
				OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID, 0,
				Collections.singletonList(InstanceStatus.ACTIVE), null, null, null, 0);
		String schemaQName = getEntityQName(schema, method);
		
		if (relationshipsRDBSchemaList == null || relationshipsRDBSchemaList.isEmpty()) {
			String relationalSchemaQName = buildQualifiedName(schemaQName, OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME, tableName);
	        InstanceProperties properties = new EntityPropertiesBuilder(context, method, null)
	                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, relationalSchemaQName)
	                .build();
			
        	EntityDetail relationalDBSchemaType = client.addEntity(userId, OpenMetadataAPIMapper.RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID,
        			properties, null, null);
        	
        	// add relationship schema->RelationalDBSchemaType
        	Relationship relationship = client.addRelationship(userId, OpenMetadataAPIMapper.ASSET_TO_SCHEMA_TYPE_TYPE_GUID, null,
        			schema.getGUID(), relationalDBSchemaType.getGUID(), null);

	        relationshipsRDBSchemaList = Arrays.asList(relationship);
		}
		
		Relationship relRDBSchema = relationshipsRDBSchemaList.get(0);
		EntityDetail entityRDBSchema =  client.getEntityDetail(userId, relRDBSchema.getEntityTwoProxy().getGUID());
				
		String tableQName = buildQualifiedName(getEntityQName(entityRDBSchema, method), 
				OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_NAME, tableName);
        InstanceProperties properties = new EntityPropertiesBuilder(context, method, null)
                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, tableQName)
                .withStringProperty(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME, tableName)
                .build();
		
    	EntityDetail entityTable = client.addEntity(userId, OpenMetadataAPIMapper.RELATIONAL_TABLE_TYPE_GUID, properties, null, null);
    			
    	// add relationship RelationalDBSchemaType->table
    	client.addRelationship(userId, OpenMetadataAPIMapper.TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, 
    			null, entityRDBSchema.getGUID(), entityTable.getGUID(), null);

    	return entityTable;
	}
	
	public EntityDetail addColumn(EntityDetail tableEntity, String columnName, String dataType, String vendorType, int position)
			throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException,
			PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException, 
			ClassificationErrorException, StatusNotSupportedException, EntityProxyOnlyException
	{
    	String method = "addColumn";
    	String columnQName = buildQualifiedName(getEntityQName(tableEntity, method), OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME, columnName);
        InstanceProperties columnTypeProperties = new EntityPropertiesBuilder(context, method, null)
                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, columnQName)
                .withStringProperty(OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME, columnName)
                .withIntegerProperty(OpenMetadataAPIMapper.POSITION_PROPERTY_NAME, position)
                .build();
        EntityDetail columnEntity = client.addEntity(userId, OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_GUID, columnTypeProperties, null, null);
        
    	client.addRelationship(userId, OpenMetadataAPIMapper.NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID, 
    			null, tableEntity.getGUID(), columnEntity.getGUID(), null);

    	if (dataType != null) {
    		setColumnNoteLogProperty(columnEntity, ODBC_TYPE, dataType);
    	}
    	
    	if (vendorType != null) {
    		setColumnNoteLogProperty(columnEntity, OpenMetadataAPIMapper.COMMENT_TYPE_PROPERTY_NAME_DEP, vendorType);
     	}
   	
        return columnEntity;
	}
	
    public void setColumnNoteLogProperty(EntityDetail columnEntity, String propName, String propValue)
    		throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException,
    		PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException,
    		EntityProxyOnlyException, ClassificationErrorException, StatusNotSupportedException
    {
    	String methodName = "setColumnNoteLogProperty";
		EntityDetail columnTypeEntity = getColumnType(columnEntity);
		InstanceProperties ap = propertyUtils.getMapProperty(context, OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME,
				columnTypeEntity.getProperties(), methodName);
				
		if (ap == null) {
			ap = new InstanceProperties();
		}
		
		EntityPropertiesBuilder pb = new EntityPropertiesBuilder(context, methodName, ap);
		pb.withStringProperty(propName, propValue);
		MapPropertyValue mpv = new MapPropertyValue();
		mpv.setMapValues(ap);
		InstanceProperties properties = columnTypeEntity.getProperties();
		properties.setProperty(OpenMetadataAPIMapper.ADDITIONAL_PROPERTIES_PROPERTY_NAME, mpv);
		client.updateEntityProperties(userId, columnTypeEntity.getGUID(), properties);
	}
    
	private EntityDetail getColumnType(EntityDetail columnEntity)
			throws InvalidParameterException, TypeErrorException, RepositoryErrorException, EntityNotKnownException,
			PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException, 
			EntityProxyOnlyException, ClassificationErrorException, StatusNotSupportedException
	{
		
		String method = "getColumnType";
		List<Relationship> columnToColumnType = client.getRelationshipsForEntity(userId, columnEntity.getGUID(),
				OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID, 0,
				Collections.singletonList(InstanceStatus.ACTIVE), null, null, null, 0);
		
		if (columnToColumnType != null && !columnToColumnType.isEmpty()) {
			return client.getEntityDetail(userId, columnToColumnType.get(0).getEntityTwoProxy().getGUID());
		}
		
		// create
		String columnName = propertyUtils.getStringProperty(context, OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME,
				columnEntity.getProperties(), method);
		
        String columnTypeQName = buildQualifiedName(getEntityQName(columnEntity, method),
        		OpenMetadataAPIMapper.RELATIONAL_COLUMN_TYPE_NAME, columnName + TYPE_SUFFIX );
        InstanceProperties columnTypeProperties = new EntityPropertiesBuilder(context, "getColumnType", null)
                .withStringProperty(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME, columnTypeQName)
                .build();
        
        EntityDetail columnNoteLogEntity = client.addEntity(userId, OpenMetadataAPIMapper.NOTE_LOG_TYPE_GUID, columnTypeProperties,
        		null, null);
        
    	client.addRelationship(userId, OpenMetadataAPIMapper.REFERENCEABLE_TO_NOTE_LOG_TYPE_GUID,
    			null, columnEntity.getGUID(), columnNoteLogEntity.getGUID(), null);
    	
    	return columnNoteLogEntity;
	}
	
	private static String buildQualifiedName(String parentQualifiedName, String typeName, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append((parentQualifiedName == null || parentQualifiedName.isEmpty()) ? "" : (parentQualifiedName + "::"))
		.append("(")
		.append(typeName)
		.append(")")
		.append("=")
		.append(value);
		
		return sb.toString();
	}

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test;

import static org.testng.Assert.fail;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.contentmanager.OMEntityDaoForTests;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.QualifiedNameUtils;
import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataExchangeRule;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveManager;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;

public class InMemoryRepositoryTest {

	protected static final String USER_ID = "userId";
	protected static final String LOCAL_SERVER_USER_ID = "localServerUserId";
	protected static final String serviceName = "serviceName";
	protected static final String serverName = "serverName";
	protected static final Integer PAGE_SIZE = 20;

	

    @Mock
    protected OMRSRepositoryConnector enterpriseConnector;
    protected OMRSRepositoryContentHelper omrsRepositoryHelper;
    @Mock
    protected OMRSAuditLog auditLog;
    private OMRSRepositoryContentManager localRepositoryContentManager = null;
    protected OMEntityDaoForTests omEntityDao;
  
    protected RepositoryHandler repositoryHandler;
    protected OMRSMetadataCollection metadataCollection;
 	protected InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();
   
    final private String context = "Analytics Modeling OMAS test";
    
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        omEntityDao = new OMEntityDaoForTests(enterpriseConnector, Collections.emptyList(), auditLog);
        omEntityDao.setContext("JUnitTest");
        
        OMRSRepositoryConnector repositoryConnector = initializeInMemoryRepositoryConnector();
        
        metadataCollection = repositoryConnector.getMetadataCollection();
        
		repositoryHandler = new RepositoryHandler(auditLog, 
				new RepositoryErrorHandler(omrsRepositoryHelper, serviceName, serverName, auditLog),
				metadataCollection,
	            PAGE_SIZE);
      
        when(enterpriseConnector.getMetadataCollection()).thenReturn(repositoryConnector.getMetadataCollection());
        when(enterpriseConnector.getRepositoryHelper()).thenReturn(repositoryConnector.getRepositoryHelper());

    }


    private OMRSRepositoryConnector initializeInMemoryRepositoryConnector() throws ConnectorCheckedException,
                                                                                   ConnectionCheckedException {

        Connection connection = new Connection();
        ConnectorType connectorType = new ConnectorType();
        connection.setConnectorType(connectorType);
        connectorType.setConnectorProviderClassName(InMemoryOMRSRepositoryConnectorProvider.class.getName());
        ConnectorBroker connectorBroker = new ConnectorBroker();
        Connector connector = connectorBroker.getConnector(connection);
        OMRSRepositoryConnector repositoryConnector = (OMRSRepositoryConnector) connector;

        localRepositoryContentManager = new OMRSRepositoryContentManager(USER_ID, auditLog);
        omrsRepositoryHelper = new OMRSRepositoryContentHelper(localRepositoryContentManager);


        OMRSRepositoryEventManager localRepositoryEventManager = new OMRSRepositoryEventManager("local repository outbound",
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL, null),
                new OMRSRepositoryContentValidator(localRepositoryContentManager),
                auditLog);

        LocalOMRSRepositoryConnector localOMRSRepositoryConnector = (LocalOMRSRepositoryConnector) new LocalOMRSConnectorProvider("testLocalMetadataCollectionId",
                connection,
                null,
                localRepositoryEventManager,
                localRepositoryContentManager,
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL, null))
                .getConnector(connection);


        localOMRSRepositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
        localOMRSRepositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
        localOMRSRepositoryConnector.setAuditLog(auditLog);
        localOMRSRepositoryConnector.setMetadataCollectionId("1234");
        localRepositoryContentManager.setupEventProcessor(localOMRSRepositoryConnector, localRepositoryEventManager);


        repositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
        repositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
        repositoryConnector.setMetadataCollectionId("testMetadataCollectionId");
        repositoryConnector.start();
        localRepositoryEventManager.start();
        localOMRSRepositoryConnector.start();
        new OMRSArchiveManager(null, auditLog).setLocalRepository(localRepositoryContentManager, localRepositoryEventManager);

        return localOMRSRepositoryConnector;
    }
    
	/**
	 * Get entity property of type string.
	 * @param entity whose property is requested.
	 * @param name if the requested property.
	 * @return property value.
	 */
	public String getStringProperty(EntityDetail entity, String name) {
		return omEntityDao.getEntityStringProperty(entity, name);
	}

	protected EntityDetail createDatabaseEntity(String dbName, String type, String version ) {
		String qualifiedName = QualifiedNameUtils.buildQualifiedName("", Constants.DATABASE, dbName);
        InstanceProperties properties = new EntityPropertiesBuilder(context, "createDatabaseEntity", null)
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedName)
                .withStringProperty(Constants.NAME, dbName)
                .withStringProperty(Constants.TYPE, type)
                .withStringProperty(Constants.VERSION, version)
                .build();
        
        try {
			return omEntityDao.addEntity(Constants.DATABASE, qualifiedName, properties, true);
		} catch (InvalidParameterException | PropertyErrorException | RepositoryErrorException | EntityNotKnownException
				| FunctionNotSupportedException | PagingErrorException | ClassificationErrorException
				| UserNotAuthorizedException | TypeErrorException | StatusNotSupportedException 
				| AnalyticsModelingCheckedException e) {
			fail("Failed create database entity.");
		}
		return null;
	}
	
	protected EntityDetail createDatabaseSchemaEntity(String guidDB, String schemaName) throws AnalyticsModelingCheckedException {
		EntityDetail db = omEntityDao.getEntityByGuid(guidDB);
		String qualifiedName =  QualifiedNameUtils.buildQualifiedName(
				omEntityDao.getEntityStringProperty(db, Constants.QUALIFIED_NAME),
				Constants.DEPLOYED_DATABASE_SCHEMA, schemaName);
        InstanceProperties properties = new EntityPropertiesBuilder(context, "createDatabaseSchemaEntity", null)
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedName)
                .withStringProperty(Constants.NAME, schemaName)
                .build();
        
        try {
        	EntityDetail schema =  omEntityDao.addEntity(Constants.DEPLOYED_DATABASE_SCHEMA, qualifiedName, properties, true);
        	// add relationship catalog->schema
        	omEntityDao.addRelationship(Constants.DATA_CONTENT_FOR_DATASET, guidDB, schema.getGUID(), null);
        	
        	return schema;
		} catch (InvalidParameterException | PropertyErrorException | RepositoryErrorException | EntityNotKnownException
				| FunctionNotSupportedException | PagingErrorException | ClassificationErrorException
				| UserNotAuthorizedException | TypeErrorException | StatusNotSupportedException e) {
			fail("Failed create schema entity.");
		}
		return null;

	}
	
	String getEntityQName(EntityDetail entity) {
		return omEntityDao.getEntityStringProperty(entity, Constants.QUALIFIED_NAME);
	}

	protected EntityDetail createSchemaTable(EntityDetail schema, String tableName) throws AnalyticsModelingCheckedException {
		
		String method = "createSchemaTable";
		List<Relationship> relationshipsRDBSchemaList = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, schema.getGUID());
		
		if (relationshipsRDBSchemaList == null || relationshipsRDBSchemaList.isEmpty()) {
			String qualifiedName =  QualifiedNameUtils.buildQualifiedName(
					getEntityQName(schema), 
					Constants.RELATIONAL_DB_SCHEMA_TYPE, Constants.SCHEMA_ASSET);
	        InstanceProperties properties = new EntityPropertiesBuilder(context, method, null)
	                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedName)
	                .build();
			
	        try {
	        	EntityDetail relationalDBSchemaType = omEntityDao.addEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE, qualifiedName, properties, true);
	        	// add relationship schema->RelationalDBSchemaType
	        	omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE, schema.getGUID(), relationalDBSchemaType.getGUID(), null);
			} catch (OMRSCheckedExceptionBase e) {
				fail("Failed create RelationalDBSchemaType entity.");
			}
	        relationshipsRDBSchemaList = omEntityDao.getRelationships(Constants.ASSET_SCHEMA_TYPE, schema.getGUID());

		}
		
		Relationship relRDBSchema = relationshipsRDBSchemaList.get(0);
		EntityDetail entityRDBSchema =  omEntityDao.getEntityByGuid(relRDBSchema.getEntityTwoProxy().getGUID());
				
        try {
			String qualifiedName =  QualifiedNameUtils.buildQualifiedName(
					omEntityDao.getEntityStringProperty(schema, Constants.QUALIFIED_NAME), 
					Constants.RELATIONAL_DB_SCHEMA_TYPE, tableName);
	        InstanceProperties properties = new EntityPropertiesBuilder(context, method, null)
	                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedName)
	                .withStringProperty(Constants.DISPLAY_NAME, tableName)
	                .build();
			
        	EntityDetail entityTable = omEntityDao.addEntity(Constants.RELATIONAL_TABLE, qualifiedName, properties, true);
        	// add relationship RelationalDBSchemaType->table
        	omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA, entityRDBSchema.getGUID(), entityTable.getGUID(), null);
        	return entityTable;
		} catch (OMRSCheckedExceptionBase e) {
			fail("Failed create table entity: " + tableName);
		}
		return null;
	}

    protected EntityDetail addColumn(EntityDetail tableEntity, String columnName, String dataType, String vendorType) throws Exception {

    	String method = "addColumn";
    	int position = getColumnPosition(tableEntity);
    	String columnQName = QualifiedNameUtils.buildQualifiedName(getEntityQName(tableEntity), Constants.RELATIONAL_COLUMN, columnName);
        InstanceProperties columnTypeProperties = new EntityPropertiesBuilder(context, method, null)
                .withStringProperty(Constants.QUALIFIED_NAME, columnQName)
                .withStringProperty(Constants.DISPLAY_NAME, columnName)
                .withIntegerProperty(Constants.POSITION, position)
                .build();
        EntityDetail columnEntity = omEntityDao.addEntity(Constants.RELATIONAL_COLUMN,
        		columnQName,
                columnTypeProperties,
                false);
        
    	omEntityDao.addRelationship(Constants.NESTED_SCHEMA_ATTRIBUTE, tableEntity.getGUID(), columnEntity.getGUID(), null);

    	if (dataType != null) {
    		setColumnNoteLogProperty(columnEntity, Constants.ODBC_TYPE, dataType);
    	}
    	
    	if (vendorType != null) {
    		setColumnNoteLogProperty(columnEntity, Constants.TYPE, vendorType);
     	}
   	
        return columnEntity;
    }

	public void setColumnProperty(EntityDetail columnEntity, String propName, Boolean propValue ) throws Exception {
		InstanceProperties properties = columnEntity.getProperties();
		properties = new EntityPropertiesBuilder(context, "setColumnProperty", properties)
				.withBooleanProperty(propName, propValue)
				.build();
		omEntityDao.updateEntityProperty(columnEntity, properties);
	}

	public void setColumnPrimaryKey(EntityDetail columnEntity, String primaryKeyName) throws Exception {

		InstanceProperties classificationProperties = new EntityPropertiesBuilder(context, "setColumnPrimaryKey", null)
				.withStringProperty(Constants.NAME, primaryKeyName).build();
		
		omEntityDao.classifyEntity(columnEntity, Constants.PRIMARY_KEY, classificationProperties);
	}
	
	public void defineColumnForeignKey(EntityDetail columnEntity, EntityDetail referencedColumnEntity) throws Exception {
    	omEntityDao.addRelationship(Constants.FOREIGN_KEY, referencedColumnEntity.getGUID(), columnEntity.getGUID(), null);
	}

	public void setColumnNoteLogProperty(EntityDetail columnEntity, String propName, String propValue ) throws Exception {
		
		EntityDetail columnTypeEntity = getColumnType(columnEntity);
		InstanceProperties ap = omEntityDao.getMapProperty(columnTypeEntity.getProperties(), Constants.ADDITIONAL_PROPERTIES);
		
		if (ap == null) {
			ap = new InstanceProperties();
		}
		
		EntityPropertiesBuilder pb = new EntityPropertiesBuilder(context, "setColumnNoteLogProperty", ap);
		pb.withStringProperty(propName, propValue);
		MapPropertyValue mpv = new MapPropertyValue();
		mpv.setMapValues(ap);
		InstanceProperties properties = columnTypeEntity.getProperties();
		properties.setProperty(Constants.ADDITIONAL_PROPERTIES, mpv);
		omEntityDao.updateEntityProperty(columnTypeEntity, properties);
	}


    /**
     * Calculate position of the column.
     * @param tableEntity for this table
     * @return column position in order of adding to the table.
     * @throws AnalyticsModelingCheckedException 
     */
	private int getColumnPosition(EntityDetail tableEntity) throws AnalyticsModelingCheckedException {
		List<Relationship> columnToColumnType = omEntityDao.getRelationshipsForEntity(tableEntity, Constants.NESTED_SCHEMA_ATTRIBUTE);
		return columnToColumnType == null ? 0 : columnToColumnType.size();
	}


	private EntityDetail getColumnType(EntityDetail columnEntity) throws Exception {
		
		List<Relationship> columnToColumnType = omEntityDao.getRelationshipsForEntity(columnEntity, Constants.ATTACHED_NOTE_LOG);
		
		if (columnToColumnType != null && !columnToColumnType.isEmpty()) {
			return omEntityDao.getEntityByGuid(columnToColumnType.get(0).getEntityTwoProxy().getGUID());
		}
		
		// create
		String columnName = omEntityDao.getEntityStringProperty(columnEntity, Constants.DISPLAY_NAME);
        String columnTypeQName = QualifiedNameUtils.buildQualifiedName(getEntityQName(columnEntity),
        		Constants.RELATIONAL_COLUMN_TYPE, columnName + Constants.TYPE_SUFFIX );
        InstanceProperties columnTypeProperties = new EntityPropertiesBuilder(context, "getColumnType", null)
                .withStringProperty(Constants.QUALIFIED_NAME, columnTypeQName)
                .build();
        
        EntityDetail columnNoteLogEntity = omEntityDao.addEntity(Constants.NOTE_LOG, columnTypeQName, columnTypeProperties, false);
        
    	omEntityDao.addRelationship(Constants.ATTACHED_NOTE_LOG, columnEntity.getGUID(), columnNoteLogEntity.getGUID(), null);

    	return columnNoteLogEntity;

	}
}

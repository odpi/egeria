/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.assets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.AnalyticsModelingErrorCode;
import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.Database;
import org.odpi.openmetadata.accessservices.analyticsmodeling.metadata.Schema;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.*;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.module.*;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.generichandlers.RelationalDataHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;

/**
 * The class builds data content of the Analytics Modeling OMAS responses.<br>
 * 
 * This class contains logic about elements required to build responses and their relationships.
 * Only methods used to build responses should be public.<br>
 * 
 * <br>Here is the exposed functionality:
 * <ul>
 * <li>Get databases {@link org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler#getDatabases}</li>
 * <li>Get database schemas {@link org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler#getSchemaTables}</li>
 * <li>Get schema tables {@link org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler#getSchemaTables}</li>
 * <li>Get physical module {@link org.odpi.openmetadata.accessservices.analyticsmodeling.assets.DatabaseContextHandler#getModule}</li>
 * </ul>
 * Database metadata is retrieved from OMRS using object of type
 * {@link org.odpi.openmetadata.accessservices.analyticsmodeling.contentmanager.OMEntityDao}<br>
 * All repository logic should be handled there.<br>
 */
public class DatabaseContextHandler {

	public static final String DATA_SOURCE_GUID = "dataSourceGUID";
	
	private RelationalDataHandler<Database, 
									Schema, 
									Object, 
									Object, 
									Object, 
									Object> relationalDataHandler;
	
	private OMEntityDao omEntityDao;
	private InvalidParameterHandler invalidParameterHandler;
	
	
	private static HashMap<String, String> mapTypes = new HashMap<>();
	
	static {
		mapTypes.put("WVARCHAR", "NVARCHAR");
	}

	public DatabaseContextHandler(
		RelationalDataHandler<Database, Schema, Object, Object, Object, Object> relationalDataHandler, 
		OMEntityDao omEntityDao, 
		InvalidParameterHandler invalidParameterHandler) {
		
		this.relationalDataHandler = relationalDataHandler;
		this.omEntityDao = omEntityDao;
		this.invalidParameterHandler = invalidParameterHandler;
	}
	
	/**
	 * Set context of the execution.
	 * Every public method should call the method setContext() first to set the context.
	 * @param context of the execution - top level method.
	 */
	void setContext(String context) {
		omEntityDao.setContext(context);
	}


	/**
	 * Get list of databases on the server.
	 * 
	 * @param userId for the call.
     * @param startFrom	starting element (used in paging through large result sets)
     * @param pageSize	maximum number of results to return
	 * @return list of database descriptors.
	 * @throws AnalyticsModelingCheckedException in case of an repository operation failure.
	 */
	public List<ResponseContainerDatabase> getDatabases(String userId, Integer startFrom, Integer pageSize) 
			throws AnalyticsModelingCheckedException {
		
		String methodName = "getDatabases";
		setContext(methodName);
		
		List<Database> databases = findDatabases(userId, startFrom, pageSize, methodName);
		return Optional.ofNullable(databases).map(Collection::stream).orElseGet(Stream::empty)
				.parallel()
				.map(this::buildDatabase)
				.filter(Objects::nonNull).collect(Collectors.toList());
	}

	private List<Database> findDatabases(String userId, Integer startFrom, Integer pageSize, String methodName)
			throws AnalyticsModelingCheckedException
	{
		try {
			return relationalDataHandler.getDatabases(userId, startFrom, pageSize, methodName);
		} catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException
				| PropertyServerException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_FETCH_DATABASES.getMessageDefinition(),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		}
	}
	
	private ResponseContainerDatabase buildDatabase(Database e) {
		ResponseContainerDatabase ret = new ResponseContainerDatabase();
		ret.setDbName(e.getName());
		ret.setDbType(e.getType());
		ret.setDbVersion(e.getVersion());
		ret.setGUID(e.getGuid());
		return ret;
	}


	/**
	 * Retrieve schemas for given database from repository.
	 * 
	 * @param userId for the call.
	 * @param guidDatabase defines database
     * @param startFrom	starting element (used in paging through large result sets)
     * @param pageSize	maximum number of results to return
	 * @return list of schemas attributes.
	 * @throws AnalyticsModelingCheckedException in case of an repository operation failure.
	 * @throws InvalidParameterException if passed GUID is invalid.
	 */
	public List<ResponseContainerDatabaseSchema> getDatabaseSchemas(String userId, String guidDatabase, Integer startFrom, Integer pageSize) 
			throws AnalyticsModelingCheckedException, InvalidParameterException {

		String methodName = "getDatabaseSchemas";
		setContext(methodName);
		
		invalidParameterHandler.validateGUID(guidDatabase, DATA_SOURCE_GUID, methodName);
		
		try {
			Database db = relationalDataHandler.getDatabaseByGUID(userId, guidDatabase, methodName);
			String dbName = db.getName();
			
			List<Schema> schemas = relationalDataHandler.getSchemasForDatabase(userId, guidDatabase, startFrom, pageSize, methodName);

			return Optional.ofNullable(schemas).map(Collection::stream).orElseGet(Stream::empty)
					.map(e->buildSchema(dbName, e))
					.filter(Objects::nonNull).collect(Collectors.toList());
			
		} catch (org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException | UserNotAuthorizedException
				| PropertyServerException ex) {
			throw new AnalyticsModelingCheckedException(
					AnalyticsModelingErrorCode.FAILED_FETCH_DATABASE_SCHEMAS.getMessageDefinition(guidDatabase),
					this.getClass().getSimpleName(),
					methodName,
					ex);
		}
	}

	/**
	 * Helper function to build database schema response object from the schema element.
	 * @param catalogName the schema belongs to.
	 * @param dbSchemaEntity source of the schema data.
	 * @return response element.
	 */
	private ResponseContainerDatabaseSchema buildSchema(String catalogName, Schema dbSchema) {
			String schemaName = dbSchema.getName();
			ResponseContainerDatabaseSchema schema = new ResponseContainerDatabaseSchema();
			schema.setCatalog(catalogName);
			schema.setSchema(schemaName);
			schema.setId(schema.buildId());
		return schema;
	}
	/**
	 * Get schema entity from relationship catalog->schema
	 * @param r relationship
	 * @return schema entity.
	 */
	private EntityDetail getSchemaEntityFromRelationship(Relationship r) {
		return this.getEntityByGuidNoThrow(r.getEntityTwoProxy().getGUID());
	}

	/**
	 * Get tables for schema.
	 * 
	 * @param guidDataSource of the schema.
	 * @param schema         name.
	 * @return list of table names.
	 * @throws AnalyticsModelingCheckedException if failed
	 * @throws InvalidParameterException if passed GUID is invalid.
	 */
	public ResponseContainerSchemaTables getSchemaTables(String guidDataSource, String schema) throws AnalyticsModelingCheckedException, InvalidParameterException {

		String context = "getSchemaTables";
		setContext(context);

		invalidParameterHandler.validateGUID(guidDataSource, DATA_SOURCE_GUID, context);

		ResponseContainerSchemaTables ret = new ResponseContainerSchemaTables();

		EntityDetail dbSchemaEntity = getSchemaEntityByName(guidDataSource, schema);
		List<String> tables = getTablesForSchema(dbSchemaEntity)
								.parallelStream()
								.map(t->getEntityStringProperty(t, Constants.DISPLAY_NAME))
								.filter(Objects::nonNull).collect(Collectors.toList());

		Collections.sort(tables);			
		ret.setTablesList(tables);

		return ret;
	}

	/**
	 * Get tables for the schema.
	 * @param dbSchemaEntity schema entity of the requested tables
	 * @return list of table entities that belongs to the schema.
	 * @throws AnalyticsModelingCheckedException 
	 */
	private List<EntityDetail> getTablesForSchema(EntityDetail dbSchemaEntity) throws AnalyticsModelingCheckedException {

		List<Relationship> allDbSchemaToSchemaType = omEntityDao.getRelationshipsForEntity(dbSchemaEntity, Constants.ASSET_SCHEMA_TYPE);

		if (allDbSchemaToSchemaType == null || allDbSchemaToSchemaType.isEmpty()) {
			return Collections.emptyList(); // no tables
		}
		
		Relationship dbSchemaToSchemaType = allDbSchemaToSchemaType.get(0);
		EntityDetail dbSchemaTypeEntity = omEntityDao.getEntityByGuid(dbSchemaToSchemaType.getEntityTwoProxy().getGUID());

		List<Relationship> dbSchemaTypeToTableRelationships = omEntityDao.getRelationshipsForEntity(dbSchemaTypeEntity, Constants.ATTRIBUTE_FOR_SCHEMA);

		return Optional.ofNullable(dbSchemaTypeToTableRelationships).map(Collection::stream)
				.orElseGet(Stream::empty).map(this::getTableEntity).filter(Objects::nonNull)
				.collect(Collectors.toList());

	}

	private EntityDetail getTableEntity(Relationship relationship) {
		String tableGuid = relationship.getEntityTwoProxy().getGUID();
		return getEntityByGuidNoThrow(tableGuid);
	}

	/**
	 * Build Analytics Modeling module for database schema
	 * @param databaseGuid of the module
	 * @param catalog of the module
	 * @param schema of the module
	 * @param filter contains optional table filter
	 * @return module built for defined schema
	 * @throws AnalyticsModelingCheckedException in case of an repository operation failure.
	 * @throws InvalidParameterException if passed GUID is invalid.
	 */
	public ResponseContainerModule getModule(String databaseGuid, String catalog, String schema, ModuleTableFilter filter) 
			throws AnalyticsModelingCheckedException, InvalidParameterException {

		String context = "getModule";
		invalidParameterHandler.validateGUID(databaseGuid, DATA_SOURCE_GUID, context);
		setContext(context);

		ResponseContainerModule ret = new ResponseContainerModule();
		ret.setId(catalog + "_" + schema);
		ret.setPhysicalModule(buildModule(databaseGuid, catalog, schema, filter));
		return ret;
	}

	private MetadataModule buildModule(String databaseGuid, String catalog, String schema, ModuleTableFilter filter) 
			throws AnalyticsModelingCheckedException
	{
		MetadataModule module = new MetadataModule();
		module.setIdentifier("physicalmodule");
		module.setDataSource(Arrays.asList(buildDataSource(databaseGuid, catalog, schema, filter)));
		return module;
	}

	private DataSource buildDataSource(String databaseGuid, String catalog, String schema, ModuleTableFilter filter) 
			throws AnalyticsModelingCheckedException {
		DataSource ds = new DataSource();
		ds.setCatalog(catalog);
		EntityDetail schemaEntity = getSchemaEntityByName(databaseGuid, schema);
		ds.setSchema(getEntityStringProperty(schemaEntity, Constants.ATTRIBUTE_NAME));
		ds.setName(catalog + "." + ds.getSchema());
		ds.setTable(buildTables(databaseGuid, schemaEntity, filter));
		ds.addProperty(Constants.GUID, schemaEntity.getGUID());
		return ds;
	}

	private List<Table> buildTables(String databaseGuid, EntityDetail schemaEntity, ModuleTableFilter tblFilter)
			throws AnalyticsModelingCheckedException {

		List<EntityDetail> tables = getTablesForSchema(schemaEntity);
		
		if (tblFilter != null) {
			tables = tables.stream()
					.filter(tbl->tblFilter.match(getEntityStringProperty(tbl, Constants.DISPLAY_NAME)))
					.collect(Collectors.toList());
		}

		List<Table> ret = tables
				.parallelStream()
				.map(this::buildSingleTable)
				.filter(Objects::nonNull).collect(Collectors.toList());
		
		ret.sort(Comparator.comparing(Table::getName));
		
		return ret;
	}

	private Table buildSingleTable(EntityDetail entityTable) {

		Table ret = new Table();
		ret.setName(getEntityStringProperty(entityTable, Constants.DISPLAY_NAME));
		ret.addProperty(Constants.GUID, entityTable.getGUID());

		List<Relationship> relationshipsTableColumns;
		try {
			relationshipsTableColumns = omEntityDao.getRelationshipsForEntity(entityTable,
					Constants.NESTED_SCHEMA_ATTRIBUTE);
		} catch (AnalyticsModelingCheckedException e) {
			// skip the table
			return null;
		}

		if (relationshipsTableColumns == null || relationshipsTableColumns.isEmpty()) {
			// report table without columns, don't create empty table
			return null;
		}

		List<TableItem> items = relationshipsTableColumns.stream()
				.parallel()
				.map(this::buildTableItems)
				.filter(Objects::nonNull)
				.collect(Collectors.toList());
		
		items.sort(Comparator.comparingInt(TableItem::getPosition));

		if (items.isEmpty()) {
			// report table without columns, don't create such table
			return null;
		}

		ret.setTableItem(items);

		processPrimaryKeys(ret, items);
		
		processForeignKeys(ret, items);

		return ret;
	}

	private void processPrimaryKeys(Table ret, List<TableItem> items) {
		List<TableItem> pk = items.stream().filter(v->v.getPkName() != null).collect(Collectors.toList());
		if (pk != null && !pk.isEmpty()) {
			PrimaryKey pKey = new PrimaryKey();
			pKey.setName(pk.get(0).getPkName());	// all PK columns should have same name	
			pKey.setKeyedColumn(pk.stream().map(v->v.getColumn().getName()).sorted().collect(Collectors.toList()));
			ret.setPrimaryKey(Arrays.asList(pKey));	// single primary key assumption
		}
	}

	/**
	 * Add foreign keys to the table.
	 * Several columns can be part of the single foreign key:
	 * 		CONSTRAINT FK_Name FOREIGN KEY (column1{, column2, ...}) REFERENCES ParentTable(column1{, column2, ...}) 
	 * Columns referencing a table are combined within single foreign key.
	 * @param table the foreign keys are added.
	 * @param items table columns to process.
	 */
	private void processForeignKeys(Table table, List<TableItem> items) {
		// select items referencing other columns: potential foreign keys columns
		List<TableItem> fKeys = items.stream().filter(v->v.getReferencedColumns() != null).collect(Collectors.toList());
		
		if (fKeys.isEmpty()) {
			return;	// no FK defined
		}
		
		Map<String, List<ForeignColumn>> tablesFKs = new HashMap<>();	// foreign key per table
		
		fKeys.forEach( v -> {
			List<EntityDetail> columnEntities = v.getReferencedColumns().stream()
					.map(this::getEntityByGuidNoThrow)
					.filter(Objects::nonNull)
					.collect(Collectors.toList());

			if(columnEntities.isEmpty()) {
				return;
			}
			
			columnEntities.forEach(ce->{
				ForeignColumn fkColumn = new ForeignColumn();
				fkColumn.setColumnName(v.getColumn().getName());	// column name in child table
				// add referenced column and ancestors up to catalog
				fkColumn.setPkColumn(getEntityStringProperty(ce, Constants.DISPLAY_NAME));
				try {
					// add table
					EntityDetail tableEntity;
					tableEntity = getParentEntity(ce, Constants.NESTED_SCHEMA_ATTRIBUTE);
					fkColumn.setPkTable(getEntityStringProperty(tableEntity, Constants.DISPLAY_NAME));
					
					List<ForeignColumn> theTableFKs = tablesFKs.get(tableEntity.getGUID());
					if (theTableFKs == null) {
						theTableFKs = new ArrayList<>();
						// use table GUID: same table name can be used in different schemas
						tablesFKs.put(tableEntity.getGUID(), theTableFKs);
					}
					theTableFKs.add(fkColumn);
					try {
						// add schema
						EntityDetail schemaAttributesEntity = getParentEntity(tableEntity, Constants.ATTRIBUTE_FOR_SCHEMA);
						EntityDetail schemaEntity = getParentEntity(schemaAttributesEntity, Constants.ASSET_SCHEMA_TYPE);
						fkColumn.setPkSchema(getEntityStringProperty(schemaEntity, Constants.ATTRIBUTE_NAME));
						
						try {
							EntityDetail catalogEntity = getParentEntity(schemaEntity, Constants.DATA_CONTENT_FOR_DATASET);
							
							fkColumn.setPkCatalog(getEntityStringProperty(catalogEntity, Constants.ATTRIBUTE_NAME));							
						} catch (AnalyticsModelingCheckedException exCatalog) {
							// log foreign key from unknown catalog
							return;
						}
						
					} catch (AnalyticsModelingCheckedException exTable) {
						// log foreign key from unknown schema
						return;
					}
				} catch (AnalyticsModelingCheckedException exSchema) {
					// log foreign key from unknown table
					return;
				}
				
				
				
			});
			
		});
		
		tablesFKs.forEach((key, list) -> {
			ForeignKey fk = new ForeignKey();
			// FK_ChildTable_ParentTable_PKColumn{_PKColumn}
			String parentTable = list.get(0).getPkTable();
			fk.setName("FK_" + table.getName() + "_" + parentTable + "_" 
					+ String.join("_", list.stream().map(v->v.getColumnName()).collect(Collectors.toList())));
			fk.setFkColumn(list);
			List<ForeignKey> fks = table.getForeignKey();
			if (fks == null) {
				fks = new ArrayList<>();
				table.setForeignKey(fks);
			}
			fks.add(fk);
		});
		
		// sort by names
		if (table.getForeignKey() != null && table.getForeignKey().size() > 1) {
			table.getForeignKey().sort(Comparator.comparing(e->e.getName()));
		}
	}
	
	/**
	 * Get entity by GUID without throwing.
	 * Log warning when entity is loaded as part of other object,
	 * and failing to find the entity is not critical.
	 * @param guid of the entity.
	 * @return entity or null if not found.
	 */
	EntityDetail getEntityByGuidNoThrow(String guid) {
		try {
			return omEntityDao.getEntityByGuid(guid);
		} catch (Exception ex) {
			// log entity is loaded
		}
		return null;
	}

	/**
	 * Get parent entity for the entity.
	 * @param child whose parent is requested.
	 * @param propertyName that defines relationship
	 * @return parent entity.
	 * @throws AnalyticsModelingCheckedException 
	 */
	private EntityDetail getParentEntity(EntityDetail child, String propertyName) throws AnalyticsModelingCheckedException {
		List<Relationship> columnRelationships = omEntityDao.getRelationshipsForEntity(child, propertyName);
		
		if (columnRelationships == null) {
			return null;
		}
		// relationship table->column
		Relationship columnTableRelationship = columnRelationships.stream()
				.filter(r->child.getGUID().equals(r.getEntityTwoProxy().getGUID()))
				.findFirst().get();
		
		EntityDetail tableEntity = omEntityDao.getEntityByGuid(columnTableRelationship.getEntityOneProxy().getGUID());
		return tableEntity;
	}

	private TableItem buildTableItems(Relationship tableTypeToColumns) {

		EntityDetail columnEntity;
		try {
			columnEntity = omEntityDao.getEntityByGuid(tableTypeToColumns.getEntityTwoProxy().getGUID());
		} catch (AnalyticsModelingCheckedException e) {
			// skip the item
			return null;
		}
		TableItem item = new TableItem();
		Column tableColumn = new Column();
		item.setColumn(tableColumn);

		item.setPosition(getEntityIntProperty(columnEntity, Constants.POSITION));
		tableColumn.setName(getEntityStringProperty(columnEntity, Constants.DISPLAY_NAME));
		tableColumn.addProperty(Constants.GUID, columnEntity.getGUID());

		String pkName = getPrimaryKeyClassification(columnEntity);
		if (pkName != null && !pkName.isEmpty()) {
			item.setPkName(pkName);
		}

		tableColumn.setNullable(getEntityBooleanProperty(columnEntity, Constants.IS_NULLABLE));
		
		List<String> fks = getReferencedColumn(columnEntity);
		if(fks != null && !fks.isEmpty()) {
			item.setReferencedColumns(fks);
		}

		try {
			EntityDetail columnTypeUniverse = getColumnType(columnEntity);
			InstanceProperties ap = getAdditiomalProperty(columnTypeUniverse.getProperties());
			tableColumn.setVendorType(omEntityDao.getStringProperty(Constants.TYPE, ap));

			String length = omEntityDao.getStringProperty(Constants.LENGTH, ap);
			String dt = omEntityDao.getStringProperty(Constants.ODBC_TYPE, ap);
			dt = mapDataType(dt);
			tableColumn.setDatatype(length == null ? dt : dt + "(" + length + ")");

		} catch (AnalyticsModelingCheckedException e) {
			// column is useless without data type information
			return null;
		}

		return item;
	}

	private String mapDataType(String dt) {
		String newType = mapTypes.get(dt);
		return newType == null ? dt : newType;
	}

	private String getPrimaryKeyClassification(EntityDetail columnEntity) {
		List<Classification> classifications = columnEntity.getClassifications();
		if (classifications == null || classifications.isEmpty()) {
			return null;
		}
		Classification classification = classifications.stream().filter(e -> e.getName().equals(Constants.PRIMARY_KEY))
				.findFirst().orElse(null);

		return classification == null ? null
				: omEntityDao.getStringProperty(Constants.NAME, classification.getProperties());
	}

	/**
	 * Returns the column type details for a given column
	 *
	 * @param column for which the type is retrieved
	 * @return the column type entity linked to column entity
	 * @throws AnalyticsModelingCheckedException 
	 */
	private EntityDetail getColumnType(EntityDetail column) throws AnalyticsModelingCheckedException {

		List<Relationship> columnToColumnType = omEntityDao.getRelationshipsForEntity(column, Constants.ATTACHED_NOTE_LOG);
		return omEntityDao.getEntityByGuid(columnToColumnType.get(0).getEntityTwoProxy().getGUID());
	}

	private EntityDetail getSchemaEntityByName(String databaseGuid, String schema) throws AnalyticsModelingCheckedException {

		EntityDetail dbEntity = omEntityDao.getEntityByGuid(databaseGuid);
		List<Relationship> databaseToDbSchemaRelationships = omEntityDao.getRelationshipsForEntity(dbEntity, Constants.DATA_CONTENT_FOR_DATASET);

		return Optional.ofNullable(databaseToDbSchemaRelationships).map(Collection::stream).orElseGet(Stream::empty)
				.map(r -> {
					EntityDetail dbSchemaEntity = getSchemaEntityFromRelationship(r);

					if (dbSchemaEntity != null) { // skip the schema in case of an error
						String schemaName = getEntityStringProperty(dbSchemaEntity, Constants.ATTRIBUTE_NAME);

						if (schema != null && schema.equals(schemaName)) { // only requested schema
							return dbSchemaEntity;
						}
					}
					return null;
				}).filter(Objects::nonNull).findFirst()
				.orElseThrow(()-> new AnalyticsModelingCheckedException(
						AnalyticsModelingErrorCode.SCHEMA_UNKNOWN.getMessageDefinition(schema),
						this.getClass().getSimpleName(),
						"getSchemaEntityByName"));
	}

	/**
	 * Get GUIDs of the columns referenced as foreign key
	 * @param columnEntity for the column.
	 * @return GUIDs of the referenced columns.
	 */
	private List<String> getReferencedColumn(EntityDetail columnEntity) {

		List<Relationship> columnForeignKeys;
		try {
			columnForeignKeys = omEntityDao.getRelationshipsForEntity(columnEntity, Constants.FOREIGN_KEY);
		} catch (AnalyticsModelingCheckedException e) {
			// no foreign keys
			return null;
		}

		if (columnForeignKeys == null || CollectionUtils.isEmpty(columnForeignKeys)) {
			return null;
		}
		String columnGuid = columnEntity.getGUID();
		return columnForeignKeys.stream()
			.filter(v->columnGuid.equals(v.getEntityTwoProxy().getGUID()))
			.map(v->v.getEntityOneProxy().getGUID())
			.collect(Collectors.toList());
	}

	private InstanceProperties getAdditiomalProperty(InstanceProperties properties) {
		return properties == null ? null : omEntityDao.getMapProperty(properties, Constants.ADDITIONAL_PROPERTIES);
	}

	private String getEntityStringProperty(EntityDetail entity, String name) {
		return entity == null ? null : omEntityDao.getEntityStringProperty(entity, name);
	}

	private Boolean getEntityBooleanProperty(EntityDetail entity, String name) {
		return entity == null ? null : omEntityDao.getEntityBooleanProperty(entity, name);
	}

	private int getEntityIntProperty(EntityDetail entity, String name) {
		return entity == null ? null : omEntityDao.getEntityIntProperty(entity, name);
	}


	
}

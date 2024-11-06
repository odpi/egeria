/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.database;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.JDBCResourceConnector;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.mappers.BaseMapper;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers.*;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryTable;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.sql.SQLException;
import java.util.*;

/**
 * Manages the connection between the repository connector and the database
 */
public class DatabaseStore
{
    private final JDBCResourceConnector jdbcResourceConnector;
    private final String                repositoryName;
    private final OMRSRepositoryHelper  repositoryHelper;

    private final BaseMapper baseMapper;

    /**
     * Create access to the entity store.
     *
     * @param jdbcResourceConnector connector to the database
     * @param repositoryName name of this repository
     * @param repositoryHelper helper
     */
    public DatabaseStore(JDBCResourceConnector jdbcResourceConnector,
                         String                repositoryName,
                         OMRSRepositoryHelper  repositoryHelper)
    {
        this.jdbcResourceConnector = jdbcResourceConnector;
        this.repositoryName        = repositoryName;
        this.repositoryHelper      = repositoryHelper;

        this.baseMapper = new BaseMapper(repositoryName);
    }


    /**
     * Extract the repository control table from the database schema.  This is used to validate that the
     * server is using the correct repository.
     *
     * @return contents of the control table (only 1 row expected)
     * @throws RepositoryErrorException problem connecting to the database
     */
    public ControlMapper getControlTable() throws RepositoryErrorException
    {
        final String methodName = "getControlTable";

        try
        {
            List<Map<String, JDBCDataValue>> controlTable = jdbcResourceConnector.getRows(RepositoryTable.CONTROL.getTableName(),
                                                                                          RepositoryTable.CONTROL.getColumnNameTypeMap());

            if (controlTable == null)
            {
                return null;
            }

            return new ControlMapper(repositoryName, controlTable);
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Save the control table row to the database.
     *
     * @param controlMapper control mapper filled with the desired values.
     *
     * @throws RepositoryErrorException problem connecting to the database
     */
    public void saveControlTable(ControlMapper controlMapper) throws RepositoryErrorException
    {
        final String methodName = "saveControlTable";

        try
        {
             jdbcResourceConnector.insertRowIntoTable(RepositoryTable.CONTROL.getTableName(),
                                                      controlMapper.getControlTableRow());
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Retrieve details about an entity from the store.  The entity mapper is returned to allow the caller to choose
     * what style of entity to return.
     *
     * @param guid unique identifier of the entity
     * @return entity mapper containing all that is known about the latest version of the entity
     * @throws RepositoryErrorException either a problem accessing the database or a mapping problem with the contents retrieved
     */
    public EntityMapper getEntityForUpdate(String guid) throws RepositoryErrorException
    {
        return this.getEntityFromStore(guid, null);
    }


    /**
     * Retrieve the version of an entity from the database that was active at the requested time.
     * Null is returned if there were no active instance.
     *
     * @param guid unique identifier of the entity
     * @param asOfTime requested time for the version
     * @return entity mapper
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public EntityMapper getEntityFromStore(String guid,
                                           Date   asOfTime) throws RepositoryErrorException
    {
        final String methodName = "getEntityFromStore";

        try
        {
            Map<String, JDBCDataValue> entityRow = jdbcResourceConnector.getMatchingRow(RepositoryTable.ENTITY.getTableName(),
                                                                                        RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "'" + getAsOfTimeClause(asOfTime),
                                                                                        RepositoryTable.ENTITY.getColumnNameTypeMap());

            return this.getCompleteEntityFromStore(guid, entityRow);
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Retrieve the related information for an entity.
     * Null is returned if there were no active instance.
     *
     * @param guid unique identifier of the entity
     * @param entityRow appropriate row from table
     * @return entity mapper
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public EntityMapper getCompleteEntityFromStore(String                     guid,
                                                   Map<String, JDBCDataValue> entityRow) throws RepositoryErrorException
    {
        final String methodName = "getCompleteEntityFromStore";

        try
        {
            if (entityRow != null)
            {
                Object versionObject = entityRow.get(RepositoryColumn.VERSION.getColumnName()).getDataValue();

                if (versionObject != null)
                {
                    String whereClause = RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + versionObject;

                    List<Map<String, JDBCDataValue>> entityProperties = jdbcResourceConnector.getMatchingRows(RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                                                                              whereClause,
                                                                                                              RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                    List<Map<String, JDBCDataValue>> classifications = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                                                             whereClause,
                                                                                                             RepositoryTable.CLASSIFICATION.getColumnNameTypeMap());

                    Map<String, MapperResultRows> classificationRowsMap = new HashMap<>();
                    if (classifications != null)
                    {
                        for (Map<String, JDBCDataValue> classificationRow : classifications)
                        {
                            String classificationName = baseMapper.getStringPropertyFromColumn(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), classificationRow, true);

                            MapperResultRows classificationMapperRow = new MapperResultRows();

                            classificationMapperRow.principleTableRow = classificationRow;

                            classificationRowsMap.put(classificationName, classificationMapperRow);
                        }

                        List<Map<String, JDBCDataValue>> classificationAttributes = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                          whereClause,
                                                                                                                          RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                        if (classificationAttributes != null)
                        {
                            for (Map<String, JDBCDataValue> classificationAttributeRow : classificationAttributes)
                            {
                                String classificationName = baseMapper.getStringPropertyFromColumn(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), classificationAttributeRow, true);

                                MapperResultRows mapperResultRows = classificationRowsMap.get(classificationName);

                                if (mapperResultRows.propertyTableRows == null)
                                {
                                    mapperResultRows.propertyTableRows = new ArrayList<>();
                                }

                                mapperResultRows.propertyTableRows.add(classificationAttributeRow);
                            }
                        }
                    }

                    List<ClassificationMapper> classificationMappers = null;

                    if (classifications != null)
                    {
                        classificationMappers = new ArrayList<>();

                        for (String classificationName : classificationRowsMap.keySet())
                        {
                            MapperResultRows classificationRow = classificationRowsMap.get(classificationName);

                            if (classificationRow != null)
                            {
                                ClassificationMapper classificationMapper = new ClassificationMapper(classificationRow.principleTableRow,
                                                                                                     classificationRow.propertyTableRows,
                                                                                                     repositoryHelper,
                                                                                                     repositoryName);

                                classificationMappers.add(classificationMapper);
                            }
                        }
                    }

                    return new EntityMapper(entityRow, entityProperties, classificationMappers, repositoryHelper, repositoryName);
                }
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        return null;
    }


    /**
     * Retrieve the related information for a list of entities that were identified in a query.
     * Null is returned if there were no instances returned from the query.
     *
     * @param entityGUIDs list of unique identifiers for the matching entities
     * @param asOfTime time for the database query
     * @return list of entity mappers
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    private List<EntityMapper> getCompleteEntitiesFromStore(List<String> entityGUIDs,
                                                            Date         asOfTime) throws RepositoryErrorException
    {
        final String methodName = "getCompleteEntitiesFromStore";

        if ((entityGUIDs != null) && (!entityGUIDs.isEmpty()))
        {
            try
            {
                Map<String, MapperResultRows> mapperResultRowsMap = new HashMap<>();

                QueryBuilder queryBuilder = new QueryBuilder(repositoryHelper, repositoryName);

                queryBuilder.setGUIDList(entityGUIDs);
                queryBuilder.setAsOfTime(asOfTime);

                String whereClause = queryBuilder.getAsOfTimeWhereClause();

                List<Map<String, JDBCDataValue>> entityRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.ENTITY.getTableName(),
                                                                                                    whereClause,
                                                                                                    RepositoryTable.ENTITY.getColumnNameTypeMap());

                if (entityRows != null)
                {
                    StringBuilder entityWhereClause = new StringBuilder();
                    boolean       firstEntity       = true;

                    for (Map<String, JDBCDataValue> entityRow : entityRows)
                    {
                        String instanceGUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), entityRow, true);
                        long version        = baseMapper.getLongPropertyFromColumn(RepositoryColumn.VERSION.getColumnName(), entityRow, true);

                        if (firstEntity)
                        {
                            firstEntity = false;
                        }
                        else
                        {
                            entityWhereClause.append(" or ");
                        }

                        entityWhereClause.append(queryBuilder.getPrimaryKeysClause(instanceGUID, version, null));

                        MapperResultRows mapperResultRows = new MapperResultRows();
                        mapperResultRows.principleTableRow = entityRow;

                        mapperResultRowsMap.put(instanceGUID, mapperResultRows);
                    }

                    List<Map<String, JDBCDataValue>> entityAttributeRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                 entityWhereClause.toString(),
                                                                                                                 RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                    if (entityAttributeRows != null)
                    {
                        for (Map<String, JDBCDataValue> entityAttributeRow : entityAttributeRows)
                        {
                            String instanceGUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), entityAttributeRow, true);

                            MapperResultRows mapperResultRows = mapperResultRowsMap.get(instanceGUID);

                            if (mapperResultRows.propertyTableRows == null)
                            {
                                mapperResultRows.propertyTableRows = new ArrayList<>();
                            }

                            mapperResultRows.propertyTableRows.add(entityAttributeRow);
                        }
                    }

                    List<Map<String, JDBCDataValue>> classifications = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                                                             whereClause,
                                                                                                             RepositoryTable.CLASSIFICATION.getColumnNameTypeMap());

                    if (classifications != null)
                    {
                        StringBuilder classificationWhereClause = new StringBuilder();
                        boolean       firstClassification       = true;

                        for (Map<String, JDBCDataValue> classificationRow : classifications)
                        {
                            String instanceGUID       = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), classificationRow, true);
                            String classificationName = baseMapper.getStringPropertyFromColumn(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), classificationRow, true);
                            long   version            = baseMapper.getLongPropertyFromColumn(RepositoryColumn.VERSION.getColumnName(), classificationRow, true);

                            if (firstClassification)
                            {
                                firstClassification = false;
                            }
                            else
                            {
                                classificationWhereClause.append(" or ");
                            }

                            classificationWhereClause.append(queryBuilder.getPrimaryKeysClause(instanceGUID, version, classificationName));

                            MapperResultRows mapperResultRows = mapperResultRowsMap.get(instanceGUID);

                            if (mapperResultRows.propertyTableRows == null)
                            {
                                mapperResultRows.propertyTableRows = new ArrayList<>();
                            }

                            MapperResultRows classificationMapperRow = mapperResultRows.classifications.get(classificationName);

                            if (classificationMapperRow == null)
                            {
                                classificationMapperRow = new MapperResultRows();
                            }

                            classificationMapperRow.principleTableRow = classificationRow;

                            mapperResultRows.classifications.put(classificationName, classificationMapperRow);
                        }

                        List<Map<String, JDBCDataValue>> classificationAttributes = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                          classificationWhereClause.toString(),
                                                                                                                          RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                        if (classificationAttributes != null)
                        {
                            for (Map<String, JDBCDataValue> classificationAttributeRow : classificationAttributes)
                            {
                                String instanceGUID       = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), classificationAttributeRow, true);
                                String classificationName = baseMapper.getStringPropertyFromColumn(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), classificationAttributeRow, true);

                                MapperResultRows mapperResultRows = mapperResultRowsMap.get(instanceGUID);

                                MapperResultRows classificationAttributesMapperResults = mapperResultRows.classifications.get(classificationName);

                                if (classificationAttributesMapperResults.propertyTableRows == null)
                                {
                                    classificationAttributesMapperResults.propertyTableRows = new ArrayList<>();
                                }

                                classificationAttributesMapperResults.propertyTableRows.add(classificationAttributeRow);
                            }
                        }
                    }

                    if (!mapperResultRowsMap.isEmpty())
                    {
                        List<EntityMapper> entityMappers = new ArrayList<>();

                        for (MapperResultRows mapperResultRows : mapperResultRowsMap.values())
                        {
                            List<ClassificationMapper> classificationMappers = null;

                            if (mapperResultRows.classifications != null)
                            {
                                classificationMappers = new ArrayList<>();

                                for (String classificationName : mapperResultRows.classifications.keySet())
                                {
                                    MapperResultRows classificationMapperResults = mapperResultRows.classifications.get(classificationName);
                                    ClassificationMapper classificationMapper = new ClassificationMapper(classificationMapperResults.principleTableRow,
                                                                                                         classificationMapperResults.propertyTableRows,
                                                                                                         repositoryHelper,
                                                                                                         repositoryName);

                                    classificationMappers.add(classificationMapper);
                                }
                            }

                            entityMappers.add(new EntityMapper(mapperResultRows.principleTableRow, mapperResultRows.propertyTableRows, classificationMappers, repositoryHelper, repositoryName));
                        }

                        return entityMappers;
                    }
                }
            }
            catch (SQLException sqlException)
            {
                throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                               sqlException.getClass().getName(),
                                                                                                               methodName,
                                                                                                               sqlException.getMessage()),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   sqlException);
            }
        }

        return null;
    }



    /**
     * Retrieve a relationship from the database.
     *
     * @param guid unique identifier of the relationship
     * @return  Relationship mapper
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public RelationshipMapper getRelationshipForUpdate(String guid) throws RepositoryErrorException
    {
        return getRelationshipFromStore(guid, null);
    }


    /**
     * Retrieve the version of a relationship from the database that was active at the requested time.
     * Null is returned if there were no active instance.
     *
     * @param guid unique identifier of the relationship
     * @param asOfTime requested time for the version
     * @return relationship mapper
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public RelationshipMapper getRelationshipFromStore(String guid,
                                                       Date   asOfTime) throws RepositoryErrorException
    {
        final String methodName = "getRelationshipFromStore";

        try
        {
            Map<String, JDBCDataValue> relationshipRow = jdbcResourceConnector.getMatchingRow(RepositoryTable.RELATIONSHIP.getTableName(),
                                                                                              RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "'" + getAsOfTimeClause(asOfTime),
                                                                                              RepositoryTable.RELATIONSHIP.getColumnNameTypeMap());

            return this.getCompleteRelationshipFromStore(guid, relationshipRow, asOfTime);
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Retrieve the related information for a relationship.
     * Null is returned if there was no active instance.
     *
     * @param guid unique identifier of the relationship
     * @param relationshipRow appropriate row from table
     * @param asOfTime time for the database query
     * @return relationship mapper
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public RelationshipMapper getCompleteRelationshipFromStore(String                     guid,
                                                               Map<String, JDBCDataValue> relationshipRow,
                                                               Date                       asOfTime) throws RepositoryErrorException
    {
        final String methodName = "getCompleteRelationshipFromStore";

        try
        {
            if (relationshipRow != null)
            {
                long version = baseMapper.getLongPropertyFromColumn(RepositoryColumn.VERSION.getColumnName(), relationshipRow, true);
                String end1GUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.END_1_GUID.getColumnName(), relationshipRow, true);
                String end2GUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.END_2_GUID.getColumnName(), relationshipRow, true);

                String whereClause = RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + version;

                List<Map<String, JDBCDataValue>> relationshipProperties = jdbcResourceConnector.getMatchingRows(RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                whereClause,
                                                                                                                RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                EntityMapper end1Mapper = this.getEntityFromStore(end1GUID, asOfTime);
                EntityMapper end2Mapper = this.getEntityFromStore(end2GUID, asOfTime);

                return new RelationshipMapper(relationshipRow, relationshipProperties, end1Mapper, end2Mapper, repositoryHelper, repositoryName);
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        return null;
    }


    /**
     * Retrieve the related information for a list of relationships that were identified in a query.
     * Null is returned if there were no instances returned from the query.
     *
     * @param relationshipGUIDs list of unique identifiers for the matching relationships
     * @param asOfTime time for the database query
     * @return list of relationship mappers
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    private List<RelationshipMapper> getCompleteRelationshipsFromStore(List<String> relationshipGUIDs,
                                                                       Date         asOfTime) throws RepositoryErrorException
    {
        final String methodName = "getCompleteRelationshipsFromStore";

        if ((relationshipGUIDs != null) && (!relationshipGUIDs.isEmpty()))
        {
            try
            {
                Map<String, MapperResultRows> mapperResultRowsMap = new HashMap<>();

                QueryBuilder queryBuilder = new QueryBuilder(repositoryHelper, repositoryName);

                queryBuilder.setGUIDList(relationshipGUIDs);
                queryBuilder.setAsOfTime(asOfTime);

                String whereClause = queryBuilder.getAsOfTimeWhereClause();

                List<Map<String, JDBCDataValue>> relationshipRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.RELATIONSHIP.getTableName(),
                                                                                                          whereClause,
                                                                                                          RepositoryTable.RELATIONSHIP.getColumnNameTypeMap());

                if (relationshipRows != null)
                {
                    Map<String, Long> guidVersionMap = new HashMap<>();

                    for (Map<String, JDBCDataValue> relationshipRow : relationshipRows)
                    {
                        String instanceGUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), relationshipRow, true);
                        Long version = baseMapper.getLongPropertyFromColumn(RepositoryColumn.VERSION.getColumnName(), relationshipRow, true);

                        guidVersionMap.put(instanceGUID, version);

                        MapperResultRows mapperResultRows = new MapperResultRows();
                        mapperResultRows.principleTableRow = relationshipRow;

                        mapperResultRowsMap.put(instanceGUID, mapperResultRows);
                    }

                    List<Map<String, JDBCDataValue>> relationshipAttributeRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                       queryBuilder.getGUIDListPropertiesQueryWhereClause(guidVersionMap),
                                                                                                                       RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                    if (relationshipAttributeRows != null)
                    {
                        for (Map<String, JDBCDataValue> relationshipAttributeRow : relationshipAttributeRows)
                        {
                            String instanceGUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), relationshipAttributeRow, true);

                            MapperResultRows mapperResultRows = mapperResultRowsMap.get(instanceGUID);

                            if (mapperResultRows.propertyTableRows == null)
                            {
                                mapperResultRows.propertyTableRows = new ArrayList<>();
                            }

                            mapperResultRows.propertyTableRows.add(relationshipAttributeRow);
                        }
                    }

                    if (!mapperResultRowsMap.isEmpty())
                    {
                        List<RelationshipMapper> relationshipMappers = new ArrayList<>();

                        for (MapperResultRows mapperResultRows : mapperResultRowsMap.values())
                        {
                            String end1GUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.END_1_GUID.getColumnName(), mapperResultRows.principleTableRow, true);
                            String end2GUID = baseMapper.getStringPropertyFromColumn(RepositoryColumn.END_2_GUID.getColumnName(), mapperResultRows.principleTableRow, true);

                            EntityMapper end1Mapper = this.getEntityFromStore(end1GUID, asOfTime);
                            EntityMapper end2Mapper = this.getEntityFromStore(end2GUID, asOfTime);

                            relationshipMappers.add(new RelationshipMapper(mapperResultRows.principleTableRow, mapperResultRows.propertyTableRows, end1Mapper, end2Mapper, repositoryHelper, repositoryName));
                        }

                        return relationshipMappers;
                    }
                }
            }
            catch (SQLException sqlException)
            {
                throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                               sqlException.getClass().getName(),
                                                                                                               methodName,
                                                                                                               sqlException.getMessage()),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   sqlException);
            }
        }

        return null;
    }


    /**
     * Gather information from a database retrieval that can be used to build a mapper
     * (entity or relationship).
     */
    static class MapperResultRows
    {
        Map<String, JDBCDataValue>       principleTableRow = null;
        List<Map<String, JDBCDataValue>> propertyTableRows = null;

        Map<String, MapperResultRows>    classifications   = null;
    }


    /**
     * Create the part of the where clause that ensures that the correct version is returned.
     *
     * @param asOfTime database time to issue the query for - null means the latest version
     * @return fragment of SQL
     */
    private String getAsOfTimeClause(Date asOfTime)
    {
        if (asOfTime == null)
        {
            return " and " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null";
        }
        else
        {
            return " and (" + RepositoryColumn.VERSION_START_TIME.getColumnName() + " < '" + asOfTime + "' and (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null or " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " > '" + asOfTime + "'))";
        }
    }


    /**
     * Return the list of versions for an entity.
     *
     * @param guid unique identifier of the instance
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     * @throws RepositoryErrorException problem communicating with the database
     */
    public List<EntityMapper> getEntityHistoryFromStore(String  guid,
                                                        Date    fromTime,
                                                        Date    toTime,
                                                        boolean oldestFirst) throws RepositoryErrorException
    {
        final String methodName = "getEntityHistoryFromStore";

        List<EntityMapper> entityMappers = new ArrayList<>();

        try
        {
            List<Map<String, JDBCDataValue>> entityRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.ENTITY.getTableName(),
                                                                                                RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "'" +
                                                                                                        getDateRangeClause(fromTime, toTime, oldestFirst),
                                                                                                RepositoryTable.ENTITY.getColumnNameTypeMap());

            if (entityRows != null)
            {
                for (Map<String, JDBCDataValue> entityRow : entityRows)
                {
                    entityMappers.add(this.getCompleteEntityFromStore(guid, entityRow));
                }
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        if (! entityMappers.isEmpty())
        {
            return entityMappers;
        }

        return null;
    }


    /**
     * Return the list of versions for a relationship.
     *
     * @param guid unique identifier of the relationship
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return list of instance versions
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public List<RelationshipMapper> getRelationshipHistoryFromStore(String  guid,
                                                                    Date    fromTime,
                                                                    Date    toTime,
                                                                    boolean oldestFirst) throws RepositoryErrorException
    {
        final String methodName = "getRelationshipHistoryFromStore";

        List<RelationshipMapper> relationshipMappers = new ArrayList<>();

        try
        {
            List<Map<String, JDBCDataValue>> relationshipRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.RELATIONSHIP.getTableName(),
                                                                                                      RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "'" +
                                                                                                              getDateRangeClause(fromTime, toTime, oldestFirst),
                                                                                                      RepositoryTable.RELATIONSHIP.getColumnNameTypeMap());

            if (relationshipRows != null)
            {
                for (Map<String, JDBCDataValue> relationshipRow : relationshipRows)
                {
                    Date asOfTime = baseMapper.getDatePropertyFromColumn(RepositoryColumn.VERSION_START_TIME.getColumnName(), relationshipRow, true);
                    relationshipMappers.add(this.getCompleteRelationshipFromStore(guid, relationshipRow, asOfTime));
                }
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        if (! relationshipMappers.isEmpty())
        {
            return relationshipMappers;
        }

        return null;
    }


    /**
     * Create the part of the where clause that ensures that the correct version is returned.
     *
     * @param fromTime starting time
     * @param toTime ending time
     * @param oldestFirst ordering
     * @return fragment of SQL
     */
    private String getDateRangeClause(Date    fromTime,
                                      Date    toTime,
                                      boolean oldestFirst)
    {
        if ((fromTime == null) && (toTime == null))
        {
            return getOrderByDateClause(oldestFirst);
        }
        else if (fromTime == null)
        {
            return " and (" + RepositoryColumn.VERSION_START_TIME.getColumnName() + " < '" + toTime + "')" + getOrderByDateClause(oldestFirst);
        }
        else if (toTime == null)
        {
            return " and (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null or " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " > '" + fromTime + "')" + getOrderByDateClause(oldestFirst);
        }
        else
        {
            return " and (" + RepositoryColumn.VERSION_START_TIME.getColumnName() + " < '" + toTime + "' and (" + RepositoryColumn.VERSION_END_TIME.getColumnName() + " is null or " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " > '" + fromTime + "'))" + getOrderByDateClause(oldestFirst);
        }
    }


    /**
     * Create an order by statement.
     *
     * @param oldestFirst ordering
     * @return fragment of sql
     */
    private String getOrderByDateClause(boolean oldestFirst)
    {
        if (oldestFirst)
        {
            return " order by " + RepositoryColumn.VERSION_START_TIME.getColumnName() + " asc";
        }
        else
        {
            return " order by " + RepositoryColumn.VERSION_START_TIME.getColumnName() + " desc";
        }
    }


    /**
     * Retrieve details about an entity from the store.  The entity mapper is returned to allow the caller to choose
     * what style of entity to return.
     *
     * @param entityGUID unique identifier of the entity
     * @param classificationName name of classification
     * @return classification mapper containing all that is known about the latest version of the classification
     * @throws RepositoryErrorException either a problem accessing the database or a mapping problem with the contents retrieved
     */
    public ClassificationMapper getClassificationForUpdate(String entityGUID,
                                                           String classificationName) throws RepositoryErrorException
    {
        final String methodName = "getClassificationForUpdate";

        try
        {
            Map<String, JDBCDataValue> classificationRow = jdbcResourceConnector.getMatchingRow(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                                        RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + entityGUID +
                                                                                                "' and " + RepositoryColumn.CLASSIFICATION_NAME.getColumnName() + " = '" + classificationName + "'" +
                                                                                                getAsOfTimeClause(null),
                                                                                        RepositoryTable.CLASSIFICATION.getColumnNameTypeMap());

            if (classificationRow != null)
            {
                BaseMapper baseMapper = new BaseMapper(repositoryName);

                long version = baseMapper.getLongPropertyFromColumn(RepositoryColumn.VERSION.getColumnName(), classificationRow, true);

                String whereClause = RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + entityGUID +
                        "' and " + RepositoryColumn.CLASSIFICATION_NAME.getColumnName() + " = '" + classificationName +
                        "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + version;


                List<Map<String, JDBCDataValue>> classificationProperties = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                  whereClause,
                                                                                                                  RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getColumnNameTypeMap());
                return new ClassificationMapper(classificationRow, classificationProperties, repositoryHelper, repositoryName);
            }
            else
            {
                return null;
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Return the list of home classifications for an entity.
     *
     * @param guid unique identifier of the entity
     * @param localMetadataCollectionId unique identifier of the local metadata collection
     * @param asOfTime database time
     * @return list of classifications or null
     * @throws RepositoryErrorException problem communicating with the database
     */
    public List<ClassificationMapper> getHomeClassifications(String guid,
                                                             String localMetadataCollectionId,
                                                             Date   asOfTime) throws RepositoryErrorException
    {
        final String methodName = "getHomeClassifications";

        try
        {
            String whereClause = RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "' and " + RepositoryColumn.METADATA_COLLECTION_GUID.getColumnName() + " = '" + localMetadataCollectionId + "' and " + this.getAsOfTimeClause(asOfTime) + ";";

            List<Map<String, JDBCDataValue>> classifications = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                                                     whereClause,
                                                                                                     RepositoryTable.CLASSIFICATION.getColumnNameTypeMap());

            List<ClassificationMapper> classificationMappers = null;

            if (classifications != null)
            {
                classificationMappers = new ArrayList<>();

                for (Map<String, JDBCDataValue> classificationRow : classifications)
                {
                    if (classificationRow != null)
                    {
                        Object classificationName = classificationRow.get(RepositoryColumn.CLASSIFICATION_NAME.getColumnName()).getDataValue();

                        String classificationWhereClause = RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + " and " + RepositoryColumn.CLASSIFICATION_NAME.getColumnName() + " = '" + classificationName + "' and " + this.getAsOfTimeClause(asOfTime);

                        List<Map<String, JDBCDataValue>> classificationProperties = jdbcResourceConnector.getMatchingRows(RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                                                                                          classificationWhereClause,
                                                                                                                          RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getColumnNameTypeMap());

                        ClassificationMapper classificationMapper = new ClassificationMapper(classificationRow,
                                                                                             classificationProperties,
                                                                                             repositoryHelper,
                                                                                             repositoryName);

                        classificationMappers.add(classificationMapper);
                    }
                }
            }

            return classificationMappers;
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * This query is issued against a join of the entity table and the entity attribute table.
     *
     * @param classificationQueryBuilder filled with conditions for the where clause for the SQL query
     * @param entityQueryBuilder filled with conditions for the where clause for the SQL query
     * @param asOfTime database time
     * @return list of matching entity mappers or null if nothing matches
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public List<EntityMapper> retrieveEntitiesByProperties(QueryBuilder entityQueryBuilder,
                                                           QueryBuilder classificationQueryBuilder,
                                                           Date         asOfTime) throws RepositoryErrorException
    {
        final String methodName = "retrieveEntitiesByProperties";

        String sqlEntityQuery = entityQueryBuilder.getPropertyJoinQuery(RepositoryTable.ENTITY.getTableName(),
                                                            RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName()) +
                " where " + entityQueryBuilder.getAsOfTimeWhereClause();

        try
        {
            List<Map<String, JDBCDataValue>> entityRows;

            if (classificationQueryBuilder == null)
            {
                entityRows = jdbcResourceConnector.getMatchingRows(sqlEntityQuery, RepositoryTable.getSingleColumnNameTypeMap(RepositoryColumn.INSTANCE_GUID));
            }
            else
            {
                String sqlClassificationQuery = classificationQueryBuilder.getPropertyJoinQuery(RepositoryTable.CLASSIFICATION.getTableName(),
                                                                                                RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName()) +
                        " where " + classificationQueryBuilder.getAsOfTimeWhereClause();

                entityRows = jdbcResourceConnector.getMatchingRows("(" + sqlClassificationQuery + ") union (" + sqlEntityQuery + ")",
                                                                   RepositoryTable.getSingleColumnNameTypeMap(RepositoryColumn.INSTANCE_GUID));
            }

            if (entityRows != null)
            {
                List<String> entityGUIDs = new ArrayList<>();

                for (Map<String, JDBCDataValue> entityRow : entityRows)
                {
                    String guid = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), entityRow, true);

                    entityGUIDs.add(guid);
                }

                return this.getCompleteEntitiesFromStore(entityGUIDs, asOfTime);
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        return null;
    }


    /**
     * This query is issued against the relationship table.
     *
     * @param whereClause where clause for the SQL query
     * @param asOfTime database time
     * @return list of matching relationship mappers or null if nothing matches
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public List<RelationshipMapper> retrieveRelationships(String whereClause,
                                                          Date   asOfTime) throws RepositoryErrorException
    {
        final String methodName = "retrieveRelationships";

        try
        {
            List<Map<String, JDBCDataValue>> relationshipRows = jdbcResourceConnector.getMatchingRows(RepositoryTable.RELATIONSHIP.getTableName(),
                                                                                                      whereClause,
                                                                                                      RepositoryTable.RELATIONSHIP.getColumnNameTypeMap());

            if (relationshipRows != null)
            {
                List<String> relationshipGUIDs = new ArrayList<>();

                for (Map<String, JDBCDataValue> relationshipRow : relationshipRows)
                {
                    String guid = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), relationshipRow, true);

                    relationshipGUIDs.add(guid);
                }

                return this.getCompleteRelationshipsFromStore(relationshipGUIDs, asOfTime);
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        return null;
    }


    /**
     * This query is issued against a join of the relationship table and the relationship attribute table.
     *
     * @param queryBuilder populated with details of the where clause for the SQL query
     * @param asOfTime database time
     * @return list of matching relationship mappers or null if nothing matches
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public List<RelationshipMapper> retrieveRelationshipsByProperties(QueryBuilder queryBuilder,
                                                                      Date         asOfTime) throws RepositoryErrorException
    {
        final String methodName = "retrieveRelationships";

        String sqlQuery = queryBuilder.getPropertyJoinQuery(RepositoryTable.RELATIONSHIP.getTableName(),
                                                            RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName()) +
                " where " + queryBuilder.getAsOfTimeWhereClause();


        try
        {
            List<Map<String, JDBCDataValue>> relationshipRows = jdbcResourceConnector.getMatchingRows(sqlQuery, RepositoryTable.getSingleColumnNameTypeMap(RepositoryColumn.INSTANCE_GUID));

            if (relationshipRows != null)
            {
                List<String> relationshipGUIDs = new ArrayList<>();

                for (Map<String, JDBCDataValue> relationshipRow : relationshipRows)
                {
                    String guid = baseMapper.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(), relationshipRow, true);

                    relationshipGUIDs.add(guid);
                }

                return this.getCompleteRelationshipsFromStore(relationshipGUIDs, asOfTime);
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }

        return null;
    }



    /**
     * Add an entity to the database.  Its header information (and effectivity dates) goes into the
     * entity table. The properties go into the entity attribute values table.
     *
     * @param entityMapper details about the entity
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public void addEntityToStore(EntityMapper entityMapper) throws RepositoryErrorException
    {
        final String methodName = "addEntityToStore";

        try
        {
            jdbcResourceConnector.insertRowIntoTable(RepositoryTable.ENTITY.getTableName(),
                                                     entityMapper.getEntityTableRow());

            jdbcResourceConnector.insertRowsIntoTable(RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                      entityMapper.getEntityPropertiesTableRows());

            saveClassifications(entityMapper.getClassificationMappers());
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Add an entity proxy to the database only if the entity is not known.  Its header information (and effectivity dates) goes into the
     * entity table. The properties go into the entity attribute values table.
     *
     * @param entityMapper details about the entity
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public void addEntityProxyToStore(EntityMapper entityMapper) throws RepositoryErrorException
    {
        final String methodName = "addEntityProxyToStore";

        try
        {
            EntityMapper existingEntity = this.getEntityForUpdate(entityMapper.getEntityProxy().getGUID());

            if (existingEntity == null)
            {
                /*
                 * The entity proxy is only added if there is no known entity.
                 */
                jdbcResourceConnector.insertRowIntoTable(RepositoryTable.ENTITY.getTableName(),
                                                         entityMapper.getEntityTableRow());

                jdbcResourceConnector.insertRowsIntoTable(RepositoryTable.ENTITY_ATTRIBUTE_VALUE.getTableName(),
                                                          entityMapper.getUniquePropertiesTableRows());

                saveClassifications(entityMapper.getClassificationMappers());
            }
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Save a list of classifications into the database.
     *
     * @param classificationMappers classification mappers
     * @throws RepositoryErrorException problem communicating with the database
     */
    private void saveClassifications(List<ClassificationMapper> classificationMappers) throws RepositoryErrorException
    {
        if (classificationMappers != null)
        {
            for (ClassificationMapper classificationMapper : classificationMappers)
            {
                saveClassification(classificationMapper);
            }
        }
    }


    /**
     * Maintain a classification within the entity.
     *
     * @param classificationMapper classification to update
     * @throws RepositoryErrorException problem communicating with the database
     */
    public void saveClassification(ClassificationMapper classificationMapper) throws RepositoryErrorException
    {
        final String methodName = "saveClassification";

        if (classificationMapper != null)
        {
            try
            {
                jdbcResourceConnector.insertRowIntoTable(RepositoryTable.CLASSIFICATION.getTableName(),
                                                         classificationMapper.getClassificationTableRow());

                jdbcResourceConnector.insertRowsIntoTable(RepositoryTable.CLASSIFICATION_ATTRIBUTE_VALUE.getTableName(),
                                                          classificationMapper.getClassificationPropertiesTableRows());
            }
            catch (SQLException sqlException)
            {
                throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                               sqlException.getClass().getName(),
                                                                                                               methodName,
                                                                                                               sqlException.getMessage()),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   sqlException);
            }
        }
    }


    /**
     * Add a relationship to the database.  Its header information (and effectivity dates) goes into the
     * relationship table. The properties go into the relationship attribute values table.
     *
     * @param relationshipMapper details about the relationship
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public void addRelationshipToStore(RelationshipMapper relationshipMapper) throws RepositoryErrorException
    {
        final String methodName = "addRelationshipToStore";

        try
        {
            /*
             * The entity proxies are only added if the entity is unknown.
             */
            this.addEntityProxyToStore(relationshipMapper.getEnd1Mapper());
            this.addEntityProxyToStore(relationshipMapper.getEnd2Mapper());

            jdbcResourceConnector.insertRowIntoTable(RepositoryTable.RELATIONSHIP.getTableName(),
                                                     relationshipMapper.getRelationshipTableRow());

            jdbcResourceConnector.insertRowsIntoTable(RepositoryTable.RELATIONSHIP_ATTRIBUTE_VALUE.getTableName(),
                                                      relationshipMapper.getRelationshipPropertiesTableRows());
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Update the version end date in an entity to the database.
     *
     * @param entityMapper details about the entity
     * @param versionEndTime time that the previous version ended
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public void updatePreviousEntityVersionEndTime(EntityMapper entityMapper,
                                                   Date         versionEndTime) throws RepositoryErrorException
    {
        final String methodName = "updatePreviousEntityVersionEndTime";

        try
        {
            jdbcResourceConnector.issueSQLCommand("update " + RepositoryTable.ENTITY.getTableName() +
                                                          " set " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " = '" + versionEndTime +
                                                          "' where " + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + entityMapper.getEntityDetail().getGUID() + "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + entityMapper.getEntityDetail().getVersion() + ";");
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Update the version end date in an entity's classification to the database.
     *
     * @param classificationMapper details about the classification
     * @param versionEndTime time that the previous version ended
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public void updatePreviousClassificationVersionEndTime(ClassificationMapper classificationMapper,
                                                           Date                 versionEndTime) throws RepositoryErrorException
    {
        final String methodName = "updatePreviousClassificationVersionEndTime";

        try
        {
            jdbcResourceConnector.issueSQLCommand("update " + RepositoryTable.CLASSIFICATION.getTableName() +
                                                          " set " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " = '" + versionEndTime +
                                                          "' where " + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + classificationMapper.getEntityGUID() +
                                                          "' and " + RepositoryColumn.CLASSIFICATION_NAME.getColumnName() + " = '" + classificationMapper.getClassification().getName() +
                                                          "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + classificationMapper.getClassification().getVersion() + ";");
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Update the version end date in a relationship to the database.
     *
     * @param relationshipMapper details about the relationship
     * @param versionEndTime time that the previous version ended
     * @throws RepositoryErrorException problem communicating with the database, or mapping the values returned
     */
    public void updatePreviousRelationshipVersionEndTime(RelationshipMapper relationshipMapper,
                                                         Date         versionEndTime) throws RepositoryErrorException
    {
        final String methodName = "updatePreviousRelationshipVersionEndTime";

        try
        {
            jdbcResourceConnector.issueSQLCommand("update " + RepositoryTable.RELATIONSHIP.getTableName() +
                                                          " set " + RepositoryColumn.VERSION_END_TIME.getColumnName() + " = '" + versionEndTime +
                                                          "' where " + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + relationshipMapper.getRelationship().getGUID() + "' and " + RepositoryColumn.VERSION.getColumnName() + " = " + relationshipMapper.getRelationship().getVersion() + ";");
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Purge a classification from the database
     *
     * @param entityGUID unique identifier
     * @param classificationName name of classification to remove
     * @throws RepositoryErrorException problem communicating with the database
     */
    public void purgeClassification(String entityGUID,
                                    String classificationName) throws RepositoryErrorException
    {
        final String methodName = "purgeClassification";

        try
        {
            jdbcResourceConnector.issueSQLCommand("delete from " + RepositoryTable.CLASSIFICATION.getTableName() +
                                                          " where " + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + entityGUID + "' and " + RepositoryColumn.CLASSIFICATION_NAME.getColumnName() + " = '" + classificationName + "';");
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Remove (purge) an entity from the repository.  This method relies on the foreign key relationships
     * to remove all versions of the entity, and its linked relationships and classifications.  This request
     * could ripple updates into all tables except the repository control table.
     *
     * @param guid unique identifier of the entity
     * @throws RepositoryErrorException problem communicating with the database
     */
    public void purgeEntity(String guid) throws RepositoryErrorException
    {
        final String methodName = "purgeEntity";

        try
        {
            jdbcResourceConnector.issueSQLCommand("delete from " + RepositoryTable.ENTITY.getTableName() +
                                                          " where " + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "';");
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }


    /**
     * Remove (purge) a relationship from the repository.  This method relies on the foreign key relationships
     * to properly remove all of the versions and properties associated with the relationship.
     * It should have no effect on the entities and classification tables.
     *
     * @param guid unique identifier of the entity
     * @throws RepositoryErrorException problem communicating with the database
     */
    public void purgeRelationship(String guid) throws RepositoryErrorException
    {
        final String methodName = "purgeRelationship";

        try
        {
            jdbcResourceConnector.issueSQLCommand("delete from " + RepositoryTable.RELATIONSHIP.getTableName() +
                                                          " where " + RepositoryColumn.INSTANCE_GUID.getColumnName() + " = '" + guid + "';");
        }
        catch (SQLException sqlException)
        {
            throw new RepositoryErrorException(PostgresErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryName,
                                                                                                           sqlException.getClass().getName(),
                                                                                                           methodName,
                                                                                                           sqlException.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               sqlException);
        }
    }
}

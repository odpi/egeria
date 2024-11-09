/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;


import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.*;

/**
 * Map between an EntityDetail or EntityProxy object and a collection of database rows.
 * This mapping can go in either direction.
 */
public class EntityMapper extends RepositoryMapper
{
    private EntityDetail                     entityDetail              = null;
    private EntityProxy                      entityProxy               = null;
    private Map<String, JDBCDataValue>       entityTableRow            = null;
    private List<Map<String, JDBCDataValue>> entityPropertiesTableRows = null;
    private List<ClassificationMapper>       classificationMappers     = null;


    /**
     * Construct an entity mapper using an OMRS instance.
     *
     * @param entityDetail full entity
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public EntityMapper(EntityDetail         entityDetail,
                        OMRSRepositoryHelper repositoryHelper,
                        String               repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.entityDetail = entityDetail;
    }


    /**
     * Construct an entity mapper using an OMRS instance.
     *
     * @param entityProxy  entity proxy
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public EntityMapper(EntityProxy          entityProxy,
                        OMRSRepositoryHelper repositoryHelper,
                        String               repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.entityProxy = entityProxy;
    }


    /**
     * Construct an entity mapper using the values from the database.
     *
     * @param entityTableRow row from the entity table
     * @param entityPropertiesTableRows rows from the entity properties table
     * @param classificationMappers mappers representing the entity's classifications
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public EntityMapper(Map<String, JDBCDataValue>       entityTableRow,
                        List<Map<String, JDBCDataValue>> entityPropertiesTableRows,
                        List<ClassificationMapper>       classificationMappers,
                        OMRSRepositoryHelper             repositoryHelper,
                        String                           repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.entityTableRow            = entityTableRow;
        this.entityPropertiesTableRows = entityPropertiesTableRows;
        this.classificationMappers     = classificationMappers;
    }


    /**
     * Determine whether this entity is a proxy or not.
     *
     * @return boolean
     * @throws RepositoryErrorException mapping problem
     */
    public boolean isProxy() throws RepositoryErrorException
    {
        final String methodName = "isProxy";

        if ((entityDetail == null) && (entityProxy != null))
        {
            return true;
        }

        if (entityTableRow != null)
        {
            return super.getBooleanPropertyFromColumn(RepositoryColumn.IS_PROXY.getColumnName(),
                                                      entityTableRow,
                                                      true);
        }

        throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("entityTableRow",
                                                                                                        methodName,
                                                                                                        this.getClass().getName()),
                                           this.getClass().getName(),
                                           methodName);
    }


    /**
     * Construct an entity detail object using the values from the database.
     *
     * @return full entity description or null if the entity is not available
     * @throws RepositoryErrorException problem mapping the properties
     */
    public EntityDetail getEntityDetail() throws RepositoryErrorException
    {
        final String methodName = "getEntityDetail";

        if (entityDetail == null)
        {
            if (entityTableRow != null)
            {
                EntityDetail entity = new EntityDetail();

                super.fillInstanceHeader(entity, entityTableRow);
                entity.setProperties(super.getInstanceProperties(entityTableRow, entityPropertiesTableRows));
                entity.setClassifications(this.getClassifications());

                entityDetail = entity;
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("entityTableRow",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return entityDetail;
    }


    /**
     * Construct an entity proxy object from the values from the database.
     *
     * @return  entity proxy description
     * @throws RepositoryErrorException problem mapping the properties
     */
    public EntityProxy getEntityProxy() throws RepositoryErrorException
    {
        final String methodName = "getEntityProxy";

        if (entityProxy == null)
        {
            if (entityTableRow != null)
            {
                EntityProxy entity = new EntityProxy();

                super.fillInstanceHeader(entity, entityTableRow);
                entity.setUniqueProperties(super.getUniqueProperties(entityTableRow, entityPropertiesTableRows));
                entity.setClassifications(this.getClassifications());

                entityProxy = entity;
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("entityTableRow",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return entityProxy;
    }


    /**
     * Return the classifications stored in the classification mappers.
     *
     * @return list of classification of null
     * @throws RepositoryErrorException problem mapping the properties
     */
    private List<Classification> getClassifications() throws RepositoryErrorException
    {
        if (classificationMappers != null)
        {
            List<Classification> classifications = new ArrayList<>();

            for (ClassificationMapper classificationMapper : classificationMappers)
            {
                if (classificationMapper != null)
                {
                    classifications.add(classificationMapper.getClassification());
                }
            }

            if (! classifications.isEmpty())
            {
                return classifications;
            }
        }

        return null;
    }


    /**
     * Extract the entity table information from the entity object.
     *
     * @return entity table row for this entity
     * @throws RepositoryErrorException problem mapping the properties
     */
    public Map<String, JDBCDataValue> getEntityTableRow() throws RepositoryErrorException
    {
        if (entityTableRow == null)
        {
            Map<String, JDBCDataValue> newEntityTableRow = new HashMap<>();

            if (entityDetail != null)
            {
                if (entityDetail.getProperties() != null)
                {
                    super.extractValuesFromInstanceHeader(newEntityTableRow,
                                                          null,
                                                          entityDetail.getProperties().getEffectiveFromTime(),
                                                          entityDetail.getProperties().getEffectiveToTime(),
                                                          null,
                                                          entityDetail);
                }
                else
                {
                    super.extractValuesFromInstanceHeader(newEntityTableRow,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          entityDetail);
                }

                super.setUpBooleanValueInRow(newEntityTableRow, false, RepositoryColumn.IS_PROXY.getColumnName());
            }
            else if (entityProxy != null)
            {
                if (entityProxy.getUniqueProperties() != null)
                {
                    super.extractValuesFromInstanceHeader(newEntityTableRow,
                                                          null,
                                                          entityProxy.getUniqueProperties().getEffectiveFromTime(),
                                                          entityProxy.getUniqueProperties().getEffectiveToTime(),
                                                          null,
                                                          entityProxy);
                }
                else
                {
                    super.extractValuesFromInstanceHeader(newEntityTableRow,
                                                          null,
                                                          null,
                                                          null,
                                                          null,
                                                          entityProxy);
                }

                super.setUpBooleanValueInRow(newEntityTableRow, true, RepositoryColumn.IS_PROXY.getColumnName());
            }

            if (! newEntityTableRow.isEmpty())
            {
                entityTableRow = newEntityTableRow;
            }
        }

        return entityTableRow;
    }


    /**
     * Return the rows that describe the properties for this entity.
     *
     * @return property rows
     * @throws RepositoryErrorException problem mapping the properties
     */
    public List<Map<String, JDBCDataValue>> getEntityPropertiesTableRows() throws RepositoryErrorException
    {
        final String methodName = "getEntityPropertiesTableRows";

        if (entityPropertiesTableRows == null)
        {
            if (entityDetail != null)
            {
                entityPropertiesTableRows = extractValuesFromInstanceProperties(entityDetail.getGUID(),
                                                                                null,
                                                                                entityDetail.getVersion(),
                                                                                entityDetail.getType().getTypeDefName(),
                                                                                entityDetail.getProperties(),
                                                                                null,
                                                                                null);
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("entityDetail",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return entityPropertiesTableRows;
    }


    /**
     * Return the rows that describe the properties for this entity.  This version allows an entity proxy's properties
     * to be stored if the entity detail is not available.
     *
     * @return property rows
     * @throws RepositoryErrorException problem mapping the properties
     */
    public List<Map<String, JDBCDataValue>> getUniquePropertiesTableRows() throws RepositoryErrorException
    {
        final String methodName = "getUniquePropertiesTableRows";

        List<Map<String, JDBCDataValue>> uniquePropertiesTableRows;

        if (entityDetail != null)
        {
            /*
             * Notice that all known properties are retrieved.  We never miss the change to save any properties we know about.
             */
            uniquePropertiesTableRows = entityPropertiesTableRows = extractValuesFromInstanceProperties(entityDetail.getGUID(),
                                                                                                        null,
                                                                                                        entityDetail.getVersion(),
                                                                                                        entityDetail.getType().getTypeDefName(),
                                                                                                        entityDetail.getProperties(),
                                                                                                        null,
                                                                                                        null);
        }
        else if (entityProxy != null)
        {
            uniquePropertiesTableRows = entityPropertiesTableRows = extractValuesFromInstanceProperties(entityProxy.getGUID(),
                                                                                                        null,
                                                                                                        entityProxy.getVersion(),
                                                                                                        entityProxy.getType().getTypeDefName(),
                                                                                                        entityProxy.getUniqueProperties(),
                                                                                                        null,
                                                                                                        null);
        }
        else
        {
            throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("entityProxy",
                                                                                                            methodName,
                                                                                                            this.getClass().getName()),
                                               this.getClass().getName(),
                                               methodName);
        }

        if ((uniquePropertiesTableRows != null) && (! uniquePropertiesTableRows.isEmpty()))
        {
            return uniquePropertiesTableRows;
        }

        return null;
    }


    /**
     * Return the list of classification mappers that represent the classifications associated with this entity.
     *
     * @return list of classification mappers or null
     */
    public List<ClassificationMapper> getClassificationMappers()
    {
        if (classificationMappers == null)
        {
            List<Classification> classificationList = null;
            String               entityGUID         = null;

            if (entityDetail != null)
            {
                classificationList = entityDetail.getClassifications();
                entityGUID = entityDetail.getGUID();
            }
            else if (entityProxy != null)
            {
                classificationList = entityProxy.getClassifications();
                entityGUID = entityProxy.getGUID();
            }

            if (classificationList != null)
            {
                List<ClassificationMapper> classificationMapperList = new ArrayList<>();

                for (Classification classification : classificationList)
                {
                    classificationMapperList.add(new ClassificationMapper(entityGUID,
                                                                          classification,
                                                                          repositoryHelper,
                                                                          repositoryName));
                }

                if (! classificationMapperList.isEmpty())
                {
                    this.classificationMappers = classificationMapperList;
                }
            }
        }

        return this.classificationMappers;
    }


    /**
     * JSON-style toString
     *
     * @return string of property names and values for this enum
     */
    @Override
    public String toString()
    {
        return "EntityMapper{" +
                "entityDetail=" + entityDetail +
                ", entityProxy=" + entityProxy +
                ", entityTableRow=" + entityTableRow +
                ", entityPropertiesTableRows=" + entityPropertiesTableRows +
                ", classificationMappers=" + classificationMappers +
                "}";
    }
}

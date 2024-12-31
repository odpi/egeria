/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.JDBCDataValue;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.ffdc.PostgresErrorCode;
import org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema.RepositoryColumn;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Map between a Classification object and a collection of database rows.
 * This mapping can go in either direction.
 */
public class ClassificationMapper extends RepositoryMapper
{
    private String                           entityGUID                        = null;
    private Classification                   classification                    = null;
    private Map<String, JDBCDataValue>       classificationTableRow            = null;
    private List<Map<String, JDBCDataValue>> classificationPropertiesTableRows = null;


    /**
     * Construct a classification mapper using an OMRS instance.
     *
     * @param entityGUID unique identifier of the associated entity
     * @param classification classification
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public ClassificationMapper(String               entityGUID,
                                Classification       classification,
                                OMRSRepositoryHelper repositoryHelper,
                                String               repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.entityGUID = entityGUID;
        this.classification = classification;
    }


    /**
     * Construct a classification mapper using the values from the database.
     *
     * @param classificationTableRow row from the classification table
     * @param classificationPropertiesTableRows rows from the classification properties table
     * @param repositoryHelper repository helper
     * @param repositoryName repository name
     */
    public ClassificationMapper(Map<String, JDBCDataValue>       classificationTableRow,
                                List<Map<String, JDBCDataValue>> classificationPropertiesTableRows,
                                OMRSRepositoryHelper             repositoryHelper,
                                String                           repositoryName)
    {
        super (repositoryHelper, repositoryName);

        this.classificationTableRow            = classificationTableRow;
        this.classificationPropertiesTableRows = classificationPropertiesTableRows;
    }


    /**
     * Construct a classification object from the values from the database.
     *
     * @return  classification description
     * @throws RepositoryErrorException problem mapping the properties
     */
    public Classification getClassification() throws RepositoryErrorException
    {
        final String methodName = "getClassification";

        if (classification == null)
        {
            if (classificationTableRow != null)
            {
                Classification newClassification = new Classification();

                super.fillInstanceAuditHeader(newClassification, classificationTableRow);
                newClassification.setName(super.getStringPropertyFromColumn(RepositoryColumn.CLASSIFICATION_NAME.getColumnName(), classificationTableRow, true));
                newClassification.setProperties(super.getInstanceProperties(classificationTableRow, classificationPropertiesTableRows));

                classification = newClassification;
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("classificationTableRow",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return classification;
    }


    /**
     * Extract the entity GUID from the values from the database.
     *
     * @return  guid
     * @throws RepositoryErrorException problem mapping the properties
     */
    public String getEntityGUID() throws RepositoryErrorException
    {
        final String methodName = "getEntityGUID";

        if (entityGUID == null)
        {
            if (classificationTableRow != null)
            {
                entityGUID = super.getStringPropertyFromColumn(RepositoryColumn.INSTANCE_GUID.getColumnName(),
                                                               classificationTableRow,
                                                               true);
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("classificationTableRow",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return entityGUID;
    }


    /**
     * Extract the classification table information from the classification object.
     *
     * @return classification table row for this classification
     * @throws RepositoryErrorException problem mapping the properties
     */
    public Map<String, JDBCDataValue> getClassificationTableRow() throws RepositoryErrorException
    {
        final String methodName = "getClassificationTableRow";

        if (classificationTableRow == null)
        {
            Map<String, JDBCDataValue> newClassificationTableRow = new HashMap<>();

            if ((classification != null) && (entityGUID != null))
            {
                if (classification.getProperties() != null)
                {
                    super.extractValuesFromInstanceAuditHeader(newClassificationTableRow,
                                                               entityGUID,
                                                               classification.getName(),
                                                               classification.getProperties().getEffectiveFromTime(),
                                                               classification.getProperties().getEffectiveToTime(),
                                                               null,
                                                               classification);
                }
                else
                {
                    super.extractValuesFromInstanceAuditHeader(newClassificationTableRow,
                                                               entityGUID,
                                                               classification.getName(),
                                                               null,
                                                               null,
                                                               null,
                                                               classification);
                }
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("classification and/or entityGUID",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }

            if (! newClassificationTableRow.isEmpty())
            {
                classificationTableRow = newClassificationTableRow;
            }
        }

        return classificationTableRow;
    }


    /**
     * Return the rows that describe the properties for this classification.
     *
     * @return property rows
     * @throws RepositoryErrorException problem mapping the properties
     */
    public List<Map<String, JDBCDataValue>> getClassificationPropertiesTableRows() throws RepositoryErrorException
    {
        final String methodName = "getClassificationPropertiesTableRows";

        if (classificationPropertiesTableRows == null)
        {
            if (classification != null)
            {
                classificationPropertiesTableRows = extractValuesFromInstanceProperties(entityGUID,
                                                                                        classification.getName(),
                                                                                        classification.getVersion(),
                                                                                        classification.getType().getTypeDefName(),
                                                                                        classification.getProperties(),
                                                                                        null,
                                                                                        null);
            }
            else
            {
                throw new RepositoryErrorException(PostgresErrorCode.MISSING_MAPPING_VALUE.getMessageDefinition("classification",
                                                                                                                methodName,
                                                                                                                this.getClass().getName()),
                                                   this.getClass().getName(),
                                                   methodName);
            }
        }

        return classificationPropertiesTableRows;
    }
}

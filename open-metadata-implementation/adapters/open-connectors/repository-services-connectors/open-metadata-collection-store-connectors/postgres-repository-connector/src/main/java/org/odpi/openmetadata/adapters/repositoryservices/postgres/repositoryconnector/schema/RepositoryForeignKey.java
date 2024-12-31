/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.schema;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLColumn;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLForeignKey;
import org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres.PostgreSQLTable;

/**
 * Defines a foreign key relationship between two tables.
 */
public enum RepositoryForeignKey implements PostgreSQLForeignKey
{
    VALID_ENTITY_GUID("valid_entity_guid", RepositoryColumn.INSTANCE_GUID, RepositoryTable.ENTITY, RepositoryColumn.INSTANCE_GUID, true, true),
    VALID_END_1_GUID("valid_end_1_guid", RepositoryColumn.INSTANCE_GUID, RepositoryTable.ENTITY, RepositoryColumn.INSTANCE_GUID, true, true),
    VALID_END_2_GUID("valid_end_2_guid", RepositoryColumn.INSTANCE_GUID, RepositoryTable.ENTITY, RepositoryColumn.INSTANCE_GUID, true, true),

    VALID_RELATIONSHIP_GUID("valid_relationship_guid", RepositoryColumn.INSTANCE_GUID, RepositoryTable.RELATIONSHIP, RepositoryColumn.INSTANCE_GUID, true, true),

    VALID_CLASSIFICATION_NAME("valid_classification_name", RepositoryColumn.CLASSIFICATION_NAME, RepositoryTable.CLASSIFICATION, RepositoryColumn.CLASSIFICATION_NAME, false, false),

    ;


    private final String           constraintName;
    private final RepositoryColumn foreignKeyColumn;
    private final RepositoryTable  referenceTable;
    private final RepositoryColumn referenceColumn;
    private final boolean          deleteCascade;
    private final boolean          updateCascade;

    RepositoryForeignKey(String           constraintName,
                         RepositoryColumn foreignKeyColumn,
                         RepositoryTable  referenceTable,
                         RepositoryColumn referenceColumn,
                         boolean          deleteCascade,
                         boolean          updateCascade)
    {
        this.constraintName   = constraintName;
        this.foreignKeyColumn = foreignKeyColumn;
        this.referenceTable   = referenceTable;
        this.referenceColumn  = referenceColumn;
        this.deleteCascade    = deleteCascade;
        this.updateCascade    = updateCascade;
    }


    /**
     * Return the name of the constraint.
     *
     * @return name
     */
    @Override
    public String getConstraintName()
    {
        return constraintName;
    }


    /**
     * Return the column in the table that is the foreign key.
     *
     * @return column
     */
    @Override
    public PostgreSQLColumn getForeignKeyColumn()
    {
        return foreignKeyColumn;
    }


    /**
     * Return details of the table where the primary key is located.
     *
     * @return table
     */
    @Override
    public PostgreSQLTable getReferenceTable()
    {
        return referenceTable;
    }


    /**
     * Return the primary key column in the reference table.
     *
     * @return column
     */
    @Override
    public PostgreSQLColumn getReferenceColumn()
    {
        return referenceColumn;
    }


    /**
     * If the row with the primary key is deleted, should the rows with the foreign key also be deleted?
     *
     * @return boolean
     */
    @Override
    public boolean isDeleteCascade()
    {
        return deleteCascade;
    }


    /**
     * If the primary key value is changed, should the foreign key values change too?
     *
     * @return boolean
     */
    @Override
    public boolean isUpdateCascade()
    {
        return updateCascade;
    }
}

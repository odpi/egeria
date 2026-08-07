/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.mssql;

/**
 * Defines a foreign key relationship between two tables.
 */
public interface MSSQLForeignKey
{
    /**
     * Return the name of the constraint.
     *
     * @return name
     */
    String getConstraintName();


    /**
     * Return the column in the table that is the foreign key.
     *
     * @return column
     */
    MSSQLColumn getForeignKeyColumn();


    /**
     * Return details of the table where the primary key is located.
     *
     * @return table
     */
    MSSQLTable getReferenceTable();


    /**
     * Return the primary key column in the reference table.
     *
     * @return column
     */
    MSSQLColumn getReferenceColumn();


    /**
     * If the row with the primary key is deleted, should the rows with the foreign key also be deleted?
     *
     * @return boolean
     */
    boolean isDeleteCascade();


    /**
     * If the primary key value is changed, should the foreign key values change too?
     *
     * @return boolean
     */
    boolean isUpdateCascade();
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.db2luw;

/**
 * Defines a foreign key relationship between two tables.
 * <br><br>
 * Unlike PostgreSQL and Microsoft SQL Server, Db2 for Linux, UNIX and Windows foreign key constraints have no
 * "ON UPDATE" clause at all (like Oracle, Db2 expects primary keys to be immutable), so there is no
 * isUpdateCascade() property to set here.
 */
public interface DB2LUWForeignKey
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
    DB2LUWColumn getForeignKeyColumn();


    /**
     * Return details of the table where the primary key is located.
     *
     * @return table
     */
    DB2LUWTable getReferenceTable();


    /**
     * Return the primary key column in the reference table.
     *
     * @return column
     */
    DB2LUWColumn getReferenceColumn();


    /**
     * If the row with the primary key is deleted, should the rows with the foreign key also be deleted?
     *
     * @return boolean
     */
    boolean isDeleteCascade();
}

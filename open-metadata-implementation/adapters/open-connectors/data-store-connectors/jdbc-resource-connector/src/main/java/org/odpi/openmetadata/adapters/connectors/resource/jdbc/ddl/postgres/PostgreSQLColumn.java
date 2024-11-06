/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.ddl.postgres;

import org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties.ColumnType;

/**
 * Describes the different types of columns found in a database schema
 */
public interface PostgreSQLColumn
{
    /**
     * retrieve the name of the column.
     *
     * @return name
     */
    public String getColumnName();


    /**
     * Return the type of the column.
     *
     * @return ColumnType
     */
    public ColumnType getColumnType();


    /**
     * Return th optional description for the column.
     *
     * @return text
     */
    public String getColumnDescription();


    /**
     * Return whether the column is not null;
     *
     * @return boolean
     */
    public boolean isNotNull();
}

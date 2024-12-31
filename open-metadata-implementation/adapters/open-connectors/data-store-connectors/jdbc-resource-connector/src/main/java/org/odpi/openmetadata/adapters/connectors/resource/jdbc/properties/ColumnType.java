/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties;

import java.sql.Types;

/**
 * Maps between different names for a database column type that are used in different interfaces.
 */
public enum ColumnType
{
    STRING(Types.VARCHAR, "text"),
    DATE(Types.TIMESTAMP, "timestamp(6) without time zone"),
    LONG(Types.BIGINT, "bigint"),
    INT(Types.INTEGER, "integer"),
    BOOLEAN(Types.BOOLEAN, "boolean")
    ;


    private final int jdbcType;
    private final String postgresType;


    ColumnType(int jdbcType, String postgresType)
    {
        this.jdbcType     = jdbcType;
        this.postgresType = postgresType;
    }


    /**
     * Return the type value used on JDBC calls to retrieve data from the database.
     *
     * @return int
     */
    public int getJdbcType()
    {
        return jdbcType;
    }


    /**
     * Return the PostgreSQL type used when defining tables.
     *
     * @return string
     */
    public String getPostgresType()
    {
        return postgresType;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties;

import java.sql.Types;

/**
 * Maps between different names for a database column type that are used in different interfaces.
 */
public enum ColumnType
{
    STRING(Types.VARCHAR, "text", "nvarchar(max)"),
    DATE(Types.TIMESTAMP, "timestamp(6) without time zone", "datetime2(6)"),
    LONG(Types.BIGINT, "bigint", "bigint"),
    INT(Types.INTEGER, "integer", "int"),
    BOOLEAN(Types.BOOLEAN, "boolean", "bit")
    ;


    private final int jdbcType;
    private final String postgresType;
    private final String mssqlType;


    ColumnType(int jdbcType, String postgresType, String mssqlType)
    {
        this.jdbcType     = jdbcType;
        this.postgresType = postgresType;
        this.mssqlType    = mssqlType;
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


    /**
     * Return the Microsoft SQL Server (T-SQL) type used when defining tables.
     *
     * @return string
     */
    public String getMssqlType()
    {
        return mssqlType;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties;

import java.sql.Types;

/**
 * Maps between different names for a database column type that are used in different interfaces.
 */
public enum ColumnType
{
    STRING(Types.VARCHAR, "text", "nvarchar(max)", "VARCHAR2(4000)"),
    DATE(Types.TIMESTAMP, "timestamp(6) without time zone", "datetime2(6)", "TIMESTAMP(6)"),
    LONG(Types.BIGINT, "bigint", "bigint", "NUMBER(19)"),
    INT(Types.INTEGER, "integer", "int", "NUMBER(10)"),
    /*
     * Oracle 23c introduced a native BOOLEAN column type, but NUMBER(1) is used here instead so that the
     * generated DDL also works against older, still widely deployed Oracle versions (eg 19c).
     */
    BOOLEAN(Types.BOOLEAN, "boolean", "bit", "NUMBER(1)")
    ;


    private final int jdbcType;
    private final String postgresType;
    private final String mssqlType;
    private final String oracleType;


    ColumnType(int jdbcType, String postgresType, String mssqlType, String oracleType)
    {
        this.jdbcType     = jdbcType;
        this.postgresType = postgresType;
        this.mssqlType    = mssqlType;
        this.oracleType   = oracleType;
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


    /**
     * Return the Oracle Database type used when defining tables.
     *
     * @return string
     */
    public String getOracleType()
    {
        return oracleType;
    }
}

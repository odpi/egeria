/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.connectors.resource.jdbc.properties;

import java.sql.Types;

/**
 * Maps between different names for a database column type that are used in different interfaces.
 */
public enum ColumnType
{
    STRING(Types.VARCHAR, "text", "nvarchar(max)", "VARCHAR2(4000)", "VARCHAR(4000)"),
    DATE(Types.TIMESTAMP, "timestamp(6) without time zone", "datetime2(6)", "TIMESTAMP(6)", "TIMESTAMP(6)"),
    /*
     * A point in time that keeps its time zone offset.  Writing a value with an offset into a DATE column loses
     * the offset - PostgreSQL silently ignores it - so '11:20+05:30' is stored as 11:20, five and a half hours
     * out.  Data copied from a source whose times carry offsets needs this type instead.  Db2 for Linux, UNIX
     * and Windows has no time zone type, so it falls back to TIMESTAMP(6) there.
     */
    TIMESTAMP_WITH_TIME_ZONE(Types.TIMESTAMP_WITH_TIMEZONE, "timestamp(6) with time zone", "datetimeoffset(6)", "TIMESTAMP(6) WITH TIME ZONE", "TIMESTAMP(6)"),
    /*
     * A calendar date with no time of day.  Oracle's DATE also holds a time of day, which is left at midnight.
     */
    LOCAL_DATE(Types.DATE, "date", "date", "DATE", "DATE"),
    LONG(Types.BIGINT, "bigint", "bigint", "NUMBER(19)", "BIGINT"),
    INT(Types.INTEGER, "integer", "int", "NUMBER(10)", "INTEGER"),
    /*
     * Oracle 23c introduced a native BOOLEAN column type, but NUMBER(1) is used here instead so that the
     * generated DDL also works against older, still widely deployed Oracle versions (eg 19c).  Db2 for Linux,
     * UNIX and Windows introduced a native BOOLEAN column type in 11.1, but SMALLINT (0/1) is used here for the
     * same reason - to keep the generated DDL working against older, still widely deployed Db2 LUW versions.
     */
    BOOLEAN(Types.BOOLEAN, "boolean", "bit", "NUMBER(1)", "SMALLINT")
    ;


    private final int jdbcType;
    private final String postgresType;
    private final String mssqlType;
    private final String oracleType;
    private final String db2luwType;


    ColumnType(int jdbcType, String postgresType, String mssqlType, String oracleType, String db2luwType)
    {
        this.jdbcType     = jdbcType;
        this.postgresType = postgresType;
        this.mssqlType    = mssqlType;
        this.oracleType   = oracleType;
        this.db2luwType   = db2luwType;
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


    /**
     * Return the IBM Db2 for Linux, UNIX and Windows type used when defining tables.
     *
     * @return string
     */
    public String getDB2LUWType()
    {
        return db2luwType;
    }
}

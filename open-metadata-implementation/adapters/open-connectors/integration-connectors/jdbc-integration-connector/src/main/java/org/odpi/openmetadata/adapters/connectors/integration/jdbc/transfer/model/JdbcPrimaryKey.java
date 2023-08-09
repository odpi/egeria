/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Represents a primary key as returned by the JDBC api. Fields are the ones described in {@link DatabaseMetaData}
 */
public class JdbcPrimaryKey {

    private final String tableCat;
    private final String tableSchem;
    private final String tableName;
    private final String columnName;
    private final short keySeq;
    private final String pkName;

    private JdbcPrimaryKey(String tableCat, String tableSchem, String tableName, String columnName, short keySeq,
                          String pkName){
        this.tableCat = tableCat;
        this.tableSchem = tableSchem;
        this.tableName = tableName;
        this.columnName = columnName;
        this.keySeq = keySeq;
        this.pkName = pkName;
    }

    public String getTableCat() {
        return tableCat;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public String getTableName() {
        return tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public short getKeySeq() {
        return keySeq;
    }

    public String getPkName() {
        return pkName;
    }

    public static JdbcPrimaryKey create(ResultSet resultSet) throws SQLException {
        String tableCat = resultSet.getString("TABLE_CAT");
        String tableSchem = resultSet.getString("TABLE_SCHEM");
        String tableName = resultSet.getString("TABLE_NAME");
        String columnName = resultSet.getString("COLUMN_NAME");
        short keySeq = resultSet.getShort("KEY_SEQ");
        String pkName = resultSet.getString("PK_NAME");

        return new JdbcPrimaryKey(tableCat, tableSchem, tableName, columnName, keySeq, pkName);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if(!(other instanceof JdbcPrimaryKey)){
            return false;
        }

        JdbcPrimaryKey other_ = (JdbcPrimaryKey) other;
        return Objects.equals(getTableCat(), other_.getTableCat()) &&
                Objects.equals(getTableSchem(), other_.getTableSchem()) &&
                Objects.equals(getTableName(), other_.getTableName()) &&
                Objects.equals(getColumnName(), other_.getColumnName()) &&
                Objects.equals(getKeySeq(), other_.getKeySeq()) &&
                Objects.equals(getPkName(), other_.getPkName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableCat, tableSchem, tableName, columnName, keySeq, pkName);
    }
}

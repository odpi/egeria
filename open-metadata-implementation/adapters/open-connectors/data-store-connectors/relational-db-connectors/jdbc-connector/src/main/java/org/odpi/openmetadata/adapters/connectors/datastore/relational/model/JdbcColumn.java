/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.relational.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcColumn {

    private final String tableCat;
    private final String tableSchem;
    private final String tableName;
    private final String columnName;
    private final int dataType;
    private final String typeName;
    private final int columnSize;
    private final int decimalDigits;
    private final int numPrecRadix;
    private final int nullable;
    private final String remarks;
    private final String columnDef;
    private final int charOctetLength;
    private final int ordinalPosition;
    private final String isNullable;
    private final String scopeCatalog;
    private final String scopeSchema;
    private final String scopeTable;
    private final short sourceDataType;
    private final String isAutoIncrement;
    private final String isGeneratedColumn;

    private JdbcColumn(String tableCat, String tableSchem, String tableName, String columnName, int dataType,
                      String typeName, int columnSize, int decimalDigits, int numPrecRadix, int nullable,
                      String remarks, String columnDef, int charOctetLength, int ordinalPosition, String isNullable,
                      String scopeCatalog, String scopeSchema, String scopeTable, short sourceDataType,
                      String isAutoIncrement, String isGeneratedColumn) {
        this.tableCat = tableCat;
        this.tableSchem = tableSchem;
        this.tableName = tableName;
        this.columnName = columnName;
        this.dataType = dataType;
        this.typeName = typeName;
        this.columnSize = columnSize;
        this.decimalDigits = decimalDigits;
        this.numPrecRadix = numPrecRadix;
        this.nullable = nullable;
        this.remarks = remarks;
        this.columnDef = columnDef;
        this.charOctetLength = charOctetLength;
        this.ordinalPosition = ordinalPosition;
        this.isNullable = isNullable;
        this.scopeCatalog = scopeCatalog;
        this.scopeSchema = scopeSchema;
        this.scopeTable = scopeTable;
        this.sourceDataType = sourceDataType;
        this.isAutoIncrement = isAutoIncrement;
        this.isGeneratedColumn = isGeneratedColumn;
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

    public int getDataType() {
        return dataType;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getColumnSize() {
        return columnSize;
    }

    public int getDecimalDigits() {
        return decimalDigits;
    }

    public int getNumPrecRadix() {
        return numPrecRadix;
    }

    public int getNullable() {
        return nullable;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getColumnDef() {
        return columnDef;
    }

    public int getCharOctetLength() {
        return charOctetLength;
    }

    public int getOrdinalPosition() {
        return ordinalPosition;
    }

    public String getIsNullable() {
        return isNullable;
    }

    public String getScopeCatalog() {
        return scopeCatalog;
    }

    public String getScopeSchema() {
        return scopeSchema;
    }

    public String getScopeTable() {
        return scopeTable;
    }

    public short getSourceDataType() {
        return sourceDataType;
    }

    public String getIsAutoIncrement() {
        return isAutoIncrement;
    }

    public String getIsGeneratedColumn() {
        return isGeneratedColumn;
    }

    public static JdbcColumn create(ResultSet resultSet) throws SQLException {
        String tableCat = resultSet.getString("TABLE_CAT");
        String tableSchem = resultSet.getString("TABLE_SCHEM");
        String tableName = resultSet.getString("TABLE_NAME");
        String columnName = resultSet.getString("COLUMN_NAME");
        int dataType = resultSet.getInt("DATA_TYPE");
        String typeName = resultSet.getString("TYPE_NAME");
        int columnSize = resultSet.getInt("COLUMN_SIZE");
        int decimalDigits = resultSet.getInt("DECIMAL_DIGITS");
        int numPrecRadix = resultSet.getInt("NUM_PREC_RADIX");
        int nullable = resultSet.getInt("NULLABLE");
        String remarks = resultSet.getString("REMARKS");
        String columnDef = resultSet.getString("COLUMN_DEF");
        int charOctetLength = resultSet.getInt("CHAR_OCTET_LENGTH");
        int ordinalPosition = resultSet.getInt("ORDINAL_POSITION");
        String isNullable = resultSet.getString("IS_NULLABLE");
        String scopeCatalog = resultSet.getString("SCOPE_CATALOG");
        String scopeSchema = resultSet.getString("SCOPE_SCHEMA");
        String scopeTable = resultSet.getString("SCOPE_TABLE");
        short sourceDataType = resultSet.getShort("SOURCE_DATA_TYPE");
        String isAutoIncrement = resultSet.getString("IS_AUTOINCREMENT");
        String isGeneratedColumn = resultSet.getString("IS_GENERATEDCOLUMN");

        return new JdbcColumn(tableCat, tableSchem, tableName, columnName, dataType, typeName, columnSize, decimalDigits,
                numPrecRadix, nullable, remarks, columnDef, charOctetLength, ordinalPosition, isNullable, scopeCatalog,
                scopeSchema, scopeTable, sourceDataType, isAutoIncrement, isGeneratedColumn);
    }

}

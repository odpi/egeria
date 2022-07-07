/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.relational.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcTable {

    private final String tableCat;
    private final String tableSchem;
    private final String tableName;
    private final String tableType;
    private final String remarks;
    private final String typeCat;
    private final String typeSchem;
    private final String typeName;
    private final String selfReferencingColName;
    private final String refGeneration;

    private JdbcTable(String tableCat, String tableSchem, String tableName, String tableType, String remarks, String typeCat,
                      String typeSchem, String typeName, String selfReferencingColName, String refGeneration){
        this.tableCat = tableCat;
        this.tableSchem = tableSchem;
        this. tableName = tableName;
        this.tableType = tableType;
        this.remarks = remarks;
        this.typeCat = typeCat;
        this.typeSchem = typeSchem;
        this.typeName = typeName;
        this.selfReferencingColName = selfReferencingColName;
        this.refGeneration = refGeneration;
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

    public String getTableType() {
        return tableType;
    }

    public String getRemarks() {
        return remarks;
    }

    public String getTypeCat() {
        return typeCat;
    }

    public String getTypeSchem() {
        return typeSchem;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSelfReferencingColName() {
        return selfReferencingColName;
    }

    public String getRefGeneration() {
        return refGeneration;
    }

    public static JdbcTable create(ResultSet resultSet) throws SQLException {
        String tableCat = resultSet.getString("TABLE_CAT");
        String tableSchem = resultSet.getString("TABLE_SCHEM");
        String tableName = resultSet.getString("TABLE_NAME");
        String tableType = resultSet.getString("TABLE_TYPE");
        String remarks = resultSet.getString("REMARKS");
        String typeCat = resultSet.getString("TYPE_CAT");
        String typeSchem = resultSet.getString("TYPE_SCHEM");
        String typeName = resultSet.getString("TYPE_NAME");
        String selfReferencingColName = resultSet.getString("SELF_REFERENCING_COL_NAME");
        String refGeneration = resultSet.getString("REF_GENERATION");


        return new JdbcTable(tableCat, tableSchem, tableName, tableType, remarks, typeCat, typeSchem, typeName,
                selfReferencingColName, refGeneration);
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.relational.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcSchema {

    private final String tableSchem;
    private final String tableCatalog;

    public JdbcSchema(String tableSchem, String tableCatalog){
        this.tableSchem = tableSchem;
        this.tableCatalog = tableCatalog;
    }

    public String getTableSchem() {
        return tableSchem;
    }

    public String getTableCatalog() {
        return tableCatalog;
    }

    public static JdbcSchema create(ResultSet resultSet) throws SQLException {
        String tableSchem = resultSet.getString("TABLE_SCHEM");
        String tableCat = resultSet.getString("TABLE_CATALOG");

        return new JdbcSchema(tableSchem, tableCat);
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.datastore.relational.model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcCatalog {

    private final String tableCat;

    public JdbcCatalog(String tableCat){
        this.tableCat = tableCat;
    }

    public String getTableCatalog() {
        return tableCat;
    }

    public static JdbcCatalog create(ResultSet resultSet) throws SQLException {
        String tableCat = resultSet.getString("TABLE_CAT");

        return new JdbcCatalog(tableCat);
    }

}

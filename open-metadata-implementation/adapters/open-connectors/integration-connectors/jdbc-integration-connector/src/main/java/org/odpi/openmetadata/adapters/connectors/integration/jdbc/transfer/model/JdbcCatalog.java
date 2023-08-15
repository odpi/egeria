/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model;


import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Represents a catalog as returned by the JDBC api. Fields are the ones described in {@link DatabaseMetaData}
 */
public class JdbcCatalog {

    private final String tableCat;

    private JdbcCatalog(String tableCat){
        this.tableCat = tableCat;
    }

    public String getTableCat() {
        return tableCat;
    }

    public static JdbcCatalog create(ResultSet resultSet) throws SQLException {
        String tableCat = resultSet.getString("TABLE_CAT");

        return new JdbcCatalog(tableCat);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if(!(other instanceof JdbcCatalog)){
            return false;
        }

        JdbcCatalog other_ = (JdbcCatalog) other;
        return Objects.equals(getTableCat(), other_.getTableCat());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(tableCat);
    }
}

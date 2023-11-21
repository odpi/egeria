/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.jdbc.transfer.model;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Represents a foreign key as returned by the JDBC api. Fields are the ones described in {@link DatabaseMetaData}
 */
public class JdbcForeignKey {

    private final String pkTableCat;
    private final String pkTableSchem;
    private final String pkTableName;
    private final String pkColumnName;
    private final String fkTableCat;
    private final String fkTableSchem;
    private final String fkTableName;
    private final String fkColumnName;
    private final short keySeq;
    private final String updateRule;
    private final String deleteRule;
    private final String fkName;
    private final String pkName;
    private final short deferrability;

    public JdbcForeignKey(String pkTableCat, String pkTableSchem, String pkTableName, String pkColumnName, String fkTableCat,
                          String fkTableSchem, String fkTableName, String fkColumnName, short keySeq, String updateRule,
                          String deleteRule, String fkName, String pkName, short deferrability) {
        this.pkTableCat = pkTableCat;
        this.pkTableSchem = pkTableSchem;
        this.pkTableName = pkTableName;
        this.pkColumnName = pkColumnName;
        this.fkTableCat = fkTableCat;
        this.fkTableSchem = fkTableSchem;
        this.fkTableName = fkTableName;
        this.fkColumnName = fkColumnName;
        this.keySeq = keySeq;
        this.updateRule = updateRule;
        this.deleteRule = deleteRule;
        this.fkName = fkName;
        this.pkName = pkName;
        this.deferrability = deferrability;
    }

    public String getPkTableCat() {
        return pkTableCat;
    }

    public String getPkTableSchem() {
        return pkTableSchem;
    }

    public String getPkTableName() {
        return pkTableName;
    }

    public String getPkColumnName() {
        return pkColumnName;
    }

    public String getFkTableCat() {
        return fkTableCat;
    }

    public String getFkTableSchem() {
        return fkTableSchem;
    }

    public String getFkTableName() {
        return fkTableName;
    }

    public String getFkColumnName() {
        return fkColumnName;
    }

    public short getKeySeq() {
        return keySeq;
    }

    public String getUpdateRule() {
        return updateRule;
    }

    public String getDeleteRule() {
        return deleteRule;
    }

    public String getFkName() {
        return fkName;
    }

    public String getPkName() {
        return pkName;
    }

    public short getDeferrability() {
        return deferrability;
    }

    public static JdbcForeignKey create(ResultSet resultSet) throws SQLException {
        String pkTableCat = resultSet.getString("PKTABLE_CAT");
        String pkTableSchem = resultSet.getString("PKTABLE_SCHEM");
        String pkTableName = resultSet.getString("PKTABLE_NAME");
        String pkColumnName = resultSet.getString("PKCOLUMN_NAME");
        String fkTableCat = resultSet.getString("FKTABLE_CAT");
        String fkTableSchem = resultSet.getString("FKTABLE_SCHEM");
        String fkTableName = resultSet.getString("FKTABLE_NAME");
        String fkColumnName = resultSet.getString("FKCOLUMN_NAME");
        short keySeq = resultSet.getShort("KEY_SEQ");
        String updateRule = resultSet.getString("UPDATE_RULE");
        String deleteRule = resultSet.getString("DELETE_RULE");
        String fkName = resultSet.getString("FK_NAME");
        String pkName = resultSet.getString("PK_NAME");
        short deferrability = resultSet.getShort("DEFERRABILITY");

        return new JdbcForeignKey(pkTableCat, pkTableSchem, pkTableName, pkColumnName, fkTableCat, fkTableSchem, fkTableName,
                fkColumnName, keySeq, updateRule, deleteRule, fkName, pkName, deferrability);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null) {
            return false;
        }
        if(!(other instanceof JdbcForeignKey)){
            return false;
        }

        JdbcForeignKey other_ = (JdbcForeignKey) other;
        return Objects.equals(getPkTableCat(), other_.getPkTableCat()) &&
                Objects.equals(getPkTableSchem(), other_.getPkTableSchem()) &&
                Objects.equals(getPkTableName(), other_.getPkTableName()) &&
                Objects.equals(getPkColumnName(), other_.getPkColumnName()) &&
                Objects.equals(getFkTableCat(), other_.getFkTableCat()) &&
                Objects.equals(getFkTableSchem(), other_.getFkTableSchem()) &&
                Objects.equals(getFkTableName(), other_.getFkTableName()) &&
                Objects.equals(getFkColumnName(), other_.getFkColumnName()) &&
                Objects.equals(getKeySeq(), other_.getKeySeq()) &&
                Objects.equals(getUpdateRule(), other_.getUpdateRule()) &&
                Objects.equals(getDeleteRule(), other_.getDeleteRule()) &&
                Objects.equals(getFkName(), other_.getFkName()) &&
                Objects.equals(getPkName(), other_.getPkName()) &&
                Objects.equals(getDeferrability(), other_.getDeferrability());
    }

    @Override
    public int hashCode() {
        return Objects.hash(pkTableCat, pkTableSchem, pkTableName, pkColumnName, fkTableCat, fkTableSchem, fkTableName,
                fkColumnName, keySeq, updateRule, deleteRule, fkName, pkName, deferrability);
    }
}

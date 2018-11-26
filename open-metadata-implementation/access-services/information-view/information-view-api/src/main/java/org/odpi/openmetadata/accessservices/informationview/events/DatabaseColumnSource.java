/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview.events;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DatabaseColumnSource extends Source {

    private String columnName;

    private TableSource tableSource = new TableSource();

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public TableSource getTableSource() {
        return tableSource;
    }

    public void setTableSource(TableSource tableSource) {
        this.tableSource = tableSource;
    }

    @Override
    public String getProtocol() {
        return tableSource != null ? tableSource.getProtocol() : super.getProtocol();
    }

    @Override
    public String getNetworkAddress() {
        return tableSource != null ? tableSource.getNetworkAddress() : super.getNetworkAddress();
    }

    @Override
    public String getConnectorProviderName() {
        return tableSource != null ? tableSource.getConnectorProviderName() : super.getConnectorProviderName();
    }

    @Override
    public Map<String, String> getAdditionalProperties() {
        return tableSource != null ? tableSource.getAdditionalProperties() : super.getAdditionalProperties();
    }

    @Override
    public String getUser() {
        return tableSource != null ? tableSource.getUser() : super.getUser();
    }

    @Override
    public String getQualifiedName() {
        String qualifiedName = getTableSource() != null ? this.getTableSource().getQualifiedName() + "." : "";
        return qualifiedName + this.getColumnName();
    }

    @Override
    public String toString() {
        return "ColumnSource{" +
                "columnName='" + columnName + '\'' +
                ", tableSource=" + tableSource +
                '}';
    }
}

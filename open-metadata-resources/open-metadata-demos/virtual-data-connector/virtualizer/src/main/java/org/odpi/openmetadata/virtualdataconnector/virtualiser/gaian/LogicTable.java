/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.virtualdataconnector.virtualiser.gaian;

import java.util.Map;

/**
 * LogicTable is a POJO which stores Logical Table information in Gaian
 */
public class LogicTable {
    private String logicalTableName;
    private String gaianNode;
    private Map<String, String> logicalTableDefinition;

    public Map<String, String> getLogicalTableDefinition() {
        return logicalTableDefinition;
    }

    public void setLogicalTableDefinition(Map<String, String> logicalTableDefinition) {
        this.logicalTableDefinition = logicalTableDefinition;
    }

    public String getGaianNode() {
        return gaianNode;
    }

    public void setGaianNode(String gaianNode) {
        this.gaianNode = gaianNode;
    }

    public String getLogicalTableName() {
        return logicalTableName;
    }

    public void setLogicalTableName(String logicalTableName) {
        this.logicalTableName = logicalTableName;
    }

    @Override
    public String toString() {
        return "LogicTable{" +
                "logicalTableName='" + logicalTableName + '\'' +
                ", gaianNode='" + gaianNode + '\'' +
                ", logicalTableDefinition=" + logicalTableDefinition +
                '}';
    }
}

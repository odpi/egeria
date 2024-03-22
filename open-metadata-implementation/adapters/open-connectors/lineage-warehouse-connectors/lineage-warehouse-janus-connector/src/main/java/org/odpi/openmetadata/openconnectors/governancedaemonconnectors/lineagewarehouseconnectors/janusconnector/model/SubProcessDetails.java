/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model;

import org.apache.tinkerpop.gremlin.structure.Vertex;

public class SubProcessDetails {
    private Vertex columnIn;
    private Vertex columnOut;
    private Vertex process;
    private String processGuid;
    private String columnInGuid;
    private String columnOutGuid;
    private String processName;

    public Vertex getColumnIn() {
        return columnIn;
    }

    public void setColumnIn(Vertex columnIn) {
        this.columnIn = columnIn;
    }

    public Vertex getColumnOut() {
        return columnOut;
    }

    public void setColumnOut(Vertex columnOut) {
        this.columnOut = columnOut;
    }

    public Vertex getProcess() {
        return process;
    }

    public void setProcess(Vertex process) {
        this.process = process;
    }

    public String getProcessGuid() {
        return processGuid;
    }

    public void setProcessGuid(String processGuid) {
        this.processGuid = processGuid;
    }

    public String getColumnInGuid() {
        return columnInGuid;
    }

    public void setColumnInGuid(String columnInGuid) {
        this.columnInGuid = columnInGuid;
    }

    public String getColumnOutGuid() {
        return columnOutGuid;
    }

    public void setColumnOutGuid(String columnOutGuid) {
        this.columnOutGuid = columnOutGuid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy.model;

import org.odpi.openmetadata.accessservices.dataengine.model.Process;

public class DataEngineProcess {

    private Process process;
    private String userId;

    public DataEngineProcess(Process process, String userId) {
        this.process = process;
        this.userId = userId;
    }

    public Process getProcess() { return process; }
    public void setProcess(Process process) { this.process = process; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

}

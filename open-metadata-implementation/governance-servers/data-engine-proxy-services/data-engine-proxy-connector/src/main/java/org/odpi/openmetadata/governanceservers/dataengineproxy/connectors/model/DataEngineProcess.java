/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.dataengineproxy.connectors.model;

import org.odpi.openmetadata.accessservices.dataengine.model.Process;

/**
 * Wrapper for the Data Engine OMAS's Process object, to also be able to include the userId information.
 */
public class DataEngineProcess {

    private Process process;
    private String userId;

    /**
     * Default constructor
     *
     * @param process the Process to be maintained
     * @param userId  the user maintaining the Process
     */
    public DataEngineProcess(Process process, String userId) {
        this.process = process;
        this.userId = userId;
    }

    /**
     * Retrieve the Process being maintained.
     *
     * @return Process
     */
    public Process getProcess() { return process; }

    /**
     * Set the Process to be maintained.
     *
     * @param process the Process to be maintained
     */
    public void setProcess(Process process) { this.process = process; }

    /**
     * Retrieve the user maintaining the Process.
     *
     * @return String
     */
    public String getUserId() { return userId; }

    /**
     * Set the user maintaining the Process.
     *
     * @param userId the user maintaining the Process
     */
    public void setUserId(String userId) { this.userId = userId; }

}

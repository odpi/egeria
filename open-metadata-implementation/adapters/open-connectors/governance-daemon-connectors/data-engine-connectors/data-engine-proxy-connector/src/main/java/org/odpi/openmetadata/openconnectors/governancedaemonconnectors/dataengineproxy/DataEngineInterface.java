/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;

import java.util.List;

public interface DataEngineInterface {

    /**
     * Send the provided process to the Data Engine OMAS.
     *
     * @param process
     */
    void sendProcess(Process process);

    /**
     * Send the provided list of LineageMappings to the Data Engine OMAS.
     *
     * @param lineageMappingList
     */
    void sendLineageMappings(List<LineageMapping> lineageMappingList);

}

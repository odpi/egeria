/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.dataengineproxy;

import org.odpi.openmetadata.accessservices.dataengine.model.LineageMapping;
import org.odpi.openmetadata.accessservices.dataengine.model.Process;
import org.odpi.openmetadata.accessservices.dataengine.model.SoftwareServerCapability;

import java.util.List;

public interface DataEngineInterface {

    /**
     * Register the provided SoftwareServiceCapbility as a Data Engine.
     *
     * @param dataEngine
     * @param userId
     * @return String - the GUID of the registered Data Engine
     */
    String registerDataEngine(SoftwareServerCapability dataEngine, String userId);

    /**
     * Send the provided process to the Data Engine OMAS.
     *
     * @param process
     * @param userId
     * @return String - the GUID of the process that was created
     */
    String sendProcess(Process process, String userId);

    /**
     * Send the provided list of LineageMappings to the Data Engine OMAS.
     *
     * @param lineageMappingList
     * @param userId
     */
    void sendLineageMappings(List<LineageMapping> lineageMappingList, String userId);

}

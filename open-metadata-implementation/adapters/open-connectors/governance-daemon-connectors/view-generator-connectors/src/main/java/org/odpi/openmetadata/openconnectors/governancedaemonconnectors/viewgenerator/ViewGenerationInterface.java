/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator;

import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;
import org.odpi.openmetadata.openconnectors.governancedaemonconnectors.viewgenerator.model.LogicTable;

import java.util.List;
import java.util.Map;

public interface ViewGenerationInterface {

   boolean deleteLogicalTable(String tableName);

   List<LogicTable> getAllLogicTables();

    /**
     * Process the serialized  information view event
     *
     * @param tableContextEvent event
     * @return the table sent to Gaian
     */
    Map<String, String> processInformationViewEvent(TableContextEvent tableContextEvent);

}

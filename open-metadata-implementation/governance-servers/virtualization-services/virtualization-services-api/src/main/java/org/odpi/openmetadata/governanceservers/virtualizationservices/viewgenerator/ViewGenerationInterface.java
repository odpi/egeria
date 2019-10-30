/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
/**
 * This is the interface for the generic operations on data virtualization solutions
 */
package org.odpi.openmetadata.governanceservers.virtualizationservices.viewgenerator;

import org.odpi.openmetadata.accessservices.informationview.events.TableContextEvent;

import java.util.Map;

public interface ViewGenerationInterface {

    /**
     * Process the serialized  information view event
     *
     * @param tableContextEvent event
     * @return the table of created views for generating the kafka events
     */
    Map<String, String> processInformationViewEvent(TableContextEvent tableContextEvent);

}

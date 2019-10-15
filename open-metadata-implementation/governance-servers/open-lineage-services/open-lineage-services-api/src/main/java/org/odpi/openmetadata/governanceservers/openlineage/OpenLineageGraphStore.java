/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;

public interface OpenLineageGraphStore {

    /**
     * Process the serialized  information view event
     *
     * @param processLineageEvent event
     * @return the table of created views for generating the kafka events
     */
    void addEntity(ProcessLineageEvent processLineageEvent);
}

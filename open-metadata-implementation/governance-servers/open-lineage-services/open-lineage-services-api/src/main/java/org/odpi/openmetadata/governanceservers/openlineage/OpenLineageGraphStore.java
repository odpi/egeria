/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;

public interface OpenLineageGraphStore {

    /**
     * Process the serialized  information view event
     *
     * @param processLineageEvent event
     */
    void addEntity(ProcessLineageEvent processLineageEvent);
}

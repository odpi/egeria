/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.ProcessLineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphStoringServices {

    private static final Logger log = LoggerFactory.getLogger(GraphStoringServices.class);

    private OpenLineageGraphStore openLineageGraphStore;

    public GraphStoringServices(OpenLineageGraphStore graphStore) {
        this.openLineageGraphStore = graphStore;
    }

    public void test(ProcessLineageEvent processLineageEvent){
        openLineageGraphStore.addEntity(processLineageEvent);
    }
}

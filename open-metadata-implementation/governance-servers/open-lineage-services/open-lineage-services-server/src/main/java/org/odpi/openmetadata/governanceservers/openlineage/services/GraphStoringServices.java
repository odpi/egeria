/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.BufferGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphStoringServices {

    private static final Logger log = LoggerFactory.getLogger(GraphStoringServices.class);

    private BufferGraphStore bufferGraphStore;
    private JobConfiguration jobConfiguration;

    public GraphStoringServices(BufferGraphStore graphStore) {
        this.bufferGraphStore = graphStore;
        this.jobConfiguration = new JobConfiguration(graphStore);

    }

    public void addEntity(LineageEvent lineageEvent){
        bufferGraphStore.addEntity(lineageEvent);
    }

}

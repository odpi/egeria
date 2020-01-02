/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraph;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoringServices {

    private static final Logger log = LoggerFactory.getLogger(StoringServices.class);

    private BufferGraph bufferGraph;
    private JobConfiguration jobConfiguration;

    public StoringServices(BufferGraph graphStore) {
        this.bufferGraph = graphStore;
        this.jobConfiguration = new JobConfiguration(graphStore);

    }

    public void addEntity(LineageEvent lineageEvent){
        bufferGraph.addEntity(lineageEvent);
    }

    public void updateEntity(LineageEvent lineageEvent){}

    public void deleteEntity(LineageEvent lineageEvent){


        bufferGraph.deleteEntity(lineageEvent.getLineageEntity().getGuid());
    }
}

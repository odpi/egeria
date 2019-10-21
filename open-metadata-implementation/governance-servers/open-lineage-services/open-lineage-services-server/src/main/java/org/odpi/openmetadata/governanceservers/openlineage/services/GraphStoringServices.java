/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.event.LineageEvent;
import org.odpi.openmetadata.governanceservers.openlineage.OpenLineageGraphStore;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GraphStoringServices {

    private static final Logger log = LoggerFactory.getLogger(GraphStoringServices.class);

    private OpenLineageGraphStore openLineageGraphStore;
    private JobConfiguration jobConfiguration;

    public GraphStoringServices(OpenLineageGraphStore graphStore) {
        this.openLineageGraphStore = graphStore;
        this.jobConfiguration = new JobConfiguration(graphStore);

    }

    public void addEntity(LineageEvent lineageEvent){
        openLineageGraphStore.addEntity(lineageEvent);
    }

//    public void test(LineageEvent lineageEvent){
//        Set<GraphContext> verticesToBeAdded = new HashSet<>();
//        lineageEvent.getAssetContext().entrySet().stream().forEach(entry ->
//                {
//                    if(entry.getValue().size()>1){
//                        verticesToBeAdded.addAll(entry.getValue());
//                    }else {
//                        verticesToBeAdded.add(entry.getValue().stream().findFirst().get());
//                    }
//                }
//        );
//
//        System.out.println(verticesToBeAdded.size());
//        for( GraphContext entry: verticesToBeAdded){
//
//            Object one = openLineageGraphStore.test(entry.getFromVertex());
//
//        }
//    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.services;

import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.governanceservers.openlineage.buffergraph.BufferGraph;
import org.odpi.openmetadata.governanceservers.openlineage.scheduler.JobConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sound.sampled.Line;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collector;

public class StoringServices {

    private static final Logger log = LoggerFactory.getLogger(StoringServices.class);

    private BufferGraph bufferGraph;
    private JobConfiguration jobConfiguration;

    public StoringServices(BufferGraph graphStore) {
        this.bufferGraph = graphStore;
        this.jobConfiguration = new JobConfiguration(graphStore);

    }

    public void addEntity(LineageEvent lineageEvent) {
        Set<GraphContext> verticesToBeAdded = new HashSet<>();
        lineageEvent.getAssetContext().entrySet().stream().forEach(entry ->
                {
                    if(entry.getValue().size()>1){
                        verticesToBeAdded.addAll(entry.getValue());
                    }else {
                        verticesToBeAdded.add(entry.getValue().stream().findFirst().get());
                    }
                }
        );

        Set<LineageEntity> vertices = new HashSet<>();
                lineageEvent.getAssetContext().entrySet().stream()

                .forEach(enrty -> {
                    if(enrty.getValue().size()>1){
                        enrty.getValue().stream().forEach(nestedEntry ->{
                            vertices.add(nestedEntry.getFromVertex());
                            vertices.add(nestedEntry.getToVertex());

                        });
                    }
                    else{
                       Optional<GraphContext> graphContext =  enrty.getValue().stream().findFirst();
                       if (graphContext.isPresent()){
                           vertices.add(graphContext.get().getFromVertex());
                           vertices.add(graphContext.get().getToVertex());
                       }
                    }
                });



//        if(lineageEntity.getTypeDefName() == null || lineageEntity.getGuid() ==null){
//            JanusConnectorErrorCode errorCode = JanusConnectorErrorCode.ENTITY_PROPERTIES_ERROR;
//            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(lineageEntity.getGuid(),
//                    methodName,
//                    this.getClass().getName());
//
//            throw new JanusConnectorException(
//                    this.getClass().getName(),
//                    methodName,
//                    errorMessage,
//                    errorCode.getSystemAction(),
//                    errorCode.getUserAction());
//
//        }
        //TODO add check gia duid kai typedef name

                vertices.stream().forEach(lineageEntity -> bufferGraph.addEntity(lineageEntity));
//        bufferGraph.addEntity(verticesToBeAdded);
    }

    public void updateEntity(LineageEvent lineageEvent){}

}

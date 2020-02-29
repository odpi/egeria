/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.accessservices.assetlineage.util.SuperTypesRetriever;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * AssetLineagePublisher is the connector responsible for publishing lineage context information about
 * new and changed assets.
 */
public class AssetLineagePublisher {

    private static final Logger log = LoggerFactory.getLogger(AssetLineagePublisher.class);
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private OpenMetadataTopicConnector outTopicConnector;
    private String serverUserName;
    private SuperTypesRetriever superTypesRetriever;
    private ProcessContextHandler processContextHandler;
    private ClassificationHandler classificationHandler;
    private AssetContextHandler assetContextHandler;
    private GlossaryHandler glossaryHandler;

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param serverName        name of the user of the server instance
     * @param serverUserName    name of this server instance
     * @param repositoryHelper
     * @param outTopicConnector connection to the out topic
     */
    public AssetLineagePublisher(String serverName, String serverUserName, OMRSRepositoryHelper repositoryHelper, OpenMetadataTopicConnector outTopicConnector)
            throws OCFCheckedExceptionBase {
        String methodName = "AssetLineagePublisher";
        this.outTopicConnector = outTopicConnector;
        this.serverUserName = serverUserName;
        this.superTypesRetriever = new SuperTypesRetriever(repositoryHelper);
        this.processContextHandler = instanceHandler.getProcessHandler(serverUserName, serverName, methodName);
        this.classificationHandler = instanceHandler.getClassificationHandler(serverUserName, serverName, methodName);
        this.assetContextHandler = instanceHandler.getAssetContextHandler(serverUserName, serverName, methodName);
        this.glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, methodName);
    }

    /**
     * Takes the context for a Process and publishes the event to the Cohort
     *
     * @param entityDetail entity to get context
     */
    public void publishProcessEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        Map<String, Set<GraphContext>> processContext = processContextHandler.getProcessContext(serverUserName, entityDetail.getGUID());
        LineageEvent event = new LineageEvent();
        event.setAssetContext(processContext);
        event.setAssetLineageEventType(AssetLineageEventType.PROCESS_CONTEXT_EVENT);
        publishEvent(event);
    }

    public void publishAssetEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        String technicalGuid = entityDetail.getGUID();
        AssetContext assetContext = this.assetContextHandler.getAssetContext(serverUserName, technicalGuid, entityDetail.getType().getTypeDefName());
        Map<String, Set<GraphContext>> context = this.glossaryHandler.getGlossaryTerm(technicalGuid, serverUserName, assetContext, this.superTypesRetriever);
        LineageEvent event = new LineageEvent();
        if (!context.isEmpty())
            event.setAssetContext(context);
        else
            event.setAssetContext(assetContext.getNeighbors());
        event.setAssetLineageEventType(AssetLineageEventType.TECHNICAL_ELEMENT_CONTEXT_EVENT);
        publishEvent(event);
    }

    public void publishClassificationEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        Map<String, Set<GraphContext>> classificationContext = this.classificationHandler.buildClassificationEvent(entityDetail);
        if (MapUtils.isEmpty(classificationContext)) {
            log.debug("No valid lineage classifications were found for the entity {} ", entityDetail.getGUID());
            return;
        }
        LineageEvent event = new LineageEvent();
        event.setAssetContext(classificationContext);
        event.setAssetLineageEventType(AssetLineageEventType.CLASSIFICATION_CONTEXT_EVENT);
        publishEvent(event);
    }


    /**
     * Output a new asset event.
     *
     * @param event event to send
     */
    public void publishEvent(AssetLineageEventHeader event) throws JsonProcessingException, ConnectorCheckedException {
        if (outTopicConnector == null)
            return;

        ObjectMapper objectMapper = new ObjectMapper();
        outTopicConnector.sendEvent(objectMapper.writeValueAsString(event));
        log.debug("Asset Lineage OMAS has published an event of type {} ", event.getAssetLineageEventType());
    }
}


/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.outtopic;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventHeader;
import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageEvent;
import org.odpi.openmetadata.accessservices.assetlineage.event.LineageRelationshipEvent;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ClassificationHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.GlossaryContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.ProcessContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageRelationship;
import org.odpi.openmetadata.accessservices.assetlineage.server.AssetLineageInstanceHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.openmetadatatopic.OpenMetadataTopicConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
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
    private ProcessContextHandler processContextHandler;
    private ClassificationHandler classificationHandler;
    private GlossaryContextHandler glossaryHandler;

    /**
     * The constructor is given the connection to the out topic for Asset Lineage OMAS
     * along with classes for testing and manipulating instances.
     *
     * @param outTopicConnector connection to the out topic
     * @param serverName        name of the user of the server instance
     * @param serverUserName    name of this server instance
     */
    public AssetLineagePublisher(OpenMetadataTopicConnector outTopicConnector,
                                 String serverName, String serverUserName)
            throws OCFCheckedExceptionBase {
        String methodName = "AssetLineagePublisher";
        this.outTopicConnector = outTopicConnector;
        this.serverUserName = serverUserName;
        this.processContextHandler = instanceHandler.getProcessHandler(serverUserName, serverName, methodName);
        this.classificationHandler = instanceHandler.getClassificationHandler(serverUserName, serverName, methodName);
        this.glossaryHandler = instanceHandler.getGlossaryHandler(serverUserName, serverName, methodName);
    }

    /**
     * Takes the context for a Process and publishes the event to the Cohort
     *
     * @param entityDetail entity to get context
     */
    public Map<String, Set<GraphContext>> publishProcessContext(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        Map<String, Set<GraphContext>> processContext = processContextHandler.getProcessContext(serverUserName, entityDetail);

        if (MapUtils.isEmpty(processContext)) {
            log.debug("No context was found for the entity {} ", entityDetail.getGUID());
            return Collections.emptyMap();
        }

        publishLineageEvent(processContext, AssetLineageEventType.PROCESS_CONTEXT_EVENT);
        return processContext;
    }

    public void publishGlossaryContext(String glossaryTermGUID) throws OCFCheckedExceptionBase, JsonProcessingException {
       EntityDetail entityDetail =  glossaryHandler.getGlossaryTermDetails(serverUserName, glossaryTermGUID);
       publishGlossaryContext(entityDetail);
    }

    public  Map<String, Set<GraphContext>> publishGlossaryContext(EntityDetail entityDetail) throws OCFCheckedExceptionBase, JsonProcessingException {
        Map<String, Set<GraphContext>> context = glossaryHandler.buildGlossaryTermContext(serverUserName, entityDetail);

        if (MapUtils.isEmpty(context)) {
            log.debug("No context were found for the entity {} ", entityDetail.getGUID());
            return Collections.emptyMap();
        }

        publishLineageEvent(context, AssetLineageEventType.GLOSSARY_TERM_CONTEXT_EVENT);
        return context;
    }

    public void publishClassificationContext(EntityDetail entityDetail, AssetLineageEventType assetLineageEventType)
            throws OCFCheckedExceptionBase, JsonProcessingException {
        Map<String, Set<GraphContext>> classificationContext = classificationHandler.buildClassificationContext(entityDetail);

        if (MapUtils.isEmpty(classificationContext)) {
            log.debug("No lineage classifications were found for the entity {} ", entityDetail.getGUID());
            return;
        }

        publishLineageEvent(classificationContext, assetLineageEventType);
    }


    /**
     * Publishes a {@link LineageRelationshipEvent} containing a {@link LineageRelationship}
     *
     * @param lineageRelationship the LineageRelationship to be published
     * @param eventType           the type on the event
     * @throws ConnectorCheckedException unable to send the event due to connectivity issue
     * @throws JsonProcessingException   exception parsing the event json
     */
    public void publishLineageRelationshipEvent(LineageRelationship lineageRelationship,
                                                AssetLineageEventType eventType) throws ConnectorCheckedException, JsonProcessingException {
        LineageRelationshipEvent event = new LineageRelationshipEvent();

        event.setLineageRelationship(lineageRelationship);
        event.setAssetLineageEventType(eventType);

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

    private void publishLineageEvent(Map<String, Set<GraphContext>> context,
                                     AssetLineageEventType processContextEvent) throws JsonProcessingException, ConnectorCheckedException {
        LineageEvent event = new LineageEvent();

        event.setAssetContext(context);
        event.setAssetLineageEventType(processContextEvent);

        publishEvent(event);
    }
}


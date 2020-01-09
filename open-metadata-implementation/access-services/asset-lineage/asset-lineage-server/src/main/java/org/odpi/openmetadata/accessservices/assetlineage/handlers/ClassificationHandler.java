/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.*;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetContextHandler.class);

    private String serviceName;
    private String serverName;
    private RepositoryHandler repositoryHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private InvalidParameterHandler invalidParameterHandler;
    private CommonHandler commonHandler;
    private AssetContext graph;


    /**
     * Instantiates a new Classification handler.
     *
     * @param serviceName             the service name
     * @param serverName              the server name
     * @param invalidParameterHandler the invalid parameter handler
     * @param repositoryHelper        the repository helper
     * @param repositoryHandler       the repository handler
     */
    public ClassificationHandler(String serviceName,
                                 String serverName,
                                 InvalidParameterHandler invalidParameterHandler,
                                 OMRSRepositoryHelper repositoryHelper,
                                 RepositoryHandler repositoryHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.repositoryHandler = repositoryHandler;
        this.repositoryHelper = repositoryHelper;
        this.invalidParameterHandler = invalidParameterHandler;
        this.commonHandler = new CommonHandler(serviceName, serverName, invalidParameterHandler, repositoryHelper, repositoryHandler);
    }


    /**
     * Gets asset context from the entity by classification type.
     *
     * @param serverName         the server name
     * @param userId             the user id
     * @param entityDetail       the entity for retrieving the classifications attached to it
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> getAssetContextByClassification(String serverName, String userId, EntityDetail entityDetail) {



        if (! entityDetail.getClassifications().isEmpty() && checkLineageClassificationTypes(entityDetail)) {

            graph = new AssetContext();

            String methodName = "getAssetContextByClassification";

            try {

                invalidParameterHandler.validateUserId(userId, methodName);
                invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);

                buildGraphContextByClassificationType(entityDetail, graph);

                return graph.getNeighbors();

            } catch (InvalidParameterException e) {
                throw new AssetLineageException(e.getReportedHTTPCode(),
                        e.getReportingClassName(),
                        e.getReportingActionDescription(),
                        e.getErrorMessage(),
                        e.getReportedSystemAction(),
                        e.getReportedUserAction()); }
        } else {

            log.info("No valid lineage classification found from entity {} ", entityDetail.getGUID());
            return null;
        }
    }

        private boolean checkLineageClassificationTypes (EntityDetail entityDetail){
            for (String classificationType : immutableQualifiedLineageClassifications) {
                if (entityDetail.getClassifications().stream().anyMatch(classification -> classification.getName().equals(classificationType))) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Build graph edge by classification type list.
         *
         * @param classifiedEntity        the start entity
         * @param graph              the graph
         * @return the list
         */
        private List<LineageEntity> buildGraphContextByClassificationType (EntityDetail classifiedEntity,
                                                                           AssetContext graph){

            List<LineageEntity> classificationEntities = new ArrayList<>();

            if (classifiedEntity.getStatus() == InstanceStatus.ACTIVE) {
                for (Classification classification : classifiedEntity.getClassifications()) {
                    if (immutableQualifiedLineageClassifications.contains(classification.getName())) {

                        LineageEntity lineageClassificationEntity = new LineageEntity();
                        lineageClassificationEntity.setGuid(classifiedEntity.getGUID());

                        mapClassificationsToLineageEntity(classification, lineageClassificationEntity);
                        classificationEntities.add(lineageClassificationEntity);
                        log.debug("Adding Classification {} ", classification.toString());
                    }
                }

                Converter converter = new Converter();
                LineageEntity startVertex = converter.createLineageEntity(classifiedEntity);

                for (LineageEntity endVertex : classificationEntities) {

                    graph.addVertex(startVertex);
                    graph.addVertex(endVertex);

                    /* Set the edge guid same as the classified entity guid */
                    GraphContext edge = new GraphContext(endVertex.getTypeDefName(), startVertex.getGuid(), startVertex, endVertex);
                    graph.addEdge(edge);

                }
                return classificationEntities;
            }
            return null;
        }

        private void mapClassificationsToLineageEntity (Classification classification, LineageEntity lineageEntity){

            final String methodName = "mapClassificationsToLineageEntity";

            Converter converter = new Converter();

            try {
                lineageEntity.setVersion(classification.getVersion());
                lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
                lineageEntity.setCreatedBy(classification.getCreatedBy());
                lineageEntity.setUpdatedBy(classification.getUpdatedBy());
                lineageEntity.setCreateTime(classification.getCreateTime());
                lineageEntity.setUpdateTime(classification.getUpdateTime());
                lineageEntity.setProperties(converter.getMapProperties(classification.getProperties()));

                log.debug("Classfication mapping for lineage entity {}: ", lineageEntity);

            } catch (Throwable exc) {

                AssetLineageErrorCode errorCode = AssetLineageErrorCode.CLASSIFICATION_MAPPING_ERROR;
                String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(classification.getName(), methodName,
                        this.getClass().getName());

                log.error("Caught exception from classification mapper {}", errorMessage);
            }

        }
    }

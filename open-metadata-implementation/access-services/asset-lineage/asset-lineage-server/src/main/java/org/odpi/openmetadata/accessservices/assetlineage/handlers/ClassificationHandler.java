/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode;
import org.odpi.openmetadata.accessservices.assetlineage.ffdc.exception.AssetLineageException;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.ffdc.AssetLineageErrorCode.ENTITY_NOT_FOUND;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.immutableQualifiedLineageClassifications;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetContextHandler.class);

    private InvalidParameterHandler invalidParameterHandler;
    private AssetContext graph;


    /**
     * Instantiates a new Classification handler.
     *
     * @param invalidParameterHandler the invalid parameter handler
     */
    public ClassificationHandler(InvalidParameterHandler invalidParameterHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
    }


    /**
     * Gets asset context from the entity by classification type.
     *
     * @param userId       the user id
     * @param entityDetail the entity for retrieving the classifications attached to it
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> getAssetContextByClassification(String userId, EntityDetail entityDetail) throws OCFCheckedExceptionBase {

        if (!CollectionUtils.isEmpty(entityDetail.getClassifications()) && checkLineageClassificationTypes(entityDetail)) {

            graph = new AssetContext();
            String methodName = "getAssetContextByClassification";
            invalidParameterHandler.validateUserId(userId, methodName);
            invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
            buildGraphContextByClassificationType(entityDetail, graph);
            return graph.getNeighbors();
        } else {
            log.info("No valid lineage classification found from entity {} ", entityDetail.getGUID());
            return null;
        }
    }

    private boolean checkLineageClassificationTypes(EntityDetail entityDetail) {
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
     * @param classifiedEntity the start entity
     * @param graph            the graph
     * @return the list
     */
    private List<LineageEntity> buildGraphContextByClassificationType(EntityDetail classifiedEntity,  AssetContext graph) throws OCFCheckedExceptionBase {

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

    private void mapClassificationsToLineageEntity(Classification classification, LineageEntity lineageEntity) throws OCFCheckedExceptionBase {

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

            log.debug("Classification mapping for lineage entity {}: ", lineageEntity);

        } catch (Throwable e) { //TODO This is basically catching nullpointers, so check for null instead

            AssetLineageErrorCode errorCode = AssetLineageErrorCode.CLASSIFICATION_MAPPING_ERROR;
            String formattedErrorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(classification.getName());
            throw new AssetLineageException(errorCode.getHTTPErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    formattedErrorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }

    }
}

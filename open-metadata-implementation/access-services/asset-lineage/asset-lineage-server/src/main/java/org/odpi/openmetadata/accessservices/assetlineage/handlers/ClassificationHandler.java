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

import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.GUID_PARAMETER;
import static org.odpi.openmetadata.accessservices.assetlineage.util.Constants.immutableQualifiedLineageClassifications;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private static final Logger log = LoggerFactory.getLogger(AssetContextHandler.class);

    private InvalidParameterHandler invalidParameterHandler;


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
     * @param entityDetail the entity for retrieving the classifications attached to it
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> buildClassificationEvent(EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        String methodName = "buildClassificationEvent";
        AssetContext assetContext = new AssetContext();
        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
        buildGraphContext(entityDetail, assetContext);
        return assetContext.getNeighbors();
    }

    /**
     * Build graph context
     *
     * @param entityDetail the start entity
     * @param graph            the graph
     * @return the list
     */
    private void buildGraphContext(EntityDetail entityDetail, AssetContext graph) throws OCFCheckedExceptionBase {
        List<LineageEntity> classificationVertices = new ArrayList<>();

        if (CollectionUtils.isEmpty(entityDetail.getClassifications()))
            return;

        for (Classification classification : entityDetail.getClassifications()) {
            if (immutableQualifiedLineageClassifications.contains(classification.getName())) {
                LineageEntity originalEntityVertex = new LineageEntity();
                originalEntityVertex.setGuid(entityDetail.getGUID());
                copyClassificationProperties(originalEntityVertex, classification);
                classificationVertices.add(originalEntityVertex);
            }
        }

        Converter converter = new Converter();
        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);

        for (LineageEntity classificationVertex : classificationVertices) {
            graph.addVertex(classificationVertex);
            GraphContext graphContext = new GraphContext(classificationVertex.getTypeDefName(), originalEntityVertex.getGuid(), originalEntityVertex, classificationVertex);
            graph.addGraphContext(graphContext);
        }
    }

    private void copyClassificationProperties(LineageEntity lineageEntity, Classification classification) throws OCFCheckedExceptionBase {
        final String methodName = "copyClassificationProperties";
        Converter converter = new Converter();

        try {
            lineageEntity.setVersion(classification.getVersion());
            lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
            lineageEntity.setCreatedBy(classification.getCreatedBy());
            lineageEntity.setUpdatedBy(classification.getUpdatedBy());
            lineageEntity.setCreateTime(classification.getCreateTime());
            lineageEntity.setUpdateTime(classification.getUpdateTime());
            lineageEntity.setProperties(converter.getMapProperties(classification.getProperties()));
        } catch (Exception e) { //TODO This is basically catching nullpointers, so check for null instead

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

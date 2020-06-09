/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.CLASSIFICATION;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private InvalidParameterHandler invalidParameterHandler;
    private OMRSRepositoryHelper repositoryHelper;
    private List<String> lineageClassificationTypes;

    /**
     * Instantiates a new Classification handler.
     *
     * @param invalidParameterHandler the invalid parameter handler
     */
    public ClassificationHandler(InvalidParameterHandler invalidParameterHandler, List<String> lineageClassificationTypes, OMRSRepositoryHelper repositoryHelper) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.lineageClassificationTypes = lineageClassificationTypes;
        this.repositoryHelper = repositoryHelper;
    }


    /**
     * Gets asset context from the entity by classification type.
     *
     * @param entityDetail the entity for retrieving the classifications attached to it
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> buildClassificationContext(EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        String methodName = "buildClassificationContext";
        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);

        List<Classification> classifications = filterLineageClassifications(entityDetail.getClassifications());
        if (CollectionUtils.isEmpty(classifications)) {
            return null;
        }

        Converter converter = new Converter(repositoryHelper);
        AssetContext assetContext = new AssetContext();

        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);
        assetContext.addVertex(originalEntityVertex);
        String entityGUID = entityDetail.getGUID();

        addClassificationsToGraphContext(classifications, originalEntityVertex, assetContext, entityGUID);

        return assetContext.getNeighbors();
    }

    private void addClassificationsToGraphContext(List<Classification> classifications, LineageEntity originalEntityVertex, AssetContext assetContext, String entityGUID) {

        for (Classification classification : classifications) {
            LineageEntity classificationVertex = getClassificationVertex(classification, entityGUID);
            assetContext.addVertex(classificationVertex);
            GraphContext graphContext = new GraphContext(CLASSIFICATION, classificationVertex.getGuid(),
                    originalEntityVertex, classificationVertex);
            assetContext.addGraphContext(graphContext);
        }

    }

    private List<Classification> filterLineageClassifications(List<Classification> classifications) {
        return classifications.stream().filter(classification
                -> lineageClassificationTypes.contains(classification.getType().getTypeDefName())).collect(Collectors.toList());
    }

    private LineageEntity getClassificationVertex(Classification classification, String entityGUID) {
        LineageEntity classificationVertex = new LineageEntity();

        String classificationGUID = classification.getName() + entityGUID;
        classificationVertex.setGuid(classificationGUID);
        copyClassificationProperties(classificationVertex, classification);

        return classificationVertex;
    }

    private void copyClassificationProperties(LineageEntity lineageEntity, Classification classification) {
        lineageEntity.setVersion(classification.getVersion());
        lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
        lineageEntity.setCreatedBy(classification.getCreatedBy());
        lineageEntity.setUpdatedBy(classification.getUpdatedBy());
        lineageEntity.setCreateTime(classification.getCreateTime());
        lineageEntity.setUpdateTime(classification.getUpdateTime());

        Converter converter = new Converter(repositoryHelper);
        lineageEntity.setProperties(converter.instancePropertiesToMap(classification.getProperties()));
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.LineageEntity;
import org.odpi.openmetadata.accessservices.assetlineage.util.Converter;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private InvalidParameterHandler invalidParameterHandler;
    private List<String> lineageClassificationTypes;

    /**
     * Instantiates a new Classification handler.
     *
     * @param invalidParameterHandler the invalid parameter handler
     */
    public ClassificationHandler(InvalidParameterHandler invalidParameterHandler, List<String> lineageClassificationTypes) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.lineageClassificationTypes = lineageClassificationTypes;
    }


    /**
     * Gets asset context from the entity by classification type.
     *
     * @param entityDetail the entity for retrieving the classifications attached to it
     * @return the asset context by classification
     */
    public Map<String, Set<GraphContext>> buildClassificationContext(EntityDetail entityDetail) throws OCFCheckedExceptionBase {
        String methodName = "buildClassificationContext";
        if (entityDetail.getClassifications() == null)
            return null;

        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);
        Converter converter = new Converter();
        LineageEntity originalEntityVertex = converter.createLineageEntity(entityDetail);
        AssetContext assetContext = new AssetContext();
        assetContext.addVertex(originalEntityVertex);

        for (Classification classification : entityDetail.getClassifications()) {
            if (!this.lineageClassificationTypes.contains(classification.getName()))
                continue;
            LineageEntity classificationVertex = new LineageEntity();
            String classificationGUID = classification.getName() + entityDetail.getGUID();
            classificationVertex.setGuid(classificationGUID);
            copyClassificationProperties(classificationVertex, classification);
            assetContext.addVertex(classificationVertex);
            GraphContext graphContext = new GraphContext(classificationVertex.getTypeDefName(), classificationGUID, originalEntityVertex, classificationVertex);
            assetContext.addGraphContext(graphContext);
        }
        return assetContext.getNeighbors();
    }

    private void copyClassificationProperties(LineageEntity lineageEntity, Classification classification) {
        lineageEntity.setVersion(classification.getVersion());
        lineageEntity.setTypeDefName(classification.getType().getTypeDefName());
        lineageEntity.setCreatedBy(classification.getCreatedBy());
        lineageEntity.setUpdatedBy(classification.getUpdatedBy());
        lineageEntity.setCreateTime(classification.getCreateTime());
        lineageEntity.setUpdateTime(classification.getUpdateTime());

        Converter converter = new Converter();
        lineageEntity.setProperties(converter.instancePropertiesToMap(classification.getProperties()));
    }
}

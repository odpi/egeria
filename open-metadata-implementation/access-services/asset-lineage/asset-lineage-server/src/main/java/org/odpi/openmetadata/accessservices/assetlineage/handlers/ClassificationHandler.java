/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.assetlineage.model.AssetContext;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;

/**
 * The classification handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private InvalidParameterHandler invalidParameterHandler;
    private HandlerHelper handlerHelper;

    /**
     * Instantiates a new Classification handler.
     *
     * @param invalidParameterHandler the invalid parameter handler
     */
    public ClassificationHandler(InvalidParameterHandler invalidParameterHandler, List<String> lineageClassificationTypes, OMRSRepositoryHelper repositoryHelper) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, null, lineageClassificationTypes);
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

        List<Classification> classifications = handlerHelper.filterLineageClassifications(entityDetail.getClassifications());
        if (CollectionUtils.isEmpty(classifications)) {
            return null;
        }

        AssetContext assetContext = new AssetContext();
        handlerHelper.addLineageClassificationToContext(entityDetail, assetContext);

        return assetContext.getNeighbors();
    }
}

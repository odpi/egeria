/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.handlers;

import org.odpi.openmetadata.accessservices.assetlineage.event.AssetLineageEventType;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GUID_PARAMETER;

/**
 * The Classification Handler maps classifications attached with an entity to an lineage entity.
 */
public class ClassificationHandler {

    private final InvalidParameterHandler invalidParameterHandler;
    private final HandlerHelper handlerHelper;

    /**
     * Instantiates a new Classification handler.
     *
     * @param invalidParameterHandler the invalid parameter handler
     */
    public ClassificationHandler(InvalidParameterHandler invalidParameterHandler, Set<String> lineageClassificationTypes,
                                 OMRSRepositoryHelper repositoryHelper) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.handlerHelper = new HandlerHelper(invalidParameterHandler, repositoryHelper, null, lineageClassificationTypes);
    }


    /**
     * Builds the classification context for an entity.
     * Gets asset context from the entity by classification type.
     *
     * @param entityDetail          the entity for retrieving the classifications attached to it
     * @param assetLineageEventType the event type to be published
     *
     * @return the classification context of the entity
     */
    public Map<String, RelationshipsContext> buildClassificationContext(EntityDetail entityDetail, AssetLineageEventType assetLineageEventType) throws
                                                                                                                                                OCFCheckedExceptionBase {
        String methodName = "buildClassificationContext";
        invalidParameterHandler.validateGUID(entityDetail.getGUID(), GUID_PARAMETER, methodName);

        Map<String, RelationshipsContext> context = new HashMap<>();

        context.put(assetLineageEventType.getEventTypeName(), handlerHelper.buildContextForLineageClassifications(entityDetail));
        return context;
    }
}

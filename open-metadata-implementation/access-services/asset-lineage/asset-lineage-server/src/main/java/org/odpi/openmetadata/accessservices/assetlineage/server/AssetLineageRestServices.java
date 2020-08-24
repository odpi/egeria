/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.GLOSSARY_TERM;
import static org.odpi.openmetadata.accessservices.assetlineage.util.AssetLineageConstants.PROCESS;

/**
 * The AssetLineageRESTServices provides the server-side implementation of the Asset Lineage Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class AssetLineageRestServices {

    private static final Logger log = LoggerFactory.getLogger(AssetLineageRestServices.class);
    private static AssetLineageInstanceHandler instanceHandler = new AssetLineageInstanceHandler();
    private final RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public AssetLineageRestServices() {
        instanceHandler.registerAccessService();
    }

    /**
     * Scan the cohort for available entities of the provided entityType
     * Publish the context for each entity on the AL OMAS out Topic
     *
     * @param serverName name of server instance to call
     * @param userId     the name of the calling user
     * @param entityType the type of the entity to search for
     * @return a list of unique identifiers (guids) of the available entityType as a response
     */
    public GUIDListResponse publishEntities(String serverName, String userId, String entityType) {
        GUIDListResponse response = new GUIDListResponse();

        String methodName = "publishEntities";
        try {
            AssetContextHandler assetContextHandler = instanceHandler.getAssetContextHandler(userId, serverName, methodName);
            List<EntityDetail> entitiesByTypeName = assetContextHandler.getEntitiesByTypeName(userId, entityType);
            if (CollectionUtils.isEmpty(entitiesByTypeName)) {
                return response;
            }

            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);
            if (publisher == null) {
                log.debug("Asset Lineage Publisher is not available. " +
                        "The context event for {} could not be published on the Asset Lineage OMAS Out Topic.", entityType);
                return response;
            }
            List<String> publishedEntitiesContext = publishEntitiesContext(entityType, entitiesByTypeName, publisher);

            response.setGUIDs(publishedEntitiesContext);
        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }

    private List<String> publishEntitiesContext(String typeName, List<EntityDetail> entitiesByType, AssetLineagePublisher publisher) {
        List<String> publishedGUIDs = new ArrayList<>();
        for (EntityDetail entityDetail : entitiesByType) {
            try {
                if (GLOSSARY_TERM.equals(typeName)) {
                    Map<String, Set<GraphContext>> context = publisher.publishGlossaryContext(entityDetail);
                    collectGUID(publishedGUIDs, entityDetail, context);
                } else if (PROCESS.equals(typeName)) {
                    Map<String, Set<GraphContext>> context = publisher.publishProcessContext(entityDetail);
                    collectGUID(publishedGUIDs, entityDetail, context);
                } else {
                    // only Processes and GlossaryTerms are the types supported for initial load
                    log.error("Unsupported typeName {} for entity with guid {}. The context can not be published", typeName, entityDetail.getGUID());
                }
            } catch (OCFCheckedExceptionBase | JsonProcessingException ocfCheckedExceptionBase) {
                ocfCheckedExceptionBase.printStackTrace();
            }
        }

        return publishedGUIDs;
    }

    private void collectGUID(List<String> publishedGUIDs, EntityDetail entityDetail, Map<String, Set<GraphContext>> context) {
        if (MapUtils.isNotEmpty(context)) {
            publishedGUIDs.add(entityDetail.getGUID());
        }
    }
}

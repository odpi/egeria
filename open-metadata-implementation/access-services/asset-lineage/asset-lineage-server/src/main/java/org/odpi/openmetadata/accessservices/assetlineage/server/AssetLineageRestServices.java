/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.server;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.odpi.openmetadata.accessservices.assetlineage.auditlog.AssetLineageAuditCode;
import org.odpi.openmetadata.accessservices.assetlineage.handlers.AssetContextHandler;
import org.odpi.openmetadata.accessservices.assetlineage.model.GraphContext;
import org.odpi.openmetadata.accessservices.assetlineage.outtopic.AssetLineagePublisher;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
     *
     * @return a list of unique identifiers (guids) of the available entityType as a response
     */
    public GUIDListResponse publishEntities(String serverName, String userId, String entityType) {
        GUIDListResponse response = new GUIDListResponse();

        String methodName = "publishEntities";
        try {
            AssetContextHandler assetContextHandler = instanceHandler.getAssetContextHandler(userId, serverName, methodName);
            AuditLog auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);

            List<EntityDetail> entitiesByTypeName = assetContextHandler.getEntitiesByTypeName(userId, entityType);
            if (entitiesByTypeName == null || CollectionUtils.isEmpty(entitiesByTypeName)) {
                auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISH_PROCESS_INFO.getMessageDefinition("ENTITIES_NOT_FOUND", entityType, "0"));
                return response;
            }

            auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISH_PROCESS_INFO.getMessageDefinition("ENTITIES_FOUND", entityType, String.valueOf(entitiesByTypeName.size())));


            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);
            if (publisher == null) {
                auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISHER_NOT_AVAILABLE_ERROR.getMessageDefinition());
                return response;
            }

            auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISH_PROCESS_INFO.getMessageDefinition("PUBLISH_SEQUENCE_START", entityType, String.valueOf(entitiesByTypeName.size())));
            List<String> publishedEntitiesContext = publishEntitiesContext(entitiesByTypeName, publisher, auditLog);
            response.setGUIDs(publishedEntitiesContext);
            auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISH_PROCESS_INFO.getMessageDefinition("PUBLISH_SEQUENCE_END", entityType, String.valueOf(publishedEntitiesContext.size())));


        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }

        return response;
    }


    public GUIDListResponse publishEntity(String serverName, String userId, String entityType, String guid) {
        GUIDListResponse response = new GUIDListResponse();

        String methodName = "publishEntity";

        try {

            AssetContextHandler assetContextHandler = instanceHandler.getAssetContextHandler(userId, serverName, methodName);
            AuditLog auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            EntityDetail entityByTypeAndGuid = assetContextHandler.getEntityByTypeAndGuid(userId, guid, entityType);

            if (entityByTypeAndGuid == null) {
                auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("ENTITY_NOT_FOUND", entityType, guid));
                return response;
            }

            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("ENTITY_FOUND", entityType, guid));
            AssetLineagePublisher publisher = instanceHandler.getAssetLineagePublisher(userId, serverName, methodName);
            if (publisher == null) {
                auditLog.logMessage(methodName, AssetLineageAuditCode.PUBLISHER_NOT_AVAILABLE_ERROR.getMessageDefinition());
                return response;
            }

            String publishedEntitiesContext = publishEntityContext(publisher, entityByTypeAndGuid, auditLog);
            response.setGUIDs(Collections.singletonList(publishedEntitiesContext));

        } catch (InvalidParameterException e) {
            restExceptionHandler.captureInvalidParameterException(response, e);
        } catch (UserNotAuthorizedException e) {
            restExceptionHandler.captureUserNotAuthorizedException(response, e);
        } catch (PropertyServerException e) {
            restExceptionHandler.capturePropertyServerException(response, e);
        }
        return response;
    }

    /**
     * Returns the list of entity GUIDs that were published on the Out Topic
     *
     * @param entitiesByType list of found entities for the requested type
     * @param publisher      Asset Lineage publisher
     *
     * @return the list of entity GUIDs published on the Asset Lineage Out Topic
     */
    private List<String> publishEntitiesContext(List<EntityDetail> entitiesByType,
                                                AssetLineagePublisher publisher, AuditLog auditLog) {

        List<String> publishedGUIDs = entitiesByType.parallelStream().map(entityDetail ->
                publishEntityContext(publisher, entityDetail, auditLog)).collect(Collectors.toList());

        CollectionUtils.filter(publishedGUIDs, PredicateUtils.notNullPredicate());

        return publishedGUIDs;
    }

    /**
     * Returns GUID that was published on the Out Topic
     *
     * @param entityDetail  the entity for which the event is published
     * @param publisher      Asset Lineage publisher
     *
     * @return the GUID published on the Asset Lineage Out Topic
     */
    private String publishEntityContext(AssetLineagePublisher publisher, EntityDetail entityDetail, AuditLog auditLog) {
        String methodName="publishEntityContext";

        try {
            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("BUILDING_CONTEXT_STARTED", entityDetail.getType().getTypeDefName(), entityDetail.getGUID()));
            String result =  publishContext(entityDetail, publisher);
            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("PUBLISHED", entityDetail.getType().getTypeDefName(), entityDetail.getGUID()));
            return result;
        } catch (Exception e) {
            auditLog.logMessage(methodName, AssetLineageAuditCode.ENTITY_INFO.getMessageDefinition("FAILED_TO_PUBLISH", entityDetail.getType().getTypeDefName(), entityDetail.getGUID()));
            auditLog.logException(methodName, AssetLineageAuditCode.ENTITY_ERROR.getMessageDefinition(entityDetail.getType().getTypeDefName(),entityDetail.getGUID()), e);
        }
        return null;
    }

    /**
     * Build entity's context and publish it on Out Topic
     *
     * @param entityDetail - the entity based on which we want to build the context
     * @param publisher    Asset Lineage publisher
     *
     * @return the entity GUID if the context is not empty
     *
     * @throws OCFCheckedExceptionBase checked exception for reporting errors found when using OCF connectors
     * @throws JsonProcessingException exception parsing the event json
     */
    private String publishContext(EntityDetail entityDetail,
                                  AssetLineagePublisher publisher) throws OCFCheckedExceptionBase, JsonProcessingException {
        String typeName = entityDetail.getType().getTypeDefName();
        Map<String, Set<GraphContext>> context = new HashMap<>();
        switch (typeName) {
            case GLOSSARY_TERM: {
                context = publisher.publishGlossaryContext(entityDetail);
                break;
            }
            case PROCESS: {
                context = publisher.publishProcessContext(entityDetail);
                break;
            }
            default:
                log.error("Unsupported typeName {} for entity with guid {}. The context can not be published",
                        typeName, entityDetail.getGUID());
                break;
        }

        if (MapUtils.isNotEmpty(context)) {
            return entityDetail.getGUID();
        }
        return null;
    }
}

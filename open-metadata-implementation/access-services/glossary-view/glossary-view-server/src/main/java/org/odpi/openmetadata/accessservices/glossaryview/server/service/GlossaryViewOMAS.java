/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.exception.OMRSExceptionWrapper;
import org.odpi.openmetadata.accessservices.glossaryview.exception.OMRSRuntimeExceptionWrapper;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewClassification;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailFactory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

/**
 * Composes OMRS calls into needed operations, handles the exceptions specific to this OMAS, converts result to needed
 * format
 */
public class GlossaryViewOMAS extends OMRSClient {

    private final static String GLOSSARY_VIEW_OMAS = "Glossary View Omas";

    /**
     * Converts a {@code Classification} into a {@code GlossaryViewClassification} with the help of a {@code OMRSRepositoryHelper}
     *
     * @param classification
     * @param helper
     *
     * @return glossaryViewClassification
     */
    private final BiFunction<Classification, OMRSRepositoryHelper, GlossaryViewClassification> classificationConverter = (classification, helper) -> {
        GlossaryViewClassification glossaryViewClassification = new GlossaryViewClassification();
        glossaryViewClassification.setName(classification.getName());
        glossaryViewClassification.setClassificationType(classification.getType().getTypeDefName());
        glossaryViewClassification.setCreatedBy(classification.getCreatedBy());
        glossaryViewClassification.setUpdatedBy(classification.getUpdatedBy());
        glossaryViewClassification.setCreateTime(classification.getCreateTime());
        glossaryViewClassification.setUpdateTime(classification.getUpdateTime());
        glossaryViewClassification.setStatus(classification.getStatus().getName());

        if(classification.getProperties().getInstanceProperties() != null) {
            classification.getProperties().getInstanceProperties().entrySet().stream()
                    .forEach(incoming -> {
                        glossaryViewClassification.addProperty(incoming.getKey(), incoming.getValue().valueAsString());
                    });
        }

        return glossaryViewClassification;
    };

    /**
     * Converts an {@code EntityDetail} into a {@code GlossaryViewEntityDetail} with the help of a {@code OMRSRepositoryHelper}
     *
     * @param entityDetail
     * @param helper
     *
     * @return glossaryViewEntityDetail
     */
    private final BiFunction<EntityDetail, OMRSRepositoryHelper, GlossaryViewEntityDetail> entityDetailConverter = (entityDetail, helper) -> {
        Optional<InstanceProperties> optionalProperties = Optional.ofNullable(entityDetail.getProperties());

        GlossaryViewEntityDetail glossaryViewEntityDetail = GlossaryViewEntityDetailFactory.build(entityDetail.getType().getTypeDefName())
            .setEntityType(entityDetail.getType().getTypeDefName())
            .setCreatedBy(entityDetail.getCreatedBy())
            .setUpdatedBy(entityDetail.getUpdatedBy())
            .setCreateTime(entityDetail.getCreateTime())
            .setUpdateTime(entityDetail.getUpdateTime())
            .setVersion(entityDetail.getVersion())
            .setGuid(entityDetail.getGUID())
            .setStatus(entityDetail.getStatus().getName());

        /*Encountered a case where an entity did not have properties. However, this should not be possible in non-dev envs*/
        if(optionalProperties.isPresent()){
            glossaryViewEntityDetail.setEffectiveFromTime(optionalProperties.get().getEffectiveFromTime());
            glossaryViewEntityDetail.setEffectiveToTime(optionalProperties.get().getEffectiveToTime());

            optionalProperties.get().getInstanceProperties().entrySet()
                    .forEach( incoming -> {
                        glossaryViewEntityDetail.putProperty(incoming.getKey(), helper.getStringProperty(GLOSSARY_VIEW_OMAS, incoming.getKey(),
                                optionalProperties.get(), "GlossaryViewOMAS.entityDetailConverter.apply"));
                    });
        }

        if(entityDetail.getClassifications() != null) {
            glossaryViewEntityDetail.addClassifications(entityDetail.getClassifications().stream()
                    .map(c -> classificationConverter.apply(c, helper)).collect(Collectors.toList()));
        }
        return glossaryViewEntityDetail;
    };

    /**
     * Extract an entity based on provided GUID and convert it to this omas's type
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityGUID guid to search for
     * @param entityTypeName entity type
     *
     * @return entity
     */
    protected GlossaryViewEntityDetailResponse getEntityDetailResponse(String userId, String serverName, String entityGUID,
                                                                       String entityTypeName, String methodName){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            Optional<EntityDetail> entityDetail = getEntityDetail(userId, serverName, entityGUID, entityTypeName, methodName);
            Optional<OMRSRepositoryHelper> omrsRepositoryHelper = getOMRSRepositoryHelper(userId, serverName);
            if(!omrsRepositoryHelper.isPresent()){
                return response;
            }

            if( entityDetail.isPresent() && omrsRepositoryHelper.isPresent()) {
                response.addEntityDetail(entityDetailConverter.apply(entityDetail.get(), omrsRepositoryHelper.get()));
            }
        }catch (OMRSExceptionWrapper ew){
            prepare(response, ew);
        }
        return response;
    }

    /**
     * Extract related entities to the given one and convert them to this omas's type
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityGUID target entity
     * @param entityTypeName target entity relationship type
     * @param relationshipTypeGUID relationship type guid
     * @param relationshipTypeName relationship type name
     * @param from
     * @param size
     *
     * @return related entities
     */
    protected GlossaryViewEntityDetailResponse getRelatedEntitiesResponse(String userId, String serverName, String entityGUID,
                                                                          String entityTypeName, String relationshipTypeGUID,
                                                                          String relationshipTypeName, Integer from, Integer size,
                                                                          String methodName){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            List<EntityDetail> entities = getRelatedEntities(userId, serverName, entityGUID, entityTypeName,
                    relationshipTypeGUID, relationshipTypeName, from, size, methodName);
            Optional<OMRSRepositoryHelper> omrsRepositoryHelper = getOMRSRepositoryHelper(userId, serverName);
            if(entities == null || !omrsRepositoryHelper.isPresent()){
                return response;
            }

            response.addEntityDetails(entities.stream()
                    .filter(entity -> !entity.getGUID().equals(entityGUID))
                    .map(entity -> entityDetailConverter.apply(entity, omrsRepositoryHelper.get()))
                    .collect(Collectors.toList()));
        }catch (OMRSExceptionWrapper e){
            prepare(response, e);
        }catch (OMRSRuntimeExceptionWrapper ew){
            prepare(response, ew);
        }

        return response;
    }

    /**
     * Extract all entities of specified type and convert them to this omas's type
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityTypeGUID entity type guid
     * @param from from
     * @param size size
     *
     * @return all entities
     */
    protected GlossaryViewEntityDetailResponse getAllEntityDetailsResponse(String userId, String serverName, String entityTypeGUID,
                                                                           Integer from, Integer size, String methodName){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            List<EntityDetail> entities = getAllEntityDetails(userId, serverName, entityTypeGUID, from, size, methodName);
            Optional<OMRSRepositoryHelper> omrsRepositoryHelper = getOMRSRepositoryHelper(userId, serverName);
            if(entities == null || !omrsRepositoryHelper.isPresent()){
                return response;
            }

            response.addEntityDetails(entities.stream()
                    .map(entity -> entityDetailConverter.apply(entity, omrsRepositoryHelper.get()) )
                    .collect(Collectors.toList()));
        }catch (OMRSExceptionWrapper ew){
            prepare(response, ew);
        }

        return response;
    }

    /**
     * Prepares the response with information from caught exception
     *
     * @param response
     * @param exception
     */
    private void prepare(GlossaryViewEntityDetailResponse response, Exception exception) {
        response.setExceptionClassName(exception.getClass().getName());
        response.setExceptionErrorMessage(exception.getMessage());
        response.setRelatedHTTPCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

}

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

        classification.getProperties().getInstanceProperties().entrySet().stream()
                .forEach( incoming -> {
                    glossaryViewClassification.addProperty(incoming.getKey(), helper.getStringProperty(GLOSSARY_VIEW_OMAS, incoming.getKey(), classification.getProperties(), "GlossaryViewOMAS.classificationConverter.apply"));
                });

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
     * Extract an entity based on provided GUID
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityGUID guid to search for
     *
     * @return EntityDetailResponse entity
     */
    protected GlossaryViewEntityDetailResponse getEntityDetailResponse(String userId, String serverName, String entityGUID){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            Optional<EntityDetail> entityDetail = getEntityDetail(userId, serverName, entityGUID);
            Optional<OMRSRepositoryHelper> omrsRepositoryHelper = getOMRSRepositoryHelper(serverName);

            if( entityDetail.isPresent() && omrsRepositoryHelper.isPresent()) {
                response.addEntityDetail(entityDetailConverter.apply(entityDetail.get(), omrsRepositoryHelper.get()));
            }
        }catch (OMRSExceptionWrapper e){
            prepare(response, e);
        }
        return response;
    }

    /**
     * Extract all entities related to given entity. This is done by first extracting the GUID for specified
     * relationship type, then the actual relationships and, ultimately, the entities at end two of these relationships
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param relationshipEndOneEntityGUID entity for which we extract relationships
     * @param relationshipTypeDefNames relationship type
     *
     * @return EntityDetailResponse related entities
     */
    protected GlossaryViewEntityDetailResponse getRelatedEntities(String userId, String serverName, String relationshipEndOneEntityGUID,
                                                                  String relationshipTypeDefNames){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            List<String> relationshipTypeGUIDs = getTypeDefGUIDs(userId, serverName, relationshipTypeDefNames);
            List<EntityDetail> entities = getNeighbourhood(userId, serverName, relationshipEndOneEntityGUID, relationshipTypeGUIDs);
            Optional<OMRSRepositoryHelper> omrsRepositoryHelper = getOMRSRepositoryHelper(serverName);

            response.addEntityDetails(entities.stream()
                    .filter(entity -> !entity.getGUID().equals(relationshipEndOneEntityGUID))
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
     * Extract all entities of specified type
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param typeDefName entity type name
     *
     * @return EntityDetailResponse all entities
     */
    protected GlossaryViewEntityDetailResponse getAllEntityDetailsResponse(String userId, String serverName, String typeDefName){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            List<EntityDetail> entities = getAllEntityDetails(userId, serverName, typeDefName);
            Optional<OMRSRepositoryHelper> omrsRepositoryHelper = getOMRSRepositoryHelper(serverName);

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

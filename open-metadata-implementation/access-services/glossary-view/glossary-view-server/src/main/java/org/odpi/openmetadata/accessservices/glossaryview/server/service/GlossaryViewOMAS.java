/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.server.service;

import org.odpi.openmetadata.accessservices.glossaryview.exception.OMRSExceptionWrapper;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewClassification;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailFactory;
import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetailResponse;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Composes OMRS calls into needed operations, handles the exceptions specific to this OMAS, converts result to needed
 * format
 */
public class GlossaryViewOMAS extends OMRSClient {

    private final static String GLOSSARY_VIEW_OMAS = "Glossary View Omas";

    /**
     * Predicate to test the current time between effectiveFrom and effectiveTo properties of an entity detail. Will return
     * false if now is outside the interval given by effectiveFromTime and effectiveToTime, true otherwise
     */
    private final Predicate<EntityDetail> effectivePredicate = (entityDetail -> {
        if(entityDetail.getProperties() == null){
            return true;
        }
        Date effectiveFromTime = entityDetail.getProperties().getEffectiveFromTime();
        Date effectiveToTime = entityDetail.getProperties().getEffectiveToTime();

        if(effectiveFromTime == null || effectiveToTime == null){
            return true;
        }

        long now = Calendar.getInstance().getTimeInMillis();
        return effectiveFromTime.getTime() <= now && now <= effectiveToTime.getTime();
    });

    /**
     * Converts a {@code Classification} into a {@code GlossaryViewClassification} with the help of a {@code OMRSRepositoryHelper}
     *
     */
    private final Function<Classification, GlossaryViewClassification> classificationConverter = (classification) -> {
        GlossaryViewClassification glossaryViewClassification = new GlossaryViewClassification();
        glossaryViewClassification.setName(classification.getName());
        glossaryViewClassification.setClassificationType(classification.getType().getTypeDefName());
        glossaryViewClassification.setCreatedBy(classification.getCreatedBy());
        glossaryViewClassification.setUpdatedBy(classification.getUpdatedBy());
        glossaryViewClassification.setCreateTime(classification.getCreateTime());
        glossaryViewClassification.setUpdateTime(classification.getUpdateTime());
        glossaryViewClassification.setStatus(classification.getStatus().getName());

        if(classification.getProperties().getInstanceProperties() != null) {
            classification.getProperties().getInstanceProperties()
                    .forEach((key, value) -> glossaryViewClassification.addProperty(key, value.valueAsString()));
        }

        return glossaryViewClassification;
    };

    /**
     * Converts an {@code EntityDetail} into a {@code GlossaryViewEntityDetail} with the help of a {@code OMRSRepositoryHelper}
     *
     */
    private final Function<EntityDetail, GlossaryViewEntityDetail> entityDetailConverter = (entityDetail) -> {
        Optional<InstanceProperties> optionalProperties = Optional.ofNullable(entityDetail.getProperties());

        GlossaryViewEntityDetail glossaryViewEntityDetail = GlossaryViewEntityDetailFactory.build(entityDetail.getType().getTypeDefName())
            .setTypeDefName(entityDetail.getType().getTypeDefName())
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

            optionalProperties.get().getInstanceProperties()
                    .forEach((key, value) -> glossaryViewEntityDetail.putProperty(key, value.valueAsString()));
        }

        if(entityDetail.getClassifications() != null) {
            glossaryViewEntityDetail.addClassifications(entityDetail.getClassifications().stream()
                    .map(classificationConverter).collect(Collectors.toList()));
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
            entityDetail.ifPresent(detail -> response.addEntityDetail(entityDetailConverter.apply(detail)));

        } catch (OMRSExceptionWrapper ew){
            prepare(response, ew.getReportedHTTPCode(), ew.getReportingClassName(), ew.getReportingActionDescription(),
                    ew.getReportedUserAction(), ew.getErrorMessage(), ew.getReportedSystemAction(), ew.getRelatedProperties());
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
     * @param relationshipTypeName relationship type name
     * @param from from
     * @param size size
     *
     * @return related entities
     */
    protected GlossaryViewEntityDetailResponse getRelatedEntitiesResponse(String userId, String serverName, String entityGUID,
                                                                          String entityTypeName, String relationshipTypeName,
                                                                          Integer from, Integer size, String methodName){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            String relationshipTypeGUID = getTypeDefGUID(relationshipTypeName, userId, serverName);
            List<EntityDetail> entities = getRelatedEntities(userId, serverName, entityGUID, entityTypeName,
                    relationshipTypeGUID, relationshipTypeName, from, size, methodName);
            if(entities == null){
                return response;
            }

            response.addEntityDetails(entities.stream()
                    .filter(entity -> !entity.getGUID().equals(entityGUID))
                    .filter(effectivePredicate)
                    .map(entityDetailConverter)
                    .collect(Collectors.toList()));
        }catch (OMRSExceptionWrapper ew){
            prepare(response, ew.getReportedHTTPCode(), ew.getReportingClassName(), ew.getReportingActionDescription(),
                    ew.getReportedUserAction(), ew.getErrorMessage(), ew.getReportedSystemAction(), ew.getRelatedProperties());
        }

        return response;
    }

    /**
     * Extract all entities of specified type and convert them to this omas's type
     *
     * @param userId calling user
     * @param serverName instance to call
     * @param entityTypeName entity type name
     * @param from from
     * @param size size
     *
     * @return all entities
     */
    protected GlossaryViewEntityDetailResponse getAllEntityDetailsResponse(String userId, String serverName, String entityTypeName,
                                                                           Integer from, Integer size, String methodName){
        GlossaryViewEntityDetailResponse response = new GlossaryViewEntityDetailResponse();

        try {
            String entityTypeGUID = getTypeDefGUID(entityTypeName, userId, serverName);
            List<EntityDetail> entities = getAllEntityDetails(userId, serverName, entityTypeGUID, from, size, methodName);
            if(entities == null){
                return response;
            }

            response.addEntityDetails(entities.stream()
                    .filter(effectivePredicate)
                    .map(entityDetailConverter)
                    .collect(Collectors.toList()));
        }catch (OMRSExceptionWrapper ew){
            prepare(response, ew.getReportedHTTPCode(), ew.getReportingClassName(), ew.getReportingActionDescription(),
                    ew.getReportedUserAction(), ew.getErrorMessage(), ew.getReportedSystemAction(), ew.getRelatedProperties());
        }

        return response;
    }

    /**
     * Prepares the response with information from caught exception
     *
     * @param response
     * @param httpCode  http response code to use if this exception flows over a REST call
     * @param className  name of class reporting error
     * @param userAction  instructions for correcting the error
     * @param errorMessage  description of error
     * @param systemAction  actions of the system as a result of the error
     * @param properties  description of function it was performing when error detected
     */
    private void prepare(GlossaryViewEntityDetailResponse response, int httpCode, String className, String actionDescription,
                         String userAction, String errorMessage, String systemAction, Map<String, Object> properties){
        response.setRelatedHTTPCode(httpCode);
        response.setExceptionClassName(className);
        response.setActionDescription(actionDescription);
        response.setExceptionUserAction(userAction);
        response.setExceptionErrorMessage(errorMessage);
        response.setExceptionSystemAction(systemAction);
        response.setExceptionProperties(properties);
    }

    /**
     * Extract the guid of a type def
     *
     * @param typeDefName
     * @param userId
     * @param serverName
     *
     * @return guid
     *
     */
    private String getTypeDefGUID(String typeDefName, String userId, String serverName) throws OMRSExceptionWrapper{
        String getTypeDefGUID = "getTypeDefGUID";
        Optional<OMRSRepositoryHelper> helper = getOMRSRepositoryHelper(userId, serverName, getTypeDefGUID);

        if(!helper.isPresent()){
            throw new OMRSExceptionWrapper(500, getClass().getSimpleName(), getTypeDefGUID, "Unable to retrieve repository helper",
                    getTypeDefGUID, null);
        }
        return helper.get().getTypeDefByName(GLOSSARY_VIEW_OMAS, typeDefName).getGUID();
    }
}
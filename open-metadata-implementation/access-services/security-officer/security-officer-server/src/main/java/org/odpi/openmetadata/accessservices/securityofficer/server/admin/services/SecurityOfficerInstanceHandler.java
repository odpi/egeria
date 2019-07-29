/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.admin.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerUpdateTagEvent;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.errorcode.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecuritySchemaElement;
import org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Builder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SCHEMA_ATTRIBUTE_TYPE;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SCHEMA_ELEMENT;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_LABELS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_OFFICER;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_PROPERTIES;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SECURITY_TAGS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.admin.utils.Constants.SOURCE;

/**
 * SecurityOfficerInstanceHandler retrieves information from the instance map for the
 * access service instances.  The instance map is thread-safe.  Instances are added
 * and removed by the SecurityOfficerAdmin class.
 */
class SecurityOfficerInstanceHandler {

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerInstanceHandler.class);
    private static SecurityOfficerServicesInstanceMap instanceMap = new SecurityOfficerServicesInstanceMap();
    private Builder builder = new Builder();

    /**
     * Default constructor registers the access service
     */
    SecurityOfficerInstanceHandler() {
        SecurityOfficerRegistration.registerAccessService();
    }

    SecurityClassification getSecurityTagBySchemaElementId(String serverName, String userId, String schemaElementId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException {
        OMRSMetadataCollection metadataCollection = getRepositoryConnector(serverName).getMetadataCollection();

        EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, schemaElementId);
        List<Classification> classifications = entityDetail.getClassifications();

        return getSecurityClassification(serverName, classifications);
    }

    List<SecuritySchemaElement> updateSecurityTagBySchemaElementId(String serverName, String userId, String schemaElementId, SecurityClassification securityClassification) throws PropertyServerException, RepositoryErrorException, ClassificationErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, TypeDefNotKnownException, TypeErrorException, PagingErrorException {
        OMRSMetadataCollection metadataCollection = getRepositoryConnector(serverName).getMetadataCollection();

        InstanceProperties instanceProperties = getInstanceProperties(serverName, securityClassification);
        EntityDetail schemaElement = metadataCollection.getEntityDetail(userId, schemaElementId);

        SecuritySchemaElement securitySchemaElement = updateSecurityTagsClassificationForEntity(serverName, userId, metadataCollection, instanceProperties, schemaElement);
        List<SecuritySchemaElement> result = new ArrayList<>();
        result.add(securitySchemaElement);

        updateSecurityTagOnColumns(serverName, userId, metadataCollection, instanceProperties, schemaElement, result, securityClassification.getSecurityLabels());

        return result;
    }

    List<SecuritySchemaElement> deleteSecurityTagBySchemaElementId(String serverName, String userId, String schemaElementId) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, ClassificationErrorException, TypeErrorException, TypeDefNotKnownException, PagingErrorException, FunctionNotSupportedException, PropertyErrorException {
        OMRSMetadataCollection metadataCollection = getRepositoryConnector(serverName).getMetadataCollection();
        EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, schemaElementId);

        OMRSRepositoryHelper repositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();
        EntityDetail response = deleteSecurityTagClassification(repositoryHelper, serverName, entityDetail);

        List<EntityDetail> deleteSecurityTagOnColumns = deleteSecurityTagOnColumns(serverName, entityDetail, userId, metadataCollection);
        if (deleteSecurityTagOnColumns != null) {
            deleteSecurityTagOnColumns.add(response);
            return builder.buildSecuritySchemaElementList(deleteSecurityTagOnColumns, repositoryHelper);
        }
        return builder.buildSecuritySchemaElementList(Collections.singletonList(response), repositoryHelper);
    }

    private EntityDetail addSecurityTagsClassification(String userId, OMRSMetadataCollection metadataCollection,
                                                       InstanceProperties instanceProperties, EntityDetail schemaElement) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, PropertyErrorException, UserNotAuthorizedException, FunctionNotSupportedException {
        if (schemaElement.getClassifications() != null && !schemaElement.getClassifications().isEmpty()) {
            return metadataCollection.updateEntityClassification(userId, schemaElement.getGUID(), SECURITY_TAGS, instanceProperties);
        } else {
            return metadataCollection.classifyEntity(userId, schemaElement.getGUID(), SECURITY_TAGS, instanceProperties);
        }
    }

    private EntityDetail deleteSecurityTagClassification(OMRSRepositoryHelper repositoryHelper, String serverName, EntityDetail entityDetail) throws ClassificationErrorException {
        return repositoryHelper.deleteClassificationFromEntity(serverName, entityDetail, SECURITY_TAGS, "deleteSecurityTagClassification");
    }

    /**
     * Return the repository connector for this server.
     *
     * @return OMRSRepositoryConnector object
     * @throws PropertyServerException the instance has not been initialized successfully
     */
    private OMRSRepositoryConnector getRepositoryConnector(String serverName) throws PropertyServerException {
        SecurityOfficerServicesInstance instance = instanceMap.getInstance(serverName);

        if (instance != null) {
            return instance.getRepositoryConnector();
        } else {
            final String methodName = "getRepositoryConnector";

            SecurityOfficerErrorCode errorCode = SecurityOfficerErrorCode.SERVICE_NOT_INITIALIZED;
            String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(serverName, methodName);

            throw new PropertyServerException(errorCode.getHttpErrorCode(),
                    this.getClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }

    private void updateSecurityTagOnColumns(String serverName, String userId, OMRSMetadataCollection metadataCollection, InstanceProperties instanceProperties,
                                            EntityDetail schemaElement, List<SecuritySchemaElement> result, List<String> newSecurityLabels) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, TypeErrorException, PagingErrorException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, ClassificationErrorException, PropertyServerException {
        if (isRelationalTable(schemaElement.getType().getTypeDefName())) {
            List<EntityDetail> columns = getColumns(metadataCollection, userId, schemaElement);

            for (EntityDetail column : columns) {
                if (isMoreRestrictiveLabel(serverName, column.getClassifications(), newSecurityLabels)) {
                    SecuritySchemaElement columnUpdated = updateSecurityTagsClassificationForEntity(serverName, userId, metadataCollection, instanceProperties, column);
                    result.add(columnUpdated);
                }
            }
        }
    }

    private List<EntityDetail> deleteSecurityTagOnColumns(String serverName, EntityDetail schemaElement, String userId,
                                                          OMRSMetadataCollection metadataCollection) throws InvalidParameterException, TypeErrorException, FunctionNotSupportedException, PropertyErrorException, EntityNotKnownException, TypeDefNotKnownException, PagingErrorException, UserNotAuthorizedException, RepositoryErrorException, ClassificationErrorException, PropertyServerException {
        if (!isRelationalTable(schemaElement.getType().getTypeDefName())) {
            return Collections.emptyList();
        }

        List<EntityDetail> columns = getColumns(metadataCollection, userId, schemaElement);
        if (columns.isEmpty()) {
            return Collections.emptyList();
        }

        List<EntityDetail> entitiesWithDeletedSecurityTags = new ArrayList<>();
        OMRSRepositoryHelper repositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();
        for (EntityDetail column : columns) {
            SecurityClassification securityTags = getSecurityClassification(serverName, column.getClassifications());
            if (securityTags != null) {
                if (isEligibleForDelete(securityTags, column.getType().getTypeDefName())) {
                    deleteSecurityTagClassification(repositoryHelper, serverName, column);
                    entitiesWithDeletedSecurityTags.add(column);
                } else {
                    log.debug("The security tag on the schema element should not be deleted because the source of the assignment is different.");
                }
            }
        }

        return entitiesWithDeletedSecurityTags;
    }

    private boolean isMoreRestrictiveLabel(String serverName, List<Classification> classifications, List<String> newSecurityLabels) throws PropertyServerException {
        SecurityClassification securityClassification = getSecurityClassification(serverName, classifications);
        if (securityClassification == null) {
            return true;
        }

        List<String> existingSecurityLabels = securityClassification.getSecurityLabels();
        Optional<String> existingConfidentialityLevel = getConfidentialityLabel(existingSecurityLabels);
        if (!existingConfidentialityLevel.isPresent()) {
            return true;
        }

        Optional<String> newConfidentialityLabel = getConfidentialityLabel(newSecurityLabels);
        if (!newConfidentialityLabel.isPresent()) {
            return true;
        }

        Integer currentConfidentialityLevel = getConfidentialityLevel(existingConfidentialityLevel.get());
        Integer newConfidentialityLevel = getConfidentialityLevel(newConfidentialityLabel.get());
        return currentConfidentialityLevel < newConfidentialityLevel;
    }

    private Boolean isEligibleForDelete(SecurityClassification securityTag, String actualSource) {
        if (securityTag.getSecurityProperties() != null) {
            String source = String.valueOf(securityTag.getSecurityProperties().get(SOURCE));
            if (actualSource.equals(source)) {
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    private Optional<String> getConfidentialityLabel(List<String> securityLabels) {
        return securityLabels.stream().filter(l -> l.startsWith("C")).findAny();
    }

    private Integer getConfidentialityLevel(String confidentialityLabel) {
        return Integer.valueOf(confidentialityLabel.substring(1));
    }


    private List<EntityDetail> getColumns(OMRSMetadataCollection metadataCollection, String userId, EntityDetail schemaElement) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, TypeErrorException, PagingErrorException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException {
        List<Relationship> tableTypeRelationship = getRelationshipsByType(metadataCollection, userId, schemaElement.getGUID(), SCHEMA_ATTRIBUTE_TYPE);

        if (tableTypeRelationship != null && tableTypeRelationship.size() == 1) {
            EntityDetail tableType = getEntity(userId, metadataCollection, schemaElement.getGUID(), tableTypeRelationship.get(0));
            if (tableType == null) {
                return Collections.emptyList();
            }
            List<Relationship> columnsRelationships = getRelationshipsByType(metadataCollection, userId, tableType.getGUID(), ATTRIBUTE_FOR_SCHEMA);

            return columnsRelationships.stream().map(columnsRelationship -> getEntity(userId, metadataCollection, tableType.getGUID(), columnsRelationship)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<Relationship> getRelationshipsByType(OMRSMetadataCollection metadataCollection, String userId, String entityGUID, String type) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, TypeErrorException, PagingErrorException {
        String schemaAttributeTypeGuid = getTypeGUID(metadataCollection, userId, type);
        return getRelationships(userId, metadataCollection, schemaAttributeTypeGuid, entityGUID);
    }

    private EntityDetail getEntity(String userId, OMRSMetadataCollection metadataCollection, String knownId, Relationship relationship) {
        String entityGUID = relationship.getEntityTwoProxy().getGUID().equals(knownId) ?
                relationship.getEntityOneProxy().getGUID() : relationship.getEntityTwoProxy().getGUID();
        try {
            return metadataCollection.getEntityDetail(userId, entityGUID);
        } catch (InvalidParameterException | RepositoryErrorException | EntityNotKnownException | EntityProxyOnlyException | UserNotAuthorizedException e) {
            log.debug("Unable to get the entity by guid {}", entityGUID);
        }
        return null;
    }

    private List<Relationship> getRelationships(String userId, OMRSMetadataCollection metadataCollection, String relationshipTypeGUID, String entityGuid) throws UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, PagingErrorException {

        return metadataCollection.getRelationshipsForEntity(userId,
                entityGuid,
                relationshipTypeGUID,
                0,
                null,
                null,
                null,
                null,
                0);
    }

    private boolean isRelationalTable(String typeDefName) {
        return typeDefName.equals(RELATIONAL_TABLE);
    }

    private boolean isSecurityTag(String name) {
        return SECURITY_TAGS.equals(name);
    }

    private SecuritySchemaElement updateSecurityTagsClassificationForEntity(String serverName, String userId, OMRSMetadataCollection metadataCollection, InstanceProperties instanceProperties,
                                                                            EntityDetail schemaElement) throws InvalidParameterException, RepositoryErrorException, EntityNotKnownException, ClassificationErrorException, PropertyErrorException, UserNotAuthorizedException, FunctionNotSupportedException, PropertyServerException {
        EntityDetail entityDetail = addSecurityTagsClassification(userId, metadataCollection, instanceProperties, schemaElement);

        OMRSRepositoryHelper omrsRepositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();
        SecuritySchemaElement schemaElementEntity = builder.buildSecuritySchemaElement(entityDetail, omrsRepositoryHelper);
        publishEventForUpdatedSecurityTags(serverName, schemaElement, schemaElementEntity);

        return schemaElementEntity;
    }

    private void publishEventForUpdatedSecurityTags(String serverName, EntityDetail schemaElement, SecuritySchemaElement schemaElementEntity) throws PropertyServerException {
        SecurityOfficerUpdateTagEvent event = buildEventForUpdatedSecurityTags(serverName, schemaElement, schemaElementEntity);
        SecurityOfficerServicesInstance instance = instanceMap.getInstance(serverName);
        try {
            instance.getSecurityOfficerPublisher().sendEvent(event);
        } catch (JsonProcessingException e) {
            log.debug("Unable to send event for updated security tags");
        }
    }

    List<SecurityClassification> getAvailableSecurityTags(String serverName, String userId) throws PropertyServerException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, FunctionNotSupportedException, ClassificationErrorException, PagingErrorException, PropertyErrorException {
        OMRSMetadataCollection metadataCollection = getRepositoryConnector(serverName).getMetadataCollection();

        String entityTypeGuid = getTypeGUID(metadataCollection, userId, SCHEMA_ELEMENT);
        if (entityTypeGuid == null) {
            return Collections.emptyList();
        }

        List<EntityDetail> entitiesWithSecurityTagsAssigned = findEntitiesWithSecurityTagsAssigned(userId, metadataCollection, entityTypeGuid);
        if (entitiesWithSecurityTagsAssigned == null || entitiesWithSecurityTagsAssigned.isEmpty()) {
            return Collections.emptyList();
        }

        OMRSRepositoryHelper omrsRepositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();

        return entitiesWithSecurityTagsAssigned.stream().flatMap(entityDetail -> entityDetail.getClassifications().stream()).filter(classification -> isSecurityTag(classification.getName())).map(classification -> builder.buildSecurityTag(classification, omrsRepositoryHelper)).distinct().collect(Collectors.toList());
    }

    private List<EntityDetail> findEntitiesWithSecurityTagsAssigned(String userId, OMRSMetadataCollection metadataCollection, String entityTypeGuid) throws InvalidParameterException, TypeErrorException, RepositoryErrorException, ClassificationErrorException, PropertyErrorException, PagingErrorException, FunctionNotSupportedException, UserNotAuthorizedException {
        return metadataCollection.findEntitiesByClassification(userId,
                entityTypeGuid,
                SECURITY_TAGS,
                null,
                null,
                0,
                null,
                null,
                null,
                null,
                0);
    }

    private String getTypeGUID(OMRSMetadataCollection metadataCollection, String userId, String typeName) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException {
        TypeDef typeDefByName = metadataCollection.getTypeDefByName(userId, typeName);
        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }

    private SecurityClassification getSecurityClassification(String serverName, List<Classification> classifications) throws PropertyServerException {
        if (classifications == null || classifications.isEmpty()) {
            return null;
        }

        Optional<Classification> securityTag = classifications.stream().filter(classification -> classification.getName().equals(SECURITY_TAGS)).findAny();
        OMRSRepositoryHelper repositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();

        return securityTag.map(classification -> builder.buildSecurityTag(classification, repositoryHelper)).orElse(null);
    }

    private SecurityOfficerUpdateTagEvent buildEventForUpdatedSecurityTags(String serverName, EntityDetail schemaElement, SecuritySchemaElement result) throws PropertyServerException {
        SecurityOfficerUpdateTagEvent event = new SecurityOfficerUpdateTagEvent();

        event.setSecuritySchemaElement(result);
        event.setPreviousClassification(getSecurityClassification(serverName, schemaElement.getClassifications()));
        event.setEventType(SecurityOfficerEventType.UPDATED_SECURITY_ASSIGNMENT);

        return event;
    }

    private InstanceProperties getInstanceProperties(String serverName, SecurityClassification securityTagLevel) throws PropertyServerException {
        InstanceProperties instanceProperties = new InstanceProperties();
        String methodName = "getInstanceProperties";
        OMRSRepositoryHelper repositoryHelper = getRepositoryConnector(serverName).getRepositoryHelper();

        repositoryHelper.addMapPropertyToInstance(SECURITY_OFFICER, instanceProperties, SECURITY_PROPERTIES, securityTagLevel.getSecurityProperties(), methodName);
        repositoryHelper.addStringArrayPropertyToInstance(SECURITY_OFFICER, instanceProperties, SECURITY_LABELS, securityTagLevel.getSecurityLabels(), methodName);

        return instanceProperties;
    }
}

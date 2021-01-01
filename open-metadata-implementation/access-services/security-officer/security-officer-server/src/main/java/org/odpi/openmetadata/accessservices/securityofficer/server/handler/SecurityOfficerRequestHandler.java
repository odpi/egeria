/*
 *  SPDX-License-Identifier: Apache-2.0
 *  Copyright Contributors to the ODPi Egeria project.
 */
package org.odpi.openmetadata.accessservices.securityofficer.server.handler;


import com.fasterxml.jackson.core.JsonProcessingException;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerEventType;
import org.odpi.openmetadata.accessservices.securityofficer.api.events.SecurityOfficerUpdateTagEvent;
import org.odpi.openmetadata.accessservices.securityofficer.api.ffdc.SecurityOfficerErrorCode;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecurityClassification;
import org.odpi.openmetadata.accessservices.securityofficer.api.model.SecuritySchemaElement;
import org.odpi.openmetadata.accessservices.securityofficer.server.publisher.SecurityOfficerPublisher;
import org.odpi.openmetadata.accessservices.securityofficer.server.utils.Builder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityProxyOnlyException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeDefNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.ATTRIBUTE_FOR_SCHEMA;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.RELATIONAL_TABLE;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SCHEMA_ATTRIBUTE_TYPE;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SCHEMA_ELEMENT;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_LABELS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_OFFICER;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_PROPERTIES;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SECURITY_TAGS;
import static org.odpi.openmetadata.accessservices.securityofficer.server.utils.Constants.SOURCE;

/**
 * SecurityOfficerRequestHandler supports REST requests for security officer function.
 */
public class SecurityOfficerRequestHandler
{

    private static final Logger log = LoggerFactory.getLogger(SecurityOfficerRequestHandler.class);
    private Builder builder = new Builder();

    private String                             serviceName;
    private String                             serverName;
    private RepositoryHandler                  repositoryHandler;
    private OMRSMetadataCollection             metadataCollection;
    private OMRSRepositoryHelper               repositoryHelper;
    private InvalidParameterHandler            invalidParameterHandler;
    private RepositoryErrorHandler             errorHandler;
    private OpenMetadataServerSecurityVerifier securityVerifier = new OpenMetadataServerSecurityVerifier();
    private List<String>                       supportedZones;
    private SecurityOfficerPublisher           securityOfficerPublisher;

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param serviceName             name of this service
     * @param serverName              name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler       manages calls to the repository services
     * @param repositoryHelper        provides utilities for manipulating the repository services objects
     * @param errorHandler            provides utilities for manipulating the repository services
     * @param supportedZones          setting of the supported zones for the handler
     * @param securityOfficerPublisher  outbound publisher
     */
    public SecurityOfficerRequestHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                  RepositoryHandler repositoryHandler, OMRSMetadataCollection metadataCollection, OMRSRepositoryHelper repositoryHelper, RepositoryErrorHandler errorHandler,
                                  List<String> supportedZones,
                                  SecurityOfficerPublisher securityOfficerPublisher) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.metadataCollection = metadataCollection;
        this.repositoryHandler = repositoryHandler;
        this.errorHandler = errorHandler;
        this.supportedZones = supportedZones;
        this.securityOfficerPublisher = securityOfficerPublisher;
    }

    public SecurityClassification getSecurityTagBySchemaElementId(String userId, String schemaElementId, String methodName) throws PropertyServerException {

        try {
            EntityDetail         entityDetail    = metadataCollection.getEntityDetail(userId, schemaElementId);
            List<Classification> classifications = entityDetail.getClassifications();
            return getSecurityClassification(classifications);
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    public List<SecuritySchemaElement> updateSecurityTagBySchemaElementId(String userId, String schemaElementId, SecurityClassification securityClassification, String methodName) throws PropertyServerException, RepositoryErrorException, ClassificationErrorException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException, InvalidParameterException, PropertyErrorException, EntityProxyOnlyException, TypeDefNotKnownException, TypeErrorException, PagingErrorException {

        try {
            InstanceProperties instanceProperties = getInstanceProperties(securityClassification);
            EntityDetail schemaElement = metadataCollection.getEntityDetail(userId, schemaElementId);

            SecuritySchemaElement securitySchemaElement = updateSecurityTagsClassificationForEntity(userId, instanceProperties, schemaElement, methodName);
            List<SecuritySchemaElement> result = new ArrayList<>();
            result.add(securitySchemaElement);

        updateSecurityTagOnColumns(userId, instanceProperties, schemaElement, result, securityClassification.getSecurityLabels(), methodName);

        return result;
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    public List<SecuritySchemaElement> deleteSecurityTagBySchemaElementId(String userId, String schemaElementId, String methodName) throws PropertyServerException, RepositoryErrorException, UserNotAuthorizedException, EntityProxyOnlyException, InvalidParameterException, EntityNotKnownException, ClassificationErrorException, TypeErrorException, TypeDefNotKnownException, PagingErrorException, FunctionNotSupportedException, PropertyErrorException {

        try {
            EntityDetail entityDetail = metadataCollection.getEntityDetail(userId, schemaElementId);
            EntityDetail response = deleteSecurityTagClassification(repositoryHelper, serverName, entityDetail, methodName);
            List<EntityDetail> deleteSecurityTagOnColumns = deleteSecurityTagOnColumns(entityDetail, userId, methodName);
            if (deleteSecurityTagOnColumns != null) {
                deleteSecurityTagOnColumns.add(response);
            return builder.buildSecuritySchemaElementList(deleteSecurityTagOnColumns, repositoryHelper);
            }
            return builder.buildSecuritySchemaElementList(Collections.singletonList(response), repositoryHelper);
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    private EntityDetail addSecurityTagsClassification(String userId, InstanceProperties instanceProperties, EntityDetail schemaElement, String methodName) throws PropertyServerException {
        try {
            if (schemaElement.getClassifications() != null && !schemaElement.getClassifications().isEmpty()) {
                return metadataCollection.updateEntityClassification(userId, schemaElement.getGUID(), SECURITY_TAGS, instanceProperties);
            } else {
                return metadataCollection.classifyEntity(userId, schemaElement.getGUID(), SECURITY_TAGS, instanceProperties);
            }
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    private EntityDetail deleteSecurityTagClassification(OMRSRepositoryHelper repositoryHelper, String serverName, EntityDetail entityDetail, String methodName) throws ClassificationErrorException {
        return repositoryHelper.deleteClassificationFromEntity(serverName, entityDetail, SECURITY_TAGS, "deleteSecurityTagClassification");
    }

    private void updateSecurityTagOnColumns(String userId, InstanceProperties instanceProperties,
                                            EntityDetail schemaElement, List<SecuritySchemaElement> result, List<String> newSecurityLabels, String methodName) throws UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, TypeErrorException, PagingErrorException, EntityNotKnownException, FunctionNotSupportedException, PropertyErrorException, ClassificationErrorException, PropertyServerException {
        if (isRelationalTable(schemaElement.getType().getTypeDefName())) {
            List<EntityDetail> columns = getColumns(userId, schemaElement, methodName);

            for (EntityDetail column : columns) {
                if (isMoreRestrictiveLabel(column.getClassifications(), newSecurityLabels)) {
                    SecuritySchemaElement columnUpdated = updateSecurityTagsClassificationForEntity(userId, instanceProperties, column, methodName);
                    result.add(columnUpdated);
                }
            }
        }
    }

    private List<EntityDetail> deleteSecurityTagOnColumns(EntityDetail schemaElement, String userId, String methodName) throws PropertyServerException {
        if (!isRelationalTable(schemaElement.getType().getTypeDefName())) {
            return Collections.emptyList();
        }

        List<EntityDetail> columns = getColumns(userId, schemaElement, methodName);
        if (columns.isEmpty()) {
            return Collections.emptyList();
        }

        List<EntityDetail> entitiesWithDeletedSecurityTags = new ArrayList<>();
        for (EntityDetail column : columns) {
            SecurityClassification securityTags = getSecurityClassification(column.getClassifications());
            if (securityTags != null) {
                if (isEligibleForDelete(securityTags, column.getType().getTypeDefName())) {
                    try {
                        deleteSecurityTagClassification(repositoryHelper, serverName, column, methodName);
                    } catch (Exception e) {
                        throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                                          this.getClass().getName(), methodName, e);
                    }
                    entitiesWithDeletedSecurityTags.add(column);
                } else {
                    log.debug("The security tag on the schema element should not be deleted because the source of the assignment is different.");
                }
            }
        }

        return entitiesWithDeletedSecurityTags;
    }

    private boolean isMoreRestrictiveLabel(List<Classification> classifications, List<String> newSecurityLabels) throws PropertyServerException {
        SecurityClassification securityClassification = getSecurityClassification(classifications);
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


    private List<EntityDetail> getColumns(String userId, EntityDetail schemaElement, String methodName) throws PropertyServerException {
        List<Relationship> tableTypeRelationship = getRelationshipsByType(userId, schemaElement.getGUID(), SCHEMA_ATTRIBUTE_TYPE, methodName);

        if (tableTypeRelationship != null && tableTypeRelationship.size() == 1) {
            EntityDetail tableType = getEntity(userId, schemaElement.getGUID(), tableTypeRelationship.get(0), methodName);
            if (tableType == null) {
                return Collections.emptyList();
            }
            List<Relationship> columnsRelationships = getRelationshipsByType(userId, tableType.getGUID(), ATTRIBUTE_FOR_SCHEMA, methodName);

            return columnsRelationships.stream().map(columnsRelationship -> getEntity(userId, tableType.getGUID(), columnsRelationship, methodName)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private List<Relationship> getRelationshipsByType(String userId, String entityGUID, String type, String methodName) throws PropertyServerException {
        String schemaAttributeTypeGuid = getTypeGUID(type);
        return getRelationships(userId, schemaAttributeTypeGuid, entityGUID, methodName);
    }

    private EntityDetail getEntity(String userId, String knownId, Relationship relationship, String methodName)  {
        String entityGUID = relationship.getEntityTwoProxy().getGUID().equals(knownId) ?
                relationship.getEntityOneProxy().getGUID() : relationship.getEntityTwoProxy().getGUID();
        try {
            return metadataCollection.getEntityDetail(userId, entityGUID);
        } catch (Exception e) {
            // throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
            //                                  this.getClass().getName(), methodName, e);
            return null;
        }
    }

    private List<Relationship> getRelationships(String userId, String relationshipTypeGUID, String entityGuid, String methodName) throws PropertyServerException {

        try {
            return metadataCollection.getRelationshipsForEntity(userId,
                                                                entityGuid,
                                                                relationshipTypeGUID,
                                                                0,
                                                                null,
                                                                null,
                                                                null,
                                                                null,
                                                                0);
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    private boolean isRelationalTable(String typeDefName) {
        return typeDefName.equals(RELATIONAL_TABLE);
    }

    private boolean isSecurityTag(String name) {
        return SECURITY_TAGS.equals(name);
    }

    private SecuritySchemaElement updateSecurityTagsClassificationForEntity(String userId, InstanceProperties instanceProperties,
                                                                            EntityDetail schemaElement, String methodName) throws PropertyServerException {
        EntityDetail entityDetail = addSecurityTagsClassification(userId, instanceProperties, schemaElement, methodName);

        SecuritySchemaElement schemaElementEntity = builder.buildSecuritySchemaElement(entityDetail, repositoryHelper);
        publishEventForUpdatedSecurityTags(schemaElement, schemaElementEntity);

        return schemaElementEntity;
    }

    private void publishEventForUpdatedSecurityTags(EntityDetail schemaElement, SecuritySchemaElement schemaElementEntity) throws PropertyServerException {
        SecurityOfficerUpdateTagEvent   event    = buildEventForUpdatedSecurityTags(schemaElement, schemaElementEntity);
        securityOfficerPublisher.publishEvent(event);
    }

    public List<SecurityClassification> getAvailableSecurityTags(String userId, String methodName) throws PropertyServerException, RepositoryErrorException, InvalidParameterException, TypeDefNotKnownException, UserNotAuthorizedException, TypeErrorException, FunctionNotSupportedException, ClassificationErrorException, PagingErrorException, PropertyErrorException {

        try {
        String entityTypeGuid = getTypeGUID(SCHEMA_ELEMENT);
        if (entityTypeGuid == null) {
            return Collections.emptyList();
        }

        List<EntityDetail> entitiesWithSecurityTagsAssigned = findEntitiesWithSecurityTagsAssigned(userId, metadataCollection, entityTypeGuid, methodName);
        if (entitiesWithSecurityTagsAssigned == null || entitiesWithSecurityTagsAssigned.isEmpty()) {
            return Collections.emptyList();
        }


        return entitiesWithSecurityTagsAssigned.stream().flatMap(entityDetail -> entityDetail.getClassifications().stream()).filter(classification -> isSecurityTag(classification.getName())).map(classification -> builder.buildSecurityTag(classification, repositoryHelper)).distinct().collect(Collectors.toList());
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    private List<EntityDetail> findEntitiesWithSecurityTagsAssigned(String userId, OMRSMetadataCollection metadataCollection, String entityTypeGuid, String methodName) throws PropertyServerException {
        try {
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
        } catch (Exception e) {
            throw new PropertyServerException(SecurityOfficerErrorCode.UNEXPECTED_REPOSITORY_EXCEPTION.getMessageDefinition(e.getClass().getName(), methodName, serviceName, serverName, e.getMessage()),
                                              this.getClass().getName(), methodName, e);
        }
    }

    private String getTypeGUID(String typeName) {
        TypeDef typeDefByName = repositoryHelper.getTypeDefByName(serviceName, typeName);
        if (typeDefByName != null) {
            return typeDefByName.getGUID();
        }
        return null;
    }

    private SecurityClassification getSecurityClassification(List<Classification> classifications) throws PropertyServerException {
        if (classifications == null || classifications.isEmpty()) {
            return null;
        }

        Optional<Classification> securityTag = classifications.stream().filter(classification -> classification.getName().equals(SECURITY_TAGS)).findAny();

        return securityTag.map(classification -> builder.buildSecurityTag(classification, repositoryHelper)).orElse(null);
    }

    private SecurityOfficerUpdateTagEvent buildEventForUpdatedSecurityTags(EntityDetail schemaElement, SecuritySchemaElement result) throws PropertyServerException {
        SecurityOfficerUpdateTagEvent event = new SecurityOfficerUpdateTagEvent();

        event.setSecuritySchemaElement(result);
        event.setPreviousClassification(getSecurityClassification(schemaElement.getClassifications()));
        event.setEventType(SecurityOfficerEventType.UPDATED_SECURITY_ASSIGNMENT);

        return event;
    }

    private InstanceProperties getInstanceProperties(SecurityClassification securityTagLevel) throws PropertyServerException {
        InstanceProperties instanceProperties = new InstanceProperties();
        String methodName = "getInstanceProperties";

        repositoryHelper.addMapPropertyToInstance(SECURITY_OFFICER, instanceProperties, SECURITY_PROPERTIES, securityTagLevel.getSecurityProperties(), methodName);
        repositoryHelper.addStringArrayPropertyToInstance(SECURITY_OFFICER, instanceProperties, SECURITY_LABELS, securityTagLevel.getSecurityLabels(), methodName);

        return instanceProperties;
    }


}

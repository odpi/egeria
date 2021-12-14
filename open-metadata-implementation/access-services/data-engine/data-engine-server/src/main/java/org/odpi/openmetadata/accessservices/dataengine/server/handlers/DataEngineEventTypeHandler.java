/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EventTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.List;
import java.util.Optional;

import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler.TOPIC_GUID_PARAMETER_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.EVENT_TYPE_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;

public class DataEngineEventTypeHandler {
    private final String serviceName;
    private final String serverName;
    private final OMRSRepositoryHelper repositoryHelper;
    private final InvalidParameterHandler invalidParameterHandler;
    private final EventTypeHandler<EventType> eventTypeHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;
    private final DataEngineTopicHandler dataEngineTopicHandler;
    private final DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;

    public static final String EVENT_TYPE_GUID_PARAMETER_NAME = "eventTypeGUID";

    public DataEngineEventTypeHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                      OMRSRepositoryHelper repositoryHelper, EventTypeHandler<EventType> eventTypeHandler,
                                      DataEngineRegistrationHandler dataEngineRegistrationHandler, DataEngineCommonHandler dataEngineCommonHandler,
                                      DataEngineTopicHandler dataEngineTopicHandler,
                                      DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.eventTypeHandler = eventTypeHandler;
        this.registrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.dataEngineTopicHandler = dataEngineTopicHandler;
        this.dataEngineSchemaAttributeHandler = dataEngineSchemaAttributeHandler;
    }

    public String upsertEventType(String userId, EventType eventType, String topicQualifiedName, String externalSourceName) throws
                                                                                                                            InvalidParameterException,
                                                                                                                            PropertyServerException,
                                                                                                                            UserNotAuthorizedException {
        final String methodName = "upsertEventType";
        return upsertEventType(userId, eventType, externalSourceName, registrationHandler.getExternalDataEngine(userId, externalSourceName),
                getTopicGUID(userId, topicQualifiedName), methodName);
    }

    private String getTopicGUID(String userId, String topicQualifiedName) throws UserNotAuthorizedException, PropertyServerException,
                                                                                 InvalidParameterException {
        final String methodName = "getTopicGUID";

        invalidParameterHandler.validateName(topicQualifiedName, QUALIFIED_NAME_PROPERTY_NAME, topicQualifiedName);

        Optional<EntityDetail> topicEntity = dataEngineTopicHandler.findTopicEntity(userId, topicQualifiedName);
        if (topicEntity.isEmpty()) {
            dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.TOPIC_NOT_FOUND, methodName, topicQualifiedName);
        }
        return topicEntity.get().getGUID();
    }

    private String upsertEventType(String userId, EventType eventType, String externalSourceName, String externalSourceGUID, String topicGUID,
                                   String methodName) throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        validateParameters(userId, methodName, eventType.getQualifiedName(), eventType.getDisplayName());

        Optional<EntityDetail> originalEventTypeEntity =
                dataEngineCommonHandler.findEntity(userId, eventType.getQualifiedName(), EVENT_TYPE_TYPE_NAME);
        String eventTypeGUID;
        if (originalEventTypeEntity.isEmpty()) {
            eventTypeGUID = eventTypeHandler.createEventType(userId, externalSourceGUID, externalSourceName, topicGUID, TOPIC_GUID_PARAMETER_NAME,
                    eventType.getQualifiedName(), eventType.getDisplayName(), eventType.getDescription(), eventType.getVersionNumber(),
                    eventType.getIsDeprecated(), eventType.getAuthor(), eventType.getUsage(), eventType.getEncodingStandard(),
                    eventType.getNamespace(), eventType.getAdditionalProperties(), EVENT_TYPE_TYPE_NAME, null, methodName);
        } else {
            eventTypeGUID = originalEventTypeEntity.get().getGUID();
            eventTypeHandler.updateEventType(userId, externalSourceGUID, externalSourceName, eventTypeGUID, EVENT_TYPE_GUID_PARAMETER_NAME,
                    eventType.getQualifiedName(), eventType.getDisplayName(), eventType.getDescription(), eventType.getVersionNumber(),
                    eventType.getIsDeprecated(), eventType.getAuthor(), eventType.getUsage(), eventType.getEncodingStandard(),
                    eventType.getNamespace(), eventType.getAdditionalProperties(), EVENT_TYPE_TYPE_NAME, null, true, methodName);
        }

        List<Attribute> attributeList = eventType.getAttributeList();
        if (CollectionUtils.isNotEmpty(attributeList)) {
            upsertEventSchemaAttributes(userId, attributeList, externalSourceName, externalSourceGUID, eventTypeGUID);
        }

        return eventTypeGUID;
    }

    private void upsertEventSchemaAttributes(String userId, List<Attribute> attributeList, String externalSourceName, String externalSourceGUID,
                                             String schemaTypeGUID) throws UserNotAuthorizedException, PropertyServerException,
                                                                           InvalidParameterException {
        for (Attribute eventSchemaAttribute : attributeList) {
            Optional<EntityDetail> schemaAttributeEntity =
                    dataEngineSchemaAttributeHandler.findSchemaAttributeEntity(userId, eventSchemaAttribute.getQualifiedName());
            if (schemaAttributeEntity.isEmpty()) {
                eventSchemaAttribute.setTypeName(EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME);
                dataEngineSchemaAttributeHandler.createSchemaAttribute(userId, schemaTypeGUID, eventSchemaAttribute, externalSourceName);
            } else {
                String schemaAttributeGUID = schemaAttributeEntity.get().getGUID();
                dataEngineSchemaAttributeHandler
                        .updateSchemaAttribute(userId, externalSourceGUID, externalSourceName, schemaAttributeGUID, eventSchemaAttribute);
            }
        }
    }

    /**
     * Verifies if the parameters are valid for a request
     *
     * @param userId        the name of the calling user
     * @param methodName    name of the calling method
     * @param qualifiedName the qualified name
     * @param displayName   the display name
     *
     * @throws InvalidParameterException the bean properties are invalid
     */
    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(displayName, DISPLAY_NAME_PROPERTY_NAME, methodName);
    }

    public void upsertEventTypes(String userId, List<EventType> eventTypes, String topicQualifiedName, String externalSourceName) throws
                                                                                                                                  InvalidParameterException,
                                                                                                                                  PropertyServerException,
                                                                                                                                  UserNotAuthorizedException {
        final String methodName = "upsertEventType";
        if (CollectionUtils.isEmpty(eventTypes)) {
            return;
        }

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        String topicGUID = getTopicGUID(userId, topicQualifiedName);

        for (EventType eventType : eventTypes) {
            upsertEventType(userId, eventType, externalSourceName, externalSourceGUID, topicGUID, methodName);
        }
    }

    public void removeEventType(String userId, String eventTypeGUID, String qualifiedName, String externalSourceName,
                                DeleteSemantic deleteSemantic) throws FunctionNotSupportedException, InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException {
        final String methodName = "removeEventType";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        eventTypeHandler.removeEventType(userId, externalSourceGUID, externalSourceName, eventTypeGUID, EVENT_TYPE_GUID_PARAMETER_NAME, qualifiedName,
                methodName);

    }
}

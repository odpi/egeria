/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.collections4.CollectionUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.Attribute;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EventTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Date;
import java.util.List;
import java.util.Optional;



/**
 * DataEngineEventTypeHandler manages event type objects. It runs server-side in the
 * DataEngine OMAS and creates and retrieves collections entities through the OMRSRepositoryConnector.
 */
public class DataEngineEventTypeHandler {
    private final InvalidParameterHandler invalidParameterHandler;
    private final EventTypeHandler<EventType> eventTypeHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;
    private final DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler;
    protected static final String EVENT_TYPE_GUID_PARAMETER_NAME = "eventTypeGUID";

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler          handler for managing parameter errors
     * @param eventTypeHandler                 provides utilities specific for manipulating event type entities
     * @param dataEngineCommonHandler          provides utilities for manipulating entities
     * @param dataEngineRegistrationHandler    provides utilities for engine entities
     * @param dataEngineSchemaAttributeHandler provides utilities specific for schema attribute entities
     */
    public DataEngineEventTypeHandler(InvalidParameterHandler invalidParameterHandler, EventTypeHandler<EventType> eventTypeHandler,
                                      DataEngineRegistrationHandler dataEngineRegistrationHandler, DataEngineCommonHandler dataEngineCommonHandler,
                                      DataEngineSchemaAttributeHandler dataEngineSchemaAttributeHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.eventTypeHandler = eventTypeHandler;
        this.registrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.dataEngineSchemaAttributeHandler = dataEngineSchemaAttributeHandler;
    }

    /**
     * Create or update the event type with event schema attributes
     *
     * @param userId             the name of the calling user
     * @param eventType          the values of the event type
     * @param topicGUID          the unique identifier of the topic
     * @param externalSourceName the unique name of the external source
     * @return unique identifier of the event type in the repository
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertEventType(String userId, EventType eventType, String topicGUID, String externalSourceName) throws
            InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "upsertEventType";

        validateParameters(userId, methodName, eventType.getQualifiedName(), eventType.getDisplayName());

        Optional<EntityDetail> originalEventTypeEntity = dataEngineCommonHandler.findEntity(userId, eventType.getQualifiedName(),
                                                                                            OpenMetadataType.EVENT_TYPE_TYPE_NAME);
        String eventTypeGUID;
        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Date now = dataEngineCommonHandler.getNow();
        if (originalEventTypeEntity.isEmpty()) {
            eventTypeHandler.verifyExternalSourceIdentity(userId, externalSourceGUID, externalSourceName,
                    false, false, null, null);
            eventTypeGUID = eventTypeHandler.createEventType(userId, externalSourceGUID, externalSourceName, topicGUID, OpenMetadataProperty.GUID.name,
                                                             eventType.getQualifiedName(), eventType.getDisplayName(), eventType.getDescription(), eventType.getVersionNumber(),
                                                             eventType.getIsDeprecated(), eventType.getAuthor(), eventType.getUsage(), eventType.getEncodingStandard(),
                                                             eventType.getNamespace(), eventType.getAdditionalProperties(), OpenMetadataType.EVENT_TYPE_TYPE_NAME, null,
                                                             null, null, false, false, now, methodName);
        } else {
            eventTypeGUID = originalEventTypeEntity.get().getGUID();
            eventTypeHandler.updateEventType(userId, externalSourceGUID, externalSourceName, eventTypeGUID, EVENT_TYPE_GUID_PARAMETER_NAME,
                    eventType.getQualifiedName(), eventType.getDisplayName(), eventType.getDescription(), eventType.getVersionNumber(),
                    eventType.getIsDeprecated(), eventType.getAuthor(), eventType.getUsage(), eventType.getEncodingStandard(),
                    eventType.getNamespace(), eventType.getAdditionalProperties(), OpenMetadataType.EVENT_TYPE_TYPE_NAME, null,
                    null, null, true, false, false, now, methodName);
        }

        List<Attribute> attributeList = eventType.getAttributeList();
        if (CollectionUtils.isNotEmpty(attributeList)) {
            attributeList.forEach(column -> {
                column.setTypeName(OpenMetadataType.EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME);
                column.setTypeGuid(OpenMetadataType.EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID);
            });
        }
        dataEngineSchemaAttributeHandler.upsertSchemaAttributes(userId, attributeList, externalSourceName, externalSourceGUID, eventTypeGUID);

        return eventTypeGUID;
    }

    /**
     * Verifies if the parameters are valid for a request
     *
     * @param userId        the name of the calling user
     * @param methodName    name of the calling method
     * @param qualifiedName the qualified name
     * @param displayName   the display name
     * @throws InvalidParameterException the bean properties are invalid
     */
    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, OpenMetadataProperty.QUALIFIED_NAME.name, methodName);
        invalidParameterHandler.validateName(displayName, OpenMetadataProperty.DISPLAY_NAME.name, methodName);
    }

    /**
     * Remove the event type
     *
     * @param userId             the name of the calling user
     * @param eventTypeGUID      unique identifier of the event type to be removed
     * @param qualifiedName      event type's qualified name
     * @param externalSourceName the external data engine name
     * @param deleteSemantic     the delete semantic
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeEventType(String userId, String eventTypeGUID, String qualifiedName, String externalSourceName,
                                DeleteSemantic deleteSemantic) throws FunctionNotSupportedException, InvalidParameterException,
            PropertyServerException,
            UserNotAuthorizedException {
        final String methodName = "removeEventType";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        eventTypeHandler.removeEventType(userId, externalSourceGUID, externalSourceName, eventTypeGUID, EVENT_TYPE_GUID_PARAMETER_NAME, qualifiedName,
                false, false, dataEngineCommonHandler.getNow(), methodName);
    }
}

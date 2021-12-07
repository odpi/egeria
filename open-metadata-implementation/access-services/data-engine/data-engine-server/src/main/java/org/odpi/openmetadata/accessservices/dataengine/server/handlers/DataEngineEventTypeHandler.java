package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.accessservices.dataengine.model.EventType;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.EventTypeHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.Optional;

import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler.TOPIC_GUID_PARAMETER_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
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

    public static final String EVENT_TYPE_GUID_PARAMETER_NAME = "eventTypeGUID";

    public DataEngineEventTypeHandler(String serviceName, String serverName, InvalidParameterHandler invalidParameterHandler,
                                      OMRSRepositoryHelper repositoryHelper, EventTypeHandler<EventType> eventTypeHandler,
                                      DataEngineRegistrationHandler dataEngineRegistrationHandler, DataEngineCommonHandler dataEngineCommonHandler,
                                      DataEngineTopicHandler dataEngineTopicHandler) {
        this.serviceName = serviceName;
        this.serverName = serverName;
        this.invalidParameterHandler = invalidParameterHandler;
        this.repositoryHelper = repositoryHelper;
        this.eventTypeHandler = eventTypeHandler;
        this.registrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
        this.dataEngineTopicHandler = dataEngineTopicHandler;
    }

    public String upsertEventType(String userId, EventType eventType, String topicQualifiedName, String externalSourceName) throws
                                                                                                                            InvalidParameterException,
                                                                                                                            PropertyServerException,
                                                                                                                            UserNotAuthorizedException {
        final String methodName = "upsertEventType";
        validateParameters(userId, methodName, eventType.getQualifiedName(), eventType.getDisplayName(), topicQualifiedName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalEventTypeEntity = dataEngineCommonHandler.findEntity(userId, eventType.getQualifiedName(), EVENT_TYPE_TYPE_NAME);

        String eventTypeGUID;
        if (originalEventTypeEntity.isEmpty()) {
            Optional<EntityDetail> topicEntity = dataEngineTopicHandler.findTopicEntity(userId, topicQualifiedName);
            if (topicEntity.isEmpty()) {
                dataEngineCommonHandler.throwInvalidParameterException(DataEngineErrorCode.TOPIC_NOT_FOUND, methodName, topicQualifiedName);
            }
            String topicGUID = topicEntity.get().getGUID();

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
        return eventTypeGUID;
    }

    /**
     * Verifies if the parameters are valid for a request
     *
     * @param userId        the name of the calling user
     * @param methodName    name of the calling method
     *
     * @param qualifiedName the qualified name
     * @param displayName   the display name
     * @param topicQualifiedName the qualified name of the topic
     * @throws InvalidParameterException the bean properties are invalid
     */
    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName, String topicQualifiedName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(displayName, DISPLAY_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(topicQualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }
}

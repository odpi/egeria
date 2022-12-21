/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.apache.commons.lang3.StringUtils;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.DISPLAY_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TOPIC_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TOPIC_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.TOPIC_TYPE_PROPERTY_NAME;

/**
 * DataEngineTopicHandler manages topic objects. It runs server-side in the
 * DataEngine OMAS and creates and retrieves collections entities through the OMRSRepositoryConnector.
 */
public class DataEngineTopicHandler {
    private final InvalidParameterHandler invalidParameterHandler;
    private final AssetHandler<Topic> topicHandler;
    private final DataEngineCommonHandler dataEngineCommonHandler;
    private final DataEngineRegistrationHandler registrationHandler;

    public static final String TOPIC_GUID_PARAMETER_NAME = "topicGUID";

    /**
     * Construct the handler information needed to interact with the repository services
     *
     * @param invalidParameterHandler       handler for managing parameter errors
     * @param topicHandler                  provides utilities specific for manipulating topic entities
     * @param dataEngineCommonHandler       provides utilities for manipulating entities
     * @param dataEngineRegistrationHandler provides utilities for engine entities
     */
    public DataEngineTopicHandler(InvalidParameterHandler invalidParameterHandler, AssetHandler<Topic> topicHandler,
                                  DataEngineRegistrationHandler dataEngineRegistrationHandler,
                                  DataEngineCommonHandler dataEngineCommonHandler) {
        this.invalidParameterHandler = invalidParameterHandler;
        this.topicHandler = topicHandler;
        this.registrationHandler = dataEngineRegistrationHandler;
        this.dataEngineCommonHandler = dataEngineCommonHandler;
    }

    /**
     * Create or update the topic with event types
     *
     * @param userId             the name of the calling user
     * @param topic              the values of the topic
     * @param externalSourceName the unique name of the external source
     *
     * @return unique identifier of the topic in the repository
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public String upsertTopic(String userId, Topic topic, String externalSourceName) throws InvalidParameterException, PropertyServerException,
                                                                                            UserNotAuthorizedException {
        final String methodName = "upsertTopic";
        validateParameters(userId, methodName, topic.getQualifiedName(), topic.getDisplayName());

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        Optional<EntityDetail> originalTopicEntity = findTopicEntity(userId, topic.getQualifiedName());

        Map<String, Object> extendedProperties = new HashMap<>();
        if (StringUtils.isNotEmpty(topic.getTopicType())) {
            extendedProperties.put(TOPIC_TYPE_PROPERTY_NAME, topic.getTopicType());
        }
        int ownerTypeOrdinal = dataEngineCommonHandler.getOwnerTypeOrdinal(topic.getOwnerType());
        Date now = dataEngineCommonHandler.getNow();
        String topicGUID;
        if (originalTopicEntity.isEmpty()) {
            topicHandler.verifyExternalSourceIdentity(userId, externalSourceGUID, externalSourceName,
                    false, false, null, methodName);
            topicGUID = topicHandler.createAssetInRepository(userId, externalSourceGUID, externalSourceName, topic.getQualifiedName(),
                     topic.getDisplayName(), null, topic.getDescription(), topic.getZoneMembership(), topic.getOwner(), ownerTypeOrdinal,
                     topic.getOriginOrganizationGUID(), topic.getOriginBusinessCapabilityGUID(), topic.getOtherOriginValues(),
                     topic.getAdditionalProperties(), TOPIC_TYPE_GUID, TOPIC_TYPE_NAME, extendedProperties,
                     null, null, InstanceStatus.ACTIVE, now, methodName);
        } else {
            topicGUID = originalTopicEntity.get().getGUID();
            topicHandler.updateAsset(userId, externalSourceGUID, externalSourceName, topicGUID, TOPIC_GUID_PARAMETER_NAME,
                    topic.getQualifiedName(), topic.getDisplayName(), null, topic.getDescription(), topic.getAdditionalProperties(),
                    TOPIC_TYPE_GUID, TOPIC_TYPE_NAME, extendedProperties, null, null, true,
                    false, false, now, methodName);
        }

        return topicGUID;
    }

    /**
     * Find out if the Topic object is already stored in the repository. It uses the fully qualified name to retrieve the entity
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualifiedName name of the process to be searched
     *
     * @return optional with entity details if found, empty optional if not found
     *
     * @throws InvalidParameterException  the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    public Optional<EntityDetail> findTopicEntity(String userId, String qualifiedName) throws UserNotAuthorizedException,
                                                                                              PropertyServerException,
                                                                                              InvalidParameterException {
        return dataEngineCommonHandler.findEntity(userId, qualifiedName, TOPIC_TYPE_NAME);
    }

    /**
     * Verifies if the parameters are valid for a request
     *
     * @param userId        the name of the calling user
     * @param qualifiedName the qualified name
     * @param displayName   the display name
     * @param methodName    name of the calling method
     *
     * @throws InvalidParameterException the bean properties are invalid
     */
    private void validateParameters(String userId, String methodName, String qualifiedName, String displayName) throws InvalidParameterException {
        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, QUALIFIED_NAME_PROPERTY_NAME, methodName);
        invalidParameterHandler.validateName(displayName, DISPLAY_NAME_PROPERTY_NAME, methodName);
    }

    /**
     * Remove the topic
     *
     * @param userId             the name of the calling user
     * @param topicGUID          unique identifier of the topic to be removed
     * @param externalSourceName the external data engine name
     * @param deleteSemantic     the delete semantic
     *
     * @throws InvalidParameterException     the bean properties are invalid
     * @throws UserNotAuthorizedException    user not authorized to issue this request
     * @throws PropertyServerException       problem accessing the property server
     * @throws FunctionNotSupportedException the repository does not support this call.
     */
    public void removeTopic(String userId, String topicGUID, String externalSourceName, DeleteSemantic deleteSemantic) throws
                                                                                                                       FunctionNotSupportedException,
                                                                                                                       InvalidParameterException,
                                                                                                                       PropertyServerException,
                                                                                                                       UserNotAuthorizedException {
        final String methodName = "removeTopic";
        dataEngineCommonHandler.validateDeleteSemantic(deleteSemantic, methodName);

        String externalSourceGUID = registrationHandler.getExternalDataEngine(userId, externalSourceName);
        topicHandler.deleteBeanInRepository(userId, externalSourceGUID, externalSourceName, topicGUID, TOPIC_GUID_PARAMETER_NAME,
                TOPIC_TYPE_GUID, TOPIC_TYPE_NAME, null, null, false,
                false, dataEngineCommonHandler.getNow(), methodName);
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.DeleteSemantic;
import org.odpi.openmetadata.accessservices.dataengine.model.Topic;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.odpi.openmetadata.accessservices.dataengine.server.handlers.DataEngineTopicHandler.TOPIC_GUID_PARAMETER_NAME;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineTopicHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String DISPLAY_NAME = "name";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";
    private static final String GUID = "guid";

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private AssetHandler<Topic> topicHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @InjectMocks
    private DataEngineTopicHandler dataEngineTopicHandler;


    @Test
    void upsertTopic_create() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.TOPIC_TYPE_NAME)).thenReturn(Optional.empty());

        Topic topic = getTopic();
        dataEngineTopicHandler.upsertTopic(USER, topic, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(topicHandler, times(1)).createAssetInRepository(USER, EXTERNAL_SOURCE_DE_GUID,
               EXTERNAL_SOURCE_DE_QUALIFIED_NAME, QUALIFIED_NAME, DISPLAY_NAME, null, null, null, null,
               0, null, null, null, null,
                                                               OpenMetadataType.TOPIC_TYPE_GUID, OpenMetadataType.TOPIC_TYPE_NAME, new HashMap<>(), null, null, InstanceStatus.ACTIVE, null, "upsertTopic");
    }

    @Test
    void upsertTopic_update() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);
        mockFindTopic();

        Topic topic = getTopic();
        dataEngineTopicHandler.upsertTopic(USER, topic, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(topicHandler, times(1)).updateAsset(USER, EXTERNAL_SOURCE_DE_GUID,
                 EXTERNAL_SOURCE_DE_QUALIFIED_NAME, GUID, TOPIC_GUID_PARAMETER_NAME, QUALIFIED_NAME, DISPLAY_NAME, null,
                 null, null, OpenMetadataType.TOPIC_TYPE_GUID, OpenMetadataType.TOPIC_TYPE_NAME, new HashMap<>(), null, null,
                 true, false, false, null, "upsertTopic");
    }

    @Test
    void findTopicEntity() throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
        EntityDetail entityDetail = mockFindTopic();

        Optional<EntityDetail> result = dataEngineTopicHandler.findTopicEntity(USER, QUALIFIED_NAME);
        assertTrue(result.isPresent());
        assertEquals(entityDetail, result.get());
    }

    @Test
    void removeTopic() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, FunctionNotSupportedException {
        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME)).thenReturn(EXTERNAL_SOURCE_DE_GUID);

        dataEngineTopicHandler.removeTopic(USER, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, DeleteSemantic.SOFT);

        verify(dataEngineCommonHandler, times(1)).validateDeleteSemantic(DeleteSemantic.SOFT, "removeTopic");
        verify(topicHandler, times(1)).deleteBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                GUID, TOPIC_GUID_PARAMETER_NAME, OpenMetadataType.TOPIC_TYPE_GUID, OpenMetadataType.TOPIC_TYPE_NAME, null, null, false, false, null,"removeTopic");
    }

    private Topic getTopic() {
        Topic topic = new Topic();

        topic.setQualifiedName(QUALIFIED_NAME);
        topic.setDisplayName(DISPLAY_NAME);

        return topic;
    }

    private EntityDetail mockFindTopic() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, OpenMetadataType.TOPIC_TYPE_NAME)).thenReturn(Optional.of(entityDetail));
        return entityDetail;
    }
}
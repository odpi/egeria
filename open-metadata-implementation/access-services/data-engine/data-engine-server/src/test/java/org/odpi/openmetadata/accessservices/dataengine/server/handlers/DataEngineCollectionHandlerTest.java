/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.handlers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.dataengine.model.Collection;
import org.odpi.openmetadata.accessservices.dataengine.server.builders.CollectionBuilder;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.lang.reflect.InvocationTargetException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.accessservices.dataengine.server.util.MockedExceptionUtil.mockException;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.COLLECTION_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.COLLECTION_TYPE_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.REFERENCEABLE_TO_COLLECTION_TYPE_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineCollectionHandlerTest {
    private static final String USER = "user";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String NAME = "name";
    private static final String GUID = "guid";
    private static final String PROCESS_GUID = "processGuid";
    private static final String EXTERNAL_SOURCE_DE_GUID = "externalSourceDataEngineGuid";
    private static final String EXTERNAL_SOURCE_DE_QUALIFIED_NAME = "externalSourceDataEngineQualifiedName";


    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private AssetHandler<Asset> assetHandler;

    @Mock
    private DataEngineRegistrationHandler dataEngineRegistrationHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Spy
    @InjectMocks
    private DataEngineCollectionHandler dataEngineCollectionHandler;

    @Test
    void createCollectionTest() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        String methodName = "createCollection";

        Collection collection = getCollection();
        CollectionBuilder mockedBuilder = Mockito.mock(CollectionBuilder.class);

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        when(assetHandler.createBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, COLLECTION_TYPE_GUID,
                COLLECTION_TYPE_NAME, QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, mockedBuilder, methodName)).thenReturn(GUID);

        doReturn(mockedBuilder).when(dataEngineCollectionHandler).getCollectionBuilder(collection);

        String result = dataEngineCollectionHandler.createCollection(USER, collection, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        assertEquals(GUID, result);
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateName(QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, methodName);
    }

    @Test
    void createProcessUserNotAuthorizedExceptionTest() throws UserNotAuthorizedException, PropertyServerException,
                                                              NoSuchMethodException,
                                                              InstantiationException,
                                                              IllegalAccessException, InvalidParameterException, InvocationTargetException {
        String methodName = "createCollection";

        Collection collection = getCollection();
        CollectionBuilder mockedBuilder = Mockito.mock(CollectionBuilder.class);

        when(dataEngineRegistrationHandler.getExternalDataEngine(USER, EXTERNAL_SOURCE_DE_QUALIFIED_NAME))
                .thenReturn(EXTERNAL_SOURCE_DE_GUID);

        UserNotAuthorizedException mockedException = mockException(UserNotAuthorizedException.class, methodName);
        doThrow(mockedException).when(assetHandler).createBeanInRepository(USER, EXTERNAL_SOURCE_DE_GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME,
                COLLECTION_TYPE_GUID, COLLECTION_TYPE_NAME, QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, mockedBuilder, methodName);

        doReturn(mockedBuilder).when(dataEngineCollectionHandler).getCollectionBuilder(collection);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                dataEngineCollectionHandler.createCollection(USER, collection, EXTERNAL_SOURCE_DE_QUALIFIED_NAME));

        assertTrue(thrown.getMessage().contains("OMAS-DATA-ENGINE-404-001 "));
    }

    @Test
    void findProcessTest() throws UserNotAuthorizedException, PropertyServerException, InvalidParameterException {
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID);
        Optional<EntityDetail> optionalOfMockedEntity = Optional.of(entityDetail);
        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, COLLECTION_TYPE_NAME)).thenReturn(optionalOfMockedEntity);

        Optional<EntityDetail> result = dataEngineCollectionHandler.findCollectionEntity(USER, QUALIFIED_NAME);

        assertTrue(result.isPresent());
        assertEquals(GUID, result.get().getGUID());
    }

    @Test
    void findProcessNotExistingTest() throws UserNotAuthorizedException, PropertyServerException,
                                             InvalidParameterException {

        when(dataEngineCommonHandler.findEntity(USER, QUALIFIED_NAME, COLLECTION_TYPE_NAME)).thenReturn(Optional.empty());

        Optional<EntityDetail> result = dataEngineCollectionHandler.findCollectionEntity(USER, QUALIFIED_NAME);

        assertFalse(result.isPresent());
    }

    @Test
    void addProcessCollectionRelationshipTest() throws UserNotAuthorizedException, PropertyServerException,
                                                       InvalidParameterException {
        dataEngineCollectionHandler.addCollectionMembershipRelationship(USER, PROCESS_GUID, GUID, EXTERNAL_SOURCE_DE_QUALIFIED_NAME);

        verify(dataEngineCommonHandler, times(1)).upsertExternalRelationship(USER, PROCESS_GUID, GUID,
                REFERENCEABLE_TO_COLLECTION_TYPE_NAME, COLLECTION_TYPE_NAME, EXTERNAL_SOURCE_DE_QUALIFIED_NAME, null);
    }


    private Collection getCollection() {
        Collection collection = new Collection();
        collection.setQualifiedName(QUALIFIED_NAME);
        collection.setName(NAME);
        return collection;
    }

}
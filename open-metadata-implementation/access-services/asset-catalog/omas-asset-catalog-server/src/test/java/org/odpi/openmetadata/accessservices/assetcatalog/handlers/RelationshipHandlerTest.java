/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.service.ClockService;
import org.odpi.openmetadata.accessservices.assetcatalog.util.Constants;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.generichandlers.AssetHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RelationshipHandlerTest {

    private static final String RELATIONSHIP_GUID = "212123-abc";
    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private static final String RELATIONSHIP_TYPE_GUID = "adadad-bcba-123";
    public static final String SERVER_NAME = "server-name";
    private final String USER = "test-user";
    private final String RELATIONSHIP_TYPE = "SemanticAssigment";
    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private AssetHandler<AssetCatalogBean> assetHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    ClockService clockService;

    @InjectMocks
    private RelationshipHandler relationshipHandler;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void getRelationshipBetweenEntities()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, RepositoryErrorException {
        String methodName = "getRelationshipBetweenEntities";

        Relationship mock = mockRelationship();
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);
        mockMetadataCollection();

        when(assetHandler.getUniqueAttachmentLink(USER, FIRST_GUID,
                Constants.GUID_PARAMETER, "", RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE, SECOND_GUID,
                "", 0, false, false, null, methodName)).thenReturn(mock);

        org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship
                result = relationshipHandler.getRelationshipBetweenEntities(USER, SERVER_NAME, FIRST_GUID, SECOND_GUID,
                RELATIONSHIP_TYPE);

        assertNotNull(result);
        assertEquals(mock.getGUID(), result.getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, "entity1GUID", methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SECOND_GUID, "entity2GUID", methodName);
    }

    @Test
    public void getRelationshipBetweenEntities_throwsUserNotAuthorizedException()
            throws PropertyServerException, UserNotAuthorizedException, RepositoryErrorException, InvalidParameterException{
        String methodName = "getRelationshipBetweenEntities";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);
        mockMetadataCollection();

        UserNotAuthorizedException mockedException = new UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(), this.getClass().getName(), "", "");
        doThrow(mockedException).when(assetHandler).getUniqueAttachmentLink(USER, FIRST_GUID,
                Constants.GUID_PARAMETER, "", RELATIONSHIP_TYPE_GUID, RELATIONSHIP_TYPE, SECOND_GUID,
                "", 0, false, false, null, methodName);

        assertThrows(UserNotAuthorizedException.class, () ->
                relationshipHandler.getRelationshipBetweenEntities(USER, SERVER_NAME, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE));

    }

    @Test
    public void getRelationshipBetweenEntities_throwsInvalidParameterException() throws InvalidParameterException {

        String methodName = "getRelationshipBetweenEntities";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        InvalidParameterException mockedException = new InvalidParameterException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getMessageDefinition(), this.getClass().getName(), "", "");

        doThrow(mockedException).when(invalidParameterHandler).validateGUID(FIRST_GUID, "entity1GUID", methodName);

        assertThrows(InvalidParameterException.class, () ->
                relationshipHandler.getRelationshipBetweenEntities(USER, SERVER_NAME, FIRST_GUID, SECOND_GUID, RELATIONSHIP_TYPE));

    }

    private void mockTypeDef(String typeName, String typeGUID) {
        TypeDef entityTypeDef = mock(TypeDef.class);
        when(repositoryHelper.getTypeDefByName(USER, typeName)).thenReturn(entityTypeDef);

        when(entityTypeDef.getName()).thenReturn(typeName);
        when(entityTypeDef.getGUID()).thenReturn(typeGUID);
    }

    private Relationship mockRelationship() {
        Relationship relationship = mock(Relationship.class);
        when(relationship.getGUID()).thenReturn(RELATIONSHIP_GUID);
        return relationship;
    }

    private void mockMetadataCollection() throws RepositoryErrorException {
        OMRSMetadataCollection metadataCollection = mock(OMRSMetadataCollection.class);

        when(repositoryHandler.getMetadataCollection()).thenReturn(metadataCollection);

        when(metadataCollection.getMetadataCollectionId(USER)).thenReturn("metadataCollectionID");
        when(repositoryHelper.getMetadataCollectionName("metadataCollectionID")).thenReturn("metadataCollectionName");
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class RelationshipHandlerTest {

    private static final String RELATIONSHIP_GUID = "212123-abc";
    private final String USER = "test-user";
    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private final String RELATIONSHIP_TYPE = "SemanticAssigment";
    private static final String RELATIONSHIP_TYPE_GUID = "adadad-bcba-123";

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @InjectMocks
    private RelationshipHandler relationshipHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getRelationshipBetweenEntities()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getRelationshipBetweenEntities";

        Relationship mock = mockRelationship();
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        when(repositoryHandler.getRelationshipBetweenEntities(USER,
                FIRST_GUID,
                "",
                SECOND_GUID,
                RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE,
                methodName)).thenReturn(mock);

        org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship
                result = relationshipHandler.getRelationshipBetweenEntities(USER,
                FIRST_GUID,
                SECOND_GUID,
                RELATIONSHIP_TYPE);


        assertNotNull(result);
        assertEquals(mock.getGUID(), result.getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, "entity1GUID", methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(SECOND_GUID, "entity2GUID", methodName);
    }

    @Test
    public void getRelationshipBetweenEntities_throwsUserNotAuthorizedException()
            throws PropertyServerException, UserNotAuthorizedException{
        String methodName = "getRelationshipBetweenEntities";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        UserNotAuthorizedException mockedException = new UserNotAuthorizedException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getHttpErrorCode(),
                this.getClass().getName(), "", "", "", "", "");
        doThrow(mockedException).when(repositoryHandler).getRelationshipBetweenEntities(USER,
                FIRST_GUID,
                "",
                SECOND_GUID,
                RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE,
                methodName);

        UserNotAuthorizedException thrown = assertThrows(UserNotAuthorizedException.class, () ->
                relationshipHandler.getRelationshipBetweenEntities(USER,
                        FIRST_GUID,
                        SECOND_GUID,
                        RELATIONSHIP_TYPE));

    }

    @Test
    public void getRelationshipBetweenEntities_throwsInvalidParameterException() {

        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);


        InvalidParameterException thrown = assertThrows(InvalidParameterException.class, () ->
                relationshipHandler.getRelationshipBetweenEntities(null,
                        null,
                        null,
                        null));


    }

    @Test
    public void getRelationshipBetweenEntities_throwsPropertyServerException()
            throws UserNotAuthorizedException, PropertyServerException{
        String methodName = "getRelationshipBetweenEntities";
        mockTypeDef(RELATIONSHIP_TYPE, RELATIONSHIP_TYPE_GUID);

        PropertyServerException mockedException = new PropertyServerException(AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED.getHttpErrorCode(),
                this.getClass().getName(), "", "", "", "");

        doThrow(mockedException).when(repositoryHandler).getRelationshipBetweenEntities(USER,
                FIRST_GUID,
                "",
                SECOND_GUID,
                RELATIONSHIP_TYPE_GUID,
                RELATIONSHIP_TYPE,
                methodName);

        PropertyServerException thrown = assertThrows(PropertyServerException.class, () ->
                relationshipHandler.getRelationshipBetweenEntities(USER,
                        FIRST_GUID,
                        SECOND_GUID,
                        RELATIONSHIP_TYPE));

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

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.handlers;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryErrorHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AssetCatalogHandlerTest {

    private static final String RELATIONSHIP_GUID = "212123-abc";
    private static final String ASSET_TYPE = "Process";
    private static final String GUID_PARAMETER = "assetGUID";
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

    @Mock
    private RepositoryErrorHandler errorHandler;

    @InjectMocks
    private AssetCatalogHandler assetCatalogHandler;

    @Before
    public void before() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getEntityDetails() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        String methodName = "getEntityDetails";

        EntityDetail mock = mockEntityDetails();

        when(repositoryHandler.getEntityByGUID(USER,
                FIRST_GUID,
                GUID_PARAMETER,
                ASSET_TYPE,
                methodName)).thenReturn(mock);


        AssetDescription result = assetCatalogHandler.getEntityDetails(USER, FIRST_GUID, ASSET_TYPE);


        assertEquals(mock.getGUID(), result.getGuid());
        verify(invalidParameterHandler, times(1)).validateUserId(USER, methodName);
        verify(invalidParameterHandler, times(1)).validateGUID(FIRST_GUID, GUID_PARAMETER, methodName);
    }

    private EntityDetail mockEntityDetails() {
        EntityDetail entityDetail = new EntityDetail();
        entityDetail.setGUID(FIRST_GUID);
        return entityDetail;
    }

    private TypeDef mockTypeDef() {
        TypeDef entityTypeDef = mock(TypeDef.class);
        entityTypeDef.setName(RELATIONSHIP_TYPE);
        entityTypeDef.setGUID(RELATIONSHIP_TYPE_GUID);
        return entityTypeDef;
    }

    private Relationship mockRelationship() {
        Relationship relationship = new Relationship();
        relationship.setGUID(RELATIONSHIP_GUID);
        return relationship;
    }

}

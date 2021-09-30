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
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.Identifiers;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.REFERENCEABLE_TYPE_GUID;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.REFERENCEABLE_TYPE_NAME;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
class DataEngineFinderHandlerTest {

    private static final String USER = "user";
    private static final String METHOD = "method";
    private static final String QUALIFIED_NAME = "qualifiedName";
    private static final String EXTERNAL_SOURCE_NAME = "externalSourceName";
    private static final String GUID_VALUE = "1";

    @Mock
    private OMRSRepositoryHelper repositoryHelper;

    @Mock
    private RepositoryHandler repositoryHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @InjectMocks
    private DataEngineFindHandler dataEngineFindHandler;

    @Test
    void find() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        mockRepositoryHelper();
        mockRepositoryHandler();

        FindRequestBody findRequestBody = buildFindRequestBody();
        GUIDListResponse guidListResponse = dataEngineFindHandler.find(findRequestBody, USER, METHOD);

        assertEquals(1, guidListResponse.getGUIDs().size());

        verify(repositoryHelper, times(1)).getExactMatchRegex(QUALIFIED_NAME, false);
        verify(repositoryHelper, times(1)).getTypeDefByName(USER, REFERENCEABLE_TYPE_NAME);
        verify(repositoryHelper, times(1))
                .getSearchPropertiesFromInstanceProperties(eq(USER), any(), eq(MatchCriteria.ALL));

        verify(repositoryHandler, times(1)).findEntities(eq(USER), eq(REFERENCEABLE_TYPE_GUID), eq(null), any(), any(),
                eq(null), eq(null), eq(null), any(), eq(0), eq(50), eq(METHOD));
    }

    private void mockRepositoryHelper(){
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME, false)).thenReturn(QUALIFIED_NAME);

        TypeDef referenceableTypeDef = mock(TypeDef.class);
        when(referenceableTypeDef.getGUID()).thenReturn(REFERENCEABLE_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(USER, REFERENCEABLE_TYPE_NAME)).thenReturn(referenceableTypeDef);

        when(repositoryHelper.getSearchPropertiesFromInstanceProperties(eq(USER), any(), eq(MatchCriteria.ALL)))
                .thenReturn(mock(SearchProperties.class));
    }

    private void mockRepositoryHandler() throws UserNotAuthorizedException, PropertyServerException {
        List<EntityDetail> findResult = buildFindResult();
        when(repositoryHandler.findEntities(eq(USER), eq(REFERENCEABLE_TYPE_GUID), eq(null), any(), any(),
                eq(null), eq(null), eq(null), any(), eq(0), eq(50), eq(METHOD)))
                .thenReturn(findResult);
    }

    private FindRequestBody buildFindRequestBody(){
        Identifiers identifiers = new Identifiers();
        identifiers.setQualifiedName(QUALIFIED_NAME);
        identifiers.setExternalSourceName(EXTERNAL_SOURCE_NAME);

        FindRequestBody findRequestBody = new FindRequestBody();
        findRequestBody.setType(REFERENCEABLE_TYPE_NAME);
        findRequestBody.setIdentifiers(identifiers);

        return findRequestBody;
    }

    private List<EntityDetail> buildFindResult(){
        EntityDetail entityDetail = mock(EntityDetail.class);
        when(entityDetail.getGUID()).thenReturn(GUID_VALUE);
        when(entityDetail.getMetadataCollectionName()).thenReturn(EXTERNAL_SOURCE_NAME);

        List<EntityDetail> findResult = new ArrayList<>();
        findResult.add(entityDetail);
        return findResult;
    }

}

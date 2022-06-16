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
import org.odpi.openmetadata.accessservices.dataengine.model.Referenceable;
import org.odpi.openmetadata.accessservices.dataengine.rest.FindRequestBody;
import org.odpi.openmetadata.accessservices.dataengine.rest.Identifiers;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIGenericHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PagingErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME;
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
    private OpenMetadataAPIGenericHandler<Referenceable> genericHandler;

    @Mock
    private InvalidParameterHandler invalidParameterHandler;

    @Mock
    private DataEngineCommonHandler dataEngineCommonHandler;

    @Mock
    private OMRSMetadataCollection omrsMetadataCollection;

    @InjectMocks
    private DataEngineFindHandler dataEngineFindHandler;

    @Test
    void find() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException, FunctionNotSupportedException,
            org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException, RepositoryErrorException,
            PropertyErrorException, TypeErrorException, PagingErrorException {
        mockRepositoryHelper();
        List<EntityDetail> findResult = buildFindResult();

        when(genericHandler.getEntitiesByValue(USER, QUALIFIED_NAME, QUALIFIED_NAME_PROPERTY_NAME, REFERENCEABLE_TYPE_GUID,
                REFERENCEABLE_TYPE_NAME, Collections.singletonList(QUALIFIED_NAME_PROPERTY_NAME), true,
                null, null, false, false,
                0, invalidParameterHandler.getMaxPagingSize(), null, METHOD)).thenReturn(findResult);

        FindRequestBody findRequestBody = buildFindRequestBody();
        GUIDListResponse guidListResponse = dataEngineFindHandler.find(findRequestBody, USER, METHOD);

        assertEquals(1, guidListResponse.getGUIDs().size());
        verify(repositoryHelper, times(1)).getTypeDefByName(USER, REFERENCEABLE_TYPE_NAME);
    }

    private void mockRepositoryHelper(){
        when(repositoryHelper.getExactMatchRegex(QUALIFIED_NAME, false)).thenReturn(QUALIFIED_NAME);

        TypeDef referenceableTypeDef = mock(TypeDef.class);
        when(referenceableTypeDef.getGUID()).thenReturn(REFERENCEABLE_TYPE_GUID);
        when(repositoryHelper.getTypeDefByName(USER, REFERENCEABLE_TYPE_NAME)).thenReturn(referenceableTypeDef);
    }

    private FindRequestBody buildFindRequestBody(){
        Identifiers identifiers = new Identifiers();
        identifiers.setQualifiedName(QUALIFIED_NAME);

        FindRequestBody findRequestBody = new FindRequestBody();
        findRequestBody.setType(REFERENCEABLE_TYPE_NAME);
        findRequestBody.setExternalSourceName(EXTERNAL_SOURCE_NAME);
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

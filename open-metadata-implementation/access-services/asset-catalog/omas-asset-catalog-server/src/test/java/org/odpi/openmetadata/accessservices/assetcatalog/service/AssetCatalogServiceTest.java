/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.CommonHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetCatalogBean;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Elements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetCatalogResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.ClassificationListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.RelationshipListResponse;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

public class AssetCatalogServiceTest {

    private static final String USER = "test-user";
    private static final String SERVER_NAME = "omas";
    private static final String FIRST_GUID = "ababa-123-acbd";
    private static final String ASSET_TYPE = "Process";
    private static final String SECOND_GUID = "ababc-2134-2341f";
    private static final String RELATIONSHIP_TYPE = "SemanticAssigment";
    private static final String RELATIONSHIP_TYPE_GUID = "adadad-bcba-123";
    private static final String CLASSIFICATION_NAME = "Confidentiality";
    private static final String SEARCH_CRITERIA = "employee";

    @Mock
    RESTExceptionHandler restExceptionHandler;

    @Mock
    private AssetCatalogInstanceHandler instanceHandler;

    @InjectMocks
    private AssetCatalogRESTService assetCatalogRESTService;

    @Mock
    private AssetCatalogHandler assetCatalogHandler;

    @Mock
    private CommonHandler commonHandler;

    @Before
    public void before() {
        MockitoAnnotations.openMocks(this);

        Field instanceHandlerField = ReflectionUtils.findField(AssetCatalogRESTService.class, "instanceHandler");
        if(instanceHandlerField != null) {
            instanceHandlerField.setAccessible(true);
            ReflectionUtils.setField(instanceHandlerField, assetCatalogRESTService, instanceHandler);
            instanceHandlerField.setAccessible(false);
        }

        Field restExceptionHandlerField = ReflectionUtils.findField(AssetCatalogRESTService.class, "restExceptionHandler");
        if(restExceptionHandlerField != null) {
            restExceptionHandlerField.setAccessible(true);
            ReflectionUtils.setField(restExceptionHandlerField, assetCatalogRESTService, restExceptionHandler);
            restExceptionHandlerField.setAccessible(false);
        }
    }

    @Test
    public void testGetAssetDetailsByGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {

        AssetCatalogBean response = mockAssetDescription(FIRST_GUID);

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetDetailsByGUID"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntityDetails(USER, FIRST_GUID, ASSET_TYPE))
                .thenReturn(response);

        AssetCatalogResponse assetDetailsByGUID = assetCatalogRESTService.getAssetDetailsByGUID(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE);
        assertEquals(FIRST_GUID, assetDetailsByGUID.getAssetCatalogBean().getGuid());
        assertEquals(response.getGuid(), assetDetailsByGUID.getAssetCatalogBean().getGuid());
        assertEquals(response.getType().getName(), assetDetailsByGUID.getAssetCatalogBean().getType().getName());
    }

    @Test
    public void testGetAssetUniverseByGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetCatalogBean response = mockAssetDescription(FIRST_GUID);

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetUniverseByGUID"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntityDetails(USER, FIRST_GUID, ASSET_TYPE))
                .thenReturn(response);

        AssetCatalogResponse assetDetailsByGUID = assetCatalogRESTService.getAssetUniverseByGUID(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE);
        assertEquals(FIRST_GUID, assetDetailsByGUID.getAssetCatalogBean().getGuid());
        assertEquals(response.getGuid(), assetDetailsByGUID.getAssetCatalogBean().getGuid());
        assertEquals(response.getType().getName(), assetDetailsByGUID.getAssetCatalogBean().getType().getName());
        assertNotNull(assetDetailsByGUID.getAssetCatalogBean().getRelationships());
    }


    @Test
    public void testGetAssetRelationships() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<Relationship> response = new ArrayList<>();
        response.add(mockRelationshipResponse());

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetRelationships"))
                .thenReturn(assetCatalogHandler);
        when(commonHandler
                .getTypeDefGUID(USER, RELATIONSHIP_TYPE))
                .thenReturn(RELATIONSHIP_TYPE_GUID);

        when(assetCatalogHandler
                .getRelationships(USER, FIRST_GUID, ASSET_TYPE, RELATIONSHIP_TYPE, 0, 10))
                .thenReturn(response);

        RelationshipListResponse assetRelationships = assetCatalogRESTService.getAssetRelationships(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE,
                RELATIONSHIP_TYPE,
                0,
                10);

        assertEquals(RELATIONSHIP_TYPE_GUID, assetRelationships.getRelationships().get(0).getGuid());
        assertEquals(response.get(0).getGuid(), assetRelationships.getRelationships().get(0).getGuid());
        assertEquals(response.get(0).getType().getName(), assetRelationships.getRelationships().get(0).getType().getName());
    }

    @Test
    public void testGetClassificationByAssetGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        List<Classification> response = new ArrayList<>();
        response.add(mockClassification(CLASSIFICATION_NAME));

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getClassificationByAssetGUID"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntityClassificationByName(USER, FIRST_GUID, ASSET_TYPE, CLASSIFICATION_NAME))
                .thenReturn(response);

        ClassificationListResponse classificationByAssetGUID = assetCatalogRESTService.getClassificationByAssetGUID(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE,
                CLASSIFICATION_NAME);

        assertEquals(CLASSIFICATION_NAME, classificationByAssetGUID.getClassifications().get(0).getName());
        assertEquals(response.get(0).getName(), classificationByAssetGUID.getClassifications().get(0).getName());
    }

    @Test
    public void testSearchByType()
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase {
        SearchParameters searchParameters = mockSearchParams();
        List<Elements> response = new ArrayList<>();
        response.add(mockTerm(FIRST_GUID));

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "searchByType"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .searchByType(USER, SEARCH_CRITERIA, searchParameters))
                .thenReturn(response);

        AssetListResponse assetResponse = assetCatalogRESTService.searchByType(SERVER_NAME,
                USER,
                SEARCH_CRITERIA,
                searchParameters);

        assertEquals(response.get(0).getGuid(), assetResponse.getElementsList().get(0).getGuid());
    }

    @Test
    public void testBuildContext() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        Elements response = mockTerm(FIRST_GUID);

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "buildContext"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .buildContextByType(USER, FIRST_GUID, ASSET_TYPE))
                .thenReturn(response);

        AssetResponse assetResponse = assetCatalogRESTService.buildContext(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE);

        assertEquals(response.getGuid(), assetResponse.getAsset().getGuid());
    }

    private Elements mockTerm(String guid) {
        Elements elements = new Elements();
        elements.setGuid(guid);
        return elements;
    }

    private SearchParameters mockSearchParams() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setPageSize(10);
        searchParameters.setFrom(0);
        return searchParameters;
    }

    private Classification mockClassification(String classificationName) {
        Classification classification = new Classification();
        classification.setName(classificationName);
        return classification;
    }

    private AssetCatalogBean mockAssetDescription(String guid) {
        AssetCatalogBean assetCatalogBean = new AssetCatalogBean();
        assetCatalogBean.setGuid(guid);
        Type type = new Type();
        type.setName(ASSET_TYPE);
        assetCatalogBean.setType(type);
        assetCatalogBean.setRelationships(Collections.singletonList(mockRelationshipResponse()));
        return assetCatalogBean;
    }

    private Relationship mockRelationshipResponse() {
        Relationship relationshipsResponse = new Relationship();

        Type type = new Type();
        type.setName(RELATIONSHIP_TYPE);
        relationshipsResponse.setType(type);
        relationshipsResponse.setGuid(RELATIONSHIP_TYPE_GUID);
        relationshipsResponse.setFromEntity(mockElement(FIRST_GUID));
        relationshipsResponse.setToEntity(mockElement(SECOND_GUID));
        return relationshipsResponse;
    }

    private Element mockElement(String guid) {
        Element asset = new Element();
        asset.setGuid(guid);
        return asset;
    }

}

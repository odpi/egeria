/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.admin.AssetCatalogInstanceHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.AssetCatalogHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.handlers.CommonHandler;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservices.assetcatalog.model.AssetElements;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Classification;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Element;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservices.assetcatalog.model.Type;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionListResponse;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.AssetDescriptionResponse;
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
        MockitoAnnotations.initMocks(this);

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

        AssetDescription response = mockAssetDescription(FIRST_GUID);

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetDetailsByGUID"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntityDetails(USER, FIRST_GUID, ASSET_TYPE))
                .thenReturn(response);

        AssetDescriptionResponse assetDetailsByGUID = assetCatalogRESTService.getAssetDetailsByGUID(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE);
        assertEquals(FIRST_GUID, assetDetailsByGUID.getAssetDescription().getGuid());
        assertEquals(response.getGuid(), assetDetailsByGUID.getAssetDescription().getGuid());
        assertEquals(response.getType().getName(), assetDetailsByGUID.getAssetDescription().getType().getName());
    }

    @Test
    public void testGetAssetUniverseByGUID() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetDescription response = mockAssetDescription(FIRST_GUID);

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetUniverseByGUID"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntityDetails(USER, FIRST_GUID, ASSET_TYPE))
                .thenReturn(response);

        AssetDescriptionResponse assetDetailsByGUID = assetCatalogRESTService.getAssetUniverseByGUID(SERVER_NAME,
                USER,
                FIRST_GUID,
                ASSET_TYPE);
        assertEquals(FIRST_GUID, assetDetailsByGUID.getAssetDescription().getGuid());
        assertEquals(response.getGuid(), assetDetailsByGUID.getAssetDescription().getGuid());
        assertEquals(response.getType().getName(), assetDetailsByGUID.getAssetDescription().getType().getName());
        assertNotNull(assetDetailsByGUID.getAssetDescription().getRelationships());
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
    public void testGetIntermediateAssets() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AssetCatalogException {
        List<AssetDescription> response = new ArrayList<>();
        response.add(mockAssetDescription(FIRST_GUID));

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getLinkingAssets"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getIntermediateAssets(USER, FIRST_GUID, SECOND_GUID))
                .thenReturn(response);

        AssetDescriptionListResponse assetDescriptionListResponse = assetCatalogRESTService.getLinkingAssets(SERVER_NAME,
                USER,
                FIRST_GUID,
                SECOND_GUID);

        assertEquals(response.get(0).getGuid(), assetDescriptionListResponse.getAssetDescriptionList().get(0).getGuid());
    }

    @Test
    public void testGetLinkingRelationships() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AssetCatalogException {
        List<Relationship> response = new ArrayList<>();
        response.add(mockRelationshipResponse());

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getLinkingRelationships"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getLinkingRelationshipsBetweenAssets(SERVER_NAME, USER, FIRST_GUID, SECOND_GUID))
                .thenReturn(response);

        RelationshipListResponse linkingRelationships = assetCatalogRESTService.getLinkingRelationships(SERVER_NAME,
                USER,
                FIRST_GUID,
                SECOND_GUID);

        assertEquals(response.get(0).getGuid(), linkingRelationships.getRelationships().get(0).getGuid());
    }

    @Test
    public void testGetAssetsFromNeighborhood()
            throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException, AssetCatalogException {
        SearchParameters searchParameters = mockSearchParams();
        List<AssetDescription> response = new ArrayList<>();
        response.add(mockAssetDescription(FIRST_GUID));

        when(instanceHandler.getAssetCatalogHandler(USER,
                SERVER_NAME,
                "getAssetsFromNeighborhood"))
                .thenReturn(assetCatalogHandler);

        when(assetCatalogHandler
                .getEntitiesFromNeighborhood(SERVER_NAME, USER, FIRST_GUID, searchParameters))
                .thenReturn(response);

        AssetDescriptionListResponse assetsFromNeighborhood = assetCatalogRESTService.getAssetsFromNeighborhood(SERVER_NAME,
                USER,
                FIRST_GUID,
                searchParameters);

        assertEquals(response.get(0).getGuid(), assetsFromNeighborhood.getAssetDescriptionList().get(0).getGuid());
    }

    @Test
    public void testSearchByType()
            throws org.odpi.openmetadata.frameworks.connectors.ffdc.OCFCheckedExceptionBase {
        SearchParameters searchParameters = mockSearchParams();
        List<AssetElements> response = new ArrayList<>();
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

        assertEquals(response.get(0).getGuid(), assetResponse.getAssetElementsList().get(0).getGuid());
    }

    @Test
    public void testBuildContext() throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException {
        AssetElements response = mockTerm(FIRST_GUID);

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

    private AssetElements mockTerm(String guid) {
        AssetElements assetElements = new AssetElements();
        assetElements.setGuid(guid);
        return assetElements;
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

    private AssetDescription mockAssetDescription(String guid) {
        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGuid(guid);
        Type type = new Type();
        type.setName(ASSET_TYPE);
        assetDescription.setType(type);
        assetDescription.setRelationships(Collections.singletonList(mockRelationshipResponse()));
        return assetDescription;
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

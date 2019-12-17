/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.view.service.assetsearch.services;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClientBase;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.assetcatalog.AssetCatalog;
import org.odpi.openmetadata.accessservices.assetcatalog.model.*;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.responses.*;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.PropertyServerException;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.UserNotAuthorizedException;
import org.odpi.openmetadata.commonservices.multitenant.OMAGServerPlatformInstanceMap;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.userinterface.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.viewservices.assetsearch.admin.serviceinstances.AssetSearchViewServicesInstance;
import org.odpi.openmetadata.viewservices.assetsearch.services.AssetSearchViewRESTServices;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class AssetSearchOMVSTest {


    private static final String SERVER_URL = "http://localhost:8081";
    private static final String SERVER_NAME = "TestServer";
    private static final String UI_SERVER_NAME = "UITestServer";
    private static final String USER_ID = "zebra91";
    private static final String GLOSSARY_TERM_TYPE_ID = "66d7f872-19bd-439c-98ae-c3fe49d8f420";
    private static final String GLOSSARY_TERM_TYPE_NAME = "GlossaryTerm";
    private static final String ASSET_TYPE = "Asset";
    private static final String SEARCH_CRITERIA = "employee";
    private static final String SECOND_ASSET_ID = "66d7f872-19bd-439c-98ae-3232430022";
    private static final String SECOND_ASSET_TYPE = "RelationalColumn";
    private static final String RELATIONSHIP_TYPE = "SemanticAssignment";
    private static final String CONFIDENTIALITY = "Confidentiality";
    private static final String COMPLEX_SCHEMA_TYPE = "ComplexSchemaType";

    private static final SearchParameters SEARCH_PARAMETERS = new SearchParameters();
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;

    private AssetCatalog assetCatalog;

    @InjectMocks
    private AssetSearchViewRESTServices assetSearchViewRESTServices;

    @Mock
    private RESTClientConnector connector;

    @Before
    public void before() throws Exception {
        MockitoAnnotations.initMocks(this);
        assetCatalog = initializeAssetCatalogClient();
        Field connectorField = FFDCRESTClientBase.class.getDeclaredField("clientConnector");
        if (connectorField != null) {
            connectorField.setAccessible(true);
            connectorField.set(assetCatalog, connector);
            connectorField.setAccessible(false);
        }
    }

    @Test
    public void testGetAssetDetails() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();

        when(connector.callGetRESTCall(eq("getAssetDetails"), eq(AssetDescriptionResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), eq(response.getAssetDescriptionList().get(0).getGuid()), eq(ASSET_TYPE))).thenReturn(response);

        List<AssetDescription> resultList = assetSearchViewRESTServices.getAssetDetails(SERVER_NAME, USER_ID, GLOSSARY_TERM_TYPE_ID, ASSET_TYPE);
        verifyAssetDescriptionResult(resultList);

    }

    @Test
    public void testGetAssetUniverse() throws Exception {
        AssetDescriptionResponse response = mockAssetDescriptionResponse();

        when(connector.callGetRESTCall(eq("getAssetUniverse"), eq(AssetDescriptionResponse.class), anyString(), eq(SERVER_NAME),
                eq(USER_ID), eq(response.getAssetDescriptionList().get(0).getGuid()), eq(ASSET_TYPE))).thenReturn(response);

        List<AssetDescription> assetElements  = assetSearchViewRESTServices.getAssetUniverse(SERVER_NAME, USER_ID,
                response.getAssetDescriptionList().get(0).getGuid(),
                ASSET_TYPE);

        Assert.assertEquals(response.getAssetDescriptionList().get(0).getGuid(), assetElements.get(0).getGuid());
    }

    @Test
    public void testGetAssetRelationships() throws Exception {
        RelationshipsResponse response = mockRelationshipsResponse();

        when(connector.callGetRESTCall(eq("getAssetRelationships"), eq(RelationshipsResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(GLOSSARY_TERM_TYPE_ID),
                eq(ASSET_TYPE),
                eq(COMPLEX_SCHEMA_TYPE),
                eq(FROM),
                eq(PAGE_SIZE))).thenReturn(response);

        List<Relationship> relationships = assetSearchViewRESTServices.getAssetRelationships(SERVER_NAME,
                USER_ID,
                GLOSSARY_TERM_TYPE_ID,
                ASSET_TYPE,
                COMPLEX_SCHEMA_TYPE,
                FROM,
                PAGE_SIZE);

        Assert.assertEquals(COMPLEX_SCHEMA_TYPE, relationships.get(0).getType().getName());
    }

    @Test
    public void testGetClassificationsForAsset() throws Exception {
        ClassificationsResponse response = mockClassificationsResponse();

        when(connector.callGetRESTCall(eq("getClassificationsForAsset"),
                eq(ClassificationsResponse.class),
                anyString(),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(GLOSSARY_TERM_TYPE_ID),
                eq(ASSET_TYPE),
                eq(CONFIDENTIALITY))).thenReturn(response);

        List<Classification> classifications = assetSearchViewRESTServices.getClassificationsForAsset(
                SERVER_NAME,
                USER_ID,
                GLOSSARY_TERM_TYPE_ID,
                ASSET_TYPE,
                CONFIDENTIALITY);

        Assert.assertEquals(CONFIDENTIALITY,classifications.get(0).getName());
    }

    @Test
    public void testSearchAssets() throws Exception {
        AssetResponse response = mockAssetResponse();

        when(connector.callPostRESTCall(eq("searchByType"),
                eq(AssetResponse.class),
                anyString(),
                eq(SEARCH_PARAMETERS),
                eq(SERVER_NAME),
                eq(USER_ID),
                eq(SEARCH_CRITERIA))).thenReturn(response);

        List<AssetElements> assetElements = assetSearchViewRESTServices.searchAssets(SERVER_NAME,
                USER_ID,
                SEARCH_CRITERIA,
                SEARCH_PARAMETERS);

        Assert.assertEquals(response.getAssets().get(0).getGuid(), assetElements.get(0).getGuid());
    }



    private AssetResponse mockAssetResponse() {
        AssetResponse assetResponse = new AssetResponse();
        assetResponse.setAssets(Collections.singletonList(mockTerm()));
        return assetResponse;
    }

    private AssetElements mockTerm() {
        AssetElements assetElements = new AssetElements();
        assetElements.setGuid(GLOSSARY_TERM_TYPE_ID);
        assetElements.setType(createNamedType(GLOSSARY_TERM_TYPE_NAME));
        return assetElements;
    }

    private RelationshipResponse mockRelationshipResponse() {
        RelationshipResponse relationshipsResponse = new RelationshipResponse();
        relationshipsResponse.setRelationship(mockRelationship());
        return relationshipsResponse;
    }

    private SearchParameters mockSearchParameters() {
        SearchParameters searchParameters = new SearchParameters();

        searchParameters.setLevel(2);
        searchParameters.setFrom(0);
        searchParameters.setPageSize(10);

        return searchParameters;
    }

    private ClassificationsResponse mockClassificationsResponse() {
        ClassificationsResponse classificationsResponse = new ClassificationsResponse();
        classificationsResponse.setClassifications(mockClassifications());
        return classificationsResponse;
    }


    private RelationshipsResponse mockRelationshipsResponse() {
        RelationshipsResponse relationshipsResponse = new RelationshipsResponse();
        relationshipsResponse.setRelationships(mockRelationships());
        return relationshipsResponse;
    }


    private AssetDescriptionResponse mockAssetDescriptionResponse() {
        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();
        expectedResponse.setRelatedHTTPCode(200);

        AssetDescription assetDescription = mockAssetDescription();
        expectedResponse.setAssetDescriptionList(Collections.singletonList(assetDescription));

        return expectedResponse;
    }

    private AssetDescription mockAssetDescription() {
        AssetDescription assetDescription = new AssetDescription();

        assetDescription.setGuid(GLOSSARY_TERM_TYPE_ID);
        assetDescription.setType(createNamedType(GLOSSARY_TERM_TYPE_NAME));

        assetDescription.setClassifications(mockClassifications());
        assetDescription.setRelationships(mockRelationships());

        return assetDescription;
    }

    private List<Relationship> mockRelationships() {
        return Collections.singletonList(mockRelationship());
    }

    private Relationship mockRelationship() {
        Relationship relationship = new Relationship();

        relationship.setFromEntity(mockAsset(GLOSSARY_TERM_TYPE_ID, ASSET_TYPE));
        relationship.setToEntity(mockAsset(SECOND_ASSET_ID, SECOND_ASSET_TYPE));
        relationship.setType(createNamedType(COMPLEX_SCHEMA_TYPE));

        return relationship;
    }

    private Element mockAsset(String defaultAssetId, String typeName) {
        Element asset = new Element();

        asset.setGuid(defaultAssetId);
        asset.setCreatedBy("admin");
        asset.setType(createNamedType(COMPLEX_SCHEMA_TYPE));

        return asset;
    }

    private Type createNamedType(String typeName) {
        Type type1 = new Type();
        type1.setName(typeName);
        return type1;
    }

    private List<Classification> mockClassifications() {
        return Collections.singletonList(mockClassification(CONFIDENTIALITY));
    }

    private Classification mockClassification(String classificationName) {
        Classification classification = new Classification();

        classification.setName(classificationName);
        classification.setCreatedBy("admin");
        classification.setStatus(InstanceStatus.ACTIVE.getName());

        return classification;
    }
    private AssetCatalog initializeAssetCatalogClient() throws InvalidParameterException {
        OMAGServerPlatformInstanceMap map = new OMAGServerPlatformInstanceMap();

        try {
            // shutdown the server if it is active.
            if (map.isServerActive(USER_ID,UI_SERVER_NAME)) {
                map.shutdownServerInstance(USER_ID, UI_SERVER_NAME, "initializeAssetCatalogClient");
            }
        } catch (PropertyServerException e) {
           // ignore the error. This call is just to ensure we have a clean static map before we start the test.
        } catch (UserNotAuthorizedException e) {
           // ignore the error. This call is just to ensure we have a clean static map before we start the test.
        }

        map.startUpServerInstance(USER_ID, UI_SERVER_NAME,null,null);
        AssetSearchViewServicesInstance instance =new AssetSearchViewServicesInstance(UI_SERVER_NAME,null, USER_ID,-1,SERVER_NAME,SERVER_URL);
        map.addServiceInstanceToPlatform(SERVER_NAME, ViewServiceDescription.ASSET_SEARCH.getViewServiceName(),instance);
        return instance.getAssetCatalogClient();
    }
    private void verifyAssetDescriptionResult(List<AssetDescription> resultList) {
        Assertions.assertFalse(resultList.isEmpty());
        AssetDescription assetDescription = resultList.get(0);
        Assertions.assertEquals(assetDescription.getGuid(), GLOSSARY_TERM_TYPE_ID);
    }
}
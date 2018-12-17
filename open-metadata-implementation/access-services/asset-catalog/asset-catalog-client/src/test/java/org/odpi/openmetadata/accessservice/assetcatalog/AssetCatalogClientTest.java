/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservice.assetcatalog;

import org.junit.Ignore;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservice.assetcatalog.client.AssetCatalog;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.AssetCatalogException;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.InvalidParameterException;
import org.odpi.openmetadata.accessservice.assetcatalog.exception.PropertyServerException;
import org.odpi.openmetadata.accessservice.assetcatalog.model.AssetDescription;
import org.odpi.openmetadata.accessservice.assetcatalog.model.Relationship;
import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.responses.AssetCatalogOMASAPIResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.responses.AssetDescriptionResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.responses.ClassificationsResponse;
import org.odpi.openmetadata.accessservice.assetcatalog.model.rest.responses.RelationshipsResponse;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
@Ignore
public class AssetCatalogClientTest {

    private static final String defaultOMASServerURL = "http://localhost:8081";
    private static final String defaultServerName = "TestServer";
    private static final String defaultUserId = "zebra91";
    private static final String defaultAssetId = "66d7f872-19bd-439c-98ae-c3fe49d8f420";
    private static final String defaultRelationshipId = "c7184523-7ca5-4876-9210-fe1bb1b55cd7";
    private static final String defaultRelationshipType = "SemanticAssignment";
    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private AssetCatalog assetCatalog = new AssetCatalog(defaultServerName, defaultOMASServerURL);

    @DisplayName("Asset Catalog - Test Invalid Server URL")
    @Test
    void assetCatalogInvalidServerURLTest() {
        AssetCatalog assetCatalog = new AssetCatalog(defaultServerName, "");

        Throwable thrown = assertThrows(PropertyServerException.class, () ->
                assetCatalog.getAssetSummary(defaultUserId, defaultAssetId));

        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-001"));
    }

    @DisplayName("Asset Catalog - Test Invalid User Id")
    @Test
    void assetCatalogInvalidUserId() {
        Throwable thrown = assertThrows(InvalidParameterException.class, () ->
                assetCatalog.getAssetSummary("", defaultAssetId));

        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-003"));
    }

    @DisplayName("Asset Catalog - Test Invalid Parameter Id")
    @Test
    void assetCatalogInvalidAssetId() {
        Throwable thrown = assertThrows(InvalidParameterException.class, () ->
                assetCatalog.getAssetSummary(defaultUserId, ""));

        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
    }

    @Test
    @DisplayName("Asset Summary")
    void getAssetSummaryTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getAssetSummary(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
        assertEquals(response.getAssetDescriptionList().size(), 1);
        assertEquals(defaultAssetId, response.getAssetDescriptionList().get(0).getGUID());
    }

    @Test
    @DisplayName("Asset Description")
    void getAssetDescriptionTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getAssetDetails(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
        assertEquals(response.getAssetDescriptionList().size(), 1);
        assertEquals(defaultAssetId, response.getAssetDescriptionList().get(0).getGUID());
    }

    @Test
    @DisplayName("Asset Universe")
    void getAssetUniverseTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getAssetUniverse(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
        assertEquals(response.getAssetDescriptionList().size(), 1);
        assertEquals(defaultAssetId, response.getAssetDescriptionList().get(0).getGUID());
    }

    @Ignore
    @Test
    @DisplayName("Asset Relationships")
    void getAssetRelationshipsTest() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        try {
            response = assetCatalog.getAssetRelationships(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Test
    @DisplayName("Asset Relationships for relationship type")
    void getAssetRelationshipsForTypeTest() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        try {
            response = assetCatalog.getAssetRelationshipsForType(defaultUserId, defaultAssetId, defaultRelationshipType);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Asset Classifications")
    void getAssetByClassificationTest() {
        defaultClassificationExpected();

        ClassificationsResponse response = new ClassificationsResponse();
        try {
            response = assetCatalog.getClassificationForAsset(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Asset by Classifications Name")
    void getAssetsByClassificationNameTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getAssetsByClassificationName(defaultUserId, "classificationName");
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Asset by Property Value")
    void getAssetsByPropertyTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getAssetsByProperty(defaultUserId, "propertyValue");
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Linking Relationships between assets")
    void getLinkingRelationshipsTest() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        final String assetId = "b827683c-2924-4dfd-a92d-7be1ddde2fd0";
        try {
            response = assetCatalog.getLinkingRelationships(defaultUserId, defaultAssetId, assetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Linking Assets")
    void getLinkingAssetsTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        final String assetId = "b827683c-2924-4dfd-a92d-7be1ddde2fd0";
        try {
            response = assetCatalog.getLinkingAssets(defaultUserId, defaultAssetId, assetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Related Assets")
    void getRelatedAssetsTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getRelatedAssets(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Asset From Neighborhood")
    void getAssetsFromNeighborhoodTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getAssetsFromNeighborhood(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Relationships From Neighborhood")
    void getRelationshipsFromNeighborhoodTest() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        try {
            response = assetCatalog.getRelationshipsFromNeighborhood(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Last Created Assets")
    void getLastCreatedAssetsTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getLastCreatedAssets(defaultUserId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Last Updated Assets")
    void getLastUpdatedAssetsTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.getLastUpdatedAssets(defaultUserId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Test
    @DisplayName("Relationship by Id")
    void getRelationshipTest() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        try {
            response = assetCatalog.getRelationship(defaultUserId, defaultAssetId);
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Test
    @DisplayName("Relationships By Label")
    void getRelationshipByLabelTest() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        try {
            response = assetCatalog.getRelationshipByLabel(defaultUserId, "getRelationshipByLabel");
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Search For Relationships")
    void searchForRelationships() {
        defaultRelationshipExpected();

        RelationshipsResponse response = new RelationshipsResponse();
        String relationshipTypeId = "b827683c-2924-4df3-a92d-7be1888e23c0";
        try {
            response = assetCatalog.searchForRelationships(defaultUserId, relationshipTypeId, "test");
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    @Ignore
    @Test
    @DisplayName("Search For Assets")
    void searchAssetsTest() {
        defaultAssetExpected();

        AssetDescriptionResponse response = new AssetDescriptionResponse();
        try {
            response = assetCatalog.searchAssets(defaultUserId, "test");
        } catch (AssetCatalogException e) {
            e.printStackTrace();
        }

        checkResponse(response);
    }

    private void defaultAssetExpected() {
        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();
        expectedResponse.setRelatedHTTPCode(200);

        AssetDescription assetDescription = new AssetDescription();
        assetDescription.setGUID(defaultAssetId);
        List<AssetDescription> list = new ArrayList<>(1);
        list.add(assetDescription);
        expectedResponse.setAssetDescriptionList(list);

        when(restTemplate.getForObject(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);
    }

    private void defaultRelationshipExpected() {
        RelationshipsResponse expectedResponse = new RelationshipsResponse();
        expectedResponse.setRelatedHTTPCode(200);

        List<Relationship> relationships = new ArrayList<>();
        Relationship relationship = new Relationship();
        relationship.setGUID(defaultRelationshipId);
        relationships.add(relationship);
        expectedResponse.setRelationships(relationships);

        when(restTemplate.getForObject(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);
    }

    private void defaultClassificationExpected() {
        ClassificationsResponse expectedResponse = new ClassificationsResponse();
        expectedResponse.setRelatedHTTPCode(200);

        when(restTemplate.getForObject(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);
    }

    private void checkResponse(AssetCatalogOMASAPIResponse expectedResponse) {
        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(),
                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());

        assertEquals(expectedResponse.getRelatedHTTPCode(), 200);
    }

}
/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog;

import org.junit.Ignore;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

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

//    @DisplayName("Asset Catalog - Test Invalid Server URL")
//    @Test
//    void assetCatalogInvalidServerURLTest() {
//        AssetCatalog assetCatalog = new AssetCatalog(defaultServerName, "");
//
//        Throwable thrown = assertThrows(PropertyServerException.class, () ->
//                assetCatalog.getAssetSummary(defaultUserId, defaultAssetId));
//
//        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-001"));
//    }
//
//    @DisplayName("Asset Catalog - Test Invalid User Id")
//    @Test
//    void assetCatalogInvalidUserId() {
//        Throwable thrown = assertThrows(InvalidParameterException.class, () ->
//                assetCatalog.getAssetSummary("", defaultAssetId));
//
//        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-003"));
//    }
//
//    @DisplayName("Asset Catalog - Test Invalid Parameter Id")
//    @Test
//    void assetCatalogInvalidAssetId() {
//        Throwable thrown = assertThrows(InvalidParameterException.class, () ->
//                assetCatalog.getAssetSummary(defaultUserId, ""));
//
//        assertTrue(thrown.getMessage().contains("OMAS-ASSET-CATALOG-400-002"));
//    }

//    @Test
//    @DisplayName("Asset Summary")
//    void getAssetSummaryTest() {
//        defaultAssetExpected();
//
//        AssetDescriptionResponse response = new AssetDescriptionResponse();
//        try {
//            response = assetCatalog.getAssetSummary(defaultUserId, defaultAssetId);
//        } catch (AssetCatalogException e) {
//            e.printStackTrace();
//        }
//
//        checkResponse(response);
//        assertEquals(response.getAssetDescriptionList().size(), 1);
//        Assert.assertEquals(defaultAssetId, response.getAssetDescriptionList().get(0).getGuid());
//    }
//
//    @Test
//    @DisplayName("Asset Description")
//    void getAssetDescriptionTest() {
//        defaultAssetExpected();
//
//        AssetDescriptionResponse response = new AssetDescriptionResponse();
//        try {
//            response = assetCatalog.getAssetDetails(defaultUserId, defaultAssetId);
//        } catch (AssetCatalogException e) {
//            e.printStackTrace();
//        }
//
//        checkResponse(response);
//        assertEquals(response.getAssetDescriptionList().size(), 1);
//        Assert.assertEquals(defaultAssetId, response.getAssetDescriptionList().get(0).getGuid());
//    }
//
//    @Test
//    @DisplayName("Asset Universe")
//    void getAssetUniverseTest() {
//        defaultAssetExpected();
//
//        AssetDescriptionResponse response = new AssetDescriptionResponse();
//        try {
//            response = assetCatalog.getAssetUniverse(defaultUserId, defaultUserId, defaultAssetId);
//        } catch (AssetCatalogException e) {
//            e.printStackTrace();
//        }
//
//        checkResponse(response);
//        assertEquals(response.getAssetDescriptionList().size(), 1);
//        Assert.assertEquals(defaultAssetId, response.getAssetDescriptionList().get(0).getGuid());
//    }
//
//     private void defaultAssetExpected() {
//        AssetDescriptionResponse expectedResponse = new AssetDescriptionResponse();
//        expectedResponse.setRelatedHTTPCode(200);
//
//        AssetDescription assetDescription = new AssetDescription();
//        assetDescription.setGuid(defaultAssetId);
//        List<AssetDescription> list = new ArrayList<>(1);
//        list.add(assetDescription);
//        expectedResponse.setAssetDescriptionList(list);
//
//        when(restTemplate.getForObject(ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);
//    }
//
//    private void defaultRelationshipExpected() {
//        RelationshipsResponse expectedResponse = new RelationshipsResponse();
//        expectedResponse.setRelatedHTTPCode(200);
//
//        List<Relationship> relationships = new ArrayList<>();
//        Relationship relationship = new Relationship();
//        relationship.setGuid(defaultRelationshipId);
//        relationships.add(relationship);
//        expectedResponse.setRelationships(relationships);
//
//        when(restTemplate.getForObject(ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);
//    }
//
//    private void defaultClassificationExpected() {
//        ClassificationsResponse expectedResponse = new ClassificationsResponse();
//        expectedResponse.setRelatedHTTPCode(200);
//
//        when(restTemplate.getForObject(ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any())).thenReturn(expectedResponse);
//    }
//
//    private void checkResponse(AssetCatalogOMASAPIResponse expectedResponse) {
//        verify(restTemplate, times(1)).getForObject(ArgumentMatchers.anyString(),
//                ArgumentMatchers.any(Class.class), ArgumentMatchers.<Object>any());
//
//        assertEquals(expectedResponse.getRelatedHTTPCode(), 200);
//    }

}
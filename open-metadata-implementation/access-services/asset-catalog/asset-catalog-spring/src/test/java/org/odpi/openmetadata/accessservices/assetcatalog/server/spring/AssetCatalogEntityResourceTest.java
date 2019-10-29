/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.server.spring;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.odpi.openmetadata.accessservices.assetcatalog.model.rest.body.SearchParameters;
import org.odpi.openmetadata.accessservices.assetcatalog.service.AssetCatalogRESTService;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.WARN)
public class AssetCatalogEntityResourceTest {

    private static final String USER = "user";
    private static final String SERVER_NAME = "serverName";
    private static final String ASSET_GUID = "b1c497ce.60641b50.0v9mgsb1m.9vbkmkr";
    private static final String SECOND_ASSET_GUID = "b1c497ce.60641b50.0v9mgsb1m.ab12343";
    private static final String ASSET_TYPE = "GlossaryTerm";
    private static final String SEMANTIC_ASSIGMENT = "SemanticAssigment";
    private static final Integer FROM = 0;
    private static final Integer PAGE_SIZE = 10;
    private static final String CONFIDENTIALITY = "Confidentiality";

    @Mock
    private AssetCatalogRESTService assetCatalogService;

    @InjectMocks
    private AssetCatalogEntityResource assetCatalogEntityResource;

    @Test
    void testGetAssetDetails() {
        assetCatalogEntityResource.getAssetDetail(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE);

        verify(assetCatalogService, times(1)).getAssetDetailsByGUID(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE);
    }

    @Test
    void testGetAssetRelationships() {
        assetCatalogEntityResource.getAssetRelationships(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE, SEMANTIC_ASSIGMENT, FROM, PAGE_SIZE);

        verify(assetCatalogService, times(1)).getAssetRelationships(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE, SEMANTIC_ASSIGMENT, FROM, PAGE_SIZE);
    }

    @Test
    void testGetAssetContext() {
        assetCatalogEntityResource.getAssetContext(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE);

        verify(assetCatalogService, times(1)).buildContext(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE);
    }

    @Test
    void testGetClassificationsForAsset() {
        assetCatalogEntityResource.getClassificationsForAsset(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE, CONFIDENTIALITY);

        verify(assetCatalogService, times(1)).getClassificationByAssetGUID(SERVER_NAME, USER, ASSET_GUID, ASSET_TYPE, CONFIDENTIALITY);
    }

    @Test
    void testGetAssetsFromNeighborhood() {
        SearchParameters searchParameters = mockSearchParameters();
        assetCatalogEntityResource.getAssetsFromNeighborhood(SERVER_NAME, USER, ASSET_GUID, searchParameters);

        verify(assetCatalogService, times(1)).getAssetsFromNeighborhood(SERVER_NAME, USER, ASSET_GUID, searchParameters);
    }

    @Test
    void testGetLinkingAssets() {
        assetCatalogEntityResource.getLinkingAssets(SERVER_NAME, USER, ASSET_GUID, SECOND_ASSET_GUID);

        verify(assetCatalogService, times(1)).getLinkingAssets(SERVER_NAME, USER, ASSET_GUID, SECOND_ASSET_GUID);
    }

    @Test
    void testGetLinkingRelationships() {
        assetCatalogEntityResource.getLinkingRelationships(SERVER_NAME, USER, ASSET_GUID, SECOND_ASSET_GUID);

        verify(assetCatalogService, times(1)).getLinkingRelationships(SERVER_NAME, USER, ASSET_GUID, SECOND_ASSET_GUID);
    }

    @Test
    void testSearchAssetsAndGlossaryTerms() {
        SearchParameters searchParameters = mockSearchParameters();
        assetCatalogEntityResource.searchByType(SERVER_NAME, USER, "employee", searchParameters);

        verify(assetCatalogService, times(1)).searchByType(SERVER_NAME, USER, "employee", searchParameters);
    }

    private SearchParameters mockSearchParameters() {
        SearchParameters searchParameters = new SearchParameters();
        searchParameters.setFrom(FROM);
        searchParameters.setPageSize(PAGE_SIZE);
        searchParameters.setSequencingOrder(SequencingOrder.LAST_UPDATE_RECENT);
        searchParameters.setLevel(1);
        return searchParameters;
    }
}
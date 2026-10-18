/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservicesfvt;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.adminservices.configuration.registration.ViewServiceDescription;
import org.odpi.openmetadata.commonservices.ffdc.rest.AssetLineageGraphRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AssetCatalogFVT covers every request of the Asset Catalog OMVS against two data sets joined by a DataFlow:
 * the supported asset types, the asset graph and lineage graph, the search within the asset domain and the
 * assets from the local metadata collection.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetCatalogFVT
{
    private static final ViewServiceClient assetCatalog  = new ViewServiceClient(ViewServiceDescription.ASSET_CATALOG);
    private static final ViewServiceClient lineageLinker = new ViewServiceClient(ViewServiceDescription.LINEAGE_LINKER);

    private static String sourceGUID      = null;
    private static String destinationGUID = null;
    private static String dataFlowGUID    = null;


    @Test
    @Order(1)
    void createAssets()
    {
        sourceGUID      = ClassificationExplorerFvtFixtures.createDataSet("catalog-source", "Catalog source");
        destinationGUID = ClassificationExplorerFvtFixtures.createDataSet("catalog-destination", "Catalog destination");

        DataFlowProperties properties = new DataFlowProperties();
        properties.setLabel("copy");

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();
        requestBody.setProperties(properties);

        dataFlowGUID = lineageLinker.create("/from-elements/" + sourceGUID + "/via/" + OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName +
                                                    "/to-elements/" + destinationGUID + "/attach",
                                            requestBody);
    }


    /**
     * getAssetTypes lists the Asset subtypes.  Its response bean lives in the asset-catalog-server module, so it
     * is read as JSON.
     */
    @Test
    @Order(2)
    void assetTypes()
    {
        JsonNode response = new RestTemplate().getForObject(OMAGPlatformExtension.getPlatformURLRoot() + "/servers/" +
                                                                    OMAGPlatformExtension.VIEW_SERVER_NAME +
                                                                    "/api/open-metadata/" + ViewServiceDescription.ASSET_CATALOG.getViewServiceURLMarker() +
                                                                    "/assets/types",
                                                            JsonNode.class);

        assertNotNull(response);
        assertEquals(200, response.path("relatedHTTPCode").asInt(), "getAssetTypes failed: " + response.path("exceptionErrorMessage").asText());

        List<String> typeNames = new ArrayList<>();

        for (JsonNode type : response.path("types"))
        {
            typeNames.add(type.path("name").asText());
        }

        assertTrue(typeNames.contains(OpenMetadataType.DATA_SET.typeName), "getAssetTypes does not list DataSet: " + response.path("types"));
        assertTrue(typeNames.contains(OpenMetadataType.TABULAR_DATA_SET.typeName), "getAssetTypes does not list TabularDataSet");
    }


    /**
     * getAssetGraph returns the asset with its relationships, including the data flow.
     */
    @Test
    @Order(3)
    void assetGraph()
    {
        OpenMetadataRootElement graph = assetCatalog.post("/assets/" + sourceGUID + "/as-graph", new ResultsRequestBody(), OpenMetadataRootElementResponse.class).getElement();

        assertNotNull(graph, "getAssetGraph returned nothing");
        assertEquals(sourceGUID, graph.getElementHeader().getGUID());
        assertTrue(lineageLinkGUIDs(graph).contains(destinationGUID), "the asset graph does not show the data flow to the destination");
        assertNotNull(graph.getMermaidGraph(), "getAssetGraph returned no mermaid graph");
    }


    /**
     * getAssetLineageGraph from the destination shows the source upstream.
     */
    @Test
    @Order(4)
    void assetLineageGraph()
    {
        AssetLineageGraphRequestBody requestBody = new AssetLineageGraphRequestBody();

        requestBody.setIncludeOnlyRelationships(List.of(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName));

        OpenMetadataRootElement graph = assetCatalog.post("/assets/" + destinationGUID + "/as-lineage-graph", requestBody, OpenMetadataRootElementResponse.class).getElement();

        assertNotNull(graph, "getAssetLineageGraph returned nothing");
        assertEquals(destinationGUID, graph.getElementHeader().getGUID());
        assertTrue(lineageLinkGUIDs(graph).contains(sourceGUID), "the lineage graph does not show the source");
        assertNotNull(graph.getMermaidGraph(), "getAssetLineageGraph returned no mermaid graph");
    }


    /**
     * findInAssetDomain finds this run's assets by their qualified names.
     */
    @Test
    @Order(5)
    void findInAssetDomain()
    {
        SearchStringRequestBody requestBody = new SearchStringRequestBody();

        requestBody.setSearchString(ViewServicesFvtTestSupport.runSearchString() + "::DataSet::catalog-");

        List<String> found = ClassificationExplorerFvtFixtures.guids(assetCatalog.elements("/assets/in-domain/by-search-string", requestBody));

        assertTrue(found.contains(sourceGUID), "findInAssetDomain does not return the source");
        assertTrue(found.contains(destinationGUID), "findInAssetDomain does not return the destination");
    }


    /**
     * getAssetsByMetadataCollectionId returns the assets created in the local metadata collection.
     */
    @Test
    @Order(6)
    void assetsByMetadataCollectionId()
    {
        String metadataCollectionId = ClassificationExplorerFvtFixtures.readElement(sourceGUID).getElementHeader().getOrigin().getHomeMetadataCollectionId();

        assertNotNull(metadataCollectionId);

        List<String> found = ClassificationExplorerFvtFixtures.guids(assetCatalog.elements("/assets/by-metadata-collection-id/" + metadataCollectionId,
                                                                                            new ResultsRequestBody()));

        assertTrue(found.contains(sourceGUID), "getAssetsByMetadataCollectionId does not return the source");
        assertTrue(found.contains(destinationGUID), "getAssetsByMetadataCollectionId does not return the destination");

        assertTrue(assetCatalog.elements("/assets/by-metadata-collection-id/" + metadataCollectionId + "-unknown", new ResultsRequestBody()).isEmpty(),
                   "an unknown metadata collection should have no assets");
    }


    @AfterAll
    static void cleanUp()
    {
        if (dataFlowGUID != null)
        {
            lineageLinker.call("/relationships/" + dataFlowGUID + "/detach", new DeleteRelationshipRequestBody());
        }

        ClassificationExplorerFvtFixtures.deleteAsset(sourceGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(destinationGUID);
    }


    private static List<String> lineageLinkGUIDs(OpenMetadataRootElement element)
    {
        List<String> guids = new ArrayList<>();

        if (element.getLineageLinkage() != null)
        {
            for (RelatedMetadataElementSummary linkage : element.getLineageLinkage())
            {
                guids.add(linkage.getRelatedElement().getElementHeader().getGUID());
            }
        }

        return guids;
    }
}

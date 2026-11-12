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
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteElementRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.DeleteRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDListResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.NewRelationshipRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.OpenMetadataRootElementResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.ResultsRequestBody;
import org.odpi.openmetadata.commonservices.ffdc.rest.SearchStringRequestBody;
import org.odpi.openmetadata.frameworks.integration.dcat.DcatDocumentFormatter;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataService;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDataset;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDistribution;
import org.odpi.openmetadata.frameworks.integration.dcat.properties.DcatDocument;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraph;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraphNode;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.AssetLineageGraphRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetContentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.ProvisioningActionProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.lineage.DataFlowProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * AssetCatalogFVT covers every request of the Asset Catalog OMVS against two data sets joined by a DataFlow:
 * the supported asset types, the asset graph and lineage graph, the search within the asset domain and the
 * assets from the local metadata collection.  The destination's data flows on through a provisioning process,
 * which is a governance definition rather than an asset, to a third data set, so the lineage graph has to
 * walk through an element that is not an asset.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AssetCatalogFVT
{
    private static final ViewServiceClient assetCatalog  = new ViewServiceClient(ViewServiceDescription.ASSET_CATALOG);
    private static final ViewServiceClient lineageLinker = new ViewServiceClient(ViewServiceDescription.LINEAGE_LINKER);
    private static final ViewServiceClient collectionManager = new ViewServiceClient(ViewServiceDescription.COLLECTION_MANAGER);
    private static final ViewServiceClient assetMaker        = new ViewServiceClient(ViewServiceDescription.ASSET_MAKER);

    private static String sourceGUID      = null;
    private static String destinationGUID = null;
    private static String dataFlowGUID    = null;
    private static String processGUID     = null;
    private static String provisionedGUID = null;
    private static String toProcessGUID   = null;
    private static String fromProcessGUID = null;
    private static String loopStartGUID   = null;
    private static String loopOtherGUID   = null;
    private static String loopOutGUID     = null;
    private static String loopBackGUID    = null;
    private static String viewGUID        = null;
    private static String viewContentGUID = null;

    private static final String DCAT_BASE      = "https://data.example.gov/" + OMAGPlatformExtension.RUN_ID;
    private static final String DCAT_PUBLISHER = "Environment Agency " + OMAGPlatformExtension.RUN_ID;

    private static String dcatDatasetGUID = null;
    private static String dcatServiceGUID = null;
    private static String dcatCatalogGUID = null;


    @Test
    @Order(1)
    void createAssets()
    {
        sourceGUID      = ClassificationExplorerFvtFixtures.createDataSet("catalog-source", "Catalog source");
        destinationGUID = ClassificationExplorerFvtFixtures.createDataSet("catalog-destination", "Catalog destination");

        dataFlowGUID = createDataFlow(sourceGUID, destinationGUID, "copy");

        ProvisioningActionProcessProperties processProperties = new ProvisioningActionProcessProperties();

        processProperties.setQualifiedName(ViewServicesFvtTestSupport.qualifiedName("ProvisioningActionProcess", "catalog-provision"));
        processProperties.setDisplayName("Catalog provisioning process");

        processGUID     = ClassificationExplorerFvtFixtures.createGovernanceDefinition(processProperties);
        provisionedGUID = ClassificationExplorerFvtFixtures.createDataSet("catalog-provisioned", "Catalog provisioned");
        toProcessGUID   = createDataFlow(destinationGUID, processGUID, "provision-in");
        fromProcessGUID = createDataFlow(processGUID, provisionedGUID, "provision-out");
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
     * getAssetLineageGraph from the destination shows the source upstream, and downstream the provisioning
     * process and the data set it provisions.  The process is not an asset, which used to stop the whole graph
     * with a wrong-type error.
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

        AssetLineageGraph lineageGraph = assertInstanceOf(AssetLineageGraph.class, graph);
        List<String>      linkedGUIDs  = new ArrayList<>();

        assertNotNull(lineageGraph.getLinkedAssets(), "the lineage graph has no linked elements");

        for (AssetLineageGraphNode node : lineageGraph.getLinkedAssets())
        {
            linkedGUIDs.add(node.getElementHeader().getGUID());
        }

        assertTrue(linkedGUIDs.contains(sourceGUID), "the lineage graph does not include the source: " + linkedGUIDs);
        assertTrue(linkedGUIDs.contains(processGUID), "the lineage graph does not include the provisioning process: " + linkedGUIDs);
        assertTrue(linkedGUIDs.contains(provisionedGUID), "the lineage graph stops at the provisioning process: " + linkedGUIDs);
        assertTrue(lineageGraph.getFullLineageMermaidGraph().contains("Catalog provisioning process"), "the mermaid graph does not show the provisioning process");
        assertEquals(linkedGUIDs.size(), new HashSet<>(linkedGUIDs).size(), "an element appears more than once in the lineage graph: " + linkedGUIDs);

        for (AssetLineageGraphNode node : lineageGraph.getLinkedAssets())
        {
            assertNull(node.getLineageLinkage(), "linked elements should be read without their relationships: " + node.getElementHeader().getGUID());
        }
    }


    /**
     * getAssetLineageGraph with maxHops set to one stops at the provisioning process: the source (one hop upstream)
     * and the process (one hop downstream) are in the graph, the provisioned data set is not, and the process is
     * flagged as having more lineage beyond it.
     */
    @Test
    @Order(5)
    void assetLineageGraphHopLimit()
    {
        AssetLineageGraphRequestBody requestBody = new AssetLineageGraphRequestBody();

        requestBody.setIncludeOnlyRelationships(List.of(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName));
        requestBody.setMaxHops(1);

        AssetLineageGraph lineageGraph = assertInstanceOf(AssetLineageGraph.class,
                                                          assetCatalog.post("/assets/" + destinationGUID + "/as-lineage-graph",
                                                                            requestBody,
                                                                            OpenMetadataRootElementResponse.class).getElement());

        assertEquals(destinationGUID, lineageGraph.getElementHeader().getGUID());
        assertNotNull(lineageGraph.getLinkedAssets(), "the hop-limited lineage graph has no linked elements");

        Map<String, AssetLineageGraphNode> linkedNodes = new HashMap<>();

        for (AssetLineageGraphNode node : lineageGraph.getLinkedAssets())
        {
            linkedNodes.put(node.getElementHeader().getGUID(), node);
        }

        assertTrue(linkedNodes.containsKey(sourceGUID), "the hop-limited lineage graph does not include the source: " + linkedNodes.keySet());
        assertTrue(linkedNodes.containsKey(processGUID), "the hop-limited lineage graph does not include the provisioning process: " + linkedNodes.keySet());
        assertFalse(linkedNodes.containsKey(provisionedGUID), "the hop-limited lineage graph goes beyond one hop: " + linkedNodes.keySet());

        assertEquals(1, linkedNodes.get(processGUID).getHopsFromStart());
        assertTrue(linkedNodes.get(processGUID).getHopLimitReached(), "the provisioning process should be flagged as having lineage beyond the hop limit");
        assertFalse(linkedNodes.get(sourceGUID).getHopLimitReached(), "the source has no lineage beyond it");

        for (AssetLineageGraphRelationship relationship : lineageGraph.getLineageRelationships())
        {
            assertTrue(linkedNodes.containsKey(relationship.getEnd1AssetGUID()) || destinationGUID.equals(relationship.getEnd1AssetGUID()),
                       "a lineage relationship leads to an element outside the graph: " + relationship);
            assertTrue(linkedNodes.containsKey(relationship.getEnd2AssetGUID()) || destinationGUID.equals(relationship.getEnd2AssetGUID()),
                       "a lineage relationship leads to an element outside the graph: " + relationship);
        }
    }


    /**
     * Two data sets that feed each other: the other data set is both downstream and upstream of the starting one.
     * It appears once in the lineage graph, carrying the relationships from both directions, rather than once for
     * each direction the walk reached it from.
     */
    @Test
    @Order(6)
    void assetLineageGraphLoop()
    {
        loopStartGUID = ClassificationExplorerFvtFixtures.createDataSet("catalog-loop-start", "Catalog loop start");
        loopOtherGUID = ClassificationExplorerFvtFixtures.createDataSet("catalog-loop-other", "Catalog loop other");
        loopOutGUID   = createDataFlow(loopStartGUID, loopOtherGUID, "loop-out");
        loopBackGUID  = createDataFlow(loopOtherGUID, loopStartGUID, "loop-back");

        AssetLineageGraphRequestBody requestBody = new AssetLineageGraphRequestBody();

        requestBody.setIncludeOnlyRelationships(List.of(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName));

        AssetLineageGraph lineageGraph = assertInstanceOf(AssetLineageGraph.class,
                                                          assetCatalog.post("/assets/" + loopStartGUID + "/as-lineage-graph",
                                                                            requestBody,
                                                                            OpenMetadataRootElementResponse.class).getElement());

        assertNotNull(lineageGraph.getLinkedAssets(), "the loop lineage graph has no linked elements");
        assertEquals(1, lineageGraph.getLinkedAssets().size(), "the other data set should appear once: " + lineageGraph.getLinkedAssets());

        AssetLineageGraphNode otherNode = lineageGraph.getLinkedAssets().get(0);

        assertEquals(loopOtherGUID, otherNode.getElementHeader().getGUID());
        assertEquals(1, otherNode.getHopsFromStart());
        assertNotNull(otherNode.getUpstreamRelationships(), "the other data set has lost its upstream relationship");
        assertNotNull(otherNode.getDownstreamRelationships(), "the other data set has lost its downstream relationship");
        assertEquals(1, otherNode.getUpstreamRelationships().size());
        assertEquals(1, otherNode.getDownstreamRelationships().size());
        assertEquals(2, lineageGraph.getLineageRelationships().size(), "expected a lineage relationship in each direction: " + lineageGraph.getLineageRelationships());
    }


    /**
     * DataSetContent is a LineageRelationship, so the default lineage types are all retrieved with one query for
     * LineageRelationship.  A data set that is a view over the destination is linked by DataSetContent: the default
     * walk includes it, along with the data flows, and a walk limited to DataFlow and ControlFlow (also queried as
     * LineageRelationship) does not.  The DataSetContent relationship is created through the Lineage Linker, which
     * accepts it now that its properties are lineage relationship properties.
     */
    @Test
    @Order(7)
    void assetLineageGraphRelationshipQueries()
    {
        viewGUID = ClassificationExplorerFvtFixtures.createDataSet("catalog-view", "Catalog view");

        DataSetContentProperties contentProperties = new DataSetContentProperties();

        contentProperties.setLabel("view-over");
        contentProperties.setQuery("select * from destination");

        NewRelationshipRequestBody contentRequestBody = new NewRelationshipRequestBody();
        contentRequestBody.setProperties(contentProperties);

        viewContentGUID = lineageLinker.create("/from-elements/" + viewGUID + "/via/" + OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName +
                                                       "/to-elements/" + destinationGUID + "/attach",
                                               contentRequestBody);

        AssetLineageGraphRequestBody flowsOnlyRequest = new AssetLineageGraphRequestBody();

        flowsOnlyRequest.setIncludeOnlyRelationships(List.of(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                             OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName));

        assertEquals(List.of(sourceGUID, processGUID, provisionedGUID).stream().sorted().toList(),
                     linkedGUIDs(flowsOnlyRequest).stream().sorted().toList(),
                     "querying with LineageRelationship for the flows gave the wrong graph");

        AssetLineageGraph defaultGraph = lineageGraph(new AssetLineageGraphRequestBody());
        List<String>      defaultGUIDs = new ArrayList<>();

        for (AssetLineageGraphNode node : defaultGraph.getLinkedAssets())
        {
            defaultGUIDs.add(node.getElementHeader().getGUID());
        }

        assertEquals(List.of(sourceGUID, processGUID, provisionedGUID, viewGUID).stream().sorted().toList(),
                     defaultGUIDs.stream().sorted().toList(),
                     "the default lineage types gave the wrong graph");

        boolean contentLinkFound = false;

        for (AssetLineageGraphRelationship relationship : defaultGraph.getLineageRelationships())
        {
            if ((viewGUID.equals(relationship.getEnd1AssetGUID())) && (destinationGUID.equals(relationship.getEnd2AssetGUID())))
            {
                contentLinkFound = relationship.getRelationshipTypes().contains("view-over [" + OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName + "]");
            }
        }

        assertTrue(contentLinkFound, "the DataSetContent link, with its label, is missing: " + defaultGraph.getLineageRelationships());

        /*
         * A supertype name includes its subtypes: LineageRelationship reaches everything the defaults reach.
         */
        AssetLineageGraphRequestBody superTypeRequest = new AssetLineageGraphRequestBody();

        superTypeRequest.setIncludeOnlyRelationships(List.of(OpenMetadataType.LINEAGE_RELATIONSHIP.typeName));

        assertEquals(List.of(sourceGUID, processGUID, provisionedGUID, viewGUID).stream().sorted().toList(),
                     linkedGUIDs(superTypeRequest).stream().sorted().toList(),
                     "requesting LineageRelationship should include all of its subtypes");

        /*
         * skipRelationships trims the defaults without restating them.
         */
        AssetLineageGraphRequestBody skipRequest = new AssetLineageGraphRequestBody();

        skipRequest.setSkipRelationships(List.of(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName));

        assertEquals(List.of(sourceGUID, processGUID, provisionedGUID).stream().sorted().toList(),
                     linkedGUIDs(skipRequest).stream().sorted().toList(),
                     "skipping DataSetContent should drop the view");

        /*
         * Skipping a supertype skips its subtypes: without DataLineageRelationship (DataFlow's supertype) only the
         * view, linked by DataSetContent, is left.
         */
        AssetLineageGraphRequestBody skipSuperTypeRequest = new AssetLineageGraphRequestBody();

        skipSuperTypeRequest.setSkipRelationships(List.of(OpenMetadataType.DATA_LINEAGE_RELATIONSHIP.typeName));

        assertEquals(List.of(viewGUID),
                     linkedGUIDs(skipSuperTypeRequest),
                     "skipping DataLineageRelationship should drop the data flows");
    }


    /**
     * The default request follows at most eight hops.
     */
    @Test
    @Order(8)
    void assetLineageGraphDefaultMaxHops()
    {
        assertEquals(AssetLineageGraphRequestBody.DEFAULT_MAX_HOPS, new AssetLineageGraphRequestBody().getMaxHops());
        assertEquals(8, AssetLineageGraphRequestBody.DEFAULT_MAX_HOPS);
    }


    /**
     * findInAssetDomain finds this run's assets by their qualified names.
     */
    @Test
    @Order(9)
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
    @Order(10)
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


    /* =====================================================================================================================
     * Data Catalog Vocabulary (DCAT)
     */

    /**
     * Build a DCAT document that describes a catalog listing a dataset with distributions and the data service that
     * serves it.  The IRIs carry the run's marker so that a run against a repository that still holds a previous
     * run's elements does not find them.
     *
     * @param description description of the dataset
     * @param withAPIDistribution should the dataset have its second distribution?
     * @return JSON-LD document
     */
    private static String dcatDocument(String  description,
                                       boolean withAPIDistribution)
    {
        String apiDistribution = withAPIDistribution ? """
                    ,
                    {
                      "@type": "dcat:Distribution",
                      "dct:title": "Air quality (API)",
                      "dcat:accessURL": { "@id": "%1$s/api/air-quality/v1" }
                    }
                """.formatted(DCAT_BASE) : "";

        return """
                {
                  "@context": {
                    "dcat": "http://www.w3.org/ns/dcat#",
                    "dct": "http://purl.org/dc/terms/",
                    "foaf": "http://xmlns.com/foaf/0.1/",
                    "vcard": "http://www.w3.org/2006/vcard/ns#"
                  },
                  "@graph": [
                    {
                      "@id": "%1$s/catalog",
                      "@type": "dcat:Catalog",
                      "dct:title": "FVT open data catalog",
                      "dcat:dataset": { "@id": "%1$s/dataset/air-quality" },
                      "dcat:service": { "@id": "%1$s/service/air-quality-api" }
                    },
                    {
                      "@id": "%1$s/dataset/air-quality",
                      "@type": "dcat:Dataset",
                      "dct:title": [ { "@value": "Air quality measurements", "@language": "en" }, { "@value": "Mesures de la qualité de l'air", "@language": "fr" } ],
                      "dct:description": "%2$s",
                      "dct:identifier": "air-quality",
                      "dct:modified": { "@type": "http://www.w3.org/2001/XMLSchema#date", "@value": "2025-11-02" },
                      "dct:publisher": { "@type": "foaf:Organization", "foaf:name": "%3$s" },
                      "dcat:contactPoint": { "@type": "vcard:Organization", "vcard:fn": "Open Data Helpdesk", "vcard:hasEmail": { "@id": "mailto:opendata@example.gov" } },
                      "dcat:keyword": [ "air quality", "pollution" ],
                      "dcat:theme": { "@id": "http://publications.europa.eu/resource/authority/data-theme/ENVI" },
                      "dct:license": { "@id": "%1$s/licence/open" },
                      "dct:conformsTo": { "@id": "%1$s/standard/observations" },
                      "foaf:page": { "@id": "%1$s/docs/air-quality" },
                      "dcat:landingPage": { "@id": "%1$s/air-quality" },
                      "dcat:version": "2.1",
                      "http://data.europa.eu/r5r/applicableLegislation": { "@id": "http://data.europa.eu/eli/reg_impl/2023/138/oj" },
                      "dcat:distribution": [
                        {
                          "@type": "dcat:Distribution",
                          "dct:title": "Air quality (CSV)",
                          "dcat:downloadURL": { "@id": "%1$s/files/air-quality.csv" },
                          "dct:format": "CSV",
                          "dcat:byteSize": 5242880
                        }%4$s
                      ]
                    },
                    {
                      "@id": "%1$s/service/air-quality-api",
                      "@type": "dcat:DataService",
                      "dct:title": "Air quality API",
                      "dcat:endpointURL": { "@id": "%1$s/api/air-quality/v1" },
                      "dcat:servesDataset": { "@id": "%1$s/dataset/air-quality" }
                    }
                  ]
                }
                """.formatted(DCAT_BASE, description, DCAT_PUBLISHER, apiDistribution);
    }


    /**
     * Retrieve the DCAT description of an asset.  The response bean lives in the asset-catalog-server module, so it
     * is read as JSON; its document is a JSON-LD document that is read with the DCAT formatter.
     *
     * @param assetGUID unique identifier of the asset
     * @return description
     */
    private static DcatDocument dcatDescription(String assetGUID) throws IOException
    {
        JsonNode response = dcatResponse(assetGUID);

        assertEquals(200, response.path("relatedHTTPCode").asInt(), "getAssetAsDcat failed: " + response.path("exceptionErrorMessage").asText());
        assertTrue(response.path("document").has("@context"), "the DCAT description does not embed its context: " + response.path("document"));

        return DcatDocumentFormatter.parseDocument(response.path("document").toString());
    }


    /**
     * Call getAssetAsDcat.
     *
     * @param elementGUID unique identifier of an element
     * @return response
     */
    private static JsonNode dcatResponse(String elementGUID)
    {
        JsonNode response = new RestTemplate().getForObject(OMAGPlatformExtension.getPlatformURLRoot() + "/servers/" +
                                                                    OMAGPlatformExtension.VIEW_SERVER_NAME +
                                                                    "/api/open-metadata/" + ViewServiceDescription.ASSET_CATALOG.getViewServiceURLMarker() +
                                                                    "/assets/" + elementGUID + "/dcat-document",
                                                            JsonNode.class);

        assertNotNull(response);

        return response;
    }


    /**
     * importDcatDocument catalogs the dataset, the data service and the catalog, and returns their GUIDs in that
     * order.
     */
    @Test
    @Order(11)
    void importDcatDocument()
    {
        List<String> guids = assetCatalog.post("/assets/dcat/import-document", dcatDocument("Hourly readings from the monitoring network.", true), GUIDListResponse.class).getGUIDs();

        assertNotNull(guids, "importDcatDocument returned no GUIDs");
        assertEquals(3, guids.size(), "importDcatDocument should catalog a dataset, a data service and a catalog: " + guids);

        dcatDatasetGUID = guids.get(0);
        dcatServiceGUID = guids.get(1);
        dcatCatalogGUID = guids.get(2);

        /*
         * The dataset is a data set whose content is its two distributions and the API that serves it.
         */
        OpenMetadataRootElement dataset = assetCatalog.post("/assets/" + dcatDatasetGUID + "/as-graph", new ResultsRequestBody(), OpenMetadataRootElementResponse.class).getElement();

        assertEquals(OpenMetadataType.DATA_SET.typeName, dataset.getElementHeader().getType().getTypeName());
        assertNotNull(dataset.getDataSetContent(), "the data set has no content");
        assertEquals(3, dataset.getDataSetContent().size(), "the data set should be linked to two distributions and a data service");

        Set<String> contentTypeNames = new HashSet<>();

        for (RelatedMetadataElementSummary content : dataset.getDataSetContent())
        {
            contentTypeNames.add(content.getRelatedElement().getElementHeader().getType().getTypeName());
        }

        assertEquals(Set.of(OpenMetadataType.DATA_FILE.typeName, OpenMetadataType.DATA_STORE.typeName, OpenMetadataType.DEPLOYED_API.typeName), contentTypeNames);
        assertNotNull(dataset.getSearchKeywords(), "the data set has no search keywords");
        assertEquals(2, dataset.getSearchKeywords().size());
        assertNotNull(dataset.getLicenses(), "the data set has no licence");
        assertNotNull(dataset.getContactDetails(), "the data set has no contact details");
        assertNotNull(dataset.getExternalReferences(), "the data set has no external references");
        assertEquals(2, dataset.getExternalReferences().size());
        assertNotNull(dataset.getMemberOfCollections(), "the data set is not a member of the catalog's collection");
        assertEquals(dcatCatalogGUID, dataset.getMemberOfCollections().get(0).getRelatedElement().getElementHeader().getGUID());
    }


    /**
     * getAssetAsDcat gives back the description that was imported.
     */
    @Test
    @Order(12)
    void dcatDescriptionOfImportedAssets() throws IOException
    {
        DcatDocument document = dcatDescription(dcatDatasetGUID);

        assertNull(document.getCatalogs());
        assertNull(document.getDataServices());
        assertEquals(1, document.getDatasets().size());

        DcatDataset dataset = document.getDatasets().get(0);

        assertEquals(DCAT_BASE + "/dataset/air-quality", dataset.getIri());
        assertEquals("Air quality measurements", dataset.getTitle());
        assertEquals("Mesures de la qualité de l'air", dataset.getTitleTranslations().get("fr"));
        assertEquals("Hourly readings from the monitoring network.", dataset.getDescription());
        assertEquals(List.of("air-quality"), dataset.getIdentifiers());
        assertEquals("2025-11-02", dataset.getModified());
        assertEquals(DCAT_PUBLISHER, dataset.getPublisher().getName());
        assertEquals("Open Data Helpdesk", dataset.getContactPoints().get(0).getName());
        assertEquals("opendata@example.gov", dataset.getContactPoints().get(0).getEmail());
        assertEquals(Set.of("air quality", "pollution"), new HashSet<>(dataset.getKeywords()));
        assertEquals("http://publications.europa.eu/resource/authority/data-theme/ENVI", dataset.getThemes().get(0).getIri());
        assertEquals(DCAT_BASE + "/licence/open", dataset.getLicense().getIri());
        assertEquals(List.of(DCAT_BASE + "/standard/observations"), dataset.getConformsTo());
        assertEquals(List.of(DCAT_BASE + "/docs/air-quality"), dataset.getDocumentation());
        assertEquals(DCAT_BASE + "/air-quality", dataset.getLandingPage());
        assertEquals("2.1", dataset.getVersion());
        assertEquals(List.of("http://data.europa.eu/eli/reg_impl/2023/138/oj"), dataset.getOtherProperties().get("http://data.europa.eu/r5r/applicableLegislation"));

        assertEquals(2, dataset.getDistributions().size(), "the dataset should have its two distributions: " + dataset.getDistributions());

        Map<String, DcatDistribution> distributions = new HashMap<>();

        for (DcatDistribution distribution : dataset.getDistributions())
        {
            distributions.put(distribution.getTitle(), distribution);
        }

        assertEquals(DCAT_BASE + "/files/air-quality.csv", distributions.get("Air quality (CSV)").getDownloadURL());
        assertEquals("CSV", distributions.get("Air quality (CSV)").getFormat());
        assertEquals(Long.valueOf(5242880L), distributions.get("Air quality (CSV)").getByteSize());
        assertEquals(DCAT_BASE + "/api/air-quality/v1", distributions.get("Air quality (API)").getAccessURL());
        assertNull(distributions.get("Air quality (API)").getDownloadURL());

        /*
         * The data service is described as a data service that serves the dataset.
         */
        document = dcatDescription(dcatServiceGUID);

        assertNull(document.getDatasets());
        assertEquals(1, document.getDataServices().size());

        DcatDataService dataService = document.getDataServices().get(0);

        assertEquals(DCAT_BASE + "/service/air-quality-api", dataService.getIri());
        assertEquals("Air quality API", dataService.getTitle());
        assertEquals(List.of(DCAT_BASE + "/api/air-quality/v1"), dataService.getEndpointURLs());
        assertEquals(List.of(DCAT_BASE + "/dataset/air-quality"), dataService.getServesDatasets());
    }


    /**
     * Importing a changed description updates the same elements: the description changes, the distribution that
     * has gone is removed and nothing is duplicated.
     */
    @Test
    @Order(13)
    void reimportDcatDocument() throws IOException
    {
        List<String> guids = assetCatalog.post("/assets/dcat/import-document", dcatDocument("Daily readings from the monitoring network.", false), GUIDListResponse.class).getGUIDs();

        assertEquals(List.of(dcatDatasetGUID, dcatServiceGUID, dcatCatalogGUID), guids, "importing the document again should update the same elements");

        DcatDataset dataset = dcatDescription(dcatDatasetGUID).getDatasets().get(0);

        assertEquals("Daily readings from the monitoring network.", dataset.getDescription());
        assertEquals(1, dataset.getDistributions().size(), "the distribution that was removed from the description is still catalogued: " + dataset.getDistributions());
        assertEquals("Air quality (CSV)", dataset.getDistributions().get(0).getTitle());
        assertEquals(2, dataset.getKeywords().size(), "the keywords were duplicated: " + dataset.getKeywords());
        assertEquals(1, dataset.getContactPoints().size(), "the contact point was duplicated");
        assertEquals(1, dataset.getConformsTo().size(), "the standard was duplicated");
    }


    /**
     * An asset that did not come from a DCAT description is described as a dataset that is identified by its GUID.
     * A data set's distributions are the assets that supply its data.
     */
    @Test
    @Order(14)
    void dcatDescriptionOfNativeAssets() throws IOException
    {
        DcatDataset dataset = dcatDescription(sourceGUID).getDatasets().get(0);

        assertEquals("urn:uuid:" + sourceGUID, dataset.getIri());
        assertEquals("Catalog source", dataset.getTitle());
        assertNotNull(dataset.getIssued(), "a native asset should be issued when it was created");
        assertNotNull(dataset.getModified(), "a native asset should have a modified date");
        assertNull(dataset.getDistributions(), "a data set with no content has no distributions");
    }


    /**
     * Documents that are not DCAT, and elements that are not assets, are refused.
     */
    @Test
    @Order(15)
    void dcatRefusals()
    {
        JsonNode response = assetCatalog.postForFailure("/assets/dcat/import-document", "@prefix dcat: <http://www.w3.org/ns/dcat#> .");

        assertTrue(response.path("exceptionErrorMessageId").asText().contains("OIF-CONNECTOR-400-007"), "a document that is not JSON-LD was refused for the wrong reason: " + response);

        response = assetCatalog.postForFailure("/assets/dcat/import-document", "{\"@context\": {\"schema\": \"http://schema.org/\"}, \"@type\": \"schema:Person\", \"schema:name\": \"Nobody\"}");

        assertTrue(response.path("exceptionErrorMessageId").asText().contains("OIF-CONNECTOR-400-008"), "a document with no DCAT resources was refused for the wrong reason: " + response);

        /*
         * The provisioning process is a governance definition, not an asset.
         */
        response = dcatResponse(processGUID);

        assertTrue(response.path("exceptionErrorMessageId").asText().contains("OIF-CONNECTOR-400-006"), "the DCAT description of a governance definition was not refused: " + response);
    }


    /**
     * publishDcatDocument passes the document to an integration daemon.  This suite runs no integration daemon, so
     * the request is sent to an element that is not one - the source data set - and must be refused rather than
     * reporting that the document was published.
     */
    @Test
    @Order(16)
    void publishingDcatNeedsAnIntegrationDaemon()
    {
        JsonNode response = assetCatalog.postForFailure("/integration-daemons/" + sourceGUID + "/dcat/publish-document", dcatDocument("Not published.", false));

        assertFalse(response.path("exceptionErrorMessage").asText().isEmpty(), "a refused publish did not say why: " + response);
    }


    @AfterAll
    static void cleanUp()
    {
        for (String relationshipGUID : new String[]{dataFlowGUID, toProcessGUID, fromProcessGUID, loopOutGUID, loopBackGUID, viewContentGUID})
        {
            if (relationshipGUID != null)
            {
                lineageLinker.call("/relationships/" + relationshipGUID + "/detach", new DeleteRelationshipRequestBody());
            }
        }

        ClassificationExplorerFvtFixtures.deleteAsset(sourceGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(destinationGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(provisionedGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(loopStartGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(loopOtherGUID);
        ClassificationExplorerFvtFixtures.deleteAsset(viewGUID);
        ClassificationExplorerFvtFixtures.deleteGovernanceDefinition(processGUID);

        /*
         * The catalog's collection has members, so its delete has to be cascaded, which deletes the members too.
         * The distributions, endpoint and contact details are anchored to the assets that they describe, so they
         * are deleted with them.
         */
        if (dcatCatalogGUID != null)
        {
            DeleteElementRequestBody cascadedDelete = new DeleteElementRequestBody();

            cascadedDelete.setCascadedDelete(true);

            collectionManager.call("/collections/" + dcatCatalogGUID + "/delete", cascadedDelete);

            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + dcatDatasetGUID + "/retrieve");
            ViewServicesFvtTestSupport.assertGone(assetMaker, "/assets/" + dcatServiceGUID + "/retrieve");
        }
    }


    /**
     * Link two elements with a DataFlow through the Lineage Linker.
     *
     * @param fromGUID element the data flows from
     * @param toGUID element the data flows to
     * @param label label for the data flow
     * @return relationship GUID
     */
    private static String createDataFlow(String fromGUID,
                                         String toGUID,
                                         String label)
    {
        DataFlowProperties properties = new DataFlowProperties();
        properties.setLabel(label);

        NewRelationshipRequestBody requestBody = new NewRelationshipRequestBody();
        requestBody.setProperties(properties);

        return lineageLinker.create("/from-elements/" + fromGUID + "/via/" + OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName +
                                            "/to-elements/" + toGUID + "/attach",
                                    requestBody);
    }


    /**
     * Retrieve the lineage graph from the destination and return the unique identifiers of its linked elements.
     *
     * @param requestBody request
     * @return element GUIDs
     */
    private static List<String> linkedGUIDs(AssetLineageGraphRequestBody requestBody)
    {
        AssetLineageGraph lineageGraph = lineageGraph(requestBody);
        List<String>      guids        = new ArrayList<>();

        if (lineageGraph.getLinkedAssets() != null)
        {
            for (AssetLineageGraphNode node : lineageGraph.getLinkedAssets())
            {
                guids.add(node.getElementHeader().getGUID());
            }
        }

        return guids;
    }


    /**
     * Retrieve the lineage graph from the destination.
     *
     * @param requestBody request
     * @return lineage graph
     */
    private static AssetLineageGraph lineageGraph(AssetLineageGraphRequestBody requestBody)
    {
        return assertInstanceOf(AssetLineageGraph.class,
                                assetCatalog.post("/assets/" + destinationGUID + "/as-lineage-graph",
                                                  requestBody,
                                                  OpenMetadataRootElementResponse.class).getElement());
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

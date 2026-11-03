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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

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

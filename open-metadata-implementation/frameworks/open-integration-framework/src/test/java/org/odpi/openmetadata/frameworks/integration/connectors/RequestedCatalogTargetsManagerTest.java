/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.connectors;

import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.integration.properties.RequestedCatalogTarget;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementVersions;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedBy;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.connectors.CatalogTargetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;


/**
 * Checks that the catalog targets manager keeps its list of catalog targets in step with the catalog target
 * relationships of the integration connector across refreshes.
 */
public class RequestedCatalogTargetsManagerTest
{
    private static final String CONNECTOR_GUID = "connector-guid";

    /*
     * The catalog target relationships that the next refresh retrieves - changed by each test step.
     */
    private List<OpenMetadataRootElement> currentTargets = new ArrayList<>();


    /**
     * A catalog target whose relationship has been removed (or whose element has been deleted) is dropped on the
     * next refresh, so its processor and resource connector stop.  It used to carry on running until the
     * integration connector restarted.
     *
     * @throws Exception test failure
     */
    @Test
    public void testRemovedCatalogTargetIsRetired() throws Exception
    {
        RequestedCatalogTargetsManager manager = new RequestedCatalogTargetsManager(null, "TestConnector", null);
        IntegrationContext integrationContext = this.getIntegrationContext();
        CatalogTargetFactory factory = new CatalogTargetFactory() {};

        currentTargets = List.of(getCatalogTarget("relationship-1", "first"), getCatalogTarget("relationship-2", "second"));

        assertEquals(getRelationshipGUIDs(manager.retrieveKnownCatalogTargets(integrationContext, factory)),
                     List.of("relationship-1", "relationship-2"));

        currentTargets = List.of(getCatalogTarget("relationship-2", "second"));

        assertEquals(getRelationshipGUIDs(manager.retrieveKnownCatalogTargets(integrationContext, factory)),
                     List.of("relationship-2"),
                     "the removed catalog target should have been retired");

        currentTargets = new ArrayList<>();

        assertEquals(getRelationshipGUIDs(manager.retrieveKnownCatalogTargets(integrationContext, factory)),
                     List.of(),
                     "the last catalog target should have been retired");
    }


    /**
     * Return a mock integration context whose asset client returns the current catalog targets as one page.
     *
     * @return integration context
     * @throws Exception mocking failure
     */
    private IntegrationContext getIntegrationContext() throws Exception
    {
        IntegrationContext integrationContext = mock(IntegrationContext.class);
        AssetClient        assetClient        = mock(AssetClient.class);

        when(integrationContext.getAssetClient()).thenReturn(assetClient);
        when(integrationContext.getIntegrationConnectorGUID()).thenReturn(CONNECTOR_GUID);
        when(integrationContext.getMaxPageSize()).thenReturn(100);

        when(assetClient.getQueryOptions(anyInt(), anyInt())).thenAnswer(invocation -> {
            QueryOptions queryOptions = new QueryOptions();
            queryOptions.setStartFrom(invocation.getArgument(0));
            queryOptions.setPageSize(invocation.getArgument(1));
            return queryOptions;
        });

        when(assetClient.getCatalogTargets(eq(CONNECTOR_GUID), any(QueryOptions.class))).thenAnswer(invocation -> {
            QueryOptions queryOptions = invocation.getArgument(1);
            return ((queryOptions.getStartFrom() == 0) && (! currentTargets.isEmpty())) ? currentTargets : null;
        });

        return integrationContext;
    }


    /**
     * Return a catalog target as the asset client returns it: the element, related by the catalog target
     * relationship.  The element is not an asset, so no connector to the target is needed.
     *
     * @param relationshipGUID unique identifier of the catalog target relationship
     * @param catalogTargetName name of the catalog target
     * @return element
     */
    private OpenMetadataRootElement getCatalogTarget(String relationshipGUID,
                                                     String catalogTargetName)
    {
        ElementType elementType = new ElementType();
        elementType.setTypeName("GlossaryTerm");

        ElementHeader elementHeader = new ElementHeader();
        elementHeader.setGUID("element-" + relationshipGUID);
        elementHeader.setType(elementType);

        ElementType relationshipType = new ElementType();
        relationshipType.setTypeName("CatalogTarget");

        ElementVersions versions = new ElementVersions();
        versions.setVersion(1L);

        ElementHeader relationshipHeader = new ElementHeader();
        relationshipHeader.setGUID(relationshipGUID);
        relationshipHeader.setType(relationshipType);
        relationshipHeader.setVersions(versions);

        CatalogTargetProperties catalogTargetProperties = new CatalogTargetProperties();
        catalogTargetProperties.setCatalogTargetName(catalogTargetName);

        RelatedBy relatedBy = new RelatedBy();
        relatedBy.setRelationshipHeader(relationshipHeader);
        relatedBy.setRelationshipProperties(catalogTargetProperties);

        OpenMetadataRootElement element = new OpenMetadataRootElement();
        element.setElementHeader(elementHeader);
        element.setRelatedBy(relatedBy);

        return element;
    }


    /**
     * Return the sorted relationship GUIDs of the known catalog targets.
     *
     * @param catalogTargets known catalog targets
     * @return relationship GUIDs
     */
    private List<String> getRelationshipGUIDs(List<RequestedCatalogTarget> catalogTargets)
    {
        return catalogTargets.stream().map(RequestedCatalogTarget::getRelationshipGUID).sorted().collect(Collectors.toList());
    }
}

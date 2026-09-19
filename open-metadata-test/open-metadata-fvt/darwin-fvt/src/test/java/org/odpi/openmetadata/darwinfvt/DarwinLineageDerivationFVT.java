/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinLineageDerivationFVT drives the Darwin Product Dependency Manager and checks the two levels of
 * lineage it derives beneath the digital products: the data flows between data assets that the data mappings
 * between their schema elements imply, and the data flows between software servers that the lineage between
 * the data assets their capabilities own implies.
 * <br>
 * Darwin's refresh is triggered explicitly rather than waited for.  The refresh is synchronous, so by the time
 * the call returns the connector has completed every level.  One refresh is enough for most of the assertions
 * here, so it is done once for the class.  The test that checks what happens when a mapping is taken away
 * changes its own set of the fixture and refreshes again; nothing else reads that set, so the other tests are
 * unaffected whatever order they run in.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DarwinLineageDerivationFVT
{
    /**
     * Run Darwin once before the assertions.
     *
     * @throws Exception a failure to refresh is fatal to every test in this class
     */
    @BeforeAll
    public static void refreshDarwin() throws Exception
    {
        OMAGPlatformExtension.refreshDarwin();
    }


    /**
     * A DataMapping between the columns of two data assets means data flows from the first asset to the
     * second.  Darwin records that as a DataFlow between the assets, carrying the mapping's information
     * supply chain, labelled so that its origin is visible in a lineage graph.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a data flow between assets from the mapping between their columns")
    public void testAssetLineageIsDerivedFromDataMapping() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        List<OpenMetadataRelationship> dataFlows = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                         DarwinFvtTestSupport.ASSET_SOURCE_GUID,
                                                                                         DarwinFvtTestSupport.ASSET_TARGET_GUID);

        assertEquals(1, dataFlows.size(),
                     "Expected exactly one DataFlow from the mapped source asset to the mapped target asset - the columns of the two"
                             + " assets are joined by a single DataMapping for one information supply chain");

        OpenMetadataRelationship dataFlow = dataFlows.get(0);

        assertEquals(DarwinFvtTestSupport.ISC_MAPPED, DarwinFvtTestSupport.getISCQualifiedName(dataFlow),
                     "The derived DataFlow should carry the information supply chain of the DataMapping it was derived from");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                   "The DataFlow between the mapped assets should have been created by Darwin (createdBy "
                           + DarwinFvtTestSupport.DARWIN_USER_ID + ") but was created by " + DarwinFvtTestSupport.getCreatedBy(dataFlow));
        assertNotNull(DarwinFvtTestSupport.getLabel(dataFlow),
                      "Darwin should label the DataFlow it derives so that its origin is visible in a lineage graph");

        /*
         * Data flows one way.  A DataFlow in the other direction would mean Darwin had confused the mapping's
         * source and target.
         */
        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.ASSET_TARGET_GUID,
                                                         DarwinFvtTestSupport.ASSET_SOURCE_GUID).isEmpty(),
                   "Darwin derived a DataFlow from the mapped target asset back to the mapped source asset - the DataMapping runs the"
                           + " other way");
    }


    /**
     * Where the archive already asserts the data flow between two mapped assets for the same information
     * supply chain, there is nothing for Darwin to add.  The archive's relationship stands and no second one
     * appears beside it.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin does not duplicate a data flow that is already asserted")
    public void testAssertedAssetLineageIsNotDuplicated() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        List<OpenMetadataRelationship> dataFlows = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                         DarwinFvtTestSupport.ASSET_PRESENT_SOURCE_GUID,
                                                                                         DarwinFvtTestSupport.ASSET_PRESENT_TARGET_GUID);

        assertEquals(1, dataFlows.size(),
                     "Expected exactly one DataFlow between the present assets - the archive asserts it for the same information"
                             + " supply chain as the mapping, so Darwin has nothing to add");
        assertEquals(DarwinFvtTestSupport.FLOW_PRESENT_GUID, dataFlows.get(0).getRelationshipGUID(),
                     "The DataFlow between the present assets should be the archive's own, not one created by Darwin");
        assertFalse(DarwinFvtTestSupport.isCreatedByDarwin(dataFlows.get(0)),
                    "The archive's DataFlow between the present assets appears to have been replaced by one of Darwin's");
    }


    /**
     * The mapped source asset is owned by one server's capability and the mapped target asset by another's,
     * so the data flow Darwin derived between the assets implies a data flow between the servers.  The
     * asset-level flow is one Darwin derived on the same refresh, so this also shows the levels feed each
     * other within a refresh.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a data flow between servers from the flow between the assets they own")
    public void testServerLineageIsDerivedFromOwnedAssets() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dataFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                               OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                               DarwinFvtTestSupport.SERVER_SOURCE_GUID,
                                                                                               DarwinFvtTestSupport.SERVER_TARGET_GUID,
                                                                                               DarwinFvtTestSupport.ISC_MAPPED);

        assertNotNull(dataFlow,
                      "Expected a DataFlow from the mapped source server to the mapped target server for the mapped information supply"
                              + " chain - each server's capability owns one of the mapped assets, and data flows between those assets");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                   "The DataFlow between the mapped servers should have been created by Darwin");
    }


    /**
     * The indirect asset is reached from the mapped source asset through a process, two relationships along
     * the same information supply chain.  Its server therefore receives data from the mapped source server,
     * even though no single relationship joins the assets the two servers own.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin follows an indirect path between the assets that servers own")
    public void testServerLineageFollowsIndirectPath() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dataFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                               OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                               DarwinFvtTestSupport.SERVER_SOURCE_GUID,
                                                                                               DarwinFvtTestSupport.SERVER_INDIRECT_GUID,
                                                                                               DarwinFvtTestSupport.ISC_INDIRECT);

        assertNotNull(dataFlow,
                      "Expected a DataFlow from the mapped source server to the indirect server for the indirect information supply"
                              + " chain - the path from the source asset runs through a process to the asset the indirect server owns");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                   "The DataFlow between the source and indirect servers should have been created by Darwin");

        /*
         * The two supply chains are different paths, so the source server must not have acquired a flow to
         * the indirect server for the mapped supply chain, nor one to the target server for the indirect one.
         */
        assertEquals(1, DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                              OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                              DarwinFvtTestSupport.SERVER_SOURCE_GUID,
                                                              DarwinFvtTestSupport.SERVER_INDIRECT_GUID).size(),
                     "Expected exactly one DataFlow from the source server to the indirect server - only the indirect information"
                             + " supply chain reaches it");
        assertEquals(1, DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                              OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                              DarwinFvtTestSupport.SERVER_SOURCE_GUID,
                                                              DarwinFvtTestSupport.SERVER_TARGET_GUID).size(),
                     "Expected exactly one DataFlow from the source server to the target server - only the mapped information"
                             + " supply chain reaches it");
    }


    /**
     * The data flow between the stale assets exists only because of the mapping between their columns.  Once
     * the mapping is taken away, Darwin's next refresh has nothing to support the data flow it created, so it
     * withdraws it.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin withdraws a derived data flow when its mapping is removed")
    public void testDerivedAssetLineageIsWithdrawnWhenMappingGoes() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dataFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                               OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                               DarwinFvtTestSupport.ASSET_STALE_SOURCE_GUID,
                                                                                               DarwinFvtTestSupport.ASSET_STALE_TARGET_GUID,
                                                                                               DarwinFvtTestSupport.ISC_STALE);

        assertNotNull(dataFlow,
                      "Expected a DataFlow between the stale assets after the first refresh - their columns are joined by a DataMapping");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                   "The DataFlow between the stale assets should have been created by Darwin");

        /*
         * Take the mapping away.  It belongs to the archive, so the removal is made on the archive's behalf.
         */
        openMetadataStore.deleteRelationshipInStore(DarwinFvtTestSupport.MAPPING_STALE_GUID, DarwinFvtTestSupport.archiveOwnedDeleteOptions());

        OMAGPlatformExtension.refreshDarwin();

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.ASSET_STALE_SOURCE_GUID,
                                                         DarwinFvtTestSupport.ASSET_STALE_TARGET_GUID).isEmpty(),
                   "Darwin should have withdrawn the DataFlow between the stale assets once the DataMapping that supported it was"
                           + " removed");
    }
}

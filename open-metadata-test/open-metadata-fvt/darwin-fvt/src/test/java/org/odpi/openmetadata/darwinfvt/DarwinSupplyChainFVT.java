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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinSupplyChainFVT checks how Darwin establishes the information supply chain of the untagged relationships in
 * the layers beneath the coarse-grained lineage.  An untagged relationship is "supply chain unknown": it can join a
 * path of any supply chain, and once the path's supply chain is known - from a tagged relationship on it, or from the
 * relationship it proves in the layer above - the untagged relationship is given it.  Where an untagged relationship
 * lies on the paths of several supply chains, Darwin leaves it alone and records a copy of it for each.
 * <br>
 * The supply chains are established at the end of a refresh, and every test class refreshes Darwin before it runs,
 * so these assertions hold however many refreshes have happened by the time they run.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DarwinSupplyChainFVT
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
     * The path between the two products' assets starts with an untagged data flow and continues with a tagged one,
     * so the whole path belongs to the tagged one's supply chain: Darwin derives the dependency for it and fills it in
     * on the untagged data flow.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin fills in the supply chain of an untagged relationship from a tagged one on the same path")
    public void testSupplyChainIsFilledInWithinPath() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertNotNull(DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                         OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                         DarwinFvtTestSupport.PRODUCT_WITHIN_TARGET_GUID,
                                                                         DarwinFvtTestSupport.PRODUCT_WITHIN_SOURCE_GUID,
                                                                         DarwinFvtTestSupport.ISC_WITHIN_PATH),
                      "Expected a DigitalProductDependency for the tagged data flow's supply chain - an untagged relationship joins a"
                              + " path of any supply chain");

        OpenMetadataRelationship untaggedFlow = this.getRelationship(openMetadataStore,
                                                                     DarwinFvtTestSupport.ASSET_WITHIN_SOURCE_GUID,
                                                                     DarwinFvtTestSupport.PROCESS_WITHIN_GUID,
                                                                     DarwinFvtTestSupport.FLOW_WITHIN_UNTAGGED_GUID);

        assertEquals(DarwinFvtTestSupport.ISC_WITHIN_PATH, DarwinFvtTestSupport.getISCQualifiedName(untaggedFlow),
                     "Darwin should have filled in the supply chain of the tagged data flow further along the path");
    }


    /**
     * The only path between the two products' assets is untagged, and the archive asserts the dependency between the
     * products for a supply chain.  A path of unknown supply chain proves it, so there is no exception and Darwin adds
     * no untagged dependency of its own; and the path's supply chain is now known, so Darwin fills it in on the data
     * flow.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin fills in the supply chain of an untagged relationship from the relationship its path proves")
    public void testSupplyChainIsFilledInFromAbove() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PRODUCT_ABOVE_TARGET_GUID).isEmpty(),
                   "Darwin recorded an exception for a dependency that an untagged path proves - a path of unknown supply chain"
                           + " proves a relationship of any supply chain");

        List<OpenMetadataRelationship> dependencies = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                            OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                            DarwinFvtTestSupport.PRODUCT_ABOVE_TARGET_GUID,
                                                                                            DarwinFvtTestSupport.PRODUCT_ABOVE_SOURCE_GUID);

        assertEquals(1, dependencies.size(),
                     "Expected only the archive's dependency between the products - Darwin should not add an untagged one beside it");
        assertEquals(DarwinFvtTestSupport.DEPENDENCY_ABOVE_GUID, dependencies.get(0).getRelationshipGUID(),
                     "The dependency between the products should be the archive's own");

        OpenMetadataRelationship untaggedFlow = this.getRelationship(openMetadataStore,
                                                                     DarwinFvtTestSupport.ASSET_ABOVE_SOURCE_GUID,
                                                                     DarwinFvtTestSupport.ASSET_ABOVE_TARGET_GUID,
                                                                     DarwinFvtTestSupport.FLOW_ABOVE_UNTAGGED_GUID);

        assertEquals(DarwinFvtTestSupport.ISC_FROM_ABOVE, DarwinFvtTestSupport.getISCQualifiedName(untaggedFlow),
                     "Darwin should have filled in the supply chain of the dependency the data flow's path proves");
    }


    /**
     * The untagged data flow leads to a process that feeds two products' assets along two supply chains, so it lies on
     * the paths of both.  Darwin leaves it untagged and records a copy of it for each supply chain.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin copies an untagged relationship for each supply chain whose paths it lies on")
    public void testAmbiguousRelationshipIsCopiedForEachSupplyChain() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        List<OpenMetadataRelationship> dataFlows = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                         DarwinFvtTestSupport.ASSET_AMBIGUOUS_SOURCE_GUID,
                                                                                         DarwinFvtTestSupport.PROCESS_AMBIGUOUS_GUID);

        Set<String> copySupplyChains = new HashSet<>();
        boolean     originalFound    = false;

        for (OpenMetadataRelationship dataFlow : dataFlows)
        {
            if (DarwinFvtTestSupport.FLOW_AMBIGUOUS_UNTAGGED_GUID.equals(dataFlow.getRelationshipGUID()))
            {
                originalFound = true;

                assertNull(DarwinFvtTestSupport.getISCQualifiedName(dataFlow),
                           "Darwin gave a supply chain to an untagged data flow that lies on the paths of two supply chains");
            }
            else
            {
                assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                           "Found a data flow beside the ambiguous one that Darwin did not create: " + dataFlow.getRelationshipGUID());
                assertEquals(DarwinFvtTestSupport.SUPPLY_CHAIN_COPY_DERIVATION, DarwinFvtTestSupport.getDerivation(dataFlow),
                             "Darwin's copy of the ambiguous data flow should record its supply chain copy derivation");

                copySupplyChains.add(DarwinFvtTestSupport.getISCQualifiedName(dataFlow));
            }
        }

        assertTrue(originalFound, "The archive's untagged data flow has gone");
        assertEquals(Set.of(DarwinFvtTestSupport.ISC_AMBIGUOUS_1, DarwinFvtTestSupport.ISC_AMBIGUOUS_2), copySupplyChains,
                     "Expected exactly one copy of the ambiguous data flow for each of its two supply chains");
        assertEquals(3, dataFlows.size(),
                     "Expected the archive's data flow and one copy for each supply chain - no duplicates");
    }


    /**
     * Return a relationship by its unique identifier from those between two elements.
     *
     * @param openMetadataStore store to read through
     * @param end1GUID element at end 1
     * @param end2GUID element at end 2
     * @param relationshipGUID the relationship wanted
     * @return the relationship
     * @throws Exception the retrieval failed, or the relationship is not there
     */
    private OpenMetadataRelationship getRelationship(OpenMetadataStore openMetadataStore,
                                                     String            end1GUID,
                                                     String            end2GUID,
                                                     String            relationshipGUID) throws Exception
    {
        for (OpenMetadataRelationship relationship : DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                           OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                           end1GUID,
                                                                                           end2GUID))
        {
            if (relationshipGUID.equals(relationship.getRelationshipGUID()))
            {
                return relationship;
            }
        }

        throw new AssertionError("Relationship " + relationshipGUID + " between " + end1GUID + " and " + end2GUID + " has gone");
    }
}

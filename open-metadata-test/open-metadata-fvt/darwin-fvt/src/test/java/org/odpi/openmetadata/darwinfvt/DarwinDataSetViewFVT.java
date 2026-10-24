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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinDataSetViewFVT checks that Darwin treats a data set and the content it is a view over (DataSetContent) as the
 * same element when it follows lineage.  The members of digital products are often abstract data sets, while the
 * lineage from traces is attached to the physical elements beneath them - so without this, the products' dependencies
 * would not be found.
 * <br>
 * As in the other classes, Darwin's refresh is triggered once for the class.  None of these tests changes the
 * fixture.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DarwinDataSetViewFVT
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
     * Each product's member is a data set over a physical asset, and the lineage runs between the physical assets
     * through a process.  Treating each data set as its content, the target product depends on the source product.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a dependency between products whose data sets are views over assets joined by lineage")
    public void testDependencyIsDerivedThroughDataSetContent() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dependency = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                                 OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                                 DarwinFvtTestSupport.PRODUCT_VIEW_TARGET_GUID,
                                                                                                 DarwinFvtTestSupport.PRODUCT_VIEW_SOURCE_GUID,
                                                                                                 DarwinFvtTestSupport.ISC_VIEW);

        assertNotNull(dependency,
                      "Expected a DigitalProductDependency between the products whose data sets are views over the physical assets"
                              + " joined by lineage - a data set is treated as its content");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dependency),
                   "The dependency between the products should have been created by Darwin");
    }


    /**
     * The third product's data set covers the same physical asset as the target product's, but its DataSetContent names
     * a different supply chain from the lineage's.  The equivalence only holds for its own supply chain, so the third
     * product does not depend on the source product.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin only treats a data set as its content for the supply chain named on the DataSetContent")
    public void testEquivalenceKeepsToItsSupplyChain() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.PRODUCT_VIEW_OTHER_GUID,
                                                         DarwinFvtTestSupport.PRODUCT_VIEW_SOURCE_GUID).isEmpty(),
                   "Darwin derived a dependency through a DataSetContent whose supply chain differs from the lineage's");
    }


    /**
     * Each product's member is a data set over a relational table, and the tables are joined through a process by
     * DataMapping relationships - with no lineage between the assets the tables belong to.  Darwin follows the
     * mappings between a table and a process, so the target product depends on the source product.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a dependency between products whose data sets are views over tables mapped through a process")
    public void testDependencyIsDerivedThroughTableMappings() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dependency = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                                 OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                                 DarwinFvtTestSupport.PRODUCT_TABLE_VIEW_TARGET_GUID,
                                                                                                 DarwinFvtTestSupport.PRODUCT_TABLE_VIEW_SOURCE_GUID,
                                                                                                 DarwinFvtTestSupport.ISC_TABLE_VIEW);

        assertNotNull(dependency,
                      "Expected a DigitalProductDependency between the products whose data sets are views over tables joined by"
                              + " table-to-process DataMapping relationships");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dependency),
                   "The dependency between the products should have been created by Darwin");
    }
}

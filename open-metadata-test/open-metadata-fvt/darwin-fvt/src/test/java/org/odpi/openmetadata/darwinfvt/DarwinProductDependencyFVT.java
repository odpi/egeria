/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinProductDependencyFVT drives the Darwin Product Dependency Manager and checks the top level of what it
 * maintains: the DigitalProductDependency relationships between digital products, derived from the lineage
 * between the products' assets, and the exceptions it records for dependencies asserted by external users
 * that the lineage does not prove.
 * <br>
 * One refresh is enough for most of the assertions here, so it is done once for the class.  The test that
 * checks an exception being cleared removes the dependency that caused it and refreshes again.  The test
 * that checks the exception was raised reads the same set, so the methods are ordered to run it first.
 */
@ExtendWith(OMAGPlatformExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DarwinProductDependencyFVT
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
     * The archive asserts that the mapped target product depends on the mapped source product, but does not
     * say through which information supply chain.  The lineage between the products' assets proves the
     * dependency through the mapped supply chain, so Darwin fills that in on the archive's relationship - on
     * the archive's behalf, since the archive owns it - and does not create a relationship of its own beside it.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @Order(1)
    @DisplayName("Darwin fills in the supply chain on an asserted dependency that lineage proves")
    public void testSupplyChainIsFilledInOnAssertedDependency() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        List<OpenMetadataRelationship> dependencies = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                            OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                            DarwinFvtTestSupport.PRODUCT_TARGET_GUID,
                                                                                            DarwinFvtTestSupport.PRODUCT_SOURCE_GUID);

        assertEquals(1, dependencies.size(),
                     "Expected exactly one DigitalProductDependency from the mapped target product to the mapped source product - the"
                             + " archive's assertion should stand in for the one Darwin would otherwise create");

        OpenMetadataRelationship dependency = dependencies.get(0);

        assertEquals(DarwinFvtTestSupport.DEPENDENCY_UNSET_ISC_GUID, dependency.getRelationshipGUID(),
                     "The dependency between the mapped products should be the archive's own relationship, not a replacement");
        assertFalse(DarwinFvtTestSupport.isCreatedByDarwin(dependency),
                    "The archive's dependency between the mapped products appears to have been replaced by one of Darwin's");
        assertEquals(DarwinFvtTestSupport.ISC_MAPPED, DarwinFvtTestSupport.getISCQualifiedName(dependency),
                     "Darwin should have filled in the information supply chain of the archive's dependency from the lineage that"
                             + " proves it");
    }


    /**
     * The indirect product's asset is reached from the mapped source product's asset through a process along
     * one information supply chain.  Nothing asserts that dependency, so Darwin creates it.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @Order(2)
    @DisplayName("Darwin derives a dependency along an indirect lineage path")
    public void testDependencyIsDerivedAlongIndirectPath() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        List<OpenMetadataRelationship> dependencies = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                            OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                            DarwinFvtTestSupport.PRODUCT_INDIRECT_GUID,
                                                                                            DarwinFvtTestSupport.PRODUCT_SOURCE_GUID);

        assertEquals(1, dependencies.size(),
                     "Expected exactly one DigitalProductDependency from the indirect product to the mapped source product - its asset"
                             + " is reached through a process along the indirect information supply chain");

        OpenMetadataRelationship dependency = dependencies.get(0);

        assertEquals(DarwinFvtTestSupport.ISC_INDIRECT, DarwinFvtTestSupport.getISCQualifiedName(dependency),
                     "The derived dependency should carry the information supply chain of the lineage path it was derived from");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dependency),
                   "The dependency of the indirect product should have been created by Darwin");
        assertNotNull(DarwinFvtTestSupport.getLabel(dependency),
                      "Darwin should label the dependencies it derives");

        /*
         * The dependency runs one way: the source product does not depend on the indirect one.
         */
        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.PRODUCT_SOURCE_GUID,
                                                         DarwinFvtTestSupport.PRODUCT_INDIRECT_GUID).isEmpty(),
                   "Darwin derived a dependency of the mapped source product on the indirect product - the data flows the other way");
    }


    /**
     * The broken chain product's asset is reached from the same process, but the relationship that reaches it
     * belongs to a different information supply chain from the one that reaches the process.  A path has to
     * keep to one supply chain, so no dependency follows.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @Order(3)
    @DisplayName("Darwin does not follow a path that changes supply chain")
    public void testPathThatChangesSupplyChainIsNotFollowed() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.PRODUCT_BROKEN_CHAIN_GUID,
                                                         DarwinFvtTestSupport.PRODUCT_SOURCE_GUID).isEmpty(),
                   "Darwin derived a dependency of the broken chain product on the mapped source product, but the path to its asset"
                           + " changes information supply chain at the process and so does not count");
    }


    /**
     * The unproven product's asset has no lineage, yet the archive asserts that the product depends on the
     * mapped source product.  Darwin does not remove a relationship it did not create; it records an exception
     * against the dependent product instead, linked to its own exception type, listing the unproven
     * relationship.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @Order(4)
    @DisplayName("Darwin records an exception for an asserted dependency that lineage does not prove")
    public void testUnprovenDependencyIsRecordedAsAnException() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        /*
         * The assertion stands.
         */
        List<OpenMetadataRelationship> dependencies = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                            OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                                                            DarwinFvtTestSupport.PRODUCT_UNPROVEN_GUID,
                                                                                            DarwinFvtTestSupport.PRODUCT_SOURCE_GUID);

        assertEquals(1, dependencies.size(),
                     "The unproven dependency asserted by the archive should still be there - Darwin never removes a relationship it"
                             + " did not create");
        assertEquals(DarwinFvtTestSupport.ISC_UNPROVEN, DarwinFvtTestSupport.getISCQualifiedName(dependencies.get(0)),
                     "Darwin should not have changed the information supply chain on the unproven dependency");

        /*
         * The exception type exists and is Darwin's.
         */
        OpenMetadataElement exceptionType = openMetadataStore.getMetadataElementByUniqueName(DarwinFvtTestSupport.EXCEPTION_TYPE_QUALIFIED_NAME,
                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                             DarwinFvtTestSupport.lineageGetOptions());

        assertNotNull(exceptionType,
                      "Darwin did not create its exception type " + DarwinFvtTestSupport.EXCEPTION_TYPE_QUALIFIED_NAME);
        assertEquals(OpenMetadataType.EXCEPTION_TYPE.typeName, exceptionType.getType().getTypeName(),
                     "Darwin's exception type is not an ExceptionType");

        /*
         * The exception is recorded against the dependent product and names the unproven relationship.
         */
        List<RelatedMetadataElement> exceptions = DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PRODUCT_UNPROVEN_GUID);

        assertEquals(1, exceptions.size(),
                     "Expected exactly one Exception relationship on the unproven product");

        RelatedMetadataElement exception = exceptions.get(0);

        assertNotNull(exception.getElement(), "The exception on the unproven product is not linked to an exception type");
        assertEquals(exceptionType.getElementGUID(), exception.getElement().getElementGUID(),
                     "The exception on the unproven product should be linked to Darwin's exception type");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(exception),
                   "The exception on the unproven product should have been created by Darwin");

        List<String> affectedRelationships = DarwinFvtTestSupport.getAffectedRelationships(exception);

        assertNotNull(affectedRelationships, "The exception on the unproven product does not list any affected relationships");
        assertEquals(List.of(DarwinFvtTestSupport.DEPENDENCY_UNPROVEN_GUID), affectedRelationships,
                     "The exception on the unproven product should list exactly the unproven dependency relationship");

        /*
         * Products whose asserted dependencies the lineage proves get no exception.
         */
        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PRODUCT_TARGET_GUID).isEmpty(),
                   "Darwin recorded an exception against the mapped target product, whose dependency the lineage proves");
    }


    /**
     * Once the unproven dependency is taken away, there is nothing left for the exception to record, so
     * Darwin's next refresh clears it.  This is ordered last because it takes away what the exception test
     * asserts on.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @Order(5)
    @DisplayName("Darwin clears the exception once the unproven dependency is removed")
    public void testExceptionIsClearedWhenUnprovenDependencyGoes() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        /*
         * The exception should be there before the dependency goes.
         */
        assertFalse(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PRODUCT_UNPROVEN_GUID).isEmpty(),
                    "Expected an exception on the unproven product before its dependency is removed");

        openMetadataStore.deleteRelationshipInStore(DarwinFvtTestSupport.DEPENDENCY_UNPROVEN_GUID, DarwinFvtTestSupport.archiveOwnedDeleteOptions());

        OMAGPlatformExtension.refreshDarwin();

        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PRODUCT_UNPROVEN_GUID).isEmpty(),
                   "Darwin should have cleared the exception on the unproven product once the unproven dependency was removed");
    }
}

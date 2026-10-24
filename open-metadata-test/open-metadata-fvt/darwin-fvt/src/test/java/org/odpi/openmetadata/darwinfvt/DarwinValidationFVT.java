/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinValidationFVT checks how Darwin validates the lineage relationships asserted by external users against the
 * layer beneath them.  A relationship the layer beneath does not prove is recorded with an exception of its own
 * relationship type, against the element the data or control reaches - but only when the layer beneath is catalogued
 * for both of its ends.  Where it is not, the missing detail is no evidence against the relationship, and nothing is
 * recorded.
 * <br>
 * As in the other classes, Darwin's refresh is triggered once for the class.  None of these tests changes the
 * fixture.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DarwinValidationFVT
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
     * Both assets' columns take part in data mappings, so the schema detail is catalogued for both ends of the data
     * flow the archive asserts between them - and none of the mappings joins the two.  The data flow is unproven, so
     * Darwin records it against the asset the data reaches, with the exception type for DataFlow relationships.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin records an exception for an asserted data flow that the schema detail does not prove")
    public void testUnprovenDataFlowIsRecordedAsAnException() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertEquals(1, DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                              OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                              DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_SOURCE_GUID,
                                                              DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_TARGET_GUID).size(),
                     "The unproven data flow asserted by the archive should still be there - Darwin never removes a relationship it"
                             + " did not create");

        this.assertSingleException(openMetadataStore,
                                   DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_TARGET_GUID,
                                   DarwinFvtTestSupport.DATA_FLOW_EXCEPTION_TYPE_QUALIFIED_NAME,
                                   DarwinFvtTestSupport.FLOW_UNPROVEN_GUID);

        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_SOURCE_GUID).isEmpty(),
                   "Darwin recorded an exception against the asset the unproven data flow comes from - it belongs on the asset the"
                           + " data reaches");
    }


    /**
     * Neither asset has any schema detail, so nothing in the layer beneath could prove the data flow the archive
     * asserts between them, or show it to be unproven.  Darwin records nothing.  The asserted data flows elsewhere in
     * the fixture that end at a process with no data mappings are unchecked in the same way.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin records no exception for an asserted data flow whose ends have no schema detail")
    public void testUncheckedDataFlowIsNotRecorded() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.ASSET_UNCHECKED_TARGET_GUID).isEmpty(),
                   "Darwin recorded an exception for a data flow between assets that have no schema detail - the missing detail is"
                           + " no evidence against it");
        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PROCESS_INDIRECT_GUID).isEmpty(),
                   "Darwin recorded an exception for the data flow into the indirect process, which takes part in no data mapping");
        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.ASSET_INDIRECT_GUID).isEmpty(),
                   "Darwin recorded an exception for the data flow out of the indirect process, which takes part in no data mapping");
    }


    /**
     * Both servers use a process, so the data fabric is catalogued for both ends of the control flow the archive
     * asserts between them - but there is no control flow between the processes.  The control flow is unproven, so
     * Darwin records it against the server control passes to, with the exception type for ControlFlow relationships.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin records an exception for an asserted control flow between servers that the data fabric does not prove")
    public void testUnprovenControlFlowIsRecordedAsAnException() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        this.assertSingleException(openMetadataStore,
                                   DarwinFvtTestSupport.SERVER_UNPROVEN_CONTROL_TO_GUID,
                                   DarwinFvtTestSupport.CONTROL_FLOW_EXCEPTION_TYPE_QUALIFIED_NAME,
                                   DarwinFvtTestSupport.CONTROL_FLOW_UNPROVEN_GUID);

        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.SERVER_CONTROL_TO_GUID).isEmpty(),
                   "Darwin recorded an exception against a server whose control flow it derived itself");
    }


    /**
     * The product has no members, so no lineage could prove the dependency the archive asserts from it, or show it to
     * be unproven.  Darwin records nothing.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin records no exception for an asserted dependency of a product with no members")
    public void testUncheckedDependencyIsNotRecorded() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        assertTrue(DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.PRODUCT_NO_MEMBERS_GUID).isEmpty(),
                   "Darwin recorded an exception against a product with no members - no lineage could prove or disprove its dependency");
    }


    /**
     * Check that an element has exactly one exception, created by Darwin, linked to the named exception type and
     * listing exactly the one unproven relationship.
     *
     * @param openMetadataStore store to read through
     * @param elementGUID the element the exception should be recorded against
     * @param exceptionTypeQualifiedName the exception type it should be linked to
     * @param unprovenRelationshipGUID the relationship it should list
     * @throws Exception the retrieval failed
     */
    private void assertSingleException(OpenMetadataStore openMetadataStore,
                                       String            elementGUID,
                                       String            exceptionTypeQualifiedName,
                                       String            unprovenRelationshipGUID) throws Exception
    {
        OpenMetadataElement exceptionType = openMetadataStore.getMetadataElementByUniqueName(exceptionTypeQualifiedName,
                                                                                             OpenMetadataProperty.QUALIFIED_NAME.name,
                                                                                             DarwinFvtTestSupport.lineageGetOptions());

        assertNotNull(exceptionType, "Darwin did not create its exception type " + exceptionTypeQualifiedName);

        List<RelatedMetadataElement> exceptions = DarwinFvtTestSupport.getExceptions(openMetadataStore, elementGUID);

        assertEquals(1, exceptions.size(), "Expected exactly one Exception relationship on element " + elementGUID);

        RelatedMetadataElement exception = exceptions.get(0);

        assertNotNull(exception.getElement(), "The exception on element " + elementGUID + " is not linked to an exception type");
        assertEquals(exceptionType.getElementGUID(), exception.getElement().getElementGUID(),
                     "The exception on element " + elementGUID + " should be linked to " + exceptionTypeQualifiedName);
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(exception),
                   "The exception on element " + elementGUID + " should have been created by Darwin");
        assertEquals(List.of(unprovenRelationshipGUID), DarwinFvtTestSupport.getAffectedRelationships(exception),
                     "The exception on element " + elementGUID + " should list exactly the unproven relationship");
    }
}

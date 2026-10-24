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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinSystemFabricFVT checks the lineage Darwin derives for processes: a process at the end of a DataMapping is a
 * data fabric element in its own right, and a process used by a software server's capability makes the server part
 * of the lineage whatever the use type.  It also checks that the system fabric carries the type of the lineage
 * beneath it - a ProcessCall proves a ProcessCall and a ControlFlow a ControlFlow - and that control flow never
 * proves a dependency between digital products.
 * <br>
 * As in the other classes, Darwin's refresh is triggered once for the class.  None of these tests changes the
 * fixture.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DarwinSystemFabricFVT
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
     * The process is used - not owned - by its server's capability, and data flows from it to an asset owned by a
     * second server, so data flows from the first server to the second.  The same process also feeds an asset that
     * a third server's capability only uses: a data asset counts for a server only when the server owns it, so
     * there is no data flow to the third server.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin counts a process for the server that uses it, and a data asset only for the server that owns it")
    public void testProcessUsedByServerIsPartOfTheSystemFabric() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dataFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                               OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                               DarwinFvtTestSupport.SERVER_PROCESS_USE_GUID,
                                                                                               DarwinFvtTestSupport.SERVER_PROCESS_OUTPUT_GUID,
                                                                                               DarwinFvtTestSupport.ISC_PROCESS_USE);

        assertNotNull(dataFlow,
                      "Expected a DataFlow from the server that uses the process to the server that owns the process's output asset"
                              + " - a process counts for the server whose capability uses it, whatever the use type");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                   "The DataFlow between the process's server and the output asset's server should have been created by Darwin");

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.SERVER_PROCESS_USE_GUID,
                                                         DarwinFvtTestSupport.SERVER_ONLY_USES_GUID).isEmpty(),
                   "Darwin derived a DataFlow to a server whose capability only uses (does not own) the data asset the process feeds");
    }


    /**
     * The two processes are joined by a ControlFlow, so control passes from the first process's server to the
     * second's.  That is a ControlFlow between the servers, not a DataFlow, and it says nothing about data, so the
     * products that the processes are members of do not depend on each other.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a control flow between servers, and no product dependency, from a control flow between processes")
    public void testControlFlowIsDerivedBetweenServers() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship controlFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                                  OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                                                                                                  DarwinFvtTestSupport.SERVER_CONTROL_FROM_GUID,
                                                                                                  DarwinFvtTestSupport.SERVER_CONTROL_TO_GUID,
                                                                                                  DarwinFvtTestSupport.ISC_CONTROL);

        assertNotNull(controlFlow,
                      "Expected a ControlFlow between the servers of the two processes joined by a ControlFlow");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(controlFlow),
                   "The ControlFlow between the servers should have been created by Darwin");
        assertEquals(DarwinFvtTestSupport.SERVER_CONTROL_FLOW_DERIVATION, DarwinFvtTestSupport.getDerivation(controlFlow),
                     "Darwin should record the rule that derived the ControlFlow in its derivation");
        assertNull(DarwinFvtTestSupport.getLabel(controlFlow),
                   "Darwin should leave the label - the summary of the data - to others");

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.SERVER_CONTROL_FROM_GUID,
                                                         DarwinFvtTestSupport.SERVER_CONTROL_TO_GUID).isEmpty(),
                   "Darwin derived a DataFlow between the servers from a ControlFlow - control flow is not data");

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.PRODUCT_CONTROL_TO_GUID,
                                                         DarwinFvtTestSupport.PRODUCT_CONTROL_FROM_GUID).isEmpty(),
                   "Darwin derived a DigitalProductDependency from a ControlFlow - a product depends on another product's data, not on"
                           + " the order in which their processes run");
    }


    /**
     * The two processes are joined by a ProcessCall, so the first process's server calls the second's.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a process call between servers from a process call between processes")
    public void testProcessCallIsDerivedBetweenServers() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship processCall = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                                  OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                                                                                                  DarwinFvtTestSupport.SERVER_CALLER_GUID,
                                                                                                  DarwinFvtTestSupport.SERVER_CALLED_GUID,
                                                                                                  DarwinFvtTestSupport.ISC_CALL);

        assertNotNull(processCall,
                      "Expected a ProcessCall between the servers of the two processes joined by a ProcessCall");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(processCall),
                   "The ProcessCall between the servers should have been created by Darwin");

        assertTrue(DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                         DarwinFvtTestSupport.SERVER_CALLER_GUID,
                                                         DarwinFvtTestSupport.SERVER_CALLED_GUID).isEmpty(),
                   "Darwin derived a DataFlow between the servers where the lineage beneath is a ProcessCall");
    }


    /**
     * A column of the asset is mapped to a process - the shape the Open Lineage Cataloguer produces for a table read
     * by a job.  The process is a data fabric element in its own right, so data flows from the asset to the process.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin derives a data flow from an asset to a process from the mapping between a column and the process")
    public void testDataFlowIsDerivedFromColumnToProcessMapping() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();

        OpenMetadataRelationship dataFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                               OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                               DarwinFvtTestSupport.ASSET_TABLE_GUID,
                                                                                               DarwinFvtTestSupport.PROCESS_MAPPED_GUID,
                                                                                               DarwinFvtTestSupport.ISC_TABLE_PROCESS);

        assertNotNull(dataFlow,
                      "Expected a DataFlow from the asset to the process its column is mapped to, for the mapping's information supply"
                              + " chain");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow),
                   "The DataFlow from the asset to the process should have been created by Darwin");
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.odpi.openmetadata.frameworks.openmetadata.enums.CapabilityAssetUseType;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumElementDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DarwinArchiveWriter builds the open metadata archive that seeds this suite's fixture: the data assets,
 * schema attributes, software servers, capabilities, digital products and the lineage between them that
 * Darwin works from.  See {@link DarwinFvtTestSupport} for what each set of the fixture is for.
 * <p>
 * The fixture is loaded as an archive rather than created through the access services so that every
 * relationship in it is owned by the archive: that is what makes them relationships "asserted by an
 * external user" from Darwin's point of view, and an instance owned elsewhere can only be changed on behalf
 * of its owner - which is the rule Darwin follows when it fills in an information supply chain.
 * <p>
 * The schema attributes are given an Anchors classification pointing at their asset rather than the full
 * schema type chain: the anchor is all Darwin uses to find the asset a schema element belongs to.
 */
class DarwinArchiveWriter extends OMRSArchiveWriter
{
    /*
     * Package visible: the fixture instances are owned by this archive, so a test that changes one has to name
     * it as the external source of the change.  See DarwinFvtTestSupport.archiveOwnedDeleteOptions().
     */
    static final String ARCHIVE_GUID        = "86c2883c-d7d8-4379-9b0d-660910a99707";
    static final String ARCHIVE_NAME        = "DarwinFvtArchive";
    private static final String ARCHIVE_DESCRIPTION = "Assets, servers, products and the lineage between them, used by the darwin-fvt test suite.";
    private static final String ORIGINATOR_NAME     = "Egeria darwin-fvt";
    private static final String ARCHIVE_VERSION     = "1.0";

    private static final String ASSET_TYPE_NAME      = OpenMetadataType.DATA_SET.typeName;
    private static final String COLUMN_TYPE_NAME     = OpenMetadataType.TABULAR_COLUMN.typeName;
    private static final String PROCESS_TYPE_NAME    = OpenMetadataType.PROCESS.typeName;
    private static final String SERVER_TYPE_NAME     = OpenMetadataType.SOFTWARE_SERVER.typeName;
    private static final String CAPABILITY_TYPE_NAME = OpenMetadataType.SOFTWARE_CAPABILITY.typeName;
    private static final String PRODUCT_TYPE_NAME    = OpenMetadataType.DIGITAL_PRODUCT.typeName;

    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;


    /**
     * Constructor - sets up the archive to build.  The open metadata types archive is a dependency because
     * the entities and relationships below are all of types it defines.
     */
    DarwinArchiveWriter()
    {
        List<OpenMetadataArchive> dependentArchives = new ArrayList<>();

        dependentArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        Date creationDate = new Date();

        this.archiveBuilder = new OMRSArchiveBuilder(ARCHIVE_GUID,
                                                      ARCHIVE_NAME,
                                                      ARCHIVE_DESCRIPTION,
                                                      OpenMetadataArchiveType.CONTENT_PACK,
                                                      ARCHIVE_VERSION,
                                                      ORIGINATOR_NAME,
                                                      null,
                                                      creationDate,
                                                      dependentArchives);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                    ARCHIVE_GUID,
                                                    ORIGINATOR_NAME,
                                                    creationDate,
                                                    1L,
                                                    ARCHIVE_VERSION);
    }


    /**
     * Build the archive and write it to the supplied file.
     *
     * @param archiveFile file to write to - its parent directory is created if it does not exist
     * @throws Exception the archive could not be written, which is fatal to the whole run
     */
    void writeArchive(File archiveFile) throws Exception
    {
        this.addMappedSet();
        this.addIndirectSet();
        this.addBrokenChainSet();
        this.addUnprovenSet();
        this.addStaleSet();
        this.addPresentSet();
        this.addProcessUseSet();
        this.addControlFlowSet();
        this.addProcessCallSet();
        this.addTableToProcessSet();
        this.addUnprovenDataFlowSet();
        this.addUncheckedDataFlowSet();
        this.addUnprovenControlFlowSet();
        this.addUncheckedDependencySet();
        this.addSupplyChainWithinPathSet();
        this.addSupplyChainFromAboveSet();
        this.addAmbiguousSupplyChainSet();
        this.addDataSetViewSet();
        this.addTableViewSet();
        this.addAdoptedDataFlowSet();

        File parentDirectory = archiveFile.getParentFile();

        if ((parentDirectory != null) && (! parentDirectory.exists()) && (! parentDirectory.mkdirs()))
        {
            throw new IllegalStateException("Could not create the directory for " + archiveFile.getAbsolutePath());
        }

        super.writeOpenMetadataArchive(archiveFile.getAbsolutePath(), archiveBuilder.getOpenMetadataArchive());
    }


    /**
     * Set 1 - two data assets whose columns are joined by a data mapping.  Each is owned by a server's
     * capability and is the member of a product, and the products' dependency is asserted without a supply
     * chain.
     */
    private void addMappedSet()
    {
        EntityDetail sourceAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_SOURCE_GUID, "Mapped:source", null);
        EntityDetail targetAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_TARGET_GUID, "Mapped:target", null);
        EntityDetail sourceColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_SOURCE_GUID, "Mapped:source:column", sourceAsset);
        EntityDetail targetColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_TARGET_GUID, "Mapped:target:column", targetAsset);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.MAPPING_SOURCE_TO_TARGET_GUID,
                        sourceColumn,
                        targetColumn,
                        DarwinFvtTestSupport.ISC_MAPPED);

        this.addOwningServer(DarwinFvtTestSupport.SERVER_SOURCE_GUID,
                             "Mapped:source:server",
                             DarwinFvtTestSupport.CAPABILITY_SOURCE_GUID,
                             DarwinFvtTestSupport.SUPPORTED_CAPABILITY_SOURCE_GUID,
                             DarwinFvtTestSupport.ASSET_USE_SOURCE_GUID,
                             sourceAsset);

        this.addOwningServer(DarwinFvtTestSupport.SERVER_TARGET_GUID,
                             "Mapped:target:server",
                             DarwinFvtTestSupport.CAPABILITY_TARGET_GUID,
                             DarwinFvtTestSupport.SUPPORTED_CAPABILITY_TARGET_GUID,
                             DarwinFvtTestSupport.ASSET_USE_TARGET_GUID,
                             targetAsset);

        EntityDetail sourceProduct = this.addProduct(DarwinFvtTestSupport.PRODUCT_SOURCE_GUID, "Mapped:source:product", DarwinFvtTestSupport.MEMBERSHIP_SOURCE_GUID, sourceAsset);
        EntityDetail targetProduct = this.addProduct(DarwinFvtTestSupport.PRODUCT_TARGET_GUID, "Mapped:target:product", DarwinFvtTestSupport.MEMBERSHIP_TARGET_GUID, targetAsset);

        /*
         * The target product depends on the source product - the dependent product is at end 1 - but whoever
         * asserted it did not say through which information supply chain.
         */
        this.addLineage(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.DEPENDENCY_UNSET_ISC_GUID,
                        targetProduct,
                        sourceProduct,
                        null);
    }


    /**
     * Set 2 - an asset reached from the mapped source asset through a process, along its own supply chain.
     * It is owned by a third server and is the member of a third product.
     */
    private void addIndirectSet()
    {
        EntityDetail sourceAsset   = archiveBuilder.getEntity(DarwinFvtTestSupport.ASSET_SOURCE_GUID);
        EntityDetail process       = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_INDIRECT_GUID, "Indirect:process", null);
        EntityDetail indirectAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_INDIRECT_GUID, "Indirect:asset", null);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_SOURCE_TO_PROCESS_GUID,
                        sourceAsset,
                        process,
                        DarwinFvtTestSupport.ISC_INDIRECT);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_PROCESS_TO_INDIRECT_GUID,
                        process,
                        indirectAsset,
                        DarwinFvtTestSupport.ISC_INDIRECT);

        this.addOwningServer(DarwinFvtTestSupport.SERVER_INDIRECT_GUID,
                             "Indirect:server",
                             DarwinFvtTestSupport.CAPABILITY_INDIRECT_GUID,
                             DarwinFvtTestSupport.SUPPORTED_CAPABILITY_INDIRECT_GUID,
                             DarwinFvtTestSupport.ASSET_USE_INDIRECT_GUID,
                             indirectAsset);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_INDIRECT_GUID, "Indirect:product", DarwinFvtTestSupport.MEMBERSHIP_INDIRECT_GUID, indirectAsset);
    }


    /**
     * Set 3 - an asset reached from the same process, but along a different supply chain from the one that
     * reaches the process.  A path has to keep to one supply chain, so this is not a dependency.
     */
    private void addBrokenChainSet()
    {
        EntityDetail process          = archiveBuilder.getEntity(DarwinFvtTestSupport.PROCESS_INDIRECT_GUID);
        EntityDetail brokenChainAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_BROKEN_CHAIN_GUID, "BrokenChain:asset", null);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_PROCESS_TO_BROKEN_CHAIN_GUID,
                        process,
                        brokenChainAsset,
                        DarwinFvtTestSupport.ISC_OTHER);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_BROKEN_CHAIN_GUID, "BrokenChain:product", DarwinFvtTestSupport.MEMBERSHIP_BROKEN_CHAIN_GUID, brokenChainAsset);
    }


    /**
     * Set 4 - a product whose asset has no lineage, with a dependency on the mapped source product that
     * nothing backs up.
     */
    private void addUnprovenSet()
    {
        EntityDetail sourceProduct   = archiveBuilder.getEntity(DarwinFvtTestSupport.PRODUCT_SOURCE_GUID);
        EntityDetail unprovenAsset   = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNPROVEN_GUID, "Unproven:asset", null);
        EntityDetail unprovenProduct = this.addProduct(DarwinFvtTestSupport.PRODUCT_UNPROVEN_GUID, "Unproven:product", DarwinFvtTestSupport.MEMBERSHIP_UNPROVEN_GUID, unprovenAsset);

        this.addLineage(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.DEPENDENCY_UNPROVEN_GUID,
                        unprovenProduct,
                        sourceProduct,
                        DarwinFvtTestSupport.ISC_UNPROVEN);
    }


    /**
     * Set 5 - two assets joined by a mapping and nothing else, so that the test can take the mapping away.
     */
    private void addStaleSet()
    {
        EntityDetail sourceAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_STALE_SOURCE_GUID, "Stale:source", null);
        EntityDetail targetAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_STALE_TARGET_GUID, "Stale:target", null);
        EntityDetail sourceColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_STALE_SOURCE_GUID, "Stale:source:column", sourceAsset);
        EntityDetail targetColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_STALE_TARGET_GUID, "Stale:target:column", targetAsset);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.MAPPING_STALE_GUID,
                        sourceColumn,
                        targetColumn,
                        DarwinFvtTestSupport.ISC_STALE);
    }


    /**
     * Set 6 - two assets joined by a mapping, where the data flow between the assets is already asserted for
     * the same supply chain.
     */
    private void addPresentSet()
    {
        EntityDetail sourceAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_PRESENT_SOURCE_GUID, "Present:source", null);
        EntityDetail targetAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_PRESENT_TARGET_GUID, "Present:target", null);
        EntityDetail sourceColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_PRESENT_SOURCE_GUID, "Present:source:column", sourceAsset);
        EntityDetail targetColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_PRESENT_TARGET_GUID, "Present:target:column", targetAsset);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.MAPPING_PRESENT_GUID,
                        sourceColumn,
                        targetColumn,
                        DarwinFvtTestSupport.ISC_PRESENT);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_PRESENT_GUID,
                        sourceAsset,
                        targetAsset,
                        DarwinFvtTestSupport.ISC_PRESENT);
    }


    /**
     * Set 7 - a process used by one server's capability, feeding an asset owned by a second server and an asset
     * that a third server's capability only uses.
     */
    private void addProcessUseSet()
    {
        EntityDetail process     = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_USED_GUID, "ProcessUse:process", null);
        EntityDetail outputAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_PROCESS_OUTPUT_GUID, "ProcessUse:output", null);
        EntityDetail usedAsset   = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ONLY_USED_GUID, "ProcessUse:only-used", null);

        this.addUsingServer(DarwinFvtTestSupport.SERVER_PROCESS_USE_GUID,
                            "ProcessUse:process:server",
                            DarwinFvtTestSupport.CAPABILITY_PROCESS_USE_GUID,
                            DarwinFvtTestSupport.SUPPORTED_CAPABILITY_PROCESS_USE_GUID,
                            DarwinFvtTestSupport.ASSET_USE_PROCESS_USE_GUID,
                            process,
                            CapabilityAssetUseType.USES);

        this.addOwningServer(DarwinFvtTestSupport.SERVER_PROCESS_OUTPUT_GUID,
                             "ProcessUse:output:server",
                             DarwinFvtTestSupport.CAPABILITY_PROCESS_OUTPUT_GUID,
                             DarwinFvtTestSupport.SUPPORTED_CAPABILITY_PROCESS_OUTPUT_GUID,
                             DarwinFvtTestSupport.ASSET_USE_PROCESS_OUTPUT_GUID,
                             outputAsset);

        this.addUsingServer(DarwinFvtTestSupport.SERVER_ONLY_USES_GUID,
                            "ProcessUse:only-used:server",
                            DarwinFvtTestSupport.CAPABILITY_ONLY_USES_GUID,
                            DarwinFvtTestSupport.SUPPORTED_CAPABILITY_ONLY_USES_GUID,
                            DarwinFvtTestSupport.ASSET_USE_ONLY_USES_GUID,
                            usedAsset,
                            CapabilityAssetUseType.USES);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_PROCESS_TO_OUTPUT_GUID,
                        process,
                        outputAsset,
                        DarwinFvtTestSupport.ISC_PROCESS_USE);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_PROCESS_TO_ONLY_USED_GUID,
                        process,
                        usedAsset,
                        DarwinFvtTestSupport.ISC_PROCESS_USE);
    }


    /**
     * Set 8 - two processes on their own servers, each the member of its own product, joined by a ControlFlow.
     */
    private void addControlFlowSet()
    {
        EntityDetail fromProcess = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_CONTROL_FROM_GUID, "Control:from", null);
        EntityDetail toProcess   = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_CONTROL_TO_GUID, "Control:to", null);

        this.addUsingServer(DarwinFvtTestSupport.SERVER_CONTROL_FROM_GUID,
                            "Control:from:server",
                            DarwinFvtTestSupport.CAPABILITY_CONTROL_FROM_GUID,
                            DarwinFvtTestSupport.SUPPORTED_CAPABILITY_CONTROL_FROM_GUID,
                            DarwinFvtTestSupport.ASSET_USE_CONTROL_FROM_GUID,
                            fromProcess,
                            CapabilityAssetUseType.USES);

        this.addUsingServer(DarwinFvtTestSupport.SERVER_CONTROL_TO_GUID,
                            "Control:to:server",
                            DarwinFvtTestSupport.CAPABILITY_CONTROL_TO_GUID,
                            DarwinFvtTestSupport.SUPPORTED_CAPABILITY_CONTROL_TO_GUID,
                            DarwinFvtTestSupport.ASSET_USE_CONTROL_TO_GUID,
                            toProcess,
                            CapabilityAssetUseType.USES);

        this.addLineage(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.CONTROL_FLOW_GUID,
                        fromProcess,
                        toProcess,
                        DarwinFvtTestSupport.ISC_CONTROL);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_CONTROL_FROM_GUID, "Control:from:product", DarwinFvtTestSupport.MEMBERSHIP_CONTROL_FROM_GUID, fromProcess);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_CONTROL_TO_GUID, "Control:to:product", DarwinFvtTestSupport.MEMBERSHIP_CONTROL_TO_GUID, toProcess);
    }


    /**
     * Set 9 - two processes on their own servers joined by a ProcessCall.
     */
    private void addProcessCallSet()
    {
        EntityDetail caller = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_CALLER_GUID, "Call:caller", null);
        EntityDetail called = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_CALLED_GUID, "Call:called", null);

        this.addUsingServer(DarwinFvtTestSupport.SERVER_CALLER_GUID,
                            "Call:caller:server",
                            DarwinFvtTestSupport.CAPABILITY_CALLER_GUID,
                            DarwinFvtTestSupport.SUPPORTED_CAPABILITY_CALLER_GUID,
                            DarwinFvtTestSupport.ASSET_USE_CALLER_GUID,
                            caller,
                            CapabilityAssetUseType.USES);

        this.addUsingServer(DarwinFvtTestSupport.SERVER_CALLED_GUID,
                            "Call:called:server",
                            DarwinFvtTestSupport.CAPABILITY_CALLED_GUID,
                            DarwinFvtTestSupport.SUPPORTED_CAPABILITY_CALLED_GUID,
                            DarwinFvtTestSupport.ASSET_USE_CALLED_GUID,
                            called,
                            CapabilityAssetUseType.USES);

        this.addLineage(OpenMetadataType.PROCESS_CALL_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.PROCESS_CALL_GUID,
                        caller,
                        called,
                        DarwinFvtTestSupport.ISC_CALL);
    }


    /**
     * Set 10 - a column of an asset mapped to a process.
     */
    private void addTableToProcessSet()
    {
        EntityDetail tableAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_TABLE_GUID, "TableToProcess:asset", null);
        EntityDetail column     = this.addColumn(DarwinFvtTestSupport.COLUMN_TABLE_GUID, "TableToProcess:asset:column", tableAsset);
        EntityDetail process    = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_MAPPED_GUID, "TableToProcess:process", null);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.MAPPING_TABLE_TO_PROCESS_GUID,
                        column,
                        process,
                        DarwinFvtTestSupport.ISC_TABLE_PROCESS);
    }


    /**
     * Set 11 - two assets whose columns are mapped to other assets' columns but not to each other, with a data flow
     * between the two asserted by the archive.
     */
    private void addUnprovenDataFlowSet()
    {
        EntityDetail sourceAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_SOURCE_GUID, "UnprovenFlow:source", null);
        EntityDetail targetAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_TARGET_GUID, "UnprovenFlow:target", null);
        EntityDetail otherAsset1  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_OTHER_1_GUID, "UnprovenFlow:other1", null);
        EntityDetail otherAsset2  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNPROVEN_FLOW_OTHER_2_GUID, "UnprovenFlow:other2", null);
        EntityDetail sourceColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_UNPROVEN_FLOW_SOURCE_GUID, "UnprovenFlow:source:column", sourceAsset);
        EntityDetail targetColumn = this.addColumn(DarwinFvtTestSupport.COLUMN_UNPROVEN_FLOW_TARGET_GUID, "UnprovenFlow:target:column", targetAsset);
        EntityDetail otherColumn1 = this.addColumn(DarwinFvtTestSupport.COLUMN_UNPROVEN_FLOW_OTHER_1_GUID, "UnprovenFlow:other1:column", otherAsset1);
        EntityDetail otherColumn2 = this.addColumn(DarwinFvtTestSupport.COLUMN_UNPROVEN_FLOW_OTHER_2_GUID, "UnprovenFlow:other2:column", otherAsset2);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.MAPPING_UNPROVEN_FLOW_OUT_GUID,
                        sourceColumn,
                        otherColumn1,
                        DarwinFvtTestSupport.ISC_UNPROVEN_FLOW);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.MAPPING_UNPROVEN_FLOW_IN_GUID,
                        otherColumn2,
                        targetColumn,
                        DarwinFvtTestSupport.ISC_UNPROVEN_FLOW);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_UNPROVEN_GUID,
                        sourceAsset,
                        targetAsset,
                        DarwinFvtTestSupport.ISC_UNPROVEN_FLOW);
    }


    /**
     * Set 12 - two assets with no schema detail and a data flow between them asserted by the archive.
     */
    private void addUncheckedDataFlowSet()
    {
        EntityDetail sourceAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNCHECKED_SOURCE_GUID, "Unchecked:source", null);
        EntityDetail targetAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_UNCHECKED_TARGET_GUID, "Unchecked:target", null);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.FLOW_UNCHECKED_GUID,
                        sourceAsset,
                        targetAsset,
                        DarwinFvtTestSupport.ISC_UNCHECKED);
    }


    /**
     * Set 13 - two servers, each using a process, with a control flow between the servers asserted by the archive and
     * none between the processes.
     */
    private void addUnprovenControlFlowSet()
    {
        EntityDetail fromProcess = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_UNPROVEN_CONTROL_FROM_GUID, "UnprovenControl:from", null);
        EntityDetail toProcess   = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_UNPROVEN_CONTROL_TO_GUID, "UnprovenControl:to", null);

        EntityDetail fromServer = this.addUsingServer(DarwinFvtTestSupport.SERVER_UNPROVEN_CONTROL_FROM_GUID,
                                                      "UnprovenControl:from:server",
                                                      DarwinFvtTestSupport.CAPABILITY_UNPROVEN_CONTROL_FROM_GUID,
                                                      DarwinFvtTestSupport.SUPPORTED_CAPABILITY_UNPROVEN_CONTROL_FROM_GUID,
                                                      DarwinFvtTestSupport.ASSET_USE_UNPROVEN_CONTROL_FROM_GUID,
                                                      fromProcess,
                                                      CapabilityAssetUseType.USES);

        EntityDetail toServer = this.addUsingServer(DarwinFvtTestSupport.SERVER_UNPROVEN_CONTROL_TO_GUID,
                                                    "UnprovenControl:to:server",
                                                    DarwinFvtTestSupport.CAPABILITY_UNPROVEN_CONTROL_TO_GUID,
                                                    DarwinFvtTestSupport.SUPPORTED_CAPABILITY_UNPROVEN_CONTROL_TO_GUID,
                                                    DarwinFvtTestSupport.ASSET_USE_UNPROVEN_CONTROL_TO_GUID,
                                                    toProcess,
                                                    CapabilityAssetUseType.USES);

        this.addLineage(OpenMetadataType.CONTROL_FLOW_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.CONTROL_FLOW_UNPROVEN_GUID,
                        fromServer,
                        toServer,
                        DarwinFvtTestSupport.ISC_UNPROVEN_FLOW);
    }


    /**
     * Set 14 - a product with no members and a dependency on the mapped source product asserted by the archive.
     */
    private void addUncheckedDependencySet()
    {
        EntityDetail sourceProduct   = archiveBuilder.getEntity(DarwinFvtTestSupport.PRODUCT_SOURCE_GUID);
        EntityDetail noMemberProduct = this.addEntity(PRODUCT_TYPE_NAME, DarwinFvtTestSupport.PRODUCT_NO_MEMBERS_GUID, "NoMembers:product", null);

        this.addLineage(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.DEPENDENCY_UNCHECKED_GUID,
                        noMemberProduct,
                        sourceProduct,
                        DarwinFvtTestSupport.ISC_UNCHECKED);
    }


    /**
     * Set 15 - two products' assets joined through a process by an untagged data flow and then a tagged one.
     */
    private void addSupplyChainWithinPathSet()
    {
        EntityDetail sourceAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_WITHIN_SOURCE_GUID, "WithinPath:source", null);
        EntityDetail targetAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_WITHIN_TARGET_GUID, "WithinPath:target", null);
        EntityDetail process     = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_WITHIN_GUID, "WithinPath:process", null);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_WITHIN_SOURCE_GUID, "WithinPath:source:product", DarwinFvtTestSupport.MEMBERSHIP_WITHIN_SOURCE_GUID, sourceAsset);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_WITHIN_TARGET_GUID, "WithinPath:target:product", DarwinFvtTestSupport.MEMBERSHIP_WITHIN_TARGET_GUID, targetAsset);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_WITHIN_UNTAGGED_GUID, sourceAsset, process, null);
        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_WITHIN_TAGGED_GUID, process, targetAsset, DarwinFvtTestSupport.ISC_WITHIN_PATH);
    }


    /**
     * Set 16 - two products' assets joined by an untagged data flow, with a dependency between the products asserted
     * for a supply chain.
     */
    private void addSupplyChainFromAboveSet()
    {
        EntityDetail sourceAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ABOVE_SOURCE_GUID, "FromAbove:source", null);
        EntityDetail targetAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ABOVE_TARGET_GUID, "FromAbove:target", null);

        EntityDetail sourceProduct = this.addProduct(DarwinFvtTestSupport.PRODUCT_ABOVE_SOURCE_GUID, "FromAbove:source:product", DarwinFvtTestSupport.MEMBERSHIP_ABOVE_SOURCE_GUID, sourceAsset);
        EntityDetail targetProduct = this.addProduct(DarwinFvtTestSupport.PRODUCT_ABOVE_TARGET_GUID, "FromAbove:target:product", DarwinFvtTestSupport.MEMBERSHIP_ABOVE_TARGET_GUID, targetAsset);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_ABOVE_UNTAGGED_GUID, sourceAsset, targetAsset, null);

        /*
         * The target product depends on the source product - the dependent product is at end 1.
         */
        this.addLineage(OpenMetadataType.DIGITAL_PRODUCT_DEPENDENCY_RELATIONSHIP.typeName,
                        DarwinFvtTestSupport.DEPENDENCY_ABOVE_GUID,
                        targetProduct,
                        sourceProduct,
                        DarwinFvtTestSupport.ISC_FROM_ABOVE);
    }


    /**
     * Set 17 - an untagged data flow from a product's asset to a process that feeds two other products' assets along
     * two supply chains.
     */
    private void addAmbiguousSupplyChainSet()
    {
        EntityDetail sourceAsset  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_AMBIGUOUS_SOURCE_GUID, "Ambiguous:source", null);
        EntityDetail targetAsset1 = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_AMBIGUOUS_TARGET_1_GUID, "Ambiguous:target1", null);
        EntityDetail targetAsset2 = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_AMBIGUOUS_TARGET_2_GUID, "Ambiguous:target2", null);
        EntityDetail process      = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_AMBIGUOUS_GUID, "Ambiguous:process", null);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_AMBIGUOUS_SOURCE_GUID, "Ambiguous:source:product", DarwinFvtTestSupport.MEMBERSHIP_AMBIGUOUS_SOURCE_GUID, sourceAsset);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_AMBIGUOUS_TARGET_1_GUID, "Ambiguous:target1:product", DarwinFvtTestSupport.MEMBERSHIP_AMBIGUOUS_TARGET_1_GUID, targetAsset1);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_AMBIGUOUS_TARGET_2_GUID, "Ambiguous:target2:product", DarwinFvtTestSupport.MEMBERSHIP_AMBIGUOUS_TARGET_2_GUID, targetAsset2);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_AMBIGUOUS_UNTAGGED_GUID, sourceAsset, process, null);
        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_AMBIGUOUS_1_GUID, process, targetAsset1, DarwinFvtTestSupport.ISC_AMBIGUOUS_1);
        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_AMBIGUOUS_2_GUID, process, targetAsset2, DarwinFvtTestSupport.ISC_AMBIGUOUS_2);
    }


    /**
     * Set 18 - products whose members are data sets over physical assets, with lineage between the physical assets.
     * The data set is at end 1 of DataSetContent and its content at end 2.
     */
    private void addDataSetViewSet()
    {
        EntityDetail sourceDataSet = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.DATA_SET_VIEW_SOURCE_GUID, "View:source:dataset", null);
        EntityDetail sourceAsset   = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_VIEW_SOURCE_GUID, "View:source:physical", null);
        EntityDetail targetDataSet = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.DATA_SET_VIEW_TARGET_GUID, "View:target:dataset", null);
        EntityDetail targetAsset   = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_VIEW_TARGET_GUID, "View:target:physical", null);
        EntityDetail otherDataSet  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.DATA_SET_VIEW_OTHER_GUID, "View:other:dataset", null);
        EntityDetail process       = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_VIEW_GUID, "View:process", null);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_VIEW_SOURCE_GUID, "View:source:product", DarwinFvtTestSupport.MEMBERSHIP_VIEW_SOURCE_GUID, sourceDataSet);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_VIEW_TARGET_GUID, "View:target:product", DarwinFvtTestSupport.MEMBERSHIP_VIEW_TARGET_GUID, targetDataSet);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_VIEW_OTHER_GUID, "View:other:product", DarwinFvtTestSupport.MEMBERSHIP_VIEW_OTHER_GUID, otherDataSet);

        this.addLineage(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, DarwinFvtTestSupport.CONTENT_VIEW_SOURCE_GUID, sourceDataSet, sourceAsset, null);
        this.addLineage(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, DarwinFvtTestSupport.CONTENT_VIEW_TARGET_GUID, targetDataSet, targetAsset, DarwinFvtTestSupport.ISC_VIEW);
        this.addLineage(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, DarwinFvtTestSupport.CONTENT_VIEW_OTHER_GUID, otherDataSet, targetAsset, DarwinFvtTestSupport.ISC_VIEW_OTHER);

        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_VIEW_IN_GUID, sourceAsset, process, DarwinFvtTestSupport.ISC_VIEW);
        this.addLineage(OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName, DarwinFvtTestSupport.FLOW_VIEW_OUT_GUID, process, targetAsset, DarwinFvtTestSupport.ISC_VIEW);
    }


    /**
     * Set 19 - products whose members are data sets over relational tables, with the tables joined through a process
     * by DataMapping relationships - the shape the Open Lineage Cataloguer produces.
     */
    private void addTableViewSet()
    {
        EntityDetail sourceDatabase = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_TABLE_VIEW_SOURCE_GUID, "TableView:source:database", null);
        EntityDetail targetDatabase = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_TABLE_VIEW_TARGET_GUID, "TableView:target:database", null);
        EntityDetail sourceTable    = this.addSchemaAttribute(OpenMetadataType.RELATIONAL_TABLE.typeName, DarwinFvtTestSupport.TABLE_VIEW_SOURCE_GUID, "TableView:source:table", sourceDatabase);
        EntityDetail targetTable    = this.addSchemaAttribute(OpenMetadataType.RELATIONAL_TABLE.typeName, DarwinFvtTestSupport.TABLE_VIEW_TARGET_GUID, "TableView:target:table", targetDatabase);
        EntityDetail sourceDataSet  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.DATA_SET_TABLE_VIEW_SOURCE_GUID, "TableView:source:dataset", null);
        EntityDetail targetDataSet  = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.DATA_SET_TABLE_VIEW_TARGET_GUID, "TableView:target:dataset", null);
        EntityDetail process        = this.addEntity(PROCESS_TYPE_NAME, DarwinFvtTestSupport.PROCESS_TABLE_VIEW_GUID, "TableView:process", null);

        this.addProduct(DarwinFvtTestSupport.PRODUCT_TABLE_VIEW_SOURCE_GUID, "TableView:source:product", DarwinFvtTestSupport.MEMBERSHIP_TABLE_VIEW_SOURCE_GUID, sourceDataSet);
        this.addProduct(DarwinFvtTestSupport.PRODUCT_TABLE_VIEW_TARGET_GUID, "TableView:target:product", DarwinFvtTestSupport.MEMBERSHIP_TABLE_VIEW_TARGET_GUID, targetDataSet);

        this.addLineage(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, DarwinFvtTestSupport.CONTENT_TABLE_VIEW_SOURCE_GUID, sourceDataSet, sourceTable, null);
        this.addLineage(OpenMetadataType.DATA_SET_CONTENT_RELATIONSHIP.typeName, DarwinFvtTestSupport.CONTENT_TABLE_VIEW_TARGET_GUID, targetDataSet, targetTable, null);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, DarwinFvtTestSupport.MAPPING_TABLE_VIEW_IN_GUID, sourceTable, process, DarwinFvtTestSupport.ISC_TABLE_VIEW);
        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, DarwinFvtTestSupport.MAPPING_TABLE_VIEW_OUT_GUID, process, targetTable, DarwinFvtTestSupport.ISC_TABLE_VIEW);
    }


    /**
     * Set 20 - two assets whose columns are joined by a mapping, each with another column mapped to a third asset so
     * that both stay catalogued when the first mapping is removed.
     */
    private void addAdoptedDataFlowSet()
    {
        EntityDetail sourceAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ADOPTED_SOURCE_GUID, "Adopted:source", null);
        EntityDetail targetAsset = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ADOPTED_TARGET_GUID, "Adopted:target", null);
        EntityDetail otherAsset1 = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ADOPTED_OTHER_1_GUID, "Adopted:other1", null);
        EntityDetail otherAsset2 = this.addEntity(ASSET_TYPE_NAME, DarwinFvtTestSupport.ASSET_ADOPTED_OTHER_2_GUID, "Adopted:other2", null);

        EntityDetail sourceColumn  = this.addColumn(DarwinFvtTestSupport.COLUMN_ADOPTED_SOURCE_GUID, "Adopted:source:column", sourceAsset);
        EntityDetail sourceColumn2 = this.addColumn(DarwinFvtTestSupport.COLUMN_ADOPTED_SOURCE_2_GUID, "Adopted:source:column2", sourceAsset);
        EntityDetail targetColumn  = this.addColumn(DarwinFvtTestSupport.COLUMN_ADOPTED_TARGET_GUID, "Adopted:target:column", targetAsset);
        EntityDetail targetColumn2 = this.addColumn(DarwinFvtTestSupport.COLUMN_ADOPTED_TARGET_2_GUID, "Adopted:target:column2", targetAsset);
        EntityDetail otherColumn1  = this.addColumn(DarwinFvtTestSupport.COLUMN_ADOPTED_OTHER_1_GUID, "Adopted:other1:column", otherAsset1);
        EntityDetail otherColumn2  = this.addColumn(DarwinFvtTestSupport.COLUMN_ADOPTED_OTHER_2_GUID, "Adopted:other2:column", otherAsset2);

        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, DarwinFvtTestSupport.MAPPING_ADOPTED_GUID, sourceColumn, targetColumn, DarwinFvtTestSupport.ISC_ADOPTED);
        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, DarwinFvtTestSupport.MAPPING_ADOPTED_SOURCE_OUT_GUID, sourceColumn2, otherColumn1, DarwinFvtTestSupport.ISC_ADOPTED);
        this.addLineage(OpenMetadataType.DATA_MAPPING_RELATIONSHIP.typeName, DarwinFvtTestSupport.MAPPING_ADOPTED_TARGET_IN_GUID, otherColumn2, targetColumn2, DarwinFvtTestSupport.ISC_ADOPTED);
    }


    /**
     * Add a schema attribute anchored to an asset.
     *
     * @param guid unique identifier to give it
     * @param name the part of the qualified name after the suite's prefix
     * @param asset the asset it belongs to
     * @return the new entity
     */
    private EntityDetail addColumn(String       guid,
                                   String       name,
                                   EntityDetail asset)
    {
        return this.addSchemaAttribute(COLUMN_TYPE_NAME, guid, name, asset);
    }


    /**
     * Add a schema attribute of a particular type anchored to an asset.
     *
     * @param typeName type of schema attribute
     * @param guid unique identifier to give it
     * @param name the part of the qualified name after the suite's prefix
     * @param asset the asset it belongs to
     * @return the new entity
     */
    private EntityDetail addSchemaAttribute(String       typeName,
                                            String       guid,
                                            String       name,
                                            EntityDetail asset)
    {
        InstanceProperties anchorProperties = this.addStringProperty(null, OpenMetadataProperty.ANCHOR_GUID.name, asset.getGUID());

        anchorProperties = this.addStringProperty(anchorProperties, OpenMetadataProperty.ANCHOR_TYPE_NAME.name, ASSET_TYPE_NAME);
        anchorProperties = this.addStringProperty(anchorProperties, OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name, OpenMetadataType.ASSET.typeName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(archiveHelper.getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                             anchorProperties,
                                                             InstanceStatus.ACTIVE));

        return this.addEntity(typeName, guid, name, classifications);
    }


    /**
     * Add a software server with one capability that owns an asset.
     *
     * @param serverGUID unique identifier for the server
     * @param serverName the part of the server's qualified name after the suite's prefix
     * @param capabilityGUID unique identifier for the capability
     * @param supportedCapabilityGUID unique identifier for the SupportedSoftwareCapability relationship
     * @param assetUseGUID unique identifier for the CapabilityAssetUse relationship
     * @param asset the asset the capability owns
     */
    private void addOwningServer(String       serverGUID,
                                 String       serverName,
                                 String       capabilityGUID,
                                 String       supportedCapabilityGUID,
                                 String       assetUseGUID,
                                 EntityDetail asset)
    {
        this.addUsingServer(serverGUID, serverName, capabilityGUID, supportedCapabilityGUID, assetUseGUID, asset, CapabilityAssetUseType.OWNS);
    }


    /**
     * Add a software server with one capability that uses an asset in a particular way.
     *
     * @param serverGUID unique identifier for the server
     * @param serverName the part of the server's qualified name after the suite's prefix
     * @param capabilityGUID unique identifier for the capability
     * @param supportedCapabilityGUID unique identifier for the SupportedSoftwareCapability relationship
     * @param assetUseGUID unique identifier for the CapabilityAssetUse relationship
     * @param asset the asset - a data asset or a process - the capability uses
     * @param useType how the capability uses it
     * @return the new server
     */
    private EntityDetail addUsingServer(String                 serverGUID,
                                        String                 serverName,
                                        String                 capabilityGUID,
                                        String                 supportedCapabilityGUID,
                                        String                 assetUseGUID,
                                        EntityDetail           asset,
                                        CapabilityAssetUseType useType)
    {
        EntityDetail server     = this.addEntity(SERVER_TYPE_NAME, serverGUID, serverName, null);
        EntityDetail capability = this.addEntity(CAPABILITY_TYPE_NAME, capabilityGUID, serverName + ":capability", null);

        /*
         * The server is at end 1 of SupportedSoftwareCapability and the capability at end 2; the capability
         * is at end 1 of CapabilityAssetUse and the asset at end 2.
         */
        this.addRelationship(OpenMetadataType.SUPPORTED_SOFTWARE_CAPABILITY_RELATIONSHIP.typeName, supportedCapabilityGUID, server, capability, null);

        this.addRelationship(OpenMetadataType.CAPABILITY_ASSET_USE_RELATIONSHIP.typeName,
                             assetUseGUID,
                             capability,
                             asset,
                             this.getUseTypeProperties(useType));

        return server;
    }


    /**
     * Add a digital product with one member.
     *
     * @param productGUID unique identifier for the product
     * @param productName the part of the product's qualified name after the suite's prefix
     * @param membershipGUID unique identifier for the CollectionMembership relationship
     * @param member the asset that is the product's member
     * @return the new product
     */
    private EntityDetail addProduct(String       productGUID,
                                    String       productName,
                                    String       membershipGUID,
                                    EntityDetail member)
    {
        EntityDetail product = this.addEntity(PRODUCT_TYPE_NAME, productGUID, productName, null);

        /*
         * The product is at end 1 of CollectionMembership and the member at end 2.
         */
        this.addRelationship(OpenMetadataType.COLLECTION_MEMBERSHIP_RELATIONSHIP.typeName, membershipGUID, product, member, null);

        return product;
    }


    /**
     * Add a lineage relationship - one that carries an information supply chain.
     *
     * @param typeName type of relationship
     * @param guid unique identifier to give it
     * @param end1 element at end 1
     * @param end2 element at end 2
     * @param iscQualifiedName information supply chain - null to leave it unset
     */
    private void addLineage(String       typeName,
                            String       guid,
                            EntityDetail end1,
                            EntityDetail end2,
                            String       iscQualifiedName)
    {
        InstanceProperties properties = null;

        if (iscQualifiedName != null)
        {
            properties = this.addStringProperty(null, OpenMetadataProperty.ISC_QUALIFIED_NAME.name, iscQualifiedName);
        }

        this.addRelationship(typeName, guid, end1, end2, properties);
    }


    /**
     * Add one entity to the archive.
     *
     * @param typeName type of entity
     * @param guid unique identifier to give it - fixed, so that the tests can refer to it directly
     * @param name the part of the qualified name after the suite's prefix - also the display name
     * @param classifications classifications to attach, or null
     * @return the new entity
     */
    private EntityDetail addEntity(String               typeName,
                                   String               guid,
                                   String               name,
                                   List<Classification> classifications)
    {
        InstanceProperties properties = this.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, DarwinFvtTestSupport.QUALIFIED_NAME_PREFIX + name);

        properties = this.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, name);

        EntityDetail entity = archiveHelper.getEntityDetail(typeName, guid, properties, InstanceStatus.ACTIVE, classifications);

        archiveBuilder.addEntity(entity);

        return entity;
    }


    /**
     * Add one relationship to the archive.
     *
     * @param typeName type of relationship
     * @param guid unique identifier to give it
     * @param end1 element at end 1
     * @param end2 element at end 2
     * @param properties properties, or null
     */
    private void addRelationship(String             typeName,
                                 String             guid,
                                 EntityDetail       end1,
                                 EntityDetail       end2,
                                 InstanceProperties properties)
    {
        archiveBuilder.addRelationship(archiveHelper.getRelationship(typeName,
                                                                      guid,
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      archiveHelper.getEntityProxy(end1),
                                                                      archiveHelper.getEntityProxy(end2)));
    }


    /**
     * Build the properties for a CapabilityAssetUse relationship.
     *
     * @param useType how the capability uses the asset
     * @return properties
     */
    private InstanceProperties getUseTypeProperties(CapabilityAssetUseType useType)
    {
        EnumElementDef enumElement = archiveHelper.getEnumElement(CapabilityAssetUseType.getOpenTypeName(), useType.getOrdinal());

        EnumPropertyValue enumValue = new EnumPropertyValue();

        enumValue.setOrdinal(enumElement.getOrdinal());
        enumValue.setSymbolicName(enumElement.getValue());
        enumValue.setDescription(enumElement.getDescription());
        enumValue.setTypeName(CapabilityAssetUseType.getOpenTypeName());
        enumValue.setTypeGUID(CapabilityAssetUseType.getOpenTypeGUID());

        InstanceProperties properties = new InstanceProperties();

        properties.setProperty(OpenMetadataProperty.USE_TYPE.name, enumValue);

        return properties;
    }


    /**
     * Add a string property to a set of instance properties, creating the set if it does not exist yet.
     *
     * @param properties properties to add to - null to start a new set
     * @param propertyName name of the property
     * @param propertyValue value of the property
     * @return the properties, with the new value in them
     */
    private InstanceProperties addStringProperty(InstanceProperties properties,
                                                 String             propertyName,
                                                 String             propertyValue)
    {
        InstanceProperties resultingProperties = properties;

        if (resultingProperties == null)
        {
            resultingProperties = new InstanceProperties();
        }

        PrimitivePropertyValue primitivePropertyValue = new PrimitivePropertyValue();

        primitivePropertyValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        primitivePropertyValue.setPrimitiveValue(propertyValue);
        primitivePropertyValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getName());
        primitivePropertyValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING.getGUID());

        resultingProperties.setProperty(propertyName, primitivePropertyValue);

        return resultingProperties;
    }
}

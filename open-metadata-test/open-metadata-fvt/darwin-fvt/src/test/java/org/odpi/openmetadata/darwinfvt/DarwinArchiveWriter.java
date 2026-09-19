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
        InstanceProperties anchorProperties = this.addStringProperty(null, OpenMetadataProperty.ANCHOR_GUID.name, asset.getGUID());

        anchorProperties = this.addStringProperty(anchorProperties, OpenMetadataProperty.ANCHOR_TYPE_NAME.name, ASSET_TYPE_NAME);
        anchorProperties = this.addStringProperty(anchorProperties, OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name, OpenMetadataType.ASSET.typeName);

        List<Classification> classifications = new ArrayList<>();

        classifications.add(archiveHelper.getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName,
                                                             anchorProperties,
                                                             InstanceStatus.ACTIVE));

        return this.addEntity(COLUMN_TYPE_NAME, guid, name, classifications);
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
                             this.getUseTypeProperties(CapabilityAssetUseType.OWNS));
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

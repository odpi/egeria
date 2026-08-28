/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.duplicatefvt;

import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.StatusIdentifier;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationOrigin;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * DuplicateArchiveWriter builds the open metadata archive that seeds this suite's duplicates.
 * <p>
 * Duplicates cannot be created through the access services - they exist precisely because something got past
 * them - so they are introduced the way they arrive in the wild: through the repository layer, by loading an
 * archive whose entities deliberately share a qualified name.  This is the same shape of problem as the one
 * that arises when two releases of a content pack give the same element different unique identifiers (see
 * the content-pack-duplicate-report utility measures that population), but small, deterministic and self-contained, so the assertions in this
 * suite can be exact.
 * <p>
 * The archive contains five sets, each exercising a different part of duplicate management.  Every element it
 * creates has a qualified name starting with {@link DuplicateFvtTestSupport#QUALIFIED_NAME_PREFIX}, so the
 * suite's debris can always be found and purged.
 * <ul>
 *     <li><b>Set 1 - undetected</b>: two elements sharing a qualified name and nothing else.  Looking one up
 *     by that name is ambiguous, which is what makes the generic handler record the duplicate.</li>
 *     <li><b>Set 7 - untouched</b>: the same, but never looked up by any test, so nothing starts managing
 *     them.  This is what shows that duplicates are combined only once something confirms them.</li>
 *     <li><b>Set 2 - close match</b>: the same, plus a DISCOVERED PeerDuplicateLink between them.  Mendel
 *     validates this one on its own authority because the two elements are the same type with the same
 *     qualified name.</li>
 *     <li><b>Set 3 - not a close match</b>: two elements with <em>different</em> qualified names and a
 *     DISCOVERED link between them.  Mendel raises a to do rather than deciding.</li>
 *     <li><b>Set 4 - validated cluster</b>: three elements sharing a qualified name, already linked with
 *     VALIDATED links and classified as KnownDuplicate.  The repository handler combines these on retrieval,
 *     and Mendel consolidates them once the cluster reaches its configured size.</li>
 *     <li><b>Set 5 - retired</b>: two elements classified as KnownDuplicate whose only link has been
 *     DEPRECATED by a steward.  Mendel removes the classifications.</li>
 * </ul>
 */
class DuplicateArchiveWriter extends OMRSArchiveWriter
{
    private static final String ARCHIVE_GUID        = "e4d5a5da-9d3b-4a5b-9e08-6de5e42f5f5e";
    private static final String ARCHIVE_NAME        = "DuplicateFvtArchive";
    private static final String ARCHIVE_DESCRIPTION = "Elements that deliberately share a qualified name, used by the duplicate-fvt test suite.";
    private static final String ORIGINATOR_NAME     = "Egeria duplicate-fvt";
    private static final String ARCHIVE_VERSION     = "1.0";

    /**
     * The type used for every element in the fixture.  Collection is a convenient choice: it is a
     * Referenceable (so it has a qualified name, which is what makes an element a candidate for Mendel's
     * close match rule), it is simple to create, and nothing else in the content packs uses these names.
     */
    private static final String ELEMENT_TYPE_NAME = OpenMetadataType.COLLECTION.typeName;

    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;


    /**
     * Constructor - sets up the archive to build.  The open metadata types archive is a dependency because
     * the entities and relationships below are all of types it defines.
     */
    DuplicateArchiveWriter()
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

        /*
         * The instances keep the archive's own provenance - they are owned by the content pack that supplied
         * them, not by the repository that loaded them.  That is deliberate: it is how real duplicates
         * arrive, including the ones created when successive releases of a content pack give the same
         * element different unique identifiers, and an instance owned elsewhere can only be changed on
         * behalf of its owner.  Mendel names the owning metadata collection on each update for exactly this
         * reason, so a fixture that was locally owned would leave that untested.
         */
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
        this.addUndetectedPair();
        this.addUntouchedPair();
        this.addCloseMatchPair();
        this.addDistantMatchPair();
        this.addValidatedCluster();
        this.addSmallValidatedCluster();
        this.addRetiredPair();

        File parentDirectory = archiveFile.getParentFile();

        if ((parentDirectory != null) && (! parentDirectory.exists()) && (! parentDirectory.mkdirs()))
        {
            throw new IllegalStateException("Could not create the directory for " + archiveFile.getAbsolutePath());
        }

        super.writeOpenMetadataArchive(archiveFile.getAbsolutePath(), archiveBuilder.getOpenMetadataArchive());
    }


    /**
     * Set 1 - two elements sharing a qualified name, with nothing linking them.  Nothing knows they are
     * duplicates until something looks one of them up by name.
     */
    private void addUndetectedPair()
    {
        this.addElement(DuplicateFvtTestSupport.UNDETECTED_GUID_ONE, DuplicateFvtTestSupport.UNDETECTED_QUALIFIED_NAME, "Undetected duplicate 1", false);
        this.addElement(DuplicateFvtTestSupport.UNDETECTED_GUID_TWO, DuplicateFvtTestSupport.UNDETECTED_QUALIFIED_NAME, "Undetected duplicate 2", false);
    }


    /**
     * Set 7 - two elements sharing a qualified name that nothing ever looks up, so nothing ever starts
     * managing them.  This is the pair used to check that duplicates are only combined once something has
     * confirmed they are the same thing.
     */
    private void addUntouchedPair()
    {
        this.addElement(DuplicateFvtTestSupport.UNTOUCHED_GUID_ONE, DuplicateFvtTestSupport.UNTOUCHED_QUALIFIED_NAME, "Untouched duplicate 1", false);
        this.addElement(DuplicateFvtTestSupport.UNTOUCHED_GUID_TWO, DuplicateFvtTestSupport.UNTOUCHED_QUALIFIED_NAME, "Untouched duplicate 2", false);
    }


    /**
     * Set 2 - two elements sharing a qualified name, already linked as discovered duplicates.  This is the
     * state the generic handler leaves a newly detected duplicate in, and the state Mendel picks up.
     */
    private void addCloseMatchPair()
    {
        EntityDetail elementOne = this.addElement(DuplicateFvtTestSupport.CLOSE_MATCH_GUID_ONE, DuplicateFvtTestSupport.CLOSE_MATCH_QUALIFIED_NAME, "Close match 1", false);
        EntityDetail elementTwo = this.addElement(DuplicateFvtTestSupport.CLOSE_MATCH_GUID_TWO, DuplicateFvtTestSupport.CLOSE_MATCH_QUALIFIED_NAME, "Close match 2", false);

        this.addDuplicateLink(DuplicateFvtTestSupport.CLOSE_MATCH_LINK_GUID, elementOne, elementTwo, StatusIdentifier.DISCOVERED.getOrdinal());
    }


    /**
     * Set 3 - two elements that something has linked as possible duplicates, but which do not share a
     * qualified name.  Mendel is not entitled to combine these on its own.
     */
    private void addDistantMatchPair()
    {
        EntityDetail elementOne = this.addElement(DuplicateFvtTestSupport.DISTANT_MATCH_GUID_ONE, DuplicateFvtTestSupport.DISTANT_MATCH_QUALIFIED_NAME_ONE, "Distant match 1", false);
        EntityDetail elementTwo = this.addElement(DuplicateFvtTestSupport.DISTANT_MATCH_GUID_TWO, DuplicateFvtTestSupport.DISTANT_MATCH_QUALIFIED_NAME_TWO, "Distant match 2", false);

        this.addDuplicateLink(DuplicateFvtTestSupport.DISTANT_MATCH_LINK_GUID, elementOne, elementTwo, StatusIdentifier.DISCOVERED.getOrdinal());
    }


    /**
     * Set 4 - three elements that a steward has already confirmed are the same thing.  This is the state the
     * repository handler combines on retrieval, and the cluster Mendel consolidates.
     */
    private void addValidatedCluster()
    {
        EntityDetail elementOne   = this.addElement(DuplicateFvtTestSupport.CLUSTER_GUID_ONE, DuplicateFvtTestSupport.CLUSTER_QUALIFIED_NAME, "Cluster member 1", true);
        EntityDetail elementTwo   = this.addElement(DuplicateFvtTestSupport.CLUSTER_GUID_TWO, DuplicateFvtTestSupport.CLUSTER_QUALIFIED_NAME, "Cluster member 2", true);
        EntityDetail elementThree = this.addElement(DuplicateFvtTestSupport.CLUSTER_GUID_THREE, DuplicateFvtTestSupport.CLUSTER_QUALIFIED_NAME, "Cluster member 3", true);

        /*
         * A chain rather than a mesh - the cluster is the set of elements reachable through the validated
         * links, so two links are enough to put all three in one cluster.
         */
        this.addDuplicateLink(DuplicateFvtTestSupport.CLUSTER_LINK_GUID_ONE, elementOne, elementTwo, StatusIdentifier.VALIDATED.getOrdinal());
        this.addDuplicateLink(DuplicateFvtTestSupport.CLUSTER_LINK_GUID_TWO, elementTwo, elementThree, StatusIdentifier.VALIDATED.getOrdinal());
    }


    /**
     * Set 6 - two elements that a steward has confirmed are the same thing, but too few of them to reach the
     * consolidation cluster size.  The repository handler combines them on retrieval; Mendel leaves them be.
     */
    private void addSmallValidatedCluster()
    {
        EntityDetail elementOne = this.addElement(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_ONE, DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME, "Small cluster member 1", true);
        EntityDetail elementTwo = this.addElement(DuplicateFvtTestSupport.SMALL_CLUSTER_GUID_TWO, DuplicateFvtTestSupport.SMALL_CLUSTER_QUALIFIED_NAME, "Small cluster member 2", true);

        this.addDuplicateLink(DuplicateFvtTestSupport.SMALL_CLUSTER_LINK_GUID, elementOne, elementTwo, StatusIdentifier.VALIDATED.getOrdinal());
    }


    /**
     * Set 5 - two elements that were treated as duplicates until a steward decided otherwise and retired the
     * link.  The classifications are still there, and it is Mendel's job to take them off.
     */
    private void addRetiredPair()
    {
        EntityDetail elementOne = this.addElement(DuplicateFvtTestSupport.RETIRED_GUID_ONE, DuplicateFvtTestSupport.RETIRED_QUALIFIED_NAME_ONE, "Retired duplicate 1", true);
        EntityDetail elementTwo = this.addElement(DuplicateFvtTestSupport.RETIRED_GUID_TWO, DuplicateFvtTestSupport.RETIRED_QUALIFIED_NAME_TWO, "Retired duplicate 2", true);

        this.addDuplicateLink(DuplicateFvtTestSupport.RETIRED_LINK_GUID, elementOne, elementTwo, StatusIdentifier.DEPRECATED.getOrdinal());
    }


    /**
     * Add one element to the archive.
     *
     * @param guid unique identifier to give it - fixed, so that the tests can refer to it directly
     * @param qualifiedName qualified name - deliberately shared with another element in most of the sets
     * @param displayName something to tell the elements apart by in a failure message
     * @param knownDuplicate should the KnownDuplicate classification be attached?
     * @return the new entity, for use as the end of a duplicate link
     */
    private EntityDetail addElement(String  guid,
                                    String  qualifiedName,
                                    String  displayName,
                                    boolean knownDuplicate)
    {
        InstanceProperties properties = this.addStringProperty(null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName);

        properties = this.addStringProperty(properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName);

        List<Classification> classifications = null;

        if (knownDuplicate)
        {
            classifications = new ArrayList<>();

            classifications.add(archiveHelper.getClassification(OpenMetadataType.KNOWN_DUPLICATE_CLASSIFICATION.typeName,
                                                                 null,
                                                                 InstanceStatus.ACTIVE));
        }

        EntityDetail entity = archiveHelper.getEntityDetail(ELEMENT_TYPE_NAME,
                                                             guid,
                                                             properties,
                                                             InstanceStatus.ACTIVE,
                                                             classifications);

        archiveBuilder.addEntity(entity);

        return entity;
    }


    /**
     * Add a PeerDuplicateLink between two of the elements.
     *
     * @param guid unique identifier to give the relationship - fixed, so that the tests can refer to it
     * @param endOne element at end one (the older of the two, by the type's definition)
     * @param endTwo element at end two
     * @param statusIdentifier the status to record on the link - this is what decides who acts on it
     */
    private void addDuplicateLink(String       guid,
                                  EntityDetail endOne,
                                  EntityDetail endTwo,
                                  int          statusIdentifier)
    {
        InstanceProperties properties = new InstanceProperties();

        PrimitivePropertyValue statusValue = new PrimitivePropertyValue();

        statusValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT);
        statusValue.setPrimitiveValue(statusIdentifier);
        statusValue.setTypeName(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getName());
        statusValue.setTypeGUID(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_INT.getGUID());

        properties.setProperty(OpenMetadataProperty.STATUS_IDENTIFIER.name, statusValue);

        properties = this.addStringProperty(properties, OpenMetadataProperty.SOURCE.name, ORIGINATOR_NAME);

        EntityProxy endOneProxy = archiveHelper.getEntityProxy(endOne);
        EntityProxy endTwoProxy = archiveHelper.getEntityProxy(endTwo);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(OpenMetadataType.PEER_DUPLICATE_LINK.typeName,
                                                                      guid,
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      endOneProxy,
                                                                      endTwoProxy));
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

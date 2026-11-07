/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.securityfvt;

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
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * StaleAnchorsZoneArchiveWriter builds an open metadata archive holding an anchor and a member whose Anchors
 * classifications carry out-of-date governance zones, and two elements with no zones.
 * <br><br>
 * The access services keep these zones up to date, so stale values can not be made through them.  They arrive
 * the way they arrive in a real repository: from an archive, which is loaded straight into the repository.
 * The anchor is in {@link OMAGPlatformExtension#OPEN_ZONE}, and the member has no zones of its own, so both are
 * governed by the open zone - but both Anchors classifications say {@link OMAGPlatformExtension#RESTRICTED_ZONE}.
 * This is what an anchor published before the server maintained these zones looks like.
 * <br><br>
 * Two more elements have no zones at all.  One is loaded with no Anchors classification (the server gives it one,
 * without zones, when it first works with the element), so the search filter has no zones to test and must
 * return it.  The other is its own anchor with an Anchors classification whose zone list is
 * empty rather than unset: it means the same, but the filter hides it until it is resynchronized.
 */
class StaleAnchorsZoneArchiveWriter extends OMRSArchiveWriter
{
    static final String ANCHOR_GUID           = "5f1c2b9e-7d0a-4c43-9a5e-2f8d6b1e4a01";
    static final String MEMBER_GUID           = "5f1c2b9e-7d0a-4c43-9a5e-2f8d6b1e4a02";
    static final String ANCHOR_QUALIFIED_NAME = "SecurityFVT:stale-anchors-zone:anchor";
    static final String MEMBER_QUALIFIED_NAME = "SecurityFVT:stale-anchors-zone:member";
    static final String NO_ANCHORS_GUID           = "5f1c2b9e-7d0a-4c43-9a5e-2f8d6b1e4a03";
    static final String NO_ANCHORS_QUALIFIED_NAME = "SecurityFVT:stale-anchors-zone:no-anchors";
    static final String EMPTY_ZONES_GUID           = "5f1c2b9e-7d0a-4c43-9a5e-2f8d6b1e4a04";
    static final String EMPTY_ZONES_QUALIFIED_NAME = "SecurityFVT:stale-anchors-zone:empty-zone-list";

    private static final String ARCHIVE_GUID        = "5f1c2b9e-7d0a-4c43-9a5e-2f8d6b1e4a00";
    private static final String ARCHIVE_NAME        = "SecurityFvtStaleAnchorsZoneArchive";
    private static final String ARCHIVE_DESCRIPTION = "An anchor and member whose Anchors classifications hold out-of-date zones, used by the security-fvt suite.";
    private static final String ORIGINATOR_NAME     = "Egeria security-fvt";
    private static final String ARCHIVE_VERSION     = "1.0";
    private static final String SOURCE_NAME         = "StaleAnchorsZoneArchiveWriter";

    private static final String ELEMENT_TYPE_NAME = OpenMetadataType.COLLECTION.typeName;

    private final OMRSArchiveBuilder archiveBuilder;
    private final OMRSArchiveHelper  archiveHelper;


    /**
     * Constructor - sets up the archive to build, which depends on the open metadata types.
     */
    StaleAnchorsZoneArchiveWriter()
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
     * @throws Exception the archive could not be written
     */
    void writeArchive(File archiveFile) throws Exception
    {
        List<String> staleZones = List.of(OMAGPlatformExtension.RESTRICTED_ZONE);

        this.addElement(ANCHOR_GUID, ANCHOR_QUALIFIED_NAME, "Stale Anchors zone anchor", ANCHOR_GUID, staleZones, List.of(OMAGPlatformExtension.OPEN_ZONE));
        this.addElement(MEMBER_GUID, MEMBER_QUALIFIED_NAME, "Stale Anchors zone member", ANCHOR_GUID, staleZones, null);
        this.addElement(NO_ANCHORS_GUID, NO_ANCHORS_QUALIFIED_NAME, "Element with no Anchors classification", null, null, null);
        this.addElement(EMPTY_ZONES_GUID, EMPTY_ZONES_QUALIFIED_NAME, "Element whose Anchors zone list is empty", EMPTY_ZONES_GUID, List.of(), null);

        File parentDirectory = archiveFile.getParentFile();

        if ((parentDirectory != null) && (! parentDirectory.exists()) && (! parentDirectory.mkdirs()))
        {
            throw new IllegalStateException("Could not create the directory for " + archiveFile.getAbsolutePath());
        }

        super.writeOpenMetadataArchive(archiveFile.getAbsolutePath(), archiveBuilder.getOpenMetadataArchive());
    }


    /**
     * Add one element.
     *
     * @param guid unique identifier
     * @param qualifiedName unique name
     * @param displayName display name
     * @param anchorGUID anchor for the Anchors classification, or null for no Anchors classification
     * @param anchorsZones zones to record in the Anchors classification
     * @param ownZones zones for a ZoneMembership classification, or null for none
     */
    private void addElement(String       guid,
                            String       qualifiedName,
                            String       displayName,
                            String       anchorGUID,
                            List<String> anchorsZones,
                            List<String> ownZones)
    {
        final String methodName = "addElement";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(SOURCE_NAME, null, OpenMetadataProperty.QUALIFIED_NAME.name, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(SOURCE_NAME, properties, OpenMetadataProperty.DISPLAY_NAME.name, displayName, methodName);

        List<Classification> classifications = new ArrayList<>();

        if (anchorGUID != null)
        {
            InstanceProperties anchorsProperties = archiveHelper.addStringPropertyToInstance(SOURCE_NAME, null, OpenMetadataProperty.ANCHOR_GUID.name, anchorGUID, methodName);
            anchorsProperties = archiveHelper.addStringPropertyToInstance(SOURCE_NAME, anchorsProperties, OpenMetadataProperty.ANCHOR_TYPE_NAME.name, ELEMENT_TYPE_NAME, methodName);
            anchorsProperties = archiveHelper.addStringPropertyToInstance(SOURCE_NAME, anchorsProperties, OpenMetadataProperty.ANCHOR_DOMAIN_NAME.name, ELEMENT_TYPE_NAME, methodName);
            anchorsProperties = archiveHelper.addStringArrayPropertyToInstance(SOURCE_NAME,
                                                                               anchorsProperties,
                                                                               OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                               anchorsZones,
                                                                               methodName);

            classifications.add(archiveHelper.getClassification(OpenMetadataType.ANCHORS_CLASSIFICATION.typeName, anchorsProperties, InstanceStatus.ACTIVE));
        }

        if (ownZones != null)
        {
            classifications.add(archiveHelper.getClassification(OpenMetadataType.ZONE_MEMBERSHIP_CLASSIFICATION.typeName,
                                                                 archiveHelper.addStringArrayPropertyToInstance(SOURCE_NAME,
                                                                                                                null,
                                                                                                                OpenMetadataProperty.ZONE_MEMBERSHIP.name,
                                                                                                                ownZones,
                                                                                                                methodName),
                                                                 InstanceStatus.ACTIVE));
        }

        EntityDetail entity = archiveHelper.getEntityDetail(ELEMENT_TYPE_NAME, guid, properties, InstanceStatus.ACTIVE, classifications);

        archiveBuilder.addEntity(entity);
    }
}

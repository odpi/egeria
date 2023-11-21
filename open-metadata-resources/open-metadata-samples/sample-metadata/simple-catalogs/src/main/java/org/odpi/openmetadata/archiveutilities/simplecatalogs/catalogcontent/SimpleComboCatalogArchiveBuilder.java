/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.simplecatalogs.catalogcontent;


import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * SimpleComboCatalogArchiveBuilder pulls together the metadata from the four simple catalogs into
 * a content pack that can be used in other scenarios.
 */
public class SimpleComboCatalogArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "83e785b1-5bcc-4714-8f89-f6639ec55ca0";
    private static final String                  archiveName        = "SimpleCatalog";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Sample metadata showing different types of asset.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1632046251579L);

    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";

    private final OMRSArchiveBuilder         archiveBuilder;
    private final SimpleCatalogArchiveHelper archiveHelper;

    /**
     * Constructor pushes all archive header values to the superclass
     *
     * @param archiveName name of the open metadata archive metadata collection.
     * @param archiveRootName non-spaced root name of the open metadata GUID map.
     */
    public SimpleComboCatalogArchiveBuilder(String                    archiveName,
                                            String                    archiveRootName)
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        /*
         * This value allows the archive to be based on the existing open metadata types
         */
        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     archiveLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new SimpleCatalogArchiveHelper(archiveBuilder,
                                                            archiveGUID,
                                                            archiveName,
                                                            archiveRootName,
                                                            originatorName,
                                                            creationDate,
                                                            versionNumber,
                                                            versionName,
                                                            InstanceProvenanceType.CONTENT_PACK,
                                                            null);
    }


    /**
     * Returns the open metadata type archive containing all the elements extracted from the connector
     * providers of the featured open connectors.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        SimpleAPICatalogArchiveBuilder simpleAPICatalogArchiveBuilder = new SimpleAPICatalogArchiveBuilder(archiveBuilder, archiveHelper);
        simpleAPICatalogArchiveBuilder.fillBuilder();

        SimpleDataCatalogArchiveBuilder simpleDataCatalogArchiveBuilder = new SimpleDataCatalogArchiveBuilder(archiveBuilder, archiveHelper);
        simpleDataCatalogArchiveBuilder.fillBuilder();

        SimpleEventCatalogArchiveBuilder simpleEventCatalogArchiveBuilder = new SimpleEventCatalogArchiveBuilder(archiveBuilder, archiveHelper);
        simpleEventCatalogArchiveBuilder.fillBuilder();

        SimpleGovernanceCatalogArchiveBuilder simpleGovernanceCatalogArchiveBuilder = new SimpleGovernanceCatalogArchiveBuilder(archiveBuilder, archiveHelper);
        simpleGovernanceCatalogArchiveBuilder.fillBuilder();

        archiveHelper.saveGUIDs();

        return archiveBuilder.getOpenMetadataArchive();
    }
}

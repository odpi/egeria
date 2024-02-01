/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.bigglossaries.catalogcontent;


import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * BigGlossaryArchiveBuilder provides API metadata.
 */
public class BigGlossaryArchiveBuilder
{
    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "9e594f24-2494-4000-ac20-59f374eaa0e6";
    private static final String                  archiveNamePrefix  = "BigGlossary";
    private static final String                  archiveLicense     = "Apache-2.0";
    private static final String                  archiveDescription = "Sample glossary with 10,000 unique terms.";
    private static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.REPOSITORY_BACKUP;
    private static final String                  originatorName     = "Egeria";
    private static final Date                    creationDate       = new Date(1681646242899L);

    /*
     * Names for glossary terms
     */
    private static final String glossaryTermPrefix = "Term";
    private static final String glossaryTermSummaryPrefix = "Summary of glossary term: ";
    private static final String glossaryTermDescriptionPrefix = "Description of glossary term: ";


    /*
     * Specific values for initializing TypeDefs
     */
    private static final long   versionNumber = 1L;
    private static final String versionName   = "1.0";

    private final OMRSArchiveBuilder         archiveBuilder;
    private final SimpleCatalogArchiveHelper archiveHelper;

    private final String                     glossaryIdentifier;

    /**
     * Constructor pushes all archive header values to the superclass
     *
     * @param glossaryIdentifier identifier of the glossary to build in the archive
     */
    public BigGlossaryArchiveBuilder(String glossaryIdentifier)
    {
        List<OpenMetadataArchive> dependentOpenMetadataArchives = new ArrayList<>();

        String archiveName = archiveNamePrefix + ":" + glossaryIdentifier;

        this.glossaryIdentifier = glossaryIdentifier;

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
                                                            archiveNamePrefix,
                                                            originatorName,
                                                            creationDate,
                                                            versionNumber,
                                                            versionName,
                                                            InstanceProvenanceType.LOCAL_COHORT,
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
        String glossaryGUID = archiveHelper.addGlossary("Glossary:" + glossaryIdentifier,
                                                        glossaryIdentifier,
                                                        null,
                                                        null,
                                                        null,
                                                        null,
                                                        null);

        for (int i=1; i<10001 ; i++)
        {
            String glossaryTermName = MessageFormat.format("Term{1, number,000000}", glossaryTermPrefix, i);

            System.out.println("Adding glossary term: " + glossaryTermName);

            archiveHelper.addTerm(glossaryGUID,
                                  null,
                                  "GlossaryTerm:" + glossaryTermName,
                                  glossaryTermName,
                                  glossaryTermDescriptionPrefix + glossaryTermName);
        }

        archiveHelper.saveGUIDs();

        return archiveBuilder.getOpenMetadataArchive();
    }
}

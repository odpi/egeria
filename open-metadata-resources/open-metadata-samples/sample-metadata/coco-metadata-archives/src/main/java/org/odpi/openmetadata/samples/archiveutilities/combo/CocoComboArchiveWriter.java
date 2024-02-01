/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.combo;


import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.businesssystems.CocoBusinessSystemsArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates.CocoClinicalTrialsArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceengines.CocoGovernanceEnginesArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.CocoSustainabilityArchiveWriter;

import java.util.Date;


/**
 * CocoComboArchiveWriter creates a physical open metadata archive file containing the combination of Coco Pharmaceuticals open metadata instances.
 */
public class CocoComboArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoComboArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String archiveGUID        = "655b1965-4c29-4a0e-8a5d-3f55a37b3799";
    private static final String archiveName        = "Coco Pharmaceuticals Combination";
    private static final String archiveDescription = "The combination of the contents of the Coco Pharmaceuticals' business systems, clinical trials templates, organization, and sustainability definitions.";


    /**
     * Default constructor initializes the archive.
     */
    public CocoComboArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              new Date(),
              archiveFileName);
    }




    /**
     * Returns the open metadata archive containing new metadata entities.
     *
     * @return populated open metadata archive object
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        getArchiveContent();

        /*
         * The completed archive is ready to be packaged up and returned
         */
        return this.archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Add the content to the archive builder.
     */
    public void getArchiveContent()
    {
        CocoClinicalTrialsArchiveWriter    clinicalTrialsArchiveWriter    = new CocoClinicalTrialsArchiveWriter();
        CocoGovernanceProgramArchiveWriter governanceProgramArchiveWriter = new CocoGovernanceProgramArchiveWriter();
        CocoGovernanceEnginesArchiveWriter governanceEnginesArchiveWriter = new CocoGovernanceEnginesArchiveWriter();
        CocoBusinessSystemsArchiveWriter   businessSystemsArchiveWriter   = new CocoBusinessSystemsArchiveWriter();
        CocoOrganizationArchiveWriter      organizationArchiveWriter      = new CocoOrganizationArchiveWriter();
        CocoSustainabilityArchiveWriter    sustainabilityArchiveWriter    = new CocoSustainabilityArchiveWriter();

        /*
         * By setting the builder to the combo builder, the archive writers create content in the combo builder rather than their own private builder.
         * It is important that the content is loaded into this builder in the right order as there is
         * no processing of dependent archives.
         */
        organizationArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        organizationArchiveWriter.getArchiveContent();

        clinicalTrialsArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        clinicalTrialsArchiveWriter.getArchiveContent();

        governanceProgramArchiveWriter.setArchiveBuilder(archiveBuilder,archiveHelper);
        governanceProgramArchiveWriter.getArchiveContent();

        governanceEnginesArchiveWriter.setArchiveBuilder(archiveBuilder,archiveHelper);
        governanceEnginesArchiveWriter.getArchiveContent();

        sustainabilityArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        sustainabilityArchiveWriter.getArchiveContent();

        businessSystemsArchiveWriter.setArchiveBuilder(archiveBuilder,archiveHelper);
        businessSystemsArchiveWriter.getArchiveContent();
    }

}

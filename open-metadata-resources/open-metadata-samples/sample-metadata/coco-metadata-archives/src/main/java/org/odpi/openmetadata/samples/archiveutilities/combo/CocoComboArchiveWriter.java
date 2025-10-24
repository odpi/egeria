/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.combo;


import org.odpi.openmetadata.archiveutilities.openconnectors.core.CorePackArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.samples.archiveutilities.EgeriaBaseArchiveWriter;
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
public class CocoComboArchiveWriter extends EgeriaBaseArchiveWriter
{
    private static final String archiveFileName = "CocoComboArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String archiveGUID        = "655b1965-4c29-4a0e-8a5d-3f55a37b3799";
    private static final String archiveName        = "Coco Pharmaceuticals Combination";
    private static final String archiveDescription = "The combination of the contents of the Coco Pharmaceuticals' business systems, clinical trials templates, organization, and sustainability definitions.";
    private static final Date   creationDate       = new Date(1639984840038L);


    /**
     * Default constructor initializes the archive.
     */
    public CocoComboArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              creationDate,
              archiveFileName,
              new OpenMetadataArchive[]{ new CorePackArchiveWriter().getOpenMetadataArchive()});
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
    @Override
    public void getArchiveContent()
    {
        CocoOrganizationArchiveWriter      organizationArchiveWriter      = new CocoOrganizationArchiveWriter();

        /*
         * By setting the builder to the combo builder, the archive writers create content in the combo builder rather than their own private builder.
         * It is important that the content is loaded into this builder in the right order as there is
         * no processing of dependent archives.
         */
        organizationArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        organizationArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        CocoGovernanceProgramArchiveWriter governanceProgramArchiveWriter = new CocoGovernanceProgramArchiveWriter();

        governanceProgramArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        governanceProgramArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        CocoClinicalTrialsArchiveWriter    clinicalTrialsArchiveWriter    = new CocoClinicalTrialsArchiveWriter();

        clinicalTrialsArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        clinicalTrialsArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        CocoGovernanceEnginesArchiveWriter governanceEnginesArchiveWriter = new CocoGovernanceEnginesArchiveWriter();

        governanceEnginesArchiveWriter.setArchiveBuilder(archiveBuilder,archiveHelper);
        governanceEnginesArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        CocoSustainabilityArchiveWriter    sustainabilityArchiveWriter    = new CocoSustainabilityArchiveWriter();

        sustainabilityArchiveWriter.setArchiveBuilder(archiveBuilder, archiveHelper);
        sustainabilityArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        CocoBusinessSystemsArchiveWriter   businessSystemsArchiveWriter   = new CocoBusinessSystemsArchiveWriter();

        businessSystemsArchiveWriter.setArchiveBuilder(archiveBuilder,archiveHelper);
        businessSystemsArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        // todo CocoProductCatalogArchiveWriter cocoProductCatalogArchiveWriter = new CocoProductCatalogArchiveWriter();

        // todo cocoProductCatalogArchiveWriter.setArchiveBuilder(archiveBuilder,archiveHelper);
        // todo cocoProductCatalogArchiveWriter.getArchiveContent();

        archiveHelper.saveGUIDs();

        archiveHelper.saveUsedGUIDs();
    }
}

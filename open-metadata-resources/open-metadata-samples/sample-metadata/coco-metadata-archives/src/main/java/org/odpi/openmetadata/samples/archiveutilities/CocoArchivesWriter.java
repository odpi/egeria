/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.samples.archiveutilities.businesssystems.CocoBusinessSystemsArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates.CocoClinicalTrialsArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoComboArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceengines.CocoGovernanceEnginesArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.governanceprogram.CocoGovernanceProgramArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.organization.CocoOrganizationArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.sustainability.CocoSustainabilityArchiveWriter;
import org.odpi.openmetadata.samples.archiveutilities.types.CocoTypesArchiveWriter;

/**
 * CocoArchivesWriter provides the main method to run the open metadata archive writers
 * that create each of the open metadata archives used in the open metadata labs and
 * other scenarios with Coco Pharmaceuticals.
 */
public class CocoArchivesWriter
{
    /**
     * Main program to initiate the archive writers for the Coco Pharmaceuticals samples and scenarios.
     *
     * @param args ignored
     */
    public static void main(String[] args)
    {
        try
        {
            final String folderName = "content-packs";

            CocoComboArchiveWriter             cocoComboArchiveWriter  = new CocoComboArchiveWriter();
            cocoComboArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoTypesArchiveWriter             typesArchiveWriter = new CocoTypesArchiveWriter();
            typesArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoOrganizationArchiveWriter      organizationArchiveWriter = new CocoOrganizationArchiveWriter();
            organizationArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoGovernanceProgramArchiveWriter cocoGovernanceProgramArchiveWriter = new CocoGovernanceProgramArchiveWriter();
            cocoGovernanceProgramArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoClinicalTrialsArchiveWriter    clinicalTrialsArchiveWriter    = new CocoClinicalTrialsArchiveWriter();
            clinicalTrialsArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoSustainabilityArchiveWriter    sustainabilityArchiveWriter  = new CocoSustainabilityArchiveWriter();
            sustainabilityArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoBusinessSystemsArchiveWriter   businessSystemsArchiveWriter = new CocoBusinessSystemsArchiveWriter();
            businessSystemsArchiveWriter.writeOpenMetadataArchive(folderName);

            CocoGovernanceEnginesArchiveWriter governanceEnginesArchiveWriter = new CocoGovernanceEnginesArchiveWriter();
            governanceEnginesArchiveWriter.writeOpenMetadataArchive(folderName);
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}

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
            CocoComboArchiveWriter             cocoComboArchiveWriter  = new CocoComboArchiveWriter();
            cocoComboArchiveWriter.writeOpenMetadataArchive();

            CocoTypesArchiveWriter             typesArchiveWriter = new CocoTypesArchiveWriter();
            CocoClinicalTrialsArchiveWriter    clinicalTrialsArchiveWriter    = new CocoClinicalTrialsArchiveWriter();
            CocoGovernanceProgramArchiveWriter cocoGovernanceProgramArchiveWriter = new CocoGovernanceProgramArchiveWriter();
            CocoGovernanceEnginesArchiveWriter governanceEnginesArchiveWriter = new CocoGovernanceEnginesArchiveWriter();
            CocoBusinessSystemsArchiveWriter   businessSystemsArchiveWriter = new CocoBusinessSystemsArchiveWriter();
            CocoOrganizationArchiveWriter      organizationArchiveWriter = new CocoOrganizationArchiveWriter();
            CocoSustainabilityArchiveWriter    sustainabilityArchiveWriter  = new CocoSustainabilityArchiveWriter();

            typesArchiveWriter.writeOpenMetadataArchive();
            clinicalTrialsArchiveWriter.writeOpenMetadataArchive();
            governanceEnginesArchiveWriter.writeOpenMetadataArchive();
            organizationArchiveWriter.writeOpenMetadataArchive();
            cocoGovernanceProgramArchiveWriter.writeOpenMetadataArchive();
            sustainabilityArchiveWriter.writeOpenMetadataArchive();
            businessSystemsArchiveWriter.writeOpenMetadataArchive();
        }
        catch (Exception error)
        {
            System.err.println("Exception: " + error);
            System.exit(-1);
        }
    }
}

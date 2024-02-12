/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.clinicaltrialtemplates;


import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveWriter;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.samples.archiveutilities.SimpleCatalogArchiveHelper;
import org.odpi.openmetadata.samples.archiveutilities.combo.CocoBaseArchiveWriter;

import java.util.Date;


/**
 * CocoClinicalTrialsArchiveWriter creates a physical open metadata archive file containing the clinical trials templates
 * needed by Coco Pharmaceuticals.
 */
public class CocoClinicalTrialsArchiveWriter extends CocoBaseArchiveWriter
{
    private static final String archiveFileName = "CocoClinicalTrialsTemplatesArchive.omarchive";

    /*
     * This is the header information for the archive.
     */
    private static final String                  archiveGUID        = "74a786b2-d6d7-401d-b8c1-7d798f752c55";
    private static final String                  archiveName        = "Coco Pharmaceuticals Clinical Trials Templates";
    private static final String                  archiveDescription = "Templates for new assets relating to a clinical trial.";

    /**
     * Default constructor initializes the archive.
     */
    public CocoClinicalTrialsArchiveWriter()
    {
        super(archiveGUID,
              archiveName,
              archiveDescription,
              new Date(),
              archiveFileName);
    }


    /**
     * Add the content to the archive builder.
     */
    public void getArchiveContent()
    {
    }

}

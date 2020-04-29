/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.testng.annotations.Test;

/**
 * Test that the owl canonical glossary model content loads without error.
 */
public class TestOwlCanonicalGlossaryModelArchiveBuilder
{
    private String modelLocation = "src/test/resources/EgeriaSampleOntology.json";
    @Test
    public void testOpenMetadataArchiveLoad() throws Exception
    {
            CanonicalGlossaryOwlParser       parser  = new CanonicalGlossaryOwlParser(modelLocation);
            CanonicalGlossaryOwlModelArchiveBuilder archive = new CanonicalGlossaryOwlModelArchiveBuilder(parser,false);

            OpenMetadataArchive archiveProperties = archive.getOpenMetadataArchive();
            OpenMetadataArchiveInstanceStore instanceStore  = archiveProperties.getArchiveInstanceStore();
            assert (instanceStore != null);
            assert ( instanceStore.getEntities().size() == 9);
            assert ( instanceStore.getRelationships().size() == 18);
    }
}


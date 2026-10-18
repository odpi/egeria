/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.mockito.Mockito;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * The classifications for a new entity are the ones set on the builder, plus any existing classifications the
 * builder was given that have not been replaced.  The existing classifications used to be skipped because the
 * loop walked the list it was adding to rather than the existing classifications.
 */
public class BuilderEntityClassificationsTest
{
    private static final String TYPE_NAME = "TestElement";


    @Test
    public void testExistingClassificationsAreKept()
    {
        OpenMetadataAPIGenericBuilder builder = getBuilder(List.of(getClassification("Existing", "existing")));

        builder.setClassification(getClassification("New", "new"));

        assertEquals(getNamesAndOrigins(builder.getEntityClassifications()), List.of("New:new", "Existing:existing"));
    }


    @Test
    public void testNewClassificationOverridesExisting()
    {
        OpenMetadataAPIGenericBuilder builder = getBuilder(List.of(getClassification("Shared", "existing"),
                                                                   getClassification("Existing", "existing")));

        builder.setClassification(getClassification("Shared", "new"));

        assertEquals(getNamesAndOrigins(builder.getEntityClassifications()), List.of("Shared:new", "Existing:existing"));
    }


    @Test
    public void testExistingClassificationsOnly()
    {
        OpenMetadataAPIGenericBuilder builder = getBuilder(List.of(getClassification("Existing", "existing")));

        assertEquals(getNamesAndOrigins(builder.getEntityClassifications()), List.of("Existing:existing"));
    }


    @Test
    public void testNoClassifications()
    {
        assertNull(getBuilder(null).getEntityClassifications());
    }


    private static OpenMetadataAPIGenericBuilder getBuilder(List<Classification> existingClassifications)
    {
        return new OpenMetadataAPIGenericBuilder("typeGUID",
                                                 TYPE_NAME,
                                                 null,
                                                 existingClassifications,
                                                 Mockito.mock(OMRSRepositoryHelper.class),
                                                 "testService",
                                                 "testServer");
    }


    /**
     * The metadata collection name is used only to tell same-named classifications apart.
     */
    private static Classification getClassification(String name, String origin)
    {
        Classification classification = new Classification();

        classification.setName(name);
        classification.setMetadataCollectionName(origin);

        return classification;
    }


    private static List<String> getNamesAndOrigins(List<Classification> classifications)
    {
        List<String> results = new ArrayList<>();

        for (Classification classification : classifications)
        {
            results.add(classification.getName() + ":" + classification.getMetadataCollectionName());
        }

        return results;
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.darwinfvt;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.OpenMetadataStore;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationship;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.search.ElementProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.PropertyHelper;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * DarwinAdoptionFVT checks what happens when a person edits a relationship that Darwin derived.  The edit adopts the
 * relationship: Darwin treats it from then on as asserted by an external user.  It no longer removes it when the
 * lineage beneath goes - which would lose the person's edit - and instead validates it like any other external
 * relationship, recording it as unproven.
 * <br>
 * The test changes its own set of the fixture (set 20), which nothing else reads.
 */
@ExtendWith(OMAGPlatformExtension.class)
public class DarwinAdoptionFVT
{
    /**
     * Run Darwin once before the test.
     *
     * @throws Exception a failure to refresh is fatal to the test
     */
    @BeforeAll
    public static void refreshDarwin() throws Exception
    {
        OMAGPlatformExtension.refreshDarwin();
    }


    /**
     * Darwin derives a data flow from the mapping between the two assets' columns.  The test edits the data flow's
     * description, then removes the mapping.  Both assets still have other columns in data mappings, so the schema
     * detail is catalogued for both ends: the adopted data flow is kept, and recorded as unproven against the asset
     * the data reaches.
     *
     * @throws Exception any failure is a test failure
     */
    @Test
    @DisplayName("Darwin keeps a derived relationship that someone has edited, and validates it like an asserted one")
    public void testAdoptedRelationshipIsKeptAndValidated() throws Exception
    {
        OpenMetadataStore openMetadataStore = ConnectorContextFactory.newContext().getOpenMetadataStore();
        PropertyHelper    propertyHelper    = new PropertyHelper();

        OpenMetadataRelationship dataFlow = DarwinFvtTestSupport.getRelationshipForSupplyChain(openMetadataStore,
                                                                                               OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                               DarwinFvtTestSupport.ASSET_ADOPTED_SOURCE_GUID,
                                                                                               DarwinFvtTestSupport.ASSET_ADOPTED_TARGET_GUID,
                                                                                               DarwinFvtTestSupport.ISC_ADOPTED);

        assertNotNull(dataFlow, "Expected Darwin to derive a DataFlow between the adopted assets from the mapping between their columns");
        assertTrue(DarwinFvtTestSupport.isCreatedByDarwin(dataFlow), "The DataFlow between the adopted assets should have been created by Darwin");

        /*
         * A person improves the description - which adopts the relationship.
         */
        final String editedDescription = "Orders are copied nightly from the source to the target.";

        ElementProperties properties = propertyHelper.addStringProperty(null, OpenMetadataProperty.DESCRIPTION.name, editedDescription);

        openMetadataStore.updateRelatedElementsInStore(dataFlow.getRelationshipGUID(), openMetadataStore.getUpdateOptions(true), properties);

        /*
         * Take the mapping away.  It belongs to the archive, so the removal is made on the archive's behalf.
         */
        openMetadataStore.deleteRelationshipInStore(DarwinFvtTestSupport.MAPPING_ADOPTED_GUID, DarwinFvtTestSupport.archiveOwnedDeleteOptions());

        OMAGPlatformExtension.refreshDarwin();

        List<OpenMetadataRelationship> dataFlows = DarwinFvtTestSupport.getRelationships(openMetadataStore,
                                                                                         OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                                         DarwinFvtTestSupport.ASSET_ADOPTED_SOURCE_GUID,
                                                                                         DarwinFvtTestSupport.ASSET_ADOPTED_TARGET_GUID);

        assertEquals(1, dataFlows.size(),
                     "Darwin should have kept the DataFlow that a person edited, even though the mapping it was derived from has gone");

        OpenMetadataRelationship adoptedFlow = dataFlows.get(0);

        assertEquals(dataFlow.getRelationshipGUID(), adoptedFlow.getRelationshipGUID(), "The adopted DataFlow should be the one Darwin derived");
        assertEquals(editedDescription,
                     propertyHelper.getStringProperty("darwin-fvt", OpenMetadataProperty.DESCRIPTION.name, adoptedFlow.getRelationshipProperties(), "testAdoptedRelationshipIsKeptAndValidated"),
                     "The person's edit to the DataFlow should have been kept");
        assertNotEquals(DarwinFvtTestSupport.DARWIN_USER_ID, adoptedFlow.getVersions().getUpdatedBy(),
                        "The DataFlow should still show that someone other than Darwin updated it");

        /*
         * Validated like an asserted relationship: unproven, so recorded against the asset the data reaches.
         */
        List<RelatedMetadataElement> exceptions = DarwinFvtTestSupport.getExceptions(openMetadataStore, DarwinFvtTestSupport.ASSET_ADOPTED_TARGET_GUID);

        assertEquals(1, exceptions.size(), "Expected one exception against the asset the adopted, now unproven, DataFlow reaches");
        assertEquals(List.of(adoptedFlow.getRelationshipGUID()), DarwinFvtTestSupport.getAffectedRelationships(exceptions.get(0)),
                     "The exception should list the adopted DataFlow");
    }
}

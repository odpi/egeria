/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRelationshipList;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.testng.Assert.*;

/**
 * The paging contract: an empty list means the page was filtered empty and the caller keeps paging; null means
 * there are no more results.  A list bean has to carry the empty list across the REST API intact.
 * OpenMetadataRelationshipList's getter used to turn an empty list into null, and since Jackson serializes through
 * the getter, a filtered page of relationships reached the caller as the end of the results.
 */
public class FilteredPageListJsonTest
{
    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    public void testEmptyRelationshipListSurvivesTheRestApi() throws Exception
    {
        OpenMetadataRelationshipList relationshipList = new OpenMetadataRelationshipList();

        relationshipList.setRelationships(new ArrayList<>());

        assertNotNull(relationshipList.getRelationships());
        assertTrue(relationshipList.getRelationships().isEmpty());

        OpenMetadataRelationshipList received = objectMapper.readValue(objectMapper.writeValueAsString(relationshipList),
                                                                       OpenMetadataRelationshipList.class);

        assertNotNull(received.getRelationships(), "a filtered page must not arrive as the end of the results");
        assertTrue(received.getRelationships().isEmpty());

        assertNotNull(new OpenMetadataRelationshipList(relationshipList).getRelationships());
    }


    @Test
    public void testNullRelationshipListStaysNull() throws Exception
    {
        OpenMetadataRelationshipList relationshipList = new OpenMetadataRelationshipList();

        OpenMetadataRelationshipList received = objectMapper.readValue(objectMapper.writeValueAsString(relationshipList),
                                                                       OpenMetadataRelationshipList.class);

        assertNull(received.getRelationships());
    }
}

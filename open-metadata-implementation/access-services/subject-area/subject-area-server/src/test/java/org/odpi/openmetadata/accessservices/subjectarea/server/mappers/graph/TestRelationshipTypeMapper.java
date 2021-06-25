/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.graph;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.RelationshipType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;


public class TestRelationshipTypeMapper {
    @Test
    public void testMapOMASRelationshipTypeToOMRSRelationshipTypeGuid() throws InvalidParameterException {
        try {
            RelationshipTypeMapper.mapOMASRelationshipTypeToOMRSRelationshipTypeGuid("operation", null);
            assertTrue(false, "Expected null to fail");
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        }
        for (RelationshipType relationshipType : RelationshipType.values()) {
            if (relationshipType == RelationshipType.Unknown) {
                try {
                    RelationshipTypeMapper.mapOMASRelationshipTypeToOMRSRelationshipTypeGuid("operation", relationshipType);
                    assertTrue(false, "Expected unknown to fail");
                } catch (InvalidParameterException e) {
                    e.printStackTrace();
                }
            } else {
                String guid = RelationshipTypeMapper.mapOMASRelationshipTypeToOMRSRelationshipTypeGuid("operation", relationshipType);
                assertTrue(guid != null, "guid should not be null");
            }
        }
    }
}

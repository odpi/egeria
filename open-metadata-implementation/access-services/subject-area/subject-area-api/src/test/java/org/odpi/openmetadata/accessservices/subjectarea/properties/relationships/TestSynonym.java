/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.LineEnd;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipEndCardinality;
import org.testng.annotations.Test;

import java.io.IOException;

import static org.testng.Assert.assertEquals;

public class TestSynonym
{
    @Test
    public void testDeser() throws IOException
    {

        Synonym synonym = new Synonym();
        synonym.setDescription("ddd");
        synonym.setExpression("Ex");
        synonym.setSource("source");
        synonym.setSteward("Stew");
        LineEnd end1 = new LineEnd("nodeType1", "name1", "description1",RelationshipEndCardinality.ANY_NUMBER);
        end1.setNodeGuid("guid1");
        synonym.setEnd1(end1);
        LineEnd end2 = new LineEnd("nodeType2", "name2", "description2",RelationshipEndCardinality.AT_MOST_ONE);
        end2.setNodeGuid("guid2");
        synonym.setEnd2(end2);

        ObjectMapper mapper = new ObjectMapper();
        String serialised = mapper.writeValueAsString(synonym);

        Synonym synonym2 = mapper.readValue(serialised,Synonym.class);
        assertEquals(synonym.getSource(),synonym2.getSource());
        assertEquals(synonym.getDescription(),synonym2.getDescription());
        assertEquals(synonym.getExpression(),synonym2.getExpression());
        assertEquals(synonym.getSteward(),synonym2.getSteward());
        // end1
        assertEquals(synonym2.getEnd1().getNodeType(),end1.getNodeType());
        assertEquals(synonym2.getEnd1().getNodeGuid(),end1.getNodeGuid());
        assertEquals(synonym2.getEnd1().getName(),end1.getName());
        assertEquals(synonym2.getEnd1().getDescription(),end1.getDescription());
        assertEquals(synonym2.getEnd1().getCardinality().getName(),end1.getCardinality().getName());
        // end2
        assertEquals(synonym2.getEnd2().getNodeType(),end2.getNodeType());
        assertEquals(synonym2.getEnd2().getNodeGuid(),end2.getNodeGuid());
        assertEquals(synonym2.getEnd2().getName(),end2.getName());
        assertEquals(synonym2.getEnd2().getDescription(),end2.getDescription());
        assertEquals(synonym2.getEnd2().getCardinality().getName(),end2.getCardinality().getName());
    }
}

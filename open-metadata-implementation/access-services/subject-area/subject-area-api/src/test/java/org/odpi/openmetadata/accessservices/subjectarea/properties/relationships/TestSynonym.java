/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.relationships;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
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
        synonym.setEntity1Guid("guid1");
        synonym.setEntity2Guid("guid2");
       // synonym.setStatus(TermRelationshipStatus.Deprecated);

        ObjectMapper mapper = new ObjectMapper();
        String serialised = mapper.writeValueAsString(synonym);

        Synonym synonym2 = mapper.readValue(serialised,Synonym.class);
        assertEquals(synonym.getSource(),synonym2.getSource());
        assertEquals(synonym.getDescription(),synonym2.getDescription());
        assertEquals(synonym.getExpression(),synonym2.getExpression());
        assertEquals(synonym.getSteward(),synonym2.getSteward());
       // assertEquals(synonym.getStatus(),synonym2.getStatus());

    }
}

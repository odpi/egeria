/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.term;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

public class TestTerm {
    @Test
    public void testBooleans() throws JsonProcessingException {
        Term term = new Term();
        term.setSpineAttribute(true);
        term.setSpineObject(false);
        if (!term.isSpineAttribute()){
            fail("Spine attribute is not correct");
        }
        if (term.isSpineObject()){
            fail("Spine object is not correct");
        }
        ObjectMapper objectMapper = new ObjectMapper();
        String termAsString = objectMapper.writeValueAsString(term);
        Term term2 = objectMapper.readValue(termAsString, Term.class);
        if (!term2.isSpineAttribute()){
            fail("Spine attribute is not correct after serialisation");
        }
        if (term2.isSpineObject()){
            fail("Spine object is not correct after serialisation");
        }


    }
}

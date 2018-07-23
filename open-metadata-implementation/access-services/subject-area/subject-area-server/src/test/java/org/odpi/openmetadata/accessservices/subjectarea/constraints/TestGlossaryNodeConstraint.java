/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.constraints;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class TestGlossaryNodeConstraint {

    @Test
    public void preCreate() {
        final String testGlossaryName = "TestGlossary";
        final String testDisplayName = "TestDisplayName";

        GlossaryTermConstraint constraint = new GlossaryTermConstraint();

        GlossaryTerm glossaryTerm = new GlossaryTerm();


        try {
            constraint.preCreate(glossaryTerm);
            fail("Expected an error as glossary terms should be allowed without a glossaryName ");
        } catch (InvalidParameterException e) {
            assertTrue(e.getErrorMessage().contains("OMAS-SUBJECTAREA-400-015"));
        }
        glossaryTerm.setGlossaryName(testGlossaryName);


        try {
            constraint.preCreate(glossaryTerm);
            fail("Expected an error");
        } catch (InvalidParameterException e) {
            assertTrue(e.getErrorMessage().contains("OMAS-SUBJECTAREA-400-016"));
        }
        glossaryTerm.setDisplayName(testDisplayName);
    }
}

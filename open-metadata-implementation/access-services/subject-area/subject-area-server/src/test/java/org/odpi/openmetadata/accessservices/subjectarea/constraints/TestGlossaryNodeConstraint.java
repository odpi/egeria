/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.constraints;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryTerm.GlossaryTerm;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
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
        try {

            glossaryTerm.setGlossaryName(testGlossaryName);
        } catch (InvalidParameterException e) {
           fail();
        }

        try {
            constraint.preCreate(glossaryTerm);
            fail("Expected an error");
        } catch (InvalidParameterException e) {
            assertTrue(e.getErrorMessage().contains("OMAS-SUBJECTAREA-400-016"));
        }

        try {
            glossaryTerm.setDisplayName(testDisplayName);
        } catch (InvalidParameterException e) {
            fail();
        }

//        try {
//            constraint.preCreate(glossaryTerm);
//            String qualifiedName = glossaryTerm.getGlossaryTermAttributes().getQualifiedName();
//           assertEquals(qualifiedName,testGlossaryName + "." + testDisplayName);
//        } catch (InvalidParameterException e) {
//            fail();
//        }
    }
}

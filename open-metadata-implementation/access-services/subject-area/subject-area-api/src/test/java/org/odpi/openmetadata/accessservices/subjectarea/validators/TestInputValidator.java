/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.validators;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.NodeType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;
public class TestInputValidator {
    @Test
    public void testGUIDNotNull()  {
        try {
            InputValidator.validateGUIDNotNull("c","m","A1","guidParm");
        } catch (InvalidParameterException e) {
            fail("Non null should not fail");
        }
        try {
            InputValidator.validateGUIDNotNull("c","m",null,"guidParm");
        } catch (InvalidParameterException e) {
            System.out.println("Expected error message " + e.getReportedErrorMessageId());
            if (e.getReportedErrorMessage().contains("{") || e.getReportedErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
    @Test
    public void testNameNotNull()  {
        try {
            InputValidator.validateNameNotNull("c","m","A1","nameParm");
        } catch (InvalidParameterException e) {
            fail("Non null should not fail");
        }
        try {
            InputValidator.validateNameNotNull("c","m",null,"nameParm");
        } catch (InvalidParameterException e) {
          System.out.println("Expected error message " + e.getReportedErrorMessageId());
             if (e.getReportedErrorMessage().contains("{") || e.getReportedErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
    @Test
    public void testOMASServerURLNotNull()  {
        try {
            InputValidator.validateRemoteServerURLNotNull("c", "m", "A1");
        } catch (InvalidParameterException e) {
            fail("Non null should not fail");
        }
        try {
            InputValidator.validateRemoteServerURLNotNull("c", "m", null);
        } catch (InvalidParameterException e) {
          System.out.println("Expected error message " + e.getReportedErrorMessageId());
             if (e.getReportedErrorMessage().contains("{") || e.getReportedErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
    @Test
    public void testUseridNotNull()  {
        try {
            InputValidator.validateUserIdNotNull("c","m","A1");
        } catch (InvalidParameterException e) {
            fail("Non null should not fail");
        }
        try {
            InputValidator.validateUserIdNotNull("c","m",null);
        } catch (InvalidParameterException e) {
          System.out.println("Expected error message " + e.getReportedErrorMessageId());
             if (e.getReportedErrorMessage().contains("{") || e.getReportedErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
    @Test
    public void testValidateStatus()  {
        try {
            InputValidator.validateStatusAndCheckNotDeleted("c","m","ACTIVE");
        } catch (InvalidParameterException e) {
            fail("Non null should not fail");
        }
        try {
            InputValidator.validateStatusAndCheckNotDeleted("c","m","DELETED");
        } catch (InvalidParameterException e) {
          System.out.println("Expected error message " + e.getReportedErrorMessageId());
             if (e.getReportedErrorMessage().contains("{") || e.getReportedErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
        try {
            InputValidator.validateStatusAndCheckNotDeleted("c","m","DANCER");
        } catch (InvalidParameterException e) {
          System.out.println("Expected error message " + e.getReportedErrorMessageId());
             if (e.getReportedErrorMessage().contains("{") || e.getReportedErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
    @Test
    public void testValidateNodeType() throws InvalidParameterException {

        NodeType nodetype = NodeType.Glossary;
        validateNodeTypeforGlossary(NodeType.Glossary);
        validateNodeTypeforGlossary(NodeType.Taxonomy);
        validateNodeTypeforGlossary(NodeType.TaxonomyAndCanonicalGlossary);
        validateNodeTypeforGlossary(NodeType.CanonicalGlossary);
        validateNodeTypeforGlossary(null);
        try {
            validateNodeTypeforGlossary(NodeType.Term);
            assertFalse(false, "Error incorrect nodetype accepted");
        } catch (InvalidParameterException e) {

        }
    }
    private void validateNodeTypeforGlossary (NodeType nodetype) throws InvalidParameterException {
        InputValidator.validateNodeType("classa","method1",nodetype, NodeType.Glossary,NodeType.Taxonomy,NodeType.TaxonomyAndCanonicalGlossary, NodeType.CanonicalGlossary);
    }
}

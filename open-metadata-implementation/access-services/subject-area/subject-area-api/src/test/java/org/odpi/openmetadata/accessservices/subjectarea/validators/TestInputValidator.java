/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.validators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
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
            System.out.println("Expected error message " + e.getErrorMessage());
            if (e.getErrorMessage().contains("{") || e.getErrorMessage().contains("}") ){
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
            System.out.println("Expected error message " + e.getErrorMessage());
            if (e.getErrorMessage().contains("{") || e.getErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
    @Test
    public void testOMASServerURLNotNull()  {
        try {
            InputValidator.validateOMASServerURLNotNull("c","m","A1");
        } catch (InvalidParameterException e) {
            fail("Non null should not fail");
        }
        try {
            InputValidator.validateOMASServerURLNotNull("c","m",null);
        } catch (InvalidParameterException e) {
            System.out.println("Expected error message " + e.getErrorMessage());
            if (e.getErrorMessage().contains("{") || e.getErrorMessage().contains("}") ){
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
            System.out.println("Expected error message " + e.getErrorMessage());
            if (e.getErrorMessage().contains("{") || e.getErrorMessage().contains("}") ){
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
            System.out.println("Expected error message " + e.getErrorMessage());
            if (e.getErrorMessage().contains("{") || e.getErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
        try {
            InputValidator.validateStatusAndCheckNotDeleted("c","m","DANCER");
        } catch (InvalidParameterException e) {
            System.out.println("Expected error message " + e.getErrorMessage());
            if (e.getErrorMessage().contains("{") || e.getErrorMessage().contains("}") ){
                fail("inserts not correct");
            }
        }
    }
}

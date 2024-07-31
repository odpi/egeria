/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassification;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementType;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the License bean can be cloned, compared, serialized, deserialized and printed as a String.
 */
public class TestLicense
{
    private ElementType                 type                 = new ElementType();
    private List<ElementClassification> classifications      = new ArrayList<>();
    private Map<String, String>         additionalProperties = new HashMap<>();

    private Date                 startDate    = new Date(27);
    private Date                 endDate      = new Date(1234);
    private ExternalReference    link         = new ExternalReference();


    /**
     * Default constructor
     */
    public TestLicense()
    {

    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private License getTestObject()
    {
        License testObject = new License();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setLicenseGUID("TestLicenseTypeGUID");
        testObject.setLicenseTypeName("TestLicenseTypeName");
        testObject.setLicensee("TestExaminer");
        testObject.setSummary("TestSummary");
        testObject.setLink(link);
        testObject.setStartDate(startDate);
        testObject.setEndDate(endDate);
        testObject.setLicenseConditions("TestLicenseConditions");
        testObject.setCreatedBy("TestCreatedBy");
        testObject.setCustodian("TestCustodian");
        testObject.setNotes("TestNotes");

        return testObject;
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(License  resultObject)
    {
        assertTrue(resultObject.getType().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getClassifications() != null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() != null);

        assertTrue(resultObject.getLicenseGUID().equals("TestLicenseTypeGUID"));
        assertTrue(resultObject.getLicenseTypeName().equals("TestLicenseTypeName"));
        assertTrue(resultObject.getLicensee().equals("TestExaminer"));
        assertTrue(resultObject.getSummary().equals("TestSummary"));
        assertTrue(resultObject.getLink().equals(link));
        assertTrue(resultObject.getStartDate().equals(startDate));
        assertTrue(resultObject.getEndDate().equals(endDate));
        assertTrue(resultObject.getLicenseConditions().equals("TestLicenseConditions"));
        assertTrue(resultObject.getCreatedBy().equals("TestCreatedBy"));
        assertTrue(resultObject.getCustodian().equals("TestCustodian"));
        assertTrue(resultObject.getNotes().equals("TestNotes"));
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        License    nullObject = new License();

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getLicenseGUID() == null);
        assertTrue(nullObject.getLicenseTypeName() == null);
        assertTrue(nullObject.getLicensee() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getLink() == null);
        assertTrue(nullObject.getStartDate() == null);
        assertTrue(nullObject.getEndDate() == null);
        assertTrue(nullObject.getLicenseConditions() == null);
        assertTrue(nullObject.getCreatedBy() == null);
        assertTrue(nullObject.getCustodian() == null);
        assertTrue(nullObject.getNotes() == null);

        nullObject = new License(null);

        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getLicenseGUID() == null);
        assertTrue(nullObject.getLicenseTypeName() == null);
        assertTrue(nullObject.getLicensee() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getLink() == null);
        assertTrue(nullObject.getStartDate() == null);
        assertTrue(nullObject.getEndDate() == null);
        assertTrue(nullObject.getLicenseConditions() == null);
        assertTrue(nullObject.getCreatedBy() == null);
        assertTrue(nullObject.getCustodian() == null);
        assertTrue(nullObject.getNotes() == null);
    }


    /**
     * Validate that 2 different objects with the same content are evaluated as equal.
     * Also that different objects are considered not equal.
     */
    @Test public void testEquals()
    {
        assertFalse(getTestObject().equals(null));
        assertFalse(getTestObject().equals("DummyString"));
        assertTrue(getTestObject().equals(getTestObject()));

        License  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        License  differentObject = getTestObject();
        differentObject.setGUID("Different");
        assertFalse(getTestObject().equals(differentObject));
    }


    /**
     *  Validate that 2 different objects with the same content have the same hash code.
     */
    @Test public void testHashCode()
    {
        assertTrue(getTestObject().hashCode() == getTestObject().hashCode());
    }


    /**
     *  Validate that an object cloned from another object has the same content as the original
     */
    @Test public void testClone()
    {
        validateResultObject(new License(getTestObject()));
    }


    /**
     * Validate that an object generated from a JSON String has the same content as the object used to
     * create the JSON String.
     */
    @Test public void testJSON()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        String       jsonString   = null;

        /*
         * This class
         */
        try
        {
            jsonString = objectMapper.writeValueAsString(getTestObject());
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject(objectMapper.readValue(jsonString, License.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        Referenceable  referenceable = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(referenceable);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((License) objectMapper.readValue(jsonString, Referenceable.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementBase elementBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(elementBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((License) objectMapper.readValue(jsonString, ElementBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        /*
         * Through superclass
         */
        ElementBase  propertyBase = getTestObject();

        try
        {
            jsonString = objectMapper.writeValueAsString(propertyBase);
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }

        try
        {
            validateResultObject((License) objectMapper.readValue(jsonString, ElementBase.class));
        }
        catch (Throwable  exc)
        {
            assertTrue(false, "Exception: " + exc.getMessage());
        }
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("License"));
    }
}

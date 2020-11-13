/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetLicense can function as a facade for its bean.
 */
public class TestAssetLicense
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
    public TestAssetLicense()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetLicense getTestObject()
    {
        License testBean = new License();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setLicenseGUID("TestLicenseTypeGUID");
        testBean.setLicenseTypeName("TestLicenseTypeName");
        testBean.setCustodian("TestCustodian");
        testBean.setLicensee("TestLicensee");
        testBean.setSummary("TestSummary");
        testBean.setLink(link);
        testBean.setStartDate(startDate);
        testBean.setEndDate(endDate);
        testBean.setLicenseConditions("TestLicenseConditions");
        testBean.setCreatedBy("TestCreatedBy");
        testBean.setCustodian("TestCustodian");
        testBean.setNotes("TestNotes");

        return new AssetLicense(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetLicense getDifferentObject()
    {
        License testBean = new License();

        testBean.setType(type);
        testBean.setGUID("TestDifferentGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setLicenseGUID("TestLicenseTypeGUID");
        testBean.setLicenseTypeName("TestLicenseTypeName");
        testBean.setSummary("TestSummary");
        testBean.setLink(link);
        testBean.setStartDate(startDate);
        testBean.setEndDate(endDate);
        testBean.setLicenseConditions("TestLicenseConditions");
        testBean.setCreatedBy("TestCreatedBy");
        testBean.setCustodian("TestCustodian");
        testBean.setLicensee("TestLicensee");
        testBean.setNotes("TestNotes");

        return new AssetLicense(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetLicense getAnotherDifferentObject()
    {
        License testBean = new License();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setLicenseGUID("TestLicenseTypeGUID");
        testBean.setLicenseTypeName("TestLicenseTypeName");
        testBean.setCustodian("TestDifferentCustodian");
        testBean.setLicensee("TestLicensee");
        testBean.setSummary("TestSummary");
        testBean.setLink(link);
        testBean.setStartDate(startDate);
        testBean.setEndDate(endDate);
        testBean.setLicenseConditions("TestLicenseConditions");
        testBean.setCreatedBy("TestCreatedBy");
        testBean.setNotes("TestNotes");

        return new AssetLicense(testBean);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetLicense  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getLicenseTypeGUID().equals("TestLicenseTypeGUID"));
        assertTrue(resultObject.getLicenseTypeName().equals("TestLicenseTypeName"));
        assertTrue(resultObject.getCustodian().equals("TestCustodian"));
        assertTrue(resultObject.getLicensee().equals("TestLicensee"));
        assertTrue(resultObject.getSummary().equals("TestSummary"));
        assertTrue(resultObject.getLink() != null);
        assertTrue(resultObject.getStartDate().equals(startDate));
        assertTrue(resultObject.getEndDate().equals(endDate));
        assertTrue(resultObject.getLicenseConditions().equals("TestLicenseConditions"));
        assertTrue(resultObject.getCreatedBy().equals("TestCreatedBy"));
        assertTrue(resultObject.getCustodian().equals("TestCustodian"));
        assertTrue(resultObject.getNotes().equals("TestNotes"));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetLicense  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getLicenseTypeGUID() == null);
        assertTrue(nullObject.getLicenseTypeName() == null);
        assertTrue(nullObject.getCustodian() == null);
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
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        License         nullBean;
        AssetLicense    nullObject;
        AssetLicense    nullTemplate;
        AssetDescriptor parentAsset;

        nullBean = null;
        nullObject = new AssetLicense(nullBean);
        validateNullObject(nullObject);

        nullBean = new License();
        nullObject = new AssetLicense(nullBean);
        validateNullObject(nullObject);

        nullBean = new License(null);
        nullObject = new AssetLicense(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetLicense(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new License();
        nullObject = new AssetLicense(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new License(null);
        nullObject = new AssetLicense(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetLicense(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the link is properly managed
     */
    @Test public void testLink()
    {
        ExternalReference  linkBean = new ExternalReference();
        linkBean.setOrganization("TestOrg");

        License license = new License();
        license.setLink(linkBean);

        AssetLicense testObject = new AssetLicense(license);
        AssetExternalReference   externalReference = testObject.getLink();

        assertTrue(externalReference.getOrganization().equals("TestOrg"));
        assertTrue(externalReference.getExternalReferenceBean().getOrganization().equals("TestOrg"));
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

        AssetLicense  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
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
        validateResultObject(new AssetLicense(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("License"));
    }
}

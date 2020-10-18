/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.*;
import org.testng.annotations.Test;

import java.util.*;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Validate that the AssetCertification can function as a facade for its bean.
 */
public class TestAssetCertification
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
    public TestAssetCertification()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetCertification getTestObject()
    {
        Certification testBean = new Certification();

        testBean.setType(type);
        testBean.setGUID("TestGUID");
        testBean.setURL("TestURL");
        testBean.setClassifications(classifications);

        testBean.setQualifiedName("TestQualifiedName");
        testBean.setAdditionalProperties(additionalProperties);

        testBean.setCertificateGUID("TestCertificationTypeGUID");
        testBean.setCertificationTypeName("TestCertificationTypeName");
        testBean.setExaminer("TestExaminer");
        testBean.setSummary("TestSummary");
        testBean.setLink(link);
        testBean.setStartDate(startDate);
        testBean.setEndDate(endDate);
        testBean.setCertificationConditions("TestCertificationConditions");
        testBean.setCreatedBy("TestCreatedBy");
        testBean.setCustodian("TestCustodian");
        testBean.setNotes("TestNotes");

        return new AssetCertification(testBean);
    }


    /**
     * Set up an example object to test.  This has a different field in the superclass (setGUID).
     *
     * @return filled in object
     */
    private AssetCertification getDifferentObject()
    {
        Certification testObject = new Certification();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setCertificateGUID("TestCertificationTypeGUID");
        testObject.setCertificationTypeName("TestCertificationTypeName");
        testObject.setExaminer("TestExaminer");
        testObject.setSummary("TestSummary");
        testObject.setLink(link);
        testObject.setStartDate(startDate);
        testObject.setEndDate(endDate);
        testObject.setCertificationConditions("TestCertificationConditions");
        testObject.setCreatedBy("TestCreatedBy");
        testObject.setCustodian("TestCustodian");
        testObject.setNotes("TestNotes");

        return new AssetCertification(testObject);
    }


    /**
     * Set up an example object to test.  This has a different field in the subclass.
     *
     * @return filled in object
     */
    private AssetCertification getAnotherDifferentObject()
    {
        Certification testObject = new Certification();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setAdditionalProperties(additionalProperties);

        testObject.setCertificateGUID("TestCertificationTypeGUID");
        testObject.setCertificationTypeName("TestCertificationTypeName");
        testObject.setExaminer("TestExaminer");
        testObject.setSummary("TestSummary");
        testObject.setLink(link);
        testObject.setStartDate(startDate);
        testObject.setEndDate(endDate);
        testObject.setCertificationConditions("TestCertificationConditions");
        testObject.setCreatedBy("TestDifferentCreatedBy");
        testObject.setCustodian("TestCustodian");
        testObject.setNotes("TestNotes");

        return new AssetCertification(testObject);
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param resultObject object returned by the test
     */
    private void validateResultObject(AssetCertification  resultObject)
    {
        assertTrue(resultObject.getType().getElementTypeBean().equals(type));
        assertTrue(resultObject.getGUID().equals("TestGUID"));
        assertTrue(resultObject.getURL().equals("TestURL"));
        assertTrue(resultObject.getAssetClassifications() == null);

        assertTrue(resultObject.getQualifiedName().equals("TestQualifiedName"));
        assertTrue(resultObject.getAdditionalProperties() == null);

        assertTrue(resultObject.getCertificationTypeGUID().equals("TestCertificationTypeGUID"));
        assertTrue(resultObject.getCertificationTypeName().equals("TestCertificationTypeName"));
        assertTrue(resultObject.getExaminer().equals("TestExaminer"));
        assertTrue(resultObject.getSummary().equals("TestSummary"));
        assertTrue(resultObject.getLink() != null);
        assertTrue(resultObject.getStartDate().equals(startDate));
        assertTrue(resultObject.getEndDate().equals(endDate));
        assertTrue(resultObject.getCertificationConditions().equals("TestCertificationConditions"));
        assertTrue(resultObject.getCreatedBy().equals("TestCreatedBy"));
        assertTrue(resultObject.getCustodian().equals("TestCustodian"));
        assertTrue(resultObject.getNotes().equals("TestNotes"));
    }


    /**
     * Validate that the object that comes out of the test has the same content as the original test object.
     *
     * @param nullObject object to test
     */
    private void validateNullObject(AssetCertification  nullObject)
    {
        assertTrue(nullObject.getType() == null);
        assertTrue(nullObject.getGUID() == null);
        assertTrue(nullObject.getURL() == null);
        assertTrue(nullObject.getAssetClassifications() == null);

        assertTrue(nullObject.getQualifiedName() == null);
        assertTrue(nullObject.getAdditionalProperties() == null);

        assertTrue(nullObject.getCertificationTypeGUID() == null);
        assertTrue(nullObject.getCertificationTypeName() == null);
        assertTrue(nullObject.getExaminer() == null);
        assertTrue(nullObject.getSummary() == null);
        assertTrue(nullObject.getLink() == null);
        assertTrue(nullObject.getStartDate() == null);
        assertTrue(nullObject.getEndDate() == null);
        assertTrue(nullObject.getCertificationConditions() == null);
        assertTrue(nullObject.getCreatedBy() == null);
        assertTrue(nullObject.getCustodian() == null);
        assertTrue(nullObject.getNotes() == null);
    }


    /**
     * Validate that the object is initialized properly
     */
    @Test public void testNullObject()
    {
        Certification         nullBean;
        AssetCertification    nullObject;
        AssetCertification    nullTemplate;
        AssetDescriptor       parentAsset;

        nullBean = null;
        nullObject = new AssetCertification(nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification();
        nullObject = new AssetCertification(nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification(null);
        nullObject = new AssetCertification(nullBean);
        validateNullObject(nullObject);

        parentAsset = null;
        nullBean = null;
        nullObject = new AssetCertification(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification();
        nullObject = new AssetCertification(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullBean = new Certification(null);
        nullObject = new AssetCertification(parentAsset, nullBean);
        validateNullObject(nullObject);

        nullTemplate = null;
        nullObject = new AssetCertification(parentAsset, nullTemplate);
        validateNullObject(nullObject);
    }


    /**
     * Test that the link is properly managed
     */
    @Test public void testLink()
    {
        ExternalReference  linkBean = new ExternalReference();
        linkBean.setOrganization("TestOrg");

        Certification certificationBean = new Certification();
        certificationBean.setLink(linkBean);

        AssetCertification testObject = new AssetCertification(certificationBean);
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

        AssetCertification  sameObject = getTestObject();
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
        validateResultObject(new AssetCertification(null, getTestObject()));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(getTestObject().toString().contains("Certification"));
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Test that AssetDetail can extend the behaviour of the Asset bean.
 */
public class TestAssetDetail
{
    private ElementType                 type                   = new ElementType();
    private List<ElementClassification> classifications        = new ArrayList<>();
    private Map<String, Object>         assetProperties        = new HashMap<>();
    private AssetExternalIdentifiers    externalIdentifiers    = null;
    private AssetRelatedMediaReferences relatedMediaReferences = null;
    private AssetNoteLogs               noteLogs               = null;
    private AssetExternalReferences     externalReferences     = null;
    private AssetConnections            connections            = null;
    private AssetLicenses               licenses               = null;
    private AssetCertifications         certifications         = null;


    /**
     * Default constructor
     */
    public TestAssetDetail()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetDetail getTestObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setDisplayName("TestDisplayName");
        testObject.setOwner("TestOwner");
        testObject.setShortDescription("TestShortDescription");
        testObject.setDescription("TestDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetDetail(testObject,
                               externalIdentifiers,
                               relatedMediaReferences,
                               noteLogs,
                               externalReferences,
                               connections,
                               licenses,
                               certifications);
    }


    /**
     * Set up an example object to test. A property from the super class is different.
     *
     * @return filled in object
     */
    private AssetDetail getDifferentObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestDifferentGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestQualifiedName");
        testObject.setDisplayName("TestDisplayName");
        testObject.setOwner("TestOwner");
        testObject.setShortDescription("TestShortDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetDetail(testObject,
                               externalIdentifiers,
                               relatedMediaReferences,
                               noteLogs,
                               externalReferences,
                               connections,
                               licenses,
                               certifications);
    }


    /**
     * Set up an example object to test.  A property from the subclass is different.
     *
     * @return filled in object
     */
    private AssetDetail getAnotherDifferentObject()
    {
        Asset testObject = new Asset();

        testObject.setType(type);
        testObject.setGUID("TestGUID");
        testObject.setURL("TestURL");
        testObject.setClassifications(classifications);

        testObject.setQualifiedName("TestDifferentQualifiedName");
        testObject.setDisplayName("TestDisplayName");
        testObject.setOwner("TestOwner");
        testObject.setShortDescription("TestShortDescription");
        testObject.setDescription("TestDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetDetail(testObject,
                               new MockAssetExternalIdentifiers(null, 45, 8),
                               relatedMediaReferences,
                               noteLogs,
                               externalReferences,
                               connections,
                               licenses,
                               certifications);
    }


    /**
     * Test that the AssetDetail can be created and accessed with null content
     */
    @Test public void testBeanConstructor()
    {
        AssetExternalIdentifiers    externalIdentifiers    = null;
        AssetRelatedMediaReferences relatedMediaReferences = null;
        AssetNoteLogs               noteLogs               = null;
        AssetExternalReferences     externalReferences     = null;
        AssetConnections            connections            = null;
        AssetLicenses               licenses               = null;
        AssetCertifications         certifications         = null;

        AssetDetail testObject = new AssetDetail(new Asset(),
                                                 externalIdentifiers,
                                                 relatedMediaReferences,
                                                 noteLogs,
                                                 externalReferences,
                                                 connections,
                                                 licenses,
                                                 certifications);

        assertTrue(testObject.getExternalIdentifiers() == null);
        assertTrue(testObject.getRelatedMediaReferences() == null);
        assertTrue(testObject.getNoteLogs() == null);
        assertTrue(testObject.getExternalReferences() == null);
        assertTrue(testObject.getConnections() == null);
        assertTrue(testObject.getLicenses() == null);
        assertTrue(testObject.getCertifications() == null);
    }


    /**
     * Test that the AssetDetail can be created and accessed with null content
     */
    @Test public void testCloneConstructor()
    {
        AssetExternalIdentifiers    externalIdentifiers    = new MockAssetExternalIdentifiers(null, 15, 50);
        AssetRelatedMediaReferences relatedMediaReferences = new MockAssetRelatedMediaReferences(null, 15, 50);
        AssetNoteLogs               noteLogs               = new MockAssetNoteLogs(null, 15, 50);
        AssetExternalReferences     externalReferences     = new MockAssetExternalReferences(null, 15, 50);
        AssetConnections            connections            = new MockAssetConnections(null, 15, 50);
        AssetLicenses               licenses               = new MockAssetLicenses(null, 15, 50);
        AssetCertifications         certifications         = new MockAssetCertifications(null, 15, 50);

        AssetDetail testObject = new AssetDetail(new Asset(),
                                                 externalIdentifiers,
                                                 relatedMediaReferences,
                                                 noteLogs,
                                                 externalReferences,
                                                 connections,
                                                 licenses,
                                                 certifications);

        assertTrue(testObject.getExternalIdentifiers() != null);
        assertTrue(testObject.getRelatedMediaReferences() != null);
        assertTrue(testObject.getNoteLogs() != null);
        assertTrue(testObject.getExternalReferences() != null);
        assertTrue(testObject.getConnections() != null);
        assertTrue(testObject.getLicenses() != null);
        assertTrue(testObject.getCertifications() != null);
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        AssetExternalIdentifiers    externalIdentifiers    = new MockAssetExternalIdentifiers(null, 15, 50);
        AssetRelatedMediaReferences relatedMediaReferences = new MockAssetRelatedMediaReferences(null, 15, 50);
        AssetNoteLogs               noteLogs               = new MockAssetNoteLogs(null, 15, 50);
        AssetExternalReferences     externalReferences     = new MockAssetExternalReferences(null, 15, 50);
        AssetConnections            connections            = new MockAssetConnections(null, 15, 50);
        AssetLicenses               licenses               = new MockAssetLicenses(null, 15, 50);
        AssetCertifications         certifications         = new MockAssetCertifications(null, 15, 50);

        AssetDetail testTemplate = new AssetDetail(new Asset(),
                                                   externalIdentifiers,
                                                   relatedMediaReferences,
                                                   noteLogs,
                                                   externalReferences,
                                                   connections,
                                                   licenses,
                                                   certifications);

        AssetDetail testObject = new AssetDetail(testTemplate);

        assertTrue(testObject.toString().contains("Asset"));
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

        AssetDetail  sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
    }
}

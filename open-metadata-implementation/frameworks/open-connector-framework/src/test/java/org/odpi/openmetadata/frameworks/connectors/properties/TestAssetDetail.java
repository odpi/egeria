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
    private final ElementType                 type                   = new ElementType();
    private final List<ElementClassification> classifications        = new ArrayList<>();
    private final Map<String, Object>         assetProperties        = new HashMap<>();
    private final ExternalIdentifiers         externalIdentifiers    = null;
    private final RelatedMediaReferences      relatedMediaReferences = null;
    private final NoteLogs                    noteLogs               = null;
    private final ExternalReferences          externalReferences     = null;
    private final Connections                 connections            = null;
    private final Licenses                    licenses               = null;
    private final Certifications              certifications         = null;


    /**
     * Default constructor
     */
    public TestAssetDetail()
    {
        type.setTypeName("TestType");
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
        testObject.setResourceName("TestResourceName");
        testObject.setOwner("TestOwner");
        testObject.setConnectionDescription("TestShortDescription");
        testObject.setResourceDescription("TestDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetDetail(testObject,
                               externalIdentifiers,
                               relatedMediaReferences,
                               noteLogs,
                               externalReferences,
                               connections,
                               licenses,
                               certifications,
                               null);
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
        testObject.setResourceName("TestResourceName");
        testObject.setOwner("TestOwner");
        testObject.setConnectionDescription("TestShortDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetDetail(testObject,
                               externalIdentifiers,
                               relatedMediaReferences,
                               noteLogs,
                               externalReferences,
                               connections,
                               licenses,
                               certifications,
                               null);
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
        testObject.setResourceName("TestResourceName");
        testObject.setOwner("TestOwner");
        testObject.setConnectionDescription("TestShortDescription");
        testObject.setResourceDescription("TestDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetDetail(testObject,
                               new MockExternalIdentifiers(45, 8),
                               relatedMediaReferences,
                               noteLogs,
                               externalReferences,
                               connections,
                               licenses,
                               certifications,
                               null);
    }


    /**
     * Test that the AssetDetail can be created and accessed with null content
     */
    @Test public void testBeanConstructor()
    {
        ExternalIdentifiers    externalIdentifiers    = null;
        RelatedMediaReferences relatedMediaReferences = null;
        NoteLogs               noteLogs               = null;
        ExternalReferences     externalReferences     = null;
        Connections            connections    = null;
        Licenses               licenses       = null;
        Certifications         certifications = null;

        AssetDetail testObject = new AssetDetail(new Asset(),
                                                 externalIdentifiers,
                                                 relatedMediaReferences,
                                                 noteLogs,
                                                 externalReferences,
                                                 connections,
                                                 licenses,
                                                 certifications,
                                                 null);

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
        ExternalIdentifiers    externalIdentifiers    = new MockExternalIdentifiers( 15, 50);
        RelatedMediaReferences relatedMediaReferences = new MockRelatedMediaReferences( 15, 50);
        NoteLogs               noteLogs               = new MockNoteLogs( 15, 50);
        ExternalReferences     externalReferences     = new MockExternalReferences( 15, 50);
        Connections         connections    = new MockConnections(15, 50);
        Licenses            licenses       = new MockLicenses(15, 50);
        Certifications      certifications = new MockAssetCertifications( 15, 50);

        AssetDetail testObject = new AssetDetail(new Asset(),
                                                 externalIdentifiers,
                                                 relatedMediaReferences,
                                                 noteLogs,
                                                 externalReferences,
                                                 connections,
                                                 licenses,
                                                 certifications,
                                                 null);

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
        ExternalIdentifiers    externalIdentifiers    = new MockExternalIdentifiers( 15, 50);
        RelatedMediaReferences relatedMediaReferences = new MockRelatedMediaReferences( 15, 50);
        NoteLogs               noteLogs               = new MockNoteLogs( 15, 50);
        ExternalReferences     externalReferences     = new MockExternalReferences( 15, 50);
        Connections            connections    = new MockConnections( 15, 50);
        Licenses               licenses       = new MockLicenses(15, 50);
        Certifications         certifications = new MockAssetCertifications( 15, 50);

        AssetDetail testTemplate = new AssetDetail(new Asset(),
                                                   externalIdentifiers,
                                                   relatedMediaReferences,
                                                   noteLogs,
                                                   externalReferences,
                                                   connections,
                                                   licenses,
                                                   certifications,
                                                   null);

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

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
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
public class TestAssetUniverse
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
    private AssetCertifications certifications = null;
    private List<AssetMeaning>  meanings       = null;
    private AssetSchemaType     schema         = null;
    private AssetFeedback       feedback       = null;
    private AssetLocations      knownLocations = null;
    private AssetLineage        lineage        = null;
    private AssetRelatedAssets  relatedAssets  = null;


    /**
     * Default constructor
     */
    public TestAssetUniverse()
    {
        type.setElementTypeName("TestType");
    }


    /**
     * Set up an example object to test.
     *
     * @return filled in object
     */
    private AssetUniverse getTestObject()
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

        return new AssetUniverse(testObject,
                                 externalIdentifiers,
                                 relatedMediaReferences,
                                 noteLogs,
                                 externalReferences,
                                 connections,
                                 licenses,
                                 certifications,
                                 meanings,
                                 schema,
                                 feedback,
                                 knownLocations,
                                 lineage,
                                 relatedAssets);
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
        testObject.setDescription("TestDescription");
        testObject.setExtendedProperties(assetProperties);

        return new AssetUniverse(testObject,
                                 externalIdentifiers,
                                 relatedMediaReferences,
                                 noteLogs,
                                 externalReferences,
                                 connections,
                                 licenses,
                                 certifications,
                                 meanings,
                                 schema,
                                 feedback,
                                 knownLocations,
                                 lineage,
                                 relatedAssets);
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

        return new AssetUniverse(testObject,
                                 externalIdentifiers,
                                 relatedMediaReferences,
                                 noteLogs,
                                 externalReferences,
                                 connections,
                                 licenses,
                                 certifications,
                                 meanings,
                                 schema,
                                 feedback,
                                 knownLocations,
                                 lineage,
                                 relatedAssets);
    }


    /**
     * Test that the AssetDetail can be created and accessed with null content
     */
    @Test public void testBeanConstructorWithNulls()
    {
        List<AssetMeaning> meanings       = null;
        AssetSchemaType    schema         = null;
        AssetFeedback      feedback       = null;
        AssetLocations     knownLocations = null;
        AssetLineage       lineage        = null;
        AssetRelatedAssets relatedAssets  = null;

        AssetUniverse testObject = new AssetUniverse(new Asset(),
                                                     externalIdentifiers,
                                                     relatedMediaReferences,
                                                     noteLogs,
                                                     externalReferences,
                                                     connections,
                                                     licenses,
                                                     certifications,
                                                     meanings,
                                                     schema,
                                                     feedback,
                                                     knownLocations,
                                                     lineage,
                                                     relatedAssets);

        assertTrue(testObject.getMeanings() == null);
        assertTrue(testObject.getSchema() == null);
        assertTrue(testObject.getFeedback() == null);
        assertTrue(testObject.getKnownLocations() == null);
        assertTrue(testObject.getLineage() == null);
        assertTrue(testObject.getRelatedAssets() == null);
    }


    /**
     * Test that the AssetDetail can be created and accessed with null content
     */
    @Test public void testBeanConstructor()
    {
        AssetInformalTags  informalTags = new MockAssetInformalTags(null, 15, 50);
        AssetLikes         likes        = new MockAssetLikes(null, 15, 50);
        AssetRatings       ratings      = new MockAssetRatings(null, 15, 50);
        AssetComments      comments     = new MockAssetComments(null, 15, 50);
        List<AssetMeaning> meanings     = new ArrayList<>();
        AssetSchemaType    schema       = new AssetPrimitiveSchemaType((PrimitiveSchemaType) null);
        AssetFeedback      feedback             = new AssetFeedback(null,
                                                                 informalTags,
                                                                 likes,
                                                                 ratings,
                                                                 comments);
        AssetLocations     knownLocations = new MockAssetLocations(null, 15, 50);
        AssetLineage       lineage        = new AssetLineage();
        AssetRelatedAssets relatedAssets  = new MockRelatedAssets(null, 15, 50);

        AssetUniverse testObject = new AssetUniverse(new Asset(),
                                                     externalIdentifiers,
                                                     relatedMediaReferences,
                                                     noteLogs,
                                                     externalReferences,
                                                     connections,
                                                     licenses,
                                                     certifications,
                                                     meanings,
                                                     schema,
                                                     feedback,
                                                     knownLocations,
                                                     lineage,
                                                     relatedAssets);

        assertTrue(testObject.getMeanings() == null);
        assertTrue(testObject.getSchema() != null);
        assertTrue(testObject.getFeedback() != null);
        assertTrue(testObject.getKnownLocations() != null);
        assertTrue(testObject.getLineage() != null);
        assertTrue(testObject.getRelatedAssets() != null);
    }


    /**
     * Validate the subclass constructor works all of the way up the inheritance hierarchy.
     */
    @Test public void testSubclassInitialization()
    {
        new AssetUniverse();
    }



    /**
     * Test that toString is overridden.
     */
    @Test
    public void testToString()
    {
        AssetInformalTags  informalTags = new MockAssetInformalTags(null, 15, 50);
        AssetLikes         likes        = new MockAssetLikes(null, 15, 50);
        AssetRatings       ratings      = new MockAssetRatings(null, 15, 50);
        AssetComments      comments       = new MockAssetComments(null, 15, 50);
        List<AssetMeaning> meanings       = new ArrayList<>();
        AssetSchemaType    schema         = new AssetPrimitiveSchemaType((PrimitiveSchemaType) null);
        AssetFeedback      feedback       = new AssetFeedback(null, informalTags, likes, ratings, comments);
        AssetLocations     knownLocations = new MockAssetLocations(null, 15, 50);
        AssetLineage       lineage        = new AssetLineage();
        AssetRelatedAssets relatedAssets  = new MockRelatedAssets(null, 15, 50);

        AssetUniverse testTemplate = new AssetUniverse(new Asset(),
                                                       externalIdentifiers,
                                                       relatedMediaReferences,
                                                       noteLogs,
                                                       externalReferences,
                                                       connections,
                                                       licenses,
                                                       certifications,
                                                       meanings,
                                                       schema,
                                                       feedback,
                                                       knownLocations,
                                                       lineage,
                                                       relatedAssets);


        AssetUniverse testObject = new AssetUniverse(testTemplate);

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

        AssetUniverse sameObject = getTestObject();
        assertTrue(sameObject.equals(sameObject));

        assertFalse(getTestObject().equals(getDifferentObject()));
        assertFalse(getTestObject().equals(getAnotherDifferentObject()));
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementClassification;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Meaning;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.PrimitiveSchemaType;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;
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
    private ExternalIdentifiers         externalIdentifiers    = null;
    private RelatedMediaReferences      relatedMediaReferences = null;
    private NoteLogs                    noteLogs               = null;
    private ExternalReferences          externalReferences     = null;
    private Connections                 connections            = null;
    private Licenses                    licenses               = null;
    private Certifications              certifications         = null;
    private List<Meaning>               meanings               = null;
    private SchemaType                  schema                 = null;
    private AssetFeedback               feedback               = null;
    private Locations                   knownLocations         = null;
    private AssetLineage                lineage                = null;
    private RelatedAssets               relatedAssets          = null;


    /**
     * Default constructor
     */
    public TestAssetUniverse()
    {
        type.setTypeName("TestType");
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
        testObject.setConnectionDescription("TestShortDescription");
        testObject.setResourceDescription("TestDescription");
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
        testObject.setConnectionDescription("TestShortDescription");
        testObject.setResourceDescription("TestDescription");
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
        testObject.setConnectionDescription("TestShortDescription");
        testObject.setResourceDescription("TestDescription");
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
        List<Meaning> meanings       = null;
        SchemaType    schema         = null;
        AssetFeedback feedback       = null;
        Locations     knownLocations = null;
        AssetLineage  lineage        = null;
        RelatedAssets relatedAssets = null;

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
        assertTrue(testObject.getRootSchemaType() == null);
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
        InformalTags informalTags = new MockInformalTags(15, 50);
        Likes        likes        = new MockLikes(15, 50);
        Ratings      ratings      = new MockRatings( 15, 50);
        Comments   comments = new MockComments( 15, 50);
        List<Meaning> meanings = new ArrayList<>();
        SchemaType    schema       = new PrimitiveSchemaType();
        AssetFeedback      feedback             = new AssetFeedback(informalTags,
                                                                 likes,
                                                                 ratings,
                                                                 comments);
        Locations    knownLocations = new MockLocations( 15, 50);
        AssetLineage lineage        = new AssetLineage();
        RelatedAssets relatedAssets = new MockRelatedAssets(15, 50);

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
        assertTrue(testObject.getRootSchemaType() != null);
        assertTrue(testObject.getFeedback() != null);
        assertTrue(testObject.getKnownLocations() != null);
        assertTrue(testObject.getLineage() != null);
        assertTrue(testObject.getRelatedAssets() != null);
    }


    /**
     * Validate the subclass constructor works all the way up the inheritance hierarchy.
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
        InformalTags informalTags = new MockInformalTags(15, 50);
        Likes        likes        = new MockLikes(15, 50);
        Ratings      ratings      = new MockRatings(15, 50);
        Comments   comments = new MockComments( 15, 50);
        List<Meaning> meanings = new ArrayList<>();
        SchemaType    schema         = new PrimitiveSchemaType();
        AssetFeedback feedback       = new AssetFeedback( informalTags, likes, ratings, comments);
        Locations     knownLocations = new MockLocations( 15, 50);
        AssetLineage  lineage        = new AssetLineage();
        RelatedAssets relatedAssets = new MockRelatedAssets( 15, 50);

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

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests the getters and setters of AssetPropertyBase.  This is an abstract class and so
 * it uses the AdditionalProperties object as the concrete class.  The full test of
 * AdditionalProperties is in TestAdditionalProperties.
 */
public class TestAdditionalProperties
{
    /**
     * Constructor
     */
    public TestAdditionalProperties()
    {
    }


    private Map<String, String>  getPropertyMap()
    {
        Map<String, String>  propertyMap = new HashMap<>();

        propertyMap.put("property1", "TestString");
        propertyMap.put("property2", "Two");

        return propertyMap;
    }


    private  void validateAdditionalProperties(AdditionalProperties  additionalProperties)
    {
        assertTrue(additionalProperties.getPropertyNames() != null);

        Iterator<String> iterator = additionalProperties.getPropertyNames();

        String propertyName;


        propertyName = iterator.next();
        assertTrue(propertyName.equals("property2"));
        assertTrue(additionalProperties.getProperty(propertyName).equals("Two"));

        propertyName = iterator.next();
        assertTrue(propertyName.equals("property1"));
        assertTrue(additionalProperties.getProperty(propertyName).equals("TestString"));

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }
    }


    @Test public void testNullProperties()
    {
        AssetDescriptor      parentAsset = null;
        AdditionalProperties template    = null;
        Map<String, String>  propertyMap = new HashMap<>();

        AdditionalProperties  additionalProperties = new AdditionalProperties(parentAsset, template);
        Iterator<String>      iterator             = additionalProperties.getPropertyNames();

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        additionalProperties = new AdditionalProperties(parentAsset, propertyMap);
        iterator             = additionalProperties.getPropertyNames();

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }

        additionalProperties = new AdditionalProperties(propertyMap);
        iterator             = additionalProperties.getPropertyNames();

        try
        {
            iterator.next();
            assertTrue(false);
        }
        catch (Throwable   exc)
        {
            assertTrue(true);
        }
    }


    @Test public void testValidProperties()
    {
        AdditionalProperties additionalProperties;

        additionalProperties = new AdditionalProperties(null,
                                                        getPropertyMap());

        validateAdditionalProperties(additionalProperties);

        validateAdditionalProperties(new AdditionalProperties(null, additionalProperties));
    }


    @Test public void testValidParentAsset()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");
        assetBean.setQualifiedName("TestAssetName");
        assetBean.setType(elementType);

        AssetDescriptor parentAsset = new AssetSummary(assetBean);

        AdditionalProperties template    = null;
        Map<String, String>  propertyMap = new HashMap<>();

        AssetPropertyBase assetPropertyBase = new AdditionalProperties(parentAsset, template);

        assertTrue(assetPropertyBase.getParentAsset() == parentAsset);
        assertTrue(assetPropertyBase.getParentAssetName().equals("TestAssetName"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("TestTypeName"));

        assetPropertyBase = new AdditionalProperties(parentAsset, propertyMap);

        assertTrue(assetPropertyBase.getParentAsset() == parentAsset);
        assertTrue(assetPropertyBase.getParentAsset().equals(parentAsset));
        assertTrue(assetPropertyBase.getParentAssetName().equals("TestAssetName"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("TestTypeName"));


        AssetDescriptor cloneParentAsset = new AssetSummary(assetBean);
        template = new AdditionalProperties(cloneParentAsset, propertyMap);

        assetPropertyBase = new AdditionalProperties(parentAsset, template);

        assertFalse(assetPropertyBase.getParentAsset() == cloneParentAsset);
        assertTrue(assetPropertyBase.getParentAsset() == parentAsset);
        assertTrue(assetPropertyBase.getParentAsset().equals(cloneParentAsset));
        assertTrue(assetPropertyBase.getParentAsset().equals(parentAsset));
        assertTrue(assetPropertyBase.getParentAssetName().equals("TestAssetName"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("TestTypeName"));
    }


    @Test public void testEquals()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");
        assetBean.setQualifiedName("TestAssetName");
        assetBean.setType(elementType);

        AssetDescriptor      parentAsset = new AssetSummary(assetBean);
        Map<String, String>  propertyMap = new HashMap<>();

        AssetPropertyBase assetPropertyBase = new AdditionalProperties(parentAsset, propertyMap);
        AdditionalProperties additionalProperties = new AdditionalProperties(parentAsset, propertyMap);

        assertFalse(assetPropertyBase.equals("String Value"));
        assertTrue(assetPropertyBase.equals(assetPropertyBase));
        assertTrue(assetPropertyBase.equals(additionalProperties));
        assertTrue(additionalProperties.equals(assetPropertyBase));

        ElementType          differentElementType = new ElementType();
        Asset                differentAssetBean   = new Asset();

        differentElementType.setElementTypeName("DifferentTypeName");
        differentAssetBean.setQualifiedName("TestAssetName");
        differentAssetBean.setType(differentElementType);
    }


    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        assertTrue(new AdditionalProperties(null,
                                            getPropertyMap()).toString().contains("AdditionalProperties"));
    }

}

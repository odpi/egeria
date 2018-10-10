/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests the getters and setters of AssetPropertyBase.  This is an abstract class and so
 * it uses the MockAssetProperty object as the concrete class.
 */
public class TestAssetPropertyBase
{
    /**
     * Constructor
     */
    public TestAssetPropertyBase()
    {
    }


    /**
     * Test that AssetPropertyBase can handle a null parent asset
     */
    @Test public void testNullParentAsset()
    {
        AssetDescriptor      parentAsset = null;
        MockAssetProperty    template    = null;

        AssetPropertyBase assetPropertyBase = new MockAssetProperty(parentAsset);

        assertTrue(assetPropertyBase.getParentAsset() == null);
        assertTrue(assetPropertyBase.getParentAssetName().equals("<Unknown>"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("<Unknown>"));

        assetPropertyBase = new MockAssetProperty(parentAsset, template);

        assertTrue(assetPropertyBase.getParentAsset() == null);
        assertTrue(assetPropertyBase.getParentAssetName().equals("<Unknown>"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("<Unknown>"));
    }


    /**
     * Test that AssetPropertyBase can handle a populated parent asset.
     */
    @Test public void testValidParentAsset()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");
        assetBean.setQualifiedName("TestAssetName");
        assetBean.setType(elementType);

        AssetDescriptor parentAsset = new AssetSummary(assetBean);

        MockAssetProperty    template    = null;

        AssetPropertyBase assetPropertyBase = new MockAssetProperty(parentAsset);

        assertTrue(assetPropertyBase.getParentAsset() == parentAsset);
        assertTrue(assetPropertyBase.getParentAssetName().equals("TestAssetName"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("TestTypeName"));

        assetPropertyBase = new MockAssetProperty(parentAsset, template);

        assertTrue(assetPropertyBase.getParentAsset() == parentAsset);
        assertTrue(assetPropertyBase.getParentAsset().equals(parentAsset));
        assertTrue(assetPropertyBase.getParentAssetName().equals("TestAssetName"));
        assertTrue(assetPropertyBase.getParentAssetTypeName().equals("TestTypeName"));


        AssetDescriptor cloneParentAsset = new AssetSummary(assetBean);
        template = new MockAssetProperty(cloneParentAsset);

        assetPropertyBase = new MockAssetProperty(parentAsset, template);

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

        AssetPropertyBase assetPropertyBase = new MockAssetProperty(parentAsset);
        MockAssetProperty mockAssetProperty = new MockAssetProperty(parentAsset);

        assertFalse(assetPropertyBase.equals("String Value"));
        assertTrue(assetPropertyBase.equals(assetPropertyBase));
        assertTrue(assetPropertyBase.equals(mockAssetProperty));
        assertTrue(mockAssetProperty.equals(assetPropertyBase));
    }
}

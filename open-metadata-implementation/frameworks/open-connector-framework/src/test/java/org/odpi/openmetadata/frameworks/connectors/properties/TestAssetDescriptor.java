/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * Tests the getters and setters of AssetDescriptor.  This is an abstract class and so
 * it uses the MockAsset object as the concrete class.
 */
public class TestAssetDescriptor
{
    /**
     * Constructor
     */
    public TestAssetDescriptor()
    {
    }


    @Test public void testNullAssetBean()
    {
        MockAsset  mockAsset = new MockAsset((Asset)null);

        assertFalse(mockAsset.getAssetBean() == null);
        assertTrue(mockAsset.getAssetName().equals("<Unknown>"));
        assertTrue(mockAsset.getAssetTypeName().equals("<Unknown>"));
    }


    @Test public void testUnnamedAssetBean()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        assetBean.setType(elementType);

        MockAsset  mockAsset = new MockAsset(assetBean);

        assertTrue(mockAsset.getAssetBean().equals(assetBean));
        assertTrue(mockAsset.getAssetName().equals("<Unknown>"));
        assertTrue(mockAsset.getAssetTypeName().equals("<Unknown>"));

        elementType.setElementTypeName("");
        assetBean.setQualifiedName("");
        assetBean.setDisplayName("");
        assetBean.setType(elementType);

        mockAsset = new MockAsset(assetBean);

        assertTrue(mockAsset.getAssetName().equals("<Unknown>"));
        assertTrue(mockAsset.getAssetTypeName().equals("<Unknown>"));
    }


    @Test public void testQualifiedNameAssetBean()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");

        assetBean.setQualifiedName("TestQualifiedName");
        assetBean.setType(elementType);

        MockAsset  mockAsset = new MockAsset(assetBean);

        assertTrue(mockAsset.getAssetBean().equals(assetBean));
        assertTrue(mockAsset.getAssetName().equals("TestQualifiedName"));
        assertTrue(mockAsset.getAssetTypeName().equals("TestTypeName"));
    }


    @Test public void testDisplayNameAssetBean()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");

        assetBean.setDisplayName("TestDisplayName");
        assetBean.setType(elementType);

        MockAsset  mockAsset = new MockAsset(assetBean);

        assertTrue(mockAsset.getAssetBean().equals(assetBean));
        assertTrue(mockAsset.getAssetName().equals("TestDisplayName"));
        assertTrue(mockAsset.getAssetTypeName().equals("TestTypeName"));
    }


    @Test public void testAllNamedAssetBean()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");

        assetBean.setQualifiedName("TestQualifiedName");
        assetBean.setDisplayName("TestDisplayName");

        assetBean.setType(elementType);

        MockAsset  mockAsset = new MockAsset(assetBean);

        assertTrue(mockAsset.getAssetBean().equals(assetBean));
        assertTrue(mockAsset.getAssetName().equals("TestQualifiedName"));
        assertTrue(mockAsset.getAssetTypeName().equals("TestTypeName"));

        assetBean.setQualifiedName("");
        assetBean.setDisplayName("TestDisplayName");
        mockAsset = new MockAsset(assetBean);

        assertTrue(mockAsset.getAssetName().equals("TestDisplayName"));
        assertTrue(mockAsset.getAssetTypeName().equals("TestTypeName"));
    }


    @Test public void testNullClone()
    {
        MockAsset  mockAsset = new MockAsset((MockAsset)null);

        assertFalse(mockAsset.getAssetBean() == null);
        assertTrue(mockAsset.getAssetName().equals("<Unknown>"));
        assertTrue(mockAsset.getAssetTypeName().equals("<Unknown>"));
    }


    @Test public void testPopulatedClone()
    {
        ElementType     elementType = new ElementType();
        Asset           assetBean   = new Asset();

        elementType.setElementTypeName("TestTypeName");

        assetBean.setDisplayName("TestDisplayName");
        assetBean.setType(elementType);

        MockAsset  mockAssetClone = new MockAsset(assetBean);
        MockAsset  mockAsset = new MockAsset(mockAssetClone);

        assertTrue(mockAsset.getAssetBean().equals(assetBean));
        assertTrue(mockAsset.getAssetName().equals("TestDisplayName"));
        assertTrue(mockAsset.getAssetTypeName().equals("TestTypeName"));
    }
}

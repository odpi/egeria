/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * TypeDefTest provides test of TypeDef
 */
public class TypeDefTest
{
    @Test public void testConstructors()
    {
        TypeDefMock testObject = new TypeDefMock();


        new TypeDefMock(testObject);

        assertTrue(testObject.getCategory().equals(TypeDefCategory.UNKNOWN_DEF));
    }

    /**
     * Test that toString is overridden.
     */
    @Test public void testToString()
    {
        TypeDef typeDef = new TypeDefMock();
        assertTrue(typeDef.toString().contains("TypeDef"));
    }
}

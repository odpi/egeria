/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * TypeDefElementHeaderTest provides test of TypeDefElementHeader
 */
public class TypeDefElementHeaderTest
{
    @Test public void testConstructors()
    {
        TypeDefElementHeaderMock testObject = new TypeDefElementHeaderMock();

        assertTrue(testObject.getHeaderVersion() == 1);

        testObject.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION+1);

        TypeDefElementHeaderMock cloneObject = new TypeDefElementHeaderMock(testObject);

        assertTrue(cloneObject.getHeaderVersion() == TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION+1);
    }
}

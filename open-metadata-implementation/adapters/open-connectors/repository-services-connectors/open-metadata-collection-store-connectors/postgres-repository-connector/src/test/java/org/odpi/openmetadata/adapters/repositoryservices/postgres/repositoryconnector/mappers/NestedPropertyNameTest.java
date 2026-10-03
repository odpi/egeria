/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.adapters.repositoryservices.postgres.repositoryconnector.mappers;

import org.testng.annotations.Test;

import java.util.List;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/**
 * Verify the encoding of nested property names.
 */
public class NestedPropertyNameTest
{
    @Test
    public void testEscapeAndUnescapeRoundTrip()
    {
        String[] names = {"jobType", "tag:coco", "tag:Coco core", "a:b:c", "back\\slash", "ends\\", "\\:", "::", "", "0"};

        for (String name : names)
        {
            assertEquals(NestedPropertyName.unescape(NestedPropertyName.escape(name)), name, name);
        }
        assertNull(NestedPropertyName.escape(null));
        assertNull(NestedPropertyName.unescape(null));
    }


    @Test
    public void testEscapedForms()
    {
        assertEquals(NestedPropertyName.escape("jobType"), "jobType");
        assertEquals(NestedPropertyName.escape("tag:coco"), "tag\\:coco");
        assertEquals(NestedPropertyName.escape("a\\b"), "a\\\\b");
    }


    @Test
    public void testSplitOnlyOnUnescapedColons()
    {
        String path = "additionalProperties:" + NestedPropertyName.escape("tag:coco");

        assertEquals(NestedPropertyName.split(path), List.of("additionalProperties", "tag\\:coco"));
        assertEquals(NestedPropertyName.split("a:b:c"), List.of("a", "b", "c"));
        assertEquals(NestedPropertyName.split("plain"), List.of("plain"));
    }


    @Test
    public void testEscapedBackslashBeforeSeparator()
    {
        /*
         * A key ending in a backslash, followed by a nested key: the escaped backslash must not
         * swallow the separator that follows it.
         */
        String path = "outer:" + NestedPropertyName.escape("ends\\") + ":" + NestedPropertyName.escape("inner");
        List<String> parts = NestedPropertyName.split(path);

        assertEquals(parts.size(), 3);
        assertEquals(NestedPropertyName.unescape(parts.get(1)), "ends\\");
        assertEquals(NestedPropertyName.unescape(parts.get(2)), "inner");
    }


    @Test
    public void testLegacyNamesSplitAsBefore()
    {
        /*
         * Names stored before escaping was introduced contain bare colons and still split on them.
         */
        assertEquals(NestedPropertyName.split("additionalProperties:tag:coco"),
                     List.of("additionalProperties", "tag", "coco"));
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.CategoryHierarchyFVT;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CategoryHierarchyIT {
    @Test
    public void testCategoryHierarchy() {
        assertDoesNotThrow(() -> CategoryHierarchyFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
}

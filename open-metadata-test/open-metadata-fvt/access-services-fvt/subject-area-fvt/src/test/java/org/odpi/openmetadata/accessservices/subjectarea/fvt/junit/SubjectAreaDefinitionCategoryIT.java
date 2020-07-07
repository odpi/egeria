/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.SubjectAreaDefinitionCategoryFVT;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class SubjectAreaDefinitionCategoryIT {
    @Test
    public void testSubjectAreaDefinitionCategory() {
        assertDoesNotThrow(() -> SubjectAreaDefinitionCategoryFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
}

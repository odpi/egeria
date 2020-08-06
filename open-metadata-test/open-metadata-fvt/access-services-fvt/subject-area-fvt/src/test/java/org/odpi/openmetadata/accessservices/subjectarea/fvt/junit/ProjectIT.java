/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;

import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.ProjectFVT;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ProjectIT {
    @Test
    public void testProject() {
        assertDoesNotThrow(() -> ProjectFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
}

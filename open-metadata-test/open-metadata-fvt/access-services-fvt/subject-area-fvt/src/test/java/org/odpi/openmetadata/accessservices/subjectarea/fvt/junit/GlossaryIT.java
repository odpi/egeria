/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.GlossaryFVT;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class GlossaryIT {
    @Test
    public void testGlossary() {
        assertDoesNotThrow(() -> GlossaryFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
}

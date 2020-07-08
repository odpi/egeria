/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.TermFVT;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Disabled
public class TermIT {
    @Test
    public void testTerm() {
        assertDoesNotThrow(() -> TermFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
}

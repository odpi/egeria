/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.CheckSerializationFVT;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class CheckSerializationT {
    @ParameterizedTest
    @ValueSource(strings = {"serverinmem","servergraph"})
    public void testRelationships(String server) {
        assertDoesNotThrow(() -> CheckSerializationFVT.runIt("https://localhost:10443", server, "garygeeke"));
    }
}

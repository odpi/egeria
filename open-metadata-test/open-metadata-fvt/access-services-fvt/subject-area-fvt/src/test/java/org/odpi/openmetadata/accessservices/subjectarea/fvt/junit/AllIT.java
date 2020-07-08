/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.subjectarea.fvt.junit;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.odpi.openmetadata.accessservices.subjectarea.fvt.*;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Execution(ExecutionMode.SAME_THREAD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AllIT {

    @Test
    @Order(1)
    public void testGlossary() {
        assertDoesNotThrow(() -> GlossaryFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
    @Test
    @Order(2)
    public void testTerm() {
        assertDoesNotThrow(() -> TermFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
    @Test
    @Order(3)
    public void testCategory() {
        assertDoesNotThrow(() -> CategoryFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }

    @Test
    @Order(4)
    public void testCategoryHierarchy() {
        assertDoesNotThrow(() -> CategoryHierarchyFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
    @Test
    @Order(5)
    public void testRelationships() {
        assertDoesNotThrow(() -> RelationshipsFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
    @Test
    @Order(6)
    public void testProject() {
        assertDoesNotThrow(() -> ProjectFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
    @Test
    @Order(7)
    public void testSubjectAreaDefinitionCategory() {
        assertDoesNotThrow(() -> SubjectAreaDefinitionCategoryFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
    @Test
    @Order(8)
    public void testGraph() {
        assertDoesNotThrow(() -> GraphFVT.runIt("https://localhost:10443", "fvtserver", "garygeeke"));
    }
}
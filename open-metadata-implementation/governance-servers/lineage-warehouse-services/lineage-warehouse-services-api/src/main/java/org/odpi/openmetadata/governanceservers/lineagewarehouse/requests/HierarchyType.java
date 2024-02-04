/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.requests;

public enum HierarchyType {
    DOWNWARD(0, "Downward", "Downward elements from the graph"),
    UPWARD(1, "Upward", "Upward elements from the graph"),
    ALL(2, "All elements from the graph", "All elements");

    private final int ordinal;
    private final String name;
    private final String description;

    HierarchyType(int ordinal, String name, String description) {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }
}

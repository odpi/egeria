/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

public enum GraphName {
    MAIN("main"),
    BUFFER("buffer"),
    HISTORY("history"),
    MOCK("mock");

    private final String value;

    GraphName(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }

    public static GraphName fromString(String text) {
        for (GraphName value : GraphName.values()) {
            if (value.value.equals(text)) {
                return value;
            }
        }
        return null;
    }
}


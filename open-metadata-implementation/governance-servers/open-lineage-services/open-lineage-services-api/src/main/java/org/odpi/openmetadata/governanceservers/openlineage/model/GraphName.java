/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

public enum GraphName {
    MAIN("main"),
    BUFFER("buffer"),
    HISTORY("history"),
    MOCK("mock");

    private final String text;

    GraphName(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }

    public static GraphName fromString(String text) {
        for (GraphName value : GraphName.values()) {
            if (value.text.equals(text)) {
                return value;
            }
        }
        return null;
    }
}


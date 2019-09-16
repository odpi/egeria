/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

public enum Scope {
    SOURCE_AND_DESTINATION("source-and-destination"),
    END_TO_END("end-to-end"),
    ULTIMATE_SOURCE("ultimate-source"),
    ULTIMATE_DESTINATION("ultimate-destination"),
    GLOSSARY("glossary");

    private final String text;

    Scope(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }

    public static Scope fromString(String text) {
        for (Scope value : Scope.values()) {
            if (value.text.equals(text)) {
                return value;
            }
        }
        return null;
    }
}
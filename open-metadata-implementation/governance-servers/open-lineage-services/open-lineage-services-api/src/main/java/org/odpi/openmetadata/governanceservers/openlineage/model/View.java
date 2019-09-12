/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.model;

public enum View {
    TABLE_VIEW("table-view"),
    COLUMN_VIEW("column-view");

    private final String text;

    View(String s) {
        this.text = s;
    }

    public String getText() {
        return text;
    }

    public static View fromString(String text) {
        for (View value : View.values()) {
            if (value.text.equalsIgnoreCase(text)) {
                return value;
            }
        }
        return null;
    }
}


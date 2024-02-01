/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.viewservices.assetcatalog.beans;

public enum Scope {
    END_TO_END("end-to-end"),
    ULTIMATE_SOURCE("ultimate-source"),
    ULTIMATE_DESTINATION("ultimate-destination"),
    VERTICAL("vertical");

    private final String value;

    Scope(String s) {
        this.value = s;
    }

    public String getValue() {
        return value;
    }

    public static Scope fromString(String text) {
        for (Scope value : Scope.values()) {
            if (value.value.equals(text)) {
                return value;
            }
        }
        return null;
    }
}

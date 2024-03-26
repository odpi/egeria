/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.lineagewarehouse.model;

/**
 * Scope characterizes the type of lineage query request.
 */
public enum Scope
{
    /**
     * end-to-end
     */
    END_TO_END("end-to-end"),

    /**
     * ultimate-source
     */
    ULTIMATE_SOURCE("ultimate-source"),

    /**
     * ultimate-destination
     */
    ULTIMATE_DESTINATION("ultimate-destination"),

    /**
     * vertical
     */
    VERTICAL("vertical");

    private final String value;

    Scope(String s) {
        this.value = s;
    }

    /**
     * Retrieve the enum value as a string.
     *
     * @return string
     */
    public String getValue() {
        return value;
    }


    /**
     * Convert a string to an enum value - or null if no match occurs.
     *
     * @param text string, typically passed on a REST call
     * @return associated enum
     */
    public static Scope fromString(String text)
    {
        for (Scope value : Scope.values())
        {
            if (value.value.equals(text))
            {
                return value;
            }
        }

        return null;
    }
}

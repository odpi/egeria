/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import java.util.StringJoiner;

public class QueryBuilder {
    private final StringJoiner j = new StringJoiner("&", "?", "");
    private final static String PARAM_FORMAT = "%s=%s";

    public QueryBuilder() {}

    public QueryBuilder addParam(Object key, Object value) {
        if (value != null) {
            this.j.add(String.format(PARAM_FORMAT, key, value));
        }
        return this;
    }

    @Override
    public String toString() {
        return this.j.toString();
    }
}

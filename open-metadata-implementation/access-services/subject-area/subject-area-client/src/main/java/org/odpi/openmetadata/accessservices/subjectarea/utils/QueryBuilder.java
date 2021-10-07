/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;

public class QueryBuilder {
    private final Map<Object, Object> paramMap = new LinkedHashMap<>();
    private final static String PARAM_FORMAT = "%s=%s";

    public QueryBuilder() {}

    public QueryBuilder addParam(Object key, Object value) {
        if (key != null && value != null) {
            paramMap.put(key, value);
        }

        return this;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner("&", "?", "");
        paramMap.forEach((key, value) -> joiner.add(String.format(PARAM_FORMAT, key, value)));

        return joiner.toString();
    }
}

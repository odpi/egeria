/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import java.util.StringJoiner;

public class QueryBuilder {
    private final StringJoiner joiner = new StringJoiner("&", "?", "");

    public QueryBuilder() {}

    public QueryBuilder addParam(Object key, Object value) {
        if (key != null && value != null) {
            join(key, value);
        }

        return this;
    }

    public QueryBuilder addParams(QueryParams queryParams) {
        if (queryParams != null) {
            queryParams
                    .getParamMap()
                    .forEach(this::join);
        }

        return this;
    }

    private void join(Object key, Object value) {
        joiner.add(key + "=" + value);
    }

    @Override
    public String toString() {
        return joiner.toString();
    }
}

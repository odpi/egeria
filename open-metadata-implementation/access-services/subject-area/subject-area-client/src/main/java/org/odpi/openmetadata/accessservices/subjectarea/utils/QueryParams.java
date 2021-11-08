/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.utils;

import java.util.LinkedHashMap;
import java.util.Map;

public class QueryParams {
    private static final String KEY_EXACT_VALUE = "exactValue";
    private static final String KEY_IGNORE_CASE = "ignoreCase";
    private static final String KEY_ONLY_TOP = "onlyTop";

    private final Map<String, Object> paramMap = new LinkedHashMap<>();

    public QueryParams() {}

    Map<String, Object> getParamMap() {
        return Map.copyOf(paramMap);
    }

    public QueryParams setExactValue(boolean exactValue) {
        paramMap.put(KEY_EXACT_VALUE, exactValue);
        return this;
    }

    public QueryParams setIgnoreCase(boolean ignoreCase) {
        paramMap.put(KEY_IGNORE_CASE, ignoreCase);
        return this;
    }

    public QueryParams setOnlyTop(boolean onlyTop) {
        paramMap.put(KEY_ONLY_TOP, onlyTop);
        return this;
    }
}

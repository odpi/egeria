/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Generic filter, to specify search criteria using name/value pairs.
 */
@JsonAutoDetect(getterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, setterVisibility= JsonAutoDetect.Visibility.PUBLIC_ONLY, fieldVisibility= JsonAutoDetect.Visibility.NONE)
@JsonSerialize(include=JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)

public class SearchFilter {
    public static final String PARAM_TYPE = "type";
    public static final String PARAM_NAME = "name";
    public static final String PARAM_SUPERTYPE = "supertype";
    public static final String PARAM_NOT_SUPERTYPE = "notsupertype";

    /**
     * to specify whether the result should be sorted? If yes, whether asc or desc.
     */
    public enum SortType { NONE, ASC, DESC }

   // private MultivaluedMap<String, String> params     = null;
    private long                startIndex = 0;
    private long                maxRows    = Long.MAX_VALUE;
    private boolean             getCount   = true;
    private String              sortBy     = null;
    private SortType            sortType   = null;
//
//    public SearchFilter() {
//        setParams(null);
//    }

//    public SearchFilter(MultivaluedMap<String, String> params) {
//        setParams(params);
//    }
//
//    public MultivaluedMap<String, String> getParams() {
//        return params;
//    }
//
//    public void setParams(MultivaluedMap<String, String> params) {
//        this.params = params;
//    }
//
//    public String getParam(String name) {
//        String ret = null;
//
//        if (name != null && params != null) {
//            ret = params.getFirst(name);
//        }
//
//        return ret;
//    }
//
//    public List<String> getParams(String name) {
//        List<String> ret = null;
//
//        if (name != null && params != null) {
//            ret = params.get(name);
//        }
//
//        return ret;
//    }
//
//    public void setParam(String name, String value) {
//        if (name != null) {
//            if (params == null) {
//                params = new MultivaluedMapImpl();
//            }
//
//            params.add(name, value);
//        }
//    }
//
//    public void setParam(String name, List<String> values) {
//        if (name != null) {
//            if (params == null) {
//                params = new MultivaluedMapImpl();
//            }
//            params.put(name, values);
//        }
//    }

    public long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    public long getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(long maxRows) {
        this.maxRows = maxRows;
    }

    public boolean isGetCount() {
        return getCount;
    }

    public void setGetCount(boolean getCount) {
        this.getCount = getCount;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public SortType getSortType() {
        return sortType;
    }

    public void setSortType(SortType sortType) {
        this.sortType = sortType;
    }
}

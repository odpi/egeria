/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.ObjectPrinter;

/**
 * Represents the metadata related to a page of results or relationships.
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Paging extends ObjectPrinter {

    public static final String IGC_TYPE_ID = "paging";

    /**
     * Specifies the total number of items that exist across all pages.
     */
    protected Integer numTotal;

    /**
     * Specifies the URL to the next page of results, within a given IGC environment. If there is no
     * next page of results, this will be null.
     * <br><br>
     * (In other words: if this is null, you are on the last page of results.)
     */
    protected String next;

    /**
     * Specifies the URL to the previous page of results, within a given IGC environment. If there is no
     * previous page of results, this will be null.
     * <br><br>
     * (In other words: if this is null, you are on the first page of results.)
     */
    protected String previous;

    /**
     * Specifies the number of results within each page.
     */
    protected Integer pageSize;

    /**
     * Specifies the numeric index at which this page of results ends.
     */
    protected Integer end;

    /**
     * Specifies the numeric index at which this page of results starts.
     */
    protected Integer begin;

    public Paging() {
        this.numTotal = 0;
        this.next = null;
        this.previous = null;
        this.pageSize = 0;
        this.end = 0;
        this.begin = 0;
    }

    /**
     * Creates a new "full" Paging object (without any previous or next pages).
     *
     * @param numTotal total number of objects that this "page" represents containing
     */
    public Paging(Integer numTotal) {
        this();
        this.numTotal = numTotal;
        this.pageSize = numTotal;
        this.end = numTotal;
    }

    /** @see #numTotal */ @JsonProperty("numTotal") public Integer getNumTotal() { return this.numTotal; }
    /** @see #numTotal */ @JsonProperty("numTotal") public void setNumTotal(Integer numTotal) { this.numTotal = numTotal; }

    /** @see #next */ @JsonProperty("next") public String getNextPageURL() { return this.next; }
    /** @see #next */ @JsonProperty("next") public void setNextPageURL(String next) { this.next = next; }

    /** @see #previous */ @JsonProperty("previous") public String getPreviousPageURL() { return this.previous; }
    /** @see #previous */ @JsonProperty("previous") public void setPreviousPageURL(String previous) { this.previous = previous; }

    /** @see #pageSize */ @JsonProperty("pageSize") public Integer getPageSize() { return this.pageSize; }
    /** @see #pageSize */ @JsonProperty("pageSize") public void setPageSize(Integer pageSize) { this.pageSize = pageSize; }

    /** @see #end */ @JsonProperty("end") public Integer getEndIndex() { return this.end; }
    /** @see #end */ @JsonProperty("end") public void setEndIndex(Integer end) { this.end = end; }

    /** @see #begin */ @JsonProperty("begin") public Integer getBeginIndex() { return this.begin; }
    /** @see #begin */ @JsonProperty("begin") public void setBeginIndex(Integer begin) { this.begin = begin; }

    /**
     * Returns true iff there are more (unretrieved) pages for the paging that this object represents.
     *
     * @return Boolean
     */
    public Boolean hasMore() {
        return (this.numTotal > this.end);
    }

}

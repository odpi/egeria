/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.common;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.odpi.openmetadata.adapters.repositoryservices.igc.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.ObjectPrinter;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.Paging;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.Reference;

import java.util.ArrayList;

/**
 * Provides a standard class for any relationship in IGC, by including 'paging' details and 'items' array.
 * <br><br>
 * Used in POJOs, this class can be defined as the type of any relationship attribute, eg.:<br>
 *   public ReferenceList assigned_assets;
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceList extends ObjectPrinter {

    /**
     * The 'paging' property of a ReferenceList gives the aspects related to paging (eg. number of items in the page,
     * total number for the relationship, etc).
     * <br><br>
     * Will be a single {@link Paging} object.
     */
    protected Paging paging = new Paging();

    /**
     * The 'items' property of a ReferenceList gives the actual items that are part of this particular page.
     * <br><br>
     * Will be a ArrayList of {@link Reference} objects.
     */
    protected ArrayList<Reference> items = new ArrayList<Reference>();

    /** @see #paging */ @JsonProperty("paging") public Paging getPaging() { return this.paging; }
    /** @see #paging */ @JsonProperty("paging") public void setPaging(Paging paging) { this.paging = paging; }

    /** @see #items */ @JsonProperty("items") public ArrayList<Reference> getItems() { return this.items; }
    /** @see #items */ @JsonProperty("items") public void setItems(ArrayList<Reference> items) { this.items = items; }

    /**
     * Returns true iff there are more (unretrieved) pages for the relationships that this object represents.
     *
     * @return Boolean
     */
    public Boolean hasMorePages() {
        return (this.paging.hasMore());
    }

    /**
     * Retrieve all pages of relationships that this object represents.
     *
     * @param igcrest the IGCRestClient connection to use to retrieve the relationships
     */
    public void getAllPages(IGCRestClient igcrest) {
        this.items = igcrest.getAllPages(this.items, this.paging);
        this.paging = new Paging(this.items.size());
    }

}

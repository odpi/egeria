/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.search;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

/**
 * Manages the criteria to use for sorting search results from IGC.
 */
public class IGCSearchSorting {

    private JsonNodeFactory nf = JsonNodeFactory.instance;

    private String property;
    private Boolean ascending;

    /**
     * Creates a new search sorting criteria directly. For example, the following would sort results by "name"
     * in descending order:
     * <ul>
     *     <li>property: "name"</li>
     *     <li>ascending: false</li>
     * </ul>
     *
     * @param property the property of an asset type to search against
     * @param ascending whether to sort results in ascending order (true) or descending order (false)
     */
    public IGCSearchSorting(String property, Boolean ascending) {
        this.property = property;
        this.ascending = ascending;
    }

    /**
     * Creates a new search sorting criteria directly, sorting by the provided property in ascending order.
     *
     * @param property the property of an asset type to search against
     */
    public IGCSearchSorting(String property) {
        this.property = property;
        this.ascending = true;
    }

    public String getProperty() { return this.property; }
    public void setProperty(String property) { this.property = property; }

    public Boolean getAscending() { return this.ascending; }
    public void setAscending(Boolean ascending) { this.ascending = ascending; }

    /**
     * Returns the JSON object representing the condition.
     *
     * @return ObjectNode
     */
    public ObjectNode getSortObject() {
        ObjectNode sortObj = nf.objectNode();
        sortObj.set("property", nf.textNode(getProperty()));
        sortObj.set("ascending", nf.booleanNode(getAscending()));
        return sortObj;
    }

    /**
     * Returns an IGCSearchSorting equivalent to the provided SequencingOrder, so long as the provided
     * sequencingOrder is not one of [ PROPERTY_ASCENDING, PROPERTY_DESCENDING ] (because these must
     * be explicitly mapped on a type-by-type basis).
     *
     * @param sequencingOrder the non-property SequencingOrder to create an IGC sort order from
     * @return IGCSearchSorting
     */
    public static IGCSearchSorting sortFromNonPropertySequencingOrder(SequencingOrder sequencingOrder) {
        IGCSearchSorting sort = null;
        if (sequencingOrder != null) {
            switch(sequencingOrder) {
                case GUID:
                    sort = new IGCSearchSorting("_id");
                    break;
                case CREATION_DATE_RECENT:
                    sort = new IGCSearchSorting("created_on", false);
                    break;
                case CREATION_DATE_OLDEST:
                    sort = new IGCSearchSorting("created_on", true);
                    break;
                case LAST_UPDATE_RECENT:
                    sort = new IGCSearchSorting("modified_on", false);
                    break;
                case LAST_UPDATE_OLDEST:
                    sort = new IGCSearchSorting("modified_on", true);
                    break;
            }
        }
        return sort;
    }

}

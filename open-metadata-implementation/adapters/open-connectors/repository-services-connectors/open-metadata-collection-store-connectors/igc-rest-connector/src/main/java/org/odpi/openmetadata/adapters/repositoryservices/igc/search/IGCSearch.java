/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.search;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * Manages the criteria to use for running a search against IGC.
 */
public class IGCSearch {

    private JsonNodeFactory nf = JsonNodeFactory.instance;

    private ArrayNode types;
    private ArrayNode properties;

    private IGCSearchConditionSet conditions;
    private int pageSize = 100;

    private boolean devGlossary = false;

    public IGCSearch() {
        this.types = nf.arrayNode();
        this.properties = nf.arrayNode();
    }

    /**
     * Creates a new search for all assets of the provided type, retrieving only their name.
     *
     * @param type the type of assets to retrieve
     */
    public IGCSearch(String type) {
        this();
        addType(type);
        addProperty("name");
    }

    /**
     * Creates a new search for assets of the provided type, based on the provided criteria, retrieving by default only
     * their name.
     *
     * @param type the type of assets to retrieve
     * @param conditions the set of conditions to use as search criteria
     */
    public IGCSearch(String type, IGCSearchConditionSet conditions) {
        this(type);
        addConditions(conditions);
    }

    /**
     * Creates a new search for assets of the provided type, based on the provided criteria, retrieving all of the
     * properties specified for each result.
     *
     * @param type the type of assets to retrieve
     * @param properties the properties to retrieve for each asset
     * @param conditions the set of conditions to use as search criteria
     */
    public IGCSearch(String type, String[] properties, IGCSearchConditionSet conditions) {
        this();
        addType(type);
        for (String property : properties) {
            addProperty(property);
        }
        addConditions(conditions);
    }

    /**
     * Add an asset type for which to search.
     *
     * @param type the name of the asset type to include in the search
     */
    public void addType(String type) {
        this.types.add(type);
    }

    /**
     * Add a property for which to search.
     *
     * @param property the name of the property to include in the search
     */
    public void addProperty(String property) {
        this.properties.add(property);
    }

    /**
     * Adds a set of conditions to use for the search.
     *
     * @param conditions the set of conditions to add to the search criteria
     */
    public void addConditions(IGCSearchConditionSet conditions) {
        this.conditions = conditions;
    }

    /**
     * Set the number of results to include in each page.
     *
     * @param size the number of results to include in each page
     */
    public void setPageSize(int size) {
        this.pageSize = size;
    }

    /**
     * Set whether to search the development glossary (true) or published glossary (false).
     *
     * @param on set to true to search the development glossary, or false (default) to search published
     */
    public void setDevGlossary(boolean on) {
        this.devGlossary = on;
    }

    /**
     * Retrieves the query string for this search object.
     *
     * @return JsonNode - the JSON structure representing the query string
     */
    public JsonNode getQuery() {
        ObjectNode query = nf.objectNode();
        query.set("types", types);
        query.set("properties", properties);
        query.set("pageSize", nf.numberNode(pageSize));
        if (conditions != null && conditions.size() > 0) {
            query.set("where", conditions.getConditionSetObject());
        }
        if (devGlossary) {
            query.set("workflowMode", nf.textNode("draft"));
        }
        return query;
    }

    public String toString() {
        return getQuery().toString();
    }

}

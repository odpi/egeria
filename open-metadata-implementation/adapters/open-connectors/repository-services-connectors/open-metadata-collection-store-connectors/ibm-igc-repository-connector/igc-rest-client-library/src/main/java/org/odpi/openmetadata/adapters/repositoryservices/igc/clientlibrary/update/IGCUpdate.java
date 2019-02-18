/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.update;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages the criteria to use for updating assets in IGC.
 */
public class IGCUpdate {

    private JsonNodeFactory nf = JsonNodeFactory.instance;

    private String ridToUpdate;
    private Map<String, List<String>> relationships;
    private Map<String, String> exclusiveRelationships;
    private UpdateMode relationshipUpdateMode;

    public enum UpdateMode { APPEND, REPLACE }

    public IGCUpdate(String ridToUpdate) {
        this.ridToUpdate = ridToUpdate;
        this.relationships = new HashMap<>();
        this.exclusiveRelationships = new HashMap<>();
    }

    /**
     * Adds a relationship on a property that can only have one such relationship (so it will replace any existing
     * relationship on that property).
     *
     * @param propertyName name of the IGC entity's exclusive relationship property
     * @param relatedRid Repository ID (RID) of the related entity
     */
    public void addExclusiveRelationship(String propertyName, String relatedRid) {
        exclusiveRelationships.put(propertyName, relatedRid);
    }

    /**
     * Adds a relationship on a property that can have many relationships. Decide whether to replace any existing
     * relationships on the property or to append to the existing relationships using setRelationshipUpdateMode.
     *
     * @param propertyName name of the IGC entity's relationship property
     * @param relatedRid Repository ID (RID) of the related entity
     * @see #setRelationshipUpdateMode(UpdateMode)
     */
    public void addRelationship(String propertyName, String relatedRid) {
        if (!relationships.containsKey(propertyName)) {
            relationships.put(propertyName, new ArrayList<>());
        }
        relationships.get(propertyName).add(relatedRid);
    }

    /**
     * Sets the update mode that should be used for the non-exclusive relationship properties.
     *
     * @param updateMode
     * @see UpdateMode
     * @see #addRelationship(String, String)
     */
    public void setRelationshipUpdateMode(UpdateMode updateMode) {
        relationshipUpdateMode = updateMode;
    }

    /**
     * Retrieves the RID of the asset that is being updated.
     *
     * @return String
     */
    public String getRidToUpdate() { return this.ridToUpdate; }

    /**
     * Retrieves the update string for this update object.
     *
     * @return JsonNode - the JSON structure representing the update operation
     */
    public JsonNode getUpdate() {
        ObjectNode update = nf.objectNode();
        for (String propertyName : relationships.keySet()) {
            List<String> rids = relationships.get(propertyName);
            ArrayNode ridArray = nf.arrayNode(rids.size());
            for (String rid : rids) {
                ridArray.add(rid);
            }
            ObjectNode items = nf.objectNode();
            items.set("items", ridArray);
            if (relationshipUpdateMode != null) {
                TextNode mode = null;
                if (relationshipUpdateMode == UpdateMode.REPLACE) {
                    mode = nf.textNode("replace");
                } else {
                    mode = nf.textNode("add");
                }
                items.set("mode", mode);
            }
            update.set(propertyName, items);
        }
        if (!exclusiveRelationships.isEmpty()) {
            for (String propertyName : exclusiveRelationships.keySet()) {
                update.set(propertyName, nf.textNode(exclusiveRelationships.get(propertyName)));
            }
        }
        return update;
    }

    /**
     * Retrieves a printable version of the update object.
     *
     * @return String
     */
    @Override
    public String toString() {
        return getUpdate().toString();
    }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.flipkart.zjsonpatch.DiffFlags;
import com.flipkart.zjsonpatch.JsonDiff;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.IGCRestClient;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.repositoryconnector.model.OMRSStub;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

/**
 * A class to capture differences between IGC objects through the JSON Patch notation.
 * (https://tools.ietf.org/html/rfc6902)
 */
public class ChangeSet {

    private static final Logger log = LoggerFactory.getLogger(ChangeSet.class);

    private ObjectMapper objectMapper;
    private IGCRestClient igcRestClient;

    private JsonNode patch;

    private HashMap<String, List<Change>> changesByProperty;

    /**
     * Create a new JSON Patch based on the provided asset details and stub.
     *
     * @param igcRestClient REST API connectivity to an IGC environment
     * @param asset the IGC asset (as a POJO) giving the most up-to-date definition of the asset
     * @param stub the OMRS stub giving the last-state of the asset (when an event was last triggered for it)
     */
    public ChangeSet(IGCRestClient igcRestClient, Reference asset, OMRSStub stub) {

        this.objectMapper = new ObjectMapper();
        this.changesByProperty = new HashMap<>();
        this.igcRestClient = igcRestClient;

        // If we receive a null stub (eg. a new entity without any stub)
        if (stub == null) {
            // ... initialise an empty stub payload that we can (hopefully) use for the comparison
            stub = new OMRSStub();
            stub.setPayload("{}");
        }

        // Calculate the delta between the latest version and the previous saved stub
        try {
            EnumSet<DiffFlags> flags = DiffFlags.dontNormalizeOpIntoMoveAndCopy().clone();
            JsonNode stubPayload = objectMapper.readTree(stub.getPayload());
            JsonNode currentAsset = objectMapper.readTree(this.igcRestClient.getValueAsJSON(asset));
            this.patch = JsonDiff.asJson(
                    stubPayload,
                    currentAsset,
                    flags
            );
            log.debug("Found the following changes: {}", this.patch.toString());
            ArrayNode changes = (ArrayNode) this.patch;
            for (int i = 0; i < changes.size(); i++) {
                JsonNode change = changes.get(i);
                // Only add the change if it refers to a non-paging node (ie. it is /items/ or a basic property)
                // (Otherwise we end up with many changes for the same set of relationships)
                String changePath = change.path("path").asText();
                Change theChange = null;
                if (changePath.contains("/items/")) {
                    // If we already have an object, use it directly
                    if (change.path("value").getNodeType().equals(JsonNodeType.OBJECT)) {
                        theChange = new Change(change, stubPayload);
                    // Otherwise see if there is a set of individual changes we should consolidate (where _id will
                    // always be different if the relationship itself has changed)
                    } else if (changePath.endsWith("_id")) {
                        // Consolidate /items/n/_id entries into objects rather than separate changes
                        ObjectNode consolidatedChange = (ObjectNode) change;
                        String indexPath = changePath.substring(0, changePath.indexOf("/_id"));
                        consolidatedChange.put("path", indexPath);
                        // Add the complete Reference (_id, _name, _type, _url) as an object value
                        // Consolidate this new object from the current asset, not the stub
                        JsonNode relatedAsset = getObjectFromIndex(indexPath, currentAsset);
                        consolidatedChange.set("value", relatedAsset);
                        theChange = new Change(consolidatedChange, stubPayload);
                    }
                } else if (!changePath.contains("/paging/")) {
                    // Otherwise add simple changes, but skip any paging information
                    theChange = new Change(change, stubPayload);
                }
                if (theChange != null) {
                    String igcProperty = theChange.getIgcPropertyName();
                    if (!this.changesByProperty.containsKey(igcProperty)) {
                        this.changesByProperty.put(igcProperty, new ArrayList<>());
                    }
                    this.changesByProperty.get(igcProperty).add(theChange);
                }
            }

        } catch (IOException e) {
            log.error("Unable to parse JSON for diff operation: {}, {}", asset, stub, e);
        }

    }

    /**
     * Retrieve the set of IGC property names that have some change.
     *
     * @return {@code Set<String>}
     */
    public Set<String> getChangedProperties() { return this.changesByProperty.keySet(); }

    /**
     * Retrieve the list of changes for the provided IGC property name.
     *
     * @param property name of the IGC property for which to retrieve changes
     * @return {@code List<Change>}
     */
    public List<Change> getChangesForProperty(String property) { return this.changesByProperty.get(property); }

    /**
     * Retrieve an object from the specified path (including index), from the provided asset JSON.
     *
     * @param objectPath the ../items/n path (including the index number)
     * @param asset the JSON representation of the asset from which to retrieve the path
     * @return JsonNode
     */
    private JsonNode getObjectFromIndex(String objectPath, JsonNode asset) {
        String arrayIndex = objectPath.substring(objectPath.lastIndexOf('/') + 1);
        String listPath = objectPath.substring(1, objectPath.indexOf("/items"));
        int idx = Integer.parseInt(arrayIndex);
        ArrayNode references = (ArrayNode) asset.path(listPath).path("items");
        return references.get(idx);
    }

    /**
     * A sub-class to capture individual differences.
     */
    public class Change {

        private JsonNode from;
        private String op;
        private String path;
        private JsonNode value;

        public Change(JsonNode patch, JsonNode from) {
            this.from = from;
            this.op = patch.get("op").asText();
            this.path = patch.get("path").asText();
            this.value = patch.get("value");
        }

/*        public Change(String op, String path, JsonNode value) {
            this.op = op;
            this.path = path;
            this.value = value;
        } */

        /**
         * Retrieve the 'op'eration indicated by the JSON Patch entry. Will be one of "replace", "add" or "remove".
         *
         * @return String
         */
        public String getOp() { return this.op; }

        /**
         * Retrieve the simple IGC property name indicated by the JSON Patch entry. The property name will be trimmed
         * from the full path of the JSON Patch entry.
         *
         * @return String
         * @see #getIgcPropertyPath()
         */
        public String getIgcPropertyName() {
            String[] aTokens = this.path.split("/");
            if (aTokens.length > 1) {
                return aTokens[1];
            } else {
                log.error("Unable to find any property in path: {}", this.path);
                return null;
            }
        }

        /**
         * Retrieve the full IGC property 'path' indicated by the JSON Patch entry. This will include the full path to
         * the property, including any object sub-entries, array index notation, etc. In general you probably want just
         * the IGC property name itself, accessible through the getIgcPropertyName method.
         *
         * @return String
         * @see #getIgcPropertyName()
         */
        public String getIgcPropertyPath() {
            return this.path;
        }

        /**
         * Retrieve the changed value indicated by the JSON Patch entry.
         *
         * @param referenceListProperties list of properties of the changed asset that are reference lists
         * @return Object
         */
        public Object getNewValue(List<String> referenceListProperties) {
            return getValueFromJSON(this.value, referenceListProperties, getIgcPropertyPath());
        }

        /**
         * Retrieve the original value that is being removed or replaced by the JSON Patch entry. (For 'add' operations
         * will return null.)
         *
         * @param referenceListProperties list of properties of the changed asset that are reference lists
         * @return Object
         */
        public Object getOldValue(List<String> referenceListProperties) {

            Object oldValue = null;

            // If the operation is an 'add', there will not be any old value so we should simply return null
            if (!getOp().equals("add")) {
                String path = getIgcPropertyPath();
                if (path.contains("/items/")) {
                    JsonNode obj = getObjectFromIndex(path, this.from);
                    oldValue = getValueFromJSON(obj, referenceListProperties, path);
                } else {
                    oldValue = getValueFromJSON(this.from.path(path), referenceListProperties, path);
                }
            }

            return oldValue;

        }

        /**
         * Retrieve an actual value from the provided JSON node.
         *
         * @param node the JSON node from which to retrieve a value
         * @param referenceListProperties at list of all IGC properties that are ReferenceLists
         * @param path the JSON path at which the node is found
         * @return Object
         */
        private Object getValueFromJSON(JsonNode node,
                                        List<String> referenceListProperties,
                                        String path) {

            Object value = null;
            JsonNodeType jsonType = node.getNodeType();
            switch (jsonType) {
                // TODO: how to translate an array?
                case NULL:
                    value = null;
                    break;
                case BOOLEAN:
                    value = node.asBoolean();
                    break;
                case NUMBER:
                    value = node.asDouble();
                    break;
                case OBJECT:
                    // If an object, must be a Reference (or ReferenceList) -- read it in as one
                    // Note that if the change path ends with /items/0 then we should only read a Reference, not
                    // a list, as the JSON Patch is only giving us a singular Reference (the paging we need for
                    // the list is split off on other change operations)
                    if (referenceListProperties.contains(getIgcPropertyName()) && !path.contains("/items/")) {
                        value = igcRestClient.readJSONIntoReferenceList(node.toString());
                    } else {
                        value = igcRestClient.readJSONIntoPOJO(node.toString());
                    }
                    break;
                case STRING:
                    value = node.asText();
                    break;
                default:
                    log.warn("Unhandled value type '{}': {}", jsonType, node);
                    break;
            }

            return value;

        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Change: {");
            sb.append(" op=");
            sb.append(this.op);
            sb.append(", path=");
            sb.append(this.path);
            sb.append(", value=");
            sb.append(this.value.toString());
            sb.append("}");
            return sb.toString();
        }

    }

}

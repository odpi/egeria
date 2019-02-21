/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeType;
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
            this.patch = JsonDiff.asJson(
                    objectMapper.readTree(stub.getPayload()),
                    objectMapper.readTree(this.igcRestClient.getValueAsJSON(asset)),
                    flags
            );
            log.debug("Found the following changes: {}", this.patch.toString());
            ArrayNode changes = (ArrayNode) this.patch;
            for (int i = 0; i < changes.size(); i++) {
                JsonNode change = changes.get(i);
                // Only add the change if it refers to a non-paging node (ie. it is /items/ or a basic property)
                // (Otherwise we end up with many changes for the same set of relationships)
                if (!change.path("path").asText().contains("/paging/")) {
                    Change theChange = new Change(change);
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
     * A sub-class to capture individual differences.
     */
    public class Change {

        private String op;
        private String path;
        private JsonNode value;

        public Change(JsonNode patch) {
            this.op = patch.get("op").asText();
            this.path = patch.get("path").asText();
            this.value = patch.get("value");
        }

        public Change(String op, String path, JsonNode value) {
            this.op = op;
            this.path = path;
            this.value = value;
        }

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

            Object actualValue = null;

            // Otherwise, look for a simple type
            JsonNodeType jsonType = this.value.getNodeType();
            switch (jsonType) {
                // TODO: how to translate an array?
                case NULL:
                    actualValue = null;
                    break;
                case BOOLEAN:
                    actualValue = this.value.asBoolean();
                    break;
                case NUMBER:
                    actualValue = this.value.asDouble();
                    break;
                case OBJECT:
                    // If an object, must be a Reference (or ReferenceList) -- read it in as one
                    // Note that if the change path ends with /items/0 then we should only read a Reference, not
                    // a list, as the JSON Patch is only giving us a singular Reference (the paging we need for
                    // the list is split off on other change operations)
                    if (referenceListProperties.contains(getIgcPropertyName()) && !this.path.contains("/items/")) {
                        actualValue = igcRestClient.readJSONIntoReferenceList(this.value.toString());
                    } else {
                        actualValue = igcRestClient.readJSONIntoPOJO(this.value.toString());
                    }
                    break;
                case STRING:
                    actualValue = this.value.asText();
                    break;
                default:
                    log.warn("Unhandled value type '{}': {}", jsonType, this.value);
                    break;
            }

            return actualValue;

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

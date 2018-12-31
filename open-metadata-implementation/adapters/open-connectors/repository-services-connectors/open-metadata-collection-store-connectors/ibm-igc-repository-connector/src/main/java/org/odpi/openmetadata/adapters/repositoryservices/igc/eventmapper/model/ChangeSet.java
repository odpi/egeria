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
     * @return Set<String>
     */
    public Set<String> getChangedProperties() { return this.changesByProperty.keySet(); }

    /**
     * Retrieve the list of changes for the provided IGC property name.
     *
     * @param property name of the IGC property for which to retrieve changes
     * @return List<Change>
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

        public String getOp() { return this.op; }

        public String getIgcPropertyName() {
            String[] aTokens = this.path.split("/");
            if (aTokens.length > 1) {
                return aTokens[1];
            } else {
                log.error("Unable to find any property in path: {}", this.path);
                return null;
            }
        }

        public String getIgcPropertyPath() {
            return this.path;
        }

        public Object getNewValue() {

            Object actualValue = null;

            // Otherwise, look for a simple type
            JsonNodeType jsonType = this.value.getNodeType();
            switch (jsonType) {
                case ARRAY:
                    // TODO: how to translate an array?
                    actualValue = "???";
                    break;
                case BOOLEAN:
                    actualValue = this.value.asBoolean();
                    break;
                case NUMBER:
                    actualValue = this.value.asDouble();
                    break;
                case OBJECT:
                    // If an object, must be a reference -- read it in as one
                    actualValue = igcRestClient.readJSONIntoPOJO(this.value.toString());
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

    }

}

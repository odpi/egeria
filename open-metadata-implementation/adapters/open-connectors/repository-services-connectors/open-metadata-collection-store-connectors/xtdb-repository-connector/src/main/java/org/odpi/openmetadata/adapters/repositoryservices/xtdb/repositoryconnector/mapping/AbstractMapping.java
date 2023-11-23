/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import clojure.lang.PersistentHashMap;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.ffdc.XTDBAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * General utilities for the mapping of any instance data.
 */
public abstract class AbstractMapping {

    protected final XTDBOMRSRepositoryConnector xtdbConnector;

    protected static final ObjectMapper mapper = new ObjectMapper();
    private static final Keyword EMBEDDED_JSON = Keyword.intern("json");

    /**
     * Default constructor.
     */
    protected AbstractMapping() {
        this.xtdbConnector = null;
    }

    /**
     * Default constructor.
     * @param xtdbConnector connectivity to XTDB
     */
    protected AbstractMapping(XTDBOMRSRepositoryConnector xtdbConnector) {
        this.xtdbConnector = xtdbConnector;
    }

    /**
     * Construct a string representation of a keyword.
     * @param name of the keyword
     * @return String representation of the form ':name'
     */
    public static String getKeyword(String name) {
        return getKeyword(null, name);
    }

    /**
     * Construct a string representation of a keyword.
     * @param namespace of the keyword (optional)
     * @param name of the keyword
     * @return String representation of the form ':namespace/name'
     */
    public static String getKeyword(String namespace, String name) {
        if (name == null || name.length() == 0) {
            return null;
        }
        if (namespace == null || namespace.length() == 0) {
            return name;
        } else {
            return namespace + "/" + name;
        }
    }

    /**
     * Retrieve an embedded JSON-serialized form of a given object. This will prevent the object from being searchable,
     * but provides an efficient means to store structured information.
     * @param xtdbConnector connectivity to the repository
     * @param type name of the type into which the value is being serialized
     * @param property name of the property for which the value is being serialized
     * @param value to JSON-serialize
     * @return IPersistentMap giving the embedded serialized form
     */
    protected static IPersistentMap getEmbeddedSerializedForm(XTDBOMRSRepositoryConnector xtdbConnector, String type, String property, Object value) {
        IPersistentMap subMap = null;
        if (value != null) {
            try {
                // Serialize the value into JSON (via Jackson)
                String json = mapper.writeValueAsString(value);
                // Create a new map {:json "serialized-json-string"}
                Map<Keyword, String> map = new HashMap<>();
                map.put(EMBEDDED_JSON, json);
                subMap = PersistentHashMap.create(map);
            } catch (IOException e) {
                xtdbConnector.logProblem(AbstractMapping.class.getName(),
                                         "getEmbeddedSerializedForm",
                                         XTDBAuditCode.SERIALIZATION_FAILURE,
                                         e,
                                         property,
                                         type,
                                         e.getClass().getName());
            }
        }
        return subMap;
    }

    /**
     * Retrieve an embedded JSON-serialized form of a given object. This will prevent the object from being searchable,
     * but provides an efficient means to store structured information.
     * @param value to JSON-serialize
     * @return IPersistentMap giving the embedded serialized form
     * @throws IOException on any issue serializing the value
     */
    protected static IPersistentMap getEmbeddedSerializedForm(Object value) throws IOException {
        IPersistentMap subMap = null;
        if (value != null) {
            // Serialize the value into JSON (via Jackson)
            String json = mapper.writeValueAsString(value);
            // Create a new map {:json "serialized-json-string"}
            Map<Keyword, String> map = new HashMap<>();
            map.put(EMBEDDED_JSON, json);
            subMap = PersistentHashMap.create(map);
        }
        return subMap;
    }

    /**
     * Retrieve the deserialized value given an embedded form. This will prevent the object from being searchable,
     * but provides an efficient means to retrieve structured information.
     * @param xtdbConnector connectivity to the repository
     * @param type name of the type from which the value is being deserialized
     * @param property name of the property from which the value is being deserialized
     * @param embedded value to JSON-deserialize
     * @param javaType the type of value to deserialize
     * @param <T> type of value to deserialize
     * @return the deserialized value
     */
    protected static <T> T getDeserializedValue(XTDBOMRSRepositoryConnector xtdbConnector, String type, String property, IPersistentMap embedded, JavaType javaType) {
        // There must be the ":json" keyword in the map for it to be an embedded serialized form
        T deserialized = null;
        if (embedded != null && embedded.containsKey(EMBEDDED_JSON)) {
            String value = (String) embedded.valAt(EMBEDDED_JSON);
            try {
                deserialized = mapper.readValue(value, javaType);
            } catch (IOException e) {
                xtdbConnector.logProblem(AbstractMapping.class.getName(),
                                         "getEmbeddedSerializedForm",
                                         XTDBAuditCode.DESERIALIZATION_FAILURE,
                                         e,
                                         property,
                                         type,
                                         javaType.getTypeName(),
                                         e.getClass().getName());
            }
        }
        return deserialized;
    }

    /**
     * Retrieve the deserialized value given an embedded form. This will prevent the object from being searchable,
     * but provides an efficient means to retrieve structured information.
     * @param embedded value to JSON-deserialize
     * @param javaType the type of value to deserialize
     * @param <T> type of value to deserialize
     * @return the deserialized value
     * @throws IOException on any error deserializing the value
     */
    protected static <T> T getDeserializedValue(IPersistentMap embedded,
                                                JavaType javaType) throws IOException {
        // There must be the ":json" keyword in the map for it to be an embedded serialized form
        T deserialized = null;
        if (embedded != null && embedded.containsKey(EMBEDDED_JSON)) {
            String value = (String) embedded.valAt(EMBEDDED_JSON);
            deserialized = mapper.readValue(value, javaType);
        }
        return deserialized;
    }

}

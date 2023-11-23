/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.*;

/**
 * Maps singular StructPropertyValues between persistence and objects.
 *
 * These cannot simply be serialized to JSON as that would impact the ability to search their values correctly, so we
 * must break apart the values and the types for each property:
 * <code>
 *     {
 *         ...
 *         :entityProperties/someProperty.json {:json "{\"class\":\"StructPropertyValue\",\"instancePropertyCategory\":\"STRUCT\",\"attributes\":{\"class\":\"InstanceProperties\",\"instanceProperties\":{\"key\":\"{\"class\":\"PrimitivePropertyValue\",\"instancePropertyCategory\":\"PRIMITIVE\",\"primitiveDefCategory\":\"OM_PRIMITIVE_TYPE_STRING\",\"primitiveValue\":\"A Simple Term\"}\"}}}"}
 *         :entityProperties/someProperty.value {"key": "A Simple Term"}
 *         ...
 *     }
 * </code>
 */
public class StructPropertyValueMapping extends InstancePropertyValueMapping {

    /**
     * Add the provided struct value to the XTDB document.
     * @param xtdbConnector connectivity to the repository
     * @param builder to which to add the property value
     * @param keywords of the property
     * @param value of the property
     */
    public static void addStructPropertyValueToDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                                   XtdbDocument.Builder builder,
                                                   PropertyKeywords keywords,
                                                   StructPropertyValue value) {
        builder.put(keywords.getSearchablePath(), getStructPropertyValueForComparison(xtdbConnector, value));
    }

    /**
     * Add the provided struct value to the XTDB map.
     * @param doc the XTDB map to which to add the property
     * @param propertyKeyword the property whose value should be set, fully-qualified with namespace and type name
     * @param value of the property
     * @return IPersistentMap containing the updated XTDB doc
     * @throws InvalidParameterException if the value cannot be persisted
     */
    public static IPersistentMap addStructPropertyValueToDoc(IPersistentMap doc,
                                                             Keyword propertyKeyword,
                                                             StructPropertyValue value) throws InvalidParameterException {
        return doc.assoc(propertyKeyword, getStructPropertyValueForComparison(value));
    }

    /**
     * Convert the provided struct property value into a XTDB comparable form.
     * @param xtdbConnector connectivity to the repository
     * @param spv Egeria value to translate to XTDB-comparable value
     * @return {@code Map<String, Object>} value that XTDB can compare
     */
    public static Map<String, Object> getStructPropertyValueForComparison(XTDBOMRSRepositoryConnector xtdbConnector, StructPropertyValue spv) {
        InstanceProperties values = spv.getAttributes();
        if (values != null && values.getInstanceProperties() != null) {
            // Create a new TreeMap of the values to ensure they are sorted by key (for consistency)
            Map<String, Object> results = new TreeMap<>();
            for (Map.Entry<String, InstancePropertyValue> entry : values.getInstanceProperties().entrySet()) {
                String key = entry.getKey();
                InstancePropertyValue value = entry.getValue();
                Object toCompare = getValueForComparison(xtdbConnector, value);
                if (toCompare != null) {
                    results.put(key, toCompare);
                }
            }
            if (!results.isEmpty()) {
                return results;
            }
        }
        return null;
    }

    /**
     * Convert the provided struct property value into a XTDB comparable form.
     * @param spv Egeria value to translate to XTDB-comparable value
     * @return {@code Map<String, Object>} value that XTDB can compare
     * @throws InvalidParameterException if the value cannot be persisted
     */
    public static Map<String, Object> getStructPropertyValueForComparison(StructPropertyValue spv) throws InvalidParameterException {
        InstanceProperties values = spv.getAttributes();
        if (values != null && values.getInstanceProperties() != null) {
            // Create a new TreeMap of the values to ensure they are sorted by key (for consistency)
            Map<String, Object> results = new TreeMap<>();
            for (Map.Entry<String, InstancePropertyValue> entry : values.getInstanceProperties().entrySet()) {
                String key = entry.getKey();
                InstancePropertyValue value = entry.getValue();
                Object toCompare = getValueForComparison(value);
                if (toCompare != null) {
                    results.put(key, toCompare);
                }
            }
            if (!results.isEmpty()) {
                return results;
            }
        }
        return null;
    }

}

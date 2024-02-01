/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import clojure.lang.Keyword;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.*;

/**
 * Maps singular ArrayPropertyValues between persistence and objects.
 *
 * These cannot simply be serialized to JSON as that would impact the ability to search their values correctly, so we
 * must break apart the values and the types for each property:
 * <code>
 *     {
 *         ...
 *         :entityProperties/someProperty.json {:json "{\"class\":\"ArrayPropertyValue\",\"instancePropertyCategory\":\"ARRAY\",\"arrayValues\":{\"class\":\"InstanceProperties\",\"instanceProperties\":{\"0\":\"{\"class\":\"PrimitivePropertyValue\",\"instancePropertyCategory\":\"PRIMITIVE\",\"primitiveDefCategory\":\"OM_PRIMITIVE_TYPE_STRING\",\"primitiveValue\":\"A Simple Term\"}\"}}}"}
 *         :entityProperties/someProperty.value ["A Simple Term"]
 *         ...
 *     }
 * </code>
 */
public class ArrayPropertyValueMapping extends InstancePropertyValueMapping {

    /**
     * Add the provided array value to the XTDB document.
     * @param xtdbConnector connectivity to the repository
     * @param builder to which to add the property value
     * @param keywords of the property
     * @param value of the property
     */
    public static void addArrayPropertyValueToDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                                  XtdbDocument.Builder builder,
                                                  PropertyKeywords keywords,
                                                  ArrayPropertyValue value) {
        builder.put(keywords.getSearchablePath(), getArrayPropertyValueForComparison(xtdbConnector, value));
    }

    /**
     * Add the provided array value to the XTDB map.
     * @param doc the XTDB map to which to add the property
     * @param propertyKeyword the property whose value should be set, fully-qualified with namespace and type name
     * @param value of the property
     * @return IPersistentMap containing the updated XTDB map
     * @throws InvalidParameterException if the value cannot be persisted
     */
    public static IPersistentMap addArrayPropertyValueToDoc(IPersistentMap doc,
                                                            Keyword propertyKeyword,
                                                            ArrayPropertyValue value) throws InvalidParameterException {
        return doc.assoc(propertyKeyword, getArrayPropertyValueForComparison(value));
    }

    /**
     * Convert the provided array property value into a XTDB comparable form.
     * @param xtdbConnector connectivity to the repository
     * @param apv Egeria value to translate to XTDB-comparable value
     * @return {@code List<Object>} value that XTDB can compare
     */
    public static List<Object> getArrayPropertyValueForComparison(XTDBOMRSRepositoryConnector xtdbConnector, ArrayPropertyValue apv) {
        InstanceProperties values = apv.getArrayValues();
        if (values != null) {
            List<Object> results = new ArrayList<>();
            int total = apv.getArrayCount();
            for (int i = 0; i < total; i++) {
                InstancePropertyValue value = values.getPropertyValue("" + i);
                Object toCompare = getValueForComparison(xtdbConnector, value);
                if (toCompare != null) {
                    results.add(toCompare);
                }
            }
            if (!results.isEmpty()) {
                return results;
            }
        }
        return null;
    }

    /**
     * Convert the provided array property value into a XTDB comparable form.
     * @param apv Egeria value to translate to XTDB-comparable value
     * @return {@code List<Object>} value that XTDB can compare
     * @throws InvalidParameterException if the value cannot be persisted
     */
    public static List<Object> getArrayPropertyValueForComparison(ArrayPropertyValue apv) throws InvalidParameterException {
        InstanceProperties values = apv.getArrayValues();
        if (values != null) {
            List<Object> results = new ArrayList<>();
            int total = apv.getArrayCount();
            for (int i = 0; i < total; i++) {
                InstancePropertyValue value = values.getPropertyValue("" + i);
                Object toCompare = getValueForComparison(value);
                if (toCompare != null) {
                    results.add(toCompare);
                }
            }
            if (!results.isEmpty()) {
                return results;
            }
        }
        return null;
    }

}

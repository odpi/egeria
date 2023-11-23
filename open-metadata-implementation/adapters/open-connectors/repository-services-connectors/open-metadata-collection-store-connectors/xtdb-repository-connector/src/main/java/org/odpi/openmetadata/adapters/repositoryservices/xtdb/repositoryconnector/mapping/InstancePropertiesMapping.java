/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.mapping;

import clojure.lang.IPersistentMap;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.XTDBOMRSRepositoryConnector;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.PropertyKeywords;
import org.odpi.openmetadata.adapters.repositoryservices.xtdb.repositoryconnector.cache.TypeDefCache;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import xtdb.api.XtdbDocument;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;

import java.io.IOException;
import java.util.*;

/**
 * Maps the properties of InstanceProperties between persistence and objects.
 *
 * The values of the properties (InstancePropertyValue) will be both JSON-serialized and searchable.
 * @see InstancePropertyValueMapping
 */
public class InstancePropertiesMapping {

    private InstancePropertiesMapping() {}

    /**
     * Retrieve the instance property values from the provided XTDB document.
     * NOTE: whether empty or null, the instance properties will always be returned back from the XTDB representation
     * as an empty set of properties (not null).
     * @param xtdbConnector connectivity to the repository
     * @param type of the Egeria instance from which we are retrieving the values
     * @param xtdbDoc from which to retrieve the values
     * @return InstanceProperties
     */
    public static InstanceProperties getFromDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                                InstanceType type,
                                                XtdbDocument xtdbDoc) {

        Set<String> validProperties = TypeDefCache.getAllPropertyKeywordsForTypeDef(type.getTypeDefGUID()).keySet();
        String namespace = TypeDefCache.getPropertyNamespaceForType(type.getTypeDefGUID());

        // Iterate through each of the properties this instance could contain and add them to the map of values
        if (!validProperties.isEmpty()) {
            Map<String, InstancePropertyValue> values = new TreeMap<>();
            for (String propertyName : validProperties) {
                InstancePropertyValue value = InstancePropertyValueMapping.getInstancePropertyValueFromDoc(xtdbConnector, xtdbDoc, namespace, propertyName);
                if (value != null) {
                    values.put(propertyName, value);
                }
            }
            InstanceProperties ip = new InstanceProperties();
            ip.setInstanceProperties(values);
            return ip;
        }

        return null;

    }

    /**
     * Translate the provided XTDB representation into an Egeria representation.
     * @param type of the Egeria instance from which we are retrieving the values
     * @param doc from which to map
     * @return InstanceProperties
     * @throws IOException on any issue deserializing values
     */
    public static InstanceProperties getFromMap(InstanceType type,
                                                IPersistentMap doc) throws IOException {

        Set<String> validProperties = TypeDefCache.getAllPropertyKeywordsForTypeDef(type.getTypeDefGUID()).keySet();
        String namespace = TypeDefCache.getPropertyNamespaceForType(type.getTypeDefGUID());

        // Iterate through each of the properties this instance could contain and add them to the map of values
        if (!validProperties.isEmpty()) {
            Map<String, InstancePropertyValue> values = new TreeMap<>();
            for (String propertyName : validProperties) {
                InstancePropertyValue value = InstancePropertyValueMapping.getInstancePropertyValueFromMap(doc, namespace, propertyName);
                if (value != null) {
                    values.put(propertyName, value);
                }
            }
            InstanceProperties ip = new InstanceProperties();
            ip.setInstanceProperties(values);
            return ip;
        }

        return null;

    }

    /**
     * Add the provided instance property values to the XTDB document.
     * @param xtdbConnector connectivity to the repository
     * @param builder to which to add the properties
     * @param type of the Egeria instance to which the values are being added
     * @param properties to add
     */
    public static void addToDoc(XTDBOMRSRepositoryConnector xtdbConnector,
                                XtdbDocument.Builder builder,
                                InstanceType type,
                                InstanceProperties properties) {

        Map<String, InstancePropertyValue> propertyMap;
        if (properties != null) {
            propertyMap = properties.getInstanceProperties();
            if (propertyMap != null) {
                for (Map.Entry<String, InstancePropertyValue> entry : propertyMap.entrySet()) {
                    InstancePropertyValueMapping.addInstancePropertyValueToDoc(xtdbConnector, type, builder, entry.getKey(), entry.getValue());
                }
            } else {
                propertyMap = new HashMap<>(); // Create an empty map if there is not one, for next set of checks
            }
        } else {
            propertyMap = new HashMap<>();     // Create an empty map if there is not one, for next set of checks
        }

        // explicitly set any other properties on the instance to null, so that we can still include them
        // if sorting, or running an explicit 'IS_NULL' or 'NOT_NULL' search against appropriately
        Set<String> allProperties = TypeDefCache.getAllPropertyKeywordsForTypeDef(type.getTypeDefGUID()).keySet();
        for (String propertyName : allProperties) {
            if (!propertyMap.containsKey(propertyName)) {
                // Only explicitly set to null if the earlier processing has not already set a value on the property
                InstancePropertyValueMapping.addInstancePropertyValueToDoc(xtdbConnector, type, builder, propertyName, null);
            }
        }

    }

    /**
     * Add the provided instance property values to the XTDB document map.
     * @param doc metadata instance in XTDB document map form to which to add the properties
     * @param typeDefGUID of the Egeria instance to which the values are being added
     * @param properties full set of properties for the instance
     * @return IPersistentMap giving the updated instance representation
     * @throws InvalidParameterException if any of the properties cannot be persisted
     * @throws IOException on any error serializing the properties
     */
    public static IPersistentMap addToMap(IPersistentMap doc,
                                          String typeDefGUID,
                                          InstanceProperties properties)
            throws InvalidParameterException, IOException {

        Map<String, PropertyKeywords> propertyKeywordMap = TypeDefCache.getAllPropertyKeywordsForTypeDef(typeDefGUID);
        Map<String, InstancePropertyValue> propertyMap;
        if (properties != null) {
            propertyMap = properties.getInstanceProperties();
            if (propertyMap != null) {
                for (Map.Entry<String, InstancePropertyValue> entry : propertyMap.entrySet()) {
                    PropertyKeywords propertyKeywords = propertyKeywordMap.get(entry.getKey());
                    doc = InstancePropertyValueMapping.addInstancePropertyValueToDoc(doc,
                            propertyKeywords,
                            entry.getValue());
                }
            } else {
                propertyMap = new HashMap<>(); // Create an empty map if there is not one, for next set of checks
            }
            // explicitly set any other properties on the instance to null, so that we can still include them
            // if sorting, or running an explicit 'IS_NULL' or 'NOT_NULL' search against appropriately
            for (String propertyName : propertyKeywordMap.keySet()) {
                if (!propertyMap.containsKey(propertyName)) {
                    // Only explicitly set to null if the earlier processing has not already set a value on the property
                    PropertyKeywords propertyKeywords = propertyKeywordMap.get(propertyName);
                    doc = InstancePropertyValueMapping.addInstancePropertyValueToDoc(doc,
                            propertyKeywords,
                            null);
                }
            }
        }

        return doc;

    }

}

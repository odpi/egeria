/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.utils;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.MapPropertyValue;

import java.util.HashMap;
import java.util.Map;

public class EntityPropertiesBuilder {

    private InstanceProperties instanceProperties;


    /**
     * default constructor
     */
    public EntityPropertiesBuilder() {
        instanceProperties = new InstanceProperties();
    }

    /**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param value - value of the property
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withStringProperty(String key, String value) {
        instanceProperties.setProperty(key, EntityPropertiesUtils.createPrimitiveStringPropertyValue(value));
        return this;
    }

    /**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param value - value of the property
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withIntegerProperty(String key, Integer value) {
        instanceProperties.setProperty(key, EntityPropertiesUtils.createPrimitiveIntPropertyValue(value));
        return this;
    }
    /**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param timestamp - timestamp to be formatted as date
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withDateProperty(String key, Long timestamp) {
        instanceProperties.setProperty(key, EntityPropertiesUtils.createPrimitiveDatePropertyValue(timestamp));
        return this;
    }


//
//    public InstanceProperties withMapPropertyToInstance(String              propertyName,
//                                                       Map<String, String> mapValues)
//    {
//
//
//            if ((mapValues != null) && (! mapValues.isEmpty()))
//            {
//
//
//                /*
//                 * The values of a map property are stored as an embedded InstanceProperties object.
//                 */
//                .addPropertyMapToInstance(sourceName,
//                        null,
//                        propertyName,
//                        mapValues,
//                        methodName);
//
//                /*
//                 * If there was content in the map then the resulting InstanceProperties are added as
//                 * a property to the resulting properties.
//                 */
//                if (mapInstanceProperties != null)
//                {
//                    MapPropertyValue mapPropertyValue = new MapPropertyValue();
//                    mapPropertyValue.setMapValues(mapInstanceProperties);
//                    resultingProperties.setProperty(propertyName, mapPropertyValue);
//
//                    log.debug("Returning instanceProperty: " + resultingProperties.toString());
//
//                    return resultingProperties;
//                }
//            }
//
//        return properties;
//    }



    /**
     * Returns the instance properties object
     *
     * @return properties
     */
    public InstanceProperties build() {
        return instanceProperties;
    }
}

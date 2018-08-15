/* SPDX-License-Identifier: Apache-2.0 */
package org.apache.atlas.virtualiser.util;

import java.util.Properties;
import java.util.ResourceBundle;

/**
 * PropertiesHelper is used to load properties
 */
public class PropertiesHelper {
    //property file
    private static final  String VIRTUALISER_CONFIG="virtualiser";
    //properties need to pick up
    public static Properties properties;
    /**
     * load property files
     */
    public static void loadProperties() {
        properties = new Properties();
        ResourceBundle resourceBundle = ResourceBundle.getBundle(VIRTUALISER_CONFIG);
        resourceBundle.keySet().stream().forEach(k -> properties.put(k, resourceBundle.getString(k)));
    }
}

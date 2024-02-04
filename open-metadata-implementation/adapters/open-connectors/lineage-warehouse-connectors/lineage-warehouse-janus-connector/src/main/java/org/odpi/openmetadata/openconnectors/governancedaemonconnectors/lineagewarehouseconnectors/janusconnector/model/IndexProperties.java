/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.lineagewarehouseconnectors.janusconnector.model;


public final class IndexProperties {
    private final String propertyName;
    private final String propertyKeyName;
    private final boolean unique;
    private final Class type;

    public IndexProperties(String propertyName, String propertyKeyName, boolean unique, Class type) {
        this.propertyName = propertyName;
        this.propertyKeyName = propertyKeyName;
        this.unique = unique;
        this.type = type;
    }

    public Class getType() {
        return type;
    }

    public String getPropertyName() {
        return propertyName;
    }

    public String getPropertyKeyName() {
        return propertyKeyName;
    }

    public boolean isUnique() {
        return unique;
    }


}

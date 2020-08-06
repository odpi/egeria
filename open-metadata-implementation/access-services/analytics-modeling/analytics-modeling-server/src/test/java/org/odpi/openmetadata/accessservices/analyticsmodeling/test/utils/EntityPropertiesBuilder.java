/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;


public class EntityPropertiesBuilder {
	
	private OMRSRepositoryPropertiesUtilities propertyUtils;

    private InstanceProperties instanceProperties;
    
    String context;
    String method;


    /**
     * default constructor
     */
    public EntityPropertiesBuilder(String context, String method, InstanceProperties properties) {
    	this.context = context;
    	this.method = method;
    	propertyUtils = new OMRSRepositoryPropertiesUtilities();
        instanceProperties = properties;
    }

	/**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param value - value of the property
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withStringProperty(String key, String value) {
    	
    	instanceProperties = propertyUtils.addStringPropertyToInstance(context, instanceProperties, key, value, method);
    	
        return this;
    }
    
	/**
     * Returns the same builder
     *
     * @param key - name of the property
     * @param value - value of the property
     * @return the builder to use to return
     */
    public EntityPropertiesBuilder withBooleanProperty(String key, Boolean value) {
    	
    	instanceProperties = propertyUtils.addBooleanPropertyToInstance(context, instanceProperties, key, value, method);
    	
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
    	instanceProperties = propertyUtils.addIntPropertyToInstance(context, instanceProperties, key, value, method);
        return this;
    }

    /**
     * Returns the instance properties object
     *
     * @return properties
     */
    public InstanceProperties build() {
        return instanceProperties;
    }
}

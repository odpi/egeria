/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * VirtualizationConfig caches the properties that are used to setup up the connector to the virtualisation
 * solutions in the server. The configurations contain the name of the connector provider and the corresponding
 * additional properties.
 *
 * This configuration class should support various types of the virtualisation connectors
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualizationConfig extends AdminServicesConfigHeader {

    private String virtualizationProvider = null;
    private String ivInTopicName          = null;
    private Connection ivInTopic          = null;
    private String ivOutTopicName         = null;
    private Connection ivOutTopic         = null;


    /**
     * Default constuctor
     */
    public VirtualizationConfig() {
        super();
    }

    /**
     * Copy data from template
     *
     * @param template
     */
    public VirtualizationConfig(VirtualizationConfig template) {
        if (template != null) {
            this.virtualizationProvider = template.virtualizationProvider;
            this.ivInTopicName          = template.ivInTopicName;
            this.ivInTopic              = template.ivInTopic;
            this.ivOutTopicName         = template.ivOutTopicName;
            this.ivOutTopic             = template.ivOutTopic;
        }
    }


    /**
     * Provide the name of virtualization provider
     * @return String virtualizationProvider
     */
    public String getVirtualizationProvider() {
        return virtualizationProvider;
    }

    /**
     * Set the name of virtualization provider
     * @param virtualizationProvider
     */
    public void setVirtualizationProvider(String virtualizationProvider) {
        this.virtualizationProvider = virtualizationProvider;
    }

    /**
     * Provide the name of the information view in topic
     * @return String ivInTopicName
     */
    public String getIvInTopicName() {
        return ivInTopicName;
    }


    /**
     * Set the name of the information view in topic
     * @param ivInTopicName String
     */
    public void setIvInTopicName(String ivInTopicName) {
        this.ivInTopicName = ivInTopicName;
    }

    /**
     * Provide the connection of the information view in topic
     * @return Connection ivInTopic
     */
    public Connection getIvInTopic() {
        return ivInTopic;
    }

    /**
     * Set the connection of the information view in topic
     * @param ivInTopic Connection
     */
    public void setIvInTopic(Connection ivInTopic) {
        this.ivInTopic = ivInTopic;
    }

    /**
     * Provide the name of the information view out topic
     * @return String ivOutTopicName
     */
    public String getIvOutTopicName() {
        return ivOutTopicName;
    }


    /**
     * Set the name of the information view out topic
     * @param ivOutTopicName String
     */
    public void setIvOutTopicName(String ivOutTopicName) {
        this.ivOutTopicName = ivOutTopicName;
    }

    /**
     * Provide the connection of the information view out topic
     * @return Connection ivOutTopic
     */
    public Connection getIvOutTopic() {
        return ivOutTopic;
    }

    /**
     * Set the connection of the information view out topic
     * @param ivOutTopic Connection
     */
    public void setIvOutTopic(Connection ivOutTopic) {
        this.ivOutTopic = ivOutTopic;
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare) {
        if (this == objectToCompare) {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) {
            return false;
        }
        VirtualizationConfig that = (VirtualizationConfig) objectToCompare;
        return  Objects.equals(getIvInTopicName(), that.getIvInTopicName())&&
                Objects.equals(getIvInTopic(), that.getIvInTopic()) &&
                Objects.equals(getIvOutTopicName(), that.getIvOutTopicName()) &&
                Objects.equals(getVirtualizationProvider(), that.getVirtualizationProvider());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode(){
        return  Objects.hash(getIvInTopic(), getIvInTopicName(), getIvOutTopic(), getIvOutTopicName(),
                             /*getVirtualizationConnection(),*/ getVirtualizationProvider());
    }
}
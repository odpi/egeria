/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * VirtualizerConfig caches the properties that are used to setup up the connector to the virtualisation
 * solutions in the server. The configurations contain the name of the connector provider and the corresponding
 * additional properties.
 *
 * This configuration class should support various types of the virtualisation connectors
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualizerConfig extends AdminServicesConfigHeader {

    private String logFileName = "virtualizer.log";
    private String virtualizationProvider = null;
    private Connection virtualizationConnection = null;
    private String ivInTopicName = null;
    private Connection ivInTipic = null;
    private String ivOutTopicName = null;
    private Connection ivOutTipic = null;


    /**
     * Default constuctor
     */
    public VirtualizerConfig() {
        super();
    }

    /**
     * Copy data from template
     *
     * @param template
     */
    public VirtualizerConfig(VirtualizerConfig template) {
        if (template != null) {
            this.virtualizationProvider = template.virtualizationProvider;
            this.virtualizationConnection = template.virtualizationConnection;
            this.ivInTopicName = template.ivInTopicName;
            this.ivInTipic = template.ivInTipic;
            this.ivOutTopicName = template.ivOutTopicName;
            this.ivOutTipic = template.ivOutTipic;
        }
    }

    public String getLogFileName() {
        return logFileName;
    }

    public void setLogFileName(String logFileName) {
        this.logFileName = logFileName;
    }

    public String getVirtualizationProvider() {
        return virtualizationProvider;
    }

    public void setVirtualizationProvider(String virtualizationProvider) {
        this.virtualizationProvider = virtualizationProvider;
    }

    public Connection getVirtualizationConnection() {
        return virtualizationConnection;
    }

    public void setVirtualizationConnection(Connection virtualizationConnection) {
        this.virtualizationConnection = virtualizationConnection;
    }

    public String getIvInTopicName() {
        return ivInTopicName;
    }

    public void setIvInTopicName(String ivInTopicName) {
        this.ivInTopicName = ivInTopicName;
    }

    public Connection getIvInTipic() {
        return ivInTipic;
    }

    public void setIvInTipic(Connection ivInTipic) {
        this.ivInTipic = ivInTipic;
    }

    public String getIvOutTopicName() {
        return ivOutTopicName;
    }

    public void setIvOutTopicName(String ivOutTopicName) {
        this.ivOutTopicName = ivOutTopicName;
    }

    public Connection getIvOutTipic() {
        return ivOutTipic;
    }

    public void setIvOutTipic(Connection ivOutTipic) {
        this.ivOutTipic = ivOutTipic;
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
        VirtualizerConfig that = (VirtualizerConfig) objectToCompare;
        return  Objects.equals(getIvInTopicName(), that.getIvInTopicName())&&
                Objects.equals(getIvInTipic(), that.getIvInTipic()) &&
                Objects.equals(getIvOutTopicName(), that.getIvOutTopicName()) &&
                Objects.equals(getVirtualizationProvider(), that.getVirtualizationProvider()) &&
                Objects.equals(getVirtualizationConnection(), that.getVirtualizationConnection());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode(){
        return  Objects.hash(getIvInTipic(), getIvInTopicName(), getIvOutTipic(), getIvOutTopicName(),
                             getVirtualizationConnection(), getVirtualizationProvider());
    }
}
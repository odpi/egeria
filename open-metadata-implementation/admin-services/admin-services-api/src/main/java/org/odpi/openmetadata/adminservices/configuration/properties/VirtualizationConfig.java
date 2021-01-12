/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * VirtualizationConfig is the configuration for the deprecated Virtualizer OMAG Server.  This function
 * is replaced by the new Database Integrator OMIS that runs in the Integration Daemon OMAG Server.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class VirtualizationConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private String              virtualizationProvider;
    private String              virtualizerOutboundTopicName;
    private Connection          virtualizerOutboundTopic;
    private String              virtualizerInboundTopicName;
    private Connection          virtualizerInboundTopic;
    private Connection          virtualizationSolutionConnection;
    private Map<String, Object> virtualizationSolutionConfig;


    /**
     * Default constructor
     */
    public VirtualizationConfig() {
        super();
    }

    /**
     * Copy data from template
     *
     * @param template object to copy
     */
    public VirtualizationConfig(VirtualizationConfig template) {
        super(template);
        if (template != null) {
            this.virtualizationProvider           = template.virtualizationProvider;
            this.virtualizerOutboundTopicName     = template.virtualizerOutboundTopicName;
            this.virtualizerOutboundTopic         = template.virtualizerOutboundTopic;
            this.virtualizerInboundTopicName      = template.virtualizerInboundTopicName;
            this.virtualizerInboundTopic          = template.virtualizerInboundTopic;
            this.virtualizationSolutionConnection = template.virtualizationSolutionConnection;
            this.virtualizationSolutionConfig     = template.virtualizationSolutionConfig;
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
     * @param virtualizationProvider connector provider
     */
    public void setVirtualizationProvider(String virtualizationProvider) {
        this.virtualizationProvider = virtualizationProvider;
    }

    /**
     * Provide the name of the information view in topic
     * @return String virtualizerOutboundTopicName
     */
    public String getVirtualizerOutboundTopicName() {
        return virtualizerOutboundTopicName;
    }


    /**
     * Set the name of the information view in topic
     * @param virtualizerOutboundTopicName String
     */
    public void setVirtualizerOutboundTopicName(String virtualizerOutboundTopicName) {
        this.virtualizerOutboundTopicName = virtualizerOutboundTopicName;
    }

    /**
     * Provide the connection of the information view in topic
     * @return Connection virtualizerOutboundTopic
     */
    public Connection getVirtualizerOutboundTopic() {
        return virtualizerOutboundTopic;
    }

    /**
     * Set the connection of the information view in topic
     * @param virtualizerOutboundTopic Connection
     */
    public void setVirtualizerOutboundTopic(Connection virtualizerOutboundTopic) {
        this.virtualizerOutboundTopic = virtualizerOutboundTopic;
    }

    /**
     * Provide the name of the information view out topic
     * @return String virtualizerInboundTopicName
     */
    public String getVirtualizerInboundTopicName() {
        return virtualizerInboundTopicName;
    }


    /**
     * Set the name of the information view out topic
     * @param virtualizerInboundTopicName String
     */
    public void setVirtualizerInboundTopicName(String virtualizerInboundTopicName) {
        this.virtualizerInboundTopicName = virtualizerInboundTopicName;
    }

    /**
     * Provide the connection of the information view out topic
     * @return Connection virtualizerInboundTopic
     */
    public Connection getVirtualizerInboundTopic() {
        return virtualizerInboundTopic;
    }

    /**
     * Set the connection of the information view out topic
     * @param virtualizerInboundTopic Connection
     */
    public void setVirtualizerInboundTopic(Connection virtualizerInboundTopic) {
        this.virtualizerInboundTopic = virtualizerInboundTopic;
    }

    /**
     * Provide the connection of the virtualization solution
     * @return Connection virtualizationSolutionConnection
     */
    public Connection getVirtualizationSolutionConnection() {
        return virtualizationSolutionConnection;
    }

    /**
     * Set the connection of the information view out topic
     * @param virtualizationSolutionConnection Connection
     */
    public void setVirtualizationSolutionConnection(Connection virtualizationSolutionConnection) {
        this.virtualizationSolutionConnection = virtualizationSolutionConnection;
    }

    /**
     * Provide the connection of the virtualization configuration
     * @return virtualizationSolutionConfig
     */
    public Map<String, Object> getVirtualizationSolutionConfig() {
        return virtualizationSolutionConfig;
    }

    /**
     * Set the connection of the virtualization configuration
     * @param virtualizationSolutionConfig Config
     */
    public void setVirtualizationSolutionConfig(Map<String, Object> virtualizationSolutionConfig) {
        this.virtualizationSolutionConfig = virtualizationSolutionConfig;
    }

    @Override
    public String toString()
    {
        return "VirtualizationConfig{" + "virtualizationProvider='" + virtualizationProvider + '\'' + ", virtualizerOutboundTopicName='" + virtualizerOutboundTopicName + '\'' + ", virtualizerOutboundTopic=" + virtualizerOutboundTopic + ", virtualizerInboundTopicName='" + virtualizerInboundTopicName + '\'' + ", virtualizerInboundTopic=" + virtualizerInboundTopic + ", virtualizationSolutionConnection=" + virtualizationSolutionConnection + ", virtualizationSolutionConfig=" + virtualizationSolutionConfig + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VirtualizationConfig)) return false;
        VirtualizationConfig that = (VirtualizationConfig) o;
        return Objects.equals(getVirtualizationProvider(), that.getVirtualizationProvider()) &&
                Objects.equals(getVirtualizerOutboundTopicName(), that.getVirtualizerOutboundTopicName()) &&
                Objects.equals(getVirtualizerOutboundTopic(), that.getVirtualizerOutboundTopic()) &&
                Objects.equals(getVirtualizerInboundTopicName(), that.getVirtualizerInboundTopicName()) &&
                Objects.equals(getVirtualizerInboundTopic(), that.getVirtualizerInboundTopic()) &&
                Objects.equals(getVirtualizationSolutionConnection(), that.getVirtualizationSolutionConnection()) &&
                Objects.equals(getVirtualizationSolutionConfig(), that.getVirtualizationSolutionConfig());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getVirtualizationProvider(), getVirtualizerOutboundTopicName(), getVirtualizerOutboundTopic(), getVirtualizerInboundTopicName(), getVirtualizerInboundTopic(), getVirtualizationSolutionConnection(), getVirtualizationSolutionConfig());
    }
}
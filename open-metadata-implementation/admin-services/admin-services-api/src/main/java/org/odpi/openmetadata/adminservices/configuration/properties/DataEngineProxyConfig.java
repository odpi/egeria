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
 * DataEngineProxyConfig caches the properties that are used to setup up the connector to a Data Engine Proxy in
 * the server.
 *
 * This configuration class should support various types of Data Engine connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataEngineProxyConfig extends AdminServicesConfigHeader {

    /* Properties needed to call the access service REST APIs */
    private String     accessServiceRootURL      = null;
    private String     accessServiceServerName   = null;

    private Connection dataEngineProxyConnection = null;
    private int        pollIntervalInSeconds     = 60;

    /**
     * Default constuctor
     */
    public DataEngineProxyConfig() {
        super();
    }

    /**
     * Copy data from template
     *
     * @param template
     */
    public DataEngineProxyConfig(DataEngineProxyConfig template) {
        if (template != null) {
            this.accessServiceRootURL      = template.accessServiceRootURL;
            this.accessServiceServerName   = template.accessServiceServerName;
            this.dataEngineProxyConnection = template.dataEngineProxyConnection;
            this.pollIntervalInSeconds     = template.pollIntervalInSeconds;
        }
    }

    /**
     * Provide the root URL of the Data Engine OMAS
     * @return String
     */
    public String getAccessServiceRootURL() { return accessServiceRootURL; }

    /**
     * Set the root URL of the Data Engine OMAS
     * @param accessServiceRootURL
     */
    public void setAccessServiceRootURL(String accessServiceRootURL) { this.accessServiceRootURL = accessServiceRootURL; }

    /**
     * Provide the server name of the Data Engine OMAS
     * @return String
     */
    public String getAccessServiceServerName() { return accessServiceServerName; }

    /**
     * Set the server name of the Data Engine OMAS
     * @param accessServiceServerName
     */
    public void setAccessServiceServerName(String accessServiceServerName) { this.accessServiceServerName = accessServiceServerName; }

    /**
     * Provide the connection to the Data Engine proxy
     * @return Connection
     */
    public Connection getDataEngineProxyConnection() {
        return dataEngineProxyConnection;
    }

    /**
     * Set the connection to the Data Engine proxy
     * @param dataEngineProxyConnection
     */
    public void setDataEngineProxyConnection(Connection dataEngineProxyConnection) { this.dataEngineProxyConnection = dataEngineProxyConnection; }

    /**
     * Provide the seconds to wait between each polling of the data engine for changes. Note that this is only used
     * by Data Engine Connectors that require polling in order to find changes.
     * @return int
     */
    public int getPollIntervalInSeconds() { return pollIntervalInSeconds; }

    /**
     * Set the number of seconds to wait between each polling of the data engine for changes. Note that this is only
     * used by Data Engine Connectors that require polling in order to find changes.
     * @param pollIntervalInSeconds
     */
    public void setPollIntervalInSeconds(int pollIntervalInSeconds) { this.pollIntervalInSeconds = pollIntervalInSeconds; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEngineProxyConfig)) return false;
        DataEngineProxyConfig that = (DataEngineProxyConfig) o;
        return Objects.equals(getAccessServiceRootURL(), that.getAccessServiceRootURL()) &&
                Objects.equals(getAccessServiceServerName(), that.getAccessServiceServerName()) &&
                Objects.equals(getDataEngineProxyConnection(), that.getDataEngineProxyConnection()) &&
                Objects.equals(getPollIntervalInSeconds(), that.getPollIntervalInSeconds());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccessServiceRootURL(), getAccessServiceServerName(),
                getDataEngineProxyConnection(), getPollIntervalInSeconds());
    }

}

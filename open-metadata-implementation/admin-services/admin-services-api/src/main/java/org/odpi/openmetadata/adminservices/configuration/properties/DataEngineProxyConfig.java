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
 * DataEngineProxyConfig caches the properties that are used to setup up the connector to a Data Engine Proxy in
 * the server.
 *
 * This configuration class should support various types of Data Engine connectors.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataEngineProxyConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 2L;

    /* Properties needed to call the access service REST APIs */
    private String     accessServiceRootURL    = null;
    private String     accessServiceServerName = null;

    private Connection dataEngineConnection    = null;
    private int        pollIntervalInSeconds   = 60;
    private int        batchWindowInSeconds    = 86400;
    private boolean    eventsClientEnabled     = false;

    /**
     * Default constructor
     */
    public DataEngineProxyConfig() {
        super();
    }

    /**
     * Copy data from template
     *
     * @param template an existing Data Engine proxy configuration from which to copy
     */
    public DataEngineProxyConfig(DataEngineProxyConfig template) {
        if (template != null) {
            this.accessServiceRootURL    = template.accessServiceRootURL;
            this.accessServiceServerName = template.accessServiceServerName;
            this.dataEngineConnection    = template.dataEngineConnection;
            this.pollIntervalInSeconds   = template.pollIntervalInSeconds;
            this.batchWindowInSeconds    = template.batchWindowInSeconds;
        }
    }

    /**
     * Provide the root URL of the Data Engine OMAS
     * @return String
     */
    public String getAccessServiceRootURL() { return accessServiceRootURL; }

    /**
     * Set the root URL of the Data Engine OMAS
     * @param accessServiceRootURL the URL of the Data Engine OMAS
     */
    public void setAccessServiceRootURL(String accessServiceRootURL) { this.accessServiceRootURL = accessServiceRootURL; }

    /**
     * Provide the server name of the Data Engine OMAS
     * @return String
     */
    public String getAccessServiceServerName() { return accessServiceServerName; }

    /**
     * Set the server name of the Data Engine OMAS
     * @param accessServiceServerName the name of the Data Engine OMAS
     */
    public void setAccessServiceServerName(String accessServiceServerName) { this.accessServiceServerName = accessServiceServerName; }

    /**
     * Provide the connection to the Data Engine
     * @return Connection
     */
    public Connection getDataEngineConnection() {
        return dataEngineConnection;
    }

    /**
     * Set the connection to the Data Engine
     * @param dataEngineConnection the connection to the Data Engine
     */
    public void setDataEngineConnection(Connection dataEngineConnection) { this.dataEngineConnection = dataEngineConnection; }

    /**
     * Provide the seconds to wait between each polling of the data engine for changes. Note that this is only used
     * by Data Engine Connectors that require polling in order to find changes.
     * @return int
     */
    public int getPollIntervalInSeconds() { return pollIntervalInSeconds; }

    /**
     * Set the number of seconds to wait between each polling of the data engine for changes. Note that this is only
     * used by Data Engine Connectors that require polling in order to find changes.
     * @param pollIntervalInSeconds the number of seconds to wait between each poll for changes
     */
    public void setPollIntervalInSeconds(int pollIntervalInSeconds) { this.pollIntervalInSeconds = pollIntervalInSeconds; }

    /**
     * Provide the maximum number of seconds to include for a window of polling. When polling, the proxy will only look
     * for changes from the last sync time through to the max sync time + this number of seconds, to ensure that the
     * batches of results being polled do not become too large. Note that this is only used by Data Engine Connectors
     * that require polling in order to find changes.
     * @return int
     */
    public int getBatchWindowInSeconds() { return batchWindowInSeconds; }

    /**
     * Set the number of seconds across which to include results for a window of polling. Note that this is only used
     * by Data Engine Connectors that require polling in order to find changes.
     * @param batchWindowInSeconds the number of seconds to include in each polling window
     * @see #getBatchWindowInSeconds()
     */
    public void setBatchWindowInSeconds(int batchWindowInSeconds) { this.batchWindowInSeconds = batchWindowInSeconds; }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DataEngineProxyConfig)) return false;
        DataEngineProxyConfig that = (DataEngineProxyConfig) o;
        return Objects.equals(getAccessServiceRootURL(), that.getAccessServiceRootURL()) &&
                Objects.equals(getAccessServiceServerName(), that.getAccessServiceServerName()) &&
                Objects.equals(getDataEngineConnection(), that.getDataEngineConnection()) &&
                Objects.equals(getPollIntervalInSeconds(), that.getPollIntervalInSeconds());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(getAccessServiceRootURL(), getAccessServiceServerName(),
                getDataEngineConnection(), getPollIntervalInSeconds());
    }

    /**
     * Configuration parameter controlling events client usage in the Data Engine Proxy server
     * @return true if enabled
     */
    public boolean isEventsClientEnabled() {
        return eventsClientEnabled;
    }

    /**
     * Sets configuration parameter controlling events client usage in the Data Engine Proxy server
     * @param eventsClientEnabled
     */
    public void setEventsClientEnabled(boolean eventsClientEnabled) {
        this.eventsClientEnabled = eventsClientEnabled;
    }


}

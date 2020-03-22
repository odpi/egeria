package org.odpi.openmetadata.adminservices.configuration.properties;
/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SearchIndexingConfig provides the configuration for search indexing repository
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchIndexingConfig extends AdminServicesConfigHeader {

    private static final long serialVersionUID = 1L;

    private String description;
    private Connection searchIndexingConnection;
    private Connection enterpriseOMRSTopicConnection;

    /**
     * Return the short description of the Search Indexing Connector
     *
     * @return String description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set up the description of the Search Indexing Connector
     *
     * @param description of the Search Indexing Connector
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Return the connection for the Search Indexing connector
     *
     * @return Connection object
     */
    public Connection getSearchIndexingConnection() {
        return searchIndexingConnection;
    }

    /**
     * Set up the connection for the Search Indexing connector.
     *
     * @param searchIndexingConnection Connection object
     */
    public void setSearchIndexingConnection(Connection searchIndexingConnection) {
        this.searchIndexingConnection = searchIndexingConnection;
    }

    /**
     * Return the connection for the Enterprise OMRS Topic connector.
     *
     * @return Connection object
     */
    public Connection getEnterpriseOMRSTopicConnection() {
        return enterpriseOMRSTopicConnection;
    }

    /**
     * Set up the connection for the Enterprise OMRS Topic connector.
     *
     * @param enterpriseOMRSTopicConnection Connection object
     */
    public void setEnterpriseOMRSTopicConnection(Connection enterpriseOMRSTopicConnection) {
        this.enterpriseOMRSTopicConnection = enterpriseOMRSTopicConnection;
    }
}

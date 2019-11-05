/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.List;
import java.util.Map;


/**
 * The Data platform config class provides the configuration properties for the data-platform-services.
 */
public class DataPlatformConfig extends AdminServicesConfigHeader
{

    /* Properties needed to call the access service REST APIs */
    private String      dataPlatformServerURL;
    private String      dataPlatformServerName;

    private String      dataPlatformConnectionProvider   = null;
    private Connection  dataPlatformConnection = null;
    private String      dataPlatformGUID = null;

    /* Connection for topic that send out topics */
    private String      dataPlatformServiceOutTopicName;
    private Connection  dataPlatformServiceOutTopic;

    /* Properties for a data platform  */
    private Map<String, Object> dataPlatformConfig = null;


    /**
     * Default Constructor
     */
    public DataPlatformConfig()
    {
    }

    /**
     * Default Constructor
     */
    public DataPlatformConfig(String dataPlatformServerURL, String dataPlatformServerName, String dataPlatformConnectionProvider, Connection dataPlatformConnection, String dataPlatformGUID, String dataPlatformServiceOutTopicName) {
        this.dataPlatformServerURL = dataPlatformServerURL;
        this.dataPlatformServerName = dataPlatformServerName;
        this.dataPlatformConnectionProvider = dataPlatformConnectionProvider;
        this.dataPlatformConnection = dataPlatformConnection;
        this.dataPlatformGUID = dataPlatformGUID;
        this.dataPlatformServiceOutTopicName = dataPlatformServiceOutTopicName;
    }

    /**
     * Constructor to set up properties in one go
     *
     * @param template object to copy
     */
    public DataPlatformConfig(AdminServicesConfigHeader template, String dataPlatformServerURL, String dataPlatformServerName, String dataPlatformConnectionProvider, Connection dataPlatformConnection, String dataPlatformGUID, String dataPlatformServiceOutTopicName) {
        super(template);
        this.dataPlatformServerURL = dataPlatformServerURL;
        this.dataPlatformServerName = dataPlatformServerName;
        this.dataPlatformConnectionProvider = dataPlatformConnectionProvider;
        this.dataPlatformConnection = dataPlatformConnection;
        this.dataPlatformGUID = dataPlatformGUID;
        this.dataPlatformServiceOutTopicName = dataPlatformServiceOutTopicName;
    }

    public String getDataPlatformServerURL() {
        return dataPlatformServerURL;
    }

    public void setDataPlatformServerURL(String dataPlatformServerURL) {
        this.dataPlatformServerURL = dataPlatformServerURL;
    }

    public String getDataPlatformServerName() {
        return dataPlatformServerName;
    }

    public void setDataPlatformServerName(String dataPlatformServerName) {
        this.dataPlatformServerName = dataPlatformServerName;
    }

    public String getDataPlatformConnectionProvider() {
        return dataPlatformConnectionProvider;
    }

    public void setDataPlatformConnectionProvider(String dataPlatformConnectionProvider) {
        this.dataPlatformConnectionProvider = dataPlatformConnectionProvider;
    }

    public Connection getDataPlatformConnection() {
        return dataPlatformConnection;
    }

    public void setDataPlatformConnection(Connection dataPlatformConnection) {
        this.dataPlatformConnection = dataPlatformConnection;
    }

    public String getDataPlatformGUID() {
        return dataPlatformGUID;
    }

    public void setDataPlatformGUID(String dataPlatformGUID) {
        this.dataPlatformGUID = dataPlatformGUID;
    }

    public String getDataPlatformServiceOutTopicName() {
        return dataPlatformServiceOutTopicName;
    }

    public void setDataPlatformServiceOutTopicName(String dataPlatformServiceOutTopicName) {
        this.dataPlatformServiceOutTopicName = dataPlatformServiceOutTopicName;
    }

    public Connection getDataPlatformServiceOutTopic() {
        return dataPlatformServiceOutTopic;
    }

    public void setDataPlatformServiceOutTopic(Connection dataPlatformServiceOutTopic) {
        this.dataPlatformServiceOutTopic = dataPlatformServiceOutTopic;
    }

    public Map<String, Object> getDataPlatformConfig() {
        return dataPlatformConfig;
    }

    public void setDataPlatformConfig(Map<String, Object> dataPlatformConfig) {
        this.dataPlatformConfig = dataPlatformConfig;
    }

    /**
     * Convert all properties to a string.
     *
     * @return string description of object
     */
    @Override
    public String toString() {
        return "DataPlatformConfig{" +
                "dataPlatformServerURL='" + dataPlatformServerURL + '\'' +
                ", dataPlatformServerName='" + dataPlatformServerName + '\'' +
                ", dataPlatformConnectionProvider='" + dataPlatformConnectionProvider + '\'' +
                ", dataPlatformConnection=" + dataPlatformConnection +
                ", dataPlatformGUID='" + dataPlatformGUID + '\'' +
                ", dataPlatformServiceOutTopicName='" + dataPlatformServiceOutTopicName + '\'' +
                ", dataPlatformServiceOutTopic=" + dataPlatformServiceOutTopic +
                ", dataPlatformConfig=" + dataPlatformConfig +
                "} " + super.toString();
    }
}
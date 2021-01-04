/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;

import java.util.Map;


/**
 * The Data platform config class provides the configuration properties for the deprecated data-platform-services.  This function
 * is replaced by the new Data Integrator OMIS running in the Integration Daemon OMAG Server.
 */
public class DataPlatformServicesConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    /* Properties needed to call the access service REST APIs */
    private String      dataPlatformServerURL;
    private String      dataPlatformServerName;

    private String      dataPlatformConnectionProvider;
    private Connection  dataPlatformConnection;
    private String      dataPlatformGUID;

    /* Connection for topic that send out topics */
    private String      dataPlatformOmasInTopicName;
    private Connection  dataPlatformOmasInTopic;

    /* Properties for a data platform  */
    private Map<String, Object> dataPlatformConfig;


    /**
     * Default Constructor
     */
    public DataPlatformServicesConfig()
    {
    }

    /**
     * Default Constructor
     */
    public DataPlatformServicesConfig(String dataPlatformServerURL, String dataPlatformServerName, String dataPlatformConnectionProvider, Connection dataPlatformConnection, String dataPlatformGUID, String dataPlatformOmasInTopicName) {
        this.dataPlatformServerURL = dataPlatformServerURL;
        this.dataPlatformServerName = dataPlatformServerName;
        this.dataPlatformConnectionProvider = dataPlatformConnectionProvider;
        this.dataPlatformConnection = dataPlatformConnection;
        this.dataPlatformGUID = dataPlatformGUID;
        this.dataPlatformOmasInTopicName = dataPlatformOmasInTopicName;
    }

    /**
     * Constructor to set up properties in one go
     *
     * @param template object to copy
     */
    public DataPlatformServicesConfig(AdminServicesConfigHeader template, String dataPlatformServerURL, String dataPlatformServerName, String dataPlatformConnectionProvider, Connection dataPlatformConnection, String dataPlatformGUID, String dataPlatformOmasInTopicName) {
        super(template);
        this.dataPlatformServerURL = dataPlatformServerURL;
        this.dataPlatformServerName = dataPlatformServerName;
        this.dataPlatformConnectionProvider = dataPlatformConnectionProvider;
        this.dataPlatformConnection = dataPlatformConnection;
        this.dataPlatformGUID = dataPlatformGUID;
        this.dataPlatformOmasInTopicName = dataPlatformOmasInTopicName;
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

    public String getDataPlatformOmasInTopicName() {
        return dataPlatformOmasInTopicName;
    }

    public void setDataPlatformOmasInTopicName(String dataPlatformOmasInTopicName) {
        this.dataPlatformOmasInTopicName = dataPlatformOmasInTopicName;
    }

    public Connection getDataPlatformOmasInTopic() {
        return dataPlatformOmasInTopic;
    }

    public void setDataPlatformOmasInTopic(Connection dataPlatformOmasInTopic) {
        this.dataPlatformOmasInTopic = dataPlatformOmasInTopic;
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
        return "DataPlatformServicesConfig{" +
                "dataPlatformServerURL='" + dataPlatformServerURL + '\'' +
                ", dataPlatformServerName='" + dataPlatformServerName + '\'' +
                ", dataPlatformConnectionProvider='" + dataPlatformConnectionProvider + '\'' +
                ", dataPlatformConnection=" + dataPlatformConnection +
                ", dataPlatformGUID='" + dataPlatformGUID + '\'' +
                ", dataPlatformOmasInTopicName='" + dataPlatformOmasInTopicName + '\'' +
                ", dataPlatformOmasInTopic=" + dataPlatformOmasInTopic +
                ", dataPlatformConfig=" + dataPlatformConfig +
                "} " + super.toString();
    }
}
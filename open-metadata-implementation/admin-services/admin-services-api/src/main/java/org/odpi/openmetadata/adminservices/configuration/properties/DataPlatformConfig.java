/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adminservices.configuration.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;


/**
 * The Data platform config class provides the configuration properties for the data-platform-services.
 */
public class DataPlatformConfig extends AdminServicesConfigHeader
{

    private String      dataPlatformServerURL;
    private String      dataPlatformServerName;
    private String      dataPlatformGUID = null;
    private Connection  dataPlatformConnection;

    /* Connection for topic that send out topics */
    private String      dataPlatformServerOutTopicName;
    private Connection  dataPlatformServerOutTopic;


    /**
     * Default Constructor
     */
    public DataPlatformConfig()
    {
    }

    /**
     * Constructor to set up properties in one go
     *
     * @param template header
     * @param dataPlatformServerURL url
     * @param dataPlatformServerName server name
     * @param dataPlatformGUID unique identifier of the data platform
     * @param dataPlatformConnection connection to data platform
     * @param dataPlatformServerOutTopicName Data Platform Services out topic name
     * @param dataPlatformServerOutTopic Data Platform Services out topic connection
     */
    public DataPlatformConfig(AdminServicesConfigHeader template,
                              String                    dataPlatformServerURL,
                              String                    dataPlatformServerName,
                              String                    dataPlatformGUID,
                              Connection                dataPlatformConnection,
                              String                    dataPlatformServerOutTopicName,
                              Connection                dataPlatformServerOutTopic)
    {
        super(template);
        this.dataPlatformServerURL = dataPlatformServerURL;
        this.dataPlatformServerName = dataPlatformServerName;
        this.dataPlatformGUID = dataPlatformGUID;
        this.dataPlatformConnection = dataPlatformConnection;
        this.dataPlatformServerOutTopicName = dataPlatformServerOutTopicName;
        this.dataPlatformServerOutTopic = dataPlatformServerOutTopic;
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

    public String getDataPlatformGUID() {
        return dataPlatformGUID;
    }

    public void setDataPlatformGUID(String dataPlatformGUID) {
        this.dataPlatformGUID = dataPlatformGUID;
    }

    public Connection getDataPlatformConnection() {
        return dataPlatformConnection;
    }

    public void setDataPlatformConnection(Connection dataPlatformConnection) {
        this.dataPlatformConnection = dataPlatformConnection;
    }

    public String getDataPlatformServerOutTopicName() {
        return dataPlatformServerOutTopicName;
    }

    public void setDataPlatformServerOutTopicName(String dataPlatformServerOutTopicName) {
        this.dataPlatformServerOutTopicName = dataPlatformServerOutTopicName;
    }

    public Connection getDataPlatformServerOutTopic() {
        return dataPlatformServerOutTopic;
    }

    public void setDataPlatformServerOutTopic(Connection dataPlatformServerOutTopic) {
        this.dataPlatformServerOutTopic = dataPlatformServerOutTopic;
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
                ", dataPlatformGUID='" + dataPlatformGUID + '\'' +
                ", dataPlatformConnection=" + dataPlatformConnection +
                ", dataPlatformServerOutTopicName='" + dataPlatformServerOutTopicName + '\'' +
                ", dataPlatformServerOutTopic=" + dataPlatformServerOutTopic +
                "} " + super.toString();
    }
}

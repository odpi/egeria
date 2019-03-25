/* SPDX-License-Identifier: Apache 2.0 */
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
 * OpenLineageConfig provides the properties for the open-lineage-services.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenLineageConfig extends AdminServicesConfigHeader {

    private int openLineageId = 0;
    private String openLineageName;
    private String openLineageDescription;
    private String lineageServerURL;
    private String assetLineageOutTopicName;
    private Connection assetLineageOutTopicConnection;


    /**
     * Default constructor
     */
    public OpenLineageConfig() {
        super();
    }


    /**
     * Set up the default values for open lineage
     *
     * @param template fixed properties about open lineage
     */
    public OpenLineageConfig(OpenLineageConfig template) {
        super(template);

        if (template != null) {
            openLineageId = template.openLineageId;
            openLineageName = template.openLineageName;
            openLineageDescription = template.openLineageDescription;
            lineageServerURL = template.lineageServerURL;
            assetLineageOutTopicName = template.assetLineageOutTopicName;
            assetLineageOutTopicConnection = template.assetLineageOutTopicConnection;
        }
    }

    /**
     * Return the code number (ordinal) for this Open Lineage
     *
     * @return the code numner for Open Lineage component
     */
    public int getOpenLineageId() {
        return openLineageId;
    }

    /**
     * Set up the code number (ordinal) for the Open Lineage
     *
     * @param openLineageId int ordinal
     */
    public void setOpenLineageId(int openLineageId) {
        this.openLineageId = openLineageId;
    }

    /**
     *  Return the name of the Open Lineage Connector
     *
     * @return the name of the open lineage connector
     */
    public String getOpenLineageName() {
        return openLineageName;
    }

    /**
     * Set up the name of the Open Lineage Connector
     *
     * @param openLineageName connector name
     */
    public void setOpenLineageName(String openLineageName) {
        this.openLineageName = openLineageName;
    }

    /**
     * Return the short description of the Open Lineage Component.  The default value is in English but this can be changed.
     *
     * @return String description
     */
    public String getOpenLineageDescription() {
        return openLineageDescription;
    }

    /**
     * Set up the short description of the Open Lineage.
     *
     * @param openLineageDescription String description
     */
    public void setOpenLineageDescription(String openLineageDescription) {
        this.openLineageDescription = openLineageDescription;
    }


    /**
     * Return the URL for the Lineage Server used in the Governance Server Connector
     *
     * @return String URL
     */
    public String getLineageServerURL() {
        return lineageServerURL;
    }

    /**
     * Set up the URL for the Lineage Server used in the Governance Server Connector.
     *
     * @param lineageServerURL String for Governance Server URL
     */
    public void setLineageServerURL(String lineageServerURL) {
        this.lineageServerURL = lineageServerURL;
    }


    /**
     * Return the Input Topic Name for Open Lineage
     *
     * @return String Input Topic name
     */
    public String getAssetLineageOutTopicName() {
        return assetLineageOutTopicName;
    }

    /**
     * Set up the Open Lineage In Topic Name
     *
     * @param assetLineageOutTopicName String Open Lineage Name
     */
    public void setAssetLineageOutTopicName(String assetLineageOutTopicName) {
        this.assetLineageOutTopicName = assetLineageOutTopicName;
    }

    /**
     * Return the OCF Connection for the In Topic used to pass requests to this Open Lineage.
     * For example, the output topic of Governance Engine OMAS can be provided
     * (e.g. "open-metadata.access-services.GovernanceEngine.outTopic")
     *
     * @return  Connection for In Topic
     */
    public Connection getAssetLineageOutTopicConnection() {
        return assetLineageOutTopicConnection;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Open Lineage.
     *
     * @param assetLineageOutTopicConnection  Connection for In Topic
     */
    public void setAssetLineageOutTopicConnection(Connection assetLineageOutTopicConnection) {
        this.assetLineageOutTopicConnection = assetLineageOutTopicConnection;
    }



    @Override
    public String toString() {
        return "OpenLineageConfig{" +
                "openLineageId=" + openLineageId +
                ", openLineageName='" + openLineageName + '\'' +
                ", openLineageDescription='" + openLineageDescription + '\'' +
                ", lineageServerURL='" + lineageServerURL + '\'' +
                ", assetLineageOutTopicName='" + assetLineageOutTopicName + '\'' +
                ", assetLineageOutTopicConnection=" + assetLineageOutTopicConnection +
                '}';
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
        OpenLineageConfig that = (OpenLineageConfig) objectToCompare;
        return getOpenLineageId() == that.getOpenLineageId() &&
                Objects.equals(getOpenLineageName(), that.getOpenLineageName()) &&
                Objects.equals(getOpenLineageDescription(), that.getOpenLineageDescription()) &&
                Objects.equals(getLineageServerURL(), that.getLineageServerURL()) &&
                Objects.equals(getAssetLineageOutTopicConnection(), that.getAssetLineageOutTopicConnection()) &&
                Objects.equals(getAssetLineageOutTopicName(), that.getAssetLineageOutTopicName());
    }

    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(getOpenLineageId(), getOpenLineageName(),
                getOpenLineageDescription(),  getLineageServerURL(), getAssetLineageOutTopicConnection(),
                getAssetLineageOutTopicName());
    }
}
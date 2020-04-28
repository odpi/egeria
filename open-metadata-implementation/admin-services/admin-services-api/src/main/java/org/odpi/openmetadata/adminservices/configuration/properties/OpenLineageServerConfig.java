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
public class OpenLineageServerConfig extends AdminServicesConfigHeader
{
    private static final long    serialVersionUID = 1L;

    private int openLineageId = 0;
    private String openLineageName;
    private String openLineageDescription;
    private String lineageServerURL;
    private String inTopicName;
    private Connection inTopicConnection;
    private Connection lineageGraphConnection;

    /**
     * Default constructor
     */
    public OpenLineageServerConfig() {
        super();
    }


    /**
     * Set up the default values for open lineage
     *
     * @param template fixed properties about open lineage
     */
    public OpenLineageServerConfig(OpenLineageServerConfig template) {
        super(template);

        if (template != null) {
            openLineageId = template.openLineageId;
            openLineageName = template.openLineageName;
            openLineageDescription = template.openLineageDescription;
            lineageServerURL = template.lineageServerURL;
            inTopicName = template.inTopicName;
            inTopicConnection = template.inTopicConnection;
            lineageGraphConnection = template.lineageGraphConnection;
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
     * Return the name of the Open Lineage Connector
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
    public String getInTopicName() {
        return inTopicName;
    }

    /**
     * Set up the Open Lineage In Topic Name
     *
     * @param inTopicName String Open Lineage Name
     */
    public void setInTopicName(String inTopicName) {
        this.inTopicName = inTopicName;
    }

    /**
     * Return the OCF Connection for the In Topic used to pass requests to this Open Lineage.
     * For example, the output topic of Governance Engine OMAS can be provided
     * (e.g. "open-metadata.access-services.GovernanceEngine.outTopic")
     *
     * @return Connection for In Topic
     */
    public Connection getInTopicConnection() {
        return inTopicConnection;
    }

    /**
     * Set up the OCF Connection for the Out Topic used to pass requests to this Open Lineage.
     *
     * @param inTopicConnection Connection for In Topic
     */
    public void setInTopicConnection(Connection inTopicConnection) {
        this.inTopicConnection = inTopicConnection;
    }


    /**
     * Return the Connection for LineageGraph that will be used for Open Lineage
     *
     * @return Connection for lineageGraph
     */
    public Connection getLineageGraphConnection() {
        return lineageGraphConnection;
    }

    /**
     * Set up the Open Lineage Graph Connection
     *
     * @param lineageGraphConnection Connection for LineageGraph
     */
    public void setLineageGraphConnection(Connection lineageGraphConnection) {
        this.lineageGraphConnection = lineageGraphConnection;
    }

    @Override
    public String toString() {
        return "OpenLineageServerConfig{" +
                "openLineageId=" + openLineageId +
                ", openLineageName='" + openLineageName + '\'' +
                ", openLineageDescription='" + openLineageDescription + '\'' +
                ", lineageServerURL='" + lineageServerURL + '\'' +
                ", inTopicName='" + inTopicName + '\'' +
                ", inTopicConnection=" + inTopicConnection +
                ", openLineageBufferGraphConnection=" + lineageGraphConnection +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OpenLineageServerConfig that = (OpenLineageServerConfig) o;
        return openLineageId == that.openLineageId &&
                Objects.equals(openLineageName, that.openLineageName) &&
                Objects.equals(openLineageDescription, that.openLineageDescription) &&
                Objects.equals(lineageServerURL, that.lineageServerURL) &&
                Objects.equals(inTopicName, that.inTopicName) &&
                Objects.equals(inTopicConnection, that.inTopicConnection) &&
                Objects.equals(lineageGraphConnection, that.lineageGraphConnection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(openLineageId, openLineageName,
                openLineageDescription, lineageServerURL,
                inTopicName, inTopicConnection, lineageGraphConnection);
    }
}
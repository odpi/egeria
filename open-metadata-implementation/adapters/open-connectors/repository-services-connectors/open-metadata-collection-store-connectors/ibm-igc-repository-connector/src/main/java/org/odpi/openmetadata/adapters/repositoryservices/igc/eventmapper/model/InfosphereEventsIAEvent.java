/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.eventmapper.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class InfosphereEventsIAEvent extends InfosphereEvents {

    @JsonIgnore public static final String COL_CLASSIFIED = "IA_COLUMN_CLASSIFIED_EVENT";
    @JsonIgnore public static final String COL_ANALYZED = "IA_COLUMN_ANALYZED_EVENT";
    @JsonIgnore public static final String TBL_PUBLISHED = "IA_TABLE_RESULTS_PUBLISHED";

    /**
     * The 'projectRid' property indicates the Repository ID (RID) of the Information Analyzer project.
     */
    protected String projectRid;

    /**
     * The 'projectName' property indicates the name of the Information Analyzer project.
     * This property will only be present on IA_PROJECT_* eventTypes.
     */
    protected String projectName;

    /**
     * The 'user' property indicates the user who took the action on the Information Analyzer project.
     * This property will only be present on IA_PROJECT_* eventTypes.
     */
    protected String user;

    /**
     * The 'tamRid' property indicates the Repository ID (RID) of a Table Analysis Master object.
     * Unfortunately these objects are currently not queryable through IGC's REST API.
     */
    protected String tamRid;

    /**
     * The 'camRid' property indicates the Repository ID (RID) of a Column Analysis Master object.
     * Unfortunately these objects are currently not queryable through IGC's REST API.
     */
    protected String camRid;

    /**
     * The 'dataCollectionRid' property indicates the Repository ID (RID) of a table ('database_table' or 'data_file_record')
     * that has been published as part of the event.
     */
    protected String dataCollectionRid;

    /**
     * The 'dataFieldRid' property indicates the Repository ID (RID) of a field ('database_column' or 'data_file_field')
     * that has been analyzed or classified as part of the event.
     */
    protected String dataFieldRid;

    /** @see #projectRid */ @JsonProperty("projectRid") public String getProjectRid() { return this.projectRid; }
    /** @see #projectRid */ @JsonProperty("projectRid") public void setProjectRid(String projectRid) { this.projectRid = projectRid; }

    /** @see #projectName */ @JsonProperty("projectName") public String getProjectName() { return this.projectName; }
    /** @see #projectName */ @JsonProperty("projectName") public void setProjectName(String projectName) { this.projectName = projectName; }

    /** @see #user */ @JsonProperty("user") public String getUser() { return this.user; }
    /** @see #user */ @JsonProperty("user") public void setUser(String user) { this.user = user; }

    /** @see #tamRid */ @JsonProperty("tamRid") public String getTamRid() { return this.tamRid; }
    /** @see #tamRid */ @JsonProperty("tamRid") public void setTamRid(String tamRid) { this.tamRid = tamRid; }

    /** @see #camRid */ @JsonProperty("camRid") public String getCamRid() { return this.camRid; }
    /** @see #camRid */ @JsonProperty("camRid") public void setCamRid(String camRid) { this.camRid = camRid; }

    /** @see #dataCollectionRid */ @JsonProperty("dataCollectionRid") public String getDataCollectionRid() { return this.dataCollectionRid; }
    /** @see #dataCollectionRid */ @JsonProperty("dataCollectionRid") public void setDataCollectionRid(String dataCollectionRid) { this.dataCollectionRid = dataCollectionRid; }

    /** @see #dataFieldRid */ @JsonProperty("dataFieldRid") public String getDataFieldRid() { return this.dataFieldRid; }
    /** @see #dataFieldRid */ @JsonProperty("dataFieldRid") public void setDataFieldRid(String dataFieldRid) { this.dataFieldRid = dataFieldRid; }

}

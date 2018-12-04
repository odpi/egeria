/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'analytics_script' asset type in IGC, displayed as 'Data Science Script' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnalyticsScript extends MainObject {

    public static final String IGC_TYPE_ID = "analytics_script";

    /**
     * The 'analytics_project' property, displayed as 'Data Science Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsProject} object.
     */
    protected Reference analytics_project;

    /**
     * The 'data_file' property, displayed as 'Data File' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference data_file;

    /**
     * The 'system_equiv_id' property, displayed as 'System Equiv Id' in the IGC UI.
     */
    protected String system_equiv_id;

    /**
     * The 'first_published_date' property, displayed as 'First Published Date' in the IGC UI.
     */
    protected Date first_published_date;

    /**
     * The 'package_name' property, displayed as 'Package Name' in the IGC UI.
     */
    protected String package_name;

    /**
     * The 'logical_name' property, displayed as 'Logical Name' in the IGC UI.
     */
    protected String logical_name;

    /**
     * The 'url' property, displayed as 'URL' in the IGC UI.
     */
    protected String url;

    /**
     * The 'script_type' property, displayed as 'Script Type' in the IGC UI.
     */
    protected String script_type;


    /** @see #analytics_project */ @JsonProperty("analytics_project")  public Reference getAnalyticsProject() { return this.analytics_project; }
    /** @see #analytics_project */ @JsonProperty("analytics_project")  public void setAnalyticsProject(Reference analytics_project) { this.analytics_project = analytics_project; }

    /** @see #data_file */ @JsonProperty("data_file")  public Reference getDataFile() { return this.data_file; }
    /** @see #data_file */ @JsonProperty("data_file")  public void setDataFile(Reference data_file) { this.data_file = data_file; }

    /** @see #system_equiv_id */ @JsonProperty("system_equiv_id")  public String getSystemEquivId() { return this.system_equiv_id; }
    /** @see #system_equiv_id */ @JsonProperty("system_equiv_id")  public void setSystemEquivId(String system_equiv_id) { this.system_equiv_id = system_equiv_id; }

    /** @see #first_published_date */ @JsonProperty("first_published_date")  public Date getFirstPublishedDate() { return this.first_published_date; }
    /** @see #first_published_date */ @JsonProperty("first_published_date")  public void setFirstPublishedDate(Date first_published_date) { this.first_published_date = first_published_date; }

    /** @see #package_name */ @JsonProperty("package_name")  public String getPackageName() { return this.package_name; }
    /** @see #package_name */ @JsonProperty("package_name")  public void setPackageName(String package_name) { this.package_name = package_name; }

    /** @see #logical_name */ @JsonProperty("logical_name")  public String getLogicalName() { return this.logical_name; }
    /** @see #logical_name */ @JsonProperty("logical_name")  public void setLogicalName(String logical_name) { this.logical_name = logical_name; }

    /** @see #url */ @JsonProperty("url")  public String getTheUrl() { return this.url; }
    /** @see #url */ @JsonProperty("url")  public void setTheUrl(String url) { this.url = url; }

    /** @see #script_type */ @JsonProperty("script_type")  public String getScriptType() { return this.script_type; }
    /** @see #script_type */ @JsonProperty("script_type")  public void setScriptType(String script_type) { this.script_type = script_type; }


    public static final Boolean isAnalyticsScript(Object obj) { return (obj.getClass() == AnalyticsScript.class); }

}

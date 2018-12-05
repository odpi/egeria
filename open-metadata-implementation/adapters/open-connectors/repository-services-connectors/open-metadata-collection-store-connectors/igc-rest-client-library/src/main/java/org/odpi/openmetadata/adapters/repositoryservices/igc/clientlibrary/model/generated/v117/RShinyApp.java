/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 * POJO for the 'r_shiny_app' asset type in IGC, displayed as 'RShiny App' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class RShinyApp extends MainObject {

    public static final String IGC_TYPE_ID = "r_shiny_app";

    /**
     * The 'analytics_project' property, displayed as 'Data Science Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsProject} object.
     */
    protected Reference analytics_project;

    /**
     * The 'contains_r_shiny_app_object' property, displayed as 'RShiny App Objects' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link RShinyAppObject} objects.
     */
    protected ReferenceList contains_r_shiny_app_object;

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
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #analytics_project */ @JsonProperty("analytics_project")  public Reference getAnalyticsProject() { return this.analytics_project; }
    /** @see #analytics_project */ @JsonProperty("analytics_project")  public void setAnalyticsProject(Reference analytics_project) { this.analytics_project = analytics_project; }

    /** @see #contains_r_shiny_app_object */ @JsonProperty("contains_r_shiny_app_object")  public ReferenceList getContainsRShinyAppObject() { return this.contains_r_shiny_app_object; }
    /** @see #contains_r_shiny_app_object */ @JsonProperty("contains_r_shiny_app_object")  public void setContainsRShinyAppObject(ReferenceList contains_r_shiny_app_object) { this.contains_r_shiny_app_object = contains_r_shiny_app_object; }

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

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isRShinyApp(Object obj) { return (obj.getClass() == RShinyApp.class); }

}

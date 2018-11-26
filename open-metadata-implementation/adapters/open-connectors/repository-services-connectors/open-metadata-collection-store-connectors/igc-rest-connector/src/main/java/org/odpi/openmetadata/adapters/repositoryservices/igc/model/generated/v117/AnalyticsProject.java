/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'analytics_project' asset type in IGC, displayed as 'Data Science Project' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AnalyticsProject extends MainObject {

    public static final String IGC_TYPE_ID = "analytics_project";

    /**
     * The 'analytics_models' property, displayed as 'Data Science Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AnalyticsModel} objects.
     */
    protected ReferenceList analytics_models;

    /**
     * The 'notebooks' property, displayed as 'Notebooks' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Notebook} objects.
     */
    protected ReferenceList notebooks;

    /**
     * The 'r_shiny_apps' property, displayed as 'RShiny Apps' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link RShinyApp} objects.
     */
    protected ReferenceList r_shiny_apps;

    /**
     * The 'analytics_scripts' property, displayed as 'Data Science Scripts' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AnalyticsScript} objects.
     */
    protected ReferenceList analytics_scripts;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #analytics_models */ @JsonProperty("analytics_models")  public ReferenceList getAnalyticsModels() { return this.analytics_models; }
    /** @see #analytics_models */ @JsonProperty("analytics_models")  public void setAnalyticsModels(ReferenceList analytics_models) { this.analytics_models = analytics_models; }

    /** @see #notebooks */ @JsonProperty("notebooks")  public ReferenceList getNotebooks() { return this.notebooks; }
    /** @see #notebooks */ @JsonProperty("notebooks")  public void setNotebooks(ReferenceList notebooks) { this.notebooks = notebooks; }

    /** @see #r_shiny_apps */ @JsonProperty("r_shiny_apps")  public ReferenceList getRShinyApps() { return this.r_shiny_apps; }
    /** @see #r_shiny_apps */ @JsonProperty("r_shiny_apps")  public void setRShinyApps(ReferenceList r_shiny_apps) { this.r_shiny_apps = r_shiny_apps; }

    /** @see #analytics_scripts */ @JsonProperty("analytics_scripts")  public ReferenceList getAnalyticsScripts() { return this.analytics_scripts; }
    /** @see #analytics_scripts */ @JsonProperty("analytics_scripts")  public void setAnalyticsScripts(ReferenceList analytics_scripts) { this.analytics_scripts = analytics_scripts; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isAnalyticsProject(Object obj) { return (obj.getClass() == AnalyticsProject.class); }

}

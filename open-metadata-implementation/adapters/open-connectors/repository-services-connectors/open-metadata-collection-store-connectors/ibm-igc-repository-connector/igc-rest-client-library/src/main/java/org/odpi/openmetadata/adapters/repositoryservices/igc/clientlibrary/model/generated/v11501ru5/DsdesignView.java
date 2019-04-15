/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501ru5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code dsdesign_view} asset type in IGC, displayed as '{@literal Design View}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("dsdesign_view")
public class DsdesignView extends Reference {

    public static String getIgcTypeDisplayName() { return "Design View"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code has_canvas_annotation} property, displayed as '{@literal Has Canvas Annotation}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList has_canvas_annotation;

    /**
     * The {@code output_pins} property, displayed as '{@literal Output Pins}' in the IGC UI.
     */
    protected String output_pins;

    /**
     * The {@code input_pins} property, displayed as '{@literal Input Pins}' in the IGC UI.
     */
    protected String input_pins;

    /**
     * The {@code stage_types} property, displayed as '{@literal Stage Types}' in the IGC UI.
     */
    protected String stage_types;

    /**
     * The {@code of_job} property, displayed as '{@literal Of Job}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsjob} object.
     */
    protected Reference of_job;

    /**
     * The {@code next_id} property, displayed as '{@literal Next ID}' in the IGC UI.
     */
    protected Number next_id;

    /**
     * The {@code next_stage_id} property, displayed as '{@literal Next Stage ID}' in the IGC UI.
     */
    protected Number next_stage_id;

    /**
     * The {@code is_top_level} property, displayed as '{@literal Is Top Level}' in the IGC UI.
     */
    protected Boolean is_top_level;

    /**
     * The {@code snap_to_grid} property, displayed as '{@literal Snap To Grid}' in the IGC UI.
     */
    protected Number snap_to_grid;

    /**
     * The {@code stage_x_pos} property, displayed as '{@literal Stage X Pos}' in the IGC UI.
     */
    protected String stage_x_pos;

    /**
     * The {@code grid_lines} property, displayed as '{@literal Grid Lines}' in the IGC UI.
     */
    protected Number grid_lines;

    /**
     * The {@code zoom_value} property, displayed as '{@literal Zoom Value}' in the IGC UI.
     */
    protected Number zoom_value;

    /**
     * The {@code container_view_sizing} property, displayed as '{@literal Container View Sizing}' in the IGC UI.
     */
    protected String container_view_sizing;

    /**
     * The {@code stage_list} property, displayed as '{@literal Stage List}' in the IGC UI.
     */
    protected String stage_list;

    /**
     * The {@code stage_y_pos} property, displayed as '{@literal Stage Y Pos}' in the IGC UI.
     */
    protected String stage_y_pos;

    /**
     * The {@code stage_y_size} property, displayed as '{@literal Stage Y Size}' in the IGC UI.
     */
    protected String stage_y_size;

    /**
     * The {@code stage_x_size} property, displayed as '{@literal Stage X Size}' in the IGC UI.
     */
    protected String stage_x_size;

    /**
     * The {@code internal_id} property, displayed as '{@literal Internal ID}' in the IGC UI.
     */
    protected String internal_id;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

    /** @see #has_canvas_annotation */ @JsonProperty("has_canvas_annotation")  public ReferenceList getHasCanvasAnnotation() { return this.has_canvas_annotation; }
    /** @see #has_canvas_annotation */ @JsonProperty("has_canvas_annotation")  public void setHasCanvasAnnotation(ReferenceList has_canvas_annotation) { this.has_canvas_annotation = has_canvas_annotation; }

    /** @see #output_pins */ @JsonProperty("output_pins")  public String getOutputPins() { return this.output_pins; }
    /** @see #output_pins */ @JsonProperty("output_pins")  public void setOutputPins(String output_pins) { this.output_pins = output_pins; }

    /** @see #input_pins */ @JsonProperty("input_pins")  public String getInputPins() { return this.input_pins; }
    /** @see #input_pins */ @JsonProperty("input_pins")  public void setInputPins(String input_pins) { this.input_pins = input_pins; }

    /** @see #stage_types */ @JsonProperty("stage_types")  public String getStageTypes() { return this.stage_types; }
    /** @see #stage_types */ @JsonProperty("stage_types")  public void setStageTypes(String stage_types) { this.stage_types = stage_types; }

    /** @see #of_job */ @JsonProperty("of_job")  public Reference getOfJob() { return this.of_job; }
    /** @see #of_job */ @JsonProperty("of_job")  public void setOfJob(Reference of_job) { this.of_job = of_job; }

    /** @see #next_id */ @JsonProperty("next_id")  public Number getNextId() { return this.next_id; }
    /** @see #next_id */ @JsonProperty("next_id")  public void setNextId(Number next_id) { this.next_id = next_id; }

    /** @see #next_stage_id */ @JsonProperty("next_stage_id")  public Number getNextStageId() { return this.next_stage_id; }
    /** @see #next_stage_id */ @JsonProperty("next_stage_id")  public void setNextStageId(Number next_stage_id) { this.next_stage_id = next_stage_id; }

    /** @see #is_top_level */ @JsonProperty("is_top_level")  public Boolean getIsTopLevel() { return this.is_top_level; }
    /** @see #is_top_level */ @JsonProperty("is_top_level")  public void setIsTopLevel(Boolean is_top_level) { this.is_top_level = is_top_level; }

    /** @see #snap_to_grid */ @JsonProperty("snap_to_grid")  public Number getSnapToGrid() { return this.snap_to_grid; }
    /** @see #snap_to_grid */ @JsonProperty("snap_to_grid")  public void setSnapToGrid(Number snap_to_grid) { this.snap_to_grid = snap_to_grid; }

    /** @see #stage_x_pos */ @JsonProperty("stage_x_pos")  public String getStageXPos() { return this.stage_x_pos; }
    /** @see #stage_x_pos */ @JsonProperty("stage_x_pos")  public void setStageXPos(String stage_x_pos) { this.stage_x_pos = stage_x_pos; }

    /** @see #grid_lines */ @JsonProperty("grid_lines")  public Number getGridLines() { return this.grid_lines; }
    /** @see #grid_lines */ @JsonProperty("grid_lines")  public void setGridLines(Number grid_lines) { this.grid_lines = grid_lines; }

    /** @see #zoom_value */ @JsonProperty("zoom_value")  public Number getZoomValue() { return this.zoom_value; }
    /** @see #zoom_value */ @JsonProperty("zoom_value")  public void setZoomValue(Number zoom_value) { this.zoom_value = zoom_value; }

    /** @see #container_view_sizing */ @JsonProperty("container_view_sizing")  public String getContainerViewSizing() { return this.container_view_sizing; }
    /** @see #container_view_sizing */ @JsonProperty("container_view_sizing")  public void setContainerViewSizing(String container_view_sizing) { this.container_view_sizing = container_view_sizing; }

    /** @see #stage_list */ @JsonProperty("stage_list")  public String getStageList() { return this.stage_list; }
    /** @see #stage_list */ @JsonProperty("stage_list")  public void setStageList(String stage_list) { this.stage_list = stage_list; }

    /** @see #stage_y_pos */ @JsonProperty("stage_y_pos")  public String getStageYPos() { return this.stage_y_pos; }
    /** @see #stage_y_pos */ @JsonProperty("stage_y_pos")  public void setStageYPos(String stage_y_pos) { this.stage_y_pos = stage_y_pos; }

    /** @see #stage_y_size */ @JsonProperty("stage_y_size")  public String getStageYSize() { return this.stage_y_size; }
    /** @see #stage_y_size */ @JsonProperty("stage_y_size")  public void setStageYSize(String stage_y_size) { this.stage_y_size = stage_y_size; }

    /** @see #stage_x_size */ @JsonProperty("stage_x_size")  public String getStageXSize() { return this.stage_x_size; }
    /** @see #stage_x_size */ @JsonProperty("stage_x_size")  public void setStageXSize(String stage_x_size) { this.stage_x_size = stage_x_size; }

    /** @see #internal_id */ @JsonProperty("internal_id")  public String getInternalId() { return this.internal_id; }
    /** @see #internal_id */ @JsonProperty("internal_id")  public void setInternalId(String internal_id) { this.internal_id = internal_id; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "output_pins",
        "input_pins",
        "stage_types",
        "next_id",
        "next_stage_id",
        "is_top_level",
        "snap_to_grid",
        "stage_x_pos",
        "grid_lines",
        "zoom_value",
        "container_view_sizing",
        "stage_list",
        "stage_y_pos",
        "stage_y_size",
        "stage_x_size",
        "internal_id"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "output_pins",
        "input_pins",
        "stage_types",
        "stage_x_pos",
        "container_view_sizing",
        "stage_list",
        "stage_y_pos",
        "stage_y_size",
        "stage_x_size",
        "internal_id"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "has_canvas_annotation"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "has_canvas_annotation",
        "output_pins",
        "input_pins",
        "stage_types",
        "of_job",
        "next_id",
        "next_stage_id",
        "is_top_level",
        "snap_to_grid",
        "stage_x_pos",
        "grid_lines",
        "zoom_value",
        "container_view_sizing",
        "stage_list",
        "stage_y_pos",
        "stage_y_size",
        "stage_x_size",
        "internal_id"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDsdesignView(Object obj) { return (obj.getClass() == DsdesignView.class); }

}

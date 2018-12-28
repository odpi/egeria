/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'classification' asset type in IGC, displayed as 'Classification' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classification extends Reference {

    public static String getIgcTypeId() { return "classification"; }
    public static String getIgcTypeDisplayName() { return "Classification"; }

    /**
     * The 'data_class' property, displayed as 'Data Class' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClass} object.
     */
    protected Reference data_class;

    /**
     * The 'classifies_asset' property, displayed as 'Asset' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference classifies_asset;

    /**
     * The 'selected' property, displayed as 'Selected' in the IGC UI.
     */
    protected Boolean selected;

    /**
     * The 'detected' property, displayed as 'Detected' in the IGC UI.
     */
    protected Boolean detected;

    /**
     * The 'detectedState' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>Candidate (displayed in the UI as 'Candidate')</li>
     *     <li>Inferred (displayed in the UI as 'Inferred')</li>
     * </ul>
     */
    protected String detectedState;

    /**
     * The 'confidencePercent' property, displayed as 'Confidence (Percent)' in the IGC UI.
     */
    protected Number confidencePercent;

    /**
     * The 'threshold' property, displayed as 'Threshold' in the IGC UI.
     */
    protected Number threshold;

    /**
     * The 'date' property, displayed as 'Date' in the IGC UI.
     */
    protected Date date;

    /**
     * The 'column_analysis' property, displayed as 'Detected from Column Analysis' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList column_analysis;


    /** @see #data_class */ @JsonProperty("data_class")  public Reference getDataClass() { return this.data_class; }
    /** @see #data_class */ @JsonProperty("data_class")  public void setDataClass(Reference data_class) { this.data_class = data_class; }

    /** @see #classifies_asset */ @JsonProperty("classifies_asset")  public Reference getClassifiesAsset() { return this.classifies_asset; }
    /** @see #classifies_asset */ @JsonProperty("classifies_asset")  public void setClassifiesAsset(Reference classifies_asset) { this.classifies_asset = classifies_asset; }

    /** @see #selected */ @JsonProperty("selected")  public Boolean getSelected() { return this.selected; }
    /** @see #selected */ @JsonProperty("selected")  public void setSelected(Boolean selected) { this.selected = selected; }

    /** @see #detected */ @JsonProperty("detected")  public Boolean getDetected() { return this.detected; }
    /** @see #detected */ @JsonProperty("detected")  public void setDetected(Boolean detected) { this.detected = detected; }

    /** @see #detectedState */ @JsonProperty("detectedState")  public String getDetectedState() { return this.detectedState; }
    /** @see #detectedState */ @JsonProperty("detectedState")  public void setDetectedState(String detectedState) { this.detectedState = detectedState; }

    /** @see #confidencePercent */ @JsonProperty("confidencePercent")  public Number getConfidencePercent() { return this.confidencePercent; }
    /** @see #confidencePercent */ @JsonProperty("confidencePercent")  public void setConfidencePercent(Number confidencePercent) { this.confidencePercent = confidencePercent; }

    /** @see #threshold */ @JsonProperty("threshold")  public Number getThreshold() { return this.threshold; }
    /** @see #threshold */ @JsonProperty("threshold")  public void setThreshold(Number threshold) { this.threshold = threshold; }

    /** @see #date */ @JsonProperty("date")  public Date getDate() { return this.date; }
    /** @see #date */ @JsonProperty("date")  public void setDate(Date date) { this.date = date; }

    /** @see #column_analysis */ @JsonProperty("column_analysis")  public ReferenceList getColumnAnalysis() { return this.column_analysis; }
    /** @see #column_analysis */ @JsonProperty("column_analysis")  public void setColumnAnalysis(ReferenceList column_analysis) { this.column_analysis = column_analysis; }

    public static final Boolean canBeCreated() { return false; }
    public static final Boolean includesModificationDetails() { return false; }
    public static final ArrayList<String> NON_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("selected");
        add("detected");
        add("detectedState");
        add("confidencePercent");
        add("threshold");
        add("date");
    }};
    public static final ArrayList<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<String>() {{
        add("column_analysis");
    }};
    public static final ArrayList<String> ALL_PROPERTIES = new ArrayList<String>() {{
        add("data_class");
        add("classifies_asset");
        add("selected");
        add("detected");
        add("detectedState");
        add("confidencePercent");
        add("threshold");
        add("date");
        add("column_analysis");
    }};
    public static final List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static final List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static final List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static final Boolean isClassification(Object obj) { return (obj.getClass() == Classification.class); }

}

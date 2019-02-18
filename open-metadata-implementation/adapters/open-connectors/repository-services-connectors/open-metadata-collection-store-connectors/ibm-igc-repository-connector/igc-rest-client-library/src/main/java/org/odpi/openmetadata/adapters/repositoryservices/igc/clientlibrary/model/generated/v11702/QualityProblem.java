/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code quality_problem} asset type in IGC, displayed as '{@literal Quality Score Problem}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class QualityProblem extends Reference {

    public static String getIgcTypeId() { return "quality_problem"; }
    public static String getIgcTypeDisplayName() { return "Quality Score Problem"; }

    /**
     * The {@code qualityScore} property, displayed as '{@literal Quality Score}' in the IGC UI.
     */
    protected Number qualityScore;

    /**
     * The {@code QualityProblemTypeName} property, displayed as '{@literal Quality Problem Type Name}' in the IGC UI.
     */
    protected String QualityProblemTypeName;

    /**
     * The {@code details} property, displayed as '{@literal Details}' in the IGC UI.
     */
    protected String details;

    /**
     * The {@code confidence} property, displayed as '{@literal Confidence}' in the IGC UI.
     */
    protected Number confidence;

    /**
     * The {@code occurrences} property, displayed as '{@literal Occurrences}' in the IGC UI.
     */
    protected Number occurrences;

    /**
     * The {@code percent_occurrences} property, displayed as '{@literal Percent Occurrences}' in the IGC UI.
     */
    protected Number percent_occurrences;

    /**
     * The {@code QualityProblemTypeDescription} property, displayed as '{@literal Quality Problem Type Description}' in the IGC UI.
     */
    protected String QualityProblemTypeDescription;

    /**
     * The {@code Column} property, displayed as '{@literal Column}' in the IGC UI.
     */
    protected String Column;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #qualityScore */ @JsonProperty("qualityScore")  public Number getQualityscore() { return this.qualityScore; }
    /** @see #qualityScore */ @JsonProperty("qualityScore")  public void setQualityscore(Number qualityScore) { this.qualityScore = qualityScore; }

    /** @see #QualityProblemTypeName */ @JsonProperty("QualityProblemTypeName")  public String getQualityproblemtypename() { return this.QualityProblemTypeName; }
    /** @see #QualityProblemTypeName */ @JsonProperty("QualityProblemTypeName")  public void setQualityproblemtypename(String QualityProblemTypeName) { this.QualityProblemTypeName = QualityProblemTypeName; }

    /** @see #details */ @JsonProperty("details")  public String getDetails() { return this.details; }
    /** @see #details */ @JsonProperty("details")  public void setDetails(String details) { this.details = details; }

    /** @see #confidence */ @JsonProperty("confidence")  public Number getConfidence() { return this.confidence; }
    /** @see #confidence */ @JsonProperty("confidence")  public void setConfidence(Number confidence) { this.confidence = confidence; }

    /** @see #occurrences */ @JsonProperty("occurrences")  public Number getOccurrences() { return this.occurrences; }
    /** @see #occurrences */ @JsonProperty("occurrences")  public void setOccurrences(Number occurrences) { this.occurrences = occurrences; }

    /** @see #percent_occurrences */ @JsonProperty("percent_occurrences")  public Number getPercentOccurrences() { return this.percent_occurrences; }
    /** @see #percent_occurrences */ @JsonProperty("percent_occurrences")  public void setPercentOccurrences(Number percent_occurrences) { this.percent_occurrences = percent_occurrences; }

    /** @see #QualityProblemTypeDescription */ @JsonProperty("QualityProblemTypeDescription")  public String getQualityproblemtypedescription() { return this.QualityProblemTypeDescription; }
    /** @see #QualityProblemTypeDescription */ @JsonProperty("QualityProblemTypeDescription")  public void setQualityproblemtypedescription(String QualityProblemTypeDescription) { this.QualityProblemTypeDescription = QualityProblemTypeDescription; }

    /** @see #Column */ @JsonProperty("Column")  public String getColumn() { return this.Column; }
    /** @see #Column */ @JsonProperty("Column")  public void setColumn(String Column) { this.Column = Column; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "qualityScore",
        "QualityProblemTypeName",
        "details",
        "confidence",
        "occurrences",
        "percent_occurrences",
        "QualityProblemTypeDescription",
        "Column",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "QualityProblemTypeName",
        "details",
        "QualityProblemTypeDescription",
        "Column",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "qualityScore",
        "QualityProblemTypeName",
        "details",
        "confidence",
        "occurrences",
        "percent_occurrences",
        "QualityProblemTypeDescription",
        "Column",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isQualityProblem(Object obj) { return (obj.getClass() == QualityProblem.class); }

}

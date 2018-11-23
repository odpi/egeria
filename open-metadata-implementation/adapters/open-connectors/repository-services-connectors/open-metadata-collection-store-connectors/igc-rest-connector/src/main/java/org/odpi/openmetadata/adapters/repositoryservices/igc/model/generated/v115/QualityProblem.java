/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'quality_problem' asset type in IGC, displayed as 'Quality Score Problem' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class QualityProblem extends MainObject {

    public static final String IGC_TYPE_ID = "quality_problem";

    /**
     * The 'data_quality_score' property, displayed as 'Data Quality Score' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference data_quality_score;

    /**
     * The 'details' property, displayed as 'Details' in the IGC UI.
     */
    protected String details;

    /**
     * The 'confidence' property, displayed as 'Confidence' in the IGC UI.
     */
    protected Number confidence;

    /**
     * The 'occurrences' property, displayed as 'Occurrences' in the IGC UI.
     */
    protected Number occurrences;

    /**
     * The 'percent_occurrences' property, displayed as 'Percent Occurrences' in the IGC UI.
     */
    protected Number percent_occurrences;

    /**
     * The 'problem_type' property, displayed as 'Problem Type' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference problem_type;


    /** @see #data_quality_score */ @JsonProperty("data_quality_score")  public Reference getDataQualityScore() { return this.data_quality_score; }
    /** @see #data_quality_score */ @JsonProperty("data_quality_score")  public void setDataQualityScore(Reference data_quality_score) { this.data_quality_score = data_quality_score; }

    /** @see #details */ @JsonProperty("details")  public String getDetails() { return this.details; }
    /** @see #details */ @JsonProperty("details")  public void setDetails(String details) { this.details = details; }

    /** @see #confidence */ @JsonProperty("confidence")  public Number getConfidence() { return this.confidence; }
    /** @see #confidence */ @JsonProperty("confidence")  public void setConfidence(Number confidence) { this.confidence = confidence; }

    /** @see #occurrences */ @JsonProperty("occurrences")  public Number getOccurrences() { return this.occurrences; }
    /** @see #occurrences */ @JsonProperty("occurrences")  public void setOccurrences(Number occurrences) { this.occurrences = occurrences; }

    /** @see #percent_occurrences */ @JsonProperty("percent_occurrences")  public Number getPercentOccurrences() { return this.percent_occurrences; }
    /** @see #percent_occurrences */ @JsonProperty("percent_occurrences")  public void setPercentOccurrences(Number percent_occurrences) { this.percent_occurrences = percent_occurrences; }

    /** @see #problem_type */ @JsonProperty("problem_type")  public Reference getProblemType() { return this.problem_type; }
    /** @see #problem_type */ @JsonProperty("problem_type")  public void setProblemType(Reference problem_type) { this.problem_type = problem_type; }


    public static final Boolean isQualityProblem(Object obj) { return (obj.getClass() == QualityProblem.class); }

}

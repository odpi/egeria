/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

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
     * The 'qualityScore' property, displayed as 'Quality Score' in the IGC UI.
     */
    protected Number qualityScore;

    /**
     * The 'QualityProblemTypeName' property, displayed as 'Quality Problem Type Name' in the IGC UI.
     */
    protected String QualityProblemTypeName;

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
     * The 'QualityProblemTypeDescription' property, displayed as 'Quality Problem Type Description' in the IGC UI.
     */
    protected String QualityProblemTypeDescription;

    /**
     * The 'Column' property, displayed as 'Column' in the IGC UI.
     */
    protected String Column;


    /** @see #qualityScore */ @JsonProperty("qualityScore")  public Number getQualityScore() { return this.qualityScore; }
    /** @see #qualityScore */ @JsonProperty("qualityScore")  public void setQualityScore(Number qualityScore) { this.qualityScore = qualityScore; }

    /** @see #QualityProblemTypeName */ @JsonProperty("QualityProblemTypeName")  public String getQualityProblemTypeName() { return this.QualityProblemTypeName; }
    /** @see #QualityProblemTypeName */ @JsonProperty("QualityProblemTypeName")  public void setQualityProblemTypeName(String QualityProblemTypeName) { this.QualityProblemTypeName = QualityProblemTypeName; }

    /** @see #details */ @JsonProperty("details")  public String getDetails() { return this.details; }
    /** @see #details */ @JsonProperty("details")  public void setDetails(String details) { this.details = details; }

    /** @see #confidence */ @JsonProperty("confidence")  public Number getConfidence() { return this.confidence; }
    /** @see #confidence */ @JsonProperty("confidence")  public void setConfidence(Number confidence) { this.confidence = confidence; }

    /** @see #occurrences */ @JsonProperty("occurrences")  public Number getOccurrences() { return this.occurrences; }
    /** @see #occurrences */ @JsonProperty("occurrences")  public void setOccurrences(Number occurrences) { this.occurrences = occurrences; }

    /** @see #percent_occurrences */ @JsonProperty("percent_occurrences")  public Number getPercentOccurrences() { return this.percent_occurrences; }
    /** @see #percent_occurrences */ @JsonProperty("percent_occurrences")  public void setPercentOccurrences(Number percent_occurrences) { this.percent_occurrences = percent_occurrences; }

    /** @see #QualityProblemTypeDescription */ @JsonProperty("QualityProblemTypeDescription")  public String getQualityProblemTypeDescription() { return this.QualityProblemTypeDescription; }
    /** @see #QualityProblemTypeDescription */ @JsonProperty("QualityProblemTypeDescription")  public void setQualityProblemTypeDescription(String QualityProblemTypeDescription) { this.QualityProblemTypeDescription = QualityProblemTypeDescription; }

    /** @see #Column */ @JsonProperty("Column")  public String getColumn() { return this.Column; }
    /** @see #Column */ @JsonProperty("Column")  public void setColumn(String Column) { this.Column = Column; }


    public static final Boolean isQualityProblem(Object obj) { return (obj.getClass() == QualityProblem.class); }

}

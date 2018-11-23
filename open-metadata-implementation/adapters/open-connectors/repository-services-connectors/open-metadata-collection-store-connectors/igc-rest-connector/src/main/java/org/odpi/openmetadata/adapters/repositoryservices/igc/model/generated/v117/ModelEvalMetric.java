/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'model_eval_metric' asset type in IGC, displayed as 'Model Eval Metric' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ModelEvalMetric extends MainObject {

    public static final String IGC_TYPE_ID = "model_eval_metric";

    /**
     * The 'of_model' property, displayed as 'Of Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link AnalyticsModel} object.
     */
    protected Reference of_model;

    /**
     * The 'metric_type' property, displayed as 'Metric Type' in the IGC UI.
     */
    protected String metric_type;

    /**
     * The 'value' property, displayed as 'Value' in the IGC UI.
     */
    protected String value;

    /**
     * The 'eval_date' property, displayed as 'Eval Date' in the IGC UI.
     */
    protected Date eval_date;


    /** @see #of_model */ @JsonProperty("of_model")  public Reference getOfModel() { return this.of_model; }
    /** @see #of_model */ @JsonProperty("of_model")  public void setOfModel(Reference of_model) { this.of_model = of_model; }

    /** @see #metric_type */ @JsonProperty("metric_type")  public String getMetricType() { return this.metric_type; }
    /** @see #metric_type */ @JsonProperty("metric_type")  public void setMetricType(String metric_type) { this.metric_type = metric_type; }

    /** @see #value */ @JsonProperty("value")  public String getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(String value) { this.value = value; }

    /** @see #eval_date */ @JsonProperty("eval_date")  public Date getEvalDate() { return this.eval_date; }
    /** @see #eval_date */ @JsonProperty("eval_date")  public void setEvalDate(Date eval_date) { this.eval_date = eval_date; }


    public static final Boolean isModelEvalMetric(Object obj) { return (obj.getClass() == ModelEvalMetric.class); }

}

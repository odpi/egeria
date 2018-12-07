/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'logical_validation_rule' asset type in IGC, displayed as 'Logical Validation Rule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalValidationRule extends MainObject {

    public static final String IGC_TYPE_ID = "logical_validation_rule";

    /**
     * The 'logical_data_model' property, displayed as 'Logical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalDataModel} object.
     */
    protected Reference logical_data_model;

    /**
     * The 'used_by_entity_attributes' property, displayed as 'Used by Entity Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList used_by_entity_attributes;

    /**
     * The 'rule_expression' property, displayed as 'Rule Expression' in the IGC UI.
     */
    protected String rule_expression;

    /**
     * The 'rule_type' property, displayed as 'Rule Type' in the IGC UI.
     */
    protected String rule_type;

    /**
     * The 'top_element' property, displayed as 'Top Element' in the IGC UI.
     */
    protected String top_element;


    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public Reference getLogicalDataModel() { return this.logical_data_model; }
    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public void setLogicalDataModel(Reference logical_data_model) { this.logical_data_model = logical_data_model; }

    /** @see #used_by_entity_attributes */ @JsonProperty("used_by_entity_attributes")  public ReferenceList getUsedByEntityAttributes() { return this.used_by_entity_attributes; }
    /** @see #used_by_entity_attributes */ @JsonProperty("used_by_entity_attributes")  public void setUsedByEntityAttributes(ReferenceList used_by_entity_attributes) { this.used_by_entity_attributes = used_by_entity_attributes; }

    /** @see #rule_expression */ @JsonProperty("rule_expression")  public String getRuleExpression() { return this.rule_expression; }
    /** @see #rule_expression */ @JsonProperty("rule_expression")  public void setRuleExpression(String rule_expression) { this.rule_expression = rule_expression; }

    /** @see #rule_type */ @JsonProperty("rule_type")  public String getRuleType() { return this.rule_type; }
    /** @see #rule_type */ @JsonProperty("rule_type")  public void setRuleType(String rule_type) { this.rule_type = rule_type; }

    /** @see #top_element */ @JsonProperty("top_element")  public String getTopElement() { return this.top_element; }
    /** @see #top_element */ @JsonProperty("top_element")  public void setTopElement(String top_element) { this.top_element = top_element; }


    public static final Boolean isLogicalValidationRule(Object obj) { return (obj.getClass() == LogicalValidationRule.class); }

}

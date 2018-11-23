/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'mapping_filter' asset type in IGC, displayed as 'Mapping Filter' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MappingFilter extends MainObject {

    public static final String IGC_TYPE_ID = "mapping_filter";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected ArrayList<String> description;

    /**
     * The 'rule_expression' property, displayed as 'Rule Expression' in the IGC UI.
     */
    protected String rule_expression;


    /** @see #description */ @JsonProperty("description")  public ArrayList<String> getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(ArrayList<String> description) { this.description = description; }

    /** @see #rule_expression */ @JsonProperty("rule_expression")  public String getRuleExpression() { return this.rule_expression; }
    /** @see #rule_expression */ @JsonProperty("rule_expression")  public void setRuleExpression(String rule_expression) { this.rule_expression = rule_expression; }


    public static final Boolean isMappingFilter(Object obj) { return (obj.getClass() == MappingFilter.class); }

}

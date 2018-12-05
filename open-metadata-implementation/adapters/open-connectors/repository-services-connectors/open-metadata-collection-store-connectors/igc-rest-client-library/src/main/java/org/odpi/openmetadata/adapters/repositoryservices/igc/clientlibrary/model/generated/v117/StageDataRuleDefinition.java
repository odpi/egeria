/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'stage_data_rule_definition' asset type in IGC, displayed as 'Stage Data Rule Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StageDataRuleDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "stage_data_rule_definition";

    /**
     * The 'stage' property, displayed as 'Stage' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Stage} object.
     */
    protected Reference stage;

    /**
     * The 'stage_logic' property, displayed as 'Stage Logic' in the IGC UI.
     */
    protected String stage_logic;

    /**
     * The 'based_on_rule' property, displayed as 'Based on Rule' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PublishedDataRuleDefinition} object.
     */
    protected Reference based_on_rule;


    /** @see #stage */ @JsonProperty("stage")  public Reference getStage() { return this.stage; }
    /** @see #stage */ @JsonProperty("stage")  public void setStage(Reference stage) { this.stage = stage; }

    /** @see #stage_logic */ @JsonProperty("stage_logic")  public String getStageLogic() { return this.stage_logic; }
    /** @see #stage_logic */ @JsonProperty("stage_logic")  public void setStageLogic(String stage_logic) { this.stage_logic = stage_logic; }

    /** @see #based_on_rule */ @JsonProperty("based_on_rule")  public Reference getBasedOnRule() { return this.based_on_rule; }
    /** @see #based_on_rule */ @JsonProperty("based_on_rule")  public void setBasedOnRule(Reference based_on_rule) { this.based_on_rule = based_on_rule; }


    public static final Boolean isStageDataRuleDefinition(Object obj) { return (obj.getClass() == StageDataRuleDefinition.class); }

}

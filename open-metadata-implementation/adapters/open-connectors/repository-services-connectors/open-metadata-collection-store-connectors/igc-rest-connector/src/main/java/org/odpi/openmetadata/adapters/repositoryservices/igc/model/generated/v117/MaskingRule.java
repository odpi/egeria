/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'masking_rule' asset type in IGC, displayed as 'Masking Rule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MaskingRule extends MainObject {

    public static final String IGC_TYPE_ID = "masking_rule";

    /**
     * The 'rule_logic' property, displayed as 'Rule Logic' in the IGC UI.
     */
    protected String rule_logic;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #rule_logic */ @JsonProperty("rule_logic")  public String getRuleLogic() { return this.rule_logic; }
    /** @see #rule_logic */ @JsonProperty("rule_logic")  public void setRuleLogic(String rule_logic) { this.rule_logic = rule_logic; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isMaskingRule(Object obj) { return (obj.getClass() == MaskingRule.class); }

}

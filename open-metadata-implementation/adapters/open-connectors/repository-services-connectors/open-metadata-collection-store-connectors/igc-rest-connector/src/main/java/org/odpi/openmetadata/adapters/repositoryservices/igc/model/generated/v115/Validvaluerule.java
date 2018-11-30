/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'validvaluerule' asset type in IGC, displayed as 'ValidValueRule' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Validvaluerule extends MainObject {

    public static final String IGC_TYPE_ID = "validvaluerule";

    /**
     * The 'rule_code' property, displayed as 'Rule Code' in the IGC UI.
     */
    protected String rule_code;

    /**
     * The 'rule_type' property, displayed as 'Rule Type' in the IGC UI.
     */
    protected String rule_type;

    /**
     * The 'valid_value_list_of_contains_valid_values_inverse' property, displayed as 'Valid Value List Of Contains Valid Values Inverse' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ValidValueList2} objects.
     */
    protected ReferenceList valid_value_list_of_contains_valid_values_inverse;

    /**
     * The 'is_not' property, displayed as 'Is Not' in the IGC UI.
     */
    protected Boolean is_not;

    /**
     * The 'sequence' property, displayed as 'Sequence' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The 'is_case_sensitive' property, displayed as 'Is Case Sensitive' in the IGC UI.
     */
    protected Boolean is_case_sensitive;

    /**
     * The 'custom_attribute_def_of_has_valid_values_inverse' property, displayed as 'Custom Attribute Def Of Has Valid Values Inverse' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Customattributedef} objects.
     */
    protected ReferenceList custom_attribute_def_of_has_valid_values_inverse;


    /** @see #rule_code */ @JsonProperty("rule_code")  public String getRuleCode() { return this.rule_code; }
    /** @see #rule_code */ @JsonProperty("rule_code")  public void setRuleCode(String rule_code) { this.rule_code = rule_code; }

    /** @see #rule_type */ @JsonProperty("rule_type")  public String getRuleType() { return this.rule_type; }
    /** @see #rule_type */ @JsonProperty("rule_type")  public void setRuleType(String rule_type) { this.rule_type = rule_type; }

    /** @see #valid_value_list_of_contains_valid_values_inverse */ @JsonProperty("valid_value_list_of_contains_valid_values_inverse")  public ReferenceList getValidValueListOfContainsValidValuesInverse() { return this.valid_value_list_of_contains_valid_values_inverse; }
    /** @see #valid_value_list_of_contains_valid_values_inverse */ @JsonProperty("valid_value_list_of_contains_valid_values_inverse")  public void setValidValueListOfContainsValidValuesInverse(ReferenceList valid_value_list_of_contains_valid_values_inverse) { this.valid_value_list_of_contains_valid_values_inverse = valid_value_list_of_contains_valid_values_inverse; }

    /** @see #is_not */ @JsonProperty("is_not")  public Boolean getIsNot() { return this.is_not; }
    /** @see #is_not */ @JsonProperty("is_not")  public void setIsNot(Boolean is_not) { this.is_not = is_not; }

    /** @see #sequence */ @JsonProperty("sequence")  public Number getSequence() { return this.sequence; }
    /** @see #sequence */ @JsonProperty("sequence")  public void setSequence(Number sequence) { this.sequence = sequence; }

    /** @see #is_case_sensitive */ @JsonProperty("is_case_sensitive")  public Boolean getIsCaseSensitive() { return this.is_case_sensitive; }
    /** @see #is_case_sensitive */ @JsonProperty("is_case_sensitive")  public void setIsCaseSensitive(Boolean is_case_sensitive) { this.is_case_sensitive = is_case_sensitive; }

    /** @see #custom_attribute_def_of_has_valid_values_inverse */ @JsonProperty("custom_attribute_def_of_has_valid_values_inverse")  public ReferenceList getCustomAttributeDefOfHasValidValuesInverse() { return this.custom_attribute_def_of_has_valid_values_inverse; }
    /** @see #custom_attribute_def_of_has_valid_values_inverse */ @JsonProperty("custom_attribute_def_of_has_valid_values_inverse")  public void setCustomAttributeDefOfHasValidValuesInverse(ReferenceList custom_attribute_def_of_has_valid_values_inverse) { this.custom_attribute_def_of_has_valid_values_inverse = custom_attribute_def_of_has_valid_values_inverse; }


    public static final Boolean isValidvaluerule(Object obj) { return (obj.getClass() == Validvaluerule.class); }

}

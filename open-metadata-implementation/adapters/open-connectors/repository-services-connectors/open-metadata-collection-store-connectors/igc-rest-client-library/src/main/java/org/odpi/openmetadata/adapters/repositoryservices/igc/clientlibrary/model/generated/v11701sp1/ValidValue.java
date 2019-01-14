/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'valid_value' asset type in IGC, displayed as 'Valid Value' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidValue extends Reference {

    public static String getIgcTypeId() { return "valid_value"; }
    public static String getIgcTypeDisplayName() { return "Valid Value"; }

    /**
     * The 'rule_component' property, displayed as 'Rule Component' in the IGC UI.
     */
    protected String rule_component;

    /**
     * The 'rule_type' property, displayed as 'Rule Type' in the IGC UI.
     */
    protected String rule_type;

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'valid_value_list' property, displayed as 'Valid Value List' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ValidValueList} object.
     */
    protected Reference valid_value_list;

    /**
     * The 'design_column' property, displayed as 'Design Column' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList design_column;


    /** @see #rule_component */ @JsonProperty("rule_component")  public String getRuleComponent() { return this.rule_component; }
    /** @see #rule_component */ @JsonProperty("rule_component")  public void setRuleComponent(String rule_component) { this.rule_component = rule_component; }

    /** @see #rule_type */ @JsonProperty("rule_type")  public String getRuleType() { return this.rule_type; }
    /** @see #rule_type */ @JsonProperty("rule_type")  public void setRuleType(String rule_type) { this.rule_type = rule_type; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #valid_value_list */ @JsonProperty("valid_value_list")  public Reference getValidValueList() { return this.valid_value_list; }
    /** @see #valid_value_list */ @JsonProperty("valid_value_list")  public void setValidValueList(Reference valid_value_list) { this.valid_value_list = valid_value_list; }

    /** @see #design_column */ @JsonProperty("design_column")  public ReferenceList getDesignColumn() { return this.design_column; }
    /** @see #design_column */ @JsonProperty("design_column")  public void setDesignColumn(ReferenceList design_column) { this.design_column = design_column; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "rule_component",
        "rule_type",
        "name",
        "short_description"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "design_column"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "rule_component",
        "rule_type",
        "name",
        "short_description",
        "valid_value_list",
        "design_column"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isValidValue(Object obj) { return (obj.getClass() == ValidValue.class); }

}

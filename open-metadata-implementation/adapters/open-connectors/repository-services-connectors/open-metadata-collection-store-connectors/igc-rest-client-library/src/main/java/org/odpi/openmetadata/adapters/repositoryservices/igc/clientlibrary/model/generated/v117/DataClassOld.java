/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_class_old' asset type in IGC, displayed as 'Data Class' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataClassOld extends Reference {

    public static String getIgcTypeId() { return "data_class_old"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The 'is_sub_of_data_class' property, displayed as 'Is Sub Of Data Class' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClassOld} object.
     */
    protected Reference is_sub_of_data_class;

    /**
     * The 'classifies_data_field' property, displayed as 'Classifies Data Field' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList classifies_data_field;

    /**
     * The 'has_sub_data_class' property, displayed as 'Has Sub Data Class' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataClassOld} objects.
     */
    protected ReferenceList has_sub_data_class;

    /**
     * The 'class_code' property, displayed as 'Class Code' in the IGC UI.
     */
    protected String class_code;

    /**
     * The 'inferred_by_df_analysis_summary' property, displayed as 'Inferred By DF Analysis Summary' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ColumnAnalysisSummary} objects.
     */
    protected ReferenceList inferred_by_df_analysis_summary;

    /**
     * The 'is_user_defined' property, displayed as 'Is User Defined' in the IGC UI.
     */
    protected Boolean is_user_defined;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

    /** @see #is_sub_of_data_class */ @JsonProperty("is_sub_of_data_class")  public Reference getIsSubOfDataClass() { return this.is_sub_of_data_class; }
    /** @see #is_sub_of_data_class */ @JsonProperty("is_sub_of_data_class")  public void setIsSubOfDataClass(Reference is_sub_of_data_class) { this.is_sub_of_data_class = is_sub_of_data_class; }

    /** @see #classifies_data_field */ @JsonProperty("classifies_data_field")  public ReferenceList getClassifiesDataField() { return this.classifies_data_field; }
    /** @see #classifies_data_field */ @JsonProperty("classifies_data_field")  public void setClassifiesDataField(ReferenceList classifies_data_field) { this.classifies_data_field = classifies_data_field; }

    /** @see #has_sub_data_class */ @JsonProperty("has_sub_data_class")  public ReferenceList getHasSubDataClass() { return this.has_sub_data_class; }
    /** @see #has_sub_data_class */ @JsonProperty("has_sub_data_class")  public void setHasSubDataClass(ReferenceList has_sub_data_class) { this.has_sub_data_class = has_sub_data_class; }

    /** @see #class_code */ @JsonProperty("class_code")  public String getClassCode() { return this.class_code; }
    /** @see #class_code */ @JsonProperty("class_code")  public void setClassCode(String class_code) { this.class_code = class_code; }

    /** @see #inferred_by_df_analysis_summary */ @JsonProperty("inferred_by_df_analysis_summary")  public ReferenceList getInferredByDfAnalysisSummary() { return this.inferred_by_df_analysis_summary; }
    /** @see #inferred_by_df_analysis_summary */ @JsonProperty("inferred_by_df_analysis_summary")  public void setInferredByDfAnalysisSummary(ReferenceList inferred_by_df_analysis_summary) { this.inferred_by_df_analysis_summary = inferred_by_df_analysis_summary; }

    /** @see #is_user_defined */ @JsonProperty("is_user_defined")  public Boolean getIsUserDefined() { return this.is_user_defined; }
    /** @see #is_user_defined */ @JsonProperty("is_user_defined")  public void setIsUserDefined(Boolean is_user_defined) { this.is_user_defined = is_user_defined; }

    public static final Boolean isDataClassOld(Object obj) { return (obj.getClass() == DataClassOld.class); }

}

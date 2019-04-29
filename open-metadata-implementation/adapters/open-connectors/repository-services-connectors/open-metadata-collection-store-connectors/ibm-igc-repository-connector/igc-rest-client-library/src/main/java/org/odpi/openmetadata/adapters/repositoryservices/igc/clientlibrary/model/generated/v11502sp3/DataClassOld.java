/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11502sp3;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code data_class_old} asset type in IGC, displayed as '{@literal Data Class}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("data_class_old")
public class DataClassOld extends Reference {

    public static String getIgcTypeDisplayName() { return "Data Class"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code long_description} property, displayed as '{@literal Long Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code labels} property, displayed as '{@literal Labels}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The {@code stewards} property, displayed as '{@literal Stewards}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The {@code assigned_to_terms} property, displayed as '{@literal Assigned to Terms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code is_sub_of_data_class} property, displayed as '{@literal Is Sub Of Data Class}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClassOld} object.
     */
    protected Reference is_sub_of_data_class;

    /**
     * The {@code classifies_data_field} property, displayed as '{@literal Classifies Data Field}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList classifies_data_field;

    /**
     * The {@code has_sub_data_class} property, displayed as '{@literal Has Sub Data Class}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataClassOld} objects.
     */
    protected ReferenceList has_sub_data_class;

    /**
     * The {@code class_code} property, displayed as '{@literal Class Code}' in the IGC UI.
     */
    protected String class_code;

    /**
     * The {@code inferred_by_df_analysis_summary} property, displayed as '{@literal Inferred By DF Analysis Summary}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ColumnAnalysisSummary} objects.
     */
    protected ReferenceList inferred_by_df_analysis_summary;

    /**
     * The {@code is_user_defined} property, displayed as '{@literal Is User Defined}' in the IGC UI.
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

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "class_code",
        "is_user_defined"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "class_code"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "classifies_data_field",
        "has_sub_data_class",
        "inferred_by_df_analysis_summary"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "is_sub_of_data_class",
        "classifies_data_field",
        "has_sub_data_class",
        "class_code",
        "inferred_by_df_analysis_summary",
        "is_user_defined"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDataClassOld(Object obj) { return (obj.getClass() == DataClassOld.class); }

}

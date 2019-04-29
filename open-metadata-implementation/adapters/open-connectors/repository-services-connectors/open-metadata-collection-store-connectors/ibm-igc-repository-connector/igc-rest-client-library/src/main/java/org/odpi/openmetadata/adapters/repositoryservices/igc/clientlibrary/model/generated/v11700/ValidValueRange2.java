/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

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
 * POJO for the {@code validvaluerange} asset type in IGC, displayed as '{@literal ValidValueRange}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("validvaluerange")
public class ValidValueRange2 extends Reference {

    public static String getIgcTypeDisplayName() { return "ValidValueRange"; }

    /**
     * The {@code is_max_inclusive} property, displayed as '{@literal Is Max Inclusive}' in the IGC UI.
     */
    protected Boolean is_max_inclusive;

    /**
     * The {@code minimum_value} property, displayed as '{@literal Minimum Value}' in the IGC UI.
     */
    protected String minimum_value;

    /**
     * The {@code is_min_inclusive} property, displayed as '{@literal Is Min Inclusive}' in the IGC UI.
     */
    protected Boolean is_min_inclusive;

    /**
     * The {@code maximum_value} property, displayed as '{@literal Maximum Value}' in the IGC UI.
     */
    protected String maximum_value;

    /**
     * The {@code valid_value_list_of_contains_valid_values_inverse} property, displayed as '{@literal Valid Value List Of Contains Valid Values Inverse}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ValidValueList2} objects.
     */
    protected ReferenceList valid_value_list_of_contains_valid_values_inverse;

    /**
     * The {@code is_not} property, displayed as '{@literal Is Not}' in the IGC UI.
     */
    protected Boolean is_not;

    /**
     * The {@code sequence} property, displayed as '{@literal Sequence}' in the IGC UI.
     */
    protected Number sequence;

    /**
     * The {@code is_case_sensitive} property, displayed as '{@literal Is Case Sensitive}' in the IGC UI.
     */
    protected Boolean is_case_sensitive;

    /**
     * The {@code custom_attribute_def_of_has_valid_values_inverse} property, displayed as '{@literal Custom Attribute Def Of Has Valid Values Inverse}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList custom_attribute_def_of_has_valid_values_inverse;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #is_max_inclusive */ @JsonProperty("is_max_inclusive")  public Boolean getIsMaxInclusive() { return this.is_max_inclusive; }
    /** @see #is_max_inclusive */ @JsonProperty("is_max_inclusive")  public void setIsMaxInclusive(Boolean is_max_inclusive) { this.is_max_inclusive = is_max_inclusive; }

    /** @see #minimum_value */ @JsonProperty("minimum_value")  public String getMinimumValue() { return this.minimum_value; }
    /** @see #minimum_value */ @JsonProperty("minimum_value")  public void setMinimumValue(String minimum_value) { this.minimum_value = minimum_value; }

    /** @see #is_min_inclusive */ @JsonProperty("is_min_inclusive")  public Boolean getIsMinInclusive() { return this.is_min_inclusive; }
    /** @see #is_min_inclusive */ @JsonProperty("is_min_inclusive")  public void setIsMinInclusive(Boolean is_min_inclusive) { this.is_min_inclusive = is_min_inclusive; }

    /** @see #maximum_value */ @JsonProperty("maximum_value")  public String getMaximumValue() { return this.maximum_value; }
    /** @see #maximum_value */ @JsonProperty("maximum_value")  public void setMaximumValue(String maximum_value) { this.maximum_value = maximum_value; }

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

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "is_max_inclusive",
        "minimum_value",
        "is_min_inclusive",
        "maximum_value",
        "is_not",
        "sequence",
        "is_case_sensitive",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "minimum_value",
        "maximum_value",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "valid_value_list_of_contains_valid_values_inverse",
        "custom_attribute_def_of_has_valid_values_inverse"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "is_max_inclusive",
        "minimum_value",
        "is_min_inclusive",
        "maximum_value",
        "valid_value_list_of_contains_valid_values_inverse",
        "is_not",
        "sequence",
        "is_case_sensitive",
        "custom_attribute_def_of_has_valid_values_inverse",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isValidValueRange2(Object obj) { return (obj.getClass() == ValidValueRange2.class); }

}

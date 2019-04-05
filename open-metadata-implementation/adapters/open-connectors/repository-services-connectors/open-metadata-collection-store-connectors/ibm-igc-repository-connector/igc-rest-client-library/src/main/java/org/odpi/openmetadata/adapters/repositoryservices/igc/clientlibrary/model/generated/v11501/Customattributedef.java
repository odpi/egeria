/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code customattributedef} asset type in IGC, displayed as '{@literal CustomAttributeDef}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Customattributedef extends Reference {

    public static String getIgcTypeId() { return "customattributedef"; }
    public static String getIgcTypeDisplayName() { return "CustomAttributeDef"; }

    /**
     * The {@code has_valid_values} property, displayed as '{@literal Has Valid Values}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Validvalues} object.
     */
    protected Reference has_valid_values;

    /**
     * The {@code of_class_descriptor} property, displayed as '{@literal Of Class Descriptor}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Classdescriptor} object.
     */
    protected Reference of_class_descriptor;

    /**
     * The {@code has_custom_attribute_val} property, displayed as '{@literal Has Custom Attribute Val}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Customattributeval} objects.
     */
    protected ReferenceList has_custom_attribute_val;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String description;

    /**
     * The {@code data_type} property, displayed as '{@literal Data Type}' in the IGC UI.
     */
    protected String data_type;

    /**
     * The {@code has_data_values} property, displayed as '{@literal Has Data Values}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataItemValue} object.
     */
    protected Reference has_data_values;

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

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


    /** @see #has_valid_values */ @JsonProperty("has_valid_values")  public Reference getHasValidValues() { return this.has_valid_values; }
    /** @see #has_valid_values */ @JsonProperty("has_valid_values")  public void setHasValidValues(Reference has_valid_values) { this.has_valid_values = has_valid_values; }

    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public Reference getOfClassDescriptor() { return this.of_class_descriptor; }
    /** @see #of_class_descriptor */ @JsonProperty("of_class_descriptor")  public void setOfClassDescriptor(Reference of_class_descriptor) { this.of_class_descriptor = of_class_descriptor; }

    /** @see #has_custom_attribute_val */ @JsonProperty("has_custom_attribute_val")  public ReferenceList getHasCustomAttributeVal() { return this.has_custom_attribute_val; }
    /** @see #has_custom_attribute_val */ @JsonProperty("has_custom_attribute_val")  public void setHasCustomAttributeVal(ReferenceList has_custom_attribute_val) { this.has_custom_attribute_val = has_custom_attribute_val; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #has_data_values */ @JsonProperty("has_data_values")  public Reference getHasDataValues() { return this.has_data_values; }
    /** @see #has_data_values */ @JsonProperty("has_data_values")  public void setHasDataValues(Reference has_data_values) { this.has_data_values = has_data_values; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

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
        "description",
        "data_type",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "description",
        "data_type",
        "name",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "has_custom_attribute_val"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "has_valid_values",
        "of_class_descriptor",
        "has_custom_attribute_val",
        "description",
        "data_type",
        "has_data_values",
        "name",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isCustomattributedef(Object obj) { return (obj.getClass() == Customattributedef.class); }

}

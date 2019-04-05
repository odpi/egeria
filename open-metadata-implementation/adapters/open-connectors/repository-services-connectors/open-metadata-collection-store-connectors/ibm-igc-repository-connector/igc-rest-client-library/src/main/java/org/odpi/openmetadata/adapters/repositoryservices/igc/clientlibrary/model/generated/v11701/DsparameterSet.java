/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code dsparameter_set} asset type in IGC, displayed as '{@literal Parameter}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DsparameterSet extends Reference {

    public static String getIgcTypeId() { return "dsparameter_set"; }
    public static String getIgcTypeDisplayName() { return "Parameter"; }

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
     * The {@code of_parameter_set} property, displayed as '{@literal Context}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link ParameterSet2} object.
     */
    protected Reference of_parameter_set;

    /**
     * The {@code display_caption} property, displayed as '{@literal Display Caption}' in the IGC UI.
     */
    protected String display_caption;

    /**
     * The {@code default_value} property, displayed as '{@literal Default Value}' in the IGC UI.
     */
    protected String default_value;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>UNUSED (displayed in the UI as 'UNUSED')</li>
     *     <li>ENCRYPTED (displayed in the UI as 'ENCRYPTED')</li>
     *     <li>PATHNAME (displayed in the UI as 'PATHNAME')</li>
     *     <li>STRINGLIST (displayed in the UI as 'STRINGLIST')</li>
     *     <li>INPUTCOL (displayed in the UI as 'INPUTCOL')</li>
     *     <li>OUTPUTCOL (displayed in the UI as 'OUTPUTCOL')</li>
     *     <li>NCHAR (displayed in the UI as 'NCHAR')</li>
     *     <li>PARAMETERSET (displayed in the UI as 'PARAMETERSET')</li>
     * </ul>
     */
    protected String type;

    /**
     * The {@code help_text} property, displayed as '{@literal Help Text}' in the IGC UI.
     */
    protected String help_text;

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


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #of_parameter_set */ @JsonProperty("of_parameter_set")  public Reference getOfParameterSet() { return this.of_parameter_set; }
    /** @see #of_parameter_set */ @JsonProperty("of_parameter_set")  public void setOfParameterSet(Reference of_parameter_set) { this.of_parameter_set = of_parameter_set; }

    /** @see #display_caption */ @JsonProperty("display_caption")  public String getDisplayCaption() { return this.display_caption; }
    /** @see #display_caption */ @JsonProperty("display_caption")  public void setDisplayCaption(String display_caption) { this.display_caption = display_caption; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #help_text */ @JsonProperty("help_text")  public String getHelpText() { return this.help_text; }
    /** @see #help_text */ @JsonProperty("help_text")  public void setHelpText(String help_text) { this.help_text = help_text; }

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
        "name",
        "short_description",
        "long_description",
        "display_caption",
        "default_value",
        "type",
        "help_text",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "display_caption",
        "default_value",
        "help_text",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "of_parameter_set",
        "display_caption",
        "default_value",
        "type",
        "help_text",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDsparameterSet(Object obj) { return (obj.getClass() == DsparameterSet.class); }

}

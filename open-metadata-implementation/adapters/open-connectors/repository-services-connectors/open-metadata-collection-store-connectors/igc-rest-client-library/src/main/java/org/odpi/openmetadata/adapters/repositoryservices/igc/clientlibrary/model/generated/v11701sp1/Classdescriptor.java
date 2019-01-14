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
 * POJO for the 'classdescriptor' asset type in IGC, displayed as 'ClassDescriptor' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classdescriptor extends Reference {

    public static String getIgcTypeId() { return "classdescriptor"; }
    public static String getIgcTypeDisplayName() { return "ClassDescriptor"; }

    /**
     * The 'identifier_attribute' property, displayed as 'Identifier Attribute' in the IGC UI.
     */
    protected String identifier_attribute;

    /**
     * The 'class_name' property, displayed as 'Class Name' in the IGC UI.
     */
    protected String class_name;

    /**
     * The 'used_in_application_function' property, displayed as 'Used In Application Function' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Applicationfunction} objects.
     */
    protected ReferenceList used_in_application_function;

    /**
     * The 'long_description_attribute' property, displayed as 'Long Description Attribute' in the IGC UI.
     */
    protected String long_description_attribute;

    /**
     * The 'has_navigation_descriptor' property, displayed as 'Has Navigation Descriptor' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Navigationdescriptor} objects.
     */
    protected ReferenceList has_navigation_descriptor;

    /**
     * The 'has_prop_descriptor' property, displayed as 'Has Prop Descriptor' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Propdescriptor} objects.
     */
    protected ReferenceList has_prop_descriptor;

    /**
     * The 'model_name' property, displayed as 'Model Name' in the IGC UI.
     */
    protected String model_name;

    /**
     * The 'has_custom_attribute_def' property, displayed as 'Has Custom Attribute Def' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList has_custom_attribute_def;

    /**
     * The 'model_uri' property, displayed as 'Model URI' in the IGC UI.
     */
    protected String model_uri;

    /**
     * The 'short_description_attribute' property, displayed as 'Short Description Attribute' in the IGC UI.
     */
    protected String short_description_attribute;

    /**
     * The 'has_action_descriptor' property, displayed as 'Has Action Descriptor' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Actiondescriptor} objects.
     */
    protected ReferenceList has_action_descriptor;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #identifier_attribute */ @JsonProperty("identifier_attribute")  public String getIdentifierAttribute() { return this.identifier_attribute; }
    /** @see #identifier_attribute */ @JsonProperty("identifier_attribute")  public void setIdentifierAttribute(String identifier_attribute) { this.identifier_attribute = identifier_attribute; }

    /** @see #class_name */ @JsonProperty("class_name")  public String getClassName() { return this.class_name; }
    /** @see #class_name */ @JsonProperty("class_name")  public void setClassName(String class_name) { this.class_name = class_name; }

    /** @see #used_in_application_function */ @JsonProperty("used_in_application_function")  public ReferenceList getUsedInApplicationFunction() { return this.used_in_application_function; }
    /** @see #used_in_application_function */ @JsonProperty("used_in_application_function")  public void setUsedInApplicationFunction(ReferenceList used_in_application_function) { this.used_in_application_function = used_in_application_function; }

    /** @see #long_description_attribute */ @JsonProperty("long_description_attribute")  public String getLongDescriptionAttribute() { return this.long_description_attribute; }
    /** @see #long_description_attribute */ @JsonProperty("long_description_attribute")  public void setLongDescriptionAttribute(String long_description_attribute) { this.long_description_attribute = long_description_attribute; }

    /** @see #has_navigation_descriptor */ @JsonProperty("has_navigation_descriptor")  public ReferenceList getHasNavigationDescriptor() { return this.has_navigation_descriptor; }
    /** @see #has_navigation_descriptor */ @JsonProperty("has_navigation_descriptor")  public void setHasNavigationDescriptor(ReferenceList has_navigation_descriptor) { this.has_navigation_descriptor = has_navigation_descriptor; }

    /** @see #has_prop_descriptor */ @JsonProperty("has_prop_descriptor")  public ReferenceList getHasPropDescriptor() { return this.has_prop_descriptor; }
    /** @see #has_prop_descriptor */ @JsonProperty("has_prop_descriptor")  public void setHasPropDescriptor(ReferenceList has_prop_descriptor) { this.has_prop_descriptor = has_prop_descriptor; }

    /** @see #model_name */ @JsonProperty("model_name")  public String getModelName() { return this.model_name; }
    /** @see #model_name */ @JsonProperty("model_name")  public void setModelName(String model_name) { this.model_name = model_name; }

    /** @see #has_custom_attribute_def */ @JsonProperty("has_custom_attribute_def")  public ReferenceList getHasCustomAttributeDef() { return this.has_custom_attribute_def; }
    /** @see #has_custom_attribute_def */ @JsonProperty("has_custom_attribute_def")  public void setHasCustomAttributeDef(ReferenceList has_custom_attribute_def) { this.has_custom_attribute_def = has_custom_attribute_def; }

    /** @see #model_uri */ @JsonProperty("model_uri")  public String getModelUri() { return this.model_uri; }
    /** @see #model_uri */ @JsonProperty("model_uri")  public void setModelUri(String model_uri) { this.model_uri = model_uri; }

    /** @see #short_description_attribute */ @JsonProperty("short_description_attribute")  public String getShortDescriptionAttribute() { return this.short_description_attribute; }
    /** @see #short_description_attribute */ @JsonProperty("short_description_attribute")  public void setShortDescriptionAttribute(String short_description_attribute) { this.short_description_attribute = short_description_attribute; }

    /** @see #has_action_descriptor */ @JsonProperty("has_action_descriptor")  public ReferenceList getHasActionDescriptor() { return this.has_action_descriptor; }
    /** @see #has_action_descriptor */ @JsonProperty("has_action_descriptor")  public void setHasActionDescriptor(ReferenceList has_action_descriptor) { this.has_action_descriptor = has_action_descriptor; }

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
        "identifier_attribute",
        "class_name",
        "long_description_attribute",
        "model_name",
        "model_uri",
        "short_description_attribute",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "used_in_application_function",
        "has_navigation_descriptor",
        "has_prop_descriptor",
        "has_custom_attribute_def",
        "has_action_descriptor"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "identifier_attribute",
        "class_name",
        "used_in_application_function",
        "long_description_attribute",
        "has_navigation_descriptor",
        "has_prop_descriptor",
        "model_name",
        "has_custom_attribute_def",
        "model_uri",
        "short_description_attribute",
        "has_action_descriptor",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isClassdescriptor(Object obj) { return (obj.getClass() == Classdescriptor.class); }

}

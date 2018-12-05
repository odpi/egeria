/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'classdescriptor' asset type in IGC, displayed as 'ClassDescriptor' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Classdescriptor extends MainObject {

    public static final String IGC_TYPE_ID = "classdescriptor";

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


    public static final Boolean isClassdescriptor(Object obj) { return (obj.getClass() == Classdescriptor.class); }

}

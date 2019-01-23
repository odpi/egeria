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
 * POJO for the 'providerpropertyinfo' asset type in IGC, displayed as 'ProviderPropertyInfo' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Providerpropertyinfo extends Reference {

    public static String getIgcTypeId() { return "providerpropertyinfo"; }
    public static String getIgcTypeDisplayName() { return "ProviderPropertyInfo"; }

    /**
     * The 'property_data_type' property, displayed as 'Property Data Type' in the IGC UI.
     */
    protected String property_data_type;

    /**
     * The 'has_provider_property_info_extended' property, displayed as 'Has Provider Property Info Extended' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Providerpropertyinfoextended} objects.
     */
    protected ReferenceList has_provider_property_info_extended;

    /**
     * The 'is_searchable' property, displayed as 'Is Searchable' in the IGC UI.
     */
    protected Boolean is_searchable;

    /**
     * The 'complex_attribute_source' property, displayed as 'Complex Attribute Source' in the IGC UI.
     */
    protected Boolean complex_attribute_source;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'is_complex_attribute' property, displayed as 'Is Complex Attribute' in the IGC UI.
     */
    protected Boolean is_complex_attribute;

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'is_required' property, displayed as 'Is Required' in the IGC UI.
     */
    protected Boolean is_required;

    /**
     * The 'display_name' property, displayed as 'Display Name' in the IGC UI.
     */
    protected String display_name;

    /**
     * The 'has_directory_provider_property' property, displayed as 'Has Directory Provider Property' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Directoryproviderproperty} objects.
     */
    protected ReferenceList has_directory_provider_property;

    /**
     * The 'is_editable' property, displayed as 'Is Editable' in the IGC UI.
     */
    protected Boolean is_editable;

    /**
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;

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


    /** @see #property_data_type */ @JsonProperty("property_data_type")  public String getPropertyDataType() { return this.property_data_type; }
    /** @see #property_data_type */ @JsonProperty("property_data_type")  public void setPropertyDataType(String property_data_type) { this.property_data_type = property_data_type; }

    /** @see #has_provider_property_info_extended */ @JsonProperty("has_provider_property_info_extended")  public ReferenceList getHasProviderPropertyInfoExtended() { return this.has_provider_property_info_extended; }
    /** @see #has_provider_property_info_extended */ @JsonProperty("has_provider_property_info_extended")  public void setHasProviderPropertyInfoExtended(ReferenceList has_provider_property_info_extended) { this.has_provider_property_info_extended = has_provider_property_info_extended; }

    /** @see #is_searchable */ @JsonProperty("is_searchable")  public Boolean getIsSearchable() { return this.is_searchable; }
    /** @see #is_searchable */ @JsonProperty("is_searchable")  public void setIsSearchable(Boolean is_searchable) { this.is_searchable = is_searchable; }

    /** @see #complex_attribute_source */ @JsonProperty("complex_attribute_source")  public Boolean getComplexAttributeSource() { return this.complex_attribute_source; }
    /** @see #complex_attribute_source */ @JsonProperty("complex_attribute_source")  public void setComplexAttributeSource(Boolean complex_attribute_source) { this.complex_attribute_source = complex_attribute_source; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #is_complex_attribute */ @JsonProperty("is_complex_attribute")  public Boolean getIsComplexAttribute() { return this.is_complex_attribute; }
    /** @see #is_complex_attribute */ @JsonProperty("is_complex_attribute")  public void setIsComplexAttribute(Boolean is_complex_attribute) { this.is_complex_attribute = is_complex_attribute; }

    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #is_required */ @JsonProperty("is_required")  public Boolean getIsRequired() { return this.is_required; }
    /** @see #is_required */ @JsonProperty("is_required")  public void setIsRequired(Boolean is_required) { this.is_required = is_required; }

    /** @see #display_name */ @JsonProperty("display_name")  public String getDisplayName() { return this.display_name; }
    /** @see #display_name */ @JsonProperty("display_name")  public void setDisplayName(String display_name) { this.display_name = display_name; }

    /** @see #has_directory_provider_property */ @JsonProperty("has_directory_provider_property")  public ReferenceList getHasDirectoryProviderProperty() { return this.has_directory_provider_property; }
    /** @see #has_directory_provider_property */ @JsonProperty("has_directory_provider_property")  public void setHasDirectoryProviderProperty(ReferenceList has_directory_provider_property) { this.has_directory_provider_property = has_directory_provider_property; }

    /** @see #is_editable */ @JsonProperty("is_editable")  public Boolean getIsEditable() { return this.is_editable; }
    /** @see #is_editable */ @JsonProperty("is_editable")  public void setIsEditable(Boolean is_editable) { this.is_editable = is_editable; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }

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
        "property_data_type",
        "is_searchable",
        "complex_attribute_source",
        "description",
        "is_complex_attribute",
        "name",
        "is_required",
        "display_name",
        "is_editable",
        "default_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "property_data_type",
        "description",
        "name",
        "display_name",
        "default_value",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "has_provider_property_info_extended",
        "has_directory_provider_property"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "property_data_type",
        "has_provider_property_info_extended",
        "is_searchable",
        "complex_attribute_source",
        "description",
        "is_complex_attribute",
        "name",
        "is_required",
        "display_name",
        "has_directory_provider_property",
        "is_editable",
        "default_value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isProviderpropertyinfo(Object obj) { return (obj.getClass() == Providerpropertyinfo.class); }

}

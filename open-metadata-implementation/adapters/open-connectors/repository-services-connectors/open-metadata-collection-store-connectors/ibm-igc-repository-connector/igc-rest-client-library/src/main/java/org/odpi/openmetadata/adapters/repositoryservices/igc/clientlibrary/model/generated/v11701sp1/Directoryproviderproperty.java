/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701sp1;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code directoryproviderproperty} asset type in IGC, displayed as '{@literal DirectoryProviderProperty}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Directoryproviderproperty extends Reference {

    public static String getIgcTypeId() { return "directoryproviderproperty"; }
    public static String getIgcTypeDisplayName() { return "DirectoryProviderProperty"; }

    /**
     * The {@code value} property, displayed as '{@literal Value}' in the IGC UI.
     */
    protected String value;

    /**
     * The {@code of_provider_property_type} property, displayed as '{@literal Of Provider Property Type}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Providerpropertytype} object.
     */
    protected Reference of_provider_property_type;

    /**
     * The {@code of_provider_property_info} property, displayed as '{@literal Of Provider Property Info}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Providerpropertyinfo} object.
     */
    protected Reference of_provider_property_info;

    /**
     * The {@code of_directory_provider_configuration} property, displayed as '{@literal Of Directory Provider Configuration}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Directoryproviderconfiguration} object.
     */
    protected Reference of_directory_provider_configuration;

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


    /** @see #value */ @JsonProperty("value")  public String getValue() { return this.value; }
    /** @see #value */ @JsonProperty("value")  public void setValue(String value) { this.value = value; }

    /** @see #of_provider_property_type */ @JsonProperty("of_provider_property_type")  public Reference getOfProviderPropertyType() { return this.of_provider_property_type; }
    /** @see #of_provider_property_type */ @JsonProperty("of_provider_property_type")  public void setOfProviderPropertyType(Reference of_provider_property_type) { this.of_provider_property_type = of_provider_property_type; }

    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public Reference getOfProviderPropertyInfo() { return this.of_provider_property_info; }
    /** @see #of_provider_property_info */ @JsonProperty("of_provider_property_info")  public void setOfProviderPropertyInfo(Reference of_provider_property_info) { this.of_provider_property_info = of_provider_property_info; }

    /** @see #of_directory_provider_configuration */ @JsonProperty("of_directory_provider_configuration")  public Reference getOfDirectoryProviderConfiguration() { return this.of_directory_provider_configuration; }
    /** @see #of_directory_provider_configuration */ @JsonProperty("of_directory_provider_configuration")  public void setOfDirectoryProviderConfiguration(Reference of_directory_provider_configuration) { this.of_directory_provider_configuration = of_directory_provider_configuration; }

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
        "value",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "value",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "value",
        "of_provider_property_type",
        "of_provider_property_info",
        "of_directory_provider_configuration",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDirectoryproviderproperty(Object obj) { return (obj.getClass() == Directoryproviderproperty.class); }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'providerpropertytype' asset type in IGC, displayed as 'ProviderPropertyType' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Providerpropertytype extends MainObject {

    public static final String IGC_TYPE_ID = "providerpropertytype";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected String description;

    /**
     * The 'property_type' property, displayed as 'Property Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>USER (displayed in the UI as 'USER')</li>
     *     <li>USER_GROUP (displayed in the UI as 'USER_GROUP')</li>
     *     <li>ROLE (displayed in the UI as 'ROLE')</li>
     *     <li>CONNECTION (displayed in the UI as 'CONNECTION')</li>
     * </ul>
     */
    protected String property_type;

    /**
     * The 'has_directory_provider_property' property, displayed as 'Has Directory Provider Property' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Directoryproviderproperty} objects.
     */
    protected ReferenceList has_directory_provider_property;


    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #property_type */ @JsonProperty("property_type")  public String getPropertyType() { return this.property_type; }
    /** @see #property_type */ @JsonProperty("property_type")  public void setPropertyType(String property_type) { this.property_type = property_type; }

    /** @see #has_directory_provider_property */ @JsonProperty("has_directory_provider_property")  public ReferenceList getHasDirectoryProviderProperty() { return this.has_directory_provider_property; }
    /** @see #has_directory_provider_property */ @JsonProperty("has_directory_provider_property")  public void setHasDirectoryProviderProperty(ReferenceList has_directory_provider_property) { this.has_directory_provider_property = has_directory_provider_property; }


    public static final Boolean isProviderpropertytype(Object obj) { return (obj.getClass() == Providerpropertytype.class); }

}

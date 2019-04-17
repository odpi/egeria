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
 * POJO for the {@code oslc_link} asset type in IGC, displayed as '{@literal Link}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("oslc_link")
public class OslcLink extends Reference {

    public static String getIgcTypeDisplayName() { return "Link"; }

    /**
     * The {@code url} property, displayed as '{@literal Url}' in the IGC UI.
     */
    protected String url;

    /**
     * The {@code link_type} property, displayed as '{@literal Link Type}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Linktype} object.
     */
    protected Reference link_type;

    /**
     * The {@code description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String description;

    /**
     * The {@code of_main_object} property, displayed as '{@literal Of Main Object}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference of_main_object;

    /**
     * The {@code of_provider_connection} property, displayed as '{@literal Of Provider Connection}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InformationAsset} object.
     */
    protected Reference of_provider_connection;


    /** @see #url */ @JsonProperty("url")  public String getTheUrl() { return this.url; }
    /** @see #url */ @JsonProperty("url")  public void setTheUrl(String url) { this.url = url; }

    /** @see #link_type */ @JsonProperty("link_type")  public Reference getLinkType() { return this.link_type; }
    /** @see #link_type */ @JsonProperty("link_type")  public void setLinkType(Reference link_type) { this.link_type = link_type; }

    /** @see #description */ @JsonProperty("description")  public String getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(String description) { this.description = description; }

    /** @see #of_main_object */ @JsonProperty("of_main_object")  public Reference getOfMainObject() { return this.of_main_object; }
    /** @see #of_main_object */ @JsonProperty("of_main_object")  public void setOfMainObject(Reference of_main_object) { this.of_main_object = of_main_object; }

    /** @see #of_provider_connection */ @JsonProperty("of_provider_connection")  public Reference getOfProviderConnection() { return this.of_provider_connection; }
    /** @see #of_provider_connection */ @JsonProperty("of_provider_connection")  public void setOfProviderConnection(Reference of_provider_connection) { this.of_provider_connection = of_provider_connection; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "url",
        "description"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "url",
        "description"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "url",
        "link_type",
        "description",
        "of_main_object",
        "of_provider_connection"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isOslcLink(Object obj) { return (obj.getClass() == OslcLink.class); }

}

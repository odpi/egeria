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
 * POJO for the {@code bi_olap_level_element} asset type in IGC, displayed as '{@literal BI OLAP Level Element}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiOlapLevelElement extends Reference {

    public static String getIgcTypeId() { return "bi_olap_level_element"; }
    public static String getIgcTypeDisplayName() { return "BI OLAP Level Element"; }

    /**
     * The {@code name} property, displayed as '{@literal Element Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code business_name} property, displayed as '{@literal Business Name}' in the IGC UI.
     */
    protected String business_name;

    /**
     * The {@code short_description} property, displayed as '{@literal Short Description}' in the IGC UI.
     */
    protected String short_description;

    /**
     * The {@code defined_in_level} property, displayed as '{@literal Defined in Level}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference defined_in_level;

    /**
     * The {@code key_level} property, displayed as '{@literal Key Level}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiLevel} object.
     */
    protected Reference key_level;

    /**
     * The {@code defined_as_an_olap_member} property, displayed as '{@literal Defined as an OLAP Member}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollectionMember} object.
     */
    protected Reference defined_as_an_olap_member;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #business_name */ @JsonProperty("business_name")  public String getBusinessName() { return this.business_name; }
    /** @see #business_name */ @JsonProperty("business_name")  public void setBusinessName(String business_name) { this.business_name = business_name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #defined_in_level */ @JsonProperty("defined_in_level")  public Reference getDefinedInLevel() { return this.defined_in_level; }
    /** @see #defined_in_level */ @JsonProperty("defined_in_level")  public void setDefinedInLevel(Reference defined_in_level) { this.defined_in_level = defined_in_level; }

    /** @see #key_level */ @JsonProperty("key_level")  public Reference getKeyLevel() { return this.key_level; }
    /** @see #key_level */ @JsonProperty("key_level")  public void setKeyLevel(Reference key_level) { this.key_level = key_level; }

    /** @see #defined_as_an_olap_member */ @JsonProperty("defined_as_an_olap_member")  public Reference getDefinedAsAnOlapMember() { return this.defined_as_an_olap_member; }
    /** @see #defined_as_an_olap_member */ @JsonProperty("defined_as_an_olap_member")  public void setDefinedAsAnOlapMember(Reference defined_as_an_olap_member) { this.defined_as_an_olap_member = defined_as_an_olap_member; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "business_name",
        "short_description"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "business_name",
        "short_description"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "business_name",
        "short_description",
        "defined_in_level",
        "key_level",
        "defined_as_an_olap_member"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isBiOlapLevelElement(Object obj) { return (obj.getClass() == BiOlapLevelElement.class); }

}

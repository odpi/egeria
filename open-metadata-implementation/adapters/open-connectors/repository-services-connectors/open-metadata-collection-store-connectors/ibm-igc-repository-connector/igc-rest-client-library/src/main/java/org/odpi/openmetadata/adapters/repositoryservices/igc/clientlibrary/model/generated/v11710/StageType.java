/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11710;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code stage_type} asset type in IGC, displayed as '{@literal Stage Type}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class StageType extends Reference {

    public static String getIgcTypeId() { return "stage_type"; }
    public static String getIgcTypeDisplayName() { return "Stage Type"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code long_description} property, displayed as '{@literal Description}' in the IGC UI.
     */
    protected String long_description;

    /**
     * The {@code steward} property, displayed as '{@literal Steward}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Steward} objects.
     */
    protected ReferenceList steward;

    /**
     * The {@code vendor} property, displayed as '{@literal Vendor}' in the IGC UI.
     */
    protected String vendor;

    /**
     * The {@code version} property, displayed as '{@literal Version}' in the IGC UI.
     */
    protected String version;

    /**
     * The {@code author} property, displayed as '{@literal Author}' in the IGC UI.
     */
    protected String author;

    /**
     * The {@code copyright} property, displayed as '{@literal Copyright}' in the IGC UI.
     */
    protected String copyright;

    /**
     * The {@code native_id} property, displayed as '{@literal Native ID}' in the IGC UI.
     */
    protected String native_id;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #steward */ @JsonProperty("steward")  public ReferenceList getSteward() { return this.steward; }
    /** @see #steward */ @JsonProperty("steward")  public void setSteward(ReferenceList steward) { this.steward = steward; }

    /** @see #vendor */ @JsonProperty("vendor")  public String getVendor() { return this.vendor; }
    /** @see #vendor */ @JsonProperty("vendor")  public void setVendor(String vendor) { this.vendor = vendor; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    /** @see #copyright */ @JsonProperty("copyright")  public String getCopyright() { return this.copyright; }
    /** @see #copyright */ @JsonProperty("copyright")  public void setCopyright(String copyright) { this.copyright = copyright; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "long_description",
        "vendor",
        "version",
        "author",
        "copyright",
        "native_id"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "long_description",
        "vendor",
        "version",
        "author",
        "copyright",
        "native_id"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "steward"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "long_description",
        "steward",
        "vendor",
        "version",
        "author",
        "copyright",
        "native_id"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isStageType(Object obj) { return (obj.getClass() == StageType.class); }

}

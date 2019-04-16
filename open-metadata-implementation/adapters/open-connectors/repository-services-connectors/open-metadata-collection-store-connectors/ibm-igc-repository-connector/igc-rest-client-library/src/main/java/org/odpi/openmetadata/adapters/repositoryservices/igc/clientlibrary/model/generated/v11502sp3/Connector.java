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
 * POJO for the {@code connector} asset type in IGC, displayed as '{@literal Connector}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("connector")
public class Connector extends Reference {

    public static String getIgcTypeDisplayName() { return "Connector"; }

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
     * The {@code host} property, displayed as '{@literal Host}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host;

    /**
     * The {@code implements_stage_type} property, displayed as '{@literal Implements Stage Type}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link StageType} object.
     */
    protected Reference implements_stage_type;

    /**
     * The {@code data_connections} property, displayed as '{@literal Data Connections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnection} objects.
     */
    protected ReferenceList data_connections;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     */
    protected String type;

    /**
     * The {@code version} property, displayed as '{@literal Version}' in the IGC UI.
     */
    protected String version;

    /**
     * The {@code variant} property, displayed as '{@literal Variant}' in the IGC UI.
     */
    protected String variant;

    /**
     * The {@code library} property, displayed as '{@literal Library}' in the IGC UI.
     */
    protected String library;

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

    /** @see #host */ @JsonProperty("host")  public Reference getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(Reference host) { this.host = host; }

    /** @see #implements_stage_type */ @JsonProperty("implements_stage_type")  public Reference getImplementsStageType() { return this.implements_stage_type; }
    /** @see #implements_stage_type */ @JsonProperty("implements_stage_type")  public void setImplementsStageType(Reference implements_stage_type) { this.implements_stage_type = implements_stage_type; }

    /** @see #data_connections */ @JsonProperty("data_connections")  public ReferenceList getDataConnections() { return this.data_connections; }
    /** @see #data_connections */ @JsonProperty("data_connections")  public void setDataConnections(ReferenceList data_connections) { this.data_connections = data_connections; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #variant */ @JsonProperty("variant")  public String getVariant() { return this.variant; }
    /** @see #variant */ @JsonProperty("variant")  public void setVariant(String variant) { this.variant = variant; }

    /** @see #library */ @JsonProperty("library")  public String getLibrary() { return this.library; }
    /** @see #library */ @JsonProperty("library")  public void setLibrary(String library) { this.library = library; }

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
        "type",
        "version",
        "variant",
        "library",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "type",
        "version",
        "variant",
        "library",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "data_connections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "host",
        "implements_stage_type",
        "data_connections",
        "type",
        "version",
        "variant",
        "library",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isConnector(Object obj) { return (obj.getClass() == Connector.class); }

}

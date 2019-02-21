/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11501ru5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code data_connection_mapping} asset type in IGC, displayed as '{@literal Data Connection Mapping}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataConnectionMapping extends Reference {

    public static String getIgcTypeId() { return "data_connection_mapping"; }
    public static String getIgcTypeDisplayName() { return "Data Connection Mapping"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code host_(engine)_name} property, displayed as '{@literal Host (Engine) Name}' in the IGC UI.
     */
    @JsonProperty("host_(engine)_name") protected String host__engine__name;

    /**
     * The {@code type} property, displayed as '{@literal Type}' in the IGC UI.
     */
    protected String type;

    /**
     * The {@code used_by} property, displayed as '{@literal Used by}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList used_by;

    /**
     * The {@code same_as_data_connections} property, displayed as '{@literal Same as Data Connections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnectionMapping} objects.
     */
    protected ReferenceList same_as_data_connections;

    /**
     * The {@code preferred_data_connection} property, displayed as '{@literal Preferred Data Connection}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataConnectionMapping} object.
     */
    protected Reference preferred_data_connection;

    /**
     * The {@code bound_to_database} property, displayed as '{@literal Bound to Database}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Database} object.
     */
    protected Reference bound_to_database;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #host__engine__name */ @JsonProperty("host_(engine)_name")  public String getHostEngineName() { return this.host__engine__name; }
    /** @see #host__engine__name */ @JsonProperty("host_(engine)_name")  public void setHostEngineName(String host__engine__name) { this.host__engine__name = host__engine__name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #used_by */ @JsonProperty("used_by")  public ReferenceList getUsedBy() { return this.used_by; }
    /** @see #used_by */ @JsonProperty("used_by")  public void setUsedBy(ReferenceList used_by) { this.used_by = used_by; }

    /** @see #same_as_data_connections */ @JsonProperty("same_as_data_connections")  public ReferenceList getSameAsDataConnections() { return this.same_as_data_connections; }
    /** @see #same_as_data_connections */ @JsonProperty("same_as_data_connections")  public void setSameAsDataConnections(ReferenceList same_as_data_connections) { this.same_as_data_connections = same_as_data_connections; }

    /** @see #preferred_data_connection */ @JsonProperty("preferred_data_connection")  public Reference getPreferredDataConnection() { return this.preferred_data_connection; }
    /** @see #preferred_data_connection */ @JsonProperty("preferred_data_connection")  public void setPreferredDataConnection(Reference preferred_data_connection) { this.preferred_data_connection = preferred_data_connection; }

    /** @see #bound_to_database */ @JsonProperty("bound_to_database")  public Reference getBoundToDatabase() { return this.bound_to_database; }
    /** @see #bound_to_database */ @JsonProperty("bound_to_database")  public void setBoundToDatabase(Reference bound_to_database) { this.bound_to_database = bound_to_database; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "host_(engine)_name",
        "type"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "host_(engine)_name",
        "type"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "used_by",
        "same_as_data_connections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "host_(engine)_name",
        "type",
        "used_by",
        "same_as_data_connections",
        "preferred_data_connection",
        "bound_to_database"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDataConnectionMapping(Object obj) { return (obj.getClass() == DataConnectionMapping.class); }

}

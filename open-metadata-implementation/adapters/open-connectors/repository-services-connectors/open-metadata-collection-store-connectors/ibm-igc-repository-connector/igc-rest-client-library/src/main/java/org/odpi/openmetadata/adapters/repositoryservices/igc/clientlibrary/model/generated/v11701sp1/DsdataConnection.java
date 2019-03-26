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
 * POJO for the {@code dsdata_connection} asset type in IGC, displayed as '{@literal Data Connection}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class DsdataConnection extends Reference {

    public static String getIgcTypeId() { return "dsdata_connection"; }
    public static String getIgcTypeDisplayName() { return "Data Connection"; }

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
     * The {@code imports_database} property, displayed as '{@literal Imports Database}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference imports_database;

    /**
     * The {@code imports_table_definitions} property, displayed as '{@literal Imports Table Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList imports_table_definitions;

    /**
     * The {@code data_connectors} property, displayed as '{@literal Data Connectors}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Connector} object.
     */
    protected Reference data_connectors;

    /**
     * The {@code connection_string} property, displayed as '{@literal Connection String}' in the IGC UI.
     */
    protected String connection_string;

    /**
     * The {@code folder_path} property, displayed as '{@literal Folder Path}' in the IGC UI.
     */
    protected String folder_path;

    /**
     * The {@code namespace} property, displayed as '{@literal Namespace}' in the IGC UI.
     */
    protected String namespace;

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

    /** @see #imports_database */ @JsonProperty("imports_database")  public Reference getImportsDatabase() { return this.imports_database; }
    /** @see #imports_database */ @JsonProperty("imports_database")  public void setImportsDatabase(Reference imports_database) { this.imports_database = imports_database; }

    /** @see #imports_table_definitions */ @JsonProperty("imports_table_definitions")  public ReferenceList getImportsTableDefinitions() { return this.imports_table_definitions; }
    /** @see #imports_table_definitions */ @JsonProperty("imports_table_definitions")  public void setImportsTableDefinitions(ReferenceList imports_table_definitions) { this.imports_table_definitions = imports_table_definitions; }

    /** @see #data_connectors */ @JsonProperty("data_connectors")  public Reference getDataConnectors() { return this.data_connectors; }
    /** @see #data_connectors */ @JsonProperty("data_connectors")  public void setDataConnectors(Reference data_connectors) { this.data_connectors = data_connectors; }

    /** @see #connection_string */ @JsonProperty("connection_string")  public String getConnectionString() { return this.connection_string; }
    /** @see #connection_string */ @JsonProperty("connection_string")  public void setConnectionString(String connection_string) { this.connection_string = connection_string; }

    /** @see #folder_path */ @JsonProperty("folder_path")  public String getFolderPath() { return this.folder_path; }
    /** @see #folder_path */ @JsonProperty("folder_path")  public void setFolderPath(String folder_path) { this.folder_path = folder_path; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

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
        "connection_string",
        "folder_path",
        "namespace",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "connection_string",
        "folder_path",
        "namespace",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "imports_table_definitions"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "host",
        "imports_database",
        "imports_table_definitions",
        "data_connectors",
        "connection_string",
        "folder_path",
        "namespace",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isDsdataConnection(Object obj) { return (obj.getClass() == DsdataConnection.class); }

}

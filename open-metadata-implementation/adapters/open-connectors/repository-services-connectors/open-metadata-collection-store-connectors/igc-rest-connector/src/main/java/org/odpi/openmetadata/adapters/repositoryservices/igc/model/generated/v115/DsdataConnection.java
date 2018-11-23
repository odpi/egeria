/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'dsdata_connection' asset type in IGC, displayed as 'Data Connection' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DsdataConnection extends MainObject {

    public static final String IGC_TYPE_ID = "dsdata_connection";

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host;

    /**
     * The 'imports_database' property, displayed as 'Imports Database' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference imports_database;

    /**
     * The 'imports_table_definitions' property, displayed as 'Imports Table Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList imports_table_definitions;

    /**
     * The 'data_connectors' property, displayed as 'Data Connectors' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Connector} object.
     */
    protected Reference data_connectors;

    /**
     * The 'connection_string' property, displayed as 'Connection String' in the IGC UI.
     */
    protected String connection_string;

    /**
     * The 'folder_path' property, displayed as 'Folder Path' in the IGC UI.
     */
    protected String folder_path;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;


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


    public static final Boolean isDsdataConnection(Object obj) { return (obj.getClass() == DsdataConnection.class); }

}

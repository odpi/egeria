/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_connection' asset type in IGC, displayed as 'Data Connection' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataConnection extends MainObject {

    public static final String IGC_TYPE_ID = "data_connection";

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host;

    /**
     * The 'imports_database' property, displayed as 'Imports Database' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Database} object.
     */
    protected Reference imports_database;

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
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;


    /** @see #host */ @JsonProperty("host")  public Reference getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(Reference host) { this.host = host; }

    /** @see #imports_database */ @JsonProperty("imports_database")  public Reference getImportsDatabase() { return this.imports_database; }
    /** @see #imports_database */ @JsonProperty("imports_database")  public void setImportsDatabase(Reference imports_database) { this.imports_database = imports_database; }

    /** @see #data_connectors */ @JsonProperty("data_connectors")  public Reference getDataConnectors() { return this.data_connectors; }
    /** @see #data_connectors */ @JsonProperty("data_connectors")  public void setDataConnectors(Reference data_connectors) { this.data_connectors = data_connectors; }

    /** @see #connection_string */ @JsonProperty("connection_string")  public String getConnectionString() { return this.connection_string; }
    /** @see #connection_string */ @JsonProperty("connection_string")  public void setConnectionString(String connection_string) { this.connection_string = connection_string; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }


    public static final Boolean isDataConnection(Object obj) { return (obj.getClass() == DataConnection.class); }

}

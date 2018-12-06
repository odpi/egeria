/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'datagroup' asset type in IGC, displayed as 'DataGroup' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Datagroup extends MainObject {

    public static final String IGC_TYPE_ID = "datagroup";

    /**
     * The 'name_quoting_char' property, displayed as 'Name Quoting Char' in the IGC UI.
     */
    protected String name_quoting_char;

    /**
     * The 'name_qualifier' property, displayed as 'Name Qualifier' in the IGC UI.
     */
    protected String name_qualifier;

    /**
     * The 'imported_via_data_connection' property, displayed as 'Data Connection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataConnection} object.
     */
    protected Reference imported_via_data_connection;

    /**
     * The 'database_schema' property, displayed as 'Database Schema' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DatabaseSchema} object.
     */
    protected Reference database_schema;


    /** @see #name_quoting_char */ @JsonProperty("name_quoting_char")  public String getNameQuotingChar() { return this.name_quoting_char; }
    /** @see #name_quoting_char */ @JsonProperty("name_quoting_char")  public void setNameQuotingChar(String name_quoting_char) { this.name_quoting_char = name_quoting_char; }

    /** @see #name_qualifier */ @JsonProperty("name_qualifier")  public String getNameQualifier() { return this.name_qualifier; }
    /** @see #name_qualifier */ @JsonProperty("name_qualifier")  public void setNameQualifier(String name_qualifier) { this.name_qualifier = name_qualifier; }

    /** @see #imported_via_data_connection */ @JsonProperty("imported_via_data_connection")  public Reference getImportedViaDataConnection() { return this.imported_via_data_connection; }
    /** @see #imported_via_data_connection */ @JsonProperty("imported_via_data_connection")  public void setImportedViaDataConnection(Reference imported_via_data_connection) { this.imported_via_data_connection = imported_via_data_connection; }

    /** @see #database_schema */ @JsonProperty("database_schema")  public Reference getDatabaseSchema() { return this.database_schema; }
    /** @see #database_schema */ @JsonProperty("database_schema")  public void setDatabaseSchema(Reference database_schema) { this.database_schema = database_schema; }


    public static final Boolean isDatagroup(Object obj) { return (obj.getClass() == Datagroup.class); }

}

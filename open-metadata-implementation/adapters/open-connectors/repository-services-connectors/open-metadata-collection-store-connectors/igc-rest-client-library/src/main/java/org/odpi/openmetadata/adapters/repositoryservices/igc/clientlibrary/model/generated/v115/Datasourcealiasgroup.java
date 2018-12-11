/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'datasourcealiasgroup' asset type in IGC, displayed as 'DataSourceAliasGroup' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Datasourcealiasgroup extends Reference {

    @JsonIgnore public static final String IGC_TYPE_ID = "datasourcealiasgroup";

    /**
     * The 'same_as_data_connections' property, displayed as 'Same as Data Connections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnectionMapping} objects.
     */
    protected ReferenceList same_as_data_connections;

    /**
     * The 'preferred_data_connection' property, displayed as 'Preferred Data Connection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataConnectionMapping} object.
     */
    protected Reference preferred_data_connection;

    /**
     * The 'bound_to_database' property, displayed as 'Bound To Database' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Database} object.
     */
    protected Reference bound_to_database;


    /** @see #same_as_data_connections */ @JsonProperty("same_as_data_connections")  public ReferenceList getSameAsDataConnections() { return this.same_as_data_connections; }
    /** @see #same_as_data_connections */ @JsonProperty("same_as_data_connections")  public void setSameAsDataConnections(ReferenceList same_as_data_connections) { this.same_as_data_connections = same_as_data_connections; }

    /** @see #preferred_data_connection */ @JsonProperty("preferred_data_connection")  public Reference getPreferredDataConnection() { return this.preferred_data_connection; }
    /** @see #preferred_data_connection */ @JsonProperty("preferred_data_connection")  public void setPreferredDataConnection(Reference preferred_data_connection) { this.preferred_data_connection = preferred_data_connection; }

    /** @see #bound_to_database */ @JsonProperty("bound_to_database")  public Reference getBoundToDatabase() { return this.bound_to_database; }
    /** @see #bound_to_database */ @JsonProperty("bound_to_database")  public void setBoundToDatabase(Reference bound_to_database) { this.bound_to_database = bound_to_database; }


    public static final Boolean isDatasourcealiasgroup(Object obj) { return (obj.getClass() == Datasourcealiasgroup.class); }

}

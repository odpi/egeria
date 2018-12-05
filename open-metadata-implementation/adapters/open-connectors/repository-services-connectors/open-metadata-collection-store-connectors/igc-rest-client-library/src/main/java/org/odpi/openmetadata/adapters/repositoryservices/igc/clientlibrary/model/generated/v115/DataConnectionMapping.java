/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'data_connection_mapping' asset type in IGC, displayed as 'Data Connection Mapping' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataConnectionMapping extends MainObject {

    public static final String IGC_TYPE_ID = "data_connection_mapping";

    /**
     * The 'host_(engine)_name' property, displayed as 'Host (Engine) Name' in the IGC UI.
     */
    @JsonProperty("host_(engine)_name") protected String host__engine__name;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'used_by' property, displayed as 'Used by' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList used_by;

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
     * The 'bound_to_database' property, displayed as 'Bound to Database' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Database} object.
     */
    protected Reference bound_to_database;


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


    public static final Boolean isDataConnectionMapping(Object obj) { return (obj.getClass() == DataConnectionMapping.class); }

}

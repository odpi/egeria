/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'validity_table' asset type in IGC, displayed as 'Validity Table' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ValidityTable extends MainObject {

    public static final String IGC_TYPE_ID = "validity_table";

    /**
     * The 'validity_type' property, displayed as 'Validity Type' in the IGC UI.
     */
    protected String validity_type;

    /**
     * The 'database_columns' property, displayed as 'Database Columns' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataField} object.
     */
    protected Reference database_columns;


    /** @see #validity_type */ @JsonProperty("validity_type")  public String getValidityType() { return this.validity_type; }
    /** @see #validity_type */ @JsonProperty("validity_type")  public void setValidityType(String validity_type) { this.validity_type = validity_type; }

    /** @see #database_columns */ @JsonProperty("database_columns")  public Reference getDatabaseColumns() { return this.database_columns; }
    /** @see #database_columns */ @JsonProperty("database_columns")  public void setDatabaseColumns(Reference database_columns) { this.database_columns = database_columns; }


    public static final Boolean isValidityTable(Object obj) { return (obj.getClass() == ValidityTable.class); }

}

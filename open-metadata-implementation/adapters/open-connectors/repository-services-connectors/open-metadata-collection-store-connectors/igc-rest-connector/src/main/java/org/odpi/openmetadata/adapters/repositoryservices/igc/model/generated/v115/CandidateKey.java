/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'candidate_key' asset type in IGC, displayed as 'Candidate Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class CandidateKey extends MainObject {

    public static final String IGC_TYPE_ID = "candidate_key";

    /**
     * The 'database_table_view' property, displayed as 'Database Table View' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference database_table_view;

    /**
     * The 'primary_key' property, displayed as 'Primary Key' in the IGC UI.
     */
    protected Boolean primary_key;

    /**
     * The 'defined_on_database_columns' property, displayed as 'Defined on Database Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList defined_on_database_columns;

    /**
     * The 'referenced_by_foreign_keys' property, displayed as 'Referenced by Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ForeignKey} objects.
     */
    protected ReferenceList referenced_by_foreign_keys;


    /** @see #database_table_view */ @JsonProperty("database_table_view")  public Reference getDatabaseTableView() { return this.database_table_view; }
    /** @see #database_table_view */ @JsonProperty("database_table_view")  public void setDatabaseTableView(Reference database_table_view) { this.database_table_view = database_table_view; }

    /** @see #primary_key */ @JsonProperty("primary_key")  public Boolean getPrimaryKey() { return this.primary_key; }
    /** @see #primary_key */ @JsonProperty("primary_key")  public void setPrimaryKey(Boolean primary_key) { this.primary_key = primary_key; }

    /** @see #defined_on_database_columns */ @JsonProperty("defined_on_database_columns")  public ReferenceList getDefinedOnDatabaseColumns() { return this.defined_on_database_columns; }
    /** @see #defined_on_database_columns */ @JsonProperty("defined_on_database_columns")  public void setDefinedOnDatabaseColumns(ReferenceList defined_on_database_columns) { this.defined_on_database_columns = defined_on_database_columns; }

    /** @see #referenced_by_foreign_keys */ @JsonProperty("referenced_by_foreign_keys")  public ReferenceList getReferencedByForeignKeys() { return this.referenced_by_foreign_keys; }
    /** @see #referenced_by_foreign_keys */ @JsonProperty("referenced_by_foreign_keys")  public void setReferencedByForeignKeys(ReferenceList referenced_by_foreign_keys) { this.referenced_by_foreign_keys = referenced_by_foreign_keys; }


    public static final Boolean isCandidateKey(Object obj) { return (obj.getClass() == CandidateKey.class); }

}

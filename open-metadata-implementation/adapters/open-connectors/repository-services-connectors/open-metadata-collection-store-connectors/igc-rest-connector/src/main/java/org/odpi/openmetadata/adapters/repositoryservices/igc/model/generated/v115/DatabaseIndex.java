/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'database_index' asset type in IGC, displayed as 'Database Index' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DatabaseIndex extends MainObject {

    public static final String IGC_TYPE_ID = "database_index";

    /**
     * The 'indexed_database_columns' property, displayed as 'Database Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList indexed_database_columns;

    /**
     * The 'expression' property, displayed as 'Expression' in the IGC UI.
     */
    protected ArrayList<String> expression;

    /**
     * The 'unique' property, displayed as 'Unique Index' in the IGC UI.
     */
    protected Boolean unique;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'function' property, displayed as 'Function' in the IGC UI.
     */
    protected Boolean function;

    /**
     * The 'partition' property, displayed as 'Partition' in the IGC UI.
     */
    protected Boolean partition;

    /**
     * The 'join' property, displayed as 'Join' in the IGC UI.
     */
    protected Boolean join;

    /**
     * The 'joined_database_columns' property, displayed as 'Joined Database Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList joined_database_columns;


    /** @see #indexed_database_columns */ @JsonProperty("indexed_database_columns")  public ReferenceList getIndexedDatabaseColumns() { return this.indexed_database_columns; }
    /** @see #indexed_database_columns */ @JsonProperty("indexed_database_columns")  public void setIndexedDatabaseColumns(ReferenceList indexed_database_columns) { this.indexed_database_columns = indexed_database_columns; }

    /** @see #expression */ @JsonProperty("expression")  public ArrayList<String> getExpression() { return this.expression; }
    /** @see #expression */ @JsonProperty("expression")  public void setExpression(ArrayList<String> expression) { this.expression = expression; }

    /** @see #unique */ @JsonProperty("unique")  public Boolean getUnique() { return this.unique; }
    /** @see #unique */ @JsonProperty("unique")  public void setUnique(Boolean unique) { this.unique = unique; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #function */ @JsonProperty("function")  public Boolean getFunction() { return this.function; }
    /** @see #function */ @JsonProperty("function")  public void setFunction(Boolean function) { this.function = function; }

    /** @see #partition */ @JsonProperty("partition")  public Boolean getPartition() { return this.partition; }
    /** @see #partition */ @JsonProperty("partition")  public void setPartition(Boolean partition) { this.partition = partition; }

    /** @see #join */ @JsonProperty("join")  public Boolean getJoin() { return this.join; }
    /** @see #join */ @JsonProperty("join")  public void setJoin(Boolean join) { this.join = join; }

    /** @see #joined_database_columns */ @JsonProperty("joined_database_columns")  public ReferenceList getJoinedDatabaseColumns() { return this.joined_database_columns; }
    /** @see #joined_database_columns */ @JsonProperty("joined_database_columns")  public void setJoinedDatabaseColumns(ReferenceList joined_database_columns) { this.joined_database_columns = joined_database_columns; }


    public static final Boolean isDatabaseIndex(Object obj) { return (obj.getClass() == DatabaseIndex.class); }

}

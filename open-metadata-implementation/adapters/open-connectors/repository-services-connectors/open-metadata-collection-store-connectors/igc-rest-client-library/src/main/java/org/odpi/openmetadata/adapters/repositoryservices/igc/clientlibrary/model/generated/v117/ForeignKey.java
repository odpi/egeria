/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'foreign_key' asset type in IGC, displayed as 'Foreign Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForeignKey extends Reference {

    public static String getIgcTypeId() { return "foreign_key"; }
    public static String getIgcTypeDisplayName() { return "Foreign Key"; }

    /**
     * The 'included_database_columns' property, displayed as 'Included Database Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList included_database_columns;

    /**
     * The 'database_table' property, displayed as 'Database Table' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList database_table;

    /**
     * The 'sorting' property, displayed as 'Sorting' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>ASCENDING (displayed in the UI as 'ASCENDING')</li>
     *     <li>DESCENDING (displayed in the UI as 'DESCENDING')</li>
     *     <li>NONE (displayed in the UI as 'NONE')</li>
     * </ul>
     */
    protected ArrayList<String> sorting;


    /** @see #included_database_columns */ @JsonProperty("included_database_columns")  public ReferenceList getIncludedDatabaseColumns() { return this.included_database_columns; }
    /** @see #included_database_columns */ @JsonProperty("included_database_columns")  public void setIncludedDatabaseColumns(ReferenceList included_database_columns) { this.included_database_columns = included_database_columns; }

    /** @see #database_table */ @JsonProperty("database_table")  public ReferenceList getDatabaseTable() { return this.database_table; }
    /** @see #database_table */ @JsonProperty("database_table")  public void setDatabaseTable(ReferenceList database_table) { this.database_table = database_table; }

    /** @see #sorting */ @JsonProperty("sorting")  public ArrayList<String> getSorting() { return this.sorting; }
    /** @see #sorting */ @JsonProperty("sorting")  public void setSorting(ArrayList<String> sorting) { this.sorting = sorting; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return false; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "sorting"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "included_database_columns",
        "database_table"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "included_database_columns",
        "database_table",
        "sorting"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isForeignKey(Object obj) { return (obj.getClass() == ForeignKey.class); }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code foreign_key_definition} asset type in IGC, displayed as '{@literal Foreign Key Definition}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForeignKeyDefinition extends Reference {

    public static String getIgcTypeId() { return "foreign_key_definition"; }
    public static String getIgcTypeDisplayName() { return "Foreign Key Definition"; }

    /**
     * The {@code column} property, displayed as '{@literal Column}' in the IGC UI.
     */
    protected String column;

    /**
     * The {@code table_definition} property, displayed as '{@literal Table Definition}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TableDefinition} object.
     */
    protected Reference table_definition;

    /**
     * The {@code references_tables} property, displayed as '{@literal References Tables}' in the IGC UI.
     */
    protected String references_tables;

    /**
     * The {@code references_columns} property, displayed as '{@literal References Columns}' in the IGC UI.
     */
    protected String references_columns;

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


    /** @see #column */ @JsonProperty("column")  public String getColumn() { return this.column; }
    /** @see #column */ @JsonProperty("column")  public void setColumn(String column) { this.column = column; }

    /** @see #table_definition */ @JsonProperty("table_definition")  public Reference getTableDefinition() { return this.table_definition; }
    /** @see #table_definition */ @JsonProperty("table_definition")  public void setTableDefinition(Reference table_definition) { this.table_definition = table_definition; }

    /** @see #references_tables */ @JsonProperty("references_tables")  public String getReferencesTables() { return this.references_tables; }
    /** @see #references_tables */ @JsonProperty("references_tables")  public void setReferencesTables(String references_tables) { this.references_tables = references_tables; }

    /** @see #references_columns */ @JsonProperty("references_columns")  public String getReferencesColumns() { return this.references_columns; }
    /** @see #references_columns */ @JsonProperty("references_columns")  public void setReferencesColumns(String references_columns) { this.references_columns = references_columns; }

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
        "column",
        "references_tables",
        "references_columns",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "column",
        "references_tables",
        "references_columns",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = new ArrayList<>();
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "column",
        "table_definition",
        "references_tables",
        "references_columns",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isForeignKeyDefinition(Object obj) { return (obj.getClass() == ForeignKeyDefinition.class); }

}

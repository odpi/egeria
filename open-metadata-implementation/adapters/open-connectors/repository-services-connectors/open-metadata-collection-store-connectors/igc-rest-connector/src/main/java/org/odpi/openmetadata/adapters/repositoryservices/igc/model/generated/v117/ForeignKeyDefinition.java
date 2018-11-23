/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'foreign_key_definition' asset type in IGC, displayed as 'Foreign Key Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ForeignKeyDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "foreign_key_definition";

    /**
     * The 'column' property, displayed as 'Column' in the IGC UI.
     */
    protected String column;

    /**
     * The 'table_definition' property, displayed as 'Table Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TableDefinition} object.
     */
    protected Reference table_definition;

    /**
     * The 'references_tables' property, displayed as 'References Tables' in the IGC UI.
     */
    protected String references_tables;

    /**
     * The 'references_columns' property, displayed as 'References Columns' in the IGC UI.
     */
    protected String references_columns;


    /** @see #column */ @JsonProperty("column")  public String getColumn() { return this.column; }
    /** @see #column */ @JsonProperty("column")  public void setColumn(String column) { this.column = column; }

    /** @see #table_definition */ @JsonProperty("table_definition")  public Reference getTableDefinition() { return this.table_definition; }
    /** @see #table_definition */ @JsonProperty("table_definition")  public void setTableDefinition(Reference table_definition) { this.table_definition = table_definition; }

    /** @see #references_tables */ @JsonProperty("references_tables")  public String getReferencesTables() { return this.references_tables; }
    /** @see #references_tables */ @JsonProperty("references_tables")  public void setReferencesTables(String references_tables) { this.references_tables = references_tables; }

    /** @see #references_columns */ @JsonProperty("references_columns")  public String getReferencesColumns() { return this.references_columns; }
    /** @see #references_columns */ @JsonProperty("references_columns")  public void setReferencesColumns(String references_columns) { this.references_columns = references_columns; }


    public static final Boolean isForeignKeyDefinition(Object obj) { return (obj.getClass() == ForeignKeyDefinition.class); }

}

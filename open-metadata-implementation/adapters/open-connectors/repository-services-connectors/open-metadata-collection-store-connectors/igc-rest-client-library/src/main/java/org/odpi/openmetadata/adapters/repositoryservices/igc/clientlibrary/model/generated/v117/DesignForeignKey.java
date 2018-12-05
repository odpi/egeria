/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'design_foreign_key' asset type in IGC, displayed as 'Design Foreign Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DesignForeignKey extends MainObject {

    public static final String IGC_TYPE_ID = "design_foreign_key";

    /**
     * The 'of_data_collection' property, displayed as 'Of Data Collection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference of_data_collection;

    /**
     * The 'included_database_columns' property, displayed as 'Design Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList included_database_columns;

    /**
     * The 'referenced_by_design_column' property, displayed as 'Parent Design Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataField} objects.
     */
    protected ReferenceList referenced_by_design_column;


    /** @see #of_data_collection */ @JsonProperty("of_data_collection")  public Reference getOfDataCollection() { return this.of_data_collection; }
    /** @see #of_data_collection */ @JsonProperty("of_data_collection")  public void setOfDataCollection(Reference of_data_collection) { this.of_data_collection = of_data_collection; }

    /** @see #included_database_columns */ @JsonProperty("included_database_columns")  public ReferenceList getIncludedDatabaseColumns() { return this.included_database_columns; }
    /** @see #included_database_columns */ @JsonProperty("included_database_columns")  public void setIncludedDatabaseColumns(ReferenceList included_database_columns) { this.included_database_columns = included_database_columns; }

    /** @see #referenced_by_design_column */ @JsonProperty("referenced_by_design_column")  public ReferenceList getReferencedByDesignColumn() { return this.referenced_by_design_column; }
    /** @see #referenced_by_design_column */ @JsonProperty("referenced_by_design_column")  public void setReferencedByDesignColumn(ReferenceList referenced_by_design_column) { this.referenced_by_design_column = referenced_by_design_column; }


    public static final Boolean isDesignForeignKey(Object obj) { return (obj.getClass() == DesignForeignKey.class); }

}

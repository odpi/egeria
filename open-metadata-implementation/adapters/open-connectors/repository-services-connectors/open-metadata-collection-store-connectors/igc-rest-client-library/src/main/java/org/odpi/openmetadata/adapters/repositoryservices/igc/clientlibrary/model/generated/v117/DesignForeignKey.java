/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'design_foreign_key' asset type in IGC, displayed as 'Design Foreign Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DesignForeignKey extends Reference {

    public static String getIgcTypeId() { return "design_foreign_key"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

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

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #of_data_collection */ @JsonProperty("of_data_collection")  public Reference getOfDataCollection() { return this.of_data_collection; }
    /** @see #of_data_collection */ @JsonProperty("of_data_collection")  public void setOfDataCollection(Reference of_data_collection) { this.of_data_collection = of_data_collection; }

    /** @see #included_database_columns */ @JsonProperty("included_database_columns")  public ReferenceList getIncludedDatabaseColumns() { return this.included_database_columns; }
    /** @see #included_database_columns */ @JsonProperty("included_database_columns")  public void setIncludedDatabaseColumns(ReferenceList included_database_columns) { this.included_database_columns = included_database_columns; }

    /** @see #referenced_by_design_column */ @JsonProperty("referenced_by_design_column")  public ReferenceList getReferencedByDesignColumn() { return this.referenced_by_design_column; }
    /** @see #referenced_by_design_column */ @JsonProperty("referenced_by_design_column")  public void setReferencedByDesignColumn(ReferenceList referenced_by_design_column) { this.referenced_by_design_column = referenced_by_design_column; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }


    public static final Boolean isDesignForeignKey(Object obj) { return (obj.getClass() == DesignForeignKey.class); }

}

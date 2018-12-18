/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'mapping_join' asset type in IGC, displayed as 'Mapping Join' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MappingJoin extends Reference {

    public static String getIgcTypeId() { return "mapping_join"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected ArrayList<String> name;

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected ArrayList<String> description;

    /**
     * The 'primary_key_columns' property, displayed as 'Primary Key Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList primary_key_columns;

    /**
     * The 'foreign_key_columns' property, displayed as 'Foreign Key Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList foreign_key_columns;

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


    /** @see #name */ @JsonProperty("name")  public ArrayList<String> getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(ArrayList<String> name) { this.name = name; }

    /** @see #description */ @JsonProperty("description")  public ArrayList<String> getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(ArrayList<String> description) { this.description = description; }

    /** @see #primary_key_columns */ @JsonProperty("primary_key_columns")  public ReferenceList getPrimaryKeyColumns() { return this.primary_key_columns; }
    /** @see #primary_key_columns */ @JsonProperty("primary_key_columns")  public void setPrimaryKeyColumns(ReferenceList primary_key_columns) { this.primary_key_columns = primary_key_columns; }

    /** @see #foreign_key_columns */ @JsonProperty("foreign_key_columns")  public ReferenceList getForeignKeyColumns() { return this.foreign_key_columns; }
    /** @see #foreign_key_columns */ @JsonProperty("foreign_key_columns")  public void setForeignKeyColumns(ReferenceList foreign_key_columns) { this.foreign_key_columns = foreign_key_columns; }

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static final Boolean includesModificationDetails() { return true; }
    public static final Boolean isMappingJoin(Object obj) { return (obj.getClass() == MappingJoin.class); }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
public class MappingJoin extends MainObject {

    public static final String IGC_TYPE_ID = "mapping_join";

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


    /** @see #description */ @JsonProperty("description")  public ArrayList<String> getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(ArrayList<String> description) { this.description = description; }

    /** @see #primary_key_columns */ @JsonProperty("primary_key_columns")  public ReferenceList getPrimaryKeyColumns() { return this.primary_key_columns; }
    /** @see #primary_key_columns */ @JsonProperty("primary_key_columns")  public void setPrimaryKeyColumns(ReferenceList primary_key_columns) { this.primary_key_columns = primary_key_columns; }

    /** @see #foreign_key_columns */ @JsonProperty("foreign_key_columns")  public ReferenceList getForeignKeyColumns() { return this.foreign_key_columns; }
    /** @see #foreign_key_columns */ @JsonProperty("foreign_key_columns")  public void setForeignKeyColumns(ReferenceList foreign_key_columns) { this.foreign_key_columns = foreign_key_columns; }


    public static final Boolean isMappingJoin(Object obj) { return (obj.getClass() == MappingJoin.class); }

}

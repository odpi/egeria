/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'design_key' asset type in IGC, displayed as 'Design Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DesignKey extends MainObject {

    public static final String IGC_TYPE_ID = "design_key";

    /**
     * The 'design_table_or_view' property, displayed as 'Design Table or View' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Datagroup} object.
     */
    protected Reference design_table_or_view;

    /**
     * The 'primary_key' property, displayed as 'Primary Key' in the IGC UI.
     */
    protected Boolean primary_key;

    /**
     * The 'defined_on_design_columns' property, displayed as 'Design Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList defined_on_design_columns;

    /**
     * The 'referenced_by_design_foreign_keys' property, displayed as 'Child Design Foreign Keys' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ForeignKey} objects.
     */
    protected ReferenceList referenced_by_design_foreign_keys;


    /** @see #design_table_or_view */ @JsonProperty("design_table_or_view")  public Reference getDesignTableOrView() { return this.design_table_or_view; }
    /** @see #design_table_or_view */ @JsonProperty("design_table_or_view")  public void setDesignTableOrView(Reference design_table_or_view) { this.design_table_or_view = design_table_or_view; }

    /** @see #primary_key */ @JsonProperty("primary_key")  public Boolean getPrimaryKey() { return this.primary_key; }
    /** @see #primary_key */ @JsonProperty("primary_key")  public void setPrimaryKey(Boolean primary_key) { this.primary_key = primary_key; }

    /** @see #defined_on_design_columns */ @JsonProperty("defined_on_design_columns")  public ReferenceList getDefinedOnDesignColumns() { return this.defined_on_design_columns; }
    /** @see #defined_on_design_columns */ @JsonProperty("defined_on_design_columns")  public void setDefinedOnDesignColumns(ReferenceList defined_on_design_columns) { this.defined_on_design_columns = defined_on_design_columns; }

    /** @see #referenced_by_design_foreign_keys */ @JsonProperty("referenced_by_design_foreign_keys")  public ReferenceList getReferencedByDesignForeignKeys() { return this.referenced_by_design_foreign_keys; }
    /** @see #referenced_by_design_foreign_keys */ @JsonProperty("referenced_by_design_foreign_keys")  public void setReferencedByDesignForeignKeys(ReferenceList referenced_by_design_foreign_keys) { this.referenced_by_design_foreign_keys = referenced_by_design_foreign_keys; }


    public static final Boolean isDesignKey(Object obj) { return (obj.getClass() == DesignKey.class); }

}

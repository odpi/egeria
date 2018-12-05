/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'inferred_foreign_key' asset type in IGC, displayed as 'Inferred Foreign Key' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class InferredForeignKey extends MainObject {

    public static final String IGC_TYPE_ID = "inferred_foreign_key";

    /**
     * The 'table_analysis' property, displayed as 'Table Analysis' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link MainObject} object.
     */
    protected Reference table_analysis;

    /**
     * The 'total_records' property, displayed as 'Total Records' in the IGC UI.
     */
    protected Number total_records;

    /**
     * The 'uses_database_fields' property, displayed as 'Uses Database Fields' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseColumn} objects.
     */
    protected ReferenceList uses_database_fields;

    /**
     * The 'references_inferred_keys' property, displayed as 'References Inferred Keys' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link InferredKey} object.
     */
    protected Reference references_inferred_keys;


    /** @see #table_analysis */ @JsonProperty("table_analysis")  public Reference getTableAnalysis() { return this.table_analysis; }
    /** @see #table_analysis */ @JsonProperty("table_analysis")  public void setTableAnalysis(Reference table_analysis) { this.table_analysis = table_analysis; }

    /** @see #total_records */ @JsonProperty("total_records")  public Number getTotalRecords() { return this.total_records; }
    /** @see #total_records */ @JsonProperty("total_records")  public void setTotalRecords(Number total_records) { this.total_records = total_records; }

    /** @see #uses_database_fields */ @JsonProperty("uses_database_fields")  public ReferenceList getUsesDatabaseFields() { return this.uses_database_fields; }
    /** @see #uses_database_fields */ @JsonProperty("uses_database_fields")  public void setUsesDatabaseFields(ReferenceList uses_database_fields) { this.uses_database_fields = uses_database_fields; }

    /** @see #references_inferred_keys */ @JsonProperty("references_inferred_keys")  public Reference getReferencesInferredKeys() { return this.references_inferred_keys; }
    /** @see #references_inferred_keys */ @JsonProperty("references_inferred_keys")  public void setReferencesInferredKeys(Reference references_inferred_keys) { this.references_inferred_keys = references_inferred_keys; }


    public static final Boolean isInferredForeignKey(Object obj) { return (obj.getClass() == InferredForeignKey.class); }

}

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_file_definition_record' asset type in IGC, displayed as 'Data File Definition Record' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileDefinitionRecord extends MainObject {

    public static final String IGC_TYPE_ID = "data_file_definition_record";

    /**
     * The 'data_file_definition' property, displayed as 'Data File Definition' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataFileDefinition} object.
     */
    protected Reference data_file_definition;

    /**
     * The 'data_file_definition_fields' property, displayed as 'Data File Definition Fields' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileDefinitionField} objects.
     */
    protected ReferenceList data_file_definition_fields;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #data_file_definition */ @JsonProperty("data_file_definition")  public Reference getDataFileDefinition() { return this.data_file_definition; }
    /** @see #data_file_definition */ @JsonProperty("data_file_definition")  public void setDataFileDefinition(Reference data_file_definition) { this.data_file_definition = data_file_definition; }

    /** @see #data_file_definition_fields */ @JsonProperty("data_file_definition_fields")  public ReferenceList getDataFileDefinitionFields() { return this.data_file_definition_fields; }
    /** @see #data_file_definition_fields */ @JsonProperty("data_file_definition_fields")  public void setDataFileDefinitionFields(ReferenceList data_file_definition_fields) { this.data_file_definition_fields = data_file_definition_fields; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDataFileDefinitionRecord(Object obj) { return (obj.getClass() == DataFileDefinitionRecord.class); }

}

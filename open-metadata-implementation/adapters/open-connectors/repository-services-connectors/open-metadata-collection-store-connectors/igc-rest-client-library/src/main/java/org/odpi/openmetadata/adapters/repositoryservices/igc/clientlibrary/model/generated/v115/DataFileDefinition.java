/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'data_file_definition' asset type in IGC, displayed as 'Data File Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "data_file_definition";

    /**
     * The 'data_file_definition_records' property, displayed as 'Data File Definition Records' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileDefinitionRecord} objects.
     */
    protected ReferenceList data_file_definition_records;

    /**
     * The 'implemented_by_data_files' property, displayed as 'Implemented by Data Files' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList implemented_by_data_files;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'references_data_file_folders' property, displayed as 'References Data File Folders' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList references_data_file_folders;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #data_file_definition_records */ @JsonProperty("data_file_definition_records")  public ReferenceList getDataFileDefinitionRecords() { return this.data_file_definition_records; }
    /** @see #data_file_definition_records */ @JsonProperty("data_file_definition_records")  public void setDataFileDefinitionRecords(ReferenceList data_file_definition_records) { this.data_file_definition_records = data_file_definition_records; }

    /** @see #implemented_by_data_files */ @JsonProperty("implemented_by_data_files")  public ReferenceList getImplementedByDataFiles() { return this.implemented_by_data_files; }
    /** @see #implemented_by_data_files */ @JsonProperty("implemented_by_data_files")  public void setImplementedByDataFiles(ReferenceList implemented_by_data_files) { this.implemented_by_data_files = implemented_by_data_files; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #references_data_file_folders */ @JsonProperty("references_data_file_folders")  public ReferenceList getReferencesDataFileFolders() { return this.references_data_file_folders; }
    /** @see #references_data_file_folders */ @JsonProperty("references_data_file_folders")  public void setReferencesDataFileFolders(ReferenceList references_data_file_folders) { this.references_data_file_folders = references_data_file_folders; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDataFileDefinition(Object obj) { return (obj.getClass() == DataFileDefinition.class); }

}

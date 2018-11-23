/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'extension_mapping_document' asset type in IGC, displayed as 'Extension Mapping Document' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ExtensionMappingDocument extends MainObject {

    public static final String IGC_TYPE_ID = "extension_mapping_document";

    /**
     * The 'parent_folder' property, displayed as 'Parent Folder' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Folder} objects.
     */
    protected ReferenceList parent_folder;

    /**
     * The 'extension_mappings' property, displayed as 'Extension Mappings' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ExtensionMapping} objects.
     */
    protected ReferenceList extension_mappings;

    /**
     * The 'file_name' property, displayed as 'File Name' in the IGC UI.
     */
    protected String file_name;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #parent_folder */ @JsonProperty("parent_folder")  public ReferenceList getParentFolder() { return this.parent_folder; }
    /** @see #parent_folder */ @JsonProperty("parent_folder")  public void setParentFolder(ReferenceList parent_folder) { this.parent_folder = parent_folder; }

    /** @see #extension_mappings */ @JsonProperty("extension_mappings")  public ReferenceList getExtensionMappings() { return this.extension_mappings; }
    /** @see #extension_mappings */ @JsonProperty("extension_mappings")  public void setExtensionMappings(ReferenceList extension_mappings) { this.extension_mappings = extension_mappings; }

    /** @see #file_name */ @JsonProperty("file_name")  public String getFileName() { return this.file_name; }
    /** @see #file_name */ @JsonProperty("file_name")  public void setFileName(String file_name) { this.file_name = file_name; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isExtensionMappingDocument(Object obj) { return (obj.getClass() == ExtensionMappingDocument.class); }

}

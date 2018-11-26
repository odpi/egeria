/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'amazon_s3_bucket' asset type in IGC, displayed as 'Amazon S3 Bucket' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AmazonS3Bucket extends MainObject {

    public static final String IGC_TYPE_ID = "amazon_s3_bucket";

    /**
     * The 'host' property, displayed as 'Host' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Host} object.
     */
    protected Reference host;

    /**
     * The 'contains_amazon_s3_data_file_folders' property, displayed as 'Contains Amazon S3 Data File Folders' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList contains_amazon_s3_data_file_folders;

    /**
     * The 'contains_amazon_s3_data_files' property, displayed as 'Contains Amazon S3 Data Files' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AmazonS3DataFile} objects.
     */
    protected ReferenceList contains_amazon_s3_data_files;

    /**
     * The 'uses_data_file_definitions' property, displayed as 'Uses Data File Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileDefinition} objects.
     */
    protected ReferenceList uses_data_file_definitions;

    /**
     * The 'location' property, displayed as 'Location' in the IGC UI.
     */
    protected String location;

    /**
     * The 'source_creation_date' property, displayed as 'Source Creation Date' in the IGC UI.
     */
    protected Date source_creation_date;

    /**
     * The 'source_modification_date' property, displayed as 'Source Modification Date' in the IGC UI.
     */
    protected Date source_modification_date;

    /**
     * The 'data_connection' property, displayed as 'Data Connection' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataConnection} objects.
     */
    protected ReferenceList data_connection;

    /**
     * The 'same_as_data_sources' property, displayed as 'Same as Data Sources' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList same_as_data_sources;

    /**
     * The 'include_for_business_lineage' property, displayed as 'Include for Business Lineage' in the IGC UI.
     */
    protected Boolean include_for_business_lineage;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #host */ @JsonProperty("host")  public Reference getHost() { return this.host; }
    /** @see #host */ @JsonProperty("host")  public void setHost(Reference host) { this.host = host; }

    /** @see #contains_amazon_s3_data_file_folders */ @JsonProperty("contains_amazon_s3_data_file_folders")  public ReferenceList getContainsAmazonS3DataFileFolders() { return this.contains_amazon_s3_data_file_folders; }
    /** @see #contains_amazon_s3_data_file_folders */ @JsonProperty("contains_amazon_s3_data_file_folders")  public void setContainsAmazonS3DataFileFolders(ReferenceList contains_amazon_s3_data_file_folders) { this.contains_amazon_s3_data_file_folders = contains_amazon_s3_data_file_folders; }

    /** @see #contains_amazon_s3_data_files */ @JsonProperty("contains_amazon_s3_data_files")  public ReferenceList getContainsAmazonS3DataFiles() { return this.contains_amazon_s3_data_files; }
    /** @see #contains_amazon_s3_data_files */ @JsonProperty("contains_amazon_s3_data_files")  public void setContainsAmazonS3DataFiles(ReferenceList contains_amazon_s3_data_files) { this.contains_amazon_s3_data_files = contains_amazon_s3_data_files; }

    /** @see #uses_data_file_definitions */ @JsonProperty("uses_data_file_definitions")  public ReferenceList getUsesDataFileDefinitions() { return this.uses_data_file_definitions; }
    /** @see #uses_data_file_definitions */ @JsonProperty("uses_data_file_definitions")  public void setUsesDataFileDefinitions(ReferenceList uses_data_file_definitions) { this.uses_data_file_definitions = uses_data_file_definitions; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #source_creation_date */ @JsonProperty("source_creation_date")  public Date getSourceCreationDate() { return this.source_creation_date; }
    /** @see #source_creation_date */ @JsonProperty("source_creation_date")  public void setSourceCreationDate(Date source_creation_date) { this.source_creation_date = source_creation_date; }

    /** @see #source_modification_date */ @JsonProperty("source_modification_date")  public Date getSourceModificationDate() { return this.source_modification_date; }
    /** @see #source_modification_date */ @JsonProperty("source_modification_date")  public void setSourceModificationDate(Date source_modification_date) { this.source_modification_date = source_modification_date; }

    /** @see #data_connection */ @JsonProperty("data_connection")  public ReferenceList getDataConnection() { return this.data_connection; }
    /** @see #data_connection */ @JsonProperty("data_connection")  public void setDataConnection(ReferenceList data_connection) { this.data_connection = data_connection; }

    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public ReferenceList getSameAsDataSources() { return this.same_as_data_sources; }
    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public void setSameAsDataSources(ReferenceList same_as_data_sources) { this.same_as_data_sources = same_as_data_sources; }

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isAmazonS3Bucket(Object obj) { return (obj.getClass() == AmazonS3Bucket.class); }

}

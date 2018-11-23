/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'host' asset type in IGC, displayed as 'Host' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class Host extends MainObject {

    public static final String IGC_TYPE_ID = "host";

    /**
     * The 'databases' property, displayed as 'Databases' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Database} objects.
     */
    protected ReferenceList databases;

    /**
     * The 'data_files' property, displayed as 'Data Files' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFile} objects.
     */
    protected ReferenceList data_files;

    /**
     * The 'idoc_types' property, displayed as 'IDoc Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link IdocType} objects.
     */
    protected ReferenceList idoc_types;

    /**
     * The 'transformation_projects' property, displayed as 'Transformation Projects' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TransformationProject} objects.
     */
    protected ReferenceList transformation_projects;

    /**
     * The 'data_connections' property, displayed as 'Data Connections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Connector} objects.
     */
    protected ReferenceList data_connections;

    /**
     * The 'amazon_s3_buckets' property, displayed as 'Amazon S3 Buckets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AmazonS3Bucket} objects.
     */
    protected ReferenceList amazon_s3_buckets;

    /**
     * The 'data_file_folders' property, displayed as 'Data File Folders' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataFileFolder} objects.
     */
    protected ReferenceList data_file_folders;

    /**
     * The 'location' property, displayed as 'Location' in the IGC UI.
     */
    protected String location;

    /**
     * The 'network_node' property, displayed as 'Network Node' in the IGC UI.
     */
    protected String network_node;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'blueprint_elements' property, displayed as 'Blueprint Elements' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BlueprintElementLink} objects.
     */
    protected ReferenceList blueprint_elements;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #databases */ @JsonProperty("databases")  public ReferenceList getDatabases() { return this.databases; }
    /** @see #databases */ @JsonProperty("databases")  public void setDatabases(ReferenceList databases) { this.databases = databases; }

    /** @see #data_files */ @JsonProperty("data_files")  public ReferenceList getDataFiles() { return this.data_files; }
    /** @see #data_files */ @JsonProperty("data_files")  public void setDataFiles(ReferenceList data_files) { this.data_files = data_files; }

    /** @see #idoc_types */ @JsonProperty("idoc_types")  public ReferenceList getIdocTypes() { return this.idoc_types; }
    /** @see #idoc_types */ @JsonProperty("idoc_types")  public void setIdocTypes(ReferenceList idoc_types) { this.idoc_types = idoc_types; }

    /** @see #transformation_projects */ @JsonProperty("transformation_projects")  public ReferenceList getTransformationProjects() { return this.transformation_projects; }
    /** @see #transformation_projects */ @JsonProperty("transformation_projects")  public void setTransformationProjects(ReferenceList transformation_projects) { this.transformation_projects = transformation_projects; }

    /** @see #data_connections */ @JsonProperty("data_connections")  public ReferenceList getDataConnections() { return this.data_connections; }
    /** @see #data_connections */ @JsonProperty("data_connections")  public void setDataConnections(ReferenceList data_connections) { this.data_connections = data_connections; }

    /** @see #amazon_s3_buckets */ @JsonProperty("amazon_s3_buckets")  public ReferenceList getAmazonS3Buckets() { return this.amazon_s3_buckets; }
    /** @see #amazon_s3_buckets */ @JsonProperty("amazon_s3_buckets")  public void setAmazonS3Buckets(ReferenceList amazon_s3_buckets) { this.amazon_s3_buckets = amazon_s3_buckets; }

    /** @see #data_file_folders */ @JsonProperty("data_file_folders")  public ReferenceList getDataFileFolders() { return this.data_file_folders; }
    /** @see #data_file_folders */ @JsonProperty("data_file_folders")  public void setDataFileFolders(ReferenceList data_file_folders) { this.data_file_folders = data_file_folders; }

    /** @see #location */ @JsonProperty("location")  public String getLocation() { return this.location; }
    /** @see #location */ @JsonProperty("location")  public void setLocation(String location) { this.location = location; }

    /** @see #network_node */ @JsonProperty("network_node")  public String getNetworkNode() { return this.network_node; }
    /** @see #network_node */ @JsonProperty("network_node")  public void setNetworkNode(String network_node) { this.network_node = network_node; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isHost(Object obj) { return (obj.getClass() == Host.class); }

}

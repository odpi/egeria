/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'mapping_specification' asset type in IGC, displayed as 'Mapping Specification' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MappingSpecification extends MainObject {

    public static final String IGC_TYPE_ID = "mapping_specification";

    /**
     * The 'description' property, displayed as 'Description' in the IGC UI.
     */
    protected ArrayList<String> description;

    /**
     * The 'mapping_project' property, displayed as 'Mapping Project' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingProject} objects.
     */
    protected ReferenceList mapping_project;

    /**
     * The 'mappings' property, displayed as 'Mappings' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Mapping} objects.
     */
    protected ReferenceList mappings;

    /**
     * The 'source_database_tables_or_views' property, displayed as 'Source Database Tables or Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList source_database_tables_or_views;

    /**
     * The 'target_database_tables_or_views' property, displayed as 'Target Database Tables or Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Datagroup} objects.
     */
    protected ReferenceList target_database_tables_or_views;

    /**
     * The 'status' property, displayed as 'Status' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>PROCESSING_STARTED (displayed in the UI as 'PROCESSING_STARTED')</li>
     *     <li>REVIEW_COMPLETED (displayed in the UI as 'REVIEW_COMPLETED')</li>
     *     <li>DEPLOYMENT_COMPLETED (displayed in the UI as 'DEPLOYMENT_COMPLETED')</li>
     * </ul>
     */
    protected String status;

    /**
     * The 'owner' property, displayed as 'Owner' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Steward} object.
     */
    protected Reference owner;

    /**
     * The 'include_for_lineage' property, displayed as 'Include for Lineage' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>setToTrue (displayed in the UI as 'True')</li>
     *     <li>setToFalse (displayed in the UI as 'False')</li>
     * </ul>
     */
    protected String include_for_lineage;

    /**
     * The 'joins' property, displayed as 'Joins' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingJoin} objects.
     */
    protected ReferenceList joins;

    /**
     * The 'filters' property, displayed as 'Filters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingFilter} objects.
     */
    protected ReferenceList filters;

    /**
     * The 'generated_jobs' property, displayed as 'Generated Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList generated_jobs;

    /**
     * The 'information_server_reports' property, displayed as 'Information Server Reports' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationServerReportMappingSpecification} objects.
     */
    protected ReferenceList information_server_reports;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #description */ @JsonProperty("description")  public ArrayList<String> getDescription() { return this.description; }
    /** @see #description */ @JsonProperty("description")  public void setDescription(ArrayList<String> description) { this.description = description; }

    /** @see #mapping_project */ @JsonProperty("mapping_project")  public ReferenceList getMappingProject() { return this.mapping_project; }
    /** @see #mapping_project */ @JsonProperty("mapping_project")  public void setMappingProject(ReferenceList mapping_project) { this.mapping_project = mapping_project; }

    /** @see #mappings */ @JsonProperty("mappings")  public ReferenceList getMappings() { return this.mappings; }
    /** @see #mappings */ @JsonProperty("mappings")  public void setMappings(ReferenceList mappings) { this.mappings = mappings; }

    /** @see #source_database_tables_or_views */ @JsonProperty("source_database_tables_or_views")  public ReferenceList getSourceDatabaseTablesOrViews() { return this.source_database_tables_or_views; }
    /** @see #source_database_tables_or_views */ @JsonProperty("source_database_tables_or_views")  public void setSourceDatabaseTablesOrViews(ReferenceList source_database_tables_or_views) { this.source_database_tables_or_views = source_database_tables_or_views; }

    /** @see #target_database_tables_or_views */ @JsonProperty("target_database_tables_or_views")  public ReferenceList getTargetDatabaseTablesOrViews() { return this.target_database_tables_or_views; }
    /** @see #target_database_tables_or_views */ @JsonProperty("target_database_tables_or_views")  public void setTargetDatabaseTablesOrViews(ReferenceList target_database_tables_or_views) { this.target_database_tables_or_views = target_database_tables_or_views; }

    /** @see #status */ @JsonProperty("status")  public String getStatus() { return this.status; }
    /** @see #status */ @JsonProperty("status")  public void setStatus(String status) { this.status = status; }

    /** @see #owner */ @JsonProperty("owner")  public Reference getOwner() { return this.owner; }
    /** @see #owner */ @JsonProperty("owner")  public void setOwner(Reference owner) { this.owner = owner; }

    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public String getIncludeForLineage() { return this.include_for_lineage; }
    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public void setIncludeForLineage(String include_for_lineage) { this.include_for_lineage = include_for_lineage; }

    /** @see #joins */ @JsonProperty("joins")  public ReferenceList getJoins() { return this.joins; }
    /** @see #joins */ @JsonProperty("joins")  public void setJoins(ReferenceList joins) { this.joins = joins; }

    /** @see #filters */ @JsonProperty("filters")  public ReferenceList getFilters() { return this.filters; }
    /** @see #filters */ @JsonProperty("filters")  public void setFilters(ReferenceList filters) { this.filters = filters; }

    /** @see #generated_jobs */ @JsonProperty("generated_jobs")  public ReferenceList getGeneratedJobs() { return this.generated_jobs; }
    /** @see #generated_jobs */ @JsonProperty("generated_jobs")  public void setGeneratedJobs(ReferenceList generated_jobs) { this.generated_jobs = generated_jobs; }

    /** @see #information_server_reports */ @JsonProperty("information_server_reports")  public ReferenceList getInformationServerReports() { return this.information_server_reports; }
    /** @see #information_server_reports */ @JsonProperty("information_server_reports")  public void setInformationServerReports(ReferenceList information_server_reports) { this.information_server_reports = information_server_reports; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isMappingSpecification(Object obj) { return (obj.getClass() == MappingSpecification.class); }

}

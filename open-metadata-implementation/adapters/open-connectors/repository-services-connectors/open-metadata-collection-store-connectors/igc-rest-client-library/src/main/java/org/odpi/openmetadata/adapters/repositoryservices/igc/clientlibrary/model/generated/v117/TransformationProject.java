/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v117;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'transformation_project' asset type in IGC, displayed as 'Transformation Project' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class TransformationProject extends MainObject {

    public static final String IGC_TYPE_ID = "transformation_project";

    /**
     * The 'host_(engine)' property, displayed as 'Host (Engine)' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link HostEngine} object.
     */
    @JsonProperty("host_(engine)") protected Reference host__engine_;

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
     * The 'include_for_lineage_desc' property, displayed as 'Include for Lineage' in the IGC UI.
     */
    protected Boolean include_for_lineage_desc;

    /**
     * The 'folders' property, displayed as 'Folders' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsfolder} objects.
     */
    protected ReferenceList folders;

    /**
     * The 'jobs' property, displayed as 'Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList jobs;

    /**
     * The 'containers' property, displayed as 'Containers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SharedContainer} objects.
     */
    protected ReferenceList containers;

    /**
     * The 'stage_types' property, displayed as 'Stage Types' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DsstageType} objects.
     */
    protected ReferenceList stage_types;

    /**
     * The 'table_definitions' property, displayed as 'Table Definitions' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList table_definitions;

    /**
     * The 'standardization_rule_sets' property, displayed as 'Standardization Rule Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StandardizationRuleSet} objects.
     */
    protected ReferenceList standardization_rule_sets;

    /**
     * The 'match_specifications' property, displayed as 'Match Specifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MatchSpecification} objects.
     */
    protected ReferenceList match_specifications;

    /**
     * The 'parameter_sets' property, displayed as 'Parameter Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ParameterSet} objects.
     */
    protected ReferenceList parameter_sets;

    /**
     * The 'transforms' property, displayed as 'Transforms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TransformsFunction} objects.
     */
    protected ReferenceList transforms;

    /**
     * The 'routines' property, displayed as 'Routines' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Routine} objects.
     */
    protected ReferenceList routines;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #host__engine_ */ @JsonProperty("host_(engine)")  public Reference getHostEngine() { return this.host__engine_; }
    /** @see #host__engine_ */ @JsonProperty("host_(engine)")  public void setHostEngine(Reference host__engine_) { this.host__engine_ = host__engine_; }

    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public String getIncludeForLineage() { return this.include_for_lineage; }
    /** @see #include_for_lineage */ @JsonProperty("include_for_lineage")  public void setIncludeForLineage(String include_for_lineage) { this.include_for_lineage = include_for_lineage; }

    /** @see #include_for_lineage_desc */ @JsonProperty("include_for_lineage_desc")  public Boolean getIncludeForLineageDesc() { return this.include_for_lineage_desc; }
    /** @see #include_for_lineage_desc */ @JsonProperty("include_for_lineage_desc")  public void setIncludeForLineageDesc(Boolean include_for_lineage_desc) { this.include_for_lineage_desc = include_for_lineage_desc; }

    /** @see #folders */ @JsonProperty("folders")  public ReferenceList getFolders() { return this.folders; }
    /** @see #folders */ @JsonProperty("folders")  public void setFolders(ReferenceList folders) { this.folders = folders; }

    /** @see #jobs */ @JsonProperty("jobs")  public ReferenceList getJobs() { return this.jobs; }
    /** @see #jobs */ @JsonProperty("jobs")  public void setJobs(ReferenceList jobs) { this.jobs = jobs; }

    /** @see #containers */ @JsonProperty("containers")  public ReferenceList getContainers() { return this.containers; }
    /** @see #containers */ @JsonProperty("containers")  public void setContainers(ReferenceList containers) { this.containers = containers; }

    /** @see #stage_types */ @JsonProperty("stage_types")  public ReferenceList getStageTypes() { return this.stage_types; }
    /** @see #stage_types */ @JsonProperty("stage_types")  public void setStageTypes(ReferenceList stage_types) { this.stage_types = stage_types; }

    /** @see #table_definitions */ @JsonProperty("table_definitions")  public ReferenceList getTableDefinitions() { return this.table_definitions; }
    /** @see #table_definitions */ @JsonProperty("table_definitions")  public void setTableDefinitions(ReferenceList table_definitions) { this.table_definitions = table_definitions; }

    /** @see #standardization_rule_sets */ @JsonProperty("standardization_rule_sets")  public ReferenceList getStandardizationRuleSets() { return this.standardization_rule_sets; }
    /** @see #standardization_rule_sets */ @JsonProperty("standardization_rule_sets")  public void setStandardizationRuleSets(ReferenceList standardization_rule_sets) { this.standardization_rule_sets = standardization_rule_sets; }

    /** @see #match_specifications */ @JsonProperty("match_specifications")  public ReferenceList getMatchSpecifications() { return this.match_specifications; }
    /** @see #match_specifications */ @JsonProperty("match_specifications")  public void setMatchSpecifications(ReferenceList match_specifications) { this.match_specifications = match_specifications; }

    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public ReferenceList getParameterSets() { return this.parameter_sets; }
    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public void setParameterSets(ReferenceList parameter_sets) { this.parameter_sets = parameter_sets; }

    /** @see #transforms */ @JsonProperty("transforms")  public ReferenceList getTransforms() { return this.transforms; }
    /** @see #transforms */ @JsonProperty("transforms")  public void setTransforms(ReferenceList transforms) { this.transforms = transforms; }

    /** @see #routines */ @JsonProperty("routines")  public ReferenceList getRoutines() { return this.routines; }
    /** @see #routines */ @JsonProperty("routines")  public void setRoutines(ReferenceList routines) { this.routines = routines; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isTransformationProject(Object obj) { return (obj.getClass() == TransformationProject.class); }

}

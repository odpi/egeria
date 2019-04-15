/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11702;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonTypeName;
import javax.annotation.Generated;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the {@code transformation_project} asset type in IGC, displayed as '{@literal Transformation Project}' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@Generated("org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.IGCRestModelGenerator")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeName("transformation_project")
public class TransformationProject extends Reference {

    public static String getIgcTypeDisplayName() { return "Transformation Project"; }

    /**
     * The {@code name} property, displayed as '{@literal Name}' in the IGC UI.
     */
    protected String name;

    /**
     * The {@code host_(engine)} property, displayed as '{@literal Host (Engine)}' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link HostEngine} object.
     */
    @JsonProperty("host_(engine)") protected Reference host__engine_;

    /**
     * The {@code implements_rules} property, displayed as '{@literal Implements Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The {@code governed_by_rules} property, displayed as '{@literal Governed by Rules}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The {@code include_for_lineage} property, displayed as '{@literal Include for Lineage}' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>setToTrue (displayed in the UI as 'True')</li>
     *     <li>setToFalse (displayed in the UI as 'False')</li>
     * </ul>
     */
    protected String include_for_lineage;

    /**
     * The {@code include_for_lineage_desc} property, displayed as '{@literal Include for Lineage}' in the IGC UI.
     */
    protected Boolean include_for_lineage_desc;

    /**
     * The {@code folders} property, displayed as '{@literal Folders}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsfolder} objects.
     */
    protected ReferenceList folders;

    /**
     * The {@code jobs} property, displayed as '{@literal Jobs}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsjob} objects.
     */
    protected ReferenceList jobs;

    /**
     * The {@code containers} property, displayed as '{@literal Containers}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SharedContainer} objects.
     */
    protected ReferenceList containers;

    /**
     * The {@code stage_types} property, displayed as '{@literal Stage Types}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DsstageType} objects.
     */
    protected ReferenceList stage_types;

    /**
     * The {@code table_definitions} property, displayed as '{@literal Table Definitions}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TableDefinition} objects.
     */
    protected ReferenceList table_definitions;

    /**
     * The {@code standardization_rule_sets} property, displayed as '{@literal Standardization Rule Sets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StandardizationRuleSet} objects.
     */
    protected ReferenceList standardization_rule_sets;

    /**
     * The {@code match_specifications} property, displayed as '{@literal Match Specifications}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MatchSpecification} objects.
     */
    protected ReferenceList match_specifications;

    /**
     * The {@code parameter_sets} property, displayed as '{@literal Parameter Sets}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ParameterSet} objects.
     */
    protected ReferenceList parameter_sets;

    /**
     * The {@code transforms} property, displayed as '{@literal Transforms}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TransformsFunction} objects.
     */
    protected ReferenceList transforms;

    /**
     * The {@code routines} property, displayed as '{@literal Routines}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Routine} objects.
     */
    protected ReferenceList routines;

    /**
     * The {@code in_collections} property, displayed as '{@literal In Collections}' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The {@code created_by} property, displayed as '{@literal Created By}' in the IGC UI.
     */
    protected String created_by;

    /**
     * The {@code created_on} property, displayed as '{@literal Created On}' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The {@code modified_by} property, displayed as '{@literal Modified By}' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The {@code modified_on} property, displayed as '{@literal Modified On}' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #host__engine_ */ @JsonProperty("host_(engine)")  public Reference getHostEngine() { return this.host__engine_; }
    /** @see #host__engine_ */ @JsonProperty("host_(engine)")  public void setHostEngine(Reference host__engine_) { this.host__engine_ = host__engine_; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

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

    /** @see #created_by */ @JsonProperty("created_by")  public String getCreatedBy() { return this.created_by; }
    /** @see #created_by */ @JsonProperty("created_by")  public void setCreatedBy(String created_by) { this.created_by = created_by; }

    /** @see #created_on */ @JsonProperty("created_on")  public Date getCreatedOn() { return this.created_on; }
    /** @see #created_on */ @JsonProperty("created_on")  public void setCreatedOn(Date created_on) { this.created_on = created_on; }

    /** @see #modified_by */ @JsonProperty("modified_by")  public String getModifiedBy() { return this.modified_by; }
    /** @see #modified_by */ @JsonProperty("modified_by")  public void setModifiedBy(String modified_by) { this.modified_by = modified_by; }

    /** @see #modified_on */ @JsonProperty("modified_on")  public Date getModifiedOn() { return this.modified_on; }
    /** @see #modified_on */ @JsonProperty("modified_on")  public void setModifiedOn(Date modified_on) { this.modified_on = modified_on; }

    public static Boolean canBeCreated() { return false; }
    public static Boolean includesModificationDetails() { return true; }
    private static final List<String> NON_RELATIONAL_PROPERTIES = Arrays.asList(
        "name",
        "include_for_lineage",
        "include_for_lineage_desc",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "implements_rules",
        "governed_by_rules",
        "folders",
        "jobs",
        "containers",
        "stage_types",
        "table_definitions",
        "standardization_rule_sets",
        "match_specifications",
        "parameter_sets",
        "transforms",
        "routines",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "host_(engine)",
        "implements_rules",
        "governed_by_rules",
        "include_for_lineage",
        "include_for_lineage_desc",
        "folders",
        "jobs",
        "containers",
        "stage_types",
        "table_definitions",
        "standardization_rule_sets",
        "match_specifications",
        "parameter_sets",
        "transforms",
        "routines",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getStringProperties() { return STRING_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isTransformationProject(Object obj) { return (obj.getClass() == TransformationProject.class); }

}

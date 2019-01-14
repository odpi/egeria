/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11700;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'shared_container' asset type in IGC, displayed as 'Shared Container' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SharedContainer extends Reference {

    public static String getIgcTypeId() { return "shared_container"; }
    public static String getIgcTypeDisplayName() { return "Shared Container"; }

    /**
     * The 'name' property, displayed as 'Name' in the IGC UI.
     */
    protected String name;

    /**
     * The 'short_description' property, displayed as 'Short Description' in the IGC UI.
     */
    protected String short_description;

    /**
     * The 'long_description' property, displayed as 'Long Description' in the IGC UI.
     */
    protected String long_description;

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'labels' property, displayed as 'Labels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Label} objects.
     */
    protected ReferenceList labels;

    /**
     * The 'stewards' property, displayed as 'Stewards' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AsclSteward} objects.
     */
    protected ReferenceList stewards;

    /**
     * The 'assigned_to_terms' property, displayed as 'Assigned to Terms' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Term} objects.
     */
    protected ReferenceList assigned_to_terms;

    /**
     * The 'implements_rules' property, displayed as 'Implements Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList implements_rules;

    /**
     * The 'governed_by_rules' property, displayed as 'Governed by Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationGovernanceRule} objects.
     */
    protected ReferenceList governed_by_rules;

    /**
     * The 'stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList stages;

    /**
     * The 'referenced_by_stages' property, displayed as 'Referenced by Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList referenced_by_stages;

    /**
     * The 'references_containers' property, displayed as 'References Containers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ReferencedContainer} objects.
     */
    protected ReferenceList references_containers;

    /**
     * The 'referenced_by_containers' property, displayed as 'Referenced by Containers' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SharedContainer} objects.
     */
    protected ReferenceList referenced_by_containers;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>SERVER (displayed in the UI as 'SERVER')</li>
     *     <li>MAINFRAME (displayed in the UI as 'MAINFRAME')</li>
     *     <li>SEQUENCE (displayed in the UI as 'SEQUENCE')</li>
     *     <li>PARALLEL (displayed in the UI as 'PARALLEL')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'parameters' property, displayed as 'Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Dsparameter} objects.
     */
    protected ReferenceList parameters;

    /**
     * The 'parameter_sets' property, displayed as 'Parameter Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DsparameterSet} objects.
     */
    protected ReferenceList parameter_sets;

    /**
     * The 'annotations' property, displayed as 'Annotations' in the IGC UI.
     */
    protected ArrayList<String> annotations;

    /**
     * The 'mapping_components' property, displayed as 'Mapping Components' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MappingComponent} objects.
     */
    protected ReferenceList mapping_components;

    /**
     * The 'source_mappings' property, displayed as 'Source Mappings' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList source_mappings;

    /**
     * The 'target_mappings' property, displayed as 'Target Mappings' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList target_mappings;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;

    /**
     * The 'created_by' property, displayed as 'Created By' in the IGC UI.
     */
    protected String created_by;

    /**
     * The 'created_on' property, displayed as 'Created On' in the IGC UI.
     */
    protected Date created_on;

    /**
     * The 'modified_by' property, displayed as 'Modified By' in the IGC UI.
     */
    protected String modified_by;

    /**
     * The 'modified_on' property, displayed as 'Modified On' in the IGC UI.
     */
    protected Date modified_on;


    /** @see #name */ @JsonProperty("name")  public String getTheName() { return this.name; }
    /** @see #name */ @JsonProperty("name")  public void setTheName(String name) { this.name = name; }

    /** @see #short_description */ @JsonProperty("short_description")  public String getShortDescription() { return this.short_description; }
    /** @see #short_description */ @JsonProperty("short_description")  public void setShortDescription(String short_description) { this.short_description = short_description; }

    /** @see #long_description */ @JsonProperty("long_description")  public String getLongDescription() { return this.long_description; }
    /** @see #long_description */ @JsonProperty("long_description")  public void setLongDescription(String long_description) { this.long_description = long_description; }

    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #labels */ @JsonProperty("labels")  public ReferenceList getLabels() { return this.labels; }
    /** @see #labels */ @JsonProperty("labels")  public void setLabels(ReferenceList labels) { this.labels = labels; }

    /** @see #stewards */ @JsonProperty("stewards")  public ReferenceList getStewards() { return this.stewards; }
    /** @see #stewards */ @JsonProperty("stewards")  public void setStewards(ReferenceList stewards) { this.stewards = stewards; }

    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public ReferenceList getAssignedToTerms() { return this.assigned_to_terms; }
    /** @see #assigned_to_terms */ @JsonProperty("assigned_to_terms")  public void setAssignedToTerms(ReferenceList assigned_to_terms) { this.assigned_to_terms = assigned_to_terms; }

    /** @see #implements_rules */ @JsonProperty("implements_rules")  public ReferenceList getImplementsRules() { return this.implements_rules; }
    /** @see #implements_rules */ @JsonProperty("implements_rules")  public void setImplementsRules(ReferenceList implements_rules) { this.implements_rules = implements_rules; }

    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public ReferenceList getGovernedByRules() { return this.governed_by_rules; }
    /** @see #governed_by_rules */ @JsonProperty("governed_by_rules")  public void setGovernedByRules(ReferenceList governed_by_rules) { this.governed_by_rules = governed_by_rules; }

    /** @see #stages */ @JsonProperty("stages")  public ReferenceList getStages() { return this.stages; }
    /** @see #stages */ @JsonProperty("stages")  public void setStages(ReferenceList stages) { this.stages = stages; }

    /** @see #referenced_by_stages */ @JsonProperty("referenced_by_stages")  public ReferenceList getReferencedByStages() { return this.referenced_by_stages; }
    /** @see #referenced_by_stages */ @JsonProperty("referenced_by_stages")  public void setReferencedByStages(ReferenceList referenced_by_stages) { this.referenced_by_stages = referenced_by_stages; }

    /** @see #references_containers */ @JsonProperty("references_containers")  public ReferenceList getReferencesContainers() { return this.references_containers; }
    /** @see #references_containers */ @JsonProperty("references_containers")  public void setReferencesContainers(ReferenceList references_containers) { this.references_containers = references_containers; }

    /** @see #referenced_by_containers */ @JsonProperty("referenced_by_containers")  public ReferenceList getReferencedByContainers() { return this.referenced_by_containers; }
    /** @see #referenced_by_containers */ @JsonProperty("referenced_by_containers")  public void setReferencedByContainers(ReferenceList referenced_by_containers) { this.referenced_by_containers = referenced_by_containers; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #parameters */ @JsonProperty("parameters")  public ReferenceList getParameters() { return this.parameters; }
    /** @see #parameters */ @JsonProperty("parameters")  public void setParameters(ReferenceList parameters) { this.parameters = parameters; }

    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public ReferenceList getParameterSets() { return this.parameter_sets; }
    /** @see #parameter_sets */ @JsonProperty("parameter_sets")  public void setParameterSets(ReferenceList parameter_sets) { this.parameter_sets = parameter_sets; }

    /** @see #annotations */ @JsonProperty("annotations")  public ArrayList<String> getAnnotations() { return this.annotations; }
    /** @see #annotations */ @JsonProperty("annotations")  public void setAnnotations(ArrayList<String> annotations) { this.annotations = annotations; }

    /** @see #mapping_components */ @JsonProperty("mapping_components")  public ReferenceList getMappingComponents() { return this.mapping_components; }
    /** @see #mapping_components */ @JsonProperty("mapping_components")  public void setMappingComponents(ReferenceList mapping_components) { this.mapping_components = mapping_components; }

    /** @see #source_mappings */ @JsonProperty("source_mappings")  public ReferenceList getSourceMappings() { return this.source_mappings; }
    /** @see #source_mappings */ @JsonProperty("source_mappings")  public void setSourceMappings(ReferenceList source_mappings) { this.source_mappings = source_mappings; }

    /** @see #target_mappings */ @JsonProperty("target_mappings")  public ReferenceList getTargetMappings() { return this.target_mappings; }
    /** @see #target_mappings */ @JsonProperty("target_mappings")  public void setTargetMappings(ReferenceList target_mappings) { this.target_mappings = target_mappings; }

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
        "short_description",
        "long_description",
        "type",
        "annotations",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "stages",
        "referenced_by_stages",
        "references_containers",
        "referenced_by_containers",
        "parameters",
        "parameter_sets",
        "mapping_components",
        "source_mappings",
        "target_mappings",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "transformation_project",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "stages",
        "referenced_by_stages",
        "references_containers",
        "referenced_by_containers",
        "type",
        "parameters",
        "parameter_sets",
        "annotations",
        "mapping_components",
        "source_mappings",
        "target_mappings",
        "in_collections",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    public static List<String> getNonRelationshipProperties() { return NON_RELATIONAL_PROPERTIES; }
    public static List<String> getPagedRelationshipProperties() { return PAGED_RELATIONAL_PROPERTIES; }
    public static List<String> getAllProperties() { return ALL_PROPERTIES; }
    public static Boolean isSharedContainer(Object obj) { return (obj.getClass() == SharedContainer.class); }

}

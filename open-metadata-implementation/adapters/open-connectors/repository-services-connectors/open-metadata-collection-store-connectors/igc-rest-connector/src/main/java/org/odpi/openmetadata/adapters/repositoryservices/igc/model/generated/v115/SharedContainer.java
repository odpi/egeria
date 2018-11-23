/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'shared_container' asset type in IGC, displayed as 'Shared Container' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class SharedContainer extends MainObject {

    public static final String IGC_TYPE_ID = "shared_container";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

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


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

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

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isSharedContainer(Object obj) { return (obj.getClass() == SharedContainer.class); }

}

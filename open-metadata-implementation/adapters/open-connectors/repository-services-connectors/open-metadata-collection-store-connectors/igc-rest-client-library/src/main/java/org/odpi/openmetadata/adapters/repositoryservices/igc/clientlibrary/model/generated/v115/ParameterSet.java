/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'parameter_set' asset type in IGC, displayed as 'Parameter Set' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class ParameterSet extends MainObject {

    public static final String IGC_TYPE_ID = "parameter_set";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'jobs' property, displayed as 'Jobs' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Jobdef} objects.
     */
    protected ReferenceList jobs;

    /**
     * The 'parameters' property, displayed as 'Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DsparameterSet} objects.
     */
    protected ReferenceList parameters;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #jobs */ @JsonProperty("jobs")  public ReferenceList getJobs() { return this.jobs; }
    /** @see #jobs */ @JsonProperty("jobs")  public void setJobs(ReferenceList jobs) { this.jobs = jobs; }

    /** @see #parameters */ @JsonProperty("parameters")  public ReferenceList getParameters() { return this.parameters; }
    /** @see #parameters */ @JsonProperty("parameters")  public void setParameters(ReferenceList parameters) { this.parameters = parameters; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isParameterSet(Object obj) { return (obj.getClass() == ParameterSet.class); }

}

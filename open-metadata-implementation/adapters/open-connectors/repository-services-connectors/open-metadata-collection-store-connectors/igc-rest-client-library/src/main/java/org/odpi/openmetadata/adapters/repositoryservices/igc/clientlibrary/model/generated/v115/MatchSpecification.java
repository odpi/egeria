/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.Reference;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

/**
 * POJO for the 'match_specification' asset type in IGC, displayed as 'Match Specification' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class MatchSpecification extends MainObject {

    public static final String IGC_TYPE_ID = "match_specification";

    /**
     * The 'transformation_project' property, displayed as 'Transformation Project' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link TransformationProject} object.
     */
    protected Reference transformation_project;

    /**
     * The 'folder' property, displayed as 'Folder' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link Dsfolder} object.
     */
    protected Reference folder;

    /**
     * The 'used_by_stages' property, displayed as 'Stages' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Stage} objects.
     */
    protected ReferenceList used_by_stages;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'data_quality_specifications' property, displayed as 'Data Quality Specifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StandardizationObject} objects.
     */
    protected ReferenceList data_quality_specifications;

    /**
     * The 'dataFields' property, displayed as 'Data Fields' in the IGC UI.
     */
    protected ArrayList<String> dataFields;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #transformation_project */ @JsonProperty("transformation_project")  public Reference getTransformationProject() { return this.transformation_project; }
    /** @see #transformation_project */ @JsonProperty("transformation_project")  public void setTransformationProject(Reference transformation_project) { this.transformation_project = transformation_project; }

    /** @see #folder */ @JsonProperty("folder")  public Reference getFolder() { return this.folder; }
    /** @see #folder */ @JsonProperty("folder")  public void setFolder(Reference folder) { this.folder = folder; }

    /** @see #used_by_stages */ @JsonProperty("used_by_stages")  public ReferenceList getUsedByStages() { return this.used_by_stages; }
    /** @see #used_by_stages */ @JsonProperty("used_by_stages")  public void setUsedByStages(ReferenceList used_by_stages) { this.used_by_stages = used_by_stages; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #data_quality_specifications */ @JsonProperty("data_quality_specifications")  public ReferenceList getDataQualitySpecifications() { return this.data_quality_specifications; }
    /** @see #data_quality_specifications */ @JsonProperty("data_quality_specifications")  public void setDataQualitySpecifications(ReferenceList data_quality_specifications) { this.data_quality_specifications = data_quality_specifications; }

    /** @see #dataFields */ @JsonProperty("dataFields")  public ArrayList<String> getDataFields() { return this.dataFields; }
    /** @see #dataFields */ @JsonProperty("dataFields")  public void setDataFields(ArrayList<String> dataFields) { this.dataFields = dataFields; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isMatchSpecification(Object obj) { return (obj.getClass() == MatchSpecification.class); }

}

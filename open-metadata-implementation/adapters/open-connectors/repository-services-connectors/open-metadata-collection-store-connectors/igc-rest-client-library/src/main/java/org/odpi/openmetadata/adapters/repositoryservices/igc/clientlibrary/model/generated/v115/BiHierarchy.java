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
 * POJO for the 'bi_hierarchy' asset type in IGC, displayed as 'BI Hierarchy' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class BiHierarchy extends MainObject {

    public static final String IGC_TYPE_ID = "bi_hierarchy";

    /**
     * The 'context' property, displayed as 'Context' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Olapobject} objects.
     */
    protected ReferenceList context;

    /**
     * The 'bi_model' property, displayed as 'BI Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiModel} object.
     */
    protected Reference bi_model;

    /**
     * The 'bi_collection' property, displayed as 'BI Collection' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link BiCollection} object.
     */
    protected Reference bi_collection;

    /**
     * The 'bi_levels' property, displayed as 'BI Levels' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiLevel} objects.
     */
    protected ReferenceList bi_levels;

    /**
     * The 'type' property, displayed as 'Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>BALANCED (displayed in the UI as 'BALANCED')</li>
     *     <li>UNBALANCED (displayed in the UI as 'UNBALANCED')</li>
     *     <li>RAGGED (displayed in the UI as 'RAGGED')</li>
     *     <li>NETWORK (displayed in the UI as 'NETWORK')</li>
     * </ul>
     */
    protected String type;

    /**
     * The 'deployment' property, displayed as 'Deployment' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>STANDARD (displayed in the UI as 'STANDARD')</li>
     *     <li>RECURSIVE (displayed in the UI as 'RECURSIVE')</li>
     * </ul>
     */
    protected String deployment;

    /**
     * The 'references_bi_collections' property, displayed as 'References BI Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link BiCollection} objects.
     */
    protected ReferenceList references_bi_collections;


    /** @see #context */ @JsonProperty("context")  public ReferenceList getTheContext() { return this.context; }
    /** @see #context */ @JsonProperty("context")  public void setTheContext(ReferenceList context) { this.context = context; }

    /** @see #bi_model */ @JsonProperty("bi_model")  public Reference getBiModel() { return this.bi_model; }
    /** @see #bi_model */ @JsonProperty("bi_model")  public void setBiModel(Reference bi_model) { this.bi_model = bi_model; }

    /** @see #bi_collection */ @JsonProperty("bi_collection")  public Reference getBiCollection() { return this.bi_collection; }
    /** @see #bi_collection */ @JsonProperty("bi_collection")  public void setBiCollection(Reference bi_collection) { this.bi_collection = bi_collection; }

    /** @see #bi_levels */ @JsonProperty("bi_levels")  public ReferenceList getBiLevels() { return this.bi_levels; }
    /** @see #bi_levels */ @JsonProperty("bi_levels")  public void setBiLevels(ReferenceList bi_levels) { this.bi_levels = bi_levels; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #deployment */ @JsonProperty("deployment")  public String getDeployment() { return this.deployment; }
    /** @see #deployment */ @JsonProperty("deployment")  public void setDeployment(String deployment) { this.deployment = deployment; }

    /** @see #references_bi_collections */ @JsonProperty("references_bi_collections")  public ReferenceList getReferencesBiCollections() { return this.references_bi_collections; }
    /** @see #references_bi_collections */ @JsonProperty("references_bi_collections")  public void setReferencesBiCollections(ReferenceList references_bi_collections) { this.references_bi_collections = references_bi_collections; }


    public static final Boolean isBiHierarchy(Object obj) { return (obj.getClass() == BiHierarchy.class); }

}

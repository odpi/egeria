/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.InformationAsset;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.MainObject;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.ReferenceList;
import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * POJO for the 'stored_procedure_definition' asset type in IGC, displayed as 'Stored Procedure Definition' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class StoredProcedureDefinition extends MainObject {

    public static final String IGC_TYPE_ID = "stored_procedure_definition";

    /**
     * The 'in_parameters' property, displayed as 'In Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InParameter} objects.
     */
    protected ReferenceList in_parameters;

    /**
     * The 'out_parameters' property, displayed as 'Out Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link OutParameter} objects.
     */
    protected ReferenceList out_parameters;

    /**
     * The 'inout_parameters' property, displayed as 'InOut Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InoutParameter} objects.
     */
    protected ReferenceList inout_parameters;

    /**
     * The 'result_columns' property, displayed as 'Result Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link ResultColumn} objects.
     */
    protected ReferenceList result_columns;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'reads_from_(static)' property, displayed as 'Reads from (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(static)") protected ReferenceList reads_from__static_;

    /**
     * The 'writes_to_(static)' property, displayed as 'Writes to (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(static)") protected ReferenceList writes_to__static_;

    /**
     * The 'reads_from_(design)' property, displayed as 'Reads from (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(design)") protected ReferenceList reads_from__design_;

    /**
     * The 'writes_to_(design)' property, displayed as 'Writes to (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(design)") protected ReferenceList writes_to__design_;

    /**
     * The 'reads_from_(operational)' property, displayed as 'Reads from (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(operational)") protected ReferenceList reads_from__operational_;

    /**
     * The 'writes_to_(operational)' property, displayed as 'Writes to (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(operational)") protected ReferenceList writes_to__operational_;

    /**
     * The 'reads_from_(user_defined)' property, displayed as 'Reads from (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("reads_from_(user_defined)") protected ReferenceList reads_from__user_defined_;

    /**
     * The 'writes_to_(user_defined)' property, displayed as 'Writes to (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("writes_to_(user_defined)") protected ReferenceList writes_to__user_defined_;

    /**
     * The 'impacted_by' property, displayed as 'Impacted by' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacted_by;

    /**
     * The 'impacts_on' property, displayed as 'Impacts on' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    protected ReferenceList impacts_on;

    /**
     * The 'include_for_business_lineage' property, displayed as 'Include for Business Lineage' in the IGC UI.
     */
    protected Boolean include_for_business_lineage;

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


    /** @see #in_parameters */ @JsonProperty("in_parameters")  public ReferenceList getInParameters() { return this.in_parameters; }
    /** @see #in_parameters */ @JsonProperty("in_parameters")  public void setInParameters(ReferenceList in_parameters) { this.in_parameters = in_parameters; }

    /** @see #out_parameters */ @JsonProperty("out_parameters")  public ReferenceList getOutParameters() { return this.out_parameters; }
    /** @see #out_parameters */ @JsonProperty("out_parameters")  public void setOutParameters(ReferenceList out_parameters) { this.out_parameters = out_parameters; }

    /** @see #inout_parameters */ @JsonProperty("inout_parameters")  public ReferenceList getInoutParameters() { return this.inout_parameters; }
    /** @see #inout_parameters */ @JsonProperty("inout_parameters")  public void setInoutParameters(ReferenceList inout_parameters) { this.inout_parameters = inout_parameters; }

    /** @see #result_columns */ @JsonProperty("result_columns")  public ReferenceList getResultColumns() { return this.result_columns; }
    /** @see #result_columns */ @JsonProperty("result_columns")  public void setResultColumns(ReferenceList result_columns) { this.result_columns = result_columns; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #reads_from__static_ */ @JsonProperty("reads_from_(static)")  public ReferenceList getReadsFromStatic() { return this.reads_from__static_; }
    /** @see #reads_from__static_ */ @JsonProperty("reads_from_(static)")  public void setReadsFromStatic(ReferenceList reads_from__static_) { this.reads_from__static_ = reads_from__static_; }

    /** @see #writes_to__static_ */ @JsonProperty("writes_to_(static)")  public ReferenceList getWritesToStatic() { return this.writes_to__static_; }
    /** @see #writes_to__static_ */ @JsonProperty("writes_to_(static)")  public void setWritesToStatic(ReferenceList writes_to__static_) { this.writes_to__static_ = writes_to__static_; }

    /** @see #reads_from__design_ */ @JsonProperty("reads_from_(design)")  public ReferenceList getReadsFromDesign() { return this.reads_from__design_; }
    /** @see #reads_from__design_ */ @JsonProperty("reads_from_(design)")  public void setReadsFromDesign(ReferenceList reads_from__design_) { this.reads_from__design_ = reads_from__design_; }

    /** @see #writes_to__design_ */ @JsonProperty("writes_to_(design)")  public ReferenceList getWritesToDesign() { return this.writes_to__design_; }
    /** @see #writes_to__design_ */ @JsonProperty("writes_to_(design)")  public void setWritesToDesign(ReferenceList writes_to__design_) { this.writes_to__design_ = writes_to__design_; }

    /** @see #reads_from__operational_ */ @JsonProperty("reads_from_(operational)")  public ReferenceList getReadsFromOperational() { return this.reads_from__operational_; }
    /** @see #reads_from__operational_ */ @JsonProperty("reads_from_(operational)")  public void setReadsFromOperational(ReferenceList reads_from__operational_) { this.reads_from__operational_ = reads_from__operational_; }

    /** @see #writes_to__operational_ */ @JsonProperty("writes_to_(operational)")  public ReferenceList getWritesToOperational() { return this.writes_to__operational_; }
    /** @see #writes_to__operational_ */ @JsonProperty("writes_to_(operational)")  public void setWritesToOperational(ReferenceList writes_to__operational_) { this.writes_to__operational_ = writes_to__operational_; }

    /** @see #reads_from__user_defined_ */ @JsonProperty("reads_from_(user_defined)")  public ReferenceList getReadsFromUserDefined() { return this.reads_from__user_defined_; }
    /** @see #reads_from__user_defined_ */ @JsonProperty("reads_from_(user_defined)")  public void setReadsFromUserDefined(ReferenceList reads_from__user_defined_) { this.reads_from__user_defined_ = reads_from__user_defined_; }

    /** @see #writes_to__user_defined_ */ @JsonProperty("writes_to_(user_defined)")  public ReferenceList getWritesToUserDefined() { return this.writes_to__user_defined_; }
    /** @see #writes_to__user_defined_ */ @JsonProperty("writes_to_(user_defined)")  public void setWritesToUserDefined(ReferenceList writes_to__user_defined_) { this.writes_to__user_defined_ = writes_to__user_defined_; }

    /** @see #impacted_by */ @JsonProperty("impacted_by")  public ReferenceList getImpactedBy() { return this.impacted_by; }
    /** @see #impacted_by */ @JsonProperty("impacted_by")  public void setImpactedBy(ReferenceList impacted_by) { this.impacted_by = impacted_by; }

    /** @see #impacts_on */ @JsonProperty("impacts_on")  public ReferenceList getImpactsOn() { return this.impacts_on; }
    /** @see #impacts_on */ @JsonProperty("impacts_on")  public void setImpactsOn(ReferenceList impacts_on) { this.impacts_on = impacts_on; }

    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public Boolean getIncludeForBusinessLineage() { return this.include_for_business_lineage; }
    /** @see #include_for_business_lineage */ @JsonProperty("include_for_business_lineage")  public void setIncludeForBusinessLineage(Boolean include_for_business_lineage) { this.include_for_business_lineage = include_for_business_lineage; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isStoredProcedureDefinition(Object obj) { return (obj.getClass() == StoredProcedureDefinition.class); }

}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'design_stored_procedure' asset type in IGC, displayed as 'Design Stored Procedure' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DesignStoredProcedure extends MainObject {

    public static final String IGC_TYPE_ID = "design_stored_procedure";

    /**
     * The 'physical_data_model' property, displayed as 'Physical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PhysicalDataModel} object.
     */
    protected Reference physical_data_model;

    /**
     * The 'implemented_by_stored_procedures' property, displayed as 'Implemented by Stored Procedures' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link StoredProcedure} objects.
     */
    protected ReferenceList implemented_by_stored_procedures;

    /**
     * The 'design_stored_procedure_parameters' property, displayed as 'Design Stored Procedure Parameters' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignStoredProcedureParameter} objects.
     */
    protected ReferenceList design_stored_procedure_parameters;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'name_qualifier' property, displayed as 'Name Qualifier' in the IGC UI.
     */
    protected String name_qualifier;

    /**
     * The 'source_code' property, displayed as 'Source Code' in the IGC UI.
     */
    protected String source_code;

    /**
     * The 'error_code' property, displayed as 'Error Code' in the IGC UI.
     */
    protected String error_code;

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


    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public Reference getPhysicalDataModel() { return this.physical_data_model; }
    /** @see #physical_data_model */ @JsonProperty("physical_data_model")  public void setPhysicalDataModel(Reference physical_data_model) { this.physical_data_model = physical_data_model; }

    /** @see #implemented_by_stored_procedures */ @JsonProperty("implemented_by_stored_procedures")  public ReferenceList getImplementedByStoredProcedures() { return this.implemented_by_stored_procedures; }
    /** @see #implemented_by_stored_procedures */ @JsonProperty("implemented_by_stored_procedures")  public void setImplementedByStoredProcedures(ReferenceList implemented_by_stored_procedures) { this.implemented_by_stored_procedures = implemented_by_stored_procedures; }

    /** @see #design_stored_procedure_parameters */ @JsonProperty("design_stored_procedure_parameters")  public ReferenceList getDesignStoredProcedureParameters() { return this.design_stored_procedure_parameters; }
    /** @see #design_stored_procedure_parameters */ @JsonProperty("design_stored_procedure_parameters")  public void setDesignStoredProcedureParameters(ReferenceList design_stored_procedure_parameters) { this.design_stored_procedure_parameters = design_stored_procedure_parameters; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #name_qualifier */ @JsonProperty("name_qualifier")  public String getNameQualifier() { return this.name_qualifier; }
    /** @see #name_qualifier */ @JsonProperty("name_qualifier")  public void setNameQualifier(String name_qualifier) { this.name_qualifier = name_qualifier; }

    /** @see #source_code */ @JsonProperty("source_code")  public String getSourceCode() { return this.source_code; }
    /** @see #source_code */ @JsonProperty("source_code")  public void setSourceCode(String source_code) { this.source_code = source_code; }

    /** @see #error_code */ @JsonProperty("error_code")  public String getErrorCode() { return this.error_code; }
    /** @see #error_code */ @JsonProperty("error_code")  public void setErrorCode(String error_code) { this.error_code = error_code; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDesignStoredProcedure(Object obj) { return (obj.getClass() == DesignStoredProcedure.class); }

}

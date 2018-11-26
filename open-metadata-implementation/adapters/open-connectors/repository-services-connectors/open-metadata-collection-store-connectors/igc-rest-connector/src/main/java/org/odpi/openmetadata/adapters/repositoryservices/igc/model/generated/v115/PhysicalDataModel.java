/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'physical_data_model' asset type in IGC, displayed as 'Physical Data Model' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class PhysicalDataModel extends MainObject {

    public static final String IGC_TYPE_ID = "physical_data_model";

    /**
     * The 'physical_model' property, displayed as 'Physical Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link PhysicalDataModel} object.
     */
    protected Reference physical_model;

    /**
     * The 'contains_physical_models' property, displayed as 'Contains Physical Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalDataModel} objects.
     */
    protected ReferenceList contains_physical_models;

    /**
     * The 'implements_logical_data_models' property, displayed as 'Implements Logical Data Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalDataModel} objects.
     */
    protected ReferenceList implements_logical_data_models;

    /**
     * The 'implemented_by_data_files' property, displayed as 'Implemented By Data Files' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link MainObject} objects.
     */
    protected ReferenceList implemented_by_data_files;

    /**
     * The 'implemented_by_database_schemas' property, displayed as 'Implemented by Database Schemas' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseSchema} objects.
     */
    protected ReferenceList implemented_by_database_schemas;

    /**
     * The 'contains_design_tables' property, displayed as 'Contains Design Tables' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignTable} objects.
     */
    protected ReferenceList contains_design_tables;

    /**
     * The 'contains_design_views' property, displayed as 'Contains Design Views' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignView} objects.
     */
    protected ReferenceList contains_design_views;

    /**
     * The 'contains_design_stored_procedures' property, displayed as 'Contains Design Stored Procedures' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignStoredProcedure} objects.
     */
    protected ReferenceList contains_design_stored_procedures;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'author' property, displayed as 'Author' in the IGC UI.
     */
    protected String author;

    /**
     * The 'target_database' property, displayed as 'Target Database' in the IGC UI.
     */
    protected String target_database;

    /**
     * The 'target_dbms' property, displayed as 'Target DBMS' in the IGC UI.
     */
    protected String target_dbms;

    /**
     * The 'native_id' property, displayed as 'Native ID' in the IGC UI.
     */
    protected String native_id;

    /**
     * The 'package' property, displayed as 'Package' in the IGC UI.
     */
    @JsonProperty("package") protected String __package;

    /**
     * The 'namespace' property, displayed as 'Namespace' in the IGC UI.
     */
    protected String namespace;

    /**
     * The 'imported_from' property, displayed as 'Imported From' in the IGC UI.
     */
    protected String imported_from;

    /**
     * The 'physical_domains' property, displayed as 'Physical Domains' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalDomain} objects.
     */
    protected ReferenceList physical_domains;

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


    /** @see #physical_model */ @JsonProperty("physical_model")  public Reference getPhysicalModel() { return this.physical_model; }
    /** @see #physical_model */ @JsonProperty("physical_model")  public void setPhysicalModel(Reference physical_model) { this.physical_model = physical_model; }

    /** @see #contains_physical_models */ @JsonProperty("contains_physical_models")  public ReferenceList getContainsPhysicalModels() { return this.contains_physical_models; }
    /** @see #contains_physical_models */ @JsonProperty("contains_physical_models")  public void setContainsPhysicalModels(ReferenceList contains_physical_models) { this.contains_physical_models = contains_physical_models; }

    /** @see #implements_logical_data_models */ @JsonProperty("implements_logical_data_models")  public ReferenceList getImplementsLogicalDataModels() { return this.implements_logical_data_models; }
    /** @see #implements_logical_data_models */ @JsonProperty("implements_logical_data_models")  public void setImplementsLogicalDataModels(ReferenceList implements_logical_data_models) { this.implements_logical_data_models = implements_logical_data_models; }

    /** @see #implemented_by_data_files */ @JsonProperty("implemented_by_data_files")  public ReferenceList getImplementedByDataFiles() { return this.implemented_by_data_files; }
    /** @see #implemented_by_data_files */ @JsonProperty("implemented_by_data_files")  public void setImplementedByDataFiles(ReferenceList implemented_by_data_files) { this.implemented_by_data_files = implemented_by_data_files; }

    /** @see #implemented_by_database_schemas */ @JsonProperty("implemented_by_database_schemas")  public ReferenceList getImplementedByDatabaseSchemas() { return this.implemented_by_database_schemas; }
    /** @see #implemented_by_database_schemas */ @JsonProperty("implemented_by_database_schemas")  public void setImplementedByDatabaseSchemas(ReferenceList implemented_by_database_schemas) { this.implemented_by_database_schemas = implemented_by_database_schemas; }

    /** @see #contains_design_tables */ @JsonProperty("contains_design_tables")  public ReferenceList getContainsDesignTables() { return this.contains_design_tables; }
    /** @see #contains_design_tables */ @JsonProperty("contains_design_tables")  public void setContainsDesignTables(ReferenceList contains_design_tables) { this.contains_design_tables = contains_design_tables; }

    /** @see #contains_design_views */ @JsonProperty("contains_design_views")  public ReferenceList getContainsDesignViews() { return this.contains_design_views; }
    /** @see #contains_design_views */ @JsonProperty("contains_design_views")  public void setContainsDesignViews(ReferenceList contains_design_views) { this.contains_design_views = contains_design_views; }

    /** @see #contains_design_stored_procedures */ @JsonProperty("contains_design_stored_procedures")  public ReferenceList getContainsDesignStoredProcedures() { return this.contains_design_stored_procedures; }
    /** @see #contains_design_stored_procedures */ @JsonProperty("contains_design_stored_procedures")  public void setContainsDesignStoredProcedures(ReferenceList contains_design_stored_procedures) { this.contains_design_stored_procedures = contains_design_stored_procedures; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    /** @see #target_database */ @JsonProperty("target_database")  public String getTargetDatabase() { return this.target_database; }
    /** @see #target_database */ @JsonProperty("target_database")  public void setTargetDatabase(String target_database) { this.target_database = target_database; }

    /** @see #target_dbms */ @JsonProperty("target_dbms")  public String getTargetDbms() { return this.target_dbms; }
    /** @see #target_dbms */ @JsonProperty("target_dbms")  public void setTargetDbms(String target_dbms) { this.target_dbms = target_dbms; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

    /** @see #__package */ @JsonProperty("package")  public String getPackage() { return this.__package; }
    /** @see #__package */ @JsonProperty("package")  public void setPackage(String __package) { this.__package = __package; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #physical_domains */ @JsonProperty("physical_domains")  public ReferenceList getPhysicalDomains() { return this.physical_domains; }
    /** @see #physical_domains */ @JsonProperty("physical_domains")  public void setPhysicalDomains(ReferenceList physical_domains) { this.physical_domains = physical_domains; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isPhysicalDataModel(Object obj) { return (obj.getClass() == PhysicalDataModel.class); }

}

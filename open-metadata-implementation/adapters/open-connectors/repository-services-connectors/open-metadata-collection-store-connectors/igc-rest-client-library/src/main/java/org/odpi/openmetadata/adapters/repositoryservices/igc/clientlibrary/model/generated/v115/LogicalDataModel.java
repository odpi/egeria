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
 * POJO for the 'logical_data_model' asset type in IGC, displayed as 'Logical Data Model' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class LogicalDataModel extends MainObject {

    public static final String IGC_TYPE_ID = "logical_data_model";

    /**
     * The 'logical_data_model' property, displayed as 'Logical Data Model' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link LogicalDataModel} object.
     */
    protected Reference logical_data_model;

    /**
     * The 'implemented_by_physical_data_models' property, displayed as 'Implemented by Physical Data Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link PhysicalDataModel} objects.
     */
    protected ReferenceList implemented_by_physical_data_models;

    /**
     * The 'implemented_by_database_schemas' property, displayed as 'Implemented by Database Schemas' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DatabaseSchema} objects.
     */
    protected ReferenceList implemented_by_database_schemas;

    /**
     * The 'contains_logical_data_models' property, displayed as 'Contains Logical Data Models' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalDataModel} objects.
     */
    protected ReferenceList contains_logical_data_models;

    /**
     * The 'subject_areas' property, displayed as 'Subject Areas' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link SubjectArea} objects.
     */
    protected ReferenceList subject_areas;

    /**
     * The 'logical_entities' property, displayed as 'Logical Entities' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalEntity} objects.
     */
    protected ReferenceList logical_entities;

    /**
     * The 'logical_domains' property, displayed as 'Logical Domains' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link LogicalDomain} objects.
     */
    protected ReferenceList logical_domains;

    /**
     * The 'alias_(business_name)' property, displayed as 'Alias (Business Name)' in the IGC UI.
     */
    @JsonProperty("alias_(business_name)") protected String alias__business_name_;

    /**
     * The 'author' property, displayed as 'Author' in the IGC UI.
     */
    protected String author;

    /**
     * The 'methodology' property, displayed as 'Methodology' in the IGC UI.
     */
    protected String methodology;

    /**
     * The 'version' property, displayed as 'Version' in the IGC UI.
     */
    protected String version;

    /**
     * The 'design_tool' property, displayed as 'Design Tool' in the IGC UI.
     */
    protected String design_tool;

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


    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public Reference getLogicalDataModel() { return this.logical_data_model; }
    /** @see #logical_data_model */ @JsonProperty("logical_data_model")  public void setLogicalDataModel(Reference logical_data_model) { this.logical_data_model = logical_data_model; }

    /** @see #implemented_by_physical_data_models */ @JsonProperty("implemented_by_physical_data_models")  public ReferenceList getImplementedByPhysicalDataModels() { return this.implemented_by_physical_data_models; }
    /** @see #implemented_by_physical_data_models */ @JsonProperty("implemented_by_physical_data_models")  public void setImplementedByPhysicalDataModels(ReferenceList implemented_by_physical_data_models) { this.implemented_by_physical_data_models = implemented_by_physical_data_models; }

    /** @see #implemented_by_database_schemas */ @JsonProperty("implemented_by_database_schemas")  public ReferenceList getImplementedByDatabaseSchemas() { return this.implemented_by_database_schemas; }
    /** @see #implemented_by_database_schemas */ @JsonProperty("implemented_by_database_schemas")  public void setImplementedByDatabaseSchemas(ReferenceList implemented_by_database_schemas) { this.implemented_by_database_schemas = implemented_by_database_schemas; }

    /** @see #contains_logical_data_models */ @JsonProperty("contains_logical_data_models")  public ReferenceList getContainsLogicalDataModels() { return this.contains_logical_data_models; }
    /** @see #contains_logical_data_models */ @JsonProperty("contains_logical_data_models")  public void setContainsLogicalDataModels(ReferenceList contains_logical_data_models) { this.contains_logical_data_models = contains_logical_data_models; }

    /** @see #subject_areas */ @JsonProperty("subject_areas")  public ReferenceList getSubjectAreas() { return this.subject_areas; }
    /** @see #subject_areas */ @JsonProperty("subject_areas")  public void setSubjectAreas(ReferenceList subject_areas) { this.subject_areas = subject_areas; }

    /** @see #logical_entities */ @JsonProperty("logical_entities")  public ReferenceList getLogicalEntities() { return this.logical_entities; }
    /** @see #logical_entities */ @JsonProperty("logical_entities")  public void setLogicalEntities(ReferenceList logical_entities) { this.logical_entities = logical_entities; }

    /** @see #logical_domains */ @JsonProperty("logical_domains")  public ReferenceList getLogicalDomains() { return this.logical_domains; }
    /** @see #logical_domains */ @JsonProperty("logical_domains")  public void setLogicalDomains(ReferenceList logical_domains) { this.logical_domains = logical_domains; }

    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public String getAliasBusinessName() { return this.alias__business_name_; }
    /** @see #alias__business_name_ */ @JsonProperty("alias_(business_name)")  public void setAliasBusinessName(String alias__business_name_) { this.alias__business_name_ = alias__business_name_; }

    /** @see #author */ @JsonProperty("author")  public String getAuthor() { return this.author; }
    /** @see #author */ @JsonProperty("author")  public void setAuthor(String author) { this.author = author; }

    /** @see #methodology */ @JsonProperty("methodology")  public String getMethodology() { return this.methodology; }
    /** @see #methodology */ @JsonProperty("methodology")  public void setMethodology(String methodology) { this.methodology = methodology; }

    /** @see #version */ @JsonProperty("version")  public String getVersion() { return this.version; }
    /** @see #version */ @JsonProperty("version")  public void setVersion(String version) { this.version = version; }

    /** @see #design_tool */ @JsonProperty("design_tool")  public String getDesignTool() { return this.design_tool; }
    /** @see #design_tool */ @JsonProperty("design_tool")  public void setDesignTool(String design_tool) { this.design_tool = design_tool; }

    /** @see #native_id */ @JsonProperty("native_id")  public String getNativeId() { return this.native_id; }
    /** @see #native_id */ @JsonProperty("native_id")  public void setNativeId(String native_id) { this.native_id = native_id; }

    /** @see #__package */ @JsonProperty("package")  public String getPackage() { return this.__package; }
    /** @see #__package */ @JsonProperty("package")  public void setPackage(String __package) { this.__package = __package; }

    /** @see #namespace */ @JsonProperty("namespace")  public String getNamespace() { return this.namespace; }
    /** @see #namespace */ @JsonProperty("namespace")  public void setNamespace(String namespace) { this.namespace = namespace; }

    /** @see #imported_from */ @JsonProperty("imported_from")  public String getImportedFrom() { return this.imported_from; }
    /** @see #imported_from */ @JsonProperty("imported_from")  public void setImportedFrom(String imported_from) { this.imported_from = imported_from; }

    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public ReferenceList getBlueprintElements() { return this.blueprint_elements; }
    /** @see #blueprint_elements */ @JsonProperty("blueprint_elements")  public void setBlueprintElements(ReferenceList blueprint_elements) { this.blueprint_elements = blueprint_elements; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isLogicalDataModel(Object obj) { return (obj.getClass() == LogicalDataModel.class); }

}

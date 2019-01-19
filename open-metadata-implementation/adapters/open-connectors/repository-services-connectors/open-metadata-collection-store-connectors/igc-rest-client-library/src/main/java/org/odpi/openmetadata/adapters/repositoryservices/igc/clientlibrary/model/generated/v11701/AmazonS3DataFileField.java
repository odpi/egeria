/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.generated.v11701;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.odpi.openmetadata.adapters.repositoryservices.igc.clientlibrary.model.common.*;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * POJO for the 'amazon_s3_data_file_field' asset type in IGC, displayed as 'Amazon S3 Data File Field' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class AmazonS3DataFileField extends Reference {

    public static String getIgcTypeId() { return "amazon_s3_data_file_field"; }
    public static String getIgcTypeDisplayName() { return "Amazon S3 Data File Field"; }

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
     * The 'data_file_record' property, displayed as 'Data File Record' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataFileRecord} object.
     */
    protected Reference data_file_record;

    /**
     * The 'qualityScore' property, displayed as 'Quality Score' in the IGC UI.
     */
    protected String qualityScore;

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
     * The 'implements_entity_attributes' property, displayed as 'Implements Entity Attributes' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link EntityAttribute} objects.
     */
    protected ReferenceList implements_entity_attributes;

    /**
     * The 'implements_design_columns' property, displayed as 'Implements Design Columns' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DesignColumn} objects.
     */
    protected ReferenceList implements_design_columns;

    /**
     * The 'type' property, displayed as 'Native Type' in the IGC UI.
     */
    protected String type;

    /**
     * The 'data_type' property, displayed as 'Data Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INT8 (displayed in the UI as 'INT8')</li>
     *     <li>INT16 (displayed in the UI as 'INT16')</li>
     *     <li>INT32 (displayed in the UI as 'INT32')</li>
     *     <li>INT64 (displayed in the UI as 'INT64')</li>
     *     <li>SFLOAT (displayed in the UI as 'SFLOAT')</li>
     *     <li>DFLOAT (displayed in the UI as 'DFLOAT')</li>
     *     <li>QFLOAT (displayed in the UI as 'QFLOAT')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>STRING (displayed in the UI as 'STRING')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>BOOLEAN (displayed in the UI as 'BOOLEAN')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>DATETIME (displayed in the UI as 'DATETIME')</li>
     *     <li>DURATION (displayed in the UI as 'DURATION')</li>
     *     <li>CHOICE (displayed in the UI as 'CHOICE')</li>
     *     <li>ORDERED_GROUP (displayed in the UI as 'ORDERED_GROUP')</li>
     *     <li>UNORDERED_GROUP (displayed in the UI as 'UNORDERED_GROUP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     *     <li>JSON (displayed in the UI as 'JSON')</li>
     *     <li>XML (displayed in the UI as 'XML')</li>
     * </ul>
     */
    protected String data_type;

    /**
     * The 'selected_classification' property, displayed as 'Selected Data Classification' in the IGC UI.
     * <br><br>
     * Will be a single {@link Reference} to a {@link DataClass} object.
     */
    protected Reference selected_classification;

    /**
     * The 'detected_classifications' property, displayed as 'Detected Data Classifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classification} objects.
     */
    protected ReferenceList detected_classifications;

    /**
     * The 'odbc_type' property, displayed as 'ODBC Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>CHAR (displayed in the UI as 'CHAR')</li>
     *     <li>VARCHAR (displayed in the UI as 'VARCHAR')</li>
     *     <li>LONGVARCHAR (displayed in the UI as 'LONGVARCHAR')</li>
     *     <li>WCHAR (displayed in the UI as 'WCHAR')</li>
     *     <li>WVARCHAR (displayed in the UI as 'WVARCHAR')</li>
     *     <li>WLONGVARCHAR (displayed in the UI as 'WLONGVARCHAR')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>NUMERIC (displayed in the UI as 'NUMERIC')</li>
     *     <li>SMALLINT (displayed in the UI as 'SMALLINT')</li>
     *     <li>INTEGER (displayed in the UI as 'INTEGER')</li>
     *     <li>REAL (displayed in the UI as 'REAL')</li>
     *     <li>FLOAT (displayed in the UI as 'FLOAT')</li>
     *     <li>DOUBLE (displayed in the UI as 'DOUBLE')</li>
     *     <li>BIT (displayed in the UI as 'BIT')</li>
     *     <li>TINYINT (displayed in the UI as 'TINYINT')</li>
     *     <li>BIGINT (displayed in the UI as 'BIGINT')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>VARBINARY (displayed in the UI as 'VARBINARY')</li>
     *     <li>LONGVARBINARY (displayed in the UI as 'LONGVARBINARY')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>TIMESTAMP (displayed in the UI as 'TIMESTAMP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     * </ul>
     */
    protected String odbc_type;

    /**
     * The 'length' property, displayed as 'Length' in the IGC UI.
     */
    protected Number length;

    /**
     * The 'minimum_length' property, displayed as 'Minimum Length' in the IGC UI.
     */
    protected Number minimum_length;

    /**
     * The 'fraction' property, displayed as 'Scale' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The 'position' property, displayed as 'Position' in the IGC UI.
     */
    protected Number position;

    /**
     * The 'level' property, displayed as 'Level Number' in the IGC UI.
     */
    protected Number level;

    /**
     * The 'allows_null_values' property, displayed as 'Allow Null Values' in the IGC UI.
     */
    protected Boolean allows_null_values;

    /**
     * The 'unique' property, displayed as 'Unique Constraint' in the IGC UI.
     */
    protected Boolean unique;

    /**
     * The 'uniqueFlag' property, displayed as 'Require Unique Values' in the IGC UI.
     */
    protected Boolean uniqueFlag;

    /**
     * The 'nullabilityFlag' property, displayed as 'Include Null Values' in the IGC UI.
     */
    protected Boolean nullabilityFlag;

    /**
     * The 'constantFlag' property, displayed as 'Include Constant Values' in the IGC UI.
     */
    protected Boolean constantFlag;

    /**
     * The 'domainType' property, displayed as 'Domain' in the IGC UI.
     */
    protected ArrayList<String> domainType;

    /**
     * The 'numberCompleteValues' property, displayed as 'Number of Complete Values' in the IGC UI.
     */
    protected ArrayList<Number> numberCompleteValues;

    /**
     * The 'numberValidValues' property, displayed as 'Number of Valid Values' in the IGC UI.
     */
    protected ArrayList<Number> numberValidValues;

    /**
     * The 'numberEmptyValues' property, displayed as 'Number of Empty Values' in the IGC UI.
     */
    protected ArrayList<Number> numberEmptyValues;

    /**
     * The 'numberNullValues' property, displayed as 'Number of Null Values' in the IGC UI.
     */
    protected ArrayList<Number> numberNullValues;

    /**
     * The 'numberDistinctValues' property, displayed as 'Number of Distinct Values' in the IGC UI.
     */
    protected ArrayList<Number> numberDistinctValues;

    /**
     * The 'numberFormats' property, displayed as 'Number of Distinct Formats' in the IGC UI.
     */
    protected ArrayList<Number> numberFormats;

    /**
     * The 'numberZeroValues' property, displayed as 'Number of Zero Values' in the IGC UI.
     */
    protected ArrayList<Number> numberZeroValues;

    /**
     * The 'inferredDataType' property, displayed as 'Inferred Data Type' in the IGC UI.
     * <br><br>
     * Can be one of the following values:
     * <ul>
     *     <li>INT8 (displayed in the UI as 'INT8')</li>
     *     <li>INT16 (displayed in the UI as 'INT16')</li>
     *     <li>INT32 (displayed in the UI as 'INT32')</li>
     *     <li>INT64 (displayed in the UI as 'INT64')</li>
     *     <li>SFLOAT (displayed in the UI as 'SFLOAT')</li>
     *     <li>DFLOAT (displayed in the UI as 'DFLOAT')</li>
     *     <li>QFLOAT (displayed in the UI as 'QFLOAT')</li>
     *     <li>DECIMAL (displayed in the UI as 'DECIMAL')</li>
     *     <li>STRING (displayed in the UI as 'STRING')</li>
     *     <li>BINARY (displayed in the UI as 'BINARY')</li>
     *     <li>BOOLEAN (displayed in the UI as 'BOOLEAN')</li>
     *     <li>DATE (displayed in the UI as 'DATE')</li>
     *     <li>TIME (displayed in the UI as 'TIME')</li>
     *     <li>DATETIME (displayed in the UI as 'DATETIME')</li>
     *     <li>DURATION (displayed in the UI as 'DURATION')</li>
     *     <li>CHOICE (displayed in the UI as 'CHOICE')</li>
     *     <li>ORDERED_GROUP (displayed in the UI as 'ORDERED_GROUP')</li>
     *     <li>UNORDERED_GROUP (displayed in the UI as 'UNORDERED_GROUP')</li>
     *     <li>GUID (displayed in the UI as 'GUID')</li>
     *     <li>UNKNOWN (displayed in the UI as 'UNKNOWN')</li>
     *     <li>JSON (displayed in the UI as 'JSON')</li>
     *     <li>XML (displayed in the UI as 'XML')</li>
     * </ul>
     */
    protected ArrayList<String> inferredDataType;

    /**
     * The 'inferredLength' property, displayed as 'Inferred Length' in the IGC UI.
     */
    protected ArrayList<Number> inferredLength;

    /**
     * The 'inferredFormat' property, displayed as 'Inferred Format' in the IGC UI.
     */
    protected ArrayList<String> inferredFormat;

    /**
     * The 'inferredScale' property, displayed as 'Inferred Scale' in the IGC UI.
     */
    protected ArrayList<Number> inferredScale;

    /**
     * The 'inferredPrecision' property, displayed as 'Inferred Precision' in the IGC UI.
     */
    protected ArrayList<Number> inferredPrecision;

    /**
     * The 'averageValue' property, displayed as 'Average Value' in the IGC UI.
     */
    protected ArrayList<String> averageValue;

    /**
     * The 'isInferredForeignKey' property, displayed as 'Inferred Foreign Key' in the IGC UI.
     */
    protected Boolean isInferredForeignKey;

    /**
     * The 'isInferredPrimaryKey' property, displayed as 'Inferred Primary Key' in the IGC UI.
     */
    protected Boolean isInferredPrimaryKey;

    /**
     * The 'hasDataClassification' property, displayed as 'Detected Data Classifications' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Classification} objects.
     */
    protected ReferenceList hasDataClassification;

    /**
     * The 'nbRecordsTested' property, displayed as 'Number of Records Tested' in the IGC UI.
     */
    protected ArrayList<Number> nbRecordsTested;

    /**
     * The 'read_by_(static)' property, displayed as 'Read by (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(static)") protected ReferenceList read_by__static_;

    /**
     * The 'written_by_(static)' property, displayed as 'Written by (Static)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(static)") protected ReferenceList written_by__static_;

    /**
     * The 'read_by_(design)' property, displayed as 'Read by (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(design)") protected ReferenceList read_by__design_;

    /**
     * The 'written_by_(design)' property, displayed as 'Written by (Design)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(design)") protected ReferenceList written_by__design_;

    /**
     * The 'read_by_(operational)' property, displayed as 'Read by (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(operational)") protected ReferenceList read_by__operational_;

    /**
     * The 'written_by_(operational)' property, displayed as 'Written by (Operational)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(operational)") protected ReferenceList written_by__operational_;

    /**
     * The 'read_by_(user_defined)' property, displayed as 'Read by (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("read_by_(user_defined)") protected ReferenceList read_by__user_defined_;

    /**
     * The 'written_by_(user_defined)' property, displayed as 'Written by (User-Defined)' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link InformationAsset} objects.
     */
    @JsonProperty("written_by_(user_defined)") protected ReferenceList written_by__user_defined_;

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
     * The 'used_by_analytics_objects' property, displayed as 'Used By Data Science' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link AnalyticsObject} objects.
     */
    protected ReferenceList used_by_analytics_objects;

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

    /** @see #data_file_record */ @JsonProperty("data_file_record")  public Reference getDataFileRecord() { return this.data_file_record; }
    /** @see #data_file_record */ @JsonProperty("data_file_record")  public void setDataFileRecord(Reference data_file_record) { this.data_file_record = data_file_record; }

    /** @see #qualityScore */ @JsonProperty("qualityScore")  public String getQualityscore() { return this.qualityScore; }
    /** @see #qualityScore */ @JsonProperty("qualityScore")  public void setQualityscore(String qualityScore) { this.qualityScore = qualityScore; }

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

    /** @see #implements_entity_attributes */ @JsonProperty("implements_entity_attributes")  public ReferenceList getImplementsEntityAttributes() { return this.implements_entity_attributes; }
    /** @see #implements_entity_attributes */ @JsonProperty("implements_entity_attributes")  public void setImplementsEntityAttributes(ReferenceList implements_entity_attributes) { this.implements_entity_attributes = implements_entity_attributes; }

    /** @see #implements_design_columns */ @JsonProperty("implements_design_columns")  public ReferenceList getImplementsDesignColumns() { return this.implements_design_columns; }
    /** @see #implements_design_columns */ @JsonProperty("implements_design_columns")  public void setImplementsDesignColumns(ReferenceList implements_design_columns) { this.implements_design_columns = implements_design_columns; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #selected_classification */ @JsonProperty("selected_classification")  public Reference getSelectedClassification() { return this.selected_classification; }
    /** @see #selected_classification */ @JsonProperty("selected_classification")  public void setSelectedClassification(Reference selected_classification) { this.selected_classification = selected_classification; }

    /** @see #detected_classifications */ @JsonProperty("detected_classifications")  public ReferenceList getDetectedClassifications() { return this.detected_classifications; }
    /** @see #detected_classifications */ @JsonProperty("detected_classifications")  public void setDetectedClassifications(ReferenceList detected_classifications) { this.detected_classifications = detected_classifications; }

    /** @see #odbc_type */ @JsonProperty("odbc_type")  public String getOdbcType() { return this.odbc_type; }
    /** @see #odbc_type */ @JsonProperty("odbc_type")  public void setOdbcType(String odbc_type) { this.odbc_type = odbc_type; }

    /** @see #length */ @JsonProperty("length")  public Number getLength() { return this.length; }
    /** @see #length */ @JsonProperty("length")  public void setLength(Number length) { this.length = length; }

    /** @see #minimum_length */ @JsonProperty("minimum_length")  public Number getMinimumLength() { return this.minimum_length; }
    /** @see #minimum_length */ @JsonProperty("minimum_length")  public void setMinimumLength(Number minimum_length) { this.minimum_length = minimum_length; }

    /** @see #fraction */ @JsonProperty("fraction")  public Number getFraction() { return this.fraction; }
    /** @see #fraction */ @JsonProperty("fraction")  public void setFraction(Number fraction) { this.fraction = fraction; }

    /** @see #position */ @JsonProperty("position")  public Number getPosition() { return this.position; }
    /** @see #position */ @JsonProperty("position")  public void setPosition(Number position) { this.position = position; }

    /** @see #level */ @JsonProperty("level")  public Number getLevel() { return this.level; }
    /** @see #level */ @JsonProperty("level")  public void setLevel(Number level) { this.level = level; }

    /** @see #allows_null_values */ @JsonProperty("allows_null_values")  public Boolean getAllowsNullValues() { return this.allows_null_values; }
    /** @see #allows_null_values */ @JsonProperty("allows_null_values")  public void setAllowsNullValues(Boolean allows_null_values) { this.allows_null_values = allows_null_values; }

    /** @see #unique */ @JsonProperty("unique")  public Boolean getUnique() { return this.unique; }
    /** @see #unique */ @JsonProperty("unique")  public void setUnique(Boolean unique) { this.unique = unique; }

    /** @see #uniqueFlag */ @JsonProperty("uniqueFlag")  public Boolean getUniqueflag() { return this.uniqueFlag; }
    /** @see #uniqueFlag */ @JsonProperty("uniqueFlag")  public void setUniqueflag(Boolean uniqueFlag) { this.uniqueFlag = uniqueFlag; }

    /** @see #nullabilityFlag */ @JsonProperty("nullabilityFlag")  public Boolean getNullabilityflag() { return this.nullabilityFlag; }
    /** @see #nullabilityFlag */ @JsonProperty("nullabilityFlag")  public void setNullabilityflag(Boolean nullabilityFlag) { this.nullabilityFlag = nullabilityFlag; }

    /** @see #constantFlag */ @JsonProperty("constantFlag")  public Boolean getConstantflag() { return this.constantFlag; }
    /** @see #constantFlag */ @JsonProperty("constantFlag")  public void setConstantflag(Boolean constantFlag) { this.constantFlag = constantFlag; }

    /** @see #domainType */ @JsonProperty("domainType")  public ArrayList<String> getDomaintype() { return this.domainType; }
    /** @see #domainType */ @JsonProperty("domainType")  public void setDomaintype(ArrayList<String> domainType) { this.domainType = domainType; }

    /** @see #numberCompleteValues */ @JsonProperty("numberCompleteValues")  public ArrayList<Number> getNumbercompletevalues() { return this.numberCompleteValues; }
    /** @see #numberCompleteValues */ @JsonProperty("numberCompleteValues")  public void setNumbercompletevalues(ArrayList<Number> numberCompleteValues) { this.numberCompleteValues = numberCompleteValues; }

    /** @see #numberValidValues */ @JsonProperty("numberValidValues")  public ArrayList<Number> getNumbervalidvalues() { return this.numberValidValues; }
    /** @see #numberValidValues */ @JsonProperty("numberValidValues")  public void setNumbervalidvalues(ArrayList<Number> numberValidValues) { this.numberValidValues = numberValidValues; }

    /** @see #numberEmptyValues */ @JsonProperty("numberEmptyValues")  public ArrayList<Number> getNumberemptyvalues() { return this.numberEmptyValues; }
    /** @see #numberEmptyValues */ @JsonProperty("numberEmptyValues")  public void setNumberemptyvalues(ArrayList<Number> numberEmptyValues) { this.numberEmptyValues = numberEmptyValues; }

    /** @see #numberNullValues */ @JsonProperty("numberNullValues")  public ArrayList<Number> getNumbernullvalues() { return this.numberNullValues; }
    /** @see #numberNullValues */ @JsonProperty("numberNullValues")  public void setNumbernullvalues(ArrayList<Number> numberNullValues) { this.numberNullValues = numberNullValues; }

    /** @see #numberDistinctValues */ @JsonProperty("numberDistinctValues")  public ArrayList<Number> getNumberdistinctvalues() { return this.numberDistinctValues; }
    /** @see #numberDistinctValues */ @JsonProperty("numberDistinctValues")  public void setNumberdistinctvalues(ArrayList<Number> numberDistinctValues) { this.numberDistinctValues = numberDistinctValues; }

    /** @see #numberFormats */ @JsonProperty("numberFormats")  public ArrayList<Number> getNumberformats() { return this.numberFormats; }
    /** @see #numberFormats */ @JsonProperty("numberFormats")  public void setNumberformats(ArrayList<Number> numberFormats) { this.numberFormats = numberFormats; }

    /** @see #numberZeroValues */ @JsonProperty("numberZeroValues")  public ArrayList<Number> getNumberzerovalues() { return this.numberZeroValues; }
    /** @see #numberZeroValues */ @JsonProperty("numberZeroValues")  public void setNumberzerovalues(ArrayList<Number> numberZeroValues) { this.numberZeroValues = numberZeroValues; }

    /** @see #inferredDataType */ @JsonProperty("inferredDataType")  public ArrayList<String> getInferreddatatype() { return this.inferredDataType; }
    /** @see #inferredDataType */ @JsonProperty("inferredDataType")  public void setInferreddatatype(ArrayList<String> inferredDataType) { this.inferredDataType = inferredDataType; }

    /** @see #inferredLength */ @JsonProperty("inferredLength")  public ArrayList<Number> getInferredlength() { return this.inferredLength; }
    /** @see #inferredLength */ @JsonProperty("inferredLength")  public void setInferredlength(ArrayList<Number> inferredLength) { this.inferredLength = inferredLength; }

    /** @see #inferredFormat */ @JsonProperty("inferredFormat")  public ArrayList<String> getInferredformat() { return this.inferredFormat; }
    /** @see #inferredFormat */ @JsonProperty("inferredFormat")  public void setInferredformat(ArrayList<String> inferredFormat) { this.inferredFormat = inferredFormat; }

    /** @see #inferredScale */ @JsonProperty("inferredScale")  public ArrayList<Number> getInferredscale() { return this.inferredScale; }
    /** @see #inferredScale */ @JsonProperty("inferredScale")  public void setInferredscale(ArrayList<Number> inferredScale) { this.inferredScale = inferredScale; }

    /** @see #inferredPrecision */ @JsonProperty("inferredPrecision")  public ArrayList<Number> getInferredprecision() { return this.inferredPrecision; }
    /** @see #inferredPrecision */ @JsonProperty("inferredPrecision")  public void setInferredprecision(ArrayList<Number> inferredPrecision) { this.inferredPrecision = inferredPrecision; }

    /** @see #averageValue */ @JsonProperty("averageValue")  public ArrayList<String> getAveragevalue() { return this.averageValue; }
    /** @see #averageValue */ @JsonProperty("averageValue")  public void setAveragevalue(ArrayList<String> averageValue) { this.averageValue = averageValue; }

    /** @see #isInferredForeignKey */ @JsonProperty("isInferredForeignKey")  public Boolean getIsinferredforeignkey() { return this.isInferredForeignKey; }
    /** @see #isInferredForeignKey */ @JsonProperty("isInferredForeignKey")  public void setIsinferredforeignkey(Boolean isInferredForeignKey) { this.isInferredForeignKey = isInferredForeignKey; }

    /** @see #isInferredPrimaryKey */ @JsonProperty("isInferredPrimaryKey")  public Boolean getIsinferredprimarykey() { return this.isInferredPrimaryKey; }
    /** @see #isInferredPrimaryKey */ @JsonProperty("isInferredPrimaryKey")  public void setIsinferredprimarykey(Boolean isInferredPrimaryKey) { this.isInferredPrimaryKey = isInferredPrimaryKey; }

    /** @see #hasDataClassification */ @JsonProperty("hasDataClassification")  public ReferenceList getHasdataclassification() { return this.hasDataClassification; }
    /** @see #hasDataClassification */ @JsonProperty("hasDataClassification")  public void setHasdataclassification(ReferenceList hasDataClassification) { this.hasDataClassification = hasDataClassification; }

    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public ArrayList<Number> getNbrecordstested() { return this.nbRecordsTested; }
    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public void setNbrecordstested(ArrayList<Number> nbRecordsTested) { this.nbRecordsTested = nbRecordsTested; }

    /** @see #read_by__static_ */ @JsonProperty("read_by_(static)")  public ReferenceList getReadByStatic() { return this.read_by__static_; }
    /** @see #read_by__static_ */ @JsonProperty("read_by_(static)")  public void setReadByStatic(ReferenceList read_by__static_) { this.read_by__static_ = read_by__static_; }

    /** @see #written_by__static_ */ @JsonProperty("written_by_(static)")  public ReferenceList getWrittenByStatic() { return this.written_by__static_; }
    /** @see #written_by__static_ */ @JsonProperty("written_by_(static)")  public void setWrittenByStatic(ReferenceList written_by__static_) { this.written_by__static_ = written_by__static_; }

    /** @see #read_by__design_ */ @JsonProperty("read_by_(design)")  public ReferenceList getReadByDesign() { return this.read_by__design_; }
    /** @see #read_by__design_ */ @JsonProperty("read_by_(design)")  public void setReadByDesign(ReferenceList read_by__design_) { this.read_by__design_ = read_by__design_; }

    /** @see #written_by__design_ */ @JsonProperty("written_by_(design)")  public ReferenceList getWrittenByDesign() { return this.written_by__design_; }
    /** @see #written_by__design_ */ @JsonProperty("written_by_(design)")  public void setWrittenByDesign(ReferenceList written_by__design_) { this.written_by__design_ = written_by__design_; }

    /** @see #read_by__operational_ */ @JsonProperty("read_by_(operational)")  public ReferenceList getReadByOperational() { return this.read_by__operational_; }
    /** @see #read_by__operational_ */ @JsonProperty("read_by_(operational)")  public void setReadByOperational(ReferenceList read_by__operational_) { this.read_by__operational_ = read_by__operational_; }

    /** @see #written_by__operational_ */ @JsonProperty("written_by_(operational)")  public ReferenceList getWrittenByOperational() { return this.written_by__operational_; }
    /** @see #written_by__operational_ */ @JsonProperty("written_by_(operational)")  public void setWrittenByOperational(ReferenceList written_by__operational_) { this.written_by__operational_ = written_by__operational_; }

    /** @see #read_by__user_defined_ */ @JsonProperty("read_by_(user_defined)")  public ReferenceList getReadByUserDefined() { return this.read_by__user_defined_; }
    /** @see #read_by__user_defined_ */ @JsonProperty("read_by_(user_defined)")  public void setReadByUserDefined(ReferenceList read_by__user_defined_) { this.read_by__user_defined_ = read_by__user_defined_; }

    /** @see #written_by__user_defined_ */ @JsonProperty("written_by_(user_defined)")  public ReferenceList getWrittenByUserDefined() { return this.written_by__user_defined_; }
    /** @see #written_by__user_defined_ */ @JsonProperty("written_by_(user_defined)")  public void setWrittenByUserDefined(ReferenceList written_by__user_defined_) { this.written_by__user_defined_ = written_by__user_defined_; }

    /** @see #impacted_by */ @JsonProperty("impacted_by")  public ReferenceList getImpactedBy() { return this.impacted_by; }
    /** @see #impacted_by */ @JsonProperty("impacted_by")  public void setImpactedBy(ReferenceList impacted_by) { this.impacted_by = impacted_by; }

    /** @see #impacts_on */ @JsonProperty("impacts_on")  public ReferenceList getImpactsOn() { return this.impacts_on; }
    /** @see #impacts_on */ @JsonProperty("impacts_on")  public void setImpactsOn(ReferenceList impacts_on) { this.impacts_on = impacts_on; }

    /** @see #used_by_analytics_objects */ @JsonProperty("used_by_analytics_objects")  public ReferenceList getUsedByAnalyticsObjects() { return this.used_by_analytics_objects; }
    /** @see #used_by_analytics_objects */ @JsonProperty("used_by_analytics_objects")  public void setUsedByAnalyticsObjects(ReferenceList used_by_analytics_objects) { this.used_by_analytics_objects = used_by_analytics_objects; }

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
        "qualityScore",
        "type",
        "data_type",
        "odbc_type",
        "length",
        "minimum_length",
        "fraction",
        "position",
        "level",
        "allows_null_values",
        "unique",
        "uniqueFlag",
        "nullabilityFlag",
        "constantFlag",
        "domainType",
        "numberCompleteValues",
        "numberValidValues",
        "numberEmptyValues",
        "numberNullValues",
        "numberDistinctValues",
        "numberFormats",
        "numberZeroValues",
        "inferredDataType",
        "inferredLength",
        "inferredFormat",
        "inferredScale",
        "inferredPrecision",
        "averageValue",
        "isInferredForeignKey",
        "isInferredPrimaryKey",
        "nbRecordsTested",
        "created_by",
        "created_on",
        "modified_by",
        "modified_on"
    );
    private static final List<String> STRING_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "qualityScore",
        "type",
        "domainType",
        "inferredFormat",
        "averageValue",
        "created_by",
        "modified_by"
    );
    private static final List<String> PAGED_RELATIONAL_PROPERTIES = Arrays.asList(
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "implements_entity_attributes",
        "implements_design_columns",
        "detected_classifications",
        "hasDataClassification",
        "read_by_(static)",
        "written_by_(static)",
        "read_by_(design)",
        "written_by_(design)",
        "read_by_(operational)",
        "written_by_(operational)",
        "read_by_(user_defined)",
        "written_by_(user_defined)",
        "impacted_by",
        "impacts_on",
        "used_by_analytics_objects",
        "in_collections"
    );
    private static final List<String> ALL_PROPERTIES = Arrays.asList(
        "name",
        "short_description",
        "long_description",
        "data_file_record",
        "qualityScore",
        "labels",
        "stewards",
        "assigned_to_terms",
        "implements_rules",
        "governed_by_rules",
        "implements_entity_attributes",
        "implements_design_columns",
        "type",
        "data_type",
        "selected_classification",
        "detected_classifications",
        "odbc_type",
        "length",
        "minimum_length",
        "fraction",
        "position",
        "level",
        "allows_null_values",
        "unique",
        "uniqueFlag",
        "nullabilityFlag",
        "constantFlag",
        "domainType",
        "numberCompleteValues",
        "numberValidValues",
        "numberEmptyValues",
        "numberNullValues",
        "numberDistinctValues",
        "numberFormats",
        "numberZeroValues",
        "inferredDataType",
        "inferredLength",
        "inferredFormat",
        "inferredScale",
        "inferredPrecision",
        "averageValue",
        "isInferredForeignKey",
        "isInferredPrimaryKey",
        "hasDataClassification",
        "nbRecordsTested",
        "read_by_(static)",
        "written_by_(static)",
        "read_by_(design)",
        "written_by_(design)",
        "read_by_(operational)",
        "written_by_(operational)",
        "read_by_(user_defined)",
        "written_by_(user_defined)",
        "impacted_by",
        "impacts_on",
        "used_by_analytics_objects",
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
    public static Boolean isAmazonS3DataFileField(Object obj) { return (obj.getClass() == AmazonS3DataFileField.class); }

}

/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.adapters.repositoryservices.igc.model.generated.v115;

import org.odpi.openmetadata.adapters.repositoryservices.igc.model.common.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import java.util.ArrayList;

/**
 * POJO for the 'data_file_field' asset type in IGC, displayed as 'Data File Field' in the IGC UI.
 * <br><br>
 * (this code has been generated based on out-of-the-box IGC metadata types;
 *  if modifications are needed, eg. to handle custom attributes,
 *  extending from this class in your own custom class is the best approach.)
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataFileField extends MainObject {

    public static final String IGC_TYPE_ID = "data_file_field";

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
     * The 'default_value' property, displayed as 'Default Value' in the IGC UI.
     */
    protected String default_value;

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
     * The 'fraction' property, displayed as 'Fraction' in the IGC UI.
     */
    protected Number fraction;

    /**
     * The 'position' property, displayed as 'Position' in the IGC UI.
     */
    protected Number position;

    /**
     * The 'level' property, displayed as 'Level' in the IGC UI.
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
     * The 'same_as_data_sources' property, displayed as 'Same as Data Sources' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataItem} objects.
     */
    protected ReferenceList same_as_data_sources;

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
     * The 'suggested_term_assignments' property, displayed as 'Suggested Term Assignments' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link TermAssignment} objects.
     */
    protected ReferenceList suggested_term_assignments;

    /**
     * The 'datafile_data_rules' property, displayed as 'Data Rules' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataRule} objects.
     */
    protected ReferenceList datafile_data_rules;

    /**
     * The 'database_data_rule_sets' property, displayed as 'Data Rule Sets' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link DataRuleSet} objects.
     */
    protected ReferenceList database_data_rule_sets;

    /**
     * The 'in_collections' property, displayed as 'In Collections' in the IGC UI.
     * <br><br>
     * Will be a {@link ReferenceList} of {@link Collection} objects.
     */
    protected ReferenceList in_collections;


    /** @see #data_file_record */ @JsonProperty("data_file_record")  public Reference getDataFileRecord() { return this.data_file_record; }
    /** @see #data_file_record */ @JsonProperty("data_file_record")  public void setDataFileRecord(Reference data_file_record) { this.data_file_record = data_file_record; }

    /** @see #qualityScore */ @JsonProperty("qualityScore")  public String getQualityScore() { return this.qualityScore; }
    /** @see #qualityScore */ @JsonProperty("qualityScore")  public void setQualityScore(String qualityScore) { this.qualityScore = qualityScore; }

    /** @see #implements_entity_attributes */ @JsonProperty("implements_entity_attributes")  public ReferenceList getImplementsEntityAttributes() { return this.implements_entity_attributes; }
    /** @see #implements_entity_attributes */ @JsonProperty("implements_entity_attributes")  public void setImplementsEntityAttributes(ReferenceList implements_entity_attributes) { this.implements_entity_attributes = implements_entity_attributes; }

    /** @see #implements_design_columns */ @JsonProperty("implements_design_columns")  public ReferenceList getImplementsDesignColumns() { return this.implements_design_columns; }
    /** @see #implements_design_columns */ @JsonProperty("implements_design_columns")  public void setImplementsDesignColumns(ReferenceList implements_design_columns) { this.implements_design_columns = implements_design_columns; }

    /** @see #type */ @JsonProperty("type")  public String getTheType() { return this.type; }
    /** @see #type */ @JsonProperty("type")  public void setTheType(String type) { this.type = type; }

    /** @see #data_type */ @JsonProperty("data_type")  public String getDataType() { return this.data_type; }
    /** @see #data_type */ @JsonProperty("data_type")  public void setDataType(String data_type) { this.data_type = data_type; }

    /** @see #default_value */ @JsonProperty("default_value")  public String getDefaultValue() { return this.default_value; }
    /** @see #default_value */ @JsonProperty("default_value")  public void setDefaultValue(String default_value) { this.default_value = default_value; }

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

    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public ReferenceList getSameAsDataSources() { return this.same_as_data_sources; }
    /** @see #same_as_data_sources */ @JsonProperty("same_as_data_sources")  public void setSameAsDataSources(ReferenceList same_as_data_sources) { this.same_as_data_sources = same_as_data_sources; }

    /** @see #uniqueFlag */ @JsonProperty("uniqueFlag")  public Boolean getUniqueFlag() { return this.uniqueFlag; }
    /** @see #uniqueFlag */ @JsonProperty("uniqueFlag")  public void setUniqueFlag(Boolean uniqueFlag) { this.uniqueFlag = uniqueFlag; }

    /** @see #nullabilityFlag */ @JsonProperty("nullabilityFlag")  public Boolean getNullabilityFlag() { return this.nullabilityFlag; }
    /** @see #nullabilityFlag */ @JsonProperty("nullabilityFlag")  public void setNullabilityFlag(Boolean nullabilityFlag) { this.nullabilityFlag = nullabilityFlag; }

    /** @see #constantFlag */ @JsonProperty("constantFlag")  public Boolean getConstantFlag() { return this.constantFlag; }
    /** @see #constantFlag */ @JsonProperty("constantFlag")  public void setConstantFlag(Boolean constantFlag) { this.constantFlag = constantFlag; }

    /** @see #domainType */ @JsonProperty("domainType")  public ArrayList<String> getDomainType() { return this.domainType; }
    /** @see #domainType */ @JsonProperty("domainType")  public void setDomainType(ArrayList<String> domainType) { this.domainType = domainType; }

    /** @see #numberCompleteValues */ @JsonProperty("numberCompleteValues")  public ArrayList<Number> getNumberCompleteValues() { return this.numberCompleteValues; }
    /** @see #numberCompleteValues */ @JsonProperty("numberCompleteValues")  public void setNumberCompleteValues(ArrayList<Number> numberCompleteValues) { this.numberCompleteValues = numberCompleteValues; }

    /** @see #numberValidValues */ @JsonProperty("numberValidValues")  public ArrayList<Number> getNumberValidValues() { return this.numberValidValues; }
    /** @see #numberValidValues */ @JsonProperty("numberValidValues")  public void setNumberValidValues(ArrayList<Number> numberValidValues) { this.numberValidValues = numberValidValues; }

    /** @see #numberEmptyValues */ @JsonProperty("numberEmptyValues")  public ArrayList<Number> getNumberEmptyValues() { return this.numberEmptyValues; }
    /** @see #numberEmptyValues */ @JsonProperty("numberEmptyValues")  public void setNumberEmptyValues(ArrayList<Number> numberEmptyValues) { this.numberEmptyValues = numberEmptyValues; }

    /** @see #numberNullValues */ @JsonProperty("numberNullValues")  public ArrayList<Number> getNumberNullValues() { return this.numberNullValues; }
    /** @see #numberNullValues */ @JsonProperty("numberNullValues")  public void setNumberNullValues(ArrayList<Number> numberNullValues) { this.numberNullValues = numberNullValues; }

    /** @see #numberDistinctValues */ @JsonProperty("numberDistinctValues")  public ArrayList<Number> getNumberDistinctValues() { return this.numberDistinctValues; }
    /** @see #numberDistinctValues */ @JsonProperty("numberDistinctValues")  public void setNumberDistinctValues(ArrayList<Number> numberDistinctValues) { this.numberDistinctValues = numberDistinctValues; }

    /** @see #numberFormats */ @JsonProperty("numberFormats")  public ArrayList<Number> getNumberFormats() { return this.numberFormats; }
    /** @see #numberFormats */ @JsonProperty("numberFormats")  public void setNumberFormats(ArrayList<Number> numberFormats) { this.numberFormats = numberFormats; }

    /** @see #numberZeroValues */ @JsonProperty("numberZeroValues")  public ArrayList<Number> getNumberZeroValues() { return this.numberZeroValues; }
    /** @see #numberZeroValues */ @JsonProperty("numberZeroValues")  public void setNumberZeroValues(ArrayList<Number> numberZeroValues) { this.numberZeroValues = numberZeroValues; }

    /** @see #inferredDataType */ @JsonProperty("inferredDataType")  public ArrayList<String> getInferredDataType() { return this.inferredDataType; }
    /** @see #inferredDataType */ @JsonProperty("inferredDataType")  public void setInferredDataType(ArrayList<String> inferredDataType) { this.inferredDataType = inferredDataType; }

    /** @see #inferredLength */ @JsonProperty("inferredLength")  public ArrayList<Number> getInferredLength() { return this.inferredLength; }
    /** @see #inferredLength */ @JsonProperty("inferredLength")  public void setInferredLength(ArrayList<Number> inferredLength) { this.inferredLength = inferredLength; }

    /** @see #inferredFormat */ @JsonProperty("inferredFormat")  public ArrayList<String> getInferredFormat() { return this.inferredFormat; }
    /** @see #inferredFormat */ @JsonProperty("inferredFormat")  public void setInferredFormat(ArrayList<String> inferredFormat) { this.inferredFormat = inferredFormat; }

    /** @see #inferredScale */ @JsonProperty("inferredScale")  public ArrayList<Number> getInferredScale() { return this.inferredScale; }
    /** @see #inferredScale */ @JsonProperty("inferredScale")  public void setInferredScale(ArrayList<Number> inferredScale) { this.inferredScale = inferredScale; }

    /** @see #inferredPrecision */ @JsonProperty("inferredPrecision")  public ArrayList<Number> getInferredPrecision() { return this.inferredPrecision; }
    /** @see #inferredPrecision */ @JsonProperty("inferredPrecision")  public void setInferredPrecision(ArrayList<Number> inferredPrecision) { this.inferredPrecision = inferredPrecision; }

    /** @see #averageValue */ @JsonProperty("averageValue")  public ArrayList<String> getAverageValue() { return this.averageValue; }
    /** @see #averageValue */ @JsonProperty("averageValue")  public void setAverageValue(ArrayList<String> averageValue) { this.averageValue = averageValue; }

    /** @see #isInferredForeignKey */ @JsonProperty("isInferredForeignKey")  public Boolean getIsInferredForeignKey() { return this.isInferredForeignKey; }
    /** @see #isInferredForeignKey */ @JsonProperty("isInferredForeignKey")  public void setIsInferredForeignKey(Boolean isInferredForeignKey) { this.isInferredForeignKey = isInferredForeignKey; }

    /** @see #isInferredPrimaryKey */ @JsonProperty("isInferredPrimaryKey")  public Boolean getIsInferredPrimaryKey() { return this.isInferredPrimaryKey; }
    /** @see #isInferredPrimaryKey */ @JsonProperty("isInferredPrimaryKey")  public void setIsInferredPrimaryKey(Boolean isInferredPrimaryKey) { this.isInferredPrimaryKey = isInferredPrimaryKey; }

    /** @see #hasDataClassification */ @JsonProperty("hasDataClassification")  public ReferenceList getHasDataClassification() { return this.hasDataClassification; }
    /** @see #hasDataClassification */ @JsonProperty("hasDataClassification")  public void setHasDataClassification(ReferenceList hasDataClassification) { this.hasDataClassification = hasDataClassification; }

    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public ArrayList<Number> getNbRecordsTested() { return this.nbRecordsTested; }
    /** @see #nbRecordsTested */ @JsonProperty("nbRecordsTested")  public void setNbRecordsTested(ArrayList<Number> nbRecordsTested) { this.nbRecordsTested = nbRecordsTested; }

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

    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public ReferenceList getSuggestedTermAssignments() { return this.suggested_term_assignments; }
    /** @see #suggested_term_assignments */ @JsonProperty("suggested_term_assignments")  public void setSuggestedTermAssignments(ReferenceList suggested_term_assignments) { this.suggested_term_assignments = suggested_term_assignments; }

    /** @see #datafile_data_rules */ @JsonProperty("datafile_data_rules")  public ReferenceList getDatafileDataRules() { return this.datafile_data_rules; }
    /** @see #datafile_data_rules */ @JsonProperty("datafile_data_rules")  public void setDatafileDataRules(ReferenceList datafile_data_rules) { this.datafile_data_rules = datafile_data_rules; }

    /** @see #database_data_rule_sets */ @JsonProperty("database_data_rule_sets")  public ReferenceList getDatabaseDataRuleSets() { return this.database_data_rule_sets; }
    /** @see #database_data_rule_sets */ @JsonProperty("database_data_rule_sets")  public void setDatabaseDataRuleSets(ReferenceList database_data_rule_sets) { this.database_data_rule_sets = database_data_rule_sets; }

    /** @see #in_collections */ @JsonProperty("in_collections")  public ReferenceList getInCollections() { return this.in_collections; }
    /** @see #in_collections */ @JsonProperty("in_collections")  public void setInCollections(ReferenceList in_collections) { this.in_collections = in_collections; }


    public static final Boolean isDataFileField(Object obj) { return (obj.getClass() == DataFileField.class); }

}

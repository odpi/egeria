/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ColumnDetails {

    private String guid;
    private String qualifiedName;
    private String attributeName;
    private Integer position;
    private String cardinality;
    private String defaultValueOverride;
    private String type;
    private String qualifiedNameColumnType;
    private BusinessTerm businessTerm;
    private ForeignKey foreignKey;
    private String primaryKeyName;
    private boolean isUnique;
    private boolean isPrimaryKey;
    private boolean isNullable;


    /**
     * Return the foreignKeyName of the column
     *
     * @return foreignKeyName of the column
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * set up the foreignKeyName of the column
     *
     * @param attributeName - foreignKeyName of the column
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * Return the position of the column
     *
     * @return position of the column
     */
    public Integer getPosition() {
        return position;
    }

    /**
     * set up the position of the column
     *
     * @param position - position of the column
     */
    public void setPosition(Integer position) {
        this.position = position;
    }

    /**
     * Return the cardinality of the column
     *
     * @return cardinality of the column
     */
    public String getCardinality() {
        return cardinality;
    }

    /**
     * set up the cardinality of the column
     *
     * @param cardinality - cardinality of the column
     */
    public void setCardinality(String cardinality) {
        this.cardinality = cardinality;
    }

    /**
     * Return the default value of the column
     *
     * @return default value of the column
     */
    public String getDefaultValueOverride() {
        return defaultValueOverride;
    }

    /**
     * set up the default value of the column
     *
     * @param defaultValueOverride - default value of the column
     */
    public void setDefaultValueOverride(String defaultValueOverride) {
        this.defaultValueOverride = defaultValueOverride;
    }

    /**
     * Return the business term associated to the column
     *
     * @return business term of the column
     */
    public BusinessTerm getBusinessTerm() {
        return businessTerm;
    }

    /**
     * set up the business term associated with the column
     *
     * @param businessTerm - business term linked to the column
     */
    public void setBusinessTerm(BusinessTerm businessTerm) {
        this.businessTerm = businessTerm;
    }

    /**
     * Return the guid of the column
     *
     * @return guid of the column
     */
    public String getGuid() {
        return guid;
    }

    /**
     * set up the giud of the column
     *
     * @param guid - guid of the column
     */
    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getType() {
        return type;
    }

    /**
     * set up the type of the column
     *
     * @param type - type of the column
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Return the qualified foreignKeyName of the column
     *
     * @return qualified foreignKeyName of the column
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * set up the qualified foreignKeyName of the column
     *
     * @param qualifiedName - qualified foreignKeyName of the column
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    /**
     * Return the qualified foreignKeyName of the column type
     *
     * @return qualified foreignKeyName of the column type
     */
    public String getQualifiedNameColumnType() {
        return qualifiedNameColumnType;
    }

    /**
     * set up the qualified foreignKeyName of the column type
     *
     * @param qualifiedNameColumnType - qualified foreignKeyName of the column type
     */
    public void setQualifiedNameColumnType(String qualifiedNameColumnType) {
        this.qualifiedNameColumnType = qualifiedNameColumnType;
    }


    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public ForeignKey getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(ForeignKey foreignKey) {
        this.foreignKey = foreignKey;
    }

    @JsonProperty("isUnique")
    public boolean isUnique() {
        return isUnique;
    }

    public void setUnique(boolean unique) {
        isUnique = unique;
    }

    @JsonProperty("isPrimaryKey")
    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    @JsonProperty("isNullable")
    public boolean isNullable() {
        return isNullable;
    }

    public void setNullable(boolean nullable) {
        isNullable = nullable;
    }

    @Override
    public String toString() {
        return "ColumnDetails{" +
                "guid='" + guid + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", attributeName='" + attributeName + '\'' +
                ", position=" + position +
                ", cardinality='" + cardinality + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", type='" + type + '\'' +
                ", qualifiedNameColumnType='" + qualifiedNameColumnType + '\'' +
                ", businessTerm=" + businessTerm +
                '}';
    }
}

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.dataplatform.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Deprecated
public class TableColumn {

    private String guid;
    private String qualifiedName;
    private String name;
    private Integer position;
    private String cardinality;
    private String defaultValueOverride;
    private String type;
    private String primaryKeyName;
    private List<BusinessTerm> businessTerms;
    private ForeignKey referencedColumn;
    private boolean isUnique;
    private boolean isPrimaryKey;
    private boolean isNullable;


    /**
     * Return the name of the column
     *
     * @return name of the column
     */
    public String getName() {
        return name;
    }

    /**
     * set up the name of the column
     *
     * @param name - name of the column
     */
    public void setName(String name) {
        this.name = name;
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
     * Gets business term.
     *
     * @return the business term
     */
    public List<BusinessTerm> getBusinessTerms() {
        return businessTerms;
    }

    /**
     * Sets business term.
     *
     * @param businessTerms the business term
     */
    public void setBusinessTerms(List<BusinessTerm> businessTerms) {
        this.businessTerms = businessTerms;
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
     * Return the qualified name of the column
     *
     * @return qualified name of the column
     */
    public String getQualifiedName() {
        return qualifiedName;
    }

    /**
     * set up the qualified name of the column
     *
     * @param qualifiedName - qualified name of the column
     */
    public void setQualifiedName(String qualifiedName) {
        this.qualifiedName = qualifiedName;
    }

    public String getPrimaryKeyName() {
        return primaryKeyName;
    }

    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }

    public ForeignKey getReferencedColumn() {
        return referencedColumn;
    }

    public void setReferencedColumn(ForeignKey referencedColumn) {
        this.referencedColumn = referencedColumn;
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
        return "{" +
                "guid='" + guid + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", name='" + name + '\'' +
                ", position=" + position +
                ", cardinality='" + cardinality + '\'' +
                ", defaultValueOverride='" + defaultValueOverride + '\'' +
                ", type='" + type + '\'' +
                ", primaryKeyName='" + primaryKeyName + '\'' +
                ", businessTerms=" + businessTerms +
                ", referencedColumn=" + referencedColumn +
                ", isUnique=" + isUnique +
                ", isPrimaryKey=" + isPrimaryKey +
                ", isNullable=" + isNullable +
                '}';
    }
}

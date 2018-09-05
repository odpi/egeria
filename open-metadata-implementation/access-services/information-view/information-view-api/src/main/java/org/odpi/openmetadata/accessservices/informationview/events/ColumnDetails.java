/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.informationview.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.ALWAYS)
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

    /**
     * Return the name of the column
     *
     * @return name of the column
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * set up the name of the column
     *
     * @param attributeName - name of the column
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

    /**
     * Return the qualified name of the column type
     *
     * @return qualified name of the column type
     */
    public String getQualifiedNameColumnType() {
        return qualifiedNameColumnType;
    }

    /**
     * set up the qualified name of the column type
     *
     * @param qualifiedNameColumnType - qualified name of the column type
     */
    public void setQualifiedNameColumnType(String qualifiedNameColumnType) {
        this.qualifiedNameColumnType = qualifiedNameColumnType;
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

/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SoftwareServerCapability extends ReferenceableProperties
{

    private static final long serialVersionUID = 1L;

    private String displayName;
    private String description;
    private String dataPlatformType;
    private String dataPlatformVersion;
    private String patchLevel;
    private String source;

    public SoftwareServerCapability()
    {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDataPlatformType() {
        return dataPlatformType;
    }

    public void setDataPlatformType(String dataPlatformType) {
        this.dataPlatformType = dataPlatformType;
    }

    public String getDataPlatformVersion() {
        return dataPlatformVersion;
    }

    public void setDataPlatformVersion(String dataPlatformVersion) {
        this.dataPlatformVersion = dataPlatformVersion;
    }

    public String getPatchLevel() {
        return patchLevel;
    }

    public void setPatchLevel(String patchLevel) {
        this.patchLevel = patchLevel;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MeaningProperties{" +
                "displayName='" + displayName + '\'' +
                ", summary='" + summary + '\'' +
                ", description='" + description + '\'' +
                ", examples='" + examples + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", usage='" + usage + '\'' +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", additionalProperties=" + getAdditionalProperties() +
                ", meanings=" + getMeanings() +
                ", type=" + getTypeName() +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MeaningProperties that = (MeaningProperties) objectToCompare;
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getSummary(), that.getSummary()) &&
                Objects.equals(getDescription(), that.getDescription()) &&
                Objects.equals(getExamples(), that.getExamples()) &&
                Objects.equals(getAbbreviation(), that.getAbbreviation()) &&
                Objects.equals(getUsage(), that.getUsage());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getDisplayName(), getSummary(), getDescription(), getExamples(),
                            getAbbreviation(), getUsage());
    }
}

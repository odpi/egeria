/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DataValueSpecificationProperties is used to provide the characterizations of the data values stored in a data field
 * described by the attached element.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DataClassProperties.class, name = "DataClassProperties"),
                @JsonSubTypes.Type(value = DataGrainProperties.class, name = "DataGrainProperties"),
        })
public class DataValueSpecificationProperties extends AuthoredReferenceableProperties
{
    private List<String>        matchPropertyNames   = null;
    private String              namespacePath        = null;
    private Integer             matchThreshold       = null;
    private String              specification        = null;
    private Map<String, String> specificationDetails = null;
    private String              dataType             = null;
    private String              units                = null;
    private Long                absoluteUncertainty  = null;
    private Long                relativeUncertainty  = null;


    /**
     * Default constructor
     */
    public DataValueSpecificationProperties()
    {
        super();
        super.typeName = OpenMetadataType.DATA_VALUE_SPECIFICATION.typeName;
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public DataValueSpecificationProperties(DataValueSpecificationProperties template)
    {
        super(template);

        if (template != null)
        {
            matchPropertyNames   = template.getMatchPropertyNames();
            namespacePath        = template.getNamespacePath();
            specification        = template.getSpecification();
            specificationDetails = template.getSpecificationDetails();
            dataType             = template.getDataType();
            matchThreshold       = template.getMatchThreshold();
            units                = template.getUnits();
            absoluteUncertainty  = template.getAbsoluteUncertainty();
            relativeUncertainty  = template.getRelativeUncertainty();
        }
    }


    /**
     * Return the list of property values filled out in this annotation.
     *
     * @return list of property names
     */
    public List<String> getMatchPropertyNames()
    {
        return matchPropertyNames;
    }


    /**
     * Set up the list of property values filled out in this annotation.
     *
     * @param matchPropertyNames list of property names
     */
    public void setMatchPropertyNames(List<String> matchPropertyNames)
    {
        this.matchPropertyNames = matchPropertyNames;
    }


    /**
     * Return the match threshold that a data field is expected to achieve to be assigned this data class.
     *
     * @return float
     */
    public Integer getMatchThreshold()
    {
        return matchThreshold;
    }


    /**
     * Set up the match threshold that a data field is expected to achieve to be assigned this data class.
     *
     * @param matchThreshold float
     */
    public void setMatchThreshold(Integer matchThreshold)
    {
        this.matchThreshold = matchThreshold;
    }


    /**
     * Return the name of the namespace that this type belongs to.
     *
     * @return string name
     */
    public String getNamespacePath()
    {
        return namespacePath;
    }


    /**
     * Set up the name of the namespace that this type belongs to.
     *
     * @param namespacePath string name
     */
    public void setNamespacePath(String namespacePath)
    {
        this.namespacePath = namespacePath;
    }


    /**
     * Return the parsing string used to identify values of this data class.
     *
     * @return string
     */
    public String getSpecification()
    {
        return specification;
    }


    /**
     *  Set up the parsing string used to identify values of this data class.
     *
     * @param specification string
     */
    public void setSpecification(String specification)
    {
        this.specification = specification;
    }


    /**
     * Return any additional properties used in the specification.
     *
     * @return property map
     */
    public Map<String, String> getSpecificationDetails()
    {
        return specificationDetails;
    }


    /**
     * Set up any additional properties used in the specification.
     *
     * @param specificationDetails property map
     */
    public void setSpecificationDetails(Map<String, String> specificationDetails)
    {
        this.specificationDetails = specificationDetails;
    }



    /**
     * Return the data type for this element.  Null means an unknown data type.
     *
     * @return String data type name
     */
    public String getDataType() { return dataType; }


    /**
     * Set up the data type for this element.  Null means an unknown data type.
     *
     * @param dataType data type name
     */
    public void setDataType(String dataType)
    {
        this.dataType = dataType;
    }


    /**
     * Return the units used to describe the granularity.
     *
     * @return string description
     */
    public String getUnits()
    {
        return units;
    }


    /**
     * Set up the units used to describe the granularity.
     *
     * @param units string description
     */
    public void setUnits(String units)
    {
        this.units = units;
    }


    /**
     * Return the absolute uncertainty.
     *
     * @return long
     */
    public Long getAbsoluteUncertainty()
    {
        return absoluteUncertainty;
    }


    /**
     * Set up the absolute uncertainty.
     *
     * @param absoluteUncertainty long
     */
    public void setAbsoluteUncertainty(Long absoluteUncertainty)
    {
        this.absoluteUncertainty = absoluteUncertainty;
    }


    /**
     * Return the relative uncertainty.
     *
     * @return long
     */
    public Long getRelativeUncertainty()
    {
        return relativeUncertainty;
    }


    /**
     * Set up the relative uncertainty.
     *
     * @param relativeUncertainty long
     */
    public void setRelativeUncertainty(Long relativeUncertainty)
    {
        this.relativeUncertainty = relativeUncertainty;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DataValueSpecificationProperties{" +
                "matchPropertyNames=" + matchPropertyNames +
                ", namespacePath='" + namespacePath + '\'' +
                ", matchThreshold=" + matchThreshold +
                ", specification='" + specification + '\'' +
                ", specificationDetails=" + specificationDetails +
                ", dataType='" + dataType + '\'' +
                ", units='" + units + '\'' +
                ", absoluteUncertainty=" + absoluteUncertainty +
                ", relativeUncertainty=" + relativeUncertainty +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        DataValueSpecificationProperties that = (DataValueSpecificationProperties) objectToCompare;
        return  Objects.equals(absoluteUncertainty, that.absoluteUncertainty) &&
                Objects.equals(relativeUncertainty, that.relativeUncertainty) &&
                Objects.equals(matchPropertyNames, that.matchPropertyNames) &&
                Objects.equals(namespacePath, that.namespacePath) &&
                Objects.equals(matchThreshold, that.matchThreshold) &&
                Objects.equals(specification, that.specification) &&
                Objects.equals(specificationDetails, that.specificationDetails) &&
                Objects.equals(dataType, that.dataType) &&
                Objects.equals(units, that.units);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), matchPropertyNames, namespacePath,
                            matchThreshold, specification, specificationDetails, dataType,
                            units, absoluteUncertainty, relativeUncertainty);
    }
}
